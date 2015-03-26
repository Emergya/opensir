/*
 * LayerPropertyDto.java
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

import com.emergya.ohiggins.model.LayerPropertyEntity;

/**
 * Dto para layerProperty
 *
 * @author jariera
 *
 */
public final class LayerPropertyDto implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 6810250086862413752L;
    private Long id;
    private String name;
    private String value;

    public LayerPropertyDto() {
    }

    public LayerPropertyDto(String name, String value) {
	this.setName(name);
	this.setValue(value);
    }

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

    public String getValue() {
	return value;
    }

    public void setValue(String value) {
	this.value = value;
    }

    public static LayerPropertyDto fromEntity(LayerPropertyEntity propEnt) {
	LayerPropertyDto dto = new LayerPropertyDto();
	dto.setId(propEnt.getId());
	dto.setName(propEnt.getName());
	dto.setValue(propEnt.getValue());
	return dto;
    }

    @Override
    public LayerPropertyDto clone() {
	LayerPropertyDto clone = new LayerPropertyDto();
	
	clone.id = this.id;
	clone.name = this.name;
	clone.value = this.value;
	return clone;
    }
}
