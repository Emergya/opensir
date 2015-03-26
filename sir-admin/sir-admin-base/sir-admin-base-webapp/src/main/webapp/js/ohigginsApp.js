// Aquí se irán metiendo los paths generados con spring:url para su uso en
// funciones javascript para que no haya necesidad de empotrar las funciones
// en los jsp.
window["urls"] = {

};

function documentReady () {
    
    jQuery(function($){
        $.datepicker.regional['es'] = {
           closeText: 'Cerrar',
           prevText: '<Ant',
           nextText: 'Sig>',
           currentText: 'Hoy',
           monthNames: ['Enero', 'Febrero', 'Marzo', 'Abril', 'Mayo', 'Junio', 'Julio', 'Agosto', 'Septiembre', 'Octubre', 'Noviembre', 'Diciembre'],
           monthNamesShort: ['Ene','Feb','Mar','Abr', 'May','Jun','Jul','Ago','Sep', 'Oct','Nov','Dic'],
           dayNames: ['Domingo', 'Lunes', 'Martes', 'Miércoles', 'Jueves', 'Viernes', 'Sábado'],
           dayNamesShort: ['Dom','Lun','Mar','Mié','Juv','Vie','Sáb'],
           dayNamesMin: ['Do','Lu','Ma','Mi','Ju','Vi','Sá'],
           weekHeader: 'Sm',
           dateFormat: 'dd/mm/yy',
           firstDay: 1,
           isRTL: false,
           showMonthAfterYear: false,
           yearSuffix: ''};
        $.datepicker.setDefaults($.datepicker.regional['es']);
     });

   

    // We set the dialog's default close button text so it
    // appears in spanish.
    jQuery.ui.dialog.prototype.options.closeText = "Cerrar";

    // Validación alfanumérica custom
    if (jQuery.validator) {
        jQuery.validator.addMethod("alphanumeric", function (value, element) {

            return this.optional(element)
                    || /(?!^[0-9]*$)(?!^[a-zA-Z]*$)^([a-zA-Z0-9]{3,60})$/i
                            .test(value);
        }, "Debe contener números y letras y tener una longitud mínima de 3.");
    }

    // Added an ajax load indicator so people don't dispair.
    $(document).ajaxSend(function () {
        $("#loadingDiv").fadeIn();
    });

    $(document).ajaxComplete(function () {
        $("#loadingDiv").fadeOut();
    });

    addJQueryImprovements();
}

function addJQueryImprovements () {
    if (jQuery.fn.chosen) {

        $("select").chosen({
            allow_single_deselect : true,
            no_results_text: "Sin resultados para",
            placeholder_text: "Click para seleccionar",
            search_contains: true
        });
    }

    if ($.fn.datepicker) {

        // Datepickers de jQuery en toda la app.
        jQuery("input.datepicker").datepicker({
            changeMonth : true,
            changeYear : true,
            // showButtonPanel: true,
            showOn : "both",
            buttonImage : urls.images + "/calendar.gif",
            buttonImageOnly : true,
            buttonText : "Click para elegir fecha",
            buttonTooltip : "Click para elegir fecha"
        });
    }

    if ($.fn.tooltip) {
        $(document).tooltip({
            track : true
        });
    }
    
    if($.fn.tableScroll) {
    	$("table.tableScroll").tableScroll();
    }
    
    if($.fn.fileinput) {
    	var fakeInputTpl = '<table><tr><td><input readonly="readonly" id="${fileInputId}Input" class="campo" type="text" /></td><td style="width:1px"> <button class="button" type="button">Examinar</button></td></tr></table>';
    	
    	$('input[type=file]').change(this.updateFileField);
        $('input[type=file]').mouseout(this.updateFileField);
        
        // We use the fileinput jquery plugin to create an styled file input instead
	    // of doing it manually in the jsp file.
	    $('input[type=file]').each(function(idx, fileInput){
	    	if($(this).closest(".fileinput-wrapper").length) {
	    		// The file input was already processed
	    		return;
	    	}
	    	var fileInputId = this.id;
	    	
	    	var fakeInput = fakeInputTpl
	    		.replace("${fileInputId}", fileInputId);
	    	
	    	$(this).fileinput(fakeInput);	    	
	    });
    }
    
    if($.fn.placeholder) {
    	$("input[type=text], input[type=password],textarea").placeholder();
    }

    applyRichEditor();
    
    
    $("form").submit(function (e) {
        if (!validateForm(this)) {
            return false;
        }

        $("#loadingDiv").fadeIn();
        return true;
    });

}

function applyRichEditor() {
	if(!$.fn.ckeditor) {
		return;
	}

	var config = {
		toolbar : [
				{
					name : 'document',
					items : [ 'Source' ]
				},
				{
					name : 'clipboard',
					items : [ 'Cut', 'Copy', 'Paste', 'PasteText',
							'PasteFromWord', '-', 'Undo', 'Redo' ]
				},
				{
					name : 'editing',
					items : [ 'Find', 'Replace', '-', 'SelectAll' ]
				},
				{
					name : 'basicstyles',
					items : [ 'Bold', 'Italic', 'Underline', 'Strike',
							'Subscript', 'Superscript', '-', 'RemoveFormat' ]
				},
				{
					name : 'paragraph',
					items : [ 'NumberedList', 'BulletedList', '',
							'Outdent', 'Indent', '', 'JustifyLeft',
							'JustifyCenter', 'JustifyRight', 'JustifyBlock' ]
				}, '/', {
					name : 'styles',
					items : [ 'Styles', 'Format', 'Font', 'FontSize' ]
				}, {
					name : 'colors',
					items : [ 'TextColor', 'BGColor' ]
				}, {
					name : 'links',
					items : [ 'Link', 'Unlink', 'Anchor' ]
				} ]
	};

	//Destroy the initilized editor
	if (CKEDITOR.instances.descripcion != undefined) {
		CKEDITOR.instances.descripcion.destroy(true);
	}

	CKEDITOR.config.removePlugins = 'elementspath';

	CKEDITOR.on('instanceCreated', function(event) {
		var editor = event.editor;
		editor.on('instanceReady', function(e) {
			$('iframe').attr("title", '');
			$('tr').attr("title", '');
		});
	});

	// Initialize the editor.
	// Callback function can be passed and executed after full instance creation.
	$('.jquery_ckeditor').ckeditor(config);

}

/* Updates the fake file input with the selected file */
function updateFileField() {
    var id = this.id;
    $('#' + id + "Input").val($(this).val());
}

$(document).ready(documentReady);

function validateForm (form) {
    if ($.type(form) === "string") {
        form = "#" + form;
    }

    return $(form).validate({
        errorClass : "campoError",
        validClass : "campo",
        errorPlacement : function (error, element) {
            $('#help-inline-' + element[0].id).html(error);
        }
    }).form();
}

/**
 * Funcion para AJAX.
 */
function cargaXMLHTTPR () {
    peticion = false;
    try {
        peticion = new XMLHttpRequest();
    } catch (trymicrosoft) {
        try {
            peticion = new ActiveXObject("Msxml2.XMLHTTP");
        } catch (othermicrosoft) {
            try {
                peticion = new ActiveXObject("Microsoft.XMLHTTP");
            } catch (failed) {
                peticion = false;
            }
        }
    }

    if (!peticion)
        alert("ERROR AL INICIALIZAR XMLHTTPREQUEST");
    return peticion;
}

/**
 * Muestra un mensaje de confirmacion
 * 
 * @param mensaje
 *            a mostrar para confimar
 */
function mostrarMensajeConfirmacion (mensaje) {
    return confirm(mensaje);
}

/**
 * Muestra un mensaje
 * 
 * @param mensaje
 * @param mensaje
 *            a mostrar para confimar
 */
function mostrarMensaje (mensaje) {
    alert(mensaje);
}

/**
 * Muestra u oculta capa por su id
 */
function showHide (name) {

    var stackElement = document.getElementById(name);

    if (stackElement.style.display != 'none') {
        stackElement.style.display = 'none';
    } else {
        stackElement.style.display = 'block';
    }
}

/**
 * Muestra capa por su id
 */
function showc (name) {
    var stackElement = document.getElementById(name);
    if (stackElement != null) {
        stackElement.style.display = 'block';
    }
}

/**
 * Oculta capa por su id
 */
function hidec (name) {
    var stackElement = document.getElementById(name);
    if (stackElement != null) {
        stackElement.style.display = 'none';
    }
}


function jq (myid) {
    // return '#' + myid.replace(/(:|\.)/g,'\\$1');
    var a = myid.replace(/(:|\.)/g, '\\$1');
    return a;
}

function generateImgAjax () {
    var img = '<img class="ajaxLoader" src="' + urls.images
            + '/ajaxloader.gif"' + "/>";
    return img;
}

function ajaxLoad (url, htmlElement) {
    var params = url;
    var img = generateImgAjax();
    $.ajax({
        type : "post",
        url : url,
        data : params,
        beforeSend : function (respuesta) {
            $('#' + htmlElement).html(img);
        },
        success : function (respuesta) {
            $('#' + htmlElement).html(respuesta);
            addJQueryImprovements();
        }
    });
}

function loadContentDialog (dialogTargetId, popupTitle, contentURL, width) {
    // We help a bit the ajaxSend and ajaxComplete methods
    // because it doesn't seem to work always when showing a popup after the
    // first time.
    $("#loadingDiv").fadeIn();
    var popWidth = 450;
    if (typeof (width) != "undefined") {
        popWidth = width;
    }
    $('#' + dialogTargetId).load(contentURL + "?r=" + Math.random(),
            function () {
                var dialogElement = $(this);
                $("#loadingDiv").fadeOut();
                // We add datepickers and other stuff that came with the loaded
                // content.
                addJQueryImprovements();
                dialogElement.dialog({
                    width : popWidth,
                    resizable : false,
                    modal : true,
                    title : popupTitle,
                    show : {
                        effect : "fade",
                        duration : 200
                    },
                    hide : {
                        effect : "fade",
                        duration : 200
                    }
                });
            });
}

/**
 * Inserta imagen de precarga en contenido html
 * 
 * @param id
 * @param urlImages
 *            Url base donde se encuentra la imagen
 */
function insertarImgAjax (id) {
    if (id != null) {
        var elemId = document.getElementById(id);
        if (elemId != null) {
            elemId.innerHTML = generateImgAjax();
        }
    }
}