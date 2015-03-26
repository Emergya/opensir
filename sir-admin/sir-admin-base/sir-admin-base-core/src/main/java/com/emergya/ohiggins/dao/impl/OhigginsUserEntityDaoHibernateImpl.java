/*
 * OhigginsUserEntityDaoHibernateImpl.java
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
package com.emergya.ohiggins.dao.impl;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.emergya.ohiggins.dao.OhigginsUserEntityDao;
import com.emergya.ohiggins.model.AuthorityTypeEntity;
import com.emergya.ohiggins.model.UserEntity;
import com.emergya.persistenceGeo.dao.impl.GenericHibernateDAOImpl;

/**
 * Implementacion de Usuario dao para hibernate
 * 
 * @author <a href="mailto:adiaz@emergya.com">adiaz</a>
 * 
 */
@Repository
public class OhigginsUserEntityDaoHibernateImpl extends
		GenericHibernateDAOImpl<UserEntity, Long> implements
		OhigginsUserEntityDao {

	/** Campo admin de la bbdd */
	private static final String ADMIN = "admin";

	/**
	 * Crea un nuevo usuario en el sistema
	 * 
	 * @param <code>userName</code>
	 * @param <code>password</code>
	 * 
	 * @return entidad del usuario creado
	 */
	@Transactional(readOnly = false)
	public UserEntity createUser(String userName, String password) {
		UserEntity entity = new UserEntity(userName);
		entity.setPassword(password);
		getSession().save(entity);
		return entity;
	}

	/**
	 * Obtiene un usuario por nombre de usuario
	 * 
	 * @param userName
	 * @param password
	 * 
	 * @return entidad asociada al nombre de usuario o null si no se encuentra
	 */
	@Transactional(readOnly = true)
	public UserEntity getUser(String userName, String password) {
		List<UserEntity> res = findByCriteria(
				Restrictions.eq("username", userName),
				Restrictions.eq("password", password));
		if (res != null && res.size() > 0) {
			return res.get(0);
		} else {
			return null;
		}
	}

	/**
	 * Obtiene un usuario por nombre de usuario
	 * 
	 * @param userName
	 * 
	 * @return entidad asociada al nombre de usuario o null si no se encuentra
	 */
	@Transactional(readOnly = true)
	public UserEntity getUser(String userName) {
		List<UserEntity> res = findByCriteria(Restrictions.eq("username",
				userName));
		if (res != null && res.size() > 0) {
			return res.get(0);
		} else {
			return null;
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<UserEntity> findAllFromToOrderBy(Integer first, Integer last,
			String propertyName) {
		Criteria criteria = getSession().createCriteria(super.persistentClass);
		criteria.setFirstResult(first);
		criteria.setMaxResults(last - first);
		criteria.addOrder(Order.asc(propertyName));
		return criteria.list();
	}

	// ################## IMPORTANTE ##########################3
	// NUEVOS METODOS
	/**
	 * Obtiene todos los usuarios del sistema que sean administradores
	 * 
	 * @return usuarios del sistema
	 */
	@SuppressWarnings("unchecked")
	public List<UserEntity> obtenerUsuariosAdministradores() {
		Criteria criteria = getSession().createCriteria(super.persistentClass);
		criteria.add(Restrictions.eq(ADMIN, true));
		return (List<UserEntity>) criteria.list();
	}

	@Override
	public boolean userHasPublicServiceAuthority(String username) {
		String hql = "select u.authority.authType.id from UserEntity u where u.username=:USERNAME";
		Long idAuthorityType = (Long) getSession().createQuery(hql).setString("USERNAME", username).uniqueResult();
		return AuthorityTypeEntity.SERVICIO_PUBLICO_ID.equals(idAuthorityType);
	}

}
