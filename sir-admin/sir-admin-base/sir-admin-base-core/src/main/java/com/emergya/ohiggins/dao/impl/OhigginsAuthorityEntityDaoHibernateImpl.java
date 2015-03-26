/*
 * OhigginsAuthorityEntityDao.java
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
package com.emergya.ohiggins.dao.impl;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.emergya.ohiggins.dao.OhigginsAuthorityEntityDao;
import com.emergya.ohiggins.model.AuthorityEntity;
import com.emergya.ohiggins.model.UserEntity;
import com.emergya.persistenceGeo.dao.impl.GenericHibernateDAOImpl;

/**
 * Implementacion de authority dao para hibernate
 * 
 * @author <a href="mailto:adiaz@emergya.com">adiaz</a>
 * 
 */
@Repository
public class OhigginsAuthorityEntityDaoHibernateImpl extends
		GenericHibernateDAOImpl<AuthorityEntity, Long> implements
		OhigginsAuthorityEntityDao {

	protected final String PEOPLE = "people";
	protected final String USER_ID = "id";
	protected final String PEOPLE_USER_ID = PEOPLE + "."
			+ UserEntity.Names.USER_ID;
	protected final String AUTHORITY = "name";

	public Long save(AuthorityEntity authorityEntity) {
		return (Long) getSession().save(authorityEntity);
	}

	public void delete(Long idgrupo) {
		AuthorityEntity entity = findById(idgrupo, false);
		if (entity != null) {
			getSession().delete(entity);
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<AuthorityEntity> findByUser(Long user_id) {
		return getSession().createCriteria(AuthorityEntity.class)
				.createAlias(PEOPLE, PEOPLE)
				.add(Restrictions.eq(PEOPLE_USER_ID, user_id)).list();
	}

	/**
	 * Obtiene lista de autoridades ordenada por nombre
	 * 
	 * @param first
	 * @param last
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<AuthorityEntity> getFromToOrdered(Integer first, Integer last) {
		Criteria criteria = getSession().createCriteria(AuthorityEntity.class);
		criteria.addOrder(Order.asc(AUTHORITY));
		criteria.setFirstResult(first);
		criteria.setMaxResults(last - first);
		return criteria.list();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<AuthorityEntity> getAllOrderedByField(
			AuthorityEntity.Names fieldName) {
		Criteria criteria = getSession().createCriteria(AuthorityEntity.class);
		criteria.addOrder(Order.asc(fieldName.toString()));
		return criteria.list();
	}

	@Override
	public AuthorityEntity findUserAuthority(Long user_id) {
		return (AuthorityEntity) getSession()
				.createCriteria(AuthorityEntity.class)
				.createAlias(PEOPLE, PEOPLE)
				.add(Restrictions.eq(PEOPLE_USER_ID, user_id)).uniqueResult();
	}
}
