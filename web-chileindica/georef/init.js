/*************************************************************************************/
/******************** VARIABLES GLOBALES**********************************************/
/*************************************************************************************/

/* Creación del mapa */
var map = L.map('map');
L.tileLayer('https://{s}.tiles.mapbox.com/v3/{id}/{z}/{x}/{y}.png', {
			maxZoom: 18,
			attribution: 'Map data &copy; <a href="http://openstreetmap.org">OpenStreetMap</a> contributors, ' +
				'<a href="http://creativecommons.org/licenses/by-sa/2.0/">CC-BY-SA</a>, ' +
				'Imagery © <a href="http://mapbox.com">Mapbox</a>',
			id: 'examples.map-i875mjb7'
}).addTo(map);

/********************************************************************************/
/*					Puntos de centrado por cada región                          */
/********************************************************************************/
var pto_cnt_def=new Array(); // key es el ID de la región
pto_cnt_def[15]={"name": "Arica y Parinacota", "lng":-70.314444,"lat":-18.475}; 
pto_cnt_def[1]={"name": "Tarapacá", "lng":-70.166667,"lat":-20.216667}; 
pto_cnt_def[2]={"name": "Antofagasta", "lng":-70.4,"lat":-23.633333}; 
pto_cnt_def[3]={"name": "Atacama", "lng":-70.316667,"lat":-27.366667}; 
pto_cnt_def[4]={"name": "Coquimbo", "lng":-71.25,"lat":-29.9}; 
pto_cnt_def[5]={"name": "Valparaíso", "lng":-71.633333,"lat":-33.066667}; 
pto_cnt_def[13]={"name": "Metropolitana de Santiago", "lng":-70.666667,"lat":-33.45}; 
pto_cnt_def[6]={"name": "O'Higgins", "lng":-70.75,"lat":-34.166667}; 
pto_cnt_def[7]={"name": "Maule", "lng":-71.666667,"lat":-35.433333}; 
pto_cnt_def[8]={"name": "BioBio", "lng":-73.05,"lat":-36.833333}; 
pto_cnt_def[9]={"name": "Araucanía", "lng":-72.666667,"lat":-38.75}; 
pto_cnt_def[14]={"name": "Los Ríos", "lng":-73.233333,"lat":-39.8}; 
pto_cnt_def[10]={"name": "Los Lagos", "lng":-72.933333,"lat":-41.466667}; 
pto_cnt_def[11]={"name": "Aysén", "lng":-72.066667,"lat":-45.566667}; 
pto_cnt_def[12]={"name": "Magallanes", "lng":-70.916667,"lat":-53.15}; 

// aqui irá el objeto con la info desde session(estudiar que datos vendran)
/*
   
   - ID Región actual
   - Array con puntos en UTM-> forma a pintar
   - Zona UTM
   - modo(Lectura(0)->solo consulta formas /Escritura(1)->Crear/eliminar formas)


*/
var info=JSON.parse($.cookie("infoGeo")); // se recoge desde formulario
//$.removeCookie("info"); // borramos la cookie por rendimiento   
// estructura de info
/* 
var info={
	"idRegion": 15,
	"modo": 1, //R=solo consulta, RMW=modificación
	"forma": [
				{"zona":"32718","x":638149,"y":5857111},
				{"zona":"32718","x":638649,"y":5887112},
				{"zona":"32718","x":638849,"y":5877112},
				{"zona":"32718","x":638149,"y":5857111},

	]
	 
}; 
*/

var forma;

/*************************************************************************************/
/******************** FUNCIONES AUXILIARES PARA VER FORMAS ***************************/
/*************************************************************************************/

// Visualiza una forma en el mapa
// @param ptos=> Array con puntos en lat y lng
function view(ptos)
{
	if(ptos.length>0)
	{
		if(ptos.length==1) view_pto(ptos[0]) // Es un punto		
		if(ptos.length>1) 
    	{
			if(equalsLngLat(ptos[0],ptos[ptos.length-1]))  view_poligono(ptos) // poligono			
			else view_plinea(ptos) // polilinea			
		}
	}	
}

// Visualiza un punto en el mapa
// @param pto=> Punto en lat y lng
function view_pto(pto)
{	
	var greenIcon = L.icon({
	    iconUrl: 'images/marker-icon.png',
	    iconSize:     [25, 41], // size of the icon	    	    
	    popupAnchor:  [0, -15] // point from which the popup should open relative to the iconAnchor
	});
	var marker=L.marker([pto.lat, pto.lng], {icon: greenIcon});
	marker.addTo(map);	
	forma=marker;
	// centramos y hacemos mayor zoom
	map.setView([pto.lat, pto.lng], 15);
}

// Visualiza una polilinea en el mapa
// @param ptos=> Array con puntos en lat y lng
function view_plinea(ptos)
{
	var pointList=new Array();
	for(var i=0;i<ptos.length;i++)
	{
		pointList[i]=new L.LatLng(ptos[i].lat, ptos[i].lng);	
	}	
	var polyline = new L.Polyline(pointList, {
	color: 'red',
	weight: 3,
	opacity: 0.5,
	smoothFactor: 1
	}).addTo(map);	
	// centramos y hacemos zoom dinamico
	//map.fitBounds(polyline.getBounds()); //--> deberia funcionar pero se queda pillado mapa
	forma=polyline;
}

// Visualiza una poligono en el mapa
// @param ptos=> Array con puntos en lat y lng
function view_poligono(ptos)
{

	var pointList=new Array();
	for(var i=0;i<ptos.length;i++)
	{
		pointList[i]=new L.LatLng(ptos[i].lat, ptos[i].lng);	
	}	
	var polygon = new L.polygon(pointList, {
												color: 'red',
												weight: 3,
												opacity: 0.5,
												smoothFactor: 1
											}).addTo(map);	
	forma=polygon;
	// centramos y hacemos zoom dinamico
	//map.fitBounds(polygon.getBounds()); //--> deberia funcionar pero se queda pillado mapa
	
}
/*************************************************************************************/
/******************** EJECUCIÓN ******************************************************/
/*************************************************************************************/

// Ejecucion del progama
$(document).ready(function(){	
	// ajuste automatico en alto del mapa e inicializa mapa
	$("#map").height($(document).height()-45);			
	// zoom y pto central(si existe forma->en primer punto y sino en centro de ciudad regional)
	if(info.forma.length==0) // no hay forma que pintar ->se centra en centro regional por constante
	{
		var latIni=pto_cnt_def[info.idRegion].lat;
		var longIni=pto_cnt_def[info.idRegion].lng;	
	}
	else // se centra en el primer punto de la forma
	{
		var latIni=UtmToLatLng(info.forma[0].x,info.forma[0].y,info.forma[0].zona).lat;
		var longIni=UtmToLatLng(info.forma[0].x,info.forma[0].y,info.forma[0].zona).lng;	
	}	
	var zoom=10;
	map.setView([latIni, longIni], zoom);
	// carga mapa solo con formas(dependera de los datos de info)	
	if(info.modo=='RMW') load_dynamic_map(info); // solo consulta
	else load_static_map(info); // solo consulta
});

// Método para cargar mapa con una forma(Solo lectura)
function load_static_map(info)
{
	// esconde div con instrucciones para crear formas
	$("#title_map").hide(); 
	// Dibujar la forma desde info
	if(info.forma.length>0) // si hay forma a pintar
	{
		// Se transforma los puntos a latLng
		array_ptos=array_UTMtoLatLng(info.forma);
		// Visualiar la forma
		view(array_ptos);		
	}
}

// Método para cargar mapa con una forma(Lectura/borrar/añadir formas)
function load_dynamic_map(info)
{	
	// Dibujar la forma desde info
	if(info.forma.length>0) // si hay forma a pintar
	{
		// Se transforma los puntos a latLng
		array_ptos=array_UTMtoLatLng(info.forma);
		// Visualiar la forma
		view(array_ptos);		
	}
	// se añade controles para gestionar formas
	var drawnItems = new L.FeatureGroup();
	map.addLayer(drawnItems);    
	// Initialise the draw control and pass it the FeatureGroup of editable layers
	var drawControl = new L.Control.Draw({
		draw: {
				position: 'topleft',
				polygon: {
					title: 'Dibujar un poligono',
					allowIntersection: false,					
					drawError: {
						color: '#b00b00',
						timeout: 1000
					},
					shapeOptions: {
						color: 'red'
					},
					showArea: true
				},
				polyline: {
					metric: false,
					shapeOptions: {
						color: 'red'
					}
				},
				circle: false,
				rectangle: false
			},
			edit: false,
			remove: false
		});
		map.addControl(drawControl);
		map.on('draw:drawstart', function (e) {		
			if(forma!=null)		
			{
				if(confirm("¿Estás seguro de que desea eliminar la forma actual?"))
				{
					map.removeLayer(forma);
					forma=null;
				}
				else
				{	
					 // quitar la forma activa por pintar
					 map.removeControl(drawControl);	
					 map.addControl(drawControl);
					 
				}
			}			
		});
		map.on('draw:created', function (e) {			

			var type = e.layerType,
				layer = e.layer;
				forma=layer;
			//console.log(type); tipo de forma			
			/*	
			if (type === 'marker') {
				layer.bindPopup('A popup!');
			}
			*/
			drawnItems.addLayer(layer);
			if(confirm("¿Desea guardar la forma actual?\n\n En caso de cancelar, se ELIMINARÁ la forma."))
			{
				// se limpia forma actual
				info.forma=[];
				//alert("A guardar forma en session y cerrar esta ventana");				
				// FALTA asignar las coordenadas nuevas a la session para tenerlo a mano
				// se asigna las coordenadas al formulario 
				var data=new Array();
				var data_input="";						
				var ptoUTM;	

				if (type === 'marker')  // un punto
				{ 					
					ptoUTM=LatLngToUtm(layer._latlng.lng,layer._latlng.lat);
					data[0]={"zona":ptoUTM.zona,"x":ptoUTM.x,"y":ptoUTM.y};
					data_input+="Punto 1 (" + ptoUTM.x + "," + ptoUTM.y  + ")" ;
				}
				else // poli-linea o polígono
				{
					var lat0,lng0; // datos del primer punto	
					for(var i=0;i<layer._latlngs.length;i++)
					{
						lat=layer._latlngs[i].lat;
						lng=layer._latlngs[i].lng;					
						if(i==0)
						{
							lat0=lat;
							lng0=lng;					
						}
						ptoUTM=LatLngToUtm(lng,lat);
						data[i]={"zona":ptoUTM.zona,"x":ptoUTM.x,"y":ptoUTM.y};
						data_input+="Punto " + (i+1) + " (" + ptoUTM.x + "," + ptoUTM.y  + ")" ;
						data_input+="\n";
					}
					// Para el caso del poligono(ultimo punto=primer punto)
					if (type === 'polygon') {
						ptoUTM=LatLngToUtm(lng0,lat0);
						data[data.length]={"zona":ptoUTM.zona,"x":ptoUTM.x,"y":ptoUTM.y};
						data_input+="Punto " + (data.length) + " (" + ptoUTM.x + "," + ptoUTM.y  + ")" ;
					}
				}				
				
				// guardar en info
				info.forma=data;
				// se guarda los cambios en la cookie
				$.cookie("infoGeo",JSON.stringify(info),{ path: '/' });										
				// se guarda formato en campo de formulario
				window.parent.document.getElementById('coordenadas').value = data_input;
				// se guarda los cambios en el input
				window.parent.document.getElementById('contenedor_array').value = JSON.stringify(info.forma);					
				// se elimina la forma actual
				forma=null;
				// se cierra la ventana
				window.parent.document.getElementById('cboxClose').click();				
			}
			else
			{
				map.removeLayer(forma);
				forma=null;
			}

		});
}


