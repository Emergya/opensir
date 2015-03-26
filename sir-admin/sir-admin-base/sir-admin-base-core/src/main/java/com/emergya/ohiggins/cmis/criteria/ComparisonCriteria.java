/*
 * 
 * Copyright (C) 2013
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
 * Authors:: Luis Román Gutiérrez (lroman@emergya.com)
 */

package com.emergya.ohiggins.cmis.criteria;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.Arrays;

import org.apache.chemistry.opencmis.client.api.ObjectType;
import org.apache.chemistry.opencmis.commons.definitions.PropertyDefinition;

/**
 * Generics comparison criteria for filtering Alfresco published documents.
 * 
 * Supports the following comparsion operators: '=', '<', '>', '<=', '>=';
 * 
 * @author lroman
 *
 */
public class ComparisonCriteria extends Criteria {

	/**
	 * Equality operator '='
	 */
	public static final String EQUALS = "=";
	/**
	 * Greater than operator '>'
	 */
	public static final String GT = ">";
	/**
	 * Greater or equals operator '>='
	 */
	public static final String GE = ">=";
	/**
	 * Less than operator '<'
	 */
	public static final String LT = "<";
	/**
	 * Less or equals operator '<='
	 */
	public static final String LE = "<=";
	
	/**
	 * Not equals operator '<>';
	 */
	public static final String NEQ= "<>";

	private static final String TIMESTAMP_PATTERN="YYYY-MM-dd'T'HH:mm:ss.SSS'Z'";
	
	private String property;
	private Object value;
	private String operator;

	public ComparisonCriteria(String property, String operator, Object value) {
		this.property = property;
		this.value = value;
		
		if(Arrays.asList(new String[]{EQUALS, GT, GE,LT,LE,NEQ}).indexOf(operator)==-1) {
			throw new IllegalArgumentException("Illegal operator for comparison criteria, must be one of '=', '<', '>=', '<', '<=','<>");
		}
		
		this.operator = operator;
	}

	@Override
	public String toString() {
		
		return String.format("%s %s %s",
				getPrefixedField(property),
				operator,
				valueToString(value));
	}
	
	private String valueToString(Object value) {
		if(value.getClass().isAssignableFrom(String.class)) {
			return "'"+this.value.toString().replaceAll(SINGLE_QUOTE, ESCAPED_SINGLE_QUOTE).trim()+"'";
		}
		
		if(value.getClass().isAssignableFrom(Date.class)) {
			return "TIMESTAMP '"+(new SimpleDateFormat(TIMESTAMP_PATTERN)).format(value)+"'";
		}
		
		return value.toString();
	}

	@Override
	public void initializeCriteria(ObjectType type) {

		PropertyDefinition<?> objectIdPropDef = type.getPropertyDefinitions()
				.get(this.property);
		this.property = objectIdPropDef.getQueryName();
	}
}
