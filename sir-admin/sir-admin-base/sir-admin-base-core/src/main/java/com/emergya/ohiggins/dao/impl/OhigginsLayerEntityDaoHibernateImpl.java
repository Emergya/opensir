/*
 * OhigginsLayerEntityDaoHibernateImpl.java
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

import com.emergya.ohiggins.dao.OhigginsLayerEntityDao;
import com.emergya.ohiggins.model.AuthorityEntity;
import com.emergya.ohiggins.model.LayerEntity;
import com.emergya.ohiggins.model.LayerPropertyEntity;
import com.emergya.persistenceGeo.dao.impl.GenericHibernateDAOImpl;

/**
 * Implementacion de Layer dao para hibernate
 * 
 * @author <a href="mailto:jariera@emergya.com">adiaz</a>
 * 
 */
@Repository
public class OhigginsLayerEntityDaoHibernateImpl extends
		GenericHibernateDAOImpl<LayerEntity, Long> implements
		OhigginsLayerEntityDao {

	private static final String ID = "id";
	private static final String AUTH = "auth";
	private static final String PUNTO = ".";
	private static final String FOLDER = "folder";
	private static final String NAME = "name";
	private static final String TYPE = "type";
	private static final String PUBLICIZED = "publicized";
	private static final String ORDER = "order";

	@SuppressWarnings("unchecked")
	@Override
	public List<LayerEntity> findAllFromToOrderBy(Integer first, Integer last,
			String propertyName, Long idAuth) {

		Criteria criteria = getSession().createCriteria(super.persistentClass);
		criteria.setFirstResult(first);
		criteria.setMaxResults(last - first);

		// Restriccion idAuth
		criteria.createAlias(AUTH, AUTH).add(
				Restrictions.eq(AUTH + PUNTO + ID, idAuth));

		criteria.addOrder(Order.asc(propertyName));
		return criteria.list();

	}

	/**
	 * Lista de capas de un folder
	 * 
	 * @param idFolder
	 *            El id de la carpeta cuyas capas se devuelven. *
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<LayerEntity> getLayersByFolderId(Long idFolder) {

		return getLayersByFolderId(idFolder, false);

	}

	/**
	 * Lista de capas de un folder
	 * 
	 * @param idFolder
	 *            El id de la carpeta cuyas capas se devuelven.
	 * @param layerOrder
	 *            Si las capas se devuelve ordenadas por su orden, o
	 *            alfabéticamente por nombre.
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<LayerEntity> getLayersByFolderId(Long idFolder,
			boolean layerOrder) {

		Criteria criteria = getSession().createCriteria(super.persistentClass);

		// Restriccion idFolder
		criteria.createAlias(FOLDER, FOLDER).add(
				Restrictions.eq(FOLDER + PUNTO + ID, idFolder));

		if (layerOrder) {
			criteria.addOrder(Order.asc(ORDER));
		}
		addDefaultLayerSorting(criteria);
		return criteria.list();

	}

	/**
	 * Lista de capas de un folder de una determinada autoridad
	 * 
	 * @param first
	 * @param last
	 * @param propertyName
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<LayerEntity> getLayersByFolderIdByAuthId(Long idFolder,
			Long idAuth) {

		Criteria criteria = getSession().createCriteria(super.persistentClass);

		// Restriccion idFolder
		criteria.createAlias(FOLDER, FOLDER);
		criteria.createAlias(AUTH, AUTH);

		criteria.add(Restrictions.eq(AUTH + PUNTO + ID, idAuth));
		criteria.add(Restrictions.eq(FOLDER + PUNTO + ID, idFolder));

		addDefaultLayerSorting(criteria);
		
		return criteria.list();

	}

	/**
	 * Lista de capas publicadas de una autoridad y un tipo determinado (raster,
	 * vectorial)
	 * 
	 * @param first
	 * @param last
	 * @param propertyName
	 * @return
	 */

	public List<LayerEntity> getLayersPubliciedByAuthIdByTypeId(Long idAuth,
			Long type) {
		Criteria criteria = getSession().createCriteria(super.persistentClass);

		// Restriccion Type
		criteria.createAlias(TYPE, TYPE);
		criteria.createAlias("requestedByAuth", "requestedByAuth");

		criteria.add(Restrictions.eq("requestedByAuth" + PUNTO + ID, idAuth));
		criteria.add(Restrictions.eq(TYPE + PUNTO + ID, type));
		criteria.add(Restrictions.eq(PUBLICIZED, new Boolean(true)));

		addDefaultLayerSorting(criteria);

		return (List<LayerEntity>) criteria.list();
	}

	/**
	 * Devuelve las capas no asignadas a ningún canal/carpeta.
	 * 
	 * @param justPublicized
	 *            Si true, devuelve solo las capas donde "publicized=true".
	 *            Sino, todas.
	 * @returns
	 */
	@Override
	public List<LayerEntity> getUnassignedLayers(boolean justPublicized) {
		return getUnassignedLayers(justPublicized, false);
	}

	/**
	 * Devuelve las capas no asignadas a ningún canal/carpeta.
	 * 
	 * @param justPublicized
	 *            Si true, devuelve solo las capas donde "publicized=true".
	 *            Sino, todas.
	 * @param layerOrder
	 *            Si las capas se devuelve ordenadas por su orden, o
	 *            alfabéticamente por nombre.
	 * @returns
	 */
	@Override
	public List<LayerEntity> getUnassignedLayers(boolean justPublicized,
			boolean layerOrder) {
		Criteria criteria = getSession().createCriteria(super.persistentClass);

		// Restriccion idFolder
		criteria.add(Restrictions.isNull(FOLDER));

		if (justPublicized) {
			criteria.add(Restrictions.eq(PUBLICIZED, true));
		}

		if (layerOrder) {
			criteria.addOrder(Order.asc(ORDER));
		}
		
		
		addDefaultLayerSorting(criteria);
		
		return criteria.list();
	}
	
	private void addDefaultLayerSorting(Criteria criteria) {
		criteria.addOrder(Order.asc("layerTitle"));
		criteria.addOrder(Order.asc("name"));
	}
	
	/**
	 * Layers by layer type
	 * 
	 * @param idLayerType
	 * 
	 * @return layers with layer type given
	 */
	@SuppressWarnings("unchecked")
	public List<LayerEntity> getLayersByLayerType(Long idLayerType){
		Criteria criteria = getSession().createCriteria(super.persistentClass)
				.createAlias(TYPE, TYPE)
				.add(Restrictions.eq(TYPE + PUNTO + ID, idLayerType));
		
		return criteria.list();
	}

	/**
	 * Layers by layer type
	 * 
	 * @param idLayerType
	 * @param layerName
	 * 
	 * @return layers with layer type and layer name given
	 */
	@SuppressWarnings("unchecked")
	public List<LayerEntity> getLayersByLayerTypeAndLayerName(Long idLayerType, String layerName){
		Criteria criteria = getSession().createCriteria(super.persistentClass)
				.add(Restrictions.ilike(NAME, layerName, MatchMode.ANYWHERE))
				.createAlias(TYPE, TYPE)
				.add(Restrictions.eq(TYPE + PUNTO + ID, idLayerType));
		
		return criteria.list();
	}
	
	/**
	 * Obtain number of layers like layerName
	 * 
	 * @param layerName
	 * 
	 * @return 0 if not found or number of layers with a name ilike layerName
	 */
	public Long getLayerCountByName(String layerName){
		return (Long) getSession()
				.createCriteria(super.persistentClass)
				.add(Restrictions.ilike(NAME, layerName,
						MatchMode.ANYWHERE))
				.setProjection(Projections.count(ID)).uniqueResult();
	}

	@Override
	public LayerEntity getByName(String geoserverName) {
		
		@SuppressWarnings("unchecked")
		List<LayerEntity> entities = (List<LayerEntity>) getSession().createCriteria(super.persistentClass)
				.add(Restrictions.eq("name", geoserverName)).list();
		
		if(entities == null || entities.isEmpty()) {
			return null;
		}
		
		// There shouldn't be layers with duplicated names, but there are because 
		// auto generated layer names weren't in place until way after the project's start.
		return entities.get(0);
	}

    @Override
    public List<LayerEntity> getByAuth(AuthorityEntity authEntity) {
        return getSession().createCriteria(super.persistentClass)
                .add(Restrictions.eq(AUTH, authEntity))
                .list();
    }

    @Override
    public List<LayerEntity> getByRequestedByAuth(AuthorityEntity authEntity) {
        return getSession().createCriteria(super.persistentClass)
                .add(Restrictions.eq("requestedByAuth", authEntity))
                .list();
    }
}
