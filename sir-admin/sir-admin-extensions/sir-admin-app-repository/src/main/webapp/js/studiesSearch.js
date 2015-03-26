var studiesSearch = {
		doSearch : function(region) {
			
			// The region name is put in the region field.
			$("#studiesSearch_area")[0].value=region;
			
			// Then we submit the form.
			$("#studiesSearch_form")[0].submit();
		},
		
		viewDetails : function(detailsUrl) {			
			loadContentDialog("studies_detailsView","Detalles del documento", detailsUrl);
		},
		
		deleteStudy : function(removeURL) {
			if (confirm("¿Seguro que desea eliminar el estudio?")) {
				window.location= removeURL;
			}
		}
}; 