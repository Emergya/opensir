/*
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
 * Authors:: María Arias de Reyna (mailto:delawen@gmail.com)
 */

package com.emergya.ohiggins.cmis.criteria;

import org.apache.chemistry.opencmis.client.api.ObjectType;
import org.apache.chemistry.opencmis.commons.definitions.PropertyDefinition;

/**
 * Dada una propiedad y su valor, los compara
 * 
 * @author marias
 * 
 */
public class LikeCriteria extends Criteria {

	private String property;
	private String value;
	private LikeCriteriaMode mode;

	public LikeCriteria(String property, String value) {
		this(property, value, LikeCriteriaMode.ANYWHERE);
	}
	
	public LikeCriteria(String property, String value, LikeCriteriaMode mode) {
		this.property = property;
		this.value = value;
		this.mode = mode;
	}

	@Override
	public String toString() {
		return String.format("%s LIKE %s",
				this.getPrefixedField(property),
				String.format(
						this.mode.pattern,
						this.value.replaceAll(SINGLE_QUOTE,ESCAPED_SINGLE_QUOTE).trim()));
	}

	@Override
	public void initializeCriteria(ObjectType type) {

		PropertyDefinition<?> objectIdPropDef = type.getPropertyDefinitions()
				.get(this.property);
		this.property = objectIdPropDef.getQueryName();
	}

}
