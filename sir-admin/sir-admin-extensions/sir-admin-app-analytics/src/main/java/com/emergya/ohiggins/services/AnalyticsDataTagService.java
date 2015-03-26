package com.emergya.ohiggins.services;

import java.io.Serializable;
import java.util.List;

import com.emergya.ohiggins.dto.AnalyticsDataTagCountDto;
import com.emergya.persistenceGeo.service.AbstractService;

public interface AnalyticsDataTagService extends AbstractService, Serializable {

	/**
	 * Saves the tags for a given identifier
	 * @param identifier
	 * @param split
	 */
	void saveTagsForIdentifier(String identifier, String[] tags);

	/**
	 * Returns a list of matching used tags.
	 * @param searchTerm
	 * @return
	 */
	List<String> searchTags(String searchTerm);

	/**
	 * Returns the tag names ordered by tag use count.
	 * @return
	 */
	List<AnalyticsDataTagCountDto> getTagCounts();

	/**
	 * Deletes all the tags associated with a given identifier.
	 * @param datumIdentifier
	 */
	void deleteAllForIdentifier(String datumIdentifier);	
}
