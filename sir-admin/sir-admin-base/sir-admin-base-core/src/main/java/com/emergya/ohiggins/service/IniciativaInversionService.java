/* IniciativaInversionService.java
 * 
 * 
 * Copyright (C) 2012
 * 
 * This file is part of Proyecto ohiggins
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
package com.emergya.ohiggins.service;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * 
 * @author <a href="mailto:jlrodriguez@emergya.com">Juan Luis Rodríguez
 *         Ponce</a>
 */
public interface IniciativaInversionService extends Serializable {

	/**
	 * Posibles tipos de proyecto.
	 */
	public enum TIPO_PROYECTOS {

		/**
		 * Proyectos de tipo preinversión.
		 */
		PREINVERSION,
		/**
		 * Proyectos de tipo ejecución PROPIR.
		 */
		EJECUCION
	}

	/**
	 * Obtiene los posibles años
	 * 
	 * @param tipoProyecto
	 * @return
	 */
	public List<String> getAnyosDisponibles(TIPO_PROYECTOS tipoProyecto);

	public List<String> getFuentesDisponibles(TIPO_PROYECTOS tipoProyecto,
			String anyo);

	public List<String> getLineasFinancierasDisponibles(
			TIPO_PROYECTOS tipoProyecto, String anyo,
			String fuenteFinanciamiento);

	public List<String> getSectoresDisponibles(TIPO_PROYECTOS tipoProyectos,
			String anyo, String fuenteFinanciamiento, String lineaFinanciera);

	public Map<String, String> getInfoFichaInversion(String codigoBip,
			String etapa, String servicioResponsable, String anyo);
	
	public Map<String, String> getInfoFichaEjecucion(String codigoBip,
			String etapa, String servicioResponsable, String anyo);

	public String getNameInversion(String codigoBip);

	public List<Map<String, Object>> getMotosGroupBy(
			TIPO_PROYECTOS tipoProyecto, String anyo, String agruparPor,
			String fuente, String lineaFinanciera, String sector,
			String nivelTerritorial);

	public List<Map<String, Object>> getProyectosGeo(
			TIPO_PROYECTOS tipoProyecto, String anyo, String fuente,
			String lineaFinanciera, String sector, String nivelTerritorial);

}