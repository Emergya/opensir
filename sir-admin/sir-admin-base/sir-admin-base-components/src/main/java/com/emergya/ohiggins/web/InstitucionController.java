/*
 * InstitucionController.java
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
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import javax.validation.Valid;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.context.request.WebRequest;

import com.emergya.chileIndica.dto.ChileIndicaInstitucionDto;
import com.emergya.chileIndica.model.ChileIndicaInstitucionEntity;
import com.emergya.chileIndica.service.ChileIndicaInstitucionService;
import com.emergya.ohiggins.dto.AuthorityDto;
import com.emergya.ohiggins.dto.AuthorityTypeDto;
import com.emergya.ohiggins.dto.NivelTerritorialDto;
import com.emergya.ohiggins.security.ConstantesPermisos;
import com.emergya.ohiggins.service.AuthorityTypeService;
import com.emergya.ohiggins.service.InstitucionService;
import com.emergya.ohiggins.service.NivelTerritorialService;
import com.emergya.ohiggins.web.bean.RegionBean;
import com.emergya.ohiggins.web.util.PaginatedList;
import com.emergya.ohiggins.web.validators.AuthorityDtoValidator;
import com.emergya.persistenceGeo.service.GeoserverService;
import com.emergya.persistenceGeo.utils.GeoserverUtils;

/**
 * Simple index page controller for user admin
 * 
 * @author <a href="mailto:jariera@emergya.com">jariera</a>
 */
@Controller
public class InstitucionController extends AbstractController implements
		Serializable {

	/** Serials */
	private static final long serialVersionUID = 7348567389693916976L;

	/** Log */
	private static Log LOG = LogFactory.getLog(InstitucionController.class);

	/** Id del tipo municipal */
	private static final String ID_TIPO_MUNICIPAL = "1";

	/** Ctes definidas */
	private static final String URL_LISTADO_INSTITUCIONES = "admin/instituciones/instituciones";
	private static final String URL_NUEVA_INSTITUCION = "admin/instituciones/nuevaInstitucion";
	private static final String URL_EDITAR_INSTITUCION = "admin/instituciones/editarInstitucion";
	private static final String LIST_TIPOS_AUTORIDAD = "tiposAutoridad";
	private static final String LIST_AMBITO_TERRITORIAL = "ambitoTerritorial";
	private static final String LIST_NOMBRE_INSTITUCION = "nombresInstitucion";
	private static final String ESMUNICIPAL = "esMunicipal";	

	/** Modulo al que pertenece */
	public final static String MODULE = "general";
	/** Submodulo al que pertenece */
	public final static String SUB_MODULE = "instituciones";

	@Resource
	private InstitucionService institucionService;

	@Resource
	private GeoserverService geoserverService;
	
	@Resource
	private AuthorityTypeService authorityTypeService;

	@Resource
	private NivelTerritorialService nivelTerritorialService;
	
	@Autowired
	private ChileIndicaInstitucionService chileIndicaInstitucionService;
	
	@Autowired
	private RegionBean region;


	/**
	 * Pagina inicial de la instituciones
	 * 
	 * @param model
	 * 
	 * @return "instituciones"
	 */
	@RequestMapping(value = "/admin/instituciones", method = RequestMethod.GET)
	@SuppressWarnings({ "rawtypes" })
	public String getInstituciones(WebRequest webRequest,
			@RequestParam(defaultValue = "10") int size,
			@RequestParam(defaultValue = "1") int page, Model model) {

		// Accede el rol ADMIN, en otro caso pagina error
		decideIfIsUserInRole(ConstantesPermisos.ADMIN);

		if (LOG.isInfoEnabled()) {
			LOG.info("Consultando las instituciones del sistema. Tamaño de página="
					+ size + ". Pagina=" + page);
		}

		calculatePagination(model);

		// first = 0;numElements = institucionService.getResults();
		Long numElements = institucionService.getResults();

		/*
		 * List institucionResult = (List<AuthorityDto>)
		 * institucionService.getFromTo((page - 1) * size, page * size);
		 */
		List institucionResult = (List<AuthorityDto>) institucionService
				.getFromToOrdered((page - 1) * size, page * size);

		PaginatedList result = new PaginatedList(institucionResult,
				(int) numElements.longValue(), size, page);

		model.addAttribute("listaInstituciones", result);

		if (LOG.isInfoEnabled()) {
			LOG.info("Redirigiendo a la vista admin/instituciones/instituciones");
		}

		return URL_LISTADO_INSTITUCIONES;
	}

	/**
	 * Pagina para la creacion de un nueva institucion Entra en la vista
	 * nuevaInstitucion para crear una nueva
	 * 
	 * @param model
	 * 
	 * @return "nuevaInstitucion"
	 */
	@RequestMapping(value = "/admin/nuevaInstitucion", method = RequestMethod.GET)
	public String createInstitucion(Model model) {
		// Accede el rol ADMIN, en otro caso pagina error
		decideIfIsUserInRole(ConstantesPermisos.ADMIN);

		calculatePagination(model);
		AuthorityDto aut = new AuthorityDto();
		aut.setId(null);
		model.addAttribute("authorityDto", aut);
		model.addAttribute(ESMUNICIPAL, false);

		cargaListasVista(model);

		return URL_NUEVA_INSTITUCION;
	}

	/**
	 * Guarda una nueva institucion
	 * 
	 * @param authorityDto
	 * @param result
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/admin/salvarInstitucion", method = RequestMethod.POST)
	public String saveInstitucion(@Valid AuthorityDto authorityDto,
			BindingResult result, Model model) {
		// Accede el rol ADMIN, en otro caso pagina error
		decideIfIsUserInRole(ConstantesPermisos.ADMIN);

                model.addAttribute("module", getActiveModule());
		model.addAttribute("submodule", getActiveSubModule());

		String tipo = authorityDto.getTipoSeleccionado();
		AuthorityDtoValidator validator  = new AuthorityDtoValidator(institucionService,geoserverService, region);
		validator.validate(authorityDto, result);

		// Validamos
		if (result.hasErrors()) {
			cargaListasVista(model);
			if (ID_TIPO_MUNICIPAL.equals(tipo)) {
				model.addAttribute(ESMUNICIPAL, true); // Tipo municipalidad
			}

			if (authorityDto.getId() == null) {
				return URL_NUEVA_INSTITUCION;
			} else {
				return URL_EDITAR_INSTITUCION;
			}
		}

		// Si todo es correcto, guardamos los cambios
		Date hoy = new Date();

		AuthorityTypeDto tipoDto = authorityTypeService.getById(Long
				.decode(tipo));// Tipo: Municipalidad, ServicioPublico, Otros
		NivelTerritorialDto ambitoDto = null;
		if (tipo != null) { 			
			String ambito = authorityDto.getAmbitoSeleccionado();
			ambitoDto = nivelTerritorialService.getById(Long.decode(ambito));			
			
		} else { // Servicio Publico u Otros, obtenemos en la tabla
					// ambito_territorial la tupla con tipo_abito
			List<NivelTerritorialDto> zonas = nivelTerritorialService
					.getNivelTerritorialByTipo("R");
			ambitoDto = zonas.get(0);
		}

		authorityDto.setNivelTerritorial(ambitoDto);
		authorityDto.setType(tipoDto);// Municipalidad, Publico Otros
		authorityDto.setFechaActualizacion(hoy);

		if (authorityDto.getId() != null) {
			
			AuthorityDto oldAuthorityDto = (AuthorityDto) institucionService
					.getById(authorityDto.getId());

			if (authorityDto.getWorkspaceName()==null){
				authorityDto.setWorkspaceName(oldAuthorityDto
						.getWorkspaceName());
			}
			
			institucionService.update(authorityDto);
		} else {
			authorityDto.setFechaCreacion(hoy);
			authorityDto.setRegion(RegionBean.getRegionDto(region));
			authorityDto.setWorkspaceName(GeoserverUtils.createName(authorityDto.getAuthority() + (RegionBean.getRegionDto(region) != null ? " " + RegionBean.getRegionDto(region).getId().toString() : null)));
			institucionService.createInstitucion(authorityDto);
		}

		return "redirect:/admin/instituciones?msg=success";

	}

	/**
	 * Elimina una institucion Solo se pueden borrar instituciones que no tengan
	 * usuarios asociados
	 * 
	 * @param id
	 *            Identificador de la institucion
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/admin/borrarInstitucion/{id}")
	public String deleteInstitucion(@PathVariable long id, Model model) {
		// Accede el rol ADMIN, en otro caso pagina error
		decideIfIsUserInRole(ConstantesPermisos.ADMIN);

		AuthorityDto institucion = (AuthorityDto) institucionService
				.getById(id);

		// boolean tiene = institucionService.tieneUsuariosAsociados(id);
		if (institucion.getTotalUser() > 0) {
			// No podemos borrar instituciones con usuarios

			// FIXME: Mensaje de error
			//return "redirect:/admin/instituciones";
			return "redirect:/admin/instituciones?msg=errorDeleteInstitution";
		}
                
                institucionService.delete(institucion);
		
		return "redirect:/admin/instituciones?msg=success";		
	}

	/**
	 * Pagina para cargar los datos de una institucion, para su posterior
	 * modificacion
	 * 
	 * @param id
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/admin/editarInstitucion/{id}")
	public String modifyInstitucion(@PathVariable long id, Model model) {
		// Accede el rol ADMIN, en otro caso pagina error
		decideIfIsUserInRole(ConstantesPermisos.ADMIN);

		AuthorityDto institucion = (AuthorityDto) institucionService
				.getById(id);

		model.addAttribute("authorityDto", institucion);

		// Control sobre tipo municipalidad
		model.addAttribute(ESMUNICIPAL, ID_TIPO_MUNICIPAL.equals(institucion
				.getTipoSeleccionado()) ? true : false);
		model.addAttribute("module", getActiveModule());
		model.addAttribute("submodule", getActiveSubModule());
		cargaListasVista(model);

		return URL_EDITAR_INSTITUCION;
	}

	/**
	 * Carga las listas necesarios para la vista
	 * 
	 * @param model
	 */
	private void cargaListasVista(Model model) {
		// Tipos de autoridad
		List<AuthorityTypeDto> tiposAutoridad = authorityTypeService
				.getAllAuthority();
		model.addAttribute(LIST_TIPOS_AUTORIDAD, tiposAutoridad);

		// Ambito territorial
		List<NivelTerritorialDto> ambitoTerritorial = nivelTerritorialService
				.getZonesOrderByTypeDescNameAsc();
		
		// Añadimos al select objeto vacio
		NivelTerritorialDto nt = new NivelTerritorialDto();
		ambitoTerritorial.add(0, nt);

		model.addAttribute(LIST_AMBITO_TERRITORIAL, ambitoTerritorial);
		
		//TODO región variable
		List<ChileIndicaInstitucionDto> nombresInstitucion = chileIndicaInstitucionService.getAllAuthorityByRegion(RegionBean.getRegionDto(region) != null ? RegionBean.getRegionDto(region).getId() : null);
		
		model.addAttribute(LIST_NOMBRE_INSTITUCION, nombresInstitucion);
 	}

	/**
	 * Incluye en el modelo los parametros por defecto de la administracion de
	 * instituciones
	 * 
	 * @param model
	 * @param update
	 *            si actualiza los parametros actualizables
	 */
        @Override
	protected void copyDefaultModel(boolean update, Model model) {
		calculatePagination(model);		
	}

	/**
	 * Referencia al enlace a la paginacion de instituciones
	 * 
	 * @return 'admin/instituciones'
	 */
	@Override
	protected String getDefaultPaginationUrl() {
		return "admin/instituciones";
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
