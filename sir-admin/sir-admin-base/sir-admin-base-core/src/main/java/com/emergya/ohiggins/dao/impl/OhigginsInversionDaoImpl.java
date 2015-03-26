/* OhigginsInversionDaoImpl.java
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
package com.emergya.ohiggins.dao.impl;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
import org.springframework.stereotype.Repository;

import com.emergya.ohiggins.dao.OhigginsInversionDao;
import com.emergya.ohiggins.dto.InversionUtilDto;
import com.emergya.ohiggins.model.AcuerdosCoreEntity;
import com.emergya.ohiggins.model.BasePreinversionGoreEntity;
import com.emergya.ohiggins.model.ChileindicaEjecucionDetalleEntity;
import com.emergya.ohiggins.model.ChileindicaEjecucionEntity;
import com.emergya.ohiggins.model.ChileindicaPreinversionEntity;
import com.emergya.ohiggins.model.ProyectosDacgEntity;
import com.emergya.ohiggins.model.ProyectosMidesoEntity;
import com.google.common.collect.Lists;

/**
 * 
 * @author jlrodriguez
 */
@Repository("ohigginsInversionDao")
public class OhigginsInversionDaoImpl extends HibernateDaoSupport implements
		OhigginsInversionDao {

	private static final String EMPTY_COMUNA_STRING = "SIN IDENTIFICACIÓN";
	private NumberFormat currencyFmt = NumberFormat.getInstance(new Locale(
			"es", "CL"));

	@Autowired
	public void init(SessionFactory sessionFactory) {
		setSessionFactory(sessionFactory);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<String> getAnyosPreinversionOrderDesc() {
		List<String> anyos;
		String hql = "select distinct p.anyo from ChileindicaPreinversionEntity p "
				+ " join p.updateInstance as e where e.enabled = true "
				+ " and p.anyo is not null and trim(p.anyo) != '' "
				+ " order by p.anyo desc ";
		anyos = getHibernateTemplate().find(hql);

		return anyos;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<String> getAnyosEjecucionOrderDesc() {
		List<String> anyos;
		String hql = "select distinct p.anyo from ChileindicaEjecucionDetalleEntity p "
				+ " join p.updateInstance as e where e.enabled = true "
				+ " and p.anyo is not null and trim(p.anyo)!='' "
				+ "order by p.anyo desc ";
		anyos = getHibernateTemplate().find(hql);

		return anyos;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<String> getFuenteFinanciamientoPreinversionOrderAsc(String anyo) {
		List<String> parameterNames = new ArrayList<String>(1);
		List<Object> parameterValues = new ArrayList<Object>(1);
		StringBuilder hql = new StringBuilder(
				"select distinct p.fuenteFinanciamiento from ").append(
				"ChileindicaPreinversionEntity p join p.updateInstance as e ")
				.append("where e.enabled = true ");

		// Check for nullabillity of parameters
		if (anyo != null) {
			hql.append("and p.anyo = :anyo ");
			parameterNames.add("anyo");
			parameterValues.add(anyo);
		}
		hql.append("and p.fuenteFinanciamiento is not null ")
				.append("and trim(p.fuenteFinanciamiento) != '' ")
				.append("order by p.fuenteFinanciamiento asc ");

		return getHibernateTemplate().findByNamedParam(hql.toString(),
				parameterNames.toArray(new String[] {}),
				parameterValues.toArray(new Object[] {}));

	}

	@SuppressWarnings("unchecked")
	@Override
	public List<String> getFuenteFinanciamientoEjecucionOrderAsc(String anyo) {
		List<String> parameterNames = new ArrayList<String>(1);
		List<Object> parameterValues = new ArrayList<Object>(1);
		StringBuilder hql = new StringBuilder(
				"select distinct p.fuenteFinanciamiento from ")
				.append("ChileindicaEjecucionDetalleEntity p join p.updateInstance as e ")
				.append("where e.enabled = true ");

		// Check for nullabillity of parameters
		if (anyo != null) {
			hql.append("and p.anyo = :anyo ");
			parameterNames.add("anyo");
			parameterValues.add(anyo);
		}

		hql.append("and p.fuenteFinanciamiento is not null ")
				.append("and trim(p.fuenteFinanciamiento) != '' ")
				.append("and p.fuenteFinanciamiento != 'Fndr' ")
				.append("order by p.fuenteFinanciamiento asc ");

		return getHibernateTemplate().findByNamedParam(hql.toString(),
				parameterNames.toArray(new String[] {}),
				parameterValues.toArray(new Object[] {}));
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<String> getLineaFinancieraPreinversionOrderAsc(String anyo,
			String fuenteFinanciamiento) {
		List<String> parameterNames = new ArrayList<String>(2);
		List<Object> parameterValues = new ArrayList<Object>(2);
		StringBuilder hql = new StringBuilder(
				"select distinct p.viaDeFinanciamiento from ")
				.append("ChileindicaPreinversionEntity as p join p.updateInstance as e ")
				.append("where e.enabled = true ");

		// Check for nullabillity of parameters
		if (anyo != null) {
			hql.append("and p.anyo = :anyo ");
			parameterNames.add("anyo");
			parameterValues.add(anyo);

		}
		if (fuenteFinanciamiento != null) {
			hql.append("and p.fuenteFinanciamiento = :fuenteFinanciamiento ");
			parameterNames.add("fuenteFinanciamiento");
			parameterValues.add(fuenteFinanciamiento);
		}

		hql.append("and p.viaDeFinanciamiento is not null ")
				.append("and trim(p.viaDeFinanciamiento) != '' ")
				.append("order by p.viaDeFinanciamiento asc ");

		return getHibernateTemplate().findByNamedParam(hql.toString(),
				parameterNames.toArray(new String[] {}),
				parameterValues.toArray(new Object[] {}));
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<String> getLineaFinancieraEjecucionOrderAsc(String anyo,
			String fuenteFinanciamiento) {
		List<String> parameterNames = new ArrayList<String>(2);
		List<Object> parameterValues = new ArrayList<Object>(2);
		StringBuilder hql = new StringBuilder(
				"select distinct p.servicioResponsable from ")
				.append("ChileindicaEjecucionDetalleEntity as p join p.updateInstance as e ")
				.append("where e.enabled = true ");

		// Check for nullabillity of parameters
		if (anyo != null) {
			hql.append("and p.anyo = :anyo ");
			parameterNames.add("anyo");
			parameterValues.add(anyo);

		}
		if (fuenteFinanciamiento != null) {
			hql.append("and p.fuenteFinanciamiento = :fuenteFinanciamiento ");
			parameterNames.add("fuenteFinanciamiento");
			parameterValues.add(fuenteFinanciamiento);
		}

		hql.append("and p.servicioResponsable is not null ")
				.append("and trim(p.servicioResponsable) != '' ")
				.append("order by p.servicioResponsable asc ");

		return getHibernateTemplate().findByNamedParam(hql.toString(),
				parameterNames.toArray(new String[] {}),
				parameterValues.toArray(new Object[] {}));

	}

	@SuppressWarnings("unchecked")
	@Override
	public List<String> getSectoresPreinversionOrderAsc(String anyo,
			String fuenteFinanciamiento, String lineaFinanciera) {
		List<String> parameterNames = new ArrayList<String>(3);
		List<Object> parameterValues = new ArrayList<Object>(3);

		StringBuilder hql = new StringBuilder("select distinct p.sector from ")
				.append("ChileindicaPreinversionEntity as p join p.updateInstance as e ")
				.append("where e.enabled = true ");

		// Check for nullabillity of parameters
		if (anyo != null) {
			hql.append("and p.anyo = :anyo ");
			parameterNames.add("anyo");
			parameterValues.add(anyo);

		}
		if (fuenteFinanciamiento != null) {
			hql.append("and p.fuenteFinanciamiento = :fuenteFinanciamiento ");
			parameterNames.add("fuenteFinanciamiento");
			parameterValues.add(fuenteFinanciamiento);
		}
		if (lineaFinanciera != null) {
			hql.append("and p.viaDeFinanciamiento = :lineaFinanciera ");
			parameterNames.add("lineaFinanciera");
			parameterValues.add(lineaFinanciera);
		}

		hql.append("and p.sector is not null ")
				.append("and trim(p.sector) != '' ")
				.append("order by p.sector asc ");
		return getHibernateTemplate().findByNamedParam(hql.toString(),
				parameterNames.toArray(new String[] {}),
				parameterValues.toArray(new Object[] {}));
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<String> getSectoresEjecucionOrderAsc(String anyo,
			String fuenteFinanciamiento, String lineaFinanciera) {
		List<String> parameterNames = new ArrayList<String>(3);
		List<Object> parameterValues = new ArrayList<Object>(3);

		StringBuilder hql = new StringBuilder("select distinct p.sector from ")
				.append("ChileindicaEjecucionDetalleEntity as p join p.updateInstance as e ")
				.append("where e.enabled = true ");

		// Check for nullabillity of parameters
		if (anyo != null) {
			hql.append("and p.anyo = :anyo ");
			parameterNames.add("anyo");
			parameterValues.add(anyo);

		}
		if (fuenteFinanciamiento != null) {
			hql.append("and p.fuenteFinanciamiento = :fuenteFinanciamiento ");
			parameterNames.add("fuenteFinanciamiento");
			parameterValues.add(fuenteFinanciamiento);
		}
		if (lineaFinanciera != null) {
			hql.append("and p.servicioResponsable = :lineaFinanciera ");
			parameterNames.add("lineaFinanciera");
			parameterValues.add(lineaFinanciera);
		}

		hql.append("and p.sector is not null ")
				.append("and trim(p.sector) != '' ")
				.append("order by p.sector asc ");
		return getHibernateTemplate().findByNamedParam(hql.toString(),
				parameterNames.toArray(new String[] {}),
				parameterValues.toArray(new Object[] {}));
	}

	@SuppressWarnings("unchecked")
	@Override
	public Map<String, String> getInfoFichaInversion(String codigoBip,
			String etapa, String servicioResponsable, String anyo) {

		Map<String, String> info = new HashMap<String, String>();
		List<Object> resultQuery = new ArrayList<Object>();

		List<String> parameterNames = new ArrayList<String>(4);
		parameterNames.add("codigoBip");
		parameterNames.add("etapa");
		parameterNames.add("servicioResponsable");
		parameterNames.add("anyo");

		List<Object> parameterValues = new ArrayList<Object>(4);
		parameterValues.add(codigoBip);
		parameterValues.add(etapa.toLowerCase());
		parameterValues.add(servicioResponsable.toLowerCase());
		parameterValues.add(anyo);

		StringBuilder filtroPunto = new StringBuilder(
				"and p.codigoBip like :codigoBip ||'%' ")
				.append("and lower(p.etapa) like :etapa ")
				.append("and lower(p.servicioResponsable) like :servicioResponsable ")
				.append("and p.anyo = :anyo ");

		// Registro ChileindicaPreinversion
		StringBuilder chileindicaPreinversionQuery = new StringBuilder(
				"select p from ").append("ChileindicaPreinversionEntity p ")
				.append("where p.updateInstance.enabled = true ")
				.append(filtroPunto);

		resultQuery = getHibernateTemplate().findByNamedParam(
				chileindicaPreinversionQuery.toString(),
				parameterNames.toArray(new String[] {}),
				parameterValues.toArray(new Object[] {}));

		ChileindicaPreinversionEntity chileindicaPreinversion = null;
		if (!resultQuery.isEmpty()) {
			chileindicaPreinversion = (ChileindicaPreinversionEntity) resultQuery
					.get(0);
		}

		// Registro BasePreinversionGore
		StringBuilder basePreinversionGoreQuery = new StringBuilder(
				"select p from ")
				.append("BasePreinversionGoreEntity p join p.updateInstance as e ")
				.append("where e.enabled = true ").append(filtroPunto);

		resultQuery = getHibernateTemplate().findByNamedParam(
				basePreinversionGoreQuery.toString(),
				parameterNames.toArray(new String[] {}),
				parameterValues.toArray(new Object[] {}));

		BasePreinversionGoreEntity basePreinversionGore = null;
		if (!resultQuery.isEmpty()) {
			basePreinversionGore = (BasePreinversionGoreEntity) resultQuery
					.get(0);
		}

		// Registro ProyectosMideso
		StringBuilder proyectosMidesoQuery = new StringBuilder("select p from ")
				.append("ProyectosMidesoEntity p join p.updateInstance as e ")
				.append("where e.enabled = true ").append(filtroPunto);

		resultQuery = getHibernateTemplate().findByNamedParam(
				proyectosMidesoQuery.toString(),
				parameterNames.toArray(new String[] {}),
				parameterValues.toArray(new Object[] {}));

		ProyectosMidesoEntity proyectosMideso = null;
		if (!resultQuery.isEmpty()) {
			proyectosMideso = (ProyectosMidesoEntity) resultQuery.get(0);
		}

		// Registro ProyectosDacg
		StringBuilder proyectosDacgQuery = new StringBuilder("select p from ")
				.append("ProyectosDacgEntity p join p.updateInstance as e ")
				.append("where e.enabled = true ").append(filtroPunto);

		resultQuery = getHibernateTemplate().findByNamedParam(
				proyectosDacgQuery.toString(),
				parameterNames.toArray(new String[] {}),
				parameterValues.toArray(new Object[] {}));

		ProyectosDacgEntity proyectosDacg = null;
		if (!resultQuery.isEmpty()) {
			proyectosDacg = (ProyectosDacgEntity) resultQuery.get(0);
		}

		// Registro AcuerdosCore
		StringBuilder acuerdosCoreQuery = new StringBuilder("select p from ")
				.append("AcuerdosCoreEntity p join p.updateInstance as e ")
				.append("where e.enabled = true ").append(filtroPunto);

		resultQuery = getHibernateTemplate().findByNamedParam(
				acuerdosCoreQuery.toString(),
				parameterNames.toArray(new String[] {}),
				parameterValues.toArray(new Object[] {}));

		AcuerdosCoreEntity acuerdosCore = null;
		if (!resultQuery.isEmpty()) {
			acuerdosCore = (AcuerdosCoreEntity) resultQuery.get(0);
		}

		// Registro ChileIndicaEjecucion
		StringBuilder chileindicaEjecucionQuery = new StringBuilder(
				"select p from ")
				.append("ChileindicaEjecucionEntity p join p.updateInstance as e ")
				.append("where e.enabled = true ").append(filtroPunto);

		resultQuery = getHibernateTemplate().findByNamedParam(
				chileindicaEjecucionQuery.toString(),
				parameterNames.toArray(new String[] {}),
				parameterValues.toArray(new Object[] {}));

		ChileindicaEjecucionEntity chileindicaEjecucion = null;
		if (!resultQuery.isEmpty()) {
			chileindicaEjecucion = (ChileindicaEjecucionEntity) resultQuery
					.get(0);
		}

		// Registro ChileIndicaEjecucionDetalle
		StringBuilder chileindicaEjecucionDetalleQuery = new StringBuilder(
				"select p from ")
				.append("ChileindicaEjecucionDetalleEntity p join p.updateInstance as e ")
				.append("where e.enabled = true ").append(filtroPunto);

		resultQuery = getHibernateTemplate().findByNamedParam(
				chileindicaEjecucionDetalleQuery.toString(),
				parameterNames.toArray(new String[] {}),
				parameterValues.toArray(new Object[] {}));

		ChileindicaEjecucionDetalleEntity chileindicaEjecucionDetalle = null;
		if (!resultQuery.isEmpty()) {
			chileindicaEjecucionDetalle = (ChileindicaEjecucionDetalleEntity) resultQuery
					.get(0);
		}

		// booleano que indica que la fuente de financiamiento es FNDR
		boolean fuenteFinanciamientoFndr = false;

		if (chileindicaPreinversion != null) {
			if (chileindicaPreinversion.getFuenteFinanciamiento() != null
					&& chileindicaPreinversion.getFuenteFinanciamiento()
							.toLowerCase().equals("fndr")) {
				// TODO
				fuenteFinanciamientoFndr = true;
			}

			info.put("institucionPostula",
					chileindicaPreinversion.getServicioResponsable());
			info.put("estadoPI", chileindicaPreinversion.getEstado());
			info.put("anyo", chileindicaPreinversion.getAnyo());
			info.put("fuenteFinanciamiento",
					chileindicaPreinversion.getFuenteFinanciamiento());
			info.put("lineaFinanciera",
					chileindicaPreinversion.getViaDeFinanciamiento());
			info.put("montoTotal",
					currencyFmt.format(chileindicaPreinversion.getCostoTotal())
							+ " CL$");
			info.put("sector", chileindicaPreinversion.getSector());
			info.put("etapa", chileindicaPreinversion.getEtapa());
			info.put("ubicacion", chileindicaPreinversion.getComuna());
			info.put("descripcion", chileindicaPreinversion.getObservaciones());
			if (fuenteFinanciamientoFndr && acuerdosCore!=null && StringUtils.isNotBlank(acuerdosCore.getNumeroAcuerdo()) ){
				info.put("institucionApruebaRecursos", "Consejo  (CORE)");
			}
		}

		if (basePreinversionGore != null) {
			info.put("oficio", basePreinversionGore.getNroOfInstitucion());
			info.put("carpeta", basePreinversionGore.getCarpetaDigitalBip());
			info.put("ingresado",
					basePreinversionGore.getIngresadoModPreinvChilenindica());
			if (StringUtils.isBlank(basePreinversionGore
					.getAtributosQuePresenta())
					|| StringUtils.isBlank(basePreinversionGore
							.getTotalDeAtributos())) {
				info.put("requisitos", "");
			} else {
				info.put("requisitos",
						basePreinversionGore.getAtributosQuePresenta() + " de "
								+ basePreinversionGore.getTotalDeAtributos());
			}
			info.put("vinculacion", basePreinversionGore.getErd());
			info.put("lineamiento", basePreinversionGore.getLineamientoUde());
			info.put("politicaTurismo",
					basePreinversionGore.getPoliticaRegionalTurismo());
			info.put("politicaCiencia",
					basePreinversionGore.getPoliticaRegionalCyt());
			info.put("plan", basePreinversionGore.getPlanOhiggins20102014());
			info.put("reconstruccion",
					basePreinversionGore.getPlanReconstruc27f());
			info.put("programacion",
					basePreinversionGore.getConvenioProgramacion());
			info.put("autoridadRegional",
					basePreinversionGore.getSeleccionadaPorIntendente());
			info.put("mesa", basePreinversionGore.getMesaTecnica());
			info.put("observaciones",
					basePreinversionGore.getNotasObservaciones());
		} 

		if (proyectosDacg != null && fuenteFinanciamientoFndr) {
			info.put("institucionEvalua", "Gobierno Regional");
			info.put("estadoETE", proyectosDacg.getObservacion());
			info.put("fechaEstado", proyectosDacg.getFechaObservacion());
		} else if (proyectosMideso != null
				&& proyectosMideso.getFechaIngresoSNI() != null) {
			info.put("institucionEvalua", "Ministerio de Desarrollo Social");
			info.put("estadoETE", proyectosMideso.getRate());
			info.put("fechaEstado", "Aprobado por Ley de Presupuesto año "
					+ proyectosMideso.getFechaRate());
		} else {
			info.put("institucionEvalua", "");
			info.put("estadoETE", "");
			info.put("fechaEstado", "");
		}

		if (acuerdosCore != null) {
			info.put("estadoRF", "Aprobado");
			info.put("nacuerdo", acuerdosCore.getNumeroAcuerdo());
			info.put("fechaAcuerdo", acuerdosCore.getFechaAcuerdo());
			info.put("montoAprobado",
					formatCurrency(acuerdosCore.getCostoTotalMs()));
		} else {
			info.put("estadoRF", "");
			info.put("nacuerdo", "");
			info.put("fechaAcuerdo", "");
			info.put("montoAprobado", "");
		}

		if (chileindicaEjecucionDetalle != null) {
			info.put("unidad", chileindicaEjecucionDetalle.getUnidadTecnica());
		} else {
			info.put("unidad", "");
		}

		if (chileindicaEjecucion != null) {
			info.put("institucionGasto",
					chileindicaEjecucion.getServicioResponsable());
			info.put(
					"gastado",
					formatCurrency(chileindicaEjecucion
							.getGastadoAnyosAnteriores()));
			info.put("pagado",
					formatCurrency(chileindicaEjecucion.getTotalPagado()));
		} else {
			info.put("institucionGasto", "");
			info.put("gastado", "");
			info.put("pagado", "");
		}

		return info;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public Map<String, String> getInfoFichaEjecucion(String codigoBip,
			String etapa, String servicioResponsable, String anyo) {

		Map<String, String> info = new HashMap<String, String>();
		List<Object> resultQuery = new ArrayList<Object>();

		List<String> parameterNames = new ArrayList<String>(4);
		parameterNames.add("codigoBip");
		parameterNames.add("etapa");
		parameterNames.add("servicioResponsable");
		parameterNames.add("anyo");

		List<Object> parameterValues = new ArrayList<Object>(4);
		parameterValues.add(codigoBip);
		parameterValues.add(etapa.toLowerCase());
		parameterValues.add(servicioResponsable.toLowerCase());
		parameterValues.add(anyo);

		StringBuilder filtroPunto = new StringBuilder(
				"and p.codigoBip like :codigoBip ||'%' ")
				.append("and lower(p.etapa) like :etapa ")
				.append("and lower(p.servicioResponsable) like :servicioResponsable ")
				.append("and p.anyo = :anyo ");

		// Registro ChileIndicaEjecucionDetalle
		StringBuilder chileindicaEjecucionDetalleQuery = new StringBuilder(
				"select p from ")
				.append("ChileindicaEjecucionDetalleEntity p join p.updateInstance as e ")
				.append("where e.enabled = true ").append(filtroPunto);

		resultQuery = getHibernateTemplate().findByNamedParam(
				chileindicaEjecucionDetalleQuery.toString(),
				parameterNames.toArray(new String[] {}),
				parameterValues.toArray(new Object[] {}));

		ChileindicaEjecucionDetalleEntity chileindicaEjecucionDetalle = null;
		if (!resultQuery.isEmpty()) {
			chileindicaEjecucionDetalle = (ChileindicaEjecucionDetalleEntity) resultQuery
					.get(0);
		}
		
		// Registro AcuerdosCore
		StringBuilder acuerdosCoreQuery = new StringBuilder("select p from ")
				.append("AcuerdosCoreEntity p join p.updateInstance as e ")
				.append("where e.enabled = true ").append(filtroPunto);

		resultQuery = getHibernateTemplate().findByNamedParam(
				acuerdosCoreQuery.toString(),
				parameterNames.toArray(new String[] {}),
				parameterValues.toArray(new Object[] {}));

		AcuerdosCoreEntity acuerdosCore = null;
		if (!resultQuery.isEmpty()) {
			acuerdosCore = (AcuerdosCoreEntity) resultQuery.get(0);
		}
		
		// Registro ProyectosMideso
		StringBuilder proyectosMidesoQuery = new StringBuilder("select p from ")
				.append("ProyectosMidesoEntity p join p.updateInstance as e ")
				.append("where e.enabled = true ").append(filtroPunto);

		resultQuery = getHibernateTemplate().findByNamedParam(
				proyectosMidesoQuery.toString(),
				parameterNames.toArray(new String[] {}),
				parameterValues.toArray(new Object[] {}));

		ProyectosMidesoEntity proyectosMideso = null;
		if (!resultQuery.isEmpty()) {
			proyectosMideso = (ProyectosMidesoEntity) resultQuery.get(0);
		}

		if (chileindicaEjecucionDetalle != null) {

			info.put("institucionPostula",
					chileindicaEjecucionDetalle.getServicioResponsable());
			info.put("estadoPI", "Aprobado");
			info.put("anyo", chileindicaEjecucionDetalle.getAnyo());
			info.put("fuenteFinanciamiento",
					chileindicaEjecucionDetalle.getFuenteFinanciamiento());
			info.put("lineaFinanciera",
					chileindicaEjecucionDetalle.getServicioResponsable());
			info.put("montoTotal",
					currencyFmt.format(chileindicaEjecucionDetalle.getCostoAjustado())
							+ " CL$");
			info.put("sector", chileindicaEjecucionDetalle.getSector());
			info.put("etapa", chileindicaEjecucionDetalle.getEtapa());
			info.put("ubicacion", chileindicaEjecucionDetalle.getComuna());
			info.put("descripcion", chileindicaEjecucionDetalle.getDescripcion());
			
			info.put("institucionGasto",
					chileindicaEjecucionDetalle.getServicioResponsable());
			info.put(
					"gastado",
					formatCurrency(chileindicaEjecucionDetalle
							.getGastadoAnyosAnteriores()));
			info.put("pagado",
					formatCurrency(chileindicaEjecucionDetalle.getPagado()));
			
			info.put("unidad", chileindicaEjecucionDetalle.getUnidadTecnica());
			
		}
		
		if (acuerdosCore != null) {
			info.put("estadoRF", "Aprobado");
			info.put("nacuerdo", acuerdosCore.getNumeroAcuerdo());
			info.put("fechaAcuerdo", acuerdosCore.getFechaAcuerdo());
			info.put("montoAprobado",
					formatCurrency(acuerdosCore.getCostoTotalMs()));
		} else {
			info.put("estadoRF", "");
			info.put("nacuerdo", "");
			info.put("fechaAcuerdo", "");
			info.put("montoAprobado", "");
		}

		if(proyectosMideso == null) {
			info.put("institucionEvalua", "");
			info.put("estadoETE", "");
			info.put("fechaEstado", "");
		} else if(proyectosMideso.getCodigoBip() == chileindicaEjecucionDetalle.getCodigoBip()) {
			info.put("institucionEvalua", "Ministerio de Desarrollo Social");
			info.put("estadoETE", proyectosMideso.getRate());
			info.put("fechaEstado", proyectosMideso.getFechaRate());
		} else {
			info.put("institucionEvalua", chileindicaEjecucionDetalle.getServicioResponsable());
			info.put("estadoETE","Aprobado");
			info.put("fechaEstado", "Aprobado por Ley de Presupuesto año " + chileindicaEjecucionDetalle.getAnyo());
		} 
		
		return info;
	}

	private String formatCurrency(BigDecimal currency) {
		if (currency == null) {
			return "-";
		}

		return currencyFmt.format(currency) + " CL$";

	}

	@SuppressWarnings("unchecked")
	public String getNameInversion(String codigoBip) {

		List<Object> resultQuery = new ArrayList<Object>();

		List<String> parameterNames = new ArrayList<String>(4);
		parameterNames.add("codigoBip");

		List<Object> parameterValues = new ArrayList<Object>(4);
		parameterValues.add(codigoBip);

		StringBuilder query = new StringBuilder("select p.nombre from ")
				.append("ShpProyectosGeorreferenciadosEntity p ")
				.append("where p.updateInstance.enabled = true ")
				.append("and p.codBip like :codigoBip ||'%' ");

		resultQuery = getHibernateTemplate().findByNamedParam(query.toString(),
				parameterNames.toArray(new String[] {}),
				parameterValues.toArray(new Object[] {}));

		String name = "";
		if (!resultQuery.isEmpty()) {
			name = (String) resultQuery.get(0);
		}

		return name;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Map<String, Object>> getMontosPreinversionGroupBy(String anyo,
			String agruparPor, String fuente, String lineaFinanciera,
			String sector, String nivelTerritorial) {
		List<String> parameterNames = Lists.newArrayList();
		List<Object> parameterValues = Lists.newArrayList();

		boolean checkComuna = false;

		StringBuilder hql = new StringBuilder(
				"select new map(sum(p.solicitadoAnyo)/1000 as monto, count(*) as numProyectos 	");
		if (StringUtils.equalsIgnoreCase("nivelTerritorial", agruparPor)) {
			hql.append(", coalesce(upper(p.comuna), '') as groupBy ");
			checkComuna = true;
		} else if (StringUtils.equalsIgnoreCase("fuente", agruparPor)) {
			hql.append(", coalesce(p.fuenteFinanciamiento, '') as groupBy ");
		} else if (StringUtils.equalsIgnoreCase("sector", agruparPor)) {
			hql.append(", coalesce(p.sector, '') as groupBy ");
		}

		hql.append(
				") from ChileindicaPreinversionEntity as p join p.updateInstance as e ")
				.append("where e.enabled = true ");

		if (anyo != null) {
			hql.append("and p.anyo = :anyo ").append("and p.anyo is not null ")
					.append("and trim(p.anyo) != '' ");
			parameterNames.add("anyo");
			parameterValues.add(anyo);

		}
		if (fuente != null) {
			hql.append("and p.fuenteFinanciamiento = :fuenteFinanciamiento ")
					.append("and p.fuenteFinanciamiento is not null ")
					.append("and trim(p.fuenteFinanciamiento) != '' ");
			parameterNames.add("fuenteFinanciamiento");
			parameterValues.add(fuente);
		}
		if (lineaFinanciera != null) {
			hql.append("and p.viaDeFinanciamiento = :lineaFinanciera ")
					.append("and p.viaDeFinanciamiento is not null ")
					.append("and trim(p.viaDeFinanciamiento) != '' ");
			parameterNames.add("lineaFinanciera");
			parameterValues.add(lineaFinanciera);
		}
		if (sector != null) {
			hql.append("and p.sector = :sector ");
			parameterNames.add("sector");
			parameterValues.add(sector);
		}
		if (nivelTerritorial != null) {
			hql.append("and upper(p.comuna) = upper(:nivelTerritorial) ");
			hql.append("and p.comuna is not null ").append(
					"and trim(p.comuna) != '' ");
			parameterNames.add("nivelTerritorial");
			parameterValues.add(nivelTerritorial);
		}

		// Group by
		if (StringUtils.equalsIgnoreCase("nivelTerritorial", agruparPor)) {
			hql.append("and locate(',', p.comuna) = 0 ").append(
					" group by upper(p.comuna) order by groupBy ");
		} else if (StringUtils.equalsIgnoreCase("fuente", agruparPor)) {
			hql.append("group by p.fuenteFinanciamiento order by groupBy ");
		} else if (StringUtils.equalsIgnoreCase("sector", agruparPor)) {
			hql.append("group by p.sector order by groupBy ");
		}

		List<Map<String, Object>> result = getHibernateTemplate()
				.findByNamedParam(hql.toString(),
						parameterNames.toArray(new String[] {}),
						parameterValues.toArray(new Object[] {}));

		// Replace blank 'comuna' by 'Sin identificación'
		if (checkComuna) {
			for (Map<String, Object> r : result) {
				if (StringUtils.isBlank((String) r.get("groupBy"))) {
					r.put("groupBy", EMPTY_COMUNA_STRING);
				}

			}
		}

		return result;

	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Map<String, Object>> getMontosEjecucionGroupBy(String anyo,
			String agruparPor, String fuente, String lineaFinanciera,
			String sector, String nivelTerritorial) {
		List<String> parameterNames = Lists.newArrayList();
		List<Object> parameterValues = Lists.newArrayList();
		boolean checkComuna = false;

		StringBuilder hql = new StringBuilder(
				"select new map(sum(p.solicitado)/1000 as monto, count(*) as numProyectos 	");
		if (StringUtils.equalsIgnoreCase("nivelTerritorial", agruparPor)) {
			hql.append(", coalesce(upper(p.comuna), '') as groupBy ");
			checkComuna = true;
		} else if (StringUtils.equalsIgnoreCase("fuente", agruparPor)) {
			hql.append(", coalesce(p.fuenteFinanciamiento, '') as groupBy ");
		} else if (StringUtils.equalsIgnoreCase("sector", agruparPor)) {
			hql.append(", coalesce(p.sector, '') as groupBy ");
		}

		hql.append(
				") from ChileindicaEjecucionDetalleEntity as p join p.updateInstance as e ")
				.append("where e.enabled = true ");

		if (anyo != null) {
			hql.append("and p.anyo = :anyo ").append("and p.anyo is not null ")
					.append("and trim(p.anyo) != '' ");
			parameterNames.add("anyo");
			parameterValues.add(anyo);

		}
		if (fuente != null) {
			hql.append("and p.fuenteFinanciamiento = :fuenteFinanciamiento ")
					.append("and p.fuenteFinanciamiento is not null ")
					.append("and trim(p.fuenteFinanciamiento) != '' ");
			parameterNames.add("fuenteFinanciamiento");
			parameterValues.add(fuente);
		}
		
		// fuente financiera no FNDR
		hql.append("and p.fuenteFinanciamiento <> 'Fndr' ");
		
		
		if (lineaFinanciera != null) {
			hql.append("and p.servicioResponsable = :lineaFinanciera ")
					.append("and p.servicioResponsable is not null ")
					.append("and trim(p.servicioResponsable) != '' ");
			parameterNames.add("lineaFinanciera");
			parameterValues.add(lineaFinanciera);
		}
		if (sector != null) {
			hql.append("and p.sector = :sector ");
			parameterNames.add("sector");
			parameterValues.add(sector);
		}
		if (nivelTerritorial != null) {
			hql.append("and upper(p.comuna) = upper(:nivelTerritorial) ");
			hql.append("and trim(p.comuna) != '' ").append(
					"and p.comuna is not null ");
			parameterNames.add("nivelTerritorial");
			parameterValues.add(nivelTerritorial);
		}

		// Group by
		if (StringUtils.equalsIgnoreCase("nivelTerritorial", agruparPor)) {
			hql.append("and locate(',', p.comuna) = 0 ").append(
					" group by upper(p.comuna) order by groupBy ");
		} else if (StringUtils.equalsIgnoreCase("fuente", agruparPor)) {
			hql.append("group by p.fuenteFinanciamiento order by groupBy ");
		} else if (StringUtils.equalsIgnoreCase("sector", agruparPor)) {
			hql.append("group by p.sector order by groupBy ");
		}

		List<Map<String, Object>> result = getHibernateTemplate()
				.findByNamedParam(hql.toString(),
						parameterNames.toArray(new String[] {}),
						parameterValues.toArray(new Object[] {}));

		// Replace blank 'comuna' by 'Sin identificación'
		if (checkComuna) {
			for (Map<String, Object> r : result) {
				if (StringUtils.isBlank((String) r.get("groupBy"))) {
					r.put("groupBy", EMPTY_COMUNA_STRING);
				}
			}
		}
		return result;

	}

	@SuppressWarnings("unchecked")
	@Override
	public List<InversionUtilDto> getProyectosGeoPreinversion(String anyo,
			String fuente, String lineaFinanciera, String sector,
			String nivelTerritorial) {
		List<String> parameterNames = Lists.newArrayList();
		List<Object> parameterValues = Lists.newArrayList();

		StringBuilder hql = new StringBuilder()
				.append("select new com.emergya.ohiggins.dto.InversionUtilDto(substr(cip.codigoBip, 1, 8) as cb, cip.etapa, cip.servicioResponsable, cip.anyo, shp.theGeom) ")
				.append("from ChileindicaPreinversionEntity cip, ShpProyectosGeorreferenciadosEntity shp ")
				.append("where cip.updateInstance.enabled = true and shp.updateInstance.enabled = true ")
				.append("and substr(cip.codigoBip, 1, 8) like substr(shp.codBip, 1, 8) ")
				.append("and cip.codigoBip is not null ")
				.append("and cip.etapa is not null ")
				.append("and cip.servicioResponsable is not null ")
				.append("and cip.anyo is not null ")
				.append("and shp.theGeom is not null ");

		if (anyo != null) {
			hql.append("and cip.anyo= :ANYO ");
			parameterNames.add("ANYO");
			parameterValues.add(anyo);
		}

		if (fuente != null) {
			hql.append("and upper(cip.fuenteFinanciamiento) like upper(:FUENTE_FINANCIAMIENTO) ");
			parameterNames.add("FUENTE_FINANCIAMIENTO");
			parameterValues.add(fuente);
		}

		if (lineaFinanciera != null) {
			hql.append("and upper(cip.viaDeFinanciamiento) like upper(:VIA_DE_FINANCIAMIENTO) ");
			parameterNames.add("VIA_DE_FINANCIAMIENTO");
			parameterValues.add(lineaFinanciera);
		}

		if (sector != null) {
			hql.append("and upper(cip.sector) like upper (:SECTOR) ");
			parameterNames.add("SECTOR");
			parameterValues.add(sector);
		}
		if (nivelTerritorial != null) {
			hql.append("and upper(cip.comuna) like upper(:COMUNA) ");
			parameterNames.add("COMUNA");
			parameterValues.add(nivelTerritorial);
		}

		hql.append("order by cb asc ");

		return getHibernateTemplate().findByNamedParam(hql.toString(),
				parameterNames.toArray(new String[] {}),
				parameterValues.toArray(new Object[] {}));

	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<InversionUtilDto> getProyectosGeoEjecucionDetalle(String anyo,
			String fuente, String lineaFinanciera, String sector,
			String nivelTerritorial) {
		List<String> parameterNames = Lists.newArrayList();
		List<Object> parameterValues = Lists.newArrayList();

		StringBuilder hql = new StringBuilder()
				.append("select new com.emergya.ohiggins.dto.InversionUtilDto(substr(cied.codigoBip, 1, 8) as cb, cied.etapa, cied.servicioResponsable, cied.anyo, shp.theGeom) ")
				.append("from ChileindicaEjecucionDetalleEntity cied, ShpProyectosGeorreferenciadosEntity shp ")
				.append("where cied.updateInstance.enabled = true and shp.updateInstance.enabled = true ")
				.append("and substr(cied.codigoBip, 1, 8) like substr(shp.codBip, 1, 8) ")
				.append("and cied.codigoBip is not null ")
				.append("and cied.etapa is not null ")
				.append("and cied.servicioResponsable is not null ")
				.append("and cied.anyo is not null ")
				.append("and shp.theGeom is not null ");

		if (anyo != null) {
			hql.append("and cied.anyo= :ANYO ");
			parameterNames.add("ANYO");
			parameterValues.add(anyo);
		}

		if (fuente != null) {
			hql.append("and upper(cied.fuenteFinanciamiento) like upper(:FUENTE_FINANCIAMIENTO) ");
			parameterNames.add("FUENTE_FINANCIAMIENTO");
			parameterValues.add(fuente);
		} else {
			//Descartamos las fuentes de financiamiento Fndr
			hql.append("and cied.fuenteFinanciamiento != 'Fndr' ");
			hql.append("order by cb asc ");
		}

		if (lineaFinanciera != null) {
			hql.append("and upper(cied.viaDeFinanciamiento) like upper(:VIA_DE_FINANCIAMIENTO) ");
			parameterNames.add("VIA_DE_FINANCIAMIENTO");
			parameterValues.add(lineaFinanciera);
		}

		if (sector != null) {
			hql.append("and upper(cied.sector) like upper (:SECTOR) ");
			parameterNames.add("SECTOR");
			parameterValues.add(sector);
		}
		if (nivelTerritorial != null) {
			hql.append("and upper(cied.comuna) like upper(:COMUNA) ");
			parameterNames.add("COMUNA");
			parameterValues.add(nivelTerritorial);
		}


		return getHibernateTemplate().findByNamedParam(hql.toString(),
				parameterNames.toArray(new String[] {}),
				parameterValues.toArray(new Object[] {}));

	}
}
