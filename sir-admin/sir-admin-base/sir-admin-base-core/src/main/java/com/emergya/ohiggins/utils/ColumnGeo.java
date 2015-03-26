/*
 * ColumnGeo.java
 * 
 * Copyright (C) 2013
 * 
 * This file is part of ohiggins-core
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
 * Authors:: Antonio J. Rodríguez (mailto:ajrodriguez@emergya.com)
 */
package com.emergya.ohiggins.utils;


/**
 * JDBC column metadata
 * 
 * @author ajrodriguez
 */
public class ColumnGeo {
	private String name;
	private DataBaseType type;
	
	public ColumnGeo() {
		super();
	}
	
	public ColumnGeo(String name, DataBaseType type) {
		this.name  = name;
		this.type = type;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name
	 *            the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the type
	 */
	public DataBaseType getType() {
		return type;
	}

	/**
	 * @param type
	 *            the type to set
	 */
	public void setType(DataBaseType type) {
		this.type = type;
	}
	
	public String toString() {
		return String.format("\"%s\" %s", name, type);
	}

}
