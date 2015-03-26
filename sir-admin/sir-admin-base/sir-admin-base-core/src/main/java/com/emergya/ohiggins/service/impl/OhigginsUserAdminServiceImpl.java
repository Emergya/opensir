/*
 * OhigginsUserAdminServiceImpl.java
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
 * Authors:: Alejandro Díaz Torres (mailto:adiaz@emergya.com)
 */
package com.emergya.ohiggins.service.impl;

import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import javax.annotation.Resource;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.emergya.ohiggins.converter.RegionConverter;
import com.emergya.ohiggins.dao.OhigginsAuthorityEntityDao;
import com.emergya.ohiggins.dao.OhigginsLayerPublishRequestEntityDao;
import com.emergya.ohiggins.dao.OhigginsRegionEntityDao;
import com.emergya.ohiggins.dao.OhigginsUserEntityDao;
import com.emergya.ohiggins.dto.GrupoUsuariosDto;
import com.emergya.ohiggins.dto.RegionDto;
import com.emergya.ohiggins.dto.UsuarioDto;
import com.emergya.ohiggins.model.AuthorityEntity;
import com.emergya.ohiggins.model.LayerPublishRequestEntity;
import com.emergya.ohiggins.model.RegionEntity;
import com.emergya.ohiggins.model.RegionUserRelationship;
import com.emergya.ohiggins.model.UserEntity;
import com.emergya.ohiggins.service.UserAdminService;
import com.emergya.persistenceGeo.dao.AbstractGenericDao;
import com.emergya.persistenceGeo.dao.GenericDAO;
import com.emergya.persistenceGeo.metaModel.AbstractAuthorityEntity;
import com.emergya.persistenceGeo.metaModel.AbstractUserEntity;
import com.emergya.persistenceGeo.service.impl.AbstractServiceImpl;

/**
 * Implementacion transacional de UserAdminService basada en el uso de DAOs
 * {@link AbstractGenericDao}
 * 
 * @author <a href="mailto:adiaz@emergya.com">adiaz</a>
 * 
 */
@Repository("ohigginsUserAdminService")
@Transactional
public class OhigginsUserAdminServiceImpl extends
		AbstractServiceImpl<UsuarioDto, UserEntity> implements UserAdminService {

	private static final long serialVersionUID = -3840238261150854752L;
	private final static String ANONYMOUS_WORKSPACE_NAME = "anonymousWorkspace";

	@Resource
	private OhigginsUserEntityDao userDao;
	@Resource
	private OhigginsAuthorityEntityDao authorityDao;

	@Resource
	private OhigginsLayerPublishRequestEntityDao publishRequestDao;

	@Resource
	private OhigginsRegionEntityDao regionDao;

	public OhigginsUserAdminServiceImpl() {
		super();
	}

	/**
	 * Obtiene un usuario por nombre
	 * 
	 * @param username
	 *            nombre de usuario
	 * 
	 * @return
	 */
	public UsuarioDto obtenerUsuarioByUsername(String username) {
		UsuarioDto dto = entityToDto(userDao.getUser(username));
		return dto;
	}

	/**
	 * Obtiene un usuario por nombre
	 * 
	 * @param name
	 *            del usuario
	 * @param password
	 *            del usuario
	 * 
	 * @return si no existia lo crea sin grupo de usuarios
	 */
	public UsuarioDto obtenerUsuario(String name, String password) {
		UsuarioDto dto = entityToDto(userDao.getUser(name, password));
		// if (dto == null) {
		// dto = entityToDto(userDao.createUser(name, password));
		// }
		return dto;
	}

	/**
	 * Obtiene todos los usuarios del sistema
	 * 
	 * @return usuarios del sistema
	 */
	public List<UsuarioDto> obtenerUsuarios() {
		List<UsuarioDto> result = new LinkedList<UsuarioDto>();
		for (UserEntity entity : userDao.findAll()) {
			result.add(entityToDto(entity));
		}
		return result;
	}

	/**
	 * Obtiene todos los grupos de usuario del sistema
	 * 
	 * @return grupos de usuario del sistema
	 */
	public List<GrupoUsuariosDto> obtenerGruposUsuarios() {
		List<GrupoUsuariosDto> result = new LinkedList<GrupoUsuariosDto>();
		for (AbstractAuthorityEntity entity : authorityDao.findAll()) {
			result.add(entityGPToDto((AuthorityEntity) entity));
		}
		return result;
	}

	/**
	 * Obtiene el grupo de usuarios por id
	 * 
	 * @param id
	 *            del grupo
	 * 
	 * @return grupo de usuarios asociado al id o null si no lo encuentra
	 */
	public GrupoUsuariosDto obtenerGrupoUsuarios(Long id) {
		return entityGPToDto((AuthorityEntity) authorityDao.findById(id, false));
	}

	/**
	 * Crea un nuevo grupo de usuarios con los datos pasados por argumento
	 * 
	 * @param dto
	 * 
	 * @return id
	 */
	public Long crearGrupoUsuarios(GrupoUsuariosDto dto) {
		return authorityDao.save(dtoToEntity(dto));
	}

	/**
	 * Asocia un usuario a un grupo en particular
	 * 
	 * @param idGrupo
	 * @param usuario
	 */
	public void addUsuarioAGrupo(Long idGrupo, String usuario) {
		AbstractAuthorityEntity authorityEntity = authorityDao.findById(
				idGrupo, false);
		Set<AbstractUserEntity> usuarios = authorityEntity.getPeople();
		if (usuarios == null) {
			usuarios = new HashSet<AbstractUserEntity>();
		}
		boolean enc = false;
		for (AbstractUserEntity usuarioEntity : usuarios) {
			if (usuarioEntity.getUsername().equals(usuario)) {
				enc = true;
				break;
			}
		}
		if (!enc) {
			usuarios.add(userDao.getUser(usuario));
			authorityEntity.setPeople(usuarios);
			authorityDao.save((AuthorityEntity) authorityEntity);
		}
	}

	/**
	 * Elimina un usuario de un grupo en particular
	 * 
	 * @param idGrupo
	 * @param usuario
	 */
	@SuppressWarnings("unchecked")
	public void eliminaUsuarioDeGrupo(Long idGrupo, String usuario) {
		AbstractAuthorityEntity authorityEntity = authorityDao.findById(
				idGrupo, false);
		Set<AbstractUserEntity> usuarios = authorityEntity.getPeople();
		if (usuarios == null) {
			usuarios = new HashSet<AbstractUserEntity>();
		}
		boolean enc = false;
		for (AbstractUserEntity usuarioEntity : usuarios) {
			if (usuarioEntity.getUsername().equals(usuario)) {
				enc = true;
				usuarios.remove(usuarioEntity);
				break;
			}
		}
		if (enc) {
			authorityEntity.setPeople(usuarios);
			authorityDao.save((AuthorityEntity) authorityEntity);
		}
	}

	/**
	 * Elimina un grupo de usuarios
	 * 
	 * @param idgrupo
	 */
	public void eliminarGrupoUsuarios(Long idgrupo) {
		authorityDao.delete(idgrupo);
	}

	/**
	 * Modifica un grupo de usuarios
	 * 
	 * @param dto
	 */
	public void modificarGrupoUsuarios(GrupoUsuariosDto dto) {
		authorityDao.save(dtoToEntity(dto));
	}

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

			AuthorityEntity authority = authorityDao.findUserAuthority(user
					.getId());
			if (authority != null) {
				dto.setAuthorityId(authority.getId());
				dto.setAuthority(entityGPToDto(authority));

				// Region
				if (authority.getRegionAuthoritys() != null
						&& authority.getRegionAuthoritys().size() > 0
						&& authority.getRegionAuthoritys().get(0).getRegion() != null) {
					RegionEntity reg = regionDao.findById(authority
							.getRegionAuthoritys().get(0).getRegion().getId(),
							false);
					if (reg != null) {
						RegionDto regDto = RegionConverter.entityToDto(reg);
						dto.setRegion(regDto);
					}
				} else {
					dto.setRegion(null);
				}
			} else {
				// Region
				if (user.getRegionUsers() != null
						&& user.getRegionUsers().size() > 0
						&& user.getRegionUsers().get(0).getRegion() != null) {
					RegionEntity reg = regionDao.findById(user.getRegionUsers()
							.get(0).getRegion().getId(), false);
					if (reg != null) {
						RegionDto regDto = RegionConverter.entityToDto(reg);
						dto.setRegion(regDto);
					}
				} else {
					dto.setRegion(null);
				}
			}
		}
		return dto;
	}

	private GrupoUsuariosDto entityGPToDto(AuthorityEntity entity) {
		GrupoUsuariosDto dto = null;
		if (entity != null) {
			dto = new GrupoUsuariosDto();
			dto.setNombre(entity.getName());
			dto.setId(entity.getId());
			List<String> usuarios = new LinkedList<String>();
			// if (entity.getPeople() != null) {
			// for (UserEntity user : entity.getPeople()) {
			// usuarios.add(user.getUsername());
			// }
			// }
			dto.setUsuarios(usuarios);
		}
		return dto;
	}

	private AuthorityEntity dtoToEntity(GrupoUsuariosDto dto) {
		AbstractAuthorityEntity entity = null;
		if (dto != null) {
			if (dto.getId() != null) {
				entity = authorityDao.findById(dto.getId(), false);
			} else {
				entity = new AuthorityEntity();
			}
			entity.setName(dto.getNombre());

			// People
			Set<AbstractUserEntity> people = new HashSet<AbstractUserEntity>();
			if (dto.getUsuarios() != null) {
				for (String userName : dto.getUsuarios()) {
					people.add(userDao.getUser(userName));
				}
			}
			entity.setPeople(people);
		}
		return (AuthorityEntity) entity;
	}

	@Override
	protected UserEntity dtoToEntity(UsuarioDto dto) {
		UserEntity entity = null;
		Date now = new Date();

		if (dto.getId() != null && dto.getId() > 0) {
			entity = (UserEntity) userDao.findById(dto.getId(), true);
			entity.setCreateDate(now);
		} else {
			entity = new UserEntity();
		}
		entity.setAdmin(dto.isAdmin());
		entity.setApellidos(dto.getApellidos());
		entity.setEmail(dto.getEmail());
		entity.setUpdateDate(now);
		entity.setPassword(dto.getPassword());
		entity.setTelefono(dto.getTelefono());
		entity.setUsername(dto.getUsername());
		entity.setNombreCompleto(dto.getNombreCompleto());
		entity.setValid(dto.isValid());

		if (dto.getAuthorityId() != null) {
			AuthorityEntity aut = authorityDao.findById(dto.getAuthorityId(),
					false);
			entity.setAuthority(aut);
		} else if (dto.getAuthority() != null
				&& dto.getAuthority().getId() != null) {
			AbstractAuthorityEntity auth = authorityDao.findById(dto
					.getAuthority().getId(), false);
			entity.setAuthority(auth);
		} else {
			entity.setAuthority(null);
		}

		// Region
		List<RegionUserRelationship> regions = new LinkedList<RegionUserRelationship>();
		if (dto.getRegion() != null && dto.getRegion().getId() != null) {

			RegionUserRelationship regionUserRelationship = new RegionUserRelationship();
			RegionEntity reg = regionDao.findById(dto.getRegion().getId(),
					false);
			regionUserRelationship.setUser(entity);
			regionUserRelationship.setRegion(reg);
			regions.add(regionUserRelationship);
		}

		entity.setRegionUsers(regions);

		return entity;
	}

	/**
	 * @see UserAdminService#obtenerUsuarioById(long).
	 */
	@Override
	public UsuarioDto obtenerUsuarioById(long id) {
		UsuarioDto dto = entityToDto(userDao.findById((long) id, false));
		return dto;
	}

	/**
	 * @see UserAdminService#isUsernameAvailable(String).
	 */
	@Override
	public boolean isUsernameAvailable(String username) {
		UserEntity user = new UserEntity(username);
		return userDao.findByExample(user, new String[] {}).size() == 0;
	}

	@Override
	protected GenericDAO<UserEntity, Long> getDao() {
		return userDao;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<UsuarioDto> getFromToOrderBy(Integer first, Integer last,
			String propertyName) {
		List<UserEntity> entities = ((OhigginsUserEntityDao) getDao())
				.findAllFromToOrderBy(first, last, propertyName);
		return (List<UsuarioDto>) entitiesToDtos(entities);
	}

	// ################## IMPORTANTE ##########################3
	// NUEVOS METODOS
	/**
	 * Obtiene todos los usuarios del sistema que sean administradores
	 * 
	 * @return usuarios del sistema
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<UsuarioDto> obtenerUsuariosAdministradores() {
		List<UserEntity> entities = ((OhigginsUserEntityDao) getDao())
				.obtenerUsuariosAdministradores();
		return (List<UsuarioDto>) entitiesToDtos(entities);
	}

	/**
	 * Obtiene el nombre del workspace asociado a un usuario
	 * 
	 * @param user
	 */
	@Override
	public String getWorkspaceName(String user) {
		String workspaceName = ANONYMOUS_WORKSPACE_NAME;

		if (user != null) {
			UserEntity userEntity = userDao.getUser(user);
			if (userEntity != null && userEntity.getAuthority() != null) {
				AuthorityEntity authority = userEntity.getAuthority();
				workspaceName = authority.getWorkspaceName();
			}
		}
		return workspaceName;
	}

	@Override
	public boolean isUserAuthorityPublicService(String username) {

		return ((OhigginsUserEntityDao) getDao())
				.userHasPublicServiceAuthority(username);
	}

	@Override
	public void delete(Serializable dto) {
		if (dto == null) {
			throw new IllegalArgumentException(
					"null user dto passed to delete method!");
		}

		UsuarioDto userDto = (UsuarioDto) dto;

		List<LayerPublishRequestEntity> publishRequests = publishRequestDao
				.getByUser(dtoToEntity(userDto));

		// Fix for #87129: The publication request remains but isn't linked to
		// the
		// to-be-removed-user.
		for (LayerPublishRequestEntity request : publishRequests) {
			request.setUser(null);
			publishRequestDao.makePersistent(request);
		}

		super.delete(dto);
	}

}
