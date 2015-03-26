/* ShpImporter.java
 * 
 * Copyright (C) 2012
 * 
 * This file is part of project ohiggins-core
 * 
 * This software is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 * 
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301  USA
 *
 * As a special exception, if you link this library with other files to
 * produce an executable, this library does not by itself cause the
 * resulting executable to be covered by the GNU General Public License.
 * This exception does not however invalidate any other reasons why the
 * executable file might be covered by the GNU General Public License.
 * 
 * Authors:: Juan Luis Rodríguez Ponce (mailto:jlrodriguez@emergya.com)
 */
package com.emergya.ohiggins.importers;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.geotools.data.DataStore;
import org.geotools.data.DataStoreFinder;
import org.geotools.data.DefaultTransaction;
import org.geotools.data.FeatureWriter;
import org.geotools.data.shapefile.ShapefileDataStore;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureIterator;
import org.geotools.data.simple.SimpleFeatureSource;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.emergya.ohiggins.dto.FileColumnDto;
import com.emergya.persistenceGeo.exceptions.ShpImporterException;
import com.emergya.persistenceGeo.utils.FileUtils;

/**
 * @author <a href="mailto:jlrodriguez@emergya.com">jlrodriguez</a>
 * 
 */
@Component
public class ShpImporter {

	private static final String INVESTMENT_INITIATIVE_UPDATE_ID = "investment_initiative_update_id";
	private static final String SHP_DATABASE_CONFIGURATION = "shpDatabaseConfiguration";
	private static final Log LOG = LogFactory.getLog(ShpImporter.class);
	@Autowired(required = false)
	@Qualifier(SHP_DATABASE_CONFIGURATION)
	private Map<String, Object> shpDatabaseProperties;

	@SuppressWarnings("rawtypes")
	public void importShapeToPosgis(String tableName,
			List<FileColumnDto> columnsModel, File shpFile, Long idInversion) {
		if (shpDatabaseProperties == null) {
			throw new UnsupportedOperationException(
					"shpDatabaseProperties bean not found. Please inject it to "
							+ this.getClass().getName());
		}

		ShapefileDataStore shpStore = null;
		DataStore postgisDs = null;
		try {
			shpStore = new ShapefileDataStore(shpFile.toURI().toURL());
			String shpTypeName = shpStore.getTypeNames()[0];
			SimpleFeatureSource shpSource = shpStore
					.getFeatureSource(shpTypeName);

			DefaultTransaction transaction = new DefaultTransaction();

			postgisDs = DataStoreFinder
					.getDataStore((Map) shpDatabaseProperties
							.get(SHP_DATABASE_CONFIGURATION));

			FeatureWriter<SimpleFeatureType, SimpleFeature> fwriter = postgisDs
					.getFeatureWriterAppend(tableName, transaction);

			SimpleFeatureCollection shpFeatureColection = shpSource
					.getFeatures();
			SimpleFeatureIterator shpFeaturesIterator = null;
			try {
				shpFeaturesIterator = shpFeatureColection.features();

				while (shpFeaturesIterator.hasNext()) {
					SimpleFeature shpFeature = shpFeaturesIterator.next();
					SimpleFeature pgFeature = fwriter.next();
					for (FileColumnDto columnDefinition : columnsModel) {
						pgFeature.setAttribute(columnDefinition.getDbName(),
								shpFeature.getAttribute(columnDefinition
										.getName()));
					}
					pgFeature.setDefaultGeometry(shpFeature
							.getDefaultGeometry());
					pgFeature.setAttribute(INVESTMENT_INITIATIVE_UPDATE_ID,
							idInversion);
					fwriter.write();
				}
				transaction.commit();
			} finally {
				transaction.close();
				fwriter.close();
				shpFeaturesIterator.close();
			}

		} catch (MalformedURLException e) {
			throw new ShpImporterException("Error con los ficheros subidos", e);
		} catch (FileNotFoundException fnfe) {
			throw new ShpImporterException("Falta algún fichero del shp", fnfe);
		} catch (IOException e) {
			throw new ShpImporterException("Falta algún fichero del shp", e);
		} finally {
			if (shpStore != null) {
				shpStore.dispose();
			}
			if (postgisDs != null) {
				postgisDs.dispose();
			}
		}
	}

	/**
	 * Unzip tempFile and looks for an shp file. If it found it, updates shpFile
	 * reference with it.
	 * 
	 * @param zipFile
	 *            zip file
	 * @param shpFile
	 * @return something not null if a problem is found.
	 */
	public File unzipAndLookForShp(File zipFile) {
		File zipDir;
		File shpFile = null;
		try {
			zipDir = FileUtils.unzipToTempDir(zipFile);
			LOG.info("Temp dir: " + zipDir.getAbsolutePath());
			shpFile = FileUtils.getUniqueFileWithExtensionInDir(zipDir, "shp");
		} catch (IOException e) {
			LOG.error("Error procesando archivo " + zipFile.getAbsolutePath(),
					e);
		}
		return shpFile;
	}

}
