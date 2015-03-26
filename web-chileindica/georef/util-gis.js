/* 
* Conversión de UTM to LngLat *
* Example: UtmToLatLng("638149","5857111","32718"); 
* Return: Object(lng,lat)
*/
function UtmToLatLng(x,y,zonaUTM)
{
	var zona=zonaUTM.substring(zonaUTM.length-2,zonaUTM.length);		
	Proj4js.defs["EPSG:" + zonaUTM] = "+proj=utm +zone=" + zona + " +south +ellps=WGS84 +datum=WGS84 +units=m +no_defs";
	var source = new Proj4js.Proj('EPSG:'+zonaUTM);   // orignn UTM18 
	var dest = new Proj4js.Proj('EPSG:4326');     // Destino LatLng
    // transforming point coordinates
    var p = new Proj4js.Point(x,y);   // pto x,y
    //console.log(Proj4js.transform(source, dest, p));  
    var pto=Proj4js.transform(source, dest, p);    
    return { lng: pto.x, lat: pto.y };
}

/* Convert LatLng to UTM*/
/* Example: LatLngToUtm(-73.43871,-37.42252) */
/* Return: Object(x,y,zone)
*/
function LatLngToUtm(lng,lat)
{
	// get zone UTM from Lng
	zona=parseInt((lng / 6) + 31);
	zonaUTM="327"+zona;	
	Proj4js.defs["EPSG:" + zonaUTM] = "+proj=utm +zone=" + zona + " +south +ellps=WGS84 +datum=WGS84 +units=m +no_defs"; // UTM12
	var source = new Proj4js.Proj('EPSG:4326');    // Origen LatLng
	var dest = new Proj4js.Proj('EPSG:'+zonaUTM);     // destino UTM18
    // transforming point coordinates
    var p = new Proj4js.Point(lng,lat);   // pto Lng,Lat
    var pto=Proj4js.transform(source, dest, p);     
    return { x: Math.floor(pto.x), y:Math.floor(pto.y), zona: zonaUTM };     
}

/* Funcion que compara si dos puntos en UTM son el mismo */
function equalsUTM(p1,p2)
{
	if((p1.zona==p2.zona)&&(p1.x==p2.x)&&(p1.y==p2.y)) return true;
	return false;
}

/* Funcion que compara si dos puntos en LngLat son el mismo */
function equalsLngLat(p1,p2)
{
	if((p1.lat==p2.lat)&&(p1.lng==p2.lng)) return true;
	return false;
}

/* Funcion para transformar un array de UTM a array LatLng */
function array_UTMtoLatLng(arrayUTM)
{
	var result=new Array();
	if(arrayUTM.length>0)
	{
		for (var i=0; i<arrayUTM.length; i++) {
    		result[i]=UtmToLatLng(arrayUTM[i].x,arrayUTM[i].y,arrayUTM[i].zona);
		}
	}
	return result;
}