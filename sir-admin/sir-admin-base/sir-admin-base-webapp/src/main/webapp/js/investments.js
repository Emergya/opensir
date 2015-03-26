/* Javascript functionality for the investments page*/

var investments = {
	submitForm : function() {
		var clickedButton = this;
		
		var updatedFile = $('#'+clickedButton.value);
		
		// We check if the user selected a file.
		if(!updatedFile.val()) {
			if(!updatedFile.parent().find(".help-inline").length){
				updatedFile.parent().append('<div class="help-inline campoError span-9">Seleccione el fichero a actualizar, por favor.</div>');
			}						
			return;
		}
		
		$('#uploadForm input[type=file]').each(function(){
			if(this.id!=clickedButton.value) {
				// We clean the file inputs whose update button wasn't clicked.
				this.value=null;
			}
		})
		
		$('#uploadForm')[0].submit();
	},
	
	init : function() {
		$('#uploadForm input[type=file]').change(this.updateFileField);
	    $('#uploadForm input[type=file]').mouseout(this.updateFileField);
	    
	    $('#uploadForm button').click(this.submitForm);   
	}
}


$().ready(function () {
   investments.init();
});