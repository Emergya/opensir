/*
 * OhigginsLayerServiceImpl.java
 * 
 * Copyright (C) 2012
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
 * Authors:: Jose Alfonso Riera (mailto:jariera@emergya.com)
 */
package com.emergya.ohiggins.service.impl;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.emergya.ohiggins.dao.LayerPropertyDao;
import com.emergya.ohiggins.dao.OhigginsAuthorityEntityDao;
import com.emergya.ohiggins.dao.OhigginsAuthorityTypeEntityDao;
import com.emergya.ohiggins.dao.OhigginsFolderEntityDao;
import com.emergya.ohiggins.dao.OhigginsLayerEntityDao;
import com.emergya.ohiggins.dao.OhigginsLayerMetadataEntityDao;
import com.emergya.ohiggins.dao.OhigginsLayerTypeEntityDao;
import com.emergya.ohiggins.dao.OhigginsNivelTerritorialEntityDao;
import com.emergya.ohiggins.dao.OhigginsUserEntityDao;
import com.emergya.ohiggins.dto.AuthorityDto;
import com.emergya.ohiggins.dto.AuthorityTypeDto;
import com.emergya.ohiggins.dto.FolderDto;
import com.emergya.ohiggins.dto.FolderTypeDto;
import com.emergya.ohiggins.dto.LayerDto;
import com.emergya.ohiggins.dto.LayerMetadataDto;
import com.emergya.ohiggins.dto.LayerPropertyDto;
import com.emergya.ohiggins.dto.LayerTypeDto;
import com.emergya.ohiggins.dto.NivelTerritorialDto;
import com.emergya.ohiggins.dto.UsuarioDto;
import com.emergya.ohiggins.model.AuthorityEntity;
import com.emergya.ohiggins.model.FolderEntity;
import com.emergya.ohiggins.model.LayerEntity;
import com.emergya.ohiggins.model.LayerMetadataEntity;
import com.emergya.ohiggins.model.LayerPropertyEntity;
import com.emergya.ohiggins.model.LayerTypeEntity;
import com.emergya.ohiggins.model.UserEntity;
import com.emergya.ohiggins.model.ZoneEntity;
import com.emergya.ohiggins.service.InstitucionService;
import com.emergya.ohiggins.service.LayerService;
import com.emergya.persistenceGeo.dao.GenericDAO;
import com.emergya.persistenceGeo.metaModel.AbstractAuthorityTypeEntity;
import com.emergya.persistenceGeo.metaModel.AbstractFolderTypeEntity;
import com.emergya.persistenceGeo.service.impl.AbstractServiceImpl;

@Repository
@Transactional
public class OhigginsLayerServiceImpl extends
		AbstractServiceImpl<LayerDto, LayerEntity> implements LayerService {

	private static final long serialVersionUID = 6400994801990819770L;

	private static final String CADENA_VACIA = "";
	// Formateo de fechas
	private static final String FORMATO_FECHA = "dd/MM/yyyy";

	@Resource
	private OhigginsLayerEntityDao ohigginsLayerDao;

	@Resource
	private OhigginsAuthorityEntityDao authorityDao;

	@Resource
	private OhigginsUserEntityDao userDao;

	@Resource
	private InstitucionService institucionService;

	@Resource
	private OhigginsNivelTerritorialEntityDao nivelTerritorialDao;

	@Resource
	private OhigginsAuthorityTypeEntityDao ohigginsAuthorityTypeEntityDao;

	@Resource
	private OhigginsLayerTypeEntityDao layerTypeDao;

	@Resource
	private OhigginsFolderEntityDao folderDao;

	@Resource
	private OhigginsLayerMetadataEntityDao layerMetadataDao;

	@Resource
	private LayerPropertyDao layerPropertyDao;

	@Override
	protected GenericDAO<LayerEntity, Long> getDao() {
		return ohigginsLayerDao;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<LayerDto> getFromToOrderBy(Integer first, Integer last,
			String propertyName, Long idAuth) {

		List<LayerEntity> entities = ((OhigginsLayerEntityDao) getDao())
				.findAllFromToOrderBy(first, last, propertyName, idAuth);

		return (List<LayerDto>) entitiesToDtos(entities);
	}

	/**
	 * Lista de capas de un folder
	 * 
	 * @param first
	 * @param last
	 * @param propertyName
	 * @return
	 */
	@Override
	public List<LayerDto> getLayersByFolderId(Long idFolder) {
		return getLayersByFolderId(idFolder, false);
	}

	/**
	 * Devuelve las capas asignadas a una carpeta.
	 * 
	 * @param idFolder
	 *            La carpeta cuyas capas se devuelven.
	 * @param layerOrder
	 *            Si las capas se devolverán ordenadas por su campo orden o
	 *            alfabéticamente por nombre .
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<LayerDto> getLayersByFolderId(Long idFolder, boolean layerOrder) {
		List<LayerEntity> entities = ((OhigginsLayerEntityDao) getDao())
				.getLayersByFolderId(idFolder, layerOrder);

		return (List<LayerDto>) entitiesToDtos(entities);
	}

	/**
	 * Lista de capas no asignadas a ninguna capa/carpeta.
	 * 
	 * @param justPublicized
	 *            Si se devolveran sólo las capas publicitadas, o todas.
	 * @param layerOrder
	 *            Si las capas se devolverán ordenadas por su campo orden, o
	 *            alfabéticamente por nombre.
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<LayerDto> getUnassignedLayers(boolean justPublicized,
			boolean layerOrder) {
		List<LayerEntity> entities = ((OhigginsLayerEntityDao) getDao())
				.getUnassignedLayers(justPublicized, layerOrder);

		return (List<LayerDto>) entitiesToDtos(entities);
	}

	/**
	 * Lista de capas de un folder de una determinada autoridad
	 * 
	 * @param first
	 * @param last
	 * @param propertyName
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<LayerDto> getLayersByFolderIdByAuthId(Long idFolder, Long idAuth) {
		List<LayerEntity> entities = ((OhigginsLayerEntityDao) getDao())
				.getLayersByFolderIdByAuthId(idFolder, idAuth);

		return (List<LayerDto>) entitiesToDtos(entities);
	}

	/**
	 * Lista de capas publicadas de una autoridad y un tipo determinado (raster,
	 * vectorial)
	 * 
	 * @param first
	 * @param last
	 * @param propertyName
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<LayerDto> getLayersPubliciedByAuthIdByTypeId(Long idAuth,
			Long type) {
		List<LayerEntity> entities = ((OhigginsLayerEntityDao) getDao())
				.getLayersPubliciedByAuthIdByTypeId(idAuth, type);

		return (List<LayerDto>) entitiesToDtos(entities);
	}

	@Override
	protected LayerDto entityToDto(LayerEntity entity) {
		if (entity == null) {
			// We do nothing...
			return null;
		}
		LayerDto dto = simpleEntityToDto(entity);

		// Autoridad
		if (entity.getAuth() != null && entity.getAuth().getId() != null) {
			AuthorityEntity aut = authorityDao.findById(entity.getAuth()
					.getId(), false);
			if (aut != null) {
				AuthorityDto e = entityToDto(aut);
				dto.setAuthority(e);
			}
		} else {
			dto.setAuthority(null);
		}

		// Usuario
		if (entity.getUser() != null && entity.getUser().getId() != null) {
			UserEntity us = userDao.findById(entity.getUser().getId(), false);
			if (us != null) {
				UsuarioDto e = entityToDto(us);
				dto.setUser(e);
			}
		} else {
			dto.setUser(null);
		}

		// Tipo
		if (entity.getType() != null && entity.getType().getId() != null) {
			LayerTypeEntity lt = layerTypeDao.findById(
					entity.getType().getId(), false);
			if (lt != null) {
				LayerTypeDto ld = entityToDto(lt);
				dto.setType(ld);
			}
		} else {
			dto.setType(null);
		}

		// Folder
		if (entity.getFolder() != null && entity.getFolder().getId() != null) {
			FolderEntity lt = folderDao.findById(entity.getFolder().getId(),
					false);
			dto.setFolder(entityToDto(lt));
		} else {
			dto.setFolder(null);
		}
		
		if(entity.getProperties()!=null) {
			List<LayerPropertyDto> propDtos = new LinkedList<LayerPropertyDto>();
			
			for(LayerPropertyEntity propEnt : entity.getProperties()) {
				propDtos.add(LayerPropertyDto.fromEntity(propEnt));
			}
			
			dto.setProperties(propDtos);
		}
		
		if(entity.getRequestedByAuth()!=null) {
			dto.setRequestedByAuth(entityToDto(entity.getRequestedByAuth()));
		}
		

		// metadatos
		if (entity.getMetadata() != null
				&& entity.getMetadata().getId() != null) {
			LayerMetadataEntity meta = entity.getMetadata();
			if (meta != null) {
				LayerMetadataDto m = entityToDto(meta);
				dto.setMetadata(m);
			}
		} else {
			// dto.setMetadata(null);
		}

		return dto;
	}

	private LayerDto simpleEntityToDto(LayerEntity entity) {

		if (entity == null) {
			// We do nothing...
			return null;
		}
		LayerDto dto = new LayerDto();

		dto.setCreateDate(entity.getCreateDate());
		dto.setData(entity.getData());
		dto.setEnabled(entity.getEnabled());
		dto.setId(entity.getId());
		dto.setName(entity.getName());
		dto.setOrder(entity.getOrder());
		dto.setIsChannel(entity.getIsChannel());
		dto.setUpdateDate(entity.getUpdateDate());
		dto.setPublicized(entity.getPublicized());
		dto.setTableName(entity.getTableName());
		dto.setLayerTitle(entity.getLayerTitle());
		dto.setServer_resource(entity.getServer_resource());
		// Descomentar en el futuro si hacen falta
		// dto.setStyleList(entity.getStyleList());

		return dto;
	}

	@Override
	protected LayerEntity dtoToEntity(LayerDto dto) {
		LayerEntity entity = simpleDtoToEntity(dto);

		if (dto != null) {

			// Autoridad
			if (dto.getAuthority() != null
					&& dto.getAuthority().getId() != null) {
				AuthorityEntity a = authorityDao.findById(dto.getAuthority()
						.getId(), false);
				entity.setAuth(a);
			} else {
				entity.setAuth(null);
			}

			if (dto.getRequestedByAuth() != null
					&& dto.getRequestedByAuth().getId() != null) {

				AuthorityEntity rA = authorityDao.findById(dto
						.getRequestedByAuth().getId(), false);
				entity.setRequestedByAuth(rA);
			} else {
				entity.setRequestedByAuth(null);
			}

			// Type
			if (dto.getType() != null && dto.getType().getId() != null) {
				LayerTypeEntity type = layerTypeDao.findById(dto.getType()
						.getId(), false);
				entity.setType(type);
			} else {
				entity.setType(null);
			}

			// Usuario
			if (dto.getUser() != null && dto.getUser().getId() != null) {
				UserEntity u = userDao.findById(dto.getUser().getId(), false);
				entity.setUser(u);
			} else {
				entity.setUser(null);
			}

			// Folder
			if (dto.getFolder() != null && dto.getFolder().getId() != null) {
				FolderEntity f = folderDao.findById(dto.getFolder().getId(),
						false);
				entity.setFolder(f);
			} else {
				entity.setFolder(null);
			}

			// metadatos
			if (dto.getMetadata() != null && dto.getMetadata().getId() != null) {
				LayerMetadataEntity m = layerMetadataDao.findById(dto
						.getMetadata().getId(), false);
				m = copyAllData(m, dto.getMetadata());
				entity.setMetadata(m);
			} else {
				if (dto.getMetadata() != null) {
					LayerMetadataEntity m = dtoToEntity(dto.getMetadata());
					//layerMetadataDao.makePersistent(m);
					entity.setMetadata(m);
				}
			}
			
			if(dto.getProperties() !=null) {
				List<LayerPropertyEntity> propEnts = new LinkedList<LayerPropertyEntity>();
				for(LayerPropertyDto propDto : dto.getProperties()) {
					LayerPropertyEntity prop;
					if(propDto.getId() != null){
						prop = layerPropertyDao.findById(propDto.getId(), false);
					}else{
						prop = new LayerPropertyEntity();
					}
					prop.setName(propDto.getName());
					prop.setValue(propDto.getValue());
					propEnts.add(prop);
				}
				
				entity.setProperties(propEnts);
			}

		}

		return entity;
	}

	/**
	 * Only copy simple properties
	 * 
	 * @param dto
	 * 
	 * @return entity with simple properties updated
	 */
	protected LayerEntity simpleDtoToEntity(LayerDto dto) {
		LayerEntity entity = null;

		if (dto != null) {
			if(dto.getId() != null){
				entity = getDao().findById(dto.getId(), true);
			}else{
				entity = new LayerEntity();
			}
			
			entity.setCreateDate(dto.getCreateDate());
			entity.setData(dto.getData());
			entity.setEnabled(dto.getEnabled());
			entity.setIsChannel(dto.getIsChannel());
			entity.setId(dto.getId());
			entity.setName(dto.getName());
			entity.setOrder(Integer.toString(dto.getOrder()));
			entity.setPublicized(dto.getPublicized());
			entity.setServer_resource(dto.getServer_resource());
			entity.setUpdateDate(dto.getUpdateDate());
			entity.setTableName(dto.getTableName());
			entity.setLayerTitle(dto.getLayerTitle());

		}

		return entity;
	}

	// ################### INCIO
	// ################### Conversiones entre tipos que nos hacen falta en Layer
	// ##################
	/**
	 * Convierte Entity a Dto
	 * 
	 * @param entity
	 * @return AuthorityDto
	 */
	protected AuthorityDto entityToDto(AuthorityEntity entity) {
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
				dto.setTipoSeleccionado((typeDto != null && typeDto.getId() != null) ? typeDto
						.getId().toString() : CADENA_VACIA);
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
				dto.setAmbitoSeleccionado((nivelTDto != null && nivelTDto
						.getId() != null) ? nivelTDto.getId().toString()
						: CADENA_VACIA);
			}

			// 3.Total usuarios
			/*
			 * if (entity.getPeople() != null) {
			 * dto.setTotalUser(entity.getPeople().size()); }
			 */

		}

		return dto;
	}

	/**
	 * Convierte de UsuarioEntity a UsuarioDto
	 * 
	 * @param user
	 * @return
	 */
	protected UsuarioDto entityToDto(UserEntity user) {
		UsuarioDto dto = null;
		if (user != null) {
			dto = new UsuarioDto();
			dto.setId(user.getId());
			dto.setUsername(user.getUsername());
			dto.setAdmin(user.getAdmin() == null ? false : user.getAdmin());
			dto.setApellidos(user.getApellidos());
			dto.setEmail(user.getEmail());
			dto.setFechaActualizacion(user.getUpdateDate());
			dto.setFechaCreacion(user.getCreateDate());
			dto.setNombreCompleto(user.getNombreCompleto());
			dto.setPassword(user.getPassword());
			dto.setTelefono(user.getTelefono());
			dto.setValid(user.getValid() == null ? false : user.getValid());

			/*
			 * AuthorityEntity authority = authorityDao.findUserAuthority(user
			 * .getId()); if (authority != null) {
			 * dto.setAuthorityId(authority.getId());
			 * dto.setAuthority(entityGPToDto(authority)); }
			 */
		}
		return dto;
	}

	/**
	 * Convierte de FolderEntity a FolderDto
	 * 
	 * @param folder
	 * @return
	 */
	protected FolderDto entityToDto(FolderEntity entity) {
		FolderDto dto = null;
		if (entity != null) {
			dto = new FolderDto();

			dto.setCreateDate(entity.getCreateDate());
			dto.setEnabled(entity.getEnabled());
			dto.setFolderOrder(entity.getFolderOrder());
			dto.setId(entity.getId());
			dto.setIsChannel(entity.getIsChannel());
			dto.setName(entity.getName());
			dto.setUpdateDate(entity.getUpdateDate());

			// Usuario
			if (entity.getUser() != null && entity.getUser().getId() != null) {
				dto.setUser(entityToDto(entity.getUser()));
			}

			// Autoridad
			if (entity.getAuthority() != null
					&& entity.getAuthority().getId() != null) {
				dto.setAuthority(entityToDto(entity.getAuthority()));
			}

			// Parent
			if (entity.getParent() != null
					&& entity.getParent().getId() != null) {
				dto.setParent(entityToDto(entity.getParent()));
			}

			// FolderType
			if(entity.getFolderType() != null
					&& entity.getFolderType().getId() != null){
				dto.setFolderType(entityToDto(entity.getFolderType()));
			}else{
				dto.setFolderType(null);
			}

		}
		return dto;
	}

	/**
	 * Convierte de FolderTypeEntity a FolderTypeDto
	 * 
	 * @param user
	 * @return
	 */
	protected FolderTypeDto entityToDto(AbstractFolderTypeEntity folderType) {
		FolderTypeDto dto = null;
		if (folderType != null) {
			dto = new FolderTypeDto();
			dto.setId(folderType.getId());
			dto.setType(folderType.getType());
		}
		return dto;
	}

	protected LayerTypeDto entityToDto(LayerTypeEntity entity) {
		LayerTypeDto dto = null;

		if (entity != null) {
			dto = new LayerTypeDto();
			dto.setId(entity.getId());
			dto.setName(entity.getName());
			dto.setTipo(entity.getTipo());
		}

		return dto;
	}

	// ################### Conversiones entre tipos que nos hacen falta en Layer
	// ##################
	// FIN ###################

	/**
	 * Dto a Entity de LayerMetadataEntity
	 * 
	 * @param dto
	 * @return
	 */
	protected LayerMetadataEntity dtoToEntity(LayerMetadataDto dto) {
		LayerMetadataEntity entity = null;

		if (dto != null) {
			entity = new LayerMetadataEntity();

			entity.setCalidad(dto.getCalidad());
			entity.setCrsPuntoReferencia(dto.getCrsPuntoReferencia());
			entity.setDistribuidor(dto.getDistribuidor());
			entity.setFechaactualizacion(dto.getFechaactualizacion());
			entity.setFechacreacion(dto.getFechacreacion());
			entity.setFormato(dto.getFormato());
			entity.setFrecuencia(dto.getFrecuencia());
			entity.setId(dto.getId());
			entity.setInformacion(dto.getInformacion());
			entity.setInformacionColeccionPuntoReferencia(dto
					.getInformacionColeccionPuntoReferencia());
			entity.setInformacionGeolocalizacion(dto
					.getInformacionGeolocalizacion());
			entity.setInformacionPuntoReferencia(dto
					.getInformacionPuntoReferencia());
			entity.setNombrePuntoReferencia(dto.getNombrePuntoReferencia());
			entity.setOtros(dto.getOtros());
			entity.setPeriodo(dto.getPeriodo());
			entity.setPosicion(dto.getPosicion());
			entity.setPrecision(dto.getPrecision());
			entity.setPuntosDeReferencia(dto.getPuntosDeReferencia());
			entity.setRango(dto.getRango());
			entity.setReferencia(dto.getReferencia());
			entity.setRequerimientos(dto.getRequerimientos());
			entity.setResponsable(dto.getResponsable());
			entity.setSiguiente(stringToDate(dto.getSiguiente(), FORMATO_FECHA));
		}

		return entity;
	}

	/**
	 * Convierte de String a Date
	 * 
	 * @param fecha
	 * @param formato
	 * @return
	 */
	public Date stringToDate(String fecha, String formato) {
		try {

			SimpleDateFormat format = new SimpleDateFormat(formato);

			return format.parse(fecha);
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * Copia de datos
	 * 
	 * @param m
	 * @param dto
	 * @return
	 */
	private LayerMetadataEntity copyAllData(LayerMetadataEntity m,
			LayerMetadataDto dto) {

		if (dto != null) {
			m.setCalidad(dto.getCalidad());
			m.setCrsPuntoReferencia(dto.getCrsPuntoReferencia());
			m.setDistribuidor(dto.getDistribuidor());
			m.setFechaactualizacion(dto.getFechaactualizacion());
			// m.setFechacreacion(dto.getFechacreacion());
			m.setFormato(dto.getFormato());
			m.setFrecuencia(dto.getFrecuencia());
			// m.setId(dto.getId());
			m.setInformacion(dto.getInformacion());
			m.setInformacionColeccionPuntoReferencia(dto
					.getInformacionColeccionPuntoReferencia());
			m.setInformacionGeolocalizacion(dto.getInformacionGeolocalizacion());
			m.setInformacionPuntoReferencia(dto.getInformacionPuntoReferencia());
			m.setNombrePuntoReferencia(dto.getNombrePuntoReferencia());
			m.setOtros(dto.getOtros());
			m.setPeriodo(dto.getPeriodo());
			m.setPosicion(dto.getPosicion());
			m.setPrecision(dto.getPrecision());
			m.setPuntosDeReferencia(dto.getPuntosDeReferencia());
			m.setRango(dto.getRango());
			m.setReferencia(dto.getReferencia());
			m.setRequerimientos(dto.getRequerimientos());
			m.setResponsable(dto.getResponsable());
			m.setSiguiente(stringToDate(dto.getSiguiente(), FORMATO_FECHA));

		}

		return m;
	}

	/**
	 * Convierte de Dto a Entity
	 * 
	 * @param entity
	 * @return
	 */
	protected LayerMetadataDto entityToDto(LayerMetadataEntity entity) {
		LayerMetadataDto dto = null;

		if (entity != null) {
			dto = new LayerMetadataDto();

			dto.setCalidad(entity.getCalidad());
			dto.setCrsPuntoReferencia(entity.getCrsPuntoReferencia());
			dto.setDistribuidor(entity.getDistribuidor());
			dto.setFechaactualizacion(entity.getFechaactualizacion());
			dto.setFechacreacion(entity.getFechacreacion());
			dto.setFormato(entity.getFormato());
			dto.setFrecuencia(entity.getFrecuencia());
			dto.setId(entity.getId());
			dto.setInformacion(entity.getInformacion());
			dto.setInformacionColeccionPuntoReferencia(entity
					.getInformacionColeccionPuntoReferencia());
			dto.setInformacionGeolocalizacion(entity
					.getInformacionGeolocalizacion());
			dto.setInformacionPuntoReferencia(entity
					.getInformacionPuntoReferencia());
			dto.setNombrePuntoReferencia(entity.getNombrePuntoReferencia());
			dto.setOtros(entity.getOtros());
			dto.setPeriodo(entity.getPeriodo());
			dto.setPosicion(entity.getPosicion());
			dto.setPrecision(entity.getPrecision());
			dto.setPuntosDeReferencia(entity.getPuntosDeReferencia());
			dto.setRango(entity.getRango());
			dto.setReferencia(entity.getReferencia());
			dto.setRequerimientos(entity.getRequerimientos());
			dto.setResponsable(entity.getResponsable());
			dto.setSiguiente(parseFechaFormat(entity.getSiguiente()));
		}

		return dto;
	}

	/**
	 * Formatea fecha
	 * 
	 * @param f
	 * @return
	 */
	public String parseFechaFormat(Date f) {
		if (f == null)
			return "";

		SimpleDateFormat formato = new SimpleDateFormat(FORMATO_FECHA);
		return formato.format(f);
	}
	
	/**
	 * Layers by layer type
	 * 
	 * @param idLayerType
	 * 
	 * @return layers with layer type given
	 */
	public List<LayerDto> getLayersByLayerType(Long idLayerType){
		return (List<LayerDto>) simpleEntitiesToDtos(ohigginsLayerDao.getLayersByLayerType(idLayerType));
	}

	/**
	 * Layers by layer type
	 * 
	 * @param idLayerType
	 * @param layerName
	 * 
	 * @return layers with layer type and layer name given
	 */
	public List<LayerDto> getLayersByLayerTypeAndLayerName(Long idLayerType, String layerName){
		return (List<LayerDto>) simpleEntitiesToDtos(ohigginsLayerDao.getLayersByLayerTypeAndLayerName(idLayerType, layerName));
	}
	
	/**
	 * Only copy simple properties
	 * 
	 * @param entities
	 * 
	 * @return dtos with simple properties
	 */
	private List<LayerDto> simpleEntitiesToDtos(
			List<LayerEntity> entities){
		List<LayerDto> dtos = new LinkedList<LayerDto>();
		for(LayerEntity layerEntity: entities){
			dtos.add(simpleEntityToDto(layerEntity));
		}
		return dtos;
	}

	/**
	 * Update only simple properties for a layer
	 * 
	 * @param layer
	 * 
	 * @return layer updated
	 */
	public LayerDto simpleUpdate(LayerDto layer){
		return entityToDto(getDao().makePersistent(simpleDtoToEntity(layer)));
	}
	
	/**
	 * Obtain number of layers like layerName
	 * 
	 * @param layerName
	 * 
	 * @return 0 if not found or number of layers with a name ilike layerName
	 */
	public Long getLayerCountByName(String layerName){
		return ohigginsLayerDao.getLayerCountByName(layerName);
	}

	@Override
	public LayerDto getByName(String geoserverName) {
		return entityToDto(ohigginsLayerDao.getByName(geoserverName));
	}

	/* (non-Javadoc)
	 * @see com.emergya.persistenceGeo.service.impl.AbstractServiceImpl#update(java.io.Serializable)
	 */
	@Override
	public Serializable update(Serializable dto) {
		// TODO Auto-generated method stub
		return super.update(dto);
	}
	
	
	
}
