/* OhigginsIniciativaInversionServiceImpl.java
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
package com.emergya.ohiggins.service.impl;

import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.geolatte.geom.jts.JTS;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.emergya.ohiggins.dao.OhigginsInversionDao;
import com.emergya.ohiggins.dto.InversionUtilDto;
import com.emergya.ohiggins.service.IniciativaInversionService;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

@Service
public class OhigginsIniciativaInversionServiceImpl implements
		IniciativaInversionService {

	private static final long serialVersionUID = -3090850178967723405L;
	private static final Log LOG = LogFactory
			.getLog(OhigginsIniciativaInversionServiceImpl.class);
	@Autowired
	private OhigginsInversionDao ohigginsInversionDao;

	private final static List<String> etapasOrdenadas = ImmutableList.of(
			"PREFACTIBILIDAD", "FACTIBILIDAD", "DISEÑO", "EJECUCIÓN");

	@Override
	@Transactional
	public List<String> getAnyosDisponibles(TIPO_PROYECTOS tipoProyecto) {
		if (LOG.isDebugEnabled()) {
			LOG.debug("getAnyosDisponibles(tipoProyecto = " + tipoProyecto
					+ ")");
		}
		if (tipoProyecto == null) {
			throw new IllegalArgumentException(
					"El parámetro tipoProyecto no puede ser null");
		}
		List<String> result;
		switch (tipoProyecto) {
		case EJECUCION:
			result = ohigginsInversionDao.getAnyosEjecucionOrderDesc();
			break;
		case PREINVERSION:
			result = ohigginsInversionDao.getAnyosPreinversionOrderDesc();
			break;
		default:
			throw new IllegalArgumentException(
					"Valor de TipoProyecto no esperado: " + tipoProyecto);
		}
		return result;
	}

	@Override
	@Transactional
	public List<String> getFuentesDisponibles(TIPO_PROYECTOS tipoProyecto,
			String anyo) {
		if (LOG.isDebugEnabled()) {
			LOG.debug("getFuentesDisponibles(tipoProyecto = " + tipoProyecto
					+ ", anyo = " + anyo + ")");
		}

		String anyoConvertido = StringUtils.trimToNull(anyo);
		if (tipoProyecto == null) {
			throw new IllegalArgumentException(
					"El parámetro tipoProyecto no puede ser null");
		}
		if (anyoConvertido == null) {
			throw new IllegalArgumentException(
					"El parámetro anyo no puede ser null ni estar vacío");
		}
		List<String> result;
		switch (tipoProyecto) {
		case EJECUCION:
			result = ohigginsInversionDao
					.getFuenteFinanciamientoEjecucionOrderAsc(anyo);
			break;
		case PREINVERSION:
			result = ohigginsInversionDao
					.getFuenteFinanciamientoPreinversionOrderAsc(anyo);
			break;

		default:
			throw new IllegalArgumentException(
					"Valor de TipoProyecto no esperado: " + tipoProyecto);
		}
		return result;

	}

	@Override
	@Transactional
	public List<String> getLineasFinancierasDisponibles(
			TIPO_PROYECTOS tipoProyecto, String anyo,
			String fuenteFinanciamiento) {
		if (LOG.isDebugEnabled()) {
			LOG.debug("getLineasFinancierasDisponibles(tipoProyecto = "
					+ tipoProyecto + ", anyo = " + anyo
					+ ", fuenteFinanciamiento = " + fuenteFinanciamiento + ")");
		}
		String anyoConvertido = StringUtils.trimToNull(anyo);
		String fuenteFinancieraConvertida = StringUtils
				.trimToNull(fuenteFinanciamiento) == null ? null
				: fuenteFinanciamiento;
		if (tipoProyecto == null) {
			throw new IllegalArgumentException(
					"El parámetro tipoProyecto no puede ser null");
		}
		if (anyoConvertido == null) {
			throw new IllegalArgumentException(
					"El parámetro anyo no puede ser null ni estar vacío");
		}

		List<String> result;
		switch (tipoProyecto) {
		case EJECUCION:
			result = ohigginsInversionDao.getLineaFinancieraEjecucionOrderAsc(
					anyo, fuenteFinancieraConvertida);
			break;
		case PREINVERSION:
			result = ohigginsInversionDao
					.getLineaFinancieraPreinversionOrderAsc(anyo,
							fuenteFinancieraConvertida);
			break;

		default:
			throw new IllegalArgumentException(
					"Valor de TipoProyecto no esperado: " + tipoProyecto);
		}
		return result;
	}

	@Override
	@Transactional
	public List<String> getSectoresDisponibles(TIPO_PROYECTOS tipoProyecto,
			String anyo, String fuenteFinanciamiento, String lineaFinanciera) {
		if (LOG.isDebugEnabled()) {
			LOG.debug("getSectoresDisponibles(tipoProyecto = " + tipoProyecto
					+ ", anyo = " + anyo + ", fuenteFinanciamiento = "
					+ fuenteFinanciamiento + ", lineaFinanciera = "
					+ lineaFinanciera + ")");
		}
		String anyoConvertido = StringUtils.trimToNull(anyo);
		String fuenteFinancieraConvertida = StringUtils
				.trimToNull(fuenteFinanciamiento) == null ? null
				: fuenteFinanciamiento;
		String lineaFinancieraConvertida = StringUtils
				.trimToNull(lineaFinanciera) == null ? null : lineaFinanciera;
		if (tipoProyecto == null) {
			throw new IllegalArgumentException(
					"El parámetro tipoProyecto no puede ser null");
		}
		if (anyoConvertido == null) {
			throw new IllegalArgumentException(
					"El parámetro anyo no puede ser null ni estar vacío");
		}

		List<String> result;
		switch (tipoProyecto) {
		case EJECUCION:
			result = ohigginsInversionDao.getSectoresEjecucionOrderAsc(anyo,
					fuenteFinancieraConvertida, lineaFinancieraConvertida);
			break;
		case PREINVERSION:
			result = ohigginsInversionDao.getSectoresPreinversionOrderAsc(anyo,
					fuenteFinancieraConvertida, lineaFinancieraConvertida);
			break;

		default:
			throw new IllegalArgumentException(
					"Valor de TipoProyecto no esperado: " + tipoProyecto);
		}
		return result;
	}

	@Override
	@Transactional
	public Map<String, String> getInfoFichaInversion(String codigoBip,
			String etapa, String servicioResponsable, String anyo) {

		if (LOG.isDebugEnabled()) {
			LOG.debug("getInfoFichaInversion(codigoBip = " + codigoBip
					+ ", etapa = " + etapa + ", servicioResponsable = "
					+ servicioResponsable + ", anyo = " + anyo + ")");
		}

		if (codigoBip == null) {
			throw new IllegalArgumentException(
					"El parámetro codigoBip no puede ser null");
		} else if (etapa == null) {
			throw new IllegalArgumentException(
					"El parámetro etapa no puede ser null");
		} else if (servicioResponsable == null) {
			throw new IllegalArgumentException(
					"El parámetro servicioResponsable no puede ser null");
		} else if (anyo == null) {
			throw new IllegalArgumentException(
					"El parámetro anyo no puede ser null");
		} else {
			return ohigginsInversionDao.getInfoFichaInversion(codigoBip, etapa,
					servicioResponsable, anyo);
		}

	}
	
	@Override
	@Transactional
	public Map<String, String> getInfoFichaEjecucion(String codigoBip,
			String etapa, String servicioResponsable, String anyo) {

		if (LOG.isDebugEnabled()) {
			LOG.debug("getInfoFichaEjecucion(codigoBip = " + codigoBip
					+ ", etapa = " + etapa + ", servicioResponsable = "
					+ servicioResponsable + ", anyo = " + anyo + ")");
		}

		if (codigoBip == null) {
			throw new IllegalArgumentException(
					"El parámetro codigoBip no puede ser null");
		} else if (etapa == null) {
			throw new IllegalArgumentException(
					"El parámetro etapa no puede ser null");
		} else if (servicioResponsable == null) {
			throw new IllegalArgumentException(
					"El parámetro servicioResponsable no puede ser null");
		} else if (anyo == null) {
			throw new IllegalArgumentException(
					"El parámetro anyo no puede ser null");
		} else {
			return ohigginsInversionDao.getInfoFichaEjecucion(codigoBip, etapa,
					servicioResponsable, anyo);
		}

	}

	public String getNameInversion(String codigoBip) {

		if (LOG.isDebugEnabled()) {
			LOG.debug("getNameInversion(codigoBip = " + codigoBip + ")");
		}

		if (codigoBip == null) {
			throw new IllegalArgumentException(
					"El parámetro codigoBip no puede ser null");
		} else {
			return ohigginsInversionDao.getNameInversion(codigoBip);
		}
	}

	@Override
	@Transactional
	public List<Map<String, Object>> getMotosGroupBy(
			TIPO_PROYECTOS tipoProyecto, String anyo, String agruparPor,
			String fuente, String lineaFinanciera, String sector,
			String nivelTerritorial) {
		if (LOG.isDebugEnabled()) {
			LOG.debug("getMotosGroupBy(tipoProyecto = " + tipoProyecto
					+ ", anyo = " + anyo + ", agruparPor = " + agruparPor
					+ ", fuente = " + fuente + ", lineaFinanciera = "
					+ lineaFinanciera + ", sector = " + sector
					+ ", nivelTerritorial = " + nivelTerritorial + ")");
		}
		if (tipoProyecto == null) {
			throw new IllegalArgumentException(
					"El parámertro tipoProyecto no puede ser null");
		} else if (anyo == null) {
			throw new IllegalArgumentException(
					"El parámetro anyo no puede ser null");
		} else if (agruparPor == null) {
			throw new IllegalArgumentException(
					"El parámetro agruparPor no puede ser null");
		}

		List<Map<String, Object>> result = Lists.newArrayList();

		if (tipoProyecto == TIPO_PROYECTOS.EJECUCION) {
			result = ohigginsInversionDao.getMontosEjecucionGroupBy(anyo,
					agruparPor, fuente, lineaFinanciera, sector,
					nivelTerritorial);
		} else if (tipoProyecto == TIPO_PROYECTOS.PREINVERSION) {
			result = ohigginsInversionDao.getMontosPreinversionGroupBy(anyo,
					agruparPor, fuente, lineaFinanciera, sector,
					nivelTerritorial);

		}
		return result;
	}

	@Override
	public List<Map<String, Object>> getProyectosGeo(
			TIPO_PROYECTOS tipoProyecto, String anyo, String fuente,
			String lineaFinanciera, String sector, String nivelTerritorial) {
		if (LOG.isDebugEnabled()) {
			LOG.debug("getProyectosGeo(tipoProyecto = " + tipoProyecto
					+ ", anyo = " + anyo + ", fuente = " + fuente
					+ ", lineaFinanciera = " + lineaFinanciera + ", sector = "
					+ sector + ", nivelTerritorial = " + nivelTerritorial + ")");
		}
		if (tipoProyecto == null) {
			throw new IllegalArgumentException(
					"El parámertro tipoProyecto no puede ser null");
		}
		List<Map<String, Object>> returnResult = Lists.newLinkedList();

		List<InversionUtilDto> inversiones = Lists.newArrayList();
		Map<String, InversionUtilDto> yaProcesados = Maps.newHashMap();

		if (tipoProyecto == TIPO_PROYECTOS.PREINVERSION) {
			inversiones = ohigginsInversionDao.getProyectosGeoPreinversion(
					anyo, fuente, lineaFinanciera, sector, nivelTerritorial);
		} else if (tipoProyecto == TIPO_PROYECTOS.EJECUCION) {
			inversiones = ohigginsInversionDao.getProyectosGeoEjecucionDetalle(
					anyo, fuente, lineaFinanciera, sector, nivelTerritorial);
		}

		for (InversionUtilDto inversion : inversiones) {
			compruebaYAnyade(inversion, yaProcesados);
		}
		for (InversionUtilDto inversion : yaProcesados.values()) {
			Map<String, Object> feature = Maps.newHashMap();
			Map<String, Object> properties = Maps.newHashMap();
			feature.put("type", "Feature");
			properties.put("codBip", inversion.getCodBip());
			properties.put("anyo", inversion.getAnyo());
			properties.put("etapa", inversion.getEtapa());
			properties.put("serRes", inversion.getSerRes());
			properties.put("tipoProyecto", tipoProyecto.toString());
			feature.put("properties", properties);
			feature.put("geometry", JTS.from(inversion.getGeometry()));
			returnResult.add(feature);

		}

		return returnResult;
	}

	private void compruebaYAnyade(InversionUtilDto inversion,
			Map<String, InversionUtilDto> yaProcesados) {
		InversionUtilDto yaProcesada = yaProcesados.get(inversion.getCodBip());
		if (yaProcesada == null) {
			yaProcesados.put(inversion.getCodBip(), inversion);
		} else {
			String etapaAnyadida = yaProcesada.getEtapa();
			String nuevaEtapa = inversion.getEtapa();
			if (etapasOrdenadas.indexOf(StringUtils.upperCase((etapaAnyadida),
					Locale.ENGLISH)) < etapasOrdenadas.indexOf(StringUtils
					.upperCase(nuevaEtapa, Locale.ENGLISH))
					&& etapasOrdenadas.indexOf(StringUtils.upperCase(
							nuevaEtapa, Locale.ENGLISH)) != -1) {
				// Si la etapa es posterior a la ya añadida sustituimos la
				// entrada antigua por la nueva
				yaProcesados.put(inversion.getCodBip(), inversion);

			}
		}

	}

}