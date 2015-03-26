/*
 * OhigginsInstitucionServiceImpl.java
 * 
 * Copyright (C) 2011
 * 
 * This file is part of Proyecto ohiggins
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
 * Authors:: Jose Alfonso (mailto:jariera@emergya.com)
 */
package com.emergya.ohiggins.service.impl;

import java.io.Serializable;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.emergya.ohiggins.converter.RegionConverter;
import com.emergya.ohiggins.dao.OhigginsAuthorityEntityDao;
import com.emergya.ohiggins.dao.OhigginsAuthorityTypeEntityDao;
import com.emergya.ohiggins.dao.OhigginsLayerEntityDao;
import com.emergya.ohiggins.dao.OhigginsLayerPublishRequestEntityDao;
import com.emergya.ohiggins.dao.OhigginsLayerResourceDao;
import com.emergya.ohiggins.dao.OhigginsNivelTerritorialEntityDao;
import com.emergya.ohiggins.dao.OhigginsRegionEntityDao;
import com.emergya.ohiggins.dto.AuthorityDto;
import com.emergya.ohiggins.dto.AuthorityTypeDto;
import com.emergya.ohiggins.dto.NivelTerritorialDto;
import com.emergya.ohiggins.dto.RegionDto;
import com.emergya.ohiggins.model.AuthorityEntity;
import com.emergya.ohiggins.model.AuthorityEntity.Names;
import com.emergya.ohiggins.model.LayerEntity;
import com.emergya.ohiggins.model.LayerPublishRequestEntity;
import com.emergya.ohiggins.model.LayerResourceEntity;
import com.emergya.ohiggins.model.RegionAuthorityRelationship;
import com.emergya.ohiggins.model.RegionEntity;
import com.emergya.ohiggins.model.ZoneEntity;
import com.emergya.ohiggins.service.InstitucionService;
import com.emergya.persistenceGeo.dao.GenericDAO;
import com.emergya.persistenceGeo.exceptions.GeoserverException;
import com.emergya.persistenceGeo.metaModel.AbstractAuthorityTypeEntity;
import com.emergya.persistenceGeo.service.GeoserverService;
import com.emergya.persistenceGeo.service.impl.AbstractServiceImpl;

@Repository
@Transactional
public class OhigginsInstitucionServiceImpl extends
		AbstractServiceImpl<AuthorityDto, AuthorityEntity> implements
		InstitucionService {

	private static final long serialVersionUID = 5883851911038690171L;
	private static final Log LOG = LogFactory
			.getLog(OhigginsInstitucionServiceImpl.class);

	// @Resource
	// private UserEntityDao userDao;

	@Resource
	private OhigginsAuthorityEntityDao authorityDao;
	@Resource
	private OhigginsNivelTerritorialEntityDao nivelTerritorialDao;
	@Resource
	private OhigginsAuthorityTypeEntityDao ohigginsAuthorityTypeEntityDao;
	@Autowired(required = false)
	private GeoserverService geoserverService;
	@Resource
	private OhigginsRegionEntityDao regionDao;
        
        @Resource
        private OhigginsLayerEntityDao layerDao;
        
        @Resource 
        private OhigginsLayerPublishRequestEntityDao publishRequestDao;
        
        @Resource
        private OhigginsLayerResourceDao layerResourceDao;

	/**
	 * Constructor
	 */
	public OhigginsInstitucionServiceImpl() {
		super();
	}

	/**
	 * Crea una nueva institucion en base de datos y el workspace
	 * correspondiente en geoserver.
	 * 
	 * @param dto
	 * @return
	 */
        @Override
	public AuthorityDto createInstitucion(AuthorityDto dto) {
		if (geoserverService == null) {
			throw new UnsupportedOperationException(
					"geoserverService bean not found. Please inject it to "
							+ this.getClass().getName());
		}

		AuthorityDto dtor = null;
		AuthorityEntity entity = dtoToEntity(dto);
		Long id = authorityDao.save(entity);
		entity = authorityDao.findById(id, false);

		//String workspaceName = GeoserverUtils.createName(entity.getName());

		if (LOG.isInfoEnabled()) {
			LOG.info("Creada institución con nombre " + entity.getName());
			LOG.info("Creando workspace " + entity.getWorkspaceName()
					+ " para la institución");
		}

		boolean result = geoserverService
				.createGsWorkspaceWithDatastore(entity.getWorkspaceName());

		if (result) {
			//entity.setWorkspaceName(workspaceName);
			authorityDao.save(entity);
		} else {
			authorityDao.delete(entity.getId());
			throw new GeoserverException(
					"No se ha podido crear el workspace para la institución en Geoserver");
		}

		if (entity.getId() != null) {
			dtor = entityToDto(entity);
		}

		return dtor;
	}
	
        @Override
	public void delete(Serializable sDto) {
            
            AuthorityDto dto = (AuthorityDto) sDto;
            
		if (dto==null || dto.getWorkspaceName()==null) {
                    throw new IllegalArgumentException("Suppliete authority cannot be null!");
                }
			
                String workspaceName = dto.getWorkspaceName();

                if (this.geoserverService.existsWorkspace(workspaceName)) {
                        this.geoserverService.deleteGsWorkspace(workspaceName);
                }
                
                AuthorityEntity authEntity = dtoToEntity(dto);
                
                // We search and remove the private layers of the authority.
                List<LayerEntity> privateLayers = layerDao.getByAuth(authEntity);                
                for(LayerEntity privateLayer : privateLayers) {
                    layerDao.makeTransient(privateLayer);
                }
                
                // We search and unlink the layers publicized due to a request by this authority
                List<LayerEntity> publicLayers = layerDao.getByRequestedByAuth(authEntity);
                for(LayerEntity publicLayer : publicLayers) {
                    publicLayer.setRequestedByAuth(null);
                    layerDao.makePersistent(publicLayer);
                }           
                
                
                // We search and remove publication requests from the institution.
               List<LayerPublishRequestEntity> publishRequests =
                        publishRequestDao.getByAuth(authEntity);
                for(LayerPublishRequestEntity publishRequest : publishRequests) {
                    publishRequestDao.makeTransient(publishRequest);
                }
                
                // We search and remove associated layer resources
                List<LayerResourceEntity> resources = 
                        layerResourceDao.getByAuth(authEntity);
                for(LayerResourceEntity resource : resources) {
                    layerResourceDao.makeTransient(resource);
                }
                                
                
                // We remove the institution itself
                super.delete(dto);
	}

	/**
	 * Convierte Entity a Dto
	 * 
	 * @param entity
	 * @return AuthorityDto
	 */
        @Override
	public AuthorityDto entityToDto(AuthorityEntity entity) {
		AuthorityDto dto = null;

		if (entity != null) {
			dto = new AuthorityDto();

			// Atributos simples
			dto.setId(entity.getId());
			dto.setFechaActualizacion(entity.getUpdateDate());
			dto.setFechaCreacion(entity.getCreateDate());
			dto.setAuthority(entity.getName());
			dto.setWorkspaceName(entity.getWorkspaceName());

			// Atributos complejos
			// 1.Tipo de institucion
			if (entity.getAuthType() != null
					&& entity.getAuthType().getId() != null) {

				AuthorityTypeDto typeDto = new AuthorityTypeDto();
				// if(dto.getType() != null && dto.getType().getId() != null){
				AbstractAuthorityTypeEntity type = ohigginsAuthorityTypeEntityDao
						.findById(entity.getAuthType().getId(), false);
				if (type != null) {
					typeDto.setId((Long) type.getId());
					typeDto.setName(type.getName());
					typeDto.setFechaActualizacion(type.getUpdateDate());
					typeDto.setFechaCreacion(type.getCreateDate());
				}
				// }

				dto.setType(typeDto);
				dto.setTipoSeleccionado((typeDto.getId() != null) ? typeDto
						.getId().toString() : StringUtils.EMPTY);
			}

			// 2.Nivel territorial
			if (entity.getZone() != null && entity.getZone().getId() != null) {

				NivelTerritorialDto nivelTDto = new NivelTerritorialDto();

				// if(dto.getNivelTerritorial() != null &&
				// dto.getNivelTerritorial().getId() != null){
				ZoneEntity nivelT = nivelTerritorialDao.findById(new Long(
						entity.getZone().getId()), false);
				if (nivelT != null) {
					nivelTDto.setId(new Integer(nivelT.getId().toString()));
					nivelTDto.setName(nivelT.getName());
					nivelTDto.setCodigo_territorio(nivelT.getCode());
					nivelTDto.setExtension(nivelT.getExtensionGeom());
					nivelTDto.setFecha_actualizacion(nivelT.getUpdateDate());
					nivelTDto.setFecha_creacion(nivelT.getCreateDate());
					nivelTDto.setTipo_ambito(nivelT.getType());
				}
				// }

				dto.setNivelTerritorial(nivelTDto);
				dto.setAmbitoSeleccionado((nivelTDto
						.getId() != null) ? nivelTDto.getId().toString()
						: StringUtils.EMPTY);
			}

			// 3.Total usuarios
			if (entity.getPeople() != null) {
				dto.setTotalUser(entity.getPeople().size());
			}
			
			// Region
			if (entity.getRegionAuthoritys() != null
					&& entity.getRegionAuthoritys().size() > 0
					&& entity.getRegionAuthoritys().get(0).getRegion() != null) {
				RegionEntity reg = regionDao
						.findById(entity.getRegionAuthoritys().get(0)
								.getRegion().getId(), false);
				if (reg != null) {
					RegionDto regDto = RegionConverter.entityToDto(reg);
					dto.setRegion(regDto);
				}
			} else {
				dto.setRegion(null);
			}

		}

		return dto;
	}

	/**
	 * Convierte de Dto a Entity
	 * 
	 * @param dto
	 * @return AuthorityEntity
	 */
        @Override
	public AuthorityEntity dtoToEntity(AuthorityDto dto) {
		AuthorityEntity entity = null;
		Date hoy = new Date();

		if (dto != null) {
			if (dto.getId() != null) {
				entity = (AuthorityEntity) authorityDao.findById(dto.getId(),
						false);
				entity.setUpdateDate(hoy);
			} else {
				entity = new AuthorityEntity();
				entity.setCreateDate(hoy);
			}

			// Atributos simples
			entity.setName(dto.getAuthority());
			entity.setWorkspaceName(dto.getWorkspaceName());

			// Atributos complejos
			// 1.Tipo de institucion
			if (dto.getType() != null && dto.getType().getId() != null) {

				AbstractAuthorityTypeEntity type = ohigginsAuthorityTypeEntityDao
						.findById(dto.getType().getId(), false);
				entity.setAuthType(type);
			}

			// 2.Nivel territorial
			if (dto.getNivelTerritorial() != null
					&& dto.getNivelTerritorial().getId() != null) {

				ZoneEntity nivelT = nivelTerritorialDao.findById(new Long(dto
						.getNivelTerritorial().getId()), false);
				entity.setZone((nivelT));
			}
			
			// Region
			List<RegionAuthorityRelationship> regions = new LinkedList<RegionAuthorityRelationship>();

			if (dto.getRegion() != null && dto.getRegion().getId() != null) {

				RegionAuthorityRelationship regionAuthorityRelationship = new RegionAuthorityRelationship();
				RegionEntity reg = regionDao.findById(dto.getRegion().getId(),
						false);
				regionAuthorityRelationship.setAuthority(entity);
				regionAuthorityRelationship.setRegion(reg);
				regions.add(regionAuthorityRelationship);
			}
			entity.setRegionAuthoritys(regions);

		}

		return entity;
	}

	@Override
	protected GenericDAO<AuthorityEntity, Long> getDao() {
		return authorityDao;
	}

	/**
	 * Comprueba si una institucion tiene usuarios asociados
	 * 
	 * @param id
	 * @return
	 */
        @Override
	public boolean hasUsers(Long id) {
		boolean tiene = false;

		AuthorityEntity entity = (AuthorityEntity) authorityDao.findById(id,
				false);
		if (entity != null && entity.getId() != null
				&& entity.getPeople() != null && entity.getPeople().size() > 0) {
			tiene = true;
		}

		return tiene;
	}

	/**
	 * Obtiene lista de autoridades ordenada por nombre.
	 * 
	 * @param first
	 * @param last
	 * @return
	 */
	@SuppressWarnings("unchecked")
        @Override
	public List<AuthorityDto> getFromToOrdered(Integer first, Integer last) {
		List<AuthorityEntity> entities = authorityDao.getFromToOrdered(first,
				last);
		List<AuthorityDto> ldto = (List<AuthorityDto>) entitiesToDtos(entities);
		return ldto;

	}

	@SuppressWarnings("unchecked")
	@Override
	public List<AuthorityDto> getAllOrderedByName() {
		List<AuthorityEntity> entities = authorityDao
				.getAllOrderedByField(Names.NAME);
		List<AuthorityDto> ldto = (List<AuthorityDto>) entitiesToDtos(entities);
		return ldto;
	}

	/**
	 * Comprueba si la institucion ya esta dada de alta
	 * 
	 * @param authorityName
	 *            nombre institucion a comprobar.
	 * @return <code>true</code> si la institucion no existe en la base de
	 *         datos, <code>false</code> si ya existe la institucion con ese
	 *         username.
	 */
        @Override
	public boolean isInstitucionAvailable(String authorityName, Long codRegion) {
		return this.isAuthorityAvalaible(authorityName, null, codRegion);
	}

	@Override
	public boolean isAuthorityAvalaible(String authorityName, Long institutionId, Long codRegion) {
		AuthorityEntity institucion = new AuthorityEntity(authorityName);
		// Region
		List<RegionAuthorityRelationship> regions = new LinkedList<RegionAuthorityRelationship>();
	
		RegionAuthorityRelationship regionAuthorityRelationship = new RegionAuthorityRelationship();
		RegionEntity reg = regionDao.findById(codRegion,
					false);
		regionAuthorityRelationship.setAuthority(institucion);
		regionAuthorityRelationship.setRegion(reg);
		regions.add(regionAuthorityRelationship);
		institucion.setRegionAuthoritys(regions);

		List<AuthorityEntity> found = authorityDao.findByExample(institucion,
				new String[] {}, true);

		if (found.isEmpty()) {
			return true;
		}

		if (found.size() == 1 && institutionId != null
				&& institutionId.equals(found.get(0).getId())) {
			// Its the same we are excluding.
			return true;
		}
		//TODO tema region

		return false;
	}
}
