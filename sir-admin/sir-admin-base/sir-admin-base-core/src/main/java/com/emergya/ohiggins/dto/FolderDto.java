/*
 * FolderDto.java
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

import com.emergya.ohiggins.service.FolderService;

/**
 * Representa al Folder
 * 
 * @author jariera
 * 
 */
public class FolderDto implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 989029520936763043L;

	private Long id;
	private String name;
	private Boolean enabled;
	private Boolean isChannel;
	private Date createDate;
	private Date updateDate;
	private Integer folderOrder;
	private AuthorityDto authority;
	private UsuarioDto user;
	private FolderDto parent;
	private Boolean isPlain;

	// FIXME, resto de atributos
	// Lis<ZoneDto>
	private NivelTerritorialDto zone;
	private String zoneSeleccionada;
	private FolderTypeDto folderType;
	private String folderTypeSelected;
	
	// IPT Types
	public static final Long CANAL = new Long(1);
	public static final Long PRC = new Long(2);
	public static final Long PRI = new Long(3);
	public static final Long CARPETA = new Long(7);
	

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
		if (name != null) {
			name = name.trim();
		}
		this.name = name;
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

	public Integer getFolderOrder() {
		return folderOrder;
	}

	public void setFolderOrder(Integer folderOrder) {
		this.folderOrder = folderOrder;
	}

	public AuthorityDto getAuthority() {
		return authority;
	}

	public void setAuthority(AuthorityDto authority) {
		this.authority = authority;
	}

	public UsuarioDto getUser() {
		return user;
	}

	public void setUser(UsuarioDto user) {
		this.user = user;
	}

	public FolderDto getParent() {
		return parent;
	}

	/**
	 * Permite saber si el canal tiene padre.
	 * 
	 * @return
	 */
	public boolean getHasParent() {
		return parent != null && parent.getId() != null;
	}

	public void setParent(FolderDto parent) {
		this.parent = parent;
	}

	public NivelTerritorialDto getZone() {
		return zone;
	}

	public void setZone(NivelTerritorialDto zone) {
		this.zone = zone;
	}

	public String getZoneSeleccionada() {
		return zoneSeleccionada;
	}

	public void setZoneSeleccionada(String zoneSeleccionada) {
		this.zoneSeleccionada = zoneSeleccionada;
	}

	public FolderTypeDto getFolderType() {
		return folderType;
	}

	public void setFolderType(FolderTypeDto folderType) {
		this.folderType = folderType;
	}

	public String getFolderTypeSelected() {
		return folderTypeSelected;
	}

	public void setFolderTypeSelected(String folderTypeSelected) {
		this.folderTypeSelected = folderTypeSelected;
	}
	
	public Boolean getIsPlain() {
		isPlain = null;
		if(this.folderTypeSelected != null
				&& new Long(this.folderTypeSelected) > FolderService.DEFAULT_FOLDER_TYPE){
			if(PRC.equals(new Long(this.folderTypeSelected))){
				isPlain = false;
			}else if(PRI.equals(new Long(this.folderTypeSelected))){
				isPlain = true;
			}
		}
		return isPlain;
	}

	public void setIsPlain(Boolean isPlain) {
		if(isPlain != null){
			if(isPlain){
				this.folderTypeSelected = PRI.toString();
			}else{
				this.folderTypeSelected = PRC.toString();
			}
		}else{
			if(this.isChannel){
				this.folderTypeSelected = CANAL.toString();
			}else{
				this.folderTypeSelected = CARPETA.toString();
			}
		}
		this.isPlain = isPlain;
	}
}

