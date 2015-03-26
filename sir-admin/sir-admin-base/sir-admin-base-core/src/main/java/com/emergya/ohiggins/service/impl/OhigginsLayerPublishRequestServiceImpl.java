/*
 * OhigginsLayerPublishRequestServiceImpl.java
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
import com.emergya.ohiggins.dao.OhigginsLayerMetadataTmpEntityDao;
import com.emergya.ohiggins.dao.OhigginsLayerPublishRequestEntityDao;
import com.emergya.ohiggins.dao.OhigginsLayerResourceDao;
import com.emergya.ohiggins.dao.OhigginsLayerTypeEntityDao;
import com.emergya.ohiggins.dao.OhigginsNivelTerritorialEntityDao;
import com.emergya.ohiggins.dao.OhigginsUserEntityDao;
import com.emergya.ohiggins.dto.AuthorityDto;
import com.emergya.ohiggins.dto.AuthorityTypeDto;
import com.emergya.ohiggins.dto.FolderDto;
import com.emergya.ohiggins.dto.LayerDto;
import com.emergya.ohiggins.dto.LayerMetadataTmpDto;
import com.emergya.ohiggins.dto.LayerPropertyDto;
import com.emergya.ohiggins.dto.LayerPublishRequestDto;
import com.emergya.ohiggins.dto.LayerTypeDto;
import com.emergya.ohiggins.dto.NivelTerritorialDto;
import com.emergya.ohiggins.dto.UsuarioDto;
import com.emergya.ohiggins.model.AuthorityEntity;
import com.emergya.ohiggins.model.FolderEntity;
import com.emergya.ohiggins.model.LayerEntity;
import com.emergya.ohiggins.model.LayerMetadataTmpEntity;
import com.emergya.ohiggins.model.LayerPropertyEntity;
import com.emergya.ohiggins.model.LayerPublishRequestEntity;
import com.emergya.ohiggins.model.LayerTypeEntity;
import com.emergya.ohiggins.model.UserEntity;
import com.emergya.ohiggins.model.ZoneEntity;
import com.emergya.ohiggins.service.LayerPublishRequestService;
import com.emergya.persistenceGeo.dao.GenericDAO;
import com.emergya.persistenceGeo.metaModel.AbstractAuthorityTypeEntity;
import com.emergya.persistenceGeo.service.impl.AbstractServiceImpl;

/**
 * Servicio para LayerPublishRequest : Solicitud de publicacion de capa
 * 
 * @author jariera
 * 
 */
@Repository
@Transactional
public class OhigginsLayerPublishRequestServiceImpl extends
		AbstractServiceImpl<LayerPublishRequestDto, LayerPublishRequestEntity>
		implements LayerPublishRequestService {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4017566309037267726L;
	// Formateo de fechas
	private static final String FORMATO_FECHA = "dd/MM/yyyy";

	@Resource
	private OhigginsLayerPublishRequestEntityDao ohigginsLayerPublishRequestDao;

	@Resource
	private OhigginsUserEntityDao userDao;

	@Resource
	private OhigginsAuthorityEntityDao authorityDao;

	@Resource
	private OhigginsNivelTerritorialEntityDao nivelTerritorialDao;

	@Resource
	private OhigginsAuthorityTypeEntityDao ohigginsAuthorityTypeEntityDao;

	@Resource
	private OhigginsLayerTypeEntityDao layerTypeDao;

	@Resource
	private OhigginsFolderEntityDao folderDao;

	@Resource
	private OhigginsLayerEntityDao layerDao;

	@Resource
	private OhigginsLayerMetadataTmpEntityDao layerMetadataTmpDao;

	@Resource
	private LayerPropertyDao layerPropertyDao;



	private static final String CADENA_VACIA = "";

	@Override
	protected GenericDAO<LayerPublishRequestEntity, Long> getDao() {
		return ohigginsLayerPublishRequestDao;
	}

	/**
	 * Lista de capas publicacion del usuario
	 * 
	 * @param first
	 * @param last
	 * @param propertyName
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<LayerPublishRequestDto> getFromToOrderBy(Integer first,
			Integer last, String propertyName, Long idUsuario) {
		List<LayerPublishRequestEntity> entities = ((OhigginsLayerPublishRequestEntityDao) getDao())
				.findAllFromToOrderBy(first, last, propertyName, idUsuario);

		return (List<LayerPublishRequestDto>) entitiesToDtos(entities);
	}

	/**
	 * Lista de capas publicacion pendientes del administrador
	 * 
	 * @param first
	 * @param last
	 * @param propertyName
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<LayerPublishRequestDto> getFromToOrderByAdmin(Integer first,
			Integer last, String propertyName, Long idUsuario) {
		List<LayerPublishRequestEntity> entities = ((OhigginsLayerPublishRequestEntityDao) getDao())
				.findAllFromToOrderByAdmin(first, last, propertyName, idUsuario);

		return (List<LayerPublishRequestDto>) entitiesToDtos(entities);
	}

	/**
	 * Convierte de entity a dto LayerPublishRequest
	 */
	@Override
	protected LayerPublishRequestDto entityToDto(
			LayerPublishRequestEntity entity) {

		LayerPublishRequestDto dto = null;

		if (entity != null) {
			dto = new LayerPublishRequestDto();

			dto.setActualizacion(entity.getActualizacion());

			dto.setComentario(entity.getComentario());
			dto.setEstado(entity.getEstado());
			dto.setFechaactualizacion(entity.getFechaactualizacion());
			dto.setFechacreacion(entity.getFechacreacion());
			dto.setFecharespuesta(entity.getFecharespuesta());
			dto.setFechasolicitud(entity.getFechasolicitud());
			dto.setId(entity.getId());
			dto.setLeida(entity.getLeida());
			dto.setNombredeseado(entity.getNombredeseado());
			dto.setRecursoservidor(entity.getRecursoservidor());
			dto.setTmpLayerName(entity.getTmpLayerName());
			dto.setPublishedFolder(entity.getPublishedFolder());

			dto.setSourceLayerName(entity.getSourceLayerName());
			dto.setSourceLayerId(entity.getSourceLayerId());

			String path = "";
			if (entity.getUpdatedLayerId() != null) {
				path = "Otros";
				LayerDto updatedLayer = entityToDto(layerDao.findById(
						entity.getUpdatedLayerId(), false));
				
				if(updatedLayer!=null) {
					if (updatedLayer.getFolder() != null) {
						path = updatedLayer.getFolder().getName();
					}
					
					path += " > " + updatedLayer.getLayerLabel();
				} else {
					path = "Capa Eliminada";
				}
				
			}
			dto.setUpdatedLayerPath(path);
			dto.setUpdatedLayerId(entity.getUpdatedLayerId());

			// Layer
			if (entity.getSourceLayerType() != null
					&& entity.getSourceLayerType().getId() != null) {
				LayerTypeEntity t = entity.getSourceLayerType();
				LayerTypeDto e = entityToDto(t);
				dto.setSourceLayerType(e);
			}

			// Autoridad
			if (entity.getAuth() != null && entity.getAuth().getId() != null) {
				AuthorityEntity aut = entity.getAuth();
				// authorityDao.findById(entity.getAuth().getId(), false);
				if (aut != null) {
					AuthorityDto e = entityToDto(aut);
					dto.setAuth(e);
				}
			}

			// Usuario
			if (entity.getUser() != null && entity.getUser().getId() != null) {
				UserEntity us = entity.getUser();
				// userDao.findById(entity.getUser().getId(), false);
				if (us != null) {
					UsuarioDto e = entityToDto(us);
					dto.setUser(e);
				}
			}

			// metadatos
			if (entity.getMetadata() != null
					&& entity.getMetadata().getId() != null) {
				LayerMetadataTmpEntity meta = entity.getMetadata();
				if (meta != null) {
					LayerMetadataTmpDto m = entityToDto(meta);
					dto.setMetadata(m);
				}
			}
			
			// We load the properties.
			if(entity.getProperties()!=null) {
				List<LayerPropertyDto> propDtos = new LinkedList<LayerPropertyDto>();				
				for(LayerPropertyEntity propEnt : entity.getProperties()) {
					propDtos.add(LayerPropertyDto.fromEntity(propEnt));
				}
				
				dto.setProperties(propDtos);
			}
		}

		return dto;
	}

	/**
	 * Convierte de dto a entity LayerPublishRequest
	 */
	@Override
	protected LayerPublishRequestEntity dtoToEntity(LayerPublishRequestDto dto) {
		LayerPublishRequestEntity entity = null;

		if (dto != null) {
			entity = new LayerPublishRequestEntity();

			entity.setActualizacion(dto.getActualizacion());
			entity.setComentario(dto.getComentario());
			entity.setEstado(dto.getEstado());
			entity.setFechaactualizacion(dto.getFechaactualizacion());
			entity.setFechacreacion(dto.getFechacreacion());
			entity.setFecharespuesta(dto.getFecharespuesta());
			entity.setFechasolicitud(dto.getFechasolicitud());
			entity.setId(dto.getId());
			entity.setLeida(dto.getLeida());
			entity.setNombredeseado(dto.getNombredeseado());
			entity.setRecursoservidor(dto.getRecursoservidor());
			entity.setTmpLayerName(dto.getTmpLayerName());
			
			// We convert the properties to entities.
			if(dto.getProperties()!=null) {
				List<LayerPropertyEntity> propEntities = new LinkedList<LayerPropertyEntity>();
				for(LayerPropertyDto propDto : dto.getProperties()) {
					LayerPropertyEntity prop;
					if(propDto.getId() != null){
						prop = layerPropertyDao.findById(propDto.getId(), false);
					}else{
						prop = new LayerPropertyEntity();
					}
					prop.setName(propDto.getName());
					prop.setValue(propDto.getValue());
					propEntities.add(prop);
				}
				entity.setProperties(propEntities);
			}
			
			entity.setPublishedFolder(dto.getPublishedFolder());

			entity.setUpdatedLayerId(dto.getUpdatedLayerId());

			// Autoridad
			if (dto.getAuth() != null && dto.getAuth().getId() != null) {
				AuthorityEntity a = authorityDao.findById(
						dto.getAuth().getId(), false);
				entity.setAuth(a);
			}

			// Usuario
			if (dto.getUser() != null && dto.getUser().getId() != null) {
				UserEntity u = userDao.findById(dto.getUser().getId(), false);
				entity.setUser(u);
			}

			entity.setSourceLayerName(dto.getSourceLayerName());
			entity.setSourceLayerId(dto.getSourceLayerId());

			// Layer
			if (dto.getSourceLayerType() != null
					&& dto.getSourceLayerType().getId() != null) {
				LayerTypeEntity f = layerTypeDao.findById(dto
						.getSourceLayerType().getId(), false);
				// f = copyAllData(f, dto.getLayer());
				entity.setSourceLayerType(f);
			}

			// metadatos
			if (dto.getMetadata() != null && dto.getMetadata().getId() != null) {
				LayerMetadataTmpEntity m = layerMetadataTmpDao.findById(dto
						.getMetadata().getId(), false);
				m = copyAllData(m, dto.getMetadata());
				entity.setMetadata(m);
			} else {
				if (dto.getMetadata() != null) {
					LayerMetadataTmpEntity m = dtoToEntity(dto.getMetadata());
					layerMetadataTmpDao.makePersistent(m);
					entity.setMetadata(m);
				}
			}
		}

		return entity;
	}

	/**
	 * Copia de datos
	 * 
	 * @param m
	 * @param dto
	 * @return
	 */
	private LayerMetadataTmpEntity copyAllData(LayerMetadataTmpEntity m,
			LayerMetadataTmpDto dto) {

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
	 * Convierte de entity a dto Layer
	 * 
	 * @param entity
	 * @return
	 */
	protected LayerDto entityToDto(LayerEntity entity) {
		LayerDto dto = null;

		if (entity != null) {
			dto = new LayerDto();

			// Autoridad
			/*
			 * if(entity.getAuth() != null && entity.getAuth().getId() != null){
			 * AuthorityEntity aut =
			 * authorityDao.findById(entity.getAuth().getId(), false); if(aut !=
			 * null){ AuthorityDto e = entityToDto(aut); dto.setAuthority(e); }
			 * }else{ dto.setAuthority(null); }
			 */

			// Usuario
			/*
			 * if(entity.getUser() != null && entity.getUser().getId() != null){
			 * UserEntity us = userDao.findById(entity.getUser().getId(),
			 * false); if(us != null){ UsuarioDto e = entityToDto(us);
			 * dto.setUser(e); } }else{ dto.setUser(null); }
			 */

			// Tipo
			if (entity.getType() != null && entity.getType().getId() != null) {
				LayerTypeEntity lt = layerTypeDao.findById(entity.getType()
						.getId(), false);
				if (lt != null) {
					LayerTypeDto ld = entityToDto(lt);
					dto.setType(ld);
				}
			} else {
				dto.setType(null);
			}

			// Folder
			
			 if(entity.getFolder() != null 
					 && entity.getFolder().getId() != null){ 
				 dto.setFolder(entityToDto(folderDao.findById(entity.getFolder().getId(),false)));
			}

			dto.setCreateDate(entity.getCreateDate());
			dto.setData(entity.getData());
			dto.setEnabled(entity.getEnabled());
			dto.setId(entity.getId());
			dto.setName(entity.getName());
			dto.setOrder(entity.getOrder());
			dto.setIsChannel(entity.getIsChannel());
			dto.setUpdateDate(entity.getUpdateDate());
			dto.setTableName(entity.getTableName());
			dto.setLayerTitle(entity.getLayerTitle());
			dto.setServer_resource(entity.getServer_resource());

		}

		return dto;
	}
	
	public FolderDto entityToDto(FolderEntity folderEntity) {
		
		if(folderEntity ==null) {
			return null;
		}
		
		FolderDto folderDto = new FolderDto();
		
		folderDto.setId(folderEntity.getId());
		folderDto.setName(folderEntity.getName());
		
		return folderDto;
	}

	/**
	 * Convierte de Dto a Entity
	 * 
	 * @param entity
	 * @return
	 */
	protected LayerMetadataTmpDto entityToDto(LayerMetadataTmpEntity entity) {
		LayerMetadataTmpDto dto = null;

		if (entity != null) {
			dto = new LayerMetadataTmpDto();

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
	 * Convierte de String a Date
	 * 
	 * @param fecha
	 * @param formato
	 * @return
	 */
	private Date stringToDate(String fecha, String formato) {
		try {

			SimpleDateFormat format = new SimpleDateFormat(formato);

			return format.parse(fecha);
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * Dto a Entity de LayerMetadataTmpEntity
	 * 
	 * @param dto
	 * @return
	 */
	protected LayerMetadataTmpEntity dtoToEntity(LayerMetadataTmpDto dto) {
		LayerMetadataTmpEntity entity = null;

		if (dto != null) {
			entity = new LayerMetadataTmpEntity();

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

	@Override
	public boolean existsRequest(AuthorityDto institucion, LayerDto layer) {
		return this.existsRequest(institucion, layer, null);
	}

	@Override
	public boolean existsRequest(AuthorityDto institucion, LayerDto layer,
			Long id) {
		LayerPublishRequestEntity searchExample = new LayerPublishRequestEntity();
		searchExample.setAuth(authorityDao.findById(institucion.getId(), true));
		searchExample.setSourceLayerId(layer.getId());

		List<LayerPublishRequestEntity> requests = ohigginsLayerPublishRequestDao
				.findByExample(searchExample, new String[] { "auth" });

		if (requests == null) {
			return false;
		}

		for (LayerPublishRequestEntity e : requests) {
			if (e.getSourceLayerId().equals(layer.getId())
					&& (id == null || !e.getId().equals(id))) {
				return true;
			}
		}
		return false;

	}

	@SuppressWarnings("unchecked")
	@Override
	public List<LayerPublishRequestDto> getPendingRequests() {
		LayerPublishRequestEntity example = new LayerPublishRequestEntity();
		example.setEstado(LayerPublishRequestEntity.PENDING_STATE);
		return (List<LayerPublishRequestDto>) entitiesToDtos(
				ohigginsLayerPublishRequestDao.findByExample(example, new String[]{}));
	}
	
	/**
	 * Obtain number of layer publish requests like sourceName
	 * 
	 * @param sourceName
	 * 
	 * @return 0 if not found or number of layer publish requests with a name ilike sourceName
	 */
	public Long sourceNameRequested(String sourceName){
		return ohigginsLayerPublishRequestDao.sourceNameRequested(sourceName);
	}

	@Override
	public LayerPublishRequestDto getByTmpLayerName(String geoserverName) {
		return entityToDto(ohigginsLayerPublishRequestDao.getByTmpLayerName(geoserverName));
	}
}
