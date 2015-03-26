/*
 * LayerDto.java
 * 
 * Copyright (C) 2012
 * 
 * This file is part of Proyecto ohiggins
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
 * Authors:: Jose Alfonso Riera (mailto:jariera@emergya.com)
 */
package com.emergya.ohiggins.dto;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;

/**
 * Dto para Layer
 * 
 * @author jariera
 * 
 */
public class LayerDto implements Serializable {
	private static final long serialVersionUID = 989029520936763030L;

	public static final String NAME_PROPERTY = "name";

	private Long id;
	private String name;
	private int order;
	private LayerTypeDto type; // cambia
	private String server_resource;
	private Boolean publicized;
	private Boolean enabled;
	private Boolean isChannel;
	private Date createDate;
	private Date updateDate;
	private UsuarioDto user;
	private AuthorityDto authority;
	private List<StyleDto> styleList;// cambia
	private FolderDto folder; // Cambia
	private byte[] data;
	private List<LayerPropertyDto> properties;// cambia
	private LayerMetadataDto metadata;
	private String tableName;

	private AuthorityDto requestedByAuthority;

	private String layerTitle;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getOrder() {
		return order;
	}

	public void setOrder(int order) {
		this.order = order;
	}

	public void setOrder(String order) {
		this.order = -1;
		if (order != null) {
			this.order = Integer.parseInt(order);
		}
	}

	public LayerTypeDto getType() {
		return type;
	}

	public void setType(LayerTypeDto type) {
		this.type = type;
	}

	public String getServer_resource() {
		return server_resource;
	}

	public void setServer_resource(String server_resource) {
		this.server_resource = server_resource;
	}

	public Boolean getPublicized() {
		return publicized;
	}

	public void setPublicized(Boolean publicized) {
		this.publicized = publicized;
	}

	public Boolean getEnabled() {
		return enabled;
	}

	public void setEnabled(Boolean enabled) {
		this.enabled = enabled;
	}

	public Boolean getIsChannel() {
		return isChannel;
	}

	public void setIsChannel(Boolean isChannel) {
		this.isChannel = isChannel;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public Date getUpdateDate() {
		return updateDate;
	}

	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}

	public UsuarioDto getUser() {
		return user;
	}

	public void setUser(UsuarioDto user) {
		this.user = user;
	}

	public AuthorityDto getAuthority() {
		return authority;
	}

	public void setAuthority(AuthorityDto auth) {
		this.authority = auth;
	}

	public List<StyleDto> getStyleList() {
		return styleList;
	}

	public void setStyleList(List<StyleDto> styleList) {
		this.styleList = styleList;
	}

	public FolderDto getFolder() {
		return folder;
	}

	public void setFolder(FolderDto folder) {
		this.folder = folder;
	}

	public byte[] getData() {
		return data;
	}

	public void setData(byte[] data) {
		this.data = ArrayUtils.clone(data);
	}

	public List<LayerPropertyDto> getProperties() {
		return properties;
	}

	public void setProperties(List<LayerPropertyDto> properties) {
		this.properties = properties;
	}

	public LayerMetadataDto getMetadata() {
		return metadata;
	}

	public void setMetadata(LayerMetadataDto metadata) {
		this.metadata = metadata;
	}

	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public AuthorityDto getRequestedByAuth() {
		return requestedByAuthority;
	}

	public void setRequestedByAuth(AuthorityDto auth) {
		this.requestedByAuthority = auth;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		LayerDto other = (LayerDto) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
	
	public String getLayerTitle() {
		return this.layerTitle;
	}

	public void setLayerTitle(String layerTitle) {
		this.layerTitle = layerTitle;		
	}

	public String getNameWithoutWorkspace() {
		if(StringUtils.isEmpty(name)) {
			return StringUtils.EMPTY;
		}
		int wsEnd = this.name.indexOf(":");
		return this.name.substring(wsEnd+1);
	}
	
	/**
	 * If the layer has title, the title, if not the name of the layer without workspace.
	 * @return
	 */
	public String getLayerLabel() {
		if(StringUtils.isEmpty(this.layerTitle)) {
			return this.getNameWithoutWorkspace();
		}
		
		return layerTitle;
	}
}
