/* LoadChannelTest.java
 * 
 * Copyright (C) 2012
 * 
 * This file is part of project ohiggins-app
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
 * Authors:: Alejandro Díaz Torres (mailto:adiaz@emergya.com)
 */
package com.emergya.ohiggins.web;

import static com.emergya.ohiggins.utils.StaticFolders.*;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

import com.emergya.persistenceGeo.dto.LayerDto;
import com.emergya.persistenceGeo.dto.Treeable;
import com.emergya.persistenceGeo.service.LayerAdminService;
import com.emergya.persistenceGeo.web.RestFoldersAdminController;

/**
 * Test del controller de carpetas para la carga de canales
 * 
 * @author <a href="mailto:adiaz@emergya.com">adiaz</a>
 * 
 * @see RestFoldersAdminController
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:ohiggins-testApplicationContext.xml" })
@TransactionConfiguration(defaultRollback = true, transactionManager = "transactionManager")
@Transactional
public class LoadChannelTest{

	private static final Log LOG = LogFactory.getLog(LoadChannelTest.class);
	
	@Resource
	private LayerAdminService layerAdminService;

	@Resource
	private RestFoldersAdminController restFoldersAdminController;
	
	private static final String DATA = "data";
	private static final String SUCCESS = "success";
	private static final String RESULTS = "results";
	
	/**
	 * Comprueba que el canal 'Machalí devuelve datos'
	 */
	@Test
	public void testLoadFolder() {
		try{
			Map<String, Object> result =  restFoldersAdminController.loadFoldersById(MACHALI.toString(), null);
			List<LayerDto> layers = layerAdminService.getLayersByFolder(MACHALI, Boolean.TRUE, Boolean.TRUE);
			Assert.assertNotNull(result);
			Assert.assertNotNull(layers);
			Assert.assertNotNull(result.get(SUCCESS));
			Assert.assertEquals(result.get(SUCCESS), true);
			Assert.assertTrue("El tamaño de la lista del servicio es menor del esperado", (layers.size() >= 2));
			Assert.assertTrue("El tamaño de la lista devuelta por el controller es menor del esperado", ((Integer) result.get(RESULTS) >= 2));
			Assert.assertTrue("El tamaño de la lista devuelta por el controller es menor que el del servicio ("+result.get(RESULTS) + "<"+layers.size()+")", ((Integer) result.get(RESULTS)>= layers.size()));
			@SuppressWarnings("unchecked")
			List<Treeable> listResult = (List<Treeable>) result.get(DATA);
			Set<Long> foundIds = new HashSet<Long>();
			for(LayerDto layer: layers){
				Long childId = layer.getId();
				for(Treeable dto: listResult){
					if(dto.getData() instanceof LayerDto 
							&& dto.getId().equals(childId)){
						foundIds.add(dto.getId());
						break;
					}
				}
			}
			Assert.assertEquals(foundIds.size(), layers.size());
			
		}catch (Exception e){
			LOG.error(e);
			Assert.fail();
		}
	}

}
