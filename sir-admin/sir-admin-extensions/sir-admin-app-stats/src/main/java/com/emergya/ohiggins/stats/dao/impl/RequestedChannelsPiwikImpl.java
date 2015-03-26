/*
 * RequestedChannelsPiwikImpl.java
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
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.emergya.ohiggins.stats.dao.ISerieDao;
import com.emergya.ohiggins.stats.dao.PiwikDao;
import com.emergya.ohiggins.stats.dto.SerieDto;
import com.google.common.collect.Maps;

/**
 * Get Statistics for the requested channels from a Piwik backend.
 * 
 * @author <a href="mailto:ahernandez@emergya.com">ahernandez</a>
 * 
 */
public class RequestedChannelsPiwikImpl extends PiwikDao implements ISerieDao {
	private static final String URL_PORTION = "/stats/channels";

	private static final Log LOG = LogFactory
			.getLog(RequestedChannelsPiwikImpl.class);

	private static final DateFormat DATE_FORMAT = new SimpleDateFormat(
			"yyyy-MM-dd");

	private final Map<String, String> defaultParams;

	public RequestedChannelsPiwikImpl() {
		Map<String, String> temp = Maps.newHashMap();
		temp.put("format", "json");
		temp.put("method", "Actions.getPageUrls");
		temp.put("segment", "pageUrl=@/stats/channels");
		temp.put("flat", "1");
		defaultParams = Collections.unmodifiableMap(temp);

	}

	/**
	 * Returns the list of channels
	 * 
	 * @return List<Map<String, Object>>
	 */
	private List<Map<String, Object>> getChannelList(String period, String date) {
		Map<String, String> params = Maps.newHashMap(defaultParams);
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
		List<Map<String, Object>> channels = getChannelList(period, date);

		return getOrderedSerieFromMap(channels, period, "Requested channels");
	}

	/**
	 * Returns a serie
	 * 
	 * @param from
	 * @param to
	 * @return SerieDto
	 */
	public SerieDto findByPeriod(Date from, Date to) {
		if (LOG.isDebugEnabled()) {
			LOG.debug("Getting requested channels stats between " + from
					+ " and " + to);
		}

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
	 * {@link RequestedChannelsPiwikImpl#findByPeriod(Date, Date)} instead.
	 */
	@Override
	public SerieDto find() {
		throw new UnsupportedOperationException(
				"Don't use find() method in RequestedChannelPiwikImpl. Please use findByPeriod(Date, Date) instead.");
	}

	@Override
	protected String getUrlPortion() {
		return URL_PORTION;
	}

}
