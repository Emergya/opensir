/*
 * RestLayerManagmentController.java
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
package com.emergya.persistenceGeo.web;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.emergya.ohiggins.dto.LayerDto;
import com.emergya.ohiggins.service.LayerService;
import com.emergya.ohiggins.service.SHP2PostgisService;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Rest controller manage layers' incoherences
 * 
 * @author <a href="mailto:adiaz@emergya.com">adiaz</a>
 */
@Controller
public class RestLayerManagmentController extends RestPersistenceGeoController
		implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3899910377468028274L;

	/**
	 * Controller logger
	 */
	private static final Log LOG = LogFactory
			.getLog(RestLayerManagmentController.class);

	/**
	 * SHP2Postgis service
	 */
	@Autowired
	private SHP2PostgisService migrateSHP;

	/**
	 * Layer service
	 */
	@Resource
	private LayerService layerService;

	private List<LayerDto> lastMigration = null;

	/**
	 * Migrate all known SHP layers (located in gis_layer) to postgis and
	 * publish in geoserver with
	 * {@link SHP2PostgisService#duplicateAllSHPLayers()}
	 * 
	 * @return new postgis layers or 'not correct login' message if user logged
	 *         is not admin
	 */
	@RequestMapping(value = "/persistenceGeo/duplicateAllSHPLayers", produces = { MediaType.APPLICATION_JSON_VALUE })
	public @ResponseBody
	Map<String, Object> duplicateAllSHPLayers() {
		Map<String, Object> result = new HashMap<String, Object>();
		if (isAdmin(Boolean.TRUE)) {
			List<? extends Serializable> list = null;
			try {
				if (LOG.isInfoEnabled()) {
					LOG.info("Migrating SHP layers to PostGIS ...");
				}
				lastMigration = migrateSHP.duplicateAllSHPLayers();
				list = lastMigration;
				if (list != null && list.size() > 0) {
					LOG.info(list.size()
							+ " layers has been migrated to PostGIS");
				} else {
					LOG.info("Cannot migrate any layer");
				}
				result.put(RESULTS, list != null ? list.size() : 0);
				result.put(ROOT, list);
				result.put(SUCCESS, true);
			} catch (Exception e) {
				LOG.error("Cannot migrate layers!!");
				result.put(RESULTS, 1);
				result.put(ROOT, e);
				result.put(SUCCESS, false);
			}
		} else {
			result.put(RESULTS, 1);
			result.put(ROOT, "Not correct login");
			result.put(SUCCESS, false);
		}
		return result;
	}

	/**
	 * Remove layers in lastMigration
	 * 
	 * @return message with operation's result
	 */
	@RequestMapping(value = "/persistenceGeo/shp2postgis/rollback", produces = { MediaType.APPLICATION_JSON_VALUE })
	public @ResponseBody
	Map<String, Object> revertMigration() {
		Map<String, Object> result = new HashMap<String, Object>();
		if (isAdmin(Boolean.TRUE)) {
			try {
				String message = "No layers removed";
				if (LOG.isInfoEnabled()) {
					LOG.info("Rollback last migration ...");
				}
				if (lastMigration != null) {
					int i = 0;
					message = "Removed [";
					for (LayerDto layerToRemove : lastMigration) {
						layerService.delete(layerToRemove);
						message += layerToRemove.getId().toString()
								+ ((++i) == lastMigration.size() ? "]" : ",");
					}
					message += " layers in gis_layer";
				}
				result.put(RESULTS, 1);
				result.put(ROOT, message);
				result.put(SUCCESS, true);
			} catch (Exception e) {
				LOG.error("Error in rollback!!");
				result.put(RESULTS, 1);
				result.put(ROOT, e);
				result.put(SUCCESS, false);
			}
		} else {
			result.put(RESULTS, 1);
			result.put(ROOT, "Not correct login");
			result.put(SUCCESS, false);
		}
		return result;
	}

	/**
	 * Delete SHP layers that has been migrated to postgis with
	 * {@link SHP2PostgisService#deleteDuplicatedSHPLayers()}
	 * 
	 * @return final postgis layers or 'not correct login' message if user
	 *         logged is not admin
	 */
	@RequestMapping(value = "/persistenceGeo/deleteDuplicatedSHPLayers", produces = { MediaType.APPLICATION_JSON_VALUE })
	public @ResponseBody
	Map<String, Object> deleteDuplicatedSHPLayers() {
		Map<String, Object> result = new HashMap<String, Object>();
		if (isAdmin(Boolean.TRUE)) {
			List<? extends Serializable> list = null;
			try {
				if (LOG.isInfoEnabled()) {
					LOG.info("Migrating SHP layers to PostGIS ...");
				}
				list = migrateSHP.deleteDuplicatedSHPLayers();
				lastMigration = null;
				if (list != null && list.size() > 0) {
					LOG.info(list.size()
							+ " layers has been migrated to PostGIS");
				} else {
					LOG.info("Cannot migrate any layer");
				}
				result.put(RESULTS, list != null ? list.size() : 0);
				result.put(ROOT, list);
				result.put(SUCCESS, true);
			} catch (Exception e) {
				LOG.error("Cannot migrate layers!!");
				result.put(RESULTS, 1);
				result.put(ROOT, e);
				result.put(SUCCESS, false);
			}
		} else {
			result.put(RESULTS, 1);
			result.put(ROOT, "Not correct login");
			result.put(SUCCESS, false);
		}
		return result;
	}

	/**
	 * Method to clean all temporal files unused
	 * 
	 * @return resume of files cleaned
	 */
	@RequestMapping(value = "/persistenceGeo/cache/clean", produces = { MediaType.APPLICATION_JSON_VALUE })
	public @ResponseBody
	Map<String, Object> cleanCache(
		@RequestParam(value="doDeletion",defaultValue = "false") boolean doDeletion) {
		Map<String, Object> result = new HashMap<String, Object>();
		
		
		
		if (isAdmin(Boolean.TRUE)) {
			try {
				String message = "Nothing to clean";
				if (LOG.isInfoEnabled()) {
					LOG.info("Cleaning temporal files ...");
				}
				List<String> deletedLayers = migrateSHP.cleanUnusedLayers(!doDeletion);
				result.put(
						RESULTS,
						deletedLayers != null && deletedLayers.size() > 0 ? deletedLayers
								.size() : 1);
				result.put(ROOT, deletedLayers != null
						&& deletedLayers.size() > 0 ? deletedLayers : message);
				result.put(SUCCESS, true);
			} catch (Exception e) {
				LOG.error("Error in file cleaning!!");
				result.put(RESULTS, 1);
				result.put(ROOT, e);
				result.put(SUCCESS, false);
			}
		} else {
			result.put(RESULTS, 1);
			result.put(ROOT, "Not correct login");
			result.put(SUCCESS, false);
		}
		return result;
	}

	/**
	 * Method to clean all temporal files unused
	 * 
	 * @return resume of files cleaned
	 */
	@RequestMapping(value = "/persistenceGeo/cache/cleanLayer/{layerName}", produces = { MediaType.APPLICATION_JSON_VALUE })
	public @ResponseBody
	Map<String, Object> cleanLayer(@PathVariable String layerName) {
		Map<String, Object> result = new HashMap<String, Object>();
		if (isAdmin(Boolean.TRUE)) {
			try {
				String message = "Cannot remove layer '" + layerName + "'!!";
				if (LOG.isInfoEnabled()) {
					LOG.info("Cleaning layer '" + layerName + "' ...");
				}
				if (!migrateSHP.layerInUse(layerName)
						&& migrateSHP.deleteLayer(layerName)) {
					message = "Layer '" + layerName + "' removed";
				}
				result.put(RESULTS, 1);
				result.put(ROOT, message);
				result.put(SUCCESS, true);
			} catch (Exception e) {
				LOG.error("Error in layer cleaning!!");
				result.put(RESULTS, 1);
				result.put(ROOT, e);
				result.put(SUCCESS, false);
			}
		} else {
			result.put(RESULTS, 1);
			result.put(ROOT, "Not correct login");
			result.put(SUCCESS, false);
		}
		return result;
	}
}