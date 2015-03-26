/*
 * ProyectosDacgEntity.java
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

//~--- JDK imports ------------------------------------------------------------
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

/**
 * 
 * @author <a href="mailto:jlrodriguez@emergya.com">Juan Luis Rodríguez
 *         Ponce</a>
 */
@Entity
@Table(name = "gis_proyectos_dacg")
@NamedQueries({ @NamedQuery(name = "ProyectosDacgEntity.findAll", query = "SELECT g FROM ProyectosDacgEntity g") })
public class ProyectosDacgEntity implements Serializable {

	private static final long serialVersionUID = 118328899482274022L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Basic(optional = false)
	@Column(name = "gis_ohiggins_id")
	private Long gisOhigginsId;

	// Key
	@Column(name = "codigo_bip")
	private String codigoBip;
	@Column(name = "servicio_responsable")
	private String servicioResponsable;
	@Column(name = "anyo")
	private String anyo;
	@Column(name = "etapa", length = 2048)
	private String etapa;

	// Additional info
	@Column(name = "observacion", length = 2048)
	private String observacion;
	@Column(name = "fecha_observacion", length = 2048)
	private String fechaObservacion;

	// Relations
	@JoinColumn(name = "investment_initiative_update_id", referencedColumnName = "id")
	@ManyToOne
	private InversionUpdateEntity updateInstance;

	public ProyectosDacgEntity() {
	}

	public ProyectosDacgEntity(Long gisOhigginsId) {
		this.gisOhigginsId = gisOhigginsId;
	}

	public InversionUpdateEntity getUpdateInstance() {
		return updateInstance;
	}

	public void setUpdateInstance(InversionUpdateEntity updateInstance) {
		this.updateInstance = updateInstance;
	}

	public Long getGisOhigginsId() {
		return gisOhigginsId;
	}

	public void setGisOhigginsId(Long gisOhigginsId) {
		this.gisOhigginsId = gisOhigginsId;
	}

	public String getEtapa() {
		return etapa;
	}

	public void setEtapa(String etapa) {
		this.etapa = etapa;
	}

	public String getFechaObservacion() {
		return fechaObservacion;
	}

	public void setFechaObservacion(String fechaObservacion) {
		this.fechaObservacion = fechaObservacion;
	}

	public String getObservacion() {
		return observacion;
	}

	public void setObservacion(String observacion) {
		this.observacion = observacion;
	}

	public String getAnyo() {
		return anyo;
	}

	public void setAnyo(String anyo) {
		this.anyo = anyo;
	}

	public String getServicioResponsable() {
		return servicioResponsable;
	}

	public void setServicioResponsable(String servicioResponsable) {
		this.servicioResponsable = servicioResponsable;
	}

	public String getCodigoBip() {
		return codigoBip;
	}

	public void setCodigoBip(String codigoBip) {
		this.codigoBip = codigoBip;
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
		if (!(object instanceof ProyectosDacgEntity)) {
			return false;
		}

		ProyectosDacgEntity other = (ProyectosDacgEntity) object;

		if (((this.gisOhigginsId == null) && (other.gisOhigginsId != null))
				|| ((this.gisOhigginsId != null) && !this.gisOhigginsId
						.equals(other.gisOhigginsId))) {
			return false;
		}

		return true;
	}

	@Override
	public String toString() {
		return "com.emergya.ohiggins.model.ProyectosDacgEntity[ gisOhigginsId="
				+ gisOhigginsId + " ]";
	}
}
