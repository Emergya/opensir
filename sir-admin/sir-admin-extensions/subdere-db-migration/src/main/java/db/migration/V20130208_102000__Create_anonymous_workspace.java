/*
 * V20130208_102000__Create_anonynous_workspace.java
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
import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.jdbc.core.JdbcTemplate;

import com.emergya.persistenceGeo.dao.impl.GeoserverGsManagerDaoImpl;
import com.emergya.persistenceGeo.service.impl.GeoserverServiceImpl;
import com.emergya.persistenceGeo.utils.GsRestApiConfigurationImpl;
import com.googlecode.flyway.core.api.FlywayException;
import com.googlecode.flyway.core.api.migration.spring.SpringJdbcMigration;

/**
 * @author <a href="mailto:jlrodriguez@emergya.com">jlrodriguez</a>
 * 
 */
public class V20130208_102000__Create_anonymous_workspace implements
		SpringJdbcMigration {
	private static final Log LOG = LogFactory
			.getLog(V20130208_102000__Create_anonymous_workspace.class);
	private GeoserverServiceImpl geoserverService;

	/**
	 * 
	 */
	public V20130208_102000__Create_anonymous_workspace() {
		if (LOG.isInfoEnabled()) {
			LOG.info("Creando migración V20130208_102000__Create_anonymous_workspace");
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
		geoserverService.setNamespaceBaseUrl("http://www.gorearicayparinacota.cl/");

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
		if (LOG.isInfoEnabled()) {
			LOG.info("Creando workspace anonimo \"anonymousWorkspace\"...");
		}
		
		String workspaceName = "anonymousWorkspace";
		boolean alreadyExists = geoserverService.existsWorkspace(workspaceName);
		if (!alreadyExists) {
			boolean creado = geoserverService
					.createGsWorkspaceWithDatastore(workspaceName);
			if (!creado) {
				throw new FlywayException(
						"No se ha podido crear el workspace anonymousWorkspace");
			} else {
				LOG.info("Workspace anónimo \"anonymousWorkspace\" creado");
			}			
		} else {
			LOG.info("El workspace anónimo ya existe en geoserver. No se vuelve a crear");
		}
		

	}

}
