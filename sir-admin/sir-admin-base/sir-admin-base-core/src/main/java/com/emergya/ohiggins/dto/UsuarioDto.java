/*
 * UsuarioDto.java
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
 * Authors:: Alejandro Díaz Torres (mailto:adiaz@emergya.com)
 */
package com.emergya.ohiggins.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Transient;
import javax.validation.constraints.AssertTrue;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotBlank;

import com.emergya.ohiggins.dto.validations.EditUserValidation;
import com.emergya.ohiggins.dto.validations.NewUserValidation;

/**
 * Dto de usuario
 * 
 * @author <a href="mailto:adiaz@emergya.com">adiaz</a>
 * 
 */
public class UsuarioDto implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6919295259367632033L;

	public static final String USERNAME_PROPERTY = "username";

	private Long id;
	private String username;
	private String nombreCompleto;
	private String password;
	private String apellidos;
	private String email;
	private String telefono;
	private boolean admin;
	private boolean valid;
	private Date fechaCreacion;
	private Date fechaActualizacion;
	private GrupoUsuariosDto authority;
	private Long authorityId;
	private AuthorityDto auth;
	private RegionDto region;
	private Long regionId;

	@Transient
	private String confirmPassword;

	/**
	 * @return the confirmPassword
	 */
	public String getConfirmPassword() {
		return confirmPassword;
	}

	/**
	 * @param confirmPassword
	 *            the confirmPassword to set
	 */
	public void setConfirmPassword(String confirmPassword) {
		this.confirmPassword = confirmPassword;
	}

	@NotBlank(message = "{com.emergya.ohiggins.validation.required}", groups = EditUserValidation.class)
	public String getApellidos() {
		return apellidos;
	}

	@Email(message = "{com.emergya.ohiggins.validation.email}", groups = EditUserValidation.class)
	@NotBlank(message = "{com.emergya.ohiggins.validation.required}", groups = EditUserValidation.class)
	public String getEmail() {
		return email;
	}

	@NotBlank(message = "{com.emergya.ohiggins.validation.required}", groups = EditUserValidation.class)
	public String getNombreCompleto() {
		return nombreCompleto;
	}

	@Pattern(regexp = "^[0-9a-zA-Z]{6,}$", message = "{com.emergya.ohiggins.validation.alphanumeric6}", groups = NewUserValidation.class)
	public String getPassword() {
		return password;
	}

	@Pattern(regexp = "[\\d\\s]+", message = "{com.emergya.ohiggins.validation.fono}", groups = NewUserValidation.class)
	public String getTelefono() {
		return telefono;
	}

	@NotBlank(message = "{com.emergya.ohiggins.validation.required}", groups = NewUserValidation.class)
	@Size(min = 4, max = 32, groups = NewUserValidation.class)
	public String getUsername() {
		return username;
	}

	@NotNull(message = "{com.emergya.ohiggins.validation.required}", groups = NewUserValidation.class)
	public boolean isAdmin() {
		return admin;
	}

	@NotNull(message = "{com.emergya.ohiggins.validation.required}", groups = NewUserValidation.class)
	public boolean isValid() {
		return valid;
	}

	public Date getFechaActualizacion() {
		return fechaActualizacion;
	}

	public Date getFechaCreacion() {
		return fechaCreacion;
	}

	public Long getId() {
		return id;
	}

	public void setAdmin(boolean admin) {
		this.admin = admin;
	}

	public void setApellidos(String apellidos) {
		this.apellidos = apellidos;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public void setFechaActualizacion(Date fechaActualizacion) {
		this.fechaActualizacion = fechaActualizacion;
	}

	public void setFechaCreacion(Date fechaCreacion) {
		this.fechaCreacion = fechaCreacion;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public void setNombreCompleto(String nombreCompleto) {
		this.nombreCompleto = nombreCompleto;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public void setTelefono(String telefono) {
		this.telefono = telefono;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public void setValid(boolean valid) {
		this.valid = valid;
	}

	public GrupoUsuariosDto getAuthority() {
		return authority;
	}

	public void setAuthority(GrupoUsuariosDto authority) {
		this.authority = authority;
	}

	// @NotNull(message = "{com.emergya.ohiggins.validation.required}", groups =
	// NewUserValidation.class)
	public Long getAuthorityId() {
		return authorityId;
	}

	public void setAuthorityId(Long long1) {
		this.authorityId = long1;
	}

	@AssertTrue(message = "{com.emergya.ohiggins.validation.equalsTo}", groups = EditUserValidation.class)
	public boolean getMatchPassword() {
		if (password == null) {
			if (confirmPassword != null)
				return false;
		} else if (!password.equals(confirmPassword))
			return false;
		return true;
	}

	public AuthorityDto getAuth() {
		return auth;
	}

	public void setAuth(AuthorityDto auth) {
		this.auth = auth;
	}

	public RegionDto getRegion() {
		return region;
	}

	public void setRegion(RegionDto region) {
		this.region = region;
	}

	public Long getRegionId() {
		return regionId;
	}

	public void setRegionId(Long regionId) {
		this.regionId = regionId;
	}

}