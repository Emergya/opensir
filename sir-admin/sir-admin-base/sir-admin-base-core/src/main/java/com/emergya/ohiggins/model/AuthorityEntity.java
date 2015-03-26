/*
 * AuthorityEntity.java
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
 * Authors:: Alejandro Díaz Torres (mailto:adiaz@emergya.com)
 * Authors:: Juan Luis Rodríguez Ponce (mailto:jlrodriguez@emergya.com)
 */
package com.emergya.ohiggins.model;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import com.emergya.persistenceGeo.metaModel.AbstractAuthorityEntity;

/**
 * Entidad de grupo de usuarios.
 * 
 * @author <a href="mailto:adiaz@emergya.com">adiaz</a>.
 * @author <a href="mailto:jlrodriguez@emergya.com">jlrodriguez</a>.
 * 
 */
@Entity
@Table(name = "gis_authority")
public class AuthorityEntity extends AbstractAuthorityEntity {

	/** Nombres de campos de la clase AuthorityEntity. */
	public static enum Names {
		/**
		 * Nombre de la propiedad ID.
		 */
		ID("id"),
		/**
		 * Nombre de la propiedad AUTHORITY.
		 */
		NAME("name"),
		/**
		 * Nombre de la propiedad PEOPLE.
		 */
		PEOPLE("people"),
		/**
		 * Nombre de la propiedad CREATE_DATE.
		 */
		CREATE_DATE("createDate"),
		/**
		 * Nombre de la propiedad UPDATE_DATE.
		 */
		UPDATE_DATE("updateDate"),
		/**
		 * Nombre de la propiedad TYPE.
		 */
		AUTH_TYPE("authType"),
		/**
		 * Nombre de la propiedad ZONE.
		 */
		ZONE("zone"),
		/**
		 * Nombre de la propiedad PARENT.
		 */
		PARENT("parent"),
		/**
		 * Nombre de la propiedad WORKSPACE_NAME.
		 */
		WORKSPACE_NAME("workspaceName");
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
	 * SerialVersionUID.
	 */
	private static final long serialVersionUID = -6293606419664405344L;

	private List<RegionAuthorityRelationship> regionAuthoritys = new LinkedList<RegionAuthorityRelationship>();;

	/**
	 * Constructor por defecto.
	 */
	public AuthorityEntity() {
	}

	/**
	 * Constructor a partir del nombre de la entidad.
	 * 
	 * @param authString
	 *            nombre de la entidad.
	 */
	public AuthorityEntity(final String authString) {
		name = authString;
	}

	/**
	 * Identificador. Clave pública de la tabla.
	 * 
	 * @return devuelve el id.
	 */
	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "gis_authority_seq")
	@SequenceGenerator(name = "gis_authority_seq", sequenceName = "gis_authority_seq", initialValue = 100)
	public Long getId() {
		return id;
	}

	/**
	 * Nombre de la entidad.
	 * 
	 * @return el nombre de la entidad.
	 */
	@Column(name = "name", nullable = false)
	@Override
	public String getName() {
		return name;
	}

	/**
	 * Obtiene la lista de los usuarios miembros de la entidad.
	 * 
	 * @return lista de los miembros de la entidad.
	 */
	@SuppressWarnings("unchecked")
	@OneToMany(mappedBy = "authority", cascade = { CascadeType.PERSIST,
			CascadeType.MERGE })
	@Override
	public Set<UserEntity> getPeople() {
		return (Set<UserEntity>) people;
	}

	/**
	 * Devuelve la fecha de creación.
	 * 
	 * @return la fecha de creación del objeto.
	 */
	@Column(name = "create_date")
	@Override
	public Date getCreateDate() {
		return createDate;
	}

	/**
	 * Devuelvel la fecha de la última actualización del objeto.
	 * 
	 * @return la fecha de la última actualización del objeto.
	 */
	@Column(name = "update_date")
	@Override
	public Date getUpdateDate() {
		return this.updateDate;
	}

	/**
	 * Tipo de institución.
	 * 
	 * @return el tipo de institución.
	 */
	@ManyToOne
	@JoinColumn(name = "auth_type_id")
	public AuthorityTypeEntity getAuthType() {
		return (AuthorityTypeEntity) authType;
	}

	/**
	 * Es el ámbito territorial de la institución.
	 * 
	 * @return el ámbito territorial de la institución.
	 */
	@ManyToOne
	@JoinColumn(name = "zone_id")
	public ZoneEntity getZone() {
		return (ZoneEntity) zone;
	}

	@SuppressWarnings("unchecked")
	@OneToMany
	@Override
	public List<LayerEntity> getLayerList() {
		return (List<LayerEntity>) layerList;
	}

	@ManyToOne
	@JoinColumn(name = "auth_parent_id")
	public AuthorityEntity getParent() {
		return (AuthorityEntity) parent;
	}

	/**
	 * @return
	 */
	@Column(name = "workspace_name", length = 255)
	public String getWorkspaceName() {
		return workspaceName;
	}

	@OneToMany(mappedBy = "ra.authority", cascade = CascadeType.ALL)
	@LazyCollection(LazyCollectionOption.FALSE)
	public List<RegionAuthorityRelationship> getRegionAuthoritys() {
		return this.regionAuthoritys;
	}

	public void setRegionAuthoritys(
			List<RegionAuthorityRelationship> regionAuthoritys) {
		this.regionAuthoritys = regionAuthoritys;
	}

	@Override
	public int hashCode() {
		HashCodeBuilder hcb = new HashCodeBuilder();
		hcb.append(id);
		hcb.append(name);
		hcb.append(workspaceName);
		hcb.append(createDate);
		hcb.append(updateDate);
		return hcb.toHashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!(obj instanceof AuthorityEntity)) {
			return false;
		}
		AuthorityEntity other = (AuthorityEntity) obj;
		EqualsBuilder eb = new EqualsBuilder();
		eb.append(id, other.id);
		eb.append(name, other.name);
		eb.append(workspaceName, other.workspaceName);
		eb.append(createDate, other.createDate);
		eb.append(updateDate, other.updateDate);
		return eb.isEquals();
	}

}
