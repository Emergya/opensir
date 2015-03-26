/*
 * OhigginsAuthentificationProvider.java
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

import java.io.Serializable;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.authority.GrantedAuthorityImpl;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import com.emergya.ohiggins.dto.AuthorityDto;
import com.emergya.ohiggins.dto.AuthorityTypeDto;
import com.emergya.ohiggins.dto.PermisoDto;
import com.emergya.ohiggins.dto.UsuarioDto;
import com.emergya.ohiggins.service.AuthorityTypeService;
import com.emergya.ohiggins.service.InstitucionService;
import com.emergya.ohiggins.service.UserAdminService;

/**
 * Authentification provider for Ohiggins Comprueba las credenciales del usuario
 * que intenta conectarse a ohiggins
 * 
 * @author <a href="mailto:jariera@emergya.com">jariera</a>
 * @version 1.0
 * 
 */
public class OhigginsAuthentificationProvider implements
		AuthenticationProvider, Serializable {

	private static final long serialVersionUID = 133875481838936541L;

	@Resource
	private UserAdminService userAdminService;

	@Resource
	private InstitucionService institucionService;

	@Resource
	private AuthorityTypeService tipoInstitucionService;

	/** Permiso admin */
	public static GrantedAuthority ADMIN_AUTH;
	/** Permiso no admin */
	public static GrantedAuthority ROLE_USER;
	private static final String ROLE_USER_GROUP = "user";

	@Autowired
	public void init(String NAME_ADMIN_GROUP) {
		ADMIN_AUTH = new SimpleGrantedAuthority(NAME_ADMIN_GROUP);// Admin
		ROLE_USER = new SimpleGrantedAuthority(ROLE_USER_GROUP);// resto
																// usuarios no
																// admin
	}

	/**
	 * Auntenticacion del usuario que intenta conectarse
	 * 
	 * @param Authentication
	 */
	@Override
	public Authentication authenticate(Authentication authentication)
			throws AuthenticationException {

		// autentica en UserDetailsImpl
		final UsernamePasswordAuthenticationToken userToken = (UsernamePasswordAuthenticationToken) authentication;

		UserDetails user = new UserDetailsImpl(authentication.getName(),
				(String) authentication.getCredentials());
		return createSuccessfulAuthentication(userToken, user);
	}

	/**
	 * Create los detalles del usuario si las credencia son correctas
	 */
	protected Authentication createSuccessfulAuthentication(
			UsernamePasswordAuthenticationToken authentication, UserDetails user) {
		Object password = authentication.getCredentials();

		UsernamePasswordAuthenticationToken result = new UsernamePasswordAuthenticationToken(
				user, password, user.getAuthorities());

		result.setDetails(authentication.getDetails());

		return result;
	}

	/**
	 * Clase interna que representa a un usuario de la aplicacion ohiggins
	 * 
	 * @author jariera
	 * @version 1.0
	 */
	private class UserDetailsImpl implements UserDetails {

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		private String username;
		private String password;
		Collection<GrantedAuthority> userAuths = null;

		/**
		 * Constructor
		 * 
		 * @param username
		 * @param password
		 */
		public UserDetailsImpl(String username, String password) {
			this.username = username;
			this.password = password;

			UsuarioDto usuario = userAdminService.obtenerUsuario(username,
					(String) password);

			userAuths = new HashSet<GrantedAuthority>();

			// Si el usuario es valido, añadimos los permisos necesarios a
			// spring security
			if (usuario != null && usuario.isValid()) {

				// Obtener institucion y tipo a la que pertenece para ver los
				// permisos que posee
				AuthorityDto institucion = (AuthorityDto) institucionService
						.getById(usuario.getAuthorityId());
				List<PermisoDto> permisosTipoInstitucion = new LinkedList<PermisoDto>();
				if (institucion.getType() != null
						&& institucion.getType().getId() != null) {
					AuthorityTypeDto tipoInstitucion = tipoInstitucionService
							.getById(institucion.getType().getId());
					// save institucion del usuario
					userAuths.add(new SimpleGrantedAuthority("institucion_" +
							tipoInstitucion.getId()));
					permisosTipoInstitucion = tipoInstitucion.getPermisos();
				}
				if (permisosTipoInstitucion != null
						&& permisosTipoInstitucion.size() > 0) {
					for (PermisoDto permisoInstitucion : permisosTipoInstitucion) {
						userAuths.add(new SimpleGrantedAuthority(
								permisoInstitucion.getNombre()));
					}
				}

				userAuths.add(ROLE_USER);

				if (usuario.isAdmin()) {
					// Add admin auth
					userAuths.add(ADMIN_AUTH);
				}
			}
		}

		@Override
		public boolean isEnabled() {
			return userAuths.contains(ROLE_USER);
		}

		@Override
		public boolean isCredentialsNonExpired() {
			return isEnabled();
		}

		@Override
		public boolean isAccountNonLocked() {
			return isEnabled();
		}

		@Override
		public boolean isAccountNonExpired() {
			return isEnabled();
		}

		@Override
		public String getUsername() {
			return username;
		}

		@Override
		public String getPassword() {
			return password;
		}

		@Override
		public Collection<? extends GrantedAuthority> getAuthorities() {
			return userAuths;
		}
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

	/**
	 * Método que restringe el acceso a un método si el usuario no posee los
	 * roles
	 * 
	 * @param roles
	 * @throws AccessDeniedException
	 *             is User principal doesn't exist or does not have any passed
	 *             roles
	 */
	protected boolean decideIfIsUserInRole(String... args)
			throws AccessDeniedException {
		UserDetailsImpl usuario = (UserDetailsImpl) getPrincipal();

		// List<GrantedAuthority> permisos = null;

		boolean permitido = false;

		if (usuario == null) {
			// throw new
			// AccessDeniedException("No tiene permiso para acceder a este servicio");
			return permitido;
		}
		// else{
		// //permisos = usuario.getAuthorities();
		// }

		if (usuario.getAuthorities() != null) {
			for (GrantedAuthority permiso : usuario.getAuthorities()) {
				for (String rol : args) {
					if (permiso.getAuthority().equals(rol)) {
						permitido = true;
					}
				}
			}
		}

		if (!permitido) {
			// throw new
			// AccessDeniedException("No tiene permiso para acceder a este servicio");
			return false;
		} else {
			return true;
		}
	}

	@Override
	public boolean supports(Class<?> authentication) {
		return false;
	}
}
