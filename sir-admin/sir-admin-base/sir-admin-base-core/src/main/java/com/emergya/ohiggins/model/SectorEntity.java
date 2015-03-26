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
 * Authors:: Juan Luis Rodríguez Ponce (mailto:jlrodriguez@emergya.com)
 */

package com.emergya.ohiggins.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.emergya.persistenceGeo.metaModel.AbstractEntity;

@Entity
@Table(name = "gis_sector")
public class SectorEntity extends AbstractEntity {

	/** Nombres de campos de la clase AuthorityEntity. */
	public static enum Names {
		/**
		 * Nombre de la propiedad ID.
		 */
		ID("id"),
		/**
		 * Nombre de la propiedad AUTHORITY.
		 */
		NOMBRE("nombre"),

		/**
		 * Nombre de la propiedad CREATE_DATE.
		 */
		CREATE_DATE("createDate"),
		/**
		 * Nombre de la propiedad UPDATE_DATE.
		 */
		UPDATE_DATE("updateDate");

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
	 * 
	 */
	private static final long serialVersionUID = -6293006419061405344L;

	private Long id;
	private String nombre;
	private Date createDate;
	private Date updateDate;

	/**
	 * @return the fecha_creacion
	 */
	@Column(name = "create_date")
	public Date getcreateDate() {
		return createDate;
	}

	/**
	 * @param fecha_creacion
	 *            the fecha_creacion to set
	 */
	public void setCreateDate(Date fecha_creacion) {
		this.createDate = fecha_creacion;
	}

	/**
	 * @return the fecha_actualizacion
	 */
	@Column(name = "update_date")
	public Date getUpdateDate() {
		return updateDate;
	}

	/**
	 * @param fecha_actualizacion
	 *            the fecha_actualizacion to set
	 */
	public void setUpdateDate(Date fecha_actualizacion) {
		this.updateDate = fecha_actualizacion;
	}

	@Id
	@Column
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "sector_seq")
	@SequenceGenerator(name = "sector_seq", sequenceName = "sector_seq", initialValue = 100)
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;

	}

	@Column(name = "nombre")
	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	@Override
	public void setId(Serializable id) {
		this.id = (Long) id;

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("SectorEntity [id=").append(id).append(", nombre=")
				.append(nombre).append("]");
		return builder.toString();
	}

}
