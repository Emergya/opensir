package com.emergya.ohiggins.dto;

import java.io.Serializable;

import com.emergya.ohiggins.model.AnalyticsDataDownloadsEntity;

public class AnalyticsDataDownloadsDto implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 968316337024665272L;

	private Long id;

	private String identifier;

	private Long downloads;

	public AnalyticsDataDownloadsDto() {
		
	}
	
	public AnalyticsDataDownloadsDto(AnalyticsDataDownloadsEntity entity) {
		this.id = entity.getId();
		this.downloads = entity.getDownloads();
		this.identifier = entity.getIdentifier();
	}
	
	public AnalyticsDataDownloadsEntity toEntity() {
		AnalyticsDataDownloadsEntity entity = new AnalyticsDataDownloadsEntity();
		entity.setId(id);
		entity.setDownloads(downloads);
		entity.setIdentifier(identifier);
		return entity;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getIdentifier() {
		return identifier;
	}

	public void setIdentifier(String identifier) {
		this.identifier = identifier;
	}

	public Long getDownloads() {
		return downloads;
	}

	public void setDownloads(Long downloads) {
		this.downloads = downloads;
	}

}
