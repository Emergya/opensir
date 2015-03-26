/*
 * SolicitudesPublicacionCapasController
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

import com.emergya.ohiggins.dto.AuthorityDto;
import java.text.DateFormat;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.ModelAndView;

import com.emergya.ohiggins.dto.EstadoPublicacionLayerRequestPublish;
import com.emergya.ohiggins.dto.LayerDto;
import com.emergya.ohiggins.dto.LayerMetadataDto;
import com.emergya.ohiggins.dto.LayerPublishRequestDto;
import com.emergya.ohiggins.dto.LayerTypeDto;
import com.emergya.ohiggins.dto.UsuarioDto;
import com.emergya.ohiggins.security.ConstantesPermisos;
import com.emergya.ohiggins.security.OhigginsUserDetails;
import com.emergya.ohiggins.service.InstitucionService;
import com.emergya.ohiggins.service.LayerPublishRequestService;
import com.emergya.ohiggins.service.LayerService;
import com.emergya.ohiggins.service.LayerTypeService;
import com.emergya.ohiggins.service.UserAdminService;
import static com.emergya.ohiggins.web.AbstractController.MODULE_KEY;
import com.emergya.ohiggins.web.helper.PublicationRequestHelper;
import java.io.IOException;
import javax.validation.Valid;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Controller handling creation of layer publication requests.
 *
 * @author jariera
 *
 */
@Controller
public class LayerPublicationRequestController extends AbstractController {

    /**
     *
     */
    private static final long serialVersionUID = 5981464002218395351L;
    /**
     * Log
     */
    private static Log LOG = LogFactory
	    .getLog(LayerPublicationRequestController.class);
    /**
     * Listado de solicitudes de publicacion de capas del usuario
     */
    private List<LayerPublishRequestDto> layerPublish = null;
    public final static String MODULE = "cartografico";
    public final static String SUB_MODULE = "solicitudesPublicacion";
    private static final String TYPELAYERS_KEY = "typeLayers";
    // Resources Services
    @Resource
    private LayerService layerService;
    @Resource
    private LayerTypeService layerTypeService;
    @Resource
    private UserAdminService userAdminService;
    @Resource
    private LayerPublishRequestService layerPublishRequestService;
    @Resource
    private InstitucionService authorityService;
    @Resource
    private PublicationRequestHelper publicationRequestHelper;
    
    
    private static final String STATUS_WINDOW_TITLE_PENDING = "Pendiende de publicación";
    private static final String STATUS_WINDOW_TITLE_ACCEPTED = "Solicitud aceptada";
    private static final String STATUS_WINDOW_TITLE_REJECTED = "Solicitud rechazada";
    // Formateo de fechas
    private static final String FORMATO_FECHA = "dd/MM/yyyy";
    private DateFormat format = DateFormat.getDateTimeInstance();

    /**
     * Solicitudes publicacion capas del usuario
     *
     * @param model
     *
     * @return "layersAuthority"
     */
    @RequestMapping(value = "/cartografico/solicitudesPublicacion", method = RequestMethod.GET)
    @SuppressWarnings({"rawtypes"})
    public String solicitudesPublicacion(
	    @RequestParam(defaultValue = "10") int size,
	    @RequestParam(defaultValue = "1") int page, Model model,
	    WebRequest webRequest) {

	isLogate(webRequest);

	if (LOG.isInfoEnabled()) {
	    LOG.info("Consultando las solicitudes publicacion capas del usuario. Tamaño de página="
		    + size + ". Pagina=" + page);
	}

	String username = getCurrentUsername(webRequest);
	UsuarioDto userLogado = userAdminService
		.obtenerUsuarioByUsername(username);


	calculatePagination(model);

	// Lista de solicitudes de capas del usuario
	List result = (List<LayerPublishRequestDto>) layerPublishRequestService
		.getFromToOrderBy(0, Integer.MAX_VALUE,
		LayerPublishRequestDto.NAME_PROPERTY,
		userLogado.getId());

	model.addAttribute("layerPublish", result);

	if (LOG.isInfoEnabled()) {
	    LOG.info("Redirigiendo a la vista cartografico/solicitudesPublicacion");
	}

	rolUser(model);
	model.addAttribute("module", getActiveModule());
	model.addAttribute("submodule", getActiveSubModule());

	return "cartografico/solicitudesPublicacion";
    }

    /**
     * Obtiene el usuario logado
     *
     * @param webRequest
     * @return
     */
    private String getCurrentUsername(WebRequest webRequest) {
	SecurityContext context = (SecurityContext) webRequest.getAttribute(
		"SPRING_SECURITY_CONTEXT", RequestAttributes.SCOPE_SESSION);

	if (context != null && context.getAuthentication().isAuthenticated()
		&& !context.getAuthentication().getAuthorities().isEmpty()) {
	    return context.getAuthentication().getName();
	}
	return null;
    }

    /**
     * Rol del usuario conectado
     *
     * @param model
     */
    private void rolUser(Model model) {
	model.addAttribute("IS_ADMIN",
		OhigginsUserDetails.userHasAuthority(ConstantesPermisos.ADMIN));
    }

    // Implementacion
    @Override
    protected void copyDefaultModel(boolean update, Model model) {
	calculatePagination(model);
	model.addAttribute("layerPublish", layerPublish);
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
	return "cartografico/solicitudesPublicacion";
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
     * Muestra el formulario con el estado de la solicitud de la publicacion de
     * capa
     *
     * @param webRequest
     * @param request
     * @param model
     * @param response
     * @param ident
     * @return
     */
    @RequestMapping(value = "/cartografico/estado/{ident}")
    public ModelAndView getEstados(WebRequest webRequest,
	    HttpServletRequest request, Model model,
	    HttpServletResponse response, @PathVariable("ident") String ident) {

	isLogate(webRequest);

	// Usuario autenticado
	//Authentication u = getAuthentication(webRequest);

	// Vista de retorno
	// ModelAndView modelAndView = new
	// ModelAndView("cartografico/infoEstado");
	ModelAndView modelAndView = new ModelAndView(
		"cartografico/infoEstadoAjax");

	// Obtenemos la capa de publicacion
	LayerPublishRequestDto p = (LayerPublishRequestDto) layerPublishRequestService
		.getById(Long.decode(ident));

	copyDefaultModel(model);

	model.addAttribute(
		"aceptar",
		!p.getEstado().equals(
		EstadoPublicacionLayerRequestPublish.RECHAZADA
		.toString()));

	// Texto a mostrar segun estado publicacion de la capa
	String texto;
	if (p.getEstado().equals(
		EstadoPublicacionLayerRequestPublish.ACEPTADA.toString())
		|| p.getEstado().equals(
		EstadoPublicacionLayerRequestPublish.LEIDA.toString())) {
	    // aceptada, leida
	    String opcion = p.isUpdate()? "actualizada" : "creada";
	    texto = MessageFormat.format("La capa «{0}» ha sido {1} con el nombre «{2}» en el canal «{3}».",
		    p.getSourceLayerName(),
		    opcion,
		    p.getNombredeseado(),
		    p.getPublishedFolder());

	    model.addAttribute("titulo", STATUS_WINDOW_TITLE_ACCEPTED);
	} else if (p.getEstado().equals(
		EstadoPublicacionLayerRequestPublish.PENDIENTE.toString())) {
	    // pendiente
	    texto = MessageFormat.format(
		    "La solicitud de publicación de la capa «{0}» realizada por la institución "
		    + "«{1}» en la fecha {2} se encuentra actualmente pendiente de"
		    + " validación por parte del Administrador.",
		    p.getSourceLayerName(),
		    p.getAuth().getAuthority(),
		    parseFechaFormat(p.getFechasolicitud()));

	    model.addAttribute("titulo", STATUS_WINDOW_TITLE_PENDING);
	} else {
	    // rechazada
	    texto = MessageFormat.format(
		    "La capa «{0}» cuya solicitud de publicación fue realizada por"
		    + " la institución «{1}» en la fecha {2} ha sido rechazada "
		    + "por el siguiente motivo:",
		    p.getSourceLayerName(),
		    p.getAuth().getAuthority(),
		    parseFechaFormat(p.getFechasolicitud()));

	    model.addAttribute("titulo", STATUS_WINDOW_TITLE_REJECTED);
	    model.addAttribute("comentario", p.getComentario());
	}

	// Añadimos las variables necesarias para la vista
	model.addAttribute("identificador", p.getId());
	model.addAttribute("estado", p.getEstado().toLowerCase());
	model.addAttribute("texto", texto);
	model.addAttribute("module", getActiveModule());
	model.addAttribute("cargaDatos", true);// Indica que la vista se debe
	// recargar
	return modelAndView;
    }

    /**
     * Editar estado de la solicitud de capa
     *
     * @param webRequest
     * @param request
     * @param model
     * @param response
     * @param ident
     * @param estado
     * @param action
     * @return
     */
    @RequestMapping(value = "/cartografico/editar/{estado}/{ident}", method = {
	RequestMethod.GET, RequestMethod.POST})
    public ModelAndView getEditarEstadoPublicacion(WebRequest webRequest,
	    HttpServletRequest request, Model model,
	    HttpServletResponse response, @PathVariable("ident") String ident,
	    @PathVariable("estado") String estado,
	    @RequestParam(required = true, value = "action") String action) {

	isLogate(webRequest);

	// Date hoy = new Date();

	// Usuario autenticado
	//Authentication u = getAuthentication(webRequest);

	// Vista de retorno
	ModelAndView modelAndView = new ModelAndView(
		"cartografico/solicitudesPublicacion");

	// Obtenemos la capa de publicacion
	LayerPublishRequestDto p = (LayerPublishRequestDto) layerPublishRequestService
		.getById(Long.decode(ident));

	if (estado
		.equalsIgnoreCase(EstadoPublicacionLayerRequestPublish.PENDIENTE
		.toString())) {
	    modelAndView = new ModelAndView(
		    "redirect:/cartografico/solicitudesPublicacion");
	} else if (estado.equalsIgnoreCase("ACEPTADA")
		|| estado.equalsIgnoreCase("LEIDA")) {
	    // Borramos la solicitud tras leerla. No necesitamos despublicar nada
	    // de geoserver porque eso ya se hizo cuando el admin rechazó la publicación.
	    layerPublishRequestService.delete(p);
	    modelAndView = new ModelAndView(
		    "redirect:/cartografico/solicitudesPublicacion");

	} else if (estado
		.equalsIgnoreCase(EstadoPublicacionLayerRequestPublish.RECHAZADA
		.toString())) {
	    if (action.equalsIgnoreCase("editar")) {
		// model.addAttribute("uri_", "../../");
		modelAndView = updatePublishRequest(model, p, webRequest);

	    } else if (action.equalsIgnoreCase("eliminar")) {
		layerPublishRequestService.delete(p);
		modelAndView = new ModelAndView(
			"redirect:/cartografico/solicitudesPublicacion?msg=success");
	    }
	} else {
	    modelAndView = new ModelAndView("estudios/mias");
	}

	// copyDefaultModel(model);
	model.addAttribute("module", getActiveModule());

	return modelAndView;
    }

    /**
     * Muestra datos de la solicitud desde la edicion del estado para su
     * edición.
     *
     * @param model
     * @param p Solicitud de publicacion
     * @return
     */
    private ModelAndView updatePublishRequest(Model model, LayerPublishRequestDto p,
	    WebRequest webRequest) {
	ModelAndView modelAndView = new ModelAndView(
		"cartografico/solicitudPublicacion");
	rolUser(model);

	// Usuario Logado y autoridad a la que pertenece
	String username = getCurrentUsername(webRequest);
	UsuarioDto userLogado = userAdminService
		.obtenerUsuarioByUsername(username);
	Long idAuth = userLogado.getAuthorityId();

	// Tipos de Layers (raster, vectorial,...)
	@SuppressWarnings("unchecked")
	List<LayerTypeDto> typeLayers = (List<LayerTypeDto>) layerTypeService
		.getAll();
	model.addAttribute(TYPELAYERS_KEY, typeLayers);



	// Obtenemos lista de capas del mismo tipo (ráster o vectorial) ya
	// publicadas anteriormente por la institución.
	List<LayerDto> layersActualizar = layerService
		.getLayersPubliciedByAuthIdByTypeId(idAuth, p.getSourceLayerType().getId());
	model.addAttribute("layersActualizar", layersActualizar);

	// Almacenamos el objeto en request
	LayerPublishRequestDto lpr =
		(LayerPublishRequestDto) layerPublishRequestService.getById(p.getId());
	lpr.setTipoCapaSeleccionada(lpr.getSourceLayerType().getNameAndTipo());

	// We check if the source layer still exists.
	LayerDto layer = (LayerDto) layerService
		.getById(lpr.getSourceLayerId());

	if (layer == null) {
	    return new ModelAndView("redirect:/cartografico/layersAuthority?msg=La capa con los datos de origen ya no existe.");
	}

	if (lpr.isUpdate()) {
	    // Estábamos actualizando una capa.
	    lpr.setNombredeseado("");
	    lpr.setAccionEjecutar(LayerPublishRequestDto.ACTION_PUBLISH_UPDATE);

	} else {
	    // La capa a actualziar no está especificada, luego estamos añadiendo una.
	    lpr.setAccionEjecutar(LayerPublishRequestDto.ACTION_PUBLISH_NEW);
	}

	// Añadimos la variable al modelo
	model.addAttribute("layerPublishRequestDto", lpr);

	model.addAttribute("module", getActiveModule());
	model.addAttribute("submodule", getActiveSubModule());
	return modelAndView;
    }

    @RequestMapping(value = "/cartografico/jsonLayerMetadata/{id}")
    public @ResponseBody
    LayerMetadataDto getJsonLayerMetadata(@PathVariable long id, Model model,
	    WebRequest webRequest) throws IOException {

	rolUser(model);

	if (id == 0) {
	    // Devolvemos una entidad vacía.
	    return new LayerMetadataDto();
	}

	LayerDto layer = (LayerDto) layerService.getById(id);

	LayerMetadataDto metadata = layer.getMetadata();

	return metadata;
    }

    /**
     * Muestra el formulario de solicitud de publicacion cartografico cuando se
     * accede desde el listado
     *
     * @param model
     * @param webrequest
     *
     * @return "solicitudPublicacion"
     */
    @RequestMapping(value = "/cartografico/nuevaSolicitudPublicacion/{id}")
    public String solicitudPublicacion(@PathVariable long id, Model model,
	    WebRequest webRequest) {

	if (LOG.isInfoEnabled()) {
	    LOG.info("Entrando en solicitud de publicacion");
	}

	isLogate(webRequest);

	// Usuario Logado y autoridad a la que pertenece
	String username = getCurrentUsername(webRequest);
	UsuarioDto userLogado = userAdminService
		.obtenerUsuarioByUsername(username);
	Long idAuth = userLogado.getAuthorityId();
	AuthorityDto institucion = (AuthorityDto) authorityService
		.getById(idAuth);

	LayerDto layer = (LayerDto) layerService.getById(id);
	// Comprobobamos que no hay una solucitud de publicación para la capa
	// por parte de la
	// autoridad del usuario actual.
	if (layerPublishRequestService.existsRequest(institucion, layer)) {
	    return requestReturn("redirect:/cartografico/layersAuthority",
		    "Ya hay una solicitud de publicación para la capa.");
	}

	// Obtenemos lista de capas del mismo tipo (ráster o vectorial) ya
	// publicadas anteriormente por la institución.
	List<LayerDto> layersActualizar = layerService
		.getLayersPubliciedByAuthIdByTypeId(idAuth, layer.getType()
		.getId());
	model.addAttribute("layersActualizar", layersActualizar);

	// Almacenamos el objeto en request
	LayerPublishRequestDto lpr = new LayerPublishRequestDto();
	lpr.setSourceLayerId(layer.getId());// Capa sobre la que clicamos
	lpr.setSourceLayerType(layer.getType());

	lpr.setTipoCapaSeleccionada(layer.getType().getNameAndTipo());
	lpr.setAuth(institucion);
	lpr.setUser(userLogado);

	lpr.setAccionEjecutar(LayerPublishRequestDto.ACTION_PUBLISH_NEW);

	// Añadimos la variable al modelo
	model.addAttribute("layerPublishRequestDto", lpr);

	if (LOG.isInfoEnabled()) {
	    LOG.info("Redirigiendo a la vista cartografico/solicitudPublicacion");
	}

	rolUser(model);
	model.addAttribute(MODULE_KEY, getActiveModule());
	model.addAttribute(SUBMODULE_KEY, getActiveSubModule());
	model.addAttribute("sourceLayerTitle", layer.getLayerLabel());

	return "cartografico/solicitudPublicacion";
    }

    /**
     * Guarda Solicitud de publicacion
     *
     * @param layerPublishRequestDto
     * @param errors
     * @param model
     * @return
     */
    @RequestMapping(value = "/cartografico/salvarSolicitudPublicacion", method = RequestMethod.POST)
    public String salvarSolicitudPublicacion(
	    @Valid LayerPublishRequestDto layerPublishRequestDto,
	    BindingResult errors, Model model, HttpServletRequest request,
	    WebRequest webRequest) {

	isLogate(webRequest);

	rolUser(model);
	model.addAttribute(MODULE_KEY, getActiveModule());
	model.addAttribute(SUBMODULE_KEY, getActiveSubModule());
	
	if(layerPublishRequestDto.isUpdate()) {
	    // We ensure we don't have a desired name, so the helper takes it
	    // from the updated layer.
	    layerPublishRequestDto.setNombredeseado(null);
	}

	PublicationRequestHelper.SaveRequestResult saveResult =
		publicationRequestHelper.savePublicationRequest(layerPublishRequestDto, true, errors);

	switch (saveResult) {
	    case FAILURE_EXISTING_REQUEST:
		return requestReturn("redirect:/cartografico/layersAuthority",
			"Ya hay una solicitud de publicación para la capa.");
	    case FAILURE_GEOSERVER_ERROR:
		return requestReturn("redirect:/cartografico/layersAuthority",
			"Ocurrió un error al publicar la capa en GeoServer.");
	    case FAILURE_VALIDATION_ERRORS:
		return "cartografico/solicitudPublicacion";
	    case FAILURE_SOURCE_LAYER_MISSING:
		return requestReturn("redirect:/cartografico/layersAuthority", "La capa con los datos de origen ya no existe.");
	    case SUCCESS_REQUEST_CREATION:
		return requestReturn("redirect:/cartografico/layersAuthority", "success");
	    case SUCCESS_REQUEST_MODIFICATION:

		return requestReturn(
			"redirect:/cartografico/solicitudesPublicacion", "success");
	    default:
		throw new IllegalStateException(String.format("Got non handled result type '%s' received when saving a publication request!", saveResult.name()));
	}
    }

    /**
     * Formatea fecha
     *
     * @param f
     * @return
     */
    public String parseFecha(Date f) {
	if (f == null) {
	    return "";
	}
	return this.format.format(f.getTime());
    }

    /**
     * Formatea fecha
     *
     * @param f
     * @return
     */
    private String parseFechaFormat(Date f) {
	if (f == null) {
	    return "";
	}

	SimpleDateFormat formato = new SimpleDateFormat(FORMATO_FECHA);
	return formato.format(f);
    }
}
