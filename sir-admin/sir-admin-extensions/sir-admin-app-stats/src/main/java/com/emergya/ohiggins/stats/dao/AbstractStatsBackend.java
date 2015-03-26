/*
 * AbstractStatsBackend.java
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
 * Authors:: Antonio Hernández (mailto:ahernandez@emergya.com)
 */
package com.emergya.ohiggins.stats.dao;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * Abstract backend
 * 
 * @author <a href="mailto:ahernandez@emergya.com">ahernandez</a>
 * 
 */
public abstract class AbstractStatsBackend {

	/**
	 * Performs a query
	 * 
	 * @param params
	 * @return List<Map<String, Object>
	 */
	public abstract List<Map<String, Object>> query(Map<String, String> params);

	/**
	 * Performs a query
	 * 
	 * @param query
	 * @return List<Map<String, Object>
	 */
	public abstract List<Map<String, Object>> query(String query);

	public Map<String, Object> sortByValuesDesc(Map<String, Object> originalMap) {
		ValueComparator bvc = new ValueComparator(originalMap);
		Map<String, Object> sortedMap = new TreeMap<String, Object>(bvc);
		sortedMap.putAll(originalMap);
		return sortedMap;
	}

}

class ValueComparator implements Comparator<Object> {

	Map<String, Object> base;

	public ValueComparator(Map<String, Object> base) {
		this.base = base;
	}

	// Note: this comparator imposes orderings that are inconsistent with
	// equals.
	public int compare(Object a, Object b) {
		if (base.get(a) instanceof Integer && base.get(b) instanceof Integer
				&& (Integer) base.get(a) >= (Integer) base.get(b)) {
			if (a instanceof String && b instanceof String
					&& (Integer) base.get(a) == (Integer) base.get(b)) {
				return ((String)a).compareTo((String) b);
			}
			return -1;
		} else {
			return 1;
		} // returning 0 would merge keys
	}

}
