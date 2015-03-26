/*
 * FaqEntityDaoHibernateImpl.java
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
package com.emergya.ohiggins.dao.impl;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.emergya.ohiggins.dao.OhigginsFaqEntityDao;
import com.emergya.ohiggins.model.FaqEntity;
import com.emergya.persistenceGeo.dao.impl.GenericHibernateDAOImpl;

/**
 * Dao para faq
 * 
 * @author <a href="mailto:jariera@emergya.com">jariera</a>
 * 
 */
@Repository
public class OhigginsFaqEntityDaoHibernateImpl extends
		GenericHibernateDAOImpl<FaqEntity, Long> implements
		OhigginsFaqEntityDao {

	protected final String MODULO = "modulo";
	protected final String HABILITADA = "habilitada";
	
	/**
	 * Crea una nueva faq en el sistema
	 * 
	 * @param <code>dto</code>
	 * 
	 * @return faq creada
	 */
	@Transactional(readOnly = false)
	public FaqEntity createFaq(FaqEntity entity) {
		getSession().save(entity);
		return entity;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<FaqEntity> findAllFromToOrderBy(Integer first, Integer last,
			String propertyName) {
		Criteria criteria = getSession().createCriteria(super.persistentClass);
		criteria.setFirstResult(first);
		criteria.setMaxResults(last - first);
		criteria.addOrder(Order.asc(propertyName));
		return criteria.list();
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<FaqEntity> findModuleFaqs(String module) {
		return getSession()
				.createCriteria(FaqEntity.class)
				.add(Restrictions.eq(MODULO, module).ignoreCase())
				.add(Restrictions.eq(HABILITADA, true))
				.list();
	}
	
}
