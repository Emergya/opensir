/*
 * FaqController.java
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
 * Authors:: Alejandro Díaz Torres (mailto:adiaz@emergya.com)
 */
package com.emergya.ohiggins.web;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import javax.annotation.Resource;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Value;


import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.emergya.ohiggins.dto.FaqDto;
import com.emergya.ohiggins.security.ConstantesPermisos;
import com.emergya.ohiggins.service.FaqService;
import com.emergya.ohiggins.web.util.PaginatedList;

/**
 * Simple index page controller for user admin
 * 
 * @author <a href="mailto:jariera@emergya.com">jariera</a>
 */
@Controller
public class FaqController extends AbstractController {

	private static final long serialVersionUID = -8430485443810787152L;

	private static Log LOG = LogFactory.getLog(FaqController.class);

	public final static String MODULE = "general";
	public final static String SUB_MODULE = "faq";
	private List<FaqDto> faqs = null;
	private static final String MODULOS_FAQ = "modulosfaq";

	private static final String CAMPO_OBLIGATORIO = "No puede estar vacio";

	@Value("#{webProperties['faq.modules']}")
	private String[] faqModules;
	
	@Resource
	private FaqService faqService;

	/**
	 * Pagina faqs para admin
	 * 
	 * @param model
	 * 
	 * @return "faqs"
	 */
	@RequestMapping(value = "/admin/faqs", method = RequestMethod.GET)
	@SuppressWarnings({ "rawtypes" })
	public String getFaqs(@RequestParam(defaultValue = "10") int size,
			@RequestParam(defaultValue = "1") int page, Model model) {

		// Accede el rol ADMIN, en otro caso pagina error
		decideIfIsUserInRole(ConstantesPermisos.ADMIN);

		if (LOG.isInfoEnabled()) {
			LOG.info("Consultando las faqs del sistema. Tamaño de página="
					+ size + ". Pagina=" + page);
		}
		calculatePagination(model);

		Long numElements = faqService.getResults();

		List faqResult = (List<FaqDto>) faqService.getFromToOrderBy((page - 1)
				* size, page * size, FaqDto.TITULO_PROPERTY);

		PaginatedList result = new PaginatedList(faqResult,
				(int) numElements.longValue(), size, page);
		model.addAttribute("listaFaqs", result);

		if (LOG.isInfoEnabled()) {
			LOG.info("Redirigiendo a la vista faq/faqs");
		}

		return "admin/faq/faqs";
	}

	/**
	 * Pagina para la creacion de un nuevo FAQ Entra en la vista nuevoFaq, solo
	 * carga la vista
	 * 
	 * @param model
	 * 
	 * @return "nuevaInstitucion"
	 */
	@RequestMapping(value = "/admin/nuevoFaq", method = RequestMethod.GET)
	public String createFaq(Model model) {
		// Accede el rol ADMIN, en otro caso pagina error
		decideIfIsUserInRole(ConstantesPermisos.ADMIN);

		calculatePagination(model);
		FaqDto faq = new FaqDto();
		faq.setId(null);
		faq.setHabilitada(Boolean.TRUE);
		model.addAttribute("faqDto", faq);

		cargaListasVista(model);

		return "admin/faq/nuevoFaq";
	}

	/**
	 * Carga las listas necesarios para la vista
	 * 
	 * @param model
	 */
	private void cargaListasVista(Model model) {
		// Tipos de autoridad
		List<String> modulos = new LinkedList<String>();
		
		//En caso de error, comportamiento antiguo
		if (this.faqModules==null){
			modulos.add("Cartográfico");
			modulos.add("Administración");
			modulos.add("Todos");
		}else{
			for (int i=0;i<this.faqModules.length;i++)
				modulos.add(this.faqModules[i]);
		}
		model.addAttribute(MODULOS_FAQ, modulos);
	}

	/**
	 * Guarda una nueva faq
	 * 
	 * @param institucionDto
	 * @param result
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/admin/salvarFaq", method = RequestMethod.POST)
	public String saveFaq(@Valid FaqDto faqDto, BindingResult result,
			Model model) {
		// Accede el rol ADMIN, en otro caso pagina error
		decideIfIsUserInRole(ConstantesPermisos.ADMIN);
		
		if (result.hasErrors()) {
			cargaListasVista(model);
			if (faqDto.getId() == null) {
				return "admin/faq/nuevoFaq";
			} else {
				return "admin/faq/editarfaq";
			}
		} else {
			if ((faqDto.getRespuesta() == null || (faqDto.getRespuesta() != null && ""
					.equals(faqDto.getRespuesta().trim())))) {
				FieldError error = new FieldError("respuesta", "respuesta",
						CAMPO_OBLIGATORIO);
				result.addError(error);

				if (faqDto.getId() == null) {
					return "admin/faq/nuevoFaq";
				} else {
					return "admin/faq/editarfaq";
				}
			}
			
		}

		// guardamos la faq
		Date hoy = new Date();

		if (faqDto.getId() != null) {// actualizar
			faqDto.setFechaActualizacion(hoy);
			faqService.update(faqDto);
		} else { // Crear
			faqDto.setFechaCreacion(hoy);
			faqDto.setFechaActualizacion(hoy);
			faqService.create(faqDto);
		}

		return "redirect:/admin/faqs?msg=success";
	}

	/**
	 * Elimina una faq Solo se pueden borrar faq
	 * 
	 * @param id
	 *            Identificador de la faq
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/admin/borrarFaq/{id}")
	public String deleteFaq(@PathVariable long id, Model model) {
		// Accede el rol ADMIN, en otro caso pagina error
		decideIfIsUserInRole(ConstantesPermisos.ADMIN);

		FaqDto faq = (FaqDto) faqService.getFaqById(id);

		faqService.delete(faq);

		return "redirect:/admin/faqs?msg=success";
	}

	/**
	 * Pagina para cargar los datos de un faq, para su posterior modificacion
	 * 
	 * @param id
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/admin/editarFaq/{id}")
	public String modifyInstitucion(@PathVariable long id, Model model) {
		// Accede el rol ADMIN, en otro caso pagina error
		decideIfIsUserInRole(ConstantesPermisos.ADMIN);

		FaqDto dto = (FaqDto) faqService.getFaqById(id);

		model.addAttribute("faqDto", dto);

		cargaListasVista(model);
		calculatePagination(model);
		return "admin/faq/editarfaq";
	}

	@Override
	protected void copyDefaultModel(boolean update, Model model) {
		calculatePagination(model);
		model.addAttribute("faq", faqs);
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
	protected String getDefaultPaginationUrl() {
		return "/faq/faqs";
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
	 * Pagina faqs para el resto de usuarios
	 * 
	 * @param model
	 * 
	 * @return "faqs"
	 */
	@RequestMapping(value = "/faq/faqs", method = RequestMethod.GET)
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public String getFaqsUser(@RequestParam(defaultValue = "10") int size,
			@RequestParam(defaultValue = "1") int page, Model model) {

		if (LOG.isInfoEnabled()) {
			LOG.info("Consultando las faqs del sistema para users. Tamaño de página="
					+ size + ". Pagina=" + page);
		}
		calculatePagination(model);

		/*
		 * Long numElements = faqService.getResults();
		 * 
		 * List faqResult = (List<FaqDto>) faqService.getFromToOrderBy( (page -
		 * 1) * size, page * size, FaqDto.TITULO_PROPERTY);
		 * 
		 * PaginatedList result = new PaginatedList(faqResult, (int)
		 * numElements.longValue(), size, page);
		 */
		//List result = (List<FaqDto>) faqService.getFromToOrderBy(0,	Integer.MAX_VALUE, FaqDto.TITULO_PROPERTY);
		// List result = faqService.getAll();
		
		List result = faqService.getModuleFaqs("todos");
		          
		model.addAttribute("listaFaqs", result);

		if (LOG.isInfoEnabled()) {
			LOG.info("Redirigiendo a la vista faq/faqs");
		}

		return "faq/faqs";
	}
	
	
	@RequestMapping(value = "/faq/faqs/{section}", method = RequestMethod.GET)
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public String getFaqsUser(@RequestParam(defaultValue = "10") int size,
			@RequestParam(defaultValue = "1") int page,
			@PathVariable("section") String section,Model model) {

		if (LOG.isInfoEnabled()) {
			LOG.info("Consultando las faqs del sistema para users. Tamaño de página="
					+ size + ". Pagina=" + page);
		}
		calculatePagination(model);

		//List<FaqDto> result = (List<FaqDto>) faqService.getFromToOrderBy(0,Integer.MAX_VALUE, FaqDto.TITULO_PROPERTY);
		//List result = faqService.getAll();
		List result = faqService.getModuleFaqs(section);
		if (!section.equals("todos")){
			List result2 = faqService.getModuleFaqs("todos");
			result.addAll(result2);
		}
		          
		model.addAttribute("listaFaqs", result);

		if (LOG.isInfoEnabled()) {
			LOG.info("Redirigiendo a la vista faq/faqs");
		}

		return "faq/faqs";
		}
	}
