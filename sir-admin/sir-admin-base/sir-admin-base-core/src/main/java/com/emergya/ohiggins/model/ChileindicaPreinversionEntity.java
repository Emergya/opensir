/*
 * ChileindicaPreinversionEntity.java
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
import java.math.BigDecimal;

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
@Table(name = "gis_chileindica_preinversion")
@NamedQueries({ @NamedQuery(name = "ChileindicaPreinversionEntity.findAll", query = "SELECT g FROM ChileindicaPreinversionEntity g") })
public class ChileindicaPreinversionEntity implements Serializable {

	private static final long serialVersionUID = 99288764857287211L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Basic(optional = false)
	@Column(name = "gis_ohiggins_id")
	private Long gisOhigginsId;

	// Key
	@Column(name = "codigo_bip", length = 2048)
	private String codigoBip;
	@Column(name = "servicio_responsable")
	private String servicioResponsable;
	@Column(name = "anyo", length = 2048)
	private String anyo;
	@Column(name = "etapa", length = 2048)
	private String etapa;

	// Additional info
	@Column(name = "nombre_iniciativa", length = 2048)
	private String nombreIniciativa;
	@Column(name = "estado", length = 2048)
	private String estado;
	@Column(name = "fuente_financiamiento", length = 2048)
	private String fuenteFinanciamiento;
	@Column(name = "via_de_financiamiento", length = 2048)
	private String viaDeFinanciamiento;
	@Column(name = "costo_total")
	private BigDecimal costoTotal;
	@Column(name = "solicitado_anyo")
	private BigDecimal solicitadoAnyo;
	@Column(name = "sector", length = 2048)
	private String sector;
	@Column(name = "comuna", length = 2048)
	private String comuna;
	@Column(name = "observaciones", length = 2048)
	private String observaciones;

	// Relations
	@JoinColumn(name = "investment_initiative_update_id", referencedColumnName = "id")
	@ManyToOne
	private InversionUpdateEntity updateInstance;

	public ChileindicaPreinversionEntity() {
	}

	public ChileindicaPreinversionEntity(Long gisOhigginsId) {
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

	public String getFuenteFinanciamiento() {
		return fuenteFinanciamiento;
	}

	public String getNombreIniciativa() {
		return nombreIniciativa;
	}

	public void setNombreIniciativa(String nombreIniciativa) {
		this.nombreIniciativa = nombreIniciativa;
	}

	public void setFuenteFinanciamiento(String fuenteFinanciamiento) {
		this.fuenteFinanciamiento = fuenteFinanciamiento;
	}

	public String getAnyo() {
		return anyo;
	}

	public void setAnyo(String anyo) {
		this.anyo = anyo;
	}

	public String getCodigoBip() {
		return codigoBip;
	}

	public void setCodigoBip(String codigoBip) {
		this.codigoBip = codigoBip;
	}

	public String getServicioResponsable() {
		return servicioResponsable;
	}

	public void setServicioResponsable(String servicioResponsable) {
		this.servicioResponsable = servicioResponsable;
	}

	public String getSector() {
		return sector;
	}

	public void setSector(String sector) {
		this.sector = sector;
	}

	public String getEstado() {
		return estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}

	public String getObservaciones() {
		return observaciones;
	}

	public void setObservaciones(String observaciones) {
		this.observaciones = observaciones;
	}

	public BigDecimal getCostoTotal() {
		return costoTotal;
	}

	public void setCostoTotal(BigDecimal costoTotal) {
		this.costoTotal = costoTotal;
	}

	public BigDecimal getSolicitadoAnyo() {
		return solicitadoAnyo;
	}

	public void setSolicitadoAnyo(BigDecimal solicitadoAnyo) {
		this.solicitadoAnyo = solicitadoAnyo;
	}

	public String getEtapa() {
		return etapa;
	}

	public void setEtapa(String etapa) {
		this.etapa = etapa;
	}

	public String getViaDeFinanciamiento() {
		return viaDeFinanciamiento;
	}

	public void setViaDeFinanciamiento(String viaDeFinanciamiento) {
		this.viaDeFinanciamiento = viaDeFinanciamiento;
	}

	public String getComuna() {
		return comuna;
	}

	public void setComuna(String comuna) {
		this.comuna = comuna;
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
		if (!(object instanceof ChileindicaPreinversionEntity)) {
			return false;
		}

		ChileindicaPreinversionEntity other = (ChileindicaPreinversionEntity) object;

		if (((this.gisOhigginsId == null) && (other.gisOhigginsId != null))
				|| ((this.gisOhigginsId != null) && !this.gisOhigginsId
						.equals(other.gisOhigginsId))) {
			return false;
		}

		return true;
	}

	@Override
	public String toString() {
		return "com.emergya.ohiggins.model.ChileindicaPreinversionEntity[ gisOhigginsId="
				+ gisOhigginsId + " ]";
	}
}
