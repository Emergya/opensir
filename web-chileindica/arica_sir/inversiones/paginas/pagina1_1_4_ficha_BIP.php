<?
   // $restriccion_modulo='01010104';
   $nivel_pagina=2;
   include_once("../../include/seguridad.php");
   include_once("../../include/clases_login.php");   
   include_once("../../include/clases_preinversion.php");
   // include_once("../../login/valida_usuario.php"); 
   require_once("../../../ws_client/class/chileindicaSOAP.php");

// ###################
// CONSUMO WEB SERVICE
// ###################
   $service    = new chileindicaSOAP();
   $codigoBip  = $_REQUEST['e_codigo_bip'];
   $dvBip      = $_REQUEST['e_dv_codigo_bip'];
   $cetapa     =  $_REQUEST['etapa_iniciativa'];
   $result     = $service->ConsultaBip($codigoBip,$dvBip); 

   //error_log(print_r($result,true));
   //print_r($result);
   //echo json_encode($result);
   //die();
   
   
   
   $xanoconsulta=$_REQUEST["bip_anio"];
   $resp       = array('estado' => true,'mensaje'=>''); // array devuelto en formato json

   $etapa_iniciativa = 0;
   $mensaje_existe = "El Código ingresado ya se encuentra registrado en una iniciativa y registra la misma etapa";
   $mensaje_no_existe = "El Código ingresado no se encuentra en los registros con la etapa indicada";

   $codigoComuna=$result[BipData][CodigoComuna];
   if ( $result[BipData][Solicitudes][DatosSolicitudes][CodigoEtapaPostula] ) {
      $var_etapa  =  $result[BipData][Solicitudes][DatosSolicitudes][CodigoEtapaPostula];
      $var_ano    =  $result[BipData][Solicitudes][DatosSolicitudes][AnoSolicitud];
      // echo "<script>alert('".$var_etapa." - ".$cetapa."');</script>";
      // echo "<script>alert('".$var_ano." - ".$xanoconsulta."');</script>";
      if ( $var_etapa == $cetapa && $var_ano == $xanoconsulta) {
         $etapa_iniciativa = $result[BipData][Solicitudes][DatosSolicitudes][CodigoEtapaPostula];
	 $rate=$result[BipData][Solicitudes][DatosSolicitudes][Rate];
         $monto_solicitado=$result[BipData][Solicitudes][MontosSolicitados][MontoSolicitadoAnoPesos];
         $saldo_por_invertir=$result[BipData][Solicitudes][MontosSolicitados][MontoSaldoPorInvertirPesos];
         $costo_total=$result[BipData][Solicitudes][MontosSolicitados][CostoTotal];

      }
   }else{
      foreach ($result[BipData][Solicitudes] as  $solicitud) {
         if ( $solicitud[DatosSolicitudes][CodigoEtapaPostula] == $cetapa && $solicitud[DatosSolicitudes][AnoSolicitud] == $xanoconsulta ) {
            $etapa_iniciativa = $cetapa;
	    $rate=$solicitud[DatosSolicitudes][Rate];
	    $monto_solicitado=$solicitud[MontosSolicitados][MontoSolicitadoAnoPesos];
	    $saldo_por_invertir=$solicitud[MontosSolicitados][MontoSaldoPorInvertirPesos];
	    $costo_total=$solicitud[MontosSolicitados][CostoTotal];

         }else{
         }
      }
   }

   // Dar error en caso de no encontrar la iniciativa con el codigo y etapa indicado
   if ( $etapa_iniciativa == 0 ) {
      $resp['estado'] = false;
      $resp['mensaje'] = $mensaje_no_existe;
      echo json_encode($resp);
      exit;
   }
   //echo json_encode($result);
   //die();
// ##########################################
// VERIFICACION COD. BIP EN LA BASE DE DATOS
// ##########################################
   $preinversion=new Preinversion($BaseDatos->conector);

 //     echo json_encode($resp);
//	exit;
   if ($preinversion->existe_codigo_bip($codigoBip, $etapa_iniciativa, $xanoconsulta)) {
      $resp['estado'] = false;
      $resp['mensaje'] = $mensaje_existe;
      echo json_encode($resp);
      exit;
   }

//exit;
// #####################
// TRATAMIENTO RESULTADO
// #####################
   if ($result[BipData][NombreIdi]=="null") {
      //echo "<script>alert('El Código solicitado no existe');parent.ocultar_mensaje();parent.mensaje_codigo_bip();</script>";exit;
   }
   $xsector=0;
   if ($result[BipData][CodigoSector]==8) {$xsector=1;}
   if ($result[BipData][CodigoSector]==14) {$xsector=2;}
   if ($result[BipData][CodigoSector]==12) {$xsector=3;}
   if ($result[BipData][CodigoSector]==11) {$xsector=4;}
   if ($result[BipData][CodigoSector]==5) {$xsector=5;}
   if ($result[BipData][CodigoSector]==4) {$xsector=6;}
   if ($result[BipData][CodigoSector]==13) {$xsector=7;}
   if ($result[BipData][CodigoSector]==3) {$xsector=8;}
   if ($result[BipData][CodigoSector]==15) {$xsector=9;}
   if ($result[BipData][CodigoSector]==2) {$xsector=10;}
   if ($result[BipData][CodigoSector]==10) {$xsector=11;}
   if ($result[BipData][CodigoSector]==1) {$xsector=12;}
   if ($result[BipData][CodigoSector]==6) {$xsector=13;}
   if ($result[BipData][CodigoSector]==9) {$xsector=14;}
   if ($result[BipData][CodigoSector]==7) {$xsector=15;}
   if ($result[BipData][CodigoTipologia]==791) {$coditem="31.02";}
   if ($result[BipData][CodigoTipologia]==792) {$coditem="31.03";}
   if ($result[BipData][CodigoTipologia]==793) {$coditem="31.01";}
   $xxproducto="";
   if ($result[BipData][Magnitudes][Valor]) {
      $xxproducto=$result[BipData][Magnitudes][Valor]." ".$result[BipData][Magnitudes][Nombre];
   } else {
      for ($r=0;$r<count($result[BipData][Magnitudes]);$r++) {
         $xxproducto .=$result[BipData][Magnitudes][$r][Valor]." ".$result[BipData][Magnitudes][$r][Nombre]." / ";
      }
   }
   $xarea_influencia=0;
   if ($result[BipData][AreaInfluencia]=="INTERNACIONAL") {$xarea_influencia=1;}
   if ($result[BipData][AreaInfluencia]=="NACIONAL") {$xarea_influencia=2;}
   if ($result[BipData][AreaInfluencia]=="REGIONAL") {$xarea_influencia=3;}
   if ($result[BipData][AreaInfluencia]=="PROVINCIAL") {$xarea_influencia=4;}
   if ($result[BipData][AreaInfluencia]=="COMUNAL") {$xarea_influencia=5;}
   $xnivelubicacion=0;
   if ($result[BipData][CodigoComuna]!="null") {
      $xnivelubicacion=3;
   } else {
      if ($result[BipData][CodigoProvincia]!="null") {
         $xnivelubicacion=2;
      } else {
         $xnivelubicacion=1;
      }
   }
//MN 

	$resp['codigoBIP']=$codigoBip;
	$resp['codigoComuna']=$codigoComuna;
	$resp['etapa']=$etapa_iniciativa;
	$resp['nivelUbicacion']=$xnivelubicacion;
	$resp['nombreIdi']=utf8_decode($result[BipData][NombreIdi]);
	$resp['nombreInstitucionEtapa']=$result[BipData][NombreInstitucionEtapa];
	$resp['areaInfluencia']=$result[BipData][AreaInfluencia];
	$resp['xareaInfluencia']=$xarea_influencia;
	$resp['codigoSectorBIP']=$result[BipData][CodigoSector];
	$resp['codigoSectorBIPChileindica']=$xsector;

	$resp['tipo']='BIP';// 1 si es codigo BIP
	$resp['unidadTecnica']='';
	$resp['viaFinanciamiento']='1';
	$resp['fechaInicio']='';
	$resp['fechaTermino']='';
	$resp['etapa']=$etapa_iniciativa;

	$resp['rate']=$rate;
	$xdescripcion=$result[BipData][Programaciones][DatosProgramacion][Descripcion];
	$xdescripcion=ereg_replace("[^/( )A-Za-z0-9_áéíóúÁÉÍÓÚ,.-]", "", $xdescripcion);

//	$resp['observaciones']=utf8_decode($result[BipData][Programaciones][DatosProgramacion][Descripcion]);
	$resp['observaciones']=utf8_decode($xdescripcion);

	$resp['justificacion']='';
	$resp['solicitado']=$monto_solicitado*1000;
	$resp['saldo']=$saldo_por_invertir*1000;
	$resp['costoTotal']=($costo_total+$saldo_por_invertir)*1000;
	$resp['descBeneficiarios']='Descripcion Beneficiarios';
	$resp['ubicacion']='';
	$resp['coordenadas']='';
	$resp['instrumento']='';
	$resp['instrumentoPrincipal']='';
	
   

   //echo "<script>parent.document.form1.SNivelUbicacion.value=".$xnivelubicacion.";parent.configurar_ut();</script>";
   
   if ($result[BipData][CodigoComuna]!="null") {
      $query = "SELECT ID_COMUNA_INE, N_COMUNA FROM COMUNA WHERE ID_COMUNA_INE='".$result[BipData][CodigoComuna]."'";
      $dataset  = mysql_query($query, $BaseDatos->conector); 
      $record = @mysql_fetch_row($dataset);
      //echo "<script>parent.document.form1.cod_ut.value='".$record[0]."';
      //parent.document.form1.nom_ut.value='".$record[1]."';</script>";
      
      $resp['cod_ut']=$record[0];
      $resp['nom_ut']=$record[1];
      
   } else {
      if ($result[BipData][CodigoProvincia]!="null") {
         $query = "SELECT ID_PROVINCIA_INE, N_PROVINCIA FROM PROVINCIA WHERE ID_PROVINCIA_INE='".$result[BipData][CodigoProvincia]."'";        
         $dataset  = mysql_query($query, $BaseDatos->conector); 
         $record = @mysql_fetch_row($dataset);
         //echo "<script>parent.document.form1.cod_ut.value='".$record[0]."';
         //parent.document.form1.nom_ut.value='".$record[1]."';</script>";
         $resp['cod_ut']=$record[0];
         $resp['nom_ut']=$record[1];
      } else {
         //echo "<script>parent.document.form1.cod_ut.value='';
         //parent.document.form1.nom_ut.value='';</script>";
         $resp['cod_ut']='';
         $resp['nom_ut']='';
      }
   }
   
   
   
   $resp["beneficiarios_hombres"]=$result[BipData][BeneficiariosHombres];
   $resp["beneficiarios_mujeres"]=$result[BipData][BeneficiariosMujeres];
   
   
   echo json_encode($resp);
   die();
   echo "<script>parent.asigna_datos_ut();parent.ocultar_mensaje();</script>";
 
// ##############
// PROGRAMACIONES
// ##############
   $xdescripcion="";$xxano=0;
   if ($result[BipData][Programaciones][DatosProgramacion]) {
      $xdescripcion=$result[BipData][Programaciones][DatosProgramacion][Descripcion];
   } else {
      foreach ($result[BipData][Programaciones] as $value) {
         if ($value[DatosProgramacion][CodigoEtapa] == $etapa_iniciativa) {
            $xdescripcion=$value[DatosProgramacion][Descripcion];
         }
      }   	
   }	
   $xdescripcion=ereg_replace("[^/( )A-Za-z0-9_áéíóúÁÉÍÓÚ,.]", "", $xdescripcion);

// ###########
// SOLICITUDES
// ###########
   $anos_anteriores = $result[BipData][Solicitudes][MontosSolicitados][GastoAcumulado];
   $canti_anos=0;
   $array_anos = array();
   if ($result[BipData][Solicitudes][DatosSolicitudes]) {
      if ($result[BipData][Solicitudes][DatosSolicitudes][AnoSolicitud]==$xanoconsulta) {$canti_anos=1;}
   } else {
      foreach ($result[BipData][Solicitudes] as $value) {
         if ($value[DatosSolicitudes][AnoSolicitud]==$xanoconsulta) {
            $canti_anos=$canti_anos+1;
         }
      }
   }  


   // Array de años disponibles
   if ($result[BipData][Solicitudes][AnoSolicitud]) {
         $array_anos[] = $value[DatosSolicitudes][AnoSolicitud];
   }else{
      foreach ($result[BipData][Solicitudes] as $value) {
         $array_anos[] = $value[DatosSolicitudes][AnoSolicitud];
      }
   }
   $ano_consulta = $array_anos[0];
   asort($array_anos);

   if ($canti_anos>1) { 
   //	exit;
   } // MÁS DE UNA ETAPA
   $total_proyecto=0;
   $total_solicitado=0;
   
   if ($result[BipData][Solicitudes][DatosSolicitudes]) {

      if ($result[BipData][Solicitudes][DatosSolicitudes][AnoSolicitud]==$xanoconsulta && $result[BipData][Solicitudes][DatosSolicitudes][CodigoEtapaPostula] == $etapa_iniciativa) {
         echo "<script>parent.document.form1.cb_etapa.disabled=false;parent.document.form1.cb_etapa.value=".$result[BipData][Solicitudes][DatosSolicitudes][CodigoEtapaPostula].";</script>";          
         
         if ($result[BipData][Solicitudes][DatosSolicitudes][Rate]!="null") {
            $nombre_rate = $result[BipData][Solicitudes][DatosSolicitudes][Rate];
            echo "<script>parent.document.form1.cb_rate.disabled=false;parent.document.form1.cb_rate.value='".$nombre_rate."';</script>";
         }   
         
         if ($result[BipData][Solicitudes][DatosSolicitudes][EstadoSituacion]=="ARRASTRE") {$situa=14;} else {$situa=16;} 

         if ($result[BipData][Solicitudes][MontosSolicitados][MontoSolicitadoAnoPesos]) {
            $total_solicitado    =  $result[BipData][Solicitudes][MontosSolicitados][MontoSolicitadoAnoPesos]*1000;
            $total_proyecto      =  $result[BipData][Solicitudes][MontosSolicitados][CostoTotal]*1000;
            $saldo_por_invertir  =  $result[BipData][Solicitudes][MontosSolicitados][MontoSaldoPorInvertirPesos]*1000;

         } else {
            echo "montos: ".$result[BipData][Solicitudes][MontosSolicitados][CodigoInstitucionFinanciera]." - ".$result[BipData][Solicitudes][MontosSolicitados][MontoSolicitadoAnoPesos]."<br>";
            $total_solicitado    =  $result[BipData][Solicitudes][MontosSolicitados][MontoSolicitadoAnoPesos]*1000;
            $total_proyecto      =  $result[BipData][Solicitudes][MontosSolicitados][CostoTotal]*1000;
            $saldo_por_invertir  =  $result[BipData][Solicitudes][MontosSolicitados][MontoSaldoPorInvertirPesos]*1000;
         }
      }
   } else {

      // Multiples iniciativas
      foreach ($result[BipData][Solicitudes] as $value) {         
         if ($value[DatosSolicitudes][AnoSolicitud]==$xanoconsulta && $value[DatosSolicitudes][CodigoEtapaPostula] == $etapa_iniciativa ) {
            echo "<script>parent.document.form1.cb_etapa.disabled=false;parent.document.form1.cb_etapa.value=".$value[DatosSolicitudes][CodigoEtapaPostula].";</script>";
            
            if ($value[DatosSolicitudes][Rate]!="null") {
               $nombre_rate = $value[DatosSolicitudes][Rate];
               echo "<script>parent.document.form1.cb_rate.disabled=false;parent.document.form1.cb_rate.value='".$nombre_rate."';</script>";
            }   
            
            if ($value[DatosSolicitudes][EstadoSituacion]=="ARRASTRE") {$situa=14;} else {$situa=16;} 
//            echo "<script>parent.document.form1.cb_situacion.disabled=false;parent.document.form1.cb_situacion.value=".$situa.";</script>";          
            if ($value[MontosSolicitados][MontoSolicitadoAnoPesos]) {
               $total_solicitado    =  $value[MontosSolicitados][MontoSolicitadoAnoPesos]*1000;
               $total_proyecto      =  $value[MontosSolicitados][CostoTotal]*1000;
               $saldo_por_invertir  =  $value[MontosSolicitados][MontoSaldoPorInvertirPesos]*1000;
            } else {
               foreach ($value[MontosSolicitados] as $value1) {
                  echo "montos: ".$value1[CodigoInstitucionFinanciera]." - ".$value1[MontoSolicitadoAnoPesos]."<br>";
                  $total_solicitado    =  $value1[MontoSolicitadoAnoPesos]*1000;
                  $total_proyecto      =  $value1[CostoTotal]*1000;
                  $saldo_por_invertir  =  $value1[MontoSaldoPorInvertirPesos]*1000;
               }
            }
         }
      }
   }  
   unset($value);
   if ($result[BipData][Solicitudes][DatosSolicitudes][Rate] ) {
         // $queryRate = "SELECT C_RATE FROM RATE WHERE N_RATE='".$result[BipData][Solicitudes][DatosSolicitudes][Rate]."'";
         // $dataset  = mysql_query($queryRate, $BaseDatos->conector); 
         // $recordRate = @mysql_fetch_row($dataset);
         $nombre_rate = $result[BipData][Solicitudes][DatosSolicitudes][Rate];
         echo "<script>parent.document.form1.cb_rate.value='".$nombre_rate."';</script>";
   }
   /**
    * Datos geolocalizacion
    */

   /* Se añade para GEOREFERENCIACION */
   $forma="";  // se inicializa la forma a vacio
   /* Fin GEOREFERENCIACION */


   if($result[BipData][Georeferenciacion][coordenadas][ejeX]){
      $coordenadas .= 'Punto '.$result[BipData][Georeferenciacion][coordenadas][orden].': ('.$result[BipData][Georeferenciacion][coordenadas][ejeX].', '.$result[BipData][Georeferenciacion][coordenadas][ejeY].')\n';
      $coordenadasarray = array(
                                 'codigoOCG' => $result[BipData][Georeferenciacion][codigoOCG],
                                 'coordenadas' => array(
                                     0 => array(
                                    'ejeX' => $result[BipData][Georeferenciacion][coordenadas][ejeX],
                                    'ejeY' => $result[BipData][Georeferenciacion][coordenadas][ejeY],
                                    'nombre' => $result[BipData][Georeferenciacion][coordenadas][nombre],
                                    'orden' => $result[BipData][Georeferenciacion][coordenadas][orden]
                                    )),
                                 'orden' => $result[BipData][Georeferenciacion][coordenadas][orden], 
                                 'elementoGrafico' => $result[BipData][Georeferenciacion][elementoGrafico]
                                 );
      /* Se añade para GEOREFERENCIACION */
      $zona=(string)$result[BipData][Georeferenciacion][codigoOCG];
      $x=$result[BipData][Georeferenciacion][coordenadas][ejeX];
      $y=$result[BipData][Georeferenciacion][coordenadas][ejeY]; 
      $fjson=array(array("zona"=>$zona,"x"=>$x,"y"=>$y));  
      $forma=json_encode($fjson);  // format json -> es un string
      /* Fin GEOREFERENCIACION */

   }else{

      /* Se añade para GEOREFERENCIACION */
      $zona=(string)$result[BipData][Georeferenciacion][codigoOCG];            
      $fjson=array();
      /* Fin GEOREFERENCIACION */

      foreach ($result[BipData][Georeferenciacion][coordenadas] as $value) {
         $coordenadas .= 'Punto '.$value[orden].': ('.$value[ejeX].', '.$value[ejeY].')\n';
         $coordenadasarray_[] = array(
                                    'ejeX' => $value[ejeX],
                                    'ejeY' => $value[ejeY],
                                    'nombre' => $value[nombre],
                                    'orden' => $value[orden]
                                    );
         /* Se añade para GEOREFERENCIACION */
         $fjson[]=array("zona"=>$zona,"x"=>$value[ejeX],"y"=>$value[ejeY]);
         /* Fin GEOREFERENCIACION */
      }
      /* Se añade para GEOREFERENCIACION */
      if(count($fjson)>0) $forma=json_encode($fjson);  // format json -> es un string 
      /* Fin GEOREFERENCIACION */

            $coordenadasarray = array(
                  'codigoOCG' => $result[BipData][Georeferenciacion][codigoOCG],
                  'coordenadas' => $coordenadasarray_,
                  'orden' => $result[BipData][Georeferenciacion][coordenadas][orden], 
                  'elementoGrafico' => $result[BipData][Georeferenciacion][elementoGrafico]
                  );
   }
// #########################
// ASIGNACION DE LOS VALORES
// #########################

  /* Se añade para GEOREFERENCIACION */ 
  echo "<script language=\"JavaScript\" src=\"../../../js/jquery-1.8.1.min.js\"></script>";
  echo "<script type=\"text/javascript\" src=\"../../../js/jquery.cookie.js\"></script>";
  /* Fin GEOREFERENCIACION */

   echo "<script>";
   echo "parent.document.form1.cb_sectorinversion.value=".$xsector.";";
   echo "parent.document.form1.e_codigo.value='".$codigoBip."';";
   echo "parent.document.form1.e_codigo_dv.value='".$dvBip."';";
   echo "parent.document.form1.e_codigo.readOnly= true;";
   echo "parent.document.form1.e_tipo.value='BIP';";
   echo "parent.document.form1.cb_etapa.disabled=true;";
   echo "parent.document.form1.e_nombre.value='".$result[BipData][NombreIdi]."';";
   //echo "parent.document.form1.cb_rate.value=SDFGHJ;";
   //echo "parent.document.form1.cb_item_presupuestario.value='".$coditem."';";
   echo "parent.document.form1.e_producto.value='".$xxproducto."';";
   echo "parent.actualizar_beneficiario(0,'Población General',1);";
   echo "parent.obj_Beneficiario.setCellText('".$result[BipData][BeneficiariosHombres]."',1,0);";
   echo "parent.obj_Beneficiario.setCellText('".$result[BipData][BeneficiariosMujeres]."',2,0);";
   //echo "parent.obj_Beneficiario.setCellText('".$result[BipData][BenefAmbosSexos]."',3,0);";
   if ($xarea_influencia==1) {echo "parent.document.getElementById('influencia1').checked=true;";}
   if ($xarea_influencia==2) {echo "parent.document.getElementById('influencia2').checked=true;";}
   if ($xarea_influencia==3) {echo "parent.document.getElementById('influencia3').checked=true;";}
   if ($xarea_influencia==4) {echo "parent.document.getElementById('influencia4').checked=true;";}
   if ($xarea_influencia==5) {echo "parent.document.getElementById('influencia5').checked=true;";}

   echo "parent.document.form1.Observaciones.value='".$xdescripcion."';";
   echo "parent.document.form1.e_costo_total.value='". ($total_solicitado + $saldo_por_invertir)."';";
   
   echo "parent.document.form1.e_solicitado.value='".$total_solicitado."';";
   echo "parent.document.form1.e_saldo.value='". $saldo_por_invertir ."';";
   echo "parent.ocultar_mensaje();";

   $tmp = serialize($coordenadasarray);
   $tmp = urlencode($tmp);
   /* Se modifica para GEOREFERENCIACION */   
   //$forma='[{"zona":"32718","x":638149,"y":5857111},{"zona":"32718","x":638649,"y":5887112}]';
   if(!empty($forma))
   {
      echo "var info=JSON.parse($.cookie(\"infoGeo\"));"; // se rescata el info general   
      echo "info.forma=JSON.parse('" . $forma . "');";        
      echo "$.cookie('infoGeo', JSON.stringify(info) , { path: '/'  });";           
      echo "parent.document.form1.contenedor_array.value='".$forma."';";
   }   
   /* Fin se modifica para GEOREFERENCIACION */

   //echo "parent.document.form1.contenedor_array.value='".$tmp."';";
   echo "parent.document.form1.coordenadas.value='".$coordenadas."';";
   echo "parent.document.form1.e_anterior.value='".$anos_anteriores."';";
   echo "</script>";
   
   //echo "<script>alert($result[BipData][Solicitudes][DatosSolicitudes][Rate]);</script>"
   /*if ($recordRate[0] == '') {
      echo "<script>alert('Es nada');</script>";
   }*/
   
   //echo "<script>parent.document.form1.cb_rate.value=$var;</script>";
/*
   $query = sprintf("SELECT CODIGO 
                        FROM PREINVERSION_ARI 
                        WHERE C_TIPO_CODIGO = 1 AND 
                              CODIGO <> 'S/C' AND 
                              CODIGO <> 'sin codigo' 
                        Limit 4 "); //primero vuelta, i = 1 , segementacion = mas de 200
            $exe = mysql_query($query, $BaseDatos->conector) or die(mysql_error());
            $row = mysql_fetch_array($exe);
            while ($row = mysql_fetch_array($exe)) {
               echo "<script>alert($row[CODIGO]);</script>";
            }
*/
?>
