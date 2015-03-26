/*
 * KMLWizardController.java
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
 * Authors:: Antonio José Rodríguez (mailto:ajrodriguez@emergya.com)
 */
package com.emergya.ohiggins.web;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.multipart.MultipartFile;

import com.emergya.ohiggins.dao.ImportGeoJdbDao;
import com.emergya.ohiggins.dao.OhigginsLayerResourceDao;
import com.emergya.ohiggins.dto.AuthorityDto;
import com.emergya.ohiggins.dto.LayerResourceDto;
import com.emergya.ohiggins.dto.LayerTypeDto;
import com.emergya.ohiggins.dto.UsuarioDto;
import com.emergya.ohiggins.security.ConstantesPermisos;
import com.emergya.ohiggins.security.OhigginsUserDetails;
import com.emergya.ohiggins.security.SecurityUtils;
import com.emergya.ohiggins.service.InstitucionService;
import com.emergya.ohiggins.service.LayerResourceService;
import com.emergya.ohiggins.service.LayerTypeService;
import com.emergya.ohiggins.service.UserAdminService;
import com.emergya.ohiggins.utils.ColumnGeo;
import com.emergya.ohiggins.web.util.FileUploadForm;
import com.emergya.persistenceGeo.dao.DBManagementDao;
import com.emergya.persistenceGeo.service.GeoserverService;
import com.emergya.persistenceGeo.utils.BoundingBox;
import com.emergya.persistenceGeo.utils.GeoserverUtils;
import com.emergya.persistenceGeo.utils.GeometryType;
import com.emergya.persistenceGeo.utils.GsRestApiConfiguration;
import com.google.common.collect.Maps;

import de.micromata.opengis.kml.v_2_2_0.Document;
import de.micromata.opengis.kml.v_2_2_0.Feature;
import de.micromata.opengis.kml.v_2_2_0.Folder;
import de.micromata.opengis.kml.v_2_2_0.Geometry;
import de.micromata.opengis.kml.v_2_2_0.Kml;
import de.micromata.opengis.kml.v_2_2_0.MultiGeometry;
import de.micromata.opengis.kml.v_2_2_0.Placemark;
import de.micromata.opengis.kml.v_2_2_0.Point;

/**
 * Controller del proceso de subida e importación de KMLs
 * 
 * @author ajrodriguez
 * 
 */
@Controller
public class KMLWizardController {

	private final static Log LOG = LogFactory.getLog(KMLWizardController.class);
	private final static String RESULTS = "results";
	private final static String ROOT = "data";
	private final static String SUCCESS = "success";

	@Autowired
	private UserAdminService userService;

	@Autowired
	private InstitucionService institucionService;

	@Autowired
	private GeoserverService geoserverService;

	@Autowired
	private LayerResourceService layerResourceService;
	
	@Autowired
	private DBManagementDao layerManagementDao;

	@Autowired
	private ImportGeoJdbDao importGeoJdbDao;

	@Autowired
	private OhigginsLayerResourceDao layerResourceDao;

	@Autowired
	private GsRestApiConfiguration gsConfiguration;

	@Autowired
	private LayerTypeService layerTypeService;

	@Autowired
	private UserAdminService userAdminService;

	@RequestMapping(value = "/uploadKml/step1", method = RequestMethod.POST)
	public @ResponseBody
	String uploadAndSaveKml(
			@ModelAttribute("uploadForm") FileUploadForm uploadForm,
			Model map, BindingResult result, WebRequest webRequest) {
		if (LOG.isInfoEnabled()) {
			LOG.info("Importando archivo KML");
		}
		LayerResourceDto layerResourceDto = new LayerResourceDto();
		File kmlFile = null;
		String userName = null;
		String srid = "4326";
		String coordX = "coordX";
		String coordY = "coordY";

		rolUser(map);

		OhigginsUserDetails usuario = (OhigginsUserDetails) SecurityUtils
				.getPrincipal();
		if (usuario != null) {
			userName = usuario.getUsername();
		}
		String workspaceName = userService.getWorkspaceName(userName);
		if (userName != null) {
			UsuarioDto userDto = userService.obtenerUsuarioByUsername(userName);
			layerResourceDto.setAuthority((AuthorityDto) institucionService.getById(userDto
					.getAuthorityId()));
		}

		String layerTitle = uploadForm.getName();

		// We clean quotes that break json response due to Ext stupidity. Fix
		// for #83421.
		layerTitle = layerTitle.replaceAll("\"", "''");

		// We generate an unique layer name
		String layerName = layerResourceService.generateNameNotYetUsed(
				GeoserverUtils.createName(layerTitle));

		try {
			// comprobamos que se ha recibido un fichero
			if (uploadForm.getFile() == null) {
				return prepareErrorResponse("1",
						"No se ha recibido ningún archivo",
						"uploadAndSaveKml. No se ha recibido ningún archivo");
			}
			// Comprobamos que el usuario ha indicado un nombre de tabla
			if (StringUtils.isBlank(uploadForm.getName())) {
				return prepareErrorResponse("5",
						"No ha indicado un nombre para la capa",
						"uploadAndSaveKml. El usuario no ha indicado un nombre para la capa.");
			}
			// String contentType = uploadForm.getFile().getContentType();
			// if (!APPLICATION_VND_KML.equalsIgnoreCase(contentType)) {
			// return prepareErrorResponse("7",
			// "El fichero indicado no es un KML compatible",
			// "uploadAndSaveKml.");
			// }

			// The table name is the same as the layer's.
			String tableName = layerName;
			String geoColumnName = "geom";
			MultipartFile mpFile = uploadForm.getFile();

			List<String> columnNames = new ArrayList<String>();
			columnNames.add("idKml");
			columnNames.add("descripcion");
			columnNames.add(coordX);
			columnNames.add(coordY);

			kmlFile = File.createTempFile("kml_imp", ".kml");
			InputStream is = mpFile.getInputStream();

			mpFile.transferTo(kmlFile);
			kmlFile.deleteOnExit();

			Kml kml = Kml.unmarshal(is);

			if (kml == null) {

				return prepareErrorResponse("7",
						"No se ha podido procesar el fichero KML. El fichero indicado no es un KML compatible.",
						"uploadAndSaveKml.");
			}
			List<ColumnGeo> columns = importGeoJdbDao.createTable(tableName, columnNames);
			importGeoJdbDao.createGeoColumnDB(tableName, geoColumnName, srid);

			Document document = (Document) kml.getFeature();
			List<Feature> t = document.getFeature();

			processPlaceMarks(t, tableName, geoColumnName, srid, coordX, coordY, columns);

			if (!publishLayerGeoserver(workspaceName, layerName, geoColumnName, layerTitle, srid)) {
				return prepareErrorResponse("10",
						"No se ha podido crear la capa geográfica",
						"uploadAndSaveKml. No se ha podido crear la capa "
								+ layerName + " en el workspace " + workspaceName);
			}

			layerResourceDto.setActive(true);
			layerResourceDto.setCreateDate(new Date());
			layerResourceDto.setUpdateDate(new Date());
			layerResourceDto.setOriginalFileName(layerName);
			layerResourceDto.setTmpLayerName(tableName);
			layerResourceDto.setWorkspaceName(workspaceName);
			LayerTypeDto layerType = layerTypeService
					.getLayerTypeById(LayerTypeService.POSTGIS_VECTORIAL_ID);
			layerResourceDto.setLayerType(layerType);
			UsuarioDto userDto = userAdminService
					.obtenerUsuarioByUsername(userName);
			Long authorityID = null;
			AuthorityDto authority = null;
			if (userDto != null) {
				authorityID = userDto.getAuthorityId();
				authority = (AuthorityDto) institucionService
						.getById(authorityID);
			}
			layerResourceDto.setAuthority(authority);
			layerResourceDto = (LayerResourceDto) layerResourceService.create(layerResourceDto);

			String layerResourceId = layerResourceDto.getId().toString();
			return prepareSuccessResponse(workspaceName, layerName,
					layerTitle, layerResourceId, layerResourceDto);

		} catch (IllegalStateException e) {
			LOG.error("uploadAndSaveKml. Error capturado", e);
		} catch (IOException e) {
			LOG.error("uploadAndSaveKml. Error capturado", e);
		} finally {
			System.out.println("finally");
		}

		return null;
	}

	@SuppressWarnings("unchecked")
	private void processPlaceMarks(List<Feature> features, String tableName, String geoColumnName,
			String srid, String coordX, String coordY, List<ColumnGeo> columns) {

		List<String> values = new ArrayList<String>();
		List<Map<String, String>> mapParamsList = new ArrayList<Map<String, String>>();
		String x = "";
		String y = "";
		Placemark pm;
		List<Geometry> geoList;
		MultiGeometry mgeo;
		Point point = null;

		for (Object o : features) {

			if (o instanceof Placemark) {
				pm = (Placemark) o;
				values.add(pm.getName());
				values.add(pm.getDescription());

				if (pm.getGeometry() instanceof MultiGeometry) {
					mgeo = (MultiGeometry) pm.getGeometry();
					geoList = mgeo.getGeometry();

					for (Geometry geo : geoList) {
						if (geo instanceof Point) {
							point = (Point) geo;
							break;
						}
					}
				} else if (pm.getGeometry() instanceof Point) {
					point = (Point) pm.getGeometry();
				}
				// Procesamos el punto encontrado
				if (point != null) {
					x = new Double(point.getCoordinates().get(0).getLongitude()).toString();
					values.add(x);
					y = new Double(point.getCoordinates().get(0).getLatitude()).toString();
					values.add(y);
					mapParamsList.add(importGeoJdbDao.addData(tableName, columns, values));
					/*
					 * importGeoJdbDao.addData(tableName, columns, values);
					 * importGeoJdbDao.updatePointColumn(geoColumnName,
					 * tableName, x, y, srid, coordX, coordY);
					 */
					values.clear();
				}

			} else if (o instanceof Folder) {
				Folder f = (Folder) o;
				List<Feature> tg = f.getFeature();
				processPlaceMarks(tg, tableName, geoColumnName, srid, coordX, coordY, columns);
			}

		}
		String sqlInsert = importGeoJdbDao.getSQL(tableName, columns, values);
		importGeoJdbDao.batchUpdate(sqlInsert, mapParamsList.toArray(new HashMap[mapParamsList.size()]));

		List<Map<String, Object>> coordenadas = importGeoJdbDao.getTableCoordinates(tableName, coordX, coordY);

		String columnXvalue;
		String columnYvalue;

		for (Map<String, Object> coordenada : coordenadas) {
			columnXvalue = (String) coordenada.get(coordX);
			columnYvalue = (String) coordenada.get(coordY);
			if (isNumeric(columnXvalue) && isNumeric(columnYvalue)) {
				importGeoJdbDao.updatePointColumn(geoColumnName, tableName, columnXvalue, columnYvalue, srid, coordX,
						coordY);
			}
		}
	}

	private boolean isNumeric(String num) {
		try {
			Double.parseDouble(num);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	/**
	 * Publica una capa en el servidor de geoserver
	 * 
	 * @param workspaceName
	 * @param geoColumnName
	 * @param layerTitle
	 * @param layerNameDepured
	 * @param SRID
	 * @return true si la capa se ha creado correctamente, false en caso
	 *         contrario
	 */
	private boolean publishLayerGeoserver(
			String workspaceName, String layerName, String geoColumnName, String layerTitle, String SRID) {

		GeometryType type = GeometryType.POINT;
		BoundingBox bbox = layerManagementDao.getTableBoundingBoxGeoColumn(geoColumnName, layerName);

		// The layer name is the table's also.
		if(!geoserverService.publishGsDbLayer(workspaceName, layerName,
				layerName, layerTitle, bbox, type)){
			return false;
		}
		

		// Fixes #82920 by copying the default style for the layer.
		if(!geoserverService.copyLayerStyle(layerName, layerName)
				|| !geoserverService.setLayerStyle(workspaceName, layerName, layerName)) {
			return false;
		} 		
		
		return true;
	}

	private String prepareSuccessResponse(String workspaceName,
			String layerName, String layerTitle,
			String layerResourceId, LayerResourceDto layerResource) {

		Map<String, Object> resultMap = Maps.newHashMap();
		Map<String, Object> rootObject = Maps.newHashMap();
		ObjectMapper mapper = new ObjectMapper();
		rootObject.put("status", "success");
		rootObject.put("layerName",
				StringEscapeUtils.escapeHtml4(workspaceName + ":" + layerName));
		rootObject.put("layerTitle", StringEscapeUtils.escapeHtml4(layerTitle));
		rootObject.put("layerResourceId", layerResourceId);
		rootObject.put("layerTypeId", layerResource.getLayerType().getId());

		resultMap.put(ROOT, rootObject);
		resultMap.put(RESULTS, Integer.valueOf(1));
		resultMap.put(SUCCESS, true);
		StringWriter sw = new StringWriter();
		try {
			String geoserverRestUrl = gsConfiguration.getServerUrl();
			String geoserverUrl = geoserverRestUrl.replace("/rest", "") + "/wms";
			rootObject.put("serverUrl", geoserverUrl);
			mapper.writeValue(sw, resultMap);
		} catch (JsonGenerationException e) {
			LOG.error(e);
			e.printStackTrace();
		} catch (JsonMappingException e) {
			LOG.error(e);
		} catch (IOException e) {
			LOG.error(e);
		}
		return sw.toString();
	}

	/**
	 * @return
	 * @throws IOException
	 * @throws JsonGenerationException
	 * @throws JsonMappingException
	 */
	private String prepareErrorResponse(String codigo, String message,
			String logMessage) {
		Map<String, Object> resultMap = Maps.newHashMap();
		Map<String, String> rootObject = Maps.newHashMap();
		ObjectMapper mapper = new ObjectMapper();
		rootObject.put("status", "error");
		rootObject.put("codigo", codigo);
		rootObject.put("message", StringEscapeUtils.escapeHtml4(message));

		if (LOG.isInfoEnabled()) {
			LOG.info(logMessage);
		}

		resultMap.put(ROOT, rootObject);
		resultMap.put(RESULTS, Integer.valueOf(1));
		resultMap.put(SUCCESS, true);
		StringWriter sw = new StringWriter();
		try {

			mapper.writeValue(sw, resultMap);
		} catch (JsonGenerationException e) {
			LOG.error(e);
			e.printStackTrace();
		} catch (JsonMappingException e) {
			LOG.error(e);
		} catch (IOException e) {
			LOG.error(e);
			;
		}
		return sw.toString();
	}

	private void rolUser(Model model) {
		model.addAttribute("IS_ADMIN",
				OhigginsUserDetails.userHasAuthority(ConstantesPermisos.ADMIN));
	}

}
