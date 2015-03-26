/* OhigginsInversionUpdateDaoHibernateImpl.java
 * 
 * Copyright (C) 2012
 * 
 * This file is part of project ohiggins-core
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

import org.springframework.stereotype.Repository;

import com.emergya.ohiggins.dao.InversionUpdateDao;
import com.emergya.ohiggins.model.InversionUpdateEntity;
import com.emergya.persistenceGeo.dao.impl.GenericHibernateDAOImpl;

/**
 * @author <a href="mailto:jlrodriguez@emergya.com">jlrodriguez</a>
 * 
 */
@Repository
public class OhigginsInversionUpdateDaoHibernateImpl extends
		GenericHibernateDAOImpl<InversionUpdateEntity, Long> implements
		InversionUpdateDao {

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.emergya.ohiggins.dao.InversionUpdateDao#disableOldUpdates(com.emergya
	 * .ohiggins.model.InversionUpdateEntity)
	 */
	@Override
	public void disableOldUpdates(InversionUpdateEntity current) {
		String hql = "UPDATE InversionUpdateEntity SET enabled = ? "
				+ " where id <> ? AND fileType = ?";
		getHibernateTemplate().bulkUpdate(hql, Boolean.FALSE, current.getId(),
				current.getFileType());

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.emergya.ohiggins.dao.InversionUpdateDao#getAllFileTypeLastUpdate()
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<InversionUpdateEntity> getAllFileTypeLastUpdate() {
		String hql = "SELECT i FROM InversionUpdateEntity i fetch all properties  "
				+ " where (i.fileType, i.lastUpdateDate) IN ("
				+ "SELECT p.fileType, max(p.lastUpdateDate)  "
				+ "FROM InversionUpdateEntity p group by p.fileType)";

		return getHibernateTemplate().find(hql);
	}

}
