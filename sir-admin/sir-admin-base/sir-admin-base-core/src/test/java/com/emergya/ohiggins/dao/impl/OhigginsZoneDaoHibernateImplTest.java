/* OhigginsNivelTerritorialDaoHibernateImplTest.java
 * 
 * Copyright (C) 2012
 * 
 * This file is part of project ohiggins-core
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
import static org.junit.Assert.assertTrue;

import java.util.List;

import javax.annotation.Resource;

import org.junit.Test;

import com.emergya.ohiggins.ApplicationContextAwareTest;
import com.emergya.ohiggins.dao.OhigginsNivelTerritorialEntityDao;
import com.emergya.ohiggins.model.ZoneEntity;

/**
 * @author <a href="mailto:jlrodriguez@emergya.com">jlrodriguez</a>
 * 
 */
public class OhigginsZoneDaoHibernateImplTest extends
		ApplicationContextAwareTest {

	@Resource
	OhigginsNivelTerritorialEntityDao ohigginsNivelTerritorialDao;

	/**
	 * Test method for
	 * {@link com.emergya.ohiggins.dao.impl.OhigginsZoneDaoHibernateImpl#getAll()}
	 * .
	 */
	@Test
	public final void testGetAll() {
		List<ZoneEntity> allResult = ohigginsNivelTerritorialDao.getAll();
		assertTrue("Debe haber al menos una zona", allResult.size() > 0);
	}

	/**
	 * Test method for
	 * {@link com.emergya.ohiggins.dao.impl.OhigginsZoneDaoHibernateImpl#getZonesByType(java.lang.String)}
	 * .
	 */
	@Test
	public final void testGetNivelTerritorialByTipo() {
		List<ZoneEntity> tipoMunicipio = ohigginsNivelTerritorialDao
				.getZonesByType("M");
		List<ZoneEntity> tipoProvincia = ohigginsNivelTerritorialDao
				.getZonesByType("P");
		List<ZoneEntity> tipoRegion = ohigginsNivelTerritorialDao
				.getZonesByType("R");
		assertEquals("El número de zonas de tipo municipio es incorrecto", 33,
				tipoMunicipio.size());
		assertEquals("El número de zonas de tipo provincia es incorrecto", 3,
				tipoProvincia.size());
		assertEquals("El número de zonas de tipo región es incorrecto", 1,
				tipoRegion.size());

	}
}
