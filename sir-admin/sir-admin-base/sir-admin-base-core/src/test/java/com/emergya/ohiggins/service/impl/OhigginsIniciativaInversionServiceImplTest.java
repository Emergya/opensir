/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.emergya.ohiggins.service.impl;

import static org.junit.Assert.fail;

import java.util.List;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.emergya.ohiggins.ApplicationContextAwareTest;
import com.emergya.ohiggins.service.IniciativaInversionService;
import com.emergya.ohiggins.service.IniciativaInversionService.TIPO_PROYECTOS;
import com.emergya.utils.TestUtils;

/**
 * 
 * @author <a href="mailto:jlrodriguez@emergya.com">Juan Luis Rodríguez
 *         Ponce</a>
 */
public class OhigginsIniciativaInversionServiceImplTest extends
		ApplicationContextAwareTest {

	@Autowired
	private IniciativaInversionService iniciativaInversionService;

	public OhigginsIniciativaInversionServiceImplTest() {
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
	 * Test of getAnyosDisponibles method, of class
	 * OhigginsIniciativaInversionServiceImpl.
	 */
	@Test
	public void testGetAnyosDisponibles() {
		System.out.println("getAnyosDisponibles");

		for (TIPO_PROYECTOS tipoProyecto : TIPO_PROYECTOS.values()) {
			List<String> result = iniciativaInversionService
					.getAnyosDisponibles(tipoProyecto);
			if (!TestUtils.isSortedDesc(result)) {
				fail("Los resultados no están ordenados descendentemente");
			}

			for (int i = 0; i < result.size() - 1; i++) {
				if (result.get(i).equals(result.get(i + 1))) {
					fail("Se han devuelto años duplicados.");
				}
			}
		}

	}

	/**
	 * Test of getFuentesDisponibles method, of class
	 * OhigginsIniciativaInversionServiceImpl.
	 */
	@Test
	public void testGetFuentesDisponibles() {
		System.out.println("getFuentesDisponibles");
		for (TIPO_PROYECTOS tipoProyecto : TIPO_PROYECTOS.values()) {
			List<String> result = iniciativaInversionService
					.getFuentesDisponibles(tipoProyecto, "2012");
			if (!TestUtils.isSortedAsc(result)) {
				fail("Los resultados no están ordenados ascendentemente");
			}

			for (int i = 0; i < result.size() - 1; i++) {
				if (result.get(i).equals(result.get(i + 1))) {
					fail("Se han devuelto fuentes duplicadas.");
				}
			}
		}
	}

	/**
	 * Test of getLineasFinancierasDisponibles method, of class
	 * OhigginsIniciativaInversionServiceImpl.
	 */
	@Test
	public void testGetLineasFinancierasDisponibles() {
		System.out.println("getLineasFinancierasDisponibles");

		for (TIPO_PROYECTOS tipoProyecto : TIPO_PROYECTOS.values()) {
			List<String> result = iniciativaInversionService
					.getLineasFinancierasDisponibles(tipoProyecto, "2012",
							"Fndr");
			if (!TestUtils.isSortedAsc(result)) {
				fail("Los resultados no están ordenados ascendentemente");
			}

			for (int i = 0; i < result.size() - 1; i++) {
				if (result.get(i).equals(result.get(i + 1))) {
					fail("Se han devuelto líneas financieras duplicadas.");
				}
			}
		}
	}

	/**
	 * Test of getSectoresDisponibles method, of class
	 * OhigginsIniciativaInversionServiceImpl.
	 */
	@Test
	public void testGetSectoresDisponibles() {
		System.out.println("getSectoresDisponibles");
		for (TIPO_PROYECTOS tipoProyecto : TIPO_PROYECTOS.values()) {
			List<String> result = iniciativaInversionService
					.getSectoresDisponibles(tipoProyecto, "2012", "Fndr", "");
			if (!TestUtils.isSortedAsc(result)) {
				fail("Los resultados no están ordenados ascendentemente");
			}

			for (int i = 0; i < result.size() - 1; i++) {
				if (result.get(i).equals(result.get(i + 1))) {
					fail("Se han devuelto líneas financieras duplicadas.");
				}
			}
		}
	}
}
