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

package com.emergya.ohiggins.model;

import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.geolatte.common.dataformats.json.jackson.JsonException;
import org.geolatte.common.dataformats.json.jackson.JsonMapper;
import org.geolatte.geom.jts.JTS;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Type;

import com.emergya.persistenceGeo.metaModel.AbstractZoneEntity;
import com.vividsolutions.jts.geom.Geometry;

/**
 * Ámbito territorial.
 * 
 * @author <a href="mailto:jlrodriguez@emergya.com">jlrodriguez</a>.
 * 
 */
@Entity
@Table(name = "gis_zone")
@Cache(usage=CacheConcurrencyStrategy.READ_WRITE)
public class ZoneEntity extends AbstractZoneEntity {
	private JsonMapper mapper; 
	public ZoneEntity() {
		mapper= new JsonMapper();
	}

	public static enum Names {
		ID("id"), CODE("code"), NAME("name"), TYPE("type"), EXTENSION_GEOM(
				"extensionGeom"), CREATE_DATE("createDate"), UPDATE_DATE(
				"updateDate"), AUTH_LIST("authList"), NIVEL_PADRE("nivelPadre"), ZONE_LIST(
				"zoneList"), ENABLED("enabled");

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

	private static final long serialVersionUID = -6103606419664405344L;
	private Geometry geom;
	private ZoneEntity nivelPadre;

	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "gis_zone_seq")
	@SequenceGenerator(name = "gis_zone_seq", sequenceName = "gis_zone_seq", initialValue = 100)
	@Override
	public Long getId() {
		return id;
	}

	/**
	 * @return the codigo_territorio
	 */
	@Column(name = "code")
	@Override
	public String getCode() {
		return code;
	}

	/**
	 * @return the nombre
	 */
	@Column(name = "name")
	@Override
	public String getName() {
		return this.name;
	}

	/**
	 * @return the tipo_ambito
	 */
	@Column(name = "type")
	public String getType() {
		return type;
	}

	/**
	 * @return the extension
	 */

	@Column(name = "geom", columnDefinition = "Geometry")
	@Type(type = "org.hibernatespatial.GeometryUserType")
	public Geometry getExtensionGeom() {
		// TODO review type mapping
		return geom;
	}

	/**
	 * @param extension
	 *            the extension to set
	 */
	public void setExtensionGeom(Geometry extension) {
		this.geom = extension;
	}

	@Override
	@Transient
	public String getExtension() {
		if (geom == null) {
			return extension;
		} else {
			org.geolatte.geom.Geometry g = JTS.from(geom);
			try {
				return mapper.toJson(g);
			} catch (JsonException e) {
				return extension;
			}
		}
	}

	/**
	 * @return the fecha_creacion
	 */
	@Column(name = "create_date")
	@Override
	public Date getCreateDate() {
		return createDate;
	}

	/**
	 * @return the fecha_actualizacion
	 */
	@Column(name = "update_date")
	@Override
	public Date getUpdateDate() {
		return updateDate;
	}

	@Override
	public String toString() {
		return this.name;
	}

	@SuppressWarnings("unchecked")
	@OneToMany(mappedBy = "zone", cascade = { CascadeType.PERSIST,
			CascadeType.MERGE }, fetch=FetchType.LAZY)
	public List<AuthorityEntity> getAuthList() {
		return authList;
	}

	@ManyToOne(optional = true, fetch=FetchType.LAZY)
	public ZoneEntity getNivelPadre() {
		return nivelPadre;
	}

	public void setNivelPadre(ZoneEntity nivelPadre) {
		this.nivelPadre = nivelPadre;
	}

	@SuppressWarnings("unchecked")
	@Override
	@OneToMany(targetEntity = ZoneEntity.class, cascade = {
			CascadeType.PERSIST, CascadeType.MERGE }, fetch= FetchType.LAZY)
	@JoinTable(name = "gis_zone", joinColumns = @JoinColumn(name = "nivelpadre_id"), inverseJoinColumns = @JoinColumn(name = "id"))
	public List<ZoneEntity> getZoneList() {
		return zoneList;

	}

	@Column(name = "enabled")
	public Boolean getEnabled() {
		return enabled;
	}

}
