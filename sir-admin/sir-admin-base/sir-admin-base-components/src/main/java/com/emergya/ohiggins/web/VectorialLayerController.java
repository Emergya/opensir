/*
 * ShpWizardController.java
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
 * Authors:: Juan Luis Rodríguez Ponce (mailto:jlrodriguez@emergya.com)
 */
package com.emergya.ohiggins.web;

import com.emergya.ohiggins.config.WorkspaceNamesConfig;

import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.hibernate.HibernateException;
import org.hibernatespatial.postgis.PostgisDialect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.DataAccessException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.WebRequest;

import com.emergya.ohiggins.dto.LayerResourceDto;
import com.emergya.ohiggins.dto.LayerTypeDto;
import com.emergya.ohiggins.importers.ShpImporter;
import com.emergya.ohiggins.security.ConstantesPermisos;
import com.emergya.ohiggins.security.OhigginsUserDetails;
import com.emergya.ohiggins.security.SecurityUtils;
import com.emergya.ohiggins.service.LayerResourceService;
import com.emergya.ohiggins.service.LayerTypeService;
import com.emergya.ohiggins.service.UserAdminService;
import com.emergya.ohiggins.utils.ColumnMetadata;
import com.emergya.ohiggins.utils.DatabaseMetadata;
import com.emergya.ohiggins.utils.TableMetadata;
import com.emergya.ohiggins.web.util.ShpFileUploadForm;
import com.emergya.persistenceGeo.dao.DBManagementDao;
import com.emergya.persistenceGeo.exceptions.MultipleFilesWithSameExtension;
import com.emergya.persistenceGeo.importer.shp.IShpImporter;
import com.emergya.persistenceGeo.service.GeoserverService;
import com.emergya.persistenceGeo.utils.BoundingBox;
import com.emergya.persistenceGeo.utils.GeographicDatabaseConfiguration;
import com.emergya.persistenceGeo.utils.GeoserverUtils;
import com.emergya.persistenceGeo.utils.GeometryType;
import com.emergya.persistenceGeo.utils.GsRestApiConfiguration;
import com.google.common.collect.Maps;

/**
 * Controller del proceso de subida e importación de SHPs
 * 
 * @author jlrodriguez
 * 
 */
@Controller
public class VectorialLayerController {
	private final static Log LOG = LogFactory
			.getLog(VectorialLayerController.class);
	private final static String RESULTS = "results";
	private final static String ROOT = "data";
	private final static String SUCCESS = "success";
	private final static int DEFAULT_NEW_LAYER_SRS_CODE = 4326;

    @Autowired
    private WorkspaceNamesConfig workspaceNamesConfig;    
	@Autowired
	private ShpImporter importer;
	@Autowired
	private IShpImporter commandLineImporter;
	@Autowired
	private LayerResourceService layerResourceService;
	@Autowired
	private DBManagementDao dbManagementDao;
	
	@Autowired
	private UserAdminService userService;
	@Autowired
	@Qualifier("dataSourceHibernate")
	private DataSource datasource;
	@Autowired(required = false)
	private GeographicDatabaseConfiguration dbConfig;
	@Autowired
	private GeoserverService geoserverService;
	@Autowired
	private LayerTypeService layerTypeService;
	@Autowired
	private GsRestApiConfiguration gsConfiguration;

	@RequestMapping(value = "/uploadShp/step1", method = RequestMethod.POST)
	public @ResponseBody
	String uploadAndSaveShp(
			@ModelAttribute("uploadForm") ShpFileUploadForm uploadForm,
			Model map, BindingResult result, WebRequest webRequest) {
		if (LOG.isInfoEnabled()) {
			LOG.info("Importando archivo SHP");
		}
		LayerResourceDto dto = new LayerResourceDto();
		File zipFile = null;
		File shpFile = null;

		rolUser(map);
		
		try {
			// comprobamos que se ha recibido un fichero
			if (uploadForm.getFile() == null) {
				return prepareErrorResponse("1",
						"No se ha recibido ningún archivo",
						"uploadAndSaveShp. No se ha recibido ningún archivo");
			}
			// Comprobamos que el usuario ha indicado un nombre de tabla
			if (StringUtils.isBlank(uploadForm.getName())) {
				return prepareErrorResponse("5",
						"No ha indicado un nombre para la capa",
						"uploadAndSaveShp. El usuario no ha indicado un nombre para la capa.");
			}

			String layerTitle = uploadForm.getName();
			
			// We clean quotes that break json response due to Ext stupidity. Fix for #83421.
			layerTitle = layerTitle.replaceAll("\"", "''");

			
			String layerName = layerResourceService
					.generateNameNotYetUsed(GeoserverUtils
							.createName(layerTitle));

			// Comprobamos que no exista ya una capa con ese nombre en el
			// workspace del usuario.
			if (geoserverService.existsLayerInWorkspace(layerName,
                    workspaceNamesConfig.getTmpWorkspace())) {
				return prepareErrorResponse(
						"6",
						"Ya existe una capa con ese nombre. Por favor elija otro nombre.",
						"uploadAndSaveShp. El nombre de la capa ya está en uso en el workspace");
			}

			// Comprobamos que el fichero es un ZIP
			if (!uploadForm.validateFiletype()) {
				return prepareErrorResponse("2", "No es un archivo ZIP",
						"uploadAndSaveShp. No se ha recibido un archivo ZIP");
			}
			zipFile = File.createTempFile("shp_imp", ".zip");
			uploadForm.getFile().transferTo(zipFile);
			zipFile.deleteOnExit();
			shpFile = importer.unzipAndLookForShp(zipFile);
			if (shpFile == null) {
				return prepareErrorResponse("3",
						"No se ha encontrado un SHP dentro del ZIP",
						"uploadAndSaveShp. No se ha encontrado ningún archivo SHP dentro del ZIP");
			}
			if (!commandLineImporter.checkIfAllFilesExist(shpFile
					.getParentFile().getAbsolutePath(), shpFile.getName()
					.replace(".shp", ""))) {
				return prepareErrorResponse(
						"4",
						"No se han encontrado los componentes del SHP necesarios",
						"uploadAndSaveShp. No se han encontrado los archivos con las extensiones necesarias para leer el SHP");
			}

			// Table name is actually the layer name.
			boolean imported = commandLineImporter.importShpToDb(
					shpFile.getAbsolutePath(), layerName, false);
			if (imported) {
				LayerTypeDto layerType = layerTypeService
						.getLayerTypeById(LayerTypeService.POSTGIS_VECTORIAL_ID);
				dto.setLayerType(layerType);
				dto.setActive(true);
				dto.setCreateDate(new Date());
				dto.setUpdateDate(new Date());
				dto.setOriginalFileName(shpFile.getName());
				dto.setTmpLayerName(layerName);
				dto.setWorkspaceName(workspaceNamesConfig.getTmpWorkspace());

				dto = (LayerResourceDto) layerResourceService.create(dto);

				List<ColumnMetadata> columns = getTableColumns(layerName);
				List<ColumnMetadata> columnsOriginal = new ArrayList<ColumnMetadata>(
						columns);
				for (ColumnMetadata column : columnsOriginal) {
					if ("geometry".equalsIgnoreCase(column.getTypeName())) {
						columns.remove(column);
					}
				}

				return prepareSuccessResponse(layerTitle, layerName, columns,
						dto);

			}

		} catch (MultipleFilesWithSameExtension mfwse) {
			return prepareErrorResponse("4",
					"Se han encontrado más de un archivo SHP en el ZIP",
					"uploadAndSaveShp. Se han encontrado más de un SHP dentro del ZIP");
		} catch (IllegalStateException e) {
			LOG.error("uploadAndSaveShp. Error capturado", e);
		} catch (IOException e) {
			LOG.error("uploadAndSaveShp. Error capturado", e);
		} catch (SQLException e) {
			LOG.error("uploadAndSaveShp. Error importando a base de datos", e);
		} finally {
			cleanupFiles(zipFile, shpFile);
		}

		return null;
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

	@RequestMapping(value = "/uploadShp/step2", produces = "application/json")
	public @ResponseBody
	String updateColumnNames(
			@RequestParam(value = "tableName") String tableName,
			@RequestParam(value = "newName") String[] newName,
			@RequestParam(value = "oldName") String[] oldName,
			@RequestParam(value = "layerTitle") String layerTitle) {
		LOG.info("/uploadShp/step2");
		try {
			if (StringUtils.isNotBlank(tableName) && newName != null
					&& oldName != null && newName.length == oldName.length) {
				for (int i = 0; i < newName.length; i++) {
					if (StringUtils.isNotBlank(newName[i])
							&& StringUtils.isNotBlank(oldName[i])
							&& !StringUtils.equals(newName[i], oldName[i])) {
						dbManagementDao.changeTableColumnName(tableName,
								oldName[i], newName[i]);
					}
				}
			}
		} catch (DataAccessException dae) {
			LOG.error("uploadAndSaveShp. Error cambiando nombre de columna",
					dae);
			return prepareErrorResponse(
					"11",
					"Se ha producido un error cambiando el nombre de los campos",
					dae.getMessage());

		} catch (HibernateException sqle) {
			LOG.error("uploadAndSaveShp. Error cambiando nombre de columna",
					sqle);
			return prepareErrorResponse(
					"12",
					"Se ha producido un error cambiando el nombre de los campos",
					sqle.getMessage());

		}

		OhigginsUserDetails usuario = (OhigginsUserDetails) SecurityUtils
				.getPrincipal();
		String userName = null;
		if (usuario != null) {
			userName = usuario.getUsername();
		}
		String workspaceName = userService.getWorkspaceName(userName);

		// Now publish the layer in geoserver.
		BoundingBox bbox = dbManagementDao.getTableBoundingBox(tableName);
		GeometryType type = dbManagementDao.getTableGeometryType(tableName);

		if (!geoserverService.publishGsDbLayer(workspaceName, tableName,
				tableName, layerTitle, bbox, type)) {
			return prepareErrorResponse("10",
					"No se ha podido crear la capa geográfica",
					"uploadAndSaveShp. No se ha podido crear la capa "
							+ tableName + " en el workspace " + workspaceName);
		}
		// We are going to create and set a new style for the newly published
		// layer,
		// Basically, we are copiying the default style (currently applied to
		// the
		// published layer) into a new style named as the layer (which is named
		// as
		// the backing table)
		if (!geoserverService.copyLayerStyle(tableName, tableName)) {
			return prepareErrorResponse("10",
					"No se ha podido crear la capa geográfica",
					"uploadAndSaveShp. No se ha podido crear la capa "
							+ tableName + " en el workspace " + workspaceName);
		}

		// We change the layer's style so its the new style we have just
		// created.
		if (!geoserverService
				.setLayerStyle(workspaceName, tableName, tableName)) {
			return prepareErrorResponse("10",
					"No se ha podido crear la capa geográfica",
					"uploadAndSaveShp. No se ha podido crear la capa "
							+ tableName + " en el workspace " + workspaceName);
		}

		return prepareSuccessResponseStep2(workspaceName, tableName,
				layerTitle, null);

	}

	@RequestMapping(value = "/vectorialLayerController/newTempLayer", produces = "application/json")
	public @ResponseBody
	String createTempLayer(
			@RequestParam(value = "layerName") String layerTitle,
			@RequestParam(value = "geometryType") GeometryType geometryType) {
		if (LOG.isDebugEnabled()) {
			LOG.debug("createTempLayer ( layerName=\"" + layerTitle
					+ "\", geometryType = \"" + geometryType + "\")");
		}

		String layerName = layerResourceService
				.generateNameNotYetUsed(layerTitle);
        
        String tmpWorkspace= workspaceNamesConfig.getTmpWorkspace();

		if (geoserverService.existsLayerInWorkspace(layerName,tmpWorkspace)) {
			return prepareErrorResponse(
					"6",
					"Ya existe una capa con ese nombre. Por favor elija otro nombre.",
					"uploadAndSaveShp. El nombre de la capa ya está en uso en el workspace");
		}

		// The table name is the same as the layer's.
		boolean tableCreated = dbManagementDao.createLayerTable(
				layerName, DEFAULT_NEW_LAYER_SRS_CODE, geometryType);
		if (!tableCreated) {
			return prepareErrorResponse(
					"20",
					"No se ha podido crear la capa. Se produjo un error creando la tabla en la base de datos.",
					"createTempLayer. No se ha podido crear la tabla "
							+ layerName + " en la base de datos");
		}

		LayerResourceDto dto = new LayerResourceDto();
		LayerTypeDto layerType = layerTypeService
				.getLayerTypeById(LayerTypeService.POSTGIS_VECTORIAL_ID);
		dto.setLayerType(layerType);
		dto.setActive(true);
		dto.setCreateDate(new Date());
		dto.setUpdateDate(dto.getCreateDate());
		dto.setTmpLayerName(layerName);
		dto.setWorkspaceName(tmpWorkspace);

		dto = (LayerResourceDto) layerResourceService.create(dto);

		// Now publish the layer in geoserver.
		boolean creado = false;
		BoundingBox bbox = new BoundingBox();
		bbox.setMinx(-180.0);
		bbox.setMiny(-90.0d);
		bbox.setMaxx(180.0d);
		bbox.setMaxy(90.0d);
		bbox.setSrs("EPSG:" + DEFAULT_NEW_LAYER_SRS_CODE);

		creado = geoserverService.publishGsDbLayer(
				tmpWorkspace, layerName, layerName,layerTitle, bbox, geometryType);
		if (creado) {
			creado = geoserverService.copyLayerStyle(layerName, layerName);
		}
		if (creado) {
			creado = geoserverService.setLayerStyle(
					tmpWorkspace, layerName, layerName);
		}

		if (!creado) {
			return prepareErrorResponse("10",
					"No se ha podido crear la capa geográfica",
					"uploadAndSaveShp. No se ha podido crear la capa "
							+ layerTitle + " en el workspace "
							+ tmpWorkspace);
		}
		return prepareSuccessResponseStep2(tmpWorkspace,layerName, layerTitle, dto);
	}

	/**
	 * @param tableName
	 * @return
	 * @throws SQLException
	 */
	private List<ColumnMetadata> getTableColumns(String tableName)
			throws SQLException {
		Connection conn = datasource.getConnection();
		DatabaseMetadata dbMeta = new DatabaseMetadata(conn,
				new PostgisDialect());

		TableMetadata tableMeta = dbMeta.getTableMetadata(tableName,
				dbConfig.getSchema(), "%", true);
		Map<String, ColumnMetadata> columns = tableMeta.getColumns();
		List<ColumnMetadata> result = new ArrayList<ColumnMetadata>(
				columns.values());

		conn.close();

		return result;

	}

	private String prepareSuccessResponseStep2(String workspaceName,
			String layerName, String layerTitle, LayerResourceDto layerResource) {
		Map<String, Object> resultMap = Maps.newHashMap();
		Map<String, Object> rootObject = Maps.newHashMap();
		ObjectMapper mapper = new ObjectMapper();
		rootObject.put("status", "success");
		rootObject.put("layerName",
				StringEscapeUtils.escapeHtml4(workspaceName + ":" + layerName));
		rootObject.put("layerTitle", StringEscapeUtils.escapeHtml4(layerTitle));
		if (layerResource != null) {
			rootObject.put("layerResourceId", layerResource.getId());
			rootObject.put("layerTypeId", layerResource.getLayerType().getId());
		}

		resultMap.put(ROOT, rootObject);
		resultMap.put(RESULTS, Integer.valueOf(1));
		resultMap.put(SUCCESS, true);
		StringWriter sw = new StringWriter();
		try {
			String geoserverRestUrl = gsConfiguration.getServerUrl();
			String geoserverUrl = geoserverRestUrl.replace("/rest", "")
					+ "/wms";
			rootObject.put("serverUrl", geoserverUrl);
			mapper.writeValue(sw, resultMap);
		} catch (JsonGenerationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JsonMappingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return sw.toString();
	}

	private String prepareSuccessResponse(String layerTitle, String tableName,
			List<ColumnMetadata> columns, LayerResourceDto layerResource) {

		for (ColumnMetadata cm : columns) {
			cm.setName(StringEscapeUtils.escapeHtml4(cm.getName()));
		}
		Map<String, Object> resultMap = Maps.newHashMap();
		Map<String, Object> rootObject = Maps.newHashMap();
		ObjectMapper mapper = new ObjectMapper();
		rootObject.put("status", "success");
		rootObject.put("tableName", StringEscapeUtils.escapeHtml4(tableName));
		rootObject.put("layerName", StringEscapeUtils.escapeHtml4(tableName));
		rootObject.put("columnsMetadata", columns);
		rootObject.put("layerResourceId", layerResource.getId());
		rootObject.put("layerTypeId", layerResource.getLayerType().getId());
		rootObject.put("layerTitle", StringEscapeUtils.escapeHtml4(layerTitle));

		resultMap.put(ROOT, rootObject);
		resultMap.put(RESULTS, Integer.valueOf(1));
		resultMap.put(SUCCESS, true);
		StringWriter sw = new StringWriter();
		try {

			mapper.writeValue(sw, resultMap);
		} catch (JsonGenerationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JsonMappingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
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
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JsonMappingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return sw.toString();
	}

	private void rolUser(Model model) {
		model.addAttribute("IS_ADMIN",
				OhigginsUserDetails.userHasAuthority(ConstantesPermisos.ADMIN));
	}

	/**
	 * Método que restringe el acceso a un método si el usuario no posee los
	 * roles
	 * 
	 * @throws AccessDeniedException
	 *             is User principal doesn't exist or does not have any passed
	 *             roles
	 */
	protected void decideIfIsUserInRole(String... args)
			throws AccessDeniedException {
		OhigginsUserDetails usuario = (OhigginsUserDetails) SecurityUtils
				.getPrincipal();

		Collection<GrantedAuthority> permisos = null;

		boolean permitido = false;

		if (usuario == null) {
			throw new AccessDeniedException(
					"No tiene permiso para acceder a este servicio");
		} else {
			permisos = usuario.getAuthorities();
		}

		for (GrantedAuthority permiso : permisos) {
			for (String rol : args) {
				if (permiso.getAuthority().equals(rol)) {
					permitido = true;
				}
			}
		}

		if (!permitido) {
			throw new AccessDeniedException(
					"No tiene permiso para acceder a este servicio");
		}
	}

	/**
	 * Comprueba si existe un usuario logado en el sistema
	 * 
	 * @param webRequest
	 */
	protected void isLoggedIn(WebRequest webRequest) {
		String usuario = getCurrentUsernameLogado(webRequest);
		if (usuario == null)
			throw new AccessDeniedException("No esta logado en el sistema");
	}

	/**
	 * Usuario logado del contexto de seguridad
	 * 
	 * @param webRequest
	 * @return
	 */
	private String getCurrentUsernameLogado(WebRequest webRequest) {
		SecurityContext context = (SecurityContext) webRequest.getAttribute(
				"SPRING_SECURITY_CONTEXT", RequestAttributes.SCOPE_SESSION);

		if (context != null && context.getAuthentication().isAuthenticated()
				&& !context.getAuthentication().getAuthorities().isEmpty()) {
			return context.getAuthentication().getName();
		}
		return null;
	}

}
