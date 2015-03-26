	/*
	* EspacioController.java
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
	 *
	 */
package com.emergya.ohiggins.web;

import java.io.InputStream;
import java.io.Serializable;
import java.util.List;
import java.util.Properties;

import javax.annotation.Resource;


import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.context.request.WebRequest;

import com.emergya.ohiggins.security.ConstantesPermisos;
import com.emergya.ohiggins.security.OhigginsUserDetails;
import com.emergya.ohiggins.service.InstitucionService;
import com.emergya.ohiggins.usedspace.dto.SpaceFilter;
import com.emergya.ohiggins.usedspace.dto.UsedSpaceDto;
import com.emergya.ohiggins.usedspace.service.UsedSpaceService;
import com.emergya.ohiggins.web.util.Utils;
import com.emergya.persistenceGeo.service.DBManagementService;
import com.emergya.persistenceGeo.service.LayerAdminService;

	
	@Controller
	public class EspacioController extends AbstractController implements
			Serializable {
		/** Serials */

		private static final long serialVersionUID = 6377332217873703600L;

		/** Log */
		private static Log LOG = LogFactory.getLog(EspacioController.class);

		/** Modulo al que pertenece */
		public final static String MODULE = "general";

		/** Submodulo al que pertenece */
		public final static String SUB_MODULE = "espacio";

		
		private SpaceFilter spaceFilter = new SpaceFilter();
		

		
		@Resource
		private LayerAdminService layerAdminService;
		@Resource
		private InstitucionService institucionService;
		@Resource
		private DBManagementService dbManagement;
		
		@Resource
		private UsedSpaceService usedSpaceService;
		
		/**
		 * Espacio usado
		 * 
		 * @param model
		 * 
		 * @return "espacio"
		 */
		@RequestMapping(value = "/admin/espacio")
		public String espacio(Model model) {

			// Accede el rol ADMIN, en otro caso pagina error
			decideIfIsUserInRole(ConstantesPermisos.ADMIN);

			rolUser(model);
			this.spaceFilter = new SpaceFilter();
			model.addAttribute("spaceFilter", this.spaceFilter);
			populateAuthorities(model);

			return "admin/espacio/espacio";
		}

		private void populateAuthorities(Model model) {
			model.addAttribute("module", getActiveModule());
			model.addAttribute("submodule", getActiveSubModule());
			model.addAttribute("instituciones",institucionService.getAllOrderedByName());
		}
		
		
		//TODO: Eliminar
		@RequestMapping(value = "/admin/espacio/{authId}")
		public String espacioInstitucion(@PathVariable long authId, Model model) {
			
			// Accede el rol ADMIN, en otro caso pagina error
			decideIfIsUserInRole(ConstantesPermisos.ADMIN);
		
			List<UsedSpaceDto> capasInstitucion = usedSpaceService.getLayersSpaceByAuthority(authId);
			model.addAttribute("capasInstitucion", capasInstitucion);

			populateAuthorities(model);
			
			this.spaceFilter.setAuthorityId(authId);
			model.addAttribute("spaceFilter", this.spaceFilter);

			return "admin/espacio/espacio";
		}
		
		
		
		@RequestMapping(value = "/admin/espacio/busqueda")
		public String busquedaInstitucion(SpaceFilter spaceFilter,
				BindingResult result, Model model, WebRequest request) {
			
			// Accede el rol ADMIN, en otro caso pagina error
			decideIfIsUserInRole(ConstantesPermisos.ADMIN);
			
			long authId = spaceFilter.getAuthorityId();
		
			List<UsedSpaceDto> capasInstitucion = usedSpaceService.getLayersSpaceByAuthority(authId);
			model.addAttribute("capasInstitucion", capasInstitucion);

		
			populateAuthorities(model);
			
			this.spaceFilter.setAuthorityId(authId);
			model.addAttribute("spaceFilter", spaceFilter);


			return "admin/espacio/espacio";
		}
		


	

		protected void copyDefaultModel(boolean update, Model model) {

		}

		/**
		 * Referencia al enlace a la paginacion de instituciones
		 * 
		 * @return 'admin/espacio'
		 */
		@Override
		protected String getDefaultPaginationUrl() {
			return "/admin/espacio";
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
		 * Obtener el fichero de propiedades
		 * 
		 * @return
		 */
		public static Properties getPropertiesApplication() {
			Properties p = new Properties();

			try {
				InputStream inStream = Utils.class
						.getResourceAsStream("/application.properties");
				p.load(inStream);
			} catch (Exception e) {
				LOG.info("Error al obtener las propiedades  : " + e);
			}

			return p;
		}

	}
