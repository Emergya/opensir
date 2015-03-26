/*
 * FaqDto.java
 * 
 * Copyright (C) 2012
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
 * Authors:: Jose Alfonso Riera (mailto:jariera@emergya.com)
 */
package com.emergya.ohiggins.dto;

import java.io.Serializable;
import java.util.Date;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.NotBlank;
import org.jsoup.Jsoup;
import org.jsoup.safety.Whitelist;

/**
 * Dto Faq
 * 
 * @author jariera
 * @version 1.0
 */
public class FaqDto implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8610295209367639031L;
	public static final String TITULO_PROPERTY = "titulo";
	public static final int TAM = 100;

	private Long id;
	private Boolean habilitada;
	private String titulo;
	private String modulo;
	private String respuesta;
	private String respuestaRecortada;
	private Date fechaCreacion;
	private Date fechaActualizacion;
	private String moduloSeleccionado;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Boolean getHabilitada() {
		return habilitada;
	}

	public void setHabilitada(Boolean habilitada) {
		this.habilitada = habilitada;
	}

	@NotBlank
	@NotNull
	@Size(min = 1, max = 250)
	public String getTitulo() {
		return titulo;
	}

	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}
	
	@NotBlank
	@NotNull
	public String getModulo() {
		return modulo;
	}

	public void setModulo(String modulo) {
		this.modulo = modulo;
	}

	@NotBlank
	@NotNull
	public String getRespuesta() {
		return respuesta;
	}

	public void setRespuesta(String respuesta) {
		this.respuesta = respuesta;
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

	/**
	 * Obtiene el valor de la respuesta, pero solo obtiene un parte del
	 * contenido para mostrarla en la vista.
	 * 
	 * @return
	 */
	public String getRespuestaRecortada() {
		respuestaRecortada = html2text(respuesta);

		//respuestaRecortada = cortarRespuesta(respuestaRecortada);

		return respuestaRecortada;
	}


	public void setRespuestaRecortada(String respuestaRecortada) {
		this.respuestaRecortada = respuestaRecortada;
	}

	public String getModuloSeleccionado() {
		return moduloSeleccionado;
	}

	public void setModuloSeleccionado(String moduloSeleccionado) {
		this.moduloSeleccionado = moduloSeleccionado;
	}

	/**
	 * Convierte de html a texto
	 * 
	 * @param html
	 * @return
	 */
	public static String html2text(String html) {
		return Jsoup.clean(html, Whitelist.none());
	}
	
	/**
	 * Corta el texto si excediera numero de caracteres
	 * 
	 * @param text
	 * @return
	 */
	private String cortarRespuesta(String text) {
		if (text != null && text.length() > 0
				&& text.length() > TAM - 1) {
			return text.substring(0, TAM - 1);
		}
		return text;
	}


}
