/*
 * OhigginsLayerPublishRequestDaoHibernateImpl.java
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
 * Authors:: Jose Alfonso Riera (mailto:jariera@emergya.com)
 */
package com.emergya.ohiggins.dao.impl;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.emergya.ohiggins.dao.OhigginsLayerPublishRequestEntityDao;
import com.emergya.ohiggins.dto.EstadoPublicacionLayerRequestPublish;
import com.emergya.ohiggins.model.AuthorityEntity;
import com.emergya.ohiggins.model.LayerPublishRequestEntity;
import com.emergya.ohiggins.model.UserEntity;
import com.emergya.persistenceGeo.dao.impl.GenericHibernateDAOImpl;

/**
 * Implementacion de Capa publicacion dao para hibernate
 * 
 * @author <a href="mailto:jariera@emergya.com">adiaz</a>
 * 
 */
@Repository
public class OhigginsLayerPublishRequestDaoHibernateImpl extends
		GenericHibernateDAOImpl<LayerPublishRequestEntity, Long> implements
		OhigginsLayerPublishRequestEntityDao {

	private static final String ID = "id";
	private static final String AUTH = "auth";
	private static final String PUNTO = ".";
	private static final String LAYER = "layer";
	private static final String METADATE = "metadata";
	private static final String USER = "user";
	private static final String ESTADO = "estado";
	private static final String SOURCE_NAME = "sourceLayerName";
	private static final String TABLE_NAME = "tmpLayerName";

	@SuppressWarnings("unchecked")
	@Override
	public List<LayerPublishRequestEntity> findAllFromToOrderBy(Integer first,
			Integer last, String propertyName, Long idUsuario) {

		Criteria criteria = getSession().createCriteria(super.persistentClass);
		criteria.setFirstResult(first);
		criteria.setMaxResults(last - first);

		// Restriccion idAuth
		criteria.createAlias(USER, USER).add(
				Restrictions.eq(USER + PUNTO + ID, idUsuario));
		// criteria.createAlias(AUTH, AUTH).add(Restrictions.eq(AUTH + PUNTO +
		// ID, idUsuario));

		criteria.addOrder(Order.asc(propertyName));
		return criteria.list();

	}

	@SuppressWarnings("unchecked")
	@Override
	public List<LayerPublishRequestEntity> findAllFromToOrderByAdmin(
			Integer first, Integer last, String propertyName, Long idUsuario) {

		Criteria criteria = getSession().createCriteria(super.persistentClass);
		criteria.setFirstResult(first);
		criteria.setMaxResults(last - first);

		// Restriccion idAuth
		// criteria.createAlias(USER, USER).add(Restrictions.eq(USER + PUNTO +
		// ID, idUsuario));
		// criteria.createAlias(AUTH, AUTH).add(Restrictions.eq(AUTH + PUNTO +
		// ID, idUsuario));

		// Estado pendientes
		criteria.add(Restrictions.eq(ESTADO,
				EstadoPublicacionLayerRequestPublish.PENDIENTE.toString()));

		criteria.addOrder(Order.asc(propertyName));
		return criteria.list();

	}
	
	/**
	 * Obtain number of layer publish requests like sourceName
	 * 
	 * @param sourceName
	 * 
	 * @return 0 if not found or number of layer publish requests with a name ilike sourceName
	 */
	public Long sourceNameRequested(String sourceName){
		return (Long) getSession()
				.createCriteria(super.persistentClass)
				.add(Restrictions.or(Restrictions.ilike(SOURCE_NAME, sourceName,
						MatchMode.ANYWHERE), Restrictions.ilike(TABLE_NAME, sourceName,
						MatchMode.ANYWHERE)))
				.setProjection(Projections.count(ID)).uniqueResult();
	}

	@Override
	public LayerPublishRequestEntity getByTmpLayerName(String geoserverName) {
		return (LayerPublishRequestEntity) getSession()
				.createCriteria(super.persistentClass)
				.add(Restrictions.eq("tmpLayerName", geoserverName))
				.uniqueResult();
	}

    @Override
    public List<LayerPublishRequestEntity> getByUser(UserEntity user) {
        return getSession().createCriteria(super.persistentClass)
                .add(Restrictions.eq(USER, user)).list();
    }

    @Override
    public List<LayerPublishRequestEntity> getByAuth(AuthorityEntity authEntity) {
        return getSession().createCriteria(super.persistentClass)
                .add(Restrictions.eq(AUTH, authEntity)).list();
    }
	
	/**
	 * Replace {@link GenericHibernateDAOImpl#makePersistent(Object)} method to 
	 * let you persist layer properties in cascade and don't broke #87143 fixes.
	 * 
	 * @param entity to be saved
	 * 
	 * @return entity after save
	 */
	public LayerPublishRequestEntity makePersistent(LayerPublishRequestEntity entity) {
		/*
		 *  Fix a problem in layer.properties save. 
		 *  If you don't use saveOrUpdate (and you use only 'merge' method), 
		 *  it will save the layer properties in 'gis_layer_property', 
		 *  but won't persist the relation in 'gis_layer_publish_request_gis_layer_property'
		 */
		
		getHibernateTemplate().saveOrUpdate(entity);
	            
		return entity;
	}
}
