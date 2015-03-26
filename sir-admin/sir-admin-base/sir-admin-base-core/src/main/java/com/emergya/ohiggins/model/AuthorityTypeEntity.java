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
 * Authors:: Jose Alfonso (mailto:jariera@emergya.com)
 * Authors:: Juan Luis Rodríguez Ponce (mailto:jlrodriguez@emergya.com)
 */
package com.emergya.ohiggins.model;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.emergya.persistenceGeo.metaModel.AbstractAuthorityTypeEntity;

/**
 * Tipo de entidad.
 * 
 * @author <a href="mailto:jariera@emergya.com">jariera</a>
 * @author <a href="mailto:jlrodriguez@emergya.com">jlrodriguez</a>
 * 
 */
@Entity
@Table(name = "gis_authority_type")
public class AuthorityTypeEntity extends AbstractAuthorityTypeEntity {

	public static final Long SERVICIO_PUBLICO_ID = 2l;
	public static enum Names {
		ID("id"), NAME("name"), CITIZEN("citizen"), CREATE_DATE("createDate"), UPDATE_DATE(
				"updateDate"), AUTH_LIST("authList"), PERMISSION_LIST(
				"permissionList");
		
		/**
		 * Nombre de la propiedad.
		 */
		private final String name;

		/**
		 * Construye el enumerado con el nombre proporcionado.
		 * 
		 * @param nameParam
		 *            nombre del campo a construir.
		 */
		private Names(final String nameParam) {
			this.name = nameParam;
		}

		/**
		 * Consulta {@link Object#toString()}.
		 * 
		 * @return Una representación del objeto.
		 */
		@Override
		public String toString() {
			return this.name;
		}

	};

	/**
	 * Serial version UID.
	 */
	private static final long serialVersionUID = -1193101419664405344L;

	/**
	 * Indica si el tipo de entidad es ciudadano.
	 */
	private Boolean citizen;

	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "gis_authority_type_seq")
	@SequenceGenerator(name = "gis_authority_type_seq", sequenceName = "gis_authority_type_seq", initialValue = 100)
	@Override
	public Long getId() {
		return id;
	}

	@Override
	public void setId(final Serializable id) {
		this.id = (Long) id;
	}

	@Column(name = "name_auth_type", nullable = false)
	@Override
	public String getName() {
		return name;
	}

	@Column(name = "create_date")
	@Override
	public Date getCreateDate() {
		return createDate;
	}

	@Column(name = "update_date")
	@Override
	public Date getUpdateDate() {
		return updateDate;
	}

	/**
	 * Indica si es ciudadano o no.
	 * 
	 * @return si es ciudadano o no.
	 */
	@Column(name = "citizen")
	public Boolean isCitizen() {
		return citizen;
	}

	/**
	 * Set the citizen property.
	 * 
	 * @param citizenParam
	 *            the citizen.
	 */
	public void setCitizen(final Boolean citizenParam) {
		this.citizen = citizenParam;
	}

	/**
	 * Obtiene la lista de autoridades que tienen este tipo de autoridad.
	 * 
	 * @return la lista de autoridades del tipo.
	 */
	@SuppressWarnings("unchecked")
	@OneToMany(mappedBy = "authType", cascade = { CascadeType.PERSIST,
			CascadeType.MERGE })
	public List<AuthorityEntity> getAuthList() {
		return this.authList;
	}

	/**
	 * Devuelve la lista de permisos asociados al tipo de autoridad.
	 * 
	 * @return permisos asociados al tipo de autoridad.
	 */
	@SuppressWarnings("unchecked")
	@ManyToMany(targetEntity = PermissionEntity.class, cascade = {
			CascadeType.PERSIST, CascadeType.MERGE })
	@JoinTable(name = "gis_permission_by_authType", joinColumns = @JoinColumn(name = "auth_type_id"), inverseJoinColumns = @JoinColumn(name = "permission_id"))
	@Override
	public List<PermissionEntity> getPermissionList() {
		return permissionList;
	}
}
