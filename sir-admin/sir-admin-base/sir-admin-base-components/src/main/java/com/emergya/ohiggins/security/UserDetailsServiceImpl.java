/*
 * UserDetailsServiceImpl.java
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
package com.emergya.ohiggins.security;

import java.util.LinkedList;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.emergya.ohiggins.dto.AuthorityDto;
import com.emergya.ohiggins.dto.AuthorityTypeDto;
import com.emergya.ohiggins.dto.PermisoDto;
import com.emergya.ohiggins.dto.UsuarioDto;
import com.emergya.ohiggins.service.AuthorityTypeService;
import com.emergya.ohiggins.service.InstitucionService;
import com.emergya.ohiggins.service.UserAdminService;
import com.emergya.persistenceGeo.dto.ToolPermissionDto;
import com.emergya.persistenceGeo.service.ToolPermissionService;

@Service(value = "userDetailsService")
public class UserDetailsServiceImpl implements UserDetailsService {
	/** Logger */
	protected static Logger logger = Logger
			.getLogger(UserDetailsServiceImpl.class);

	@Autowired
	private UserAdminService userAdminService;

	@Autowired
	private InstitucionService institucionService;

	@Autowired
	private AuthorityTypeService tipoInstitucionService;

	@Autowired
	private ToolPermissionService toolPermissionService;

	public UserAdminService getUserAdminService() {
		return userAdminService;
	}

	public void setUserAdminService(UserAdminService userAdminService) {
		this.userAdminService = userAdminService;
	}

	/** Permiso admin */
	public static GrantedAuthority ADMIN_AUTH;
	/** Permiso no admin */
	public static GrantedAuthority ROLE_USER;
	private static final String ROLE_USER_GROUP = ConstantesPermisos.USER;// "user";

	@Autowired
	@Qualifier("NAME_ADMIN_GROUP")
	public void init(String NAME_ADMIN_GROUP) {
		ADMIN_AUTH = new SimpleGrantedAuthority(NAME_ADMIN_GROUP);// Admin
		ROLE_USER = new SimpleGrantedAuthority(ROLE_USER_GROUP);// resto
																// usuarios no
																// admin
	}

	/**
	 * Obtenemos el usuario
	 */
	public UserDetails loadUserByUsername(String username)
			throws UsernameNotFoundException {

		logger.debug("Entrando en UserDetailsServiceImpl.loadUserByUsername");
		UsuarioDto user = null;
		logger.debug("valor del username " + username);
		try {
			user = userAdminService.obtenerUsuarioByUsername(username);
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
		if (user == null) {
			logger.info("Usuario no encontrado: " + username);
			throw new UsernameNotFoundException("User not found: " + username);
		} else {
			logger.debug("saliendo en UserDetailsServiceImpl.loadUserByUsername");
			return makeUser(user);
		}
	}

	/**
	 * Guardamos el usuario en security spring
	 * 
	 * @param user
	 * @return
	 */
	private org.springframework.security.core.userdetails.User makeUser(
			UsuarioDto user) {
		// Obtener institucion y tipo a la que pertenece para ver los permisos
		// que posee
		AuthorityDto institucion = null;
		if (user.getAuthorityId() != null) {
			institucion = (AuthorityDto) institucionService.getById(user
					.getAuthorityId());
		}
		List<PermisoDto> permisos = new LinkedList<PermisoDto>();

		if (institucion != null && institucion.getType() != null
				&& institucion.getType().getId() != null) {
			AuthorityTypeDto tipoInstitucion = tipoInstitucionService
					.getById(institucion.getType().getId());
			permisos = tipoInstitucion.getPermisos();
		} else {
			for (ToolPermissionDto permission : toolPermissionService
					.getPermissionsByUser(user.getId())) {
				permisos.add(new PermisoDto(permission));
			}
		}

		return new OhigginsUserDetails(user, permisos);
	}
}
