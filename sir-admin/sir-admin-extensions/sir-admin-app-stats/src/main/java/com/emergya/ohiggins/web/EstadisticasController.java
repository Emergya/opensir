/*
 * EstadisticasController.java
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
 * Authors::Jose Alfonso (mailto:jariera@emergya.com)
 */
package com.emergya.ohiggins.web;

import java.io.InputStream;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Set;

import javax.annotation.Resource;
import javax.validation.Valid;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.WebRequest;

import com.emergya.ohiggins.dto.EstadisticaFilterDto;
import com.emergya.ohiggins.dto.LayerDto;
import com.emergya.ohiggins.dto.LayerPublishRequestDto;
import com.emergya.ohiggins.dto.LayerResourceDto;
import com.emergya.ohiggins.security.ConstantesPermisos;
import com.emergya.ohiggins.security.OhigginsUserDetails;
import com.emergya.ohiggins.service.LayerPublishRequestService;
import com.emergya.ohiggins.service.LayerResourceService;
import com.emergya.ohiggins.service.LayerService;
import com.emergya.ohiggins.stats.dto.EstadisticaNodeDto;
import com.emergya.ohiggins.stats.dto.SerieDto;
import com.emergya.ohiggins.stats.service.StatsService;
import com.emergya.ohiggins.web.util.Utils;

/**
 * Controlador para estadisticas
 * 
 * @author <a href="mailto:jariera@emergya.com">jariera</a>
 */
@Controller
public class EstadisticasController extends AbstractController implements
		Serializable {
	/** Serials */
	private static final long serialVersionUID = 7348567000693006976L;

	/** Log */
	private static Log LOG = LogFactory.getLog(EstadisticasController.class);

	/** Modulo al que pertenece */
	public final static String MODULE = "general";

	/** Submodulo al que pertenece */
	public final static String SUB_MODULE = "estadisticas";

	// Formateo de fechas
	private static final String FORMATO_FECHA = "dd/MM/yyyy";

	public final static String AYER = "ayer";
	public final static String SEMANA = "semana";
	public final static String MES = "mes";
	public final static String ANYO = "anyo";

	public final static String REPOSITORIO = "Repositorio";
	public final static String INSTRUMENTOS = "Instrumentos";
	public final static String INVERSION = "Inversión";
	public final static String CANALES = "Canales";

	public final static String GLOBALS = "globals";
	public final static String GOALS = "goals";
	public final static String REQUESTED_LAYERS = "requested_layers";
	public final static String UPDATED_LAYERS = "updated_layers";
	public final static String REQUESTED_CHANNELS = "requested_channels";

	public final static String CERO = "0";
	public final static String SOURCE_URL_ESTADISTICAS_SIG = "source.url.estadisticas.sig";
	public final static String SOURCE_URL_ESTADISTICAS_GEN = "source.url.estadisticas.gen";
	public final static String URL_ESTADISTICAS_SIG = "urlEstadisticasSig";
	public final static String URL_ESTADISTICAS_GEN = "urlEstadisticasGen";

	public final static int DIFERENCIA_EN_DIAS = 7;

	private final static String INVALID_DATES_MSG = "El rango de fechas es incorrecto. Asegúrese que la fecha 'Desde' no es posterior a la fecha 'Hasta'";

	// TODO inject this service
	private StatsService estadisticasService = new StatsService();
	
	@Resource
	private LayerService layerService;
	
	@Resource
	private LayerResourceService layerResourceService;
	
	@Resource
	private LayerPublishRequestService layerPublishRequestService;

	/**
	 * Pagina inicial de la instituciones
	 * 
	 * @param model
	 * 
	 * @return "instituciones"
	 */
	@RequestMapping(value = "/admin/estadisticas")
	public String estadisticas(WebRequest webRequest, Model model) {

		// Accede el rol ADMIN, en otro caso pagina error
		decideIfIsUserInRole(ConstantesPermisos.ADMIN);

		Date hoy = new Date();

		rolUser(model);
		model.addAttribute("module", getActiveModule());
		model.addAttribute("submodule", getActiveSubModule());
		EstadisticaFilterDto e = new EstadisticaFilterDto();

		Calendar calendarFechaAnterior = Calendar.getInstance();
		calendarFechaAnterior.add(Calendar.DATE, -DIFERENCIA_EN_DIAS);
		Date fechaAyer = calendarFechaAnterior.getTime();

		e.setDesde(parseFechaFormat(fechaAyer));
		e.setHasta(parseFechaFormat(hoy));

		model.addAttribute("estadisticaFilterDto", e);

		Map<String, SerieDto> mapa = estadisticasService.findByPeriod(
				fechaAyer, hoy);
		model = cargaDatosEstadisticos(mapa, model);

		Properties p = EstadisticasController.getPropertiesApplication();
		String urlEstadisticasSig = (String) p.get(SOURCE_URL_ESTADISTICAS_SIG);
		model.addAttribute(URL_ESTADISTICAS_SIG, urlEstadisticasSig);

		String urlEstadisticasGen = (String) p.get(SOURCE_URL_ESTADISTICAS_GEN);
		model.addAttribute(URL_ESTADISTICAS_GEN, urlEstadisticasGen);

		return "admin/estadisticas/estadisticas";
	}

	/**
	 * Carga los datos estadisticos
	 * 
	 * @param mapa
	 * @param model
	 * @return
	 */
	private Model cargaDatosEstadisticos(Map<String, SerieDto> mapa, Model model) {

		if (mapa != null) {
			// Estadisticas globales: Cabecera de la pagina de estadisticas
			SerieDto globals = mapa.get(GLOBALS);
			Map<String, String> mglobals = (Map<String, String>) globals
					.getValues();

			// Estadisticas generales
			/*
			 * {unique_visits_week=0,
			 * visits_year=99,visits_today=0,visits_month=
			 * 99,unique_visits_month=99,
			 * unique_visits_year=0,unique_visits_today=0,visits_week=0}
			 */
			if (mglobals != null) {
				model.addAttribute(AYER, mglobals.get("visits_today"));
				model.addAttribute(SEMANA, mglobals.get("visits_week"));
				model.addAttribute(MES, mglobals.get("visits_month"));
				model.addAttribute(ANYO, mglobals.get("visits_year"));
			} else {
				model.addAttribute(AYER, CERO);
				model.addAttribute(SEMANA, CERO);
				model.addAttribute(MES, CERO);
				model.addAttribute(ANYO, CERO);
			}

			// Parte izquierda de la parte de estadisticas
			SerieDto goals = mapa.get(GOALS);
			Map<String, String> mgoals = (Map<String, String>) goals
					.getValues();
			if (mgoals != null && mgoals.size() > 0) {
				/*
				 * model.addAttribute(REPOSITORIO,
				 * CERO);model.addAttribute(INSTRUMENTOS, CERO);
				 * model.addAttribute(INVERSION,
				 * CERO);model.addAttribute(CANALES, CERO);
				 */

				// Iteramos sobre el conjunto y lo guardamos en la lista
				List<EstadisticaNodeDto> lgoals = new LinkedList<EstadisticaNodeDto>();
				EstadisticaNodeDto ne;

				for (Entry<String, String> s : mgoals.entrySet()) {
					ne = new EstadisticaNodeDto();
					ne.setNombre(s.getKey());
					ne.setValue(s.getValue());
					lgoals.add(ne);
				}

				model.addAttribute("lgoals", lgoals);
			} else {
				// Valores por defecto
				/*
				 * model.addAttribute(REPOSITORIO,
				 * 0);model.addAttribute(INSTRUMENTOS, 0);
				 * model.addAttribute(INERSION, 0);model.addAttribute(CANALES,
				 * 0);
				 */
				List<EstadisticaNodeDto> lgoals = new LinkedList<EstadisticaNodeDto>();
				lgoals.add(new EstadisticaNodeDto(REPOSITORIO,CERO));
				lgoals.add(new EstadisticaNodeDto(INSTRUMENTOS,CERO));
				lgoals.add(new EstadisticaNodeDto(INVERSION,CERO));
				lgoals.add(new EstadisticaNodeDto(CANALES,CERO));

				model.addAttribute("lgoals", lgoals);
			}

			// Parde derecha
			SerieDto channels = mapa.get(REQUESTED_CHANNELS);
			Map<String, String> mchannels = (Map<String, String>) channels
					.getValues();
			if (mchannels != null && mchannels.size() > 0) {
				// Iteramos sobre el conjunto y lo guardamos en la lista
				List<EstadisticaNodeDto> lchannels = new LinkedList<EstadisticaNodeDto>();				

				// Claves del conjunto
				for(Entry<String,String> channelData : mchannels.entrySet()){
					lchannels.add(new EstadisticaNodeDto(channelData.getKey(),channelData.getValue()));
				}
				model.addAttribute("lchannels", lchannels);
				
			} else {
				// valores por defecto, lista vacia
				List<EstadisticaNodeDto> lchannels = new LinkedList<EstadisticaNodeDto>();
				// EstadisticaNodeDto ne = new
				// EstadisticaNodeDto();lupdatelayers.add(ne);
				model.addAttribute("lchannels", lchannels);
			}

			SerieDto requestedlayers = mapa.get(REQUESTED_LAYERS);
			Map<String, String> mrequestedlayers = 
					(Map<String, String>) requestedlayers.getValues();
			if (mrequestedlayers != null && mrequestedlayers.size() > 0) {
				// Iteramos sobre el conjunto y lo guardamos en la lista
				List<EstadisticaNodeDto> lrequestedlayers = new LinkedList<EstadisticaNodeDto>();
				// Claves del conjunto
				for(Entry<String,String> layerData : mrequestedlayers.entrySet()){
					lrequestedlayers.add(new EstadisticaNodeDto(getLayerLabel(layerData.getKey()),layerData.getValue()));
				}

				model.addAttribute("lrequestedlayers", lrequestedlayers);
			} else {
				// valores por defecto, lista vacia
				List<EstadisticaNodeDto> lrequestedlayers = new LinkedList<EstadisticaNodeDto>();
				// EstadisticaNodeDto ne = new
				// EstadisticaNodeDto();lrequestedlayers.add(ne);
				model.addAttribute("lrequestedlayers", lrequestedlayers);
			}

			SerieDto updatelayers = mapa.get(UPDATED_LAYERS);
			Map<String, String> mupdatelayers = (Map<String, String>) updatelayers
					.getValues();
			if (mupdatelayers != null && mupdatelayers.size() > 0) {
				// Iteramos sobre el conjunto y lo guardamos en la lista
				List<EstadisticaNodeDto> lupdatelayers = new LinkedList<EstadisticaNodeDto>();
				
				for(Entry<String,String> updateLayerData : mupdatelayers.entrySet()){
					lupdatelayers.add(new EstadisticaNodeDto(getLayerLabel(updateLayerData.getKey()),updateLayerData.getValue()));
				}
				
				model.addAttribute("lupdatelayers", lupdatelayers);
			} else {
				// valores por defecto, lista vacia
				List<EstadisticaNodeDto> lupdatelayers = new LinkedList<EstadisticaNodeDto>();
				// EstadisticaNodeDto ne = new
				// EstadisticaNodeDto();lupdatelayers.add(ne);
				model.addAttribute("lupdatelayers", lupdatelayers);
			}

		}

		return model;
	}
	
	private String getLayerLabel(String geoserverName) {
		
		LayerDto layerDto = layerService.getByName(geoserverName);
		if(layerDto!=null) {
			return layerDto.getLayerLabel();
		}
		
		LayerResourceDto layerResourceDto = layerResourceService.getByTmpLayerName(geoserverName);
		if(layerResourceDto!=null) {
			return layerResourceDto.getOriginalFileName()+ " (Temporal)";
		}
		
		LayerPublishRequestDto publicationRequest = layerPublishRequestService.getByTmpLayerName(geoserverName);
		if(publicationRequest!=null) {
			return publicationRequest.getNombredeseado() +"(Solicitud)";			
		}
		
		return "Capa eliminada / no disponible";
	}

	@RequestMapping(value = "/admin/cartografico/estadisticas/consultaEstadistica")
	public String consultaEstadisticas(WebRequest webRequest, Model model,
			@Valid EstadisticaFilterDto estadisticaFilterDto,
			BindingResult result) {

		// Accede el rol ADMIN, en otro caso pagina error
		decideIfIsUserInRole(ConstantesPermisos.ADMIN);

		boolean errores = false;
		Date from = stringToDate(estadisticaFilterDto.getDesde(), FORMATO_FECHA);
		Date to = stringToDate(estadisticaFilterDto.getHasta(), FORMATO_FECHA);

		if (from == null || to == null || from.after(to) || to.before(from)) {
			// mostrar mensaje de error
			errores = true;
			model.addAttribute("rangoFechaIncorrecta", true);
			result.rejectValue("desde",
					"estadisticaFilterDto.dates.datesInvalid",
					INVALID_DATES_MSG);
		}
		if (errores == false) {
			Map<String, SerieDto> mapa = estadisticasService.findByPeriod(from,
					to);
			model = cargaDatosEstadisticos(mapa, model);
		}

		rolUser(model);
		model.addAttribute("module", getActiveModule());
		model.addAttribute("submodule", getActiveSubModule());
		model.addAttribute("estadisticaFilterDto", estadisticaFilterDto);

		Properties p = EstadisticasController.getPropertiesApplication();
		String urlEstadisticasSig = (String) p.get(SOURCE_URL_ESTADISTICAS_SIG);
		model.addAttribute(URL_ESTADISTICAS_SIG, urlEstadisticasSig);

		String urlEstadisticasGen = (String) p.get(SOURCE_URL_ESTADISTICAS_GEN);
		model.addAttribute(URL_ESTADISTICAS_GEN, urlEstadisticasGen);

		if (errores == true) {
			return "admin/estadisticas/estadisticas";// ?msg=fechaIncorrecta
		}

		return "admin/estadisticas/estadisticas";
	}

	protected void copyDefaultModel(boolean update, Model model) {

	}

	/**
	 * Referencia al enlace a la paginacion de instituciones
	 * 
	 * @return 'admin/instituciones'
	 */
	@Override
	protected String getDefaultPaginationUrl() {
		return "/admin/estadisticas";
	}

	@Override
	protected String getAllSubTabs() {
		return TabsByModule.GENERAL_SUBTABS;
	}

	@Override
	protected int getSelectedSubTab() {
		return 0;
	}

	@Override
	protected String getActiveModule() {
		return MODULE;
	}

	@Override
	protected String getActiveSubModule() {
		return SUB_MODULE;
	}

	/**
	 * 
	 * @param model
	 */
	private void rolUser(Model model) {
		model.addAttribute("IS_ADMIN",
				OhigginsUserDetails.userHasAuthority(ConstantesPermisos.ADMIN));
	}

	/**
	 * Get authentication
	 * 
	 * @param webRequest
	 *            .
	 * @return
	 */
	private Authentication getAuthentication(WebRequest webRequest) {
		SecurityContext context = (SecurityContext) webRequest.getAttribute(
				"SPRING_SECURITY_CONTEXT", RequestAttributes.SCOPE_SESSION);

		Authentication u = null;
		if (context != null && context.getAuthentication().isAuthenticated()
				&& !context.getAuthentication().getAuthorities().isEmpty()) {
			u = context.getAuthentication();
		}
		return u;
	}

	/**
	 * Formatea fecha en formato dd/mm/yyyy
	 * 
	 * @param f
	 * @return
	 */
	public String parseFechaFormat(Date f) {
		if (f == null)
			return "";

		SimpleDateFormat formato = new SimpleDateFormat(FORMATO_FECHA);
		return formato.format(f);
	}

	/**
	 * Convierte fecha en formato dd/mm/yyyy a Date
	 * 
	 * @param fecha
	 * @param formato
	 * @return
	 */
	public Date stringToDate(String fecha, String formato) {
		try {

			SimpleDateFormat format = new SimpleDateFormat(formato);

			return format.parse(fecha);
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * Obtener el fichero de propiedades
	 * 
	 * @return
	 */
	public static Properties getPropertiesApplication() {
		Properties p = new Properties();

		try {
			InputStream inStream = Utils.class
					.getResourceAsStream("/application.properties");
			p.load(inStream);
		} catch (Exception e) {
			LOG.info("Error al obtener las propiedades  : " + e);
		}

		return p;
	}
}
