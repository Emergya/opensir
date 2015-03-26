/*
 * UpdatedLayersGeoServerImpl.java
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
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.emergya.ohiggins.stats.dao.GeoServerDao;
import com.emergya.ohiggins.stats.dao.ISerieDao;
import com.emergya.ohiggins.stats.dto.SerieDto;

/**
 * Dao for returning the updated layers from GeoServer
 * 
 * @author <a href="mailto:ahernandez@emergya.com">ahernandez</a>
 * 
 */
public class UpdatedLayersGeoServerImpl extends GeoServerDao implements
		ISerieDao {
	private static final Log LOG = LogFactory
			.getLog(UpdatedLayersGeoServerImpl.class);


	private static final DateFormat DATE_FORMAT = new SimpleDateFormat(
			"yyyy-MM-dd");


	/**
	 * Returns a serie. Don't use the value "range" for the parameter "period",
	 * call findByPeriod(Date, Date) instead.
	 * 
	 * @param period
	 * @param date
	 * @return SerieDto
	 */
	public SerieDto findByPeriod(String period, Date date) {

		Date from = date;
		Date to = date;
		Calendar calendar = Calendar.getInstance();

		calendar.setTime(date);

		if (SerieDto.RANGE.equals(period)) {
			from = date;
			to = date;
		} else if (SerieDto.DAY.equals(period)) {
			from = date;
			to = date;
		} else if (SerieDto.WEEK.equals(period)) {
			calendar.set(Calendar.DAY_OF_WEEK, 1);
			from = calendar.getTime();
			calendar.set(Calendar.DAY_OF_WEEK, 7);
			to = calendar.getTime();
		} else if (SerieDto.MONTH.equals(period)) {
			calendar.set(Calendar.DAY_OF_MONTH, 1);
			from = calendar.getTime();
			calendar.set(Calendar.DAY_OF_MONTH, 31);
			to = calendar.getTime();
		} else if (SerieDto.YEAR.equals(period)) {
			calendar.set(Calendar.DAY_OF_YEAR, 1);
			from = calendar.getTime();
			calendar.set(Calendar.DAY_OF_YEAR, 366);
			to = calendar.getTime();
		}

		SerieDto serie = findByPeriod(from, to);
		serie.setPeriod(period);
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
		if (LOG.isDebugEnabled()) {
			LOG.debug("Getting requested updated layers stats between " + from
					+ " and " + to);
		}

		String startDate = DATE_FORMAT.format(from);
		String endDate = DATE_FORMAT.format(to);

		String query = "select a.name, sum(1) as total from  request_resources a join request b ";
		query = query
				.concat("on a.request_id = b.id where b.service = 'WMS' and start_time between '%start%' and '%end%' ");
		query = query
				.concat("and b.http_method = 'PUT' group by a.name order by total desc");

		query = query.replaceAll("%start%", startDate);
		query = query.replaceAll("%end%", endDate);

		List<Map<String, Object>> result = query(query);

		SerieDto serie = new SerieDto();
		// Use a LinkedHashMap for preserving insertion order
		HashMap<String, Object> values = new LinkedHashMap<String, Object>();

		Iterator<Map<String, Object>> it = result.iterator();
		while (it.hasNext()) {
			Map<String, Object> item = it.next();
			values.put(item.get("name").toString(), item.get("total"));
		}

		serie.setName("Updated layers");
		serie.setPeriod(SerieDto.RANGE);
		serie.setValues(values);
		serie.setFrom(from);
		serie.setTo(to);

		return serie;
	}

	/**
	 * This method is unsupported in this implementation class. Please use
	 * {@link UpdatedLayersGeoServerImpl#findByPeriod(Date, Date)} instead.
	 */
	@Override
	public SerieDto find() {
		throw new UnsupportedOperationException(
				"Do not use method find() with UpdatedLayersGeoServerImpl. Please use findByPeriod(Date, Date) instead");
	}

}
