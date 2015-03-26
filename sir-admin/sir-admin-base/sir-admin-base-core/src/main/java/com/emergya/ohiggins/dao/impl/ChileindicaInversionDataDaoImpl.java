/*
 * Copyright (C) 2013
 * 
 * This file is part of Proyecto sir-adminn
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
 * Authors:: Juan Luis Rodríguez Ponce (mailto:jlrodriguez@emergya.com)
 */
package com.emergya.ohiggins.dao.impl;

import java.util.List;

import org.hibernate.Session;
import org.springframework.stereotype.Repository;

import com.emergya.ohiggins.dao.ChileindicaInversionDataDao;
import com.emergya.ohiggins.model.ChileindicaInversionDataEntity;
import com.emergya.persistenceGeo.dao.impl.GenericHibernateDAOImpl;

/**
 * 
 * @author <a href="mailto:jlrodriguez@emergya.com">Juan Luis Rodríguez
 *         Ponce</a>
 */
@Repository
public class ChileindicaInversionDataDaoImpl extends
		GenericHibernateDAOImpl<ChileindicaInversionDataEntity, Long> implements
		ChileindicaInversionDataDao {

	@Override
	public void makeTransient(ChileindicaInversionDataEntity entity) {
		super.makeTransient(getHibernateTemplate().merge(entity));

	}

	@Override
	@SuppressWarnings("unchecked")
	public List<Long> getAllIds() {
		return getSession().getNamedQuery(
				"ChileindicaInversionDataEntity.findAllIDs").list();
	}

	@Override
	public void deleteById(Long id) {
		String hql = "delete from ChileindicaInversionDataEntity where id= :id";
		Session s = getSession();
		s.createQuery(hql).setLong("id", id).executeUpdate();
	}
}
