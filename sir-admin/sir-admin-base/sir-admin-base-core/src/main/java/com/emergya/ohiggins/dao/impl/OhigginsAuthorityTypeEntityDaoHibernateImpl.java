/* OhigginsAuthorityTypeEntityDaoHibernateImpl.java
 * 
 * 
 * Copyright (C) 2012
 * 
 * This file is part of Proyecto ohiggins
 * 
 * This software is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 * 
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301  USA
 *
 * As a special exception, if you link this library with other files to
 * produce an executable, this library does not by itself cause the
 * resulting executable to be covered by the GNU General Public License.
 * This exception does not however invalidate any other reasons why the
 * executable file might be covered by the GNU General Public License.
 * 
 * Authors:: Juan Luis Rodríguez Ponce (mailto:jlrodriguez@emergya.com)
 */
package com.emergya.ohiggins.dao.impl;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.emergya.ohiggins.dao.OhigginsAuthorityTypeEntityDao;
import com.emergya.ohiggins.model.AuthorityTypeEntity;
import com.emergya.persistenceGeo.dao.impl.GenericHibernateDAOImpl;

@Repository
public class OhigginsAuthorityTypeEntityDaoHibernateImpl extends
		GenericHibernateDAOImpl<AuthorityTypeEntity, Long> implements
		OhigginsAuthorityTypeEntityDao {

	public static final String NAME = AuthorityTypeEntity.Names.NAME.toString();
	public static final String CITIZEN = AuthorityTypeEntity.Names.CITIZEN
			.toString();

	/**
	 * Obtiene lista de tipos autoridades ordenada por nombre No muestra el tipo
	 * ciudadania
	 * 
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<AuthorityTypeEntity> getAllOrdered() {
		Criteria criteria = getSession().createCriteria(
				AuthorityTypeEntity.class);
		criteria.addOrder(Order.asc(NAME));
		return criteria.list();
	}

	/**
	 * Obtiene lista de tipos autoridades ordenada por nombre
	 * 
	 * @param campo
	 *            Ordena por el campo
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<AuthorityTypeEntity> getAllOrderedByField(String campo) {
		Criteria criteria = getSession().createCriteria(
				AuthorityTypeEntity.class);
		criteria.addOrder(Order.asc(campo));
		return criteria.list();
	}

	/**
	 * Obtiene lista de tipos autoridades que no sean ciudadano
	 * 
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<AuthorityTypeEntity> getAllAuthority() {
		Criteria criteria = getSession().createCriteria(
				AuthorityTypeEntity.class);
		criteria.add(Restrictions.eq(CITIZEN, false));
		return criteria.list();
	}

	/**
	 * Obtiene lista de tipos autoridades ordenada por nombre que no sean
	 * ciudadano
	 * 
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<AuthorityTypeEntity> getAllAuthorityOrdered() {
		Criteria criteria = getSession().createCriteria(
				AuthorityTypeEntity.class);
		criteria.add(Restrictions.eq(CITIZEN, false));
		criteria.addOrder(Order.asc(NAME));
		return criteria.list();
	}
}
