package com.emergya.chileIndica.dto;

import java.io.Serializable;

public class ChileIndicaInstitucionDto implements Serializable {

	private static final long serialVersionUID = -4845257458553878945L;
	
	private Long cInstitucion;
	private String nInstitucion;

	public ChileIndicaInstitucionDto() {
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
