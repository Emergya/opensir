/* OhigginsInversionDao.java
 * 
 * Copyright (C) 2012
 * 
 * This file is part of project ohiggins-core
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
 */
package com.emergya.ohiggins.dao;

import java.util.List;
import java.util.Map;
import java.util.SortedSet;

import com.emergya.ohiggins.dto.InversionUtilDto;


/**
 * Facilita el acceso a datos y consulta de varias tablas relacionadas con las
 * iniciativas de inversión.
 * 
 */
public interface AricaInversionDao {

	public SortedSet<String> getNivelesTerritorialesOrderDesc();

	public List<Integer> getAnyosOrderDesc();

	public List<String> getFuenteFinanciamientoOrderAsc(); 

	public List<String> getItemPresupuestarioOrderAsc();

	public List<Map<String, Object>> getMontosGroupBy(String anyo,
			String agruparPor, String fuente, String lineaFinanciera, String nivelTerritorial);

	public List<InversionUtilDto> getProyectosGeo(String anyo, 
			String lineaFinanciera, String itemPresupuestario, String nivelTerritorial);

	public Map<String, String> getInfoFichaInversion(String codBip, String etapa, String anyo);
}