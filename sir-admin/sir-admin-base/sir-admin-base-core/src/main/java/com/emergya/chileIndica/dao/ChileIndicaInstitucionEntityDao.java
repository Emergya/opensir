package com.emergya.chileIndica.dao;

import java.util.List;

import com.emergya.chileIndica.model.ChileIndicaInstitucionEntity;

public interface ChileIndicaInstitucionEntityDao extends ChileIndicaDatabaseGenericDAO<ChileIndicaInstitucionEntity, Long>{

	/**
	 * Obtiene lista de instituciones provenientes de la base de datos
	 * de ChileIndica según la región.
	 * 
	 * @return
	 */
	List<ChileIndicaInstitucionEntity> getAllAuthorityByRegion(Long codRegion);
}
