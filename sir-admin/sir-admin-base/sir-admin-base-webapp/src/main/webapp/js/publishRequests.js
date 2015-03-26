var publishRequests = {
	
	showMetadata: function(url, layerName) {
 		loadContentDialog("metadataDialog", "Medatos de la solicitud «"+layerName+"»", url);
 	},

	publishLayer: function (url){
	   loadContentDialog("acceptDialog", "Publicación", url,375);
	},
	
	denyPublication : function(url) {
		loadContentDialog("denyDialog", "Denegación de publicación", url,442);
	},
	
	toggleAction: function (newAction, loadMetadata) {
		var showNewRow = newAction=="PUBLISH_NEW";
		document.getElementById("desiredNameRow").style.display = showNewRow?"block":"none";
		document.getElementById("updatedLayerRow").style.display = showNewRow?"none":"block";	
		
		if(showNewRow) {
			document.getElementById('updatedLayerId').value=null;
			// So chosen realizes we have manually changed the option.
			$("#capaActualizar").trigger("liszt:updated")
		}
		
		// We clean the metadata when changing actions
		var load = true;
		if(typeof("loadMetadata")!="undefined") {
			load = loadMetadata;
		}
		if(load){
			this.loadMetadataOfUpdatableLayer(0);
		}
		
	},
	
	loadMetadataOfUpdatableLayer: function(layerId) {
		// Sending 0 as the id will return an empty metadata object,
		// used to clean the metadata fields of the form.
		if(!layerId) {
			layerId = 0;
		}
		var url = urls.loadMetadata+"/"+layerId;

		$.get(url, {},function(metadata){
			
			for(var field in metadata) {
				var input = document.getElementById("metadata."+field);
				
				if(input) {
					input.value=metadata[field];
				}	
			}
			
		},"json");
	}

};