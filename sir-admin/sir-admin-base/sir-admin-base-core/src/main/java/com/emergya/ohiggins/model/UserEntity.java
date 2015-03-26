/*
 * UserEntity.java
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
 * Authors:: Alejandro Díaz Torres (mailto:adiaz@emergya.com)
 * Authors:: Juan Luis Rodríguez Ponce (mailto:jlrodriguez@emergya.com)
 */
package com.emergya.ohiggins.model;

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
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import com.emergya.persistenceGeo.metaModel.AbstractUserEntity;

/**
 * Entidad de usuario.
 * 
 * @author <a href="mailto:adiaz@emergya.com">adiaz</a>
 * @author <a href="mailto:jlrodriguez@emergya.com">jlrodriguez</a>
 * 
 */
@Entity
@Table(name = "gis_user")
public class UserEntity extends AbstractUserEntity {
	public static enum Names {
		USER_ID("id"), USERNAME("username"), PASSWORD("password"), NOMBRE_COMPLETO(
				"nombreCompleto"), APELLIDOS("apellidos"), EMAIL("email"), TELEFONO(
				"telefono"), ADMIN("admin"), VALID("valid"), CREATE_DATE(
				"createDate"), UPDATE_DATE("updateDate"), AUTHORITY("authority"), REGIONUSERS(
				"regionUsers");

		private String name;

		private Names(String name) {
			this.name = name;
		}

		@Override
		public String toString() {
			return this.name;
		}

	};

	/**
	 * 
	 */
	private static final long serialVersionUID = -6272520927189358861L;

	private AuthorityEntity authority;
	private List<RegionUserRelationship> regionUsers = new LinkedList<RegionUserRelationship>();;

	/**
	 * Constructor por defecto.
	 */
	public UserEntity() {

	}

	/**
	 * Crea un usuario con un nombre de usuario.
	 * 
	 * @param username
	 *            el nombre de usuario.
	 */
	public UserEntity(String username) {
		this.username = username;
	}

	/**
	 * Devuelve la clave primaria del objeto.
	 * 
	 * @return el id del usuario.
	 */
	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "gis_user_seq")
	@SequenceGenerator(name = "gis_user_seq", sequenceName = "gis_user_seq", initialValue = 100)
	@Override
	public Long getId() {
		return this.id;
	}

	/**
	 * Nombre del usuario. Único en la tabla de base de datos.
	 * 
	 * @return el nombre de usuario.
	 */
	@Column(name = "username", nullable = false)
	@Override
	public String getUsername() {
		return username;
	}

	/**
	 * Devuelve la contraseña.
	 * 
	 * @return la contraseña del usuario.
	 */
	@Column(name = "password", nullable = false)
	@Override
	public String getPassword() {
		return password;
	}

	/**
	 * Devuelve el nombre completo.
	 * 
	 * @return el nombre completo.
	 */
	@Column(name = "nombre_completo")
	public String getNombreCompleto() {
		return nombreCompleto;
	}

	/**
	 * 
	 * @return
	 */
	@Column(name = "apellidos")
	public String getApellidos() {
		return apellidos;
	}

	/**
	 * 
	 * @return
	 */
	@Column(name = "email")
	public String getEmail() {
		return email;
	}

	/**
	 * El número de teléfono de contacto.
	 * 
	 * @return el número de teléfono del usuario.
	 */
	@Column(name = "telefono")
	public String getTelefono() {
		return telefono;
	}

	/**
	 * Indica si el usuario tiene el rol administrador o no.
	 * 
	 * @return <code>true</code> si el usuario es administrador o
	 *         <code>false</code> en caso contrario.
	 */
	@Column(name = "admin")
	public Boolean getAdmin() {
		return admin;
	}

	/**
	 * Indica si el usuario está habilitado o no.
	 * 
	 * @return <code>true</code> si el usuario está habilitado,
	 *         <code>false</code> en caso contrario.
	 */
	@Column(name = "valid")
	public Boolean getValid() {
		return valid;
	}

	/**
	 * Fecha de creación.
	 * 
	 * @return la fecha de creación
	 */
	@Column(name = "create_date")
	public Date getCreateDate() {
		return createDate;
	}

	/**
	 * Fecha de última actualización.
	 * 
	 * @return fecha de última actualización.
	 */
	@Column(name = "update_date")
	public Date getUpdateDate() {
		return updateDate;
	}

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "user_authority_id")
	public AuthorityEntity getAuthority() {
		return authority;
	}

	public void setAuthority(AuthorityEntity authority) {
		this.authority = authority;
	}

	@OneToMany(mappedBy = "ru.user", cascade = CascadeType.ALL)
	@LazyCollection(LazyCollectionOption.FALSE)
	public List<RegionUserRelationship> getRegionUsers() {
		return this.regionUsers;
	}

	public void setRegionUsers(List<RegionUserRelationship> regionUsers) {
		this.regionUsers = regionUsers;
	}

	@Override
	public int hashCode() {
		HashCodeBuilder hcb = new HashCodeBuilder();
		hcb.append(id);
		hcb.append(username);
		hcb.append(password);
		hcb.append(nombreCompleto);
		hcb.append(apellidos);
		hcb.append(email);
		hcb.append(telefono);
		hcb.append(admin);
		hcb.append(valid);
		hcb.append(createDate);
		hcb.append(updateDate);
		return hcb.toHashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!(obj instanceof UserEntity)) {
			return false;
		}
		UserEntity other = (UserEntity) obj;
		EqualsBuilder eb = new EqualsBuilder();
		eb.append(id, other.id);
		eb.append(username, other.username);
		eb.append(password, other.password);
		eb.append(nombreCompleto, other.nombreCompleto);
		eb.append(apellidos, other.apellidos);
		eb.append(email, other.email);
		eb.append(telefono, other.telefono);
		eb.append(admin, other.admin);
		eb.append(valid, other.valid);
		eb.append(createDate, other.createDate);
		eb.append(updateDate, other.updateDate);
		return eb.isEquals();
	}

}