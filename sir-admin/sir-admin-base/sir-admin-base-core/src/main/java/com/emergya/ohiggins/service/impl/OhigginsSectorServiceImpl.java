package com.emergya.ohiggins.service.impl;

import java.util.Collection;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.emergya.ohiggins.dao.OhigginsSectorEntityDao;
import com.emergya.ohiggins.dto.SectorDto;
import com.emergya.ohiggins.model.SectorEntity;
import com.emergya.ohiggins.service.SectorService;
import com.emergya.persistenceGeo.dao.GenericDAO;
import com.emergya.persistenceGeo.service.impl.AbstractServiceImpl;
import com.google.common.base.Function;
import com.google.common.collect.Collections2;

@Repository
@Transactional
public class OhigginsSectorServiceImpl extends
		AbstractServiceImpl<SectorDto, SectorEntity> implements SectorService {

	private static final long serialVersionUID = 6829644986522602024L;
	@Resource
	private OhigginsSectorEntityDao sectorDao;

	/**
	 * Constructor
	 */
	public OhigginsSectorServiceImpl() {
		super();
	}

	@Override
	protected SectorDto entityToDto(SectorEntity entity) {
		SectorDto dto = null;

		if (entity != null) {
			dto = new SectorDto();
			dto.setId(new Integer(Integer.parseInt(entity.getId().toString())));
			dto.setName(entity.getNombre());
		}

		return dto;
	}

	@Override
	protected SectorEntity dtoToEntity(SectorDto dto) {
		SectorEntity entity = null;

		if (dto != null) {
			entity = new SectorEntity();
			entity.setId(new Integer(Integer.parseInt(dto.getId().toString())));
			entity.setNombre(dto.getName());
		}

		return entity;
	}

	@Override
	protected GenericDAO<SectorEntity, Long> getDao() {
		return sectorDao;
	}

	public Collection<SectorDto> getByName(String sector) {
		SectorEntity example = new SectorEntity();
		example.setNombre(sector);
		List<SectorEntity> daoResult = sectorDao.findByExample(example,
				new String[] {});
		Collection<SectorDto> result = Collections2.transform(daoResult,
				new Function<SectorEntity, SectorDto>() {
					public SectorDto apply(SectorEntity entity) {
						return entityToDto(entity);
					}
				});

		return result;
	}

}
