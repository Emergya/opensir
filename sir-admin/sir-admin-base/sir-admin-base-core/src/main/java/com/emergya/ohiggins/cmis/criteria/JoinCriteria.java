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

import java.util.ArrayList;
import java.util.List;

import org.apache.chemistry.opencmis.client.api.ObjectType;
import org.apache.commons.lang3.StringUtils;

/**
 * Allows the creation of a conjunction or disjunction of many criterias at the
 * same time.
 * 
 * @author lroman 
 */
public class JoinCriteria extends Criteria {
	
	public final static String OR = "OR";
	public final static String AND = "AND";
	
	List<Criteria> joinedCriteria;
	String operation;
	
	public JoinCriteria(String operation) {
		if(!operation.equals(OR) && !operation.equals(AND)) {
			throw new IllegalArgumentException("Only AND and OR are supported as joining operators");
		}
		
		this.operation = operation;
		joinedCriteria = new ArrayList<Criteria>();
	}
	
	public void add(Criteria c) {
		c.setPrefix(this.getPrefix());
		joinedCriteria.add(c);
	}

	@Override
	public String toString() {
		
		return "("+ StringUtils.join(joinedCriteria, " "+operation+" ")+")";
	}

	@Override
	public void initializeCriteria(ObjectType objectType) {
		for(Criteria c : joinedCriteria) {
			c.initializeCriteria(objectType);
		}

	}

}
