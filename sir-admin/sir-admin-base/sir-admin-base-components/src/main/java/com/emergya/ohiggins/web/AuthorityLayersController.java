/*
 * CartograficoController
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

import com.emergya.ohiggins.dto.LayerDto;
import com.emergya.ohiggins.dto.UsuarioDto;
import com.emergya.ohiggins.security.ConstantesPermisos;
import com.emergya.ohiggins.security.OhigginsUserDetails;
import com.emergya.ohiggins.service.LayerService;
import com.emergya.ohiggins.service.UserAdminService;
import com.emergya.persistenceGeo.service.GeoserverService;

@Controller
public class AuthorityLayersController extends AbstractController {

	private static final long serialVersionUID = -3301052158744970660L;
	private static Log LOG = LogFactory.getLog(AuthorityLayersController.class);
	public final static String MODULE = "cartografico";
	public final static String SUB_MODULE = "layersAuthority";	

	private List<LayerDto> layersAuthority = null;

	@Resource
	private LayerService layerService;
	
	@Resource
	private UserAdminService userAdminService;
	
	@Resource
	private GeoserverService geoserverService;
	
	/**
	 * Pagina capas de la autoridad del usuario conectado
	 * 
	 * @param model
	 * 
	 * @return "layersAuthority"
	 */
	@RequestMapping(value = "/cartografico/layersAuthority", method = RequestMethod.GET)
	@SuppressWarnings({ "rawtypes" })
	public String getLayersAuthority(
			@RequestParam(defaultValue = "10") int size,
			@RequestParam(defaultValue = "1") int page, Model model,
			WebRequest webRequest) {

		if (LOG.isInfoEnabled()) {
			LOG.info(String.format("Consultando las capas de la autoridad. Tama\u00f1o de p\u00e1gina=%s. Pagina=%s", size, page));
		}

		isLogate(webRequest);

		String username = getCurrentUsername(webRequest);
		UsuarioDto userLogado = userAdminService
				.obtenerUsuarioByUsername(username);

		Long idAuth = userLogado.getAuthorityId();

		calculatePagination(model);

		List result = (List<com.emergya.ohiggins.dto.LayerDto>) layerService
				.getFromToOrderBy(0, Integer.MAX_VALUE,
						com.emergya.ohiggins.dto.LayerDto.NAME_PROPERTY, idAuth);

		model.addAttribute("layersAuthority", result);

		if (LOG.isInfoEnabled()) {
			LOG.info("Redirigiendo a la vista cartografico/layersAuthority");
		}

		rolUser(model);
		model.addAttribute(MODULE_KEY, getActiveModule());
		model.addAttribute(SUBMODULE_KEY, getActiveSubModule());

		return "cartografico/layersAuthority";
	}

	/**
	 * Elimina una capa
	 * 
	 * @param id
	 *            Identificador de la capa
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/cartografico/borrarLayer/{id}")
	public String deleteLayer(@PathVariable long id, Model model,
			WebRequest webRequest) {

		isLogate(webRequest);

		UsuarioDto currentUser = userAdminService
				.obtenerUsuarioByUsername(getCurrentUsername(webRequest));

		Long authorityId = currentUser.getAuthorityId();

		LayerDto layer = (LayerDto) layerService.getById(id);
		if(layer==null) {
		    // We can't remove the layer.
		    return "redirect:/cartografico/layersAuthority?msg=La capa no existe.";
		}
		if (layer.getAuthority() == null
				|| !layer.getAuthority().getId().equals(authorityId)) {
			// We can't remove the layer.
			return "redirect:/cartografico/layersAuthority?msg=No tiene permisos para borrar la capa.";
		}

		if (layer.getId() != null) {
			if (!geoserverService.deleteGeoServerLayer(
				layer.getAuthority().getWorkspaceName(),
				layer.getNameWithoutWorkspace(),				
				layer.getType().getNameAndTipo(),
				layer.getTableName())) {
				return requestReturn("redirect:/cartografico/layersAuthority",
						"Ocurrió un error al borrar la capa de GeoServer.");
			}

			layerService.delete(layer);
		}

		return "redirect:/cartografico/layersAuthority?msg=success";

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
	 * Muestra los metadatos de una capa
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
	@RequestMapping(value = "/visor-api/mostrarMetadatosDeCapa/{ident}", method = {
			RequestMethod.GET, RequestMethod.POST })
	public ModelAndView mostrarMetadatosDeCapa(WebRequest webRequest,
			HttpServletRequest request, Model model,
			HttpServletResponse response, @PathVariable("ident") String ident) {
		// Vista de retorno
		ModelAndView modelAndView = new ModelAndView(
				"admin/cartografico/metadatosOnLayer");
		// Obtenemos la Layer a la que pertenece la solicitud de publicacion
		LayerDto layer = (LayerDto) layerService.getById(Long.decode(ident));

		model.addAttribute("layerDto", layer);

		if (layer.getMetadata() == null) {
			model.addAttribute("noMetadata", true);
		}

		// model.addAttribute("layerType", layer.getType().getName());
		model.addAttribute("layerType", layer.getType().getTipo());
		return modelAndView;
	}

	/**
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
		model.addAttribute("layersAuthority", layersAuthority);
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
		return "cartografico/layersAuthority";
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
