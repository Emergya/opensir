/*
 * ContactoEntityDaoHibernateImpl.java
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.persistence.PersistenceException;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.ResultTransformer;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.emergya.ohiggins.dao.ContactoEntityDao;
import com.emergya.ohiggins.model.ContactoEntity;
import com.emergya.persistenceGeo.dao.impl.GenericHibernateDAOImpl;

/**
 * Dao para contacto
 * 
 * @author <a href="mailto:jariera@emergya.com">jariera</a>
 * 
 */
@Repository("contactoEntityDao")
public class ContactoEntityDaoHibernateImpl extends
		GenericHibernateDAOImpl<ContactoEntity, Long> implements
		ContactoEntityDao {

	/**
	 * Crea una nuevo contacto en el sistema
	 * 
	 * @param <code>dto</code>
	 * 
	 * @return contacto creado
	 */
	@Transactional(readOnly = false)
	public ContactoEntity createContacto(ContactoEntity entity) {
		getSession().save(entity);
		return entity;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ContactoEntity> findAllFromToOrderBy(Integer first,
			Integer last, String propertyName) {
		Criteria criteria = getSession().createCriteria(super.persistentClass);
		criteria.setFirstResult(first);
		criteria.setMaxResults(last - first);
		criteria.addOrder(Order.asc(propertyName));
		return criteria.list();
	}

	@Override
	public Long getResultsByRegionId(Long idRegion) {

		Long count = null;
		try {

			// Creamos la query.
			final StringBuilder sbQuery = new StringBuilder();
			sbQuery.append(" SELECT COUNT(*) FROM ContactoEntity AS ce");
			sbQuery.append(" LEFT OUTER JOIN ce.regionContactos AS rcr");
			sbQuery.append(" WHERE rcr.rc.region.id = :idRegion");

			final Map<String, Object> parametros = new HashMap<String, Object>();
			parametros.put("idRegion", idRegion);

			final Query query = getSession().createQuery(sbQuery.toString());

			// Aplicamos los parametros a la query.
			super.aplicarParametros(query, parametros);

			count = (Long) query.uniqueResult();

		} catch (Exception ex) {
			throw new PersistenceException(
					"getResultsByRegionId: No se ha podido recuperar la cantidad de entidades '"
							+ this.persistentClass, ex);
		}
		return count.longValue();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ContactoEntity> findAllFromToByRegionIdOrderBy(Integer first,
			Integer last, Long idRegion, String propertyName) {

		List<ContactoEntity> listResult = new ArrayList<ContactoEntity>();

		try {

			// Creamos la query.
			final StringBuilder sbQuery = new StringBuilder();
			sbQuery.append(" SELECT ce FROM ContactoEntity AS ce");
			sbQuery.append(" LEFT OUTER JOIN ce.regionContactos AS rcr");
			sbQuery.append(" WHERE rcr.rc.region.id = :idRegion");
			sbQuery.append(" ORDER BY :propertyName ASC");

			final Map<String, Object> parametros = new HashMap<String, Object>();
			parametros.put("idRegion", idRegion);
			parametros.put("propertyName", propertyName);

			final Query query = getSession().createQuery(sbQuery.toString());
			query.setFirstResult(first);
			query.setMaxResults(last - first);

			// Aplicamos los parametros a la query.
			super.aplicarParametros(query, parametros);

			listResult = (List<ContactoEntity>) query.list();

		} catch (Exception ex) {
			throw new PersistenceException(
					"findAllFromToByRegionIdOrderBy: No se ha podido las entidades '"
							+ this.persistentClass, ex);
		}
		return listResult;
	}
}
