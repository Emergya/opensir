/*
 * ContactoService.java
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

import com.emergya.ohiggins.dto.ContactoDto;
import com.emergya.persistenceGeo.service.AbstractService;

public interface ContactoService extends AbstractService, Serializable {

	/**
	 * Crea uno nuevo contacto
	 * 
	 * @param dto
	 * @return
	 */
	public ContactoDto createContacto(ContactoDto dto);

	/**
	 * Obtiene un contacto por id <code>id</code>.
	 * 
	 * @param id
	 *            el identificador de l contacto
	 * @return un contacto
	 */
	public ContactoDto getContactoById(long id);

	/**
	 * Lista de contacto
	 * 
	 * @param first
	 * @param last
	 * @param propertyName
	 * @return
	 */
	public List<ContactoDto> getFromToOrderBy(Integer first, Integer last,
			String propertyName);

	/**
	 * Obtiene el número de contactos asociados a una region
	 * 
	 * @param idRegion
	 *            Long
	 * @return Long
	 */
	public Long getResultsByRegionId(Long idRegion);

	/**
	 * Lista de contactos asociados a una region
	 * 
	 * @param first
	 * @param last
	 * @param propertyName
	 * @return
	 */
	public List<ContactoDto> getFromToByRegionIdOrderBy(Integer first,
			Integer last, Long idRegion, String propertyName);
}
