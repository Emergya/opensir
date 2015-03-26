/*
 * GoalsPiwikImpl.java
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
package com.emergya.ohiggins.stats.dao.impl;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.emergya.ohiggins.stats.dao.ISerieDao;
import com.emergya.ohiggins.stats.dao.PiwikDao;
import com.emergya.ohiggins.stats.dto.SerieDto;

/**
 * Dao for series returned from a Piwik backend
 * 
 * @author <a href="mailto:ahernandez@emergya.com">ahernandez</a>
 * 
 */
public class GoalsPiwikImpl extends PiwikDao implements ISerieDao {

	private static final Log LOG = LogFactory.getLog(GoalsPiwikImpl.class);

	private final String URL_PORTION = "/stats/modules";

	private static final DateFormat DATE_FORMAT = new SimpleDateFormat(
			"yyyy-MM-dd");

	private Hashtable<String, String> params;

	public GoalsPiwikImpl() {

		params = new Hashtable<String, String>();
		params.put("format", "json");
	}

	/**
	 * Returns the list of modules
	 * 
	 * @return List<Map<String, Object>>
	 */
	private List<Map<String, Object>> getModuleList(String period, String date) {
		params.put("method", "Actions.getPageUrls");
		params.put("segment", "pageUrl=@/stats/modules");
		params.put("flat", "1");
		params.put("period", period);
		params.put("date", date);
		List<Map<String, Object>> channelList = query(params);
		return channelList;
	}

	/**
	 * Returns a serie
	 * 
	 * @param period
	 * @param date
	 * @return SerieDto
	 */
	private SerieDto find(String period, String date) {

		List<Map<String, Object>> modules = getModuleList(period, date);

		return getOrderedSerieFromMap(modules, period, "Requested modules");

	}

	/**
	 * Returns a serie
	 * 
	 * @param from
	 * @param to
	 * @return SerieDto
	 */
	public SerieDto findByPeriod(Date from, Date to) {

		String strDateFrom = DATE_FORMAT.format(from);
		String strDateTo = DATE_FORMAT.format(to);
		String strDate = strDateFrom + "," + strDateTo;

		SerieDto serie = find(SerieDto.RANGE, strDate);
		serie.setFrom(from);
		serie.setTo(to);

		return serie;
	}

	/**
	 * This method is unsupported in this implementation class. Please use
	 * {@link GoalsPiwikImpl#findByPeriod(Date, Date)} instead.
	 */
	@Override
	public SerieDto find() {
		throw new UnsupportedOperationException(
				"Do not call this on GoalsPiwikImpl. Use findByPeriod(Date, Date) instead");
	}

	@Override
	protected String getUrlPortion() {
		return this.URL_PORTION;
	}

}
