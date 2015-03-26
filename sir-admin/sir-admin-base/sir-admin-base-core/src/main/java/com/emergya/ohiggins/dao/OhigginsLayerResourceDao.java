/*
 * OhigginsLayerResourceDao.java
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
package com.emergya.ohiggins.dao;

import com.emergya.ohiggins.model.AuthorityEntity;
import com.emergya.ohiggins.model.LayerResourceEntity;
import com.emergya.persistenceGeo.dao.GenericDAO;
import java.util.List;

/**
 * @author <a href="mailto:jlrodriguez@emergya.com">jlrodriguez</a>
 *
 */
public interface OhigginsLayerResourceDao extends
	GenericDAO<LayerResourceEntity, Long> {

    public LayerResourceEntity getByTmpLayerName(String tmpLayerName);

    /**
     * Generates a table name not yet used in the database with a fixed prefix.
     *
     * @return
     */
    public String generateNameNotYetUsed();

    /**
     * Generates a table name not yet used in the database with a custom prefix;
     *
     * @param prefix
     * @return
     */
    public String generateNameNotYetUsed(String prefix);

    public List<LayerResourceEntity> getByAuth(AuthorityEntity authEntity);
    
    
    /**
	 * Obtain number of tables used with a name ilike tableName
	 * 
	 * @param tableName
	 * 
	 * @return 0 if not found and number of occurrences otherwise 
	 */
    public Long tableUses(String tableName);
}
