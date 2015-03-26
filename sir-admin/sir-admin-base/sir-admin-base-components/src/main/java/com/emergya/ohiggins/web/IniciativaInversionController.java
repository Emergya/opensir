/* IniciativaInversionController.java
 * 
 * Copyright (C) 2012
 * 
 * This file is part of project ohiggins-app
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
package com.emergya.ohiggins.web;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.http.converter.json.MappingJacksonHttpMessageConverter;
import org.springframework.http.server.ServletServerHttpResponse;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.emergya.ohiggins.dto.NivelTerritorialDto;
import com.emergya.ohiggins.service.IniciativaInversionService;
import com.emergya.ohiggins.service.IniciativaInversionService.TIPO_PROYECTOS;
import com.emergya.ohiggins.service.NivelTerritorialService;
import com.emergya.ohiggins.web.util.geo.GeometryJsonMapper;
import com.google.inject.internal.Maps;

/**
 * @author <a href="mailto:jlrodriguez@emergya.com">jlrodriguez</a>
 * 
 */
@Controller
@RequestMapping("/inversion")
public class IniciativaInversionController {

	private final static Log LOG = LogFactory
			.getLog(IniciativaInversionController.class);
	private final static String RESULTS = "results";
	private final static String ROOT = "data";
	private final static String SUCCESS = "success";

	@Autowired
	private IniciativaInversionService iniciativaInversionService;
	@Autowired
	private NivelTerritorialService nivelTerritorialService;

	/**
	 * Devuelve la lista de los distintos años para los que hay proyectos de
	 * tipo <code>tipoProyecto</code>
	 * 
	 * @param tipoProyecto
	 *            el tipo de proyecto ({@link TIPO_PROYECTOS#PREINVERSION} y
	 *            {@link TIPO_PROYECTOS#EJECUCION}).
	 * @return la lista de los distintos años.
	 */
	@RequestMapping(value = "/getAnyos")
	@ResponseBody
	public Map<String, Object> getAnyos(
			@RequestParam("tipoProyecto") TIPO_PROYECTOS tipoProyecto) {
		Map<String, Object> result = new HashMap<String, Object>();
		try {
			List<String> anyos = iniciativaInversionService
					.getAnyosDisponibles(tipoProyecto);
			List<Map<String, String>> anyosJson = simpleListToMapList("anyo",
					anyos);
			result.put(ROOT, anyosJson);
			result.put(RESULTS, anyos.size());
			result.put(SUCCESS, true);
		} catch (Exception e) {
			LOG.error(
					"Error recuperando los distintos años de las iniciativas de inversión de tipo "
							+ tipoProyecto, e);
			result.put(SUCCESS, false);
		}

		return result;
	}

	/**
	 * @param elements
	 * @return
	 */
	private List<Map<String, String>> simpleListToMapList(String key,
			List<String> elements) {
		List<Map<String, String>> mapList = new ArrayList<Map<String, String>>(
				elements.size());
		for (String element : elements) {
			Map<String, String> anyoObject = new HashMap<String, String>(1);
			anyoObject.put(key, element);
			mapList.add(anyoObject);

		}
		return mapList;
	}

	/**
	 * Devuelve la lista de las fuentes de financiamiento para el tipo de
	 * proyecto y el año pasados como parámetros.
	 * 
	 * @param tipoProyecto
	 *            el tipo de proyecto ({@link TIPO_PROYECTOS#PREINVERSION} y
	 *            {@link TIPO_PROYECTOS#EJECUCION}
	 * @param anyo
	 *            el año.
	 * @return la lista de las distintas fuentes de financiamiento.
	 */
	@RequestMapping(value = "/getFuentes")
	@ResponseBody
	public Map<String, Object> getFuentes(
			@RequestParam("tipoProyecto") TIPO_PROYECTOS tipoProyecto,
			@RequestParam("anyo") String anyo) {
		Map<String, Object> result = new HashMap<String, Object>();
		try {
			List<String> fuentes = iniciativaInversionService
					.getFuentesDisponibles(tipoProyecto, anyo);
			if (tipoProyecto != TIPO_PROYECTOS.PREINVERSION) {
				fuentes.add(0, "Todos");
			}
			result.put(ROOT, simpleListToMapList("fuente", fuentes));
			result.put(RESULTS, fuentes.size());
			result.put(SUCCESS, true);
		} catch (Exception e) {
			LOG.error("Error obteniendo las fuentes de financiación", e);
			result.put(SUCCESS, false);
		}

		return result;
	}

	/**
	 * Devuelve la lista de las líneas financieras para el tipo de proyecto, el
	 * año y la fuente de financiamiento pasados como parámetros.
	 * 
	 * @param tipoProyecto
	 *            el tipo de proyecto ({@link TIPO_PROYECTOS#PREINVERSION} y
	 *            {@link TIPO_PROYECTOS#EJECUCION}
	 * @param anyo
	 *            el año.
	 * @param fuente
	 *            fuente financiera (parámetro opcional).
	 * @return la lista de las distintas líneas financieras.
	 */
	@RequestMapping(value = "/getLineasFinancieras")
	@ResponseBody
	public Map<String, Object> getLineasFinacieras(
			@RequestParam("tipoProyecto") TIPO_PROYECTOS tipoProyecto,
			@RequestParam("anyo") String anyo,
			@RequestParam(value = "fuente", required = false) String fuente) {
		Map<String, Object> result = new HashMap<String, Object>();
		try {
			List<String> lineas = new ArrayList<String>();
			lineas.add("Todas");
			if (fuente != null && !fuente.equals("Todos")) {
				lineas.addAll(iniciativaInversionService
						.getLineasFinancierasDisponibles(tipoProyecto, anyo,
								fuente));

			}
			result.put(ROOT, simpleListToMapList("linea", lineas));
			result.put(RESULTS, lineas.size());
			result.put(SUCCESS, true);

		} catch (Exception e) {
			LOG.error("Error recuperando las líneas finacieras", e);
			result.put(SUCCESS, false);
		}

		return result;
	}

	@RequestMapping(value = "/getSectores")
	@ResponseBody
	public Map<String, Object> getSectores(
			@RequestParam("tipoProyecto") TIPO_PROYECTOS tipoProyecto,
			@RequestParam("anyo") String anyo,
			@RequestParam(value = "fuente", required = false) String fuente,
			@RequestParam(value = "lineaFinanciera", required = false) String lineaFinanciera) {
		Map<String, Object> result = new HashMap<String, Object>();
		try {
			if (StringUtils.equalsIgnoreCase("todos", fuente)) {
				fuente = null;
			}
			if (StringUtils.equalsIgnoreCase("todas", lineaFinanciera)) {
				lineaFinanciera = null;
			}
			List<String> sectores = new ArrayList<String>();
			sectores.add("Todos");
			sectores.addAll(iniciativaInversionService.getSectoresDisponibles(
					tipoProyecto, anyo, fuente, lineaFinanciera));
			result.put(ROOT, simpleListToMapList("sector", sectores));
			result.put(RESULTS, sectores.size());
			result.put(SUCCESS, true);
		} catch (Exception e) {
			LOG.error("Error obtieniendo sectores", e);
			result.put(SUCCESS, false);
		}

		return result;
	}

	@RequestMapping(value = "/getNivelesTerritoriales")
	@ResponseBody
	public Map<String, Object> getNivelTerritorial() {
		Map<String, Object> result = Maps.newHashMap();
		try {
			List<NivelTerritorialDto> niveles = nivelTerritorialService
					.getNivelTerritorialByTipo(NivelTerritorialService.TIPO_NIVEL_MUNICIPAL);
			List<String> niv = new ArrayList<String>(34);
			niv.add("Regional");
			for (NivelTerritorialDto nivel : niveles) {
				niv.add(nivel.getName());
			}
			result.put(ROOT, simpleListToMapList("nivelTerritorial", niv));
			result.put(RESULTS, niv.size());
			result.put(SUCCESS, true);
		} catch (Exception e) {
			LOG.error("Error al obtener los niveles territoriales", e);
			result.put(SUCCESS, false);
		}
		return result;
	}

	@RequestMapping(value = "/getMontosGroupBy")
	@ResponseBody
	public Map<String, Object> getMontosGroupBy(
			@RequestParam(value = "tipoProyecto", required = false) TIPO_PROYECTOS tipoProyecto,
			@RequestParam(value = "anyo", required = false) String anyo,
			@RequestParam(value = "fuente", required = false) String fuente,
			@RequestParam(value = "lineaFinanciera", required = false) String lineaFinanciera,
			@RequestParam(value = "sector", required = false) String sector,
			@RequestParam(value = "nivelTerritorial", required = false) String nivelTerritorial,
			@RequestParam(value = "agruparPor", required = false) String agruparPor) {
		boolean shouldQuery = true;

		if (StringUtils.trimToNull(anyo) == null) {
			anyo = null;
		}
		if (StringUtils.trimToNull("fuente") == null
				|| StringUtils.equalsIgnoreCase("Todos", fuente)) {
			fuente = null;
		}
		if (StringUtils.trimToNull(lineaFinanciera) == null
				|| StringUtils.equalsIgnoreCase("Todas", lineaFinanciera)) {
			lineaFinanciera = null;
		}
		if (StringUtils.trimToNull(sector) == null
				|| StringUtils.equalsIgnoreCase("Todos", sector)) {
			sector = null;
		}
		if (StringUtils.trimToNull(nivelTerritorial) == null
				|| StringUtils.equalsIgnoreCase("regional", nivelTerritorial)) {
			nivelTerritorial = null;
		}
		if (StringUtils.trimToNull(agruparPor) == null) {
			nivelTerritorial = null;
		}

		if (tipoProyecto == null || anyo == null || agruparPor == null) {
			shouldQuery = false;
		}

		Map<String, Object> result = new HashMap<String, Object>();

		try {
			LOG.debug("Obteniendo montos");
			if (shouldQuery) {
				List<Map<String, Object>> montos = iniciativaInversionService
						.getMotosGroupBy(tipoProyecto, anyo, agruparPor,
								fuente, lineaFinanciera, sector,
								nivelTerritorial);
				result.put(ROOT, montos);
				result.put(RESULTS, montos.size());
				result.put(SUCCESS, true);

			} else {
				result.put(ROOT, new HashMap<String, String>(0));
				result.put(RESULTS, 0);
				result.put(SUCCESS, true);
			}

		} catch (Exception e) {
			LOG.error("Error al obtener los montos acumulados", e);
			result.put(SUCCESS, false);
		}

		return result;
	}

	@RequestMapping(value = "/getProyectosGeo")
	public void getProyectosGeo(
			@RequestParam(value = "tipoProyecto", required = false) TIPO_PROYECTOS tipoProyecto,
			@RequestParam(value = "anyo", required = false) String anyo,
			@RequestParam(value = "fuente", required = false) String fuente,
			@RequestParam(value = "lineaFinanciera", required = false) String lineaFinanciera,
			@RequestParam(value = "sector", required = false) String sector,
			@RequestParam(value = "nivelTerritorial", required = false) String nivelTerritorial,
			HttpServletResponse response) {
		boolean shouldQuery = true;

		if (StringUtils.trimToNull(anyo) == null) {
			anyo = null;
		}
		if (StringUtils.trimToNull("fuente") == null
				|| StringUtils.equalsIgnoreCase("Todos", fuente)) {
			fuente = null;
		}
		if (StringUtils.trimToNull(lineaFinanciera) == null
				|| StringUtils.equalsIgnoreCase("Todas", lineaFinanciera)) {
			lineaFinanciera = null;
		}
		if (StringUtils.trimToNull(sector) == null
				|| StringUtils.equalsIgnoreCase("Todos", sector)) {
			sector = null;
		}
		if (StringUtils.trimToNull(nivelTerritorial) == null
				|| StringUtils.equalsIgnoreCase("regional", nivelTerritorial)) {
			nivelTerritorial = null;
		}

		if (tipoProyecto == null) {
			shouldQuery = false;
		}

		Map<String, Object> result = new HashMap<String, Object>();
		MappingJacksonHttpMessageConverter jsonConverter = new MappingJacksonHttpMessageConverter();

		try {
			if (shouldQuery) {
				jsonConverter.setObjectMapper(new GeometryJsonMapper());
				List<Map<String, Object>> iniciativas = iniciativaInversionService
						.getProyectosGeo(tipoProyecto, anyo, fuente,
								lineaFinanciera, sector, nivelTerritorial);
				result.put(ROOT, iniciativas);
				result.put(RESULTS, iniciativas.size());
				result.put(SUCCESS, true);

			} else {
				result.put(ROOT, new HashMap<String, String>(0));
				result.put(RESULTS, 0);
				result.put(SUCCESS, true);
			}

		} catch (Exception e) {
			LOG.error("Error al obtener los montos acumulados", e);
			result.put(SUCCESS, false);
		}

		MediaType jsonMimeType = MediaType.APPLICATION_JSON;
		try {
			jsonConverter.write(result, jsonMimeType,
					new ServletServerHttpResponse(response));
		} catch (HttpMessageNotWritableException e) {
			LOG.error("Error generando JSON de respuesta", e);
		} catch (IOException e) {
			LOG.error("Error escribiendo JSON de respuesta", e);
		}

	}
}
