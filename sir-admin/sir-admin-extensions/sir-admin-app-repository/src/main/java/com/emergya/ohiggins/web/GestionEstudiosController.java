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
 * Authors:: Jose Alfonso Riera (mailto:jariera@emergya.com)
 */
package com.emergya.ohiggins.web;

import java.io.File;
import java.io.FileInputStream;
import java.io.Serializable;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.displaytag.tags.TableTagParameters;
import org.displaytag.util.ParamEncoder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.FileCopyUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.ModelAndView;

import com.emergya.ohiggins.cmis.ASortOrder;
import com.emergya.ohiggins.cmis.CMISConnector;
import com.emergya.ohiggins.cmis.PublicacionCMISConnector;
import com.emergya.ohiggins.cmis.bean.ADocumentState;
import com.emergya.ohiggins.cmis.bean.EstudioFilter;
import com.emergya.ohiggins.cmis.bean.Publicacion;
import com.emergya.ohiggins.dto.AuthorityDto;
import com.emergya.ohiggins.dto.NivelTerritorialDto;
import com.emergya.ohiggins.dto.SectorDto;
import com.emergya.ohiggins.security.ConstantesPermisos;
import com.emergya.ohiggins.security.OhigginsUserDetails;
import com.emergya.ohiggins.service.InstitucionService;
import com.emergya.ohiggins.service.NivelTerritorialService;
import com.emergya.ohiggins.service.SectorService;
import com.google.common.base.Function;
import com.google.common.collect.Lists;

/**
 * Pages to manage "Gestion Estudios"
 * 
 * @author <a href="mailto:jariera@emergya.com">jariea</a>
 */
@Controller
@RequestMapping(value = "gestionestudios")
public class GestionEstudiosController extends AbstractController implements
		Serializable {

	/** Serials */
	private static final long serialVersionUID = -3875472517254226098L;

	/** Logs */
	private static Log LOG = LogFactory.getLog(EstudiosController.class);

	/**
	 * Indica el número de elementos por tabla
	 */
	private static Integer pageSize = 10;
	/**
	 * Indica la columna por defecto por la que se ordena
	 */
	private static String defaultOrderName = "estado";
	/**
	 * Indica el orden por defecto (asc/desc)
	 */
	private static Integer defaultOrder = 1;

	/** Modulo */
	public final static String MODULE = "repositorio";

	/** Submodulo */
	public static String SUB_MODULE = "submodule";

	private static final String GESTION_ESTUDIOS = "gestion";

	private static final int ANYO_INICIAL = 1980;

	@Autowired
	private PublicacionCMISConnector cmisc = null;

	@Resource
	private NivelTerritorialService nivelTerritorialService;
	@Resource
	private SectorService sectorService;
	@Resource
	private InstitucionService institucionService;

	public PublicacionCMISConnector getCmisc() {
		return cmisc;
	}

	/**
	 * Muestra el buscador de publicaciones
	 * 
	 * @param request
	 * @param model
	 * @param webRequest
	 * @return
	 */
	@RequestMapping(value = "/buscador", method = RequestMethod.GET)
	public ModelAndView buscador(HttpServletRequest request, Model model,
			WebRequest webRequest) {

		ModelAndView modelAndView = new ModelAndView("gestionestudios/buscador");

		// Accede el rol ADMIN, en otro caso pagina error
		decideIfIsUserInRole(ConstantesPermisos.ADMIN);

		EstudioFilter filter = new EstudioFilter();
		model.addAttribute("estudioFilter", filter);

		populate(model);
		model.addAttribute("module", getActiveModule());
		model.addAttribute("submodule", GESTION_ESTUDIOS);

		return modelAndView;
	}

	/**
	 * Carga los combos necesarios para la vista
	 * 
	 * @param request
	 * @param model
	 */
	@SuppressWarnings("unchecked")
	private void populate(Model model) {

		// Sectores
		List<SectorDto> sectores = (List<SectorDto>) this.sectorService
				.getAll();

		// Nivel territorial
		List<NivelTerritorialDto> niveles = (List<NivelTerritorialDto>) nivelTerritorialService
				.getZonesOrderByTypeDescNameAsc();

		// Años
		List<Integer> anyos = new LinkedList<Integer>();

		// Instituciones
		List<AuthorityDto> instituciones = institucionService
				.getAllOrderedByName();

		int anyoFinal = Calendar.getInstance().get(Calendar.YEAR);
		for (int i = ANYO_INICIAL; i <= anyoFinal; i++) {
			anyos.add(new Integer(i));
		}

		model.addAttribute("sectores", sectores);
		model.addAttribute("niveles", niveles);
		model.addAttribute("anyos", anyos);
		model.addAttribute("instituciones", instituciones);

		rolUser(model);

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
	 * Busca estudios
	 * 
	 * @param EstudioFilter
	 * @param result
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/buscar")
	public ModelAndView buscar(EstudioFilter estudioFilter,
			BindingResult result, Model model, WebRequest webRequest,
			HttpServletRequest request,
			@RequestParam(defaultValue = "10") int size,
			@RequestParam(defaultValue = "1") int page) {

		ModelAndView modelAndView = new ModelAndView("gestionestudios/estudios");

		// Accede el rol ADMIN, en otro caso pagina error
		decideIfIsUserInRole(ConstantesPermisos.ADMIN);

		if (LOG.isInfoEnabled()) {
			LOG.info("Consultando las gestion de estudios del sistema. Tamaño de página="
					+ size + ". Pagina=" + page);
		}

		estudioFilter = controlEliminar(estudioFilter);

		String tableId = "lista";

		Long from = getFrom(request, tableId);
		ASortOrder order = getAscDesc(request, tableId);
		String orderCol = getOrder(request, tableId);

		ADocumentState[] estados = new ADocumentState[] {
				ADocumentState.ACEPTADA, ADocumentState.LEIDA,
				ADocumentState.RECHAZADA };

		String nivelTerritorial = estudioFilter.getNivelTerritorial();
		if (StringUtils.isNotBlank(nivelTerritorial)) {
			List<NivelTerritorialDto> niveles = nivelTerritorialService
					.findNivelAndChildren(nivelTerritorial);
			List<String> nivelesTerritoriales = Lists.transform(niveles,
					new Function<NivelTerritorialDto, String>() {
						public String apply(NivelTerritorialDto input) {
							String name = null;
							if (input != null) {
								name = input.getName();
							}
							return name;
						};
					});
			estudioFilter.setNivelesTerritoriales(nivelesTerritoriales
					.toArray(new String[] {}));

		}

		int total = getCmisc().countFilteredDocuments(estudioFilter, estados)
				.intValue();

		List<Publicacion> estudios = getCmisc().getFilteredDocuments(
				estudioFilter, from, pageSize, orderCol, order, estados);

		generateModel(model, modelAndView, from, total, estudios);

		rolUser(model);
		populate(model);
		model.addAttribute("module", getActiveModule());
		model.addAttribute("submodule", GESTION_ESTUDIOS);

		// return "gestionestudios/estudios";
		return modelAndView;

	}

	/**
	 * Control para eliminar estudio desde el listado
	 * 
	 * @param estudioFilter
	 * @return
	 */
	private EstudioFilter controlEliminar(EstudioFilter estudioFilter) {
		// Control eliminar estudio si procede
		if (estudioFilter.getEliminar() != null
				&& !"".equals(estudioFilter.getEliminar())
				&& "true".equals(estudioFilter.getEliminar())
				&& estudioFilter.getIdeliminar() != null
				&& !"".equals(estudioFilter.getIdeliminar())) {

			String idEliminar = estudioFilter.getIdeliminar();

			Publicacion p = getCmisc().getDocument(idEliminar);
			if (p != null && p.getIdentifier() != null
					&& !"".equals(p.getIdentifier())) {
				getCmisc().deleteDocument(p);
			}

			estudioFilter.setEliminar("");
			estudioFilter.setIdeliminar("");
		}
		// Fin Eliminar
		return estudioFilter;
	}

	/**
	 * Download a document
	 * 
	 * @param webRequest
	 * @param request
	 * @param response
	 * @param model
	 * @param ident
	 * @return
	 */
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

	/**
	 * Ver el detalle de la gestion de un estudio
	 * 
	 * @param webRequest
	 * @param request
	 * @param response
	 * @param model
	 * @param ident
	 * @return
	 */
	@RequestMapping(value = "/verDetalle/{ident}", method = RequestMethod.GET)
	public ModelAndView getVerDetalle(WebRequest webRequest,
			HttpServletRequest request, HttpServletResponse response,
			Model model, @PathVariable("ident") String ident) {

		ModelAndView modelAndView = null;
		modelAndView = new ModelAndView("gestionestudios/verDetalle");

		// Accede el rol ADMIN, en otro caso pagina error
		decideIfIsUserInRole(ConstantesPermisos.ADMIN);

		copyDefaultModel(model);

		Publicacion p = getCmisc().getDocument(ident);

		model.addAttribute("nombre", p.getName());
		model.addAttribute("sector", p.getSector());
		model.addAttribute("institucion", p.getInstitucion());
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

		rolUser(model);
		model.addAttribute("modulo", getActiveModule());
		model.addAttribute("submodule", GESTION_ESTUDIOS);
		return modelAndView;
	}

	/**
	 * Elimina un estudio
	 * 
	 * @param webRequest
	 * @param request
	 * @param response
	 * @param model
	 * @param ident
	 * @return
	 */
	@RequestMapping(value = "/eliminar/{ident}", method = RequestMethod.GET)
	public String eliminar(WebRequest webRequest, HttpServletRequest request,
			HttpServletResponse response, Model model,
			@PathVariable("ident") String ident) {

		// Accede el rol ADMIN, en otro caso pagina error
		decideIfIsUserInRole(ConstantesPermisos.ADMIN);

		copyDefaultModel(model);

		Publicacion p = getCmisc().getDocument(ident);
		if (p != null && p.getIdentifier() != null
				&& !"".equals(p.getIdentifier())) {
			getCmisc().deleteDocument(p);

			/*
			 * String tableId = "list";
			 * 
			 * Long from = getFrom(request, tableId); Integer order =
			 * getAscDesc(request, tableId); String orderCol = getOrder(request,
			 * tableId);
			 * 
			 * EstudioFilter estudioFilter = new EstudioFilter(); int total =
			 * getCmisc().getGestionEstudios(estudioFilter).intValue();
			 * 
			 * List<Publicacion> estudios =
			 * getCmisc().getGestionEstudios(estudioFilter, from * pageSize,
			 * pageSize, orderCol, order);
			 * 
			 * 
			 * 
			 * 
			 * generateModel(model, modelAndView, from, total, estudios);
			 */
		}

		// Comboboxes
		populate(model);
		rolUser(model);
		model.addAttribute("modulo", getActiveModule());
		model.addAttribute("submodule", GESTION_ESTUDIOS);

		return "redirect:/gestionestudios/buscar?msg=success";
	}

	/**
	 * Autentication
	 * 
	 * @param webRequest
	 * @return
	 */
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

	/**
	 * Ordenacion
	 * 
	 * @param request
	 * @param tableId
	 * @return
	 */
	private String getOrder(HttpServletRequest request, String tableId) {
		// Columna para ordenar:
		String orderCol = request.getParameter((new ParamEncoder(tableId)
				.encodeParameterName(TableTagParameters.PARAMETER_SORT)));
		if (orderCol == null)
			orderCol = defaultOrderName;
		return orderCol;
	}

	/**
	 * 
	 * @param request
	 * @param tableId
	 * @return
	 */
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
	 * Ordern ascendente o descendente
	 * 
	 * @param request
	 * @param tableId
	 * @return
	 */
	private ASortOrder getAscDesc(HttpServletRequest request, String tableId) {
		// Orden de las columnas:
		String tmp = request.getParameter((new ParamEncoder(tableId)
				.encodeParameterName(TableTagParameters.PARAMETER_ORDER)));
		ASortOrder order = ASortOrder.ASC;
		if (tmp != null) {
			int orderIdx = (Integer.parseInt(tmp));
			order = ASortOrder.values()[orderIdx-1];
		}
		
		return order;
	}

	@Override
	protected void copyDefaultModel(boolean update, Model model) {
		calculatePagination(model);

	}

	@Override
	protected String getAllSubTabs() {
		return TabsByModule.GENERAL_SUBTABS;
	}

	@Override
	protected int getSelectedSubTab() {
		return 0;
	}

	@Override
	protected String getDefaultPaginationUrl() {
		return "/gestionestudios/";
	}

	@Override
	protected String getActiveModule() {
		return MODULE;
	}

	@Override
	protected String getActiveSubModule() {
		return SUB_MODULE;
	}

}
