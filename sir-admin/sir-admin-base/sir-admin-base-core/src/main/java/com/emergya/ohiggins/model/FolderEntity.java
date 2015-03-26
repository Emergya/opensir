/*
 * FolderEntity.java
 * 
 * Copyright (C) 2011
 * 
 * This file is part of Proyecto persistenceGeo
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
 * Authors:: Moisés Arcos Santiago (mailto:marcos@emergya.com)
 */
package com.emergya.ohiggins.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.emergya.persistenceGeo.metaModel.AbstractFolderEntity;

/**
 * Entidad de carpeta.
 * 
 * @author <a href="mailto:marcos@emergya.com">marcos</a>
 * @author <a href="mailto:jlrodriguez@emergya.com">jlrodriguez</a>
 * @author <a href="mailto:adiaz@emergya.com">adiaz</a>
 * 
 */
@Entity
@Table(name = "gis_folder")
public class FolderEntity extends AbstractFolderEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7230829079248633279L;

	/**
	 * Constructor por defecto.
	 */
	public FolderEntity() {

	}

	/**
	 * Constructor que crea un {@link FolderEntity} con el nombre pasado como
	 * parámetro.
	 * 
	 * @param folderName
	 *            el nombre de la carpeta.
	 */
	public FolderEntity(final String folderName) {
		name = folderName;
	}

	/**
	 * Clave primaria de la tabla.
	 * 
	 * @return la clave primaria del objeto.
	 */
	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "folder_seq")
	@SequenceGenerator(name = "folder_seq", sequenceName = "folder_seq", initialValue = 100)
	@Override
	public Long getId() {
		return id;
	}

	/**
	 * El nombre de la carpeta.
	 * 
	 * @return el nombre.
	 */
	@Column(name = "name")
	public String getName() {
		return name;
	}

	/**
	 * Indica si la carpeta está activada o no. Una carpeta que no esté activa
	 * no se presentará a los usuarios no administradores en su árbol de capas.
	 * 
	 * @return <code>true</code> si la carpeta está habilitada o
	 *         <code>false</code> en caso contrario.
	 */
	@Column(name = "enabled")
	public Boolean getEnabled() {
		return enabled;
	}

	/**
	 * Indica si la carpeta es un canal temático o no.
	 * 
	 * @return <code>true</code> si la carpeta es un canal temático,
	 *         <code>false</code> en caso contrario.
	 */
	@Column(name = "is_channel")
	public Boolean getIsChannel() {
		return isChannel;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.emergya.persistenceGeo.metaModel.AbstractFolderEntity#getCreateDate()
	 */
	@Column(name = "create_date")
	public Date getCreateDate() {
		return createDate;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.emergya.persistenceGeo.metaModel.AbstractFolderEntity#getUpdateDate()
	 */
	@Column(name = "update_date")
	public Date getUpdateDate() {
		return updateDate;
	}

	@Column(name = "folder_order")
	@Override
	public Integer getFolderOrder() {
		return this.folderOrder;

	}

	@ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.MERGE)
	@JoinColumn(name = "folder_zone_id")
	public ZoneEntity getZone() {
		return (ZoneEntity) zone;
	}

	@ManyToOne(cascade = CascadeType.MERGE, fetch = FetchType.LAZY)
	@JoinColumn(name = "folder_auth_id")
	public AuthorityEntity getAuthority() {
		return (AuthorityEntity) authority;
	}

	@ManyToOne(cascade = CascadeType.MERGE, fetch = FetchType.LAZY)
	@JoinColumn(name = "folder_user_id")
	public UserEntity getUser() {
		return (UserEntity) user;
	}

	@ManyToOne(cascade = CascadeType.MERGE, fetch = FetchType.LAZY)
	@JoinColumn(name = "folder_parent_id")
	public FolderEntity getParent() {
		return (FolderEntity) parent;
	}

	@Override
	public void setId(final Serializable id) {
		this.id = (Long) id;
	}

	@ManyToOne(fetch = FetchType.LAZY,
			cascade = CascadeType.MERGE)
	@JoinColumn(name="folder_type_id")
	public FolderTypeEntity getFolderType() {
		return (FolderTypeEntity) folderType;
	}

}
