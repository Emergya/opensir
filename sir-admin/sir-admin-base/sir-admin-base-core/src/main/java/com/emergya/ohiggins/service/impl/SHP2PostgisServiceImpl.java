/*
 * SHP2PostgisService.java
 * 
 * Copyright (C) 2013
 * 
 * This file is part of Proyecto sir-admin
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
 * Authors:: Alejandro Díaz Torres (mailto:adiaz@emergya.com)
 */
package com.emergya.ohiggins.service.impl;

import it.geosolutions.geoserver.rest.decoder.RESTDataStore;
import it.geosolutions.geoserver.rest.decoder.RESTDataStore.DBType;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.net.URLDecoder;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.io.FileUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.exception.SQLGrammarException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.emergya.ohiggins.dto.LayerDto;
import com.emergya.ohiggins.dto.LayerMetadataDto;
import com.emergya.ohiggins.dto.LayerPropertyDto;
import com.emergya.ohiggins.dto.LayerResourceDto;
import com.emergya.ohiggins.importers.ShpImporter;
import com.emergya.ohiggins.service.LayerPublishRequestService;
import com.emergya.ohiggins.service.LayerResourceService;
import com.emergya.ohiggins.service.LayerService;
import com.emergya.ohiggins.service.LayerTypeService;
import com.emergya.ohiggins.service.SHP2PostgisService;
import com.emergya.persistenceGeo.dao.DBManagementDao;
import com.emergya.persistenceGeo.exceptions.GeoserverException;
import com.emergya.persistenceGeo.importer.shp.IShpImporter;
import com.emergya.persistenceGeo.service.GeoserverService;
import com.emergya.persistenceGeo.utils.BoundingBox;
import com.emergya.persistenceGeo.utils.GeoserverUtils;
import com.emergya.persistenceGeo.utils.GeometryType;
import it.geosolutions.geoserver.rest.decoder.RESTLayer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.PatternMatchUtils;

/**
 * SHP to postgis migration service implementation
 * 
 * @author <a href="mailto:adiaz@emergya.com">adiaz</a>
 * 
 */
@Repository
public class SHP2PostgisServiceImpl implements SHP2PostgisService {

	
        private String[] layersExcludedFromClean;
	
	
	private String[] workspacesExcludedFromClean;
    
	/**
	 * 
	 */
	private static final long serialVersionUID = -8849653978147726857L;

	private static final Log LOG = LogFactory
			.getLog(SHP2PostgisServiceImpl.class);

	@Autowired
	private ShpImporter importer;
	@Autowired
	private IShpImporter commandLineImporter;
	@Autowired
	private GeoserverService geoserverService;
	@Autowired
	private LayerResourceService layerResourceService;
	@Autowired
	private DBManagementDao dbManagementDao;
	@Autowired
	private LayerTypeService layerTypeService;
	@Resource
	private LayerService layerService;
	@Resource
	private LayerPublishRequestService layerPublishRequest;

	/**
	 * Geoserver temporal url constant to be replaced in
	 * {@link SHP2PostgisServiceImpl#obtainExportUrl(String, String, String)}
	 */
	private static final String GEOSERVER = "${geoserver}";

	/**
	 * Workspace temporal url constant to be replaced in
	 * {@link SHP2PostgisServiceImpl#obtainExportUrl(String, String, String)}
	 */
	private static final String WORKSPACE = "${workspace}";

	/**
	 * Type name temporal url constant to be replaced in
	 * {@link SHP2PostgisServiceImpl#obtainExportUrl(String, String, String)}
	 */
	private static final String TYPE_NAME = "${typeName}";

	/**
	 * SHP url export constant to be used in
	 * {@link SHP2PostgisServiceImpl#obtainExportUrl(String, String, String)}
	 */
	private static final String DEFAULT_SHP_URL_DOWNLOAD = GEOSERVER + "/"
			+ WORKSPACE
			+ "/ows?service=WFS&version=1.0.0&request=GetFeature&typename="
			+ TYPE_NAME + "&outputformat=shape-zip";

	/**
	 * Metadata info constant
	 */
	private static final String DEFAULT_METADATA_INFO = "Cartografía base";

	/**
	 * Metadata responsable constant
	 */
	private static final String DEFAULT_METADATA_RESPONSABLE = "Administrador";

	private String defaultMetadataInfo;
	private String defaultMetadataResponsable;

	public String getDefaultMetadataInfo() {
		return defaultMetadataInfo != null ? defaultMetadataInfo
				: DEFAULT_METADATA_INFO;
	}

	public void setDefaultMetadataInfo(String defaultMetadataInfo) {
		this.defaultMetadataInfo = defaultMetadataInfo;
	}

	public String getDefaultMetadataResponsable() {
		return defaultMetadataResponsable != null ? defaultMetadataResponsable
				: DEFAULT_METADATA_RESPONSABLE;
	}

	public void setDefaultMetadataResponsable(String defaultMetadataResponsable) {
		this.defaultMetadataResponsable = defaultMetadataResponsable;
	}

	/**
	 * Migrate a geoserver exportable to SHP layer to postgis
	 * 
	 * @param idLayer
	 *            identifier of gis_layer to be migrated
	 * @param typeName
	 *            workspace and type of the featureType separated by ':'
	 * 
	 * @return layer updated
	 * 
	 * @throws Exception
	 */
	public LayerDto migrateLayerToPostgis(Long idLayer, String typeName)
			throws Exception {
		return migrateLayerToPostgis(idLayer, typeName, false);
	}

	/**
	 * Migrate a geoserver exportable to SHP layer to postgis
	 * 
	 * @param idLayer
	 *            identifier of gis_layer to be migrated
	 * @param typeName
	 *            workspace and type of the featureType separated by ':'
	 * @param update
	 *            flag to update or generate a new layer
	 * 
	 * @return layer updated
	 * 
	 * @throws Exception
	 */
	public LayerDto migrateLayerToPostgis(Long idLayer, String typeName,
			boolean update) throws Exception {
		return migrateLayerToPostgis(idLayer, typeName, false, false);
	}

	/**
	 * Migrate a geoserver exportable to SHP layer to postgis
	 * 
	 * @param idLayer
	 *            identifier of gis_layer to be migrated
	 * @param typeName
	 *            workspace and type of the featureType separated by ':'
	 * @param update
	 *            flag to update or generate a new layer
	 * @param connectGeoserver
	 *            flag to try to create and delete layers in geoserver. Default
	 *            is false
	 * 
	 * @return layer updated
	 * 
	 * @throws Exception
	 */
	public LayerDto migrateLayerToPostgis(Long idLayer, String typeName,
			boolean update, boolean connectGeoserver) throws Exception {
		return migrateLayerToPostgis(idLayer, typeName, false, false, false);
	}

	/**
	 * Migrate a geoserver exportable to SHP layer to postgis
	 * 
	 * @param idLayer
	 *            identifier of gis_layer to be migrated
	 * @param typeName
	 *            workspace and type of the featureType separated by ':'
	 * @param update
	 *            flag to update or generate a new layer
	 * @param connectGeoserver
	 *            flag to try to create and delete layers in geoserver
	 * @param local
	 *            flag to try use local SHP instead remote download (needs to be
	 *            executed in the same machine that target geoserver)
	 * 
	 * @return layer updated
	 * 
	 * @throws Exception
	 */
	@Transactional
	public LayerDto migrateLayerToPostgis(Long idLayer, String typeName,
			boolean update, boolean connectGeoserver, boolean local)
			throws Exception {
		String workspaceName = typeName.split(":")[0], layerName = typeName
				.split(":")[1], finalLayerName = layerName;

		// DataStore
		RESTDataStore datastore = geoserverService.getDatastore(layerName);
		// Generate new table
		String tableName = generateTableFromLayer(typeName, local, datastore);
		// Generate bbox
		BoundingBox bbox = dbManagementDao.getTableBoundingBox(tableName);
		// Generate GeometryType
		GeometryType type = dbManagementDao.getTableGeometryType(tableName);

		LayerDto layer = (LayerDto) layerService.getById(idLayer);

		// Update new properties
		String title = layer.getLayerLabel();
		layer.setLayerTitle(title);
		layer.setTableName(tableName);
		layer.setUpdateDate(new Date());
		layer.setMetadata(generateLayerMetadata());
		layer.setType(layerTypeService
				.getLayerTypeById(LayerTypeService.POSTGIS_VECTORIAL_ID));

		// LayerProperties
		List<LayerPropertyDto> layerProperties = new LinkedList<LayerPropertyDto>();
		layerProperties.add(new LayerPropertyDto("tiled", "true"));
		layerProperties.add(new LayerPropertyDto("buffer", "1"));
		layerProperties.add(new LayerPropertyDto("maxExtent", bbox.toString()));
		layerProperties.add(new LayerPropertyDto("layers", typeName));
		layerProperties.add(new LayerPropertyDto("opacity", "1"));
		layerProperties.add(new LayerPropertyDto("visibility", "true"));
		layerProperties.add(new LayerPropertyDto("format", "image/png"));
		layerProperties.add(new LayerPropertyDto("visibility", "transparent"));
		layer.setProperties(layerProperties);

		// Change name and rest id for a new layer
		if (!update) {
			layer.setId(null);
			layerName = tableName;
			layer.setName(layerName);
		}

		// Publish new dbLayer
		if (connectGeoserver) {
			geoserverService.publishGsDbLayer(workspaceName, tableName,
					layerName, title, bbox, type);
			geoserverService.copyLayerStyle(typeName, layerName);
			geoserverService.setLayerStyle(workspaceName, layerName, layerName);
		}

		// Update or create gis_layer
		if (update) {
			layer = (LayerDto) layerService.update(layer);
		} else {
			layer = (LayerDto) layerService.create(layer);
		}

		// Unpublish old layer
		if (update && connectGeoserver) {
			String finalDataStoreName = geoserverService
					.getDatastore(layerName).getName();
			// Unpublish SHP layer.
			geoserverService.unpublishLayer(workspaceName, datastore.getName(),
					finalLayerName);
			// Copy created temporal layer.
			geoserverService.copyLayer(workspaceName, finalDataStoreName,
					layerName, tableName, title, bbox, type, workspaceName,
					finalDataStoreName, finalLayerName);
			// delete temporal layer.
			geoserverService.unpublishLayer(workspaceName, finalDataStoreName,
					layerName);
		}

		return layer;
	}

	/**
	 * Download the SHP file and upload in a new postgis layer with
	 * {@link SHP2PostgisService#uploadAndSaveShp(File, String)}.
	 * 
	 * @param typeName
	 *            workspace and type of the featureType separated by ':'
	 * 
	 * @return name of the new table created
	 * 
	 * @throws IOException
	 *             if SHP file cannot be found
	 */
	public String generateTableFromLayer(String typeName) throws Exception {
		return generateTableFromLayer(typeName, true);
	}

	/**
	 * Download the SHP file and upload in a new postgis layer with
	 * {@link SHP2PostgisService#uploadAndSaveShp(File, String)}.
	 * 
	 * @param typeName
	 *            workspace and type of the featureType separated by ':'
	 * @param local
	 *            flag to try use local SHP instead remote download (needs to be
	 *            executed in the same machine that target geoserver)
	 * 
	 * @return name of the new table created
	 * 
	 * @throws IOException
	 *             if SHP file cannot be found
	 */
	public String generateTableFromLayer(String typeName, boolean local)
			throws Exception {
		return generateTableFromLayer(typeName, local,
				geoserverService.getDatastore(typeName.split(":")[1]));
	}

	/**
	 * Download the SHP file and upload in a new postgis layer with
	 * {@link SHP2PostgisService#uploadAndSaveShp(File, String)}.
	 * 
	 * @param typeName
	 *            workspace and type of the featureType separated by ':'
	 * @param local
	 *            flag to try use local SHP instead remote download (needs to be
	 *            executed in the same machine that target geoserver)
	 * @param datastore
	 *            of the layer
	 * 
	 * @return name of the new table created
	 * 
	 * @throws IOException
	 *             if SHP file cannot be found
	 */
	public String generateTableFromLayer(String typeName, boolean local,
			RESTDataStore datastore) throws Exception {
		String tableName = null;
		File file = null;

		if (local) {
			String layerName = typeName.split(":")[1];

			// Check if the layer to be exported has a datastore, is SHP type
			// and have a url to download the SHP
			String errorText = null;
			if (datastore == null) {
				errorText = "Layer '" + layerName + "' not found!!";
			} else if (datastore.getType() == DBType.POSTGIS) {
				errorText = "Try to migrate a not SHP layer '" + layerName
						+ "' (" + datastore.getType() + ")!!";
			} else if (datastore.getConnectionParameters() == null
					|| datastore.getConnectionParameters().isEmpty()
					|| datastore.getConnectionParameters().get("url") == null) {
				errorText = "Layer '" + layerName + "' SHP file not found!!";
			}

			if (errorText != null) {
				LOG.error(errorText);
				throw new GeoserverException(errorText);
			} else {
				URL fileUrl = new URL(datastore.getConnectionParameters().get(
						"url"));
				String fileDir = URLDecoder.decode(fileUrl.getFile(), "UTF-8")
						+ "/" + layerName + ".shp";
				file = new File(fileDir);
			}
			tableName = importShp(file, typeName.split(":")[1], false);
		} else {
			String url = obtainExportUrl(geoserverService.getGeoserverUrl(),
					typeName.split(":")[0], typeName);
			LOG.info("URL is '" + url + "'");
			file = com.emergya.persistenceGeo.utils.FileUtils.createFileTemp(
					"tmp", "zip");
			downloadFile(url, file);
			tableName = uploadAndSaveShp(file, typeName.split(":")[1], true);
		}

		return tableName;
	}

	/**
	 * Upload the SHP in a new postgis layer.
	 * 
	 * @param zipFile
	 *            SHP file downloaded with
	 *            {@link SHP2PostgisService#obtainExportUrl(String, String, String)}
	 *            and {@link SHP2PostgisService#downloadFile(String, File)}
	 *            methods
	 * @param layerTitle
	 *            target layer title
	 * 
	 * @return name of the new table created
	 */
	public String uploadAndSaveShp(File zipFile, String layerTitle) {
		return uploadAndSaveShp(zipFile, layerTitle, false);
	}

	/**
	 * Upload the SHP in a new postgis layer.
	 * 
	 * @param zipFile
	 *            SHP file downloaded with
	 *            {@link SHP2PostgisService#obtainExportUrl(String, String, String)}
	 *            and {@link SHP2PostgisService#downloadFile(String, File)}
	 *            methods
	 * @param layerTitle
	 *            target layer title
	 * @param cleanSHP
	 *            delete zipFile finally
	 * 
	 * @return name of the new table created
	 */
	public String uploadAndSaveShp(File zipFile, String layerTitle,
			boolean cleanSHP) {
		if (LOG.isInfoEnabled()) {
			LOG.info("Importando archivo SHP");
		}
		File shpFile = null;
		String layerName = null;

		try {

			shpFile = importer.unzipAndLookForShp(zipFile);

			layerName = importShp(shpFile, layerTitle, false);

		} catch (Exception e) {
			LOG.error("uploadAndSaveShp. Error capturado", e);
		} finally {
			if (cleanSHP)
				cleanupFiles(zipFile, shpFile);
		}

		return layerName;
	}

	/**
	 * Upload the SHP in a new postgis layer.
	 * 
	 * @param zipFile
	 *            SHP file downloaded with
	 *            {@link SHP2PostgisService#obtainExportUrl(String, String, String)}
	 *            and {@link SHP2PostgisService#downloadFile(String, File)}
	 *            methods
	 * @param layerTitle
	 *            target layer title
	 * @param cleanSHP
	 *            delete zipFile finally
	 * 
	 * @return name of the new table created
	 */
	private String importShp(File shpFile, String layerTitle, boolean cleanSHP) {
		if (LOG.isInfoEnabled()) {
			LOG.info("Importando archivo SHP");
		}
		String layerName = null;

		try {

			layerName = layerResourceService
					.generateNameNotYetUsed(GeoserverUtils
							.createName(layerTitle));

			// Table name is actually the layer name.
			boolean imported = commandLineImporter.importShpToDb(
					shpFile.getAbsolutePath(), layerName, false);
			if (!imported)
				return null;

		} catch (Exception e) {
			LOG.error("uploadAndSaveShp. Error capturado", e);
		} finally {
			if (cleanSHP)
				FileUtils.deleteQuietly(shpFile);
		}

		return layerName;
	}

	/**
	 * Generate common SHP export url for a Geoserver instance
	 * 
	 * @param baseGeoserverUrl
	 * @param workspace
	 * @param typeName
	 * 
	 * @return url to download the layer identified by <code>workspace</code>
	 *         and <code>typeName</code>
	 */
	public String obtainExportUrl(String baseGeoserverUrl, String workspace,
			String typeName) {
		String url = DEFAULT_SHP_URL_DOWNLOAD
				.replace(GEOSERVER, baseGeoserverUrl)
				.replace(WORKSPACE, workspace).replace(TYPE_NAME, typeName);

		return url;
	}

	/**
	 * Copy url content downloadable content to a File.
	 * 
	 * @param url
	 *            to be downloaded
	 * @param file
	 *            to be written
	 * 
	 * @throws IOException
	 *             if file cannot be written
	 */
	public void downloadFile(String url, File file) throws IOException {
		FileOutputStream fos = null;
		try {
			URL website = new URL(url);
			ReadableByteChannel rbc = Channels.newChannel(website.openStream());
			fos = new FileOutputStream(file);
			fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
		} catch (IOException e) {
			throw e;
		} finally {
			fos.close();
		}
	}

	/**
	 * Generate a LayerMetadataDto to initialize layer metadata
	 * 
	 * @return
	 */
	private LayerMetadataDto generateLayerMetadata() {
		LayerMetadataDto dto = null;
		dto = new LayerMetadataDto();
		dto.setInformacion(getDefaultMetadataInfo());
		dto.setResponsable(getDefaultMetadataResponsable());
		return dto;
	}

	/**
	 * Borra el archivo <code>zipFile</code> y el directorio en el que está
	 * contenido <code>shpFile</code>
	 * 
	 * @param zipFile
	 * @param shpFile
	 */
	private void cleanupFiles(File zipFile, File shpFile) {
		FileUtils.deleteQuietly(zipFile);
		if (shpFile != null) {
			FileUtils.deleteQuietly(shpFile.getParentFile());
		}
	}

	/**
	 * Migrate all known SHP layers (located in gis_layer) to postgis and
	 * publish in geoserver
	 * 
	 * @return new postgis layers
	 */
	public List<LayerDto> duplicateAllSHPLayers() {
		List<LayerDto> newLayers = new LinkedList<LayerDto>();

		for (LayerDto shpLayer : layerService
				.getLayersByLayerType(LayerTypeService.WMS_VECTORIAL_ID)) {
			try {
				LayerDto migratedLayer = duplicateSHPLayers(shpLayer);
				LOG.debug("Layer migrated to " + migratedLayer.getId());
				newLayers.add(migratedLayer);
			} catch (Exception e) {
				LOG.error("Error in layer's " + shpLayer.getId() + " migration");
			}
		}
		return newLayers;
	}

	/**
	 * Delete SHP layers that has been migrated to postgis with
	 * {@link SHP2PostgisService#duplicateAllSHPLayers()}
	 * 
	 * @return final postgis layers
	 */
	public List<LayerDto> deleteDuplicatedSHPLayers() {
		List<LayerDto> newLayers = new LinkedList<LayerDto>();

		for (LayerDto shpLayer : layerService
				.getLayersByLayerType(LayerTypeService.WMS_VECTORIAL_ID)) {
			try {
				newLayers.add(deleteDuplicatedSHPLayer(shpLayer));
			} catch (Exception e) {
				LOG.error("Error in layer's " + shpLayer.getId() + " migration");
			}
		}
		return newLayers;
	}

	/**
	 * Migrate all known SHP layer (located in gis_layer) to postgis and publish
	 * in geoserver
	 * 
	 * @param shpLayer
	 * 
	 * @return new postgis layer
	 * @throws Exception
	 */
	@Transactional
	public LayerDto duplicateSHPLayers(LayerDto shpLayer) throws Exception {
		LayerDto migratedlayer = null;
		List<LayerDto> migratedLayers = layerService
				.getLayersByLayerTypeAndLayerName(
						LayerTypeService.POSTGIS_VECTORIAL_ID,
						shpLayer.getName());
		if (migratedLayers == null || migratedLayers.isEmpty()) {
			migratedlayer = migrateLayerToPostgis(shpLayer.getId(),
					shpLayer.getName(), false, true, true);
		} else {
			migratedlayer = migratedLayers.get(0);
		}
		return migratedlayer;
	}

	/**
	 * Delete SHP layer that has been migrated to postgis with
	 * {@link SHP2PostgisService#duplicateSHPLayers(LayerDto)}
	 * 
	 * @param shpLayer
	 * 
	 * @return final postgis layer
	 */
	@Transactional
	public LayerDto deleteDuplicatedSHPLayer(LayerDto shpLayer) {
		LayerDto migratedLayer = null;
		String iLayerName = shpLayer.getName().indexOf(":") > -1 ? shpLayer
				.getName().split(":")[1] : shpLayer.getName();
		List<LayerDto> migratedLayers = layerService
				.getLayersByLayerTypeAndLayerName(
						LayerTypeService.POSTGIS_VECTORIAL_ID, iLayerName);
		if (migratedLayers != null && !migratedLayers.isEmpty()) {
			String oldWorkspaceName = shpLayer.getName().split(":")[0];
			String oldLayerName = shpLayer.getName().split(":")[1];
			boolean renamed = false;
			for (LayerDto migratedlayer : migratedLayers) {
				if (!renamed) {
					// Obtain final datastore name
					String finalDataStoreName = geoserverService.getDatastore(
							migratedlayer.getName()).getName();
					// Generate bbox
					BoundingBox bbox = dbManagementDao
							.getTableBoundingBox(migratedlayer.getTableName());
					// Generate GeometryType
					GeometryType type = dbManagementDao
							.getTableGeometryType(migratedlayer.getTableName());
					try{
						// Obtain old datastore name
						String oldDataStoreName = geoserverService.getDatastore(
								shpLayer.getName()).getName();
						// unpublish old SHP layer
						geoserverService.unpublishLayer(oldWorkspaceName,
								oldDataStoreName, oldLayerName);
					}catch (Exception e){
						LOG.error("Layer '"+oldLayerName +"' already deleted!");
					}
					// copy the table with all data include directly in PostGis
					dbManagementDao.duplicateLayerTable(migratedlayer.getTableName(), oldLayerName);
					// Copy temporal layer to old layerName
					geoserverService.copyLayer(oldWorkspaceName,
							finalDataStoreName,
							migratedlayer.getNameWithoutWorkspace(),
							oldLayerName,
							migratedlayer.getLayerTitle(), bbox, type,
							oldWorkspaceName, finalDataStoreName, oldLayerName);
					// delete temporal layer.
					geoserverService.unpublishLayer(oldWorkspaceName,
							finalDataStoreName,
							migratedlayer.getNameWithoutWorkspace());
					renamed = true;
				}
				// Change layer name in gis_layer
				migratedlayer.setName(oldWorkspaceName + ":" + oldLayerName);
				migratedLayer = (LayerDto) layerService
						.simpleUpdate(migratedlayer);
				break;
			}
			// delete old layer
			layerService.delete(shpLayer);
		}
		return migratedLayer;
	}
	
	/**
	 * Remove a layer from geoserver, the table name if exists, the layer's style or another layer's resource
	 * 
	 * @param layerName of the layer
	 * 
	 * @return true if successfully remove or false otherwise  
	 */
	public boolean deleteLayer(String layerName){

	    
	    
	    // We delete temporal layers from the database.
	    LayerResourceDto tmpLayer = layerResourceService.getByTmpLayerName(layerName);
	    if(tmpLayer !=null) {
		try {
		    layerResourceService.delete(tmpLayer);
		} catch(RuntimeException e) {
		   LOG.error("Error cleaning layerResource of layerName '" + layerName + "'",e);
		}
	    }
	    
	    RESTLayer layer = geoserverService.getLayerInfo(layerName);

	    switch(layer.getType()) {
		case  VECTOR:
		    return deletePostGisLayer(layerName);
		case RASTER:
		    return deleteRasterLayer(layerName);
		default: 
		    return false;
	    }
	}
	
	private boolean deletePostGisLayer(String layerName) {
		RESTDataStore ds = geoserverService.getDatastore(layerName);
		String datastore = ds.getName();
		String workspace = ds.getWorkspaceName();
		
		return deleteLayer(workspace, datastore, layerName);
	}
	
	/**
	 * Remove a layer from geoserver, the table name if exists, the layer's style or another layer's resource
	 * 
	 * @param workspace of the layer
	 * @param datastore of the layer
	 * @param name of the layer
	 * 
	 * @return true if successfully remove or false otherwise  
	 */
	@Override
	public boolean deleteLayer(String workspace, String datastore, String name){

		boolean success = false;
		// We need to remove the data table backing the layer.
		try {
			String tableName = geoserverService.getNativeName(name);
			dbManagementDao.deleteLayerTable(tableName);
		} catch (SQLGrammarException ge) {
			// This launches if the table was already deleted, not an actual
			// grammar error.
		}
		
		// Remove geoserver layer and styles
		success = geoserverService.unpublishLayer(workspace, datastore, name);
		removeStyles(name);
		
		return success;
	}
	
	private boolean deleteRasterLayer(String layerName) {
	    
	    String workspaceName = geoserverService.getLayerWorkspace(layerName);	    
	    return geoserverService.unpublishGsCoverageLayer(workspaceName, layerName);
	}
	
	/**
	 * Remove unused styles in a layer
	 * 
	 * @param layerName
	 */
	private void removeStyles(String layerName){
		List<String> toRemove = new LinkedList<String>();
		for(String style : geoserverService.getStyleNames()){
			if(style.contains(layerName)){
				toRemove.add(style);
			}
		}
		geoserverService.cleanUnusedStyles(toRemove);
	}
	
	/**
	 * Remove all unused layers and his styles
	 * 
	 * @return list of removed layers
	 */
	@Override
	public List<String> cleanUnusedLayers(boolean justTest){
		List<String> removed = new LinkedList<String>();
		for(String layerName: geoserverService.getLayersNames()){
			try{
				if(!isLayerExcluded(layerName) 
					&&!layerInUse(layerName)				
					&& (justTest || deleteLayer(layerName))){
					removed.add(layerName);
				}	
			}catch (Exception e){
				LOG.error("Error removing layer '"+layerName + "'", e);
			}
		}
		
		return removed;
	}
	
	/**
	 * Check layer is in use, that is, is a published layer, or an authority's
	 * one, or is a publication request.
	 * 
	 * @param layerName
	 * 
	 * @return true if layer is in use or false otherwise
	 */
	public boolean layerInUse(String layerName){
		boolean inUse = false;
		// We want to keep authorities' layers, public ones (in gis_layer)
		// and publication requests (in gis_layer_resource)
		// Anything else can go.
		if(layerService.getLayerCountByName(layerName) > 0 
		    || layerPublishRequest.sourceNameRequested(layerName) > 0) {
			inUse = true;
		}
		
		return inUse;
	}
	
	private boolean isLayerExcluded(String layerName) {
	    
	    if(layersExcludedFromClean!=null
		    && PatternMatchUtils.simpleMatch(layersExcludedFromClean, layerName)) { 
		return true;
	    }
	
	    if(workspacesExcludedFromClean != null) {
		String workspaceName = geoserverService.getLayerWorkspace(layerName);
		
		if(PatternMatchUtils.simpleMatch(workspacesExcludedFromClean, workspaceName)) {
		    return true;
		}
	    }
	    
	    return false;
	}

    /**
     * @return the layersExcludedFromClean
     */
    public String[] getLayersExcludedFromClean() {
	return layersExcludedFromClean;
    }

    /**
     * @param layersExcludedFromClean the layersExcludedFromClean to set
     */
    @Value("#{webProperties['layersCleaning.excludedLayers']}")
    public void setLayersExcludedFromClean(String[] layersExcludedFromClean) {
	this.layersExcludedFromClean = layersExcludedFromClean;
    }

    /**
     * @return the workspacesExcludedFromClean
     */
    public String[] getWorkspacesExcludedFromClean() {
	return workspacesExcludedFromClean;
    }

    /**
     * @param workspacesExcludedFromClean the workspacesExcludedFromClean to set
     */
    @Value("#{webProperties['layersCleaning.excludedWorkspaces']}")
    public void setWorkspacesExcludedFromClean(String[] workspacesExcludedFromClean) {
	this.workspacesExcludedFromClean = workspacesExcludedFromClean;
    }

    
}
