<?php

/*********************************************************************

INTERFACES GRAFICAS

**********************************************************************/

class I_Ubicacion_Territorial {

   function crear_interface($xnivel_ut,$xsoloconsulta,$xubicacion,$xinfluencia) {
		/****DIV 1****/
		$ventana =	"<div class='dvUTerritorialLeft'>";
		$ventana .= 	"<span><div class='titulo_medio'><p>Nivel</p>";
		$ventana .= 		"<select id=\"SNivelUbicacion\" name=\"SNivelUbicacion\" onChange=\"configurar_ut();\"".$sel1.">\n";
		if ($xnivel_ut==1) {$sel='selected="selected"';} else {$sel='';}
		$ventana .= 			"<option value=\"1\"".$sel.">Regional</option>\n";
		if ($xnivel_ut==2) {$sel='selected="selected"';} else {$sel='';}
		$ventana .= 			"<option value=\"2\"".$sel.">Provincial</option>\n";
		if ($xnivel_ut==3) {$sel='selected="selected"';} else {$sel='';}
		$ventana .= 			"<option value=\"3\"".$sel.">Comunal</option>\n";
		if ($xnivel_ut==4) {$sel='selected="selected"';} else {$sel='';}
		$ventana .= 			"<option value=\"4\"".$sel.">Localidad</option>\n";
		$ventana .= 		"</select></div>\n";
		if ($xsoloconsulta==1) {$sel1=" disabled='disabled'";} else {$sel1="";}
		$ventana .=		"</span>";
		$ventana .=		"<div>";
		$ventana .= 	"</div></br>\n";
		$ventana .= 	"<select id=\"LUbicacion\" name=\"LUbicacion\" class='selectMulti' multiple='multiple'></select></br>\n";
		if ($xsoloconsulta!=1) {
			$ventana .= "<input type=\"button\" class='boton' id=\"SeleccionarUbicacion\" value=\"Seleccionar\" onClick=\"seleccionar_ut();\" name=\"button\" />\n";
		}
		$ventana .=	"</div>";
		
		if ($xsoloconsulta==1) {
		/****DIV 2****/
		$ventana .=	"<div class='dvUTerritorialLeft'>";
		$ventana .=		"<span><div class='titulo_medio'><p>Area de Influencia</p></div></span></br>";
		if ($xinfluencia==1) {$sel='checked';} else {$sel='';}
		$ventana .= 	"<div class='ov_auto'><input type='radio' class='inputRadio' id='influencia1' name='influencia' value='1'".$sel." disabled/><label for='influencia1'>Internacional</label></div>\n";
		if ($xinfluencia==2) {$sel='checked';} else {$sel='';}
		$ventana .= 	"<div class='ov_auto'><input type='radio' class='inputRadio' id='influencia2' name='influencia' value='2'".$sel." disabled/><label for='influencia2'>Nacional</label></div>\n";
		if ($xinfluencia==3) {$sel='checked';} else {$sel='';}
		$ventana .= 	"<div class='ov_auto'><input type='radio' class='inputRadio' id='influencia3' name='influencia' value='3'".$sel." disabled/><label for='influencia3' >Regional</label></div>\n";
		if ($xinfluencia==4) {$sel='checked';} else {$sel='';}
		$ventana .= 	"<div class='ov_auto'><input type='radio' class='inputRadio' id='influencia4' name='influencia' value='4'".$sel." disabled/><label for='influencia4'>Provincial</label></div>\n";
		if ($xinfluencia==5) {$sel='checked';} else {$sel='';}
		$ventana .= 	"<div class='ov_auto'><input type='radio' class='inputRadio' id='influencia5' name='influencia' value='5'".$sel." disabled/><label for='influencia5'> Comunal </label></div>\n";
		$ventana .=	"</div>";
		}
		if ($xsoloconsulta!=1) {
		/****DIV 2****/
		$ventana .=	"<div class='dvUTerritorialLeft'>";
		$ventana .=		"<span><div class='titulo_medio'><p>Area de Influencia</p></div></span></br>";
		if ($xinfluencia==1) {$sel='checked';} else {$sel='';}
		$ventana .= 	"<div class='ov_auto'><input type='radio' class='inputRadio' id='influencia1' name='influencia' value='1'".$sel."/><label for='influencia1'>Internacional</label></div>\n";
		if ($xinfluencia==2) {$sel='checked';} else {$sel='';}
		$ventana .= 	"<div class='ov_auto'><input type='radio' class='inputRadio' id='influencia2' name='influencia' value='2'".$sel."/><label for='influencia2'>Nacional</label></div>\n";
		if ($xinfluencia==3) {$sel='checked';} else {$sel='';}
		$ventana .= 	"<div class='ov_auto'><input type='radio' class='inputRadio' id='influencia3' name='influencia' value='3'".$sel."/><label for='influencia3' >Regional</label></div>\n";
		if ($xinfluencia==4) {$sel='checked';} else {$sel='';}
		$ventana .= 	"<div class='ov_auto'><input type='radio' class='inputRadio' id='influencia4' name='influencia' value='4'".$sel."/><label for='influencia4'>Provincial</label></div>\n";
		if ($xinfluencia==5) {$sel='checked';} else {$sel='';}
		$ventana .= 	"<div class='ov_auto'><input type='radio' class='inputRadio' id='influencia5' name='influencia' value='5'".$sel."/><label for='influencia5'> Comunal </label></div>\n";
		$ventana .=	"</div>";
		}
		/****DIV 3****/
		$ventana .=	"<div class='dvUTerritorialLeft'>";
		$ventana .=		"<span><div class='titulo_medio'><p>Especificaciones</p></div></span></br>\n";
		if ($xsoloconsulta==1) {$sel=" readonly='readonly'";} else {$sel="";}
		$ventana .= 	"<textarea id=\"TUbicacion\" name=\"TUbicacion\" style='width:100px; height:100px;' wrap='VIRTUAL' class=\"area_texto\" ".$sel.">".$xubicacion."</textarea>\n";
		$ventana .=	"</div>";
		
		/****DIV 4****/
		$ventana .=	"<div class='dvUTerritorialLeft'>";
		$ventana .=		"<span><div class='titulo_medio'><p>Georeferenciaci&oacute;n (x,y)</p></div></span></br>\n";
		// Para la geolocalización en mapa	  
		$ventana .=	"<div style='display:block;clear:both;margin-left:20px;margin-bottom:3px'><a id='linkMmap' href='../../../georef/mapa.html' title='Datos Georeferenciaci&oacute;n' alt='Datos Georeferenciaci&oacute;n'><img style='width:25px' align='absmiddle' src=\"../../../georef/images/icon_map.png\"> Gestionar en mapa </a></div>";		
		// fin para la geolocalización en mapa	
		$ventana .= 	"<textarea id=\"coordenadas\" name=\"coordenadas\" class=\"area_texto\" disabled=\"disabled\"></textarea>\n";
		$ventana .=	"</div>";
		
		return $ventana;
   }
}

// MENSAJE
class I_Mensaje {

   function crear_interface() {
      $ventana =  "<div id=\"carga\" style=\"position:absolute; left:440px; top:200px; width:225px; height:38px; z-index:1; visibility: hidden\">";
      $ventana .= "  <table width=\"230\" height=\"40\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" bgcolor=\"#ECEEAA\" class=\"borde_completonegro\">";
      $ventana .= "            <tr>";
      $ventana .= "               <td width=\"50\" align=\"center\" valign=\"middle\" class=\"textomensaje\"><img src=\"../../imagenes/cargando.gif\" width=\"32\" height=\"32\"></td>";
      $ventana .= "               <td width=\"148\" align=\"center\" valign=\"middle\" class=\"tabla\" id=\"textomensaje\" name=\"textomensaje\"><strong>MENSAJE</strong></td>";
      $ventana .= "            </tr>";
      $ventana .= "         </table>";
      $ventana .= "      </div>";
      return $ventana;
   }
   function crear_interface1($xleft,$xtop) {
      $ventana =  "<div id=\"carga\" style=\"position:absolute; left:".$xleft."px; top:".$xtop."px; width:225px; height:38px; z-index:1; visibility: hidden\">";
      $ventana .= "  <table width=\"230\" height=\"40\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" bgcolor=\"#ECEEAA\" class=\"borde_completonegro\">";
      $ventana .= "            <tr>";
      $ventana .= "               <td width=\"50\" align=\"center\" valign=\"middle\" class=\"textomensaje\"><img src=\"../../imagenes/cargando.gif\" width=\"32\" height=\"32\"></td>";
      $ventana .= "               <td width=\"148\" align=\"center\" valign=\"middle\" class=\"tabla\" id=\"textomensaje\" name=\"textomensaje\"><strong>MENSAJE</strong></td>";
      $ventana .= "            </tr>";
      $ventana .= "         </table>";
      $ventana .= "      </div>";
      return $ventana;
   }
}      

?>