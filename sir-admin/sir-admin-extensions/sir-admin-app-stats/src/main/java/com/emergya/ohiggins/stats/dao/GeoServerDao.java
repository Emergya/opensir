/*
 * GeoServerDao.java
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

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.emergya.persistenceGeo.dao.GeoserverDao;

/**
 * Dao for series returned from a GeoServer backend
 * 
 * @author <a href="mailto:ahernandez@emergya.com">ahernandez</a>
 * 
 */
public abstract class GeoServerDao extends AbstractStatsBackend {

	private static final Log LOG = LogFactory.getLog(GeoServerDao.class);

	protected static String JDBC_DRIVER;
	protected static String JDBC_URL;
	protected static String JDBC_USER;
	protected static String JDBC_PASSWD;

	protected Properties appProperties;
	protected Connection connection;

	public GeoServerDao() {
		try {

			appProperties = new Properties();

			if (LOG.isInfoEnabled()) {
				URL propertiesPath = GeoServerDao.class
						.getResource("/application.properties");
				LOG.info("Loading Stats module configuration from "
						+ propertiesPath.toString());
			}

			appProperties.load(GeoServerDao.class
					.getResourceAsStream("/application.properties"));
			JDBC_DRIVER = appProperties
					.getProperty("backend.geoserver.jdbc.driverClass");
			JDBC_URL = appProperties.getProperty("backend.geoserver.jdbc.url");
			JDBC_USER = appProperties
					.getProperty("backend.geoserver.jdbc.user");
			JDBC_PASSWD = appProperties
					.getProperty("backend.geoserver.jdbc.password");

			Class.forName(JDBC_DRIVER);
			connection = DriverManager.getConnection(JDBC_URL, JDBC_USER,
					JDBC_PASSWD);

		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Performs a query
	 * 
	 * @param query
	 * @return List<Map<String, Object>
	 */
	public List<Map<String, Object>> query(String query) {

		List<Map<String, Object>> results = new ArrayList<Map<String, Object>>();
		Statement st;
		ResultSet rs;

		try {

			st = connection.createStatement();
			rs = st.executeQuery(query);
			ResultSetMetaData md = rs.getMetaData();
			int cols = md.getColumnCount();

			while (rs.next()) {
				HashMap<String, Object> row = new HashMap<String, Object>();
				for (int i = 1; i <= cols; i++) {
					row.put(md.getColumnName(i), rs.getObject(i));
				}
				results.add(row);
			}

			rs.close();
			st.close();

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return results;
	}

	/**
	 * Performs a query
	 * 
	 * @param params
	 * @return List<Map<String, Object>
	 */
	public List<Map<String, Object>> query(Map<String, String> params) {
		// TODO: Format the SQL query and call query(String) method
		return new ArrayList<Map<String, Object>>();
	}
}
