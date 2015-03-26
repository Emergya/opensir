/*
 * Copyright (C) 2013
 * 
 * This file is part of Proyecto sir-adminn
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
 * Authors:: Juan Luis Rodríguez Ponce (mailto:jlrodriguez@emergya.com)
 */
package com.emergya.ohiggins.model;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.Date;
import java.util.List;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.Type;

import com.vividsolutions.jts.geom.Geometry;

/**
 * 
 * @author <a href="mailto:jlrodriguez@emergya.com">Juan Luis Rodríguez
 *         Ponce</a>
 */
@Entity
@Table(name = "gis_chileindica_inversion_data")
@NamedQueries({
		@NamedQuery(name = "ChileindicaInversionDataEntity.findAllIDs", query = "SELECT c.id FROM ChileindicaInversionDataEntity c order by c.id"),
		@NamedQuery(name = "ChileindicaInversionDataEntity.findAll", query = "SELECT c FROM ChileindicaInversionDataEntity c"),
		@NamedQuery(name = "ChileindicaInversionDataEntity.findById", query = "SELECT c FROM ChileindicaInversionDataEntity c WHERE c.id = :id"),
		@NamedQuery(name = "ChileindicaInversionDataEntity.findByRegion", query = "SELECT c FROM ChileindicaInversionDataEntity c WHERE c.region = :region"),
		@NamedQuery(name = "ChileindicaInversionDataEntity.findByAno", query = "SELECT c FROM ChileindicaInversionDataEntity c WHERE c.ano = :ano"),
		@NamedQuery(name = "ChileindicaInversionDataEntity.findByCInstitucion", query = "SELECT c FROM ChileindicaInversionDataEntity c WHERE c.cInstitucion = :cInstitucion"),
		@NamedQuery(name = "ChileindicaInversionDataEntity.findByCPreinversion", query = "SELECT c FROM ChileindicaInversionDataEntity c WHERE c.cPreinversion = :cPreinversion"),
		@NamedQuery(name = "ChileindicaInversionDataEntity.findByCFicha", query = "SELECT c FROM ChileindicaInversionDataEntity c WHERE c.cFicha = :cFicha"),
		@NamedQuery(name = "ChileindicaInversionDataEntity.findByCodigo", query = "SELECT c FROM ChileindicaInversionDataEntity c WHERE c.codigo = :codigo"),
		@NamedQuery(name = "ChileindicaInversionDataEntity.findByCTipoCodigo", query = "SELECT c FROM ChileindicaInversionDataEntity c WHERE c.cTipoCodigo = :cTipoCodigo"),
		@NamedQuery(name = "ChileindicaInversionDataEntity.findByNTipoCodigo", query = "SELECT c FROM ChileindicaInversionDataEntity c WHERE c.nTipoCodigo = :nTipoCodigo"),
		@NamedQuery(name = "ChileindicaInversionDataEntity.findByCEtapaIdi", query = "SELECT c FROM ChileindicaInversionDataEntity c WHERE c.cEtapaIdi = :cEtapaIdi"),
		@NamedQuery(name = "ChileindicaInversionDataEntity.findByNEtapaIdi", query = "SELECT c FROM ChileindicaInversionDataEntity c WHERE c.nEtapaIdi = :nEtapaIdi"),
		@NamedQuery(name = "ChileindicaInversionDataEntity.findByNombreInstitucionResponsable", query = "SELECT c FROM ChileindicaInversionDataEntity c WHERE c.nombreInstitucionResponsable = :nombreInstitucionResponsable"),
		@NamedQuery(name = "ChileindicaInversionDataEntity.findByNombreUnidadTecnica", query = "SELECT c FROM ChileindicaInversionDataEntity c WHERE c.nombreUnidadTecnica = :nombreUnidadTecnica"),
		@NamedQuery(name = "ChileindicaInversionDataEntity.findByNRegion", query = "SELECT c FROM ChileindicaInversionDataEntity c WHERE c.nRegion = :nRegion"),
		@NamedQuery(name = "ChileindicaInversionDataEntity.findByNombreProyecto", query = "SELECT c FROM ChileindicaInversionDataEntity c WHERE c.nombreProyecto = :nombreProyecto"),
		@NamedQuery(name = "ChileindicaInversionDataEntity.findByItemPresupuestario", query = "SELECT c FROM ChileindicaInversionDataEntity c WHERE c.itemPresupuestario = :itemPresupuestario"),
		@NamedQuery(name = "ChileindicaInversionDataEntity.findByNombreItemPresupuestario", query = "SELECT c FROM ChileindicaInversionDataEntity c WHERE c.nombreItemPresupuestario = :nombreItemPresupuestario"),
		@NamedQuery(name = "ChileindicaInversionDataEntity.findByNombreProvincia", query = "SELECT c FROM ChileindicaInversionDataEntity c WHERE c.nombreProvincia = :nombreProvincia"),
		@NamedQuery(name = "ChileindicaInversionDataEntity.findByNombreComuna", query = "SELECT c FROM ChileindicaInversionDataEntity c WHERE c.nombreComuna = :nombreComuna"),
		@NamedQuery(name = "ChileindicaInversionDataEntity.findByNombreLocalidad", query = "SELECT c FROM ChileindicaInversionDataEntity c WHERE c.nombreLocalidad = :nombreLocalidad"),
		@NamedQuery(name = "ChileindicaInversionDataEntity.findByCostoTotalAjustadoInversion", query = "SELECT c FROM ChileindicaInversionDataEntity c WHERE c.costoTotalAjustadoInversion = :costoTotalAjustadoInversion"),
		@NamedQuery(name = "ChileindicaInversionDataEntity.findByGastadoAnosAnterioresInversion", query = "SELECT c FROM ChileindicaInversionDataEntity c WHERE c.gastadoAnosAnterioresInversion = :gastadoAnosAnterioresInversion"),
		@NamedQuery(name = "ChileindicaInversionDataEntity.findBySolicitadoAnoInversion", query = "SELECT c FROM ChileindicaInversionDataEntity c WHERE c.solicitadoAnoInversion = :solicitadoAnoInversion"),
		@NamedQuery(name = "ChileindicaInversionDataEntity.findBySaldoProximoAnoInversion", query = "SELECT c FROM ChileindicaInversionDataEntity c WHERE c.saldoProximoAnoInversion = :saldoProximoAnoInversion"),
		@NamedQuery(name = "ChileindicaInversionDataEntity.findBySaldoAnosRestantesInversion", query = "SELECT c FROM ChileindicaInversionDataEntity c WHERE c.saldoAnosRestantesInversion = :saldoAnosRestantesInversion"),
		@NamedQuery(name = "ChileindicaInversionDataEntity.findByTotalAsignadoInversion", query = "SELECT c FROM ChileindicaInversionDataEntity c WHERE c.totalAsignadoInversion = :totalAsignadoInversion"),
		@NamedQuery(name = "ChileindicaInversionDataEntity.findByAsignacionDisponibleInversion", query = "SELECT c FROM ChileindicaInversionDataEntity c WHERE c.asignacionDisponibleInversion = :asignacionDisponibleInversion"),
		@NamedQuery(name = "ChileindicaInversionDataEntity.findBySaldoPorAsignarInversion", query = "SELECT c FROM ChileindicaInversionDataEntity c WHERE c.saldoPorAsignarInversion = :saldoPorAsignarInversion"),
		@NamedQuery(name = "ChileindicaInversionDataEntity.findByTotalPagadoInversion", query = "SELECT c FROM ChileindicaInversionDataEntity c WHERE c.totalPagadoInversion = :totalPagadoInversion"),
		@NamedQuery(name = "ChileindicaInversionDataEntity.findByFechaRegistroChileindica", query = "SELECT c FROM ChileindicaInversionDataEntity c WHERE c.fechaRegistroChileindica = :fechaRegistroChileindica") })
public class ChileindicaInversionDataEntity implements Serializable {

	public static enum UpdateStatus {
		WS_ERROR, DB_ERROR, OK
	}

	private static final long serialVersionUID = 8469466285164979426L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Basic(optional = false)
	@Column(name = "id")
	private Long id;
	@Column(name = "region")
	private Integer region;
	@Column(name = "ano")
	private Integer ano;
	@Column(name = "c_institucion")
	private Integer cInstitucion;
	@Column(name = "c_preinversion")
	private Integer cPreinversion;
	@Column(name = "c_ficha")
	private Integer cFicha;
	@Column(name = "codigo")
	private String codigo;
	@Column(name = "c_tipo_codigo")
	private Integer cTipoCodigo;
	@Column(name = "n_tipo_codigo")
	private String nTipoCodigo;
	@Column(name = "c_etapa_idi")
	private Integer cEtapaIdi;
	@Column(name = "n_etapa_idi")
	private String nEtapaIdi;
	@Column(name = "nombre_institucion_responsable")
	private String nombreInstitucionResponsable;
	@Column(name = "nombre_unidad_tecnica")
	private String nombreUnidadTecnica;
	@Column(name = "n_region")
	private String nRegion;
	@Column(name = "nombre_proyecto")
	private String nombreProyecto;
	@Column(name = "item_presupuestario")
	private String itemPresupuestario;
	@Column(name = "nombre_item_presupuestario")
	private String nombreItemPresupuestario;
	@Column(name = "nombre_provincia")
	private String nombreProvincia;
	@Column(name = "nombre_comuna")
	private String nombreComuna;
	@Column(name = "nombre_localidad")
	private String nombreLocalidad;
	@Column(name = "costo_total_ajustado_inversion")
	private BigInteger costoTotalAjustadoInversion;
	@Column(name = "gastado_anos_anteriores_inversion")
	private BigInteger gastadoAnosAnterioresInversion;
	@Column(name = "solicitado_ano_inversion")
	private BigInteger solicitadoAnoInversion;
	@Column(name = "saldo_proximo_ano_inversion")
	private BigInteger saldoProximoAnoInversion;
	@Column(name = "saldo_anos_restantes_inversion")
	private BigInteger saldoAnosRestantesInversion;
	@Column(name = "total_asignado_inversion")
	private BigInteger totalAsignadoInversion;
	@Column(name = "asignacion_disponible_inversion")
	private BigInteger asignacionDisponibleInversion;
	@Column(name = "saldo_por_asignar_inversion")
	private BigInteger saldoPorAsignarInversion;
	@Column(name = "total_pagado_inversion")
	private BigInteger totalPagadoInversion;
	@Column(name = "fecha_registro_chileindica")
	@Temporal(TemporalType.DATE)
	private Date fechaRegistroChileindica;
	@Column(name = "update_status")
	@Enumerated(EnumType.STRING)
	private UpdateStatus updateStatus;
	@Column(name = "last_update_try")
	@Temporal(TemporalType.TIMESTAMP)
	private Date lastUpdateTry;

	@Column(name = "the_geom", columnDefinition = "Geometry")
	@Type(type = "org.hibernatespatial.GeometryUserType")
	private Geometry theGeom;
	@OneToMany(mappedBy = "inversionData", cascade = CascadeType.ALL)
	private List<ChileindicaInversionFinanciamientoDataEntity> financiamientosList;
	@OneToMany(mappedBy = "inversionData", cascade = CascadeType.ALL)
	private List<ChileindicaRelacionIntrumentosDataEntity> relacionInstrumentosList;

	public ChileindicaInversionDataEntity() {
	}

	public ChileindicaInversionDataEntity(Long id) {
		this.id = id;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Integer getRegion() {
		return region;
	}

	public void setRegion(Integer region) {
		this.region = region;
	}

	public Integer getAno() {
		return ano;
	}

	public void setAno(Integer ano) {
		this.ano = ano;
	}

	public Integer getCInstitucion() {
		return cInstitucion;
	}

	public void setCInstitucion(Integer cInstitucion) {
		this.cInstitucion = cInstitucion;
	}

	public Integer getCPreinversion() {
		return cPreinversion;
	}

	public void setCPreinversion(Integer cPreinversion) {
		this.cPreinversion = cPreinversion;
	}

	public Integer getCFicha() {
		return cFicha;
	}

	public void setCFicha(Integer cFicha) {
		this.cFicha = cFicha;
	}

	public String getCodigo() {
		return codigo;
	}

	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}

	public Integer getCTipoCodigo() {
		return cTipoCodigo;
	}

	public void setCTipoCodigo(Integer cTipoCodigo) {
		this.cTipoCodigo = cTipoCodigo;
	}

	public String getNTipoCodigo() {
		return nTipoCodigo;
	}

	public void setNTipoCodigo(String nTipoCodigo) {
		this.nTipoCodigo = nTipoCodigo;
	}

	public Integer getCEtapaIdi() {
		return cEtapaIdi;
	}

	public void setCEtapaIdi(Integer cEtapaIdi) {
		this.cEtapaIdi = cEtapaIdi;
	}

	public String getNEtapaIdi() {
		return nEtapaIdi;
	}

	public void setNEtapaIdi(String nEtapaIdi) {
		this.nEtapaIdi = nEtapaIdi;
	}

	public String getNombreInstitucionResponsable() {
		return nombreInstitucionResponsable;
	}

	public void setNombreInstitucionResponsable(
			String nombreInstitucionResponsable) {
		this.nombreInstitucionResponsable = nombreInstitucionResponsable;
	}

	public String getNombreUnidadTecnica() {
		return nombreUnidadTecnica;
	}

	public void setNombreUnidadTecnica(String nombreUnidadTecnica) {
		this.nombreUnidadTecnica = nombreUnidadTecnica;
	}

	public String getNRegion() {
		return nRegion;
	}

	public void setNRegion(String nRegion) {
		this.nRegion = nRegion;
	}

	public String getNombreProyecto() {
		return nombreProyecto;
	}

	public void setNombreProyecto(String nombreProyecto) {
		this.nombreProyecto = nombreProyecto;
	}

	public String getItemPresupuestario() {
		return itemPresupuestario;
	}

	public void setItemPresupuestario(String itemPresupuestario) {
		this.itemPresupuestario = itemPresupuestario;
	}

	public String getNombreItemPresupuestario() {
		return nombreItemPresupuestario;
	}

	public void setNombreItemPresupuestario(String nombreItemPresupuestario) {
		this.nombreItemPresupuestario = nombreItemPresupuestario;
	}

	public String getNombreProvincia() {
		return nombreProvincia;
	}

	public void setNombreProvincia(String nombreProvincia) {
		this.nombreProvincia = nombreProvincia;
	}

	public String getNombreComuna() {
		return nombreComuna;
	}

	public void setNombreComuna(String nombreComuna) {
		this.nombreComuna = nombreComuna;
	}

	public String getNombreLocalidad() {
		return nombreLocalidad;
	}

	public void setNombreLocalidad(String nombreLocalidad) {
		this.nombreLocalidad = nombreLocalidad;
	}

	public BigInteger getCostoTotalAjustadoInversion() {
		return costoTotalAjustadoInversion;
	}

	public void setCostoTotalAjustadoInversion(
			BigInteger costoTotalAjustadoInversion) {
		this.costoTotalAjustadoInversion = costoTotalAjustadoInversion;
	}

	public BigInteger getGastadoAnosAnterioresInversion() {
		return gastadoAnosAnterioresInversion;
	}

	public void setGastadoAnosAnterioresInversion(
			BigInteger gastadoAnosAnterioresInversion) {
		this.gastadoAnosAnterioresInversion = gastadoAnosAnterioresInversion;
	}

	public BigInteger getSolicitadoAnoInversion() {
		return solicitadoAnoInversion;
	}

	public void setSolicitadoAnoInversion(BigInteger solicitadoAnoInversion) {
		this.solicitadoAnoInversion = solicitadoAnoInversion;
	}

	public BigInteger getSaldoProximoAnoInversion() {
		return saldoProximoAnoInversion;
	}

	public void setSaldoProximoAnoInversion(BigInteger saldoProximoAnoInversion) {
		this.saldoProximoAnoInversion = saldoProximoAnoInversion;
	}

	public BigInteger getSaldoAnosRestantesInversion() {
		return saldoAnosRestantesInversion;
	}

	public void setSaldoAnosRestantesInversion(
			BigInteger saldoAnosRestantesInversion) {
		this.saldoAnosRestantesInversion = saldoAnosRestantesInversion;
	}

	public BigInteger getTotalAsignadoInversion() {
		return totalAsignadoInversion;
	}

	public void setTotalAsignadoInversion(BigInteger totalAsignadoInversion) {
		this.totalAsignadoInversion = totalAsignadoInversion;
	}

	public BigInteger getAsignacionDisponibleInversion() {
		return asignacionDisponibleInversion;
	}

	public void setAsignacionDisponibleInversion(
			BigInteger asignacionDisponibleInversion) {
		this.asignacionDisponibleInversion = asignacionDisponibleInversion;
	}

	public BigInteger getSaldoPorAsignarInversion() {
		return saldoPorAsignarInversion;
	}

	public void setSaldoPorAsignarInversion(BigInteger saldoPorAsignarInversion) {
		this.saldoPorAsignarInversion = saldoPorAsignarInversion;
	}

	public BigInteger getTotalPagadoInversion() {
		return totalPagadoInversion;
	}

	public void setTotalPagadoInversion(BigInteger totalPagadoInversion) {
		this.totalPagadoInversion = totalPagadoInversion;
	}

	public Date getFechaRegistroChileindica() {
		return fechaRegistroChileindica;
	}

	public void setFechaRegistroChileindica(Date fechaRegistroChileindica) {
		this.fechaRegistroChileindica = fechaRegistroChileindica;
	}

	public Integer getcInstitucion() {
		return cInstitucion;
	}

	public void setcInstitucion(Integer cInstitucion) {
		this.cInstitucion = cInstitucion;
	}

	public Integer getcPreinversion() {
		return cPreinversion;
	}

	public void setcPreinversion(Integer cPreinversion) {
		this.cPreinversion = cPreinversion;
	}

	public Integer getcFicha() {
		return cFicha;
	}

	public void setcFicha(Integer cFicha) {
		this.cFicha = cFicha;
	}

	public Integer getcTipoCodigo() {
		return cTipoCodigo;
	}

	public void setcTipoCodigo(Integer cTipoCodigo) {
		this.cTipoCodigo = cTipoCodigo;
	}

	public String getnTipoCodigo() {
		return nTipoCodigo;
	}

	public void setnTipoCodigo(String nTipoCodigo) {
		this.nTipoCodigo = nTipoCodigo;
	}

	public Integer getcEtapaIdi() {
		return cEtapaIdi;
	}

	public void setcEtapaIdi(Integer cEtapaIdi) {
		this.cEtapaIdi = cEtapaIdi;
	}

	public String getnEtapaIdi() {
		return nEtapaIdi;
	}

	public void setnEtapaIdi(String nEtapaIdi) {
		this.nEtapaIdi = nEtapaIdi;
	}

	public String getnRegion() {
		return nRegion;
	}

	public void setnRegion(String nRegion) {
		this.nRegion = nRegion;
	}

	public UpdateStatus getUpdateStatus() {
		return updateStatus;
	}

	public void setUpdateStatus(UpdateStatus updateStatus) {
		this.updateStatus = updateStatus;
	}

	public Date getLastUpdateTry() {
		return lastUpdateTry;
	}

	public void setLastUpdateTry(Date lastUpdateTry) {
		this.lastUpdateTry = lastUpdateTry;
	}

	public Geometry getTheGeom() {
		return theGeom;
	}

	public void setTheGeom(Geometry theGeom) {
		this.theGeom = theGeom;
	}

	public List<ChileindicaInversionFinanciamientoDataEntity> getFinanciamientosList() {
		return financiamientosList;
	}

	public void setFinanciamientosList(
			List<ChileindicaInversionFinanciamientoDataEntity> chileindicaInversionFinanciamientoDataEntityList) {
		this.financiamientosList = chileindicaInversionFinanciamientoDataEntityList;
	}

	public List<ChileindicaRelacionIntrumentosDataEntity> getRelacionInstrumentosList() {
		return relacionInstrumentosList;
	}

	public void setRelacionInstrumentosList(
			List<ChileindicaRelacionIntrumentosDataEntity> chileindicaRelacionIntrumentosDataEntityList) {
		this.relacionInstrumentosList = chileindicaRelacionIntrumentosDataEntityList;
	}

	@Override
	public int hashCode() {
		int hash = 0;
		hash += (id != null ? id.hashCode() : 0);
		return hash;
	}

	@Override
	public boolean equals(Object object) {
		// TODO: Warning - this method won't work in the case the id fields are
		// not set
		if (!(object instanceof ChileindicaInversionDataEntity)) {
			return false;
		}
		ChileindicaInversionDataEntity other = (ChileindicaInversionDataEntity) object;
		if ((this.id == null && other.id != null)
				|| (this.id != null && !this.id.equals(other.id))) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "ChileindicaInversionDataEntity[ id=" + id + " ]";
	}

}
