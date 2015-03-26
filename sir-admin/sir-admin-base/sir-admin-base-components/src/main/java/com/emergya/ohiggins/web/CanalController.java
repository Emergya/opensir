/*
 * CanalesController
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

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import javax.annotation.Resource;
import javax.validation.Valid;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.WebRequest;

import com.emergya.ohiggins.dto.FolderDto;
import com.emergya.ohiggins.dto.FolderTypeDto;
import com.emergya.ohiggins.dto.LayerDto;
import com.emergya.ohiggins.dto.NivelTerritorialDto;
import com.emergya.ohiggins.dto.NodeLayerTree;
import com.emergya.ohiggins.dto.UsuarioDto;
import com.emergya.ohiggins.security.ConstantesPermisos;
import com.emergya.ohiggins.security.OhigginsUserDetails;
import com.emergya.ohiggins.service.FolderService;
import com.emergya.ohiggins.service.InstitucionService;
import com.emergya.ohiggins.service.LayerService;
import com.emergya.ohiggins.service.NivelTerritorialService;
import com.emergya.ohiggins.service.UserAdminService;
import com.emergya.ohiggins.web.validators.FolderDtoValidator;
import com.emergya.persistenceGeo.service.FoldersAdminService;

/**
 * Controlador para la creacion de canales
 * 
 * @author jariera
 * 
 */
@Controller
public class CanalController extends AbstractController {

	/**
	 * Serials
	 */
	private static final long serialVersionUID = -5520103642663907201L;

	/** Log */
	private static Log LOG = LogFactory.getLog(CanalController.class);

	/** Modulo y submodulo */
	public final static String MODULE = "cartografico";
	public final static String SUB_MODULE = "";
	public final static String CANALES = "canales";
	private static final String LIST_AMBITO_TERRITORIAL = "ambitoTerritorial";
	private static final String ALL_AMBITO_TERRITORIAL = "allAmbitoTerritorial";

	private static final String SUCCESS_URL = "redirect:/cartografico/canales?msg=success";
	private static final String UNASSIGNED_LAYERS_ATTR = "unassignedLayers";
	private static final String FOLDER_LAYERS_ATTR = "folderLayers";
	private static final String IS_IN_CHANNEL_ATTR = "folderIsInChannel";
	
	private static final String MAX_PRE_LAYERS_REACHED_KEY="MAX_PRE_LAYERS_REACHED";

	/** Canales y Carpetas */
	private static final String CARPETA = "Carpeta";

	// Servicios utilizados
	@Resource
	private NivelTerritorialService nivelTerritorialService;
	// @Resource
	// private LayerService layerService;
	// @Resource
	// private LayerTypeService layerTypeService;
	@Resource
	private UserAdminService userAdminService;
	// @Resource
	// private InversionUpdateService inversionUpdateService;
	// @Resource
	// private LayerPublishRequestService layerPublishRequest;
	@Resource
	private InstitucionService authorityService;
	@Resource
	private FolderService folderService;
	@Resource
	private FoldersAdminService folderAdminService;

	@Resource
	private LayerService layerService;

	public static final String LAYERTREE = "layerTree";
	public static final int TAM_DEFAULT = 10000000;
	private static final String PRI = "PRI";
	private static final String PRC = "PRC";
	private static final String NONE = "NONE";

	private static final String PROVINCIAL = "P";
	private static final String MUNICIPAL = "M";
	private static final String REGIONAL = "R";

	private static final int MAX_PRE_LAYERS_PER_FOLDER = 5;

	/**
	 * Muestra el listado de canales
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/cartografico/canales")
	public String listadoCanales(Model model, WebRequest webRequest) {
		String r = "cartografico/canales";

		isLogate(webRequest);

		if (LOG.isInfoEnabled()) {
			LOG.info("Consultando listadoCanales");
		}

		webRequest.removeAttribute(FOLDER_LAYERS_ATTR,
				RequestAttributes.SCOPE_SESSION);
		webRequest.removeAttribute(UNASSIGNED_LAYERS_ATTR,
				RequestAttributes.SCOPE_SESSION);

		// Arbol de capas.
		List<NodeLayerTree> layerTree = (List<NodeLayerTree>) folderService
				.getArbolCapas((1 - 1) * TAM_DEFAULT, 1 * TAM_DEFAULT,
						LayerDto.NAME_PROPERTY);

		// Solo las carpetas, no las capas
		List<NodeLayerTree> layerTree2 = new LinkedList<NodeLayerTree>();
		if (layerTree != null && layerTree.size() > 0) {
			for (NodeLayerTree tree : layerTree) {
				if (!NodeLayerTree.getLayer().equals(tree.getTipo())) {
					layerTree2.add(tree);
				}
			}
		}

		model.addAttribute(LAYERTREE, layerTree2);

		rolUser(model);
		model.addAttribute(MODULE_KEY, getActiveModule());
		model.addAttribute(SUBMODULE_KEY, CANALES);

		model.addAttribute("cargaDatos", false);
		model.addAttribute("folderDto", new FolderDto());
		return r;
	}

	/**
	 * Muestra el formulario para la creacion de nuevos canales
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/cartografico/nuevoCanal")
	public String nuevoCanal(Model model, WebRequest webRequest) {
		isLogate(webRequest);

		if (LOG.isInfoEnabled()) {
			LOG.info("Entrando en nuevoCanal");
		}

		String r = "cartografico/nuevoCanal";

		FolderDto folderDto = new FolderDto();
		FolderTypeDto defaultFolderType = folderService.getFolderTypeDto(FolderService.DEFAULT_FOLDER_TYPE);

		// Valores por defecto, es canal, es Plain y activado
		folderDto.setIsChannel(true);
		folderDto.setFolderType(defaultFolderType);
		folderDto.setEnabled(true);

		model.addAttribute("folderDto", folderDto);
		model.addAttribute("folderTypeDtoList", folderAdminService.getNotParentFolderTypes());

		// Ambito territorial (Zone)
		List<NivelTerritorialDto> ambitoTerritorial = new LinkedList<NivelTerritorialDto>();
		model.addAttribute(LIST_AMBITO_TERRITORIAL, ambitoTerritorial);
		model.addAttribute(ALL_AMBITO_TERRITORIAL, nivelTerritorialService.getZonesOrderByTypeDescNameAsc());

		rolUser(model);
		model.addAttribute(MODULE_KEY, getActiveModule());
		model.addAttribute(SUBMODULE_KEY, CANALES);

		return r;
	}

	/**
	 * Salva como nuevo, o edita el canal
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/cartografico/saveCanal")
	public String saveCanal(@Valid FolderDto folderDto, BindingResult result,
			Model model, WebRequest webRequest) {

		isLogate(webRequest);
		decideIfIsUserInRole(ConstantesPermisos.ADMIN);

		if (LOG.isInfoEnabled()) {
			LOG.info("Entrando en saveNuevoCanal");
		}

		FolderDtoValidator validator = new FolderDtoValidator(folderService);
		validator.validate(folderDto, result);

		boolean maxPreloadedLayersReached = folderDto.getId()!=null && this.checkMaxPreloadLayersReached(webRequest);
		
		// Validamos
		if (result.hasErrors() || maxPreloadedLayersReached) {
			cargaListasVista(model);
			rolUser(model);
			model.addAttribute(MODULE_KEY, getActiveModule());
			model.addAttribute(SUBMODULE_KEY, CANALES);
			model.addAttribute("folderDto", folderDto);
			
			if(maxPreloadedLayersReached) {
				model.addAttribute(MAX_PRE_LAYERS_REACHED_KEY,true);
			}

			return "cartografico/nuevoCanal";
		}

		Date hoy = new Date();

		// Recogemos la institucion a la que pertenece el usuario, Usuario
		// Logado y autoridad a la que pertenece
		String username = getCurrentUsername(webRequest);
		UsuarioDto userLogado = userAdminService
				.obtenerUsuarioByUsername(username);

		folderDto.setUser(userLogado);

		if (folderDto.getZoneSeleccionada() != null
				&& !"".equals(folderDto.getZoneSeleccionada())) {
			NivelTerritorialDto zone = (NivelTerritorialDto) nivelTerritorialService
					.getById(Long.decode(folderDto.getZoneSeleccionada()));
			folderDto.setZone(zone);
		}
		// Sacamos el tipo de carpeta seleccionado
		if (folderDto.getFolderTypeSelected() != null
				&& !folderDto.getFolderTypeSelected().equals("")) {
			FolderTypeDto folderTypeDto = folderService
					.getFolderTypeDto(Long.decode(folderDto.getFolderTypeSelected()));
			if (folderTypeDto.getType().equals(CARPETA)) {
				folderDto.setIsChannel(false);
			} else {
				folderDto.setIsChannel(true);
			}
			folderDto.setFolderType(folderTypeDto);
		}

		// Creamos o acutalizamos el canal
		if (folderDto.getId() == null) { // Nuevo
			folderDto.setCreateDate(hoy);
			folderService.create(folderDto);
		} else { // Actualizar
			folderDto.setUpdateDate(hoy);
			FolderDto f = (FolderDto) folderService.getById(folderDto.getId());
			folderDto.setCreateDate(f.getCreateDate());

			folderService.update(folderDto);

			// Actualizamos la información de capas.
			updateFolderLayers(folderDto, webRequest);
			
		}

		rolUser(model);
		model.addAttribute(MODULE_KEY, getActiveModule());
		model.addAttribute(SUBMODULE_KEY, CANALES);

		return SUCCESS_URL;
	}

	/**
	 * Muestra el formulario para la edicion de canales
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/cartografico/editarCanal/{id}")
	public String editCanal(@PathVariable long id, Model model,
			WebRequest webRequest) {
		isLogate(webRequest);

		if (LOG.isInfoEnabled()) {
			LOG.info("Entrando en editCanal");
		}

		String r = "cartografico/nuevoCanal";

		FolderDto folderDto = new FolderDto();
		folderDto = (FolderDto) folderService.getById(id);
		// Tenemos una zona?
		boolean zoneNotNull = folderDto.getZone() != null
				&& folderDto.getZone().getId() != null;
		// Tenemos un tipo de carpeta?
		boolean folderTypeNotNull = folderDto.getFolderType() != null
				&& folderDto.getFolderType().getId() != null;

		folderDto.setZoneSeleccionada(zoneNotNull ? folderDto.getZone().getId()
				.toString() : "");
		// Cogemos el id folderType del objeto folderType
		folderDto.setFolderTypeSelected(folderTypeNotNull ?
				folderDto.getFolderType().getId().toString() : "");
		String tipoAmbito = zoneNotNull ? folderDto.getZone().getTipo_ambito()
				: "";

		model.addAttribute("folderDto", folderDto);
		model.addAttribute("folderTypeDtoList", folderAdminService.getNotParentFolderTypes());

		// Ambito territorial (Zone)
		List<NivelTerritorialDto> ambitoTerritorial = new LinkedList<NivelTerritorialDto>();
		/*
		 * List<NivelTerritorialDto> ambitoTerritorial = nivelTerritorialService
		 * .getAllNivelTerritorial();
		 */

		if (PROVINCIAL.equals(tipoAmbito) || REGIONAL.equals(tipoAmbito)) {// "P"
																			// ||
																			// "R"
			// Ambito territorial (Zone)
			// ambitoTerritorial =
			// nivelTerritorialService.getNivelTerritorialByTipo("P");
			String[] tips = new String[2];
			tips[0] = PROVINCIAL;// "P";
			tips[1] = REGIONAL;// "R";
			ambitoTerritorial = nivelTerritorialService
					.getNivelTerritorialByTipos(tips);
		} else if (MUNICIPAL.equals(tipoAmbito)) {// "M"
			ambitoTerritorial = nivelTerritorialService
					.getNivelTerritorialByTipo(MUNICIPAL);// "M"
		}

		model.addAttribute(LIST_AMBITO_TERRITORIAL, ambitoTerritorial);
		model.addAttribute(ALL_AMBITO_TERRITORIAL, nivelTerritorialService.getZonesOrderByTypeDescNameAsc());

		loadLayersInSession(id, folderDto.getIsChannel(), webRequest);

		rolUser(model);
		model.addAttribute(MODULE_KEY, getActiveModule());
		model.addAttribute(SUBMODULE_KEY, CANALES);
		
		return r;
	}

	/**
	 * Borrar el canal
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/cartografico/borrarCanal/{id}")
	public String borrarCanal(@PathVariable long id, Model model,
			WebRequest webRequest) {
		isLogate(webRequest);

		if (LOG.isInfoEnabled()) {
			LOG.info("Entrando en borrarCanal");
		}

		rolUser(model);
		model.addAttribute(MODULE_KEY, getActiveModule());
		model.addAttribute(SUBMODULE_KEY, CANALES);

		FolderDto folderDto = (FolderDto) folderService.getById(id);
		if (folderDto == null) {
			return showNotice("La carpeta seleccionada ya no existe.");
		}
		
		List<LayerDto> layers = layerService.getLayersByFolderId(folderDto.getId());
		
		if(!layers.isEmpty()) {
			// No podemos borrar, la base de datos no se deja por las FK y tal.			
			return showNotice("No se puede borrar porque tiene capas asginadas. Quítelas primero, por favor.");
		}

		List<FolderDto> children = folderService.getFolderByParentId(folderDto
				.getId());
		if (!children.isEmpty()) {
			// No podemos borrar, la base de datos no se deja por las FK y tal.			
			return showNotice("No se puede borrar porque tiene subcarpetas.");
		}
		
		

		folderService.delete(folderDto);

		return SUCCESS_URL;
	}

	private String showNotice(String msg) {

		String value = "redirect:/cartografico/canales?msg=Error";
		try {
			value =  String.format("redirect:/cartografico/canales?msg=%s", URLEncoder.encode(msg, "UTF-8"));
		} catch (UnsupportedEncodingException e) {
			// Shouldn't happen.			
		}
		return value;
	}

	/**
	 * Muestra el popup para la creacion de nuevas subcarpetas
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping(value = " /cartografico/nuevasubcarpeta/{id}")
	public String nuevaSubcarpeta(@PathVariable long id, Model model,
			WebRequest webRequest) {
		isLogate(webRequest);

		if (LOG.isInfoEnabled()) {
			LOG.info("Entrando en nuevaSubcarpeta");
		}

		String r = "cartografico/nuevaSubcarpeta";

		FolderDto folderDto = (FolderDto) folderService.getById(id);
		if (folderDto != null && folderDto.getId() != null) {
			// FIXME
			folderDto.setName(new String());
		}

		rolUser(model);

		model.addAttribute("cargaDatos", true);
		model.addAttribute("folderDto", folderDto);
		return r;
	}

	/**
	 * Guarda la subcarpeta como hija de la que nos llega en el dto
	 * 
	 * @param folderDto
	 * @param model
	 * @param webRequest
	 * @return
	 */
	@RequestMapping(value = "/cartografico/saveNuevaSubcarpeta")
	public String saveNuevaSubcarpeta(@Valid FolderDto folderDto, Model model,
			WebRequest webRequest) {
		isLogate(webRequest);

		if (LOG.isInfoEnabled()) {
			LOG.info("Entrando en saveNuevaSubcarpeta");
		}

		Date hoy = new Date();

		// Obtenemos la carpeta padre sobre la que insertamos el hijo
		FolderDto folderpadre = (FolderDto) folderService.getById(folderDto
				.getId());
		if (folderpadre != null && folderpadre.getId() != null) {
			FolderDto nf = new FolderDto();
			nf.setName(folderDto.getName());
			nf.setCreateDate(hoy);
			nf.setParent(folderpadre);
			nf.setIsChannel(false);// No es un canal, es una carpeta
			nf.setEnabled(false);// Por defecto se guarda desactivo

			// Resto de propiedades que se quieran guardar

			folderService.create(nf);
		}

		return SUCCESS_URL;
	}

	/**
	 * Muestra la página para editar una carpeta.
	 * 
	 * @param id
	 * @param model
	 * @param webRequest
	 * @return
	 */
	@RequestMapping(value = " /cartografico/editarSubcarpeta/{id}")
	public String editarSubcarpeta(@PathVariable long id, Model model,
			WebRequest webRequest) {

		isLogate(webRequest);

		if (LOG.isInfoEnabled()) {
			LOG.info("Entrando en editarCarpeta");
		}

		// Obtenemos la carpeta a editar.
		FolderDto folderDto = (FolderDto) folderService.getById(id);

		// Obtenemos los nombres de los padres para formar el breadcumb.
		List<String> parentNames = new ArrayList<String>();
		FolderDto parentFolder = folderDto.getParent();
		while (true) {
			// Insertamos al pricipio...
			parentNames.add(0, parentFolder.getName());
			
			if(parentFolder.getParent()==null) {
				break;
			}
			
			parentFolder = parentFolder.getParent();
		}

		rolUser(model);
		
		loadLayersInSession(id, parentFolder.getIsChannel(), webRequest);

		model.addAttribute("folderDto", folderDto);

		model.addAttribute(MODULE_KEY, getActiveModule());
		model.addAttribute(SUBMODULE_KEY, CANALES);
		webRequest.setAttribute("parentNames", parentNames, WebRequest.SCOPE_SESSION);

		return "cartografico/editarSubcarpeta";
	}

	private void loadLayersInSession(long folderId, Boolean isInChannel, WebRequest webRequest) {
		webRequest.setAttribute(IS_IN_CHANNEL_ATTR, isInChannel, RequestAttributes.SCOPE_SESSION);

		// Recuperamos las carpetas asignadas a la carpeta y las que no están
		// asignadas a ninguna carpeta
		// y las metemos en la sesión para poder editar propiedades y mover de
		// una a otra mediante AJAX.
		List<LayerDto> unassignedLayers = layerService.getUnassignedLayers(
				true, true);
		List<LayerDto> folderLayers = layerService.getLayersByFolderId(
				folderId, true);
		webRequest.setAttribute(UNASSIGNED_LAYERS_ATTR, unassignedLayers,
				RequestAttributes.SCOPE_SESSION);
		webRequest.setAttribute(FOLDER_LAYERS_ATTR, folderLayers,
				RequestAttributes.SCOPE_SESSION);

	}

	@RequestMapping(value = "/cartografico/saveSubcarpeta")
	public String saveSubcarpeta(@Valid FolderDto editedFolderDto, Model model,
			WebRequest webRequest) {
		isLogate(webRequest);

		if (LOG.isInfoEnabled()) {
			LOG.info("Entrando en saveSubcarpeta");
		}
		
		if(this.checkMaxPreloadLayersReached(webRequest)) {
			rolUser(model);
			model.addAttribute(MODULE_KEY, getActiveModule());
			model.addAttribute(SUBMODULE_KEY, CANALES);
			model.addAttribute("folderDto", editedFolderDto);
			model.addAttribute(MAX_PRE_LAYERS_REACHED_KEY,true);

			return "cartografico/editarSubcarpeta";
		}

		Date now = new Date();

		// Cargamos los datos existentes.
		FolderDto existingFolderDto = (FolderDto) folderService
				.getById(editedFolderDto.getId());

		// Modificamos los datos introducibles en el formulario.
		existingFolderDto.setName(editedFolderDto.getName());
		existingFolderDto.setEnabled(editedFolderDto.getEnabled());
		existingFolderDto.setUpdateDate(now);

		folderService.update(existingFolderDto);

		updateFolderLayers(existingFolderDto, webRequest);

		return SUCCESS_URL;
	}

	/**
	 * Gestiona la (des)asignación de capas a un canal o carpeta.
	 * 
	 * @param folderDto
	 *            Canal/carpeta al que se asignarán las capas marcadas como
	 *            asignadas.
	 * @param webRequest
	 * @returns
	 */
	@SuppressWarnings("unchecked")
	private void updateFolderLayers(FolderDto folderDto, WebRequest webRequest) {

		List<LayerDto> folderLayers = (List<LayerDto>) webRequest.getAttribute(
				FOLDER_LAYERS_ATTR, RequestAttributes.SCOPE_SESSION);

		Date now = new Date();
		

		// A las capas marcadas como pertenecientes al canal/capa se lo
		// asignamos y
		// guardamos.
		int order = 0;		
		for (LayerDto folderLayer : folderLayers) {
			folderLayer.setFolder(folderDto);
			folderLayer.setOrder(order);
			folderLayer.setUpdateDate(now);
			 
			if(!folderLayer.getEnabled()) {
				folderLayer.setIsChannel(false);
			}
			
			folderLayer.setPublicized(true);
			layerService.update(folderLayer);
			order++;
		}

		List<LayerDto> unassignedLayers = (List<LayerDto>) webRequest
				.getAttribute(UNASSIGNED_LAYERS_ATTR,
						RequestAttributes.SCOPE_SESSION);
		// Las capas marcadas como no asignadas pierden el padre (si lo tenían)
		// y se guardan.
		order = 0;
		for (LayerDto unassignedLayer : unassignedLayers) {
			unassignedLayer.setFolder(null);
			unassignedLayer.setPublicized(true);
			unassignedLayer.setOrder(order);
			unassignedLayer.setUpdateDate(now);
			layerService.update(unassignedLayer);

			order++;
		}
	}

	/**
	 * Carga las listas necesarios para la vista
	 * 
	 * @param model
	 */
	private void cargaListasVista(Model model) {
		// Ambito territorial
		List<NivelTerritorialDto> ambitoTerritorial = nivelTerritorialService
				.getAllNivelTerritorial();

		// Añadimos al select objeto vacio
		NivelTerritorialDto nt = new NivelTerritorialDto();
		ambitoTerritorial.add(0, nt);

		model.addAttribute(LIST_AMBITO_TERRITORIAL, ambitoTerritorial);
		
		model.addAttribute("folderTypeDtoList", folderAdminService.getNotParentFolderTypes());
		model.addAttribute(ALL_AMBITO_TERRITORIAL, nivelTerritorialService.getZonesOrderByTypeDescNameAsc());

	}

	@Override
	protected void copyDefaultModel(boolean update, Model model) {

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
		return null;
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
	 * 
	 * @param model
	 */
	private void rolUser(Model model) {
		model.addAttribute("IS_ADMIN",
				OhigginsUserDetails.userHasAuthority(ConstantesPermisos.ADMIN));
	}

	/**
	 * Autenticator
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
	 * Muestra el listado de zonas segun el tipo de planificacion
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/cartografico/listZones/{tip}")
	public String listZones(@PathVariable String tip, Model model,
			WebRequest webRequest) {
		String r = "cartografico/listZones";

		List<NivelTerritorialDto> ambitoTerritorial = new LinkedList<NivelTerritorialDto>();
		// PRC tipo municipal (M)
		// PRI EL RESTO (R,P)

		if (PRI.equals(tip)) {
			// Ambito territorial (Zone)
			// ambitoTerritorial =
			// nivelTerritorialService.getNivelTerritorialByTipo("P");
			String[] tips = new String[2];
			tips[0] = PROVINCIAL;// "P";
			tips[1] = REGIONAL;// "R";
			ambitoTerritorial = nivelTerritorialService
					.getNivelTerritorialByTipos(tips);
		} else if (PRC.equals(tip)) {
			ambitoTerritorial = nivelTerritorialService
					.getNivelTerritorialByTipo(MUNICIPAL);// "M"
		}

		model.addAttribute(LIST_AMBITO_TERRITORIAL, ambitoTerritorial);
		model.addAttribute("folderDto", new FolderDto());

		return r;
	}

	/**
	 * Usado para, desde la edición de canal/carpeta, habilitar o deshabilitar
	 * una capa.
	 * 
	 * @param assigned
	 *            Si true, el índice hace referencia a las capas asignadas al
	 *            canal/carpeta. Si no, hace referencia a las capas sin asignar.
	 * @param id
	 *            El id de la capa a la que se hace referencia (dentro de las
	 *            asignadas/desasignadas).
	 * @param model
	 * @param webRequest
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/cartografico/toggleLayerEnabled/{assigned}/{id}", method = RequestMethod.POST)
	public @ResponseBody
	String toggleLayerEnabled(@PathVariable boolean assigned,
			@PathVariable long id, Model model, WebRequest webRequest) {
		if (LOG.isInfoEnabled()) {
			LOG.info("Entrando en toggleLayerEnabled");
		}

		// Decidimos si trabajamos con las capas asignadas o las no asignadas.
		String listAttr = assigned ? FOLDER_LAYERS_ATTR
				: UNASSIGNED_LAYERS_ATTR;

		// Y las recuperamos.
		List<LayerDto> layers = (List<LayerDto>) webRequest.getAttribute(
				listAttr, RequestAttributes.SCOPE_SESSION);

		// Cambiamos el estado de activación de la capa.
		int idx = getLayerIndexFromId(id, layers);
		LayerDto toggledLayer = layers.get(idx);
		toggledLayer.setEnabled(!toggledLayer.getEnabled());

		
		if (assigned && !toggledLayer.getEnabled()) {
			// Disabled layers cannot be preloaded.
			toggledLayer.setIsChannel(false);
		}

		// Y volvemos a meter la lista de capas en la sesión.
		webRequest.setAttribute(listAttr, layers,
				RequestAttributes.SCOPE_SESSION);

		return "success";
	}

	/**
	 * Usado para, desde la edición de canal/carpeta, indicar que se precarga o
	 * no una capa de entre las asignadas.
	 * 
	 * @param id
	 *            El id de la capa a la que se hace referencia.
	 * @param model
	 * @param webRequest
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/cartografico/toggleLayerIsChannel/{id}", method = RequestMethod.POST)
	public @ResponseBody
	String toggleLayerIsChannel(@PathVariable long id, Model model,
			WebRequest webRequest) {
		if (LOG.isInfoEnabled()) {
			LOG.info("Entrando en toggleLayerIsChannel");
		}

		Boolean folderIsChannel = (Boolean) webRequest.getAttribute(IS_IN_CHANNEL_ATTR, RequestAttributes.SCOPE_SESSION);

		if (folderIsChannel == null || !folderIsChannel) {
			// We do nothing as this shouldn't happen.
			return "success";
		}

		// Recuperamos las capas asignadas al canal/carpeta.
		List<LayerDto> layers = (List<LayerDto>) webRequest.getAttribute(
				FOLDER_LAYERS_ATTR, RequestAttributes.SCOPE_SESSION);
		
		
		// Cambiamos el estado de precarga (correspondiente al atributo
		// isChannel) de la capa.
		int idx = getLayerIndexFromId(id, layers);
		LayerDto toggledLayer = layers.get(idx);
		boolean isChannel = false;
		if (toggledLayer.getIsChannel() != null) {
			isChannel = toggledLayer.getIsChannel();
		}
		
		toggledLayer.setIsChannel(!isChannel);

		// Y volvemos a meter la lista de capas en la sesión.
		webRequest.setAttribute(FOLDER_LAYERS_ATTR, layers,
				RequestAttributes.SCOPE_SESSION);

		return "success";
	}

	/**
	 * Usado para añadir una capa a un canal/carpeta.
	 * 
	 * @param id
	 *            El id de la capa no asignada a la que se hace referencia.
	 * @param model
	 * @param webRequest
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/cartografico/addLayerToFolder/{id}", method = RequestMethod.POST)
	public @ResponseBody
	String addLayerToFolder(@PathVariable long id, Model model,
			WebRequest webRequest) {

		if (LOG.isInfoEnabled()) {
			LOG.info("Entrando en addLayerToFolder");
		}

		// Recuperamos las capas
		List<LayerDto> unassignedLayers = (List<LayerDto>) webRequest
				.getAttribute(UNASSIGNED_LAYERS_ATTR,
						RequestAttributes.SCOPE_SESSION);
		List<LayerDto> folderLayers = (List<LayerDto>) webRequest.getAttribute(
				FOLDER_LAYERS_ATTR, RequestAttributes.SCOPE_SESSION);

		
		// Quitamos la capa de las no asignadas y la añadimos a las de la
		// carpeta/canal.
		int idx = getLayerIndexFromId(id, unassignedLayers);
		
		
		
		LayerDto addedLayer = unassignedLayers.remove(idx);

		folderLayers.add(addedLayer);

		// Actualizamos las capas en la sesión.
		webRequest.setAttribute(FOLDER_LAYERS_ATTR, folderLayers,
				RequestAttributes.SCOPE_SESSION);
		webRequest.setAttribute(UNASSIGNED_LAYERS_ATTR, unassignedLayers,
				RequestAttributes.SCOPE_SESSION);

		Boolean folderIsChannel = (Boolean) webRequest.getAttribute(IS_IN_CHANNEL_ATTR, RequestAttributes.SCOPE_SESSION);
		
		
		

		if (folderIsChannel == null || !folderIsChannel || !addedLayer.getEnabled()) {
			addedLayer.setIsChannel(false);
		}

		boolean isChannel = false;
		if (addedLayer.getIsChannel() != null) {
			isChannel = addedLayer.getIsChannel();
		}
		// Creamos la fila que añadiremos a la tabla.
		String layerIdStr = addedLayer.getId().toString();

		boolean disabledIsChannel = false;
		if (folderIsChannel == null || !folderIsChannel || !addedLayer.getEnabled()) {
			disabledIsChannel = true;
		}

		return String
				.format("<tr><td title=\"%s\" class=\"primera-cebreada\">%s</td><td>%s</td><td>%s</td><td>%s %s</td><td class=\"ultima-cebreada\">%s</td></tr>",
						addedLayer.getLayerLabel(),
						addedLayer.getLayerLabel(),
						createCheckBox(false, addedLayer.getEnabled(),
								"enabled-"+layerIdStr,"toggleLayerEnabled", "true", layerIdStr, "checked"),
						createCheckBox(disabledIsChannel, isChannel, "isChannel-"+layerIdStr, 
								"toggleLayerIsChannel", layerIdStr),
						createLink("Subir",
								"layerUpLink icon textless arrowUp",
								"moveLayer", "true", "-1", layerIdStr, "this"),
						createLink("Bajar",
								"layerDownLink icon textless arrowDown",
								"moveLayer", "true", "1", layerIdStr, "this"),
						createLink("Quitar", "icon textless remove",
								"removeLayerFromFolder", layerIdStr, "this"));
	}

	private boolean checkMaxPreloadLayersReached(WebRequest webRequest) {
		List<LayerDto> folderLayers = (List<LayerDto>) webRequest.getAttribute(
				FOLDER_LAYERS_ATTR, RequestAttributes.SCOPE_SESSION);
		int preLayers = 0;
		for(LayerDto folderLayer: folderLayers) {
			if(folderLayer.getIsChannel()) {
				preLayers++;
			}
		}
		
		// It should never be greater, but still...	
		return preLayers > MAX_PRE_LAYERS_PER_FOLDER;
	}

	private String createCheckBox(boolean disabled, boolean checked, String id,String onClickCallBack,
			String... callbackArgs) {

		return String.format(
				"<input type=\"checkbox\" %s %s onchange=\"%s(%s)\" id=\"%s\"/>",
				disabled? "disabled=\"disabled\"":"",
				checked ? "checked=\"checked\"" : "", onClickCallBack,
				StringUtils.join(callbackArgs, ", "),
				id);
	}

	private String createLink(String label, String cssClasses,
			String onClickCallBack, String... callbackArgs) {
		return String
				.format("<a href=\"javascript:void(0)\" %s onclick=\"%s(%s)\" title=\"%s\">%s</a>",
						StringUtils.isNotEmpty(cssClasses) ? String.format(
								"class=\"%s\"", cssClasses) : "",
						onClickCallBack, StringUtils.join(callbackArgs, ", "),
						label, label);
	}

	/**
	 * Usado para quitar una capa de un canal/carpeta.
	 * 
	 * @param id
	 *            El íd de la capa no asignada que se le quita a la carpeta.
	 * @param model
	 * @param webRequest
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/cartografico/removeLayerFromFolder/{id}", method = RequestMethod.POST)
	public @ResponseBody
	String removeLayerFromFolder(@PathVariable long id, Model model,
			WebRequest webRequest) {

		if (LOG.isInfoEnabled()) {
			LOG.info("Entrando en removeLayerFromFolder");
		}

		// Recuperamos las capas
		List<LayerDto> unassignedLayers = (List<LayerDto>) webRequest
				.getAttribute(UNASSIGNED_LAYERS_ATTR,
						RequestAttributes.SCOPE_SESSION);
		List<LayerDto> folderLayers = (List<LayerDto>) webRequest.getAttribute(
				FOLDER_LAYERS_ATTR, RequestAttributes.SCOPE_SESSION);

		// Quitamos la capa de las de la carpeta/canal y la añadimos a las no
		// asignadas.
		int idx = getLayerIndexFromId(id, folderLayers);
		LayerDto removedLayer = folderLayers.remove(idx);
		unassignedLayers.add(removedLayer);

		// Actualizamos las capas en la sesión.
		webRequest.setAttribute(FOLDER_LAYERS_ATTR, folderLayers,
				RequestAttributes.SCOPE_SESSION);
		webRequest.setAttribute(UNASSIGNED_LAYERS_ATTR, unassignedLayers,
				RequestAttributes.SCOPE_SESSION);

		// Creamos la fila que añadiremos a la tabla.
		String layerIdStr = removedLayer.getId().toString();
		return String
				.format(
						"<tr><td title=\"%s\" class=\"primera-cebreada\">%s</td><td>%s</td><td>%s %s</td><td class=\"ultima-cebreada\">%s</td></tr>",
						removedLayer.getLayerLabel(),
						removedLayer.getLayerLabel(),
						createCheckBox(false, removedLayer.getEnabled(),
								"enabled-"+layerIdStr,
								"toggleLayerEnabled","false", layerIdStr),
						createLink("Subir", "layerUpLink icon textless arrowUp",
								"moveLayer", "false", "-1", layerIdStr, "this"),
						createLink("Bajar", "layerDownLink icon textless arrowDown",
								"moveLayer", "false", "1", layerIdStr, "this"),
						createLink("Añadir", "icon textless add",
								"addLayerToFolder", layerIdStr, "this"));
	}

	/**
	 * Usado para reordenar una capa.
	 * 
	 * @param assigned
	 *            Si la capa es de las no asignadas o de las del canal/carpeta
	 *            siendo editada.
	 *            
	 * @param displacement
	 *            El desplazamiento que se aplica a la capa.
	 * @param id
	 *            El id de la capa que se mueve.
	 * @param model
	 * @param webRequest
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/cartografico/moveLayer/{assigned}/{displacement}/{id}", method = RequestMethod.POST)
	public @ResponseBody
	String moveLayer(@PathVariable boolean assigned,
			@PathVariable int displacement, @PathVariable long id, Model model,
			WebRequest webRequest) {

		if (LOG.isInfoEnabled()) {
			LOG.info("Entrando en moveLayer");
		}

		String layersAttr = assigned ? FOLDER_LAYERS_ATTR
				: UNASSIGNED_LAYERS_ATTR;

		// Recuperamos las capas con las que trabajaremos.
		List<LayerDto> layers = (List<LayerDto>) webRequest.getAttribute(
				layersAttr, RequestAttributes.SCOPE_SESSION);

		int idx = getLayerIndexFromId(id, layers);

		// La capa que estamos moviendo.
		LayerDto movedLayer = layers.get(idx);

		int targetIdx = idx + displacement;

		// La capa que se desplaza debido al movimiento de la otra.
		LayerDto displacedLayer = layers.get(targetIdx);

		// Intercambiamos las capas en la colección.
		layers.set(targetIdx, movedLayer);
		layers.set(idx, displacedLayer);

		// Actualizamos las capas en la sesión.
		webRequest.setAttribute(layersAttr, layers,
				RequestAttributes.SCOPE_SESSION);

		return "success";
	}

	private int getLayerIndexFromId(long id, List<LayerDto> layers) {
		LayerDto dummyLayer = new LayerDto();
		dummyLayer.setId(id);

		return layers.indexOf(dummyLayer);
	}
}
