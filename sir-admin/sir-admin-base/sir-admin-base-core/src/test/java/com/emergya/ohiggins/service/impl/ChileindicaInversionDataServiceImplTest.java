package com.emergya.ohiggins.service.impl;

import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.*;

import java.math.BigInteger;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.junit.Before;
import org.junit.Test;

import com.emergya.ohiggins.ApplicationContextAwareTest;
import com.emergya.ohiggins.dto.ChileindicaInversionDataDto;
import com.emergya.ohiggins.dto.ChileindicaInversionFinanciamientoDataDto;
import com.emergya.ohiggins.service.ChileindicaInversionDataService;

public class ChileindicaInversionDataServiceImplTest extends
		ApplicationContextAwareTest {
	@Resource
	private ChileindicaInversionDataService service;

	@Before
	public void setUp() throws Exception {
	}

	@Test
	public void testCreate() {
		ChileindicaInversionDataDto newDto = create(1, new Date());
		assertNotNull(newDto);
		assertNotNull("El ID del elemento salvado es nulo", newDto.getId());
		assertEquals("El ano no coincide", Integer.valueOf(2013),
				newDto.getAno());
		assertEquals("La asignación disponible no coincide",
				BigInteger.valueOf(10000),
				newDto.getAsignacionDisponibleInversion());
		assertNotNull("La lista de financiamientos es nula",
				newDto.getFinanciamientosList());
		assertThat("La lista de financimientos no tiene ningún elemento",
				newDto.getFinanciamientosList().size(), is(greaterThan(0)));
		ChileindicaInversionFinanciamientoDataDto financiamiento = newDto
				.getFinanciamientosList().get(0);
		assertNotNull("El id del financiamiento es null",
				financiamiento.getId());
		assertEquals("El nombre de la asignación presupuestaria no coincide",
				"Nombre", financiamiento.getNombreAsignacionPresupuestaria());

	}

	public ChileindicaInversionDataDto create(int cPreinversion,
			Date fechaRegistro) {
		ChileindicaInversionDataDto dto = new ChileindicaInversionDataDto();
		dto.setAno(2013);
		dto.setRegion(15);
		dto.setCInstitucion(1);
		dto.setCFicha(1);
		dto.setCPreinversion(cPreinversion);
		dto.setFechaRegistroChileindica(fechaRegistro);
		dto.setAsignacionDisponibleInversion(BigInteger.valueOf(10000));

		ChileindicaInversionFinanciamientoDataDto financiamientoDto = new ChileindicaInversionFinanciamientoDataDto();
		financiamientoDto.setNombreAsignacionPresupuestaria("Nombre");
		List<ChileindicaInversionFinanciamientoDataDto> financiamientos = new ArrayList<ChileindicaInversionFinanciamientoDataDto>();
		financiamientos.add(financiamientoDto);
		dto.setFinanciamientosList(financiamientos);
		ChileindicaInversionDataDto newDto = (ChileindicaInversionDataDto) service
				.create(dto);

		return newDto;
	}

	@Test
	public void testUpdate() {

		fail("Not yet implemented");
	}

	@Test
	public void testDelete() {
		ChileindicaInversionDataDto dto = create(1, Calendar.getInstance()
				.getTime());
		service.delete(dto);
		ChileindicaInversionDataDto searchedDto = (ChileindicaInversionDataDto) service
				.getById(dto.getId());
		assertNull("El objeto ya no debería estar en la base de datos",
				searchedDto);

	}

	@Test
	public void testCheckIfProjectMustBeUpdated() {
		create(10, null); // debe devolver true
		create(11, new Date()); // debe devolver false

		try {
			Date d = new SimpleDateFormat("yyyyMMdd").parse("20130601");
			create(12, d);
		} catch (ParseException e) {
		}

		assertTrue("Si el proyecto no tiene fecha, debe ser actualizado",
				service.checkIfProjectMustBeUpdated(15, 2013, 1, 10, 1, null));

		assertTrue("Si el proyecto no tiene fecha debe ser actualizado",
				service.checkIfProjectMustBeUpdated(15, 2013, 1, 10, 1, 7777));
		assertTrue("Si el proyecto no tiene fecha debe ser actualizado",
				service.checkIfProjectMustBeUpdated(15, 2013, 1, 10, 1,
						20130501));
		assertTrue("Si el proyecto no tiene fecha debe ser actualizado",
				service.checkIfProjectMustBeUpdated(15, 2013, 1, 10, 1,
						20130615));
		assertTrue(
				"Si la fecha pasada es null el proyecto debe ser actualizado",
				service.checkIfProjectMustBeUpdated(15, 2013, 1, 11, 1, null));
		assertFalse(
				"Si la fecha es anterior a la del proyecto, no debe ser actualizado",
				service.checkIfProjectMustBeUpdated(15, 2013, 1, 11, 1,
						20130501));
		assertTrue(
				"Si la fecha pasada no tiene formato de fecha, el proyecto debe ser actualizado",
				service.checkIfProjectMustBeUpdated(15, 2013, 1, 12, 1, 7777));
		assertFalse(
				"Si la fecha pasada es anterior a la del proyecto, no debe ser actualizado",
				service.checkIfProjectMustBeUpdated(15, 2013, 1, 12, 1,
						20130501));
		assertTrue(
				"Si la fecha pasada es posterior a la del proyecto, debe ser actualizado",
				service.checkIfProjectMustBeUpdated(15, 2013, 1, 12, 1,
						20130615));
		assertFalse(
				"Si la fecha pasada es la misma que la del proyecto, no debe ser actualizado",
				service.checkIfProjectMustBeUpdated(15, 2013, 1, 12, 1,
						20130601));

	}

	@Test
	public void testGetProjectByBussinessKey() {
		String nombreProyecto = "Test getProjectByBussinessKey";
		ChileindicaInversionDataDto dto = new ChileindicaInversionDataDto();
		Integer ano = 2013;
		Integer region = 15;
		Integer cInstitucion = -1;
		Integer cFicha = -2;
		Integer cPreinversion = -3;

		dto.setAno(ano);
		dto.setRegion(region);
		dto.setCInstitucion(cInstitucion);
		dto.setCFicha(cFicha);
		dto.setCPreinversion(cPreinversion);
		dto.setFechaRegistroChileindica(new Date());
		dto.setAsignacionDisponibleInversion(BigInteger.valueOf(10000));
		dto.setNombreProyecto(nombreProyecto);

		ChileindicaInversionFinanciamientoDataDto financiamientoDto = new ChileindicaInversionFinanciamientoDataDto();
		financiamientoDto.setNombreAsignacionPresupuestaria("Nombre");
		List<ChileindicaInversionFinanciamientoDataDto> financiamientos = new ArrayList<ChileindicaInversionFinanciamientoDataDto>();
		financiamientos.add(financiamientoDto);
		dto.setFinanciamientosList(financiamientos);
		service.create(dto);

		ChileindicaInversionDataDto foundDto = service
				.findProjectByBussinessKey(ano, region, cInstitucion,
						cPreinversion, cFicha);
		assertNotNull("No se ha encontrado el proyecto buscado", foundDto);
		assertEquals("El año no coincide", ano, foundDto.getAno());
		assertEquals("La region no coincide", region, foundDto.getRegion());
		assertEquals("El código de institución no coincide", cInstitucion,
				foundDto.getCInstitucion());
		assertEquals("El código de preinversión no coincide", cPreinversion,
				foundDto.getCPreinversion());
		assertEquals("El código de ficha no coincide", cFicha,
				foundDto.getCFicha());
		assertEquals("El nombre del proyecto no coincide", nombreProyecto,
				foundDto.getNombreProyecto());

	}
}
