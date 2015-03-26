package com.emergya.ohiggins.dto;

import java.io.Serializable;

import javax.validation.constraints.Size;

import org.springframework.beans.factory.annotation.Required;

import com.emergya.ohiggins.model.AnalyticsCategoryEntity;

/**
 * Class transfer Categories data for the Analytics module.
 * 
 * @author lroman
 */
public class AnalyticsCategoryDto implements Serializable{
	private static final long serialVersionUID = 2984947274213463735L;

	private Long id;
	
	private String name;
	private boolean enabled =true;
	
	public AnalyticsCategoryDto() {
		
	}
	
	public AnalyticsCategoryDto(AnalyticsCategoryEntity entity) {
		this.id = entity.getId();
		this.name = entity.getName();
		this.enabled = entity.isEnabled();
	}
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	
	@Required
	@Size(min=3)
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public boolean isEnabled() {
		return enabled;
	}
	
	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}
	public  AnalyticsCategoryEntity toEntity() {
		AnalyticsCategoryEntity entity = new AnalyticsCategoryEntity();
		entity.setId(id);
		entity.setEnabled(enabled);
		entity.setName(name);
		
		return entity;
		
	}	
}
