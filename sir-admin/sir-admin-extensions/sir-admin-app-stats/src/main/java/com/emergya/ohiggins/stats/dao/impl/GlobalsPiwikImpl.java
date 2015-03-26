/*
 * GlobalsPiwikImpl.java
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

import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.emergya.ohiggins.stats.dao.ISerieDao;
import com.emergya.ohiggins.stats.dao.PiwikDao;
import com.emergya.ohiggins.stats.dto.SerieDto;
import com.google.common.collect.Maps;

/**
 * Dao for series returned from a Piwik backend.
 * 
 * @author <a href="mailto:ahernandez@emergya.com">ahernandez</a>
 * 
 */
public class GlobalsPiwikImpl extends PiwikDao implements ISerieDao {

	private static final Log LOG = LogFactory.getLog(GlobalsPiwikImpl.class);

	private static final String YEAR_KEY = "year";
	private static final String MONTH_KEY = "month";
	private static final String WEEK_KEY = "week";
	private static final String TODAY_KEY = "today";
	private static final String YESTERDAY_KEY = "yesterday";
	private static final String DAY_KEY = "day";
	private final Map<String, String> defaultParams;

	public GlobalsPiwikImpl() {
		Map<String, String> temp = Maps.newHashMap();
		temp.put("method", "VisitsSummary.get");
		temp.put("format", "json");
		temp.put("columns", NB_VISITS_KEY + "," + NB_UNIQ_VISITORS_KEY);
		temp.put("date", TODAY_KEY);

		defaultParams = Collections.unmodifiableMap(temp);
	}

	/**
	 * Returns a serie
	 * 
	 * @return SerieDto
	 */
	public SerieDto find() {
		if (LOG.isDebugEnabled()) {
			LOG.debug("Getting global statistics for yesterday, current week, "
					+ "current month and current year");
		}

		Map<String, String> params = new HashMap<String, String>(defaultParams);

		Map<String, Object> values = new HashMap<String, Object>();
		List<Map<String, Object>> results;
		Map<String, Object> data;

		// Ayer
		params.put("period", DAY_KEY);
		params.put("date", YESTERDAY_KEY);

		results = query(params);
		if (!results.isEmpty()) {
			data = results.get(0);
			values.put("visits_today", data.get(NB_VISITS_KEY));
			values.put("unique_visits_today", data.get(NB_UNIQ_VISITORS_KEY));
		}
		params.put("date", TODAY_KEY);

		// Semana
		params.put("period", WEEK_KEY);

		results = query(params);
		if (!results.isEmpty()) {
			data = results.get(0);
			values.put("visits_week", data.get(NB_VISITS_KEY));
			values.put("unique_visits_week", data.get(NB_UNIQ_VISITORS_KEY));
		}

		// Mes
		params.put("period", MONTH_KEY);
		results = query(params);
		if (!results.isEmpty()) {
			data = results.get(0);
			values.put("visits_month", data.get(NB_VISITS_KEY));
			values.put("unique_visits_month", data.get(NB_UNIQ_VISITORS_KEY));
		}

		// Año
		params.put("period", YEAR_KEY);
		results = query(params);
		if (!results.isEmpty()) {
			data = results.get(0);
			values.put("visits_year", data.get(NB_VISITS_KEY));
			values.put("unique_visits_year", data.get(NB_UNIQ_VISITORS_KEY));
		}

		Calendar calendar = Calendar.getInstance();
		SerieDto serie = new SerieDto();
		serie.setName("Global stats");
		serie.setPeriod(SerieDto.YEAR);
		calendar.set(Calendar.DAY_OF_YEAR, 1);
		serie.setFrom(calendar.getTime());
		calendar.set(Calendar.DAY_OF_YEAR, 366);
		serie.setTo(calendar.getTime());
		serie.setValues(values);

		return serie;
	}

	/**
	 * Returns a serie
	 * 
	 * @param from
	 * @param to
	 * @return SerieDto
	 */
	public SerieDto findByPeriod(Date from, Date to) {
		throw new UnsupportedOperationException(
				"findByPeriod(Date from, Date to) is not supported in GobalsPiwikImpl class. Please call method find() instead.");
	}

	@Override
	protected String getUrlPortion() {
		throw new UnsupportedOperationException("getUrlPortion is not used in GolobalsPiwikImpl. If this change please implement this method.");
	}

}
