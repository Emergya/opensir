package com.emergya.ohiggins.services.impl;

import java.io.Serializable;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.emergya.ohiggins.dao.AnalyticsCategoryDao;
import com.emergya.ohiggins.dto.AnalyticsCategoryDto;
import com.emergya.ohiggins.model.AnalyticsCategoryEntity;
import com.emergya.ohiggins.services.AnalyticsCategoryService;
import com.emergya.persistenceGeo.dao.GenericDAO;
import com.emergya.persistenceGeo.service.impl.AbstractServiceImpl;

@Repository
@Transactional
public class AnalyticsCategoryServiceImpl
	extends AbstractServiceImpl<AnalyticsCategoryDto, AnalyticsCategoryEntity>
	implements AnalyticsCategoryService {

	private static final long serialVersionUID = 733624935925009295L;
	
	@Resource
	private AnalyticsCategoryDao analyticsCategoryServiceDao;
	
	@Override
	protected GenericDAO<AnalyticsCategoryEntity, Long> getDao() {
		return analyticsCategoryServiceDao;
	}

	@Override
	protected AnalyticsCategoryDto entityToDto(AnalyticsCategoryEntity entity) {		
		return new AnalyticsCategoryDto(entity);		
	}

	@Override
	protected AnalyticsCategoryEntity dtoToEntity(AnalyticsCategoryDto dto) {
		return dto.toEntity();
	}
}
