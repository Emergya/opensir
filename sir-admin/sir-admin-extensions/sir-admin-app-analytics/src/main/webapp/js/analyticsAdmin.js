
var analyticsAdmin = {
	acceptRequest: function(url) {
		if(confirm("Se publicarará el dato analítico estadístico. ¿Desea continuar?")){
	        window.location=url;
	    }
	},
	viewDetails : function(name,url) {
        loadContentDialog("viewDetailsPopup", name, url);
    },
    toggleSearchForm: function() {
        $('#searchFormCtr').toggleClass('hidden', 300);
    }
};