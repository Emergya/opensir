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
 */
package com.emergya.ohiggins.service.impl;

import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.SortedSet;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.geolatte.geom.jts.JTS;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.emergya.ohiggins.dao.AricaInversionDao;
import com.emergya.ohiggins.dto.InversionUtilDto;
import com.emergya.ohiggins.service.AricaInversionService;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

@Service
public class AricaInversionServiceImpl implements
		AricaInversionService {

	private static final long serialVersionUID = -3090850178967723405L;
	private static final Log LOG = LogFactory
			.getLog(OhigginsIniciativaInversionServiceImpl.class);
	
	
	private final static List<String> etapasOrdenadas = ImmutableList.of(
			"PREFACTIBILIDAD", "FACTIBILIDAD", "DISEÑO", "EJECUCIÓN");
	
	@Autowired
	private AricaInversionDao aricaInversionDao;


    @Override
    @Transactional
    public SortedSet<String> getNivelesTerritoriales() {
        if (LOG.isDebugEnabled()) {
            LOG.debug("getNivelesTerritoriales()");
        }

        SortedSet<String> result;

        result = aricaInversionDao.getNivelesTerritorialesOrderDesc();

        return result;
    }

    @Override
    @Transactional
    public List<Integer> getAnyosDisponibles() {
        if (LOG.isDebugEnabled()) {
            LOG.debug("getAnyosDisponibles()");
        }
    
        List<Integer> result;
        
        result = aricaInversionDao.getAnyosOrderDesc();

        return result;
    }

    
    @Override
    @Transactional
	public List<String> getItemPresupuestarioOrderAsc() {
        if (LOG.isDebugEnabled()) {
            LOG.debug("getItemPresupuestarios()");
        }
    
        List<String> result;
        
        result = aricaInversionDao.getItemPresupuestarioOrderAsc();

        return result;
    }
    
	@Override
	public List<String> getFuentesDisponibles() {
        if (LOG.isDebugEnabled()) {
            LOG.debug("getFuentes()");
        }
    
        List<String> result;
        
        result = aricaInversionDao.getFuenteFinanciamientoOrderAsc();

        return result;		
	}


	@Override
	@Transactional
	public List<Map<String, Object>> getMontosGroupBy(String anyo, String agruparPor,
			String financiamiento, String itemPresupuestario,String nivelTerritorial) {
		if (LOG.isDebugEnabled()) {
			LOG.debug("getMotosGroupBy("
					+ "anyo = " + anyo + ", agruparPor = " + agruparPor
					+ ", financiamiento = " + financiamiento + ", itemPresupuestario = "
					+ itemPresupuestario
					+ ", nivelTerritorial = " + nivelTerritorial + ")");
		}

		if (agruparPor == null) {
			throw new IllegalArgumentException(
					"El parámetro agruparPor no puede ser null");
		}

		List<Map<String, Object>> result = aricaInversionDao.getMontosGroupBy(
                anyo,agruparPor, financiamiento, itemPresupuestario, nivelTerritorial);
        
		return result;
	}


	@Override
	public List<Map<String, Object>> getProyectosGeo(String anyo, 
			String financiamiento, String itemPresupuestario,  
			String nivelTerritorial) {
		if (LOG.isDebugEnabled()) {
			LOG.debug("getProyectosGeo(anyo = " + anyo + ", itemPresupuestario = " + itemPresupuestario
					+ ", financiamiento = " + financiamiento  + ", nivelTerritorial = " + nivelTerritorial + ")");
		}
		
		List<Map<String, Object>> returnResult = Lists.newLinkedList();

		List<InversionUtilDto> inversiones;
		Map<String, InversionUtilDto> yaProcesados = Maps.newHashMap();
		
		inversiones = aricaInversionDao.getProyectosGeo(
                anyo, financiamiento, itemPresupuestario,  nivelTerritorial);
		
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
			feature.put("properties", properties);
			if(inversion.getGeometry()!=null){
				feature.put("geometry", JTS.from(inversion.getGeometry()));
			}
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


	@Override
	public Map<String, String> getInfoFicha(String codBip, String etapa, String anyo) {
		return aricaInversionDao.getInfoFichaInversion(codBip, etapa, anyo);
	}
}