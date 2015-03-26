// Funciones del selector de capas para carpetas.


/**
 * Llama al servicio web que controla que la capa esté habilitada o no.
 * @param isAssigned Si la capa clickada está en la lista de capas de la carpeta, o no.
 * @param layerId El id de la capa.
 */
function toggleLayerEnabled(isAssigned, layerId, checked) {
	var url = urls.toggleLayerEnabled + "/" + isAssigned + "/" + layerId;
	if(isAssigned) {
		var checkbox = $('#isChannel-'+layerId)[0];
		
		checkbox.disabled = !checked || !isInChannel;
		if(checkbox.disabled) {
			checkbox.checked=false;
		}
	}
	$.ajax({
		type : "POST",
		url : url,
		data : url,
		success : function(response) {
			// alert("Success is enabled")
		},
		error : function(error) {
			alert("Ocurrió un error.");
		}
	});
}

/**
 * Llama al servicio web que controla si una capa de la carpeta es canal o no.
 * @param layerId El id de la capa.
 */
function toggleLayerIsChannel(layerId) {
	var url = urls.toggleLayerIsChannel + "/" + layerId;
	$.ajax({
		type : "POST",
		url : url,
		data : url,
		success : function(response) {
			// alert("Success is channel")
		},
		error : function(error) {
			alert("Ocurrió un error.");
		}
	});
}

/**
 * Llama al servicio web que añade una capa que estaba desasignada a la carpeta.
 * Elimina la fila correspondiente de la tabla de desasignadas y añade la fila
 * devuelta por el servicio web a las asignadas.
 * @param layerId El id de la capa añadida.
 * @param clickedLink El enlace usado para realizar la acción, permite determinar la fila
 * para su eliminación.
 */
function addLayerToFolder(layerId, clickedLink) {
	var url = urls.addLayerToFolder + "/" + layerId;
	$.ajax({
		type : "POST",
		url : url,
		data : url,
		success : function(response) {
			if (response == "MAX_FOLDERS_REACHED") {
				alert("No puede haber más de 5 capas en el canal/carpeta.");
				return;
			}

			removeLayerRow(clickedLink);
			addLayerRow("fLayer",response);

		},
		error : function(error) {
			console.debug(error);
			alert("Ocurrió un error");
		}
	});
}

/**
 * Añade una fila a una tabla.
 * @param tableId
 * @param rowContent
 */
function addLayerRow(tableId, rowContent) {
	var tableSelector ="#"+tableId+ " tbody";
	// Solo tenemos una fila. Podría ser la fila que indica que la tabla está vacía.
	var row = $(rowContent)[0];
	if($(tableSelector + " tr").length==1 
			&& $(tableSelector + " td").length==1) {
		
		// Borramos el contenido de la tabla (la fila que indica que la tabla está vacía).
		$(tableSelector + " tr").fadeOut({
			complete : function() {
				$(tableSelector).html("");
				$(tableSelector).append(row);
				row.scrollIntoView();
			}
		});		
	} else {
		$(tableSelector).append(row);
		row.scrollIntoView();
	}
	
	
}

/**
 * Llama al servicio web que quita una capa que estaba asignada a una carpeta.
 * Elimina la fila correspondiente de la tabla de asignadas y añade la fila
 * devuelta por el servicio web a las desaasignadas.
 * @param layerId El id de la capa quitada.
 * @param clickedLink El enlace usado para realizar la acción, permite determinar la fila
 * para su eliminación.
 */
function removeLayerFromFolder(layerId, clickedLink) {
	var url = urls.removeLayerFromFolder + "/" + layerId;
	$.ajax({
		type : "POST",
		url : url,
		data : url,
		success : function(response, checkbox) {
			removeLayerRow(clickedLink);
			addLayerRow("uLayer", response);
		},
		error : function(error) {
			console.debug(error);
			alert("Ocurrió un error.");
		}
	});
}

/**
 * Elminina una fila de una tabla dado un elemento contenido (directamente) en una de sus celdas.
 * @param tdContentElement
 * @returns
 */
function removeLayerRow(tdContentElement) {
	var row = $(tdContentElement).parents("tr")[0];
	$(row).fadeOut({
		complete : function() {
			var table = $(row).parents("tbody")[0];
			var colspan = row.children.length;
			if(table.children.length==1) {
				// Solo una fila, si la borramos nos quedamos sin elementos, y añadimos un mensaje.			
				$(table).append('<tr class="empty"><td colspan="'+colspan+'">No existen elementos que mostrar</td></tr>');
			}
			row.parentElement.removeChild(row);
			
		}
	});

	return row;
}

/**
 * Llama al servicio web que permite cambiar el orden de una capa, y realiza
 * la ordenación visualmente. 
 * @param assigned Si la capa pertenece a la carpeta o no.
 * @param displacement Si la capa sube -1, si la capa baja +1.
 * @param layerId El id de la capa movida.
 * @param clickedLink El enlace clickado para realizar la operación, usado para determinar la fila.
 */
function moveLayer(assigned, displacement, layerId, clickedLink) {
	var url = urls.moveLayer +"/"+ assigned + "/" + displacement + "/" + layerId;
	$.ajax({
		type : "POST",
		url : url,
		data : url,
		success : function(response) {
			var row = clickedLink.parentElement.parentElement;
			$(row).fadeOut({
				complete : function() {
					if (displacement > 0) {
						$(row).next().after($(row));
					} else {
						$(row).prev().before($(row));
					}

					$(row).fadeIn();
				}
			});

		},
		error : function(error) {
			alert("Ocurrió un error.");
		}
	});
}