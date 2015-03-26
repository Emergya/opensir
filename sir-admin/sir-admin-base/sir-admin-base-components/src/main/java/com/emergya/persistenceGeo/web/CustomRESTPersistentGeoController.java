/*
 * CustomRESTPersistentGeoController.java
 *
 * Copyright (C) 2012
 *
 * This file is part of Proyecto ohiggins
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
 * Authors:: Antonio Hernández (mailto:ahernandez@emergya.com)
 */
package com.emergya.persistenceGeo.web;

import com.emergya.ohiggins.config.WorkspaceNamesConfig;
import java.io.Serializable;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.collections.ListUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.emergya.ohiggins.dto.LayerPublishRequestDto;
import com.emergya.ohiggins.dto.LayerResourceDto;
import com.emergya.ohiggins.service.InstitucionService;
import com.emergya.ohiggins.service.LayerPublishRequestService;
import com.emergya.ohiggins.service.LayerResourceService;
import com.emergya.ohiggins.service.LayerService;
import com.emergya.ohiggins.web.util.Utils;
import com.emergya.persistenceGeo.dto.FolderDto;
import com.emergya.persistenceGeo.dto.FolderTypeDto;
import com.emergya.persistenceGeo.dto.LayerDto;
import com.emergya.persistenceGeo.dto.TreeFolderDto;
import com.emergya.persistenceGeo.dto.TreeNode;
import com.emergya.persistenceGeo.dto.Treeable;
import com.emergya.persistenceGeo.dto.UserDto;
import com.emergya.persistenceGeo.dto.ZoneDto;
import com.emergya.persistenceGeo.service.FoldersAdminService;
import com.emergya.persistenceGeo.service.GeoserverService;
import com.emergya.persistenceGeo.service.GeoserverService.DuplicationResult;
import com.emergya.persistenceGeo.service.LayerAdminService;
import com.emergya.persistenceGeo.service.UserAdminService;
import com.emergya.persistenceGeo.service.ZoneAdminService;
import com.emergya.persistenceGeo.utils.GeoserverUtils;

/**
 * Tree service REST API
 * 
 * @author <a href="mailto:ahernandez@emergya.com">ahernandez</a>
 */
@Controller
public class CustomRESTPersistentGeoController implements Serializable {

	/** Serials */
	private static final long serialVersionUID = -3301002658344978661L;

	/** Log */
	private static final Log LOG = LogFactory
			.getLog(CustomRESTPersistentGeoController.class);

	private static final String SHOW_UNASSIGNED_FOLDER_FILTER= "SHOW_UNASSIGNED_FOLDER";
	

    @Resource
    private WorkspaceNamesConfig workspaceNamesConfig;	
	
	@Resource
	private InstitucionService institutionService;

	@Resource
	private ZoneAdminService zoneAdminService;

	@Resource
	private UserAdminService userAdminService;
	@Resource
	private FoldersAdminService folderAdminService;

	@Resource
	private LayerPublishRequestService layerPublishRequestService;
	
	@Resource
	private LayerService layerService;
	
	@Resource
	private LayerAdminService layerAdminService;

	@Resource
	private RestFoldersAdminController restFoldersAdminController;
	
	@Resource
	private LayerResourceService layerResourceService;
	
	@Resource
	private GeoserverService geoserverService;
	

	protected final String RESULTS = "results";
	protected final String ROOT = "data";
	protected final String SUCCESS = "success";

	// TODO: Those constants should be defined in a more concret implementation
	// of the tree service.
	private static final String NODE_TYPE_PRC = "prc";
	private static final String NODE_TYPE_PRI = "pri";
	private static final String NODE_TYPE_ZONE = "zone";
	private static final String NODE_TYPE_NOT_IPT[] = { "Carpeta", "Canal" };
	private static final String NODE_TYPE_FOLDER = FolderDto.class
			.getSimpleName();
	private static final String NODE_TYPE_CHANNELS_ROOT = "channelsRoot";
	private static final String NODE_TYPE_CHANNELS_BY_ZONE = "zonesRoot";

	/**
	 * Returns the node types
	 * 
	 * @param filter
	 *            The condition the returned nodes has to meet
	 * 
	 * @return JSON file with success
	 */
	@RequestMapping(value = "/persistenceGeo/getNodeTypes", produces = { MediaType.APPLICATION_JSON_VALUE })
	public @ResponseBody
	List<FolderTypeDto> getIPTNodeTypes() {

		Map<String, Object> result = new HashMap<String, Object>();
		List<FolderTypeDto> iptFolderTypes = new LinkedList<FolderTypeDto>();
		try {
			iptFolderTypes = folderAdminService
					.getIPTtFolderType(NODE_TYPE_NOT_IPT);
		} catch (Exception e) {
			LOG.error(e);
			result.put(SUCCESS, false);
		}
		return iptFolderTypes;
	}

	/**
	 * Returns the children of a specific container node. The container type is
	 * specified using the type parameter. The condition the returned nodes has
	 * to meet is specified using the filter parameter.
	 * 
	 * TODO: This should be a generic method that instantiate instances of more
	 * concret implementations.
	 * 
	 * @param node
	 *            The node id
	 * @param type
	 *            The type of the node (zone, folder)
	 * @param filter
	 *            The condition the returned nodes has to meet
	 * 
	 * @return JSON file with success
	 * 
	 * @Deprecated use {@link RestTreeFolderController#treeService(String, String, String)}
	 */
	@RequestMapping(value = "/persistenceGeo/treeService", produces = { MediaType.APPLICATION_JSON_VALUE })
	public @ResponseBody
	List<Treeable> treeService(
			@RequestParam(value = "node", required = false) String nodeId,
			@RequestParam(value = "type", required = false) String type,
			@RequestParam(value = "filter", required = false) String filter) {

		Map<String, Object> result = new HashMap<String, Object>();
		List<Treeable> nodes = new LinkedList<Treeable>();

		// TODO: The parameter filter could be passed by the client request
		// or can be setted in a more concret implementation of this class.
		// As an example, the string "M" doesn't need to be sent by the client
		// because it's already associated to the type PRC.

		try {

			if (NODE_TYPE_PRC.equals(type)) {

				String zoneId = null;
				List<ZoneDto> zones = zoneAdminService.findByType("M");

				for (ZoneDto zone : zones) {

					zoneId = String.valueOf(zone.getId());
					Map<String, Object> ret = restFoldersAdminController
							.loadFoldersByZone(zoneId, null);
					List<FolderDto> folders = (List<FolderDto>) ret.get(ROOT);

					for (FolderDto folder : folders) {
						nodes.add((Treeable) new TreeNode(folder, false,
								FolderDto.class.getSimpleName()));
					}
				}

			} else if (NODE_TYPE_PRI.equals(type)) {

				String zoneId = null;
				List<ZoneDto> zones = zoneAdminService.findByType("R");

				for (ZoneDto zone : zones) {

					zoneId = String.valueOf(zone.getId());
					Map<String, Object> ret = restFoldersAdminController
							.loadFoldersByZone(zoneId, null);
					List<FolderDto> folders = (List<FolderDto>) ret.get(ROOT);

					for (FolderDto folder : folders) {
						nodes.add((Treeable) new TreeNode(folder, false,
								FolderDto.class.getSimpleName()));
					}
				}

				zones = zoneAdminService.findByType("P");

				for (ZoneDto zone : zones) {

					zoneId = String.valueOf(zone.getId());
					Map<String, Object> ret = restFoldersAdminController
							.loadFoldersByZone(zoneId, null);
					List<FolderDto> folders = (List<FolderDto>) ret.get(ROOT);

					for (FolderDto folder : folders) {
						nodes.add((Treeable) new TreeNode(folder, false,
								FolderDto.class.getSimpleName()));
					}
				}

			} else if (NODE_TYPE_ZONE.equals(type)) {

				Map<String, Object> ret = restFoldersAdminController
						.loadFoldersByZone(nodeId, null);
				List<FolderDto> folders = (List<FolderDto>) ret.get(ROOT);
				for (FolderDto folder : folders) {
					nodes.add((Treeable) new TreeNode(folder, false,
							FolderDto.class.getSimpleName()));
				}

			} else if (NODE_TYPE_CHANNELS_BY_ZONE.equals(type)
					|| NODE_TYPE_CHANNELS_ROOT.equals(type)) {

				Map<String, Object> ret = restFoldersAdminController
						.loadChannels(filter);
				List<FolderDto> folders = (List<FolderDto>) ret.get(ROOT);
				for (FolderDto folder : folders) {
					nodes.add(new TreeNode(folder, false));
				}

			} else if (!StringUtils.isEmpty(type)
					&& StringUtils.isNumeric(type)) {
				// Nos traemos todos los tipos de nodos IPT
				List<FolderDto> folders = folderAdminService
						.findFoldersByType(Long.decode(type));
				if (!folders.isEmpty()) {
					for (FolderDto folder : folders) {
						nodes.add((Treeable) new TreeNode(folder, false));
					}
				}
			} else {

				// The rest of types are consider like folders

				Map<String, Object> ret = restFoldersAdminController
						.loadFoldersById(nodeId, filter);
				return (List<Treeable>) ret.get(ROOT);
			}

			result.put(SUCCESS, true);

		} catch (Exception e) {
			LOG.error(e);
			result.put(SUCCESS, false);
		}

		return nodes;
	}

	/**
	 * Returns the children of a specific container node. The container type is
	 * specified using the type parameter. The condition the returned nodes has
	 * to meet is specified using the fileter parameter.
	 * 
	 * TODO: This should be a generic method that instantiate instances of more
	 * concret implementations.
	 * 
	 * @param node
	 *            The node id
	 * @param type
	 *            The type of the node (zone, folder)
	 * @param filter
	 *            The condition the returned nodes has to meet
	 * 
	 * @return JSON file with success
	 * 
	 * @Deprecated use {@link RestTreeFolderController#treeServiceMap(String, String, String)} 
	 */
	@RequestMapping(value = "/persistenceGeo/treeServiceMap", produces = { MediaType.APPLICATION_JSON_VALUE })
	public @ResponseBody
	Map<String, Object> treeServiceMap(
			@RequestParam(value = "node", required = false) String nodeId,
			@RequestParam(value = "type", required = false) String type,
			@RequestParam(value = "filter", required = false) String filter) {

		Map<String, Object> result = new HashMap<String, Object>();
		List<Treeable> nodes = new LinkedList<Treeable>();

		// TODO: The parameter filter could be passed by the client request
		// or can be setted in a more concret implementation of this class.
		// As an example, the string "M" doesn't need to be sent by the client
		// because it's already associated to the type PRC.

		try {

			if (NODE_TYPE_PRC.equals(type)) {

				String zoneId = null;
				List<ZoneDto> zones = zoneAdminService.findByType("M");

				for (ZoneDto zone : zones) {

					zoneId = String.valueOf(zone.getId());
					Map<String, Object> ret = restFoldersAdminController
							.loadFoldersByZone(zoneId, null);
					List<FolderDto> folders = (List<FolderDto>) ret.get(ROOT);

					for (FolderDto folder : folders) {
						nodes.add((Treeable) new TreeNode(folder, false,
								FolderDto.class.getSimpleName()));
					}
				}

			} else if (NODE_TYPE_PRI.equals(type)) {

				String zoneId = null;
				List<ZoneDto> zones = zoneAdminService.findByType("R");

				for (ZoneDto zone : zones) {

					zoneId = String.valueOf(zone.getId());
					Map<String, Object> ret = restFoldersAdminController
							.loadFoldersByZone(zoneId, null);
					List<FolderDto> folders = (List<FolderDto>) ret.get(ROOT);

					for (FolderDto folder : folders) {
						nodes.add((Treeable) new TreeNode(folder, false,
								FolderDto.class.getSimpleName()));
					}
				}

				zones = zoneAdminService.findByType("P");

				for (ZoneDto zone : zones) {

					zoneId = String.valueOf(zone.getId());
					Map<String, Object> ret = restFoldersAdminController
							.loadFoldersByZone(zoneId, null);
					List<FolderDto> folders = (List<FolderDto>) ret.get(ROOT);

					for (FolderDto folder : folders) {
						nodes.add((Treeable) new TreeNode(folder, false,
								FolderDto.class.getSimpleName()));
					}
				}

			} else if (NODE_TYPE_ZONE.equals(type)) {

				return restFoldersAdminController.loadFoldersByZone(nodeId,
						null);

			} else if (NODE_TYPE_CHANNELS_BY_ZONE.equals(type)
					|| NODE_TYPE_CHANNELS_ROOT.equals(type)) {
				
				result =   restFoldersAdminController.loadChannels(filter);
				
				if(((Boolean) result.get(SUCCESS)) 
						&& filter!=null && filter.contains(SHOW_UNASSIGNED_FOLDER_FILTER)) {
					
					@SuppressWarnings("unchecked")
					List<TreeFolderDto> folders = (List<TreeFolderDto>) result.get(ROOT);
					
					FolderDto unassingedLayersFolder = new FolderDto();
					unassingedLayersFolder.setId(RestFoldersAdminController.UNASSIGNED_LAYERS_VIRTUAL_FOLDER_ID);
					unassingedLayersFolder.setName("Otros");
					
					folders.add(new TreeFolderDto(unassingedLayersFolder));
					Collections.sort(folders);
					
					result.put(RESULTS, folders.size());
				}
				
				return result;
			} else {

				// The rest of types are consider like folders

				return restFoldersAdminController.loadFoldersById(nodeId,
						filter);
			}

			result.put(SUCCESS, true);

		} catch (Exception e) {
			LOG.error(e);
			result.put(SUCCESS, false);
		}

		result.put(RESULTS, nodes != null ? nodes.size() : 0);
		result.put(ROOT, nodes != null ? nodes : ListUtils.EMPTY_LIST);

		return result;
	}

	/**
	 * Returns the children and descendents of a specific container node. The
	 * container type is specified using the type parameter. The condition the
	 * returned nodes has to meet is specified using the fileter parameter.
	 * 
	 * TODO: This should be a generic method that instantiate instances of more
	 * concret implementations.
	 * 
	 * @param node
	 *            The node id
	 * @param type
	 *            The type of the node (zone, folder)
	 * @param filter
	 *            The condition the returned nodes has to meet
	 * 
	 * @return JSON file with success
	 */
	@RequestMapping(value = "/persistenceGeo/layersByFolderId", produces = { MediaType.APPLICATION_JSON_VALUE })
	public @ResponseBody
	List<Treeable> layersByFolderId(
			@RequestParam(value = "node", required = false) String nodeId,
			@RequestParam(value = "type", required = false) String type,
			@RequestParam(value = "filter", required = false) String filter) {

		Map<String, Object> result = new HashMap<String, Object>();
		List<Treeable> nodes = new LinkedList<Treeable>();

		// TODO: The parameter filter could be passed by the client request
		// or can be setted in a more concret implementation of this class.
		// As an example, the string "M" doesn't need to be sent by the client
		// because it's already associated to the type PRC.

		try {

			if (NODE_TYPE_PRC.equals(type)) {

				List<ZoneDto> zones = zoneAdminService.findByType("M");
				for (ZoneDto zone : zones) {
					nodes.add((Treeable) new TreeNode(zone, false, "zone"));
				}

			} else if (NODE_TYPE_PRI.equals(type)) {

				List<ZoneDto> zones = zoneAdminService.findByType("R");
				for (ZoneDto zone : zones) {
					nodes.add((Treeable) new TreeNode(zone, false, "zone"));
				}

				zones = zoneAdminService.findByType("P");
				for (ZoneDto zone : zones) {
					nodes.add((Treeable) new TreeNode(zone, false, "zone"));
				}

			} else if (NODE_TYPE_ZONE.equals(type)) {

				Map<String, Object> ret = restFoldersAdminController
						.loadFoldersByZone(nodeId, null);
				List<FolderDto> folders = (List<FolderDto>) ret.get(ROOT);
				for (FolderDto folder : folders) {
					nodes.add((Treeable) new TreeNode(folder, false,
							FolderDto.class.getSimpleName()));
				}

			} else if (NODE_TYPE_CHANNELS_BY_ZONE.equals(type)
					|| NODE_TYPE_CHANNELS_ROOT.equals(type)) {

				Map<String, Object> ret = restFoldersAdminController
						.loadChannels(filter);
				List<FolderDto> folders = (List<FolderDto>) ret.get(ROOT);
				for (FolderDto folder : folders) {
					nodes.add(new TreeNode(folder, false));
				}

			} else if (NODE_TYPE_FOLDER.equals(type)) {

				// The rest of types are consider like folders

				Map<String, Object> ret = restFoldersAdminController
						.loadFoldersById(nodeId, filter);
				return (List<Treeable>) ret.get(ROOT);
			}

			result.put(SUCCESS, true);

		} catch (Exception e) {
			LOG.error(e);
			result.put(SUCCESS, false);
		}

		return nodes;
	}

	@RequestMapping(value = "/persistenceGeo/loadPendingLayerRequestsLayers/{userId}", method = RequestMethod.GET, produces = { MediaType.APPLICATION_JSON_VALUE })
	public @ResponseBody
	Map<String, Object> loadPendingLayerRequests(@PathVariable Long userId) {
		Map<String, Object> result = new HashMap<String, Object>();
		List<LayerDto> layers = null;
		try {

			// TODO: Secure call!
			// String loggedUserName = ((String)
			// SecurityContextHolder.getContext()
			// .getAuthentication().getPrincipal());

			// UserDto loggedUser =
			// userAdminService.obtenerUsuario(loggedUserName);
			// if(loggedUser == null || !loggedUser.getAdmin() ||
			// !loggedUser.getId().equals(userId)) {
			// // If no logged user, or if he is no admin or he isn't the
			// requesting user...
			// result.put(SUCCESS, false);
			// return result;
			// }

			UserDto userDto = (UserDto) userAdminService.getById(userId);
			if (userDto == null || !userDto.getAdmin()) {
				result.put(SUCCESS, false);
				return result;
			}

			List<LayerPublishRequestDto> publicationRequests = layerPublishRequestService
					.getPendingRequests();

			layers = new LinkedList<LayerDto>();
			if (!publicationRequests.isEmpty()) {
				// We convert the publish requests to layers so we output the
				// same format in all requests.
				for (LayerPublishRequestDto requestDto : publicationRequests) {
					layers.add(requestDto.toPGLayerDto());
				}
			}

			result.put(SUCCESS, true);
		} catch (Exception e) {
			result.put(SUCCESS, false);
			return result;
		}

		result.put(RESULTS, layers.size());
		result.put(ROOT, layers);

		return result;
	}
	
	
	/**
	 * This method saves a layer resource as new layer for a group.
	 * 
	 * @param idGroup the group the new layer will belong to.
	 * @param idLayerResource the id of the layer resource which will be converted to a layer.
     * @param layerTitle
     * @param serverResource
     * @return 
	 */
	@RequestMapping(value = "/persistenceGeo/saveLayerResourceByGroup/{idGroup}/{idLayerResource}", method = RequestMethod.POST)
	public @ResponseBody 
	Map<String, Object>  saveLayerResourceByGroup(
			@PathVariable Long idGroup,
			@PathVariable Long idLayerResource,
			@RequestParam(value = "name", required = true) String layerTitle,
			@RequestParam(value = "server_resource", required = true) String serverResource) {
		
		Map<String, Object> result = new HashMap<String, Object>();
		result.put(SUCCESS, false);
		
		com.emergya.ohiggins.dto.AuthorityDto authDto = 
				(com.emergya.ohiggins.dto.AuthorityDto) institutionService.getById(idGroup);
		if(authDto==null) {
			return result;
		}
		
		String targetWorkspace = authDto.getWorkspaceName();
		
		// We create a sanitized unique name for persisted layers.
		String serverLayerName;
		do{
		   serverLayerName = GeoserverUtils.createUniqueName(layerTitle);
		} while(geoserverService.existsLayerInWorkspace(serverLayerName, targetWorkspace));

		LayerResourceDto layerResourceDto = 
				(LayerResourceDto) layerResourceService.getById(idLayerResource);
		
		if(layerResourceDto==null) {
			return result;
		}	
		
		
		com.emergya.ohiggins.dto.LayerDto newLayer = 
				new com.emergya.ohiggins.dto.LayerDto();
		
		newLayer.setName(String.format("%s:%s", targetWorkspace, serverLayerName));
		
		newLayer.setAuthority(authDto);
		
		newLayer.setEnabled(true);
		newLayer.setIsChannel(false);
		newLayer.setCreateDate(new Date());
		newLayer.setLayerTitle(layerTitle);
		newLayer.setServer_resource(serverResource);
		newLayer.setType(layerResourceDto.getLayerType());		
		
		
		DuplicationResult duplicationResult = geoserverService.duplicateGeoServerLayer(
            workspaceNamesConfig.getTmpWorkspace(),		    
		    layerResourceDto.getLayerType().getNameAndTipo(),
		    layerResourceDto.getTmpLayerName(), 
		    layerResourceDto.getTmpLayerName(), 
		    targetWorkspace, serverLayerName, layerTitle);
		if(duplicationResult == DuplicationResult.FAILURE) {
		    return result;
		} else if(duplicationResult== DuplicationResult.SUCCESS_VECTORIAL) {
		    newLayer.setTableName(serverLayerName);
		}		
	
		try {
			newLayer = (com.emergya.ohiggins.dto.LayerDto) layerService.create(newLayer);
		} catch(RuntimeException e) {
			LOG.error("Error persisting layer", e);
			return result;
		}
		
		// We load the save layer as PersistenceGeo LayerDto to return the same
		// types as the rest of methods and avoid conversion problems.
		LayerDto layerDto = (LayerDto) layerAdminService.getById(newLayer.getId());
		
		result.put(SUCCESS, true);
		result.put(RESULTS, 1);
		result.put(ROOT, layerDto);
		
		return result;		
	}
	
	

}
