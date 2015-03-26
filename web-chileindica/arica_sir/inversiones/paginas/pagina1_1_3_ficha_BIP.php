<?
   $restriccion_modulo='01010103';
   $nivel_pagina=2;
   include_once("../../include/seguridad.php");
   include_once("../../include/clases_login.php");   
   include_once("../../login/valida_usuario.php"); 
   require_once("../../../ws_client/class/chileindicaSOAP.php");

// ###################
// CONSUMO WEB SERVICE
// ###################
   $service = new chileindicaSOAP();
   $codigoBip = $_REQUEST[ncodigo];
   $dvBip = $_REQUEST[ndigito];
   $result = $service->ConsultaBip($codigoBip,$dvBip);      
   $xanoconsulta=$_REQUEST["anoconsulta"];

// #####################
// TRATAMIENTO RESULTADO
// #####################
   if ($result[BipData][NombreIdi]=="null") {
      echo "<script>alert('El Código solicitado no existe');parent.ocultar_mensaje();parent.mensaje_codigo_bip();</script>";exit;
   } else {
      echo "<script>parent.configurar_cb_tipo_codigo(1);</script>";
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
   echo "<script>parent.document.form1.SNivelUbicacion.value=".$xnivelubicacion.";parent.configurar_ut();</script>";
   if ($result[BipData][CodigoComuna]!="null") {
      $query = "SELECT ID_COMUNA_INE, N_COMUNA FROM COMUNA WHERE ID_COMUNA_INE='".$result[BipData][CodigoComuna]."'";
      $dataset  = mysql_query($query, $BaseDatos->conector); 
      $record = @mysql_fetch_row($dataset);
      echo "<script>parent.document.form1.cod_ut.value='".$record[0]."';parent.document.form1.nom_ut.value='".$record[1]."';</script>";
   } else {
      if ($result[BipData][CodigoProvincia]!="null") {
         $query = "SELECT ID_PROVINCIA_INE, N_PROVINCIA FROM PROVINCIA WHERE ID_PROVINCIA_INE='".$result[BipData][CodigoProvincia]."'";        
         $dataset  = mysql_query($query, $BaseDatos->conector); 
         $record = @mysql_fetch_row($dataset);
         echo "<script>parent.document.form1.cod_ut.value='".$record[0]."';parent.document.form1.nom_ut.value='".$record[1]."';</script>";
      } else {
         echo "<script>parent.document.form1.cod_ut.value='';parent.document.form1.nom_ut.value='';</script>";
      }
   }
   echo "<script>parent.asigna_datos_ut();parent.ocultar_mensaje();</script>";
 
// ##############
// PROGRAMACIONES
// ##############
   $xdescripcion="";$xxano=0;
   if ($result[BipData][Programaciones][DatosProgramacion]) {
      $xdescripcion=$result[BipData][Programaciones][DatosProgramacion][Descripcion];
   } else {
      foreach ($result[BipData][Programaciones] as $value) {
         if ($value[DatosProgramacion][AnoMoneda]>$xxano) {
            $xdescripcion=$value[DatosProgramacion][Descripcion];
         }
      }   	
   }	
   $xdescripcion=ereg_replace("[^/( )A-Za-z0-9_áéíóúÁÉÍÓÚ,.]", "", $xdescripcion);

// ###########
// SOLICITUDES
// ###########
   $canti_anos=0;
   if ($result[BipData][Solicitudes][DatosSolicitudes]) {
      if ($result[BipData][Solicitudes][DatosSolicitudes][AnoSolicitud]==$xanoconsulta) {$canti_anos=1;}
   } else {
      foreach ($result[BipData][Solicitudes] as $value) {
         if ($value[DatosSolicitudes][AnoSolicitud]==$xanoconsulta) {
            $canti_anos=$canti_anos+1;
         }
      }
   }  
   if ($canti_anos>1) { 
   //	exit;
   } // MÁS DE UNA ETAPA
   $total_proyecto=0;$total_solicitado=0;
   if ($result[BipData][Solicitudes][DatosSolicitudes]) {
      if ($result[BipData][Solicitudes][DatosSolicitudes][AnoSolicitud]==$xanoconsulta) {

         /*Agregar fecha inicio*/
         if(isset($result[BipData][Solicitudes][DatosSolicitudes][fechaInicioEstimada])){
            echo "<script>parent.document.form1.fecha_inicio.value='".str_replace('-','/',$result[BipData][Solicitudes][DatosSolicitudes][fechaInicioEstimada])."';</script>";
         }

         echo "<script>parent.document.form1.cb_etapa.disabled=false;parent.document.form1.cb_etapa.value=".$result[BipData][Solicitudes][DatosSolicitudes][CodigoEtapaPostula].";</script>";          
         if ($result[BipData][Solicitudes][DatosSolicitudes][Rate]!="null") {
            $query = "SELECT C_RATE FROM RATE WHERE N_RATE='".$result[BipData][Solicitudes][DatosSolicitudes][Rate]."'";
            $dataset  = mysql_query($query, $BaseDatos->conector); 
            $record = @mysql_fetch_row($dataset);
            echo "<script>parent.document.form1.cb_rate.disabled=false;parent.document.form1.cb_rate.value=".$record[0].";</script>";
         }   
         if ($result[BipData][Solicitudes][DatosSolicitudes][EstadoSituacion]=="ARRASTRE") {$situa=14;} else {$situa=16;} 
         echo "<script>parent.document.form1.cb_situacion.disabled=false;parent.document.form1.cb_situacion.value=".$situa.";</script>";          
         if ($result[BipData][Solicitudes][MontosSolicitados][MontoSolicitadoAnoPesos]) {
            $total_solicitado=$total_solicitado+($result[BipData][Solicitudes][MontosSolicitados][MontoSolicitadoAnoPesos]*1000);
         } else {
            echo "montos: ".$result[BipData][Solicitudes][MontosSolicitados][CodigoInstitucionFinanciera]." - ".$result[BipData][Solicitudes][MontosSolicitados][MontoSolicitadoAnoPesos]."<br>";
            $total_solicitado=$total_solicitado+($result[BipData][Solicitudes][MontosSolicitados][MontoSolicitadoAnoPesos]*1000);
         }
      } else {
         if ($result[BipData][Solicitudes][MontosSolicitados][MontoSolicitadoAnoPesos]) {
            $total_proyecto=$total_proyecto+($result[BipData][Solicitudes][MontosSolicitados][MontoSolicitadoAnoPesos]*1000);
         } else {
            $total_proyecto=$total_proyecto+($result[BipData][Solicitudes][MontoSolicitadoAnoPesos]*1000);
         }
      }
   } else {
      foreach ($result[BipData][Solicitudes] as $value) {
         if ($value[DatosSolicitudes][AnoSolicitud]==$xanoconsulta) {

            /*Agregar fecha inicio*/
            if(isset($value[DatosSolicitudes][fechaInicioEstimada])){

               echo "<script>parent.document.form1.fecha_inicio.value='".str_replace('-','/',$value[DatosSolicitudes][fechaInicioEstimada])."';</script>";
            }

            echo "<script>parent.document.form1.cb_etapa.disabled=false;parent.document.form1.cb_etapa.value=".$value[DatosSolicitudes][CodigoEtapaPostula].";</script>";
            if ($value[DatosSolicitudes][Rate]!="null") {
               $query = "SELECT C_RATE FROM RATE WHERE N_RATE='".$value[DatosSolicitudes][Rate]."'";
               $dataset  = mysql_query($query, $BaseDatos->conector); 
               $record = @mysql_fetch_row($dataset);
               echo "<script>parent.document.form1.cb_rate.disabled=false;parent.document.form1.cb_rate.value=".$record[0].";</script>";
            }   
            if ($value[DatosSolicitudes][EstadoSituacion]=="ARRASTRE") {$situa=14;} else {$situa=16;} 
            echo "<script>parent.document.form1.cb_situacion.disabled=false;parent.document.form1.cb_situacion.value=".$situa.";</script>";          
            
            if ($value[MontosSolicitados][MontoSolicitadoAnoPesos]) {
               $total_solicitado=$total_solicitado+($value[MontosSolicitados][MontoSolicitadoAnoPesos]*1000);
            } else {
               foreach ($value[MontosSolicitados] as $value1) {
                  echo "montos: ".$value1[CodigoInstitucionFinanciera]." - ".$value1[MontoSolicitadoAnoPesos]."<br>";
                  $total_solicitado=$total_solicitado+($value1[MontoSolicitadoAnoPesos]*1000);
               }
            }
         } else {
            if ($value[MontosSolicitados][MontoSolicitadoAnoPesos]) {
               $total_proyecto=$total_proyecto+($value[MontosSolicitados][MontoSolicitadoAnoPesos]*1000);
            } else {
               foreach ($value[MontosSolicitados] as $value1) {
                  $total_proyecto=$total_proyecto+($value1[MontoSolicitadoAnoPesos]*1000);
               }   
            }
         }
      }
   }  
   unset($value);

   /**
    * Montos extra
    */

   if($result[BipData][Solicitudes][MontosSolicitados][MontoSolicitadoAnoPesos]){
      $montos_solicitados  = $result[BipData][Solicitudes][MontosSolicitados][MontoSolicitadoAnoPesos]*1000;
      $costo_total         = $result[BipData][Solicitudes][MontosSolicitados][CostoTotal]*1000;
      $gasto_acumulado     = $result[BipData][Solicitudes][MontosSolicitados][GastoAcumulado]*1000;
   }else{
      foreach ($result[BipData][Solicitudes] as $key => $value) {
         $montos_solicitados  = $result[BipData][Solicitudes][$key][MontosSolicitados][MontoSolicitadoAnoPesos]*1000;
         $costo_total         = $result[BipData][Solicitudes][$key][MontosSolicitados][CostoTotal]*1000;
         $gasto_acumulado     = $result[BipData][Solicitudes][$key][MontosSolicitados][GastoAcumulado]*1000;
      }
   }

   /**
    * Nueva forma de obtener los valores que corresponden a los datos del año de consulta
    * 
    */
   if ($result[BipData][Solicitudes][DatosSolicitudes]) {
      // Iniciativa con una solicitud
      if ($result[BipData][Solicitudes][DatosSolicitudes][AnoSolicitud]==$xanoconsulta) {
         $costo_total         =  $result[BipData][Solicitudes][MontosSolicitados][CostoTotal] * 1000;
         $montos_solicitados  =  $result[BipData][Solicitudes][MontosSolicitados][GastoAcumulado] * 1000;
         $total_solicitado    =  $result[BipData][Solicitudes][MontosSolicitados][MontoSolicitadoAnoPesos] * 1000;
         $gasto_acumulado     =  $result[BipData][Solicitudes][MontosSolicitados][MontoSaldoPorInvertirPesos] * 1000;
      }
   }else{
      // Iniciativa con varias solicitudes
      foreach ($result[BipData][Solicitudes] as $value) {
         if ($value[DatosSolicitudes][AnoSolicitud]==$xanoconsulta) {
            $costo_total         =  $value[MontosSolicitados][CostoTotal] * 1000;
            $montos_solicitados  =  $value[MontosSolicitados][GastoAcumulado] * 1000;
            $total_solicitado    =  $value[MontosSolicitados][MontoSolicitadoAnoPesos] * 1000;
            $gasto_acumulado     =  $value[MontosSolicitados][MontoSaldoPorInvertirPesos] * 1000;
         }
      }
   }

   /* Fin nueva forma */



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
   echo "parent.document.form1.e_codigo.value='".$codigoBip."';parent.document.form1.cb_tipo_codigo.value=1;";
   echo "parent.document.form1.e_nombre.value='".$result[BipData][NombreIdi]."';";
   echo "parent.document.form1.cb_item_presupuestario.value='".$coditem."';";
   echo "parent.document.form1.e_producto.value='".$xxproducto."';";
   echo "parent.actualizar_beneficiario(0,'Población General',1);";
   echo "parent.obj_Beneficiario.setCellText('".$result[BipData][BeneficiariosHombres]."',1,0);";
   echo "parent.obj_Beneficiario.setCellText('".$result[BipData][BeneficiariosMujeres]."',2,0);";
//   echo "parent.obj_Beneficiario.setCellText('".$result[BipData][BenefAmbosSexos]."',3,0);";
   if ($xarea_influencia==1) {echo "parent.document.getElementById('influencia1').checked=true;";}
   if ($xarea_influencia==2) {echo "parent.document.getElementById('influencia2').checked=true;";}
   if ($xarea_influencia==3) {echo "parent.document.getElementById('influencia3').checked=true;";}
   if ($xarea_influencia==4) {echo "parent.document.getElementById('influencia4').checked=true;";}
   if ($xarea_influencia==5) {echo "parent.document.getElementById('influencia5').checked=true;";}
   echo "parent.document.form1.Observaciones.value='".$xdescripcion."';";
   echo "parent.configurar_ficha();parent.actualizar_fuente(0,'No Definida','');";

   echo "parent.obj3.setCellText('".$costo_total."',1,0);";
   echo "parent.obj3.setCellText('".$montos_solicitados."',2,0);";
   echo "parent.obj3.setCellText('".$total_solicitado."',3,0);";
   echo "parent.obj3.setCellText('".$gasto_acumulado."',4,0);";
   




   echo "parent.ocultar_mensaje();";
   //echo "console.log('".json_encode($result)."');";

   echo "parent.document.form1.coordenadas.value='".$coordenadas."';";
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


   echo "</script>";

// ##############
// FIN DE PROCESO
// ##############
  

?>