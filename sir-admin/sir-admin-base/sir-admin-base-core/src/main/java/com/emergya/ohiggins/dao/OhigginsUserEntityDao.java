/*
 * UserEntityDao.java
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
 * Authors:: Alejandro Díaz Torres (mailto:adiaz@emergya.com)
 */
package com.emergya.ohiggins.dao;

import java.util.List;

import com.emergya.ohiggins.model.UserEntity;
import com.emergya.persistenceGeo.dao.GenericDAO;

/**
 * Dao para usuarios
 * 
 * @author <a href="mailto:adiaz@emergya.com">adiaz</a>
 * 
 */
public interface OhigginsUserEntityDao extends GenericDAO<UserEntity, Long> {

	/**
	 * Crea un nuevo usuario en el sistema
	 * 
	 * @param <code>userName</code>
	 * @param <code>password</code>
	 * 
	 * @return entidad del usuario creado
	 */
	public UserEntity createUser(String userName, String password);

	/**
	 * Obtiene un usuario por nombre de usuario
	 * 
	 * @param userName
	 * @param password
	 * 
	 * @return entidad asociada al nombre de usuario o null si no se encuentra
	 */
	public UserEntity getUser(String userName, String password);

	/**
	 * Obtiene un usuario por nombre de usuario
	 * 
	 * @param userName
	 * 
	 * @return entidad asociada al nombre de usuario o null si no se encuentra
	 */
	public UserEntity getUser(String userName);

	/**
	 * Obtiene una lista de usuarios ordenados por una propiedad.
	 * 
	 * @param first
	 * @param last
	 * @param propertyName
	 * @return
	 */
	public List<UserEntity> findAllFromToOrderBy(Integer first, Integer last,
			String propertyName);

	// ################## IMPORTANTE ##########################3
	// NUEVOS METODOS
	/**
	 * Obtiene todos los usuarios del sistema que sean administradores
	 * 
	 * @return usuarios del sistema
	 */
	public List<UserEntity> obtenerUsuariosAdministradores();

	public boolean userHasPublicServiceAuthority(String username);
}
