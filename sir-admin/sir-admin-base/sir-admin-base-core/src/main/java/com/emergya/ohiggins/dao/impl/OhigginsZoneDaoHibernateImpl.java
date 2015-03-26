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

import java.util.LinkedList;
import java.util.List;

import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.emergya.ohiggins.dao.OhigginsNivelTerritorialEntityDao;
import com.emergya.ohiggins.dto.NivelTerritorialDto;
import com.emergya.ohiggins.model.ZoneEntity;
import com.emergya.ohiggins.service.NivelTerritorialService;
import com.emergya.persistenceGeo.dao.impl.GenericHibernateDAOImpl;

/**
 * Implementacion de Sector dao para hibernate
 * 
 * @author <a href="mailto:delawen@gmail.com">delawen</a>
 * 
 */
@Repository("nivelTEntityDao")
public class OhigginsZoneDaoHibernateImpl extends
		GenericHibernateDAOImpl<ZoneEntity, Long> implements
		OhigginsNivelTerritorialEntityDao {

	private static final String TIPO_AMBITO = ZoneEntity.Names.TYPE.toString();
	private static final String NAME = ZoneEntity.Names.NAME.toString();

	@SuppressWarnings("unchecked")
	@Override
	@Transactional(readOnly = true)
	public List<ZoneEntity> getAll() {
//		return getSession().createCriteria(ZoneEntity.class)
//				.addOrder(Order.asc(NAME)).setCacheable(true).list();
//		dto = new NivelTerritorialDto();
//		dto.setCodigo_territorio(entity.getCode());
//		dto.setExtension(entity.getExtensionGeom());
//		dto.setFecha_actualizacion(entity.getUpdateDate());
//		dto.setFecha_creacion(entity.getCreateDate());
//		dto.setId(new Integer(entity.getId().toString()));
//		dto.setName(entity.getName());
//		dto.setTipo_ambito(entity.getType());
		final StringBuffer queryBuilder = new StringBuffer();
		queryBuilder.append("SELECT z.code, z.id, z.name, z.type from ZoneEntity z order by z.name");
		final List<Object[]> lista = getSession().createQuery(queryBuilder.toString()).list();
		if(lista != null && !lista.isEmpty()) {
			final LinkedList<ZoneEntity> list = new LinkedList<ZoneEntity>();
			for(Object[] a : lista) {
				final ZoneEntity zoneEntity = new ZoneEntity();
				zoneEntity.setCode((String)a[0]);
				zoneEntity.setId((Long)a[1]);
				zoneEntity.setName((String)a[2]);
				zoneEntity.setType((String)a[3]);
				list.add(zoneEntity);
			}
			return list;
		}
		return null;
	}

	/**
	 * Obtiene el nivel territorial por tipo para el tipo servicio público u
	 * otro.
	 * 
	 * @param type
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@Override
	@Transactional(readOnly = true)
	public List<ZoneEntity> getZonesByType(String type) {
		List<ZoneEntity> l = getSession().createCriteria(ZoneEntity.class)
				.add(Restrictions.eq(TIPO_AMBITO, type))
				.addOrder(Order.asc(NAME)).list();
		return l;
	}

	/**
	 * Obtiene el nivel territorial por tipos.
	 * 
	 * @param tipo
	 *            tipo de ámbito (
	 *            {@link NivelTerritorialService#TIPO_NIVEL_REGIONAL},
	 *            {@link NivelTerritorialService#TIPO_NIVEL_PROVINCIAL} o
	 *            {@link NivelTerritorialService#TIPO_NIVEL_MUNICIPAL}).
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@Override
	@Transactional(readOnly = true)
	public List<ZoneEntity> getNivelTerritorialByTipos(String[] tipos) {
		List<ZoneEntity> l = getSession().createCriteria(ZoneEntity.class)
				.add(Restrictions.in(TIPO_AMBITO, tipos))
				.addOrder(Order.asc(NAME)).list();
		return l;
	}
}
