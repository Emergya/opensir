<?php
//**************************************************************
// Grabar OTROS ANTECEDENTES
// 
//**************************************************************
  $nivel_pagina=2;
  include_once("../../include/seguridad.php");
  include_once("../../include/clases_inversiones.php");
  
 // GRABAR UBICACION TERRITORIAL
  $Bubicacion=new Ubicacion_Territorial_Iniciativa($BaseDatos->conector);
  $Bubicacion->ano=$_REQUEST["nano"];
  $Bubicacion->c_institucion=$_REQUEST["ninstitucion"];
  $Bubicacion->c_iniciativa=$_REQUEST["npreinversion"];
  $Bubicacion->c_ficha=$_REQUEST["nficha"];
  $error=$Bubicacion->eliminar_ubicacion_territorial();
  $Bubicacion->c_nivel_ut=$_REQUEST["SNivelUbicacion"];
  $Bubicacion->ubicacion_general=$_REQUEST["TUbicacion"];
  $Bubicacion->ubicacion_especifica=$_REQUEST["cod_ut"];
  $Bubicacion->ubicacion_especifica_nombre=$_REQUEST["nom_ut"];
  $Bubicacion->area_influencia=$_REQUEST["influencia"];
  $error=$Bubicacion->actualizar_ubicacion_territorial();      	   
  // GRABAR RELACION INSTRUMENTOS
  $Binstrumento=new Relacion_Instrumentos_Iniciativa($BaseDatos->conector);
  $Binstrumento->ano=$_REQUEST["nano"];      
  $Binstrumento->c_institucion=$_REQUEST["ninstitucion"];
  $Binstrumento->c_iniciativa=$_REQUEST["npreinversion"];
  $Binstrumento->c_ficha=$_REQUEST["nficha"];
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
// GRABAR BENEFICIARIO
  $Bbeneficiario=new Beneficiario_Inversion($BaseDatos->conector);
  $Bbeneficiario->ano=$_REQUEST["nano"];       
  $Bbeneficiario->c_institucion=$_REQUEST["ninstitucion"];
  $Bbeneficiario->c_preinversion=$_REQUEST["npreinversion"];
  $Bbeneficiario->c_ficha=$_REQUEST["nficha"];
  $error=$Bbeneficiario->eliminar_beneficiarios();
  $xbeneficiarios=$_REQUEST["vector_beneficiarios"];
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

  $BInversion=new Inversion($BaseDatos->conector);
  $BInversion->ano=$_REQUEST["nano"];
  $BInversion->c_institucion=$_REQUEST["ninstitucion"];
  $BInversion->c_preinversion=$_REQUEST["npreinversion"];
  $BInversion->c_ficha=$_REQUEST["nficha"];
  $BInversion->beneficiarios=$_REQUEST["texto_beneficiarios"];
  $BInversion->actualizar_texto_beneficiarios();

  
  /* Modificación georeferenciacion */
  $contenedor_array     = $_POST['contenedor_array'];
  // se borra la cookie de info de forma
  
  // Como al guardar el formulario no cambia de página no borramos la cookie
  //echo "<script language=\"JavaScript\" src=\"../../../js/jquery-1.8.1.min.js\"></script>";
  //echo "<script type=\"text/javascript\" src=\"../../../js/jquery.cookie.js\"></script>";
  //echo "<script> $.removeCookie('infoGeo', { path: '/' }); </script>";

  //$contenedor_array_ari     = array_recibe($contenedor_array_ari);
  if(!empty(contenedor_array))
  {
    $BInversion->ingresa_georeferencia($contenedor_array);  
  }
  

  /* Fin modificacion georeferenciacion */


?>