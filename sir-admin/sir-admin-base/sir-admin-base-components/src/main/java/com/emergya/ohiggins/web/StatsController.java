/*
 * StatsController.java
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
 * Authors:: Antonio Hernández Díaz (mailto:ahernandez@emergya.com)
 */
package com.emergya.ohiggins.web;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Simple controller for tracking statistics.
 * 
 * @author <a href="mailto:ahernandez@emergya.com">ahernandez</a>
 */
@Controller
public class StatsController {

	/**
	 * Group request by funcionality.
	 * 
	 * @param moduleName
	 * @return Map<String, Object>
	 */
	@RequestMapping(value = "/stats/modules/{moduleName}", produces = { MediaType.APPLICATION_JSON_VALUE })
	@ResponseBody
	public Map<String, Object> trackModules(
			@PathVariable("moduleName") String moduleName) {
		Map<String, Object> response = new HashMap<String, Object>();
		response.put("success", true);
		return response;
	}

	/**
	 * Group requests by requested channels.
	 * 
	 * @param channelName
	 * @return Map<String, Object>
	 */
	@RequestMapping(value = "/stats/channels/{channelName}", produces = { MediaType.APPLICATION_JSON_VALUE })
	@ResponseBody
	public Map<String, Object> trackChannels(
			@PathVariable("channelName") String channelName) {
		Map<String, Object> response = new HashMap<String, Object>();
		response.put("success", true);
		return response;
	}
}
