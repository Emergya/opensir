/*
 * LayerMetadataTmpEntity.java
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
 * Authors:: Jose Alfonso Riera(mailto:jariera@emergya.com)
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
import javax.persistence.Lob;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;

/**
 * Entidad de layer metadatos tmp
 * 
 * @author <a href="mailto:jariera@emergya.com">jariera</a>
 * 
 */
@SuppressWarnings("unchecked")
@Entity
@Table(name = "gis_layer_metadata")
public class LayerMetadataEntity implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1799150560884991597L;

	private Long id;
	private Date fechacreacion;
	private Date fechaactualizacion;

	// Metadatos para capas vectoriales

	// Informacion de mantenimiento
	private String frecuencia;
	private Date siguiente;
	private String periodo;
	private String rango;
	private String otros;
	private String responsable;
	private String requerimientos;

	// Informacion de distribucion
	private String formato;
	private String distribuidor;
	private String informacion;

	// Metadatos para capas raster
	// Los definidos para capas vectoriales mas los siguientes:

	// Informacion geoRectificacion
	private String referencia;
	// Informacion GeoReferenciación
	private String informacionGeolocalizacion;

	// Informacion de geolocalizacion
	private String calidad;
	private String informacionColeccionPuntoReferencia;
	private String informacionPuntoReferencia;
	private String nombrePuntoReferencia;
	private String crsPuntoReferencia;
	private String puntosDeReferencia;

	// Puntos de control
	private String posicion;
	private String precision;

	private List<LayerEntity> layer;

	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.AUTO)
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Column(name = "create_date")
	@Temporal(javax.persistence.TemporalType.DATE)
	public Date getFechacreacion() {
		return fechacreacion;
	}

	public void setFechacreacion(Date fechacreacion) {
		this.fechacreacion = fechacreacion;
	}

	@Column(name = "update_date")
	@Temporal(javax.persistence.TemporalType.DATE)
	public Date getFechaactualizacion() {
		return fechaactualizacion;
	}

	public void setFechaactualizacion(Date fechaactualizacion) {
		this.fechaactualizacion = fechaactualizacion;
	}

	@Column(name = "frecuencia")
	public String getFrecuencia() {
		return frecuencia;
	}

	public void setFrecuencia(String frecuencia) {
		this.frecuencia = frecuencia;
	}

	@Column(name = "siguiente")
	@Temporal(javax.persistence.TemporalType.DATE)
	public Date getSiguiente() {
		return siguiente;
	}

	public void setSiguiente(Date siguiente) {
		this.siguiente = siguiente;
	}

	@Column(name = "periodo")
	public String getPeriodo() {
		return periodo;
	}

	public void setPeriodo(String periodo) {
		this.periodo = periodo;
	}

	@Column(name = "rango")
	public String getRango() {
		return rango;
	}

	public void setRango(String rango) {
		this.rango = rango;
	}

	@Column(name = "otros")
	@Lob
	public String getOtros() {
		return otros;
	}

	public void setOtros(String otros) {
		this.otros = otros;
	}

	@Column(name = "responsable")
	@Lob
	public String getResponsable() {
		return responsable;
	}

	public void setResponsable(String responsable) {
		this.responsable = responsable;
	}

	@Column(name = "requerimientos")
	@Lob
	public String getRequerimientos() {
		return requerimientos;
	}

	public void setRequerimientos(String requerimientos) {
		this.requerimientos = requerimientos;
	}

	@Column(name = "formato")
	public String getFormato() {
		return formato;
	}

	public void setFormato(String formato) {
		this.formato = formato;
	}

	@Column(name = "distribuidor")
	public String getDistribuidor() {
		return distribuidor;
	}

	public void setDistribuidor(String distribuidor) {
		this.distribuidor = distribuidor;
	}

	@Column(name = "informacion")
	@Lob
	public String getInformacion() {
		return informacion;
	}

	public void setInformacion(String informacion) {
		this.informacion = informacion;
	}

	@Column(name = "referencia")
	public String getReferencia() {
		return referencia;
	}

	public void setReferencia(String referencia) {
		this.referencia = referencia;
	}

	@Column(name = "informacion_geo_localizacion")
	public String getInformacionGeolocalizacion() {
		return informacionGeolocalizacion;
	}

	public void setInformacionGeolocalizacion(String informacionGeolocalizacion) {
		this.informacionGeolocalizacion = informacionGeolocalizacion;
	}

	@Column(name = "calidad")
	public String getCalidad() {
		return calidad;
	}

	public void setCalidad(String calidad) {
		this.calidad = calidad;
	}

	@Column(name = "informacion_cpr")
	public String getInformacionColeccionPuntoReferencia() {
		return informacionColeccionPuntoReferencia;
	}

	public void setInformacionColeccionPuntoReferencia(
			String informacionColeccionPuntoReferencia) {
		this.informacionColeccionPuntoReferencia = informacionColeccionPuntoReferencia;
	}

	@Column(name = "informacion_pr")
	public String getInformacionPuntoReferencia() {
		return informacionPuntoReferencia;
	}

	public void setInformacionPuntoReferencia(String informacionPuntoReferencia) {
		this.informacionPuntoReferencia = informacionPuntoReferencia;
	}

	@Column(name = "nombre_pr")
	public String getNombrePuntoReferencia() {
		return nombrePuntoReferencia;
	}

	public void setNombrePuntoReferencia(String nombrePuntoReferencia) {
		this.nombrePuntoReferencia = nombrePuntoReferencia;
	}

	@Column(name = "crs_pr")
	public String getCrsPuntoReferencia() {
		return crsPuntoReferencia;
	}

	public void setCrsPuntoReferencia(String crsPuntoReferencia) {
		this.crsPuntoReferencia = crsPuntoReferencia;
	}

	@Column(name = "puntos_referencia")
	public String getPuntosDeReferencia() {
		return puntosDeReferencia;
	}

	public void setPuntosDeReferencia(String puntosDeReferencia) {
		this.puntosDeReferencia = puntosDeReferencia;
	}

	@Column(name = "posicion")
	public String getPosicion() {
		return posicion;
	}

	public void setPosicion(String posicion) {
		this.posicion = posicion;
	}

	@Column(name = "precision")
	public String getPrecision() {
		return precision;
	}

	public void setPrecision(String precision) {
		this.precision = precision;
	}

	// Aqui
	@OneToMany(mappedBy = "metadata", cascade = { CascadeType.PERSIST,
			CascadeType.MERGE })
	public List<LayerEntity> getLayer() {
		return layer;
	}

	public void setLayer(List<LayerEntity> layer) {
		this.layer = layer;
	}

}
