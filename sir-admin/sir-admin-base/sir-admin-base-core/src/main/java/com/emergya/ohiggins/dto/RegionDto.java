package com.emergya.ohiggins.dto;

import java.io.Serializable;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotBlank;

public class RegionDto implements Serializable {

	private static final long serialVersionUID = 141105725886291516L;

	public static final String ID_PROPERTY = "id";

	private Long id;
	private String name_region;
	private String prefix_wks;
	private String node_analytics;
	private String node_publicacion;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@NotBlank
	@NotNull
	public String getName_region() {
		return name_region;
	}

	public void setName_region(String name_region) {
		this.name_region = name_region;
	}

	@NotBlank
	@NotNull
	public String getPrefix_wks() {
		return prefix_wks;
	}

	public void setPrefix_wks(String prefix_wks) {
		this.prefix_wks = prefix_wks;
	}

	@NotBlank
	@NotNull
	public String getNode_analytics() {
		return node_analytics;
	}

	public void setNode_analytics(String node_analytics) {
		this.node_analytics = node_analytics;
	}

	@NotBlank
	@NotNull
	public String getNode_publicacion() {
		return node_publicacion;
	}

	public void setNode_publicacion(String node_publicacion) {
		this.node_publicacion = node_publicacion;
	}
}
