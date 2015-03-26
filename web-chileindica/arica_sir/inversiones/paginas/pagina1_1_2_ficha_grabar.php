<?php

//**************************************************************
// Grabar FICHA DE PREINVERSION ARI
// 
//**************************************************************
$restriccion_modulo='01010102';
$nivel_pagina=2;
include_once("../../include/seguridad.php");
include_once("../../include/clases_login.php");   
include_once("../../login/valida_usuario.php"); 
include_once("../../include/clases_iniciativa_ari.php");
  
$xpreinversion      = $_REQUEST["npreinversion"];
$xnano              = $_REQUEST["nano"];
$xinstitucion       = $_REQUEST["ninstitucion"];
$xfinanciamiento    = $_REQUEST["vector_financiamiento"];
$xbeneficiarios     = $_REQUEST["vector_beneficiarios"];
$xnulo              = '';




// CONFIGURAR FECHAS  
$xfecha_inicio=$_REQUEST["fecha_inicio"];
if ($xfecha_inicio.length>0) {
  $xfecha_inicio=substr($xfecha_inicio,6,4).substr($xfecha_inicio,3,2).substr($xfecha_inicio,0,2);
} else {
  $xfecha_inicio='0';
}
$xfecha_termino=$_REQUEST["fecha_termino"];
if ($xfecha_termino.length>0) {
  $xfecha_termino=substr($xfecha_termino,6,4).substr($xfecha_termino,3,2).substr($xfecha_termino,0,2);
} else {
  $xfecha_termino='0';
} 
  

$Biniciativa=new Iniciativa_Ari($BaseDatos->conector);
$Biniciativa->ano=$xnano;
$Biniciativa->c_institucion=$xinstitucion;
if ($xpreinversion==0) {
  $dataset=$Biniciativa->calcular_valor_maximo();
  $Registro = mysql_fetch_row($dataset);
  $xpreinversion=$Registro[0]+1;
} else {
  $xpreinversion=substr($xpreinversion, strlen($xpreinversion)-3,3)+0;
}  
$Biniciativa->c_preinversion      = $xpreinversion;
$Biniciativa->unidad_tecnica      = $_REQUEST["e_unidad_tecnica"];
$Biniciativa->n_preinversion      = $_REQUEST["e_nombre"];
$Biniciativa->responsable_propir  = $_REQUEST["e_responsable_propir"];
$Biniciativa->c_programa_ari      = $_REQUEST["e_programa"];
$Biniciativa->c_clasificador_presupuestario=$_REQUEST["cb_item_presupuestario"];
$Biniciativa->c_etapa_idi         = $_REQUEST["cb_etapa"];
$Biniciativa->fecha_inicio        = $xfecha_inicio;
$Biniciativa->fecha_termino       = $xfecha_termino;
$Biniciativa->c_estado_proyecto   = $_REQUEST["cb_situacion"];
$Biniciativa->producto            = $_REQUEST["e_producto"];
$Biniciativa->codigo              = $_REQUEST["e_codigo"];
$Biniciativa->c_tipo_codigo       = $_REQUEST["cb_tipo_codigo"];
$Biniciativa->rate                = $_REQUEST["cb_rate"];
// $Biniciativa->impactos            = $_REQUEST["e_impactos"];
$Biniciativa->c_sector            = $_REQUEST["cb_sectorinversion"];
$Biniciativa->c_estado_preinversion=$_REQUEST["nestadoari"];
$Biniciativa->observaciones       = $_REQUEST["Observaciones"];
$Biniciativa->beneficiarios       = $_REQUEST["texto_beneficiarios"];
$Biniciativa->costo_total         = $_REQUEST["ncosto_total"];
$Biniciativa->gastado_anos_anteriores=$_REQUEST["ngastado_anos_anteriores"];
$Biniciativa->solicitado          = $_REQUEST["nsolicitado"];
$Biniciativa->saldo_proximos_anos = $_REQUEST["nsaldo_proximos_anos"];
$Biniciativa->fecha_registro      = date('Ymd');
$Biniciativa->c_usuario           = $_REQUEST["id_usuario"];

$contenedor_array_ari     = $_POST['contenedor_array'];
/* Modificación georeferenciacion */
// se borra la cookie de info de forma

echo "<script language=\"JavaScript\" src=\"../../../js/jquery-1.8.1.min.js\"></script>";
echo "<script type=\"text/javascript\" src=\"../../../js/jquery.cookie.js\"></script>";
echo "<script> $.removeCookie('infoGeo', { path: '/' }); </script>";

//$contenedor_array_ari     = array_recibe($contenedor_array_ari);
if(!empty(contenedor_array_ari))
{
  $Biniciativa->ingresa_georeferencia_ari($contenedor_array_ari);  
}


/* Fin modificacion georeferenciacion */


$error=$Biniciativa->actualizar_iniciativa();
echo "<script>console.log('Actualizando');</script>";
if ($error!='') {
  echo $error;
  echo "<script>console.log('Error al actualizar iniciativa');</script>";
} else { 
  //     $query = " REPLACE INTO RDR_PREINVERSION_ARI VALUES('";
  //     $query .= $xnano."','".$xinstitucion."','".$xpreinversion."','".$_REQUEST["c_recurso"]."','".$_REQUEST["cb_recurso"]."')";	
  //     $dataset  = mysql_query($query, $BaseDatos->conector);   	
  //GRABAR UBICACION TERRITORIAL
  $Bubicacion=new Ubicacion_Territorial_Iniciativa_Ari($BaseDatos->conector);
  $Bubicacion->c_institucion=$xinstitucion;
  $Bubicacion->c_preinversion=$xpreinversion;
  $Bubicacion->ano=$xnano;
  $error=$Bubicacion->eliminar_ubicacion_territorial();
  $Bubicacion->c_nivel_ut=$_REQUEST["SNivelUbicacion"];
  $Bubicacion->ubicacion_general=$_REQUEST["TUbicacion"];
  $Bubicacion->ubicacion_especifica=$_REQUEST["cod_ut"];
  $Bubicacion->ubicacion_especifica_nombre=$_REQUEST["nom_ut"];
  $Bubicacion->area_influencia=$_REQUEST["influencia"];
  $error=$Bubicacion->actualizar_ubicacion_territorial();      	
  //GRABAR RELACION INSTRUMENTOS
  $Binstrumento=new Relacion_Instrumentos_Iniciativa_Ari($BaseDatos->conector);
  $Binstrumento->c_institucion=$xinstitucion;
  $Binstrumento->c_preinversion=$xpreinversion;
  $Binstrumento->ano=$xnano;        
  $error=$Binstrumento->eliminar_relacion_instrumentos();  
  // TIPO 1
  for ($i=0; $i < $_REQUEST["totinst1"]; $i++) {
    $Binstrumento->c_instrumento=$_REQUEST["cod_inst".$i];
    $Binstrumento->relacionado=$nulo;
    $Binstrumento->relacion1=$_REQUEST["cod_inst_ppal".$i];
    $Binstrumento->relacion2=$_REQUEST["cod_inst_otro".$i];
    $Binstrumento->relacion1_nombre=$_REQUEST["nom_inst_ppal".$i];
    $Binstrumento->relacion2_nombre=$_REQUEST["nom_inst_otro".$i];
    $error=$Binstrumento->actualizar_relacion_instrumentos();  
  }
  // TIPO 2
  for ($i=0; $i < $_REQUEST["totinst2"]; $i++) {
    $Binstrumento->c_instrumento=$_REQUEST["cod_inst".($_REQUEST["totinst1"]+$i)];
    $Binstrumento->relacionado=$_REQUEST["rel_inst".($_REQUEST["totinst1"]+$i)];
    $Binstrumento->relacion1=$nulo;
    $Binstrumento->relacion2=$_REQUEST["EspecificaInstrumento".($_REQUEST["totinst1"]+$i)];
    $Binstrumento->relacion1_nombre=$nulo;
    $Binstrumento->relacion2_nombre=$nulo;
    $error=$Binstrumento->actualizar_relacion_instrumentos();  
  }
  // GRABAR FINANCIAMIENTO
  $Bfinanciamiento=new Financiamiento_Iniciativa_Ari($BaseDatos->conector);
  $Bfinanciamiento->c_institucion=$xinstitucion;
  $Bfinanciamiento->c_preinversion=$xpreinversion;
  $Bfinanciamiento->ano=$xnano;        
  $error=$Bfinanciamiento->eliminar_fuentes_financiamiento();
  $vector=explode('[', $xfinanciamiento);
  $valores="";$k=1;$lv=1;
  for ($i=0; $i<(count($vector)-1); $i++)	{
	  
    if ($k==2 || $k==3 || $k==4 || $k==5) {
      $valores .=",'".$vector[$i]."'";
    }
    
    if ($k==6) {
      $valores ="'".$vector[$i]."'".$valores;
	     
      if ($vector[$i]!='0') {
        $vector1[$lv]=$valores;$lv++;  	
      }   
      
      $valores="";$k=0;
    }	  
    
    $k++;  
  }
  for ($i=1; $i<(count($vector1)+1); $i++) {  	
    $Bfinanciamiento->vector=$vector1[$i];
    $Bfinanciamiento->actualizar_fuentes_financiamiento();
  }	
  // GRABAR BENEFICIARIO
  $Bbeneficiario=new Beneficiario_Iniciativa_Ari($BaseDatos->conector);
  $Bbeneficiario->c_institucion=$xinstitucion;
  $Bbeneficiario->c_preinversion=$xpreinversion;
  $Bbeneficiario->ano=$xnano;        
  $error=$Bbeneficiario->eliminar_beneficiarios();
  $vector=explode('[', $xbeneficiarios);
  $valores="";$k=1;$lv=1;
  for ($i=0; $i<(count($vector)-1); $i++)	{
    if ($k==2 || $k==3 || $k==4 || $k==6) {
      $valores .=",'".$vector[$i]."'";
    }
	
    if ($k==7) {
      $valores ="'".$vector[$i]."'".$valores;
      
      if ($vector[$i]!='0') {
	      $vector2[$lv]=$valores;$lv++;  	
      }   
	  
      $valores="";$k=0;
    }	  
    
    $k++;  
  }	
  for ($i=1; $i<(count($vector2)+1); $i++) {  	
    $Bbeneficiario->vector=$vector2[$i];
    $Bbeneficiario->actualizar_beneficiarios();
  }	
  echo "<script language=\"JavaScript\">";
  echo "var nn='".(($xinstitucion*1000)+$xpreinversion)."';";
  echo "parent.document.form1.npreinversion.value=nn;";
  echo "parent.configurar_ficha();";
  // echo "parent.cerrar();";
 echo 'parent.document.form1.action="pagina1_1_2.php";';
 echo 'parent.document.form1.target="_self";';
 echo 'parent.document.form1.submit();'; 
  echo "</script>";  	        	        	
  @mysql_close($BaseDatos->conector);
} 

/**
 * Transforma y devuelve un array
 */
function array_recibe($contenedor_array_ari) { 
  $tmp = stripslashes($contenedor_array_ari); 
  $tmp = urldecode($tmp); 
  $tmp = unserialize($tmp); 
  return $tmp; 
} 
?>