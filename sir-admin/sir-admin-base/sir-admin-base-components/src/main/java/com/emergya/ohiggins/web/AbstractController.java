/*
 * AbstractController.java
 * 
 * Copyright (C) 2011
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
 * Authors:: Alejandro Díaz Torres (mailto:adiaz@emergya.com)
 */
package com.emergya.ohiggins.web;

import java.io.IOException;
import java.io.Serializable;
import java.net.URLEncoder;
import java.util.Collection;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.ui.Model;
import org.springframework.util.ClassUtils;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.WebRequest;

import com.emergya.ohiggins.security.OhigginsUserDetails;
import com.emergya.ohiggins.security.SecurityUtils;

/**
 * Clase abstracta a extender por los controles de la aplicacion
 * 
 * @author <a href="mailto:adiaz@emergya.com">adiaz</a>
 */
public abstract class AbstractController implements Serializable {

	protected final static String MODULE_KEY = "module";
	protected final static String SUBMODULE_KEY = "submodule";
	public final static String ADMIN = "admin";

	/**
	 * 
	 */
	private static final long serialVersionUID = 2248980333660013583L;

	/**
	 * Incluye en el modelo los parametros por defecto de la administracion de
	 * grupos
	 * 
	 * @param model
	 * @param update
	 *            si actualiza los parametros actualizables
	 */
	protected abstract void copyDefaultModel(boolean update, Model model);

	/**
	 * @return subtabs para mostrar en formato 'tab1:dest1,tab2:dest2'
	 */
	protected abstract String getAllSubTabs();

	/**
	 * @return SubTab seleccionada
	 */
	protected abstract int getSelectedSubTab();

	/**
	 * @return pagina por defecto para la paginacion en el formato
	 *         url/first/last
	 * @see WB-INF/jsp/decaorators/footerPagination.jsp
	 */
	protected abstract String getDefaultPaginationUrl();

	/**
	 * Modifica el modelo para incluir los parametros comunes: pestanas,
	 * paginacion...
	 * 
	 * @param model
	 */
	protected void calculatePagination(Model model) {
		model.addAttribute("paginationUrl", getDefaultPaginationUrl());
		model.addAttribute("allSubTabs", getAllSubTabs());
		model.addAttribute("selectedSubTab", getSelectedSubTab());
		model.addAttribute(MODULE_KEY, getActiveModule());
		model.addAttribute(SUBMODULE_KEY, getActiveSubModule());
	}

	/**
	 * 
	 * @return el identificador del modulo activo.
	 */
	protected abstract String getActiveModule();

	/**
	 * 
	 * @return el identificador del submodulo activo.
	 */
	protected abstract String getActiveSubModule();

	/**
	 * Copia el error al attribute error del modelo
	 * 
	 * @param error
	 * @param e
	 * 
	 * @param model
	 */
	public void errorToModel(String error, Exception e, Model model) {
		String message = error + ".";
		if (e != null) {
			message += "\n Causa: " + e.getMessage();
		}
		model.addAttribute("error", message);
	}

	/**
	 * Copia el error al attribute error del modelo
	 * 
	 * @param error
	 * 
	 * @param model
	 */
	public void errorToModel(String error, Model model) {
		errorToModel(error, null, model);
	}

	/**
	 * Copia el mensaje al atributo info del modelo
	 * 
	 * @param message
	 * @param model
	 */
	public void infoToModel(String message, Model model) {
		model.addAttribute("info", message);
	}

	/**
	 * Incluye en el modelo los parametros por defecto de la administracion de
	 * grupos
	 * 
	 * @param model
	 */
	protected void copyDefaultModel(Model model) {
		copyDefaultModel(false, model);
	}

	@ExceptionHandler(IOException.class)
	public String handleIOException(IOException ex, HttpServletRequest request) {
		return ClassUtils.getShortName(ex.getClass());
	}

	/**
	 * Método que restringe el acceso a un método si el usuario no posee los
	 * roles
	 * 
	 * @param roles
	 * @throws AccessDeniedException
	 *             is User principal doesn't exist or does not have any passed
	 *             roles
	 */
	protected void decideIfIsUserInRole(String... args)
			throws AccessDeniedException {
		OhigginsUserDetails usuario = (OhigginsUserDetails) SecurityUtils
				.getPrincipal();

		Collection<GrantedAuthority> permisos = null;

		boolean permitido = false;

		if (usuario == null) {
			throw new AccessDeniedException(
					"No tiene permiso para acceder a este servicio");
		} else {
			permisos = usuario.getAuthorities();
		}

		for (GrantedAuthority permiso : permisos) {
			for (String rol : args) {
				if (permiso.getAuthority().equals(rol)) {
					permitido = true;
				}
			}
		}

		if (!permitido) {
			throw new AccessDeniedException(
					"No tiene permiso para acceder a este servicio");
		}
	}

	/**
	 * Comprueba si existe un usuario logado en el sistema
	 * 
	 * @param webRequest
	 */
	protected void isLogate(WebRequest webRequest) {
		String usuario = getCurrentUsernameLogado(webRequest);
		if (usuario == null)
			throw new AccessDeniedException("No esta logado en el sistema");
	}

	/**
	 * Usuario logado del contexto de seguridad
	 * 
	 * @param webRequest
	 * @return
	 */
	private String getCurrentUsernameLogado(WebRequest webRequest) {
		SecurityContext context = (SecurityContext) webRequest.getAttribute(
				"SPRING_SECURITY_CONTEXT", RequestAttributes.SCOPE_SESSION);

		if (context != null && context.getAuthentication().isAuthenticated()
				&& !context.getAuthentication().getAuthorities().isEmpty()) {
			return context.getAuthentication().getName();
		}
		return null;
	}
	
	@SuppressWarnings("deprecation")
	protected String requestReturn(String page, String msg) {
		if(StringUtils.isEmpty(msg)) {
			return page;
		}		
		return page+"?msg="+URLEncoder.encode(msg);
	}
}
