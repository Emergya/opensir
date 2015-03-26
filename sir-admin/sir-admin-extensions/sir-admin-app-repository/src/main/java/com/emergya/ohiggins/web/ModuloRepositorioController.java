/*
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

import java.io.File;
import java.io.FileInputStream;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.displaytag.tags.TableTagParameters;
import org.displaytag.util.ParamEncoder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.FileCopyUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.ModelAndView;

import com.emergya.ohiggins.cmis.ASortOrder;
import com.emergya.ohiggins.cmis.CMISConnector;
import com.emergya.ohiggins.cmis.PublicacionCMISConnector;
import com.emergya.ohiggins.cmis.bean.ADocumentState;
import com.emergya.ohiggins.cmis.bean.EstudioFilter;
import com.emergya.ohiggins.cmis.bean.Publicacion;
import com.emergya.ohiggins.dto.AuthorityDto;
import com.emergya.ohiggins.dto.NivelTerritorialDto;
import com.emergya.ohiggins.dto.SectorDto;
import com.emergya.ohiggins.security.ConstantesPermisos;
import com.emergya.ohiggins.security.OhigginsUserDetails;
import com.emergya.ohiggins.service.InstitucionService;
import com.emergya.ohiggins.service.NivelTerritorialService;
import com.emergya.ohiggins.service.SectorService;
import com.google.common.base.Function;
import com.google.common.collect.Lists;

/**
 * @author jlrodriguez
 * 
 */
@Controller
@RequestMapping("/repositorioEstudios")
public class ModuloRepositorioController {
	private static final Log LOG = LogFactory
			.getLog(ModuloRepositorioController.class);
	private static final int ANYO_INICIAL = 1980;

	/**
	 * Indica el número de elementos por tabla
	 */
	private static final Integer PAGE_SIZE = 10;
	private static final Integer DEFAULT_ORDER = 1;
	private static final String DEFAULT_ORDER_NAME = "nombre";

	@Autowired
	private PublicacionCMISConnector cmisc = null;

	@Resource
	private NivelTerritorialService nivelTerritorialService;
	@Resource
	private SectorService sectorService;
	@Resource
	private InstitucionService institucionService;

	@RequestMapping(method = RequestMethod.GET)
	public ModelAndView buscador(HttpServletRequest request, Model model,
			WebRequest webRequest) {

		ModelAndView modelAndView = new ModelAndView(
				"moduloRepositorio/buscador");

		EstudioFilter filter = new EstudioFilter();
		model.addAttribute("estudioFilter", filter);

		populate(model);

		return modelAndView;
	}

	/**
	 * Busca estudios
	 * 
	 * @param EstudioFilter
	 * @param result
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/buscar")
	public ModelAndView buscar(EstudioFilter estudioFilter,
			BindingResult result, Model model, WebRequest request,
			@RequestParam(defaultValue = "10") int size,
			@RequestParam(defaultValue = "1") int page) {

		ModelAndView modelAndView = new ModelAndView(
				"moduloRepositorio/estudios");

		String tableId = "lista";

		Long from = getFrom(request, tableId, size);
		ASortOrder order = getAscDesc(request, tableId);
		String orderCol = getOrder(request, tableId);
		ADocumentState[] estados = new ADocumentState[] {
				ADocumentState.ACEPTADA, ADocumentState.LEIDA };
		String nivelTerritorial = estudioFilter.getNivelTerritorial();
		if (StringUtils.isNotBlank(nivelTerritorial)) {
			List<NivelTerritorialDto> niveles = nivelTerritorialService
					.findNivelAndChildren(nivelTerritorial);
			List<String> nivelesTerritoriales = Lists.transform(niveles,
					new Function<NivelTerritorialDto, String>() {
						public String apply(NivelTerritorialDto input) {
							String name = null;
							if (input != null) {
								name = input.getName();
							}
							return name;
						};
					});
			estudioFilter.setNivelesTerritoriales(nivelesTerritoriales
					.toArray(new String[] {}));

		}

		Long totalEstudios = cmisc.countFilteredDocuments(estudioFilter, estados);
		int total = totalEstudios == null ? 0 : totalEstudios.intValue();

		List<Publicacion> estudios = cmisc.getFilteredDocuments(estudioFilter,
				from, size, orderCol, order, estados);

		generateModel(model, modelAndView, from, total, estudios, size);

		rolUser(model);
		//populate(model);

		// return "gestionestudios/estudios";
		return modelAndView;

	}

	/**
	 * Download a document
	 * 
	 * @param webRequest
	 * @param request
	 * @param response
	 * @param model
	 * @param ident
	 * @return
	 */
	@RequestMapping(value = "/download/{ident}", method = RequestMethod.GET)
	public String getDocumento(WebRequest webRequest,
			HttpServletRequest request, HttpServletResponse response,
			Model model, @PathVariable("ident") String ident) {

		// Authentication u = getAuthentication(webRequest);

		try {
			Publicacion publicacion = cmisc.getDocument(ident);

			if (publicacion != null
					&& !(ADocumentState.ACEPTADA == publicacion.getEstado() || ADocumentState.LEIDA == publicacion
							.getEstado())) {
				throw new AccessDeniedException(
						"No tiene permiso para descargar este documento");
			}
			String nombre = publicacion.getName();
			File file = cmisc.downloadDocument(ident);
			String contentType = cmisc.getMimeType(ident);
			response.setContentType(contentType);
			if (contentType != null
					&& StringUtils.containsIgnoreCase(contentType, "pdf")) {
				nombre += ".pdf";
			} else {
				nombre = file.getName();
			}
			response.setHeader("Content-Disposition", "attachment; filename=\""
					+ nombre + "\"");

			FileCopyUtils.copy(new FileInputStream(file),
					response.getOutputStream());
		} catch (Exception t) {
			LOG.error("Error descargando " + ident, t);
		}

		return null;
	}

	/**
	 * Ver el detalle de la gestion de un estudio
	 * 
	 * @param webRequest
	 * @param request
	 * @param response
	 * @param model
	 * @param ident
	 * @return
	 */
	@RequestMapping(value = "/verDetalle/{ident}", method = RequestMethod.GET)
	public ModelAndView getVerDetalle(WebRequest webRequest,
			HttpServletRequest request, HttpServletResponse response,
			Model model, @PathVariable("ident") String ident) {

		ModelAndView modelAndView = null;
		modelAndView = new ModelAndView("moduloRepositorio/verDetalle");

		Publicacion p = cmisc.getDocument(ident);
		if (p != null
				&& !(ADocumentState.ACEPTADA == p.getEstado() || ADocumentState.LEIDA == p
						.getEstado())) {
			throw new AccessDeniedException(
					"No tiene permiso para consultar los detalles de este documento");
		}

		model.addAttribute("nombre", p.getName());
		model.addAttribute("institucion", p.getInstitucion());
		model.addAttribute("sector", p.getSector());
		model.addAttribute("anyo", p.getAnyo());
		model.addAttribute("nivelT", p.getNivelTerritorial());
		model.addAttribute("autor", p.getAutor());
		model.addAttribute("resumen", p.getResumen());
		model.addAttribute("identificador", p.getIdentifier());
		model.addAttribute("comentario",
				p.getComentario() != null ? p.getComentario() : new String());

		if (p != null && p.getComentario() == null) {
			p.setComentario(new String());
		}
		model.addAttribute("Publicacion", p);

		rolUser(model);

		return modelAndView;
	}

	@SuppressWarnings("unchecked")
	private void generateModel(Model model, ModelAndView modelAndView,
			Long from, int total, List<Publicacion> estudios, int pageSize) {
		// copyDefaultModel(model);
		modelAndView.addAllObjects((Map<String, ?>) model);
		modelAndView.addObject("list", estudios);
		modelAndView.addObject("size", total);
		modelAndView.addObject("pageSize", pageSize);
		modelAndView.addObject("currentPage", from);
	}

	/**
	 * 
	 * @param request
	 * @param tableId
	 * @return
	 */
	private Long getFrom(WebRequest request, String tableId, int pageSize) {
		// Elemento de inicio:
		String tmp1 = request.getParameter((new ParamEncoder(tableId)
				.encodeParameterName(TableTagParameters.PARAMETER_PAGE)));
		if (tmp1 == null)
			tmp1 = "1";
		Long from = (Long.parseLong(tmp1) - 1) * (long) pageSize;
		return from;
	}

	/**
	 * Ordern ascendente o descendente.
	 * 
	 * @param request
	 * @param tableId
	 * @return
	 */
	private ASortOrder getAscDesc(WebRequest request, String tableId) {
		// Orden de las columnas:
		String tmp = request.getParameter((new ParamEncoder(tableId)
				.encodeParameterName(TableTagParameters.PARAMETER_ORDER)));
		ASortOrder order = ASortOrder.ASC;
		if (tmp != null) {
			int orderIdx = (Integer.parseInt(tmp));
			order = ASortOrder.values()[orderIdx-1];
		}
		
		return order;
	}
	
	/**
	 * Ordenacion
	 * 
	 * @param request
	 * @param tableId
	 * @return
	 */
	private String getOrder(WebRequest request, String tableId) {
		// Columna para ordenar:
		String orderCol = request.getParameter((new ParamEncoder(tableId)
				.encodeParameterName(TableTagParameters.PARAMETER_SORT)));
		if (orderCol == null)
			orderCol = DEFAULT_ORDER_NAME;
		return orderCol;
	}

	/**
	 * Carga los combos necesarios para la vista
	 * 
	 * @param request
	 * @param model
	 */
	@SuppressWarnings("unchecked")
	private void populate(Model model) {

		// Sectores
		List<SectorDto> sectores = (List<SectorDto>) this.sectorService
				.getAll();

		// Nivel territorial
		List<NivelTerritorialDto> localities;
		localities = (List<NivelTerritorialDto>) nivelTerritorialService.getNivelTerritorialByTipo(
				NivelTerritorialService.TIPO_NIVEL_MUNICIPAL);
		
		List<NivelTerritorialDto> provinces;
		provinces = nivelTerritorialService.getNivelTerritorialByTipo(
				NivelTerritorialService.TIPO_NIVEL_PROVINCIAL);
		
		List<NivelTerritorialDto> regions;
		regions = nivelTerritorialService.getNivelTerritorialByTipo(
				NivelTerritorialService.TIPO_NIVEL_REGIONAL);

		// There should be only one!
		NivelTerritorialDto region = regions.get(0);
		
		// Años
		List<Integer> anyos = new LinkedList<Integer>();

		// Instituciones
		List<AuthorityDto> instituciones = institucionService
				.getAllOrderedByName();

		int anyoFinal = Calendar.getInstance().get(Calendar.YEAR);
		for (int i = ANYO_INICIAL; i <= anyoFinal; i++) {
			anyos.add(new Integer(i));
		}

		model.addAttribute("sectores", sectores);
		model.addAttribute("localities", localities);
		model.addAttribute("provinces", provinces);
		model.addAttribute("region", region);
		model.addAttribute("anyos", anyos);
		model.addAttribute("instituciones", instituciones);

		rolUser(model);

	}

	/**
	 * 
	 * @param model
	 */
	private void rolUser(Model model) {
		// model.addAttribute("IS_ADMIN",
		// DummyAuthentificationProvider.userHasAuthority(ADMIN));
		model.addAttribute("IS_ADMIN",
				OhigginsUserDetails.userHasAuthority(ConstantesPermisos.ADMIN));
	}

}
