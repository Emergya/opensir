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
package com.emergya.ohiggins.dto;

import java.io.Serializable;
import java.math.BigInteger;

/**
 * 
 * @author <a href="mailto:jlrodriguez@emergya.com">Juan Luis Rodríguez
 *         Ponce</a>
 */

public class ChileindicaInversionFinanciamientoDataDto implements Serializable {

	private static final long serialVersionUID = 6749183415776031791L;
	private Long id;
	private String codigoFuenteFinanciamiento;
	private String nombreFuenteFinanciamiento;
	private String asignacionPresupuestaria;
	private String nombreAsignacionPresupuestaria;
	private BigInteger costoTotalEbi;
	private BigInteger costoTotalEbiActualizado;
	private BigInteger costoTotalEbiActualizadoMasDiezPorciento;
	private BigInteger costoTotalCore;
	private BigInteger costoTotalAjustado;
	private BigInteger gastadoAnosAnteriores;
	private BigInteger solicitadoAno;
	private BigInteger saldoProximoAno;
	private BigInteger saldoAnosRestantes;
	private BigInteger totalAsignado;
	private BigInteger asignacionDisponible;
	private BigInteger saldoPorAsignar;
	private BigInteger totalContratado;
	private BigInteger totalPagado;
	private BigInteger arrastre;
	private BigInteger totalProgramado;
	private BigInteger eneroProgramado;
	private BigInteger febreroProgramado;
	private BigInteger marzoProgramado;
	private BigInteger abrilProgramado;
	private BigInteger mayoProgramado;
	private BigInteger junioProgramado;
	private BigInteger julioProgramado;
	private BigInteger agostoProgramado;
	private BigInteger septiembreProgramado;
	private BigInteger octubreProgramado;
	private BigInteger noviembreProgramado;
	private BigInteger diciembreProgramado;
	private BigInteger eneroPagado;
	private BigInteger febreroPagado;
	private BigInteger marzoPagado;
	private BigInteger abrilPagado;
	private BigInteger mayoPagado;
	private BigInteger junioPagado;
	private BigInteger julioPagado;
	private BigInteger agostoPagado;
	private BigInteger septiembrePagado;
	private BigInteger octubrePagado;
	private BigInteger noviembrePagado;
	private BigInteger diciembrePagado;

	private ChileindicaInversionDataDto inversionData;

	public ChileindicaInversionFinanciamientoDataDto() {
	}

	public ChileindicaInversionFinanciamientoDataDto(Long id) {
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

	public ChileindicaInversionDataDto getInversionData() {
		return inversionData;
	}

	public void setInversionData(
			ChileindicaInversionDataDto inversionData) {
		this.inversionData = inversionData;
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
		if (!(object instanceof ChileindicaInversionFinanciamientoDataDto)) {
			return false;
		}
		ChileindicaInversionFinanciamientoDataDto other = (ChileindicaInversionFinanciamientoDataDto) object;
		if ((this.id == null && other.id != null)
				|| (this.id != null && !this.id.equals(other.id))) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "ChileindicaInversionFinanciamientoDataDto[ id=" + id + " ]";
	}

}
