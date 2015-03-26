package com.emergya.ohiggins.dto;

/**
 * Dto class for showing tag info in the pages.
 * 
 * @author lroman
 *
 */
public class AnalyticsDataTagCountDto {
	private String name;
	private long useCount;
	
	public AnalyticsDataTagCountDto(String name, long useCount) {
		this.name = name;
		this.useCount = useCount;
	}
	
	public long getUseCount() {
		return useCount;
	}
	public void setUseCount(long useCount) {
		this.useCount = useCount;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
}
