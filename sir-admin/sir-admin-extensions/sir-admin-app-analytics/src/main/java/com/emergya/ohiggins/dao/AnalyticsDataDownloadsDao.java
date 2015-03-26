package com.emergya.ohiggins.dao;

import com.emergya.ohiggins.model.AnalyticsDataDownloadsEntity;
import com.emergya.persistenceGeo.dao.GenericDAO;

public interface AnalyticsDataDownloadsDao 
	extends GenericDAO<AnalyticsDataDownloadsEntity , Long>{
	
	public AnalyticsDataDownloadsEntity getByIdentifier(String identifier);
}
