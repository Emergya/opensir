package com.emergya.ohiggins.services;

import java.io.Serializable;
import java.util.List;

import com.emergya.ohiggins.dto.AnalyticsDataDownloadsDto;
import com.emergya.persistenceGeo.service.AbstractService;

public interface AnalyticsDataDownloadsService extends AbstractService, Serializable {

	/**
	 * Retrieves the downloads for a given analyitics related document
	 * by said document's identifier.
	 * @param identifier
	 * @return
	 */
	AnalyticsDataDownloadsDto getByIdentifier(String identifier);

	/**
	 * Retrieves the most downloaded documents.
	 * 
	 * @param maxDefaultViewCount
	 * @return
	 */
	List<AnalyticsDataDownloadsDto> getMostDownloaded(int count);
}
