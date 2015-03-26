package com.emergya.ohiggins.services.impl;

import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.collections.ListUtils;
import org.apache.commons.collections.Transformer;
import org.springframework.stereotype.Repository;

import com.emergya.ohiggins.dao.AnalyticsDataDownloadsDao;
import com.emergya.ohiggins.dto.AnalyticsDataDownloadsDto;
import com.emergya.ohiggins.model.AnalyticsDataDownloadsEntity;
import com.emergya.ohiggins.services.AnalyticsDataDownloadsService;
import com.emergya.persistenceGeo.dao.GenericDAO;
import com.emergya.persistenceGeo.service.impl.AbstractServiceImpl;

@Repository
public class AnalyticsDocumentDownloadsServiceImpl
	extends AbstractServiceImpl<AnalyticsDataDownloadsDto, AnalyticsDataDownloadsEntity>
	implements AnalyticsDataDownloadsService
{

	@Resource
	AnalyticsDataDownloadsDao analyticsDownloadsDao;
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -1771903544015247108L;

	@Override
	protected GenericDAO<AnalyticsDataDownloadsEntity, Long> getDao() {
		return analyticsDownloadsDao;
	}

	@Override
	protected AnalyticsDataDownloadsDto entityToDto(AnalyticsDataDownloadsEntity entity) {
		if(entity==null) {
			return null;
		}
		return new AnalyticsDataDownloadsDto(entity);
	}

	@Override
	protected AnalyticsDataDownloadsEntity dtoToEntity(AnalyticsDataDownloadsDto dto) {
		return dto.toEntity();
	}

	@Override
	public AnalyticsDataDownloadsDto getByIdentifier(String identifier) {
		
		AnalyticsDataDownloadsEntity downloadsE = analyticsDownloadsDao.getByIdentifier(identifier);
		if(downloadsE==null) {
			return null;
		}
		
		return entityToDto(downloadsE);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<AnalyticsDataDownloadsDto> getMostDownloaded(int count) {
		return (List<AnalyticsDataDownloadsDto>) entitiesToDtos(
				analyticsDownloadsDao.findOrdered(0, count-1, "downloads", false));		
	}

}
