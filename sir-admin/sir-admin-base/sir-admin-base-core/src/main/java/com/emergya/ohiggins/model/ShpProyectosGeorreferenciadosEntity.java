/*
 * ShpProyectosGeorreferenciadosEntity.java
 *
 * Copyright (C) 2012 Emergya
 *
 * This file is part of ohiggins-core
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 *
 * As a special exception, if you link this library with other files to produce
 * an executable, this library does not by itself cause the resulting executable
 * to be covered by the GNU General Public License. This exception does not
 * however invalidate any other reasons why the executable file might be covered
 * by the GNU General Public License.
 *
 * Authors::Juan Luis Rodríguez Ponce (mailto:jlrodriguez@emergya.com)


 */

package com.emergya.ohiggins.model;

//~--- non-JDK imports --------------------------------------------------------

import java.io.Serializable;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import org.hibernate.annotations.Type;

import com.vividsolutions.jts.geom.Point;

//~--- JDK imports ------------------------------------------------------------

/**
 * 
 * @author <a href="mailto:jlrodriguez@emergya.com">Juan Luis Rodríguez
 *         Ponce</a>
 */
@Entity
@Table(name = "gis_shp_proyectos_georreferenciados")
@NamedQueries({ @NamedQuery(name = "ShpProyectosGeorreferenciadosEntity.findAll", query = "SELECT g FROM ShpProyectosGeorreferenciadosEntity g") })
public class ShpProyectosGeorreferenciadosEntity implements Serializable {
	private static final long serialVersionUID = 992737771283994844L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Basic(optional = false)
	@Column(name = "gis_ohiggins_id")
	private Long gisOhigginsId;

	// key
	@Column(name = "cod_bip")
	private String codBip;

	// Additional info
	@Column(name = "nombre")
	private String nombre;

	@Column(name = "the_geom", columnDefinition = "Geometry")
	@Type(type = "org.hibernatespatial.GeometryUserType")
	private Point theGeom;

	// Relations
	@JoinColumn(name = "investment_initiative_update_id", referencedColumnName = "id")
	@ManyToOne
	private InversionUpdateEntity updateInstance;

	public ShpProyectosGeorreferenciadosEntity() {
	}

	public ShpProyectosGeorreferenciadosEntity(Long gisOhigginsId) {
		this.gisOhigginsId = gisOhigginsId;
	}

	public Long getGisOhigginsId() {
		return gisOhigginsId;
	}

	public void setGisOhigginsId(Long gisOhigginsId) {
		this.gisOhigginsId = gisOhigginsId;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getCodBip() {
		return codBip;
	}

	public void setCodBip(String codBip) {
		this.codBip = codBip;
	}

	public Point getTheGeom() {
		return theGeom;
	}

	public void setTheGeom(Point theGeom) {
		this.theGeom = theGeom;
	}

	public InversionUpdateEntity getUpdateInstance() {
		return updateInstance;
	}

	public void setUpdateInstance(InversionUpdateEntity updateInstance) {
		this.updateInstance = updateInstance;
	}

	@Override
	public int hashCode() {
		int hash = 0;

		hash += ((gisOhigginsId != null) ? gisOhigginsId.hashCode() : 0);

		return hash;
	}

	@Override
	public boolean equals(Object object) {

		// TODO: Warning - this method won't work in the case the id fields are
		// not set
		if (!(object instanceof ShpProyectosGeorreferenciadosEntity)) {
			return false;
		}

		ShpProyectosGeorreferenciadosEntity other = (ShpProyectosGeorreferenciadosEntity) object;

		if (((this.gisOhigginsId == null) && (other.gisOhigginsId != null))
				|| ((this.gisOhigginsId != null) && !this.gisOhigginsId
						.equals(other.gisOhigginsId))) {
			return false;
		}

		return true;
	}

	@Override
	public String toString() {
		return "com.emergya.ohiggins.model.ShpProyectosGeorreferenciadosEntity[ gisOhigginsId="
				+ gisOhigginsId + " ]";
	}
}
