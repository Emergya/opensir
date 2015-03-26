package com.emergya.chileIndica.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.emergya.chileIndica.dao.ChileIndicaDatabaseGenericDAO;
import com.emergya.chileIndica.dao.ChileIndicaInstitucionEntityDao;
import com.emergya.chileIndica.dto.ChileIndicaInstitucionDto;
import com.emergya.chileIndica.model.ChileIndicaInstitucionEntity;
import com.emergya.chileIndica.service.ChileIndicaInstitucionService;

@Service
@Transactional
public class ChileIndicaInstitucionServiceImpl extends
		ChileIndicaAbstractServiceImpl<ChileIndicaInstitucionDto, ChileIndicaInstitucionEntity> implements
		ChileIndicaInstitucionService {

	private static final long serialVersionUID = 5025865567430925020L;
	
	@Autowired
	private ChileIndicaInstitucionEntityDao chileIndicaInstitucionEntityDao;

	@Override
	public ChileIndicaInstitucionDto entityToDto(ChileIndicaInstitucionEntity entity) {
		ChileIndicaInstitucionDto dto = null;

		if (entity != null) {
			dto = new ChileIndicaInstitucionDto();
			dto.setCInstitucion(entity.getCInstitucion());
			dto.setNIstitucion(entity.getNInstitucion());
		}
		return dto;
	}

	@Override
	public ChileIndicaInstitucionEntity dtoToEntity(ChileIndicaInstitucionDto dto) {
		ChileIndicaInstitucionEntity entity = null;

		if (dto != null) {
			entity = new ChileIndicaInstitucionEntity();
			entity.setCInstitucion(dto.getCInstitucion());
			entity.setNIstitucion(dto.getNInstitucion());
		}

		return entity;
	}
	
	@Override
	protected ChileIndicaDatabaseGenericDAO<ChileIndicaInstitucionEntity, Long> getDao() {
		return (ChileIndicaDatabaseGenericDAO<ChileIndicaInstitucionEntity, Long>) chileIndicaInstitucionEntityDao;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<ChileIndicaInstitucionDto> getAllAuthorityByRegion(
			Long codRegion) {
		List<ChileIndicaInstitucionEntity> entities = chileIndicaInstitucionEntityDao
				.getAllAuthorityByRegion(codRegion);
		List<ChileIndicaInstitucionDto> ldto = (List<ChileIndicaInstitucionDto>) entitiesToDtos(entities);
		return ldto;
	}
}
