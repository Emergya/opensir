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
package com.emergya.ohiggins.model;

import java.io.Serializable;
import java.math.BigInteger;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 * 
 * @author <a href="mailto:jlrodriguez@emergya.com">Juan Luis Rodríguez
 *         Ponce</a>
 */
@Entity
@Table(name = "gis_inversion_financiamiento_data")
@NamedQueries({
		@NamedQuery(name = "ChileindicaInversionFinanciamientoDataEntity.findAll", query = "SELECT c FROM ChileindicaInversionFinanciamientoDataEntity c"),
		@NamedQuery(name = "ChileindicaInversionFinanciamientoDataEntity.findById", query = "SELECT c FROM ChileindicaInversionFinanciamientoDataEntity c WHERE c.id = :id"),
		@NamedQuery(name = "ChileindicaInversionFinanciamientoDataEntity.findByCodigoFuenteFinanciamiento", query = "SELECT c FROM ChileindicaInversionFinanciamientoDataEntity c WHERE c.codigoFuenteFinanciamiento = :codigoFuenteFinanciamiento"),
		@NamedQuery(name = "ChileindicaInversionFinanciamientoDataEntity.findByNombreFuenteFinanciamiento", query = "SELECT c FROM ChileindicaInversionFinanciamientoDataEntity c WHERE c.nombreFuenteFinanciamiento = :nombreFuenteFinanciamiento"),
		@NamedQuery(name = "ChileindicaInversionFinanciamientoDataEntity.findByAsignacionPresupuestaria", query = "SELECT c FROM ChileindicaInversionFinanciamientoDataEntity c WHERE c.asignacionPresupuestaria = :asignacionPresupuestaria"),
		@NamedQuery(name = "ChileindicaInversionFinanciamientoDataEntity.findByNombreAsignacionPresupuestaria", query = "SELECT c FROM ChileindicaInversionFinanciamientoDataEntity c WHERE c.nombreAsignacionPresupuestaria = :nombreAsignacionPresupuestaria"),
		@NamedQuery(name = "ChileindicaInversionFinanciamientoDataEntity.findByCostoTotalEbi", query = "SELECT c FROM ChileindicaInversionFinanciamientoDataEntity c WHERE c.costoTotalEbi = :costoTotalEbi"),
		@NamedQuery(name = "ChileindicaInversionFinanciamientoDataEntity.findByCostoTotalEbiActualizado", query = "SELECT c FROM ChileindicaInversionFinanciamientoDataEntity c WHERE c.costoTotalEbiActualizado = :costoTotalEbiActualizado"),
		@NamedQuery(name = "ChileindicaInversionFinanciamientoDataEntity.findByCostoTotalEbiActualizadoMasDiezPorciento", query = "SELECT c FROM ChileindicaInversionFinanciamientoDataEntity c WHERE c.costoTotalEbiActualizadoMasDiezPorciento = :costoTotalEbiActualizadoMasDiezPorciento"),
		@NamedQuery(name = "ChileindicaInversionFinanciamientoDataEntity.findByCostoTotalCore", query = "SELECT c FROM ChileindicaInversionFinanciamientoDataEntity c WHERE c.costoTotalCore = :costoTotalCore"),
		@NamedQuery(name = "ChileindicaInversionFinanciamientoDataEntity.findByCostoTotalAjustado", query = "SELECT c FROM ChileindicaInversionFinanciamientoDataEntity c WHERE c.costoTotalAjustado = :costoTotalAjustado"),
		@NamedQuery(name = "ChileindicaInversionFinanciamientoDataEntity.findByGastadoAnosAnteriores", query = "SELECT c FROM ChileindicaInversionFinanciamientoDataEntity c WHERE c.gastadoAnosAnteriores = :gastadoAnosAnteriores"),
		@NamedQuery(name = "ChileindicaInversionFinanciamientoDataEntity.findBySolicitadoAno", query = "SELECT c FROM ChileindicaInversionFinanciamientoDataEntity c WHERE c.solicitadoAno = :solicitadoAno"),
		@NamedQuery(name = "ChileindicaInversionFinanciamientoDataEntity.findBySaldoProximoAno", query = "SELECT c FROM ChileindicaInversionFinanciamientoDataEntity c WHERE c.saldoProximoAno = :saldoProximoAno"),
		@NamedQuery(name = "ChileindicaInversionFinanciamientoDataEntity.findBySaldoAnosRestantes", query = "SELECT c FROM ChileindicaInversionFinanciamientoDataEntity c WHERE c.saldoAnosRestantes = :saldoAnosRestantes"),
		@NamedQuery(name = "ChileindicaInversionFinanciamientoDataEntity.findByTotalAsignado", query = "SELECT c FROM ChileindicaInversionFinanciamientoDataEntity c WHERE c.totalAsignado = :totalAsignado"),
		@NamedQuery(name = "ChileindicaInversionFinanciamientoDataEntity.findByAsignacionDisponible", query = "SELECT c FROM ChileindicaInversionFinanciamientoDataEntity c WHERE c.asignacionDisponible = :asignacionDisponible"),
		@NamedQuery(name = "ChileindicaInversionFinanciamientoDataEntity.findBySaldoPorAsignar", query = "SELECT c FROM ChileindicaInversionFinanciamientoDataEntity c WHERE c.saldoPorAsignar = :saldoPorAsignar"),
		@NamedQuery(name = "ChileindicaInversionFinanciamientoDataEntity.findByTotalContratado", query = "SELECT c FROM ChileindicaInversionFinanciamientoDataEntity c WHERE c.totalContratado = :totalContratado"),
		@NamedQuery(name = "ChileindicaInversionFinanciamientoDataEntity.findByTotalPagado", query = "SELECT c FROM ChileindicaInversionFinanciamientoDataEntity c WHERE c.totalPagado = :totalPagado"),
		@NamedQuery(name = "ChileindicaInversionFinanciamientoDataEntity.findByArrastre", query = "SELECT c FROM ChileindicaInversionFinanciamientoDataEntity c WHERE c.arrastre = :arrastre"),
		@NamedQuery(name = "ChileindicaInversionFinanciamientoDataEntity.findByTotalProgramado", query = "SELECT c FROM ChileindicaInversionFinanciamientoDataEntity c WHERE c.totalProgramado = :totalProgramado"),
		@NamedQuery(name = "ChileindicaInversionFinanciamientoDataEntity.findByEneroProgramado", query = "SELECT c FROM ChileindicaInversionFinanciamientoDataEntity c WHERE c.eneroProgramado = :eneroProgramado"),
		@NamedQuery(name = "ChileindicaInversionFinanciamientoDataEntity.findByFebreroProgramado", query = "SELECT c FROM ChileindicaInversionFinanciamientoDataEntity c WHERE c.febreroProgramado = :febreroProgramado"),
		@NamedQuery(name = "ChileindicaInversionFinanciamientoDataEntity.findByMarzoProgramado", query = "SELECT c FROM ChileindicaInversionFinanciamientoDataEntity c WHERE c.marzoProgramado = :marzoProgramado"),
		@NamedQuery(name = "ChileindicaInversionFinanciamientoDataEntity.findByAbrilProgramado", query = "SELECT c FROM ChileindicaInversionFinanciamientoDataEntity c WHERE c.abrilProgramado = :abrilProgramado"),
		@NamedQuery(name = "ChileindicaInversionFinanciamientoDataEntity.findByMayoProgramado", query = "SELECT c FROM ChileindicaInversionFinanciamientoDataEntity c WHERE c.mayoProgramado = :mayoProgramado"),
		@NamedQuery(name = "ChileindicaInversionFinanciamientoDataEntity.findByJunioProgramado", query = "SELECT c FROM ChileindicaInversionFinanciamientoDataEntity c WHERE c.junioProgramado = :junioProgramado"),
		@NamedQuery(name = "ChileindicaInversionFinanciamientoDataEntity.findByJulioProgramado", query = "SELECT c FROM ChileindicaInversionFinanciamientoDataEntity c WHERE c.julioProgramado = :julioProgramado"),
		@NamedQuery(name = "ChileindicaInversionFinanciamientoDataEntity.findByAgostoProgramado", query = "SELECT c FROM ChileindicaInversionFinanciamientoDataEntity c WHERE c.agostoProgramado = :agostoProgramado"),
		@NamedQuery(name = "ChileindicaInversionFinanciamientoDataEntity.findBySeptiembreProgramado", query = "SELECT c FROM ChileindicaInversionFinanciamientoDataEntity c WHERE c.septiembreProgramado = :septiembreProgramado"),
		@NamedQuery(name = "ChileindicaInversionFinanciamientoDataEntity.findByOctubreProgramado", query = "SELECT c FROM ChileindicaInversionFinanciamientoDataEntity c WHERE c.octubreProgramado = :octubreProgramado"),
		@NamedQuery(name = "ChileindicaInversionFinanciamientoDataEntity.findByNoviembreProgramado", query = "SELECT c FROM ChileindicaInversionFinanciamientoDataEntity c WHERE c.noviembreProgramado = :noviembreProgramado"),
		@NamedQuery(name = "ChileindicaInversionFinanciamientoDataEntity.findByDiciembreProgramado", query = "SELECT c FROM ChileindicaInversionFinanciamientoDataEntity c WHERE c.diciembreProgramado = :diciembreProgramado"),
		@NamedQuery(name = "ChileindicaInversionFinanciamientoDataEntity.findByEneroPagado", query = "SELECT c FROM ChileindicaInversionFinanciamientoDataEntity c WHERE c.eneroPagado = :eneroPagado"),
		@NamedQuery(name = "ChileindicaInversionFinanciamientoDataEntity.findByFebreroPagado", query = "SELECT c FROM ChileindicaInversionFinanciamientoDataEntity c WHERE c.febreroPagado = :febreroPagado"),
		@NamedQuery(name = "ChileindicaInversionFinanciamientoDataEntity.findByMarzoPagado", query = "SELECT c FROM ChileindicaInversionFinanciamientoDataEntity c WHERE c.marzoPagado = :marzoPagado"),
		@NamedQuery(name = "ChileindicaInversionFinanciamientoDataEntity.findByAbrilPagado", query = "SELECT c FROM ChileindicaInversionFinanciamientoDataEntity c WHERE c.abrilPagado = :abrilPagado"),
		@NamedQuery(name = "ChileindicaInversionFinanciamientoDataEntity.findByMayoPagado", query = "SELECT c FROM ChileindicaInversionFinanciamientoDataEntity c WHERE c.mayoPagado = :mayoPagado"),
		@NamedQuery(name = "ChileindicaInversionFinanciamientoDataEntity.findByJunioPagado", query = "SELECT c FROM ChileindicaInversionFinanciamientoDataEntity c WHERE c.junioPagado = :junioPagado"),
		@NamedQuery(name = "ChileindicaInversionFinanciamientoDataEntity.findByJulioPagado", query = "SELECT c FROM ChileindicaInversionFinanciamientoDataEntity c WHERE c.julioPagado = :julioPagado"),
		@NamedQuery(name = "ChileindicaInversionFinanciamientoDataEntity.findByAgostoPagado", query = "SELECT c FROM ChileindicaInversionFinanciamientoDataEntity c WHERE c.agostoPagado = :agostoPagado"),
		@NamedQuery(name = "ChileindicaInversionFinanciamientoDataEntity.findBySeptiembrePagado", query = "SELECT c FROM ChileindicaInversionFinanciamientoDataEntity c WHERE c.septiembrePagado = :septiembrePagado"),
		@NamedQuery(name = "ChileindicaInversionFinanciamientoDataEntity.findByOctubrePagado", query = "SELECT c FROM ChileindicaInversionFinanciamientoDataEntity c WHERE c.octubrePagado = :octubrePagado"),
		@NamedQuery(name = "ChileindicaInversionFinanciamientoDataEntity.findByNoviembrePagado", query = "SELECT c FROM ChileindicaInversionFinanciamientoDataEntity c WHERE c.noviembrePagado = :noviembrePagado"),
		@NamedQuery(name = "ChileindicaInversionFinanciamientoDataEntity.findByDiciembrePagado", query = "SELECT c FROM ChileindicaInversionFinanciamientoDataEntity c WHERE c.diciembrePagado = :diciembrePagado") })
public class ChileindicaInversionFinanciamientoDataEntity implements
		Serializable {

	private static final long serialVersionUID = 1796175692212448279L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Basic(optional = false)
	@Column(name = "id")
	private Long id;
	@Column(name = "codigo_fuente_financiamiento")
	private String codigoFuenteFinanciamiento;
	@Column(name = "nombre_fuente_financiamiento")
	private String nombreFuenteFinanciamiento;
	@Column(name = "asignacion_presupuestaria")
	private String asignacionPresupuestaria;
	@Column(name = "nombre_asignacion_presupuestaria")
	private String nombreAsignacionPresupuestaria;
	@Column(name = "costo_total_ebi")
	private BigInteger costoTotalEbi;
	@Column(name = "costo_total_ebi_actualizado")
	private BigInteger costoTotalEbiActualizado;
	@Column(name = "costo_total_ebi_actualizado_mas_diez_porciento")
	private BigInteger costoTotalEbiActualizadoMasDiezPorciento;
	@Column(name = "costo_total_core")
	private BigInteger costoTotalCore;
	@Column(name = "costo_total_ajustado")
	private BigInteger costoTotalAjustado;
	@Column(name = "gastado_anos_anteriores")
	private BigInteger gastadoAnosAnteriores;
	@Column(name = "solicitado_ano")
	private BigInteger solicitadoAno;
	@Column(name = "saldo_proximo_ano")
	private BigInteger saldoProximoAno;
	@Column(name = "saldo_anos_restantes")
	private BigInteger saldoAnosRestantes;
	@Column(name = "total_asignado")
	private BigInteger totalAsignado;
	@Column(name = "asignacion_disponible")
	private BigInteger asignacionDisponible;
	@Column(name = "saldo_por_asignar")
	private BigInteger saldoPorAsignar;
	@Column(name = "total_contratado")
	private BigInteger totalContratado;
	@Column(name = "total_pagado")
	private BigInteger totalPagado;
	@Column(name = "arrastre")
	private BigInteger arrastre;
	@Column(name = "total_programado")
	private BigInteger totalProgramado;
	@Column(name = "enero_programado")
	private BigInteger eneroProgramado;
	@Column(name = "febrero_programado")
	private BigInteger febreroProgramado;
	@Column(name = "marzo_programado")
	private BigInteger marzoProgramado;
	@Column(name = "abril_programado")
	private BigInteger abrilProgramado;
	@Column(name = "mayo_programado")
	private BigInteger mayoProgramado;
	@Column(name = "junio_programado")
	private BigInteger junioProgramado;
	@Column(name = "julio_programado")
	private BigInteger julioProgramado;
	@Column(name = "agosto_programado")
	private BigInteger agostoProgramado;
	@Column(name = "septiembre_programado")
	private BigInteger septiembreProgramado;
	@Column(name = "octubre_programado")
	private BigInteger octubreProgramado;
	@Column(name = "noviembre_programado")
	private BigInteger noviembreProgramado;
	@Column(name = "diciembre_programado")
	private BigInteger diciembreProgramado;
	@Column(name = "enero_pagado")
	private BigInteger eneroPagado;
	@Column(name = "febrero_pagado")
	private BigInteger febreroPagado;
	@Column(name = "marzo_pagado")
	private BigInteger marzoPagado;
	@Column(name = "abril_pagado")
	private BigInteger abrilPagado;
	@Column(name = "mayo_pagado")
	private BigInteger mayoPagado;
	@Column(name = "junio_pagado")
	private BigInteger junioPagado;
	@Column(name = "julio_pagado")
	private BigInteger julioPagado;
	@Column(name = "agosto_pagado")
	private BigInteger agostoPagado;
	@Column(name = "septiembre_pagado")
	private BigInteger septiembrePagado;
	@Column(name = "octubre_pagado")
	private BigInteger octubrePagado;
	@Column(name = "noviembre_pagado")
	private BigInteger noviembrePagado;
	@Column(name = "diciembre_pagado")
	private BigInteger diciembrePagado;
	@JoinColumn(name = "id_gis_chileindica_inversion_data", referencedColumnName = "id")
	@ManyToOne
	private ChileindicaInversionDataEntity inversionData;

	public ChileindicaInversionFinanciamientoDataEntity() {
	}

	public ChileindicaInversionFinanciamientoDataEntity(Long id) {
		this.id = id;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getCodigoFuenteFinanciamiento() {
		return codigoFuenteFinanciamiento;
	}

	public void setCodigoFuenteFinanciamiento(String codigoFuenteFinanciamiento) {
		this.codigoFuenteFinanciamiento = codigoFuenteFinanciamiento;
	}

	public String getNombreFuenteFinanciamiento() {
		return nombreFuenteFinanciamiento;
	}

	public void setNombreFuenteFinanciamiento(String nombreFuenteFinanciamiento) {
		this.nombreFuenteFinanciamiento = nombreFuenteFinanciamiento;
	}

	public String getAsignacionPresupuestaria() {
		return asignacionPresupuestaria;
	}

	public void setAsignacionPresupuestaria(String asignacionPresupuestaria) {
		this.asignacionPresupuestaria = asignacionPresupuestaria;
	}

	public String getNombreAsignacionPresupuestaria() {
		return nombreAsignacionPresupuestaria;
	}

	public void setNombreAsignacionPresupuestaria(
			String nombreAsignacionPresupuestaria) {
		this.nombreAsignacionPresupuestaria = nombreAsignacionPresupuestaria;
	}

	public BigInteger getCostoTotalEbi() {
		return costoTotalEbi;
	}

	public void setCostoTotalEbi(BigInteger costoTotalEbi) {
		this.costoTotalEbi = costoTotalEbi;
	}

	public BigInteger getCostoTotalEbiActualizado() {
		return costoTotalEbiActualizado;
	}

	public void setCostoTotalEbiActualizado(BigInteger costoTotalEbiActualizado) {
		this.costoTotalEbiActualizado = costoTotalEbiActualizado;
	}

	public BigInteger getCostoTotalEbiActualizadoMasDiezPorciento() {
		return costoTotalEbiActualizadoMasDiezPorciento;
	}

	public void setCostoTotalEbiActualizadoMasDiezPorciento(
			BigInteger costoTotalEbiActualizadoMasDiezPorciento) {
		this.costoTotalEbiActualizadoMasDiezPorciento = costoTotalEbiActualizadoMasDiezPorciento;
	}

	public BigInteger getCostoTotalCore() {
		return costoTotalCore;
	}

	public void setCostoTotalCore(BigInteger costoTotalCore) {
		this.costoTotalCore = costoTotalCore;
	}

	public BigInteger getCostoTotalAjustado() {
		return costoTotalAjustado;
	}

	public void setCostoTotalAjustado(BigInteger costoTotalAjustado) {
		this.costoTotalAjustado = costoTotalAjustado;
	}

	public BigInteger getGastadoAnosAnteriores() {
		return gastadoAnosAnteriores;
	}

	public void setGastadoAnosAnteriores(BigInteger gastadoAnosAnteriores) {
		this.gastadoAnosAnteriores = gastadoAnosAnteriores;
	}

	public BigInteger getSolicitadoAno() {
		return solicitadoAno;
	}

	public void setSolicitadoAno(BigInteger solicitadoAno) {
		this.solicitadoAno = solicitadoAno;
	}

	public BigInteger getSaldoProximoAno() {
		return saldoProximoAno;
	}

	public void setSaldoProximoAno(BigInteger saldoProximoAno) {
		this.saldoProximoAno = saldoProximoAno;
	}

	public BigInteger getSaldoAnosRestantes() {
		return saldoAnosRestantes;
	}

	public void setSaldoAnosRestantes(BigInteger saldoAnosRestantes) {
		this.saldoAnosRestantes = saldoAnosRestantes;
	}

	public BigInteger getTotalAsignado() {
		return totalAsignado;
	}

	public void setTotalAsignado(BigInteger totalAsignado) {
		this.totalAsignado = totalAsignado;
	}

	public BigInteger getAsignacionDisponible() {
		return asignacionDisponible;
	}

	public void setAsignacionDisponible(BigInteger asignacionDisponible) {
		this.asignacionDisponible = asignacionDisponible;
	}

	public BigInteger getSaldoPorAsignar() {
		return saldoPorAsignar;
	}

	public void setSaldoPorAsignar(BigInteger saldoPorAsignar) {
		this.saldoPorAsignar = saldoPorAsignar;
	}

	public BigInteger getTotalContratado() {
		return totalContratado;
	}

	public void setTotalContratado(BigInteger totalContratado) {
		this.totalContratado = totalContratado;
	}

	public BigInteger getTotalPagado() {
		return totalPagado;
	}

	public void setTotalPagado(BigInteger totalPagado) {
		this.totalPagado = totalPagado;
	}

	public BigInteger getArrastre() {
		return arrastre;
	}

	public void setArrastre(BigInteger arrastre) {
		this.arrastre = arrastre;
	}

	public BigInteger getTotalProgramado() {
		return totalProgramado;
	}

	public void setTotalProgramado(BigInteger totalProgramado) {
		this.totalProgramado = totalProgramado;
	}

	public BigInteger getEneroProgramado() {
		return eneroProgramado;
	}

	public void setEneroProgramado(BigInteger eneroProgramado) {
		this.eneroProgramado = eneroProgramado;
	}

	public BigInteger getFebreroProgramado() {
		return febreroProgramado;
	}

	public void setFebreroProgramado(BigInteger febreroProgramado) {
		this.febreroProgramado = febreroProgramado;
	}

	public BigInteger getMarzoProgramado() {
		return marzoProgramado;
	}

	public void setMarzoProgramado(BigInteger marzoProgramado) {
		this.marzoProgramado = marzoProgramado;
	}

	public BigInteger getAbrilProgramado() {
		return abrilProgramado;
	}

	public void setAbrilProgramado(BigInteger abrilProgramado) {
		this.abrilProgramado = abrilProgramado;
	}

	public BigInteger getMayoProgramado() {
		return mayoProgramado;
	}

	public void setMayoProgramado(BigInteger mayoProgramado) {
		this.mayoProgramado = mayoProgramado;
	}

	public BigInteger getJunioProgramado() {
		return junioProgramado;
	}

	public void setJunioProgramado(BigInteger junioProgramado) {
		this.junioProgramado = junioProgramado;
	}

	public BigInteger getJulioProgramado() {
		return julioProgramado;
	}

	public void setJulioProgramado(BigInteger julioProgramado) {
		this.julioProgramado = julioProgramado;
	}

	public BigInteger getAgostoProgramado() {
		return agostoProgramado;
	}

	public void setAgostoProgramado(BigInteger agostoProgramado) {
		this.agostoProgramado = agostoProgramado;
	}

	public BigInteger getSeptiembreProgramado() {
		return septiembreProgramado;
	}

	public void setSeptiembreProgramado(BigInteger septiembreProgramado) {
		this.septiembreProgramado = septiembreProgramado;
	}

	public BigInteger getOctubreProgramado() {
		return octubreProgramado;
	}

	public void setOctubreProgramado(BigInteger octubreProgramado) {
		this.octubreProgramado = octubreProgramado;
	}

	public BigInteger getNoviembreProgramado() {
		return noviembreProgramado;
	}

	public void setNoviembreProgramado(BigInteger noviembreProgramado) {
		this.noviembreProgramado = noviembreProgramado;
	}

	public BigInteger getDiciembreProgramado() {
		return diciembreProgramado;
	}

	public void setDiciembreProgramado(BigInteger diciembreProgramado) {
		this.diciembreProgramado = diciembreProgramado;
	}

	public BigInteger getEneroPagado() {
		return eneroPagado;
	}

	public void setEneroPagado(BigInteger eneroPagado) {
		this.eneroPagado = eneroPagado;
	}

	public BigInteger getFebreroPagado() {
		return febreroPagado;
	}

	public void setFebreroPagado(BigInteger febreroPagado) {
		this.febreroPagado = febreroPagado;
	}

	public BigInteger getMarzoPagado() {
		return marzoPagado;
	}

	public void setMarzoPagado(BigInteger marzoPagado) {
		this.marzoPagado = marzoPagado;
	}

	public BigInteger getAbrilPagado() {
		return abrilPagado;
	}

	public void setAbrilPagado(BigInteger abrilPagado) {
		this.abrilPagado = abrilPagado;
	}

	public BigInteger getMayoPagado() {
		return mayoPagado;
	}

	public void setMayoPagado(BigInteger mayoPagado) {
		this.mayoPagado = mayoPagado;
	}

	public BigInteger getJunioPagado() {
		return junioPagado;
	}

	public void setJunioPagado(BigInteger junioPagado) {
		this.junioPagado = junioPagado;
	}

	public BigInteger getJulioPagado() {
		return julioPagado;
	}

	public void setJulioPagado(BigInteger julioPagado) {
		this.julioPagado = julioPagado;
	}

	public BigInteger getAgostoPagado() {
		return agostoPagado;
	}

	public void setAgostoPagado(BigInteger agostoPagado) {
		this.agostoPagado = agostoPagado;
	}

	public BigInteger getSeptiembrePagado() {
		return septiembrePagado;
	}

	public void setSeptiembrePagado(BigInteger septiembrePagado) {
		this.septiembrePagado = septiembrePagado;
	}

	public BigInteger getOctubrePagado() {
		return octubrePagado;
	}

	public void setOctubrePagado(BigInteger octubrePagado) {
		this.octubrePagado = octubrePagado;
	}

	public BigInteger getNoviembrePagado() {
		return noviembrePagado;
	}

	public void setNoviembrePagado(BigInteger noviembrePagado) {
		this.noviembrePagado = noviembrePagado;
	}

	public BigInteger getDiciembrePagado() {
		return diciembrePagado;
	}

	public void setDiciembrePagado(BigInteger diciembrePagado) {
		this.diciembrePagado = diciembrePagado;
	}

	public ChileindicaInversionDataEntity getInversionData() {
		return inversionData;
	}

	public void setInversionData(
			ChileindicaInversionDataEntity idGisChileindicaInversionData) {
		this.inversionData = idGisChileindicaInversionData;
	}

	@Override
	public int hashCode() {
		int hash = 0;
		hash += (id != null ? id.hashCode() : 0);
		return hash;
	}

	@Override
	public boolean equals(Object object) {
		// TODO: Warning - this method won't work in the case the id fields are
		// not set
		if (!(object instanceof ChileindicaInversionFinanciamientoDataEntity)) {
			return false;
		}
		ChileindicaInversionFinanciamientoDataEntity other = (ChileindicaInversionFinanciamientoDataEntity) object;
		if ((this.id == null && other.id != null)
				|| (this.id != null && !this.id.equals(other.id))) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "ChileindicaInversionFinanciamientoDataEntity[ id=" + id + " ]";
	}

}
