/*
 * Copyright (C) 2013 Emergya
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 * 
 * Author: Luis Román Gutiérrez (lroman@emergya.com)
 */
package com.emergya.ohiggins.web;

import java.io.File;
import java.io.FileInputStream;
import java.security.Principal;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.FileCopyUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.context.request.WebRequest;

import com.emergya.ohiggins.service.InstitucionService;
import com.emergya.ohiggins.service.NivelTerritorialService;
import com.emergya.ohiggins.services.AnalyticsCategoryService;
import com.emergya.ohiggins.services.AnalyticsDataDownloadsService;
import com.emergya.ohiggins.services.AnalyticsDataTagService;
import com.emergya.ohiggins.cmis.ASortOrder;
import com.emergya.ohiggins.cmis.AnalyticsDataCMISConnector;
import com.emergya.ohiggins.cmis.bean.ADocumentState;
import com.emergya.ohiggins.cmis.bean.AnalyticsData;
import com.emergya.ohiggins.cmis.bean.AnalyticsDataFilter;
import com.emergya.ohiggins.dto.AnalyticsCategoryDto;
import com.emergya.ohiggins.dto.AnalyticsDataDto;
import com.emergya.ohiggins.dto.AnalyticsDataTagCountDto;
import com.emergya.ohiggins.dto.AnalyticsDataDownloadsDto;

/**
 * 
 * @author lroman
 */
@Controller
public class AnalyticsPublicController {
	/** The number of elements shown in the default views */
	private static final int MAX_DEFAULT_VIEW_COUNT = 6;
	private static final int PAGE_SIZE = 9;

	private static final ADocumentState[] PUBLIC_DATA_FILTER = new ADocumentState[] {
			ADocumentState.ACEPTADA,
			ADocumentState.LEIDA
	};

	@Resource
	private AnalyticsCategoryService categoryService;

	@Resource
	private AnalyticsDataCMISConnector cmisConnector;

	@Resource
	private AnalyticsControllerHelper analyticsControllerHelper;

	@Resource
	private AnalyticsDataTagService tagService;

	@Resource
	private AnalyticsDataDownloadsService downloadsService;

	@Resource
	private InstitucionService institutionService;

	@Resource
	private NivelTerritorialService geoContextService;

	@RequestMapping("/moduloAnaliticoEstadistico")
	public String viewPublicData(WebRequest webRequest, Model model) {
		return viewPublicData(webRequest, model, "");
	}

	@RequestMapping("/moduloAnaliticoEstadistico/{mode}")
	public String viewPublicData(WebRequest webRequest, Model model,
			@PathVariable("mode") String mode) {

		addCommonSearchAttrs(model);

		AnalyticsDataFilter filter = new AnalyticsDataFilter();

		if (mode.equals("masDescargados")) {
			List<AnalyticsDataDownloadsDto> mostDownloaded =
					downloadsService.getMostDownloaded(MAX_DEFAULT_VIEW_COUNT);

			List<AnalyticsData> data = new LinkedList<AnalyticsData>();
			for (AnalyticsDataDownloadsDto download : mostDownloaded) {
				AnalyticsData datum = cmisConnector.getDocument(download.getIdentifier());
				analyticsControllerHelper.addDatumNameFields(datum);
				data.add(datum);
			}

			model.addAttribute("data", data);

			model.addAttribute("downloadedBtnClass", "pressed");
			model.addAttribute("resultsLabel", "más descargados");
		} else {

			doSearch(null, filter, 1, MAX_DEFAULT_VIEW_COUNT, model);

			model.addAttribute("resultsLabel", "más recientes");
			model.addAttribute("recentBtnClass", "pressed");
		}

		return "analytics/publicDataView";
	}

	@RequestMapping("/moduloAnaliticoEstadistico/categoria/{categoryId}")
	public String searchByCategory(WebRequest webRequest, Model model,
			@PathVariable("categoryId") Long categoryId) {

		addCommonSearchAttrs(model);

		AnalyticsDataFilter filter = new AnalyticsDataFilter();

		filter.setCategoryId(categoryId);

		// We create a unique filter id so we can have several filters in the
		// same session
		// to support several tabs open at the same time, etc.
		String filterId = UUID.randomUUID().toString();

		webRequest.setAttribute(filterId, filter, WebRequest.SCOPE_SESSION);

		doSearch(filterId, filter, 1, PAGE_SIZE, model);

		AnalyticsCategoryDto category = (AnalyticsCategoryDto) categoryService.getById(categoryId);
		model.addAttribute("resultsLabel", String.format("dentro de la categoría «%s»",
				category.getName()));

		return "analytics/publicDataView";
	}
	
	@RequestMapping("/moduloAnaliticoEstadistico/busquedaAvanzada")
	public String customSearch(WebRequest webRequest, Model model,
			@Valid AnalyticsDataFilter filter, BindingResult result) {

		addCommonSearchAttrs(model);
		
		model.addAttribute("searchFilter", filter);
		model.addAttribute("searchFormCtrClass", "");

		// We create a unique filter id so we can have several filters in the
		// same session
		// to support several tabs open at the same time, etc.
		String filterId = UUID.randomUUID().toString();

		webRequest.setAttribute(filterId, filter, WebRequest.SCOPE_SESSION);

		doSearch(filterId, filter, 1, PAGE_SIZE, model);

		model.addAttribute("resultsLabel","de la búsqueda avanzada");

		return "analytics/publicDataView";
	}

	@RequestMapping("/moduloAnaliticoEstadistico/tag/{tagName}")
	public String searchByTag(WebRequest webRequest, Model model,
			@PathVariable("tagName") String tagName) {

		addCommonSearchAttrs(model);

		AnalyticsDataFilter filter = new AnalyticsDataFilter();

		filter.setTagName(tagName);

		// We create a unique filter id so we can have several filters in the
		// same session
		// to support several tabs open at the same time, etc.
		String filterId = UUID.randomUUID().toString();

		webRequest.setAttribute(filterId, filter, WebRequest.SCOPE_SESSION);

		doSearch(filterId, filter, 1, PAGE_SIZE, model);

		model.addAttribute("resultsLabel", String.format(
				"con la etiqueta «%s»", tagName));

		return "analytics/publicDataView";
	}

	private void doSearch(String filterId, AnalyticsDataFilter filter, int page, int pageSize, Model model) {
		Long count = cmisConnector.countFilteredDocuments(filter, PUBLIC_DATA_FILTER);

		// Pages are 1 based
		Long first = (long) ((page - 1) * pageSize);
		List<AnalyticsData> data = cmisConnector.getFilteredDocuments(
				filter, first, pageSize, "requestAnswerDate",
				ASortOrder.DESC, PUBLIC_DATA_FILTER);

		for (AnalyticsData datum : data) {
			analyticsControllerHelper.addDatumNameFields(datum);
		}

		if (filterId != null && count > (first + data.size()) + 1) {
			model.addAttribute("filterId", filterId);
			model.addAttribute("nextPage", page + 1);
		}

		model.addAttribute("data", data);
	}

	private void addCommonSearchAttrs(Model model) {
		model.addAttribute("submodule", "analitico");
		
		// So the search form container is hidden by default.
		model.addAttribute("searchFormCtrClass", "hidden");

		model.addAttribute("searchFilter", new AnalyticsDataFilter());
		model.addAttribute("categories", categoryService.getOrdered(0, Integer.MAX_VALUE, "name", true));
		model.addAttribute("institutions", institutionService.getAllOrderedByName());
		model.addAttribute("geoContexts", geoContextService.getZonesOrderByTypeDescNameAsc());

		List<AnalyticsDataTagCountDto> tags = tagService.getTagCounts();

		model.addAttribute("tags", tags);
	}

	@RequestMapping("/popup/moduloAnaliticoEstadistico/verDetalle/{identifier}")
	public String viewDatumDetails(WebRequest webRequest, Model model,
			@PathVariable("identifier") String identifier) {

		AnalyticsData datum = cmisConnector.getDocument(identifier);

		analyticsControllerHelper.addDatumNameFields(datum);

		model.addAttribute("datum", datum);

		return "analytics/viewDetailsPopup";
	}

	@RequestMapping(value = "/popup/moduloAnaliticoEstadistico/cargaPagina/{filterId}/{pageNum}")
	public String loadNextPage(
			WebRequest request, Model model,
			@PathVariable("filterId") String filterId,
			@PathVariable("pageNum") int pageNum) {

		// We retrieve the filter from the session.
		AnalyticsDataFilter filter = (AnalyticsDataFilter) request.getAttribute(filterId, WebRequest.SCOPE_SESSION);

		doSearch(filterId, filter, pageNum, PAGE_SIZE, model);

		return "analytics/dataGrid";
	}

	@RequestMapping(value = "/moduloAnaliticoEstadistico/descarga/{ident}", method = RequestMethod.GET)
	public String downloadDocument(
			HttpServletRequest request, HttpServletResponse response,
			Model model, @PathVariable("ident") String ident) {
		
		// We check if the document is published.
		AnalyticsData datum = cmisConnector.getDocument(ident);	
		

		AnalyticsDataDownloadsDto downloads = downloadsService.getByIdentifier(ident);
		if (downloads == null) {
			// We create a new entity;
			downloads = new AnalyticsDataDownloadsDto();
			downloads.setDownloads(1L);
			downloads.setIdentifier(ident);
			downloadsService.create(downloads);
		} else {
			downloads.setDownloads(downloads.getDownloads() + 1);
			downloadsService.update(downloads);
		}

		try {
			File file = cmisConnector.downloadDocument(ident);
			response.setHeader("Content-Disposition", "attachment; filename=\""
					+ file.getName() + "\"");
			response.setContentType(cmisConnector.getMimeType(ident));

			FileCopyUtils.copy(new FileInputStream(file),
					response.getOutputStream());

			file.delete();
		} catch (Exception t) {
			Logger.getLogger(this.getClass()).error("Error descargando " + ident, t);
		}

		return null;
	}
}
