/*
 * PermissionEntity.java
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
 * Authors:: Juan Luis Rodríguez Ponce (mailto:jlrodriguez@emergya.com)
 */
package com.emergya.ohiggins.model;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.emergya.persistenceGeo.metaModel.AbstractPermissionEntity;

/**
 * Entidad de permisos.
 * 
 * @author <a href="mailto:marcos@emergya.com">marcos</a>.
 * @author <a href="mailto:jlrodriguez@emergya.com>jlrodriguez</a>.
 * 
 */
@SuppressWarnings("unchecked")
@Entity
@Table(name = "gis_permission")
public class PermissionEntity extends AbstractPermissionEntity {

	/**
	 * Serial version UID.
	 */
	private static final long serialVersionUID = 8185264482816302475L;

	/**
	 * Constructor sin parámetros.
	 */
	public PermissionEntity() {
	}

	/**
	 * Construye un permiso a partir de su nombre.
	 * 
	 * @param permissionName
	 *            nombre del permiso.
	 */
	public PermissionEntity(final String permissionName) {
		name = permissionName;
	}

	/**
	 * Obtiene la clave primaria del objeto.
	 * 
	 * @return el id.
	 */
	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "permission_seq")
	@SequenceGenerator(name = "permission_seq", sequenceName = "permission_seq", initialValue = 100)
	public Long getId() {
		return id;
	}

	/**
	 * Devuelve la fecha de creación del registro en base de datos.
	 * 
	 * @return la fecha de creación.
	 */
	@Column(name = "create_date")
	public Date getCreateDate() {
		return createDate;
	}

	/**
	 * Devuelve la fecha de la última actualización realizada en el registro.
	 * 
	 * @return la fecha de la última actualización.
	 */
	@Column(name = "update_date")
	public Date getUpdateDate() {
		return updateDate;
	}

	/**
	 * Lista de tipos de entidad con el permiso.
	 * 
	 * @return la lista de tipos de entidad que tienen este permiso asignado.
	 */
	@ManyToMany(fetch = FetchType.LAZY, mappedBy = "permissionList")
	public List<AuthorityTypeEntity> getAuthTypeList() {
		return authTypeList;
	}

	@Column(name = "filter")
	public String getFilter() {
		return filter;
	}

	@Column(name = "name")
	public String getName() {
		return name;
	}

	@Column(name = "ptype")
	public String getPtype() {
		return ptype;
	}

	@Column(name = "config")
	public String getConfig() {
		return config;
	}
}
