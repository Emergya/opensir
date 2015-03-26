package com.emergya.ohiggins.usedspace.service.impl;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;


import com.emergya.ohiggins.dto.AuthorityDto;
import com.emergya.ohiggins.dto.LayerDto;
import com.emergya.ohiggins.service.InstitucionService;
import com.emergya.ohiggins.service.LayerService;
import com.emergya.ohiggins.usedspace.dto.UsedSpaceDto;
import com.emergya.ohiggins.usedspace.service.UsedSpaceService;
import com.emergya.persistenceGeo.service.DBManagementService;
import com.emergya.persistenceGeo.service.GeoserverService;


@Service
public class UsedSpaceServiceImpl implements UsedSpaceService{


	
	@Resource
	private LayerService layerService;
	@Resource
	private InstitucionService institucionService;
	@Resource
	private DBManagementService dbManagementService;
	
	//GeoServer
	@Value("#{webProperties['geoserver.data.basePath']}")
	private String GEOSERVER_BASE_PATH;
	
	@Resource
	private GeoserverService geoserverService;
	@Resource
	private String namespaceBaseUrl;
	

	@Override
	public List<UsedSpaceDto> getLayersSpaceByAuthority(long authId) {
		
		
		List<LayerDto> authorityLayers = layerService.getFromToOrderBy(0, Integer.MAX_VALUE, "name", authId);
		List<UsedSpaceDto> spaceLayers = new ArrayList<UsedSpaceDto>();
		
		//GeoServer
		AuthorityDto auth = (AuthorityDto) this.institucionService.getById(authId);
		
		
		for (LayerDto layer: authorityLayers){
			
			if (layer!=null && layer.getIsChannel()!=null && layer.getIsChannel() == false) {
				UsedSpaceDto tmp = this.createSpaceDto(auth,layer);
				spaceLayers.add(tmp);
			}
			
		}
		
		return spaceLayers;
	}


	private UsedSpaceDto createSpaceDto(AuthorityDto auth, LayerDto layer) {

		UsedSpaceDto tmp = new UsedSpaceDto();
		
		//NAME
		String table_name = layer.getTableName();
		tmp.setLayerName(layer.getLayerLabel());
		
		//TYPE
		tmp.setLayerType(layer.getType().getNameAndTipo());

		//SPACE USED
		tmp.setLayerSpace("");
		if (layer.getType().getTipo().equals("Raster")) {
			
			//String name = GeoserverUtils.createName(layer.getLayerTitle());
			String name = layer.getNameWithoutWorkspace();
			if (name!=null){
				String filePath = GEOSERVER_BASE_PATH + "/workspaces/"
						+ auth.getWorkspaceName() + "/" + name;
				
				File directory = new File(filePath);
				
				if (directory!=null && directory.exists()){
			
					long layerSize = FileUtils.sizeOfDirectory(directory);
					tmp.setLayerSpace(this.getFormattedSize(layerSize));
				}
			}
			
			
		} else {//Vectorial. Layer should have a table name.
			if (table_name != null){
				long size = dbManagementService.getTableSize(table_name.toLowerCase());
				if (size<0)
					tmp.setLayerSpace("-");
				else
					tmp.setLayerSpace(this.getFormattedSize(size));
			/*}else{
				long size = dbManagementService.getTableSize(layer.getNameWithoutWorkspace().toLowerCase());
				tmp.setLayerSpace(this.getFormattedSize(size));*/
			}
			
		}
		
		return tmp;
	}


	private String getFormattedSize(long layerSize) {
		String result = "";
		if (layerSize>=(1024*1024))
			result = ((layerSize/1024)/1024+" MB");
		else if (layerSize>=1024)
			result = (layerSize/1024 + " KB");
		else
			result = (layerSize + " bytes");		
		
		return result;
	}
	
}
