/*
 * FaqServiceImpl.java
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

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.emergya.ohiggins.dao.OhigginsFaqEntityDao;
import com.emergya.ohiggins.dto.FaqDto;
import com.emergya.ohiggins.model.FaqEntity;
import com.emergya.ohiggins.service.FaqService;
import com.emergya.persistenceGeo.dao.GenericDAO;
import com.emergya.persistenceGeo.service.impl.AbstractServiceImpl;

@Repository
@Transactional
public class OhigginsFaqServiceImpl extends
		AbstractServiceImpl<FaqDto, FaqEntity> implements FaqService {

	private static final long serialVersionUID = -2999779298779607676L;

	@Resource
	private OhigginsFaqEntityDao ohigginsFaqDao;

	/**
	 * Crea una nueva faq en el sistema
	 * 
	 * @param <code>dto</code>
	 * 
	 * @return faq creada
	 */
	public FaqDto createFaq(FaqDto dto) {
		FaqDto dtor = null;
		FaqEntity entity = dtoToEntity(dto);
		entity = ohigginsFaqDao.createFaq(entity);

		if (entity != null && entity.getId() != null) {
			dtor = entityToDto(entity);
		}

		return dtor;
	}

	/**
	 * Obtiene una faq por id <code>id</code>.
	 * 
	 * @param id
	 *            el identificador de la faq
	 * @return una faq
	 */
	public FaqDto getFaqById(long id) {
		FaqDto dto = entityToDto(ohigginsFaqDao.findById((long) id, false));
		return dto;
	}

	@Override
	protected FaqDto entityToDto(FaqEntity entity) {
		FaqDto dto = null;

		if (entity != null) {
			dto = new FaqDto();
			dto.setFechaActualizacion(entity.getFechaActualizacion());
			dto.setFechaCreacion(entity.getFechaCreacion());
			dto.setId(entity.getId());
			dto.setHabilitada(entity.getHabilitada());
			dto.setModulo(entity.getModulo());
			dto.setRespuesta(entity.getRespuesta());
			dto.setTitulo(entity.getTitulo());
		}

		return dto;
	}

	@Override
	protected FaqEntity dtoToEntity(FaqDto dto) {
		FaqEntity entity = null;

		if (dto != null) {
			entity = new FaqEntity();
			entity.setFechaActualizacion(dto.getFechaActualizacion());
			entity.setFechaCreacion(dto.getFechaCreacion());
			entity.setId(dto.getId());
			entity.setHabilitada(dto.getHabilitada());
			entity.setModulo(dto.getModulo());
			entity.setRespuesta(dto.getRespuesta());
			entity.setTitulo(dto.getTitulo());
		}

		return entity;
	}

	@Override
	protected GenericDAO<FaqEntity, Long> getDao() {
		return ohigginsFaqDao;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<FaqDto> getFromToOrderBy(Integer first, Integer last,
			String propertyName) {
		List<FaqEntity> entities = ((OhigginsFaqEntityDao) getDao())
				.findAllFromToOrderBy(first, last, propertyName);
		return (List<FaqDto>) entitiesToDtos(entities);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<FaqDto> getModuleFaqs(String module){
		
		List<FaqEntity> entities = ((OhigginsFaqEntityDao) getDao())
				.findModuleFaqs(module);
		
		return (List<FaqDto>) entitiesToDtos(entities);
	}

}
