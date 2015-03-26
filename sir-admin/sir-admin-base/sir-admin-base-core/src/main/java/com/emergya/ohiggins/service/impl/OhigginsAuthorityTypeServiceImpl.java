/*
 * OhigginsAuthorityTypeServiceImpl.java
 * 
 * Copyright (C) 2011
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
 * Authors:: Jose Alfonso(mailto:jariera@emergya.com)
 */
package com.emergya.ohiggins.service.impl;

import java.util.LinkedList;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.emergya.ohiggins.dao.OhigginsAuthorityTypeEntityDao;
import com.emergya.ohiggins.dao.OhigginsPermissionEntityDao;
import com.emergya.ohiggins.dto.AuthorityTypeDto;
import com.emergya.ohiggins.dto.PermisoDto;
import com.emergya.ohiggins.model.AuthorityTypeEntity;
import com.emergya.ohiggins.model.PermissionEntity;
import com.emergya.ohiggins.service.AuthorityTypeService;
import com.emergya.persistenceGeo.dao.GenericDAO;
import com.emergya.persistenceGeo.service.impl.AbstractServiceImpl;

@Repository("ohigginsAuthorityTypeService")
@Transactional
public class OhigginsAuthorityTypeServiceImpl extends
		AbstractServiceImpl<AuthorityTypeDto, AuthorityTypeEntity> implements
		AuthorityTypeService {

	private static final long serialVersionUID = 1111235718858394232L;

	@Autowired
	private OhigginsAuthorityTypeEntityDao ohigginsAuthorityTypeDao;

	@Resource
	private OhigginsPermissionEntityDao permisoDao;

	public OhigginsAuthorityTypeServiceImpl() {
		super();
	}

	@Override
	public AuthorityTypeDto entityToDto(AuthorityTypeEntity entity) {
		AuthorityTypeDto dto = null;

		if (entity != null) {
			dto = new AuthorityTypeDto();
			dto.setFechaActualizacion(entity.getUpdateDate());
			dto.setFechaCreacion(entity.getCreateDate());
			dto.setId(entity.getId());
			dto.setName(entity.getName());
			dto.setEsCiudadano(entity.isCitizen());

			// Permisos
			if (entity.getPermissionList() != null
					&& entity.getPermissionList().size() > 0) {
				List<PermisoDto> lpermisoDto = new LinkedList<PermisoDto>();

				for (PermissionEntity permisoEntity : entity
						.getPermissionList()) {
					PermisoDto pdto = new PermisoDto();
					pdto.setFechaActualizacion(permisoEntity.getUpdateDate());
					pdto.setFechaCreacion(permisoEntity.getCreateDate());
					pdto.setId(permisoEntity.getId());
					pdto.setNombre(permisoEntity.getName());

					lpermisoDto.add(pdto);
				}

				dto.setPermisos(lpermisoDto);
			}
		}

		return dto;
	}

	@Override
	public AuthorityTypeEntity dtoToEntity(AuthorityTypeDto dto) {
		AuthorityTypeEntity entity = null;

		if (dto != null) {
			entity = new AuthorityTypeEntity();
			entity.setUpdateDate(dto.getFechaActualizacion());
			entity.setCreateDate(dto.getFechaCreacion());
			entity.setId(dto.getId());
			entity.setName(dto.getName());
			entity.setCitizen(dto.getEsCiudadano());

			// Permisos
			if (dto.getPermisos() != null && dto.getPermisos().size() > 0) {
				List<PermissionEntity> lpermisoEntity = new LinkedList<PermissionEntity>();

				for (PermisoDto permisoDto : dto.getPermisos()) {
					PermissionEntity pentity = new PermissionEntity();
					pentity = permisoDao.findById(permisoDto.getId(), false);
					lpermisoEntity.add(pentity);
				}

				entity.setPermissionList(lpermisoEntity);
			} else {
				entity.setPermissionList(null);
			}
		}

		return entity;
	}

	@Override
	protected GenericDAO<AuthorityTypeEntity, Long> getDao() {
		return (GenericDAO<AuthorityTypeEntity, Long>) ohigginsAuthorityTypeDao;
	}

	/**
	 * Obtiene el tipo de institucion por id
	 * 
	 * @param id
	 * @return
	 */
	public AuthorityTypeDto getById(Long id) {
		return entityToDto((AuthorityTypeEntity) ohigginsAuthorityTypeDao
				.findById(id, false));
	}

	/**
	 * Obtiene lista de tipos autoridades ordenada por nombre
	 * 
	 * @param first
	 * @param last
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<AuthorityTypeDto> getAllOrdered() {
		List<AuthorityTypeEntity> entities = ohigginsAuthorityTypeDao
				.getAllOrdered();
		List<AuthorityTypeDto> ldto = (List<AuthorityTypeDto>) entitiesToDtos(entities);
		return ldto;
	}

	/**
	 * Obtiene lista de tipos autoridades ordenada por nombre
	 * 
	 * @param campo
	 *            Ordena por el campo
	 * @return
	 */
	public List<AuthorityTypeDto> getAllOrderedByField(String campo) {
		List<AuthorityTypeEntity> entities = ohigginsAuthorityTypeDao
				.getAllOrderedByField(campo);
		List<AuthorityTypeDto> ldto = (List<AuthorityTypeDto>) entitiesToDtos(entities);
		return ldto;
	}

	/**
	 * Obtiene lista de tipos autoridades que no sean ciudadano
	 * 
	 * @return
	 */
	public List<AuthorityTypeDto> getAllAuthority() {
		List<AuthorityTypeEntity> entities = ohigginsAuthorityTypeDao
				.getAllAuthority();
		List<AuthorityTypeDto> ldto = (List<AuthorityTypeDto>) entitiesToDtos(entities);
		return ldto;
	}

	/**
	 * Obtiene lista de tipos autoridades ordenada por nombre que no sean
	 * ciudadano
	 * 
	 * @return
	 */
	public List<AuthorityTypeDto> getAllAuthorityOrdered() {
		List<AuthorityTypeEntity> entities = ohigginsAuthorityTypeDao
				.getAllAuthorityOrdered();
		List<AuthorityTypeDto> ldto = (List<AuthorityTypeDto>) entitiesToDtos(entities);
		return ldto;
	}

}
