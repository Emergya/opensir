/*
 * V20130204_163600__Create_authorities_workspaces.java
 * 
 * Copyright (C) 2013
 * 
 * This file is part of ohiggins-core
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
 * Authors:: Juan Luis Rodríguez Ponce (mailto:jlrodriguez@emergya.com)
 */
package db.migration;

import java.io.IOException;
import java.io.InputStream;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCallback;

import com.emergya.ohiggins.model.AuthorityEntity;
import com.emergya.ohiggins.model.AuthorityEntity.Names;
import com.emergya.persistenceGeo.dao.impl.GeoserverGsManagerDaoImpl;
import com.emergya.persistenceGeo.service.impl.GeoserverServiceImpl;
import com.emergya.persistenceGeo.utils.GsRestApiConfigurationImpl;
import com.googlecode.flyway.core.api.FlywayException;
import com.googlecode.flyway.core.api.migration.spring.SpringJdbcMigration;

/**
 * @author <a href="mailto:jlrodriguez@emergya.com">jlrodriguez</a>
 * 
 */
public class V20130204_163600__Create_authorities_workspaces implements
		SpringJdbcMigration {
	private static final Log LOG = LogFactory
			.getLog(V20130204_163600__Create_authorities_workspaces.class);
	private GeoserverServiceImpl geoserverService;

	/**
	 * 
	 */
	public V20130204_163600__Create_authorities_workspaces() {
		if (LOG.isInfoEnabled()) {
			LOG.info("Creando migración V20130204_163600__Create_authorities_workspaces");
		}
		// Init goeserverService
		Properties p = new Properties();
		ClassLoader loader = Thread.currentThread().getContextClassLoader();
		InputStream stream = loader
				.getResourceAsStream("/geoserver.properties");
		try {
			p.load(stream);
		} catch (IOException e) {
			LOG.info(
					"Error creando migración. No se ha podido leer el archivo de proopiedades geoserver.properties",
					e);
			throw new FlywayException(
					"Error creando migración. No se ha podido leer el archivo de proopiedades geoserver.properties",
					e);
		}
		geoserverService = new GeoserverServiceImpl();
		GeoserverGsManagerDaoImpl dao = new GeoserverGsManagerDaoImpl();
		GsRestApiConfigurationImpl conf = new GsRestApiConfigurationImpl();
		conf.setAdminPassword(p.getProperty("app.proxy.geoserver.password"));
		conf.setAdminUsername(p.getProperty("app.proxy.geoserver.username"));
		conf.setDbHost(p.getProperty("geoserver.db.host"));
		conf.setDbName(p.getProperty("geoserver.db.name"));
		conf.setDbPassword(p.getProperty("geoserver.db.password"));
		conf.setDbPort(Integer.valueOf((String) p.get("geoserver.db.port")));
		conf.setDbSchema(p.getProperty("geoserver.db.schema"));
		conf.setDbUser(p.getProperty("geoserver.db.user"));
		conf.setServerUrl(p.getProperty("geoserver.rest.url"));
		conf.setDatasourceType(p.getProperty("geoserver.db.datasourceType"));
		conf.setDbType(p.getProperty("geoserver.db.type"));
		conf.setJndiReferenceName(p
				.getProperty("geoserver.db.jndiReferenceName"));

		dao.setGsConfiguration(conf);
		geoserverService.setGsDao(dao);
		geoserverService
				.setNamespaceBaseUrl("http://www.gorearicayparinacota.cl/");

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.googlecode.flyway.core.api.migration.spring.SpringJdbcMigration#migrate
	 * (org.springframework.jdbc.core.JdbcTemplate)
	 */
	@Override
	public void migrate(JdbcTemplate jdbcTemplate) throws Exception {

		jdbcTemplate.execute(
				"SELECT * FROM gis_authority where workspace_name is null",
				new PreparedStatementCallback<AuthorityEntity>() {

					@Override
					public AuthorityEntity doInPreparedStatement(
							PreparedStatement ps) throws SQLException,
							DataAccessException {
						ResultSet rs = ps.executeQuery();
						PreparedStatement s = ps
								.getConnection()
								.prepareStatement(
										"update gis_authority set workspace_name=? where id=?");
						while (rs.next()) {
							AuthorityEntity authority = new AuthorityEntity();
							authority.setId(rs.getLong(AuthorityEntity.Names.ID
									.toString()));
							authority.setName(rs.getString(Names.NAME
									.toString()));
							String workspaceName = generateWorkspaceName(authority
									.getName());

							if (!geoserverService
									.existsWorkspace(workspaceName)) {

								boolean creado = geoserverService
										.createGsWorkspaceWithDatastore(workspaceName);
								LOG.info("Creando workspace " + workspaceName
										+ " para la institución "
										+ authority.getName());
								if (creado) {
									s.setString(1, workspaceName);
									s.setLong(2, authority.getId());
									s.executeUpdate();

								}
							} else {
								LOG.info("Ya existe el workspace "
										+ workspaceName
										+ ". No se crea pero se asocia a la institución "
										+ authority.getName());
								s.setString(1, workspaceName);
								s.setLong(2, authority.getId());
								s.executeUpdate();

							}
						}
						return null;
					}

				});

	}

	public final String generateWorkspaceName(String name) {
		String workspaceName = name;
		workspaceName = StringUtils
				.replaceChars(workspaceName, ".- \t", "____");
		workspaceName = StringUtils.lowerCase(workspaceName);
		workspaceName = StringUtils.replaceChars(workspaceName,
				"áéíóúñàèìòùü''\"´ñ", "aeiounaeiouu____");
		if (StringUtils.isNumeric(name.substring(0, 1))) {
			workspaceName = "_" + workspaceName;
		}
		return workspaceName;
	}

}
