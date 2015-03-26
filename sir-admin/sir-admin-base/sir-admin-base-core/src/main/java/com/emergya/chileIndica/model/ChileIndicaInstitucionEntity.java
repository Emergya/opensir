package com.emergya.chileIndica.model;

import java.io.Serializable;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "INSTITUCION")
public class ChileIndicaInstitucionEntity implements Serializable {

	private static final long serialVersionUID = -4845257458553878945L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Basic(optional = false)
	@Column(name = "C_INSTITUCION")
	private Long cInstitucion;
	@Column(name = "N_INSTITUCION", length = 85)
	private String nInstitucion;

	public ChileIndicaInstitucionEntity() {
	}
	
	public Long getCInstitucion() {
		return cInstitucion;
	}

	public void setCInstitucion(final Long cInstitucion) {
		this.cInstitucion = cInstitucion;
	}

	public String getNInstitucion() {
		return nInstitucion;
	}

	public void setNIstitucion(final String nInstitucion) {
		this.nInstitucion = nInstitucion;
	}

	
}
