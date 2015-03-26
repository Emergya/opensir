/*
 * SecurityUtils.java
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

import java.util.List;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

/**
 * Clase de utilidades para spring security
 * 
 * @author jariera
 * @version 1.0
 */
public class SecurityUtils {

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
	 * Checks if is user in role.
	 * 
	 * @param role
	 *            the role
	 * @return true, if is user in role
	 */
	public static boolean isUserInRole(String role) {

		return userHasAuthority(role);
	}

	/**
	 * Obtiene el objeto principal almacenado en el contexto de spring security
	 * 
	 * @return UserDetails
	 */
	public static UserDetails getPrincipal() {
		UserDetails result = null;
		Object principal = null;

		if (SecurityContextHolder.getContext() != null
				&& SecurityContextHolder.getContext().getAuthentication() != null) {
			principal = SecurityContextHolder.getContext().getAuthentication()
					.getPrincipal();
		}
		if (principal instanceof UserDetails) {
			result = (UserDetails) SecurityContextHolder.getContext()
					.getAuthentication().getPrincipal();
		}

		return result;
	}

	/**
	 * Metodo para hacer el logout
	 */
	public static void logout() {
		// Spring Security
		if (SecurityContextHolder.getContext() != null) {
			SecurityContextHolder.clearContext();
		}
	}
}
