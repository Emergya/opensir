/*
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
 * Authors:: María Arias de Reyna (mailto:delawen@gmail.com)
 */
package com.emergya.ohiggins.dto;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import com.vividsolutions.jts.geom.Geometry;

public class NivelTerritorialDto implements Serializable {
	private static final long serialVersionUID = 6902295259384139031L;

	private Integer id;
	private String name;
	private String codigo_territorio;
	private String tipo_ambito;
	private Geometry extension;
	private Date fecha_creacion;
	private Date fecha_actualizacion;
	private List<AuthorityDto> authorities;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return getName() != null ? getName().toString() : null;
	}

	public String getCodigo_territorio() {
		return codigo_territorio;
	}

	public void setCodigo_territorio(String codigo_territorio) {
		this.codigo_territorio = codigo_territorio;
	}

	public String getTipo_ambito() {
		return tipo_ambito;
	}

	public void setTipo_ambito(String tipo_ambito) {
		this.tipo_ambito = tipo_ambito;
	}

	public Geometry getExtension() {
		return extension;
	}

	public void setExtension(Geometry extension) {
		this.extension = extension;
	}

	public Date getFecha_creacion() {
		return fecha_creacion;
	}

	public void setFecha_creacion(Date fecha_creacion) {
		this.fecha_creacion = fecha_creacion;
	}

	public Date getFecha_actualizacion() {
		return fecha_actualizacion;
	}

	public void setFecha_actualizacion(Date fecha_actualizacion) {
		this.fecha_actualizacion = fecha_actualizacion;
	}

	public List<AuthorityDto> getAuthorities() {
		return authorities;
	}

	public void setAuthorities(List<AuthorityDto> authorities) {
		this.authorities = authorities;
	}

}
