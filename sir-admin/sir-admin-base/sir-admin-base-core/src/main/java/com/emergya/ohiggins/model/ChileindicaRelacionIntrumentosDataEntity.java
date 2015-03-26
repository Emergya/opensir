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
@Table(name = "gis_relacion_intrumentos")
@NamedQueries({
    @NamedQuery(name = "ChileindicaRelacionIntrumentosDataEntity.findAll", query = "SELECT c FROM ChileindicaRelacionIntrumentosDataEntity c"),
    @NamedQuery(name = "ChileindicaRelacionIntrumentosDataEntity.findById", query = "SELECT c FROM ChileindicaRelacionIntrumentosDataEntity c WHERE c.id = :id"),
    @NamedQuery(name = "ChileindicaRelacionIntrumentosDataEntity.findByCodigoInstrumento", query = "SELECT c FROM ChileindicaRelacionIntrumentosDataEntity c WHERE c.codigoInstrumento = :codigoInstrumento"),
    @NamedQuery(name = "ChileindicaRelacionIntrumentosDataEntity.findByNombreInstrumento", query = "SELECT c FROM ChileindicaRelacionIntrumentosDataEntity c WHERE c.nombreInstrumento = :nombreInstrumento"),
    @NamedQuery(name = "ChileindicaRelacionIntrumentosDataEntity.findByEspecificacion", query = "SELECT c FROM ChileindicaRelacionIntrumentosDataEntity c WHERE c.especificacion = :especificacion"),
    @NamedQuery(name = "ChileindicaRelacionIntrumentosDataEntity.findByRelacionPrincipalCodigo", query = "SELECT c FROM ChileindicaRelacionIntrumentosDataEntity c WHERE c.relacionPrincipalCodigo = :relacionPrincipalCodigo"),
    @NamedQuery(name = "ChileindicaRelacionIntrumentosDataEntity.findByRelacionPrincipalNombre", query = "SELECT c FROM ChileindicaRelacionIntrumentosDataEntity c WHERE c.relacionPrincipalNombre = :relacionPrincipalNombre"),
    @NamedQuery(name = "ChileindicaRelacionIntrumentosDataEntity.findByRelacionAsociadaCodigo", query = "SELECT c FROM ChileindicaRelacionIntrumentosDataEntity c WHERE c.relacionAsociadaCodigo = :relacionAsociadaCodigo"),
    @NamedQuery(name = "ChileindicaRelacionIntrumentosDataEntity.findByRelacionAsociadaNombre", query = "SELECT c FROM ChileindicaRelacionIntrumentosDataEntity c WHERE c.relacionAsociadaNombre = :relacionAsociadaNombre")})
public class ChileindicaRelacionIntrumentosDataEntity implements Serializable {

	private static final long serialVersionUID = 8536158163010022130L;
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Column(name = "codigo_instrumento")
    private Integer codigoInstrumento;
    @Column(name = "nombre_instrumento")
    private String nombreInstrumento;
    @Column(name = "especificacion")
    private String especificacion;
    @Column(name = "relacion_principal_codigo")
    private String relacionPrincipalCodigo;
    @Column(name = "relacion_principal_nombre")
    private String relacionPrincipalNombre;
    @Column(name = "relacion_asociada_codigo")
    private String relacionAsociadaCodigo;
    @Column(name = "relacion_asociada_nombre")
    private String relacionAsociadaNombre;
    @JoinColumn(name = "id_gis_chileindica_inversion_data", referencedColumnName = "id")
    @ManyToOne
    private ChileindicaInversionDataEntity inversionData;

    public ChileindicaRelacionIntrumentosDataEntity() {
    }

    public ChileindicaRelacionIntrumentosDataEntity(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getCodigoInstrumento() {
        return codigoInstrumento;
    }

    public void setCodigoInstrumento(Integer codigoInstrumento) {
        this.codigoInstrumento = codigoInstrumento;
    }

    public String getNombreInstrumento() {
        return nombreInstrumento;
    }

    public void setNombreInstrumento(String nombreInstrumento) {
        this.nombreInstrumento = nombreInstrumento;
    }

    public String getEspecificacion() {
        return especificacion;
    }

    public void setEspecificacion(String especificacion) {
        this.especificacion = especificacion;
    }

    public String getRelacionPrincipalCodigo() {
        return relacionPrincipalCodigo;
    }

    public void setRelacionPrincipalCodigo(String relacionPrincipalCodigo) {
        this.relacionPrincipalCodigo = relacionPrincipalCodigo;
    }

    public String getRelacionPrincipalNombre() {
        return relacionPrincipalNombre;
    }

    public void setRelacionPrincipalNombre(String relacionPrincipalNombre) {
        this.relacionPrincipalNombre = relacionPrincipalNombre;
    }

    public String getRelacionAsociadaCodigo() {
        return relacionAsociadaCodigo;
    }

    public void setRelacionAsociadaCodigo(String relacionAsociadaCodigo) {
        this.relacionAsociadaCodigo = relacionAsociadaCodigo;
    }

    public String getRelacionAsociadaNombre() {
        return relacionAsociadaNombre;
    }

    public void setRelacionAsociadaNombre(String relacionAsociadaNombre) {
        this.relacionAsociadaNombre = relacionAsociadaNombre;
    }

    public ChileindicaInversionDataEntity getInversionData() {
        return inversionData;
    }

    public void setInversionData(ChileindicaInversionDataEntity idGisChileindicaInversionData) {
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
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ChileindicaRelacionIntrumentosDataEntity)) {
            return false;
        }
        ChileindicaRelacionIntrumentosDataEntity other = (ChileindicaRelacionIntrumentosDataEntity) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ChileindicaRelacionIntrumentosDataEntity[ id=" + id + " ]";
    }
    
}
