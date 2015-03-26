/*
 * LayerMetadataServiceImpl.java
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
 * Authors:: Jose Alfonso Riera (mailto:jariera@emergya.com)
 */
package com.emergya.ohiggins.service.impl;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.annotation.Resource;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.emergya.ohiggins.dao.OhigginsLayerMetadataEntityDao;
import com.emergya.ohiggins.dto.LayerMetadataDto;
import com.emergya.ohiggins.model.LayerMetadataEntity;
import com.emergya.ohiggins.service.LayerMetadataService;
import com.emergya.persistenceGeo.dao.GenericDAO;
import com.emergya.persistenceGeo.service.impl.AbstractServiceImpl;

/**
 * Implementacion Servicio para el manejo de LayerMetadata
 * 
 * @author jariera
 * 
 */
@Repository
@Transactional
public class OhigginsLayerMetadataServiceImpl extends
		AbstractServiceImpl<LayerMetadataDto, LayerMetadataEntity> implements
		LayerMetadataService {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6538994926376403408L;

	// Formateo de fechas
	private static final String FORMATO_FECHA = "dd/MM/yyyy";
	private DateFormat format = DateFormat.getDateTimeInstance();

	@Resource
	private OhigginsLayerMetadataEntityDao ohigginsLayerMetadataDao;

	@Override
	protected GenericDAO<LayerMetadataEntity, Long> getDao() {
		return ohigginsLayerMetadataDao;
	}

	@Override
	protected LayerMetadataDto entityToDto(LayerMetadataEntity entity) {
		LayerMetadataDto dto = null;

		if (entity != null) {
			dto = new LayerMetadataDto();

			dto.setCalidad(entity.getCalidad());
			dto.setCrsPuntoReferencia(entity.getCrsPuntoReferencia());
			dto.setDistribuidor(entity.getDistribuidor());
			dto.setFechaactualizacion(entity.getFechaactualizacion());
			dto.setFechacreacion(entity.getFechacreacion());
			dto.setFormato(entity.getFormato());
			dto.setFrecuencia(entity.getFrecuencia());
			dto.setId(entity.getId());
			dto.setInformacion(entity.getInformacion());
			dto.setInformacionColeccionPuntoReferencia(entity
					.getInformacionColeccionPuntoReferencia());
			dto.setInformacionGeolocalizacion(entity
					.getInformacionGeolocalizacion());
			dto.setInformacionPuntoReferencia(entity
					.getInformacionPuntoReferencia());
			dto.setNombrePuntoReferencia(entity.getNombrePuntoReferencia());
			dto.setOtros(entity.getOtros());
			dto.setPeriodo(entity.getPeriodo());
			dto.setPosicion(entity.getPosicion());
			dto.setPrecision(entity.getPrecision());
			dto.setPuntosDeReferencia(entity.getPuntosDeReferencia());
			dto.setRango(entity.getRango());
			dto.setReferencia(entity.getReferencia());
			dto.setRequerimientos(entity.getRequerimientos());
			dto.setResponsable(entity.getResponsable());
			dto.setSiguiente(parseFechaFormat(entity.getSiguiente()));
		}

		return dto;
	}

	/**
	 * Formatea fecha en formato dd/mm/yyyy
	 * 
	 * @param f
	 * @return
	 */
	public String parseFechaFormat(Date f) {
		if (f == null)
			return "";

		SimpleDateFormat formato = new SimpleDateFormat(FORMATO_FECHA);
		return formato.format(f);
	}

	/**
	 * Convierte fecha en formato dd/mm/yyyy a Date
	 * 
	 * @param fecha
	 * @param formato
	 * @return
	 */
	public Date stringToDate(String fecha, String formato) {
		try {

			SimpleDateFormat format = new SimpleDateFormat(formato);

			return format.parse(fecha);
		} catch (Exception e) {
			return null;
		}
	}

	@Override
	protected LayerMetadataEntity dtoToEntity(LayerMetadataDto dto) {
		LayerMetadataEntity entity = null;

		if (dto != null) {
			entity = new LayerMetadataEntity();

			entity.setCalidad(dto.getCalidad());
			entity.setCrsPuntoReferencia(dto.getCrsPuntoReferencia());
			entity.setDistribuidor(dto.getDistribuidor());
			entity.setFechaactualizacion(dto.getFechaactualizacion());
			entity.setFechacreacion(dto.getFechacreacion());
			entity.setFormato(dto.getFormato());
			entity.setFrecuencia(dto.getFrecuencia());
			entity.setId(dto.getId());
			entity.setInformacion(dto.getInformacion());
			entity.setInformacionColeccionPuntoReferencia(dto
					.getInformacionColeccionPuntoReferencia());
			entity.setInformacionGeolocalizacion(dto
					.getInformacionGeolocalizacion());
			entity.setInformacionPuntoReferencia(dto
					.getInformacionPuntoReferencia());
			entity.setNombrePuntoReferencia(dto.getNombrePuntoReferencia());
			entity.setOtros(dto.getOtros());
			entity.setPeriodo(dto.getPeriodo());
			entity.setPosicion(dto.getPosicion());
			entity.setPrecision(dto.getPrecision());
			entity.setPuntosDeReferencia(dto.getPuntosDeReferencia());
			entity.setRango(dto.getRango());
			entity.setReferencia(dto.getReferencia());
			entity.setRequerimientos(dto.getRequerimientos());
			entity.setResponsable(dto.getResponsable());
			entity.setSiguiente(stringToDate(dto.getSiguiente(), FORMATO_FECHA));

		}

		return entity;
	}

	/**
	 * obtiene el layer metadato tmo a partir del id de la capa
	 * 
	 * @param dto
	 * @return
	 */
	public LayerMetadataDto getMetadataByIdLayer(Long idLayer) {
		LayerMetadataDto dtor = null;
		LayerMetadataEntity entity = ohigginsLayerMetadataDao
				.getMetadataByIdLayer(idLayer);

		if (entity != null && entity.getId() != null) {
			dtor = entityToDto(entity);
		}

		return dtor;
	}

}
