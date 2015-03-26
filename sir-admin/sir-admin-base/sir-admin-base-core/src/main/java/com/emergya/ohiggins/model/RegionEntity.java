/*
 * RegionEntity.java
 * 
 * Copyright (C) 2011
 * 
 * This file is part of Proyecto persistenceGeo
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
 * Authors:: igutierrez (mailto:igutierrez@emergya.com)
 */
package com.emergya.ohiggins.model;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import com.emergya.persistenceGeo.metaModel.AbstractEntity;

/**
 * Entidad de region
 * 
 * @author <a href="mailto:igutierrez@emergya.com">igutierrez</a>
 * 
 */
@Entity
@Table(name = "gis_region", uniqueConstraints = {
		@UniqueConstraint(columnNames = "prefix_wks"),
		@UniqueConstraint(columnNames = "node_analytics"),
		@UniqueConstraint(columnNames = "node_publicacion") })
public class RegionEntity extends AbstractEntity {

	/** Nombres de campos de la clase RegionEntity. */
	public static enum Names {
		/**
		 * Nombre de la propiedad ID.
		 */
		ID("id"),
		/**
		 * Nombre de la propiedad name_region.
		 */
		NAME("name_region"),

		/**
		 * Nombre de la propiedad prefix_wks.
		 */
		PREFIX("prefix_wks"),

		/**
		 * Nombre de la propiedad node_analytics.
		 */
		NODE_ANALYTICS("node_analytics"),

		/**
		 * Nombre de la propiedad node_publicacion.
		 */
		NODE_PUBLICACION("node_publicacion");

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

	private static final long serialVersionUID = -7761379925677293910L;

	private Long id;
	private String name_region;
	private String prefix_wks;
	private String node_analytics;
	private String node_publicacion;

	private List<RegionContactoRelationship> regionContactos = new LinkedList<RegionContactoRelationship>();
	private List<RegionUserRelationship> regionUsers = new LinkedList<RegionUserRelationship>();
	private List<RegionAuthorityRelationship> regionAuthoritys = new LinkedList<RegionAuthorityRelationship>();

	@Id
	@Column
	public Long getId() {
		return id;
	}

	@Override
	public void setId(Serializable id) {
		this.id = (Long) id;

	}

	@Column(name = "name_region")
	public String getName_region() {
		return name_region;
	}

	public void setName_region(String name_region) {
		this.name_region = name_region;
	}

	@Column(name = "prefix_wks")
	public String getPrefix_wks() {
		return prefix_wks;
	}

	public void setPrefix_wks(String prefix_wks) {
		this.prefix_wks = prefix_wks;
	}

	@Column(name = "node_analytics")
	public String getNode_analytics() {
		return node_analytics;
	}

	public void setNode_analytics(String node_analytics) {
		this.node_analytics = node_analytics;
	}

	@Column(name = "node_publicacion")
	public String getNode_publicacion() {
		return node_publicacion;
	}

	public void setNode_publicacion(String node_publicacion) {
		this.node_publicacion = node_publicacion;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "rc.region", cascade = CascadeType.ALL)
	public List<RegionContactoRelationship> getRegionContactos() {
		return this.regionContactos;
	}

	public void setRegionContactos(
			List<RegionContactoRelationship> regionContactos) {
		this.regionContactos = regionContactos;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "ru.region")
	public List<RegionUserRelationship> getRegionUsers() {
		return this.regionUsers;
	}

	public void setRegionUsers(List<RegionUserRelationship> regionUsers) {
		this.regionUsers = regionUsers;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "ra.region")
	public List<RegionAuthorityRelationship> getRegionAuthoritys() {
		return this.regionAuthoritys;
	}

	public void setRegionAuthoritys(
			List<RegionAuthorityRelationship> regionAuthoritys) {
		this.regionAuthoritys = regionAuthoritys;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("RegionEntity [id=").append(id).append(", name_region=")
				.append(name_region).append(", prefix_wks=").append(prefix_wks)
				.append("]");
		return builder.toString();
	}

	@Override
	public int hashCode() {
		HashCodeBuilder hcb = new HashCodeBuilder();
		hcb.append(id);
		hcb.append(name_region);
		hcb.append(prefix_wks);
		hcb.append(node_analytics);
		hcb.append(node_publicacion);
		return hcb.toHashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!(obj instanceof RegionEntity)) {
			return false;
		}
		RegionEntity other = (RegionEntity) obj;
		EqualsBuilder eb = new EqualsBuilder();
		eb.append(id, other.id);
		eb.append(name_region, other.name_region);
		eb.append(prefix_wks, other.prefix_wks);
		eb.append(node_analytics, other.node_analytics);
		eb.append(node_publicacion, other.node_publicacion);
		return eb.isEquals();
	}
}
