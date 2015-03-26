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

package com.emergya.ohiggins.service;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.List;

import com.emergya.ohiggins.dto.LayerDto;

/**
 * SHP to postgis migration service
 * 
 * @author <a href="mailto:adiaz@emergya.com">adiaz</a>
 * 
 */
public interface SHP2PostgisService extends Serializable {
	
	/**
	 * Migrate all known SHP layers (located in gis_layer) to postgis and publish in geoserver
	 *   
	 * @return new postgis layers
	 */
	public List<LayerDto> duplicateAllSHPLayers();
	
	/**
	 * Delete SHP layers that has been migrated to postgis with {@link SHP2PostgisService#duplicateAllSHPLayers()}
	 *  
	 * @return final postgis layers 
	 */
	public List<LayerDto> deleteDuplicatedSHPLayers();
	
	/**
	 * Migrate all known SHP layer (located in gis_layer) to postgis and publish in geoserver
	 * 
	 * @param shpLayer
	 *   
	 * @return new postgis layer
	 */
	public LayerDto duplicateSHPLayers(LayerDto shpLayer)  throws Exception;
	
	/**
	 * Delete SHP layer that has been migrated to postgis with {@link SHP2PostgisService#duplicateSHPLayers(LayerDto)}
	 * 
	 * @param shpLayer
	 *  
	 * @return final postgis layer
	 */
	public LayerDto deleteDuplicatedSHPLayer(LayerDto shpLayer);

	/**
	 * Migrate a geoserver exportable to SHP layer to postgis
	 * 
	 * @param idLayer identifier of gis_layer to be migrated
	 * @param typeName workspace and type of the featureType separated by ':'
	 * 
	 * @return layer updated
	 * 
	 * @throws Exception
	 */
	public LayerDto migrateLayerToPostgis(Long idLayer, String typeName) throws Exception;
	
	/**
	 * Migrate a geoserver exportable to SHP layer to postgis
	 * 
	 * @param idLayer identifier of gis_layer to be migrated
	 * @param typeName workspace and type of the featureType separated by ':'
	 * @param update flag to update or generate a new layer
	 * 
	 * @return layer updated
	 * 
	 * @throws Exception
	 */
	public LayerDto migrateLayerToPostgis(Long idLayer, String typeName, boolean update) throws Exception;
	
	/**
	 * Migrate a geoserver exportable to SHP layer to postgis
	 * 
	 * @param idLayer identifier of gis_layer to be migrated
	 * @param typeName workspace and type of the featureType separated by ':'
	 * @param update flag to update or generate a new layer
	 * @param connectGeoserver flag to try to create and delete layers in geoserver
	 * 
	 * @return layer updated
	 * 
	 * @throws Exception
	 */
	public LayerDto migrateLayerToPostgis(Long idLayer, String typeName, boolean update, boolean connectGeoserver) throws Exception;
	
	/**
	 * Migrate a geoserver exportable to SHP layer to postgis
	 * 
	 * @param idLayer identifier of gis_layer to be migrated
	 * @param typeName workspace and type of the featureType separated by ':'
	 * @param update flag to update or generate a new layer
	 * @param connectGeoserver flag to try to create and delete layers in geoserver
	 * @param local flag to try use local SHP instead remote download (needs to be executed in the same machine that target geoserver) 
	 * 
	 * @return layer updated
	 * 
	 * @throws Exception
	 */
	public LayerDto migrateLayerToPostgis(Long idLayer, String typeName, boolean update, boolean connectGeoserver, boolean local) throws Exception;
	
	/**
	 * Download the SHP file and upload in a new postgis layer with {@link SHP2PostgisService#uploadAndSaveShp(File, String)}.
	 * 
	 * @param typeName workspace and type of the featureType separated by ':'
	 * 
	 * @return name of the new table created
	 * 
	 * @throws IOException if SHP file cannot be found
	 */
	public String generateTableFromLayer(String typeName) throws Exception;
	

	
	/**
	 * Upload the SHP in a new postgis layer.
	 * 
	 * @param zipFile SHP file downloaded with {@link SHP2PostgisService#obtainExportUrl(String, String, String)} and {@link SHP2PostgisService#downloadFile(String, File)} methods
	 * @param layerTitle target layer title
	 * 
	 * @return name of the new table created
	 */
	public String uploadAndSaveShp(File zipFile, String layerTitle);
	
	/**
	 * Upload the SHP in a new postgis layer.
	 * 
	 * @param zipFile SHP file downloaded with {@link SHP2PostgisService#obtainExportUrl(String, String, String)} and {@link SHP2PostgisService#downloadFile(String, File)} methods
	 * @param layerTitle target layer title
	 * @param cleanSHP delete zipFile finally
	 * 
	 * @return name of the new table created
	 */
	public String uploadAndSaveShp(File zipFile, String layerTitle, boolean cleanSHP);

	/**
	 * Generate common SHP export url for a Geoserver instance
	 * 
	 * @param baseGeoserverUrl
	 * @param workspace
	 * @param typeName
	 * 
	 * @return url to download the layer identified by <code>workspace</code> and <code>typeName</code>
	 */
	public String obtainExportUrl(String baseGeoserverUrl, String workspace, String typeName);
	
	/**
	 * Copy url content downloadable content to a File.
	 * 
	 * @param url to be downloaded
	 * @param file to be written
	 * 
	 * @throws IOException if file cannot be written
	 */
	public void downloadFile(String url, File file) throws IOException;
	
	/**
	 * Remove a layer from geoserver, the table name if exists, the layer's style or another layer's resource
	 * 
	 * @param name of the layer
	 * 
	 * @return true if successfully remove or false otherwise  
	 */
	public boolean deleteLayer(String name);
	
	/**
	 * Remove a layer from geoserver, the table name if exists, the layer's style or another layer's resource
	 * 
	 * @param workspace of the layer
	 * @param datastore of the layer
	 * @param name of the layer
	 * 
	 * @return true if successfully remove or false otherwhise  
	 */
	public boolean deleteLayer(String workspace, String datastore, String name);
	
	/**
	 * Remove all unused layers and his styles
	 * 
	 * @return list of removed layers
	 */
	public List<String> cleanUnusedLayers(boolean justTest);
	
	/**
	 * Check layer is in use 
	 * 
	 * @param layerName
	 * 
	 * @return true if layer is in use or false otherwise
	 */
	public boolean layerInUse(String layerName);

}
