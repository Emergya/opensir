/* InversionUtilDto.java
 * 
 * Copyright (C) 2012
 * 
 * This file is part of project ohiggins-core
 * 
 * This software is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 * 
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301  USA
 *
 * As a special exception, if you link this library with other files to
 * produce an executable, this library does not by itself cause the
 * resulting executable to be covered by the GNU General Public License.
 * This exception does not however invalidate any other reasons why the
 * executable file might be covered by the GNU General Public License.
 * 
 * Authors:: Juan Luis Rodríguez Ponce (mailto:jlrodriguez@emergya.com)
 */
package com.emergya.ohiggins.dto;

import java.io.Serializable;

import com.vividsolutions.jts.geom.Geometry;

/**
 * @author jlrodriguez
 * 
 */
public class InversionUtilDto implements Serializable {
	private static final long serialVersionUID = -1663030314498459557L;
	private String codBip;
	private String etapa;
	private String serRes;
	private String anyo;
	private Geometry geometry;

	public InversionUtilDto() {

	}

	public InversionUtilDto(String codBip, String etapa, String serRes,
			String anyo, Geometry geometry) {
		super();
		this.codBip = codBip;
		this.etapa = etapa;
		this.serRes = serRes;
		this.anyo = anyo;
		this.geometry = geometry;
	}

	public String getCodBip() {
		return codBip;
	}

	public void setCodBip(String codBip) {
		this.codBip = codBip;
	}

	public String getEtapa() {
		return etapa;
	}

	public void setEtapa(String etapa) {
		this.etapa = etapa;
	}

	public String getSerRes() {
		return serRes;
	}

	public void setSerRes(String serRes) {
		this.serRes = serRes;
	}

	public String getAnyo() {
		return anyo;
	}

	public void setAnyo(String anyo) {
		this.anyo = anyo;
	}

	public Geometry getGeometry() {
		return geometry;
	}

	public void setGeometry(Geometry geometry) {
		this.geometry = geometry;
	}
}