/*
 * OhigginsUserDetails.java
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

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.authority.GrantedAuthorityImpl;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;

import com.emergya.ohiggins.dto.PermisoDto;
import com.emergya.ohiggins.dto.UsuarioDto;

/**
 * Usuario representado en la sesion de spring security
 * 
 * @author jariera
 * @version 1.0
 */
public class OhigginsUserDetails extends User {

	/** Serial UID */
	private static final long serialVersionUID = -6756082819779550L;

	/** Permiso admin */
	public static GrantedAuthority ADMIN_AUTH;

	/** Permiso no admin */
	public static GrantedAuthority ROLE_USER;

	private static final String ROLE_USER_GROUP = ConstantesPermisos.USER;// "user";
	private static final String NAME_ADMIN_GROUP = ConstantesPermisos.ADMIN;// "admin";

	private final int codigo;
	private final String nombre;
	private final String apellidos;
	private final Long group;

	// Se pueden añadir mas propiedades que hagan falta

	public Long getGroup() {
		return group;
	}

	@Autowired
	public void init(String NAME_ADMIN_GROUP) {
		ADMIN_AUTH = new SimpleGrantedAuthority(NAME_ADMIN_GROUP);// Admin
		ROLE_USER = new SimpleGrantedAuthority(ROLE_USER_GROUP);// resto
																// usuarios no
																// admin
	}

	/**
	 * Constructor
	 * 
	 * @param usuario
	 * @param institucion
	 */
	public OhigginsUserDetails(UsuarioDto usuario, List<PermisoDto> permisos) {
		super(usuario.getUsername(), usuario.getPassword(), usuario.isValid(), // enable
				usuario.isValid(), // account not expired
				usuario.isValid(), // credentials not expired
				usuario.isValid(), // account not locket
				toAuthorities(usuario, permisos));// permisos del usuario

		this.codigo = usuario.getId().intValue();
		this.nombre = usuario.getNombreCompleto();
		this.apellidos = usuario.getApellidos();
		this.group = usuario.getAuthorityId();

	}

	/**
	 * Permisos en security spring
	 * 
	 * @param permisos
	 * @return
	 */
	private static List<GrantedAuthority> toAuthorities(UsuarioDto usuario,
			List<PermisoDto> permisos) {
		List<GrantedAuthority> auths = new ArrayList<GrantedAuthority>();

		ADMIN_AUTH = new SimpleGrantedAuthority(NAME_ADMIN_GROUP);// Admin
		ROLE_USER = new SimpleGrantedAuthority(ROLE_USER_GROUP);// user

		if (permisos != null && permisos.size() > 0) {
			for (PermisoDto permiso : permisos) {
				auths.add(new GrantedAuthorityImpl(permiso.getNombre()));
			}
		}

		auths.add(ROLE_USER);
		if (usuario.isAdmin()) {
			auths.add(ADMIN_AUTH);
		}

		return auths;
	}

	public int getCodigo() {
		return codigo;
	}

	public String getNombre() {
		return nombre;
	}

	public String getApellidos() {
		return apellidos;
	}

	/**
	 * Returns true if the current user has the specified authority.
	 * 
	 * @param authority
	 *            the authority to test for (e.g. "ROLE_A").
	 * @return true if a GrantedAuthority object with the same string
	 *         representation as the supplied authority name exists in the
	 *         current user's list of authorities. False otherwise, or if the
	 *         user in not authenticated.
	 */
	public static boolean userHasAuthority(String authority) {
		List<GrantedAuthority> authorities = getUserAuthorities();

		for (GrantedAuthority grantedAuthority : authorities) {
			if (authority.equals(grantedAuthority.getAuthority())) {
				return true;
			}
		}

		return false;
	}

	/**
	 * Returns the authorities of the current user.
	 * 
	 * @return an array containing the current user's authorities (or an empty
	 *         array if not authenticated), never null.
	 */
	private static List<GrantedAuthority> getUserAuthorities() {
		Authentication auth = SecurityContextHolder.getContext()
				.getAuthentication();

		if (auth == null || auth.getAuthorities() == null) {
			return AuthorityUtils.NO_AUTHORITIES;
		}

		return (List<GrantedAuthority>) auth.getAuthorities();
	}

}
