/*
 * Copyright (C) 2013
 * 
 * This file is part of Proyecto sir-adminn
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
package com.emergya.ohiggins.service;

import java.util.Date;
import java.util.List;

import com.emergya.ohiggins.dto.ChileindicaInversionDataDto;
import com.emergya.ohiggins.model.ChileindicaInversionDataEntity;
import com.emergya.persistenceGeo.service.AbstractService;

/**
 * 
 * @author <a href="mailto:jlrodriguez@emergya.com">Juan Luis Rodríguez
 *         Ponce</a>
 */
public interface ChileindicaInversionDataService extends AbstractService {

	/**
	 * Comprueba si un proyecto debe ser actualizado. Para localizar el proyecto
	 * se utilizan los parámetros region, ano, cInstitucion y cFicha. Una vez
	 * localizado se comprueba si debe ser actualizado comprobando si la fecha
	 * de registro es distinta al parámetro fechaRegistro.
	 * 
	 * @param region
	 *            código de región.
	 * @param ano
	 *            año.
	 * @param cInstitucion
	 *            código de institución.
	 * @param cPreinversion
	 *            código de preinversión.
	 * @param cFicha
	 *            código de ficha
	 * @param fechaRegistro
	 *            fecha de registro. Si la fecha no es un número que represente
	 *            a una fecha con formato YYYYMMDD, se considera que el registro
	 *            necesita actualización.
	 * @return <code>true</code> si no se encuentra el proyecto, la fecha de
	 *         actualización no está en formato YYYYMMDD o si el proyecto tiene
	 *         una fecha de registro anterior a la proporcionada. Devuelve
	 *         <code>false</code> si existe el proyecto y su fecha de
	 *         actualización es nula o anterior a la proporcionada en el
	 *         parámetro <code>fechaRegistro</code>
	 */
	public abstract boolean checkIfProjectMustBeUpdated(Integer region,
			Integer ano, Integer cInstitucion, Integer cPreinversion,
			Integer cFicha, Integer fechaRegistro);

	/**
	 * Devuelve un proyecto que tiene la clave de negocio formado por todos los
	 * parámetros pasados. En el caso de un proyecto de inversión los datos
	 * necesarios son :
	 * <ul>
	 * <li>Año.</li>
	 * <li>Código de región.</li>
	 * <li>Código de institución.</li>
	 * <li>Código de preinversión.</li>
	 * <li>Código de ficha.</li>
	 * </ul>
	 * 
	 * @param ano
	 *            año.
	 * @param region
	 *            código de región.
	 * @param cInstitucion
	 *            código de institución.
	 * @param cPreinversion
	 *            código de preinversión.
	 * @param cFicha
	 *            código de ficha.
	 * @return el proyecto de inversión de la base de datos cuya clave de
	 *         negocio coincide con los datos pasados por parámetros.
	 */
	public abstract ChileindicaInversionDataDto findProjectByBussinessKey(
			Integer ano, Integer region, Integer cInstitucion,
			Integer cPreinversion, Integer cFicha);

	/**
	 * 
	 * @return la lista de todos los {@link ChileindicaInversionDataEntity}
	 *         existentes en la base de datos.
	 */
	public abstract List<Long> getAllProjectDbIds();

	public abstract Date getValidDate(Integer dateString);

	public abstract void deleteById(Long id);

}
