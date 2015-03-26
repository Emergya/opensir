package com.emergya.ohiggins.service.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.emergya.ohiggins.dao.OhigginsNivelTerritorialEntityDao;
import com.emergya.ohiggins.dto.NivelTerritorialDto;
import com.emergya.ohiggins.model.ZoneEntity;
import com.emergya.ohiggins.service.NivelTerritorialService;
import com.emergya.persistenceGeo.dao.GenericDAO;
import com.emergya.persistenceGeo.service.impl.AbstractServiceImpl;

@Repository
@Transactional
public class OhigginsNivelTerritorialServiceImpl extends
		AbstractServiceImpl<NivelTerritorialDto, ZoneEntity> implements
		NivelTerritorialService {

	/** Serial */
	private static final long serialVersionUID = 1096205718808394232L;

	@Resource
	private OhigginsNivelTerritorialEntityDao nivelTEntityDao;

	public OhigginsNivelTerritorialServiceImpl() {
		super();
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<NivelTerritorialDto> getAllNivelTerritorial() {
		List<NivelTerritorialDto> nivelTerritorial = (List<NivelTerritorialDto>) entitiesToDtos(nivelTEntityDao
				.getAll()); 
//		List<NivelTerritorialDto> nivelTerritorial = (List<NivelTerritorialDto>) entitiesToDtos(nivelTEntityDao
//				.findAllFromTo(0, 15));
		return nivelTerritorial;
	}

	@Override
	protected NivelTerritorialDto entityToDto(ZoneEntity entity) {
		NivelTerritorialDto dto = null;

		if (entity != null) {
			dto = new NivelTerritorialDto();
			dto.setCodigo_territorio(entity.getCode());
			dto.setExtension(entity.getExtensionGeom());
			dto.setFecha_actualizacion(entity.getUpdateDate());
			dto.setFecha_creacion(entity.getCreateDate());
			dto.setId(new Integer(entity.getId().toString()));
			dto.setName(entity.getName());
			dto.setTipo_ambito(entity.getType());
		}
		return dto;
	}

	@Override
	protected ZoneEntity dtoToEntity(NivelTerritorialDto dto) {
		ZoneEntity entity = null;

		if (dto != null) {
			entity = new ZoneEntity();
			entity.setCode(dto.getCodigo_territorio());
			entity.setExtensionGeom(dto.getExtension());
			entity.setUpdateDate(dto.getFecha_actualizacion());
			entity.setCreateDate(dto.getFecha_creacion());
			entity.setId(dto.getId());
			entity.setName(dto.getName());
			entity.setType(dto.getTipo_ambito());
		}
		return entity;
	}

	@Override
	protected GenericDAO<ZoneEntity, Long> getDao() {
		return nivelTEntityDao;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.emergya.persistenceGeo.service.impl.AbstractServiceImpl#getById(java
	 * .lang.Long)
	 */
	public NivelTerritorialDto getById(Long id) {
		return entityToDto(nivelTEntityDao.findById(id, false));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.emergya.ohiggins.service.NivelTerritorialService#
	 * getNivelTerritorialByTipo(java.lang.String)
	 */
	public List<NivelTerritorialDto> getNivelTerritorialByTipo(String tipo) {
		List<ZoneEntity> zonas = nivelTEntityDao.getZonesByType(tipo);
		List<NivelTerritorialDto> result = new ArrayList<NivelTerritorialDto>(
				zonas.size());
		for (ZoneEntity zone : zonas) {
			result.add(entityToDto(zone));
		}
		return result;
	}

	/**
	 * Obtiene el nivel territorial por tipos.
	 * 
	 * @param tipo
	 *            tipo de ámbito (
	 *            {@link NivelTerritorialService#TIPO_NIVEL_REGIONAL},
	 *            {@link NivelTerritorialService#TIPO_NIVEL_PROVINCIAL} o
	 *            {@link NivelTerritorialService#TIPO_NIVEL_MUNICIPAL}).
	 * @return
	 */
	public List<NivelTerritorialDto> getNivelTerritorialByTipos(String[] tipos) {
		List<ZoneEntity> zonas = nivelTEntityDao
				.getNivelTerritorialByTipos(tipos);
		List<NivelTerritorialDto> result = new ArrayList<NivelTerritorialDto>(
				zonas.size());
		for (ZoneEntity zone : zonas) {
			result.add(entityToDto(zone));
		}
		return result;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public List<NivelTerritorialDto> findNivelAndChildren(
			String nivelTerritorial) {

		ZoneEntity example = new ZoneEntity();
		example.setName(nivelTerritorial);
		List<ZoneEntity> intermediateResult = nivelTEntityDao.findByExample(
				example, new String[] {});
		int i = 0;
		while (i < intermediateResult.size()) {
			// vamos añadiendo los hijos a la lista de resultados y seguimos
			// buscando más hijos
			ZoneEntity padre = intermediateResult.get(i);
			intermediateResult.addAll(padre.getZoneList());
			i++;
		}

		List result = entitiesToDtos(intermediateResult);

		return result;
	}

	/**
	 * @return
	 */
	public List<NivelTerritorialDto> getZonesOrderByTypeDescNameAsc() {
		List<NivelTerritorialDto> ambitoTerritorial = this
				.getAllNivelTerritorial();

		Collections.sort(ambitoTerritorial,
				new Comparator<NivelTerritorialDto>() {
					public int compare(NivelTerritorialDto s1,
							NivelTerritorialDto s2) {
						if (s1 == null)
							return 1;
						if (s2 == null)
							return -1;
						if (s1.getTipo_ambito() == null
								&& s2.getTipo_ambito() == null)
							return s1.getName().compareToIgnoreCase(
									s2.getName());
						if (s1.getTipo_ambito() == null)
							return 1;
						if (s2.getTipo_ambito() == null)
							return -1;
						if (s1.getTipo_ambito().equals(s2.getTipo_ambito()))
							return s1.getName().compareToIgnoreCase(
									s2.getName());
						else
							return s2.getTipo_ambito().compareToIgnoreCase(
									s1.getTipo_ambito());
					}
				});
		return ambitoTerritorial;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<NivelTerritorialDto> getByName(String nivelTerritorial) {
		ZoneEntity example = new ZoneEntity();
		example.setName(nivelTerritorial);
		List<ZoneEntity> daoResult = nivelTEntityDao.findByExample(example,
				new String[] {});
		return (List<NivelTerritorialDto>) entitiesToDtos(daoResult);
	}

}
