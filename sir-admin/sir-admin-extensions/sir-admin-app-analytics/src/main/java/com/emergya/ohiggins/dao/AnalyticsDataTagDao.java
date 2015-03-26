package com.emergya.ohiggins.dao;

import java.util.List;

import com.emergya.ohiggins.dto.AnalyticsDataTagCountDto;
import com.emergya.ohiggins.model.AnalyticsDataTagEntity;
import com.emergya.persistenceGeo.dao.GenericDAO;

public interface AnalyticsDataTagDao 
	extends GenericDAO<AnalyticsDataTagEntity , Long>{

	List<String> searchTags(String searchTerm);

	List<AnalyticsDataTagCountDto> getTagCounts();
	
	
}
