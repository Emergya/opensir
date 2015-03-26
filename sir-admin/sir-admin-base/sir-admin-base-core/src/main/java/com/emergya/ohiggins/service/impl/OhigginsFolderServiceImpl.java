/*
 * OhigginsFolderServiceImpl.java
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

import java.util.LinkedList;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.emergya.ohiggins.dao.OhigginsAuthorityEntityDao;
import com.emergya.ohiggins.dao.OhigginsAuthorityTypeEntityDao;
import com.emergya.ohiggins.dao.OhigginsFolderEntityDao;
import com.emergya.ohiggins.dao.OhigginsFolderTypeEntityDao;
import com.emergya.ohiggins.dao.OhigginsLayerEntityDao;
import com.emergya.ohiggins.dao.OhigginsNivelTerritorialEntityDao;
import com.emergya.ohiggins.dao.OhigginsUserEntityDao;
import com.emergya.ohiggins.dto.AuthorityDto;
import com.emergya.ohiggins.dto.AuthorityTypeDto;
import com.emergya.ohiggins.dto.FolderDto;
import com.emergya.ohiggins.dto.FolderTypeDto;
import com.emergya.ohiggins.dto.LayerDto;
import com.emergya.ohiggins.dto.NivelTerritorialDto;
import com.emergya.ohiggins.dto.NodeLayerTree;
import com.emergya.ohiggins.dto.UsuarioDto;
import com.emergya.ohiggins.model.AuthorityEntity;
import com.emergya.ohiggins.model.FolderEntity;
import com.emergya.ohiggins.model.FolderTypeEntity;
import com.emergya.ohiggins.model.UserEntity;
import com.emergya.ohiggins.model.ZoneEntity;
import com.emergya.ohiggins.service.FolderService;
import com.emergya.ohiggins.service.InstitucionService;
import com.emergya.ohiggins.service.LayerService;
import com.emergya.persistenceGeo.dao.GenericDAO;
import com.emergya.persistenceGeo.metaModel.AbstractAuthorityTypeEntity;
import com.emergya.persistenceGeo.service.FoldersAdminService;
import com.emergya.persistenceGeo.service.impl.AbstractServiceImpl;

/**
 * Implementacion FolderService
 * 
 * @author jariera
 * 
 */
@Repository
@Transactional
public class OhigginsFolderServiceImpl extends
		AbstractServiceImpl<FolderDto, FolderEntity> implements FolderService {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5543479268615968044L;

	private static final String CADENA_VACIA = "";

	@Resource
	private OhigginsFolderEntityDao folderDao;

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
	private OhigginsLayerEntityDao layerDao;
	
	@Resource
	private OhigginsFolderTypeEntityDao folderTypeDao;

	@Resource
	private LayerService layerService;

	@Override
	protected GenericDAO<FolderEntity, Long> getDao() {
		return folderDao;
	}

	@Override
	protected FolderDto entityToDto(FolderEntity entity) {
		FolderDto dto = null;

		if (entity != null) {
			dto = new FolderDto();
			dto.setCreateDate(entity.getCreateDate());
			dto.setEnabled(entity.getEnabled());
			dto.setFolderOrder(entity.getFolderOrder());
			dto.setId(entity.getId());
			dto.setName(entity.getName());
			dto.setUpdateDate(entity.getUpdateDate());

			// Autoridad
			if (entity.getAuthority() != null
					&& entity.getAuthority().getId() != null) {
				AuthorityEntity a = authorityDao.findById(entity.getAuthority()
						.getId(), false);
				dto.setAuthority(entityToDto(a));
			} else {
				dto.setAuthority(null);
			}

			// Usuario
			if (entity.getUser() != null && entity.getUser().getId() != null) {
				UserEntity u = userDao
						.findById(entity.getUser().getId(), false);
				dto.setUser(entityToDto(u));
			} else {
				dto.setUser(null);
			}

			// Folder padre
			if (entity.getParent() != null
					&& entity.getParent().getId() != null) {
				FolderEntity f = folderDao.findById(entity.getParent().getId(),
						false);
				dto.setParent(entityToDto(f));
			} else {
				dto.setParent(null);
			}

			// Zone
			if (entity.getZone() != null && entity.getZone().getId() != null) {
				ZoneEntity nivelT = nivelTerritorialDao.findById(new Long(
						entity.getZone().getId()), false);
				dto.setZone(entityToDto(nivelT));
			} else {
				dto.setZone(null);
			}
			
			// Folder Type
            if(entity.getFolderType() != null
            		&& entity.getFolderType().getId() != null){
            	FolderTypeEntity folderType = folderTypeDao.findById(
            			new Long(entity.getFolderType().getId()), false);
            	dto.setFolderType(entityToDto(folderType));
            }
            
            // TODO: remove IPT and channel flags
			boolean isChannel = false;
			if(entity.getParent() != null){
				FolderEntity parent = entity.getParent();
				while (parent.getParent() != null){
					parent = parent.getParent();
				}
				// only is channel if folder type of root parent is channel.
				isChannel = FoldersAdminService.DEFAULT_FOLDER_TYPE.equals(parent.getFolderType().getId()); 
			}else{
				// only is channel if folder type is channel.
				isChannel = FoldersAdminService.DEFAULT_FOLDER_TYPE.equals(entity.getFolderType().getId());
			}
			dto.setIsChannel(isChannel);

		}

		return dto;
	}

	@Override
	protected FolderEntity dtoToEntity(FolderDto dto) {
		FolderEntity entity = null;

		if (dto != null) {
			entity = new FolderEntity();
			if (dto.getCreateDate() != null)
				entity.setCreateDate(dto.getCreateDate());
			entity.setEnabled(dto.getEnabled());
			entity.setFolderOrder(dto.getFolderOrder());
			entity.setId(dto.getId());
			entity.setIsChannel(dto.getIsChannel());
			entity.setName(dto.getName());
			if (dto.getUpdateDate() != null)
				entity.setUpdateDate(dto.getUpdateDate());

			// Usuario
			if (dto.getUser() != null && dto.getUser().getId() != null) {
				UserEntity u = userDao.findById(dto.getUser().getId(), false);
				entity.setUser(u);
			} else {
				entity.setUser(null);
			}

			// Autoridad
			if (dto.getAuthority() != null
					&& dto.getAuthority().getId() != null) {
				AuthorityEntity a = authorityDao.findById(dto.getAuthority()
						.getId(), false);
				entity.setAuthority(a);
			} else {
				entity.setAuthority(null);
			}

			// Folder parent
			if (dto.getParent() != null && dto.getParent().getId() != null) {
				FolderEntity padre = folderDao.findById(
						dto.getParent().getId(), false);
				entity.setParent(padre);
			} else {
				entity.setParent(null);
			}

			// Zone
			if (dto.getZone() != null && dto.getZone().getId() != null) {
				ZoneEntity zone = nivelTerritorialDao.findById(
						Long.decode(dto.getZone().getId().toString()), false);
				entity.setZone(zone);
			}
			
			// Folder Type
            if(dto.getFolderType() != null){
            	FolderTypeEntity folderType = folderTypeDao.findById(
            			Long.decode(dto.getFolderType().getId().toString()), false);
            	entity.setFolderType(folderType);
            }
		}

		return entity;
	}

	/**
	 * 
	 * @return Devuelve lista de folder por id de autoridad
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<FolderDto> getFolderByAuthorityId(Long idAuth) {
		List<FolderEntity> entities = folderDao.getFolderByAuthorityId(idAuth);
		List<FolderDto> ldto = (List<FolderDto>) entitiesToDtos(entities);
		return ldto;
	}

	/**
	 * 
	 * @return Devuelve lista de folder que tengan como padre Id
	 * @param id
	 *            Identificador del padre
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<FolderDto> getFolderByParentId(Long idPadre) {
		List<FolderEntity> entities = folderDao.getFolderByParentId(idPadre);
		List<FolderDto> ldto = (List<FolderDto>) entitiesToDtos(entities);
		return ldto;
	}

	/**
	 * 
	 * @return Devuelve lista de folder que sean del usuario
	 * @param id
	 *            Identificador del usuario
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<FolderDto> getFolderByUserId(Long idUser) {
		List<FolderEntity> entities = folderDao.getFolderByUserId(idUser);
		List<FolderDto> ldto = (List<FolderDto>) entitiesToDtos(entities);
		return ldto;
	}

	/**
	 * 
	 * @return Devuelve lista de folder que son padres, es decir no tienen hijos
	 *         (folder_parent_id is null)
	 * @param id
	 *            Identificador del padre
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<FolderDto> getFolderParent() {
		List<FolderEntity> entities = folderDao.getFolderParent();
		List<FolderDto> ldto = (List<FolderDto>) entitiesToDtos(entities);
		return ldto;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<NodeLayerTree> getArbolCapas(Integer first, Integer last,
			String propertyName) {

		/*
		 * List<FolderEntity> entities = ((OhigginsFolderEntityDao)
		 * getDao()).getArbolCapas(first, last, propertyName);
		 */
		List<NodeLayerTree> l = new LinkedList<NodeLayerTree>();

		// Foldes raices
		List<FolderEntity> entities = ((OhigginsFolderEntityDao) getDao())
				.getFolderParent();

		// Para cada folder raiz
		NodeLayerTree nlt;
		FolderDto fdto;
		int nivel = 0;

		for (int i = 0; i < entities.size(); i++) {
			FolderEntity ee = entities.get(i);

			// Añadir Padre a la lista
			nlt = new NodeLayerTree();
			fdto = entityToDto(ee);
			nlt.setNivel(nivel);
			nlt.setNode(fdto);
			nlt.setTipo(NodeLayerTree.getFolder());

			l.add(nlt);

			listarFolders(ee, "", l, nivel + 1);

			l = addLayersOfFolder(ee.getId(), l, nivel);

		}

		return l;
	}

	/**
	 * Recursividad arbol de capas
	 * 
	 * @param f
	 * @param separador
	 * @param l
	 */
	private void listarFolders(FolderEntity f, String separador,
			List<NodeLayerTree> l, int nivel) {
		NodeLayerTree nlt;
		FolderDto fdto;

		// Obtener hijos
		List<FolderEntity> ficheros = folderDao.getFolderByParentId(f.getId());

		// Para cada hijo
		for (int x = 0; x < ficheros.size(); x++) {
			// System.out.println(separador + ficheros.get(x).getName());

			// Añadimos a la lista el Nodo
			nlt = new NodeLayerTree();
			fdto = entityToDto(ficheros.get(x));
			nlt.setNivel(nivel);
			nlt.setNode(fdto);
			nlt.setTipo(NodeLayerTree.getFolder());

			l.add(nlt);

			// l.add(entityToDto(ficheros.get(x)));

			// if (ficheros.get(x).getParent() != null &&
			// ficheros.get(x).getParent().getId() != null){//isDirectory()
			List<FolderEntity> lr = folderDao.getFolderByParentId(ficheros.get(
					x).getId());
			if (lr != null && lr.size() > 0) {// isDirectory()
				String nuevo_separador;
				nuevo_separador = separador + " ";
				// nivel++;
				listarFolders(ficheros.get(x), nuevo_separador, l, nivel + 1);
			}

			l = addLayersOfFolder(ficheros.get(x).getId(), l, nivel);

		}
	}

	/**
	 * Añade lista de capas de un folder
	 * 
	 * @param idFolder
	 * @param l
	 * @param nivel
	 * @return
	 */
	private List<NodeLayerTree> addLayersOfFolder(Long idFolder,
			List<NodeLayerTree> l, int nivel) {
		// Lista de capas de un folder "Padres"
		List<LayerDto> layers = layerService.getLayersByFolderId(idFolder);

		NodeLayerTree nlt;
		if (layers != null && layers.size() > 0) {
			for (LayerDto layerDto : layers) {
				nlt = new NodeLayerTree();
				nlt.setNivel(nivel + 1);
				nlt.setNode(layerDto);
				nlt.setTipo(NodeLayerTree.getLayer());

				l.add(nlt);
			}
		}

		return l;
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
	 * Convierte zoneEntity a NivelTerritorial
	 * 
	 * @param zone
	 * @return
	 */
	protected NivelTerritorialDto entityToDto(ZoneEntity zone) {
		NivelTerritorialDto dto = null;
		if (zone != null && zone.getId() != null) {
			dto = new NivelTerritorialDto();
			dto.setCodigo_territorio(zone.getCode());
			dto.setExtension(zone.getExtensionGeom());
			dto.setFecha_actualizacion(zone.getUpdateDate());
			dto.setFecha_creacion(zone.getCreateDate());
			dto.setId(new Integer(zone.getId().toString()));
			dto.setName(zone.getName());
			dto.setTipo_ambito(zone.getType());
		}
		return dto;
	}
	
	/**
	 * Convierte de FolderTypeEntity a FolderTypeDto
	 * 
	 * @param user
	 * @return
	 */
	protected FolderTypeDto entityToDto(FolderTypeEntity folderType) {
		FolderTypeDto dto = null;
		if (folderType != null) {
			dto = new FolderTypeDto();
			dto.setId(folderType.getId());
			dto.setType(folderType.getType());
		}
		return dto;
	}

	/**
	 * 
	 * @param idFolder
	 * 			Identificador del tipo de la carpeta
	 * @return FolderTypeDto
	 * 			Devuelve el FolderTypeDto que coincida con el id
	 */
	public FolderTypeDto getFolderTypeDto(Long idFolder) {
		FolderTypeDto dto = null;
		if(idFolder != null){
			dto = entityToDto(folderTypeDao.findById(idFolder, false));
		}
		return dto; 
	}

	/**
	 * @return List<FolderTypeDto>
	 * 			Devuelve la lista de todos los folder types
	 */
	public List<FolderTypeDto> getAllFolderType() {
		List<FolderTypeDto> dtoList = new LinkedList<FolderTypeDto>();
		List<FolderTypeEntity> entityList = folderTypeDao.findAll();
		if(entityList != null){
			for(FolderTypeEntity e: entityList){
				dtoList.add(entityToDto(e));
			}
		}
		return dtoList;
	}

}
