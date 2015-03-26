/*
 * OhigginsLayerMetadataTmpEntityDaoHibernateImpl.java
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

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.emergya.ohiggins.dao.OhigginsLayerMetadataEntityDao;
import com.emergya.ohiggins.model.LayerEntity;
import com.emergya.ohiggins.model.LayerMetadataEntity;
import com.emergya.persistenceGeo.dao.impl.GenericHibernateDAOImpl;

/**
 * Implementacion de Capa metadata tmp publicacion dao para hibernate
 * 
 * @author <a href="mailto:jariera@emergya.com">adiaz</a>
 * 
 */
@Repository
public class OhigginsLayerMetadataEntityDaoHibernateImpl extends
		GenericHibernateDAOImpl<LayerMetadataEntity, Long> implements
		OhigginsLayerMetadataEntityDao {

	protected final String METADATA = "metadata";
	protected final String LAYER = "layer";
	protected final String PUNTO = ".";
	protected final String ID = "id";

	/**
	 * obtiene el layer metadato tmo a partir del id de la capa
	 * 
	 * @param dto
	 * @return
	 */
	public LayerMetadataEntity getMetadataByIdLayer(Long idLayer) {
		LayerMetadataEntity m = null;
		Criteria criteria = getSession().createCriteria(LayerEntity.class);
		LayerEntity e = (LayerEntity) criteria
				.add(Restrictions.eq(ID, idLayer)).uniqueResult();

		if (e != null) {
			m = e.getMetadata();
		}
		return m;
	}
}
