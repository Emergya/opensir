/*
 * OhigginsFolderEntityDao.java
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
 * Authors:: Jose Alfonso (mailto:jariera@emergya.com)
 */
package com.emergya.ohiggins.dao;

import java.util.List;

import com.emergya.ohiggins.model.FolderEntity;
import com.emergya.persistenceGeo.dao.GenericDAO;

/**
 * Dao para Folder
 * 
 * @author jariera
 * 
 */
public interface OhigginsFolderEntityDao extends GenericDAO<FolderEntity, Long> {

	/**
	 * 
	 * @return Devuelve lista de folder por id de autoridad
	 */
	public List<FolderEntity> getFolderByAuthorityId(Long idAuth);

	/**
	 * 
	 * @return Devuelve lista de folder que tengan como padre Id
	 * @param id
	 *            Identificador del padre
	 */
	public List<FolderEntity> getFolderByParentId(Long idPadre);

	/**
	 * 
	 * @return Devuelve lista de folder que sean del usuario
	 * @param id
	 *            Identificador del usuario
	 */
	public List<FolderEntity> getFolderByUserId(Long idUser);

	/**
	 * 
	 * @return Devuelve lista de folder que son padres, es decir no tienen hijos
	 *         (folder_parent_id is null)
	 * @param id
	 *            Identificador del padre
	 */
	public List<FolderEntity> getFolderParent();

	/**
	 * Arbol de capas
	 * 
	 * @param first
	 * @param last
	 * @param propertyName
	 * @return
	 */
	public List<FolderEntity> getArbolCapas(Integer first, Integer last,
			String propertyName);
}
