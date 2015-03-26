/*
 * PermisoController.java
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

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

import javax.annotation.Resource;
import javax.validation.Valid;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.emergya.ohiggins.dto.AuthorityTypeDto;
import com.emergya.ohiggins.dto.PermisoDto;
import com.emergya.ohiggins.security.ConstantesPermisos;
import com.emergya.ohiggins.service.AuthorityTypeService;
import com.emergya.ohiggins.service.PermisoService;

/**
 * Simple index page controller for user admin
 * 
 * @author <a href="mailto:jariera@emergya.com">jariera</a>
 */
@Controller
public class PermisoController extends AbstractController implements
		Serializable {

	/** Serials */
	private static final long serialVersionUID = -8414751999110639712L;

	/** Log */
	private static final Log LOG = LogFactory.getLog(PermisoController.class);

	/** Modulo al que pertenece */
	public final static String MODULE = "general";
	/** Submodulo al que pertenece */
	public final static String SUB_MODULE = "permisos";

	@Resource
	private AuthorityTypeService authorityTypeService;

	@Resource
	private PermisoService permisoService;

	private static final String LIST_TIPOS_AUTORIDAD = "tiposAutoridad";
	private static final String LIST_PERMISOS = "permisos";
	private static final String URL_PERMISOS = "admin/permisos/permisos";

	private static final String SEPARADOR = "/";

	/**
	 * Pagina para mostrar los permisos por institucion Entra en la vista para
	 * mostrar los permisos por institucion que han sido creados
	 * 
	 * @param model
	 * 
	 * @return "nuevaInstitucion"
	 */
	@RequestMapping(value = "/admin/permisos", method = RequestMethod.GET)
	public String permisos(Model model) {
		// Accede el rol ADMIN, en otro caso pagina error
		decideIfIsUserInRole(ConstantesPermisos.ADMIN);

		calculatePagination(model);

		PermisoDto perm = new PermisoDto();
		List<AuthorityTypeDto> tiposAutoridad = (List<AuthorityTypeDto>) authorityTypeService
				.getAllOrdered();

		cargaListasVista(model, tiposAutoridad);

		// Carga la matriz con los datos
		perm.setSeleccionados(cargaMatrizTiposAutoridadByPermisos(tiposAutoridad));

		model.addAttribute("permisoDto", perm);
		return URL_PERMISOS;
	}

	/**
	 * Carga las listas necesarias para la vista
	 * 
	 * @param model
	 */
	@SuppressWarnings("unchecked")
	private void cargaListasVista(Model model,
			List<AuthorityTypeDto> tiposAutoridad) {
		// Tipos autoridad
		model.addAttribute(LIST_TIPOS_AUTORIDAD, tiposAutoridad);

		// Permisos
		List<PermisoDto> permisos = (List<PermisoDto>) permisoService.getOrdered(0, Integer.MAX_VALUE , "name", true);
		model.addAttribute(LIST_PERMISOS, permisos);
	}

	/**
	 * Carga la matriz tipos autoridad por permisos
	 * 
	 * @param tiposAutoridad
	 * @return Array de string con los valores seleccionados
	 */
	private String[] cargaMatrizTiposAutoridadByPermisos(
			List<AuthorityTypeDto> tiposAutoridad) {
		// Obtener los valores que se han guardado
		// Para ello, iteramos sobre la lista de tipos de autoridad, y vemos si
		// tiene permisos asociados
		// creando la tupla (idTypeAutoridad / idPermiso)

		List<String> lselec = new LinkedList<String>();
		String[] sel = new String[0];

		if (tiposAutoridad != null && tiposAutoridad.size() > 0) {

			String idTipoAutoridad;
			String idPermiso;

			for (AuthorityTypeDto dto : tiposAutoridad) {
				if (dto != null && dto.getId() != null
						&& dto.getPermisos() != null
						&& dto.getPermisos().size() > 0) {
					idTipoAutoridad = dto.getId().toString();

					for (PermisoDto perDto : dto.getPermisos()) {
						if (perDto != null && perDto.getId() != null) {
							idPermiso = perDto.getId().toString();
							lselec.add(idTipoAutoridad + SEPARADOR + idPermiso);
						}
					}
				}
			}

			if (lselec != null) {
				sel = (String[]) lselec.toArray(sel);
			}
		}

		return sel;
	}

	/**
	 * Guarda los nuevos permisos por institucion
	 * 
	 * @param PermisoDto
	 * @param result
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/admin/salvarPermiso", method = RequestMethod.POST)
	public String savePermisos(@Valid PermisoDto permisoDto,
			BindingResult result, Model model) {
		// Accede el rol ADMIN, en otro caso pagina error
		decideIfIsUserInRole(ConstantesPermisos.ADMIN);

		// Si todo es correcto, guardamos los cambios

		// 1:Recoger los valores para cada tipo de institucion
		String[] par;
		String idTipoInstitucion;
		String idPermiso;

		AuthorityTypeDto tipoInstitucionDto;
		PermisoDto permiDto;
		List<PermisoDto> lpermisos = new LinkedList<PermisoDto>();

		// Borramos todos
		deleteAllPermisosTipoInstitucion();

		if (permisoDto.getSeleccionados() != null
				&& permisoDto.getSeleccionados().length > 0) {
			String[] seleccionados = permisoDto.getSeleccionados();

			// En seleccionados tenemos la tupla de valores marcadas en el
			// formulario
			for (int i = 0; i < seleccionados.length; i++) {
				par = seleccionados[i].split(SEPARADOR); // Tenemos la tupla:
															// idTipoInstitucion
															// / idPermiso
				idTipoInstitucion = par[0];
				idPermiso = par[1];

				// Tipo Institucion
				tipoInstitucionDto = authorityTypeService.getById(new Long(
						idTipoInstitucion));
				lpermisos = tipoInstitucionDto.getPermisos();
				if (lpermisos == null) {
					lpermisos = new LinkedList<PermisoDto>();
				}

				// Permiso
				permiDto = permisoService.getById(new Long(idPermiso));
				lpermisos.add(permiDto);

				// Actualizamos el tipo de institucion
				tipoInstitucionDto.setPermisos(lpermisos);
				authorityTypeService.update(tipoInstitucionDto);

			}
		}

		// FIXME:Mensaje de exito
		return "redirect:/admin/permisos?msg=success";
	}

	/**
	 * Elimina todos los permisos de los tipos institucion
	 */
	private void deleteAllPermisosTipoInstitucion() {
		List<AuthorityTypeDto> tiposAutoridad = (List<AuthorityTypeDto>) authorityTypeService
				.getAllOrdered();
		for (int i = 0; i < tiposAutoridad.size(); i++) {
			tiposAutoridad.get(i).setPermisos(new LinkedList<PermisoDto>());
			authorityTypeService.update(tiposAutoridad.get(i));
		}
	}

	/**
	 * Incluye en el modelo los parametros por defecto de la administracion de
	 * permisos
	 * 
	 * @param model
	 * @param update
	 *            si actualiza los parametros actualizables
	 */
	protected void copyDefaultModel(boolean update, Model model) {
		// calculatePagination(model);
		// model.addAttribute("instituciones", instituciones);
	}

	/**
	 * Referencia al enlace a la paginacion de permisos
	 * 
	 * @return 'admin/permisos'
	 */
	@Override
	protected String getDefaultPaginationUrl() {
		return "/admin/permisos";
	}

	@Override
	protected String getAllSubTabs() {
		return TabsByModule.GENERAL_SUBTABS;
	}

	@Override
	protected int getSelectedSubTab() {
		// indice 0 dentro de GENERAL_SUBTABS
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

}
