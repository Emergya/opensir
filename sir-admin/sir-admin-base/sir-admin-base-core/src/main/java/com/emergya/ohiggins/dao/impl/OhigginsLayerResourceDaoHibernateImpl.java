/*
 * OhigginsLayerResourceDaoHibernateImpl.java
 * 
 * Copyright (C) 2013
 * 
 * This file is part of ohiggins-core
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

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.emergya.ohiggins.dao.OhigginsLayerResourceDao;
import com.emergya.ohiggins.model.AuthorityEntity;
import com.emergya.ohiggins.model.LayerResourceEntity;
import com.emergya.persistenceGeo.dao.impl.GenericHibernateDAOImpl;
import com.emergya.persistenceGeo.utils.GeoserverUtils;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Projections;

/**
 * @author <a href="mailto:jlrodriguez@emergya.com">jlrodriguez</a>
 *
 */
@Repository
public class OhigginsLayerResourceDaoHibernateImpl extends
        GenericHibernateDAOImpl<LayerResourceEntity, Long> implements
        OhigginsLayerResourceDao {

    private static final String TABLE_PREFIX = "tmpVectLayer";
    private static final String TABLE_NAME = "tmpLayerName";
    private static final String ID = "id";

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.emergya.ohiggins.dao.OhigginsLayerResourceDao#getByTmpLayerName(java
     * .lang.String)
     */
    @Override
    public LayerResourceEntity getByTmpLayerName(String tmpLayerName) {
        Criteria crit = getSession().createCriteria(LayerResourceEntity.class)
                .add(Restrictions.eq("tmpLayerName", tmpLayerName));
        return (LayerResourceEntity) crit.uniqueResult();
    }

    /**
     * Generates a unused table name with a fixed prefix "shpImported".
     */
    @Override
    public String generateNameNotYetUsed() {
        return this.generateNameNotYetUsed(TABLE_PREFIX);
    }

    /**
     * Generates an unused table name with a customizable prefix.
     */
    @Override
    public String generateNameNotYetUsed(String prefix) {
        boolean unique = false;
        String result = null;
        while (!unique) {
            String candidate = GeoserverUtils.createUniqueName(prefix);
            if (getByTmpLayerName(candidate) == null) {
                unique = true;
                result = candidate;
            }
        }
        return result;
    }

    /**
     * Obtain number of tables used with a name ilike tableName
     *
     * @param tableName
     *
     * @return 0 if not found and number of occurrences otherwise
     */
    @Override
    public Long tableUses(String tableName) {
        return (Long) getSession()
                .createCriteria(super.persistentClass)
                .add(Restrictions.ilike(TABLE_NAME, tableName,
                                MatchMode.ANYWHERE))
                .setProjection(Projections.count(ID)).uniqueResult();
    }

    @Override
    public List<LayerResourceEntity> getByAuth(AuthorityEntity authEntity) {
        return getSession().createCriteria(super.persistentClass)
                .add(Restrictions.eq("authority", authEntity))
                .list();

    }
}
