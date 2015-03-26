package com.emergya.ohiggins.service;

import java.io.Serializable;
import java.util.Collection;

import com.emergya.ohiggins.dto.SectorDto;
import com.emergya.persistenceGeo.service.AbstractService;

public interface SectorService extends AbstractService, Serializable {

	Collection<SectorDto> getByName(String sector);

}
