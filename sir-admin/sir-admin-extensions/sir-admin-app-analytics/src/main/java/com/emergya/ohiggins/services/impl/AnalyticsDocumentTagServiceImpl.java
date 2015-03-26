package com.emergya.ohiggins.services.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Repository;

import com.emergya.ohiggins.dao.AnalyticsDataTagDao;
import com.emergya.ohiggins.dto.AnalyticsDataTagCountDto;
import com.emergya.ohiggins.dto.AnalyticsDataTagDto;
import com.emergya.ohiggins.model.AnalyticsDataTagEntity;
import com.emergya.ohiggins.services.AnalyticsDataTagService;
import com.emergya.persistenceGeo.dao.GenericDAO;
import com.emergya.persistenceGeo.service.impl.AbstractServiceImpl;

@Repository
public class AnalyticsDocumentTagServiceImpl
		extends AbstractServiceImpl<AnalyticsDataTagDto, AnalyticsDataTagEntity>
		implements AnalyticsDataTagService
{
	private static final long serialVersionUID = 2520082799020556348L;

	@Resource
	private AnalyticsDataTagDao analyticsDocumentTagDao;

	@Override
	protected GenericDAO<AnalyticsDataTagEntity, Long> getDao() {
		return analyticsDocumentTagDao;
	}

	@Override
	protected AnalyticsDataTagDto entityToDto(AnalyticsDataTagEntity entity) {
		if (entity == null) {
			return null;
		}

		return new AnalyticsDataTagDto(entity);
	}

	@Override
	protected AnalyticsDataTagEntity dtoToEntity(AnalyticsDataTagDto dto) {
		return dto.toEntity();
	}

	@Override
	public void saveTagsForIdentifier(String identifier, String[] tags) {
		// We remove the currently set tags, so we start clean.
		this.deleteAllForIdentifier(identifier);

		// We add the new tags.
		for (String tagName : tags) {
			AnalyticsDataTagEntity newTag = new AnalyticsDataTagEntity();
			newTag.setIdentifier(identifier);
			newTag.setTagName(tagName);

			analyticsDocumentTagDao.makePersistent(newTag);
		}

	}

	@Override
	public List<String> searchTags(String searchTerm) {
		return analyticsDocumentTagDao.searchTags(searchTerm);
	}

	@Override
	public List<AnalyticsDataTagCountDto> getTagCounts() {

		return analyticsDocumentTagDao.getTagCounts();

	}

	@Override
	public void deleteAllForIdentifier(String identifier) {

		AnalyticsDataTagEntity searchExample = new AnalyticsDataTagEntity();
		searchExample.setIdentifier(identifier);
		List<AnalyticsDataTagEntity> currentTags =
				analyticsDocumentTagDao.findByExample(searchExample, new String[] {});

		for (AnalyticsDataTagEntity currentTag : currentTags) {
			analyticsDocumentTagDao.makeTransient(currentTag);
		}

	}

}
