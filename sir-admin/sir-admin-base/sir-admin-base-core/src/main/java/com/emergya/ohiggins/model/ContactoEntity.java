/*
 * ContactoEntity.java
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
package com.emergya.ohiggins.model;

import java.io.Serializable;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

/**
 * Entidad de Contacto
 * 
 * @author <a href="mailto:jariera@emergya.com">jariera</a>
 * 
 */
@Entity
@Table(name = "gis_contacto")
public class ContactoEntity implements Serializable {

	private static final long serialVersionUID = -9192529929189358111L;

	private Long id;
	private String titulo;
	private String nombre;
	private String email;
	private String descripcion;

	private Boolean leido;

	private Date fechaCreacion;
	private Date fechaActualizacion;

	private List<RegionContactoRelationship> regionContactos = new LinkedList<RegionContactoRelationship>();;

	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.AUTO)
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Column(name = "fecha_creacion")
	public Date getFechaCreacion() {
		return fechaCreacion;
	}

	public void setFechaCreacion(Date fechaCreacion) {
		this.fechaCreacion = fechaCreacion;
	}

	@Column(name = "fecha_actualizacion")
	public Date getFechaActualizacion() {
		return fechaActualizacion;
	}

	public void setFechaActualizacion(Date fechaActualizacion) {
		this.fechaActualizacion = fechaActualizacion;
	}

	@Column(name = "titulo", nullable = false)
	public String getTitulo() {
		return titulo;
	}

	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}

	@Column(name = "nombre")
	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	@Column(name = "email", nullable = false)
	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	@Column(name = "descripcion", nullable = false)
	@Lob
	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public Boolean getLeido() {
		return leido;
	}

	public void setLeido(Boolean leido) {
		this.leido = leido;
	}

	@OneToMany(mappedBy = "rc.contacto", cascade = CascadeType.ALL)
	@LazyCollection(LazyCollectionOption.FALSE)
	public List<RegionContactoRelationship> getRegionContactos() {
		return this.regionContactos;
	}

	public void setRegionContactos(
			List<RegionContactoRelationship> regionContactos) {
		this.regionContactos = regionContactos;
	}

	@Override
	public int hashCode() {
		HashCodeBuilder hcb = new HashCodeBuilder();
		hcb.append(id);
		hcb.append(titulo);
		hcb.append(descripcion);
		hcb.append(nombre);
		hcb.append(email);
		hcb.append(leido);
		hcb.append(fechaCreacion);
		hcb.append(fechaActualizacion);
		return hcb.toHashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!(obj instanceof ContactoEntity)) {
			return false;
		}
		ContactoEntity other = (ContactoEntity) obj;
		EqualsBuilder eb = new EqualsBuilder();
		eb.append(id, other.id);
		eb.append(titulo, other.titulo);
		eb.append(descripcion, other.descripcion);
		eb.append(nombre, other.nombre);
		eb.append(email, other.email);
		eb.append(leido, other.leido);
		eb.append(fechaCreacion, other.fechaCreacion);
		eb.append(fechaActualizacion, other.fechaActualizacion);
		return eb.isEquals();
	}
}
