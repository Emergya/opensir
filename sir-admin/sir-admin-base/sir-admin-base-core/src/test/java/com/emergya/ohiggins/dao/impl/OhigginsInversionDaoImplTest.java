/* OhigginsInversionDaoImplTest.java
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

import static org.junit.Assert.fail;

import java.util.List;
import java.util.Map;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.emergya.ohiggins.ApplicationContextAwareTest;
import com.emergya.ohiggins.dao.OhigginsInversionDao;
import com.emergya.utils.TestUtils;

/**
 * 
 * @author <a href="mailto:jlrodriguez@emergya.com">Juan Luis Rodríguez
 *         Ponce</a>
 */
public class OhigginsInversionDaoImplTest extends ApplicationContextAwareTest {

	@Autowired
	private OhigginsInversionDao ohigginsInversionDao;

	public OhigginsInversionDaoImplTest() {
	}

	@BeforeClass
	public static void setUpClass() {
	}

	@AfterClass
	public static void tearDownClass() {
	}

	@Before
	public void setUp() {
	}

	@After
	public void tearDown() {
	}

	/**
	 * Test of getAnyosPreinversionOrderDesc method, of class
	 * OhigginsInversionDaoImpl.
	 */
	@Test
	public void testGetAnyosPreinversionOrderDesc() {
		System.out.println("getAnyosPreinversionOrderDesc");

		List<String> result = ohigginsInversionDao
				.getAnyosPreinversionOrderDesc();
		if (!TestUtils.isSortedDesc(result)) {
			fail("Los resultados no están ordenados descendentemente");
		}

		for (int i = 0; i < result.size() - 1; i++) {
			if (result.get(i).equals(result.get(i + 1))) {
				fail("Se han devuelto años duplicados.");
			}
		}
	}

	/**
	 * Test of getAnyosEjecucionOrderDesc method, of class
	 * OhigginsInversionDaoImpl.
	 */
	@Test
	public void testGetAnyosEjecucionOrderDesc() {
		System.out.println("getAnyosEjecucionOrderDesc");
		List<String> result = ohigginsInversionDao.getAnyosEjecucionOrderDesc();
		if (!TestUtils.isSortedDesc(result)) {
			fail("Los resultados no están ordenados descendentemente");
		}

		for (int i = 0; i < result.size() - 1; i++) {
			if (result.get(i).equals(result.get(i + 1))) {
				fail("Se han devuelto años duplicados.");
			}
		}
	}

	/**
	 * Test of getFuenteFinanciamientoPreinversionOrderAsc method, of class
	 * OhigginsInversionDaoImpl.
	 */
	@Test
	public void testGetFuenteFinanciamentoPreinversionOderAsc() {
		System.out.println("getFuenteFinanciamentoPreinversionOderAsc");
		List<String> result = ohigginsInversionDao
				.getFuenteFinanciamientoPreinversionOrderAsc("2012");
		if (!TestUtils.isSortedAsc(result)) {
			fail("Los resultados no están ordenados ascendentemente");
		}

		for (int i = 0; i < result.size() - 1; i++) {
			if (result.get(i).equals(result.get(i + 1))) {
				fail("Se han devuelto fuentes de financiamiento duplicadas.");
			}
		}
	}

	/**
	 * Test of getFuenteFinanciamientoEjecucionOrderAsc method, of class
	 * OhigginsInversionDaoImpl.
	 */
	@Test
	public void testGetFuenteFinanciamientoEjecucionOrderAsc() {
		System.out.println("getFuenteFinanciamientoEjecucionOrderAsc");
		List<String> result = ohigginsInversionDao
				.getFuenteFinanciamientoEjecucionOrderAsc("2012");
		if (!TestUtils.isSortedAsc(result)) {
			fail("Los resultados no están ordenados ascendentemente");
		}

		for (int i = 0; i < result.size() - 1; i++) {
			if (result.get(i).equals(result.get(i + 1))) {
				fail("Se han devuelto fuentes de financiamiento duplicadas.");
			}
		}
	}

	/**
	 * Test of getLineaFinancieraPreinversionOrderAsc method, of class
	 * OhigginsInversionDaoImpl.
	 */
	@Test
	public void testGetLineaFinancieraPreinversionOrderAsc() {
		System.out.println("getLineaFinancieraPreinversionOrderAsc");
		List<String> result = ohigginsInversionDao
				.getLineaFinancieraPreinversionOrderAsc("2012", "Fndr");
		if (!TestUtils.isSortedAsc(result)) {
			fail("Los resultados no están ordenados ascendentemente");
		}

		for (int i = 0; i < result.size() - 1; i++) {
			if (result.get(i).equals(result.get(i + 1))) {
				fail("Se han devuelto líneas financieras duplicadas.");
			}
		}
	}

	/**
	 * Test of getLineaFinancieraEjecucionOrderAsc method, of class
	 * OhigginsInversionDaoImpl.
	 */
	@Test
	public void testGetLineaFinancieraEjecucionOrderAsc() {
		System.out.println("getLineaFinancieraEjecucionOrderAsc");
		List<String> result = ohigginsInversionDao
				.getLineaFinancieraEjecucionOrderAsc("2012",
						"Prov.Fondo de Innovación a la Competitividad");
		if (!TestUtils.isSortedAsc(result)) {
			fail("Los resultados no están ordenados ascendentemente");
		}

		for (int i = 0; i < result.size() - 1; i++) {
			if (result.get(i).equals(result.get(i + 1))) {
				fail("Se han devuelto líneas financieras duplicadas.");
			}
		}
	}

	/**
	 * Test of getSectoresEjecucionOrderAsc method, of class
	 * OhigginsInversionDaoImpl.
	 */
	@Test
	public void testGetSectoresEjecucionOrderAsc() {
		System.out.println("getSectoresEjecucionOrderAsc");
		List<String> result = ohigginsInversionDao
				.getSectoresEjecucionOrderAsc("2012",
						"Prov.Fondo de Innovación a la Competitividad",
						"Gobierno Regional");
		if (!TestUtils.isSortedAsc(result)) {
			fail("Los resultados no están ordenados ascendentemente");
		}

		for (int i = 0; i < result.size() - 1; i++) {
			if (result.get(i).equals(result.get(i + 1))) {
				fail("Se han devuelto sectores duplicados.");
			}
		}
	}

	/**
	 * Test of getSectoresPreinversionOrderAsc method, of class
	 * OhigginsInversionDaoImpl.
	 */
	@Test
	public void testGetSectoresPreinversionOrderAsc() {
		System.out.println("getSectoresPreinversionOrderAsc");
		List<String> result = ohigginsInversionDao
				.getSectoresPreinversionOrderAsc("2012", "Fndr",
						"Cir. 33 Adquisición Máquinas y Equipos");
		if (!TestUtils.isSortedAsc(result)) {
			fail("Los resultados no están ordenados ascendentemente");
		}

		for (int i = 0; i < result.size() - 1; i++) {
			if (result.get(i).equals(result.get(i + 1))) {
				fail("Se han devuelto sectores duplicados.");
			}
		}
	}

	/**
	 * Test of getMontosPreinversionGroupBy method, of class
	 * OhigginsInversionDaoImpl.
	 */
	@SuppressWarnings("rawtypes")
	@Test
	public void testGetMontosPreinversionGroupBy() {
		System.out.println("getMontosPreinversionGroupBy");
		List<Map<String, Object>> result = ohigginsInversionDao
				.getMontosPreinversionGroupBy("2012", "sector", "Fndr", null,
						null, null);
		System.out.print(result);
		result = ohigginsInversionDao.getMontosPreinversionGroupBy("2012",
				"nivelTerritorial", "Fndr", null, null, null);

		System.out.print(result);
		result = ohigginsInversionDao.getMontosPreinversionGroupBy("2012",
				"fuente", null, null, null, null);
		System.out.print(result);
	}

	/**
	 * Test of getMontosEjecucionGroupBy method, of class
	 * OhigginsInversionDaoImpl.
	 */
	@Test
	public void testGetMontosEjecucionGroupBy() {
		System.out.println("getMontosEjecucionGroupBy");
		List<Map<String, Object>> result = ohigginsInversionDao
				.getMontosEjecucionGroupBy("2012", "sector",
						"Prov.Fondo de Innovación a la Competitividad", null,
						null, null);
		System.out.print(result);
		result = ohigginsInversionDao.getMontosEjecucionGroupBy("2012",
				"nivelTerritorial",
				"Prov.Fondo de Innovación a la Competitividad", null, null,
				null);

		System.out.print(result);
		result = ohigginsInversionDao.getMontosEjecucionGroupBy("2012",
				"fuente", null, null, null, null);
		System.out.print(result);
	}

}
