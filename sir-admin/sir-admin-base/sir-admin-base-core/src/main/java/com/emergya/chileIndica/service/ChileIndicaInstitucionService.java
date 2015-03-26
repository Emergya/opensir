package com.emergya.chileIndica.service;

import java.io.Serializable;
import java.util.List;

import com.emergya.chileIndica.dto.ChileIndicaInstitucionDto;

public interface ChileIndicaInstitucionService extends ChileIndicaAbstractService, Serializable{

	/**
	 * Obtiene lista de instituciones provenientes de la base de datos
	 * de ChileIndica según la región.
	 *  
	 * @param codRegion
	 * @return
	 */
	List<ChileIndicaInstitucionDto> getAllAuthorityByRegion(Long codRegion);
}
