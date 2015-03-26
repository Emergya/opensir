/*
 * OhigginsFolderEntityDaoHibernateImpl.java
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

import com.emergya.ohiggins.dao.OhigginsFolderEntityDao;
import com.emergya.ohiggins.model.FolderEntity;
import com.emergya.persistenceGeo.dao.impl.GenericHibernateDAOImpl;

/**
 * Implementacion Dao para folder
 * 
 * @author jariera
 * 
 */
@Repository
public class OhigginsFolderEntityDaoHibernateImpl extends
		GenericHibernateDAOImpl<FolderEntity, Long> implements
		OhigginsFolderEntityDao {

	private static final String AUTHORITY = "authority";
	private static final String NAME = "name";
	private static final String PARENT = "parent";
	private static final String USER = "user";
	private static final String PUNTO = ".";
	private static final String ID = "id";

	/**
	 * 
	 * @return Devuelve lista de folder por id de autoridad
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<FolderEntity> getFolderByAuthorityId(Long idAuth) {
		return getSession().createCriteria(FolderEntity.class)
				.createAlias(AUTHORITY, AUTHORITY)
				.add(Restrictions.eq(AUTHORITY + PUNTO + ID, idAuth))
				.addOrder(Order.asc(NAME)).list();
	}

	/**
	 * 
	 * @return Devuelve lista de folder que tengan como padre Id
	 * @param id
	 *            Identificador del padre
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<FolderEntity> getFolderByParentId(Long idPadre) {
		return getSession().createCriteria(FolderEntity.class)
				.createAlias(PARENT, PARENT)
				.add(Restrictions.eq(PARENT + PUNTO + ID, idPadre))
				.addOrder(Order.asc(NAME)).list();
	}

	/**
	 * 
	 * @return Devuelve lista de folder que pertenecen al usuario
	 * @param id
	 *            Identificador del usuario
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<FolderEntity> getFolderByUserId(Long idUser) {
		return getSession().createCriteria(FolderEntity.class)
				.createAlias(USER, USER)
				.add(Restrictions.eq(USER + PUNTO + ID, idUser))
				.addOrder(Order.asc(NAME)).list();
	}

	/**
	 * 
	 * @return Devuelve lista de folder que son padres, es decir no tienen hijos
	 *         (folder_parent_id is null)
	 * @param id
	 *            Identificador del padre
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<FolderEntity> getFolderParent() {
		return getSession().createCriteria(FolderEntity.class)
				// .createAlias(PARENT, PARENT)
				.add(Restrictions.isNull(PARENT)).addOrder(Order.asc(NAME))
				.list();

	}

	@SuppressWarnings("unchecked")
	@Override
	public List<FolderEntity> getArbolCapas(Integer first, Integer last,
			String propertyName) {

		Criteria criteria = getSession().createCriteria(super.persistentClass);
		criteria.setFirstResult(first);
		criteria.setMaxResults(last - first);

		criteria.addOrder(Order.asc(propertyName));
		return criteria.list();

	}
}
