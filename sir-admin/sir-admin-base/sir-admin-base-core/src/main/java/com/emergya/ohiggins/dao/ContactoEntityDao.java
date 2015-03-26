/*
 * ContactoEntityDao.java
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

import com.emergya.ohiggins.model.ContactoEntity;
import com.emergya.persistenceGeo.dao.GenericDAO;

/**
 * Dao para Contacto
 * 
 * @author <a href="mailto:jariera@emergya.com">jariera</a>
 * 
 */
public interface ContactoEntityDao extends GenericDAO<ContactoEntity, Long> {

	/**
	 * Crea una nuevo contaco en el sistema
	 * 
	 * @param <code>dto</code>
	 * 
	 * @return Contacto creado
	 */
	public ContactoEntity createContacto(ContactoEntity entity);

	/**
	 * Obtiene una lista de contacto ordenados por una propiedad.
	 * 
	 * @param first
	 * @param last
	 * @param propertyName
	 * @return
	 */
	public List<ContactoEntity> findAllFromToOrderBy(Integer first,
			Integer last, String propertyName);

	/**
	 * Obtiene el número de contactos asociados a una region
	 * 
	 * @param idRegion
	 *            Long
	 * @return Long
	 */
	public Long getResultsByRegionId(Long idRegion);

	/**
	 * Obtiene los contactos asociados a una region
	 * 
	 * @param first
	 * @param last
	 * @param idRegion
	 * @param propertyName
	 * @return List<ContactoEntity>
	 */
	public List<ContactoEntity> findAllFromToByRegionIdOrderBy(Integer first,
			Integer last, Long idRegion, String propertyName);
}
