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
 * Authors:: Luis Román Gutiérrez (mailto:lroman@emergya.com)
 */
package com.emergya.ohiggins.cmis.criteria;

import org.apache.chemistry.opencmis.client.api.ObjectType;

public class InDocumentCriteria extends Criteria{
	
	private String text;
	
	public InDocumentCriteria(String text) {
		this.text = text;
	}

	@Override
	public String toString() {
		return String.format("CONTAINS(D, '%s')",
				text.replaceAll(SINGLE_QUOTE, ESCAPED_SINGLE_QUOTE));
	}

	@Override
	public void initializeCriteria(ObjectType objectType) {
		// TODO Auto-generated method stub
		
	}

}
