/*
 * SHP2PostgisServiceTest.java
 * 
 * Copyright (C) 2013
 * 
 * This file is part of sir-admin
 * 
 * This software is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License as published by the Free
 * Software Foundation; either version 2 of the License, or (at your option) any
 * later version.
 * 
 * This software is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU General Public License along with
 * this library; if not, write to the Free Software Foundation, Inc., 51
 * Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA
 * 
 * As a special exception, if you link this library with other files to produce
 * an executable, this library does not by itself cause the resulting executable
 * to be covered by the GNU General Public License. This exception does not
 * however invalidate any other reasons why the executable file might be covered
 * by the GNU General Public License.
 * 
 * Authors:: Alejandro Díaz Torres (mailto:adiaz@emergya.com)
 */
package com.emergya.ohiggins.service.impl;

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

import com.emergya.ohiggins.dto.FolderDto;
import com.emergya.ohiggins.dto.LayerDto;
import com.emergya.ohiggins.dto.LayerPublishRequestDto;
import com.emergya.ohiggins.dto.LayerResourceDto;
import com.emergya.ohiggins.service.FolderService;
import com.emergya.ohiggins.service.LayerPublishRequestService;
import com.emergya.ohiggins.service.LayerResourceService;
import com.emergya.ohiggins.service.LayerService;
import com.emergya.ohiggins.service.LayerTypeService;
import com.emergya.ohiggins.service.SHP2PostgisService;


/**
 * SHP 2 postgis test
 * 
 * @author <a href="mailto:adiaz@emergya.com">adiaz</a>
 * 
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:ohiggins-testModelContext.xml" })
@TransactionConfiguration(defaultRollback = false, transactionManager = "transactionManager")
@Transactional
public class SHP2PostgisServiceTest{	

	private static final Log LOG = LogFactory.getLog(SHP2PostgisServiceTest.class);

	@Resource
	private SHP2PostgisService migrateSHP;
	
	@Resource
	private LayerService layerService;
	
	@Resource
	private LayerResourceService layerResourceService;
	
	@Resource
	private LayerPublishRequestService layerPublishRequestService;
	
	@Resource
	private LayerTypeService layerTypeService;
	
	@Resource
	private FolderService folderService;
	
	/**
	 * Flag to activate or deactivate simple test
	 */
	private static final boolean SIMPLE_TEST = false;
	
	/**
	 * Flag to activate or deactivate all migration test
	 */
	private static final boolean TEST_ALL = false;
	
	/**
	 * Temporal dto with shape file layer representation created in {@link #testSHPMigrateStep0()} 
	 */
	private static LayerDto SHP_LAYER;
	
	/**
	 * Temporal Dto with shape file layer migration to postgis in {@link #testSHPMigrateStep1()} 
	 */
	private static LayerDto TEMPORAL_MIGRATION;
	
	/**
	 * Create a simple layer that represents a SHP layer for a shape file called PRI_Villarrica_Pucon and published in geoserver
	 * 'sig-minen-apps.emergya.es' in workspace 'gore'
	 */
	@Test
	public void testSHPMigrateStep0() {
		if(SIMPLE_TEST){
			try{
				// Needs PRI Villarica PUCON in /home/devel/tmp-shp
				SHP_LAYER = new LayerDto();
				SHP_LAYER.setName("minen:PRI_Villarrica_Pucon");
				SHP_LAYER.setServer_resource("http://sig-minen-apps.emergya.es/geoserver/wms?");
				SHP_LAYER.setEnabled(Boolean.TRUE);
				SHP_LAYER.setType(layerTypeService.getLayerTypeById(LayerTypeService.WMS_VECTORIAL_ID));
				SHP_LAYER.setFolder((FolderDto) folderService.getById(new Long (1)));
				SHP_LAYER = (LayerDto) layerService.create(SHP_LAYER);
			}catch (Exception e){
				LOG.error(e);
				Assert.fail();
			}
		}else{
			LOG.info("SHP 2 postgis migration test is disabled!");
		}
	} 
	
	/**
	 * In this step migrate the {@link #SHP_LAYER} created in {@link #testSHPMigrateStep0()}  
	 * to postgis and save into {@link #TEMPORAL_MIGRATION} 
	 */
	@Test
	public void testSHPMigrateStep1() {
		if(SIMPLE_TEST){
		try{
			TEMPORAL_MIGRATION = migrateSHP.duplicateSHPLayers(SHP_LAYER);			
			LOG.debug("Layer migrated after SHP delete "+ TEMPORAL_MIGRATION.getId());
		}catch (Exception e){
			LOG.error(e);
			Assert.fail();
		}
		}else{
			LOG.info("SHP 2 postgis migration test is disabled!");
		}
	} 
	
	/**
	 * In this step migrate clean {@link #SHP_LAYER} in database and rename 
	 * {@link #TEMPORAL_MIGRATION} to the old name of {@link #SHP_LAYER} for 
	 * do a transparent migration    
	 */
	@Test
	public void testSHPMigrateStep2() {
		if(SIMPLE_TEST){
			try{
				LayerDto layerBeforeDelete = migrateSHP.deleteDuplicatedSHPLayer(SHP_LAYER);			
				LOG.debug("Layer migrated before delete "+ layerBeforeDelete.getId());
				Assert.assertEquals(TEMPORAL_MIGRATION.getId(), layerBeforeDelete.getId());
			}catch (Exception e){
				LOG.error(e);
				Assert.fail();
			}
		}else{
			LOG.info("SHP 2 postgis migration test is disabled!");
		}
	} 

	
	/**
	 * SHP layers to postgis migration test.
	 * Temporal test that should not be executed in all compilations.
	 */
	@Test
	public void testDuplicateAllSHPLayers() {
		if(TEST_ALL){
			try{
				migrateSHP.duplicateAllSHPLayers();
			}catch (Exception e){
				LOG.error(e);
				Assert.fail();
			}
		}else{
			LOG.info("SHP 2 postgis migration test is disabled!");
		}
	}
	
	/**
	 * SHP layers to postgis migration test.
	 * Temporal test that should not be executed in all compilations.
	 */
	@Test
	public void testDeleteDuplicatedSHPLayers() {
		if(TEST_ALL){
			try{
				migrateSHP.duplicateAllSHPLayers();
				migrateSHP.deleteDuplicatedSHPLayers();
			}catch (Exception e){
				LOG.error(e);
				Assert.fail();
			}
		}else{
			LOG.info("SHP 2 postgis migration test is disabled!");
		}
	}
	
	private static final String LAYER_IN_USE_1 = "testLayerInUse1";
	private static final String LAYER_IN_USE_2 = "testLayerInUse2";
	private static final String LAYER_IN_USE_3 = "testLayerInUse3";
	private static final String LAYER_IN_USE_4 = "testLayerInUse4";
	
	@Test
	public void testLayerInUse() {
		try{
			LayerDto layer = new LayerDto();
			layer.setName("minen:" + LAYER_IN_USE_1);
			layer.setServer_resource("http://sig-minen-apps.emergya.es/geoserver/wms?");
			layer.setEnabled(Boolean.TRUE);
			layer.setType(layerTypeService.getLayerTypeById(LayerTypeService.WMS_VECTORIAL_ID));
			layer.setFolder((FolderDto) folderService.getById(new Long (1)));
			layer = (LayerDto) layerService.create(layer);
			LayerResourceDto layerResource = new LayerResourceDto();
			layerResource.setTmpLayerName(LAYER_IN_USE_2);
			layerResourceService.create(layerResource);
			LayerPublishRequestDto layerPublishRequestDto = new LayerPublishRequestDto();
			layerPublishRequestDto.setSourceLayerName(LAYER_IN_USE_3);
			layerPublishRequestService.create(layerPublishRequestDto);
			Assert.assertTrue(migrateSHP.layerInUse(LAYER_IN_USE_1));
			Assert.assertFalse(migrateSHP.layerInUse(LAYER_IN_USE_2));
			Assert.assertTrue(migrateSHP.layerInUse(LAYER_IN_USE_3));
			Assert.assertFalse(migrateSHP.layerInUse(LAYER_IN_USE_4));
		}catch (Exception e){
			LOG.error(e);
			Assert.fail();
		}
	}
}
