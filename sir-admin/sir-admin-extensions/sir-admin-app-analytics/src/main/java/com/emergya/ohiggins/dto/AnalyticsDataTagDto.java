package com.emergya.ohiggins.dto;

import java.io.Serializable;

import com.emergya.ohiggins.model.AnalyticsDataTagEntity;

/**
 *
 * @author lroman
 */
public class AnalyticsDataTagDto implements Serializable {
	private static final long serialVersionUID = 5389313347833557155L;
	private Long id;
    private String analyticsDataIdentifier;
    private String tagName;

    public AnalyticsDataTagDto() {
    	
    }
    
    public AnalyticsDataTagDto(AnalyticsDataTagEntity entity) {
		this.id = entity.getId();
		this.analyticsDataIdentifier = entity.getIdentifier();
		this.tagName = entity.getTagName();
	}

	/**
     * @return the analyticsDataIdentifier
     */
    public String getIdentifier() {
        return analyticsDataIdentifier;
    }

    /**
     * @param analyticsDataIdentifier the analyticsDataIdentifier to set
     */
    public void setIdentifier(String analyticsDataIdentifier) {
        this.analyticsDataIdentifier = analyticsDataIdentifier;
    }

    /**
     * @return the tagName
     */
    public String getTagName() {
        return tagName;
    }

    /**
     * @param tagName the tagName to set
     */
    public void setTagName(String tagName) {
        this.tagName = tagName;
    }

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public AnalyticsDataTagEntity toEntity() {
		AnalyticsDataTagEntity entity = new AnalyticsDataTagEntity();
		entity.setId(id);
		entity.setIdentifier(analyticsDataIdentifier);
		entity.setTagName(tagName);
		
		return entity;
	}
}
