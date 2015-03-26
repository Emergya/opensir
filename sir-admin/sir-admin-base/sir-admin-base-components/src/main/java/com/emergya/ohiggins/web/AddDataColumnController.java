/*
 * AddDataColumnController.java
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
import com.emergya.persistenceGeo.utils.GeoserverUtils;
import com.google.common.collect.Maps;

/**
 * Controller del proceso de creación de una nueva columna en la tabla de una capa en base de datos.
 * 
 * @author ajrodriguez
 * 
 */
@Controller
public class AddDataColumnController {
	
	private final static Log LOG = LogFactory.getLog(AddDataColumnController.class);
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

	@RequestMapping(value = "/addDataColumn/step1", method = RequestMethod.POST)
	public @ResponseBody
	String createNewColumnDB(
			@RequestParam(value = "columnName") String columnName,
			@RequestParam(value = "columnType") String columnType,
			@RequestParam(value = "layerSelectedId") String layerSelectedId,
			@RequestParam(value = "layerSelectedTemporal") String layerSelectedTemporal,
			Model map,
			WebRequest webRequest) {
		if (LOG.isInfoEnabled()) {
			LOG.info("Creando columna en base de datos.");
		}
		LayerResourceDto dto = new LayerResourceDto();
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
			// Comprobamos que el usuario ha indicado un nombre de columna
			if (StringUtils.isBlank(columnName)) {
				return prepareErrorResponse("5",
						"No ha indicado un nombre para la nueva columna",
						"createNewColumnDB. El usuario no ha indicado un nombre para la nueva columna.");
			}
			
			if (StringUtils.isBlank(columnType)) {
				return prepareErrorResponse("5",
						"No ha indicado un tipo para la nueva columna",
						"createNewColumnDB. El usuario no ha indicado un tipo para la nueva columna.");
			}

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
						"createNewColumnDB. La capa seleccionada no posee tabla asociada en base de datos.");
			}
			
			//Parseamos el nombre de columna introducido por el usurio
			columnName = GeoserverUtils.sanitizeColumnName(columnName);			
			
			
			if(!importGeoJdbDao.existColumn(tableName, columnName)) {
				if (importGeoJdbDao.createNewColumn(tableName, columnName, columnType)) {
					geoserverService.reset();
					
					return prepareSuccessResponse();
				} else {
					return prepareErrorResponse("10",
							"No se ha podido crear la nueva columna.",
							"createNewColumnDB. No se ha podido crear la nueva columna."
									+ columnName + " en el workspace " + workspaceName);
				}
			} else {
				return prepareErrorResponse("10",
						"Ya existe una columna con ese nombre.",
						"createNewColumnDB. No se ha podido crear la nueva columna "
								+ columnName + " en el workspace " + workspaceName);
			}

		} catch (IllegalStateException e) {
			LOG.error("createNewColumnDB. Error capturado", e);
		} finally {
			System.out.println("finally");
		}

		return null;
	}
	
	private String prepareSuccessResponse() {
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
