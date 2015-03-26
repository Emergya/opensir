/*
 * SerieDto.java
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
package com.emergya.ohiggins.stats.dto;

import java.util.Date;
import java.util.Map;

/**
 * Represents a list of values
 * 
 * @author <a href="mailto:ahernandez@emergya.com">ahernandez</a>
 */
public class SerieDto {

	// public enum Period {
	// RANGE, DAY, WEEK, MONTH, YEAR
	// };

	public final static String RANGE = "range";
	public final static String DAY = "day";
	public final static String WEEK = "week";
	public final static String MONTH = "month";
	public final static String YEAR = "year";

	private String name;
	private String period;
	private Date from;
	private Date to;
	private Map<?, ?> values;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPeriod() {
		return period;
	}

	public void setPeriod(String period) {
		this.period = period;
	}

	public Date getFrom() {
		return from;
	}

	public void setFrom(Date from) {
		this.from = from;
	}

	public Date getTo() {
		return to;
	}

	public void setTo(Date to) {
		this.to = to;
	}

	public Map<?, ?> getValues() {
		return values;
	}

	public void setValues(Map<?, ?> values) {
		this.values = values;
	}
}
