package com.emergya.ohiggins.service;

import java.io.Serializable;
import java.util.List;

import com.emergya.ohiggins.dto.AuthorityTypeDto;
import com.emergya.persistenceGeo.service.AbstractService;

public interface AuthorityTypeService extends AbstractService, Serializable {

	/**
	 * Obtiene el tipo de institucion por id.
	 * 
	 * @param id
	 * @return
	 */
	public AuthorityTypeDto getById(Long id);

	/**
	 * Obtiene lista de tipos autoridades ordenada por nombre sin incluir a los
	 * ciudadanos.
	 * 
	 * @param first
	 * @param last
	 * @return
	 */
	public List<AuthorityTypeDto> getAllOrdered();

	/**
	 * Obtiene lista de tipos autoridades ordenada por nombre incluyendo a los
	 * ciudadanos.
	 * 
	 * @param campo
	 *            Ordena por el campo
	 * @return
	 */
	public List<AuthorityTypeDto> getAllOrderedByField(String campo);

	/**
	 * Obtiene lista de tipos autoridades. que no sean ciudadano
	 * 
	 * @return
	 */
	public List<AuthorityTypeDto> getAllAuthority();

	/**
	 * Obtiene lista de tipos autoridades ordenada por nombre. que no sean
	 * ciudadano
	 * 
	 * @return
	 */
	public List<AuthorityTypeDto> getAllAuthorityOrdered();

}
