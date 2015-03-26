/* OhigginsInversionUpdateServiceImpl.java
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
package com.emergya.ohiggins.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.emergya.ohiggins.dao.InversionUpdateDao;
import com.emergya.ohiggins.dao.OhigginsFileTypeDao;
import com.emergya.ohiggins.dto.FileTypeDto;
import com.emergya.ohiggins.dto.InversionUpdateDto;
import com.emergya.ohiggins.model.FileTypeEntity;
import com.emergya.ohiggins.model.InversionUpdateEntity;
import com.emergya.ohiggins.service.InversionUpdateService;
import com.emergya.persistenceGeo.dao.GenericDAO;
import com.emergya.persistenceGeo.service.impl.AbstractServiceImpl;

/**
 * @author <a href="mailto:jlrodriguez@emergya.com">jlrodriguez</a>
 * 
 */
@Service
public class OhigginsInversionUpdateServiceImpl extends
		AbstractServiceImpl<InversionUpdateDto, InversionUpdateEntity>
		implements InversionUpdateService {

	@Autowired
	private InversionUpdateDao inversionUpdateDao;
	@Autowired
	private OhigginsFileTypeDao fileTypeDao;

	@Override
	protected GenericDAO<InversionUpdateEntity, Long> getDao() {
		return this.inversionUpdateDao;
	}

	@Override
	protected InversionUpdateDto entityToDto(InversionUpdateEntity entity) {
		InversionUpdateDto result = new InversionUpdateDto();
		result.setEnabled(entity.isEnabled());
		result.setId(entity.getId());
		result.setLastUpdateDate(entity.getLastUpdateDate());
		if (entity.getFileType() != null) {
			FileTypeDto ftdto = new FileTypeDto();
			FileTypeEntity ftentity = entity.getFileType();
			ftdto.setCreateDate(ftentity.getCreateDate());
			ftdto.setId(ftentity.getId());
			ftdto.setTableName(ftentity.getTableName());
			ftdto.setTypeName(ftentity.getTypeName());
			ftdto.setUpdateDate(ftentity.getUpdateDate());
			result.setFileType(ftdto);

		}
		return result;
	}

	@Override
	protected InversionUpdateEntity dtoToEntity(InversionUpdateDto dto) {
		InversionUpdateEntity entity = new InversionUpdateEntity();
		entity.setEnabled(dto.isEnabled());
		entity.setId(dto.getId());
		entity.setLastUpdateDate(dto.getLastUpdateDate());
		if (dto.getFileType() != null) {
			FileTypeEntity ftentity = fileTypeDao.getFileTypeByTypeName(dto
					.getFileType().getTypeName());
			entity.setFileType(ftentity);
		}
		return entity;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.emergya.ohiggins.service.InversionUpdateService#
	 * enableCurrentUpdateAndDisableOldOnes
	 * (com.emergya.ohiggins.dto.InversionUpdateDto)
	 */
	@Override
	@Transactional
	public void enableCurrentUpdateAndDisableOldOnes(
			InversionUpdateDto inversionDto) {
		InversionUpdateEntity current = inversionUpdateDao.findById(
				inversionDto.getId(), false);
		current.setEnabled(true);
		inversionUpdateDao.disableOldUpdates(current);
		inversionUpdateDao.makePersistent(current);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.emergya.ohiggins.service.InversionUpdateService#getAllFileTypeLastUpdate
	 * ()
	 */
	@Override
	@Transactional
	public List<InversionUpdateDto> getAllFileTypeLastUpdate() {
		List<InversionUpdateDto> result = new ArrayList<InversionUpdateDto>();
		List<InversionUpdateEntity> intermediate = inversionUpdateDao
				.getAllFileTypeLastUpdate();

		for (InversionUpdateEntity entity : intermediate) {
			result.add(entityToDto(entity));
		}
		return result;
	}

}
