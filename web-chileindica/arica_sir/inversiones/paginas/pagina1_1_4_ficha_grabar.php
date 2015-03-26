<?php

//**************************************************************
// Grabar FICHA DE PREINVERSION
// 
//**************************************************************

  $restriccion_modulo='0101010401';
  $nivel_pagina=2;
  include_once("../../include/seguridad.php");
  include_once("../../include/clases_login.php");   
  include_once("../../login/valida_usuario.php"); 
  include_once("../../include/clases_preinversion.php");
  
  $xaccion        = $_REQUEST['naccion'];
  $xpreinversion  = $_REQUEST["npreinversion"];
  $xnano          = $_REQUEST["nano"];
  $xinstitucion   = $_REQUEST["ninstitucion"];
  $xbeneficiarios = $_REQUEST["vector_beneficiarios"];
  $xnulo          = '';

  $numero_oficio        = $_REQUEST['obs_numero_oficio'];
  $fecha_oficio         = $_REQUEST['obs_fecha_oficio'];
  $respuesta_municipio  = $_REQUEST['obs_respuesta'];
  $id_observacion       = $_REQUEST['id_observacion'];

  $contenedor_array     = $_POST['contenedor_array'];
  //$contenedor_array     = array_recibe($contenedor_array);


  // Valores para el detalle de las vias de financiamiento
  $solicitado_ano       = $_POST['solicitado_ano'];
  $saldo_por_invertir   = $_POST['saldo_por_invertir'];
  $costo_total          = $_POST['costo_total'];
  $u_tecnica            = $_POST['u_tecnica'];
  $clasificadores       = $_POST['clasificador_detalle'];

// CONFIGURAR FECHAS  
  $xfecha_inicio=$_REQUEST["fecha_inicio"];
  if ($xfecha_inicio.length>0) {
		$xfecha_inicio=substr($xfecha_inicio,6,4).substr($xfecha_inicio,3,2).substr($xfecha_inicio,0,2);
  } else {$xfecha_inicio='0';}
  $xfecha_termino=$_REQUEST["fecha_termino"];
  if ($xfecha_termino.length>0) {
		$xfecha_termino=substr($xfecha_termino,6,4).substr($xfecha_termino,3,2).substr($xfecha_termino,0,2);
  } else {$xfecha_termino='0';} 
  

  $Biniciativa=new Preinversion($BaseDatos->conector);
  $Biniciativa->ano=$xnano;
  $Biniciativa->c_institucion=$xinstitucion;

  if ($xpreinversion==0) {
     $dataset=$Biniciativa->calcular_valor_maximo();
     $Registro = mysql_fetch_row($dataset);
     $xpreinversion=$Registro[0]+1;
  }

  $Biniciativa->c_preinversion          = $xpreinversion;
  $Biniciativa->n_preinversion          = $_REQUEST["e_nombre"];
  $Biniciativa->unidad_tecnica          = $_REQUEST["e_unidad_tecnica"];
  $Biniciativa->c_via_financiamiento    = $_REQUEST["cb_via_financiamiento"];
  $Biniciativa->fecha_inicio            = $xfecha_inicio;
  $Biniciativa->fecha_termino           = $xfecha_termino;
  $Biniciativa->producto                = $_REQUEST["e_producto"];
  $Biniciativa->codigo                  = $_REQUEST["e_codigo"];
  $Biniciativa->codigo_dv               = $_REQUEST['e_codigo_dv'];
  $Biniciativa->c_tipo                  = $_REQUEST['e_tipo'];
  $Biniciativa->impactos                = $_REQUEST["e_impactos"];
  $Biniciativa->c_etapa                 = $_REQUEST["cb_etapa"];
  $Biniciativa->c_sector                = $_REQUEST["cb_sectorinversion"];
  $Biniciativa->c_estado_preinversion   = $_REQUEST["nestadoari"];
  $Biniciativa->observaciones           = $_REQUEST["Observaciones"];
  $Biniciativa->beneficiarios           = $_REQUEST["texto_beneficiarios"];
  $Biniciativa->rate                    = $_REQUEST['cb_rate'];
  $Biniciativa->descriptor              = $_REQUEST['descriptor'];
  $Biniciativa->cb_fuentef              = $_REQUEST['cb_fuentef'];

  //new fields added 03-10-2013
  $Biniciativa->direccion               = $_REQUEST['direccion'];
  $Biniciativa->justificacion           = $_REQUEST['justificacion_proyecto'];
  
//nuevos campos Pedidos por Magallanes 17 de Junio 2014
  $Biniciativa->nombre_formulador       = $_REQUEST['nombre_formulador'];
  $Biniciativa->telefono                = $_REQUEST['telefono'];
  $Biniciativa->correo_electronico      = $_REQUEST['correo_electronico'];
   
  
  $monto=str_replace(".", "", $_REQUEST["e_costo_total"]);
  $Biniciativa->costo_total             = $monto;
  $monto=str_replace(".", "", $_REQUEST["e_solicitado"]);
  $Biniciativa->solicitado              = $monto;
  $monto=str_replace(".", "", $_REQUEST["e_saldo"]);
  $Biniciativa->saldo_proximos_anos     = $monto;
  $Biniciativa->fecha_registro          = date('Ymd');
  $Biniciativa->c_usuario               = $_REQUEST["id_usuario"];
  $error=$Biniciativa->actualizar_iniciativa();
  $Biniciativa->registra_cambio_estado();
  $Biniciativa->cambiar_estado_iniciativa($_REQUEST["nestadoari"]);


    // Se ingresa el detalle de las vias de financiamiento
  $Biniciativa->clean_detalle_financiamiento();
  $id_defi = 1;
  foreach ($solicitado_ano as $key => $value) {
    $soli       = ($value != "") ? str_replace(".", "",$value) : 0;
    $clas       = ($clasificadores[$key] != "" ) ? $clasificadores[$key] : 0;
    $saldo      = ($saldo_por_invertir[$key] != "" ) ? str_replace(".", "",$saldo_por_invertir[$key]) : 0;
    $total      = ($costo_total[$key] != "" ) ? str_replace(".", "",$costo_total[$key]) : 0;
    $unidad     = $u_tecnica[$key];

    $Biniciativa->add_detalle_financiamiento($clas, $soli, $saldo, $total, $unidad,$id_defi);
    $id_defi = $id_defi +1;
  
  }

  // Se agrega registro en Bitacota del proyecto
  $Biniciativa->registra_cambio_estado_bitacora($_REQUEST["nestado_actual"],$_REQUEST["nestadoari"]);

  //$datos = array_recibe($contenedor_array);
  /* Modificación georeferenciacion */
  // se borra la cookie de info de forma

  echo "<script language=\"JavaScript\" src=\"../../../js/jquery-1.8.1.min.js\"></script>";
  echo "<script type=\"text/javascript\" src=\"../../../js/jquery.cookie.js\"></script>";
  echo "<script> $.removeCookie('infoGeo', { path: '/' }); </script>";
  
  if(!empty(contenedor_array))
  {
    $Biniciativa->ingresa_georeferencia($contenedor_array);  
  }


  /* Fin modificacion georeferenciacion */

  //$Biniciativa->ingresa_georeferencia($contenedor_array);

  // save files
  function reArrayFiles(&$file_post) {

    $file_ary = array();
    $file_count = count($_FILES['file']['name']) + 1;
    $file_keys = array_keys($file_post);

    for ($i=1; $i<$file_count; $i++) {
        foreach ($file_keys as $key) {
            $file_ary[$i][$key] = $file_post[$key][$i];
        }
    }

    return $file_ary;
  }

  if($xaccion == 1 || $xaccion == 10){

    // die(print_r($_POST));

    $query_ndoc = sprintf("SELECT MAX(C_DOCUMENTO) FROM PREINVERSION_DOCUMENTACION WHERE ANO = %d AND C_PREINVERSION = %d AND C_INSTITUCION = %d", $_POST["nano"], $xpreinversion, $_POST["ninstitucion"]);
    $dataset_ndoc = mysql_query($query_ndoc, $BaseDatos->conector);
    $record_ndoc =  mysql_fetch_row($dataset_ndoc);
    $ndocumento=$record_ndoc[0];
    $docinicial=0;
    $file_ary = reArrayFiles($_FILES['file']);
    
    foreach ($file_ary as $key => $value) {
      $ndocumento++;
      $narchivo=$file_ary[$key]['name'];
      $extension = explode(".",$narchivo);
      $num = count($extension)-1; 
      if ($extension[$num]=='pdf' ) {$ntipo=1;}
      if ($extension[$num]=='jpg') {$ntipo=2;}
      if ($extension[$num]=='gif') {$ntipo=3;}
      if ($extension[$num]=='dwg') {$ntipo=4;} //Autocad
      if ($extension[$num]=='docx') {$ntipo=5;}
      if ($extension[$num]=='doc') {$ntipo=6;}
      if ($extension[$num]=='odt') {$ntipo=7;} // Libre/Open Office Writter 
      if ($extension[$num]=='xlsx') {$ntipo=8;}
      if ($extension[$num]=='xls') {$ntipo=9;}
      if ($extension[$num]=='ods') {$ntipo=10;} // Libre/Open Office Calc 
      if ($extension[$num]=='pptx') {$ntipo=11;}
      if ($extension[$num]=='ppt') {$ntipo=12;}
      if ($extension[$num]=='odp') {$ntipo=13;} // Libre/Open Office Impress 

      $nombrearchivo = "DOC_PREINV_".$_POST["nano"]."_".$xinstitucion."_".$xpreinversion."_".$key.".".$extension[$num];
      $query_documentacion[$key] = sprintf("INSERT INTO PREINVERSION_DOCUMENTACION (ANO, C_PREINVERSION, C_INSTITUCION, C_DOCUMENTO, TIPO, DESCRIPCION, ARCHIVO, ESTADO) VALUES( %d, %d, %d, %d, %d, '%s', '%s', %d)", $_POST["nano"], $xpreinversion, $xinstitucion, $ndocumento, 1, $_POST['filename'][$key], $nombrearchivo, 0);
      $dataset_documentacion[$key] = mysql_query($query_documentacion[$key], $BaseDatos->conector); 
      
      $query_archivo[$key] = sprintf("INSERT INTO PREINVERSION_ARCHIVO (C_ARCHIVO, ANO, C_PREINVERSION, C_INSTITUCION, C_DOCUMENTO, NOMBRE ) VALUES (%d, %d, %d, %d,'%d','%s' )", $docinicial, $_POST["nano"], $xpreinversion, $xinstitucion, $ndocumento, $nombrearchivo);
      $dataset_archivo[$key]  = mysql_query($query_archivo[$key], $BaseDatos->conector);

      if (!copy($file_ary[$key]['tmp_name'], "../adjuntos/".$nombrearchivo)) {
        echo "<script>alert('No se pudo anexar el archivo');</script>";
      }
    }
  }

  // save files

  // GRABAR RESPUESTA EN CASO DE ESTAR OBSERVADO desde la etama de Pre-Admisibilidad
  if ($xaccion == 15) { // Respuesta a Observacion - Accion 15
    $id_observacion++;
    if (!empty($_FILES['obs_adjunto']['name'])) 
    {
      $narchivo=$_FILES['obs_adjunto']['name'];   
      $tarchivo=$_FILES['obs_adjunto']['type'];   
      $sarchivo=$_FILES['obs_adjunto']['size']; 
      $temparchivo=$_FILES['obs_adjunto']['tmp_name'];
      $extension = explode(".",$narchivo);
      $num = count($extension)-1; 

      if ($extension[$num]=='pdf' ) {$ntipo=1;}
      if ($extension[$num]=='jpg') {$ntipo=2;}
      if ($extension[$num]=='gif') {$ntipo=3;}
      // $id_observacion++;
      $nombrearchivo="DOC_OBSERVACION_RESPUESTA_".$_POST["nano"]."_".$xinstitucion."_".$_POST["npreinversion"]."_".$id_observacion.".".$extension[$num];
       
      if (!copy($temparchivo, "../adjuntos/".$nombrearchivo)) {
        echo "<script>alert('No se pudo anexar el archivo');</script>";
      }
    }else{
      $nombrearchivo = 0;
    }

    if ($fecha_oficio.length>0) {
      $fecha_oficio=substr($fecha_oficio,6,4).substr($fecha_oficio,3,2).substr($fecha_oficio,0,2);
    } else {$fecha_oficio='0';} 

    $Biniciativa->numero_oficio         = "";
    $Biniciativa->fecha_oficio          = "";
    $Biniciativa->respuesta_municipio   = $respuesta_municipio;
    $Biniciativa->id_observacion        = $id_observacion;
    $Biniciativa->estado_actual         = $_REQUEST['nestado_actual'];
    $Biniciativa->c_estado_destino      = $_REQUEST["nestadoari"];

    $error_1 = $Biniciativa->ingresar_respuesta($nombrearchivo,$id_observacion);
    
    if ($error_1!= "") {
      echo "error_mysql " . $error_1;
    }
  }

  if ($error!='') {
    echo "Error " . $error;
  
  } else { 
// GRABAR UBICACION TERRITORIAL
     $Bubicacion=new Ubicacion_Territorial_Preinversion($BaseDatos->conector);
     $Bubicacion->c_institucion         = $xinstitucion;
     $Bubicacion->c_preinversion        = $xpreinversion;
     $Bubicacion->ano=$xnano;
     $error=$Bubicacion->eliminar_ubicacion_territorial();
     $Bubicacion->c_nivel_ut            = $_REQUEST["SNivelUbicacion"];
     $Bubicacion->ubicacion_general     = $_REQUEST["TUbicacion"];
     $Bubicacion->ubicacion_especifica  = $_REQUEST["cod_ut"];
     $Bubicacion->ubicacion_especifica_nombre=$_REQUEST["nom_ut"];
     $Bubicacion->area_influencia       = $_REQUEST["influencia"];
     $error=$Bubicacion->actualizar_ubicacion_territorial();   
       	
// GRABAR RELACION INSTRUMENTOS
     $Binstrumento=new Relacion_Instrumentos_Preinversion($BaseDatos->conector);
     $Binstrumento->c_institucion       = $xinstitucion;
     $Binstrumento->c_preinversion      = $xpreinversion;
     $Binstrumento->ano                 = $xnano;        
     $error=$Binstrumento->eliminar_relacion_instrumentos();  
// TIPO 1
     for ($i=0; $i < $_REQUEST["totinst1"]; $i++) {
        $Binstrumento->c_instrumento    = $_REQUEST["cod_inst".$i];
        $Binstrumento->relacionado      = $nulo;
        $Binstrumento->relacion1        = $_REQUEST["cod_inst_ppal".$i];
        $Binstrumento->relacion2        = $_REQUEST["cod_inst_otro".$i];
        $Binstrumento->relacion1_nombre = $_REQUEST["nom_inst_ppal".$i];
        $Binstrumento->relacion2_nombre = $_REQUEST["nom_inst_otro".$i];
        $error=$Binstrumento->actualizar_relacion_instrumentos();  
     }
// TIPO 2
     for ($i=0; $i < $_REQUEST["totinst2"]; $i++) {
        $Binstrumento->c_instrumento    = $_REQUEST["cod_inst".($_REQUEST["totinst1"]+$i)];
        $Binstrumento->relacionado      = $_REQUEST["rel_inst".($_REQUEST["totinst1"]+$i)];
        $Binstrumento->relacion1        = $nulo;
        $Binstrumento->relacion2        = $_REQUEST["EspecificaInstrumento".($_REQUEST["totinst1"]+$i)];
        $Binstrumento->relacion1_nombre = $nulo;
        $Binstrumento->relacion2_nombre = $nulo;
        $error=$Binstrumento->actualizar_relacion_instrumentos();  
     }
// GRABAR BENEFICIARIO
     $Bbeneficiario=new Beneficiario_Preinversion($BaseDatos->conector);
     $Bbeneficiario->c_institucion      = $xinstitucion;
     $Bbeneficiario->c_preinversion     = $xpreinversion;
     $Bbeneficiario->ano                = $xnano;        
     $error=$Bbeneficiario->eliminar_beneficiarios();
     // recorrer el vector que esta cargado con la nueva grilla que trae 5 campos
     // tengo que tener el dato de cuantos grupos de beneficiarios tengo , esto esta en variable b_cangsel
     //
     
     $vector=explode('|', $xbeneficiarios);

     foreach($vector as $fila){
	$valores=explode('[',$fila);
	$grupo_id=$valores[0];	
	$hombres_id=$valores[1];	
	$mujeres_id=$valores[2];	
	$ambos_id=$valores[3];	
	$indirectos_id=$valores[4];	
	if($grupo_id!=0){
		$Bbeneficiario->actualizar_beneficiarios_ng($grupo_id, $hombres_id,$mujeres_id,$ambos_id,$indirectos_id);
	}

    } 
      

  echo "<script language=\"JavaScript\">";
  echo "var nn='".$xpreinversion."';";
  echo "parent.document.form1.npreinversion.value=nn;";
  echo 'parent.document.form1.action="pagina1_1_4.php";';
  echo 'parent.document.form1.target="_self";';
  echo 'parent.document.form1.submit();'; 
  echo "</script>";  	        	        	
  @mysql_close($BaseDatos->conector);
    
} 
/**
 * Transforma y devuelve un array
 */
function array_recibe($url_array) { 
     $tmp = stripslashes($url_array); 
     $tmp = urldecode($tmp); 
     $tmp = unserialize($tmp); 

    return $tmp; 
} 
?>
