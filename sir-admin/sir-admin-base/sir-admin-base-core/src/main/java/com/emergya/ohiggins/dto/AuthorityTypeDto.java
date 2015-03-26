package com.emergya.ohiggins.dto;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class AuthorityTypeDto implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1912295259367639000L;

	private Long id;
	private String name;
	private Date fechaCreacion;
	private Date fechaActualizacion;
	private Boolean esCiudadano;
	private List<AuthorityDto> authorities;
	private List<PermisoDto> permisos;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Date getFechaCreacion() {
		return fechaCreacion;
	}

	public void setFechaCreacion(Date fechaCreacion) {
		this.fechaCreacion = fechaCreacion;
	}

	public Date getFechaActualizacion() {
		return fechaActualizacion;
	}

	public void setFechaActualizacion(Date fechaActualizacion) {
		this.fechaActualizacion = fechaActualizacion;
	}

	public List<AuthorityDto> getAuthorities() {
		return authorities;
	}

	public void setAuthorities(List<AuthorityDto> authorities) {
		this.authorities = authorities;
	}

	public List<PermisoDto> getPermisos() {
		return permisos;
	}

	public void setPermisos(List<PermisoDto> permisos) {
		this.permisos = permisos;
	}

	public Boolean getEsCiudadano() {
		return esCiudadano;
	}

	public void setEsCiudadano(Boolean esCiudadano) {
		this.esCiudadano = esCiudadano;
	}

}
