/*
 * LayerTypeService.java
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

import com.emergya.ohiggins.dto.LayerTypeDto;
import com.emergya.ohiggins.model.LayerTypeEntity;
import com.emergya.persistenceGeo.service.AbstractService;

/**
 * Interface para el manejo de layer type
 * 
 * @author jariera
 * 
 */
public interface LayerTypeService extends AbstractService, Serializable {
	public static final Long WMS_RASTER_ID = 1L;
	public static final Long WFS_RASTER_ID = 2L;
	public static final Long KML_RASTER_ID = 3L;
	public static final Long WMS_VECTORIAL_ID = 4L;
	public static final Long WFS_VECTORIAL_ID = 5L;
	public static final Long KML_VECTORIAL_ID = 6L;
	public static final Long POSTGIS_VECTORIAL_ID = 7L;
	public static final Long GEOTIFF_RASTER_ID = 8L;
	public static final Long IMAGEMOSAIC_RASTERR_ID = 9L;
	public static final Long IMAGEWORLD_RASTER_ID = 10L;
	public static final Long XLS_VECTORIAL_ID = 11L;

	/**
	 * @param entity
	 * @return
	 */
	public LayerTypeDto entityToDto(LayerTypeEntity entity);

	/**
	 * @param dto
	 * @return
	 */
	public LayerTypeEntity dtoToEntity(LayerTypeDto dto);

	/**
	 * Obtiene un tipo de capa por el nombre del tipo.
	 * 
	 * @param nameTypeLayer
	 *            nombre del tipo de la capa
	 * 
	 * @return
	 */
	public LayerTypeDto getLayerTypeByName(String nameTypeLayer);

	/**
	 * Obtiene un tipo de capa por el ID del tipo.
	 * 
	 * @param id
	 *            identificador del tipo de capa.
	 * @return
	 */
	public LayerTypeDto getLayerTypeById(Long id);
}
