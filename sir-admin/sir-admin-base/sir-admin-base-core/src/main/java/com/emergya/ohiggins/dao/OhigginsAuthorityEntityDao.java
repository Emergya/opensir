/*
 * UserEntityDao.java
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
 * Authors:: Alejandro Díaz Torres (mailto:adiaz@emergya.com)
 */
package com.emergya.ohiggins.dao;

import java.util.List;

import com.emergya.ohiggins.model.AuthorityEntity;
import com.emergya.persistenceGeo.dao.GenericDAO;

/**
 * DAO para {@link AuthorityEntity}.
 * 
 * @author <a href="mailto:adiaz@emergya.com">adiaz</a>.
 * @author <a href="mailto:jlrodriguez@emergya.com">jlrodriguez</a>.
 * 
 */
public interface OhigginsAuthorityEntityDao extends
		GenericDAO<AuthorityEntity, Long> {

	Long save(AuthorityEntity authorityEntity);

	void delete(Long idgrupo);

	/**
	 * Obtiene la lista de autoridades asociada a un usuario.
	 * 
	 * @param user_id
	 *            el identificador del usuario.
	 * @return la lista de autoridades asociadas al usuario.
	 */
	List<AuthorityEntity> findByUser(Long user_id);

	/**
	 * Obtiene la {@link AuthorityEntity} asociada a un usuario.
	 * 
	 * @param user_id
	 *            clave primaria del usuario
	 * @return la {@link AuthorityEntity} del usuario si la tiene.
	 *         <code>null</code> en otro caso.
	 */
	AuthorityEntity findUserAuthority(Long user_id);

	/**
	 * Obtiene lista de autoridades ordenada por nombre
	 * 
	 * @param first
	 * @param last
	 * @return
	 */
	public List<AuthorityEntity> getFromToOrdered(Integer first, Integer last);

	/**
	 * Obtiene la lista de todas las Authorities ordenadas por el campo pasado
	 * por parámetro en orden ascendente.
	 * 
	 * @param field
	 *            nombre del campo por el que ordenar.
	 * @return todas las {@link AuthorityEntity} ordenadas por el campo
	 *         <code>field</code> ascendente.
	 */
	public List<AuthorityEntity> getAllOrderedByField(
			AuthorityEntity.Names field);

}
