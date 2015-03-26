/*
 * DeleteColumnController.java
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

import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.WebRequest;

import com.emergya.ohiggins.dao.ImportGeoJdbDao;
import com.emergya.ohiggins.dto.AuthorityDto;
import com.emergya.ohiggins.dto.LayerDto;
import com.emergya.ohiggins.dto.LayerResourceDto;
import com.emergya.ohiggins.dto.UsuarioDto;
import com.emergya.ohiggins.security.ConstantesPermisos;
import com.emergya.ohiggins.security.OhigginsUserDetails;
import com.emergya.ohiggins.security.SecurityUtils;
import com.emergya.ohiggins.service.InstitucionService;
import com.emergya.ohiggins.service.LayerResourceService;
import com.emergya.ohiggins.service.LayerService;
import com.emergya.ohiggins.service.UserAdminService;
import com.emergya.persistenceGeo.service.GeoserverService;
import com.google.common.collect.Maps;

/**
 * Controller del proceso de eliminación de columnas en la tabla de una capa en base de datos.
 * 
 * @author ajrodriguez
 * 
 */
@Controller
public class DeleteColumnController {
	
	private final static Log LOG = LogFactory.getLog(DeleteColumnController.class);
	private final static String RESULTS = "results";
	private final static String ROOT = "data";
	private final static String SUCCESS = "success";
	
	@Autowired
	private UserAdminService userService;
	
	@Autowired
	private LayerService layerService;
	
	@Autowired
	private LayerResourceService layerResourceService;
	
	@Autowired
	private InstitucionService institucionService;
	
	@Autowired
	private ImportGeoJdbDao importGeoJdbDao;
	
	@Autowired
	private GeoserverService geoserverService;

	@SuppressWarnings("static-access")
	@RequestMapping(value = "/deleteDataColumn/loadData", method = RequestMethod.POST)
	public @ResponseBody
	String loadColumnNames(
			@RequestParam(value = "layerSelectedId") String layerSelectedId,
			@RequestParam(value = "layerSelectedTemporal") String layerSelectedTemporal,
			Model map,
			WebRequest webRequest) {
		if (LOG.isInfoEnabled()) {
			LOG.info("Consultando columnas de la capa seleccionada.");
		}
		LayerResourceDto dto = new LayerResourceDto();
		String userName = null;
		
		rolUser(map);

		OhigginsUserDetails usuario = (OhigginsUserDetails) SecurityUtils
				.getPrincipal();
		if (usuario != null) {
			userName = usuario.getUsername();
		}
		if (userName != null) {
			UsuarioDto userDto = userService.obtenerUsuarioByUsername(userName);
			dto.setAuthority((AuthorityDto) institucionService.getById(userDto
					.getAuthorityId()));
		}

		try {
			String tableName = null;	
			
			//Obtenemos la tabla asociada a la capa seleccionada en función de si es temporal o no.
			if(layerSelectedTemporal.equals("false")) {
				LayerDto selectedLayer = (LayerDto) layerService.getById(new Long(layerSelectedId));
				tableName = selectedLayer.getTableName();
			} else if(layerSelectedTemporal.equals("true")) {
				LayerResourceDto selectedLayer = (LayerResourceDto) layerResourceService.getById(new Long(layerSelectedId));
				tableName = selectedLayer.getTmpLayerName();
			}
			
			//Comprobamos que la capa posee tabla asociada en la base de datos.
			if (tableName == null) {
				return prepareErrorResponse("15",
						"Nombre de capa inválido. La capa seleccionada no posee tabla asociada en base de datos.",
						"loadColumnNames. La capa seleccionada no posee tabla asociada en base de datos.");
			}
			
			webRequest.setAttribute("tableName", tableName, webRequest.SCOPE_SESSION);
			
			//layerService.getById(id); //si está persistida si no layerresource
			
			List<Map<String,Object>> columnNames = importGeoJdbDao.getColumnsName(tableName);
			List<Map<String,Object>> columnConstraints= importGeoJdbDao.getColumnsConstraints(tableName);
			List<String> columnNamesStr = new ArrayList<String>();
			List<String> columnConstraintsStr = new ArrayList<String>();
			
			//Creamos la lista de columnas disponibles para su eliminación
			if (columnNames != null && !columnNames.isEmpty()) {
				
				for(Map<String,Object> columName: columnNames) {
					columnNamesStr.add((String) columName.get("column_name"));
				}
				
				//Si alguna de las columnas tiene un constraint...
				if (columnConstraints != null && !columnConstraints.isEmpty()) {
					for(Map<String,Object> columnConstraint: columnConstraints) {
						columnConstraintsStr.add((String) columnConstraint.get("column_name"));
					}
				}
				
				//... la eliminamos de la lista de columnas disponibles para su eliminación
				columnNamesStr.removeAll(columnConstraintsStr);
				return prepareSuccessResponseNames(columnNamesStr);
				
			} else {
				return prepareErrorResponse("10",
						"No se han recuperado columnas candidatas a eliminar.",
						"loadColumnNames. No se han recuperado columnas candidatas a eliminar. "
								+ " en la tabla " + tableName);
			}

		} catch (IllegalStateException e) {
			LOG.error("loadColumnNames. Error capturado", e);
		} finally {
			System.out.println("finally");
		}

		return null;
	}
	
	@SuppressWarnings("static-access")
	@RequestMapping(value = "/deleteDataColumn/step1", method = RequestMethod.POST)
	public @ResponseBody
	String deleteColumns(
			@RequestParam(value = "columnsSelectedString") String columnsSelectedString,
			Model map,
			WebRequest webRequest) {
		if (LOG.isInfoEnabled()) {
			LOG.info("Eliminando las columnas seleccionadas.");
		}
		LayerResourceDto dto = new LayerResourceDto();
		String userName = null;
		
		rolUser(map);

		OhigginsUserDetails usuario = (OhigginsUserDetails) SecurityUtils
				.getPrincipal();
		if (usuario != null) {
			userName = usuario.getUsername();
		}
		if (userName != null) {
			UsuarioDto userDto = userService.obtenerUsuarioByUsername(userName);
			dto.setAuthority((AuthorityDto) institucionService.getById(userDto
					.getAuthorityId()));
		}

		try {
			String tableName = (String) webRequest.getAttribute("tableName", webRequest.SCOPE_SESSION);
			
			String[] columnsToDelete = columnsSelectedString.split(",");
			for (String column: columnsToDelete) {
				importGeoJdbDao.deleteTableColumns(tableName, column);
			}
			geoserverService.reset();
			return prepareSuccessResponseDeleted();

		} catch (IllegalStateException e) {
			LOG.error("deleteColumns. Error capturado", e);
		} finally {
			System.out.println("finally");
		}

		return null;
	}
	
	private String prepareSuccessResponseNames(List<String> columnNames) {
		StringWriter sw = new StringWriter();
		Map<String, Object> resultMap = Maps.newHashMap();
		Map<String, Object> rootObject = Maps.newHashMap();
		ObjectMapper mapper = new ObjectMapper();
		
		rootObject.put("columnNames", columnNames);
		
		resultMap.put(ROOT, rootObject);
		resultMap.put(RESULTS, Integer.valueOf(columnNames.size()));
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
	
	private String prepareSuccessResponseDeleted() {
		StringWriter sw = new StringWriter();
		Map<String, Object> resultMap = Maps.newHashMap();
		ObjectMapper mapper = new ObjectMapper();
		
		resultMap.put(ROOT, null);
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
	
	/** Envío de errores a al vista.
	 * @param codigo
	 * @param message
	 * @param logMessage
	 * @return
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
