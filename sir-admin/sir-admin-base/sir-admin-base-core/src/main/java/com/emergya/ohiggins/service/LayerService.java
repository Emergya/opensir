/*
 * LayerService.java
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

import com.emergya.ohiggins.dto.LayerDto;
import com.emergya.persistenceGeo.service.AbstractService;

/**
 * Interface para el manejo de layer
 * 
 * @author jariera
 * 
 */
public interface LayerService extends AbstractService, Serializable {
	
	/**
	 * Layers by layer type
	 * 
	 * @param idLayerType
	 * 
	 * @return layers with layer type given
	 */
	public List<LayerDto> getLayersByLayerType(Long idLayerType);

	/**
	 * Layers by layer type
	 * 
	 * @param idLayerType
	 * @param layerName
	 * 
	 * @return layers with layer type and layer name given
	 */
	public List<LayerDto> getLayersByLayerTypeAndLayerName(Long idLayerType, String layerName);

	/**
	 * Lista de capas autoridad
	 * 
	 * @param first
	 * @param last
	 * @param propertyName
	 * @return
	 */
	public List<LayerDto> getFromToOrderBy(Integer first, Integer last,
			String propertyName, Long idAuth);

	/**
	 * Lista de capas de un folder
	 * 
	 * @param first
	 * @param last
	 * @param propertyName
	 * @return
	 */
	public List<LayerDto> getLayersByFolderId(Long idFolder);

	/**
	 * Devuelve las capas asignadas a una carpeta.
	 * 
	 * @param idFolder
	 *            La carpeta cuyas capas se devuelven.
	 * @param layerOrder
	 *            Si las capas se devolverán ordenadas por su campo orden o
	 *            alfabéticamente por nombre .
	 * @return
	 */
	public List<LayerDto> getLayersByFolderId(Long idFolder, boolean layerOrder);

	/**
	 * Lista de capas de un folder de una determinada autoridad
	 * 
	 * @param first
	 * @param last
	 * @param propertyName
	 * @return
	 */
	public List<LayerDto> getLayersByFolderIdByAuthId(Long idFolder, Long idAuth);

	/**
	 * Lista de capas publicadas de una autoridad y un tipo determinado (raster,
	 * vectorial)
	 * 
	 * @param first
	 * @param last
	 * @param propertyName
	 * @return
	 */
	public List<LayerDto> getLayersPubliciedByAuthIdByTypeId(Long idAuth,
			Long type);

	/**
	 * Lista de capas no asignadas a ninguna capa/carpeta.
	 * 
	 * @param justPublicized
	 *            Si true, sólo se devuelven las capas con "publicized=true".
	 * @param layerOrder
	 *            Si las capas se devolverán ordenadas por su campo orden, o
	 *            alfabéticamente por nombre.
	 * @return
	 */
	public List<LayerDto> getUnassignedLayers(boolean justPublicized,
			boolean layerOrder);
	
	/**
	 * Update only simple properties for a layer
	 * 
	 * @param layer
	 * 
	 * @return layer updated
	 */
	public LayerDto simpleUpdate(LayerDto layer);
	
	/**
	 * Obtain number of layers like layerName
	 * 
	 * @param layerName
	 * 
	 * @return 0 if not found or number of layers with a name ilike layerName
	 */
	public Long getLayerCountByName(String layerName);

	/**
	 * Gets a layer by the name it has in geoserver.
	 * @param geoserverName
	 * @return
	 */
	public LayerDto getByName(String geoserverName);

}
