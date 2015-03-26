/*
 * BasePreinversionGoreEntity.java
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
@Table(name = "gis_base_preinversion_gore")
@NamedQueries({ @NamedQuery(name = "BasePreinversionGoreEntity.findAll", query = "SELECT g FROM BasePreinversionGoreEntity g") })
public class BasePreinversionGoreEntity implements Serializable {
	private static final long serialVersionUID = 483899922293838L;

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
	@Column(name = "etapa")
	private String etapa;

	// Aditional columns
	@Column(name = "nro_of_institucion", length = 2048)
	private String nroOfInstitucion;
	@Column(name = "carpeta_digital_bip", length = 2048)
	private String carpetaDigitalBip;
	@Column(name = "ingresado_mod_preinv_chilenindica", length = 2048)
	private String ingresadoModPreinvChilenindica;
	@Column(name = "atributos_que_presenta", length = 2048)
	private String atributosQuePresenta;
	@Column(name = "total_de_atributos", length = 2048)
	private String totalDeAtributos;
	@Column(name = "erd", length = 2048)
	private String erd;
	@Column(name = "lineamiento_ude", length = 2048)
	private String lineamientoUde;
	@Column(name = "politica_regional_turismo", length = 2048)
	private String politicaRegionalTurismo;
	@Column(name = "politica_regional_cyt", length = 2048)
	private String politicaRegionalCyt;
	@Column(name = "plan_ohiggins_2010_2014", length = 2048)
	private String planOhiggins20102014;
	@Column(name = "plan_reconstruc_27f", length = 2048)
	private String planReconstruc27f;
	@Column(name = "convenio_programacion", length = 2048)
	private String convenioProgramacion;
	@Column(name = "seleccionada_por_intendente", length = 2048)
	private String seleccionadaPorIntendente;
	@Column(name = "mesa_tecnica", length = 512)
	private String mesaTecnica;
	@Column(name = "notas_observaciones", length = 2048)
	private String notasObservaciones;

	@JoinColumn(name = "investment_initiative_update_id", referencedColumnName = "id")
	@ManyToOne
	private InversionUpdateEntity updateInstance;

	public BasePreinversionGoreEntity() {
	}

	public BasePreinversionGoreEntity(Long gisOhigginsId) {
		this.gisOhigginsId = gisOhigginsId;
	}

	@Override
	public boolean equals(Object object) {

		// TODO: Warning - this method won't work in the case the id fields are
		// not set
		if (!(object instanceof BasePreinversionGoreEntity)) {
			return false;
		}

		BasePreinversionGoreEntity other = (BasePreinversionGoreEntity) object;

		if (((this.gisOhigginsId == null) && (other.gisOhigginsId != null))
				|| ((this.gisOhigginsId != null) && !this.gisOhigginsId
						.equals(other.gisOhigginsId))) {
			return false;
		}

		return true;
	}

	public String getAnyo() {
		return anyo;
	}

	public String getAtributosQuePresenta() {
		return atributosQuePresenta;
	}

	public String getCarpetaDigitalBip() {
		return carpetaDigitalBip;
	}

	public String getCodigoBip() {
		return codigoBip;
	}

	public String getConvenioProgramacion() {
		return convenioProgramacion;
	}

	public String getErd() {
		return erd;
	}

	public String getEtapa() {
		return etapa;
	}

	public Long getGisOhigginsId() {
		return gisOhigginsId;
	}

	public String getIngresadoModPreinvChilenindica() {
		return ingresadoModPreinvChilenindica;
	}

	public String getLineamientoUde() {
		return lineamientoUde;
	}

	public String getNotasObservaciones() {
		return notasObservaciones;
	}

	public String getNroOfInstitucion() {
		return nroOfInstitucion;
	}

	public String getPlanOhiggins20102014() {
		return planOhiggins20102014;
	}

	public String getPlanReconstruc27f() {
		return planReconstruc27f;
	}

	public String getPoliticaRegionalCyt() {
		return politicaRegionalCyt;
	}

	public String getPoliticaRegionalTurismo() {
		return politicaRegionalTurismo;
	}

	public String getSeleccionadaPorIntendente() {
		return seleccionadaPorIntendente;
	}

	public String getServicioResponsable() {
		return servicioResponsable;
	}

	public String getTotalDeAtributos() {
		return totalDeAtributos;
	}

	public InversionUpdateEntity getUpdateInstance() {
		return updateInstance;
	}

	public String getMesaTecnica() {
		return mesaTecnica;
	}

	public void setMesaTecnica(String mesaTecnica) {
		this.mesaTecnica = mesaTecnica;
	}

	@Override
	public int hashCode() {
		int hash = 0;

		hash += ((gisOhigginsId != null) ? gisOhigginsId.hashCode() : 0);

		return hash;
	}

	public void setAnyo(String anyo) {
		this.anyo = anyo;
	}

	public void setAtributosQuePresenta(String atributosQuePresenta) {
		this.atributosQuePresenta = atributosQuePresenta;
	}

	public void setCarpetaDigitalBip(String carpetaDigitalBip) {
		this.carpetaDigitalBip = carpetaDigitalBip;
	}

	public void setCodigoBip(String codigoBip) {
		this.codigoBip = codigoBip;
	}

	public void setConvenioProgramacion(String convenioProgramacion) {
		this.convenioProgramacion = convenioProgramacion;
	}

	public void setErd(String erd) {
		this.erd = erd;
	}

	public void setEtapa(String etapa) {
		this.etapa = etapa;
	}

	public void setGisOhigginsId(Long gisOhigginsId) {
		this.gisOhigginsId = gisOhigginsId;
	}

	public void setIngresadoModPreinvChilenindica(
			String ingresadoModPreinvChilenindica) {
		this.ingresadoModPreinvChilenindica = ingresadoModPreinvChilenindica;
	}

	public void setLineamientoUde(String lineamientoUde) {
		this.lineamientoUde = lineamientoUde;
	}

	public void setNotasObservaciones(String notasObservaciones) {
		this.notasObservaciones = notasObservaciones;
	}

	public void setNroOfInstitucion(String nroOfInstitucion) {
		this.nroOfInstitucion = nroOfInstitucion;
	}

	public void setPlanOhiggins20102014(String planOhiggins20102014) {
		this.planOhiggins20102014 = planOhiggins20102014;
	}

	public void setPlanReconstruc27f(String planReconstruc27f) {
		this.planReconstruc27f = planReconstruc27f;
	}

	public void setPoliticaRegionalCyt(String politicaRegionalCyt) {
		this.politicaRegionalCyt = politicaRegionalCyt;
	}

	public void setPoliticaRegionalTurismo(String politicaRegionalTurismo) {
		this.politicaRegionalTurismo = politicaRegionalTurismo;
	}

	public void setSeleccionadaPorIntendente(String seleccionadaPorIntendente) {
		this.seleccionadaPorIntendente = seleccionadaPorIntendente;
	}

	public void setServicioResponsable(String servicioResponsable) {
		this.servicioResponsable = servicioResponsable;
	}

	public void setTotalDeAtributos(String totalDeAtributos) {
		this.totalDeAtributos = totalDeAtributos;
	}

	public void setUpdateInstance(InversionUpdateEntity updateInstance) {
		this.updateInstance = updateInstance;
	}

	@Override
	public String toString() {
		return "com.emergya.ohiggins.model.BasePreinversionGoreEntity[ gisOhigginsId="
				+ gisOhigginsId + " ]";
	}
}

// ~ Formatted by Jindent --- http://www.jindent.com
