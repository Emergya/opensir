/* OhigginsInstitucionServiceImplTest.java
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
package com.emergya.ohiggins.service.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.junit.Test;

import com.emergya.ohiggins.ApplicationContextAwareTest;
import com.emergya.ohiggins.dto.AuthorityDto;
import com.emergya.ohiggins.dto.AuthorityTypeDto;
import com.emergya.ohiggins.dto.NivelTerritorialDto;
import com.emergya.ohiggins.service.InstitucionService;

/**
 * @author <a href="mailto:jlrodriguez@emergya.com">jlrodriguez</a>
 * 
 */
public class OhigginsInstitucionServiceImplTest extends
		ApplicationContextAwareTest {

	@Resource
	private InstitucionService ohigginsInstitucionService;

	/**
	 * Test method for
	 * {@link com.emergya.ohiggins.service.impl.OhigginsInstitucionServiceImpl#createInstitucion(com.emergya.ohiggins.dto.AuthorityDto)}
	 * .
	 */
	@Test
	public void testCreateInstitucion() {
		NivelTerritorialDto ntDto = new NivelTerritorialDto();
		ntDto.setCodigo_territorio("100");
		ntDto.setId(1);
		ntDto.setName("Zona");
		ntDto.setTipo_ambito("R");

		AuthorityTypeDto authorityType = new AuthorityTypeDto();
		authorityType.setEsCiudadano(false);
		authorityType.setId(Long.valueOf(1));
		authorityType.setName("Nombre tipo");

		AuthorityDto authorityDto = new AuthorityDto();
		authorityDto.setAuthority("Nombre Institución");
		authorityDto.setFechaActualizacion(new Date());
		authorityDto.setFechaCreacion(new Date());
		authorityDto.setNivelTerritorial(ntDto);
		authorityDto.setType(authorityType);
		AuthorityDto result = ohigginsInstitucionService
				.createInstitucion(authorityDto);
		assertNotNull(result);
		assertNotNull("El objeto guardado no tiene ID", result.getId());
		assertNotNull(result.getNivelTerritorial());
		assertNotNull(result.getType());

	}

	/**
	 * Test method for
	 * {@link com.emergya.ohiggins.service.impl.OhigginsInstitucionServiceImpl#getInstitucionById(long)}
	 * .
	 */
	@Test
	public void testGetInstitucionById() {
		AuthorityDto result = (AuthorityDto) ohigginsInstitucionService
				.getById(Long.valueOf(10));
		assertNotNull("El resultado devuelto es nulo", result);
		assertEquals(Long.valueOf(10), result.getId());
		assertEquals("I. Municipalidad de Litueche", result.getAuthority());
		assertNotNull("El tipo de autoridad es nulo", result.getType());
		assertEquals(Long.valueOf(1), result.getType().getId());
		assertNotNull("El ámbito territorial es nulo",
				result.getNivelTerritorial());
		assertEquals(Integer.valueOf(23), result.getNivelTerritorial().getId());
	}

	/**
	 * Test method for
	 * {@link com.emergya.ohiggins.service.impl.OhigginsInstitucionServiceImpl#tieneUsuariosAsociados(java.lang.Long)}
	 * .
	 */
	@Test
	public void testTieneUsuariosAsociados() {
		boolean mustBeTrue = ohigginsInstitucionService
				.hasUsers(Long.valueOf(34));
		boolean mustBeFalse = ohigginsInstitucionService
				.hasUsers(Long.valueOf(10));
		assertFalse("La institución no debería tener usuarios asociados",
				mustBeFalse);
		assertTrue("La institución debería tener usuarios asociados",
				mustBeTrue);

	}

	/**
	 * Test method for
	 * {@link com.emergya.ohiggins.service.impl.OhigginsInstitucionServiceImpl#getFromToOrdered(java.lang.Integer, java.lang.Integer)}
	 * .
	 */
	@Test
	public void testGetFromToOrdered() {
		List<AuthorityDto> result = ohigginsInstitucionService
				.getFromToOrdered(10, 20);
		assertEquals(10, result.size());

	}

	/**
	 * Test method for
	 * {@link com.emergya.ohiggins.service.impl.OhigginsInstitucionServiceImpl#getAllOrderedByName()}
	 * .
	 */
	@Test
	public void testGetAllOrderedByName() {
		List<AuthorityDto> result = ohigginsInstitucionService
				.getAllOrderedByName();
		assertTrue(result.size() > 0);
		String nombreElementoAnterior = "";
		for (AuthorityDto aut : result) {
			System.out.println(aut.getAuthority());
			assertTrue(
					"Results are not ordered by name (\""
							+ nombreElementoAnterior
							+ "\" should come before \"" + aut.getAuthority()
							+ "\")",
					nombreElementoAnterior.compareToIgnoreCase(aut
							.getAuthority()) <= 0);
			if (aut.getAuthority() != null) {
				nombreElementoAnterior = aut.getAuthority();
			}
		}

	}

	/**
	 * Test method for
	 * {@link com.emergya.ohiggins.service.impl.OhigginsInstitucionServiceImpl#isInstitucionAvailable(java.lang.String)}
	 * .
	 */
	@Test
	public void testIsInstitucionAvailable() {
		boolean mustBeTrue = ohigginsInstitucionService
				.isInstitucionAvailable("nonExistentName", Long.valueOf(1));
		boolean mustBeFalse = ohigginsInstitucionService
				.isInstitucionAvailable("I. Municipalidad de Navidad", Long.valueOf(1));

		assertTrue("El nombre de institución debería estar disponible",
				mustBeTrue);
		assertFalse("El nombre de institución debería estar ocupado",
				mustBeFalse);
	}

}
