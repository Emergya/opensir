/*
 * OhigginsAuthorityTypeEntityDao.java
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
 * Authors:: Jose Alfonso(mailto:jariera@emergya.com)
 */
package com.emergya.ohiggins.dao;

import java.util.List;

import com.emergya.ohiggins.model.AuthorityTypeEntity;
import com.emergya.persistenceGeo.dao.GenericDAO;

/**
 * Dao para tipo de institucion.
 * 
 * @author jariera
 * 
 */
public interface OhigginsAuthorityTypeEntityDao extends
		GenericDAO<AuthorityTypeEntity, Long> {

	/**
	 * Obtiene lista de tipos autoridades ordenada por nombre sin incluir a los
	 * ciudadanos.
	 * 
	 * @param first
	 * @param last
	 * @return
	 */
	public List<AuthorityTypeEntity> getAllOrdered();

	/**
	 * Obtiene lista de tipos autoridades ordenada por nombre incluyendo a los
	 * ciudadanos.
	 * 
	 * @param campo
	 *            Ordena por el campo.
	 * @return
	 */
	public List<AuthorityTypeEntity> getAllOrderedByField(String campo);

	/**
	 * Obtiene lista de tipos autoridades ordenada por nombre que no sean
	 * ciudadano.
	 * 
	 * @return
	 */
	public List<AuthorityTypeEntity> getAllAuthority();

	/**
	 * Obtiene lista de tipos autoridades que no sean ciudadano.
	 * 
	 * @return
	 */
	public List<AuthorityTypeEntity> getAllAuthorityOrdered();
}
