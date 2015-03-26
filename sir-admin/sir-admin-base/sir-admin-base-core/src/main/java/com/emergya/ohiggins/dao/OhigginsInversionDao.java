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
 * Authors:: Juan Luis Rodríguez Ponce (mailto:jlrodriguez@emergya.com)
 */
package com.emergya.ohiggins.dao;

import java.util.List;
import java.util.Map;

import com.emergya.ohiggins.dto.InversionUtilDto;

/**
 * Facilita el acceso a datos y consulta de varias tablas relacionadas con las
 * iniciativas de inversión.
 * 
 * @author <a href="mailto:jlrodriguez@emergya.com">Juan Luis Rodríguez
 *         Ponce</a>
 */
public interface OhigginsInversionDao {

	/**
	 * Consulta la tabla de la entidad ChileIndicaPreinversionEntity y obtiene
	 * los distintos valores almacenados de la propiedad anyo.
	 * 
	 * @return la lista de años para los que hay proyectos en preinversión en
	 *         orden descendente.
	 */
	public List<String> getAnyosPreinversionOrderDesc();

	/**
	 * Consulta la tabla de la entidad ChileIndicaEjecucionDetalleEntity y
	 * obtiene los distintos valores almacenados de la propiedad anyo.
	 * 
	 * @return la lista de años para los que hay proyectos en ejecución en orden
	 *         descendente.
	 */
	public List<String> getAnyosEjecucionOrderDesc();

	/**
	 * Consulta la tabla de la entidad ChileindicaPreinversionEntity y obtiene
	 * los distintos valores almacenados en la propiedad fuenteFinanciamento de
	 * los registros del <code>anyo</code> en orden ascendente.
	 * 
	 * @param anyo
	 *            Año de los proyectos. Si es <code>null</code> no se utiliza
	 *            para el filtrado.
	 * @return la lista de los distintos valores de fuenteFinanciamento para un
	 *         anyo.
	 */
	public List<String> getFuenteFinanciamientoPreinversionOrderAsc(String anyo);

	/**
	 * Consulta la tabla de la entidad ChileindicaEjecucionDetalle y obtiene los
	 * distintos valores almacenados en la propiedad fuenteFinanciamiento de los
	 * registros del <code>anyo</code> en orden ascendente.
	 * 
	 * @param anyo
	 *            año de los proyectos. Si es <code>null</code> no se utiliza
	 *            para el filtrado.
	 * @return la lista de los distintos valores de fuenteFinanciamiento para un
	 *         anyo dado.
	 */
	public List<String> getFuenteFinanciamientoEjecucionOrderAsc(String anyo);

	/**
	 * 
	 * Consulta la tabla de la entidad ChileindicaPreinversionEntity y obtiene
	 * los distintos valores almacenados en la propiedad viaDeFinanciamiento de
	 * los registros del <code>anyo</code> y <code>fuenteFinanciamiento</code>
	 * en orden ascendente.
	 * 
	 * @param anyo
	 *            año de los proyectos. Si es <code>null</code> no se utiliza
	 *            para el filtrado.
	 * @param fuenteFinanciamiento
	 *            fuente de financiamiento de los proyectos. Si es
	 *            <code>null</code> no se utiliza para el filtrado.
	 * @return la lista de los distintos valores de viaDeFinanciamiento para un
	 *         anyo y fuenteFinanciamiento dados.
	 */
	public List<String> getLineaFinancieraPreinversionOrderAsc(String anyo,
			String fuenteFinanciamiento);

	/**
	 * 
	 * Consulta la tabla de la entidad ChileindicaEjecucionDetalleEntity y
	 * obtiene los distintos valores almacenados en la propiedad
	 * servicioResponsable de los registros del <code>anyo</code> y
	 * <code>fuenteFinanciamiento</code> en orden ascendente.
	 * 
	 * @param anyo
	 *            año de los proyectos. Si es <code>null</code> no se utiliza
	 *            para el filtrado.
	 * @param fuenteFinanciamiento
	 *            fuente de financiamiento de los proyectos. Si es
	 *            <code>null</code> no se utiliza para el filtrado.
	 * @return la lista de los distintos valores de servicioResponsable para un
	 *         anyo y fuenteFinanciamiento dados.
	 */
	public List<String> getLineaFinancieraEjecucionOrderAsc(String anyo,
			String fuenteFinanciamiento);

	/**
	 * 
	 * Consulta la tabla de la entidad ChileindicaPreinversionEntity y obtiene
	 * los distintos valores almacenados en la propiedad sector de los registros
	 * del <code>anyo</code>, <code>fuenteFinanciamiento</code> y
	 * <code>lineaFinanciera</code> en orden ascendente.
	 * 
	 * @param anyo
	 *            año de los proyectos. Si es <code>null</code> no se utiliza
	 *            para el filtrado.
	 * @param fuenteFinanciamiento
	 *            fuente de financiamiento de los proyectos. Si es
	 *            <code>null</code> no se utiliza para el filtrado.
	 * @param lineaFinanciera
	 *            línea financiera. Si es <code>null</code> no se utiliza para
	 *            el filtrado.
	 * @return la lista de los distintos valores de sector para un anyo,
	 *         fuenteFinanciamiento y lineaFinanciera dados.
	 */
	public List<String> getSectoresPreinversionOrderAsc(String anyo,
			String fuenteFinanciamiento, String lineaFinanciera);

	/**
	 * 
	 * Consulta la tabla de la entidad ChileindicaEjecucionDetalleEntity y
	 * obtiene los distintos valores almacenados en la propiedad sector de los
	 * registros del <code>anyo</code>, <code>fuenteFinanciamiento</code> y
	 * <code>lineaFinanciera</code> en orden ascendente.
	 * 
	 * @param anyo
	 *            año de los proyectos. Si es <code>null</code> no se utiliza
	 *            para el filtrado.
	 * @param fuenteFinanciamiento
	 *            fuente de financiamiento de los proyectos. Si es
	 *            <code>null</code> no se utiliza para el filtrado.
	 * @param lineaFinanciera
	 *            línea financiera. Si es <code>null</code> no se utiliza para
	 *            el filtrado.
	 * @return la lista de los distintos valores de sector para un anyo,
	 *         fuenteFinanciamiento y lineaFinanciera dados.
	 */
	public List<String> getSectoresEjecucionOrderAsc(String anyo,
			String fuenteFinanciamiento, String lineaFinanciera);

	/**
	 * Obtiene un mapa con la informacion requerida para completar la ficha de
	 * inversion
	 * 
	 * @param codigoBip
	 * @param etapa
	 * @param servicioResponsable
	 * @param anyo
	 * @return un mapa con la informacion requerida para completar la ficha de
	 *         inversion
	 */
	public Map<String, String> getInfoFichaInversion(String codigoBip,
			String etapa, String servicioResponsable, String anyo);

	/**
	 * Obtiene el nombre de la inversion
	 * 
	 * @param codigoBip
	 * @return el nombre de la inversion
	 */
	public String getNameInversion(String codigoBip);

	/**
	 * Devuelve una lista de arrays compuestos de monto, número de proyectos y
	 * valor del campo usado para agrupar los proyectos de tipo preinversión que
	 * cumplen con el filtro pasado. Si alguno de los campos no obligatorios es
	 * <code>null</code> no se utiliza para realizar el filtrado.
	 * 
	 * @param anyo
	 *            obligatorio.
	 * @param agruparPor
	 *            el campo que se utilizará para agrupar los resultados
	 *            (obligatorio).
	 * @param fuente
	 * @param lineaFinanciera
	 * @param sector
	 * @param nivelTerritorial
	 * 
	 * @return una lista de arrays con el monto acumulado, el número de
	 *         proyectos acumulados y el valor del campo usado para la
	 *         agrupación.
	 */
	public List<Map<String, Object>> getMontosPreinversionGroupBy(String anyo,
			String agruparPor, String fuente, String lineaFinanciera,
			String sector, String nivelTerritorial);

	/**
	 * Devuelve una lista de arrays compuestos de monto, número de proyectos y
	 * valor del campo usado para agrupar los proyectos de tipo ejecución PROPIR
	 * que cumplen con el filtro pasado. Si alguno de los campos no obligatorios
	 * es <code>null</code> no se utiliza para realizar el filtrado.
	 * 
	 * @param anyo
	 *            obligatorio.
	 * @param agruparPor
	 *            el campo que se utilizará para agrupar los resultados
	 *            (obligatorio).
	 * @param fuente
	 * @param lineaFinanciera
	 * @param sector
	 * @param nivelTerritorial
	 * 
	 * @return una lista de arrays con el monto acumulado, el número de
	 *         proyectos acumulados y el valor del campo usado para la
	 *         agrupación.
	 */
	public List<Map<String, Object>> getMontosEjecucionGroupBy(String anyo,
			String agruparPor, String fuente, String lineaFinanciera,
			String sector, String nivelTerritorial);

	public List<InversionUtilDto> getProyectosGeoPreinversion(String anyo,
			String fuente, String lineaFinanciera, String sector,
			String nivelTerritorial);

	public Map<String, String> getInfoFichaEjecucion(String codigoBip, String etapa,
			String servicioResponsable, String anyo);

	public List<InversionUtilDto> getProyectosGeoEjecucionDetalle(String anyo, String fuente,
			String lineaFinanciera, String sector, String nivelTerritorial);
}