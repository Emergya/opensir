/* OhigginsAuthorityEntityDaoHibernateImplTest.java
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

import static org.junit.Assert.*;
import static org.hamcrest.Matchers.*;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.junit.Before;
import org.junit.Test;

import com.emergya.ohiggins.ApplicationContextAwareTest;
import com.emergya.ohiggins.dao.OhigginsAuthorityEntityDao;
import com.emergya.ohiggins.model.AuthorityEntity;

/**
 * @author <a href="mailto:jlrodriguez@emergya.com">jlrodriguez</a>
 * 
 */

public class OhigginsAuthorityEntityDaoHibernateImplTest extends
		ApplicationContextAwareTest {

	@Resource
	OhigginsAuthorityEntityDao ohigginsAuthorityEntityDao;

	private AuthorityEntity exampleAuthority;

	@Before
	public final void preTest() {
		exampleAuthority = new AuthorityEntity();
		exampleAuthority.setCreateDate(new Date());
		exampleAuthority.setUpdateDate(new Date());
		exampleAuthority.setName("Example authority");
	}

	/**
	 * Test method for
	 * {@link com.emergya.ohiggins.dao.impl.OhigginsAuthorityEntityDaoHibernateImpl#save(com.emergya.ohiggins.model.AuthorityEntity)}
	 * .
	 */
	@Test
	public final void testSave() {

		Long id = ohigginsAuthorityEntityDao.save(exampleAuthority);
		AuthorityEntity retrievedAuthority = ohigginsAuthorityEntityDao
				.findById(id, false);
		assertEquals("Los id no coinciden", id, retrievedAuthority.getId());
		assertEquals("El nombre no coincide", "Example authority",
				retrievedAuthority.getName());

	}

	/**
	 * Test method for
	 * {@link com.emergya.ohiggins.dao.impl.OhigginsAuthorityEntityDaoHibernateImpl#delete(java.lang.Long)}
	 * .
	 */
	@Test
	public final void testDelete() {
		Long id = ohigginsAuthorityEntityDao.save(exampleAuthority);
		AuthorityEntity retrievedAuthority = ohigginsAuthorityEntityDao
				.findById(id, false);
		assertNotNull("No se ha podido guardar la AuthorityEntity de pruebas",
				retrievedAuthority);
		ohigginsAuthorityEntityDao.delete(id);
		AuthorityEntity deletedAuthority = ohigginsAuthorityEntityDao.findById(
				id, false);
		assertNull("Se ha recuperado un objeto con el id del objeto eliminado",
				deletedAuthority);

	}

	/**
	 * Test method for
	 * {@link com.emergya.ohiggins.dao.impl.OhigginsAuthorityEntityDaoHibernateImpl#findByUser(java.lang.Long)}
	 * .
	 */
	@Test
	public final void testFindByUser() {
		List<AuthorityEntity> result = ohigginsAuthorityEntityDao
				.findByUser(7L);
		assertEquals(
				"Se ha encontrado un número de authorities distinto del esperado",
				1, result.size());
		AuthorityEntity entity = result.get(0);
		assertEquals("El usuario no está asociado a la authority esperada",
				new Long(34), entity.getId());

	}

	/**
	 * Test method for
	 * {@link com.emergya.ohiggins.dao.impl.OhigginsAuthorityEntityDaoHibernateImpl#getFromToOrdered(java.lang.Integer, java.lang.Integer)}
	 * .
	 */
	@Test
	public final void testGetFromToOrdered() {
		List<AuthorityEntity> result = ohigginsAuthorityEntityDao
				.getFromToOrdered(40, 50);
		assertEquals("El tamaño de la lista no es el esperado", 10,
				result.size());

		String oldName = "";
		for (AuthorityEntity e : result) {
			assertTrue("Authorities are not sorted by name",
					oldName.compareToIgnoreCase(e.getName()) <= 0);
			if (e.getName() != null) {
				oldName = e.getName();
			}

		}

	}

	/**
	 * Test method for
	 * {@link com.emergya.ohiggins.dao.impl.OhigginsAuthorityEntityDaoHibernateImpl#getAllOrderedByField(com.emergya.ohiggins.model.AuthorityEntity.Names)}
	 * .
	 */
	@Test
	public final void testGetAllOrderedByField() {
		// Order by id
		List<AuthorityEntity> byId = ohigginsAuthorityEntityDao
				.getAllOrderedByField(AuthorityEntity.Names.ID);
		List<AuthorityEntity> byName = ohigginsAuthorityEntityDao
				.getAllOrderedByField(AuthorityEntity.Names.NAME);
		List<AuthorityEntity> byAuthType = ohigginsAuthorityEntityDao
				.getAllOrderedByField(AuthorityEntity.Names.AUTH_TYPE);
		List<AuthorityEntity> byCreateDate = ohigginsAuthorityEntityDao
				.getAllOrderedByField(AuthorityEntity.Names.CREATE_DATE);
		List<AuthorityEntity> byPeople = ohigginsAuthorityEntityDao
				.getAllOrderedByField(AuthorityEntity.Names.PEOPLE);
		List<AuthorityEntity> byUpdateDate = ohigginsAuthorityEntityDao
				.getAllOrderedByField(AuthorityEntity.Names.UPDATE_DATE);
		List<AuthorityEntity> byZone = ohigginsAuthorityEntityDao
				.getAllOrderedByField(AuthorityEntity.Names.ZONE);

		// By ID
		assertTrue(
				"[orderByID] El tamaño de la lista de authorities no es el esperado",
				byId.size() > 0);
		assertEquals(
				"[orderByID] El primer elemento de la lista no se corresponde con el esperado",
				new Long(1), byId.get(0).getId());

		// By Name
		assertEquals(
				"[orderByName] El tamaño de la lista de authorities no es el esperado",
				91, byName.size());
		assertEquals(
				"[orderByName] El primer elemento de la lista no se corresponde con el esperado",
				new Long(42), byName.get(0).getId());

		// By authType
		assertThat(
				"[orderByAuthType] El tamaño de la lista de authorities no es el esperado",
				0, lessThan(byAuthType.size()));

		assertEquals(
				"[orderByAuthType] El primer elemento de la lista no se corresponde con el esperado",
				new Long(1), byAuthType.get(0).getAuthType().getId());

		// By CreateDate
		assertEquals(
				"[orderByCrDate] El tamaño de la lista de authorities no es el esperado",
				91, byCreateDate.size());
		assertEquals(
				"[orderByCrdate] El primer elemento de la lista no se corresponde con el esperado",
				new Long(1), byCreateDate.get(0).getId());

		// By People
		assertEquals(
				"[orderByPeople] El tamaño de la lista de authorities no es el esperado",
				91, byPeople.size());
		assertEquals(
				"[orderByPeople] El primer elemento de la lista no se corresponde con el esperado",
				new Long(1), byId.get(0).getId());

		// By UpdateDate
		assertEquals(
				"[orderByUpDate] El tamaño de la lista de authorities no es el esperado",
				91, byUpdateDate.size());
		assertEquals(
				"[orderByUpDate] El primer elemento de la lista no se corresponde con el esperado",
				new Long(1), byUpdateDate.get(0).getId());

		// ByZone
		assertEquals(
				"[orderByZone] El tamaño de la lista de authorities no es el esperado",
				91, byZone.size());
		assertEquals(
				"[orderByZone] El primer elemento de la lista no se corresponde con el esperado",
				new Long(1), byZone.get(0).getZone().getId());
	}

	/**
	 * Test method for
	 * {@link com.emergya.ohiggins.dao.impl.OhigginsAuthorityEntityDaoHibernateImpl#findUserAuthority(java.lang.Long)}
	 * .
	 */
	@Test
	public final void testFindUserAuthority() {
		AuthorityEntity entity = ohigginsAuthorityEntityDao
				.findUserAuthority(Long.valueOf(7L));
		assertNotNull("No se ha encontrado authority asociada al usuario",
				entity);
		assertEquals("Se esperaba otra autoridad distinta asociada al usuario",
				Long.valueOf(34L), entity.getId());

	}

	/**
	 * Test method for
	 * {@link com.emergya.persistenceGeo.dao.impl.GenericHibernateDAOImpl#findById(java.lang.Long, boolean)}
	 * .
	 */
	@Test
	public final void testFindById() {
		AuthorityEntity entity = ohigginsAuthorityEntityDao.findById(
				Long.valueOf(57), false);
		assertNotNull("No se ha encontrado ninguna entidad con el id dado",
				entity);
		assertEquals(
				"El id de la authority devuelta no se corresponde con el solicitado",
				Long.valueOf(57), entity.getId());
		assertEquals(
				"El nombre de la authority devuelta no se corresponde con el esperado",
				"INJUV", entity.getName());
	}

}
