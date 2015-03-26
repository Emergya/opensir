/* OhigginsFolderAdminServiceImplTest.java
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
 * Authors:: Alejandro Díaz Torres (mailto:adiaz@emergya.com)
 */
package com.emergya.ohiggins.service.impl;

import static com.emergya.ohiggins.utils.StaticFolders.EDUCACION;
import static com.emergya.ohiggins.utils.StaticFolders.MACHALI;
import static com.emergya.ohiggins.utils.StaticFolders.TEST_CHANNEL_CHILDREN;
import static com.emergya.ohiggins.utils.StaticFolders.TEST_CHANNEL_DISABLED_FOLDERS;
import static com.emergya.ohiggins.utils.StaticFolders.TEST_CHANNEL_ROOT_FOLDERS;
import static com.emergya.ohiggins.utils.StaticFolders.TEST_CHANNEL_ZONE_FOLDERS;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.annotation.Resource;

import org.apache.commons.collections.SetUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Assert;
import org.junit.Test;

import com.emergya.ohiggins.ApplicationContextAwareTest;
import com.emergya.persistenceGeo.dto.FolderDto;
import com.emergya.persistenceGeo.dto.LayerDto;
import com.emergya.persistenceGeo.service.FoldersAdminService;
import com.emergya.persistenceGeo.service.LayerAdminService;

/**
 * Test que comprueba si los datos se han inicializado correctamente en
 * dataset.xml
 * 
 * @author <a href="mailto:adiaz@emergya.com">adiaz</a>
 * 
 */
public class OhigginsFolderAdminServiceImplTest extends
		ApplicationContextAwareTest {

	private static final Log LOG = LogFactory
			.getLog(OhigginsFolderAdminServiceImplTest.class);

	@Resource
	private FoldersAdminService foldersAdminService;

	@Resource
	private LayerAdminService layerAdminService;

	/**
	 * Comprueba que al menos se cargan correctamente los canales de prueba
	 * definidos en el dataset: 'Agricultura', 'Censo', 'Educación', 'Medio
	 * Ambiente' y 'Salud'
	 */
	@Test
	public void testGetChannelFolders() {
		try {
			List<FolderDto> channelFolders = foldersAdminService
					.getChannelFolders(Boolean.FALSE, null, Boolean.TRUE);
			Assert.assertNotNull(channelFolders);
			Set<Long> notIn = new HashSet<Long>();
			notIn.addAll(TEST_CHANNEL_DISABLED_FOLDERS);
			notIn.addAll(TEST_CHANNEL_ZONE_FOLDERS);
			for (Set<Long> children : TEST_CHANNEL_CHILDREN.values()) {
				notIn.addAll(children);
			}
			checkFoldersIn(channelFolders, TEST_CHANNEL_ROOT_FOLDERS, notIn);
		} catch (Exception e) {
			LOG.error(e);
			Assert.fail();
		}
	}

	/**
	 * Comprueba que al menos se cargan correctamente los canales de prueba
	 * definidos en el dataset: 'Machalí' y 'Rancagua'
	 */
	@Test
	public void testGetChannelsInZoneFolders() {
		try {
			List<FolderDto> channelFolders = foldersAdminService
					.getChannelFolders(Boolean.TRUE, null, Boolean.TRUE);
			Assert.assertNotNull(channelFolders);
			Set<Long> notIn = new HashSet<Long>();
			notIn.addAll(TEST_CHANNEL_DISABLED_FOLDERS);
			notIn.addAll(TEST_CHANNEL_ROOT_FOLDERS);
			for (Set<Long> children : TEST_CHANNEL_CHILDREN.values()) {
				notIn.addAll(children);
			}
			checkFoldersIn(channelFolders, TEST_CHANNEL_ZONE_FOLDERS, notIn);
		} catch (Exception e) {
			LOG.error(e);
			Assert.fail();
		}
	}

	/**
	 * Comprueba que se recuperan correctamente los hijos de 'Educación'
	 */
	@Test
	public void testGetChannelChildren() {
		try {
			List<FolderDto> channelFolders = foldersAdminService.findByZone(
					null, EDUCACION, Boolean.TRUE);
			Assert.assertNotNull(channelFolders);
			Set<Long> notIn = new HashSet<Long>();
			notIn.addAll(TEST_CHANNEL_DISABLED_FOLDERS);
			notIn.addAll(TEST_CHANNEL_ROOT_FOLDERS);
			notIn.addAll(TEST_CHANNEL_ZONE_FOLDERS);
			checkFoldersIn(channelFolders,
					TEST_CHANNEL_CHILDREN.get(EDUCACION), notIn);
		} catch (Exception e) {
			LOG.error(e);
			Assert.fail();
		}
	}

	/**
	 * Comprueba que se recuperan correctamente los hijos de 'Educación'
	 */
	@Test
	public void testGetChannel() {
		try {
			List<LayerDto> channel = layerAdminService.getLayersByFolder(
					MACHALI, Boolean.TRUE, Boolean.TRUE);
			Assert.assertNotNull(channel);
			Assert.assertTrue(channel.size() >= 2);
			List<LayerDto> channel2 = layerAdminService.getLayersByFolder(
					MACHALI, Boolean.FALSE, Boolean.TRUE);
			Assert.assertNotNull(channel2);
			Assert.assertTrue(channel2.size() >= 1);
			for (LayerDto layer : channel) {
				Long layerChannelId = layer.getId();
				for (LayerDto layer2 : channel2) {
					Long layerNotChannelId = layer2.getId();
					if (layerChannelId.equals(layerNotChannelId)) {
						Assert.fail("The layer '" + layerChannelId + ":"
								+ layer.getName()
								+ "'  is channel and not channel layer");
					}
				}
			}
		} catch (Exception e) {
			LOG.error(e);
			Assert.fail();
		}
	}

	/**
	 * Check if a folder list contains folders identified in idsIn an not
	 * contains folders in idsNotIn
	 * 
	 * @param folders
	 * @param idsIn
	 * @param idsNotIn
	 */
	@SuppressWarnings("unchecked")
	public void checkFoldersIn(List<FolderDto> folders, Set<Long> idsIn,
			Set<Long> idsNotIn) {
		// init if null
		if (idsIn == null) {
			idsIn = SetUtils.EMPTY_SET;
		}
		if (idsNotIn == null) {
			idsNotIn = SetUtils.EMPTY_SET;
		}
		// check
		Assert.assertNotNull(folders);
		Assert.assertTrue(folders.size() >= idsIn.size());
		Set<Long> foundedIds = new HashSet<Long>();
		if (idsIn.size() > 0 || idsNotIn.size() > 0) {
			for (FolderDto folder : folders) {
				Long folderId = folder.getId();
				if (folderId != null) {
					if (idsIn.contains(folderId)) {
						if (!foundedIds.contains(folderId)) {
							foundedIds.add(folderId);
						}
					}
					if (idsNotIn.contains(folderId)) {
						Assert.fail("Folder " + folderId
								+ " must'nt be in folder list");
					}
				}
			}
			int diff = idsIn.size() - foundedIds.size();
			if (diff != 0) {
				String idsNotFound = new String();
				for (Long id : idsIn) {
					if (!foundedIds.contains(id)) {
						idsNotFound += id;
						diff--;
						if (diff > 0) {
							idsNotFound += ", ";
						}
					}
				}
				Assert.fail("Folders [" + idsNotFound
						+ "] not found in folder list");
			}
		}
	}

}
