/*
 * LayerPublishRequestService.java
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

import com.emergya.ohiggins.dto.AuthorityDto;
import com.emergya.ohiggins.dto.LayerDto;
import com.emergya.ohiggins.dto.LayerPublishRequestDto;
import com.emergya.persistenceGeo.service.AbstractService;

/**
 * Interface para el manejo de capas de publicacion
 * 
 * @author jariera
 * 
 */
public interface LayerPublishRequestService extends AbstractService,
		Serializable {

	/**
	 * Lista de capas publicacion del usuario
	 * 
	 * @param first
	 * @param last
	 * @param propertyName
	 * @return
	 */
	public List<LayerPublishRequestDto> getFromToOrderBy(Integer first,
			Integer last, String propertyName, Long idUsuario);

	/**
	 * Lista de capas publicacion pendientes del administrador
	 * 
	 * @param first
	 * @param last
	 * @param propertyName
	 * @return
	 */
	public List<LayerPublishRequestDto> getFromToOrderByAdmin(Integer first,
			Integer last, String propertyName, Long idUsuario);

	/**
	 * Comprueba si existe una petición de publicación para una capa por parte
	 * de una autoridad determinada.
	 * 
	 * @param institucion
	 *            La autoridad que realiza la petición.
	 * @param layer
	 *            La capa que desea publicar la autoridad.
	 * @return
	 */
	public boolean existsRequest(AuthorityDto institucion, LayerDto layer);

	/**
	 * Comprueba si existe una petición de publicación para una capa por parte
	 * de una autoridad determinada.
	 * 
	 * @param institucion
	 *            La autoridad que realiza la petición.
	 * @param layer
	 *            La capa que desea publicar la autoridad.
	 * @param id
	 *            The id of the request being edited, which should be excluded
	 *            from the query.
	 * @return
	 */
	public boolean existsRequest(AuthorityDto institucion, LayerDto layer,
			Long id);

	/**
	 * Returns all pending requests.
	 * @return
	 */
	public List<LayerPublishRequestDto> getPendingRequests();

	
	/**
	 * Obtain number of layer publish requests like sourceName
	 * 
	 * @param sourceName
	 * 
	 * @return 0 if not found or number of layer publish requests with a name ilike sourceName
	 */
	public Long sourceNameRequested(String sourceName);

	/**
	 * Gets a layer publish request dto by its name in geoserver.
	 * @param geoserverName
	 * @return
	 */
	public LayerPublishRequestDto getByTmpLayerName(String geoserverName);
}
