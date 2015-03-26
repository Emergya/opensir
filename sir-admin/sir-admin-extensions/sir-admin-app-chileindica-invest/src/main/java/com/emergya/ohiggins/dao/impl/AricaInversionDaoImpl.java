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

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
import org.springframework.stereotype.Repository;

import com.emergya.ohiggins.dao.AricaInversionDao;
import com.emergya.ohiggins.dto.InversionUtilDto;
import com.emergya.ohiggins.model.ChileindicaInversionDataEntity;
import com.emergya.ohiggins.model.ChileindicaInversionFinanciamientoDataEntity;
import com.google.common.collect.Lists;
import org.apache.commons.lang.WordUtils;


@Repository("aricaInversionDao")
public class AricaInversionDaoImpl extends HibernateDaoSupport implements
		AricaInversionDao {

	private static final String EMPTY_COMUNA_STRING = "SIN IDENTIFICACIÓN";
	private final NumberFormat currencyFmt = NumberFormat.getInstance(new Locale(
			"es", "CL"));

	@Autowired
	public void init(SessionFactory sessionFactory) {
		setSessionFactory(sessionFactory);
	}

	@SuppressWarnings("unchecked")
	@Override
	public SortedSet<String> getNivelesTerritorialesOrderDesc() {

		SortedSet<String> zones = new TreeSet<String>(String.CASE_INSENSITIVE_ORDER);

		List<String> queryResult;
		String hql = "select distinct p.nombreComuna from ChileindicaInversionDataEntity p "
				+ " where  p.nombreComuna is not null and update_status = 'OK' "
				+ "order by p.nombreComuna desc ";
		queryResult = getHibernateTemplate().find(hql);

		for (String e: queryResult){
			if (!StringUtils.isBlank(e)) {
				String[] pieces = e.trim().split("\\s*,\\s*");
				for(String piece : pieces) {
				    zones.add(WordUtils.capitalizeFully(piece));
				}
			}
		}
		return zones;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Integer> getAnyosOrderDesc() {
		        List<Integer> anyos;
		        String hql = "select distinct p.ano from ChileindicaInversionDataEntity p "
		                + " where  p.ano is not null "
		                + "order by p.ano desc ";
		        anyos = getHibernateTemplate().find(hql);

		return anyos;
	}
	


	@SuppressWarnings("unchecked")
	@Override
	public List<String> getFuenteFinanciamientoOrderAsc() {

        List<String> result;
		
		StringBuilder hql = new StringBuilder(
				"select distinct p.nombreFuenteFinanciamiento from ")
		.append(
				"ChileindicaInversionFinanciamientoDataEntity as p ")
				.append("where p.nombreFuenteFinanciamiento is not null ")
				.append("and trim(p.nombreFuenteFinanciamiento) != '' ")
				.append("order by p.nombreFuenteFinanciamiento asc ");

		result = getHibernateTemplate().find(hql.toString());
		return result;
	}


	@SuppressWarnings("unchecked")
	@Override
	public List<String> getItemPresupuestarioOrderAsc() { 
        List<String> result;

		
		StringBuilder hql = new StringBuilder(
				"select distinct p.nombreItemPresupuestario from ")
				.append("ChileindicaInversionDataEntity as p ")
				.append("where ");

		hql.append("p.nombreItemPresupuestario is not null ")
				.append("and trim(p.nombreItemPresupuestario) != '' ")
				.append("order by p.nombreItemPresupuestario asc ");

		result = getHibernateTemplate().find(hql.toString());
        
                return result;

	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<Map<String, Object>> getMontosGroupBy(String anyo,
			String agruparPor, String tipoProyecto, String itemPresupuestario, String nivelTerritorial){
		List<String> parameterNames = Lists.newArrayList();
		List<Object> parameterValues = Lists.newArrayList();

		boolean groupedByComuna = false;

		StringBuilder hql = new StringBuilder(
				"select new map(sum(p.costoTotalAjustadoInversion)/1000.0 as monto, count(*) as numProyectos");
		if (StringUtils.equalsIgnoreCase("nivelTerritorial", agruparPor)) {
			hql.append(", coalesce(upper(p.nombreComuna), '') as groupBy ");
			groupedByComuna = true;
		} else if (StringUtils.equalsIgnoreCase("anyo", agruparPor)) {
			hql.append(", p.ano as groupBy ");
		} 

		hql.append(") from ChileindicaInversionDataEntity as p join p.financiamientosList as financiamiento ")
			.append("where p.updateStatus = 'OK' ");

		if (!StringUtils.isBlank(anyo) && !anyo.equalsIgnoreCase("Todos")) {
			hql.append("and p.ano = :anyo ");
			parameterNames.add("anyo");
			parameterValues.add(Integer.parseInt(anyo));

		}
		
		if (!StringUtils.isBlank(tipoProyecto)) {
			hql.append("and financiamiento.nombreFuenteFinanciamiento = :nombreFuenteFinanciamiento ");				
			parameterNames.add("nombreFuenteFinanciamiento");
			parameterValues.add(tipoProyecto);
		}
		
		if (!StringUtils.isBlank(itemPresupuestario)) {
			hql.append("and p.nombreItemPresupuestario = :nombreItemPresupuestario ");
			parameterNames.add("nombreItemPresupuestario");
			parameterValues.add(itemPresupuestario);
		}
	
		if (!StringUtils.isBlank(nivelTerritorial)) {

			String nivelTerritorialUpperCase = nivelTerritorial.toUpperCase();
			hql.append("and (upper(p.nombreComuna) = upper(:nivelTerritorial) ");
			hql.append("or upper(p.nombreComuna) like '%,").append(nivelTerritorialUpperCase).append("%' ");
			hql.append("or upper(p.nombreComuna) like '%").append(nivelTerritorialUpperCase).append(",%' ");
			hql.append("or upper(p.nombreComuna) like '%,").append(nivelTerritorialUpperCase).append(",%') ");


			parameterNames.add("nivelTerritorial");
			parameterValues.add(nivelTerritorial);
		}

		// Group by
		if (StringUtils.equalsIgnoreCase("nivelTerritorial", agruparPor)) {
			hql.append(" group by upper(p.nombreComuna) order by groupBy ");
		} else if (StringUtils.equalsIgnoreCase("anyo", agruparPor)) {
			hql.append(" group by ano order by groupBy ");
		} 

		List<Map<String, Object>> result = getHibernateTemplate()
				.findByNamedParam(hql.toString(),
						parameterNames.toArray(new String[] {}),
						parameterValues.toArray(new Object[] {}));

		if (!groupedByComuna) { 
		    return result;
		}
		
		// We need to further process the data, as comunas might
			// not appear in the data row or come several separated by commas.
		Map<String,Map<String, Object>> realResult = new TreeMap<String,Map<String, Object>> (String.CASE_INSENSITIVE_ORDER);
			
		for (Map<String, Object> r : result) {

			// Replace blank 'comuna' by 'Sin identificación'
			if (StringUtils.isBlank((String) r.get("groupBy"))) {
				r.put("groupBy", EMPTY_COMUNA_STRING);
			}

			//Divide comma separated fields.
			String group = ((String) r.get("groupBy")).toUpperCase();

			if(!StringUtils.isBlank(nivelTerritorial) 
				&& !group.equalsIgnoreCase(nivelTerritorial)) {
			    // Returned because of several comunas in one row separated by commas,but unwanted.
			    continue;
			}
			Double monto = (Double)r.get("monto");
			Long numProyectos = (Long)r.get("numProyectos");

			// We remove possible separators
			List<String> splitted = Arrays.asList(group.trim().split("\\s*,\\s*"));
			for (String key: splitted) {
				this.addOrSum(realResult, key, monto, numProyectos);
			}


		}
		 

		return new ArrayList<Map<String, Object>>(realResult.values());

	}

	private void addOrSum(Map<String, Map<String, Object>> realResult, String group, Double monto, Long numProyectos) {

		if (!realResult.containsKey(group)){
			Map <String, Object> tmp = new HashMap<String, Object>();
			tmp.put("groupBy", group);
			tmp.put("monto", monto);
			tmp.put("numProyectos", numProyectos);
			realResult.put((String) group, tmp);
		} else {
			Double montoSum = (Double) realResult.get(group).get("monto") + monto;
			realResult.get(group).put("monto", montoSum);

			Long numProyectosSum = (Long) realResult.get(group).get("numProyectos")+ numProyectos;
			realResult.get(group).put("numProyectos", numProyectosSum);
		}
	}

	@Override
	public List<InversionUtilDto> getProyectosGeo(
			String anyo,String financiamiento,  String itemPresupuestario,
			String nivelTerritorial)  {
		List<String> parameterNames = Lists.newArrayList();
		List<Object> parameterValues = Lists.newArrayList();

		StringBuilder hql = new StringBuilder()
            .append("select distinct new com.emergya.ohiggins.dto.InversionUtilDto(substr(cip.codigo, 1, 8) as cb, cip.nEtapaIdi, '', str(cip.ano), cip.theGeom) ")
            .append("from ChileindicaInversionDataEntity cip join cip.financiamientosList as financiamiento ")
            .append("where cip.updateStatus = 'OK' ")				
            .append("and cip.codigo is not null ")
            .append("and cip.nEtapaIdi is not null ")
            .append("and cip.ano is not null ")
            .append("and cip.theGeom is not null ");

		if (!StringUtils.isBlank(anyo) && !anyo.equalsIgnoreCase("Todos")) {
			hql.append("and cip.ano= :ANYO ");
			parameterNames.add("ANYO");
			try {
				parameterValues.add(Integer.parseInt(anyo));
			} catch (NumberFormatException e) {
				throw new RuntimeException(e);
			}
		}

		if (!StringUtils.isBlank(financiamiento)) {
			hql.append("and upper(financiamiento.nombreFuenteFinanciamiento) like upper(:VIA_DE_FINANCIAMIENTO) ");
			parameterNames.add("VIA_DE_FINANCIAMIENTO");
			parameterValues.add(financiamiento);
		}

		if (!StringUtils.isBlank(itemPresupuestario)) {
			hql.append("and upper(cip.nombreItemPresupuestario) like upper(:N_ITEM_PRESUPUETARIO) ");
			parameterNames.add("N_ITEM_PRESUPUETARIO");
			parameterValues.add(itemPresupuestario);
		}
		if (!StringUtils.isBlank(nivelTerritorial)) {
			//hql.append("and upper(cip.nombreComuna) like upper(:COMUNA) ");

			String nivelTerritorialUpperCase = nivelTerritorial.toUpperCase();
			hql.append("and (upper(cip.nombreComuna) = upper(:nivelTerritorial) ");
			hql.append("or upper(cip.nombreComuna) like '%,").append(nivelTerritorialUpperCase).append("%' ");
			hql.append("or upper(cip.nombreComuna) like '%").append(nivelTerritorialUpperCase).append(",%' ");
			hql.append("or upper(cip.nombreComuna) like '%,").append(nivelTerritorialUpperCase).append(",%') ");


			parameterNames.add("nivelTerritorial");
			parameterValues.add(nivelTerritorial);
		}

		hql.append("order by cb asc ");

		return getHibernateTemplate().findByNamedParam(hql.toString(),
				parameterNames.toArray(new String[] {}),
				parameterValues.toArray(new Object[] {}));
	}

	@Override
	public Map<String, String> getInfoFichaInversion(String codBip, String etapa, String anyo) {
		Map<String, String> info = new HashMap<String, String>();
		

		String[] parameterNames = new String[]{
				"codigoBip",
				"etapa",
				"anyo"
		};
		
		Object[] parameterValues = new Object[] {
				codBip,
				etapa,
				Integer.parseInt(anyo)
		};		

		StringBuilder filtroPunto = new StringBuilder(
				"and p.codigo like :codigoBip ||'%' ")
				.append("and lower(p.nEtapaIdi) like lower(:etapa) ")				
				.append("and p.ano = :anyo ");

		// Registro ChileindicaPreinversion
		StringBuilder chileindicaPreinversionQuery = new StringBuilder(
				"select p from ").append("ChileindicaInversionDataEntity p join fetch p.financiamientosList as financiamiento ")
				.append("where p.updateStatus='OK' ")
				.append(filtroPunto);

		
		List<Object> resultQuery = getHibernateTemplate().findByNamedParam(
				chileindicaPreinversionQuery.toString(),
				parameterNames, parameterValues);

		ChileindicaInversionDataEntity investmentData = null;
		if (!resultQuery.isEmpty()) {
			investmentData = (ChileindicaInversionDataEntity) resultQuery
					.get(0);
		}

		if(investmentData==null)  {
			return info;			
		}
		
		// Details 
		info.put("name", investmentData.getNombreProyecto());
		info.put("codBip", codBip);
		
		info.put("ano", investmentData.getAno().toString());
		info.put("etapa", investmentData.getNEtapaIdi());
		info.put("nombreInstitucionResponsable", investmentData.getNombreInstitucionResponsable());
		info.put("nRegion", investmentData.getnRegion());
		info.put("nombreUnidadTecnica", investmentData.getNombreUnidadTecnica());
		info.put("comuna", investmentData.getNombreComuna());
		
		// financing sources
		List<String> financingSources= new LinkedList<String>();
		for(ChileindicaInversionFinanciamientoDataEntity financing : investmentData.getFinanciamientosList()) {
            String financingSourceName = financing.getNombreFuenteFinanciamiento();
            if(!financingSources.contains(financingSourceName)){
                financingSources.add(financingSourceName);
            }
		}
		
		info.put("fuenteFinanciamiento", StringUtils.join(financingSources, ", "));
		info.put("costoTotalAjustado", formatCurrency(investmentData.getCostoTotalAjustadoInversion()));
		info.put("itemPresupuestario", investmentData.getItemPresupuestario());
		
		
		// investment execution
		
		info.put("gastadoAnosAnteriores", formatCurrency(investmentData.getGastadoAnosAnterioresInversion()));
		info.put("solicitadoAno", formatCurrency(investmentData.getSolicitadoAnoInversion()));
		info.put("saldoProximoAno", formatCurrency(investmentData.getSaldoProximoAnoInversion()));
		info.put("saldoAnosRestantes", formatCurrency(investmentData.getSaldoAnosRestantesInversion()));
		info.put("totalAsignado", formatCurrency(investmentData.getTotalAsignadoInversion()));
		info.put("totalPagado",formatCurrency(investmentData.getTotalPagadoInversion()));
		info.put("saldoPorAsignar", formatCurrency(investmentData.getSaldoPorAsignarInversion()));
		info.put("asignacionDisponible", formatCurrency(investmentData.getAsignacionDisponibleInversion()));		
		
		return info;
	}
	
	private String formatCurrency(Number number) {
		if(number==null) {
			return "-";
		}
		
		return String.format("%s $CL",
				currencyFmt.format(number));
	}
	
}