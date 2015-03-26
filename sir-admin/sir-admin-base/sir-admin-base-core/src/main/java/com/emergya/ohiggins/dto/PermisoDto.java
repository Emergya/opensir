/*
 * PermisoDto.java
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
 * Authors:: Jose Alfonso Riera (mailto:jariera@emergya.com)
 */
package com.emergya.ohiggins.dto;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.ArrayUtils;

import com.emergya.persistenceGeo.dto.ToolPermissionDto;

/**
 * Dto de permiso
 * 
 * @author <a href="mailto:jariera@emergya.com">jariera</a>
 * 
 */
public class PermisoDto extends ToolPermissionDto implements Serializable {

	private static final long serialVersionUID = -6892520920184358000L;

	private String[] seleccionados;

	/** Lista de tipos de instituciones */
	private List<AuthorityTypeDto> authoritiesTypes;
	
	public PermisoDto(){
		super();
	}
	
	public PermisoDto(ToolPermissionDto toolPermissionDto){
		super();
		this.setId((Long) toolPermissionDto.getId());
		this.setName(toolPermissionDto.getName());
		this.setPtype(toolPermissionDto.getPtype());
		this.setConfig(toolPermissionDto.getConfig());
		this.setUpdateDate(toolPermissionDto.getUpdateDate());
		this.setCreateDate(toolPermissionDto.getCreateDate());
		this.setFilter(toolPermissionDto.getFilter());
	}

	public String getNombre() {
		return name;
	}

	public void setNombre(String nombre) {
		this.name = nombre;
	}

	public Date getFechaCreacion() {
		return createDate;
	}

	public void setFechaCreacion(Date fechaCreacion) {
		this.createDate = fechaCreacion;
	}

	public Date getFechaActualizacion() {
		return updateDate;
	}

	public void setFechaActualizacion(Date fechaActualizacion) {
		this.updateDate = fechaActualizacion;
	}

	public List<AuthorityTypeDto> getAuthoritiesTypes() {
		return authoritiesTypes;
	}

	public void setAuthoritiesTypes(List<AuthorityTypeDto> authoritiesTypes) {
		this.authoritiesTypes = authoritiesTypes;
	}

	public String[] getSeleccionados() {
		return seleccionados;
	}

	public void setSeleccionados(String[] seleccionados) {
		this.seleccionados = ArrayUtils.clone(seleccionados);
	}

}
