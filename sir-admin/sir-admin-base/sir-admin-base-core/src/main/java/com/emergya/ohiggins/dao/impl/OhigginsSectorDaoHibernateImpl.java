/*
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
 * Authors:: María Arias de Reyna (mailto:delawen@gmail.com)
 */
package com.emergya.ohiggins.dao.impl;

import java.util.List;

import org.hibernate.criterion.Order;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.emergya.ohiggins.dao.OhigginsSectorEntityDao;
import com.emergya.ohiggins.model.SectorEntity;
import com.emergya.persistenceGeo.dao.impl.GenericHibernateDAOImpl;

/**
 * Implementacion de Sector dao para hibernate
 * 
 * @author <a href="mailto:delawen@gmail.com">delawen</a>
 * 
 */
@Repository("ohigginsSectorEntityDao")
public class OhigginsSectorDaoHibernateImpl extends
		GenericHibernateDAOImpl<SectorEntity, Long> implements
		OhigginsSectorEntityDao {

	public static final String NOMBRE = SectorEntity.Names.NOMBRE.toString();

	@SuppressWarnings("unchecked")
	@Override
	@Transactional(readOnly = true)
	public List<SectorEntity> getAll() {
		return getSession().createCriteria(SectorEntity.class)
				.addOrder(Order.asc(NOMBRE)).list();
	}

}
