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

/**
 * Dados dos criteria, los une mediante un AND.
 * 
 * @author marias
 * 
 */
public class AndCriteria extends Criteria {

	private Criteria c1;
	private Criteria c2;

	public AndCriteria(Criteria c1, Criteria c2) {
		this.c1 = c1;
		this.c2 = c2;
	}

	@Override
	public String toString() {
		return "(" + this.c1.toString() + ") AND (" + this.c2.toString() + ")";
	}

	@Override
	public void initializeCriteria(ObjectType objectType) {
		this.c1.initializeCriteria(objectType);
		this.c2.initializeCriteria(objectType);

	}

	@Override
	public void setPrefix(String p) {
		this.c1.setPrefix(p);
		this.c2.setPrefix(p);
	}
}
