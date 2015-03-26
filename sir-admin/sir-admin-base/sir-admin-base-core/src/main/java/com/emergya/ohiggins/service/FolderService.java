/*
 * FolderService.java
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
package com.emergya.ohiggins.service;

import java.io.Serializable;
import java.util.List;

import com.emergya.ohiggins.dto.FolderDto;
import com.emergya.ohiggins.dto.FolderTypeDto;
import com.emergya.ohiggins.dto.NodeLayerTree;
import com.emergya.persistenceGeo.service.AbstractService;

/**
 * Interface para folder
 * 
 * @author jariera
 * 
 */
public interface FolderService extends AbstractService, Serializable {

	public static final Long DEFAULT_FOLDER_TYPE = new Long(1);

	/**
	 * 
	 * @return Devuelve lista de folder por id de autoridad
	 * @param idAuth
	 *            Identificador autoridad
	 */
	public List<FolderDto> getFolderByAuthorityId(Long idAuth);

	/**
	 * 
	 * @return Devuelve lista de folder que tengan como padre Id
	 * @param id
	 *            Identificador del padre
	 */
	public List<FolderDto> getFolderByParentId(Long idPadre);

	/**
	 * 
	 * @return Devuelve lista de folder que sean del usuario
	 * @param id
	 *            Identificador del usuario
	 */
	public List<FolderDto> getFolderByUserId(Long idUser);

	/**
	 * 
	 * @return Devuelve lista de folder que son padres, es decir no tienen hijos
	 *         (folder_parent_id is null)
	 * @param id
	 *            Identificador del padre
	 */
	public List<FolderDto> getFolderParent();

	/**
	 * Arbol de capas
	 * 
	 * @param first
	 * @param last
	 * @param propertyName
	 * @return
	 */
	public List<NodeLayerTree> getArbolCapas(Integer first, Integer last,
			String propertyName);

	/**
	 * 
	 * @param idFolder
	 * 			Identificador del tipo de la carpeta
	 * @return FolderTypeDto
	 * 			Devuelve el FolderTypeDto que coincida con el id
	 */
	public FolderTypeDto getFolderTypeDto(Long idFolder);

	/**
	 * @return List<FolderTypeDto>
	 * 			Devuelve la lista de todos los folder types
	 */
	public List<FolderTypeDto> getAllFolderType();
}
