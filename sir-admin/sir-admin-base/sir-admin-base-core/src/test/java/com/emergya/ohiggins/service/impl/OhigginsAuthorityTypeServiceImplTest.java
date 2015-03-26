/* OhigginsAuthorityTypeServiceImplTest.java
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
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.junit.Test;

import com.emergya.ohiggins.ApplicationContextAwareTest;
import com.emergya.ohiggins.dto.AuthorityTypeDto;
import com.emergya.ohiggins.model.AuthorityTypeEntity;
import com.emergya.ohiggins.service.AuthorityTypeService;

/**
 * @author <a href="mailto:jlrodriguez@emergya.com">jlrodriguez</a>
 * 
 */
public class OhigginsAuthorityTypeServiceImplTest extends
		ApplicationContextAwareTest {

	@Resource(name = "ohigginsAuthorityTypeService")
	private AuthorityTypeService ohigginsAuthorityTypeService;

	/**
	 * Test method for
	 * {@link com.emergya.ohiggins.service.impl.OhigginsAuthorityTypeServiceImpl#getById(java.lang.Long)}
	 * .
	 */
	@Test
	public void testGetById() {
		AuthorityTypeDto dto = ohigginsAuthorityTypeService.getById(Long
				.valueOf(2));
		checkDto(dto);
	}

	/**
	 * @param dto
	 */
	private void checkDto(AuthorityTypeDto dto) {
		assertNotNull("Se esperaba un objeto", dto);
		assertNotNull(dto.getId());
		assertNotNull(dto.getPermisos());
		assertNotNull(dto.getEsCiudadano());
		assertNotNull(dto.getFechaActualizacion());
		assertNotNull(dto.getFechaCreacion());
		assertNotNull(dto.getName());
		// assertNotNull(dto.getAuthorities());
	}

	/**
	 * Test method for
	 * {@link com.emergya.ohiggins.service.impl.OhigginsAuthorityTypeServiceImpl#getAllOrdered()}
	 * .
	 */
	@Test
	public void testGetAllOrdered() {
		List<AuthorityTypeDto> result = ohigginsAuthorityTypeService
				.getAllAuthorityOrdered();
		assertEquals("Número incorrecto de AuthorityTypes devuelto", 3,
				result.size());
		String name = "";
		for (AuthorityTypeDto dto : result) {
			checkDto(dto);
			assertTrue("Los resultados no están en orden",
					name.compareTo(dto.getName()) <= 0);
			name = dto.getName();
		}

	}

	/**
	 * Test method for
	 * {@link com.emergya.ohiggins.service.impl.OhigginsAuthorityTypeServiceImpl#getAllOrderedByField(java.lang.String)}
	 * .
	 */
	@Test
	public void testGetAllOrderedByField() {
		List<AuthorityTypeDto> result = ohigginsAuthorityTypeService
				.getAllOrderedByField(AuthorityTypeEntity.Names.NAME.toString());
		assertTrue("Número incorrecto de AuthorityTypes devuelto",
				result.size() > 0);

		String name = "";
		for (AuthorityTypeDto dto : result) {
			checkDto(dto);
			assertTrue("Los resultados no están en orden",
					name.compareTo(dto.getName()) <= 0);
			name = dto.getName();
		}

	}

	/**
	 * Test method for
	 * {@link com.emergya.ohiggins.service.impl.OhigginsAuthorityTypeServiceImpl#getAllAuthority()}
	 * .
	 */
	@Test
	public void testGetAllAuthority() {
		List<AuthorityTypeDto> result = ohigginsAuthorityTypeService
				.getAllAuthority();
		assertEquals("Número incorrecto de AuthorityTypes devuelto", 3,
				result.size());

		for (AuthorityTypeDto dto : result) {
			checkDto(dto);
		}
	}

	/**
	 * Test method for
	 * {@link com.emergya.ohiggins.service.impl.OhigginsAuthorityTypeServiceImpl#getAllAuthorityOrdered()}
	 * .
	 */
	@Test
	public void testGetAllAuthorityOrdered() {
		List<AuthorityTypeDto> result = ohigginsAuthorityTypeService
				.getAllAuthorityOrdered();
		assertEquals("Número incorrecto de AuthorityTypes devuelto", 3,
				result.size());

		for (AuthorityTypeDto dto : result) {
			checkDto(dto);
		}
	}

	/**
	 * Test method for
	 * {@link com.emergya.persistenceGeo.service.impl.AbstractServiceImpl#create(java.io.Serializable)}
	 * .
	 */
	@Test
	public void testCreate() {
		AuthorityTypeDto dto = new AuthorityTypeDto();
		dto.setEsCiudadano(true);
		dto.setFechaActualizacion(new Date());
		dto.setFechaCreacion(new Date());
		dto.setName("Test authorityType");

		AuthorityTypeDto result = (AuthorityTypeDto) ohigginsAuthorityTypeService
				.create(dto);
		assertNotNull(result);
		assertNotNull("El objeto debe tener un ID", result.getId());
		assertEquals(dto.getEsCiudadano(), result.getEsCiudadano());
		assertEquals(dto.getFechaActualizacion(),
				result.getFechaActualizacion());
		assertEquals(dto.getFechaCreacion(), result.getFechaCreacion());
		assertEquals(dto.getName(), result.getName());
		assertEquals(dto.getPermisos(), result.getPermisos());
	}

	/**
	 * Test method for
	 * {@link com.emergya.persistenceGeo.service.impl.AbstractServiceImpl#update(java.io.Serializable)}
	 * .
	 */
	@Test
	public void testUpdate() {
		AuthorityTypeDto dto = new AuthorityTypeDto();
		dto.setId(Long.valueOf(1));
		dto.setName("Nuevo nombre");
		AuthorityTypeDto result = (AuthorityTypeDto) ohigginsAuthorityTypeService
				.update(dto);
		assertNotNull(result);
		assertNull(result.getFechaCreacion());
		assertEquals(
				"El objeto devuelto por la actualización tiene un ID distinto al original",
				dto.getId(), result.getId());
		assertEquals(dto.getName(), result.getName());

	}

	/**
	 * Test method for
	 * {@link com.emergya.persistenceGeo.service.impl.AbstractServiceImpl#delete(java.io.Serializable)}
	 * .
	 */
	@Test
	public void testDelete() {
		AuthorityTypeDto dto = new AuthorityTypeDto();
		dto.setId(Long.valueOf(1));
		ohigginsAuthorityTypeService.delete(dto);
		assertNull(ohigginsAuthorityTypeService.getById(Long.valueOf(1)));

	}

}
