/*
 * InstitucionService.java
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
 * Authors:: Jose Alfonso (mailto:jariera@emergya.com)
 */
package com.emergya.ohiggins.service;

import java.io.Serializable;
import java.util.List;

import com.emergya.ohiggins.dto.AuthorityDto;
import com.emergya.ohiggins.model.AuthorityEntity;
import com.emergya.persistenceGeo.service.AbstractService;

public interface InstitucionService extends AbstractService, Serializable {

	/**
	 * Crea una nueva institucion
	 * 
	 * @param dto
	 * @return
	 */
	public AuthorityDto createInstitucion(AuthorityDto dto);

	
	/**
	 * Comprueba si una institucion tiene usuarios asociados
	 * 
	 * @param id
	 * @return
	 */
	public boolean hasUsers(Long id);

	/**
	 * Obtiene lista de autoridades ordenada por nombre
	 * 
	 * @param first
	 * @param last
	 * @return
	 */
	public List<AuthorityDto> getFromToOrdered(Integer first, Integer last);

	/**
	 * 
	 * @return devuelve todas las instituciones ordenadas por nombre en orden
	 *         ascendente.
	 */
	public List<AuthorityDto> getAllOrderedByName();

	/**
	 * Comprueba si la institucion ya esta dada de alta
	 * 
	 * @param institucionname
	 *            nombre institucion a comprobar.
	 * @return <code>true</code> si la institucion no existe en la base de
	 *         datos, <code>false</code> si ya existe la institucion con ese
	 *         username.
	 */
	public boolean isInstitucionAvailable(String institucionname, Long codRegion);
	
	/**
	 * Checks if an authority name is avalaible, discarding the passed id 
	 * (this allows for skipping the own authority being edited).
	 * @param authorityName
	 * @param institutionId
	 * @return
	 */
	public boolean isAuthorityAvalaible(String authorityName, Long institutionId, Long codRegion);

	public AuthorityDto entityToDto(AuthorityEntity entity);

	/**
	 * @param authority
	 * @return
	 */
	public AuthorityEntity dtoToEntity(AuthorityDto authority);

}
