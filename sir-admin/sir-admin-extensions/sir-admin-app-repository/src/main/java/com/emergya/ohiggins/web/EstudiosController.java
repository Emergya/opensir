/*
 * 
 * Copyright (C) 2012
 * 
 * This file is part of Proyecto ohiggins
 * 
 * This software is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License as published by the Free
 * Software Foundation; either version 2 of the License, or (at your option) any
 * later version.
 * 
 * This software is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU General Public License along with
 * this library; if not, write to the Free Software Foundation, Inc., 51
 * Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA
 * 
 * As a special exception, if you link this library with other files to produce
 * an executable, this library does not by itself cause the resulting executable
 * to be covered by the GNU General Public License. This exception does not
 * however invalidate any other reasons why the executable file might be covered
 * by the GNU General Public License.
 * 
 * Authors:: María Arias de Reyna (mailto:delawen@gmail.com)
 */

package com.emergya.ohiggins.web;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.displaytag.tags.TableTagParameters;
import org.displaytag.util.ParamEncoder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.FileCopyUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.support.StringMultipartFileEditor;
import org.springframework.web.servlet.ModelAndView;

import com.emergya.email.sender.PropertiesEmail;
import com.emergya.email.sender.SendEmailUtils;
import com.emergya.ohiggins.cmis.ASortOrder;
import com.emergya.ohiggins.cmis.CMISConnector;
import com.emergya.ohiggins.cmis.PublicacionCMISConnector;
import com.emergya.ohiggins.cmis.bean.ADocumentState;
import com.emergya.ohiggins.cmis.bean.Publicacion;
import com.emergya.ohiggins.dao.OhigginsNivelTerritorialEntityDao;
import com.emergya.ohiggins.dao.OhigginsSectorEntityDao;
import com.emergya.ohiggins.dao.OhigginsUserEntityDao;
import com.emergya.ohiggins.dto.NivelTerritorialDto;
import com.emergya.ohiggins.dto.SectorDto;
import com.emergya.ohiggins.dto.UsuarioDto;
import com.emergya.ohiggins.security.ConstantesPermisos;
import com.emergya.ohiggins.security.OhigginsUserDetails;
import com.emergya.ohiggins.service.NivelTerritorialService;
import com.emergya.ohiggins.service.SectorService;
import com.emergya.ohiggins.service.UserAdminService;
import com.emergya.ohiggins.web.util.Utils;

/**
 * Pages to manage "Estudios"
 * 
 * @author <a href="mailto:delawen@gmail.com">marias</a>
 */
@Controller
@RequestMapping(value = "estudios")
public class EstudiosController extends AbstractController implements
		Serializable {

	private static final long serialVersionUID = 321207830977114324L;
	private static Log LOG = LogFactory.getLog(EstudiosController.class);

	public final static String MODULE = "repositorio";
	public static String SUB_MODULE = "";

	// TODO sacar estos string a algún fichero de internacionalización?
	private static final String i18n_ErrorSolicitud = "Ocurrió un error procesando tu solicitud. Consulta al administrador.";
	private static final String i18n_SolicitudOK = "Se añadió correctamente la solicitud";
	private static final String i18n_haSidoRechazado = " ha sido rechazado por el siguiente motivo: ";
	private static final String i18n_haSidoPublicado = " ha sido publicado a la fecha ";
	private static final String i18n_estaPendiente = " se encuentra actualmente pendiente de validación por parte del administrador.";

	private static final String URL_RECHAZAR = "estudios/rechazar";

	@Autowired
	private PublicacionCMISConnector cmisc = null;

	@Autowired
	private OhigginsUserEntityDao userDao = null;
	@Autowired
	private OhigginsSectorEntityDao sectorDao = null;
	@Autowired
	private OhigginsNivelTerritorialEntityDao nivelTDao = null;
	@Resource
	private UserAdminService userAdminService;
	@Resource
	private NivelTerritorialService nivelTerritorialService;
	@Resource
	private SectorService sectorService;

	private static final int ANYO_INICIAL = 1980;

	private static final String PENDIENTES = "pendientes";
	private static final String SOLICITUD = "solicitud";
	private static final String MIAS = "mias";

	/**
	 * Indica el número de elementos por tabla.
	 */
	private static Integer pageSize = 10;
	/**
	 * Indica la columna por defecto por la que se ordena.
	 */
	private static String defaultOrderName = "estado";
	/**
	 * Indica el orden por defecto (asc/desc).
	 */
	private static ASortOrder defaultOrder = ASortOrder.ASC;

	/**
	 * 
	 * @return el ICMISConnector usado por el controlador.
	 */
	public PublicacionCMISConnector getCmisc() {
		return cmisc;
	}

	/**
	 * Establece el conector CMIS usado por el controlador.
	 * 
	 * @param cmisc
	 *            el conector CMIS.
	 */
	public void setCmisc(PublicacionCMISConnector cmisc) {
		this.cmisc = cmisc;
	}

	/**
	 * Genera la página con la lista de peticiones de publicación de estudios
	 * pendientes de aprobar por el administrador.
	 * 
	 * @param request
	 * @param model
	 * @param webRequest
	 * @return
	 */
	@RequestMapping(value = "/pendientes", method = RequestMethod.GET)
	public ModelAndView getPendientes(HttpServletRequest request, Model model,
			WebRequest webRequest) {

		ModelAndView modelAndView = new ModelAndView("estudios/pendientes");

		// Accede el rol ADMIN, en otro caso pagina error
		decideIfIsUserInRole(ConstantesPermisos.ADMIN);

		String tableId = "lista";

		Long from = getFrom(request, tableId);
		ASortOrder order = getAscDesc(request, tableId);
		String orderCol = getOrder(request, tableId);

		// String username = controlUsuarioAdministrador(webRequest);

		int total = getCmisc().countPendingRequests().intValue();

		List<Publicacion> estudios = getCmisc().getPagedPendingRequests(from, pageSize,
				orderCol, order);

		if (LOG.isDebugEnabled()) {
			LOG.debug("Obtenidos " + estudios.size()
					+ " estudios para la página " + (from / pageSize + 1));
		}

		generateModel(model, modelAndView, from, total, estudios);
		model.addAttribute("module", getActiveModule());
		model.addAttribute("submodule", PENDIENTES);
		rolUser(model);

		return modelAndView;
	}

	/**
	 * 
	 * @param model
	 */
	private void rolUser(Model model) {
		// model.addAttribute("IS_ADMIN",
		// DummyAuthentificationProvider.userHasAuthority(ADMIN));
		model.addAttribute("IS_ADMIN",
				OhigginsUserDetails.userHasAuthority(ConstantesPermisos.ADMIN));
	}

	/**
	 * Genera la página de las peticiones de publicación de estudios de un
	 * usuario no administrador.
	 * 
	 * @param webRequest
	 * @param request
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/mias", method = RequestMethod.GET)
	public ModelAndView getMias(WebRequest webRequest,
			HttpServletRequest request, Model model) {

		// Accede el rol ADMIN, en otro caso pagina error
		// decideIfIsUserInRole(ConstantesPermisos.ADMIN);

		isLogate(webRequest);
		String usuario = getCurrentUsername(webRequest);

		ModelAndView modelAndView = new ModelAndView("estudios/mias");
		String tableId = "lista";

		Long from = getFrom(request, tableId);
		ASortOrder order = getAscDesc(request, tableId);
		String orderCol = getOrder(request, tableId);

		int total = getCmisc().countUserRequests(usuario).intValue();

		List<Publicacion> estudios = getCmisc().getPagedUserRequests(usuario, from,
				pageSize, orderCol, order);

		if (LOG.isDebugEnabled()) {
			LOG.debug("Obtenidos " + estudios.size()
					+ " estudios para la página " + from / pageSize + 1);
		}

		model.addAttribute("module", getActiveModule());
		model.addAttribute("submodule", MIAS);
		generateModel(model, modelAndView, from, total, estudios);
		rolUser(model);

		return modelAndView;
	}

	@RequestMapping(value = "/estado/{estado}/{ident}", method = RequestMethod.GET)
	public ModelAndView getEstados(WebRequest webRequest,
			HttpServletRequest request, Model model,
			HttpServletResponse response, @PathVariable("ident") String ident,
			@PathVariable("estado") String estado) {

		Authentication u = getAuthentication(webRequest);
		ModelAndView modelAndView = new ModelAndView("estudios/infoEstado");
		Publicacion p = getCmisc().getDocument(ident);

		if (u == null || !u.getName().equals(p.getUsuario()))
			return null; // TODO

		copyDefaultModel(model);
		model.addAttribute("nombre", p.getName());
		model.addAttribute("institucion", p.getInstitucion());
		model.addAttribute("solicitud", p.getSolicitudParsed());
		model.addAttribute("comentario", p.getComentario());
		model.addAttribute("aceptar",
				!p.getEstado().equals(ADocumentState.RECHAZADA));
		model.addAttribute("estado", p.getEstado().toString());
		model.addAttribute("identificador", p.getIdentifier());

		String texto = "";

		if (p.getEstado().equals(ADocumentState.ACEPTADA)
				|| p.getEstado().equals(ADocumentState.LEIDA)) {
			texto = i18n_haSidoPublicado + p.getRespuestaParsed() + ".";
		} else if (p.getEstado().equals(ADocumentState.PENDIENTE)) {
			texto = i18n_estaPendiente;
		} else {
			texto = i18n_haSidoRechazado;
		}

		model.addAttribute("texto", texto);
		model.addAttribute("module", getActiveModule());
		model.addAttribute("submodule", MIAS);
		return modelAndView;
	}

	private Authentication getAuthentication(WebRequest webRequest) {
		SecurityContext context = (SecurityContext) webRequest.getAttribute(
				"SPRING_SECURITY_CONTEXT", RequestAttributes.SCOPE_SESSION);

		Authentication u = null;
		if (context != null && context.getAuthentication().isAuthenticated()
				&& !context.getAuthentication().getAuthorities().isEmpty()) {
			u = context.getAuthentication();
		}
		return u;
	}

	@RequestMapping(value = "/editar/{estado}/{ident}", method = {
			RequestMethod.GET, RequestMethod.POST })
	public ModelAndView getEditarPublicacion(WebRequest webRequest,
			HttpServletRequest request, Model model,
			HttpServletResponse response, @PathVariable("ident") String ident,
			@PathVariable("estado") String estado,
			@RequestParam(required = true, value = "action") String action) {

		/*
		 * Authentication u = getAuthentication(webRequest); ModelAndView
		 * modelAndView = null; Publicacion p =
		 * getCmisc().getPublicacion(ident);
		 * 
		 * if (u == null || !u.getName().equals(p.getUsuario())) return null; //
		 * TODO
		 * 
		 * if (estado.equalsIgnoreCase("PENDIENTE")) { modelAndView = new
		 * ModelAndView("estudios/close"); } else if
		 * (estado.equalsIgnoreCase("ACEPTADA") ||
		 * estado.equalsIgnoreCase("LEIDA")) {
		 * p.setEstado(EstadoPublicacion.LEIDA); getCmisc().editar(p);
		 * modelAndView = new ModelAndView("estudios/close"); } else if
		 * (estado.equalsIgnoreCase("RECHAZADA")) { if
		 * (action.equalsIgnoreCase("editar")) { model.addAttribute("uri_",
		 * "../../"); modelAndView = showSolicitud(model, p); } else if
		 * (action.equalsIgnoreCase("eliminar")) { getCmisc().borrar(p);
		 * modelAndView = new ModelAndView("estudios/close"); } } else
		 * modelAndView = new ModelAndView("estudios/mias");
		 * 
		 * copyDefaultModel(model);
		 * 
		 * return modelAndView;
		 */

		Authentication u = getAuthentication(webRequest);
		ModelAndView modelAndView = null;
		Publicacion p = getCmisc().getDocument(ident);
		String usuario = getCurrentUsername(webRequest);

		if (u == null || !u.getName().equals(p.getUsuario())) {
			throw new AccessDeniedException(
					"No tiene permiso para acceder a este servicio");
			// return null; // TODO
		}

		if (estado.equalsIgnoreCase("PENDIENTE")) {
			modelAndView = new ModelAndView("redirect:/estudios/mias");
		} else if (estado.equalsIgnoreCase("ACEPTADA")
				|| estado.equalsIgnoreCase("LEIDA")) {
			int total1 = getCmisc().countUserRequests(usuario).intValue();
			p.setEstado(ADocumentState.LEIDA);
			getCmisc().updateDocument(p);
			modelAndView = new ModelAndView("redirect:/estudios/mias");
			// simula refresco
			simulaRefreshMiasActualizacion(total1, webRequest);
		} else if (estado.equalsIgnoreCase("RECHAZADA")) {
			if (action.equalsIgnoreCase("editar")) {
				model.addAttribute("uri_", "../../");
				modelAndView = showSolicitud(model, p);
			} else if (action.equalsIgnoreCase("eliminar")) {
				int total1 = getCmisc().countUserRequests(usuario).intValue();
				getCmisc().deleteDocument(p);
				modelAndView = new ModelAndView(
						"redirect:/estudios/mias?msg=success");
				// simula refresco
				simulaRefreshMiasActualizacion(total1, webRequest);
			}
		} else
			modelAndView = new ModelAndView("estudios/mias");

		copyDefaultModel(model);
		model.addAttribute("module", getActiveModule());
		model.addAttribute("submodule", MIAS);

		return modelAndView;
	}

	@RequestMapping(value = "/download/{ident}", method = RequestMethod.GET)
	public String getDocumento(WebRequest webRequest,
			HttpServletRequest request, HttpServletResponse response,
			Model model, @PathVariable("ident") String ident) {

		// Authentication u = getAuthentication(webRequest);
		try {
			File file = getCmisc().downloadDocument(ident);
			response.setHeader("Content-Disposition", "attachment; filename=\""
					+ file.getName() + "\"");
			response.setContentType(getCmisc().getMimeType(ident));

			FileCopyUtils.copy(new FileInputStream(file),
					response.getOutputStream());
		} catch (Exception t) {
			LOG.error("Error descargando " + ident, t);
		}

		return null;
	}

	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/verDetalle/{ident}", method = RequestMethod.GET)
	public ModelAndView getVerDetalle(WebRequest webRequest,
			HttpServletRequest request, HttpServletResponse response,
			Model model, @PathVariable("ident") String ident) {

		isLogate(webRequest);
		decideIfIsUserInRole(ConstantesPermisos.ADMIN);

		ModelAndView modelAndView = null;
		modelAndView = new ModelAndView("estudios/verDetalle");
		copyDefaultModel(model);

		Publicacion p = getCmisc().getDocument(ident);

		model.addAttribute("nombre", p.getName());
		model.addAttribute("sector", p.getSector());
		model.addAttribute("anyo", p.getAnyo());
		model.addAttribute("nivelT", p.getNivelTerritorial());
		model.addAttribute("autor", p.getAutor());
		model.addAttribute("resumen", p.getResumen());
		model.addAttribute("identificador", p.getIdentifier());
		model.addAttribute("comentario",
				p.getComentario() != null ? p.getComentario() : new String());

		if (p != null && p.getComentario() == null) {
			p.setComentario(new String());
		}
		model.addAttribute("Publicacion", p);

		// Comboboxes
		Collection<SectorDto> sectores =  this.sectorService.getByName(p.getSector());
		Collection<NivelTerritorialDto> niveles =  nivelTerritorialService
				.getByName(p.getNivelTerritorial());
		List<Integer> anyos = new ArrayList<Integer>();
		anyos.add(p.getAnyo());
		
		

		model.addAttribute("sectores", sectores);
		model.addAttribute("niveles", niveles);
		model.addAttribute("anyos", anyos);
		model.addAttribute("publicacion", p);
		model.addAttribute("module", getActiveModule());
		model.addAttribute("submodule", PENDIENTES);
		rolUser(model);

		return modelAndView;
	}

	@RequestMapping(value = "/solicitud", method = RequestMethod.GET)
	public ModelAndView getSolicitud(WebRequest webRequest,
			HttpServletRequest request, Model model) {

		isLogate(webRequest);// Si no esta logado lanza la excepcion
		rolUser(model);
		// Un administrador no debe acceder a esta página.
		if (controlUsuarioAdministrador(webRequest) == null) {
			throw new AccessDeniedException(
					"No tiene permiso para acceder a este apartado");
		}

		model.addAttribute(MODULE, getActiveModule());
		model.addAttribute("submodule", SOLICITUD);

		return showSolicitud(model, new Publicacion());
	}

	@SuppressWarnings("unchecked")
	private ModelAndView showSolicitud(Model model, Publicacion p) {
		ModelAndView modelAndView = new ModelAndView("estudios/editar");
		// model.addAttribute("publicacion", p);

		// Comboboxes
		// List<SectorEntity> sectores = this.sectorDao.getAll();
		// List<NivelTerritorialEntity> niveles = this.nivelTDao.getAll();
		List<SectorDto> sectores = (List<SectorDto>) this.sectorService
				.getAll();
		List<NivelTerritorialDto> niveles = (List<NivelTerritorialDto>) nivelTerritorialService
				.getZonesOrderByTypeDescNameAsc();
		List<Integer> anyos = new LinkedList<Integer>();

		// Lista de años
		int anyoFinal = Calendar.getInstance().get(Calendar.YEAR);
		for (int i = ANYO_INICIAL; i <= anyoFinal; i++) {
			anyos.add(new Integer(i));
		}

		// Valores iniciales
		if (p.getAnyo() == null) {
			p.setAnyo(new Integer(0));
		}
		if (p.getNivelTerritorial() == null) {
			p.setNivelTerritorial(new String());
		}
		if (p.getSector() == null) {
			p.setSector(new String());
		}

		model.addAttribute("publicacion", p);
		model.addAttribute("anyos", anyos);
		model.addAttribute("sectores", sectores);
		model.addAttribute("niveles", niveles);
		rolUser(model);

		model.addAttribute("module", getActiveModule());
		model.addAttribute("submodule", SOLICITUD);
		return modelAndView;
	}

	@RequestMapping(value = "/upload", method = { RequestMethod.POST })
	public ModelAndView uploadNew(
			HttpServletRequest request,
			WebRequest webRequest,
			HttpServletResponse response,
			Model model,
			@RequestParam("anyo") Integer anyo,
			@RequestParam("sector") String sector,
			@RequestParam("nivelTerritorial") String nivelTerritorial,
			@RequestParam(value = "identificador", required = false) String identificador,
			@RequestParam("name") String nombre,
			@RequestParam("autor") String autor,
			@RequestParam("resumen") String resumen,
			@RequestParam(value = "file", required = false) MultipartFile file) {

		// Accede el rol ADMIN, en otro caso pagina error
		// decideIfIsUserInRole(ConstantesPermisos.ADMIN);

		String message = i18n_SolicitudOK;
		// ModelAndView modelAndView = new ModelAndView("estudios/editar");
		// ModelAndView modelAndView = new
		// ModelAndView("redirect:/estudios/pendientes?msg=success");
		ModelAndView modelAndView = new ModelAndView(
				"redirect:/estudios/mias?msg=success");

		// String username = controlUsuarioAdministrador(webRequest);
		int total1 = getCmisc().countPendingRequests().intValue();

		Publicacion pub = new Publicacion();
		if (identificador != null && !identificador.equals("")) {
			pub = getCmisc().getDocument(identificador);
		}

		pub.setUsuario(getAuthentication(webRequest).getName());
		pub.setAnyo(anyo);
		pub.setSector(sector);
		pub.setNivelTerritorial(nivelTerritorial);
		pub.setNombre(nombre);
		pub.setAutor(autor);
		pub.setInstitucion(this.userDao
				.getUser(getAuthentication(webRequest).getName())
				.getAuthority().getName());
		pub.setResumen(resumen);
		pub.setSolicitud(Calendar.getInstance());
		pub.getSolicitud().setTimeInMillis(System.currentTimeMillis());

		if (identificador != null && !identificador.equals("")) {
			pub.setEstado(ADocumentState.PENDIENTE);
			pub.setComentario("");
			pub.setRespuesta(null);
			getCmisc().updateDocument(pub);
		} else {
			File f = getTemporaryFile(file);

			if (f == null || getCmisc().createDocument(pub, f) == null)
				message = i18n_ErrorSolicitud;
			if (f != null)
				f.delete();
		}

		populate(model, new Publicacion());
		modelAndView.addObject("message", message);

		simulaRefreshPendientesAlta(total1, webRequest);

		enviarEmailAdministradores(pub, webRequest, request);
		return modelAndView;
		// return getPendientes(request, model, webRequest);
	}

	/**
	 * Arreglo problema del refresco de alfresco y la aplicacion De forma
	 * temporal
	 * 
	 * @param total1
	 *            Total de estudios pendientes antes de actualizar
	 */
	private void simulaRefreshPendientesAlta(int total1, WebRequest webRequest) {
		try {
			// String username = controlUsuarioAdministrador(webRequest);
			// total2 despues de insertar el estudio
			int maxTries = 20;
			int total2 = getCmisc().countPendingRequests().intValue();
			while (total2 <= total1 && maxTries-- > 0) {
				total2 = getCmisc().countPendingRequests().intValue();
				if (maxTries == 0 && LOG.isInfoEnabled()) {
					LOG.info("Espera activa refresco lista pendientes tras alta finalizada tras el número máximo de intentos");
				}
				Thread.sleep(500);
			}
		} catch (Exception e) {
			// Wait for Alfresco
		}
	}

	/**
	 * Arreglo problema del refresco de alfresco y la aplicacion De forma
	 * temporal
	 * 
	 * @param total1
	 *            Total de estudios pendientes antes de actualizar
	 */
	private void simulaRefreshPendientesActualizacion(int total1,
			WebRequest webRequest) {
		try {
			// String username = controlUsuarioAdministrador(webRequest);
			// total2 despues de insertar el estudio
			int total2 = getCmisc().countPendingRequests().intValue();
			int maxTries = 20;
			while (total2 == total1 && maxTries-- > 0) {
				total2 = getCmisc().countPendingRequests().intValue();
				if (maxTries == 0 && LOG.isInfoEnabled()) {
					LOG.info("Espera activa refresco lista estudios pendiente finalizada tras el número máximo de intentos");
				}
				Thread.sleep(500);
			} 
		} catch (Exception e) {
		}
	}

	/**
	 * Arreglo problema del refresco de alfresco y la aplicacion De forma
	 * temporal
	 * 
	 * @param total1
	 *            Total de estudios pendientes antes de actualizar
	 */
	private void simulaRefreshMiasActualizacion(int total1,
			WebRequest webRequest) {
		try {
			int maxTries = 20;
			String username = controlUsuarioAdministrador(webRequest);
			// total2 despues de insertar el estudio
			int total2 = getCmisc().countUserRequests(username).intValue();
			while (total2 == total1 && maxTries-- > 0) {
				total2 = getCmisc().countUserRequests(username).intValue();
				if (maxTries == 0 && LOG.isInfoEnabled()) {
					LOG.info("Espera activa refresco lista solicitudes estudios mías finalizada tras el número máximo de intentos");
				}
				Thread.sleep(500);
			}
		} catch (Exception e) {
		}
	}

	/**
	 * Puebla los combos necesarios para la vista
	 */
	@SuppressWarnings("unchecked")
	private void populate(Model model, Publicacion p) {
		List<SectorDto> sectores = (List<SectorDto>) this.sectorService
				.getAll();
		List<NivelTerritorialDto> niveles = (List<NivelTerritorialDto>) nivelTerritorialService
				.getAll();
		List<Integer> anyos = new LinkedList<Integer>();

		// p = new Publicacion();

		// Lista de años

		int anyoFinal = Calendar.getInstance().get(Calendar.YEAR);
		for (int i = ANYO_INICIAL; i <= anyoFinal; i++) {
			anyos.add(new Integer(i));
		}

		model.addAttribute("publicacion", p);
		model.addAttribute("anyos", anyos);
		model.addAttribute("sectores", sectores);
		model.addAttribute("niveles", niveles);
	}

	/**
	 * Given a {@link MultipartFile}, return a temporary file with the same
	 * content
	 * 
	 * @param file
	 * @return
	 */
	private File getTemporaryFile(MultipartFile file) {
		File f = null;
		FileOutputStream fw = null;
		try {
			f = File.createTempFile("ohg", file.getOriginalFilename());
			f.deleteOnExit();

			fw = new FileOutputStream(f);
			fw.write(file.getBytes());

		} catch (IOException e) {
			f = null;
			LOG.error("Error leyendo el fichero", e);
		} finally {
			if (fw != null)
				try {
					fw.close();
				} catch (IOException e) {
					LOG.error("Error cerrando fileWriter", e);
				}
		}
		return f;
	}

	@InitBinder
	protected void initBinder(HttpServletRequest request,
			ServletRequestDataBinder binder) throws ServletException {
		// to actually be able to convert Multipart instance to a String
		// we have to register a custom editor
		binder.registerCustomEditor(String.class,
				new StringMultipartFileEditor());
		// now Spring knows how to handle multipart object and convert them
	}

	private String getCurrentUsername(WebRequest webRequest) {
		SecurityContext context = (SecurityContext) webRequest.getAttribute(
				"SPRING_SECURITY_CONTEXT", RequestAttributes.SCOPE_SESSION);

		if (context != null && context.getAuthentication().isAuthenticated()
				&& !context.getAuthentication().getAuthorities().isEmpty()) {
			return context.getAuthentication().getName();
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	private void generateModel(Model model, ModelAndView modelAndView,
			Long from, int total, List<Publicacion> estudios) {
		// copyDefaultModel(model);
		modelAndView.addAllObjects((Map<String, ?>) model);
		modelAndView.addObject("list", estudios);
		modelAndView.addObject("size", total);
		modelAndView.addObject("pageSize", pageSize);
		modelAndView.addObject("currentPage", from);
	}

	private String getOrder(HttpServletRequest request, String tableId) {
		// Columna para ordenar:
		String orderCol = request.getParameter((new ParamEncoder(tableId)
				.encodeParameterName(TableTagParameters.PARAMETER_SORT)));
		if (orderCol == null)
			orderCol = defaultOrderName;
		return orderCol;
	}

	private ASortOrder getAscDesc(HttpServletRequest request, String tableId) {
		// Orden de las columnas:
		String tmp = request.getParameter((new ParamEncoder(tableId)
				.encodeParameterName(TableTagParameters.PARAMETER_ORDER)));
		ASortOrder order = defaultOrder;
		if (tmp != null) {
			int orderIdx = (Integer.parseInt(tmp));
			order = ASortOrder.values()[orderIdx-1];
		}
		
		return order;
	}

	private Long getFrom(HttpServletRequest request, String tableId) {
		// Elemento de inicio:
		String tmp1 = request.getParameter((new ParamEncoder(tableId)
				.encodeParameterName(TableTagParameters.PARAMETER_PAGE)));
		if (tmp1 == null)
			tmp1 = "1";
		Long from = (Long.parseLong(tmp1) - 1) * pageSize;
		return from;
	}

	/**
	 * Referencia al enlace a la paginacion de estudios
	 * 
	 * @return 'estudios'
	 */
	@Override
	protected String getDefaultPaginationUrl() {
		return "/estudios/";
	}

	/**
	 * Incluye en el modelo los parametros por defecto de los estudios
	 * 
	 * @param model
	 * @param update
	 *            si actualiza los parametros actualizables
	 */
	protected void copyDefaultModel(boolean update, Model model) {
		// TODO
		calculatePagination(model);
	}

	@Override
	protected String getAllSubTabs() {
		// TODO
		return TabsByModule.GENERAL_SUBTABS;
	}

	@Override
	protected int getSelectedSubTab() {
		// TODO
		return 0;
	}

	@Override
	protected String getActiveModule() {
		return MODULE;
	}

	@Override
	protected String getActiveSubModule() {
		return SUB_MODULE;
	}

	/**
	 * @return the userDao
	 */
	public OhigginsUserEntityDao getUserDao() {
		return userDao;
	}

	/**
	 * @param userDao
	 *            the userDao to set
	 */
	public void setUserDao(OhigginsUserEntityDao userDao) {
		this.userDao = userDao;
	}

	/**
	 * @return the sectorDao
	 */
	public OhigginsSectorEntityDao getSectorDao() {
		return sectorDao;
	}

	/**
	 * @param sectorDao
	 *            the sectorDao to set
	 */
	public void setSectorDao(OhigginsSectorEntityDao sectorDao) {
		this.sectorDao = sectorDao;
	}

	/**
	 * @return the nivelTDao
	 */
	public OhigginsNivelTerritorialEntityDao getNivelTDao() {
		return nivelTDao;
	}

	/**
	 * @param nivelTDao
	 *            the nivelTDao to set
	 */
	public void setNivelTDao(OhigginsNivelTerritorialEntityDao nivelTDao) {
		this.nivelTDao = nivelTDao;
	}

	/**
	 * Rechazar publicacion
	 * 
	 * @param id
	 * @param model
	 * @return
	 */
	// @RequestMapping(value = "/estudios/rechazar/{id}")
	// public String rechazar(@PathVariable long id, Model model) {
	@RequestMapping(value = "/rechazar/{id}", method = RequestMethod.GET)
	public String rechazar(WebRequest webRequest, HttpServletRequest request,
			HttpServletResponse response, Model model,
			@PathVariable("id") String id) {

		// Accede el rol ADMIN, en otro caso pagina error
		decideIfIsUserInRole(ConstantesPermisos.ADMIN);

		Publicacion p = getCmisc().getDocument(id);
		if (p != null && p.getComentario() == null) {
			p.setComentario(new String());
		}

		model.addAttribute("publicacion", p);
		return URL_RECHAZAR;
	}

	/**
	 * Rechaza una publicacion
	 * 
	 * @param Publicacion
	 * @param result
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/rechazarPublicacion", method = RequestMethod.POST)
	public String rechazarPublicacion(@Valid Publicacion publicacion,
			BindingResult result, Model model, HttpServletRequest request,
			WebRequest webRequest) {

		// Accede el rol ADMIN, en otro caso pagina error
		decideIfIsUserInRole(ConstantesPermisos.ADMIN);

		int total1 = getCmisc().countPendingRequests().intValue();
		Publicacion p = getCmisc().getDocument(
				publicacion.getIdentifier());
		if (p != null && p.getIdentifier() != null
				&& !p.getIdentifier().equals("")) {

			p.setEstado(ADocumentState.RECHAZADA);
			p.setComentario(publicacion.getComentario());
			getCmisc().updateDocument(p);

			// Envio Email
			// 1: Obtenemos la plantilla
			String ruta = request
					.getSession()
					.getServletContext()
					.getRealPath(
							Utils.DIRECTORIO_TEMPLATES + Utils.BARRA
									+ Utils.TEMPLATE_ENVIO_PUBLICACION_EMAIL);

			// 2: Sustituir parametros
			String user = getAuthentication(webRequest).getName();
			UsuarioDto us = userAdminService.obtenerUsuarioByUsername(user);
			String nameUser = new String();
			if (us != null && us.getId() != null) {
				nameUser = us.getNombreCompleto();
			}

			Map<String, String> parametrosPlantilla = new LinkedHashMap<String, String>();
			parametrosPlantilla.put("[\\$]NOMBRE", nameUser);
			parametrosPlantilla.put("[\\$]ESTADO",
					ADocumentState.RECHAZADA.toString());
			parametrosPlantilla.put("[\\$]ESTUDIO", p.getName());
			parametrosPlantilla.put("[\\$]INSTITUCION", p.getInstitucion());

			// String mensajeAEnviar = Utils.generaMensajeEnviar(ruta,
			// parametrosPlantilla);
			// 3: Enviar email
			// TODO enviar correo

		}

		simulaRefreshPendientesActualizacion(total1, webRequest);

		return "redirect:/estudios/pendientes?msg=success";
	}

	/**
	 * Edita una publicacion
	 * 
	 * @param webRequest
	 * @param request
	 * @param model
	 * @param response
	 * @param ident
	 * @param action
	 * @return
	 */
	@RequestMapping(value = "/editar/{ident}", method = { RequestMethod.GET,
			RequestMethod.POST })
	public ModelAndView getEditar(WebRequest webRequest,
			HttpServletRequest request, Model model,
			HttpServletResponse response, @PathVariable("ident") String ident) {

		// Authentication u = getAuthentication(webRequest);
		ModelAndView modelAndView = new ModelAndView("estudios/editar");
		Publicacion p = getCmisc().getDocument(ident);

		// if (u == null || !u.getName().equals(p.getUsuario()))
		// return null; // TODO

		copyDefaultModel(model);

		model.addAttribute("nombre", p.getName());
		model.addAttribute("sector", p.getSector());
		model.addAttribute("anyo", p.getAnyo());
		model.addAttribute("nivelT", p.getNivelTerritorial());
		model.addAttribute("autor", p.getAutor());
		model.addAttribute("resumen", p.getResumen());
		model.addAttribute("identificador", p.getIdentifier());
		model.addAttribute("comentario",
				p.getComentario() != null ? p.getComentario() : new String());

		if (p != null && p.getComentario() == null) {
			p.setComentario(new String());
		}

		model.addAttribute("publicacion", p);

		populate(model, p);

		return modelAndView;

	}

	/**
	 * Autorizar una publicacion
	 * 
	 * @param webRequest
	 * @param request
	 * @param response
	 * @param model
	 * @param ident
	 * @return
	 */
	@RequestMapping(value = "/autorizarPublicacion/{ident}", method = RequestMethod.GET)
	public ModelAndView autorizarPublicacion(WebRequest webRequest,
			HttpServletRequest request, HttpServletResponse response,
			Model model, @PathVariable("ident") String ident) {

		// Accede el rol ADMIN, en otro caso pagina error
		decideIfIsUserInRole(ConstantesPermisos.ADMIN);

		// Authentication u = getAuthentication(webRequest);
		int total = getCmisc().countPendingRequests().intValue();
		try {
			Publicacion pub = new Publicacion();
			if (ident != null && !ident.equals("")) {
				pub = getCmisc().getDocument(ident);
			}

			if (ident != null && !ident.equals("")) {
				pub.setEstado(ADocumentState.ACEPTADA);

				getCmisc().updateDocument(pub);
			}

			// populate(model, new Publicacion());

		} catch (Exception t) {
			LOG.error("Error al autorizar " + ident, t);
		}

		// Utils.simulaRetardo();
		simulaRefreshPendientesActualizacion(total, webRequest);

		ModelAndView modelAndView = new ModelAndView(
				"redirect:/estudios/pendientes?msg=success");

		return modelAndView;
	}

	/**
	 * Indica si el usuario conectado es administrador
	 * 
	 * @param username
	 * @return
	 */
	private boolean esAdministrador(String username) {
		boolean esAdministrador = false;
		if (username != null && !"".equals(username)) {
			UsuarioDto userLogado = userAdminService
					.obtenerUsuarioByUsername(username);

			if (userLogado.isAdmin()) {
				esAdministrador = true;
			}
		}
		return esAdministrador;
	}

	/**
	 * Si el usuario es administrador devuelve null en otro caso el nick del
	 * usuario que nos servira para filtrar en las consultas de alfresco
	 * 
	 * @param webRequest
	 * @return
	 */
	private String controlUsuarioAdministrador(WebRequest webRequest) {
		String username = getCurrentUsername(webRequest);
		boolean esAdministrador = esAdministrador(username);
		String user = null;
		if (!esAdministrador) {
			// filtraremos por las de ese usuario
			user = username;
		}

		return user;
	}

	/**
	 * 
	 * Envia el email de la publicacion a la dirección predeterminada de
	 * admininstración.
	 * 
	 * @param pub
	 *            Publicacion
	 * @param webRequest
	 * @return
	 */
	private boolean enviarEmailAdministradores(Publicacion pub,
			WebRequest webRequest, HttpServletRequest request) {
		boolean enviado = true;

		try {

			// 1: Obtener fichero propiedades
			Properties p = Utils.getPropertiesEmailApplication();
			String toAddress = p
					.getProperty(Utils.MAIL_SIR_ADDRESS_PROPERTY_KEY);

			// 2: Properties email
			PropertiesEmail props = new PropertiesEmail();
			props = Utils.inicializaServidorEmail(props, p);// Valores
															// iniciales
			props.setMAIL_ISSUE_VALUE(Utils.ASUNTO_NUEVA_SOLICITUD_PUBLICACION);// Resto
																				// de
																				// Valores

			// 3: Plantilla envio correo
			String rutaPlantilla = request
					.getSession()
					.getServletContext()
					.getRealPath(
							Utils.DIRECTORIO_TEMPLATES
									+ Utils.TEMPLATE_ENVIO_NUEVA_PUBLICACION_EMAIL);

			String username = getCurrentUsername(webRequest);
			UsuarioDto userLogado = userAdminService
					.obtenerUsuarioByUsername(username);

			boolean e = false;
			// 4: Enviar

			// send email
			Map<String, String> parametrosPlantilla = new HashMap<String, String>();
			parametrosPlantilla.put("[\\$]NOMBRE",
					Utils.ADMINISTRADOR_MAIL_NAME);
			parametrosPlantilla.put("[\\$]INSTITUCION", pub.getInstitucion());
			parametrosPlantilla.put("[\\$]ESTUDIO", pub.getName());
			parametrosPlantilla.put("[\\$]USUARIO",
					userLogado != null ? userLogado.getNombreCompleto()
							: new String());
			String message = Utils.generaMensajeEnviar(rutaPlantilla,
					parametrosPlantilla);

			// Sender email user single
			e = SendEmailUtils.sendEmailUser(props, message,
					Utils.ADMINISTRADOR_MAIL_NAME, toAddress);

			enviado = e;

		} catch (Exception e) {
			LOG.info("Error al carga el fichero de propiedades:");
			enviado = false;
		}

		return enviado;
	}

}
