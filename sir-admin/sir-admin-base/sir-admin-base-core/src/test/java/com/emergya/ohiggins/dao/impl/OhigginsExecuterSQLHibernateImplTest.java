/* OhigginsExecuterSQLHibernateImplTest.java
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

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.List;

import javax.annotation.Resource;

import org.junit.Test;

import com.emergya.ohiggins.ApplicationContextAwareTest;

/**
 * 
 * @author <a href="mailto:jlrodriguez@emergya.com">jlrodriguez</a>
 * 
 */
public class OhigginsExecuterSQLHibernateImplTest extends
		ApplicationContextAwareTest {

	@Resource
	private OhigginsExecuterSQLHibernateImpl ohigginsExecuterSQL;

	@Test(expected = org.hibernate.exception.SQLGrammarException.class)
	public final void testExecute() {
		ohigginsExecuterSQL.execute("SELECT * FROM non_existent_table");
	}

	@Test(expected = org.hibernate.NonUniqueResultException.class)
	public final void testUniqueResult() {
		Object result = ohigginsExecuterSQL.uniqueResult("select now()");
		assertNotNull("No se devolvió el resultado de la consulta", result);

		// Debe lanzar una excepción
		ohigginsExecuterSQL
				.uniqueResult("select * from public.spatial_ref_sys");
	}

	@Test
	public final void testList() {
		@SuppressWarnings("rawtypes")
		List result = ohigginsExecuterSQL
				.list("select * from public.spatial_ref_sys limit 10");
		assertTrue("La lista devuelta debería tener elementos",
				result.size() > 0);
	}

}
