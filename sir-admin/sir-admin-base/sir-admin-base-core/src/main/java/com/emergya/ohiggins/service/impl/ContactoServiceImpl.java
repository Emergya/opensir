/*
 * ContactoServiceImpl.java
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

import java.util.LinkedList;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.emergya.ohiggins.converter.RegionConverter;
import com.emergya.ohiggins.dao.ContactoEntityDao;
import com.emergya.ohiggins.dao.OhigginsRegionEntityDao;
import com.emergya.ohiggins.dto.ContactoDto;
import com.emergya.ohiggins.dto.RegionDto;
import com.emergya.ohiggins.model.ContactoEntity;
import com.emergya.ohiggins.model.RegionContactoRelationship;
import com.emergya.ohiggins.model.RegionEntity;
import com.emergya.ohiggins.service.ContactoService;
import com.emergya.persistenceGeo.dao.GenericDAO;
import com.emergya.persistenceGeo.service.impl.AbstractServiceImpl;

@Repository
@Transactional
public class ContactoServiceImpl extends
		AbstractServiceImpl<ContactoDto, ContactoEntity> implements
		ContactoService {

	private static final long serialVersionUID = -1037375049776502846L;
	@Resource
	private ContactoEntityDao contactoDao;

	@Resource
	private OhigginsRegionEntityDao regionDao;

	/**
	 * Crea una nueva faq en el sistema
	 * 
	 * @param <code>dto</code>
	 * 
	 * @return faq creada
	 */
	public ContactoDto createContacto(ContactoDto dto) {
		ContactoDto dtor = null;
		ContactoEntity entity = dtoToEntity(dto);
		entity = contactoDao.createContacto(entity);

		if (entity != null && entity.getId() != null) {
			dtor = entityToDto(entity);
		}

		return dtor;
	}

	/**
	 * Obtiene un contacto por id <code>id</code>.
	 * 
	 * @param id
	 *            el identificador del contacto
	 * @return un contacto
	 */
	public ContactoDto getContactoById(long id) {
		ContactoDto dto = entityToDto(contactoDao.findById((long) id, false));
		return dto;
	}

	@Override
	protected ContactoDto entityToDto(ContactoEntity entity) {
		ContactoDto dto = null;

		if (entity != null) {
			dto = new ContactoDto();

			dto.setId(entity.getId());
			dto.setFechaCreacion(entity.getFechaCreacion());
			dto.setFechaActualizacion(entity.getFechaActualizacion());
			dto.setDescripcion(entity.getDescripcion());
			dto.setEmail(entity.getEmail());
			dto.setLeido(entity.getLeido());
			dto.setNombre(entity.getNombre());
			dto.setTitulo(entity.getTitulo());

			// Region
			if (entity.getRegionContactos() != null
					&& entity.getRegionContactos().size() > 0
					&& entity.getRegionContactos().get(0).getRegion() != null) {
				RegionEntity reg = regionDao
						.findById(entity.getRegionContactos().get(0)
								.getRegion().getId(), false);
				if (reg != null) {
					RegionDto regDto = RegionConverter.entityToDto(reg);
					dto.setRegion(regDto);
				}
			} else {
				dto.setRegion(null);
			}

		}

		return dto;
	}

	@Override
	protected ContactoEntity dtoToEntity(ContactoDto dto) {
		ContactoEntity entity = null;

		if (dto != null) {
			entity = new ContactoEntity();

			entity.setId(dto.getId());
			entity.setFechaActualizacion(dto.getFechaActualizacion());
			entity.setFechaCreacion(dto.getFechaCreacion());
			entity.setDescripcion(dto.getDescripcion());
			entity.setEmail(dto.getEmail());
			entity.setLeido(dto.getLeido());
			entity.setNombre(dto.getNombre());
			entity.setTitulo(dto.getTitulo());

			// Region
			List<RegionContactoRelationship> regions = new LinkedList<RegionContactoRelationship>();

			if (dto.getRegion() != null && dto.getRegion().getId() != null) {

				RegionContactoRelationship regionContactoRelationship = new RegionContactoRelationship();
				RegionEntity reg = regionDao.findById(dto.getRegion().getId(),
						false);
				regionContactoRelationship.setContacto(entity);
				regionContactoRelationship.setRegion(reg);
				regions.add(regionContactoRelationship);
			}
			entity.setRegionContactos(regions);

		}
		return entity;
	}

	@Override
	protected GenericDAO<ContactoEntity, Long> getDao() {
		return contactoDao;
	}

	@Override
	public Long getResultsByRegionId(Long idRegion) {
		return ((ContactoEntityDao) getDao()).getResultsByRegionId(idRegion);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ContactoDto> getFromToOrderBy(Integer first, Integer last,
			String propertyName) {
		List<ContactoEntity> entities = ((ContactoEntityDao) getDao())
				.findAllFromToOrderBy(first, last, propertyName);
		return (List<ContactoDto>) entitiesToDtos(entities);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ContactoDto> getFromToByRegionIdOrderBy(Integer first,
			Integer last, Long idRegion, String propertyName) {
		List<ContactoEntity> entities = ((ContactoEntityDao) getDao())
				.findAllFromToByRegionIdOrderBy(first, last, idRegion,
						propertyName);
		return (List<ContactoDto>) entitiesToDtos(entities);
	}

}
