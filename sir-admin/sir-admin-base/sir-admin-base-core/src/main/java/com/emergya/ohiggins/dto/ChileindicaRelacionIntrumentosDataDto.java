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
package com.emergya.ohiggins.dto;

import java.io.Serializable;

/**
 * 
 * @author <a href="mailto:jlrodriguez@emergya.com">Juan Luis Rodríguez
 *         Ponce</a>
 */
public class ChileindicaRelacionIntrumentosDataDto implements Serializable {

	private static final long serialVersionUID = 7825623217969534859L;
	private Long id;
	private Integer codigoInstrumento;
	private String nombreInstrumento;
	private String especificacion;
	private String relacionPrincipalCodigo;
	private String relacionPrincipalNombre;
	private String relacionAsociadaCodigo;
	private String relacionAsociadaNombre;
	private ChileindicaInversionDataDto inversionData;

	public ChileindicaRelacionIntrumentosDataDto() {
	}

	public ChileindicaRelacionIntrumentosDataDto(Long id) {
		this.id = id;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Integer getCodigoInstrumento() {
		return codigoInstrumento;
	}

	public void setCodigoInstrumento(Integer codigoInstrumento) {
		this.codigoInstrumento = codigoInstrumento;
	}

	public String getNombreInstrumento() {
		return nombreInstrumento;
	}

	public void setNombreInstrumento(String nombreInstrumento) {
		this.nombreInstrumento = nombreInstrumento;
	}

	public String getEspecificacion() {
		return especificacion;
	}

	public void setEspecificacion(String especificacion) {
		this.especificacion = especificacion;
	}

	public String getRelacionPrincipalCodigo() {
		return relacionPrincipalCodigo;
	}

	public void setRelacionPrincipalCodigo(String relacionPrincipalCodigo) {
		this.relacionPrincipalCodigo = relacionPrincipalCodigo;
	}

	public String getRelacionPrincipalNombre() {
		return relacionPrincipalNombre;
	}

	public void setRelacionPrincipalNombre(String relacionPrincipalNombre) {
		this.relacionPrincipalNombre = relacionPrincipalNombre;
	}

	public String getRelacionAsociadaCodigo() {
		return relacionAsociadaCodigo;
	}

	public void setRelacionAsociadaCodigo(String relacionAsociadaCodigo) {
		this.relacionAsociadaCodigo = relacionAsociadaCodigo;
	}

	public String getRelacionAsociadaNombre() {
		return relacionAsociadaNombre;
	}

	public void setRelacionAsociadaNombre(String relacionAsociadaNombre) {
		this.relacionAsociadaNombre = relacionAsociadaNombre;
	}

	public ChileindicaInversionDataDto getInversionData() {
		return inversionData;
	}

	public void setInversionData(ChileindicaInversionDataDto inversionData) {
		this.inversionData = inversionData;
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
		if (!(object instanceof ChileindicaRelacionIntrumentosDataDto)) {
			return false;
		}
		ChileindicaRelacionIntrumentosDataDto other = (ChileindicaRelacionIntrumentosDataDto) object;
		if ((this.id == null && other.id != null)
				|| (this.id != null && !this.id.equals(other.id))) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "ChileindicaRelacionIntrumentosDataDto[ id=" + id + " ]";
	}

}
