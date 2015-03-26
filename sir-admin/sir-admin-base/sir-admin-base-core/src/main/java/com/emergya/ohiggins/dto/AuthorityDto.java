/*
 * AuthorityDto.java
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
 * Authors:: Jose Alfonso (mailto:jariera@emergya.com)
 */
package com.emergya.ohiggins.dto;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotBlank;

public class AuthorityDto implements Serializable {
	/**
		 * 
		 */
	private static final long serialVersionUID = 6912295259367639031L;

	private Long id;
	private String authority;
	private List<UsuarioDto> people;
	private Date fechaCreacion;
	private Date fechaActualizacion;
	private AuthorityTypeDto type;
	private NivelTerritorialDto nivelTerritorial;
	// Tipo de entidad que se selecciona
	private String tipoSeleccionado;
	// Ambito que se ha seleccionado
	private String ambitoSeleccionado;
	// Total de usuarios de la institucion
	private int totalUser;

	private String workspaceName;
	
	private RegionDto region;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@NotBlank
	@NotNull
	//@Size(min = 3, max = 50)
	public String getAuthority() {
		return authority;
	}

	public void setAuthority(String authority) {
		this.authority = authority;
	}

	public List<UsuarioDto> getPeople() {
		return people;
	}

	public void setPeople(List<UsuarioDto> people) {
		this.people = people;
	}

	public Date getFechaCreacion() {
		return fechaCreacion;
	}

	public void setFechaCreacion(Date fechaCreacion) {
		this.fechaCreacion = fechaCreacion;
	}

	public Date getFechaActualizacion() {
		return fechaActualizacion;
	}

	public void setFechaActualizacion(Date fechaActualizacion) {
		this.fechaActualizacion = fechaActualizacion;
	}

	public AuthorityTypeDto getType() {
		return type;
	}

	public void setType(AuthorityTypeDto type) {
		this.type = type;
	}

	public NivelTerritorialDto getNivelTerritorial() {
		return nivelTerritorial;
	}

	public void setNivelTerritorial(NivelTerritorialDto nivelTerritorial) {
		this.nivelTerritorial = nivelTerritorial;
	}

	public String getTipoSeleccionado() {
		return tipoSeleccionado;
	}

	public void setTipoSeleccionado(String tipoSeleccionado) {
		this.tipoSeleccionado = tipoSeleccionado;
	}

	public String getAmbitoSeleccionado() {
		return ambitoSeleccionado;
	}

	public void setAmbitoSeleccionado(String ambitoSeleccionado) {
		this.ambitoSeleccionado = ambitoSeleccionado;
	}

	public int getTotalUser() {
		return totalUser;
	}

	public void setTotalUser(int totalUser) {
		this.totalUser = totalUser;
	}

	/**
	 * @param workspaceName
	 */
	public void setWorkspaceName(String workspaceName) {
		this.workspaceName = workspaceName;
	}

	/**
	 * @return the workspaceName
	 */
	public String getWorkspaceName() {
		return workspaceName;
	}
	
	public RegionDto getRegion() {
		return region;
	}

	public void setRegion(RegionDto region) {
		this.region = region;
	}

}
