/*
 * StatsService.java
 *
 * Copyright (C) 2011
 *
 * This file is part of Proyecto persistenceGeo
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
 * Authors:: Antonio Hernández Díaz (mailto:ahernandez@emergya.com)
 */
package com.emergya.ohiggins.stats.service;

import java.util.Date;
import java.util.Map;

import com.emergya.ohiggins.stats.dao.ISerieDao;
import com.emergya.ohiggins.stats.dao.impl.GlobalsPiwikImpl;
import com.emergya.ohiggins.stats.dao.impl.GoalsPiwikImpl;
import com.emergya.ohiggins.stats.dao.impl.RequestedChannelsPiwikImpl;
import com.emergya.ohiggins.stats.dao.impl.RequestedLayersGeoServerImpl;
import com.emergya.ohiggins.stats.dao.impl.UpdatedLayersGeoServerImpl;
import com.emergya.ohiggins.stats.dto.SerieDto;
import com.google.inject.internal.Maps;

/**
 * Stats service
 * 
 * @author <a href="mailto:ahernandez@emergya.com">ahernandez</a>
 * 
 */
public class StatsService {

	private ISerieDao statsGlobals;
	private GoalsPiwikImpl statsGoals;
	private RequestedLayersGeoServerImpl statsRequestedLayers;
	private UpdatedLayersGeoServerImpl statsUpdatedLayers;
	private RequestedChannelsPiwikImpl statsRequestedChannels;

	public StatsService() {
		statsGlobals = new GlobalsPiwikImpl();
		statsGoals = new GoalsPiwikImpl();
		statsRequestedLayers = new RequestedLayersGeoServerImpl();
		statsUpdatedLayers = new UpdatedLayersGeoServerImpl();
		statsRequestedChannels = new RequestedChannelsPiwikImpl();
	}

	/**
	 * Returns a HashMap with all the stats.
	 * 
	 * @param from
	 * @param to
	 * @return Map<String, SerieDto>
	 */
	public Map<String, SerieDto> findByPeriod(Date from, Date to) {

		Map<String, SerieDto> stats = Maps.newHashMap();

		stats.put("globals", statsGlobals.find());
		stats.put("goals", statsGoals.findByPeriod(from, to));
		stats.put("requested_layers",
				statsRequestedLayers.findByPeriod(from, to));
		stats.put("updated_layers", statsUpdatedLayers.findByPeriod(from, to));
		stats.put("requested_channels",
				statsRequestedChannels.findByPeriod(from, to));

		return stats;
	}

}
