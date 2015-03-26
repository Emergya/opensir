/*
 * LayerTreeController
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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.WebRequest;

import com.emergya.ohiggins.dto.LayerDto;
import com.emergya.ohiggins.dto.NodeLayerTree;
import com.emergya.ohiggins.dto.UsuarioDto;
import com.emergya.ohiggins.security.ConstantesPermisos;
import com.emergya.ohiggins.security.OhigginsUserDetails;
import com.emergya.ohiggins.service.FolderService;
import com.emergya.ohiggins.service.LayerService;
import com.emergya.ohiggins.service.LayerTypeService;
import com.emergya.ohiggins.service.UserAdminService;

@Controller
public class LayerTreeController extends AbstractController {

	private static final long serialVersionUID = -3301052158744970660L;
	private static Log LOG = LogFactory.getLog(LayerTreeController.class);

	public final static String MODULE = "cartografico";
	public final static String SUB_MODULE = "layerTree";
	public static final String LAYERTREE = "layerTree";

	private static final String CAMPO_OBLIGATORIO = "No puede estar vacio";
	private static final String TYPELAYERS = "typeLayers";

	private List<Object> layerTree = null;

	@Resource
	private LayerService layerService;

	@Resource
	private LayerTypeService layerTypeService;

	@Resource
	private UserAdminService userAdminService;

	@Resource
	private FolderService folderService;

	/**
	 * Arbol de capas
	 * 
	 * @param model
	 * 
	 * @return "layerTree
	 */
	@RequestMapping(value = "/admin/cartografico")
	@SuppressWarnings({ "rawtypes" })
	public String getLayertree(
			@RequestParam(defaultValue = "10000000") int size,
			@RequestParam(defaultValue = "1") int page, Model model,
			WebRequest webRequest) {

		// Accede el rol ADMIN, en otro caso pagina error
		decideIfIsUserInRole(ConstantesPermisos.ADMIN);

		String username = getCurrentUsername(webRequest);
		UsuarioDto userLogado = userAdminService
				.obtenerUsuarioByUsername(username);


		calculatePagination(model);
		// Arbol de capas
		// List<FolderDto> ll = folderService.getFolderParent();
		List layerTree = (List<NodeLayerTree>) folderService.getArbolCapas(
				(page - 1) * size, page * size, LayerDto.NAME_PROPERTY);

		model.addAttribute(LAYERTREE, layerTree);

		rolUser(model);
		model.addAttribute("module", getActiveModule());
		model.addAttribute("submodule", getActiveSubModule());

		return "admin/cartografico/layerTree";
	}
	
	/**
	 * Layer Tree printing.
	 */
	@RequestMapping(value = "/admin/cartografico/impresionArbolCapas")
	@SuppressWarnings({ "rawtypes" })
	public String printLayerTree(
			@RequestParam(defaultValue = "10000000") int size,
			@RequestParam(defaultValue = "1") int page, Model model,
			WebRequest webRequest) {

		// Accede el rol ADMIN, en otro caso pagina error
		decideIfIsUserInRole(ConstantesPermisos.ADMIN);

		String username = getCurrentUsername(webRequest);
		UsuarioDto userLogado = userAdminService
				.obtenerUsuarioByUsername(username);


		calculatePagination(model);
		// Arbol de capas
		// List<FolderDto> ll = folderService.getFolderParent();
		List layerTree = (List<NodeLayerTree>) folderService.getArbolCapas(
				(page - 1) * size, page * size, LayerDto.NAME_PROPERTY);

		model.addAttribute(LAYERTREE, layerTree);

		rolUser(model);

		return "admin/cartografico/layerTreePrinting";
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
		model.addAttribute(LAYERTREE, layerTree);
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
		return "/admin/cartografico/layerTree";
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
