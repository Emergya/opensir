/*
 * MapConfigurationEntity.java
 * 
 * Copyright (C) 2012
 * 
 * This file is part of Proyecto persistenceGeo
 * 
 * This software is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License as published by the Free
 * Software Foundation; either version 2 of the License, or (at your option) any
 * later version.
 * 
 * This software is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU General Public License along with
 * this library; if not, write to the Free Software Foundation, Inc., 51
 * Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA
 * 
 * As a special exception, if you link this library with other files to produce
 * an executable, this library does not by itself cause the resulting executable
 * to be covered by the GNU General Public License. This exception does not
 * however invalidate any other reasons why the executable file might be covered
 * by the GNU General Public License.
 * 
 */
package com.emergya.ohiggins.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.emergya.persistenceGeo.metaModel.AbstractMapConfigurationEntity;

/**
 * MapConfigurationEntity
 * 
 * @author <a href="mailto:mmartos@emergya.com">mmartos</a>
 * 
 */
@Entity
@Table(name = "gis_map_conf")
public class MapConfigurationEntity extends AbstractMapConfigurationEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4734979934356043083L;

	public MapConfigurationEntity() {

	}

	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "gis_map_conf_seq")
	@SequenceGenerator(name = "gis_map_conf_seq", sequenceName = "gis_map_conf_seq", initialValue = 0, allocationSize = 200)
	public Long getId() {
		return id;
	}

	@Column(name = "bbox")
	public String getBbox() {
		return bbox;
	}

	@Column(name = "PDFServer")
	public String getPDFServer() {
		return PDFServer;
	}

	@Column(name = "uploadServletURL")
	public String getUploadServletURL() {
		return uploadServletURL;
	}

	@Column(name = "downloadServletURL")
	public String getDownloadServletURL() {
		return downloadServletURL;
	}

	@Column(name = "defaultUserLogo")
	public String getDefaultUserLogo() {
		return defaultUserLogo;
	}

	@Column(name = "defaultWMSServer")
	public String getDefaultWMSServer() {
		return defaultWMSServer;
	}

	@Column(name = "openLayersProxyHost")
	public String getOpenLayersProxyHost() {
		return OpenLayersProxyHost;
	}

	@Column(name = "defaultIdioma")
	public String getDefaultIdioma() {
		return defaultIdioma;
	}

	@Column(name = "numZoomLevels")
	public String getNumZoomLevels() {
		return numZoomLevels;
	}

	@Column(name = "displayProjection")
	public String getDisplayProjection() {
		return displayProjection;
	}

	@Column(name = "projection")
	public String getProjection() {
		return projection;
	}

	@Column(name = "initialBbox")
	public String getInitalBbox() {
		return initalBbox;
	}

	@Column(name = "maxScale")
	public String getMaxScale() {
		return maxScale;
	}

	@Column(name = "minScale")
	public String getMinScale() {
		return minScale;
	}

	@Column(name = "resolutions")
	public String getResolutions() {
		return resolutions;
	}

	@Column(name = "maxResolution")
	public String getMaxResolution() {
		return maxResolution;
	}

	@Column(name = "version")
	public String getVersion() {
		return version;
	}

	@Column(name = "minResolution")
	public String getMinResolution() {
		return minResolution;
	}

}
