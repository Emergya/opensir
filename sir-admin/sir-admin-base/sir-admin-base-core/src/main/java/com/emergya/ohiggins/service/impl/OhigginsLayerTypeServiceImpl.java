/*
 * OhigginsLayerTypeServiceImpl.java
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
package com.emergya.ohiggins.service.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.emergya.ohiggins.dao.OhigginsLayerTypeEntityDao;
import com.emergya.ohiggins.dto.LayerTypeDto;
import com.emergya.ohiggins.model.LayerTypeEntity;
import com.emergya.ohiggins.service.LayerTypeService;
import com.emergya.persistenceGeo.dao.GenericDAO;
import com.emergya.persistenceGeo.service.impl.AbstractServiceImpl;

@Repository("ohigginsLayerTypeServiceImpl")
@Transactional
public class OhigginsLayerTypeServiceImpl extends
		AbstractServiceImpl<LayerTypeDto, LayerTypeEntity> implements
		LayerTypeService {

	@Resource
	private OhigginsLayerTypeEntityDao ohigginsLayerTypeDao;

	/**
	 * 
	 */
	private static final long serialVersionUID = 4170986683055890365L;

	@Override
	protected GenericDAO<LayerTypeEntity, Long> getDao() {
		return ohigginsLayerTypeDao;
	}

	@Override
	public LayerTypeDto entityToDto(LayerTypeEntity entity) {
		LayerTypeDto dto = null;

		if (entity != null) {
			dto = new LayerTypeDto();
			dto.setId(entity.getId());
			dto.setName(entity.getName());
			dto.setTipo(entity.getTipo());
		}

		return dto;
	}

	@Override
	public LayerTypeEntity dtoToEntity(LayerTypeDto dto) {
		LayerTypeEntity entity = null;

		if (dto != null) {
			entity = new LayerTypeEntity();
			entity.setId(dto.getId());
			entity.setName(dto.getName());
			entity.setTipo(dto.getTipo());
		}

		return entity;
	}

	@Override
	public LayerTypeDto getLayerTypeByName(String nameTypeLayer) {
		return entityToDto(ohigginsLayerTypeDao.getLayerType(nameTypeLayer));
	}

	@Override
	public LayerTypeDto getLayerTypeById(Long id) {
		return entityToDto(ohigginsLayerTypeDao.findById(id, false));
	}

}
