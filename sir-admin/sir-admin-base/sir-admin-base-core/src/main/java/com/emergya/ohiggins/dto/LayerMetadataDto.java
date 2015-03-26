package com.emergya.ohiggins.dto;

import java.io.Serializable;
import java.util.Date;

public class LayerMetadataDto implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 6478969590479483598L;
    private Long id;
    private Date fechacreacion;
    private Date fechaactualizacion;
    // Metadatos para capas vectoriales
    // Informacion de mantenimiento
    private String frecuencia;
    private String siguiente; // tipo fecha
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

    public Long getId() {
	return id;
    }

    public void setId(Long id) {
	this.id = id;
    }

    public Date getFechacreacion() {
	return fechacreacion;
    }

    public void setFechacreacion(Date fechacreacion) {
	this.fechacreacion = fechacreacion;
    }

    public Date getFechaactualizacion() {
	return fechaactualizacion;
    }

    public void setFechaactualizacion(Date fechaactualizacion) {
	this.fechaactualizacion = fechaactualizacion;
    }

    public String getFrecuencia() {
	return frecuencia;
    }

    public void setFrecuencia(String frecuencia) {
	this.frecuencia = frecuencia;
    }

    public String getSiguiente() {
	return siguiente;
    }

    public void setSiguiente(String siguiente) {
	this.siguiente = siguiente;
    }

    public String getPeriodo() {
	return periodo;
    }

    public void setPeriodo(String periodo) {
	this.periodo = periodo;
    }

    public String getRango() {
	return rango;
    }

    public void setRango(String rango) {
	this.rango = rango;
    }

    public String getOtros() {
	return otros;
    }

    public void setOtros(String otros) {
	this.otros = otros;
    }

    public String getResponsable() {
	return responsable;
    }

    public void setResponsable(String responsable) {
	this.responsable = responsable;
    }

    public String getRequerimientos() {
	return requerimientos;
    }

    public void setRequerimientos(String requerimientos) {
	this.requerimientos = requerimientos;
    }

    public String getFormato() {
	return formato;
    }

    public void setFormato(String formato) {
	this.formato = formato;
    }

    public String getDistribuidor() {
	return distribuidor;
    }

    public void setDistribuidor(String distribuidor) {
	this.distribuidor = distribuidor;
    }

    public String getInformacion() {
	return informacion;
    }

    public void setInformacion(String informacion) {
	this.informacion = informacion;
    }

    public String getReferencia() {
	return referencia;
    }

    public void setReferencia(String referencia) {
	this.referencia = referencia;
    }

    public String getInformacionGeolocalizacion() {
	return informacionGeolocalizacion;
    }

    public void setInformacionGeolocalizacion(String informacionGeolocalizacion) {
	this.informacionGeolocalizacion = informacionGeolocalizacion;
    }

    public String getCalidad() {
	return calidad;
    }

    public void setCalidad(String calidad) {
	this.calidad = calidad;
    }

    public String getInformacionColeccionPuntoReferencia() {
	return informacionColeccionPuntoReferencia;
    }

    public void setInformacionColeccionPuntoReferencia(
	    String informacionColeccionPuntoReferencia) {
	this.informacionColeccionPuntoReferencia = informacionColeccionPuntoReferencia;
    }

    public String getInformacionPuntoReferencia() {
	return informacionPuntoReferencia;
    }

    public void setInformacionPuntoReferencia(String informacionPuntoReferencia) {
	this.informacionPuntoReferencia = informacionPuntoReferencia;
    }

    public String getNombrePuntoReferencia() {
	return nombrePuntoReferencia;
    }

    public void setNombrePuntoReferencia(String nombrePuntoReferencia) {
	this.nombrePuntoReferencia = nombrePuntoReferencia;
    }

    public String getCrsPuntoReferencia() {
	return crsPuntoReferencia;
    }

    public void setCrsPuntoReferencia(String crsPuntoReferencia) {
	this.crsPuntoReferencia = crsPuntoReferencia;
    }

    public String getPuntosDeReferencia() {
	return puntosDeReferencia;
    }

    public void setPuntosDeReferencia(String puntosDeReferencia) {
	this.puntosDeReferencia = puntosDeReferencia;
    }

    public String getPosicion() {
	return posicion;
    }

    public void setPosicion(String posicion) {
	this.posicion = posicion;
    }

    public String getPrecision() {
	return precision;
    }

    public void setPrecision(String precision) {
	this.precision = precision;
    }

    public static LayerMetadataDto fromTmpMetadata(LayerMetadataTmpDto entity) {
	if (entity == null) {
	    return null;
	}

	LayerMetadataDto dto = new LayerMetadataDto();

	dto.setCalidad(entity.getCalidad());
	dto.setCrsPuntoReferencia(entity.getCrsPuntoReferencia());
	dto.setDistribuidor(entity.getDistribuidor());
	dto.setFechaactualizacion(entity.getFechaactualizacion());
	dto.setFechacreacion(entity.getFechacreacion());
	dto.setFormato(entity.getFormato());
	dto.setFrecuencia(entity.getFrecuencia());
	dto.setId(entity.getId());
	dto.setInformacion(entity.getInformacion());
	dto.setInformacionColeccionPuntoReferencia(entity
		.getInformacionColeccionPuntoReferencia());
	dto.setInformacionGeolocalizacion(entity
		.getInformacionGeolocalizacion());
	dto.setInformacionPuntoReferencia(entity
		.getInformacionPuntoReferencia());
	dto.setNombrePuntoReferencia(entity.getNombrePuntoReferencia());
	dto.setOtros(entity.getOtros());
	dto.setPeriodo(entity.getPeriodo());
	dto.setPosicion(entity.getPosicion());
	dto.setPrecision(entity.getPrecision());
	dto.setPuntosDeReferencia(entity.getPuntosDeReferencia());
	dto.setRango(entity.getRango());
	dto.setReferencia(entity.getReferencia());
	dto.setRequerimientos(entity.getRequerimientos());
	dto.setResponsable(entity.getResponsable());
	dto.setSiguiente(entity.getSiguiente());

	return dto;
    }
}
