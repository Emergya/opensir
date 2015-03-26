/* OhigginsAuthorityTypeEntityDaoHibernateImplTest.java
 * 
 * 
 * Copyright (C) 2012
 * 
 * This file is part of Proyecto ohiggins
 * 
 * This software is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 * 
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301  USA
 *
 * As a special exception, if you link this library with other files to
 * produce an executable, this library does not by itself cause the
 * resulting executable to be covered by the GNU General Public License.
 * This exception does not however invalidate any other reasons why the
 * executable file might be covered by the GNU General Public License.
 * 
 * Authors:: Juan Luis Rodríguez Ponce (mailto:jlrodriguez@emergya.com)
 */
package com.emergya.ohiggins.dao.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.junit.Before;
import org.junit.Test;

import com.emergya.ohiggins.ApplicationContextAwareTest;
import com.emergya.ohiggins.dao.OhigginsAuthorityTypeEntityDao;
import com.emergya.ohiggins.model.AuthorityTypeEntity;

/**
 * 
 * @author <a href="mailto:jlrodriguez@emergya.com">jlrodriguez</a>
 * 
 */
public class OhigginsAuthorityTypeEntityDaoHibernateImplTest extends
		ApplicationContextAwareTest {

	@Resource
	private OhigginsAuthorityTypeEntityDao ohigginsAuthorityTypeEntityDao;

	private AuthorityTypeEntity authorityType;

	@Before
	public void beforeTest() {
		authorityType = new AuthorityTypeEntity();
		authorityType.setName("Test authority type");
		authorityType.setCitizen(false);
		authorityType.setCreateDate(new Date());
		authorityType.setUpdateDate(new Date());
	}

	/**
	 * Test method for
	 * {@link com.emergya.ohiggins.dao.impl.OhigginsAuthorityTypeEntityDaoHibernateImpl#getAllOrdered()}
	 * .
	 */
	@Test
	public final void testGetAllOrdered() {
		List<AuthorityTypeEntity> result = ohigginsAuthorityTypeEntityDao
				.getAllOrdered();
		// Check order
		assertEquals("El primer elemento no es el esperado", "Ciudadano",
				result.get(0).getName());
		assertEquals("El último tipo de autoridad no es el esperado",
				"Servicio Público", result.get(3).getName());

	}

	/**
	 * Test method for
	 * {@link com.emergya.ohiggins.dao.impl.OhigginsAuthorityTypeEntityDaoHibernateImpl#getAllOrderedByField(java.lang.String)}
	 * .
	 */
	@SuppressWarnings("unused")
	@Test
	public final void testGetAllsOrderedByField() {
		List<AuthorityTypeEntity> byId = ohigginsAuthorityTypeEntityDao
				.getAllOrderedByField(AuthorityTypeEntity.Names.ID.toString());
		List<AuthorityTypeEntity> byName = ohigginsAuthorityTypeEntityDao
				.getAllOrderedByField(AuthorityTypeEntity.Names.NAME.toString());

		assertEquals("El primer elememento no es el esperado", Long.valueOf(1),
				byId.get(0).getId());
		assertEquals("El primer elememento no es el esperado", Long.valueOf(4),
				byName.get(0).getId());

		// Pedimos el resto para comprobar el correcto mapping del enum Names
		// pero no hacemos ninguna comprobación sobre los resultados.
		List<AuthorityTypeEntity> byAuthList = ohigginsAuthorityTypeEntityDao
				.getAllOrderedByField(AuthorityTypeEntity.Names.AUTH_LIST
						.toString());
		List<AuthorityTypeEntity> byCitizen = ohigginsAuthorityTypeEntityDao
				.getAllOrderedByField(AuthorityTypeEntity.Names.CITIZEN
						.toString());
		List<AuthorityTypeEntity> byCreateDate = ohigginsAuthorityTypeEntityDao
				.getAllOrderedByField(AuthorityTypeEntity.Names.CREATE_DATE
						.toString());
		List<AuthorityTypeEntity> byPermissionList = ohigginsAuthorityTypeEntityDao
				.getAllOrderedByField(AuthorityTypeEntity.Names.PERMISSION_LIST
						.toString());
		List<AuthorityTypeEntity> byUpdateDate = ohigginsAuthorityTypeEntityDao
				.getAllOrderedByField(AuthorityTypeEntity.Names.UPDATE_DATE
						.toString());
	}

	/**
	 * Test method for
	 * {@link com.emergya.ohiggins.dao.impl.OhigginsAuthorityTypeEntityDaoHibernateImpl#getAllAuthority()}
	 * .
	 */
	@Test
	public final void testGetAllAuthority() {
		List<AuthorityTypeEntity> authorityEntityList = ohigginsAuthorityTypeEntityDao
				.getAllAuthority();
		assertEquals("Número de authorityType no ciudadanos equivocado", 3,
				authorityEntityList.size());
		for (AuthorityTypeEntity authorityTypeEntity : authorityEntityList) {
			assertFalse("Se ha devuelto un authoritype de tipo ciudadano",
					authorityTypeEntity.isCitizen());
		}
	}

	/**
	 * Test method for
	 * {@link com.emergya.ohiggins.dao.impl.OhigginsAuthorityTypeEntityDaoHibernateImpl#getAllAuthorityOrdered()}
	 * .
	 */
	@Test
	public final void testGetAllAuthorityOrdered() {
		List<AuthorityTypeEntity> result = ohigginsAuthorityTypeEntityDao
				.getAllAuthorityOrdered();
		assertEquals("Número equivocado de resultados", 3, result.size());
		assertEquals("Primer elemento equivocado", "Municipalidad",
				result.get(0).getName());
		for (AuthorityTypeEntity authorityTypeEntity : result) {
			assertFalse("Se ha devuelto un authoritype de tipo ciudadano",
					authorityTypeEntity.isCitizen());
		}
	}

	/**
	 * Test method for
	 * {@link com.emergya.persistenceGeo.dao.impl.GenericHibernateDAOImpl#findById(java.lang.Long, boolean)}
	 * .
	 */
	@Test
	public final void testFindById() {
		AuthorityTypeEntity result = ohigginsAuthorityTypeEntityDao.findById(
				Long.valueOf(2), false);
		assertNotNull("No se encontró el AuthorityTypeEntity solicitado",
				result);
		assertEquals(
				"ID de AuthorityTypeEntity no se corresponde con el solicitado",
				Long.valueOf(2), result.getId());

	}

	/**
	 * Test method for
	 * {@link com.emergya.persistenceGeo.dao.impl.GenericHibernateDAOImpl#findAll()}
	 * .
	 */
	@Test
	public final void testFindAll() {
		List<AuthorityTypeEntity> result = ohigginsAuthorityTypeEntityDao
				.findAll();
		assertTrue("Número de resultados no corresponde con el esperado",
				0 >= result.size());
	}
}
