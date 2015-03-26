package com.emergya.ohiggins.web;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.SortedSet;

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
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.ModelAndView;

import com.emergya.ohiggins.service.AricaInversionService;
import com.emergya.ohiggins.web.util.geo.GeometryJsonMapper;
import com.google.inject.internal.Maps;

@Controller
@RequestMapping("/chileIndicaInversion")
public class ChileIndicaInversionController {

	private final static Log LOG = LogFactory
			.getLog(ChileIndicaInversionController.class);
	private final static String RESULTS = "results";
	private final static String ROOT = "data";
	private final static String SUCCESS = "success";

	@Autowired
	private AricaInversionService inversionDataService;

	@RequestMapping(value = "/getAnyos")
	@ResponseBody
	public Map<String, Object> getAnyos() {

		Map<String, Object> result = new HashMap<String, Object>();
		try {
			List<Integer> anyos = inversionDataService.getAnyosDisponibles();

			List<String> items = new ArrayList<String>();
			items.add("Todos");
			for (Integer e : anyos)
				items.add(e.toString());

			List<Map<String, String>> anyosJson = simpleListToMapList("anyo", items);
			result.put(ROOT, anyosJson);
			result.put(RESULTS, items.size());
			result.put(SUCCESS, true);
		} catch (Exception e) {
			LOG.error(
					"Error recuperando los distintos años de las iniciativas de inversión ", e);
			result.put(SUCCESS, false);
		}

		return result;
	}

	@RequestMapping(value = "/getFuentes")
	@ResponseBody
	public Map<String, Object> getFuentes() {
		Map<String, Object> result = new HashMap<String, Object>();
		try {

			List<String> fuentes = inversionDataService.getFuentesDisponibles();

			result.put(ROOT, simpleListToMapList("nombreFinanciamiento", fuentes));
			result.put(RESULTS, fuentes.size());
			result.put(SUCCESS, true);
		} catch (Exception e) {
			LOG.error("Error obteniendo las fuentes de financiación", e);
			result.put(SUCCESS, false);
		}

		return result;
	}

	@RequestMapping(value = "/getItemsPresupuestarios")
	@ResponseBody
	public Map<String, Object> getItemsPresupuestarios() {

		Map<String, Object> result = new HashMap<String, Object>();
		try {
			List<String> items = new ArrayList<String>();
			items.add("Todas");

			items.addAll(inversionDataService.getItemPresupuestarioOrderAsc());

			result.put(ROOT, simpleListToMapList("itemPresupuestario", items));
			result.put(RESULTS, items.size());
			result.put(SUCCESS, true);

		} catch (Exception e) {
			LOG.error("Error recuperando los items presupuestarios", e);
			result.put(SUCCESS, false);
		}

		return result;
	}

	@RequestMapping(value = "/getNivelesTerritoriales")
	@ResponseBody
	public Map<String, Object> getNivelTerritorial() {
		Map<String, Object> result = Maps.newHashMap();
		try {
			SortedSet<String> niveles = inversionDataService.getNivelesTerritoriales();

			List<String> niv = new ArrayList<String>(niveles.size());
			niv.add("Regional");
			for (String nivel : niveles) {
				niv.add(nivel);
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

	private List<Map<String, Integer>> simpleListIntegerToMapList(String key,
			List<Integer> elements) {
		List<Map<String, Integer>> mapList = new ArrayList<Map<String, Integer>>(
				elements.size());
		for (Integer element : elements) {
			Map<String, Integer> anyoObject = new HashMap<String, Integer>(1);
			anyoObject.put(key, element);
			mapList.add(anyoObject);

		}
		return mapList;
	}

	@RequestMapping(value = "/getMontosGroupBy")
	@ResponseBody
	public Map<String, Object> getMontosGroupBy(
			@RequestParam(value = "financiamiento", required = false) String fuente,
			@RequestParam(value = "anyo", required = false) String anyo,
			@RequestParam(value = "itemPresupuestario", required = false) String itemPresupuestario,
			@RequestParam(value = "nivelTerritorial", required = false) String nivelTerritorial,
			@RequestParam(value = "agruparPor", required = false) String agruparPor) {
		boolean shouldQuery = true;

        anyo = StringUtils.trimToNull(anyo);
		
        fuente = StringUtils.trimToNull(fuente);
		if (StringUtils.equalsIgnoreCase("Todos", fuente)) {
			fuente = null;
		}
        
        itemPresupuestario = StringUtils.trimToNull(itemPresupuestario);
		if (StringUtils.equalsIgnoreCase("Todas", itemPresupuestario)) {
			itemPresupuestario = null;
		}

        nivelTerritorial = StringUtils.trimToNull(nivelTerritorial);
		if (StringUtils.equalsIgnoreCase("regional", nivelTerritorial)) {
			nivelTerritorial = null;
		}
        
        
        agruparPor = StringUtils.trimToNull(agruparPor);	

		Map<String, Object> result = new HashMap<String, Object>();

		try {
			LOG.debug("Obteniendo montos");
			if (shouldQuery) {
				List<Map<String, Object>> montos = inversionDataService
						.getMontosGroupBy(anyo, agruparPor,
								fuente, itemPresupuestario,
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
			@RequestParam(value = "financiamiento", required = false) String fuente,
			@RequestParam(value = "anyo", required = false) String anyo,
			@RequestParam(value = "itemPresupuestario", required = false) String itemPresupuestario,
			@RequestParam(value = "nivelTerritorial", required = false) String nivelTerritorial,
			HttpServletResponse response) {

        anyo = StringUtils.trimToNull(anyo);
		
        fuente = StringUtils.trimToNull(fuente);
		if (StringUtils.equalsIgnoreCase("Todos", fuente)) {
			fuente = null;
		}
        
        itemPresupuestario =  StringUtils.trimToNull(itemPresupuestario);
		if ( StringUtils.equalsIgnoreCase("Todas", itemPresupuestario)) {
			itemPresupuestario = null;
		}

        StringUtils.trimToNull(nivelTerritorial);
		if (StringUtils.equalsIgnoreCase("regional", nivelTerritorial)) {
			nivelTerritorial = null;
		}

		Map<String, Object> result = new HashMap<String, Object>();
		MappingJacksonHttpMessageConverter jsonConverter = new MappingJacksonHttpMessageConverter();

		try {
			jsonConverter.setObjectMapper(new GeometryJsonMapper());
			List<Map<String, Object>> iniciativas = inversionDataService
					.getProyectosGeo(anyo, fuente, itemPresupuestario, nivelTerritorial);
			result.put(ROOT, iniciativas);
			result.put(RESULTS, iniciativas.size());
			result.put(SUCCESS, true);
		} catch (Exception e) {
			LOG.error("Error al obtener los proyectos georeferenciados", e);
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

	@RequestMapping("/fichaPopup")
	public ModelAndView getFichaPopup(
			@RequestParam("codBip") String codBip,
			@RequestParam("etapa") String etapa,
			@RequestParam("anyo") String anyo,
			WebRequest webRequest) {

		ModelAndView modelAndView = new ModelAndView();

		Map<String, String> info = inversionDataService.getInfoFicha(
				codBip, etapa, anyo);

		modelAndView.addObject("info", info);

		modelAndView.setViewName("chileIndicaInversion/fichaPopup");

		return modelAndView;
	}

	@RequestMapping("/fichaImprimir")
	public ModelAndView getFichaPrint(
			@RequestParam("codBip") String codBip,
			@RequestParam("etapa") String etapa,
			@RequestParam("anyo") String anyo,
			WebRequest webRequest) {

		ModelAndView modelAndView = new ModelAndView();

		Map<String, String> info = inversionDataService.getInfoFicha(
				codBip, etapa, anyo);

		modelAndView.addObject("info", info);

		modelAndView.setViewName("chileIndicaInversion/fichaImpresion");

		return modelAndView;
	}
}
