/*
 * LayerResourceServiceImpl.java
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
package com.emergya.ohiggins.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.emergya.ohiggins.dao.OhigginsLayerResourceDao;
import com.emergya.ohiggins.dto.AuthorityDto;
import com.emergya.ohiggins.dto.LayerResourceDto;
import com.emergya.ohiggins.dto.LayerTypeDto;
import com.emergya.ohiggins.model.LayerResourceEntity;
import com.emergya.ohiggins.service.InstitucionService;
import com.emergya.ohiggins.service.LayerResourceService;
import com.emergya.ohiggins.service.LayerTypeService;
import com.emergya.persistenceGeo.dao.GenericDAO;
import com.emergya.persistenceGeo.service.impl.AbstractServiceImpl;

/**
 * @author <a href="mailto:jlrodriguez@emergya.com">jlrodriguez</a>
 *
 */
@Transactional
@Service
public class LayerResourceServiceImpl extends
        AbstractServiceImpl<LayerResourceDto, LayerResourceEntity> implements
        LayerResourceService {

    @Autowired
    private OhigginsLayerResourceDao dao;
    @Autowired
    private InstitucionService institucionService;
    @Autowired
    private LayerTypeService layerTypeService;

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.emergya.ohiggins.service.LayerResourceService#generateNameNotYetUsed
     * ()
     */
    @Override
    public String generateNameNotYetUsed() {

        return dao.generateNameNotYetUsed();
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.emergya.ohiggins.service.LayerResourceService#getByTableName(java
     * .lang.String)
     */
    @Override
    public LayerResourceDto getByTmpLayerName(String tmpLayerName) {
        LayerResourceEntity result = dao.getByTmpLayerName(tmpLayerName);
        LayerResourceDto dto = entityToDto(result);
        return dto;

    }

    /*
     * (non-Javadoc)
     * 
     * @see com.emergya.persistenceGeo.service.impl.AbstractServiceImpl#getDao()
     */
    @Override
    protected GenericDAO<LayerResourceEntity, Long> getDao() {
        return dao;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.emergya.persistenceGeo.service.impl.AbstractServiceImpl#entityToDto
     * (java.io.Serializable)
     */
    @Override
    public LayerResourceDto entityToDto(LayerResourceEntity entity) {
        LayerResourceDto dto = null;
        if (entity != null) {
            dto = new LayerResourceDto();
            dto.setId(entity.getId());
            dto.setActive(entity.isActive());
            dto.setCreateDate(entity.getCreateDate());
            dto.setOriginalFileName(entity.getOriginalFileName());
            dto.setTmpLayerName(entity.getTmpLayerName());
            dto.setUpdateDate(entity.getUpdateDate());
            dto.setWorkspaceName(entity.getWorkspaceName());
            dto.setAuthority(institucionService.entityToDto(entity
                    .getAuthority()));
            dto.setLayerType(layerTypeService.entityToDto(entity.getLayerType()));
        }
        return dto;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.emergya.persistenceGeo.service.impl.AbstractServiceImpl#dtoToEntity
     * (java.io.Serializable)
     */
    @Override
    protected LayerResourceEntity dtoToEntity(LayerResourceDto dto) {
        LayerResourceEntity entity = null;
        if (dto != null) {
            entity = new LayerResourceEntity();
            entity.setActive(dto.isActive());
            entity.setCreateDate(dto.getCreateDate());
            entity.setId(dto.getId());
            entity.setOriginalFileName(dto.getOriginalFileName());
            entity.setTmpLayerName(dto.getTmpLayerName());
            entity.setUpdateDate(dto.getUpdateDate());
            entity.setWorkspaceName(dto.getWorkspaceName());
            if (dto.getAuthority() != null
                    && dto.getAuthority().getId() != null) {
                AuthorityDto authority = (AuthorityDto) institucionService
                        .getById(dto.getAuthority().getId());
                entity.setAuthority(institucionService.dtoToEntity(authority));
            }
            if (dto.getLayerType() != null
                    && dto.getLayerType().getId() != null) {
                LayerTypeDto layerType = (LayerTypeDto) layerTypeService
                        .getById(dto.getLayerType().getId());
                entity.setLayerType(layerTypeService.dtoToEntity(layerType));
            }
        }

        return entity;
    }

    @Override
    public String generateNameNotYetUsed(String prefix) {
        return dao.generateNameNotYetUsed(prefix);
    }
}
