/*
 * PiwikDao.java
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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Properties;

import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;

import com.emergya.ohiggins.stats.dto.SerieDto;

/**
 * Dao for series returned from a Piwik backend
 * 
 * @author <a href="mailto:ahernandez@emergya.com">ahernandez</a>
 * 
 */
public abstract class PiwikDao extends AbstractStatsBackend {

	protected static String SERVICE_URL;
	protected static String SITE_ID;
	protected static String AUTH_TOKEN;

	protected Properties appProperties;

	public PiwikDao() {
		try {
			appProperties = new Properties();
			appProperties.load(PiwikDao.class.getClassLoader()
					.getResourceAsStream("application.properties"));
			SERVICE_URL = appProperties.getProperty("backend.piwik.url");
			SITE_ID = appProperties.getProperty("backend.piwik.siteId");
			AUTH_TOKEN = appProperties.getProperty("backend.piwik.authToken");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Performs a query
	 * 
	 * @param params
	 * @return List<Map<String, Object>
	 */
	public List<Map<String, Object>> query(Map<String, String> params) {

		List<Map<String, Object>> results = new ArrayList<Map<String, Object>>();
		BufferedReader rd = null;
		StringBuilder sb = null;
		URL serviceURL = null;
		HttpURLConnection connection = null;

		try {

			// Always overwrite sensitive params
			params.put("module", "API");
			params.put("idSite", SITE_ID);
			params.put("token_auth", AUTH_TOKEN);

			String qstring = mapToString(params);
			String url = SERVICE_URL + "?" + qstring;
			System.out.println("URL: " + url);

			serviceURL = new URL(url);
			connection = (HttpURLConnection) serviceURL.openConnection();
			connection.setRequestMethod("GET");
			connection.setReadTimeout(10000);

			connection.connect();

			rd = new BufferedReader(new InputStreamReader(
					connection.getInputStream()));
			sb = new StringBuilder();

			String line;
			while ((line = rd.readLine()) != null) {
				sb.append(line);
			}

			results = parseJSON(sb.toString());

		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (ProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();

		} finally {

			connection.disconnect();
			connection = null;
			rd = null;
			sb = null;
		}

		return results;
	}

	/**
	 * Performs a query
	 * 
	 * @param query
	 * @return List<Map<String, Object>
	 */
	public List<Map<String, Object>> query(String query) {
		return new ArrayList<Map<String, Object>>();
	}

	public SerieDto getOrderedSerieFromMap(List<Map<String, Object>> listOfValues, String period, String serieName) {
	
		Map<String, Object> values = new HashMap<String, Object>();
		ListIterator<Map<String, Object>> listOfValuesIterator = listOfValues
				.listIterator();
	
		while (listOfValuesIterator.hasNext()) {
			Map<String, Object> module = listOfValuesIterator.next();
			if (module.get("label").toString().contains(getUrlPortion())) {
				String moduleName = module.get("label").toString()
						.replaceAll(".*" + getUrlPortion() + "/", "");
				moduleName = moduleName.replaceAll("&.*$", "").replaceAll("_",
						" ");
				Integer oldValue = (Integer) values.get(moduleName);
				if (oldValue != null) {
					Integer newOcurrences = (Integer) module
							.get(ISerieDao.NB_VISITS_KEY);
					newOcurrences += oldValue;
					values.put(moduleName, newOcurrences);
	
				} else {
					values.put(moduleName, module.get(ISerieDao.NB_VISITS_KEY));
				}
			}
		}
	
		SerieDto serie = new SerieDto();
		serie.setName(serieName);
		serie.setPeriod(period);
		serie.setValues(sortByValuesDesc(values));
	
		return serie;
	}

	protected abstract String getUrlPortion();

	public static String mapToString(Map<String, ?> params)
			throws UnsupportedEncodingException {

		StringBuilder qstring = new StringBuilder();

		for (String key : params.keySet()) {

			if (qstring.length() > 0) {
				qstring.append("&");
			}

			String value = (String) params.get(key);

			qstring.append(URLEncoder.encode(key, "UTF-8"));
			qstring.append("=");
			qstring.append(URLEncoder.encode(value, "UTF-8"));
		}

		return qstring.toString();
	}

	public static List<Map<String, Object>> parseJSON(String jsonString)
			throws IOException {

		List<Map<String, Object>> jsonMap = new ArrayList<Map<String, Object>>();
		ObjectMapper mapper = new ObjectMapper();

		try {
			HashMap<String, Object> map = mapper.readValue(jsonString,
					HashMap.class);
			jsonMap.add(map);
		} catch (JsonMappingException e) {
			jsonMap = mapper.readValue(jsonString,
					new TypeReference<List<HashMap>>() {
					});
		}

		return jsonMap;
	}

}
