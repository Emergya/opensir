/*
 * OhigginsLayerEntityDao.java
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

import com.emergya.ohiggins.model.AuthorityEntity;
import java.util.List;

import com.emergya.ohiggins.model.LayerEntity;
import com.emergya.persistenceGeo.dao.GenericDAO;

/**
 * Dao para Layer
 * 
 * @author <a href="mailto:jariera@emergya.com">jariera</a>
 * 
 */
public interface OhigginsLayerEntityDao extends GenericDAO<LayerEntity, Long> {
	
	/**
	 * Layers by layer type
	 * 
	 * @param idLayerType
	 * 
	 * @return layers with layer type given
	 */
	public List<LayerEntity> getLayersByLayerType(Long idLayerType);

	/**
	 * Layers by layer type
	 * 
	 * @param idLayerType
	 * @param layerName
	 * 
	 * @return layers with layer type and layer name given
	 */
	public List<LayerEntity> getLayersByLayerTypeAndLayerName(Long idLayerType, String layerName);

	/**
	 * Lista de capas autoridad
	 * 
	 * @param first
	 * @param last
	 * @param propertyName
	 * @return
	 */
	public List<LayerEntity> findAllFromToOrderBy(Integer first, Integer last,
			String propertyName, Long idAuth);

	/**
	 * Lista de capas de un folder
	 * 
	 * @param first
	 * @param last
	 * @param propertyName
	 * @return
	 */
	public List<LayerEntity> getLayersByFolderId(Long idFolder);

	/**
	 * Obtiene la lista de carpetas de un folder
	 * 
	 * @param idFolder
	 *            El id de la carpeta cuyas capas se devuelven.
	 * @param folderOrder
	 *            Si true, las carpetas se ordena por el orden de carpeta. Sino,
	 *            por orden alfabético.
	 * @return
	 */
	public List<LayerEntity> getLayersByFolderId(Long idFolder,
			boolean folderOrder);

	/**
	 * Lista de capas de un folder de una determinada autoridad
	 * 
	 * @param first
	 * @param last
	 * @param propertyName
	 * @return
	 */
	public List<LayerEntity> getLayersByFolderIdByAuthId(Long idFolder,
			Long idAuth);

	/**
	 * Lista de capas publicadas de una autoridad y un tipo determinado (raster,
	 * vectorial)
	 * 
	 * @param first
	 * @param last
	 * @param propertyName
	 * @return
	 */
	public List<LayerEntity> getLayersPubliciedByAuthIdByTypeId(Long idAuth,
			Long type);

	/**
	 * Lista de capas no asignada a ninguna carpeta;
	 * 
	 * @param justPublicized
	 *            Si true, solo se devuelven las capas con "publicized=true".
	 *            Sino, se devuelven todas.
	 * @return
	 */
	public List<LayerEntity> getUnassignedLayers(boolean justPublicized);

	/**
	 * Lista de capas no asignada a ninguna carpeta;
	 * 
	 * @param justPublicized
	 *            Si true, solo se devuelven las capas con "publicized=true".
	 *            Sino, se devuelven todas.
	 * @param folderOrder
	 *            Si true, las capas se devuelven ordenadas por el orden de
	 *            carpeta. Sino, alfabéticamente.
	 * @return
	 */
	public List<LayerEntity> getUnassignedLayers(boolean justPublicized,
			boolean folderOrder);
	
	/**
	 * Obtain number of layers like layerName
	 * 
	 * @param layerName
	 * 
	 * @return 0 if not found or number of layers with a name ilike layerName
	 */
	public Long getLayerCountByName(String layerName);

	/**
	 * 
	 * @param geoserverName
	 * @return
	 */
	public LayerEntity getByName(String geoserverName);

    public List<LayerEntity> getByAuth(AuthorityEntity authEntity);

    public List<LayerEntity> getByRequestedByAuth(AuthorityEntity authEntity);
}
