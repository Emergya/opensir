/*
 * LoginController.java
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

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.WebRequest;

import com.emergya.ohiggins.dto.UsuarioDto;
import com.emergya.ohiggins.security.ConstantesPermisos;
import com.emergya.ohiggins.security.OhigginsUserDetails;
import com.emergya.ohiggins.service.RegionService;
import com.emergya.ohiggins.service.UserAdminService;
import com.emergya.ohiggins.web.bean.RegionBean;

/**
 * Controlador para comprobar que usuario se conecta y redireccionar a una u
 * otra pagina en funcion
 * 
 * @author jariera
 * 
 */
@Controller
public class LoginController {

	private static Log LOG = LogFactory.getLog(LoginController.class);

	@Resource
	private UserDetailsService userDetailsService;

	@Resource
	private UserAdminService userAdminService;

	@Resource
	private RegionService regionService;

	@Autowired
	private RegionBean region;

	@RequestMapping(value = "/controlUsuarioLogado")
	public String controlUsuario(Model model, WebRequest webRequest,
			HttpServletRequest request) {

		String r = new String();

		String username = getCurrentUsername(webRequest);
		UsuarioDto userLogado = userAdminService
				.obtenerUsuarioByUsername(username);

		if (userLogado != null && userLogado.getRegion() != null) {
			region.loadRegion(regionService.getById(userLogado.getRegion()
					.getId()));

		}

		if (LOG.isInfoEnabled()) {
			LOG.info("Region asociada al usuario logado= "
					+ region.getName_region());
		}

		if (OhigginsUserDetails.userHasAuthority(ConstantesPermisos.ADMIN)) {
			r = "redirect:/admin/cartografico/solicitudesPublicacion";
		} else {
			r = "redirect:/cartografico/solicitudesPublicacion";
		}

		return r;
	}

	@RequestMapping(value = "/loginFromChileIndica", method = RequestMethod.POST)
	public String loginFromChileIndica(
			@RequestParam(value = "username", required = true) String username) {
		OhigginsUserDetails multiSirUser = (OhigginsUserDetails) userDetailsService
				.loadUserByUsername(username);

		if (multiSirUser != null) {
			return "Usuario logado";
		} else {
			return "Usuario no dado de alta en el multi-SIR";
		}
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
}
