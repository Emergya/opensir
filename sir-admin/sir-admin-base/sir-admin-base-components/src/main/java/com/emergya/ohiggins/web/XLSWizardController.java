/*
 * XLSWizardController.java
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
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.math.NumberUtils;
import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
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
import org.springframework.web.bind.annotation.RequestParam;
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
import org.apache.poi.hssf.OldExcelFormatException;

/**
 * Controller del proceso de subida e importación de XLSs
 * 
 * @author ajrodriguez
 * 
 */
@Controller
public class XLSWizardController {

	private final static Log LOG = LogFactory.getLog(XLSWizardController.class);
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
	private LayerResourceService layerService;

	@Autowired
	private ImportGeoJdbDao importGeoJdbDao;

	@Autowired
	private OhigginsLayerResourceDao layerResourceDao;
	
	@Autowired
	private DBManagementDao dbManagementDao;

	@Autowired
	private GsRestApiConfiguration gsConfiguration;

	@Autowired
	private LayerTypeService layerTypeService;

	@Autowired
	private UserAdminService userAdminService;

	@SuppressWarnings({ "static-access", "unchecked" })
	@RequestMapping(value = "/uploadXls/step1", method = RequestMethod.POST)
	public @ResponseBody
	String uploadAndSaveXls(
			@ModelAttribute("uploadForm") FileUploadForm uploadForm,
			Model map, BindingResult result, WebRequest webRequest) {
		if (LOG.isInfoEnabled()) {
			LOG.info("Importando archivo XLS");
		}
		LayerResourceDto dto = new LayerResourceDto();
		File xslFile = null;
		String userName = null;

		rolUser(map);

		OhigginsUserDetails usuario = (OhigginsUserDetails) SecurityUtils
				.getPrincipal();
		if (usuario != null) {
			userName = usuario.getUsername();
		}
		String workspaceName = userService.getWorkspaceName(userName);
		if (userName != null) {
			UsuarioDto userDto = userService.obtenerUsuarioByUsername(userName);
			dto.setAuthority((AuthorityDto) institucionService.getById(userDto
					.getAuthorityId()));
		}

		try {
			// comprobamos que se ha recibido un fichero
			if (uploadForm.getFile() == null) {
				return prepareErrorResponse("1",
						"No se ha recibido ningún archivo",
						"uploadAndSaveXls. No se ha recibido ningún archivo");
			}
			// Comprobamos que el usuario ha indicado un nombre de tabla
			if (StringUtils.isBlank(uploadForm.getName())) {
				return prepareErrorResponse("5",
						"No ha indicado un nombre para la capa",
						"uploadAndSaveXls. El usuario no ha indicado un nombre para la capa.");
			}
			// String contentType = uploadForm.getFile().getContentType();
			// if (!APPLICATION_VND_MS_EXCEL.equalsIgnoreCase(contentType)
			// && !XLSX_MIME_TYPE.equalsIgnoreCase(contentType)) {
			// return prepareErrorResponse("7",
			// "El fichero indicado no es un XLS compatible",
			// "uploadAndSavXls.");
			// }

			String layerTitle = uploadForm.getName();

			// We clean quotes that break json response due to Ext stupidity.
			// Fix for #83421.
			layerTitle = layerTitle.replaceAll("\"", "''");

			// We create a unique name from the user-supplied layer title.
			// It will also be the layer name when created in the next step.
			String tableName = layerService.generateNameNotYetUsed(GeoserverUtils.createName(layerTitle));

			// This shouldn't happen as we have just created an unique name, but
			// still...
			if (geoserverService.existsLayerInWorkspace(tableName, workspaceName)) {
				return prepareErrorResponse(
						"6",
						"Ya existe una capa con ese nombre. Por favor elija otro nombre.",
						"uploadAndSaveXls. El nombre de la capa ya está en uso en el workspace");
			}

			InputStream is;
			MultipartFile mpFile = uploadForm.getFile();
			List<String> columnNames = new ArrayList<String>();
			List<String> columnNamesParsed = new ArrayList<String>();
			List<Integer> columnsIgnored = new ArrayList<Integer>();

			xslFile = File.createTempFile("xls_imp", ".xls");
			is = mpFile.getInputStream();

			mpFile.transferTo(xslFile);
			xslFile.deleteOnExit();

			Workbook workbook;
            try {
                 workbook = WorkbookFactory.create(is);
            } catch(OldExcelFormatException ofe) {
                return prepareErrorResponse(
						"8",
						"No se pudo abrir el fichero de Excel, ya que parece que tiene un formato antiguo. Sólo se pueden abrir ficheros de Excel XLS versiones 97/XP/2003 o XLSX.",
						"uploadAndSaveXls. Wrong format version");
            }
			Sheet sheet = workbook.getSheetAt(0);
			Row row = sheet.getRow(sheet.getFirstRowNum());

			if (row.getPhysicalNumberOfCells() < 2) {
				return prepareErrorResponse(
						"8",
						"No se han encontrado las columnas necesarias para procesar la hoja de cálculo. Debe contener al menos dos columnas.",
						"uploadAndSaveXls. El fichero no contiene el número de columnas necesarias.");
			}

			Iterator<Cell> headerCells = row.cellIterator();
			String columnName;
			String columnNameParsed;
			int headerIdx = 0;
			while (headerCells.hasNext()) {
				// se obtienen los nombres de las columnas cuyas cabeceras
				// no están vacías y no están formadas exclusivamente por
				// caracteres extraños
				columnName = headerCells.next().getStringCellValue();                
				columnNameParsed = GeoserverUtils.sanitizeColumnName(columnName);
				if (StringUtils.isNotBlank(columnNameParsed)) {
					columnNames.add(columnName);
					columnNamesParsed.add(columnNameParsed);
				} else {
					// las ignoradas se añaden a una lista
					columnsIgnored.add(new Integer(headerIdx));
				}
				headerIdx++;
			}

			if (columnNamesParsed.size() < 2) {
				return prepareErrorResponse(
						"8",
						"No se han encontrado las columnas necesarias para procesar la hoja de cálculo. Compruebe el nombre de las columnas.",
						"uploadAndSaveXls.");
			}

			// Comprobamos columnas repetidas
			if (new HashSet<String>(columnNamesParsed).size() != columnNamesParsed.size()) {
				return prepareErrorResponse(
						"8",
						"Se han encontrado nombres de columas repetidas en el fichero de entrada.",
						"uploadAndSaveXls.");
			}

			// Importamos el Excel

			int firstRowNumber = sheet.getFirstRowNum();
			int rowsNumber = sheet.getLastRowNum() - firstRowNumber + 1;

			if (dbManagementDao.tableExists("ohiggins", tableName)) {
				return prepareErrorResponse(
						"9",
						"No se ha podido crear la tabla en base de datos.",
						"uploadAndSaveXls.");
			}
			List<ColumnGeo> columns = importGeoJdbDao.createTable(tableName, columnNamesParsed);
			if (columns.isEmpty()) {
				return prepareErrorResponse(
						"9",
						"No se ha podido crear la tabla en base de datos. No existen columnas.",
						"uploadAndSaveXls.");
			}

			List<String> values = new ArrayList<String>();

			String strCell = "";

			List<Map<String, String>> mapParamsList = new ArrayList<Map<String, String>>();
			
			// Obtenemos los datos de la fila actual
			for (int rowIdx = firstRowNumber + 1; rowIdx < rowsNumber; rowIdx++) {

				for (int colIdx = 0; colIdx < columnNames.size(); colIdx++) {
					if (!columnsIgnored.contains(new Integer(colIdx))) {
						if (sheet.getRow(rowIdx).getCell(colIdx) != null
								&& sheet.getRow(rowIdx).getCell(colIdx).getCellType() == Cell.CELL_TYPE_NUMERIC) {
							strCell = new Double(sheet.getRow(rowIdx).getCell(colIdx).getNumericCellValue()).toString();
						} else if (sheet.getRow(rowIdx).getCell(colIdx) != null
								&& sheet.getRow(rowIdx).getCell(colIdx).getCellType() == Cell.CELL_TYPE_STRING) {
							strCell = sheet.getRow(rowIdx).getCell(colIdx).getStringCellValue();
						} else {
							strCell = "";
						}
						values.add(strCell);
					}
				}
				if (values.size() > 0) {
					mapParamsList.add(importGeoJdbDao.addData(tableName, columns, values));
					values.clear();
				}
			}
			String sqlInsert = importGeoJdbDao.getSQL(tableName, columns, values);
			// bathcupdate
			importGeoJdbDao.batchUpdate(sqlInsert, mapParamsList.toArray(new HashMap[mapParamsList.size()]));
		
			webRequest.setAttribute("tableName", tableName, webRequest.SCOPE_SESSION);
			webRequest.setAttribute("layerName", layerTitle, webRequest.SCOPE_SESSION);

			return prepareSuccessResponseExcel(columnNames);

		} catch (IllegalStateException e) {
			LOG.error("uploadAndSaveXls. Error capturado", e);
		} catch (IOException e) {
			LOG.error("uploadAndSaveXls. Error capturado", e);
		} catch (InvalidFormatException e) {
			LOG.error("uploadAndSaveXls. Error capturado al construir var: workbook", e);
			e.printStackTrace();
			return prepareErrorResponse("7",
					"El fichero indicado no es un XLS compatible",
					"uploadAndSavXls.");
		} finally {
			// cleanupFiles(zipFile, shpFile);
			System.out.println("finally");
		}

		return null;
	}

	@SuppressWarnings("static-access")
	@RequestMapping(value = "/uploadXls/step2", produces = "application/json")
	public @ResponseBody
	String updateColumnNames(
			@RequestParam(value = "projection") String projection,
			@RequestParam(value = "columnX") String columnX,
			@RequestParam(value = "columnY") String columnY,
			WebRequest webRequest) {
		LOG.info("/uploadXls/step2");

		if (StringUtils.isBlank(projection) || StringUtils.isBlank(columnX) && StringUtils.isBlank(columnY)) {

			return prepareErrorResponse(
					"10",
					"Faltan campos necesarios.",
					"uploadAndSaveXls.");
		}

		String geoColumnName = "geom";
		String projectionNum = projection.split(":")[1];
		String tableName = (String) webRequest.getAttribute("tableName", webRequest.SCOPE_SESSION);
		String layerTitle = (String) webRequest.getAttribute("layerName", webRequest.SCOPE_SESSION);

		columnX = GeoserverUtils.sanitizeColumnName(columnX);
		columnY = GeoserverUtils.sanitizeColumnName(columnY);

		String columnXvalue;
		String columnYvalue;

		// The table name is the same as the layer's.
		if (importGeoJdbDao.existColumn(tableName, geoColumnName)) {
			importGeoJdbDao.deleteTableColumns(tableName, geoColumnName);
		}

		importGeoJdbDao.createGeoColumnDB(tableName, geoColumnName, projectionNum);

		List<Map<String, Object>> coordenadas = importGeoJdbDao.getTableCoordinates(tableName, columnX, columnY);

		for (Map<String, Object> coordenada : coordenadas) {
			columnXvalue = (String) coordenada.get(columnX);
			columnYvalue = (String) coordenada.get(columnY);
			if (NumberUtils.isNumber(columnXvalue) && NumberUtils.isNumber(columnYvalue)) {
				importGeoJdbDao.updatePointColumn(geoColumnName, tableName, columnXvalue, columnYvalue,
						projectionNum, columnX, columnY);
			}
		}

		String userName = null;
		OhigginsUserDetails usuario = (OhigginsUserDetails) SecurityUtils
				.getPrincipal();
		if (usuario != null) {
			userName = usuario.getUsername();
		}
		String workspaceName = userService.getWorkspaceName(userName);
		LayerResourceDto layerResourceDto = new LayerResourceDto();

		// Now publish the layer in geoserver.
		try {
			BoundingBox bbox = dbManagementDao.getTableBoundingBoxGeoColumn(geoColumnName, tableName);
			GeometryType type = GeometryType.POINT;

			boolean creado = geoserverService.publishGsDbLayer(
					workspaceName, tableName, tableName, layerTitle, bbox, type);
			if (!creado) {
				return prepareErrorResponse("10",
						"No se ha podido publicar la capa geográfica.",
						"uploadAndSaveXls." + tableName + " en el workspace " + workspaceName);
			}

			// Fixes #82919 by copying the default style for the layer.
			if (!geoserverService.copyLayerStyle(tableName, tableName)
					|| !geoserverService.setLayerStyle(workspaceName, tableName, tableName)) {
				return prepareErrorResponse("10",
						"No se ha podido publicar la capa geográfica.",
						"uploadAndSaveXls." + tableName + " en el workspace " + workspaceName);
			}

			layerResourceDto.setActive(true);
			layerResourceDto.setCreateDate(new Date());
			layerResourceDto.setUpdateDate(new Date());
			layerResourceDto.setOriginalFileName(layerTitle);
			// The layer name is the same as the table's.
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
			layerResourceDto = (LayerResourceDto) layerService.create(layerResourceDto);

			String layerResourceId = layerResourceDto.getId().toString();
			return prepareSuccessResponseStep2(workspaceName, tableName,
					layerTitle, layerResourceId, layerResourceDto);

		} catch (Exception e) {
			return prepareErrorResponse("10",
					"No se ha podido publicar la capa geográfica.",
					"uploadAndSaveXls." + tableName + " en el workspace " + workspaceName);
		}

	}

	private String prepareSuccessResponseExcel(List<String> columnNames) {
		StringWriter sw = new StringWriter();
		Map<String, Object> resultMap = Maps.newHashMap();
		Map<String, Object> rootObject = Maps.newHashMap();
		ObjectMapper mapper = new ObjectMapper();
		List<String[]> columnNamesJSon = new ArrayList<String[]>();

		for (String column : columnNames) {
			column = StringEscapeUtils.escapeHtml4(column);
			String[] arrString = { column };
			columnNamesJSon.add(arrString);
		}

		rootObject.put("status", "success");
		rootObject.put("columnNames", columnNamesJSon);

		resultMap.put(ROOT, rootObject);
		resultMap.put(RESULTS, Integer.valueOf(1));
		resultMap.put(SUCCESS, true);

		try {
			mapper.writeValue(sw, resultMap);
		} catch (JsonGenerationException e) {
			LOG.error(e);
		} catch (JsonMappingException e) {
			LOG.error(e);
			e.printStackTrace();
		} catch (IOException e) {
			LOG.error(e);
		}

		return sw.toString();
	}

	private String prepareSuccessResponseStep2(String workspaceName,
			String layerName, String layerTitle, String layerResourceId, LayerResourceDto layerResource) {
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
		} catch (JsonMappingException e) {
			LOG.error(e);
		} catch (IOException e) {
			LOG.error(e);
		}
		return sw.toString();
	}

	private void rolUser(Model model) {
		model.addAttribute("IS_ADMIN",
				OhigginsUserDetails.userHasAuthority(ConstantesPermisos.ADMIN));
	}

}
