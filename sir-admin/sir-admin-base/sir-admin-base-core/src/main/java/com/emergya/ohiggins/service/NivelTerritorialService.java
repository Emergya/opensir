/*
 * NivelTerritorialService.java
 * 
 * Copyright (C) 2011
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
 * Authors:: Jose Alfonso (mailto:jariera@emergya.com)
 */
package com.emergya.ohiggins.service;

import java.io.Serializable;
import java.util.List;

import com.emergya.ohiggins.dto.NivelTerritorialDto;
import com.emergya.persistenceGeo.service.AbstractService;

/**
 * Interfaz de nivel territorial
 * 
 * @author <a href="mailto:jariera@emergya.com">jariera</a>
 * 
 */
public interface NivelTerritorialService extends AbstractService, Serializable {
	public final static String TIPO_NIVEL_REGIONAL = "R";
	public final static String TIPO_NIVEL_PROVINCIAL = "P";
	public final static String TIPO_NIVEL_MUNICIPAL = "M";

	/**
	 * Obtiene lista de ambitos regionales ordenados por nombres
	 * 
	 * @return Lista de ambiros regionales
	 */
	public List<NivelTerritorialDto> getAllNivelTerritorial();

	/**
	 * Obtiene el nivel territorial por id
	 * 
	 * @param id
	 * @return
	 */
	public NivelTerritorialDto getById(Long id);

	/**
	 * Obtiene el nivel territorial por tipo.
	 * 
	 * @param tipo
	 *            tipo de ámbito (
	 *            {@link NivelTerritorialService#TIPO_NIVEL_REGIONAL},
	 *            {@link NivelTerritorialService#TIPO_NIVEL_PROVINCIAL} o
	 *            {@link NivelTerritorialService#TIPO_NIVEL_MUNICIPAL}).
	 * @return
	 */
	public List<NivelTerritorialDto> getNivelTerritorialByTipo(String tipo);

	/**
	 * Obtiene el nivel territorial por tipos.
	 * 
	 * @param tipo
	 *            tipo de ámbito (
	 *            {@link NivelTerritorialService#TIPO_NIVEL_REGIONAL},
	 *            {@link NivelTerritorialService#TIPO_NIVEL_PROVINCIAL} o
	 *            {@link NivelTerritorialService#TIPO_NIVEL_MUNICIPAL}).
	 * @return
	 */
	public List<NivelTerritorialDto> getNivelTerritorialByTipos(String[] tipos);

	/**
	 * Obtiene el nivel territorial con el nombre pasado como parámetro y todos
	 * sus descendientes.
	 * 
	 * @param nivelTerritorial
	 * @return
	 */
	public List<NivelTerritorialDto> findNivelAndChildren(
			String nivelTerritorial);

	/**
	 * 
	 * @return devuelve las zonas ordenadas por tipo descendente y nombre
	 *         ascendente.
	 */
	public List<NivelTerritorialDto> getZonesOrderByTypeDescNameAsc();

	/**
	 * 
	 * @param nivelTerritorial
	 *            nombre del nivel territorial
	 * @return una lista de los niveles territoriales cuyo nombre coincide con el pasado.
	 */
	public List<NivelTerritorialDto> getByName(String nivelTerritorial);
}
