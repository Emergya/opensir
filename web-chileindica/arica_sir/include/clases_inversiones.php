<?php

/*********************************************************************

    Entidad Inversiones

**********************************************************************/

//*************************************************************

// INVERSION
   
Class Inversion {
   var $conector;
   var $mensaje_error;
   var $nroregistros;
   var $ano;
   var $c_institucion;
   var $n_institucion="";
   var $c_preinversion;
   var $c_ficha;
   var $c_sector;
   var $n_sector="";
   var $nombre="";
   var $c_clasificador_presupuestario="";
   var $n_clasificador_presupuestario="";
   var $codigo="";
   var $tipo_codigo;
   var $fecha_inicio;
   var $fecha_termino;
   var $unidad_tecnica;
   var $nunidad_tecnica;
   var $rate;
   var $n_rate;
   var $c_etapa_idi;
   var $n_etapa_idi="";
   var $c_estado_proyecto;
   var $beneficiarios="";
   var $descripcion="";
   var $costo_ebi;
   var $costo_core;
   var $costo_total;
   var $gastado_anos_anteriores;
   var $solicitado;
   var $saldo_proximo_ano;
   var $saldo_proximos_anos;
   var $asignado;
   var $total_programado;
   var $contratado;
   var $pagado;
   var $saldo;
   var $arrastre;
   var $avance_fisico;
   var $avance_financiero;
   var $ejecutivo="";
   var $usuario;
   var $filaseleccionada;
   var $footer;
   var $ntipo_codigo="";
   var $fecha_registro;
   var $asignacion_disponible;
   var $saldo_por_asignar;
   var $c_estado;
   var $c_preinversion_propir;
   var $ut1="";
   var $ut2="";
   var $id_clasificadores;
   var $id_clasificadores1;
   var $n_clasificadores;
   var $sigfe_clasificadores;
   var $msolicitado;
   var $masignado;
   var $msaldo1;
   var $mrequerido;
   var $msaldo2;
   var $marequerir;
   var $etapa_sigfe;
   var $id_sigfe="";
   var $c_unidad_demandante;
   var $sianticipo;


   function get_admin_parametros($nombre){
      $query = sprintf("SELECT valor FROM ADMIN_PARAMETROS WHERE nombre='$nombre'");
      $dataset  = mysql_query($query, $this->conector);

      while ($record = mysql_fetch_row($dataset)) {
         $result[] = $record[0];
      }
      return $result;
   }

  function carga_georeferencia(){
      /*
      $query_coo  ="
		SELECT 
			ANO, 
			C_INSTITUCION, 
			C_PREINVERSION, 
			C_FICHA, 
			ORDEN, 
			EJEX, 
			EJEY 
		FROM 
			INVERSION_GEOREF_COOR 
		WHERE 
			ANO= $this->ano AND
			C_INSTITUCION= $this->c_institucion AND
			C_PREINVERSION= $this->c_preinversion AND 
			C_FICHA= $this->c_ficha
		ORDER BY
			ANO, 
			C_INSTITUCION, 
			C_PREINVERSION, 
			C_FICHA, 
			ORDEN";
      $dataset_coo  = mysql_query($query_coo, $this->conector); 
      $dataset_num = mysql_num_rows($dataset_coo);
      for($x = 0; $x < $dataset_num; $x++) { 
          $tabla[]= mysql_fetch_array($dataset_coo); 
      }
      foreach ($tabla as $key => $value) {
        $coo .= 'Punto '.$tabla[$key][ORDEN].': ('.$tabla[$key][EJEX].', '.$tabla[$key][EJEY].')\n';
      }
      return $coo;
      */
      /* Insertar para la georeferenciacion */
      $query  ="SELECT CODIGO_OGC FROM INVERSION_GEOREF WHERE ANO= $this->ano AND C_INSTITUCION= $this->c_institucion AND C_PREINVERSION= $this->c_preinversion LIMIT 1";                        
      $dataset  = mysql_query($query, $this->conector); 
      if(mysql_num_rows($dataset)>0) $R=mysql_fetch_array($dataset);
      $zona=(string)$R['CODIGO_OGC'];
      $forma=array();
      /* Fin nueva georeferenciacion */

      $query_coo  ="SELECT ANO, C_INSTITUCION, C_PREINVERSION, ORDEN, EJEX, EJEY FROM INVERSION_GEOREF_COOR WHERE  ANO= $this->ano AND C_INSTITUCION= $this->c_institucion AND C_PREINVERSION= $this->c_preinversion AND C_FICHA= $this->c_ficha ORDER BY ORDEN";      
      // die(print_r($query_coo));
      $dataset_coo  = mysql_query($query_coo, $this->conector); 
      $dataset_num = mysql_num_rows($dataset_coo);
      for($x = 0; $x < $dataset_num; $x++) { 
          $tabla[]= mysql_fetch_array($dataset_coo); 
      }
      foreach ($tabla as $key => $value) {
        $coo .= 'Punto '.$tabla[$key][ORDEN].': ('.$tabla[$key][EJEX].', '.$tabla[$key][EJEY].')\n';

        /*se añade para georeferenciacion */
        $forma[]=array("zona"=>(string)$zona,"x"=>(float)$tabla[$key][EJEX],"y"=>(float)$tabla[$key][EJEY]);
        /* fin se añade para georeferenciacion*/
      }

      /* Insertar para la georeferenciacion */
      $res=array();
      $res[0]=$coo;
      if(count($forma)==0) $res[1]="";
      else $res[1]=json_encode($forma);
      return $res;
      /* Fin Insertar para la georeferenciacion */
  }
 
   
   function ingresa_georeferencia($contenedor_array){
    // elimina la georeferenciacion actual por si tiene
    $d="DELETE FROM INVERSION_GEOREF WHERE ANO= $this->ano AND C_INSTITUCION= $this->c_institucion AND C_PREINVERSION= $this->c_preinversion AND C_FICHA= $this->c_ficha";
    mysql_query($d, $this->conector);
    $d="DELETE FROM INVERSION_GEOREF_COOR  WHERE ANO= $this->ano AND C_INSTITUCION= $this->c_institucion AND C_PREINVERSION= $this->c_preinversion AND C_FICHA= $this->c_ficha";
    mysql_query($d, $this->conector);
    // se inserta la nueva georeferenciacion

    $data=json_decode($contenedor_array);    
    if(count($data)>0) // si hay datos
    {          
       $grafico=0; // un punto
       if(count($data)>1) $grafico=1; // una forma
       $query_georef_ari = "INSERT INTO INVERSION_GEOREF (ANO, C_INSTITUCION, C_PREINVERSION, ELEMENTO_GRAFICO, CODIGO_OGC) VALUES ('".$this->ano."', '".$this->c_institucion."', '".$this->c_preinversion."', '". $grafico."', '".$data[0]->zona."')";   
       $orden=1;
       foreach ($data as $val) {       
           $query_georef_coor_ari    = "INSERT INTO INVERSION_GEOREF_COOR (ANO, C_INSTITUCION, C_PREINVERSION,C_FICHA,ORDEN, EJEX, EJEY) VALUES ('".$this->ano."', '".$this->c_institucion."', '".$this->c_preinversion. "', '" . $this->c_ficha  . "', '".$orden."', '".$val->x."', '".$val->y."')";
           $dataset_georef_coor_ari  = mysql_query($query_georef_coor_ari, $this->conector);
           $orden++;
       }
       $dataset_georef_ari  = mysql_query($query_georef_ari, $this->conector);    
    }
    return true;

   }
  

   function Inversion($conector){ 
      $this->conector =$conector;
      $this->mensaje_error='';
      $this->estado=0;
      $this->nroregistros=0;
   }
   
   function consultar_codigo_bip() {
      $query  = " SELECT CODIGO, C_TIPO_CODIGO FROM INVERSION ";
      $query .= " WHERE ANO= ".$this->ano." AND C_INSTITUCION=".$this->c_institucion." AND C_PREINVERSION=".$this->c_preinversion;
      $query .= " AND C_FICHA=".$this->c_ficha." AND C_TIPO_CODIGO=1";
      $dataset  = mysql_query($query, $this->conector); 
      $record = @mysql_fetch_row($dataset);
      if ($record[1]==1) {return $record[0];} else {return -1;}
   }

   function codigo_institucion_sigfe() {
      $query  = " SELECT ID_SIGFE FROM INSTITUCION WHERE C_INSTITUCION=".$this->c_institucion;
      $dataset  = mysql_query($query, $this->conector); 
      $record = @mysql_fetch_row($dataset);
      return $record[0];                
   }

   function id_sigfe_bip() {
      $query  = " SELECT ID_SIGFE FROM INVERSION_ANTECEDENTES";
      $query .= " WHERE ANO= ".$this->ano." AND C_INSTITUCION=".$this->c_institucion." AND C_PREINVERSION=".$this->c_preinversion;
      $query .= " AND C_FICHA=".$this->c_ficha;
      $dataset  = mysql_query($query, $this->conector); 
      $record = @mysql_fetch_row($dataset);
      return $record[0];                
   }

   function mail_usuario($xusuario) {
      $query  = " SELECT MAIL FROM ADMIN_USUARIO_PERFIL WHERE C_USUARIO=".$xusuario;
      $dataset  = mysql_query($query, $this->conector); 
      $record = @mysql_fetch_row($dataset);
      return $record[0];                
   }

   function valor_ipc($xnano) {
      $query  = " SELECT IPC FROM VALOR_IPC WHERE ANO= ".$xnano;
      $dataset  = mysql_query($query, $this->conector); 
      $record = @mysql_fetch_row($dataset);
      return $record[0];          
   }

   function calcular_totales_montos_y_fuentes_base() {
      $query  = " SELECT IPC FROM VALOR_IPC WHERE ANO= ".($this->ano-1);
      $dataset  = mysql_query($query, $this->conector); 
      $record = @mysql_fetch_row($dataset);
      $xipc=$record[0];
      $query  = " SELECT COSTO_EBI, (G1+G2+G3+G4+G5+G6+G7+G8+G9+G10+G11+G12), COSTO_TOTAL, (GASTADO_ANOS_ANTERIORES+SOLICITADO+SALDO_PROXIMO_ANO),";
      $query .= " ASIGNADO";
      $query .= " FROM INVERSION_FINANCIAMIENTO ";
      $query .= " WHERE ANO= ".$this->ano." AND C_INSTITUCION=".$this->c_institucion." AND C_PREINVERSION=".$this->c_preinversion;
      $query .= " AND C_FICHA=".$this->c_ficha." AND C_CLASIFICADOR_PRESUPUESTARIO='".$this->c_clasificador_presupuestario."'";
      $dataset  = mysql_query($query, $this->conector); 
      $record = @mysql_fetch_row($dataset);
      $costo_ebi_actualizado=$record[0]+($record[0]*$xipc/100);
      $costo_ebi_actualizado_mas=round(($costo_ebi_actualizado+($costo_ebi_actualizado*10/100)),0);
      $costo_ebi_actualizado=round($costo_ebi_actualizado,0);
      $pagado=$record[1];
      $saldo_proximos_anos=($record[2]-$record[3]);
      $asignacion_disponible=($record[4]-$record[1]);
      $query  = " UPDATE INVERSION_FINANCIAMIENTO SET ";
      $query .= " PAGADO=".$pagado;
      $query .= " , SALDO_PROXIMOS_ANOS=".$saldo_proximos_anos;
      $query .= " , ASIGNACION_DISPONIBLE=".$asignacion_disponible;
      $query .= " WHERE ANO= ".$this->ano." AND C_INSTITUCION=".$this->c_institucion." AND C_PREINVERSION=".$this->c_preinversion;
      $query .= " AND C_FICHA=".$this->c_ficha." AND C_CLASIFICADOR_PRESUPUESTARIO='".$this->c_clasificador_presupuestario."'";
      $dataset  = mysql_query($query, $this->conector); 
      // ACTUALIZA DATOS INVERSION RELACIONADA
      $query  = " UPDATE INVERSION SET ";
      $query .= " PAGADO=(";
      $query .= " SELECT SUM(PAGADO) FROM INVERSION_FINANCIAMIENTO ";
      $query .= " WHERE INVERSION.C_INSTITUCION=INVERSION_FINANCIAMIENTO.C_INSTITUCION AND ";
      $query .= " INVERSION.C_PREINVERSION=INVERSION_FINANCIAMIENTO.C_PREINVERSION AND ";
      $query .= " INVERSION.C_FICHA=INVERSION_FINANCIAMIENTO.C_FICHA AND INVERSION.ANO=INVERSION_FINANCIAMIENTO.ANO)";
      $query .= " ,SALDO_PROXIMOS_ANOS=(";
      $query .= " SELECT SUM(SALDO_PROXIMOS_ANOS) FROM INVERSION_FINANCIAMIENTO ";
      $query .= " WHERE INVERSION.C_INSTITUCION=INVERSION_FINANCIAMIENTO.C_INSTITUCION AND ";
      $query .= " INVERSION.C_PREINVERSION=INVERSION_FINANCIAMIENTO.C_PREINVERSION AND ";
      $query .= " INVERSION.C_FICHA=INVERSION_FINANCIAMIENTO.C_FICHA AND INVERSION.ANO=INVERSION_FINANCIAMIENTO.ANO)";
      $query .= " ,ASIGNACION_DISPONIBLE=(";
      $query .= " SELECT SUM(ASIGNACION_DISPONIBLE) FROM INVERSION_FINANCIAMIENTO ";
      $query .= " WHERE INVERSION.C_INSTITUCION=INVERSION_FINANCIAMIENTO.C_INSTITUCION AND ";
      $query .= " INVERSION.C_PREINVERSION=INVERSION_FINANCIAMIENTO.C_PREINVERSION AND ";
      $query .= " INVERSION.C_FICHA=INVERSION_FINANCIAMIENTO.C_FICHA AND INVERSION.ANO=INVERSION_FINANCIAMIENTO.ANO)";
      $query .= " WHERE ANO= ".$this->ano." AND C_INSTITUCION=".$this->c_institucion." AND C_PREINVERSION=".$this->c_preinversion;
      $query .= " AND C_FICHA=".$this->c_ficha;
      $dataset  = mysql_query($query, $this->conector); 
      $query  = " UPDATE INVERSION SET AVANCE_FINANCIERO=PAGADO*100/SOLICITADO ";
      $query .= " WHERE ANO= ".$this->ano." AND C_INSTITUCION=".$this->c_institucion." AND C_PREINVERSION=".$this->c_preinversion;
      $query .= " AND C_FICHA=".$this->c_ficha;
      $dataset  = mysql_query($query, $this->conector); 
      
      // FALTA TOTAL_PROGRAMADO, SALDO Y ARRASTRE
   }

   function diferencia_monto() {
      $query  = " SELECT C_ESTADO FROM INVERSION";
      $query .= " WHERE ANO= ".$this->ano." AND C_INSTITUCION=".$this->c_institucion." AND C_PREINVERSION=".$this->c_preinversion;
      $query .= " AND C_FICHA=".$this->c_ficha;
      $dataset  = mysql_query($query, $this->conector); 
      $this->nroregistros = mysql_num_rows($dataset);
      $record = @mysql_fetch_row($dataset);
      if ($record[0]!="0") {$aa=1;} else {$aa=0;}
      return $aa;      
   }

   function diferencia_garantia2() {
      $query  = " SELECT COUNT(*) FROM INVERSION_GARANTIA";
      $query .= " WHERE ANO= ".$this->ano." AND C_INSTITUCION=".$this->c_institucion." AND C_PREINVERSION=".$this->c_preinversion;
      $query .= " AND C_FICHA=".$this->c_ficha." AND C_ESTADO=1";
      $dataset  = mysql_query($query, $this->conector); 
      $record = @mysql_fetch_row($dataset);
      if ($record[0]>0) {$aa=1;} else {$aa=0;}
      return $aa;      
   }

   function consultar_tipo_seguimiento() {
      $query  = " SELECT C_TIPO_SEGUIMIENTO FROM INVERSION_CONFIGURACION ";
      $query .= " WHERE ANO= ".$this->ano." AND C_INSTITUCION=".$this->c_institucion." AND C_PREINVERSION=".$this->c_preinversion;
      $query .= " AND C_FICHA=".$this->c_ficha;
      $dataset  = mysql_query($query, $this->conector); 
      $record = @mysql_fetch_row($dataset);
      return $record[0];      
   }

   function actualizar_tipo_seguimiento($xseguimiento) {
      $query  = " REPLACE INTO INVERSION_CONFIGURACION (ANO, C_INSTITUCION, C_PREINVERSION, C_FICHA, C_TIPO_SEGUIMIENTO) VALUES(";
      $query .= "'".$this->ano."','".$this->c_institucion."','".$this->c_preinversion."','".$this->c_ficha."','";
      $query .= $xseguimiento."')"; 
      $dataset  = mysql_query($query, $this->conector);
      if (mysql_errno($this->conector) != 0) { 
         $this->mensaje_error="No se ha podido Actualizar la Base de Datos, mensaje de error de Mysql es: " . mysql_error($this->conector).$query; 
      } else {$this->mensaje_error='';}   
      return $this->mensaje_error;
   }

   function diferencia_garantia3() {
      $query  = " SELECT COUNT(*) FROM INVERSION_GARANTIA";
      $query .= " WHERE ANO= ".$this->ano." AND C_INSTITUCION=".$this->c_institucion." AND C_PREINVERSION=".$this->c_preinversion;
      $query .= " AND C_FICHA=".$this->c_ficha." AND C_ESTADO=2";
      $dataset  = mysql_query($query, $this->conector); 
      $record = @mysql_fetch_row($dataset);
      if ($record[0]>0) {$aa=1;} else {$aa=0;}
      return $aa;      
   }

   function diferencia_garantia4() {
      $query  = " SELECT COUNT(*) FROM INVERSION_GARANTIA";
      $query .= " WHERE ANO= ".$this->ano." AND C_INSTITUCION=".$this->c_institucion." AND C_PREINVERSION=".$this->c_preinversion;
      $query .= " AND C_FICHA=".$this->c_ficha." AND C_ESTADO=3";
      $dataset  = mysql_query($query, $this->conector); 
      $record = @mysql_fetch_row($dataset);
      if ($record[0]>0) {$aa=1;} else {$aa=0;}
      return $aa;      
   }

   function diferencia_garantia5() {
      $ffecha=Date('Ymd');
      $query  = " SELECT COUNT(*) FROM INVERSION_GARANTIA";
      $query .= " WHERE ANO= ".$this->ano." AND C_INSTITUCION=".$this->c_institucion." AND C_PREINVERSION=".$this->c_preinversion;
      $query .= " AND C_FICHA=".$this->c_ficha." AND C_ESTADO IN (1,2,3,4) AND DATEDIFF(FECHA_VENCIMIENTO,".$ffecha.")>15";
      $query .= " AND DATEDIFF(FECHA_VENCIMIENTO,".$ffecha.")<=30";
      $dataset  = mysql_query($query, $this->conector); 
      $record = @mysql_fetch_row($dataset);
      if ($record[0]>0) {$aa=1;} else {$aa=0;}
      return $aa;      
   }

   function diferencia_garantia6() {
      $ffecha=Date('Ymd');
      $query  = " SELECT COUNT(*) FROM INVERSION_GARANTIA";
      $query .= " WHERE ANO= ".$this->ano." AND C_INSTITUCION=".$this->c_institucion." AND C_PREINVERSION=".$this->c_preinversion;
      $query .= " AND C_FICHA=".$this->c_ficha." AND C_ESTADO IN (1,2,3,4) AND DATEDIFF(FECHA_VENCIMIENTO,".$ffecha.")>=0";
      $query .= " AND DATEDIFF(FECHA_VENCIMIENTO,".$ffecha.")<=15";
      $dataset  = mysql_query($query, $this->conector); 
      $record = @mysql_fetch_row($dataset);
      if ($record[0]>0) {$aa=1;} else {$aa=0;}
      return $aa;      
   }

   function diferencia_garantia7() {
      $ffecha=Date('Ymd');
      $query  = " SELECT COUNT(*) FROM INVERSION_GARANTIA";
      $query .= " WHERE ANO= ".$this->ano." AND C_INSTITUCION=".$this->c_institucion." AND C_PREINVERSION=".$this->c_preinversion;
      $query .= " AND C_FICHA=".$this->c_ficha." AND C_ESTADO IN (1,2,3,4) AND DATEDIFF(FECHA_VENCIMIENTO,".$ffecha.")<0";
      $dataset  = mysql_query($query, $this->conector); 
      $record = @mysql_fetch_row($dataset);
      if ($record[0]>0) {$aa=1;} else {$aa=0;}
      return $aa;      
   }

   function calcular_valor_maximo() {
      $query  = " SELECT MAX(C_FICHA) FROM INVERSION";
      $query .= " WHERE ANO= ".$this->ano." AND C_INSTITUCION=".$this->c_institucion." AND C_PREINVERSION=".$this->c_preinversion;
      $dataset  = mysql_query($query, $this->conector); 
      $this->nroregistros = mysql_num_rows($dataset);
      $record = @mysql_fetch_row($dataset);
      return $record[0]+1;
   }

   function actualizar_antecedentes_generales() {  
      $query  = " UPDATE INVERSION SET C_SECTOR='".$this->c_sector."', NOMBRE='".$this->nombre."', ";
      $query .= " FECHA_INICIO='".$this->fecha_inicio."', FECHA_TERMINO='".$this->fecha_termino."', ";
      $query .= " RATE='".$this->rate."', C_ETAPA_IDI='".$this->c_etapa_idi."', CODIGO='".$this->codigo."', ";
      $query .= " DESCRIPCION='".$this->descripcion."', AVANCE_FISICO='".$this->avance_fisico."', AVANCE_FINANCIERO='".$this->avance_financiero."', ";
      $query .= " FECHA_REGISTRO='".$this->fecha_registro."'";
      $query .= " WHERE ANO='".$this->ano."' AND C_INSTITUCION='".$this->c_institucion."' AND C_PREINVERSION='".$this->c_preinversion."' AND C_FICHA='".$this->c_ficha."'";
      $dataset  = mysql_query($query, $this->conector); 
      if (mysql_errno($this->conector) != 0) { 
         $this->mensaje_error="No se ha podido Actualizar la Base de Datos, mensaje de error de Mysql es: " . mysql_error($this->conector).$query; 
      } else {$this->mensaje_error='';}   
      return $this->mensaje_error;
   }   

   function actualizar_texto_beneficiarios() {  
      $query  = " UPDATE INVERSION SET BENEFICIARIOS='".$this->beneficiarios."'";
      $query .= " WHERE ANO='".$this->ano."' AND C_INSTITUCION='".$this->c_institucion."' AND C_PREINVERSION='".$this->c_preinversion."'";
      $query .= " AND C_FICHA='".$this->c_ficha."'";
      $dataset  = mysql_query($query, $this->conector); 
      if (mysql_errno($this->conector) != 0) { 
         $this->mensaje_error="No se ha podido Actualizar la Base de Datos, mensaje de error de Mysql es: " . mysql_error($this->conector).$query; 
      } else {$this->mensaje_error='';}   
      return $this->mensaje_error;
   }   

   function actualizar_montos_totales() {  
      $query  = " UPDATE INVERSION SET COSTO_EBI='".$this->costo_ebi."', COSTO_CORE='".$this->costo_core."', COSTO_TOTAL='".$this->costo_total."', GASTADO_ANOS_ANTERIORES='".$this->gastado_anos_anteriores."', ";
      $query .= " SOLICITADO='".$this->solicitado."', SALDO_PROXIMO_ANO='".$this->saldo_proximo_ano."', SALDO_PROXIMOS_ANOS='".$this->saldo_proximos_anos."', ";
      $query .= " ASIGNADO='".$this->asignado."', TOTAL_PROGRAMADO='".$this->total_programado."', CONTRATADO='".$this->contratado."',PAGADO='".$this->pagado."', ";
      $query .= " SALDO='".$this->saldo."', ARRASTRE='".$this->arrastre."', ASIGNACION_DISPONIBLE='".$this->asignacion_disponible."', ";
      $query .= " SALDO_POR_ASIGNAR='".$this->saldo_por_asignar."', C_ESTADO='".$this->c_estado."', AVANCE_FINANCIERO='".$this->avance_financiero."'";
      $query .= " WHERE ANO='".$this->ano."' AND C_INSTITUCION='".$this->c_institucion."' AND C_PREINVERSION='".$this->c_preinversion."'";
      $query .= " AND C_FICHA='".$this->c_ficha."'";
      $dataset  = mysql_query($query, $this->conector); 
      if (mysql_errno($this->conector) != 0) { 
         $this->mensaje_error="No se ha podido Actualizar la Base de Datos, mensaje de error de Mysql es: " . mysql_error($this->conector).$query; 
      } else {$this->mensaje_error='';}   
      return $this->mensaje_error;
   }   

   function cargar_antecedentes_generales() {
      $query  = " SELECT A.ANO, A.C_INSTITUCION, B.N_INSTITUCION, A.C_PREINVERSION, A.C_FICHA, A.C_SECTOR, A.NOMBRE, A.C_CLASIFICADOR_PRESUPUESTARIO, ";
      $query .= " A.CODIGO, A.C_TIPO_CODIGO, DATE_FORMAT(A.FECHA_INICIO,'%d/%m/%Y'),DATE_FORMAT(A.FECHA_TERMINO,'%d/%m/%Y'), ";
      $query .= " A.UNIDAD_TECNICA, A.RATE, A.C_ETAPA_IDI, A.DESCRIPCION, A.AVANCE_FISICO, A.AVANCE_FINANCIERO, C.N_CLASIFICADOR, ";
      $query .= " D.N_SECTOR, E.N_RATE, F.N_ETAPA_IDI, G.N_INSTITUCION ";
      $query .= " FROM INVERSION A, INSTITUCION B, TCLASIFICADOR_PRESUPUESTARIO C, SECTOR D, RATE E, ETAPA_IDI F, INSTITUCION G ";
      $query .= " WHERE A.C_INSTITUCION=B.C_INSTITUCION AND A.C_CLASIFICADOR_PRESUPUESTARIO=C.C_CLASIFICADOR AND A.ANO=C.ANIO AND (A.C_INSTITUCION=C.INSTITUCION OR C.INSTITUCION=0) AND ";
      $query .= " A.C_SECTOR=D.C_SECTOR AND A.RATE=E.C_RATE AND A.C_ETAPA_IDI=F.C_ETAPA_IDI AND A.UNIDAD_TECNICA=G.C_INSTITUCION AND ";
      $query .= " A.ANO=".$this->ano." AND A.C_INSTITUCION=".$this->c_institucion." AND A.C_PREINVERSION=".$this->c_preinversion." AND A.C_FICHA=".$this->c_ficha;
      $dataset= mysql_query($query, $this->conector); 
      $this->nroregistros = mysql_num_rows($dataset);
      $record = mysql_fetch_row($dataset);
      $this->ano=$record[0];
      $this->c_institucion=$record[1];
      $this->n_institucion=$record[2];
      $this->c_preinversion=$record[3];
      $this->c_ficha=$record[4];
      $this->c_sector=$record[5];
      $this->nombre=$record[6];
      $this->c_clasificador_presupuestario=$record[7];
      $this->codigo=$record[8];
      $this->tipo_codigo=$record[9];
      $this->fecha_inicio=$record[10];
      $this->fecha_termino=$record[11];
      $this->unidad_tecnica=$record[12];
      $this->rate=$record[13];
      $this->c_etapa_idi=$record[14];
      $this->descripcion=$record[15];
      $this->avance_fisico=$record[16];
      $this->avance_financiero=$record[17];
      $this->n_clasificador_presupuestario=$record[18];
      $this->n_sector=$record[19];
      $this->n_rate=$record[20];
      $this->n_etapa_idi=$record[21];
      $this->nunidad_tecnica=$record[22];
      return $this->nroregistros;
   }

   function cargar_antecedentes_requerimiento() {
      $query  = " SELECT B.C_ETAPA_SIGFE, C.UBICACION_ESPECIFICA, C.UBICACION_ESPECIFICA_NOMBRE, D.ID_SIGFE, D.C_UNIDAD_DEMANDANTE, X.N_UNIDAD_DEMANDANTE ";
      $query .= " FROM ETAPA_IDI B, INVERSION A ";
      $query .= " LEFT JOIN UBICACION_TERRITORIAL_INICIATIVA C ON (A.ANO=C.ANO AND A.C_INSTITUCION=C.C_INSTITUCION AND A.C_PREINVERSION=C.C_INICIATIVA AND A.C_FICHA=C.C_FICHA)";
      $query .= " LEFT JOIN INVERSION_ANTECEDENTES D ON (A.ANO=D.ANO AND A.C_INSTITUCION=D.C_INSTITUCION AND A.C_PREINVERSION=D.C_PREINVERSION AND A.C_FICHA=D.C_FICHA)";      
      $query .= " LEFT JOIN UNIDAD_DEMANDANTE X ON (D.C_UNIDAD_DEMANDANTE=X.C_UNIDAD_DEMANDANTE)";      
      $query .= " WHERE A.C_ETAPA_IDI=B.C_ETAPA_IDI AND A.ANO=".$this->ano." AND A.C_INSTITUCION=".$this->c_institucion." AND A.C_PREINVERSION=".$this->c_preinversion;
      $query .= " AND A.C_FICHA=".$this->c_ficha;
      $dataset= mysql_query($query, $this->conector); 
      $this->nroregistros = mysql_num_rows($dataset);
      $record = mysql_fetch_row($dataset);
      $this->etapa_sigfe=$record[0];
      $this->ut1=$record[1];
      $this->ut2=$record[2];
      $this->id_sigfe=$record[3];
      $this->c_unidad_demandante=$record[4].' - '.$record[5];
      return $this->nroregistros;
   }

   function actualizar_codigo_sigfe() {
      $query  = " SELECT COUNT(*) FROM INVERSION_ANTECEDENTES ";
      $query .= " WHERE ANO=".$this->ano." AND C_INSTITUCION=".$this->c_institucion." AND C_PREINVERSION=".$this->c_preinversion;
      $query .= " AND C_FICHA=".$this->c_ficha;
      $dataset= mysql_query($query, $this->conector);
      $record = mysql_fetch_row($dataset);
      if ($record[0]==0) {
         $query  = " INSERT INTO INVERSION_ANTECEDENTES VALUES('";
         $query .= $this->ano."','".$this->c_institucion."','".$this->c_preinversion."','".$this->c_ficha."','".$this->id_sigfe."','')";
      } else {
         $query  = " UPDATE INVERSION_ANTECEDENTES SET ID_SIGFE='".$this->id_sigfe."' ";
         $query .= " WHERE ANO=".$this->ano." AND C_INSTITUCION=".$this->c_institucion." AND C_PREINVERSION=".$this->c_preinversion;
         $query .= " AND C_FICHA=".$this->c_ficha;
      }   
      $dataset= mysql_query($query, $this->conector);
   }

   function actualizar_codigo_sigfe_clasificador($xcodigo,$xano,$xcodigo1) {
      $query  = " UPDATE TCLASIFICADOR_PRESUPUESTARIO SET ID_SIGFE='".$xcodigo1."'";
      $query .= " WHERE ANIO=".$xano." AND C_CLASIFICADOR='".$xcodigo."'";
      $dataset= mysql_query($query, $this->conector);
   }

   function cargar_antecedentes_requerimiento1($xrequerimiento,$xestado) {
      $fila=0;$sianticipo=0;
      if ($xestado<3 || $xestado==8) {
         $query  = " SELECT A.C_CLASIFICADOR_PRESUPUESTARIO, B.N_CLASIFICADOR, B.ID_SIGFE, A.SOLICITADO, A.ASIGNADO, (A.SOLICITADO-A.ASIGNADO) ";
         $query .= " FROM TCLASIFICADOR_PRESUPUESTARIO B, INVERSION_FINANCIAMIENTO A ";
         $query .= " WHERE A.ANO=".$this->ano." AND A.C_INSTITUCION=".$this->c_institucion." AND A.C_PREINVERSION=".$this->c_preinversion;
         $query .= " AND A.C_FICHA=".$this->c_ficha." AND A.C_CLASIFICADOR_PRESUPUESTARIO=B.C_CLASIFICADOR";
         $query .= " AND A.ANO=B.ANIO AND (A.C_INSTITUCION=B.INSTITUCION OR B.INSTITUCION=0) AND (A.C_FUENTE_FINANCIAMIENTO LIKE '03%' OR A.C_FUENTE_FINANCIAMIENTO LIKE '02%')"; 
         $dataset= mysql_query($query, $this->conector);
         $this->nroregistros = mysql_num_rows($dataset);$fila=1;
         while ($record = mysql_fetch_row($dataset)) { 
             $this->id_clasificadores1[$fila]=$record[0];
             $this->id_clasificadores[$fila]=ereg_replace("[^/( )A-Za-z0-9_áéíóúÁÉÍÓÚ]", "", $record[0]);
             $this->n_clasificadores[$fila]=$record[1];
             $this->sigfe_clasificadores[$fila]=$record[2];
             $this->msolicitado[$fila]=$record[3];
             $this->masignado[$fila]=$record[4];
             $this->msaldo1[$fila]=$record[5];
             $fila=$fila+1;
         }
         $query  = "SELECT SUM(A.ANTICIPO-A.ANTICIPO_DEVUELTO) FROM INVERSION_CONTRATO A ";
         $query .= " WHERE A.ANO=".$this->ano." AND A.C_INSTITUCION=".$this->c_institucion." AND A.C_PREINVERSION=".$this->c_preinversion;
         $query .= " AND A.C_FICHA=".$this->c_ficha." AND A.C_ESTADO=2";
         $dataset= mysql_query($query, $this->conector);
         $record = mysql_fetch_row($dataset);
         if ($record[0]>0) {
            $this->sianticipo=1;
            $query1  = " SELECT C_CLASIFICADOR, N_CLASIFICADOR, ID_SIGFE ";
            $query1 .= " FROM TCLASIFICADOR_PRESUPUESTARIO ";
            $query1 .= " WHERE ANIO=".$this->ano." AND C_CLASIFICADOR='32.06.001'";
            $dataset1= mysql_query($query1, $this->conector);
       $record1 = mysql_fetch_row($dataset1);
            $this->nroregistros=$this->nroregistros+1;
             $this->id_clasificadores1[$fila]=$record1[0];
             $this->id_clasificadores[$fila]=ereg_replace("[^/( )A-Za-z0-9_áéíóúÁÉÍÓÚ]", "", $record1[0]);
             $this->n_clasificadores[$fila]=$record1[1];
             $this->sigfe_clasificadores[$fila]=$record1[2];            
             $this->msolicitado[$fila]=$record[0];
             $this->masignado[$fila]=$record[0];
         }
         for ($i=1; $i<=$this->nroregistros; $i++) {
            $query1  = " SELECT SUM(B.MONTO) FROM INVERSION_REQUERIMIENTO A, INVERSION_REQUERIMIENTO_MONTOS B";
            $query1 .= " WHERE A.ANO=".$this->ano." AND A.C_INSTITUCION=".$this->c_institucion." AND A.C_PREINVERSION=".$this->c_preinversion;
            $query1 .= " AND A.C_FICHA=".$this->c_ficha." AND B.C_CLASIFICADOR='".$this->id_clasificadores1[$i]."'";
            $query1 .= " AND A.C_ESTADO=4 AND A.ANO=B.ANO AND A.C_INSTITUCION=B.C_INSTITUCION AND A.C_PREINVERSION=B.C_PREINVERSION";
            $query1 .= " AND A.C_FICHA=B.C_FICHA AND A.C_REQUERIMIENTO=B.C_REQUERIMIENTO ";
            $dataset1= mysql_query($query1, $this->conector);
       $record1 = mysql_fetch_row($dataset1);
            $this->mrequerido[$i]=$record1[0];
            if ($xrequerimiento>0) {
               $query1  = " SELECT MONTO FROM INVERSION_REQUERIMIENTO A, INVERSION_REQUERIMIENTO_MONTOS B";
               $query1 .= " WHERE A.ANO=".$this->ano." AND A.C_INSTITUCION=".$this->c_institucion." AND A.C_PREINVERSION=".$this->c_preinversion;
               $query1 .= " AND A.C_FICHA=".$this->c_ficha." AND B.C_CLASIFICADOR='".$this->id_clasificadores1[$i]."'";
               $query1 .= " AND A.ANO=B.ANO AND A.C_INSTITUCION=B.C_INSTITUCION AND A.C_PREINVERSION=B.C_PREINVERSION";
               $query1 .= " AND A.C_FICHA=B.C_FICHA AND A.C_REQUERIMIENTO=B.C_REQUERIMIENTO AND A.C_REQUERIMIENTO=".$xrequerimiento;
               $dataset1= mysql_query($query1, $this->conector);
          $record1 = mysql_fetch_row($dataset1);
               $this->marequerir[$i]=$record1[0];
            }
         }
      } else {
         $query  = " SELECT A.C_CLASIFICADOR, B.N_CLASIFICADOR, B.ID_SIGFE,A.SOLICITADO, A.ASIGNADO, A.SALDO1, A.REQUERIDO, A.SALDO2, A.MONTO ";
         $query .= " FROM INVERSION_REQUERIMIENTO_MONTOS A, TCLASIFICADOR_PRESUPUESTARIO B ";
         $query .= " WHERE A.ANO=".$this->ano." AND A.C_INSTITUCION=".$this->c_institucion." AND A.C_PREINVERSION=".$this->c_preinversion;
         $query .= " AND A.C_FICHA=".$this->c_ficha." AND A.C_REQUERIMIENTO=".$xrequerimiento." AND A.C_CLASIFICADOR=B.C_CLASIFICADOR";
         $query .= " AND A.ANO=B.ANIO AND (A.C_INSTITUCION=B.INSTITUCION OR B.INSTITUCION=0)"; 
         $dataset= mysql_query($query, $this->conector);
         $this->nroregistros = mysql_num_rows($dataset);$fila=1;
         while ($record = mysql_fetch_row($dataset)) { 
             $this->id_clasificadores1[$fila]=$record[0];
            if ($record[0]=="32.06.001") {$this->sianticipo=1;}
             $this->id_clasificadores[$fila]=ereg_replace("[^/( )A-Za-z0-9_áéíóúÁÉÍÓÚ]", "", $record[0]);
             $this->n_clasificadores[$fila]=$record[1];
             $this->sigfe_clasificadores[$fila]=$record[2];
             $this->msolicitado[$fila]=$record[3];
             $this->masignado[$fila]=$record[4];
             $this->msaldo1[$fila]=$record[5];
             $this->mrequerido[$fila]=$record[6];
             $this->msaldo2[$fila]=$record[7];
             $this->marequerir[$fila]=$record[8];
             $fila=$fila+1;
         }
      }
      for ($i=1; $i<=$this->nroregistros; $i++) {
         $this->msaldo2[$i]=($this->masignado[$i]-$this->mrequerido[$i]);
         $this->msolicitado[$i]=number_format($this->msolicitado[$i],0,',','.');
         $this->masignado[$i]=number_format($this->masignado[$i],0,',','.');
         $this->msaldo1[$i]=number_format($this->msaldo1[$i],0,',','.');
         $this->mrequerido[$i]=number_format($this->mrequerido[$i],0,',','.');
         $this->msaldo2[$i]=number_format($this->msaldo2[$i],0,',','.');
         $this->marequerir[$i]=number_format($this->marequerir[$i],0,',','.');
      }
      return $this->nroregistros;
   }

   function cargar_antecedentes_pago() {
      $query  = " SELECT A.NOMBRE, CONCAT(B.N_CLASIFICADOR,' (',B.C_CLASIFICADOR,')'), C.N_ETAPA_IDI, A.CODIGO, X.N_RATE ";
      $query .= " FROM TCLASIFICADOR_PRESUPUESTARIO B, ETAPA_IDI C, INVERSION A";
      $query .= " LEFT JOIN RATE X ON (A.RATE=X.C_RATE) ";
      $query .= " WHERE A.C_CLASIFICADOR_PRESUPUESTARIO=B.C_CLASIFICADOR AND A.ANO=B.ANIO AND (A.C_INSTITUCION=B.INSTITUCION OR B.INSTITUCION=0) AND A.C_ETAPA_IDI=C.C_ETAPA_IDI ";
      $query .= " AND A.ANO=".$this->ano." AND A.C_INSTITUCION=".$this->c_institucion." AND A.C_PREINVERSION=".$this->c_preinversion;
      $query .= " AND A.C_FICHA=".$this->c_ficha;
      $dataset= mysql_query($query, $this->conector); 
      $this->nroregistros = mysql_num_rows($dataset);
      $record = mysql_fetch_row($dataset);
      $this->nombre=$record[0];
      $this->n_clasificador_presupuestario=$record[1];
      $this->n_etapa_idi=$record[2];
      $this->codigo=$record[3];
      $this->n_rate=$record[4];
      return $this->nroregistros;
   }

   function cargar_antecedentes_pago1($nitem) {
      $query  = " SELECT ASIGNADO, PAGADO FROM INVERSION_FINANCIAMIENTO ";
      $query .= " WHERE ANO=".$this->ano." AND C_INSTITUCION=".$this->c_institucion." AND C_PREINVERSION=".$this->c_preinversion;
      $query .= " AND C_FICHA=".$this->c_ficha." AND C_CLASIFICADOR_PRESUPUESTARIO='".$nitem."'";
      $dataset= mysql_query($query, $this->conector); 
      $this->nroregistros = mysql_num_rows($dataset);
      $record = mysql_fetch_row($dataset);
      return $record;
   }

   function cargar_texto_beneficiarios() {
      $query  = " SELECT BENEFICIARIOS FROM INVERSION ";
      $query .= " WHERE ANO=".$this->ano." AND C_INSTITUCION=".$this->c_institucion;
      $query .= " AND C_PREINVERSION=".$this->c_preinversion." AND C_FICHA=".$this->c_ficha;
      $dataset= mysql_query($query, $this->conector); 
      $this->nroregistros = mysql_num_rows($dataset);
      $record = mysql_fetch_row($dataset);
      $this->beneficiarios=$record[0];
      return $this->beneficiarios;
   }

   function cargar_encabezado_inversion() {
      $query  = " SELECT A.CODIGO, A.C_TIPO_CODIGO, A.NOMBRE, B.N_SECTOR, A.C_PREINVERSION, A.C_FICHA, A.C_INSTITUCION, C.N_INSTITUCION, A.C_CLASIFICADOR_PRESUPUESTARIO ";
      $query .= " FROM INVERSION A, SECTOR B, INSTITUCION C ";
      $query .= " WHERE A.C_SECTOR=B.C_SECTOR AND A.C_INSTITUCION=C.C_INSTITUCION AND A.ANO=".$this->ano;
      $query .= " AND A.C_INSTITUCION=".$this->c_institucion." AND A.C_PREINVERSION=".$this->c_preinversion;
      $query .= " AND A.C_FICHA=".$this->c_ficha;
      $dataset= mysql_query($query, $this->conector); 
      $this->nroregistros = mysql_num_rows($dataset);
      $record = mysql_fetch_row($dataset);
      $this->codigo=$record[0];
      if ($record[1]==1) {$this->ntipo_codigo="BIP";} else {$this->ntipo_codigo="Otro";}
      $this->nombre=$record[2];
      $this->n_sector=$record[3];
      $this->c_preinversion=$record[4];
      $this->c_ficha=$record[5];
      $this->c_institucion=$record[6];
      $this->n_institucion=$record[7];
      $this->c_clasificador_presupuestario=$record[8];
      return $this->nroregistros;
   }

   function cargar_item_presupuestario() {
      $query  = " SELECT C_CLASIFICADOR_PRESUPUESTARIO FROM INVERSION ";
      $query .= " WHERE ANO=".$this->ano." AND C_INSTITUCION=".$this->c_institucion." AND C_PREINVERSION=".$this->c_preinversion;
      $query .= " AND C_FICHA=".$this->c_ficha;
      $dataset= mysql_query($query, $this->conector); 
      $this->nroregistros = @mysql_num_rows($dataset);
      if ($this->nroregistros>0) {
         $record = mysql_fetch_row($dataset);
         $this->c_clasificador_presupuestario=$record[0];
      }
      return $this->nroregistros;
   }

   function cargar_item_presupuestario_combo() {
      $query  = " SELECT C_CLASIFICADOR, CONCAT(N_CLASIFICADOR,' (',C_CLASIFICADOR,')') FROM TCLASIFICADOR_PRESUPUESTARIO ";
      $query .= " WHERE C_CLASIFICADOR LIKE '".$this->c_clasificador_presupuestario."%' AND ";
      $query .= " C_CLASIFICADOR <> '".$this->c_clasificador_presupuestario."' AND C_CLASIFICADOR<>'' AND INICIAL=2";
      $dataset= mysql_query($query, $this->conector); 
      return $dataset;
   }

   function cargar_item_presupuestario_combo_filtro(){
   	
   	  
      $query = "SELECT CONCAT(C.N_CLASIFICADOR,' (',C.C_CLASIFICADOR,')') 
       FROM FUENTE_FINANCIAMIENTO B, TCLASIFICADOR_PRESUPUESTARIO C, INVERSION_FINANCIAMIENTO A 
				WHERE A.C_FUENTE_FINANCIAMIENTO=B.C_FUENTE_FINANCIAMIENTO 
				AND A.C_CLASIFICADOR_PRESUPUESTARIO=C.C_CLASIFICADOR AND A.ANO=C.ANIO AND (A.C_INSTITUCION=C.INSTITUCION OR C.INSTITUCION=0)
				AND A.ANO=".$this->ano." AND A.C_INSTITUCION=".$this->c_institucion." AND A.C_PREINVERSION=".$this->c_preinversion."
				AND A.C_FICHA=".$this->c_ficha."
				ORDER BY A.C_FUENTE_FINANCIAMIENTO, A.C_CLASIFICADOR_PRESUPUESTARIO";
		 
      $dataset= mysql_query($query, $this->conector); 
     
      return $dataset;

   }
   

   /**
    * Carga la lista de las fuentes de financiamiento con sus
    * respectivos codigos y nombres
    */
    
   function validar_cfuentes(){
   
   $query ="SELECT DISTINCT(CONCAT(B.N_FUENTE_FINANCIAMIENTO, ' (',B.SIGLA_FUENTE_FINANCIAMIENTO,')'))
   FROM INVERSION_FINANCIAMIENTO A LEFT JOIN FUENTE_FINANCIAMIENTO B ON (A.C_FUENTE_FINANCIAMIENTO = B.C_FUENTE_FINANCIAMIENTO) 
   WHERE A.C_CLASIFICADOR_PRESUPUESTARIO ='".$this->c_itemInversion."' AND A.ANO=".$this->ano." AND A.C_INSTITUCION=".$this->c_institucion." AND A.C_PREINVERSION=".$this->c_preinversion." AND A.C_FICHA=".$this->c_ficha;
   
   $dataset = mysql_query($query,$this->conector);
   
   
      return $dataset;
     	
  }
   function cargar_fuente_financiamiento($valor){
      $query=  "SELECT 
               C_FUENTE_FINANCIAMIENTO,
               N_FUENTE_FINANCIAMIENTO,
               SIGLA_FUENTE_FINANCIAMIENTO
               FROM FUENTE_FINANCIAMIENTO
               WHERE MODULO_ASIGNACIONES = 'S' AND C_FUENTE_FINANCIAMIENTO=".$valor."
               ORDER BY C_FUENTE_FINANCIAMIENTO ";

      $dataset = mysql_query($query, $this->conector);
      
      $result = array();
      while ($record = mysql_fetch_assoc($dataset)) {
        $result[$record['C_FUENTE_FINANCIAMIENTO']] = $record['SIGLA_FUENTE_FINANCIAMIENTO'];
      }

      return $result;
   }

   function cargar_grilla_inversiones($xnano, $xcodigobip, $xsector, $xinstitucion, $xaccesoinstituciones,$xfuente,$xcampo,$xvalor,$xanalista,$xexcel,$xdetalle,$xmes,$xfndr,$xasignacion) {
      $fila=0;$this->filaseleccionada=-1;
      $query  =" SELECT if (A.CODIGO!='',if(A.C_TIPO_CODIGO=1,CONCAT(A.CODIGO,' (BIP)'),CONCAT(A.CODIGO,'')),''), A.NOMBRE, L.N_RATE, ";
      $query .=" CONCAT(M.N_CLASIFICADOR,' (',M.C_CLASIFICADOR,')'), N.N_ETAPA_IDI, ";
      $query .=" IF (E.C_NIVEL_UT=1,'Regional',IF (E.C_NIVEL_UT=2,CONCAT('Provincial (',E.UBICACION_ESPECIFICA_NOMBRE,')'),IF (E.C_NIVEL_UT=3,CONCAT('Comunal (',E.UBICACION_ESPECIFICA_NOMBRE,')'),CONCAT('Localidades (',E.UBICACION_ESPECIFICA_NOMBRE,')')))),";   
      $query .=" B.N_SECTOR, C.N_INSTITUCION, ";
      $query .=" IF (LEFT(A.C_CLASIFICADOR_PRESUPUESTARIO,2)='31',A.COSTO_CORE, '-'),";
      $query .=" IF (LEFT(A.C_CLASIFICADOR_PRESUPUESTARIO,2)='31',A.COSTO_EBI, '-'),";
      $query .=" A.COSTO_TOTAL, A.GASTADO_ANOS_ANTERIORES, A.SOLICITADO, A.SALDO_PROXIMO_ANO, ";
/*      $query .=" IF (LEFT(A.C_CLASIFICADOR_PRESUPUESTARIO,2)='31',A.ASIGNADO, '-'),";
      $query .=" IF (LEFT(A.C_CLASIFICADOR_PRESUPUESTARIO,2)='31',A.ASIGNACION_DISPONIBLE, '-'),";
      $query .=" IF (LEFT(A.C_CLASIFICADOR_PRESUPUESTARIO,2)='31',A.SALDO_POR_ASIGNAR, '-'),";*/
      $query .=" A.ASIGNADO, A.ASIGNACION_DISPONIBLE, A.SALDO_POR_ASIGNAR, A.TOTAL_PROGRAMADO, A.PAGADO, A.AVANCE_FINANCIERO, A.AVANCE_FISICO, A.C_ESTADO, A.C_USUARIO, A.ANO, A.C_INSTITUCION, A.C_PREINVERSION, A.C_FICHA ";
      if ($xanalista==1) {
         $query .=" , CONCAT(LEFT(V.NOMBRES,1),LEFT(V.APELLIDO_PATERNO,1),LEFT(V.APELLIDO_MATERNO,1)) ";
      } else {$query .= ", '' "; }
      $query .=" , A.C_USUARIO";
      if ($xmes==1) {$query .= ", SUM(T.G1)";}
      if ($xmes==2) {$query .= ", SUM(T.G1+T.G2)";}
      if ($xmes==3) {$query .= ", SUM(T.G1+T.G2+T.G3)";}
      if ($xmes==4) {$query .= ", SUM(T.G1+T.G2+T.G3+T.G4)";}
      if ($xmes==5) {$query .= ", SUM(T.G1+T.G2+T.G3+T.G4+T.G5)";}
      if ($xmes==6) {$query .= ", SUM(T.G1+T.G2+T.G3+T.G4+T.G5+T.G6)";}
      if ($xmes==7) {$query .= ", SUM(T.G1+T.G2+T.G3+T.G4+T.G5+T.G6+T.G7)";}
      if ($xmes==8) {$query .= ", SUM(T.G1+T.G2+T.G3+T.G4+T.G5+T.G6+T.G7+T.G8)";}
      if ($xmes==9) {$query .= ", SUM(T.G1+T.G2+T.G3+T.G4+T.G5+T.G6+T.G7+T.G8+T.G9)";}
      if ($xmes==10) {$query .= ", SUM(T.G1+T.G2+T.G3+T.G4+T.G5+T.G6+T.G7+T.G8+T.G9+T.G10)";}
      if ($xmes==11) {$query .= ", SUM(T.G1+T.G2+T.G3+T.G4+T.G5+T.G6+T.G7+T.G8+T.G9+T.G10+T.G11)";}
      if ($xmes==12) {$query .= ", SUM(T.G1+T.G2+T.G3+T.G4+T.G5+T.G6+T.G7+T.G8+T.G9+T.G10+T.G11+T.G12)";}
      if ($xdetalle==1) {
         $query .= ", SUM(T.G1), SUM(T.G2), SUM(T.G3), SUM(T.G4), SUM(T.G5), SUM(T.G6), SUM(T.G7), SUM(T.G8), SUM(T.G9), SUM(T.G10), SUM(T.G11), SUM(T.G12) ";
      }
      $query .=" , A.SALDO_PROXIMOS_ANOS";
      $query .=" FROM RATE L, TCLASIFICADOR_PRESUPUESTARIO M, ETAPA_IDI N, SECTOR B, INSTITUCION C, INVERSION A ";
      $query .=" LEFT JOIN UBICACION_TERRITORIAL_INICIATIVA E ON (A.ANO=E.ANO AND A.C_INSTITUCION=E.C_INSTITUCION AND ";
      $query .=" A.C_PREINVERSION=E.C_INICIATIVA AND A.C_FICHA=E.C_FICHA)";
      $query .=" LEFT JOIN INVERSION_FINANCIAMIENTO T ON (A.ANO=T.ANO AND A.C_INSTITUCION=T.C_INSTITUCION AND ";
      $query .=" A.C_PREINVERSION=T.C_PREINVERSION AND A.C_FICHA=T.C_FICHA)";
      if ($xanalista==1) {
         $query .=" LEFT JOIN ADMIN_USUARIO V ON (A.C_USUARIO=V.C_USUARIO) ";
      }
      $query .=" WHERE A.ANO=".$xnano." AND A.RATE=L.C_RATE ";
      $query .=" AND A.C_CLASIFICADOR_PRESUPUESTARIO=M.C_CLASIFICADOR AND A.ANO=M.ANIO AND (A.C_INSTITUCION=M.INSTITUCION OR M.INSTITUCION=0) AND A.C_ETAPA_IDI=N.C_ETAPA_IDI";
      $query .=" AND A.C_SECTOR=B.C_SECTOR AND A.C_INSTITUCION=C.C_INSTITUCION";
      if ($xanalista==1 && $xfuente!='0') {
         $query .= " AND A.C_USUARIO=".$xfuente;
      }
      if ($xasignacion>0) { 
         $query .= " AND T.C_CLASIFICADOR_PRESUPUESTARIO LIKE '".$xasignacion."%'";
      }
      if ($xanalista!=1 && $xfuente!='0') { $query .= " AND T.C_FUENTE_FINANCIAMIENTO LIKE '".$xfuente."%'";}
      if ($xfndr!=0) { $query .= " AND (T.C_FUENTE_FINANCIAMIENTO LIKE '03%' OR T.C_FUENTE_FINANCIAMIENTO LIKE '02%')";}
      if ($xsector>0) { $query .= " AND A.C_SECTOR = ".$xsector;}
      if ($xanalista==1) {
   $query .= " AND A.C_INSTITUCION = 1";
      }
      if ($xanalista!=1 && $xinstitucion>0) { 
   $query .= " AND A.C_INSTITUCION = ".$xinstitucion;
      } else {
   if ($xaccesoinstituciones!='999') {
      $query .= " AND A.C_INSTITUCION IN (".$xaccesoinstituciones.") ";
   }
      }
      if ($xvalor!="") {
         if ($xcampo==1) {$query .=" AND A.CODIGO LIKE '%".$xvalor."%'";}
         if ($xcampo==2) {$query .=" AND L.N_RATE LIKE '%".$xvalor."%'";}
         if ($xcampo==3) {$query .=" AND (M.N_CLASIFICADOR LIKE '%".$xvalor."%' OR M.C_CLASIFICADOR LIKE '%".$xvalor."%')";}
         if ($xcampo==4) {$query .=" AND N.N_ETAPA_IDI LIKE '%".$xvalor."%'";}
         if ($xcampo==5) {$query .=" AND A.EJECUTIVO LIKE '%".$xvalor."%'";}
      }
      $query .=" GROUP BY A.CODIGO, A.NOMBRE, A.ANO, A.C_INSTITUCION, A.C_PREINVERSION, A.C_FICHA ";
      $query .=" ORDER BY C.C_INSTITUCION, A.CODIGO, A.NOMBRE ";
	  
      $dataset= mysql_query($query, $this->conector); 
      $this->nroregistros = mysql_num_rows($dataset);    
      if ($xexcel!=1) {
         $rows = array(); 
         $total = array();
         for ($t=8; $t<19; $t++) {$total[$t]=0;}
         $total[20]=0;
         $total[29]=0;
         if ($xdetalle==1) {
            for ($t=29; $t<43; $t++) {$total[$t]=0;}
         }
         while ($record = mysql_fetch_row($dataset)) { 
            for ($t=8; $t<19; $t++) {
               if ($record[$t]!="-") {$total[$t]=$total[$t]+$record[$t];}
               if (($t==8 || $t==9 || $t==10 || $t==11 || $t==12 || $t==13 || $t==14 || $t==15 || $t==16 || $t==17 || $t==18) && $record[$t]!="-") {$record[$t]=number_format($record[$t],0,',','.');}
            }
            $total[20]=$total[20]+$record[20];
            $total[29]=$total[29]+$record[29];
            if ($xdetalle==1) {
               for ($t=30; $t<43; $t++) {
                  $total[$t]=$total[$t]+$record[$t];
               }
            }
       $cols = array();
       $colsel=0;
       foreach ($record as $value) { 
          if ($colsel==1) {$value=ereg_replace("[^/( )A-Za-z0-9_áéíóúÁÉÍÓÚ]", "", $value);}
          $cols[] = '"'.addslashes($value).'"';
          if ($colsel==0) {
             if ($value==$xpreinversionsel) {$this->filaseleccionada=$fila;}
          }       
              $colsel++;        
       } 
       $rows[] = "\t[".implode(",", $cols)."]"; 
       $fila++;
         } 
         $datagrilla="[\n".implode(",\n",$rows)."\n];\n"; 
         if ($total[12]>0) {$total[19]=$total[18]*100/$total[12];} else {$total[19]='0';}
         if ($total[20]>0 && $this->nroregistros>0) {$total[20]=$total[20]*100/$this->nroregistros;} else {$total[20]='0';}
         for ($t=8; $t<19; $t++) {
            $total[$t]=number_format($total[$t],0,',','.');
         }
         if ($xdetalle==1) {
            for ($t=30; $t<43; $t++) {
               $total[$t]=number_format($total[$t],0,',','.');
            }
         }
         $total[19]=number_format($total[19],2,',','.');      
         $total[20]=number_format($total[20],2,',','.');      
         $total[29]=number_format($total[29],0,',','.');      
         $rows = array();$cols = array();
         $total[1]=$this->nroregistros." Iniciativas visualizadas";
         if ($xdetalle==1) {
            for ($j=0; $j<43; $j++) {
               $pie[$j]=$total[$j];
               $cols[] = '"'.addslashes($pie[$j]).'"'; 
            }
         } else {
            for ($j=0; $j<20; $j++) {
               $pie[$j]=$total[$j];
               $cols[] = '"'.addslashes($pie[$j]).'"'; 
            }
            $pie[29]=$total[29];
         }
         $rows[] = "\t[".implode(",", $cols)."]"; 
         $this->footer=implode(",\n",$rows).";\n"; 
         return $datagrilla;
      } else {
         while ($record = mysql_fetch_row($dataset)) { 
            for ($t=0; $t<14; $t++) {
               $this->datainforme[$fila][$t]=$record[$t];
            }
            $this->datainforme[$fila][15]=$record[14];
            $this->datainforme[$fila][16]=$record[15];
            $this->datainforme[$fila][17]=$record[16];
            $this->datainforme[$fila][18]=$record[18];
            $this->datainforme[$fila][32]=$record[20];
            $this->datainforme[$fila][33]=$record[19];
            if ($xanalista==1) {$this->datainforme[$fila][34]=$record[27];}
            if ($xdetalle==1) {
                  $this->datainforme[$fila][14]=$record[42];
                  $this->datainforme[$fila][19]=$record[29];
      $this->datainforme[$fila][20]=$record[30];
      $this->datainforme[$fila][21]=$record[31];
      $this->datainforme[$fila][22]=$record[32];
      $this->datainforme[$fila][23]=$record[33];
      $this->datainforme[$fila][24]=$record[34];
      $this->datainforme[$fila][25]=$record[35];
      $this->datainforme[$fila][26]=$record[36];
      $this->datainforme[$fila][27]=$record[37];
      $this->datainforme[$fila][28]=$record[38];
      $this->datainforme[$fila][29]=$record[39];
      $this->datainforme[$fila][30]=$record[40];
      $this->datainforme[$fila][31]=$record[41];
            } else {
                  $this->datainforme[$fila][14]=$record[30];
            }
       $fila++;
         } 
         return $this->datainforme;
      }
   }
   
   function cargar_grilla_inversiones_pagos($xnano, $xcodigobip, $xsector, $xinstitucion, $xaccesoinstituciones,$xfuente,$xcampo,$xvalor,$xanalista,$xexcel,$xtipo,$xsituacion) {
      $fila=0;$this->filaseleccionada=-1;$total=0;
      $query  =" SELECT if (A.CODIGO!='',if(A.C_TIPO_CODIGO=1,CONCAT(A.CODIGO,' (BIP)'),CONCAT(A.CODIGO,'')),''), A.NOMBRE, L.N_RATE, ";
      $query .=" CONCAT(M.N_CLASIFICADOR,' (',M.C_CLASIFICADOR,')'), N.N_ETAPA_IDI, ";
      $query .=" IF (E.C_NIVEL_UT=1,'Regional',IF (E.C_NIVEL_UT=2,CONCAT('Provincial (',E.UBICACION_ESPECIFICA_NOMBRE,')'),IF (E.C_NIVEL_UT=3,CONCAT('Comunal (',E.UBICACION_ESPECIFICA_NOMBRE,')'),CONCAT('Localidades (',E.UBICACION_ESPECIFICA_NOMBRE,')')))),";   
      $query .=" B.N_SECTOR, C.SIGLA_INSTITUCION, A.COSTO_CORE, A.COSTO_EBI,A.COSTO_TOTAL, A.GASTADO_ANOS_ANTERIORES, A.SOLICITADO, A.SALDO_PROXIMO_ANO, A.ASIGNADO, A.ASIGNACION_DISPONIBLE,";
      $query .=" A.SALDO_POR_ASIGNAR, A.TOTAL_PROGRAMADO, A.PAGADO, A.AVANCE_FINANCIERO, A.AVANCE_FISICO, A.C_ESTADO, A.EJECUTIVO, A.ANO, A.C_INSTITUCION, A.C_PREINVERSION, A.C_FICHA ";
      if ($xanalista==1) {
         $query .=" , CONCAT(LEFT(V.NOMBRES,1),LEFT(V.APELLIDO_PATERNO,1),LEFT(V.APELLIDO_MATERNO,1)) ";
      }
      $query .=" , A.C_USUARIO";
      if ($xtipo==1) {
         $query .=" FROM INVERSION_CONTRATO W, ";
      } 
      if ($xtipo==2) {
         $query .=" FROM INVERSION_CONVENIOS W, ";
      }          
      if ($xtipo==3) {
         $query .=" FROM INVERSION_CONVENIOS W, INVERSION_FINANCIAMIENTO Z, ";
      }          
      
      if ($xsituacion>0) {
         if ($xtipo==1) {
            $query .=" INVERSION_PAGO Y, ";
         } 
         if ($xtipo==2) {
            $query .=" INVERSION_TRANSFERENCIA Y, ";
         }           
         if ($xtipo==3) {
            $query .=" INVERSION_GASTO_ADMINISTRATIVO Y, ";
         }                     
      }     
 
      $query .= " RATE L, TCLASIFICADOR_PRESUPUESTARIO M, ETAPA_IDI N, SECTOR B, INSTITUCION C, INVERSION A ";
      $query .=" LEFT JOIN UBICACION_TERRITORIAL_INICIATIVA E ON (A.ANO=E.ANO AND A.C_INSTITUCION=E.C_INSTITUCION AND ";
      $query .=" A.C_PREINVERSION=E.C_INICIATIVA AND A.C_FICHA=E.C_FICHA)";
      if ($xanalista!=1 && $xfuente!='0') {
         $query .=" LEFT JOIN INVERSION_FINANCIAMIENTO T ON (A.ANO=T.ANO AND A.C_INSTITUCION=T.C_INSTITUCION AND ";
         $query .=" A.C_PREINVERSION=T.C_PREINVERSION AND A.C_FICHA=T.C_FICHA)";
      }   
      if ($xanalista==1) {
         $query .=" LEFT JOIN ADMIN_USUARIO V ON (A.C_USUARIO=V.C_USUARIO) ";
      }
      $query .=" WHERE A.ANO=".$xnano." AND A.RATE=L.C_RATE ";

      if ($xsituacion>0) {
         $query .=" AND A.ANO=Y.ANO AND A.C_INSTITUCION=Y.C_INSTITUCION AND A.C_PREINVERSION=Y.C_PREINVERSION AND A.C_FICHA=Y.C_FICHA AND Y.C_ESTADO=".$xsituacion;
      }     

      // NUEVO
      if ($xtipo==1) {
         $query .= " AND W.C_ESTADO=2 AND A.ANO=W.ANO AND A.C_INSTITUCION=W.C_INSTITUCION AND A.C_PREINVERSION=W.C_PREINVERSION AND A.C_FICHA=W.C_FICHA ";
         $query .= " AND (A.C_CLASIFICADOR_PRESUPUESTARIO LIKE '29%' || A.C_CLASIFICADOR_PRESUPUESTARIO LIKE '31%'|| A.C_CLASIFICADOR_PRESUPUESTARIO LIKE '22%') ";
      }
      if ($xtipo==2) {
         $query .= " AND W.MONTO>0 AND A.ANO=W.ANO AND A.C_INSTITUCION=W.C_INSTITUCION AND A.C_PREINVERSION=W.C_PREINVERSION AND A.C_FICHA=W.C_FICHA ";
         $query .= " AND (A.C_CLASIFICADOR_PRESUPUESTARIO LIKE '24%' || A.C_CLASIFICADOR_PRESUPUESTARIO LIKE '33%') ";
      }   
      if ($xtipo==3) {
         $query .= " AND W.MONTO>0 AND A.ANO=W.ANO AND A.C_INSTITUCION=W.C_INSTITUCION AND A.C_PREINVERSION=W.C_PREINVERSION AND A.C_FICHA=W.C_FICHA ";
         $query .= " AND A.C_CLASIFICADOR_PRESUPUESTARIO LIKE '31%' ";
         $query .= " AND A.ANO=Z.ANO AND A.C_INSTITUCION=Z.C_INSTITUCION AND A.C_PREINVERSION=Z.C_PREINVERSION AND A.C_FICHA=Z.C_FICHA ";
         $query .= " AND Z.C_CLASIFICADOR_PRESUPUESTARIO IN ('31.01.001','31.02.001','31.03.001')";
      }   
      $query .=" AND A.C_CLASIFICADOR_PRESUPUESTARIO=M.C_CLASIFICADOR AND A.ANO=M.ANIO AND (A.C_INSTITUCION=M.INSTITUCION OR M.INSTITUCION=0) AND A.C_ETAPA_IDI=N.C_ETAPA_IDI";
      $query .=" AND A.C_SECTOR=B.C_SECTOR AND A.C_INSTITUCION=C.C_INSTITUCION";
      if ($xanalista==1 && $xfuente!='0') {
         $query .= " AND A.C_USUARIO=".$xfuente;
      }
      if ($xanalista==1 && $xinstitucion>0) { 
         $query .= " AND A.C_CLASIFICADOR_PRESUPUESTARIO LIKE '".$xinstitucion."%'";
      }
      if ($xanalista!=1 && $xfuente!='0') { $query .= " AND T.C_FUENTE_FINANCIAMIENTO LIKE '".$xfuente."%'";}
      if ($xsector>0) { $query .= " AND A.C_SECTOR = ".$xsector;}
      if ($xanalista!=1 && $xinstitucion>0) { 
   $query .= " AND A.C_INSTITUCION = ".$xinstitucion;
      } else {
   if ($xaccesoinstituciones!='999') {
      $query .= " AND A.C_INSTITUCION IN (".$xaccesoinstituciones.") ";
   }
      }
      if ($xvalor!="") {
         if ($xcampo==1) {$query .=" AND A.CODIGO LIKE '%".$xvalor."%'";}
         if ($xcampo==2) {$query .=" AND L.N_RATE LIKE '%".$xvalor."%'";}
         if ($xcampo==3) {$query .=" AND (M.N_CLASIFICADOR LIKE '%".$xvalor."%' OR M.C_CLASIFICADOR LIKE '%".$xvalor."%')";}
         if ($xcampo==4) {$query .=" AND N.N_ETAPA_IDI LIKE '%".$xvalor."%'";}
         if ($xcampo==5) {$query .=" AND A.EJECUTIVO LIKE '%".$xvalor."%'";}
      }
      $query .=" GROUP BY A.CODIGO, A.NOMBRE, A.ANO, A.C_INSTITUCION, A.C_PREINVERSION, A.C_FICHA ";
      $query .=" ORDER BY C.C_INSTITUCION, A.C_PREINVERSION, A.CODIGO, A.NOMBRE ";
      $dataset= mysql_query($query, $this->conector); 
      $this->nroregistros = mysql_num_rows($dataset);    
      if ($xexcel!=1) {
         $rows = array(); 
         $total = array();
         for ($t=8; $t<19; $t++) {$total[$t]=0;}
         while ($record = mysql_fetch_row($dataset)) { 
            for ($t=8; $t<19; $t++) {
               $total[$t]=$total[$t]+$record[$t];
            }
       $cols = array();
       $colsel=0;
       foreach ($record as $value) { 
          $cols[] = '"'.addslashes($value).'"';
          if ($colsel==0) {
             if ($value==$xpreinversionsel) {$this->filaseleccionada=$fila;}
          }       
              $colsel++;        
       } 
       $rows[] = "\t[".implode(",", $cols)."]"; 
       $fila++;
         } 
         $datagrilla="[\n".implode(",\n",$rows)."\n];\n"; 
         if ($total[12]>0) {$total[19]=$total[18]*100/$total[12];} else {$total[19]='0';}
         for ($t=8; $t<19; $t++) {
            $total[$t]=number_format($total[$t],0,',','.');
         }
         $total[19]=number_format($total[$t],2,',','.');      
         $rows = array();$cols = array();
         $total[1]=$this->nroregistros." Iniciativas visualizadas";
         for ($j=0; $j<20; $j++) {
             $pie[$j]=$total[$j];
            $cols[] = '"'.addslashes($pie[$j]).'"'; 
         }
         $rows[] = "\t[".implode(",", $cols)."]"; 
         $this->footer=implode(",\n",$rows).";\n"; 
         return $datagrilla;
      } else {
         while ($record = mysql_fetch_row($dataset)) { 
            for ($t=0; $t<17; $t++) {
               $this->datainforme[$fila][$t]=$record[$t];
            }
            $this->datainforme[$fila][17]=$record[18];
            $this->datainforme[$fila][18]=$record[20];
            $this->datainforme[$fila][19]=$record[19];
            if ($xanalista==1) {$this->datainforme[$fila][20]=$record[27];}
       $fila++;
         } 
         return $this->datainforme;
      }
   }

   function cargar_grilla_inversiones_requerimientos($xnano, $xcodigobip, $xsector, $xinstitucion, $xaccesoinstituciones,$xfuente,$xcampo,$xvalor,$xanalista,$xexcel,$xsituacion) {
      $fila=0;$this->filaseleccionada=-1;$total=0;
      $query  =" SELECT A.CODIGO, Z.ID_SIGFE, A.NOMBRE, L.N_RATE, ";
      $query .=" CONCAT(M.N_CLASIFICADOR,' (',M.C_CLASIFICADOR,')'), N.N_ETAPA_IDI, ";
      $query .=" IF (E.C_NIVEL_UT=1,'Regional',IF (E.C_NIVEL_UT=2,CONCAT('Provincial (',E.UBICACION_ESPECIFICA_NOMBRE,')'),IF (E.C_NIVEL_UT=3,CONCAT('Comunal (',E.UBICACION_ESPECIFICA_NOMBRE,')'),CONCAT('Localidades (',E.UBICACION_ESPECIFICA_NOMBRE,')')))),";   
      $query .=" B.N_SECTOR, C.SIGLA_INSTITUCION, A.COSTO_CORE, A.COSTO_EBI,A.COSTO_TOTAL, A.GASTADO_ANOS_ANTERIORES, A.SOLICITADO, A.SALDO_PROXIMO_ANO, A.ASIGNADO, A.ASIGNACION_DISPONIBLE,";
      $query .=" A.SALDO_POR_ASIGNAR, A.TOTAL_PROGRAMADO, A.PAGADO, A.AVANCE_FINANCIERO, A.AVANCE_FISICO, A.C_ESTADO, A.EJECUTIVO, A.ANO, A.C_INSTITUCION, A.C_PREINVERSION, A.C_FICHA ";
      if ($xanalista==1) {
         $query .=" , CONCAT(LEFT(V.NOMBRES,1),LEFT(V.APELLIDO_PATERNO,1),LEFT(V.APELLIDO_MATERNO,1)) ";
      }
      $query .=" , A.C_USUARIO";
      $query .=" FROM RATE L,";      
      if ($xsituacion>0) {
         $query .=" INVERSION_REQUERIMIENTO Y, ";
      }     
 
      $query .= " TCLASIFICADOR_PRESUPUESTARIO M, ETAPA_IDI N, SECTOR B, INSTITUCION C, INVERSION A ";
      $query .=" LEFT JOIN UBICACION_TERRITORIAL_INICIATIVA E ON (A.ANO=E.ANO AND A.C_INSTITUCION=E.C_INSTITUCION AND ";
      $query .=" A.C_PREINVERSION=E.C_INICIATIVA AND A.C_FICHA=E.C_FICHA)";
      $query .=" LEFT JOIN INVERSION_ANTECEDENTES Z ON (A.ANO=Z.ANO AND A.C_INSTITUCION=Z.C_INSTITUCION AND ";
      $query .=" A.C_PREINVERSION=Z.C_PREINVERSION AND A.C_FICHA=Z.C_FICHA)";
      if ($xanalista!=1 && $xfuente!='0') {
         $query .=" LEFT JOIN INVERSION_FINANCIAMIENTO T ON (A.ANO=T.ANO AND A.C_INSTITUCION=T.C_INSTITUCION AND ";
         $query .=" A.C_PREINVERSION=T.C_PREINVERSION AND A.C_FICHA=T.C_FICHA)";
      }   
      if ($xanalista==1) {
         $query .=" LEFT JOIN ADMIN_USUARIO V ON (A.C_USUARIO=V.C_USUARIO) ";
      }
      $query .=" WHERE A.ANO=".$xnano." AND A.RATE=L.C_RATE ";
//      $query .= " AND A.C_TIPO_CODIGO=1 ";
      if ($xsituacion>0) {
         $query .=" AND A.ANO=Y.ANO AND A.C_INSTITUCION=Y.C_INSTITUCION AND A.C_PREINVERSION=Y.C_PREINVERSION AND A.C_FICHA=Y.C_FICHA AND Y.C_ESTADO=".$xsituacion;
      }     

      // NUEVO
//      $query .= " AND W.C_ESTADO=2 AND A.ANO=W.ANO AND A.C_INSTITUCION=W.C_INSTITUCION AND A.C_PREINVERSION=W.C_PREINVERSION AND A.C_FICHA=W.C_FICHA ";
//      $query .= " AND (A.C_CLASIFICADOR_PRESUPUESTARIO LIKE '29%' || A.C_CLASIFICADOR_PRESUPUESTARIO LIKE '31%'|| A.C_CLASIFICADOR_PRESUPUESTARIO LIKE '22%') ";
      $query .=" AND A.C_CLASIFICADOR_PRESUPUESTARIO=M.C_CLASIFICADOR AND A.ANO=M.ANIO AND (A.C_INSTITUCION=M.INSTITUCION OR M.INSTITUCION=0) AND A.C_ETAPA_IDI=N.C_ETAPA_IDI";
      $query .=" AND A.C_SECTOR=B.C_SECTOR AND A.C_INSTITUCION=C.C_INSTITUCION";
      if ($xanalista==1 && $xfuente!='0') {
         $query .= " AND A.C_USUARIO=".$xfuente;
      }
      if ($xanalista==1 && $xinstitucion>0) { 
         $query .= " AND A.C_CLASIFICADOR_PRESUPUESTARIO='".$xinstitucion."'";
      }
      if ($xanalista!=1 && $xfuente!='0') { $query .= " AND T.C_FUENTE_FINANCIAMIENTO LIKE '".$xfuente."%'";}
      if ($xsector>0) { $query .= " AND A.C_SECTOR = ".$xsector;}
      if ($xanalista!=1 && $xinstitucion>0) { 
   $query .= " AND A.C_INSTITUCION = ".$xinstitucion;
      } else {
   if ($xaccesoinstituciones!='999') {
      $query .= " AND A.C_INSTITUCION IN (".$xaccesoinstituciones.") ";
   }
      }
      if ($xvalor!="") {
         if ($xcampo==1) {$query .=" AND A.CODIGO LIKE '%".$xvalor."%'";}
         if ($xcampo==2) {$query .=" AND L.N_RATE LIKE '%".$xvalor."%'";}
         if ($xcampo==3) {$query .=" AND (M.N_CLASIFICADOR LIKE '%".$xvalor."%' OR M.C_CLASIFICADOR LIKE '%".$xvalor."%')";}
         if ($xcampo==4) {$query .=" AND N.N_ETAPA_IDI LIKE '%".$xvalor."%'";}
         if ($xcampo==5) {$query .=" AND A.EJECUTIVO LIKE '%".$xvalor."%'";}
      }
      $query .=" GROUP BY A.CODIGO, A.NOMBRE, A.ANO, A.C_INSTITUCION, A.C_PREINVERSION, A.C_FICHA ";
      $query .=" ORDER BY C.C_INSTITUCION, A.C_PREINVERSION, A.CODIGO, A.NOMBRE ";
      $dataset= mysql_query($query, $this->conector); 
      $this->nroregistros = mysql_num_rows($dataset);    
      if ($xexcel!=1) {
         $rows = array(); 
         $total = array();
         for ($t=8; $t<19; $t++) {$total[$t]=0;}
         while ($record = mysql_fetch_row($dataset)) { 
            for ($t=8; $t<19; $t++) {
               $total[$t]=$total[$t]+$record[$t];
            }
       $cols = array();
       $colsel=0;
       foreach ($record as $value) { 
          $cols[] = '"'.addslashes($value).'"';
          if ($colsel==0) {
             if ($value==$xpreinversionsel) {$this->filaseleccionada=$fila;}
          }       
              $colsel++;        
       } 
       $rows[] = "\t[".implode(",", $cols)."]"; 
       $fila++;
         } 
         $datagrilla="[\n".implode(",\n",$rows)."\n];\n"; 
         if ($total[12]>0) {$total[19]=$total[18]*100/$total[12];} else {$total[19]='0';}
         for ($t=8; $t<19; $t++) {
            $total[$t]=number_format($total[$t],0,',','.');
         }
         $total[19]=number_format($total[$t],2,',','.');      
         $rows = array();$cols = array();
         $total[2]=$this->nroregistros." Iniciativas visualizadas";
         for ($j=0; $j<20; $j++) {
             $pie[$j]=$total[$j];
            $cols[] = '"'.addslashes($pie[$j]).'"'; 
         }
         $rows[] = "\t[".implode(",", $cols)."]"; 
         $this->footer=implode(",\n",$rows).";\n"; 
         return $datagrilla;
      } else {
         while ($record = mysql_fetch_row($dataset)) { 
            for ($t=0; $t<17; $t++) {
               $this->datainforme[$fila][$t]=$record[$t];
            }
            $this->datainforme[$fila][17]=$record[18];
            $this->datainforme[$fila][18]=$record[20];
            $this->datainforme[$fila][19]=$record[19];
            if ($xanalista==1) {$this->datainforme[$fila][20]=$record[27];}
       $fila++;
         } 
         return $this->datainforme;
      }
   }

   function cargar_grilla_inversiones_compromisos($xnano, $xcodigobip, $xsector, $xinstitucion, $xaccesoinstituciones,$xfuente,$xcampo,$xvalor,$xanalista,$xexcel,$xsituacion) {
      $fila=0;$this->filaseleccionada=-1;$total=0;$xtipo=1;
      if ($xtipo==1) {
         $query  =" SELECT CONCAT(Z.RUN,'-',Z.DV,'  ',Z.NOMBRE,' ',Z.AP_PATERNO,' ',Z.AP_MATERNO), W.C_CONTRATO, ";
      } else {
      }
      $query .= "if (A.CODIGO!='',if(A.C_TIPO_CODIGO=1,CONCAT(A.CODIGO,' (BIP)'),CONCAT(A.CODIGO,'')),''), A.NOMBRE, L.N_RATE, ";
      $query .=" CONCAT(M.N_CLASIFICADOR,' (',M.C_CLASIFICADOR,')'), N.N_ETAPA_IDI, ";
      $query .=" IF (E.C_NIVEL_UT=1,'Regional',IF (E.C_NIVEL_UT=2,CONCAT('Provincial (',E.UBICACION_ESPECIFICA_NOMBRE,')'),IF (E.C_NIVEL_UT=3,CONCAT('Comunal (',E.UBICACION_ESPECIFICA_NOMBRE,')'),CONCAT('Localidades (',E.UBICACION_ESPECIFICA_NOMBRE,')')))),";   
      $query .=" B.N_SECTOR, C.SIGLA_INSTITUCION, A.COSTO_CORE, A.COSTO_EBI,A.COSTO_TOTAL, A.GASTADO_ANOS_ANTERIORES, A.SOLICITADO, A.SALDO_PROXIMO_ANO, A.ASIGNADO, A.ASIGNACION_DISPONIBLE,";
      $query .=" A.SALDO_POR_ASIGNAR, A.TOTAL_PROGRAMADO, A.PAGADO, A.AVANCE_FINANCIERO, A.AVANCE_FISICO, A.C_ESTADO, A.EJECUTIVO, A.ANO, A.C_INSTITUCION, A.C_PREINVERSION, A.C_FICHA ";
      if ($xanalista==1) {
         $query .=" , CONCAT(LEFT(V.NOMBRES,1),LEFT(V.APELLIDO_PATERNO,1),LEFT(V.APELLIDO_MATERNO,1)) ";
      }
      $query .=" , A.C_USUARIO";
      if ($xtipo==1) {
         $query .=" FROM INVERSION_CONTRATO W, ";
      } 
      if ($xtipo==2) {
         $query .=" FROM INVERSION_CONVENIOS W, ";
      }                
      if ($xsituacion>0) {
         if ($xtipo==1) {
            $query .=" INVERSION_PAGO Y, ";
         } 
         if ($xtipo==2) {
            $query .=" INVERSION_TRANSFERENCIA Y, ";
         }           
      }     
 
      $query .= " RATE L, TCLASIFICADOR_PRESUPUESTARIO M, ETAPA_IDI N, SECTOR B, INSTITUCION C, TCONTRATISTA Z, INVERSION A ";
      $query .=" LEFT JOIN UBICACION_TERRITORIAL_INICIATIVA E ON (A.ANO=E.ANO AND A.C_INSTITUCION=E.C_INSTITUCION AND ";
      $query .=" A.C_PREINVERSION=E.C_INICIATIVA AND A.C_FICHA=E.C_FICHA)";
      if ($xanalista!=1 && $xfuente!='0') {
         $query .=" LEFT JOIN INVERSION_FINANCIAMIENTO T ON (A.ANO=T.ANO AND A.C_INSTITUCION=T.C_INSTITUCION AND ";
         $query .=" A.C_PREINVERSION=T.C_PREINVERSION AND A.C_FICHA=T.C_FICHA)";
      }   
      if ($xanalista==1) {
         $query .=" LEFT JOIN ADMIN_USUARIO V ON (A.C_USUARIO=V.C_USUARIO) ";
      }
      $query .=" WHERE A.ANO=".$xnano." AND A.RATE=L.C_RATE ";

      if ($xsituacion>0) {
         $query .=" AND A.ANO=Y.ANO AND A.C_INSTITUCION=Y.C_INSTITUCION AND A.C_PREINVERSION=Y.C_PREINVERSION AND A.C_FICHA=Y.C_FICHA AND Y.C_ESTADO=".$xsituacion;
      }     

      // NUEVO
      if ($xtipo==1) {
         $query .= " AND W.RUT_CONTRATISTA=Z.RUN ";
         $query .= " AND W.C_ESTADO=2 AND A.ANO=W.ANO AND A.C_INSTITUCION=W.C_INSTITUCION AND A.C_PREINVERSION=W.C_PREINVERSION AND A.C_FICHA=W.C_FICHA ";
         $query .= " AND (A.C_CLASIFICADOR_PRESUPUESTARIO LIKE '29%' || A.C_CLASIFICADOR_PRESUPUESTARIO LIKE '31%'|| A.C_CLASIFICADOR_PRESUPUESTARIO LIKE '22%') ";
      }
      if ($xtipo==2) {
         $query .= " AND W.MONTO>0 AND A.ANO=W.ANO AND A.C_INSTITUCION=W.C_INSTITUCION AND A.C_PREINVERSION=W.C_PREINVERSION AND A.C_FICHA=W.C_FICHA ";
         $query .= " AND (A.C_CLASIFICADOR_PRESUPUESTARIO LIKE '24%' || A.C_CLASIFICADOR_PRESUPUESTARIO LIKE '33%') ";
      }   
      $query .=" AND A.C_CLASIFICADOR_PRESUPUESTARIO=M.C_CLASIFICADOR AND A.ANO=M.ANIO AND (A.C_INSTITUCION=M.INSTITUCION OR M.INSTITUCION=0) AND A.C_ETAPA_IDI=N.C_ETAPA_IDI";
      $query .=" AND A.C_SECTOR=B.C_SECTOR AND A.C_INSTITUCION=C.C_INSTITUCION";
      if ($xanalista==1 && $xfuente!='0') {
         $query .= " AND A.C_USUARIO=".$xfuente;
      }
      if ($xanalista==1 && $xinstitucion>0) { 
         $query .= " AND A.C_CLASIFICADOR_PRESUPUESTARIO='".$xinstitucion."'";
      }
      if ($xanalista!=1 && $xfuente!='0') { $query .= " AND T.C_FUENTE_FINANCIAMIENTO LIKE '".$xfuente."%'";}
      if ($xsector>0) { $query .= " AND A.C_SECTOR = ".$xsector;}
      if ($xanalista!=1 && $xinstitucion>0) { 
   $query .= " AND A.C_INSTITUCION = ".$xinstitucion;
      } else {
   if ($xaccesoinstituciones!='999') {
      $query .= " AND A.C_INSTITUCION IN (".$xaccesoinstituciones.") ";
   }
      }
      $query .=" GROUP BY A.CODIGO, A.NOMBRE, A.ANO, A.C_INSTITUCION, A.C_PREINVERSION, A.C_FICHA ";
      if ($xtipo==1) {$query .= ", W.C_CONTRATO ";} else {$query .= ", W.C_CONVENIO";}
      $query .=" ORDER BY C.C_INSTITUCION, A.C_PREINVERSION, A.CODIGO, A.NOMBRE";
      if ($xtipo==1) {$query .= ", W.C_CONTRATO ";} else {$query .= ", W.C_CONVENIO";}
      $dataset= mysql_query($query, $this->conector); 
      $this->nroregistros = mysql_num_rows($dataset);    
      if ($xexcel!=1) {
         $rows = array(); 
         $total = array();
         for ($t=8; $t<19; $t++) {$total[$t]=0;}
         while ($record = mysql_fetch_row($dataset)) { 
            for ($t=8; $t<19; $t++) {
               $total[$t]=$total[$t]+$record[$t];
            }
       $cols = array();
       $colsel=0;
       foreach ($record as $value) { 
          $cols[] = '"'.addslashes($value).'"';
          if ($colsel==0) {
             if ($value==$xpreinversionsel) {$this->filaseleccionada=$fila;}
          }       
              $colsel++;        
       } 
       $rows[] = "\t[".implode(",", $cols)."]"; 
       $fila++;
         } 
         $datagrilla="[\n".implode(",\n",$rows)."\n];\n"; 
         if ($total[12]>0) {$total[19]=$total[18]*100/$total[12];} else {$total[19]='0';}
         for ($t=8; $t<19; $t++) {
            $total[$t]=number_format($total[$t],0,',','.');
         }
         $total[19]=number_format($total[$t],2,',','.');      
         $rows = array();$cols = array();
         $total[2]=$this->nroregistros." Iniciativas visualizadas";
         for ($j=0; $j<20; $j++) {
             $pie[$j]=$total[$j];
            $cols[] = '"'.addslashes($pie[$j]).'"'; 
         }
         $rows[] = "\t[".implode(",", $cols)."]"; 
         $this->footer=implode(",\n",$rows).";\n"; 
         return $datagrilla;
      } else {
         while ($record = mysql_fetch_row($dataset)) { 
            for ($t=0; $t<17; $t++) {
               $this->datainforme[$fila][$t]=$record[$t];
            }
            $this->datainforme[$fila][17]=$record[18];
            $this->datainforme[$fila][18]=$record[20];
            $this->datainforme[$fila][19]=$record[19];
            if ($xanalista==1) {$this->datainforme[$fila][20]=$record[27];}
       $fila++;
         } 
         return $this->datainforme;
      }
   }

   function cargar_grilla_inversiones_asignacion($xnano,$xanalista,$xasignacion) {
      $fila=0;$this->filaseleccionada=-1;$total=0;
      $query  =" SELECT 
			if (A.CODIGO!='',A.CODIGO,'') AS CODIGO, 
            A.NOMBRE,
            A.SOLICITADO, 
            A.ASIGNADO, 
            A.ASIGNACION_DISPONIBLE, 
            A.SALDO_POR_ASIGNAR, 
            A.TOTAL_PROGRAMADO,
            A.PAGADO, 
            A.SALDO, 
            A.ARRASTRE, 
            A.C_INSTITUCION, 
            A.C_PREINVERSION, 
            A.C_FICHA, 
            A.C_CLASIFICADOR_PRESUPUESTARIO,
            A.SOLICITADO,
			CONCAT(T.N_CLASIFICADOR, ' (', T.C_CLASIFICADOR, ')'),
			(CASE WHEN A.C_TIPO_CODIGO=1 THEN 'BIP' ELSE 'OTRO' END) AS TIPO_CODIGO
            FROM INVERSION A LEFT JOIN TCLASIFICADOR_PRESUPUESTARIO T ON
            A.ANO=T.ANIO AND A.C_CLASIFICADOR_PRESUPUESTARIO=T.C_CLASIFICADOR AND (A.C_INSTITUCION=T.INSTITUCION OR T.INSTITUCION=0)
            WHERE A.ANO=".$xnano." AND A.C_INSTITUCION=1";
      if ($xanalista!='0') {
         $query .=" AND A.C_USUARIO=".$xanalista;
      }
	  // filtro resolución afecta FIC
	  if ($xasignacion=='3') {
         $query .=" AND A.C_CLASIFICADOR_PRESUPUESTARIO LIKE '33%'";
      }
	  // filtro resolución exenta
	  if ($xasignacion=='4') {
         $query .=" AND A.C_CLASIFICADOR_PRESUPUESTARIO LIKE '33.03%'";
      }
	  
      $dataset= mysql_query($query, $this->conector); 
      $this->nroregistros = mysql_num_rows($dataset);
      $rows = array();$pie = array();
      $pie[1]=$this->nroregistros." iniciativas visualizadas";
      for ($i=2;$i<10;$i++) {$pie[$i]=0;}
      while ($record = mysql_fetch_row($dataset)) { 
   $cols = array();
   $colsel=0;
   foreach ($record as $value) { 
      $cols[] = '"'.addslashes($value).'"';
      if ($colsel>1 && $colsel<10) {$pie[$colsel]=$pie[$colsel]+$value;}
          $colsel++;        
   } 
   $rows[] = "\t[".implode(",", $cols)."]"; 
   $fila++;
      } 
      $datagrilla="[\n".implode(",\n",$rows)."\n];\n"; 
      $rows = array();$cols = array();
      for ($j=0; $j<10; $j++) {
        if ($j>1) {$pie[$j]=number_format($pie[$j],0,',','.'); }
        $cols[] = '"'.addslashes($pie[$j]).'"'; 
      }
      $rows[] = "\t[".implode(",", $cols)."]"; 
      $this->footer=implode(",\n",$rows).";\n"; 
      return $datagrilla;
   }
   
   function cargar_grilla_inversiones_asignacion1($xnano, $xasignacion) {
      $fila=0;$this->filaseleccionada=-1;$total=0;
      /*$query  = " SELECT 
				  if (A.CODIGO!='',A.CODIGO,'') AS CODIGO, 
                  A.NOMBRE, 
                  B.C_ITEM_PRESUPUESTARIO, 
                  B.MONTO, 
                  A.C_INSTITUCION, 
                  A.C_PREINVERSION, 
                  A.C_FICHA, 
                  A.C_CLASIFICADOR_PRESUPUESTARIO, 
                  '0',
                  F.SIGLA_FUENTE_FINANCIAMIENTO,
                  A.SOLICITADO,
                  A.ASIGNADO,
                  A.SALDO_POR_ASIGNAR,
				  B.MONTO_DISMINUCION,
				  (CASE WHEN A.C_TIPO_CODIGO=1 THEN 'BIP' ELSE 'OTRO' END) AS TIPO_CODIGO,
				  B.SUBASIGNACION,
                  B.C_FUENTE_FINANCIAMIENTO
                  FROM INVERSION A, ASIGNACION_INVERSION B
                  LEFT JOIN FUENTE_FINANCIAMIENTO F ON(F.C_FUENTE_FINANCIAMIENTO = B.C_FUENTE_FINANCIAMIENTO)";
      $query .=" WHERE A.ANO=B.ANO AND A.C_INSTITUCION=B.C_INSTITUCION AND A.C_PREINVERSION=B.C_PREINVERSION AND A.C_FICHA=B.C_FICHA ";
      $query .=" AND A.ANO=".$xnano." AND B.C_ASIGNACION=".$xasignacion; */
	  
	  
	  
		$query ="SELECT ";
		$query .="TABLA_ASIGNACION.CODIGO, ";
		$query .="TABLA_ASIGNACION.NOMBRE, ";
		$query .="TABLA_ASIGNACION.C_ITEM_PRESUPUESTARIO, ";
		$query .="TABLA_ASIGNACION.MONTO, ";
		$query .="TABLA_ASIGNACION.C_INSTITUCION, ";
		$query .="TABLA_ASIGNACION.C_PREINVERSION, ";
		$query .="TABLA_ASIGNACION.C_FICHA, ";
		$query .="TABLA_ASIGNACION.C_CLASIFICADOR_PRESUPUESTARIO, ";
		$query .="'0', ";
		$query .="TABLA_ASIGNACION.SIGLA_FUENTE_FINANCIAMIENTO,  ";
		$query .="TABLA_ASIGNACION.SOLICITADO,  ";
		$query .="TABLA_ASIGNACION.ASIGNADO,  ";
		$query .="TABLA_ASIGNACION.SALDO_POR_ASIGNAR,  ";
		$query .="TABLA_ASIGNACION.MONTO_DISMINUCION,  ";
		$query .="TABLA_ASIGNACION.TIPO_CODIGO, ";
		$query .="TABLA_ASIGNACION.SUBASIGNACION, ";
		$query .="TABLA_ASIGNACION.C_FUENTE_FINANCIAMIENTO, ";
		$query .="CAST(TABLA_ASIGNACION.CLASIFICADOR_MARCO AS CHAR), ";
		
		
		//$query .="TABLA_MARCO.PRESUPUESTO_VIGENTE,  ";
		//$query .="(TABLA_MARCO.PRESUPUESTO_VIGENTE + TABLA_VARIACION_ASIGNADO.AUMENTO_AGREGADO - TABLA_VARIACION_ASIGNADO.DISMINUCION_AGREGADA) AS PRESUPUESTO_PROYECTADO, ";
		//$query .="TABLA_MARCO.TOTAL_ASIGNADO_CLASIFICADOR ";

		$query .="TABLA_MARCO.TOTAL_ASIGNADO_CLASIFICADOR, ";
		$query .="(TABLA_MARCO.TOTAL_ASIGNADO_CLASIFICADOR + TABLA_VARIACION_ASIGNADO.AUMENTO_AGREGADO - TABLA_VARIACION_ASIGNADO.DISMINUCION_AGREGADA) AS PRESUPUESTO_PROYECTADO, ";
		$query .="TABLA_MARCO.PRESUPUESTO_VIGENTE ";

		$query .="FROM ";
		$query .="( ";
		$query .="SELECT  ";
		$query .="if (A.CODIGO!='',A.CODIGO,'') AS CODIGO,  ";
		$query .="A.NOMBRE,  ";
		$query .="B.C_ITEM_PRESUPUESTARIO,  ";
		$query .="B.MONTO,  ";
		$query .="A.C_INSTITUCION,  ";
		$query .="A.C_PREINVERSION,  ";
		$query .="A.C_FICHA,  ";
		$query .="A.C_CLASIFICADOR_PRESUPUESTARIO,  ";
		$query .="F.SIGLA_FUENTE_FINANCIAMIENTO, ";
		$query .="A.SOLICITADO, ";
		$query .="A.ASIGNADO, ";
		$query .="A.SALDO_POR_ASIGNAR, ";
		$query .="B.MONTO_DISMINUCION, ";
		$query .="(CASE WHEN A.C_TIPO_CODIGO=1 THEN 'BIP' ELSE 'OTRO' END) AS TIPO_CODIGO, ";
		$query .="B.SUBASIGNACION, ";
		$query .="B.C_FUENTE_FINANCIAMIENTO, ";
		$query .="(CASE ";
		$query .="WHEN LEFT(B.C_ITEM_PRESUPUESTARIO,2)='31' THEN LEFT(B.C_ITEM_PRESUPUESTARIO, 5) ";
		$query .="ELSE B.C_ITEM_PRESUPUESTARIO ";
		$query .="END) AS CLASIFICADOR_MARCO ";
		$query .="FROM  ";
		$query .="INVERSION A ";
		$query .="JOIN ASIGNACION_INVERSION B ON ";
		$query .="A.ANO=B.ANO AND  ";
		$query .="A.C_INSTITUCION=B.C_INSTITUCION AND  ";
		$query .="A.C_PREINVERSION=B.C_PREINVERSION AND  ";
		$query .="A.C_FICHA=B.C_FICHA ";
		$query .="LEFT JOIN FUENTE_FINANCIAMIENTO F ON ";
		$query .="F.C_FUENTE_FINANCIAMIENTO = B.C_FUENTE_FINANCIAMIENTO ";
		$query .="WHERE  ";
		$query .="A.ANO=".$xnano." AND  ";
		$query .="B.C_ASIGNACION=".$xasignacion." ";
		$query .=") AS TABLA_ASIGNACION ";
		$query .="JOIN ( ";
		$query .="SELECT ";
		$query .="TMP_Presupuesto.TMP_P_numero AS CLASIFICADOR_PRESUPUESTARIO, ";
		$query .="(CASE ";
		$query .="WHEN TMP_Presupuesto.TMP_P_montoPreVige IS NULL THEN TMP_Presupuesto.TMP_P_montoPreIni*1000 ";
		$query .="ELSE TMP_Presupuesto.TMP_P_montoPreVige*1000 ";
		$query .="END) AS PRESUPUESTO_VIGENTE, ";
		$query .="(CASE ";
		$query .="WHEN SUM(INVERSION_FINANCIAMIENTO.ASIGNADO) IS NULL THEN 0 ";
		$query .="ELSE SUM(INVERSION_FINANCIAMIENTO.ASIGNADO) ";
		$query .="END) AS TOTAL_ASIGNADO_CLASIFICADOR ";
		$query .="FROM ";
		$query .="TMP_Presupuesto ";
		$query .="LEFT JOIN INVERSION_FINANCIAMIENTO ON ";
		$query .="TMP_Presupuesto.TMP_P_agno=INVERSION_FINANCIAMIENTO.ANO AND ";
		$query .="INVERSION_FINANCIAMIENTO.C_INSTITUCION=1 AND ";
		$query .="TMP_Presupuesto.TMP_P_numero=LEFT(INVERSION_FINANCIAMIENTO.C_CLASIFICADOR_PRESUPUESTARIO,2) ";
		$query .="WHERE ";
		$query .="TMP_Presupuesto.TMP_P_agno=".$xnano." AND ";
		$query .="TMP_Presupuesto.TMP_P_tipo=1 ";
		$query .="GROUP BY ";
		$query .="TMP_Presupuesto.TMP_P_id ";
		$query .="UNION ALL ";
		$query .="SELECT ";
		$query .="CONCAT(PPTO_PADRE.TMP_P_numero, '.', TMP_Presupuesto.TMP_P_numero) AS CLASIFICADOR_PRESUPUESTARIO, ";
		$query .="(CASE ";
		$query .="WHEN TMP_Presupuesto.TMP_P_montoPreVige IS NULL THEN TMP_Presupuesto.TMP_P_montoPreIni*1000 ";
		$query .="ELSE TMP_Presupuesto.TMP_P_montoPreVige*1000 ";
		$query .="END) AS PRESUPUESTO_VIGENTE, ";
		$query .="(CASE ";
		$query .="WHEN SUM(INVERSION_FINANCIAMIENTO.ASIGNADO) IS NULL THEN 0 ";
		$query .="ELSE SUM(INVERSION_FINANCIAMIENTO.ASIGNADO) ";
		$query .="END) AS TOTAL_ASIGNADO_CLASIFICADOR ";
		$query .="FROM ";
		$query .="TMP_Presupuesto ";
		$query .="LEFT JOIN TMP_Presupuesto AS PPTO_PADRE ON ";
		$query .="TMP_Presupuesto.TMP_P_idFrom=PPTO_PADRE.TMP_P_id ";
		$query .="LEFT JOIN INVERSION_FINANCIAMIENTO ON ";
		$query .="TMP_Presupuesto.TMP_P_agno=INVERSION_FINANCIAMIENTO.ANO AND ";
		$query .="INVERSION_FINANCIAMIENTO.C_INSTITUCION=1 AND ";
		$query .="CONCAT(PPTO_PADRE.TMP_P_numero, '.', TMP_Presupuesto.TMP_P_numero)=LEFT(INVERSION_FINANCIAMIENTO.C_CLASIFICADOR_PRESUPUESTARIO,5) ";
		$query .="WHERE ";
		$query .="TMP_Presupuesto.TMP_P_agno=".$xnano." AND ";
		$query .="TMP_Presupuesto.TMP_P_tipo=2 ";
		$query .="GROUP BY ";
		$query .="TMP_Presupuesto.TMP_P_id ";
		$query .="UNION ALL ";
		$query .="SELECT ";
		$query .="CONCAT(PPTO_PADRE_DE_PADRE.TMP_P_numero, '.', PPTO_PADRE.TMP_P_numero, '.', TMP_Presupuesto.TMP_P_numero) AS CLASIFICADOR_PRESUPUESTARIO, ";
		$query .="(CASE ";
		$query .="WHEN TMP_Presupuesto.TMP_P_montoPreVige IS NULL THEN TMP_Presupuesto.TMP_P_montoPreIni*1000 ";
		$query .="ELSE TMP_Presupuesto.TMP_P_montoPreVige*1000 ";
		$query .="END) AS PRESUPUESTO_VIGENTE, ";
		$query .="(CASE ";
		$query .="WHEN SUM(INVERSION_FINANCIAMIENTO.ASIGNADO) IS NULL THEN 0 ";
		$query .="ELSE SUM(INVERSION_FINANCIAMIENTO.ASIGNADO) ";
		$query .="END) AS TOTAL_ASIGNADO_CLASIFICADOR ";
		$query .="FROM ";
		$query .="TMP_Presupuesto ";
		$query .="LEFT JOIN TMP_Presupuesto AS PPTO_PADRE ON ";
		$query .="TMP_Presupuesto.TMP_P_idFrom=PPTO_PADRE.TMP_P_id ";
		$query .="LEFT JOIN TMP_Presupuesto AS PPTO_PADRE_DE_PADRE ON ";
		$query .="PPTO_PADRE.TMP_P_idFrom=PPTO_PADRE_DE_PADRE.TMP_P_id ";
		$query .="LEFT JOIN INVERSION_FINANCIAMIENTO ON ";
		$query .="TMP_Presupuesto.TMP_P_agno=INVERSION_FINANCIAMIENTO.ANO AND ";
		$query .="INVERSION_FINANCIAMIENTO.C_INSTITUCION=1 AND ";
		$query .="CONCAT(PPTO_PADRE_DE_PADRE.TMP_P_numero, '.', PPTO_PADRE.TMP_P_numero, '.', TMP_Presupuesto.TMP_P_numero)=LEFT(INVERSION_FINANCIAMIENTO.C_CLASIFICADOR_PRESUPUESTARIO,9) ";
		$query .="WHERE ";
		$query .="TMP_Presupuesto.TMP_P_agno=".$xnano." AND ";
		$query .="TMP_Presupuesto.TMP_P_tipo=3 ";
		$query .="GROUP BY ";
		$query .="TMP_Presupuesto.TMP_P_id ";
		$query .="ORDER BY ";
		$query .="CLASIFICADOR_PRESUPUESTARIO ";
		$query .=") AS TABLA_MARCO ON ";
		$query .="TABLA_ASIGNACION.CLASIFICADOR_MARCO=TABLA_MARCO.CLASIFICADOR_PRESUPUESTARIO ";
		$query .="LEFT JOIN ( ";
		$query .="SELECT ";
		$query .="(CASE ";
		$query .="WHEN LEFT(ASIGNACION_INVERSION.C_ITEM_PRESUPUESTARIO,2)='31' THEN LEFT(ASIGNACION_INVERSION.C_ITEM_PRESUPUESTARIO, 5) ";
		$query .="ELSE ASIGNACION_INVERSION.C_ITEM_PRESUPUESTARIO ";
		$query .="END) AS CLASIFICADOR_MARCO, ";
		$query .="SUM(ASIGNACION_INVERSION.MONTO) AS AUMENTO_AGREGADO, ";
		$query .="SUM(ASIGNACION_INVERSION.MONTO_DISMINUCION) AS DISMINUCION_AGREGADA ";
		$query .="FROM ";
		$query .="ASIGNACION_INVERSION ";
		$query .="WHERE ";
		$query .="ASIGNACION_INVERSION.ANO=".$xnano." AND ";
		$query .="ASIGNACION_INVERSION.C_ASIGNACION=".$xasignacion." ";
		$query .="GROUP BY ";
		$query .="CLASIFICADOR_MARCO ";
		$query .=") AS TABLA_VARIACION_ASIGNADO ON ";
		$query .="TABLA_ASIGNACION.CLASIFICADOR_MARCO=TABLA_VARIACION_ASIGNADO.CLASIFICADOR_MARCO ";


	  
      $dataset= mysql_query($query, $this->conector); 
      $this->nroregistros = mysql_num_rows($dataset);
      
	  
	  
	  
	  
	  /*
	  $rows = array();$pie = array();
	  $pie[1]=$this->nroregistros." iniciativas visualizadas";
      for ($i=2;$i<14;$i++) {$pie[$i]=0;}
      while ($record = mysql_fetch_row($dataset)) { 
		$cols = array();
		$colsel=0;
		foreach ($record as $value) { 
			$cols[] = '"'.addslashes($value).'"';
			if ($colsel==3 || $colsel==13) {$pie[$colsel]=$pie[$colsel]+$value;}
			$colsel++;        
		} 
		$rows[] = "\t[".implode(",", $cols)."]"; 
		$fila++;
      } 
      $datagrilla="[\n".implode(",\n",$rows)."\n];\n"; 
      $rows = array();$cols = array();
      $pie[3]=number_format($pie[3],0,'.',','); 
      $cols[] = '"'.addslashes($pie[3]).'"';
      $rows[] = "\t[".implode(",", $cols)."]"; 
      $this->footer=implode(",\n",$rows).";\n"; 
	  
      return $datagrilla;
	  
	  */
	  
	  

      $rows = array();$pie = array();
	  
	  if ($this->nroregistros==1) {
		$pie[1]=$this->nroregistros." solicitud de asignación visualizada";
	  } else {
	    $pie[1]=$this->nroregistros." solicitudes de asignación visualizadas";
	  }
      // for ($i=2;$i<10;$i++) {$pie[$i]=0;}
	  for ($i=2;$i<14;$i++) {$pie[$i]=0;}
      while ($record = mysql_fetch_row($dataset)) { 
		$cols = array();
		$colsel=0;
		foreach ($record as $value) { 
			$cols[] = '"'.addslashes($value).'"';
			// if ($colsel>1 && $colsel<10) {$pie[$colsel]=$pie[$colsel]+$value;}
			if ($colsel>1 && $colsel<14) {$pie[$colsel]=$pie[$colsel]+$value;}
			$colsel++;        
		} 
		$rows[] = "\t[".implode(",", $cols)."]"; 
		$fila++;
      }
	  
      $datagrilla="[\n".implode(",\n",$rows)."\n];\n"; 
      $rows = array();$cols = array();
      // for ($j=0; $j<10; $j++) {
	  for ($j=0; $j<14; $j++) {
        if ($j>1) {
			$pie[$j]=number_format($pie[$j],0,',','.'); 
		}
		$pie[2]='';
		$pie[9]='';
		
        $cols[] = '"'.addslashes($pie[$j]).'"'; 
      }
      $rows[] = "\t[".implode(",", $cols)."]"; 
      $this->footer=implode(",\n",$rows).";\n"; 
      return $datagrilla;
	  
   }

   function cargar_nombre_institucion($xinstitucion) {
      $query  =" SELECT N_INSTITUCION from INSTITUCION WHERE C_INSTITUCION=".$xinstitucion;
      $dataset  = mysql_query($query, $this->conector); 
      $this->nroregistros = mysql_num_rows($dataset);
      $record = @mysql_fetch_row($dataset);
      return $record[0];
   }

   function asignar_iniciativa() {
      /* INVERSION*/
      $query  =" UPDATE INVERSION SET C_USUARIO=".$this->usuario;
      $query .=" WHERE ANO= ".$this->ano." AND C_INSTITUCION=".$this->c_institucion." AND C_PREINVERSION=".$this->c_preinversion." AND C_FICHA=".$this->c_ficha;
      $dataset  = mysql_query($query, $this->conector); 
   }
   
   function eliminar_iniciativa_completa() {
      // BENEFICIARIOS
      $query  =" DELETE FROM BENEFICIARIO_INVERSION";
      $query .=" WHERE ANO= ".$this->ano." AND C_INSTITUCION=".$this->c_institucion." AND C_PREINVERSION=".$this->c_preinversion." AND C_FICHA=".$this->c_ficha;
      $dataset  = mysql_query($query, $this->conector); 
      // INVERSION
      $query  =" DELETE FROM INVERSION";
      $query .=" WHERE ANO= ".$this->ano." AND C_INSTITUCION=".$this->c_institucion." AND C_PREINVERSION=".$this->c_preinversion." AND C_FICHA=".$this->c_ficha;
      $dataset  = mysql_query($query, $this->conector); 
      // INVERSION CONFIGURACION
      $query  =" DELETE FROM INVERSION_CONFIGURACION";
      $query .=" WHERE ANO= ".$this->ano." AND C_INSTITUCION=".$this->c_institucion." AND C_PREINVERSION=".$this->c_preinversion." AND C_FICHA=".$this->c_ficha;
      $dataset  = mysql_query($query, $this->conector); 
      // CONTRATOS
      $query  =" DELETE FROM INVERSION_CONTRATO";
      $query .=" WHERE ANO= ".$this->ano." AND C_INSTITUCION=".$this->c_institucion." AND C_PREINVERSION=".$this->c_preinversion." AND C_FICHA=".$this->c_ficha;
      $dataset  = mysql_query($query, $this->conector); 
      // CONTRATOS MODIFICACION
      $query  =" DELETE FROM INVERSION_CONTRATO_MODIFICACION";
      $query .=" WHERE ANO= ".$this->ano." AND C_INSTITUCION=".$this->c_institucion." AND C_PREINVERSION=".$this->c_preinversion." AND C_FICHA=".$this->c_ficha;
      $dataset  = mysql_query($query, $this->conector); 
      // CONVENIOS
      $query  =" DELETE FROM INVERSION_CONVENIOS";
      $query .=" WHERE ANO= ".$this->ano." AND C_INSTITUCION=".$this->c_institucion." AND C_PREINVERSION=".$this->c_preinversion." AND C_FICHA=".$this->c_ficha;
      $dataset  = mysql_query($query, $this->conector); 
      // CONVENIOS DATOS
      $query  =" DELETE FROM INVERSION_CONVENIOS_DATOS";
      $query .=" WHERE ANO= ".$this->ano." AND C_INSTITUCION=".$this->c_institucion." AND C_PREINVERSION=".$this->c_preinversion." AND C_FICHA=".$this->c_ficha;
      $dataset  = mysql_query($query, $this->conector); 
      // DOCUMENTACION
      $query  =" DELETE FROM INVERSION_DOCUMENTACION";
      $query .=" WHERE ANO= ".$this->ano." AND C_INSTITUCION=".$this->c_institucion." AND C_PREINVERSION=".$this->c_preinversion." AND C_FICHA=".$this->c_ficha;
      $dataset  = mysql_query($query, $this->conector); 
      // FINANCIAMIENTO
      $query  =" DELETE FROM INVERSION_FINANCIAMIENTO";
      $query .=" WHERE ANO= ".$this->ano." AND C_INSTITUCION=".$this->c_institucion." AND C_PREINVERSION=".$this->c_preinversion." AND C_FICHA=".$this->c_ficha;
      $dataset  = mysql_query($query, $this->conector); 
      // GARANTIAS
      $query  =" DELETE FROM INVERSION_GARANTIA";
      $query .=" WHERE ANO= ".$this->ano." AND C_INSTITUCION=".$this->c_institucion." AND C_PREINVERSION=".$this->c_preinversion." AND C_FICHA=".$this->c_ficha;
      $dataset  = mysql_query($query, $this->conector); 
      // GARANTIAS CAMBIO DE ESTADO
      $query  =" DELETE FROM INVERSION_GARANTIA_CAMBIO_ESTADO";
      $query .=" WHERE ANO= ".$this->ano." AND C_INSTITUCION=".$this->c_institucion." AND C_PREINVERSION=".$this->c_preinversion." AND C_FICHA=".$this->c_ficha;
      $dataset  = mysql_query($query, $this->conector); 
      // GASTOS ADMINISTRATIVOS
      $query  =" DELETE FROM INVERSION_GASTO_ADMINISTRATIVO";
      $query .=" WHERE ANO= ".$this->ano." AND C_INSTITUCION=".$this->c_institucion." AND C_PREINVERSION=".$this->c_preinversion." AND C_FICHA=".$this->c_ficha;
      $dataset  = mysql_query($query, $this->conector); 
      // GASTOS ADMINISTRATIVOS DEVENGO
      $query  =" DELETE FROM INVERSION_GASTO_ADMINISTRATIVO_DEVENGO";
      $query .=" WHERE ANO= ".$this->ano." AND C_INSTITUCION=".$this->c_institucion." AND C_PREINVERSION=".$this->c_preinversion." AND C_FICHA=".$this->c_ficha;
      $dataset  = mysql_query($query, $this->conector); 
      // GASTOS ADMINISTRATIVOS FIRMA
      $query  =" DELETE FROM INVERSION_GASTO_ADMINISTRATIVO_FIRMA";
      $query .=" WHERE ANO= ".$this->ano." AND C_INSTITUCION=".$this->c_institucion." AND C_PREINVERSION=".$this->c_preinversion." AND C_FICHA=".$this->c_ficha;
      $dataset  = mysql_query($query, $this->conector); 
      // GASTOS ADMINISTRATIVOS OBSERVACIONES
      $query  =" DELETE FROM INVERSION_GASTO_ADMINISTRATIVO_OBSERVACIONES";
      $query .=" WHERE ANO= ".$this->ano." AND C_INSTITUCION=".$this->c_institucion." AND C_PREINVERSION=".$this->c_preinversion." AND C_FICHA=".$this->c_ficha;
      $dataset  = mysql_query($query, $this->conector);     
      // LICITACIONES
      $query  =" DELETE FROM INVERSION_LICITACION";
      $query .=" WHERE ANO= ".$this->ano." AND C_INSTITUCION=".$this->c_institucion." AND C_PREINVERSION=".$this->c_preinversion." AND C_FICHA=".$this->c_ficha;
      $dataset  = mysql_query($query, $this->conector); 
      // LICITACIONES EMPRESAS
      $query  =" DELETE FROM INVERSION_LICITACION_EMPRESA";
      $query .=" WHERE ANO= ".$this->ano." AND C_INSTITUCION=".$this->c_institucion." AND C_PREINVERSION=".$this->c_preinversion." AND C_FICHA=".$this->c_ficha;
      $dataset  = mysql_query($query, $this->conector); 
      // LICITACIONES ADJUDICACIONES
      $query  =" DELETE FROM INVERSION_LICITACION_ADJUDICACION";
      $query .=" WHERE ANO= ".$this->ano." AND C_INSTITUCION=".$this->c_institucion." AND C_PREINVERSION=".$this->c_preinversion." AND C_FICHA=".$this->c_ficha;
      $dataset  = mysql_query($query, $this->conector); 
      // MANO DE OBRA
      $query  =" DELETE FROM INVERSION_MANO_OBRA";
      $query .=" WHERE ANO= ".$this->ano." AND C_INSTITUCION=".$this->c_institucion." AND C_PREINVERSION=".$this->c_preinversion." AND C_FICHA=".$this->c_ficha;
      $dataset  = mysql_query($query, $this->conector); 
      // PAGOS
      $query  =" DELETE FROM INVERSION_PAGO";
      $query .=" WHERE ANO= ".$this->ano." AND C_INSTITUCION=".$this->c_institucion." AND C_PREINVERSION=".$this->c_preinversion." AND C_FICHA=".$this->c_ficha;
      $dataset  = mysql_query($query, $this->conector); 
      // PAGOS DEVENGO
      $query  =" DELETE FROM INVERSION_PAGO_DEVENGO";
      $query .=" WHERE ANO= ".$this->ano." AND C_INSTITUCION=".$this->c_institucion." AND C_PREINVERSION=".$this->c_preinversion." AND C_FICHA=".$this->c_ficha;
      $dataset  = mysql_query($query, $this->conector); 
      // PAGOS FIRMAS
      $query  =" DELETE FROM INVERSION_PAGO_FIRMA";
      $query .=" WHERE ANO= ".$this->ano." AND C_INSTITUCION=".$this->c_institucion." AND C_PREINVERSION=".$this->c_preinversion." AND C_FICHA=".$this->c_ficha;
      $dataset  = mysql_query($query, $this->conector); 
      // PAGOS OBSERVACIONES
      $query  =" DELETE FROM INVERSION_PAGO_OBSERVACIONES";
      $query .=" WHERE ANO= ".$this->ano." AND C_INSTITUCION=".$this->c_institucion." AND C_PREINVERSION=".$this->c_preinversion." AND C_FICHA=".$this->c_ficha;
      $dataset  = mysql_query($query, $this->conector);  
      // SITUACION
      $query  =" DELETE FROM INVERSION_SITUACION";
      $query .=" WHERE ANO= ".$this->ano." AND C_INSTITUCION=".$this->c_institucion." AND C_PREINVERSION=".$this->c_preinversion." AND C_FICHA=".$this->c_ficha;
      $dataset  = mysql_query($query, $this->conector); 
      // TRANSFERENCIAS
      $query  =" DELETE FROM INVERSION_TRANSFERENCIA";
      $query .=" WHERE ANO= ".$this->ano." AND C_INSTITUCION=".$this->c_institucion." AND C_PREINVERSION=".$this->c_preinversion." AND C_FICHA=".$this->c_ficha;
      $dataset  = mysql_query($query, $this->conector); 
      // TRANSFERENCIAS DEVENGO
      $query  =" DELETE FROM INVERSION_TRANSFERENCIA_DEVENGO";
      $query .=" WHERE ANO= ".$this->ano." AND C_INSTITUCION=".$this->c_institucion." AND C_PREINVERSION=".$this->c_preinversion." AND C_FICHA=".$this->c_ficha;
      $dataset  = mysql_query($query, $this->conector); 
      // TRANSFERENCIAS FIRMAS
      $query  =" DELETE FROM INVERSION_TRANSFERENCIA_FIRMA";
      $query .=" WHERE ANO= ".$this->ano." AND C_INSTITUCION=".$this->c_institucion." AND C_PREINVERSION=".$this->c_preinversion." AND C_FICHA=".$this->c_ficha;
      $dataset  = mysql_query($query, $this->conector); 
      // TRANSFERENCIAS OBSERVACIONES
      $query  =" DELETE FROM INVERSION_TRANSFERENCIA_OBSERVACIONES";
      $query .=" WHERE ANO= ".$this->ano." AND C_INSTITUCION=".$this->c_institucion." AND C_PREINVERSION=".$this->c_preinversion." AND C_FICHA=".$this->c_ficha;
      $dataset  = mysql_query($query, $this->conector);  
      // RELACION INVERSION ARRASTRE
      $query  = " DELETE FROM RELACION_INVERSION_ARRASTRE";
      $query .=" WHERE ANO= ".$this->ano." AND C_INSTITUCION=".$this->c_institucion." AND C_INICIATIVA=".$this->c_preinversion." AND C_FICHA=".$this->c_ficha;
      $dataset  = mysql_query($query, $this->conector); 
      // RELACION INSTRUMENTOS
      $query  = " DELETE FROM RELACION_INSTRUMENTO_INICIATIVA";
      $query .=" WHERE ANO= ".$this->ano." AND C_INSTITUCION=".$this->c_institucion." AND C_INICIATIVA=".$this->c_preinversion." AND C_FICHA=".$this->c_ficha;
      $dataset  = mysql_query($query, $this->conector); 
      // UBICACION TERRITORIAL
      $query  = " DELETE FROM UBICACION_TERRITORIAL_INICIATIVA";
      $query .=" WHERE ANO= ".$this->ano." AND C_INSTITUCION=".$this->c_institucion." AND C_INICIATIVA=".$this->c_preinversion." AND C_FICHA=".$this->c_ficha;
      $dataset  = mysql_query($query, $this->conector); 
      // RELACION PROPIR
      $query  = " DELETE FROM RELACION_INVERSION_PROPIR";
      $query .=" WHERE ANO= ".$this->ano." AND C_INSTITUCION=".$this->c_institucion." AND C_PREINVERSION=".$this->c_preinversion." AND C_FICHA=".$this->c_ficha;
      $dataset  = mysql_query($query, $this->conector); 
      if (mysql_errno($BaseDatos->conector) != 0) { 
         $this->mensaje_error="No se ha podido Actualizar la Base de Datos, mensaje de error de Mysql es: " . mysql_error($BaseDatos->conector).$query; 
      } else {$this->mensaje_error='';}   
      return $this->mensaje_error;
   }   

   function get_nroregistros(){ return $this->nroregistros;}  
   
   function get_error()       { return $this->mensaje_error;}
        
   function get_filaseleccionada()       { return $this->filaseleccionada;}

   function get_estados_garantia(){
      $query = sprintf("SELECT * FROM GARANTIA_ESTADO");
      $dataset  = mysql_query($query, $this->conector);

      while ($record = mysql_fetch_row($dataset)) {
         $result[] = $record;
      }
      return $result;
   }
}

//**********************************************************************

// UBICACION TERRITORIAL INICIATIVA
   
Class Ubicacion_Territorial_Iniciativa {
   var $conector;
   var $mensaje_error;
   var $nroregistros;
   var $ano;  
   var $c_institucion;
   var $c_iniciativa;
   var $c_ficha;
   var $c_nivel_ut;
   var $ubicacion_general="";
   var $ubicacion_especifica="";
   var $ubicacion_especifica_nombre="";
   var $area_influencia;
   var $usuario;
   var $estado;
   
   function Ubicacion_Territorial_Iniciativa($conector) { 
      $this->conector =$conector;
      $this->mensaje_error='';
      $this->estado=0;
      $this->nroregistros=0;
   }
   
   function cargar_ubicacion_territorial() {
      $query  =" SELECT C_NIVEL_UT, UBICACION_GENERAL, UBICACION_ESPECIFICA, UBICACION_ESPECIFICA_NOMBRE, AREA_INFLUENCIA ";
      $query .=" FROM UBICACION_TERRITORIAL_INICIATIVA ";
      $query .=" WHERE C_FICHA=".$this->c_ficha." AND C_INICIATIVA=".$this->c_iniciativa." AND C_INSTITUCION= ".$this->c_institucion;
      $query .=" AND ANO= ".$this->ano;
      $dataset  = mysql_query($query, $this->conector); 
      $this->nroregistros = mysql_num_rows($dataset);
      return $dataset;
   }

   function eliminar_ubicacion_territorial() {
      $query  = " DELETE FROM UBICACION_TERRITORIAL_INICIATIVA";
      $query .= " WHERE ANO=".$this->ano." AND C_INSTITUCION=".$this->c_institucion;
      $query .= " AND C_INICIATIVA=".$this->c_iniciativa." AND C_FICHA=".$this->c_ficha;
      $dataset  = mysql_query($query, $this->conector); 
      if (mysql_errno($this->conector) != 0) { 
         $this->mensaje_error="No se ha podido Actualizar la Base de Datos, mensaje de error de Mysql es: " . mysql_error($this->conector).$query; 
      } else {$this->mensaje_error='';}   
      return $this->mensaje_error;
   }
   
   function actualizar_ubicacion_territorial() {
      $query  = " REPLACE INTO UBICACION_TERRITORIAL_INICIATIVA (ANO, C_INSTITUCION, C_INICIATIVA, C_FICHA, C_NIVEL_UT, UBICACION_GENERAL, UBICACION_ESPECIFICA, UBICACION_ESPECIFICA_NOMBRE, AREA_INFLUENCIA) VALUES(";
      $query .= "'".$this->ano."','".$this->c_institucion."','".$this->c_iniciativa."','".$this->c_ficha."',";
      $query .= "'".$this->c_nivel_ut."','".$this->ubicacion_general."','".$this->ubicacion_especifica."',";
      $query .= "'".$this->ubicacion_especifica_nombre."','".$this->area_influencia."')"; 
      $dataset  = mysql_query($query, $this->conector);
      if (mysql_errno($this->conector) != 0) { 
         $this->mensaje_error="No se ha podido Actualizar la Base de Datos, mensaje de error de Mysql es: " . mysql_error($this->conector).$query; 
      } else {$this->mensaje_error='';}   
      return $this->mensaje_error;
   }

   function get_nroregistros(){ return $this->nroregistros;}  
   
   function get_error()       { return $this->mensaje_error;}
      
}

//**********************************************************************

// BENEFICIARIOS INVERSIONES
   
Class Beneficiario_Inversion {
   var $conector;
   var $mensaje_error;
   var $nroregistros;
   var $ano;  
   var $c_institucion;
   var $c_preinversion;
   var $c_ficha;
   var $c_grupo_beneficiario;
   var $hombres;
   var $mujeres;
   var $ambos;
   var $indirectos;
   var $usuario;
   var $estado;
   var $vector="";
   
   function Beneficiario_Inversion($conector) { 
      $this->conector =$conector;
      $this->mensaje_error='';
      $this->estado=0;
      $this->nroregistros=0;
   }
   
   function cargar_beneficiarios() {
      $query  =" SELECT B.N_GRUPO_BENEFICIARIO,A.HOMBRES,A.MUJERES, A.AMBOS, A.MUJERES+A.HOMBRES+A.AMBOS,A.INDIRECTOS, B.C_GRUPO_BENEFICIARIO ";
      $query .=" FROM GRUPO_BENEFICIARIO B, BENEFICIARIO_INVERSION A WHERE B.C_GRUPO_BENEFICIARIO=A.C_GRUPO_BENEFICIARIO "; 
      $query .=" AND A.ANO= ".$this->ano." AND A.C_INSTITUCION=".$this->c_institucion;
      $query .=" AND A.C_PREINVERSION=".$this->c_preinversion." AND A.C_FICHA=".$this->c_ficha;
      $dataset  = mysql_query($query, $this->conector); 
      $this->nroregistros = mysql_num_rows($dataset);
      return $dataset;
   }

   function eliminar_beneficiarios() {
      $query  = " DELETE FROM BENEFICIARIO_INVERSION";
      $query .= " WHERE ANO=".$this->ano." AND C_INSTITUCION=".$this->c_institucion;
      $query .= " AND C_PREINVERSION=".$this->c_preinversion." AND C_FICHA=".$this->c_ficha;
      $dataset  = mysql_query($query, $this->conector); 
      if (mysql_errno($this->conector) != 0) { 
         $this->mensaje_error="No se ha podido Actualizar la Base de Datos, mensaje de error de Mysql es: " . mysql_error($this->conector).$query; 
      } else {$this->mensaje_error='';}   
      return $this->mensaje_error;
   }
   
   function actualizar_beneficiarios() {
      $query  =" REPLACE INTO BENEFICIARIO_INVERSION VALUES ('";
      $query .= $this->ano."','".$this->c_institucion."','".$this->c_preinversion."','".$this->c_ficha."',".$this->vector.")";
      $dataset  = mysql_query($query, $this->conector);
      if (mysql_errno($this->conector) != 0) { 
         $this->mensaje_error="No se ha podido Actualizar la Base de Datos, mensaje de error de Mysql es: " . mysql_error($this->conector).$query; 
      } else {$this->mensaje_error='';}   
      return $this->mensaje_error;
   }

   function get_nroregistros(){ return $this->nroregistros;}  
   
   function get_error()       { return $this->mensaje_error;}
      
}

//**********************************************************************

// FINANCIAMIENTO INVERSIONES
   
Class Inversion_Financiamiento {
   var $conector;
   var $mensaje_error;
   var $nroregistros;
   var $ano;  
   var $c_institucion;
   var $c_preinversion;
   var $c_ficha;
   var $c_fuente_financiamiento="";
   var $c_clasificador_presupuestario="";
   var $p1;
   var $p2;
   var $p3;
   var $p4;
   var $p5;
   var $p6;
   var $p7;
   var $p8;
   var $p9;
   var $p10;
   var $p11;
   var $p12;
   var $g1;
   var $g2;
   var $g3;
   var $g4;
   var $g5;
   var $g6;
   var $g7;
   var $g8;
   var $g9;
   var $g10;
   var $g11;
   var $g12;
   var $costo_ebi;
   var $costo_core;
   var $costo_total;
   var $gastado_anos_anteriores;
   var $solicitado;
   var $saldo_proximo_ano;
   var $saldo_proximos_anos;
   var $asignado;
   var $total_programado;
   var $contratado;
   var $pagado;
   var $saldo;
   var $arrastre;
   var $c_usuario;
   var $fecha_registro;
   var $asignacion_disponible;
   var $saldo_por_asignar;
   var $c_estado;
   var $vector="";
   
   function Inversion_Financiamiento($conector) { 
      $this->conector =$conector;
      $this->mensaje_error='';
      $this->estado=0;
      $this->nroregistros=0;
   }
   
  
   function getSolicitadoAgno(){
   	
   	
		/*JCalderón 2013-04-08, función que obtiene solicitado año para que cuando se cree una asignación nueva valide montos*/
		$query	=	"SELECT A.SOLICITADO
					FROM FUENTE_FINANCIAMIENTO B, TCLASIFICADOR_PRESUPUESTARIO C, INVERSION_FINANCIAMIENTO A 
					WHERE 	A.C_FUENTE_FINANCIAMIENTO=B.C_FUENTE_FINANCIAMIENTO AND 
					A.C_CLASIFICADOR_PRESUPUESTARIO=C.C_CLASIFICADOR AND 
					A.ANO=C.ANIO AND (A.C_INSTITUCION=C.INSTITUCION OR C.INSTITUCION=0) AND 
					A.ANO= ".$this->ano." AND 
					A.C_INSTITUCION=".$this->c_institucion." AND 
					A.C_PREINVERSION=".$this->c_preinversion." AND 
					A.C_FICHA=".$this->c_ficha." AND
					
					B.SIGLA_FUENTE_FINANCIAMIENTO = '".$this->c_fuente_financiamiento."' AND
					C.C_CLASIFICADOR = '".$this->c_clasificador_presupuestario."'";
		
		$dataset  = mysql_query($query, $this->conector); 
		return $dataset;
		/*$query2 = "SELECT B.SOLICITADO, B.ASIGNADO, B.SALDO_POR_ASIGNAR, A.MONTO FROM ASIGNACION_INVERSION A 
              LEFT JOIN INVERSION B ON (A.ANO = B.ANO AND A.C_PREINVERSION = B.C_PREINVERSION and A.C_INSTITUCION = B.C_INSTITUCION)
              WHERE A.ANO= ".$this->ano." AND A.C_INSTITUCION=".$this->c_institucion." AND A.C_PREINVERSION=".$this->c_preinversion." AND 
					    A.C_FICHA=".$this->c_ficha;
   	$dataset2 = mysql_query($query2, $this->conector);
   	while($rec = mysql_fetch_row($dataset2)){
   		
   		
      $solci = $rec[0];
      $asigna = $rec[1];
      $salxasig= $rec[2];
      $monto  = $rec[3];
      
   	}
   	
   	echo "Solicitado:".$solci." Asignado:".$asigna." Saldo x Asignar:".$salxasig." Monto:".$monto;
		$difsolci = $monto+$asigna;
		if($monto <= ($salxasig+999)){
		      if( $difsolci <= ($solci+999)){
		      
		      return $monto; 
		      
		      }
		}else {
		      
		      return false;
		      
		     }
		      
		
   	*/
	}
	
	function getSolicitado(){
	
		$query ="SELECT A.SOLICITADO FROM INVERSION_FINANCIAMIENTO A
    LEFT JOIN FUENTE_FINANCIAMIENTO B ON (A.C_FUENTE_FINANCIAMIENTO = B.C_FUENTE_FINANCIAMIENTO)
    LEFT JOIN TCLASIFICADOR_PRESUPUESTARIO C ON (A.C_CLASIFICADOR_PRESUPUESTARIO = C.C_CLASIFICADOR AND A.ANO = C.ANIO)
    WHERE A.ANO =".$this->ano." AND A.C_INSTITUCION =".$this->c_institucion." AND A.C_PREINVERSION =".$this->c_preinversion." 
    AND A.C_FICHA=".$this->c_ficha." AND C.C_CLASIFICADOR = '".$this->c_item2."'";
		$dataset  = mysql_query($query, $this->conector); 
		$record = mysql_fetch_row($dataset);
		
		return $record[0];
		
	}
	
	function getAsignado(){
	
	$query ="SELECT A.ASIGNADO FROM INVERSION_FINANCIAMIENTO A
    LEFT JOIN FUENTE_FINANCIAMIENTO B ON (A.C_FUENTE_FINANCIAMIENTO = B.C_FUENTE_FINANCIAMIENTO)
    LEFT JOIN TCLASIFICADOR_PRESUPUESTARIO C ON (A.C_CLASIFICADOR_PRESUPUESTARIO = C.C_CLASIFICADOR AND A.ANO = C.ANIO)
    WHERE A.ANO =".$this->ano." AND A.C_INSTITUCION =".$this->c_institucion." AND A.C_PREINVERSION =".$this->c_preinversion." 
    AND A.C_FICHA=".$this->c_ficha." AND C.C_CLASIFICADOR = '".$this->c_item2."'";
		
		$dataset  = mysql_query($query, $this->conector); 
		$record = mysql_fetch_row($dataset);
		
		return $record[0];
		
	}
	function getXasignar(){
	
	$query ="SELECT A.SALDO_POR_ASIGNAR FROM INVERSION_FINANCIAMIENTO A
    LEFT JOIN FUENTE_FINANCIAMIENTO B ON (A.C_FUENTE_FINANCIAMIENTO = B.C_FUENTE_FINANCIAMIENTO)
    LEFT JOIN TCLASIFICADOR_PRESUPUESTARIO C ON (A.C_CLASIFICADOR_PRESUPUESTARIO = C.C_CLASIFICADOR AND A.ANO = C.ANIO)
    WHERE A.ANO =".$this->ano." AND A.C_INSTITUCION =".$this->c_institucion." AND A.C_PREINVERSION =".$this->c_preinversion." 
    AND A.C_FICHA=".$this->c_ficha." AND C.C_CLASIFICADOR = '".$this->c_item2."'";
		
		$dataset  = mysql_query($query, $this->conector); 
		$record = mysql_fetch_row($dataset);
		
		return $record[0];
	}
	
	function getFuenteFinanciamiento() {
	
	$query ="SELECT B.N_FUENTE_FINANCIAMIENTO FROM INVERSION_FINANCIAMIENTO A
    LEFT JOIN FUENTE_FINANCIAMIENTO B ON (A.C_FUENTE_FINANCIAMIENTO = B.C_FUENTE_FINANCIAMIENTO)
    LEFT JOIN TCLASIFICADOR_PRESUPUESTARIO C ON (A.C_CLASIFICADOR_PRESUPUESTARIO = C.C_CLASIFICADOR AND A.ANO = C.ANIO)
    WHERE A.ANO =".$this->ano." AND A.C_INSTITUCION =".$this->c_institucion." AND A.C_PREINVERSION =".$this->c_preinversion." 
    AND A.C_FICHA=".$this->c_ficha." AND C.C_CLASIFICADOR = '".$this->c_item2."'";
		
		$dataset  = mysql_query($query, $this->conector); 
		$record = mysql_fetch_row($dataset);
		
		return $record[0];
	
	}
	
	function getCodigoFuenteFinanciamiento() {
	
	$query ="SELECT B.C_FUENTE_FINANCIAMIENTO FROM INVERSION_FINANCIAMIENTO A
    LEFT JOIN FUENTE_FINANCIAMIENTO B ON (A.C_FUENTE_FINANCIAMIENTO = B.C_FUENTE_FINANCIAMIENTO)
    LEFT JOIN TCLASIFICADOR_PRESUPUESTARIO C ON (A.C_CLASIFICADOR_PRESUPUESTARIO = C.C_CLASIFICADOR AND A.ANO = C.ANIO)
    WHERE A.ANO =".$this->ano." AND A.C_INSTITUCION =".$this->c_institucion." AND A.C_PREINVERSION =".$this->c_preinversion." 
    AND A.C_FICHA=".$this->c_ficha." AND C.C_CLASIFICADOR = '".$this->c_item2."'";
		
		$dataset  = mysql_query($query, $this->conector); 
		$record = mysql_fetch_row($dataset);
		
		return $record[0];
	
	}
	   
	function getTotalAsignadoClasificador() {
	
		$query ="SELECT SUM(ASIGNADO) FROM INVERSION_FINANCIAMIENTO WHERE ANO=".$this->ano." AND C_INSTITUCION=".$this->c_institucion." AND C_CLASIFICADOR_PRESUPUESTARIO LIKE '".$this->c_item2."%'";
		
		$dataset  = mysql_query($query, $this->conector); 
		$record = mysql_fetch_row($dataset);
		
		return $record[0];
	}
	
	function getPptoVigenteMarco($ano_ppto, $clasificador_ppto) {
	
		$query ="
			SELECT
				TABLA_VIGENTE_MARCO.PRESUPUESTO_VIGENTE
			FROM (
				SELECT
					TMP_Presupuesto.TMP_P_numero AS CLASIFICADOR_PRESUPUESTARIO,
					(CASE
						WHEN TMP_Presupuesto.TMP_P_montoPreVige IS NULL THEN TMP_Presupuesto.TMP_P_montoPreIni*1000
						ELSE TMP_Presupuesto.TMP_P_montoPreVige*1000
					END) AS PRESUPUESTO_VIGENTE
					
				FROM
					TMP_Presupuesto
				WHERE
					TMP_Presupuesto.TMP_P_agno=".$ano_ppto." AND
					TMP_Presupuesto.TMP_P_tipo=1
				GROUP BY
					TMP_Presupuesto.TMP_P_id
					
				UNION ALL

				SELECT
					CONCAT(PPTO_PADRE.TMP_P_numero, '.', TMP_Presupuesto.TMP_P_numero) AS CLASIFICADOR_PRESUPUESTARIO,
					(CASE
						WHEN TMP_Presupuesto.TMP_P_montoPreVige IS NULL THEN TMP_Presupuesto.TMP_P_montoPreIni*1000
						ELSE TMP_Presupuesto.TMP_P_montoPreVige*1000
					END) AS PRESUPUESTO_VIGENTE
					
				FROM
					TMP_Presupuesto
					LEFT JOIN TMP_Presupuesto AS PPTO_PADRE ON
						TMP_Presupuesto.TMP_P_idFrom=PPTO_PADRE.TMP_P_id
				WHERE
					TMP_Presupuesto.TMP_P_agno=".$ano_ppto." AND
					TMP_Presupuesto.TMP_P_tipo=2
				GROUP BY
					TMP_Presupuesto.TMP_P_id 
				UNION ALL

				SELECT
					CONCAT(PPTO_PADRE_DE_PADRE.TMP_P_numero, '.', PPTO_PADRE.TMP_P_numero, '.', TMP_Presupuesto.TMP_P_numero) AS CLASIFICADOR_PRESUPUESTARIO,
					(CASE
						WHEN TMP_Presupuesto.TMP_P_montoPreVige IS NULL THEN TMP_Presupuesto.TMP_P_montoPreIni*1000
						ELSE TMP_Presupuesto.TMP_P_montoPreVige*1000
					END) AS PRESUPUESTO_VIGENTE
					
				FROM
					TMP_Presupuesto
					LEFT JOIN TMP_Presupuesto AS PPTO_PADRE ON
						TMP_Presupuesto.TMP_P_idFrom=PPTO_PADRE.TMP_P_id
					LEFT JOIN TMP_Presupuesto AS PPTO_PADRE_DE_PADRE ON
						PPTO_PADRE.TMP_P_idFrom=PPTO_PADRE_DE_PADRE.TMP_P_id
				WHERE
					TMP_Presupuesto.TMP_P_agno=".$ano_ppto." AND
					TMP_Presupuesto.TMP_P_tipo=3
				GROUP BY
					TMP_Presupuesto.TMP_P_id
				ORDER BY
					CLASIFICADOR_PRESUPUESTARIO
			) AS TABLA_VIGENTE_MARCO
			WHERE
				TABLA_VIGENTE_MARCO.CLASIFICADOR_PRESUPUESTARIO='".$clasificador_ppto."'";
		
		$dataset  = mysql_query($query, $this->conector); 
		$record = mysql_fetch_row($dataset);
		
		return $record[0];
	}
	
	
	   
   function cargar_financiamiento() {
      $query  =" SELECT 
         B.N_FUENTE_FINANCIAMIENTO,
         CONCAT(C.N_CLASIFICADOR,' (',C.C_CLASIFICADOR,')'), 
         A.COSTO_EBI, 
         A.COSTO_CORE, 
         A.COSTO_TOTAL, 
         A.GASTADO_ANOS_ANTERIORES, 
         A.SOLICITADO, 
         A.SALDO_PROXIMO_ANO,
         A.SALDO_PROXIMOS_ANOS, 
         A.ASIGNADO,
         '',
         '', 
         A.CONTRATADO,
         A.PAGADO, 
         A.ARRASTRE,
         A.P1,
         A.G1,
         A.P2,
         A.G2,
         A.P3,
         A.G3,
         A.P4,
         A.G4,
         A.P5,
         A.G5,
         A.P6,
         A.G6,
         A.P7,
         A.G7,
         A.P8,
         A.G8,
         A.P9,
         A.G9,
         A.P10,
         A.G10,
         A.P11,
         A.G11,
         A.P12,
         A.G12,
		 A.P13,
         A.C_ESTADO,
         A.C_FUENTE_FINANCIAMIENTO,
         A.C_CLASIFICADOR_PRESUPUESTARIO,
         '' 
      FROM FUENTE_FINANCIAMIENTO B, TCLASIFICADOR_PRESUPUESTARIO C, INVERSION_FINANCIAMIENTO A 
      WHERE A.C_FUENTE_FINANCIAMIENTO=B.C_FUENTE_FINANCIAMIENTO AND A.C_CLASIFICADOR_PRESUPUESTARIO=C.C_CLASIFICADOR AND A.ANO=C.ANIO AND (A.C_INSTITUCION=C.INSTITUCION OR C.INSTITUCION=0)
      AND A.ANO= ".$this->ano." AND A.C_INSTITUCION=".$this->c_institucion." AND A.C_PREINVERSION=".$this->c_preinversion."
      AND A.C_FICHA=".$this->c_ficha."
      ORDER BY A.C_FUENTE_FINANCIAMIENTO, A.C_CLASIFICADOR_PRESUPUESTARIO";
      $dataset  = mysql_query($query, $this->conector); 
      $this->nroregistros = mysql_num_rows($dataset);
      $nrocolumnas = mysql_num_fields($dataset);
      $rows = array();$canti=0; 
      while ($record = mysql_fetch_row($dataset)) { 
   $cols = array();$columna=0;
   foreach ($record as $value) { 
            $cols[] = '"'.addslashes($value).'"';
            $columna=$columna+1;
            if ($columna==$nrocolumnas) {
              $value1="";
              $cols[] = '"'.addslashes($value1).'"';
              $value1="";
              $cols[] = '"'.addslashes($value1).'"';
              $value1="";
              $cols[] = '"'.addslashes($value1).'"';
                $query1  = " SELECT SUM(A.ANTICIPO), SUM(A.ANTICIPO_DEVUELTO), SUM(A.ANTICIPO_SALDO), SUM(A.RETENCION), SUM(A.RETENCION_DEVUELTO), SUM(A.RETENCION_SALDO), SUM(A.ANTICIPO_ENTREGADO), SUM(A.RETENCION_DEVOLUCION) ";
                $query1 .= "  FROM INVERSION_CONTRATO A LEFT JOIN TCONTRATISTA D ON (A.RUT_CONTRATISTA=D.RUN)";
                $query1 .= " WHERE A.ANO= ".$this->ano." AND A.C_INSTITUCION=".$this->c_institucion;
                $query1 .= " AND A.C_PREINVERSION=".$this->c_preinversion." AND A.C_FICHA=".$this->c_ficha;
                $query1 .= " AND A.C_CLASIFICADOR='".$record[41]."' AND A.C_ESTADO IN (2,3,4,9)";
                $dataset1  = mysql_query($query1, $this->conector); 
                $record1 = mysql_fetch_row($dataset1);
          foreach ($record1 as $value1) { 
                   $cols[] = '"'.addslashes(number_format($value1,0,',','.')).'"';
                }
             }    
   } 
   $rows[] = "\t[".implode(",", $cols)."]"; 
   $canti=$canti+1;
      } 
      for ($i=$canti; $i<10; $i++) {
   $rows[] = "\t['','','','','','','','','','','','','','','','','','','','','','','','','','','','','','','','','','','','','','','','','','','','','','','','','','']"; 
      }   
      $datagrilla="[\n".implode(",\n",$rows)."\n];\n"; 
      return $datagrilla;
   }
   
   function cargar_financiamiento_asignacion() {
      $query  =" SELECT 
          B.N_FUENTE_FINANCIAMIENTO,
         CONCAT(C.N_CLASIFICADOR,' (',C.C_CLASIFICADOR,')'), 
         A.COSTO_EBI, 
         A.COSTO_CORE, 
         A.COSTO_TOTAL, 
         A.GASTADO_ANOS_ANTERIORES, 
         A.SOLICITADO, 
         A.SALDO_PROXIMO_ANO,
         A.SALDO_PROXIMOS_ANOS, 
         A.ASIGNADO,
         '',
         '', 
         A.CONTRATADO,
         A.PAGADO, 
         A.ARRASTRE,
         A.P1,
         A.G1,
         A.P2,
         A.G2,
         A.P3,
         A.G3,
         A.P4,
         A.G4,
         A.P5,
         A.G5,
         A.P6,
         A.G6,
         A.P7,
         A.G7,
         A.P8,
         A.G8,
         A.P9,
         A.G9,
         A.P10,
         A.G10,
         A.P11,
         A.G11,
         A.P12,
         A.G12,
         A.TOTAL_PROGRAMADO, 
         A.PAGADO, 
         A.SALDO 
      
      FROM FUENTE_FINANCIAMIENTO B, TCLASIFICADOR_PRESUPUESTARIO C, INVERSION_FINANCIAMIENTO A 
      WHERE A.C_FUENTE_FINANCIAMIENTO=B.C_FUENTE_FINANCIAMIENTO AND A.C_CLASIFICADOR_PRESUPUESTARIO=C.C_CLASIFICADOR AND A.ANO=C.ANIO AND (A.C_INSTITUCION=C.INSTITUCION OR C.INSTITUCION=0)
      AND A.ANO= ".$this->ano." AND A.C_INSTITUCION=".$this->c_institucion." AND A.C_PREINVERSION=".$this->c_preinversion."
      AND A.C_FICHA=".$this->c_ficha."
      ORDER BY A.C_FUENTE_FINANCIAMIENTO, A.C_CLASIFICADOR_PRESUPUESTARIO";

      $dataset  = mysql_query($query, $this->conector); 
      $this->nroregistros = mysql_num_rows($dataset);
      $rows = array();$canti=0; 
      $pie[0]="Totales";$pie[1]="";
      while ($record = mysql_fetch_row($dataset)) { 
   $cols = array();$columna=0;
   foreach ($record as $value) { 
            if ($columna>1) {$pie[$columna]=$pie[$columna]+$value;}
            $cols[] = '"'.addslashes($value).'"';
      $columna=$columna+1;
   } 
   $rows[] = "\t[".implode(",", $cols)."]"; 
   $canti=$canti+1;
      } 
      $datagrilla="[\n".implode(",\n",$rows)."\n];\n"; 
      $rows = array();$cols = array();
      for ($j=0; $j<31; $j++) {
        if ($j>1) {$pie[$j]=number_format($pie[$j],0,'.',','); }
        $cols[] = '"'.addslashes($pie[$j]).'"'; 
      }
      $rows[] = "\t[".implode(",", $cols)."]"; 
      $this->footer=implode(",\n",$rows).";\n"; 
      return $datagrilla;
   }
   
   function eliminar_financiamiento() {
      $query  =" DELETE FROM INVERSION_FINANCIAMIENTO WHERE C_FICHA=".$this->c_ficha." AND C_PREINVERSION=".$this->c_preinversion." AND ";
      $query .=" C_INSTITUCION=".$this->c_institucion." AND ANO=".$this->ano;
      $dataset  = mysql_query($query, $this->conector);
      if (mysql_errno($this->conector) != 0) { 
         $this->mensaje_error="No se ha podido Actualizar la Base de Datos, mensaje de error de Mysql es: " . mysql_error($this->conector).$query; 
      } else {$this->mensaje_error='';}   
      return $this->mensaje_error;
   }

   function actualizar_financiamiento() {
      $query  = "REPLACE INTO INVERSION_FINANCIAMIENTO(ANO, C_INSTITUCION, C_PREINVERSION, C_FICHA,C_FUENTE_FINANCIAMIENTO, C_CLASIFICADOR_PRESUPUESTARIO, ";
      $query .= "P1, P2, P3, P4, P5, P6, P7, P8, P9, P10, P11, P12, ";      
      $query .= "G1, G2, G3, G4, G5, G6, G7, G8, G9, G10, G11, G12, ";
      $query .= "COSTO_EBI, COSTO_CORE, COSTO_TOTAL, GASTADO_ANOS_ANTERIORES, SOLICITADO, SALDO_PROXIMO_ANO, SALDO_PROXIMOS_ANOS, ASIGNADO, ASIGNACION_DISPONIBLE, SALDO_POR_ASIGNAR, TOTAL_PROGRAMADO, CONTRATADO, ";
      $query .= "PAGADO, SALDO, ARRASTRE, FECHA_REGISTRO, C_USUARIO, C_ESTADO) VALUES('";
      $query .= $this->ano."','".$this->c_institucion."','".$this->c_preinversion."','".$this->c_ficha."','".$this->c_fuente_financiamiento."','".$this->c_clasificador_presupuestario."','";
      $query .= $this->p1."','".$this->p2."','".$this->p3."','".$this->p4."','".$this->p5."','".$this->p6."','".$this->p7."','".$this->p8."','".$this->p9."','".$this->p10."','".$this->p11."','".$this->p12."','";
      $query .= $this->g1."','".$this->g2."','".$this->g3."','".$this->g4."','".$this->g5."','".$this->g6."','".$this->g7."','".$this->g8."','".$this->g9."','".$this->g10."','".$this->g11."','".$this->g12."','";
      $query .= $this->costo_ebi."','".$this->costo_core."','".$this->costo_total."','".$this->gastado_anos_anteriores."','".$this->solicitado."','".$this->saldo_proximo_ano."','".$this->saldo_proximos_anos."','".$this->asignado."','";
      $query .= $this->asignacion_disponible."','".$this->saldo_por_asignar."','".$this->total_programado."','".$this->contratado."','".$this->pagado."','".$this->saldo."','".$this->arrastre."','".$this->fecha_registro."','".$this->c_usuario."','".$this->c_estado."');";
      $dataset  = mysql_query($query, $this->conector);
      if (mysql_errno($this->conector) != 0) { 
         $this->mensaje_error="No se ha podido Actualizar la Base de Datos, mensaje de error de Mysql es: " . mysql_error($this->conector).$query; 
      } else {$this->mensaje_error='';}   
      return $this->mensaje_error;
   }

   function get_nroregistros(){ return $this->nroregistros;}  
   
   function get_error()       { return $this->mensaje_error;}
      
}

//**********************************************************************

// LICITACION INVERSIONES
   
Class Inversion_Licitacion {
   var $conector;
   var $mensaje_error;
   var $nroregistros;
   var $ano;  
   var $c_institucion;
   var $c_preinversion;
   var $c_ficha;
   var $c_licitacion; 
   var $c_tipo_licitacion;
   var $justificacion="";
   var $fecha_publicacion;
   var $fecha_apertura;
   var $fecha_recepcion_consultas;
   var $fecha_aclaracion_consultas;
   var $id_chile_compras="";
   var $ord_aprobacion_bases="";
   var $c_estado;
   var $c_usuario;
   var $c_clasificador="";
   var $n_clasificador="";
   var $monto_disponible;
   var $estado;
   var $vector="";
   var $n_tipo_licitacion="";
   var $n_estado="";
   var $id_empresa;
   var $run="";
   var $valor_ofertado;
   var $aceptada="";
   var $observaciones_oferta;
   var $c_adjudicacion;
   var $empresa_adjudicada;
   var $nempresa_adjudicada;
   var $fecha_adjudicacion;
   var $resolucion_adjudicacion;
   var $monto_adjudicacion;
   var $justificacion_adjudicacion;
   
   function Inversion_Licitacion($conector) { 
      $this->conector =$conector;
      $this->mensaje_error='';
      $this->estado=0;
      $this->nroregistros=0;
   }
   
   function cargar_licitacion() {
      $query  = " SELECT A.C_LICITACION, DATE_FORMAT(A.FECHA_PUBLICACION,'%d/%m/%Y'), A.ID_CHILE_COMPRAS, A.ORD_APROBACION_BASES, ";
      $query .= " C.N_TIPO_LICITACION, B.N_ESTADO, A.JUSTIFICACION , A.C_ESTADO FROM INVERSION_LICITACION A, LICITACION_ESTADO B, TIPO_LICITACION C";
      $query .= " WHERE A.C_ESTADO=B.C_ESTADO AND A.C_TIPO_LICITACION=C.C_TIPO_LICITACION";
      $query .= " AND A.ANO= ".$this->ano." AND A.C_INSTITUCION=".$this->c_institucion;
      $query .= " AND A.C_PREINVERSION=".$this->c_preinversion." AND A.C_FICHA=".$this->c_ficha;
      $query .= " ORDER BY A.C_LICITACION DESC";
      $dataset  = mysql_query($query, $this->conector); 
      $nroregistros = @mysql_num_rows($dataset);
      $rows = array();
      while ($record = mysql_fetch_row($dataset)) { 
   $cols = array();$columna=0;
   foreach ($record as $value) { 
            if ($columna==6) {$value=ereg_replace("[^ A-Za-z0-9_]", "", $value); }
            $cols[] = '"'.addslashes($value).'"';
            $columna=$columna+1;
   } 
   $rows[] = "\t[".implode(",", $cols)."]"; 
      } 
      $datagrilla="[\n".implode(",\n",$rows)."\n];\n"; 
      return $datagrilla;
   }

   function cargar_ficha_licitacion() {
      $query  = " SELECT A.C_LICITACION, A.ORD_APROBACION_BASES, A.C_TIPO_LICITACION, C.N_TIPO_LICITACION, A.C_ESTADO, B.N_ESTADO, ";
      $query .= " DATE_FORMAT(A.FECHA_PUBLICACION,'%d/%m/%Y'), A.ID_CHILE_COMPRAS, DATE_FORMAT(A.FECHA_RECEPCION_CONSULTAS,'%d/%m/%Y'), ";
      $query .= " DATE_FORMAT(A.FECHA_ACLARACION_CONSULTAS,'%d/%m/%Y'), DATE_FORMAT(A.FECHA_APERTURA,'%d/%m/%Y'), A.JUSTIFICACION, A.C_CLASIFICADOR, CONCAT(D.N_CLASIFICADOR,' (',A.C_CLASIFICADOR,')'), A.MONTO_DISPONIBLE ";
      $query .= " FROM INVERSION_LICITACION A, LICITACION_ESTADO B, TIPO_LICITACION C, TCLASIFICADOR_PRESUPUESTARIO D";
      $query .= " WHERE A.C_ESTADO=B.C_ESTADO AND A.C_TIPO_LICITACION=C.C_TIPO_LICITACION";
      $query .= " AND A.ANO= ".$this->ano." AND A.C_INSTITUCION=".$this->c_institucion;
      $query .= " AND A.C_PREINVERSION=".$this->c_preinversion." AND A.C_FICHA=".$this->c_ficha;
      $query .= " AND A.C_LICITACION=".$this->c_licitacion;
      $query .= " AND A.C_CLASIFICADOR=D.C_CLASIFICADOR AND A.ANO=D.ANIO AND (A.C_INSTITUCION=D.INSTITUCION OR D.INSTITUCION=0)";
      $dataset  = mysql_query($query, $this->conector); 
      $nroregistros = @mysql_num_rows($dataset);
      $record = mysql_fetch_row($dataset);
      $this->c_licitacion=$record[0];
      $this->ord_aprobacion_bases=$record[1];
      $this->c_tipo_licitacion=$record[2];
      $this->n_tipo_licitacion=$record[3];
      $this->c_estado=$record[4];
      $this->n_estado=$record[5];
      $this->fecha_publicacion=$record[6];
      $this->id_chile_compras=$record[7];
      $this->fecha_recepcion_consultas=$record[8];
      $this->fecha_aclaracion_consultas=$record[9];
      $this->fecha_apertura=$record[10];
      $this->justificacion=$record[11];
      $this->c_clasificador=$record[12];
      $this->n_clasificador=$record[13];
      $this->monto_disponible=number_format($record[14],0,'.',',');;
      if (mysql_errno($this->conector) != 0) { 
         $this->mensaje_error="No se ha podido Actualizar la Base de Datos, mensaje de error de Mysql es: " . mysql_error($this->conector).$query; 
      } else {$this->mensaje_error='';}   
      return $this->mensaje_error;
   }

   function eliminar_licitacion() {
      $query  =" DELETE FROM INVERSION_LICITACION WHERE C_FICHA=".$this->c_ficha." AND C_PREINVERSION=".$this->c_preinversion." AND ";
      $query .=" C_INSTITUCION=".$this->c_institucion." AND ANO=".$this->ano." AND C_LICITACION=".$this->c_licitacion;
      $dataset  = mysql_query($query, $this->conector);
      $query  =" DELETE FROM INVERSION_LICITACION_EMPRESA WHERE C_FICHA=".$this->c_ficha." AND C_PREINVERSION=".$this->c_preinversion." AND ";
      $query .=" C_INSTITUCION=".$this->c_institucion." AND ANO=".$this->ano." AND C_LICITACION=".$this->c_licitacion;
      $dataset  = mysql_query($query, $this->conector);
      $query  =" DELETE FROM INVERSION_LICITACION_ADJUDICACION WHERE C_FICHA=".$this->c_ficha." AND C_PREINVERSION=".$this->c_preinversion." AND ";
      $query .=" C_INSTITUCION=".$this->c_institucion." AND ANO=".$this->ano." AND C_LICITACION=".$this->c_licitacion;
      $dataset  = mysql_query($query, $this->conector);
      $query  =" DELETE FROM INVERSION_GARANTIA WHERE C_FICHA=".$this->c_ficha." AND C_PREINVERSION=".$this->c_preinversion." AND ";
      $query .=" C_INSTITUCION=".$this->c_institucion." AND ANO=".$this->ano." AND C_LICITACION=".$this->c_licitacion;
      $dataset  = mysql_query($query, $this->conector);
      if (mysql_errno($this->conector) != 0) { 
         $this->mensaje_error="No se ha podido Actualizar la Base de Datos, mensaje de error de Mysql es: " . mysql_error($this->conector).$query; 
      } else {$this->mensaje_error='';}   
      return $this->mensaje_error;
   }

   function existe_contrato() {
      $query  =" SELECT COUNT(*) FROM INVERSION_CONTRATO WHERE C_FICHA=".$this->c_ficha." AND C_PREINVERSION=".$this->c_preinversion." AND ";
      $query .=" C_INSTITUCION=".$this->c_institucion." AND ANO=".$this->ano." AND C_LICITACION=".$this->c_licitacion;
      $dataset  = mysql_query($query, $this->conector);
      $record = mysql_fetch_row($dataset);
      if (mysql_errno($this->conector) != 0) { 
         $this->mensaje_error="No se ha podido Actualizar la Base de Datos, mensaje de error de Mysql es: " . mysql_error($this->conector).$query; 
      } else {$this->mensaje_error='';}   
      return $record[0];
   }

   function eliminar_licitacion_empresas() {
      $query  =" DELETE FROM INVERSION_LICITACION_EMPRESA WHERE C_FICHA=".$this->c_ficha." AND C_PREINVERSION=".$this->c_preinversion." AND ";
      $query .=" C_INSTITUCION=".$this->c_institucion." AND ANO=".$this->ano." AND C_LICITACION=".$this->c_licitacion;
      $dataset  = mysql_query($query, $this->conector);
      if (mysql_errno($this->conector) != 0) { 
         $this->mensaje_error="No se ha podido Actualizar la Base de Datos, mensaje de error de Mysql es: " . mysql_error($this->conector).$query; 
      } else {$this->mensaje_error='';}   
      return $this->mensaje_error;
   }

   function eliminar_licitacion_garantias() {
      $query  =" DELETE FROM INVERSION_GARANTIA WHERE C_FICHA=".$this->c_ficha." AND C_PREINVERSION=".$this->c_preinversion." AND ";
      $query .=" C_INSTITUCION=".$this->c_institucion." AND ANO=".$this->ano." AND C_LICITACION=".$this->c_licitacion;
      $dataset  = mysql_query($query, $this->conector);
      if (mysql_errno($this->conector) != 0) { 
         $this->mensaje_error="No se ha podido Actualizar la Base de Datos, mensaje de error de Mysql es: " . mysql_error($this->conector).$query; 
      } else {$this->mensaje_error='';}   
      return $this->mensaje_error;
   }

   function eliminar_licitacion_adjudicacion() {
      $query  =" DELETE FROM INVERSION_LICITACION_ADJUDICACION WHERE C_FICHA=".$this->c_ficha." AND C_PREINVERSION=".$this->c_preinversion." AND ";
      $query .=" C_INSTITUCION=".$this->c_institucion." AND ANO=".$this->ano." AND C_LICITACION=".$this->c_licitacion;
      $dataset  = mysql_query($query, $this->conector);
      if (mysql_errno($this->conector) != 0) { 
         $this->mensaje_error="No se ha podido Actualizar la Base de Datos, mensaje de error de Mysql es: " . mysql_error($this->conector).$query; 
      } else {$this->mensaje_error='';}   
      return $this->mensaje_error;
   }

   function calcular_valor_maximo() {
      $query  = " SELECT MAX(C_LICITACION) FROM INVERSION_LICITACION";
      $query .= " WHERE ANO= ".$this->ano." AND C_INSTITUCION=".$this->c_institucion;
      $query .= " AND C_PREINVERSION=".$this->c_preinversion." AND C_FICHA=".$this->c_ficha;
      $dataset  = mysql_query($query, $this->conector); 
      $this->nroregistros = mysql_num_rows($dataset);
      $record = @mysql_fetch_row($dataset);
      return $record[0]+1;
   }
   
   function actualizar_estado_licitacion() {
      $query  = " UPDATE INVERSION_LICITACION SET C_ESTADO=".$this->c_estado;
      $query .= " WHERE C_FICHA=".$this->c_ficha." AND C_PREINVERSION=".$this->c_preinversion." AND ";
      $query .= " C_INSTITUCION=".$this->c_institucion." AND ANO=".$this->ano." AND C_LICITACION=".$this->c_licitacion;
      $dataset  = mysql_query($query, $this->conector);
   }

   function actualizar_licitacion() {
      $query  = "REPLACE INTO INVERSION_LICITACION(ANO, C_INSTITUCION, C_PREINVERSION, C_FICHA, C_LICITACION, C_TIPO_LICITACION, JUSTIFICACION, FECHA_PUBLICACION, ";
      $query .= "FECHA_APERTURA, FECHA_RECEPCION_CONSULTAS, FECHA_ACLARACION_CONSULTAS, ID_CHILE_COMPRAS, ORD_APROBACION_BASES, C_ESTADO, C_USUARIO, C_CLASIFICADOR, MONTO_DISPONIBLE) VALUES('";
      $query .= $this->ano."','".$this->c_institucion."','".$this->c_preinversion."','".$this->c_ficha."','".$this->c_licitacion."','".$this->c_tipo_licitacion."','";
      $query .= $this->justificacion."','".$this->fecha_publicacion."','".$this->fecha_apertura."','";
      $query .= $this->fecha_recepcion_consultas."','".$this->fecha_aclaracion_consultas."','".$this->id_chile_compras."','";
      $query .= $this->ord_aprobacion_bases."','".$this->c_estado."','".$this->c_usuario."','".$this->c_clasificador."','".$this->monto_disponible."');";
      $dataset  = mysql_query($query, $this->conector);
      if (mysql_errno($this->conector) != 0) { 
         $this->mensaje_error="No se ha podido Actualizar la Base de Datos, mensaje de error de Mysql es: " . mysql_error($this->conector).$query; 
      } else {$this->mensaje_error='';}   
      return $this->mensaje_error;
   }

   function actualizar_licitacion_empresas() {
      $query  = "REPLACE INTO INVERSION_LICITACION_EMPRESA(ANO, C_INSTITUCION, C_PREINVERSION, C_FICHA, C_LICITACION, ID_EMPRESA, RUN, VALOR_OFERTADO, ACEPTADA, OBSERVACIONES) VALUES('";
      $query .= $this->ano."','".$this->c_institucion."','".$this->c_preinversion."','".$this->c_ficha."','".$this->c_licitacion."','".$this->id_empresa."','";
      $query .= $this->run."','".$this->valor_ofertado."','".$this->aceptada."','".$this->observaciones_oferta."');";
      $dataset  = mysql_query($query, $this->conector);
      if (mysql_errno($this->conector) != 0) { 
         $this->mensaje_error="No se ha podido Actualizar la Base de Datos, mensaje de error de Mysql es: " . mysql_error($this->conector).$query; 
      } else {$this->mensaje_error='';}   
      return $this->mensaje_error;
   }

   function actualizar_licitacion_adjudicacion() {
      $query  = "REPLACE INTO INVERSION_LICITACION_ADJUDICACION(ANO, C_INSTITUCION, C_PREINVERSION, C_FICHA, C_LICITACION, C_ADJUDICACION, EMPRESA_ADJUDICADA,";
      $query .= "FECHA_ADJUDICACION, RESOLUCION_ADJUDICACION, MONTO_ADJUDICACION, JUSTIFICACION_ADJUDICACION) VALUES('";
      $query .= $this->ano."','".$this->c_institucion."','".$this->c_preinversion."','".$this->c_ficha."','".$this->c_licitacion."','".$this->c_adjudicacion."','".$this->empresa_adjudicada."','";
      $query .= $this->fecha_adjudicacion."','".$this->resolucion_adjudicacion."','".$this->monto_adjudicacion."','".$this->justificacion_adjudicacion."');";
      $dataset  = mysql_query($query, $this->conector);
      if (mysql_errno($this->conector) != 0) { 
         $this->mensaje_error="No se ha podido Actualizar la Base de Datos, mensaje de error de Mysql es: " . mysql_error($this->conector).$query; 
      } else {$this->mensaje_error='';}   
      return $this->mensaje_error;
   }

   function crear_interface_licitaciones($opcion) {
      $query  = " SELECT A.ID_EMPRESA, A.RUN, X.DV, IF(X.TIPO='J',X.NOMBRE,CONCAT(X.NOMBRE,' ',X.AP_PATERNO,' ',X.AP_MATERNO)), A.VALOR_OFERTADO, A.ACEPTADA, IF(A.ACEPTADA='S','SI','NO'), ";
      $query .= " B.C_TIPO_GARANTIA, C.N_TIPO_GARANTIA,  B.BANCO, B.NRO_DOCUMENTO, B.MONTO, B.C_UNIDAD_MONETARIA, ";
      $query .= " D.N_UNIDAD_MONETARIA, DATE_FORMAT(B.FECHA_VENCIMIENTO,'%d/%m/%Y'), A.OBSERVACIONES ";
      $query .= " FROM TCONTRATISTA X, INVERSION_LICITACION_EMPRESA A ";
      $query .= " LEFT JOIN INVERSION_GARANTIA B ON (A.ANO=B.ANO AND A.C_INSTITUCION=B.C_INSTITUCION AND A.C_PREINVERSION=B.C_PREINVERSION AND A.C_FICHA=B.C_FICHA ";
      $query .= " AND A.C_LICITACION=B.C_LICITACION AND A.ID_EMPRESA=B.ID_EMPRESA) ";
      $query .= " LEFT JOIN TIPO_GARANTIA C ON (B.C_TIPO_GARANTIA=C.C_TIPO_GARANTIA)";
      $query .= " LEFT JOIN UNIDAD_MONETARIA D ON (B.C_UNIDAD_MONETARIA=D.C_UNIDAD_MONETARIA)";
      $query .= " WHERE A.ANO= ".$this->ano." AND A.C_INSTITUCION=".$this->c_institucion;
      $query .= " AND A.C_PREINVERSION=".$this->c_preinversion." AND A.C_FICHA=".$this->c_ficha;
      $query .= " AND A.C_LICITACION=".$this->c_licitacion;
      $query .= " AND A.RUN=X.RUN ";
      $query .= " ORDER BY A.ID_EMPRESA";
      $dataset  = mysql_query($query, $this->conector); 
      $nroregistros = @mysql_num_rows($dataset);$respuesta="";
      $fecha_actual=date('d/m/Y');
      echo "<script>var observa=new Array();</script>";
      while ($record = mysql_fetch_row($dataset)) { 
         $respuesta .= "<tr bgcolor='#FFFFFF'height='21px'>\n";        
         if ($opcion==0) {$sel=" readOnly ";} else {$sel="";}
         if ($opcion==0) {
            $respuesta .= "<td align='right' class='form_editsinmarco'>".$record[1]."&nbsp;</td>\n";
            $respuesta .= "<td align='center' class='form_editsinmarco'>".$record[2]."</td>\n";
            $respuesta .= "<td align='left' class='form_editsinmarco'>&nbsp;".$record[3]."</td>\n";
            if (round($record[4],0)>0) {$mm=number_format($record[4],0,',','.');} else {$mm="";}
       $respuesta .= "<td align='right' class='form_editsinmarco'>".$mm."&nbsp;</td>\n";
            $respuesta .= "<td align='center' class='form_editsinmarco'>".$record[6]."</td>\n";
            $respuesta .= "<td align='center' class='form_editsinmarco'>".$record[8]."</td>\n";
         } else {
            $respuesta .= "<td align='center'><input name='rut_".$record[0]."' maxlength='11' type='text' class='form_editsinmarco' id='rut_".$record[0]."' ".$sel." style='width:56px;TEXT-ALIGN: right;' onKeyPress='esnro();' onChange='validar_rut(".$record[0].");' value='".$record[1]."'></td>\n";
            $respuesta .= "<td align='center'><input name='dv_".$record[0]."' maxlength='1' type='text' class='form_editsinmarco' id='dv_".$record[0]."' readOnly style='width:20px;TEXT-ALIGN: center;' value='".$record[2]."'></td>\n";
            $respuesta .= "<td align='center'><input name='nombre_".$record[0]."' type='text' class='form_editsinmarco' maxlength='50' id='nombre_".$record[0]."' ".$sel." readOnly style='width:100%;TEXT-ALIGN: left;' value='".$record[3]."'</td>\n";
            $respuesta .= "<td align='center'><input name='monto_".$record[0]."' type='text' class='form_editsinmarco' maxlength='14' id='monto_".$record[0]."' ".$sel." style='width:66px;TEXT-ALIGN: right;' onkeyPress='esnro();' onBlur='formato_numero(1,this.value,\"monto_".$record[0]."\");' onFocus='formato_numero(2,this.value,\"monto_".$record[0]."\");' value='".$record[4]."'></td>\n";
            $respuesta .= "<td align='center'><input type='checkbox' name='aceptada_".$record[0]."' id='aceptada_".$record[0]."' value='1' onClick='configurar_grilla_empresas();'>\n";$record[5]."</td>\n";
            if ($record[5]=="S") {$respuesta .= "<script>document.getElementById('aceptada_".$record[0]."').checked=true;</script>\n";}
            $respuesta .= "<td align='center'>\n";
            $respuesta .= "<select name='cb_tipo_".$record[0]."' size='1' class='tabla' id='cb_tipo_".$record[0]."' style='width:80px'>\n";
            $respuesta .= "    <option value='0' selected></option>\n";
            $respuesta .= "    <option value='1'>Boleta</option>\n";
            $respuesta .= "    <option value='2'>Póliza</option>\n";
            $respuesta .= "    <option value='3'>Vale Vista</option>\n";
            $respuesta .= "  </select>\n";
            $respuesta .= "</td>\n";
            $respuesta .= "<script>document.getElementById('cb_tipo_".$record[0]."').value=".round($record[7],0).";</script>\n";
         }
         if ($opcion==0) {
            $respuesta .= "<td align='left' class='form_editsinmarco'>&nbsp;".$record[9]."</td>\n";
            $respuesta .= "<td align='center' class='form_editsinmarco'>".$record[10]."</td>\n";
            if (round($record[11],0)>0) {$mm=number_format($record[11],0,',','.');} else {$mm="";}
            $respuesta .= "<td align='right' class='form_editsinmarco'>".$mm."&nbsp;</td>\n";
         } else {
            $respuesta .= "<td align='center'><input maxlength='30' name='banco_".$record[0]."' type='text' class='form_editsinmarco' id='banco_".$record[0]."' ".$sel." style='width:96px;TEXT-ALIGN: left;' value='".$record[9]."'></td>\n";
            $respuesta .= "<td align='center'><input name='nro_".$record[0]."' maxlength='20' type='text' class='form_editsinmarco' id='nro_".$record[0]."' ".$sel." style='width:56px;TEXT-ALIGN: center;' value='".$record[10]."'></td>\n";
            $respuesta .= "<td align='center'><input name='monto1_".$record[0]."' maxlength='14' type='text' class='form_editsinmarco' id='monto1_".$record[0]."' ".$sel." style='width:66px;TEXT-ALIGN: right;' onkeyPress='esnro();' onBlur='formato_numero(1,this.value,\"monto1_".$record[0]."\");' onFocus='formato_numero(2,this.value,\"monto1_".$record[0]."\");' value='".$record[11]."'></td>\n"; 
         }
         if ($opcion==0) {
            $respuesta .= "<td align='center' class='form_editsinmarco'>".$record[13]."</td>\n";
         } else {
            $respuesta .= "<td align='center'>\n";
            $respuesta .= "<select name='cb_unidad_".$record[0]."' size='1' class='tabla' id='cb_unidad_".$record[0]."' style='width:40px'>\n";
            $respuesta .= "    <option value='0' selected></option>\n";
            $respuesta .= "    <option value='1'>$</option>\n";
            $respuesta .= "    <option value='2'>UF</option>\n";
            $respuesta .= "  </select>\n";
            $respuesta .= "</td>\n";
            $respuesta .= "<script>document.getElementById('cb_unidad_".$record[0]."').value=".round($record[12],0).";</script>\n";
         }
         $respuesta .= "<td align='center'><input name='vencimiento_".$record[0]."' class='form_editsinmarco' readOnly id='vencimiento_".$record[0]."' ".$sel;
         if ($opcion!=0) {
            $respuesta .= " style='cursor:hand; TEXT-ALIGN: center;width:70px;' onClick='define_fecha(1);displayCalendar2(document.form_ficha.vencimiento_".$record[0].",\"dd/mm/yyyy\",this,2,ffecha1,ffecha2,630,200,0);'";
         } else {
       $respuesta .= " style='TEXT-ALIGN: center;width:70px;'";
    }
         $respuesta .= " value='".$record[14]."'></td>\n";
         $respuesta .= "<td align='center'><img src='../../imagenes/historico.gif' width='16' height='15' name='observ_".$record[0]."' id='observ_".$record[0]."' style='cursor:pointer' onclick='observaciones(".$record[0].");'></td>\n"; 
         $respuesta .= "<script>observa[".$record[0]."]='".$record[15]."';</script>";
         $respuesta .= "</tr>\n";
      }
    if ($opcion!=0) {
      for ($j=$nroregistros+1; $j<21; $j++) {         
         $respuesta .= "<tr bgcolor='#FFFFFF'height='21px'>\n";        
         $respuesta .= "<td align='center'><input name='rut_".$j."' maxlength='11' type='text' class='form_editsinmarco' value='' onKeyPress='esnro();' id='rut_".$j."' style='width:56px;TEXT-ALIGN: right;' onChange='validar_rut(".$j.");'></td>\n";
         $respuesta .= "<td align='center'><input name='dv_".$j."' maxlength='1' type='text' class='form_editsinmarco' value='' readOnly id='dv_".$j."' style='width:20px;TEXT-ALIGN:center;'></td>\n";
         $respuesta .= "<td align='center'><input name='nombre_".$j."' maxlength='50' type='text' class='form_editsinmarco' value='' readOnly id='nombre_".$j."' style='width:100%;TEXT-ALIGN: left;'></td>\n";
         $respuesta .= "<td align='center'><input name='monto_".$j."' maxlength='14' type='text' class='form_editsinmarco' value='' id='monto_".$j."' style='width:66px;TEXT-ALIGN: right;' onkeyPress='esnro();' onBlur='formato_numero(1,this.value,\"monto_".$j."\");' onFocus='formato_numero(2,this.value,\"monto_".$j."\");'></td>\n";
         $respuesta .= "<td align='center'><input type='checkbox' name='aceptada_".$j."' id='aceptada_".$j."' value='1' onClick='configurar_grilla_empresas();'></td>\n";
         $respuesta .= "<td align='center'>\n";
         $respuesta .= "<select name='cb_tipo_".$j."' size='1' class='tabla' id='cb_tipo_".$j."' style='width:80px'>\n"; 
         $respuesta .= "    <option value='0' selected></option>\n";
         $respuesta .= "    <option value='1'>Boleta</option>\n";
         $respuesta .= "    <option value='2'>Póliza</option>\n";
         $respuesta .= "    <option value='3'>Vale Vista</option>\n";
         $respuesta .= "  </select>\n";
         $respuesta .= "</td>\n";
         $respuesta .= "<td align='center'><input name='banco_".$j."' maxlength='30' type='text' class='form_editsinmarco' value='' id='banco_".$j."' style='width:96px;TEXT-ALIGN: left;'></td>\n";
         $respuesta .= "<td align='center'><input name='nro_".$j."' maxlength='20' type='text' class='form_editsinmarco' value='' id='nro_".$j."' style='width:56px;TEXT-ALIGN: center;'></td>\n";
         $respuesta .= "<td align='center'><input name='monto1_".$j."' maxlength='14' type='text' class='form_editsinmarco' value='' id='monto1_".$j."' style='width:66px;TEXT-ALIGN: right;' onkeyPress='esnro();' onBlur='formato_numero(1,this.value,\"monto1_".$j."\");' onFocus='formato_numero(2,this.value,\"monto1_".$j."\");'></td>\n";
         $respuesta .= "<td align='center'>\n";
         $respuesta .= "<select name='cb_unidad_".$j."' size='1' class='tabla' id='cb_unidad_".$j."' style='width:40px'>\n";
         $respuesta .= "    <option value='0' selected></option>\n";
         $respuesta .= "    <option value='1'>$</option>\n";
         $respuesta .= "    <option value='2'>UF</option>\n";
         $respuesta .= "  </select>\n";
         $respuesta .= "</td>\n";
         $respuesta .= "<td align='center'><input name='vencimiento_".$j."' type='text' class='form_editsinmarco' value='' id='vencimiento_".$j."' ".$sel;
         $respuesta .= " style='cursor:hand; TEXT-ALIGN: center;width:70px;' onClick='define_fecha(1);displayCalendar2(document.form_ficha.vencimiento_".$j.",\"dd/mm/yyyy\",this,2,ffecha1,ffecha2,630,200,0);'";
         $respuesta .= " value=''></td>\n";
         $respuesta .= "<td align='center'><img src='../../imagenes/spacer.gif' name='observ_".$j."' id='observ_".$j."' width='16' height='15' style='cursor:pointer' onclick='observaciones(".$j.");'></td>\n"; 
         $respuesta .= "</tr>\n";
         $respuesta .= "<script>observa[".$j."]='';</script>";
      }
     }
      return $respuesta;
   }   

   function crear_interface_adjudicacion($opcion) {
      $query1  = " SELECT A.ID_EMPRESA, IF(B.TIPO=1,CONCAT(A.RUN,' ',B.NOMBRE),CONCAT(A.RUN,' ',B.NOMBRE,' ',B.AP_PATERNO,' ',B.AP_MATERNO)) ";
      $query1 .= " FROM INVERSION_LICITACION_EMPRESA A, TCONTRATISTA B ";
      $query1 .= " WHERE A.ANO= ".$this->ano." AND A.C_INSTITUCION=".$this->c_institucion;
      $query1 .= " AND A.C_PREINVERSION=".$this->c_preinversion." AND A.C_FICHA=".$this->c_ficha;
      $query1 .= " AND A.C_LICITACION=".$this->c_licitacion." AND A.RUN=B.RUN AND A.ACEPTADA='S'";
      $dataset1  = mysql_query($query1, $this->conector); 
      $seleccion="";
      while ($record1 = mysql_fetch_row($dataset1)) {
         $seleccion .= "    <option value='".$record1[0]."'>".$record1[1]."</option>\n";        
      } 
      $query  = " SELECT A.C_ADJUDICACION, A.EMPRESA_ADJUDICADA, IF(C.TIPO=1,CONCAT(B.RUN,' ',C.NOMBRE),CONCAT(B.RUN,' ',C.NOMBRE,' ',C.AP_PATERNO,' ',C.AP_MATERNO)), DATE_FORMAT(A.FECHA_ADJUDICACION,'%d/%m/%Y'), ";
      $query .= " A.RESOLUCION_ADJUDICACION, A.MONTO_ADJUDICACION, A.JUSTIFICACION_ADJUDICACION ";
      $query .= " FROM INVERSION_LICITACION_ADJUDICACION A, INVERSION_LICITACION_EMPRESA B, TCONTRATISTA C ";
      $query .= " WHERE A.ANO= ".$this->ano." AND A.C_INSTITUCION=".$this->c_institucion;
      $query .= " AND A.C_PREINVERSION=".$this->c_preinversion." AND A.C_FICHA=".$this->c_ficha;
      $query .= " AND A.C_LICITACION=".$this->c_licitacion." AND B.RUN=C.RUN";
      $query .= " AND A.ANO=B.ANO AND A.C_INSTITUCION=B.C_INSTITUCION AND A.C_PREINVERSION=B.C_PREINVERSION AND A.C_FICHA=B.C_FICHA AND A.C_LICITACION=B.C_LICITACION";
      $query .= " AND A.EMPRESA_ADJUDICADA=B.ID_EMPRESA ORDER BY A.C_ADJUDICACION";
      $dataset  = mysql_query($query, $this->conector); 
      $nroregistros = @mysql_num_rows($dataset);$respuesta="";
      $fecha_actual=date('d/m/Y');
      while ($record = mysql_fetch_row($dataset)) { 
         $respuesta .= "<tr bgcolor='#FFFFFF'height='21px'>\n";        
         if ($opcion==0) {
            $respuesta .= "<td align='left' class='form_editsinmarco'>&nbsp;".$record[2]."</td>\n";
            $respuesta .= "<td align='center' class='form_editsinmarco'>&nbsp;".$record[3]."</td>\n";
            $respuesta .= "<td align='left' class='form_editsinmarco'>&nbsp;".$record[4]."</td>\n";
            $respuesta .= "<td align='right' class='form_editsinmarco'>".number_format($record[5],0,',','.')."&nbsp;</td>\n";
            $respuesta .= "<td align='left' class='form_editsinmarco'>&nbsp;".$record[6]."</td>\n";
         } else {
            $respuesta .= "<td align='left'>\n";
            $respuesta .= "<select name='cb_empresa_".$record[0]."' size='1' class='tabla' disabled id='cb_empresa_".$record[0]."' style='width:100%'>\n";
            $respuesta .= "    <option value='0' selected></option>\n";
            $respuesta .= $seleccion;
            $respuesta .= "  </select>\n";
            $respuesta .= "</td>\n";
            $respuesta .= "<script>document.getElementById('cb_empresa_".$record[0]."').value=".$record[1].";</script>\n";
            $respuesta .= "<td align='center'><input name='fecha_adjudicacion_".$record[0]."' readOnly type='text' class='form_editsinmarco' id='fecha_adjudicacion_".$record[0]."' ".$sel." style='width:60px;TEXT-ALIGN: center;'";
            $respuesta .= " style='cursor:hand; TEXT-ALIGN: center;width:70px;' onClick='if (document.form_ficha.cb_empresa_".$record[0].".value>0) {define_fecha(1);displayCalendar2(document.form_ficha.fecha_adjudicacion_".$record[0].",\"dd/mm/yyyy\",this,2,ffecha1,ffecha2,258,377,0);}'";
            $respuesta .= " value=".$record[3]."></td>\n";
            $respuesta .= "<td align='center'><input name='resolucion_".$record[0]."' maxlength='20' type='text' readOnly class='form_editsinmarco' id='resolucion_".$record[0]."' ".$sel." style='width:56px;TEXT-ALIGN: left;' value='".$record[4]."'</td>\n";
            $respuesta .= "<td align='center'><input name='monto2_".$record[0]."' maxlength='14' type='text' readOnly class='form_editsinmarco' id='monto2_".$record[0]."' ".$sel." style='width:66px;TEXT-ALIGN: right;' onkeyPress='esnro();' onBlur='formato_numero(1,this.value,\"monto2_".$record[0]."\");' onFocus='formato_numero(2,this.value,\"monto2_".$record[0]."\");' value='".$record[5]."'></td>\n";
            $respuesta .= "<td align='center'><input name='justificacion_".$record[0]."' readOnly type='text' class='form_editsinmarco' id='justificacion_".$record[0]."' ".$sel." style='width:100%;TEXT-ALIGN: left;' value='".$record[6]."'</td>\n";
         }
         $respuesta .= "</tr>\n";
      }
    if ($opcion!=0) {
      for ($j=$nroregistros+1; $j<21; $j++) {            
         $respuesta .= "<tr bgcolor='#FFFFFF'height='21px'>\n";        
         $respuesta .= "<td align='center'>\n";
         $respuesta .= "<select name='cb_empresa_".$j."' size='1' class='tabla' id='cb_empresa_".$j."' style='width:100%' disabled onChange='configurar_grilla_adjudicacion();'>\n";
         $respuesta .= "    <option value='0' selected></option>\n";
         $respuesta .= $seleccion;
         $respuesta .= "  </select>\n";
         $respuesta .= "</td>\n";
         $respuesta .= "<td align='center'><input readOnly name='fecha_adjudicacion_".$j."' type='text' class='form_editsinmarco' value='' id='fecha_adjudicacion_".$j."' ".$sel." style='width:66px;TEXT-ALIGN: center;'";
         $respuesta .= " style='cursor:hand; TEXT-ALIGN: center;width:70px;' onClick='if (document.form_ficha.cb_empresa_".$j.".value>0) {define_fecha(1);displayCalendar2(document.form_ficha.fecha_adjudicacion_".$j.",\"dd/mm/yyyy\",this,2,ffecha1,ffecha2,258,377,0);}'";
         $respuesta .= " value=''></td>\n";
         $respuesta .= "<td align='center'><input name='resolucion_".$j."' type='text' maxlength='20' class='form_editsinmarco' readOnly value='' id='resolucion_".$j."' style='width:56px;TEXT-ALIGN: left;'></td>\n";
         $respuesta .= "<td align='center'><input name='monto2_".$j."' type='text' maxlength='14' class='form_editsinmarco' readOnly id='monto2_".$j."' ".$sel." style='width:66px;TEXT-ALIGN: right;' onkeyPress='esnro();' onBlur='formato_numero(1,this.value,\"monto2_".$j."\");' onFocus='formato_numero(2,this.value,\"monto2_".$j."\");' value=''></td>\n"; 
         $respuesta .= "<td align='center'><input name='justificacion_".$j."' type='text' readOnly class='form_editsinmarco' readOnlyvalue='' id='justificacion_".$j."' style='width:100%;TEXT-ALIGN: left;'></td>\n";
         $respuesta .= "</tr>\n";
      }
     }
      return $respuesta;
   }  

   function cargar_licitacion_adjudicacion() {
      $query  = " SELECT A.EMPRESA_ADJUDICADA, CONCAT(B.RUT,' ',B.NOMBRE), DATE_FORMAT(A.FECHA_ADJUDICACION,'%d/%m/%Y'), ";
      $query .= " A.RESOLUCION_ADJUDICACION, A.JUSTIFICACION_ADJUDICACION ";
      $query .= " FROM INVERSION_LICITACION_ADJUDICACION A, INVERSION_LICITACION_EMPRESA B ";
      $query .= " WHERE A.ANO= ".$this->ano." AND A.C_INSTITUCION=".$this->c_institucion;
      $query .= " AND A.C_PREINVERSION=".$this->c_preinversion." AND A.C_FICHA=".$this->c_ficha;
      $query .= " AND A.C_LICITACION=".$this->c_licitacion;
      $query .= " AND A.ANO=B.ANO AND A.C_INSTITUCION=B.C_INSTITUCION AND A.C_PREINVERSION=B.C_PREINVERSION ";
      $query .= " AND A.C_FICHA=B.C_FICHA AND A.C_LICITACION=B.C_LICITACION ";
      $dataset  = mysql_query($query, $this->conector); 
      $record = mysql_fetch_row($dataset);
      $this->empresa_adjudicada=$record[0];
      $this->nempresa_adjudicada=$record[1];
      $this->fecha_adjudicacion=$record[2];
      $this->resolucion_adjudicacion=$record[4];
      $this->justificacion_adjudicacion=$record[5];
      if (mysql_errno($this->conector) != 0) { 
         $this->mensaje_error="No se ha podido Actualizar la Base de Datos, mensaje de error de Mysql es: " . mysql_error($this->conector).$query; 
      } else {$this->mensaje_error='';}   
      return $this->mensaje_error;
   }

   function get_nroregistros(){ return $this->nroregistros;}  
   
   function get_error()       { return $this->mensaje_error;}
      
}

//**********************************************************************

// CONTRATOS INVERSIONES
   
Class Inversion_Contrato {
   var $conector;
   var $mensaje_error;
   var $nroregistros;
   var $ano;  
   var $c_institucion;
   var $c_preinversion;
   var $c_ficha;
   var $c_licitacion;
   var $c_contrato;
   var $c_condicion;
   var $c_tipo_contrato;
   var $n_tipo_contrato="";
   var $c_modalidad_contrato;
   var $n_modalidad_contrato="";
   var $rut_contratista="";
   var $c_clasificador="";
   var $n_clasificador="";
   var $resolucion="";
   var $fecha_resolucion;
   var $monto;
   var $fecha_inicio;
   var $fecha_termino;
   var $plazo;
   var $fecha_entrega_terreno;
   var $fecha_recepcion_provisoria;
   var $fecha_recepcion_definitiva;
   var $anticipo;
   var $anticipo_entregado;
   var $mensaje_anticipo_entregado="";
   var $anticipo_devuelto;
   var $mensaje_anticipo_recuperado="";
   var $mensaje_retencion_devuelto="";
   var $anticipo_saldo;
   var $anticipo_condiciones="";
   var $retencion;
   var $retencion_devuelto;
   var $retencion_saldo;
   var $devolucion_retencion;
   var $retencion_condiciones="";
   var $observaciones="";
   var $c_estado;
   var $n_estado="";
   var $fecha_registro;
   var $c_usuario;
   var $estado;
   var $nestadopago;
   var $contrato_inicial;
   var $pagado;
   var $saldo_por_cancelar;

   var $dv_contratista="";
   var $nombre_contratista="";
   var $monto_adjudicado;
   
   function Inversion_Contrato($conector) { 
      $this->conector =$conector;
      $this->mensaje_error='';
      $this->estado=0;
      $this->nroregistros=0;
   }
   
   function cargar_contrato() {
      $query  = " SELECT A.C_LICITACION, A.C_CONTRATO, C.N_TIPO_CONTRATO, ";
      $query .= " if (A.C_CONDICION=1,'Contrato Inicial','Modificación'), ";
      $query .= " A.RESOLUCION, DATE_FORMAT(A.FECHA_RESOLUCION,'%d/%m/%Y'), IF(D.TIPO=1,CONCAT(D.RUN,'- ',D.NOMBRE),CONCAT(D.RUN,' ',D.NOMBRE,' ',D.AP_PATERNO,' ',D.AP_MATERNO)), A.MONTO, ";
      $query .= " B.N_ESTADO, A.OBSERVACIONES, A.C_ESTADO FROM CONTRATO_ESTADO B, TIPO_CONTRATO C, INVERSION_CONTRATO A LEFT JOIN TCONTRATISTA D";
      $query .= " ON (A.RUT_CONTRATISTA=D.RUN)";
      $query .= " WHERE A.C_ESTADO=B.C_ESTADO AND A.C_TIPO_CONTRATO=C.C_TIPO_CONTRATO ";
      $query .= " AND A.ANO= ".$this->ano." AND A.C_INSTITUCION=".$this->c_institucion;
      $query .= " AND A.C_PREINVERSION=".$this->c_preinversion." AND A.C_FICHA=".$this->c_ficha;
      $query .= " ORDER BY A.C_LICITACION, A.C_CONTRATO DESC";
      $dataset  = mysql_query($query, $this->conector); 
      $nroregistros = @mysql_num_rows($dataset);
      $rows = array();
      while ($record = mysql_fetch_row($dataset)) { 
   $cols = array();$columna=0;
   foreach ($record as $value) { 
            if ($columna==9) {
               $value=ereg_replace("[^ A-Za-z0-9_]", "", $value); 
            }
            $cols[] = '"'.addslashes($value).'"';
            $columna=$columna+1;
   } 
   $rows[] = "\t[".implode(",", $cols)."]"; 
      } 
      $datagrilla="[\n".implode(",\n",$rows)."\n];\n"; 
      return $datagrilla;
   }

   function cargar_contratistas() {
      $query  = " SELECT A.RUN, A.DV, IF(A.TIPO=1,A.NOMBRE, CONCAT(A.NOMBRE,' ',A.AP_PATERNO,' ',A.AP_MATERNO)), ";
      $query .= " B.C_LICITACION, DATE_FORMAT(B.FECHA_PUBLICACION,'%d/%m/%Y'), D.MONTO_ADJUDICACION, CONCAT(E.N_CLASIFICADOR,' (',E.C_CLASIFICADOR,')'), E.C_CLASIFICADOR, Z.C_CONTRATO ";
      $query .= " FROM TCONTRATISTA A, INVERSION_LICITACION B, INVERSION_LICITACION_ADJUDICACION D, TCLASIFICADOR_PRESUPUESTARIO E, INVERSION_LICITACION_EMPRESA C";
      $query .= " LEFT JOIN INVERSION_CONTRATO Z ON (C.ANO=Z.ANO AND C.C_INSTITUCION=Z.C_INSTITUCION AND C.C_PREINVERSION=Z.C_PREINVERSION AND C.C_FICHA=Z.C_FICHA ";
      $query .= " AND C.C_LICITACION=Z.C_LICITACION AND C.RUN=Z.RUT_CONTRATISTA)";
      $query .= " WHERE A.RUN=C.RUN AND B.ANO=C.ANO AND B.C_INSTITUCION=C.C_INSTITUCION AND B.C_PREINVERSION=C.C_PREINVERSION ";
      $query .= " AND B.C_FICHA=C.C_FICHA AND B.C_LICITACION=C.C_LICITACION AND C.ID_EMPRESA= D.EMPRESA_ADJUDICADA";
      $query .= " AND B.ANO=D.ANO AND B.C_INSTITUCION=D.C_INSTITUCION AND B.C_PREINVERSION=D.C_PREINVERSION ";
      $query .= " AND B.C_FICHA=D.C_FICHA AND B.C_LICITACION=D.C_LICITACION ";
      $query .= " AND B.ANO= ".$this->ano." AND B.C_INSTITUCION=".$this->c_institucion;
      $query .= " AND B.C_PREINVERSION=".$this->c_preinversion." AND B.C_FICHA=".$this->c_ficha." AND B.C_CLASIFICADOR=E.C_CLASIFICADOR AND B.ANO=E.ANIO AND (B.C_INSTITUCION=E.INSTITUCION OR E.INSTITUCION=0) ";
      $query .= "  ORDER BY IF(A.TIPO=1,A.NOMBRE, CONCAT(A.NOMBRE,' ',A.AP_PATERNO,' ',A.AP_MATERNO))";
      $dataset  = mysql_query($query, $this->conector); 
      $nroregistros = @mysql_num_rows($dataset);
      $rows = array();
      while ($record = mysql_fetch_row($dataset)) { 
         if (round($record[8],0)==0) {
         $cols = array();
       foreach ($record as $value) { 
               $cols[] = '"'.addslashes($value).'"';
       } 
       $rows[] = "\t[".implode(",", $cols)."]"; 
         }
      } 
      $datagrilla="[\n".implode(",\n",$rows)."\n];\n"; 
      return $datagrilla;
   }

   function cargar_empresa_contrato() {
      $query  = " SELECT A.RUN, A.DV, IF(A.TIPO=1,A.NOMBRE, CONCAT(A.NOMBRE,' ',A.AP_PATERNO,' ',A.AP_MATERNO)), ";
      $query .= " B.C_LICITACION, DATE_FORMAT(B.FECHA_PUBLICACION,'%d/%m/%Y'), D.MONTO_ADJUDICACION, CONCAT(E.N_CLASIFICADOR,' (',E.C_CLASIFICADOR,')'), E.C_CLASIFICADOR";
      $query .= " FROM TCONTRATISTA A, INVERSION_LICITACION B, INVERSION_LICITACION_EMPRESA C, TCLASIFICADOR_PRESUPUESTARIO E, INVERSION_LICITACION_ADJUDICACION D";
      $query .= " WHERE A.RUN=C.RUN AND B.ANO=C.ANO AND B.C_INSTITUCION=C.C_INSTITUCION AND B.C_PREINVERSION=C.C_PREINVERSION ";
      $query .= " AND B.C_FICHA=C.C_FICHA AND B.C_LICITACION=C.C_LICITACION";
      $query .= " AND B.ANO=D.ANO AND B.C_INSTITUCION=D.C_INSTITUCION AND B.C_PREINVERSION=D.C_PREINVERSION ";
      $query .= " AND B.C_FICHA=D.C_FICHA AND B.C_LICITACION=D.C_LICITACION ";
      $query .= " AND B.ANO= ".$this->ano." AND B.C_INSTITUCION=".$this->c_institucion;
      $query .= " AND B.C_PREINVERSION=".$this->c_preinversion." AND B.C_FICHA=".$this->c_ficha;
      $query .= " AND B.C_LICITACION=".round($this->c_licitacion,0)." AND B.C_CLASIFICADOR=E.C_CLASIFICADOR AND B.ANO=E.ANIO AND (B.C_INSTITUCION=E.INSTITUCION OR E.INSTITUCION=0)";
      $query .= " AND A.RUN=".round($this->rut_contratista,0);
      $dataset  = mysql_query($query, $this->conector); 
      $nroregistros = @mysql_num_rows($dataset);
      $record = mysql_fetch_row($dataset);
      $this->rut_contratista=$record[0];
      $this->dv_contratista=$record[1];
      $this->nombre_contratista=$record[2];
      $this->c_licitacion=$record[3];
      $this->monto_adjudicado=$record[5];
      $this->n_clasificador=$record[6];
      $this->c_clasificador=$record[7];
   }

   function cargar_empresa_contrato2() {
      $query  = " SELECT B.RUN, B.DV, IF(B.TIPO=1,B.NOMBRE, CONCAT(B.NOMBRE,' ',B.AP_PATERNO,' ',B.AP_MATERNO)), ";
      $query .= " CONCAT(C.N_CLASIFICADOR,' (',C.C_CLASIFICADOR,')'), C.C_CLASIFICADOR ";
      $query .= " FROM INVERSION_CONTRATO A, TCONTRATISTA B, TCLASIFICADOR_PRESUPUESTARIO C";
      $query .= " WHERE A.RUT_CONTRATISTA=B.RUN AND A.C_CLASIFICADOR=C.C_CLASIFICADOR AND A.ANO=C.ANIO AND (A.C_INSTITUCION=C.INSTITUCION OR C.INSTITUCION=0) AND ";
      $query .= " A.ANO= ".$this->ano." AND A.C_INSTITUCION=".$this->c_institucion;
      $query .= " AND A.C_PREINVERSION=".$this->c_preinversion." AND A.C_FICHA=".$this->c_ficha;
      $query .= " AND A.C_CONTRATO=".round($this->c_contrato,0)." ";
      $dataset  = mysql_query($query, $this->conector); 
      $nroregistros = @mysql_num_rows($dataset);
      $record = mysql_fetch_row($dataset);
      $this->rut_contratista=$record[0];
      $this->dv_contratista=$record[1];
      $this->nombre_contratista=$record[2];
      $this->n_clasificador=$record[3];
      $this->c_clasificador=$record[4];
   }

   function cargar_ficha_contrato() {
      $query  = " SELECT A.C_ESTADO, B.N_ESTADO, A.C_TIPO_CONTRATO, C.N_TIPO_CONTRATO, A.C_MODALIDAD_CONTRATO, D.N_MODALIDAD, A.C_CONDICION, ";
      $query .= " A.RESOLUCION, DATE_FORMAT(A.FECHA_RESOLUCION,'%d/%m/%Y'), A.RUT_CONTRATISTA, ";
      $query .= " A.C_LICITACION, A.MONTO, DATE_FORMAT(A.FECHA_INICIO,'%d/%m/%Y'), DATE_FORMAT(A.FECHA_TERMINO,'%d/%m/%Y'), A.PLAZO, ";
      $query .= " DATE_FORMAT(A.FECHA_ENTREGA_TERRENO,'%d/%m/%Y'), DATE_FORMAT(A.FECHA_RECEPCION_PROVISORIA,'%d/%m/%Y'),DATE_FORMAT(A.FECHA_RECEPCION_DEFINITIVA,'%d/%m/%Y'),";
      $query .= " A.ANTICIPO, A.ANTICIPO_DEVUELTO, A.ANTICIPO_SALDO, A.ANTICIPO_CONDICIONES, ";
      $query .= " A.RETENCION, A.RETENCION_DEVUELTO, A.RETENCION_SALDO, A.RETENCION_CONDICIONES, ";
      $query .= " A.OBSERVACIONES, A.C_ESTADO, A.C_CLASIFICADOR, A.ANTICIPO_ENTREGADO, A.RETENCION_DEVOLUCION, A.PAGADO ";
      $query .= " FROM INVERSION_CONTRATO A, CONTRATO_ESTADO B, TIPO_CONTRATO C, MODALIDAD_CONTRATO D";
      $query .= " WHERE A.C_ESTADO=B.C_ESTADO AND A.C_TIPO_CONTRATO=C.C_TIPO_CONTRATO";
      $query .= " AND A.ANO= ".$this->ano." AND A.C_INSTITUCION=".$this->c_institucion;
      $query .= " AND A.C_PREINVERSION=".$this->c_preinversion." AND A.C_FICHA=".$this->c_ficha;
      $query .= " AND A.C_LICITACION=".$this->c_licitacion." AND A.C_CONTRATO=".$this->c_contrato;
      $query .= " AND A.C_MODALIDAD_CONTRATO=D.C_MODALIDAD";
      $dataset  = mysql_query($query, $this->conector); 
      $nroregistros = @mysql_num_rows($dataset);
      $record = mysql_fetch_row($dataset);
      $this->c_estado=$record[0];
      $this->n_estado=$record[1];
      $this->c_tipo_contrato=$record[2];
      $this->n_tipo_contrato=$record[3];
      $this->c_modalidad_contrato=$record[4];
      $this->n_modalidad_contrato=$record[5];
      $this->c_condicion=$record[6];
      $this->resolucion=$record[7];
      $this->fecha_resolucion=$record[8];
      $this->rut_contratista=$record[9];
      $this->c_licitacion=$record[10];
      $this->monto=$record[11];
      $this->fecha_inicio=$record[12];
      $this->fecha_termino=$record[13];
      $this->plazo=$record[14];
      $this->fecha_entrega_terreno=$record[15];
      $this->fecha_recepcion_provisoria=$record[16];
      $this->fecha_recepcion_definitiva=$record[17];
      $this->anticipo=$record[18];
      $this->anticipo_devuelto=$record[19];
      $this->anticipo_saldo=$record[20];
      $this->anticipo_condiciones=$record[21];
      $this->retencion=$record[22];
      $this->retencion_devuelto=$record[23];
      $this->retencion_saldo=$record[24];
      $this->retencion_condiciones=$record[25];
      $this->observaciones=$record[26];
      $this->c_clasificador=$record[28];
      $this->anticipo_entregado=$record[29];
      $this->retencion_devolucion=$record[30];
      $this->pagado=number_format($record[31],0,',','.');
      $this->saldo_por_cancelar=number_format(($record[11]-$record[31]),0,',','.');
      if (mysql_errno($this->conector) != 0) { 
         $this->mensaje_error="No se ha podido Actualizar la Base de Datos, mensaje de error de Mysql es: " . mysql_error($this->conector).$query; 
      } else {$this->mensaje_error='';}   
      return $this->mensaje_error;
   }

   function cargar_ficha_contrato_impresion_pago() {
      $query  = " SELECT DATE_FORMAT(A.FECHA_INICIO,'%d/%m/%Y'), DATE_FORMAT(A.FECHA_TERMINO,'%d/%m/%Y'), A.PLAZO";
      $query .= " FROM INVERSION_CONTRATO A";
      $query .= " WHERE A.ANO= ".$this->ano." AND A.C_INSTITUCION=".$this->c_institucion;
      $query .= " AND A.C_PREINVERSION=".$this->c_preinversion." AND A.C_FICHA=".$this->c_ficha;
      $query .= " AND A.C_CONTRATO=".$this->c_contrato;
      $dataset  = mysql_query($query, $this->conector); 
      $nroregistros = @mysql_num_rows($dataset);
      $record = mysql_fetch_row($dataset);
      $this->fecha_inicio=$record[0];
      $this->fecha_termino=$record[1];
      $this->plazo=$record[2];
      if (mysql_errno($this->conector) != 0) { 
         $this->mensaje_error="No se ha podido Actualizar la Base de Datos, mensaje de error de Mysql es: " . mysql_error($this->conector).$query; 
      } else {$this->mensaje_error='';}   
      return $this->mensaje_error;
   }

   function recuperar_informacion_pago() {
//    ANTICIPOS ENTREGADOS
      $query1  = " SELECT SUM(A.VALOR_PAGO) FROM INVERSION_PAGO A ";
      $query1 .= " WHERE A.C_TIPO_PAGO=4 AND A.C_ESTADO IN (2,3) ";
      $query1 .= " AND A.ANO= ".$this->ano." AND A.C_INSTITUCION=".$this->c_institucion;
      $query1 .= " AND A.C_PREINVERSION=".$this->c_preinversion." AND A.C_FICHA=".$this->c_ficha;
      $query1 .= " AND A.C_CONTRATO=".$this->c_contrato." AND C_PAGO<>".$this->nestadopago;
      $dataset1  = mysql_query($query1, $this->conector); 
      $record1 = mysql_fetch_row($dataset1);
      if (round($record1[0],0)>0) {
         $this->mensaje_anticipo_entregado="Anticipos Entregados en V°B° o para Devengar por $".number_format($record1[0],0,',','.');
      } else {
         $this->mensaje_anticipo_entregado="";
      }     
      $this->anticipo_entregado=$record1[0];            
//    ANTICIPOS DEVUELTOS
      $query1  = " SELECT SUM(A.ANTICIPO) FROM INVERSION_PAGO A ";
      $query1 .= " WHERE A.C_TIPO_PAGO IN (1,2,3) AND A.C_ESTADO IN (2,3) ";
      $query1 .= " AND A.ANO= ".$this->ano." AND A.C_INSTITUCION=".$this->c_institucion;
      $query1 .= " AND A.C_PREINVERSION=".$this->c_preinversion." AND A.C_FICHA=".$this->c_ficha;
      $query1 .= " AND A.C_CONTRATO=".$this->c_contrato." AND C_PAGO<>".$this->nestadopago;
      $dataset1  = mysql_query($query1, $this->conector); 
      $record1 = mysql_fetch_row($dataset1);
      if (round($record1[0],0)>0) {
         $this->mensaje_anticipo_recuperado="Anticipos Recuperados en V°B° o para Devengar por $".number_format($record1[0],0,',','.');
      } else {
         $this->mensaje_anticipo_recuperado="";
      }     
      $this->anticipo_devuelto=$record1[0];            
//    RETENCION RECUPERADA
      $query1  = " SELECT SUM(A.RETENCION) FROM INVERSION_PAGO A ";
      $query1 .= " WHERE A.C_TIPO_PAGO IN (1,2,3) AND A.C_ESTADO IN (2,3) ";
      $query1 .= " AND A.ANO= ".$this->ano." AND A.C_INSTITUCION=".$this->c_institucion;
      $query1 .= " AND A.C_PREINVERSION=".$this->c_preinversion." AND A.C_FICHA=".$this->c_ficha;
      $query1 .= " AND A.C_CONTRATO=".$this->c_contrato." AND C_PAGO<>".$this->nestadopago;
      $dataset1  = mysql_query($query1, $this->conector); 
      $record1 = mysql_fetch_row($dataset1);
      if (round($record1[0],0)>0) {
         $this->mensaje_retencion_devuelto="Retenciones Recuperadas en V°B° o para Devengar por $".number_format($record1[0],0,',','.');
      } else {
         $this->mensaje_retencion_devuelto="";
      }     
      $this->retencion_devuelto=$record1[0];            
//    DEVOLUCION RETENCION
      $query1  = " SELECT SUM(A.VALOR_PAGO) FROM INVERSION_PAGO A ";
      $query1 .= " WHERE A.C_TIPO_PAGO = 5 AND A.C_ESTADO IN (2,3) ";
      $query1 .= " AND A.ANO= ".$this->ano." AND A.C_INSTITUCION=".$this->c_institucion;
      $query1 .= " AND A.C_PREINVERSION=".$this->c_preinversion." AND A.C_FICHA=".$this->c_ficha;
      $query1 .= " AND A.C_CONTRATO=".$this->c_contrato." AND C_PAGO<>".$this->nestadopago;
      $dataset1  = mysql_query($query1, $this->conector); 
      $record1 = mysql_fetch_row($dataset1);
      if (round($record1[0],0)>0) {
//         $this->mensaje_retencion_devuelucion="Anticipos Recuperados en V°B° o para Devengar por $".number_format($record1[0],0,',','.');
      } else {
//         $this->mensaje_retencion_devolucion="";
      }     
      $this->retencion_devolucion=$record1[0];            
      $query1  = " SELECT MONTO FROM INVERSION_CONTRATO_MODIFICACION ";
      $query1 .= " WHERE ANO= ".$this->ano." AND C_INSTITUCION=".$this->c_institucion;
      $query1 .= " AND C_PREINVERSION=".$this->c_preinversion." AND C_FICHA=".$this->c_ficha;
      $query1 .= " AND C_CONTRATO=".$this->c_contrato." AND C_MODIFICACION=1";
      $dataset1  = mysql_query($query1, $this->conector); 
      $record1 = mysql_fetch_row($dataset1);
      $this->contrato_inicial=round($record1[0],0);
      $query  = " SELECT A.MONTO, A.ANTICIPO, A.ANTICIPO_DEVUELTO, A.ANTICIPO_SALDO, ";
      $query .= " A.RETENCION, A.RETENCION_DEVUELTO, A.RETENCION_SALDO, A.C_CLASIFICADOR, B.N_CLASIFICADOR, A.RUT_CONTRATISTA, ";
      $query .= " A.ANTICIPO_ENTREGADO, A.RETENCION_DEVOLUCION ";
      $query .= " FROM INVERSION_CONTRATO A, TCLASIFICADOR_PRESUPUESTARIO B";
      $query .= " WHERE A.C_CLASIFICADOR=B.C_CLASIFICADOR AND (A.C_INSTITUCION=B.INSTITUCION OR B.INSTITUCION=0) AND B.ANIO=".$this->ano;
      $query .= " AND A.ANO= ".$this->ano." AND A.C_INSTITUCION=".$this->c_institucion;
      $query .= " AND A.C_PREINVERSION=".$this->c_preinversion." AND A.C_FICHA=".$this->c_ficha;
      $query .= " AND A.C_CONTRATO=".$this->c_contrato;
      $dataset  = mysql_query($query, $this->conector); 
      $record = mysql_fetch_row($dataset);
      $this->anticipo_devuelto=$this->anticipo_devuelto+$record[2];            
      $this->anticipo_entregado=$this->anticipo_entregado+$record[10];            
      $this->retencion_devuelto=$this->retencion_devuelto+$record[5];            
      $this->retencion_devolucion=$this->retencion_devolucion+$record[5];            
      if ($this->contrato_inicial==0) {$this->contrato_inicial=$record[0];}
      return $record;
   }

   function recuperar_pagos_anteriores() {
      $query  = " SELECT PAGADO FROM INVERSION_CONTRATO A";
      $query .= " WHERE ANO= ".$this->ano." AND C_INSTITUCION=".$this->c_institucion;
      $query .= " AND C_PREINVERSION=".$this->c_preinversion." AND C_FICHA=".$this->c_ficha;
      $query .= " AND C_CONTRATO=".$this->c_contrato;
      $dataset  = mysql_query($query, $this->conector); 
      $record = mysql_fetch_row($dataset);
      return $record;
   }

   function recuperar_pagos_anteriores_no_devengados() {
      $query  = " SELECT SUM(VALOR_PAGO-ANTICIPO) FROM INVERSION_PAGO ";
      $query .= " WHERE ANO= ".$this->ano." AND C_INSTITUCION=".$this->c_institucion;
      $query .= " AND C_PREINVERSION=".$this->c_preinversion." AND C_FICHA=".$this->c_ficha;
      $query .= " AND C_CONTRATO=".$this->c_contrato." AND C_PAGO<>".$this->nestadopago;
      $query .= " AND C_ESTADO IN (2,3,9)";
      $dataset  = mysql_query($query, $this->conector); 
      $record = mysql_fetch_row($dataset);
      return $record;
   }

   function eliminar_contrato() {
      $query  =" DELETE FROM INVERSION_CONTRATO WHERE C_FICHA=".$this->c_ficha." AND C_PREINVERSION=".$this->c_preinversion." AND ";
      $query .=" C_INSTITUCION=".$this->c_institucion." AND ANO=".$this->ano." AND C_CONTRATO=".$this->c_contrato;
      $dataset  = mysql_query($query, $this->conector);
      if (mysql_errno($this->conector) != 0) { 
         $this->mensaje_error="No se ha podido Actualizar la Base de Datos, mensaje de error de Mysql es: " . mysql_error($this->conector).$query; 
      } else {$this->mensaje_error='';}   
      return $this->mensaje_error;
   }

   function cambiar_estado($xestado) {
      $query  = " UPDATE INVERSION_CONTRATO SET C_ESTADO=".$xestado;
      $query .= " WHERE C_FICHA=".$this->c_ficha." AND C_PREINVERSION=".$this->c_preinversion." AND ";
      $query .=" C_INSTITUCION=".$this->c_institucion." AND ANO=".$this->ano." AND C_CONTRATO=".$this->c_contrato;
      $dataset  = mysql_query($query, $this->conector);
      if (mysql_errno($this->conector) != 0) { 
         $this->mensaje_error="No se ha podido Actualizar la Base de Datos, mensaje de error de Mysql es: " . mysql_error($this->conector).$query; 
      } else {$this->mensaje_error='';}   
      return $this->mensaje_error;
   }

   function crear_interface_modificaciones() {
      $query  = " SELECT C_MODIFICACION, RESOLUCION, DATE_FORMAT(FECHA_RESOLUCION,'%d/%m/%Y'), MONTO, DATE_FORMAT(FECHA_INICIO,'%d/%m/%Y'),";
      $query .= " DATE_FORMAT(FECHA_ENTREGA_TERRENO,'%d/%m/%Y'), PLAZO, DATE_FORMAT(FECHA_TERMINO,'%d/%m/%Y'), OBSERVACIONES ";
      $query .= " FROM INVERSION_CONTRATO_MODIFICACION ";
      $query .= " WHERE C_FICHA=".$this->c_ficha." AND C_PREINVERSION=".$this->c_preinversion." AND ";
      $query .=" C_INSTITUCION=".$this->c_institucion." AND ANO=".$this->ano." AND C_CONTRATO=".$this->c_contrato;
      $query .= " ORDER BY C_MODIFICACION DESC";
      $dataset  = mysql_query($query, $this->conector); 
      $nroregistros = @mysql_num_rows($dataset);$respuesta="";
      while ($record = mysql_fetch_row($dataset)) { 
         $respuesta .= "<tr bgcolor='#FFFFFF'height='21px'>\n";        
         $respuesta .= "<td align='center' class='form_editsinmarco'>".$record[0]."</td>\n";
         $respuesta .= "<td align='center' class='form_editsinmarco'>".$record[1]."</td>\n";
         $respuesta .= "<td align='center' class='form_editsinmarco'>".$record[2]."</td>\n";
         $respuesta .= "<td align='center' class='form_editsinmarco'>".$record[3]."</td>\n";
         $respuesta .= "<td align='center' class='form_editsinmarco'>".$record[4]."</td>\n";
         $respuesta .= "<td align='center' class='form_editsinmarco'>".$record[5]."</td>\n";
         $respuesta .= "<td align='center' class='form_editsinmarco'>".$record[6]."</td>\n";
         $respuesta .= "<td align='center' class='form_editsinmarco'>".$record[7]."</td>\n";
         $respuesta .= "<td align='center' class='form_editsinmarco'>".$record[8]."</td>\n";
         $respuesta .= "</tr>\n";
      }
      return $respuesta;
   }   

   function respaldar_datos() {
      $query  = " SELECT MAX(C_MODIFICACION) FROM INVERSION_CONTRATO_MODIFICACION";
      $query .= " WHERE C_FICHA=".$this->c_ficha." AND C_PREINVERSION=".$this->c_preinversion." AND ";
      $query .=" C_INSTITUCION=".$this->c_institucion." AND ANO=".$this->ano." AND C_CONTRATO=".$this->c_contrato;
      $dataset  = mysql_query($query, $this->conector); 
      $this->nroregistros = mysql_num_rows($dataset);
      $record = @mysql_fetch_row($dataset);
      $cmodificacion=$record[0]+1;
      $query  = " INSERT INTO INVERSION_CONTRATO_MODIFICACION (SELECT ";
      $query .= " ANO, C_INSTITUCION, C_PREINVERSION, C_FICHA, C_CONTRATO, ".$cmodificacion.", RESOLUCION, FECHA_RESOLUCION,";
      $query .= " MONTO, FECHA_INICIO, FECHA_TERMINO, PLAZO, FECHA_ENTREGA_TERRENO, RETENCION, RETENCION_CONDICIONES, OBSERVACIONES,";
      $query .= $this->c_usuario.",".$this->fecha_registro." FROM INVERSION_CONTRATO ";
      $query .= " WHERE C_FICHA=".$this->c_ficha." AND C_PREINVERSION=".$this->c_preinversion." AND ";
      $query .=" C_INSTITUCION=".$this->c_institucion." AND ANO=".$this->ano." AND C_CONTRATO=".$this->c_contrato.")";
      $dataset  = mysql_query($query, $this->conector); 
      if (mysql_errno($this->conector) != 0) { 
         $this->mensaje_error="No se ha podido Actualizar la Base de Datos, mensaje de error de Mysql es: " . mysql_error($this->conector).$query; 
      } else {$this->mensaje_error='';}   
      return $this->mensaje_error;            
   }
   
   function actualizar_anticipo_entregado_contrato($xmonto) {
      $query  = " UPDATE INVERSION_CONTRATO SET ANTICIPO_ENTREGADO= (ANTICIPO_ENTREGADO+".$xmonto.")";
      $query .= " WHERE C_FICHA=".$this->c_ficha." AND C_PREINVERSION=".$this->c_preinversion." AND ";
      $query .= " C_INSTITUCION=".$this->c_institucion." AND ANO=".$this->ano." AND C_CONTRATO=".$this->c_contrato;
      $dataset  = mysql_query($query, $this->conector); 
      $query  = " UPDATE INVERSION_CONTRATO SET ANTICIPO_SALDO=ANTICIPO_ENTREGADO-ANTICIPO_DEVUELTO ";
      $query .= " WHERE C_FICHA=".$this->c_ficha." AND C_PREINVERSION=".$this->c_preinversion." AND ";
      $query .= " C_INSTITUCION=".$this->c_institucion." AND ANO=".$this->ano." AND C_CONTRATO=".$this->c_contrato;
      $dataset  = mysql_query($query, $this->conector); 
      $query  = " UPDATE INVERSION_CONTRATO SET PAGADO= (PAGADO+".$xmonto.")";
      $query .= " WHERE C_FICHA=".$this->c_ficha." AND C_PREINVERSION=".$this->c_preinversion." AND ";
      $query .= " C_INSTITUCION=".$this->c_institucion." AND ANO=".$this->ano." AND C_CONTRATO=".$this->c_contrato;
      $dataset  = mysql_query($query, $this->conector); 
      if (mysql_errno($this->conector) != 0) { 
         $this->mensaje_error="No se ha podido Actualizar la Base de Datos, mensaje de error de Mysql es: " . mysql_error($this->conector).$query; 
      } else {$this->mensaje_error='';}   
      return $this->mensaje_error;
   }

   function actualizar_anticipo_recuperado_contrato($xmonto) {
      $query  = " UPDATE INVERSION_CONTRATO SET ANTICIPO_DEVUELTO= (ANTICIPO_DEVUELTO+".$xmonto.")";
      $query .= " WHERE C_FICHA=".$this->c_ficha." AND C_PREINVERSION=".$this->c_preinversion." AND ";
      $query .= " C_INSTITUCION=".$this->c_institucion." AND ANO=".$this->ano." AND C_CONTRATO=".$this->c_contrato;
      $dataset  = mysql_query($query, $this->conector); 
      $query  = " UPDATE INVERSION_CONTRATO SET ANTICIPO_SALDO=ANTICIPO_ENTREGADO-ANTICIPO_DEVUELTO ";
      $query .= " WHERE C_FICHA=".$this->c_ficha." AND C_PREINVERSION=".$this->c_preinversion." AND ";
      $query .= " C_INSTITUCION=".$this->c_institucion." AND ANO=".$this->ano." AND C_CONTRATO=".$this->c_contrato;
      $dataset  = mysql_query($query, $this->conector); 
      if (mysql_errno($this->conector) != 0) { 
         $this->mensaje_error="No se ha podido Actualizar la Base de Datos, mensaje de error de Mysql es: " . mysql_error($this->conector).$query; 
      } else {$this->mensaje_error='';}   
      return $this->mensaje_error;
   }

   function actualizar_retencion_recuperada_contrato($xmonto) {
      $query  = " UPDATE INVERSION_CONTRATO SET RETENCION_DEVUELTO= (RETENCION_DEVUELTO+".$xmonto.")";
      $query .= " WHERE C_FICHA=".$this->c_ficha." AND C_PREINVERSION=".$this->c_preinversion." AND ";
      $query .= " C_INSTITUCION=".$this->c_institucion." AND ANO=".$this->ano." AND C_CONTRATO=".$this->c_contrato;
      $dataset  = mysql_query($query, $this->conector); 
      $query  = " UPDATE INVERSION_CONTRATO SET RETENCION_SALDO=RETENCION-RETENCION_DEVUELTO ";
      $query .= " WHERE C_FICHA=".$this->c_ficha." AND C_PREINVERSION=".$this->c_preinversion." AND ";
      $query .= " C_INSTITUCION=".$this->c_institucion." AND ANO=".$this->ano." AND C_CONTRATO=".$this->c_contrato;
      $dataset  = mysql_query($query, $this->conector); 
      if (mysql_errno($this->conector) != 0) { 
         $this->mensaje_error="No se ha podido Actualizar la Base de Datos, mensaje de error de Mysql es: " . mysql_error($this->conector).$query; 
      } else {$this->mensaje_error='';}   
      return $this->mensaje_error;
   }

   function actualizar_canje_retencion_contrato($xmonto) {
      $query  = " UPDATE INVERSION_CONTRATO SET RETENCION_DEVOLUCION= (RETENCION_DEVOLUCION+".$xmonto.")";
      $query .= " WHERE C_FICHA=".$this->c_ficha." AND C_PREINVERSION=".$this->c_preinversion." AND ";
      $query .= " C_INSTITUCION=".$this->c_institucion." AND ANO=".$this->ano." AND C_CONTRATO=".$this->c_contrato;
      $dataset  = mysql_query($query, $this->conector); 
      if (mysql_errno($this->conector) != 0) { 
         $this->mensaje_error="No se ha podido Actualizar la Base de Datos, mensaje de error de Mysql es: " . mysql_error($this->conector).$query; 
      } else {$this->mensaje_error='';}   
      return $this->mensaje_error;
   }

   function actualizar_pagado_contrato($xmonto) {
      $query  = " UPDATE INVERSION_CONTRATO SET PAGADO= (PAGADO+".$xmonto.")";
      $query .= " WHERE C_FICHA=".$this->c_ficha." AND C_PREINVERSION=".$this->c_preinversion." AND ";
      $query .= " C_INSTITUCION=".$this->c_institucion." AND ANO=".$this->ano." AND C_CONTRATO=".$this->c_contrato;
      $dataset  = mysql_query($query, $this->conector); 
      if (mysql_errno($this->conector) != 0) { 
         $this->mensaje_error="No se ha podido Actualizar la Base de Datos, mensaje de error de Mysql es: " . mysql_error($this->conector).$query; 
      } else {$this->mensaje_error='';}   
      return $this->mensaje_error;
   }

   function actualizar_anticipo_entregado_montos_y_fuentes($xmonto,$xmes, $xclasificador) {
      $query  = " UPDATE INVERSION_FINANCIAMIENTO SET G".$xmes."= (G".$xmes."+".$xmonto.")";
      $query .= " WHERE C_FICHA=".$this->c_ficha." AND C_PREINVERSION=".$this->c_preinversion." AND ";
      $query .= " C_INSTITUCION=".$this->c_institucion." AND ANO=".$this->ano;
      $query .= " AND (C_FUENTE_FINANCIAMIENTO LIKE '03%' OR C_FUENTE_FINANCIAMIENTO LIKE '02%') AND C_CLASIFICADOR_PRESUPUESTARIO='".$xclasificador."'";
      $dataset  = mysql_query($query, $this->conector); 
      $query  = " UPDATE INVERSION SET G".$xmes."= (G.".$xmes."+".$xmonto.")";
      $query .= " WHERE C_FICHA=".$this->c_ficha." AND C_PREINVERSION=".$this->c_preinversion." AND ";
      $query .= " C_INSTITUCION=".$this->c_institucion." AND ANO=".$this->ano;
      $dataset  = mysql_query($query, $this->conector); 
      if (mysql_errno($this->conector) != 0) { 
         $this->mensaje_error="No se ha podido Actualizar la Base de Datos, mensaje de error de Mysql es: " . mysql_error($this->conector).$query; 
      } else {$this->mensaje_error='';}   
      return $this->mensaje_error;      
   }  

   function actualizar_datos_contrato_modificado() {
      $xmonto=0;
      $query  =" SELECT MONTO, C_CLASIFICADOR FROM INVERSION_CONTRATO WHERE C_FICHA=".$this->c_ficha." AND C_PREINVERSION=".$this->c_preinversion." AND ";
      $query .=" C_INSTITUCION=".$this->c_institucion." AND ANO=".$this->ano." AND C_CONTRATO=".$this->c_contrato;
      $dataset  = mysql_query($query, $this->conector); 
      $record = @mysql_fetch_row($dataset);
      $xmonto=round($record[0],0);
      $xclasificador=$record[1];
      $query  = "UPDATE INVERSION_CONTRATO SET ";
      $query .= "C_CONDICION='2',";
      $query .= "RESOLUCION='".$this->resolucion."',";
      $query .= "FECHA_RESOLUCION='".$this->fecha_resolucion."',";
      $query .= "MONTO='".$this->monto."',";
      $query .= "FECHA_INICIO='".$this->fecha_inicio."',";
      $query .= "FECHA_TERMINO='".$this->fecha_termino."',";
      $query .= "PLAZO='".$this->plazo."',";
      $query .= "FECHA_ENTREGA_TERRENO='".$this->fecha_entrega_terreno."',";
      $query .= "ANTICIPO='".$this->anticipo."',";
      $query .= "ANTICIPO_CONDICIONES='".$this->anticipo_condiciones."',";
      $query .= "RETENCION='".$this->retencion."',";
      $query .= "RETENCION_CONDICIONES='".$this->retencion_condiciones."',";
      $query .= "OBSERVACIONES='".$this->observaciones."',";
      $query .= "C_ESTADO='".$this->c_estado."',";
      $query .= "C_USUARIO='".$this->c_usuario."',";
      $query .= "FECHA_REGISTRO='".$this->fecha_registro."'";
      $query .= " WHERE C_FICHA=".$this->c_ficha." AND C_PREINVERSION=".$this->c_preinversion." AND ";
      $query .=" C_INSTITUCION=".$this->c_institucion." AND ANO=".$this->ano." AND C_CONTRATO=".$this->c_contrato;
      $dataset  = mysql_query($query, $this->conector);
      $xmonto=round(($this->monto-$xmonto),0);
      $query  = " UPDATE INVERSION_FINANCIAMIENTO SET CONTRATADO= (CONTRATADO+".$xmonto.")";
      $query .= " WHERE C_FICHA=".$this->c_ficha." AND C_PREINVERSION=".$this->c_preinversion." AND ";
      $query .= " C_INSTITUCION=".$this->c_institucion." AND ANO=".$this->ano;
      $query .= " AND (C_FUENTE_FINANCIAMIENTO LIKE '03%' OR C_FUENTE_FINANCIAMIENTO LIKE '02%') AND C_CLASIFICADOR_PRESUPUESTARIO='".$xclasificador."'";
      $dataset  = mysql_query($query, $this->conector); 
      $query  = " UPDATE INVERSION SET CONTRATADO= (CONTRATADO+".$xmonto.")";
      $query .= " WHERE C_FICHA=".$this->c_ficha." AND C_PREINVERSION=".$this->c_preinversion." AND ";
      $query .= " C_INSTITUCION=".$this->c_institucion." AND ANO=".$this->ano;
      $dataset  = mysql_query($query, $this->conector); 
      if (mysql_errno($this->conector) != 0) { 
         $this->mensaje_error="No se ha podido Actualizar la Base de Datos, mensaje de error de Mysql es: " . mysql_error($this->conector).$query; 
      } else {$this->mensaje_error='';}   
      return $this->mensaje_error;
   }

   function existe_garantia() {
      $query  =" SELECT COUNT(*) FROM INVERSION_GARANTIA WHERE C_FICHA=".$this->c_ficha." AND C_PREINVERSION=".$this->c_preinversion." AND ";
      $query .=" C_INSTITUCION=".$this->c_institucion." AND ANO=".$this->ano." AND C_CONTRATO=".$this->c_contrato;
      $dataset  = mysql_query($query, $this->conector);
      $record = mysql_fetch_row($dataset);
      if (mysql_errno($this->conector) != 0) { 
         $this->mensaje_error="No se ha podido Actualizar la Base de Datos, mensaje de error de Mysql es: " . mysql_error($this->conector).$query; 
      } else {$this->mensaje_error='';}   
      return $record[0];
   }

   function calcular_valor_maximo() {
      $query  = " SELECT MAX(C_CONTRATO) FROM INVERSION_CONTRATO";
      $query .= " WHERE ANO= ".$this->ano." AND C_INSTITUCION=".$this->c_institucion;
      $query .= " AND C_PREINVERSION=".$this->c_preinversion." AND C_FICHA=".$this->c_ficha;;
      $dataset  = mysql_query($query, $this->conector); 
      $this->nroregistros = mysql_num_rows($dataset);
      $record = @mysql_fetch_row($dataset);
      return $record[0]+1;
   }  

   function actualizar_contrato($xestado) {
      $xmonto=0;
/*      if ($xestado==2) {
         $query  =" SELECT MONTO FROM INVERSION_CONTRATO WHERE C_FICHA=".$this->c_ficha." AND C_PREINVERSION=".$this->c_preinversion." AND ";
         $query .=" C_INSTITUCION=".$this->c_institucion." AND ANO=".$this->ano." AND C_CONTRATO=".$this->c_contrato;
         $dataset  = mysql_query($query, $this->conector); 
         $record = @mysql_fetch_row($dataset);
         $xmonto=round($record[0],0);
      }*/
      $query  = "REPLACE INTO INVERSION_CONTRATO(ANO,C_INSTITUCION,C_PREINVERSION,C_FICHA,C_CONTRATO,C_CONDICION,C_TIPO_CONTRATO, ";
      $query .= " C_MODALIDAD_CONTRATO,C_LICITACION,RUT_CONTRATISTA,RESOLUCION,FECHA_RESOLUCION,";
      $query .= " C_CLASIFICADOR,MONTO,FECHA_INICIO,FECHA_TERMINO,PLAZO,FECHA_ENTREGA_TERRENO, ";
      $query .= " FECHA_RECEPCION_PROVISORIA, FECHA_RECEPCION_DEFINITIVA,ANTICIPO,ANTICIPO_DEVUELTO,ANTICIPO_SALDO,ANTICIPO_CONDICIONES,RETENCION,RETENCION_DEVUELTO,";
      $query .= "RETENCION_SALDO,RETENCION_CONDICIONES,OBSERVACIONES,C_ESTADO,C_USUARIO,FECHA_REGISTRO, ANTICIPO_ENTREGADO, RETENCION_DEVOLUCION, PAGADO) VALUES('";
      $query .= $this->ano."','".$this->c_institucion."','".$this->c_preinversion."','".$this->c_ficha."','".$this->c_contrato."','".$this->c_condicion."','".$this->c_tipo_contrato."','";
      $query .= $this->c_modalidad_contrato."','".$this->c_licitacion."','".$this->rut_contratista."','".$this->resolucion."','".$this->fecha_resolucion."','";
      $query .= $this->c_clasificador."','".$this->monto."','".$this->fecha_inicio."','".$this->fecha_termino."','".$this->plazo."','".$this->fecha_entrega_terreno."','";
      $query .= $this->fecha_recepcion_provisoria."','".$this->fecha_recepcion_definitiva."','".$this->anticipo."','".$this->anticipo_devuelto."','";
      $query .= $this->anticipo_saldo."','".$this->anticipo_condiciones."','".$this->retencion."','".$this->retencion_devuelto."','";
      $query .= $this->retencion_saldo."','".$this->retencion_condiciones."','";
      $query .= $this->observaciones."','".$this->c_estado."','".$this->c_usuario."','".$this->fecha_registro."','".$this->anticipo_entregado."','".$this->retencion_devolucion."','".$this->pagado."');";
      $dataset  = mysql_query($query, $this->conector);
      if ($xestado==2) {
         $xmonto=round(($this->monto-$xmonto),0);
         $query  = " UPDATE INVERSION_FINANCIAMIENTO SET CONTRATADO= (CONTRATADO+".$xmonto.")";
         $query .= " WHERE C_FICHA=".$this->c_ficha." AND C_PREINVERSION=".$this->c_preinversion." AND ";
         $query .= " C_INSTITUCION=".$this->c_institucion." AND ANO=".$this->ano;
         $query .= " AND (C_FUENTE_FINANCIAMIENTO LIKE '03%' OR C_FUENTE_FINANCIAMIENTO LIKE '02%') AND C_CLASIFICADOR_PRESUPUESTARIO='".$this->c_clasificador."'";
         $dataset  = mysql_query($query, $this->conector); 
         $query  = " UPDATE INVERSION SET CONTRATADO= (CONTRATADO+".$xmonto.")";
         $query .= " WHERE C_FICHA=".$this->c_ficha." AND C_PREINVERSION=".$this->c_preinversion." AND ";
         $query .= " C_INSTITUCION=".$this->c_institucion." AND ANO=".$this->ano;
         $dataset  = mysql_query($query, $this->conector); 
      }      
      if (mysql_errno($this->conector) != 0) { 
         $this->mensaje_error="No se ha podido Actualizar la Base de Datos, mensaje de error de Mysql es: " . mysql_error($this->conector).$query; 
      } else {$this->mensaje_error='';}   
      return $this->mensaje_error;
   }

   function get_nroregistros(){ return $this->nroregistros;}  
   
   function get_error()       { return $this->mensaje_error;}
      
}

//**********************************************************************




// GARANTIAS INVERSIONES
   
include_once 'clases_inversiones_garantias.php';


// Inversion_Garantia_P01

Class Inversion_Garantia_P01 {
   var $conector;
   var $mensaje_error;
   var $nroregistros;
   var $ano;  
   var $c_institucion;
   var $c_preinversion;
   var $c_ficha;
   var $c_garantia;
   var $c_licitacion;
   var $id_empresa;
   var $c_contrato; 
   var $nro_documento="";
   var $banco=0;
   var $fecha;
   var $c_tipo_garantia;
   var $c_detalle_garantia;
   var $n_tipo_garantia="";
   var $monto;
   var $c_unidad_monetaria;
   var $fecha_vencimiento;
   var $fecha_devolucion;  
   var $c_estado; 
   var $c_usuario;
   var $observaciones="";
   var $rut;
   var $memo_juridico;
   var $fecha_memo;
   var $n_estado="";
   var $estado_previo;
   var $fecha_registro;
   
   function Inversion_Garantia_P01($conector) { 
      $this->conector =$conector;
      $this->mensaje_error='';
      $this->estado=0;
      $this->nroregistros=0;
   }
   
   function cargar_datos_garantiaP01() {
      $query  = " SELECT C.N_TIPO_GARANTIA, B.BANCO, E.N_DETALLE_GARANTIA, B.NRO_DOCUMENTO, B.MONTO, D.N_UNIDAD_MONETARIA, DATE_FORMAT(B.FECHA_VENCIMIENTO,'%d/%m/%Y') ";
      $query .= " FROM GARANTIA_ESTADO X, INVERSION_GARANTIA B ";
      $query .= " LEFT JOIN TIPO_GARANTIA C ON (B.C_TIPO_GARANTIA=C.C_TIPO_GARANTIA)";
      $query .= " LEFT JOIN UNIDAD_MONETARIA D ON (B.C_UNIDAD_MONETARIA=D.C_UNIDAD_MONETARIA)";
      $query .= " LEFT JOIN DETALLE_GARANTIA E ON (B.C_DETALLE_GARANTIA=E.C_DETALLE_GARANTIA)";
      $query .= " WHERE B.ANO= ".$this->ano." AND B.C_INSTITUCION=".$this->c_institucion;
      $query .= " AND B.C_PREINVERSION=".$this->c_preinversion." AND B.C_FICHA=".$this->c_ficha;
      $query .= " AND B.C_GARANTIA=".$this->c_garantia;
      $dataset  = mysql_query($query, $this->conector); 
      $record = mysql_fetch_row($dataset);
      $this->n_tipo_garantia=$record[0];
      $this->banco=$record[1];
      $this->n_detalle_garantia=$record[2];
      $this->nro_documento=$record[3];
      $this->monto=$record[4];
      $this->n_unidad_monetaria=$record[5];
      $this->fecha_vencimiento=$record[6];
      if (mysql_errno($this->conector) != 0) { 
         $this->mensaje_error="No se ha podido Actualizar la Base de Datos, mensaje de error de Mysql es: " . mysql_error($this->conector).$query; 
      } else {$this->mensaje_error='';}   
      return $this->mensaje_error;    
   }

   function calcular_valor_maximo() {
      $query  = "SELECT MAX(C_PREINVERSION) FROM PREINVERSION WHERE ANO = " . $this->ano . " AND C_INSTITUCION = ". $this->c_institucion;
      
      $dataset  = mysql_query($query, $this->conector); 
      $this->nroregistros = mysql_num_rows($dataset);
      return $dataset;
   }

   function actualizar_iniciativa() {
      $query1 = "SELECT C_CLASIFICADOR FROM PREINVERSION_VIA_FINANCIAMIENTO WHERE C_VIA_FINANCIAMIENTO=".$this->c_via_financiamiento." AND ANIO=".$this->ano;
      $dataset1= mysql_query($query1, $this->conector); 
      $record1=mysql_fetch_row($dataset1);
      $this->c_clasificador_presupuestario = $record1[0];
      $query  = " REPLACE INTO PREINVERSION";
      $query .= " (ANO, C_INSTITUCION, C_PREINVERSION, N_PREINVERSION, UNIDAD_TECNICA, DIRECCION, ";
      $query .= " C_CLASIFICADOR_PRESUPUESTARIO, FECHA_INICIO, FECHA_TERMINO, ";
      $query .= " PRODUCTO, CODIGO, IMPACTOS, C_SECTOR, OBSERVACIONES, JUSTIFICACION, ";
      $query .= " BENEFICIARIOS, COSTO_TOTAL, SOLICITADO, SALDO_PROXIMOS_ANOS, ";
      $query .= " FECHA_REGISTRO, C_USUARIO, C_ETAPA, C_VIA_FINANCIAMIENTO, TIPO, CODIGO_DV, RATE, DESCRIPTOR, FUENTE_FINANCIAMIENTO) VALUES('";
      $query .= $this->ano."','".$this->c_institucion."','".$this->c_preinversion."','".$this->n_preinversion."','".$this->unidad_tecnica."','".$this->direccion."', '"; 
      $query .= $this->c_clasificador_presupuestario."','".$this->fecha_inicio."','".$this->fecha_termino."','";
      $query .= $this->producto."','".$this->codigo."','".$this->impactos."','".$this->c_sector."','".$this->observaciones."','".$this->justificacion."','";
      $query .= $this->beneficiarios."','".$this->costo_total."','".$this->solicitado."','".$this->saldo_proximos_anos."','";
      $query .= $this->fecha_registro."','".$this->c_usuario."','".$this->c_etapa."','".$this->c_via_financiamiento."','".$this->c_tipo."','".$this->codigo_dv."','".$this->rate."','".$this->descriptor."','".$this->cb_fuentef."')";
      $dataset  = mysql_query($query, $this->conector); 
      if (mysql_errno($this->conector) != 0) { 
         $this->mensaje_error="No se ha podido Actualizar la Base de Datos, mensaje de error de Mysql es: " . mysql_error($this->conector).$query; 
      } else {$this->mensaje_error='';}   
      return $this->mensaje_error;
   }   



}

// RELACION INSTRUMENTOS INVERSION
   
Class Relacion_Instrumentos_Iniciativa {
   var $conector;
   var $mensaje_error;
   var $nroregistros;
   var $ano;
   var $c_institucion;
   var $c_iniciativa;
   var $c_ficha;
   var $c_instrumento;
   var $relacionado="";
   var $relacion1="";
   var $relacion2="";
   var $relacion1_nombre="";
   var $relacion2_nombre="";
   var $usuario;
   var $estado;
   
   function Relacion_Instrumentos_Iniciativa($conector) { 
      $this->conector =$conector;
      $this->mensaje_error='';
      $this->estado=0;
      $this->nroregistros=0;
   }
   
   function eliminar_relacion_instrumentos() {
      $query  = " DELETE FROM RELACION_INSTRUMENTO_INICIATIVA";
      $query .= " WHERE ANO=".$this->ano." AND C_INSTITUCION=".$this->c_institucion;
      $query .= " AND C_INICIATIVA=".$this->c_iniciativa." AND C_FICHA=".$this->c_ficha;
      $dataset  = mysql_query($query, $this->conector); 
      if (mysql_errno($this->conector) != 0) { 
         $this->mensaje_error="No se ha podido Actualizar la Base de Datos, mensaje de error de Mysql es: " . mysql_error($this->conector).$query; 
      } else {$this->mensaje_error='';}   
      return $this->mensaje_error;
   }
   
   function actualizar_relacion_instrumentos() {
      $query  = " REPLACE INTO RELACION_INSTRUMENTO_INICIATIVA (ANO, C_INSTITUCION, C_INICIATIVA,C_FICHA, C_INSTRUMENTO, RELACIONADO, RELACION1, RELACION2,RELACION1_NOMBRE, RELACION2_NOMBRE) VALUES(";      
      $query .= "'".$this->ano."','".$this->c_institucion."','".$this->c_iniciativa."','".$this->c_ficha."',";
      $query .= "'".$this->c_instrumento."','".$this->relacionado."','".$this->relacion1."','".$this->relacion2."','";
      $query .= $this->relacion1_nombre."','".$this->relacion2_nombre."')";
      $dataset  = mysql_query($query, $this->conector);
      if (mysql_errno($this->conector) != 0) { 
         $this->mensaje_error="No se ha podido Actualizar la Base de Datos, mensaje de error de Mysql es: " . mysql_error($this->conector).$query; 
      } else {$this->mensaje_error='';}   
      return $this->mensaje_error;
   }

   function get_nroregistros(){ return $this->nroregistros;}  
   
   function get_error()       { return $this->mensaje_error;}
      
}

//*****************************************

// ASIGNACION

//*****************************************
   
Class Asignacion {
   var $conector;
   var $mensaje_error;
   var $nroregistros;
   var $ano;
   var $c_asignacion;
   var $fecha;
   var $c_tipo_solicitud;
   var $fecha_envio;
   var $ordinario_envio="";
   var $fecha_decreto;
   var $decreto="";
   var $fecha_toma_razon;
   var $c_estado;
   var $usuario;
   var $filaseleccionada;
   var $observacion="";
   var $total;
   var $total_disminucion;
   var $documento = "";
   
   function Asignacion($conector) { 
      $this->conector =$conector;
      $this->mensaje_error='';
      $this->c_estado=0;
      $this->nroregistros=0;
   }
   
   function consultar_datos_asignacion($xnano,$xasignacion) {
      $query  = " SELECT C_ASIGNACION, C_TIPO_SOLICITUD, FECHA FROM ASIGNACION WHERE ANO=".$xnano." AND C_ASIGNACION=".$xasignacion;
      $dataset  = mysql_query($query, $this->conector); 
      $record = mysql_fetch_row($dataset);
      $this->c_tipo_solicitud=$record[1];
      $this->fecha->$record[2];
      return true;
   }

   function cargar_grilla_asignacion($xnano,$xtipo,$xasignacion,$xestado) {
      $datainforme=array();$filas=0;$this->filaseleccionada=-1;
      $query  = " SELECT A.C_ASIGNACION, 
			DATE_FORMAT(A.FECHA,'%d/%m/%Y'), 
			(CASE A.C_TIPO_SOLICITUD WHEN '1' THEN 'Decreto' WHEN '2' THEN 'Resolución Afecta' WHEN '3' THEN 'Resolución Afecta FIC' WHEN '4' THEN 'Resolución Exenta' ELSE '' END), 
			D.NUM_OFICIO, 
			(CASE
            	WHEN A.C_TIPO_SOLICITUD='1' THEN A.DECRETO
                ELSE D.NUM_RESOLUCION
            END) AS NUM_DECRETO_RESOLUCION,
			(CASE
            	WHEN A.C_TIPO_SOLICITUD='1' THEN DATE_FORMAT(A.FECHA_DECRETO,'%d/%m/%Y')
                ELSE DATE_FORMAT(D.FECHA_RESOLUCION,'%d/%m/%Y')
            END) AS FECHA_DECRETO_RESOLUCION,
			DECRETO,
			DATE_FORMAT(A.FECHA_TOMA_RAZON,'%d/%m/%Y'), 
			B.N_ESTADO,
			A.C_ESTADO, 
			'', 
			SUM(C.MONTO)/1000,
			IF (A.DOCUMENTO != '',CONCAT(B.N_ESTADO,'<br>','<a href=../adjuntos/',A.DOCUMENTO,' target= _blank>','Ver Documento','</a>') , B.N_ESTADO),
			SUM(C.MONTO_DISMINUCION)/1000,
			DATE_FORMAT(D.FECHA_OFICIO,'%d/%m/%Y')
			FROM ASIGNACION_ESTADO B, ASIGNACION_INVERSION C, ASIGNACION A
			LEFT JOIN ASIGNACION_OFICINAPARTE D ON (A.C_ASIGNACION=D.C_ASIGNACION AND A.ANO=D.ANO)
			WHERE A.C_ESTADO=B.C_ESTADO AND A.ANO=C.ANO AND A.C_ASIGNACION=C.C_ASIGNACION
			and A.ANO =".$xnano." and A.C_ESTADO<>9";
      if ($xtipo!=0) {
         $query .= " AND A.C_TIPO_SOLICITUD=".$xtipo;
      }
	  if ($xestado!=0) {
         $query .= " AND A.C_ESTADO=".$xestado;
      }
      $query .= " GROUP BY A.ANO, A.C_ASIGNACION ";
      $query .= " ORDER BY C_ASIGNACION DESC";
      $dataset  = mysql_query($query, $this->conector); 
      $this->nroregistros = mysql_num_rows($dataset);
      $total1=0;	// suma de los aumentos de costos
	  $total2=0;	// suma de las disminuciones de costos
      while ($record = mysql_fetch_row($dataset)) { 
         // for ($j=0; $j<13;$j++) {
		 for ($j=0; $j<15;$j++) {           
            if ($j==0) {
              if ($record[$j]==$xasignacion) {$this->filaseleccionada=$filas;}
            }
            $datainforme[$filas][$j]=$record[$j];
            if ($j==11) {$total1=$total1+$datainforme[$filas][$j];$datainforme[$filas][$j]=number_format($datainforme[$filas][$j],0,',','.');}
			if ($j==13) {$total2=$total2+$datainforme[$filas][$j];$datainforme[$filas][$j]=number_format($datainforme[$filas][$j],0,',','.');}
            if ($j==0) {
               $obs="";
     					 $query1  = " SELECT DATE_FORMAT(FECHA,'%d/%m/%Y'), OBSERVACION FROM ASIGNACION_OBSERVACION WHERE ANO=".$xnano." AND C_ASIGNACION=".$record[0];
               $dataset1  = mysql_query($query1, $this->conector); 
               while ($record1 = mysql_fetch_row($dataset1)) { 
                  if ($obs!='') {$obs .= " / ";}
				  $obs .=$record1[1]." (".$record1[0].")";
			   }			
               $datainforme[$filas][10]=$obs;
            }
         }           
         $filas=$filas+1;
      }
      $this->total=number_format($total1,0,',','.');
	  $this->total_disminucion=number_format($total2,0,',','.');
      $rows = array();
      for ($i=0; $i<$filas; $i++) {
   $cols = array();
   // for ($j=0;$j<13;$j++) {
   for ($j=0;$j<15;$j++) {
            $cols[] = '"'.addslashes($datainforme[$i][$j]).'"';
   } 
   $rows[] = "\t[".implode(",", $cols)."]"; 
      } 
      $datagrilla="[\n".implode(",\n",$rows)."\n];\n"; 
      return $datagrilla;
   }

   function actualizar_asignacion() {
      $query  = " UPDATE ASIGNACION SET FECHA=".$this->fecha.",C_TIPO_SOLICITUD=".$this->c_tipo_solicitud.", C_ESTADO=".$this->c_estado;
      $query .= " WHERE C_ASIGNACION=".$this->c_asignacion." AND ANO=".$this->ano;
      $dataset  = mysql_query($query, $this->conector);
      if (mysql_errno($this->conector) != 0) { 
         $this->mensaje_error="No se ha podido Actualizar la Base de Datos, mensaje de error de Mysql es: " . mysql_error($this->conector).$query; 
      } else {$this->mensaje_error='';}   
      return $this->mensaje_error;
   }

   /**
    * funcion que envia las asignaciones a URS
    */
   function enviar_urs(){
      $query  = " UPDATE ASIGNACION SET C_ESTADO = ". $this->c_estado ." 
      WHERE C_ASIGNACION = ". $this->c_asignacion ." AND ANO = ". $this->ano ."
      ";
      $dataset = mysql_query($query, $this->conector);

      if (mysql_errno($this->conector) != 0) { 
         $this->mensaje_error="No se ha podido Actualizar la Base de Datos, mensaje de error de Mysql es: " . mysql_error($this->conector).$query; 
      } else {$this->mensaje_error='';}   
      return $this->mensaje_error;
   }

   /**
    * Funcion que apreba las asignaciones y las deja disponibles para imprimir
    * propuesta de resolucion
    */

   function aprobar_asignacion(){
      $query  = " UPDATE ASIGNACION SET C_ESTADO = ". $this->c_estado ." 
      WHERE C_ASIGNACION = ". $this->c_asignacion ." AND ANO = ". $this->ano ."
      ";
      $dataset = mysql_query($query, $this->conector);

      if (mysql_errno($this->conector) != 0) { 
         $this->mensaje_error="No se ha podido Actualizar la Base de Datos, mensaje de error de Mysql es: " . mysql_error($this->conector).$query; 
      } else {$this->mensaje_error='';}   
      return $this->mensaje_error;
   }
   
   /* Aprueba solicitud de asignación del tipo resolución exenta */
   
   function aprobar_asignacion_res_exenta(){
      $query  = " UPDATE ASIGNACION SET C_ESTADO = ". $this->c_estado ." 
      WHERE C_ASIGNACION = ". $this->c_asignacion ." AND ANO = ". $this->ano ."
      ";
      $dataset = mysql_query($query, $this->conector);

      if (mysql_errno($this->conector) != 0) { 
         $this->mensaje_error="No se ha podido Actualizar la Base de Datos, mensaje de error de Mysql es: " . mysql_error($this->conector).$query; 
      } else {$this->mensaje_error='';}   
      return $this->mensaje_error;
   }
   
   function asignacion_con_res(){
      $query  = " UPDATE ASIGNACION SET C_ESTADO = ". $this->c_estado ." 
      WHERE C_ASIGNACION = ". $this->c_asignacion ." AND ANO = ". $this->ano ."
      ";
      $dataset = mysql_query($query, $this->conector);

      if (mysql_errno($this->conector) != 0) { 
         $this->mensaje_error="No se ha podido Actualizar la Base de Datos, mensaje de error de Mysql es: " . mysql_error($this->conector).$query; 
      } else {$this->mensaje_error='';}   
      return $this->mensaje_error;
   }
   
   function validar_monto($xano, $asig){
   	
   	$query = "SELECT B.SOLICITADO, B.ASIGNADO, B.SALDO_POR_ASIGNAR, A.MONTO FROM ASIGNACION_INVERSION A 
              LEFT JOIN INVERSION B ON (A.ANO = B.ANO AND A.C_PREINVERSION = B.C_PREINVERSION and A.C_INSTITUCION = B.C_INSTITUCION)
              WHERE A.ANO=".$xano." AND A.C_ASIGNACION=".$asig;
   	$dataset = mysql_query($query, $this->conector);
	
   	while($rec = mysql_fetch_row($dataset)){
   		

      $solci = $rec[0];
      $asigna = $rec[1];
      $salxasig= $rec[2];
      $monto  = $rec[3];
	  
   	}
   	
   	echo "Solicitado:".$solci." Asignado:".$asigna." Saldo x Asignar:".$salxasig." Monto:".$monto;
   	
   	return $rec;
   	
   	
  }

   function insertar_asignacion() {
      $query  = " INSERT INTO ASIGNACION (ANO, C_ASIGNACION, FECHA, C_TIPO_SOLICITUD, C_ESTADO) VALUES('";
      $query .= $this->ano."','".$this->c_asignacion."','".$this->fecha."','".$this->c_tipo_solicitud."','".$this->c_estado."')";
      $dataset  = mysql_query($query, $this->conector);
      if (mysql_errno($this->conector) != 0) { 
         $this->mensaje_error="No se ha podido Actualizar la Base de Datos, mensaje de error de Mysql es: " . mysql_error($this->conector).$query; 
      } else {$this->mensaje_error='';}   
      return $this->mensaje_error;
   }

   function anular_asignacion() {
      $query  = " UPDATE ASIGNACION SET C_ESTADO=9 WHERE C_ASIGNACION=".$this->c_asignacion." AND ANO=".$this->ano;
      $dataset  = mysql_query($query, $this->conector);
      if (mysql_errno($this->conector) != 0) { 
         $this->mensaje_error="No se ha podido Actualizar la Base de Datos, mensaje de error de Mysql es: " . mysql_error($this->conector).$query; 
      } else {$this->mensaje_error='';}   
      return $this->mensaje_error;
   }

   function observar_asignacion($estado) {
      $query  = " UPDATE ASIGNACION SET C_ESTADO=". $estado ." WHERE C_ASIGNACION=".$this->c_asignacion." AND ANO=".$this->ano;
      $dataset  = mysql_query($query, $this->conector);
      
      $query  = " INSERT INTO ASIGNACION_OBSERVACION(ANO,C_ASIGNACION, OBSERVACION) VALUES('". $this->ano ."','".$this->c_asignacion."','".$this->observacion."');";
      $dataset  = mysql_query($query, $this->conector);
      if (mysql_errno($this->conector) != 0) { 
         $this->mensaje_error="No se ha podido Actualizar la Base de Datos, mensaje de error de Mysql es: " . mysql_error($this->conector).$query; 
      } else {$this->mensaje_error='';}   
      return $this->mensaje_error;
   }

   function get_observaciones(){
      $observaciones = array();
      $query  = " SELECT * FROM ASIGNACION_OBSERVACION
                  WHERE C_ASIGNACION ='". $this->c_asignacion ."' AND 
                  ANO = '". $this->ano ."'";

      $dataset = mysql_query($query, $this->conector);

      while ($observacion = mysql_fetch_assoc($dataset)) {
         $observaciones[] = $observacion;
      }

      return $observaciones;
   }

   function eliminar_valores() {
      $query  = " DELETE FROM ASIGNACION_INVERSION WHERE C_ASIGNACION=".$this->c_asignacion." AND ANO=".$this->ano;
      $dataset  = mysql_query($query, $this->conector);
      if (mysql_errno($this->conector) != 0) { 
         $this->mensaje_error="No se ha podido Actualizar la Base de Datos, mensaje de error de Mysql es: " . mysql_error($this->conector).$query; 
      } else {$this->mensaje_error='';}   
      return $this->mensaje_error;
   }

   function enviar_asignacion() {
      $query  = " UPDATE ASIGNACION SET FECHA_ENVIO=".$this->fecha_envio.", ORDINARIO_ENVIO='".$this->ordinario_envio."', C_ESTADO=".$this->c_estado." WHERE C_ASIGNACION=".$this->c_asignacion." AND ANO=".$this->ano;
      $dataset  = mysql_query($query, $this->conector);
      if (mysql_errno($this->conector) != 0) { 
         $this->mensaje_error="No se ha podido Actualizar la Base de Datos, mensaje de error de Mysql es: " . mysql_error($this->conector).$query; 
      } else {$this->mensaje_error='';}   
      return $this->mensaje_error;
   }

/*   function actualizar_asignacion_inversion() {
      $query  = "SELECT * FROM ASIGNACION_INVERSION WHERE C_ASIGNACION=".$this->c_asignacion." AND ANO=".$this->ano;
      $dataset  = mysql_query($query, $this->conector); 
      while ($record = mysql_fetch_row($dataset)) { 
         $query1 = "SELECT C_CLASIFICADOR_PRESUPUESTARIO FROM INVERSION WHERE ANO=".$record[0]." 

      }

   }*/

   function ingresar_decreto() {
      $query  = " UPDATE ASIGNACION SET FECHA_DECRETO=".$this->fecha_decreto.", DECRETO='".$this->decreto."', C_ESTADO=".$this->c_estado." WHERE C_ASIGNACION=".$this->c_asignacion." AND ANO=".$this->ano;
      $dataset  = mysql_query($query, $this->conector);
      if (mysql_errno($this->conector) != 0) { 
         $this->mensaje_error="No se ha podido Actualizar la Base de Datos, mensaje de error de Mysql es: " . mysql_error($this->conector).$query; 
      } else {$this->mensaje_error='';}   
      return $this->mensaje_error;
   }
  
  function grabar_termino() {
    
    // Se actualiza fecha de toma de razon
      
    $query  = " UPDATE ASIGNACION 
      SET FECHA_TOMA_RAZON=".$this->fecha_toma_razon.", 
      C_ESTADO=".$this->c_estado.", 
      DOCUMENTO='". $this->documento."',
	  DECRETO='". $this->decreto."',
	  FECHA_DECRETO=".$this->fecha_decreto." 
      WHERE C_ASIGNACION=".$this->c_asignacion." AND 
      ANO=".$this->ano;
    $dataset  = mysql_query($query, $this->conector);
    
    if (mysql_errno($this->conector) != 0) { 
      $this->mensaje_error="No se ha podido Actualizar la Base de Datos, mensaje de error de Mysql es: " . mysql_error($this->conector).$query; 
    } else {
      $this->mensaje_error='';
    }  
    
    /*****************************************/
    /****ACTUALIZACIÓN DE MONTOS Y FUENTES****/
    /*****************************************/
    
    /***RECORRER ASIGNACIONES DE SOLICITUD***/
    $query2  = " SELECT C_INSTITUCION,
        C_PREINVERSION,
        C_FICHA,
        C_ITEM_PRESUPUESTARIO,
        MONTO,
		MONTO_DISMINUCION,
        C_FUENTE_FINANCIAMIENTO
      FROM ASIGNACION_INVERSION 
      WHERE C_ASIGNACION=".$this->c_asignacion." 
      AND ANO=".$this->ano;
    $dtsAsigInv = mysql_query($query2, $this->conector);
    
    // Se recorre cada linea de financiamiento correspondiente a la asignación
    while($asig_inv = mysql_fetch_assoc($dtsAsigInv)){
      
      $C_INSTITUCION          = $asig_inv['C_INSTITUCION'];
      $C_PREINVERSION         = $asig_inv['C_PREINVERSION'];
      $C_FICHA                = $asig_inv['C_FICHA'];
      $C_ITEM_PRESUPUESTARIO  = $asig_inv['C_ITEM_PRESUPUESTARIO'];
      $C_FUENTE_FINANCIAMIENTO 	= $asig_inv['C_FUENTE_FINANCIAMIENTO']; 
      $MONTO					= $asig_inv['MONTO'];
	  $MONTO_DISMINUCION		= $asig_inv['MONTO_DISMINUCION'];
      
      /***OBTENER REGISTRO DE INVERSION***/
      $query2 = " SELECT * 
         FROM INVERSION 
         WHERE ANO=".$this->ano." 
         AND C_INSTITUCION = ".$C_INSTITUCION ." 
         AND C_PREINVERSION = ".$C_PREINVERSION ." 
         AND C_FICHA = ".$C_FICHA;
      $dtsInv = mysql_query($query2, $this->conector);
      
      $cant = mysql_num_rows($dtsInv);  
      // echo "<script>console.log('Cantidad de registros: ".$cant."');</script>";
        
      if(mysql_num_rows($dtsInv)!=1){
        /*ERROR..Debe encontrar 1 registro..ni más ni menos*/
      }else{
         // Datos de inversion asociado a la asignación
         /***OBTENER DATOS DE INVERSION***/
         $costo_ebi=0;
         $costo_core=0;
         $costo_total=0;
         $gastado_anos_anteriores=0;
         $solicitado=0;
         $saldo_proximo_ano=0;
         $saldos_proximos_anos=0;
         $asignado=0;
         $asignacion_disponible=0;
         $saldo_por_asignar=0;
         $total_programado=0;
         $contratado=0;
         $pagado=0;
         $saldo=0;
         $arrastre=0;
        
         $inv = mysql_fetch_assoc($dtsInv);
		 
		 // Obtiene los datos de la inversion y los ingresa a variables
		 
         if (isset($inv['COSTO_EBI']))          { $costo_ebi         = $inv["COSTO_EBI"]; }
         if (isset($inv['COSTO_CORE']))         { $costo_core        = $inv["COSTO_CORE"]; }
         if (isset($inv['COSTO_TOTAL']))        { $costo_total       = $inv["COSTO_TOTAL"]; }
         if (isset($inv['GASTADO_ANOS_ANTERIORES'])) { $gastado_anos_anteriores  =$inv["GASTADO_ANOS_ANTERIORES"]; }
         if (isset($inv['SOLICITADO']))         { $solicitado        = $inv["SOLICITADO"]; }
         if (isset($inv['SALDO_PROXIMO_ANO']))  { $saldo_proximo_ano = $inv["SALDO_PROXIMO_ANO"]; }
         if (isset($inv['SALDO_PROXIMOS_ANOS'])) { $saldos_proximos_anos =$inv["SALDO_PROXIMOS_ANOS"];}
         if (isset($inv['ASIGNADO']))           { $asignado          = $inv["ASIGNADO"]; }
         if (isset($inv['ASIGNACION_DISPONIBLE'])) { $asignacion_disponible =$inv["ASIGNACION_DISPONIBLE"];}
         if (isset($inv['SALDO_POR_ASIGNAR']))  { $saldo_por_asignar = $inv["SALDO_POR_ASIGNAR"];}
         if (isset($inv['TOTAL_PROGRAMADO']))   { $total_programado  = $inv["TOTAL_PROGRAMADO"];}
         if (isset($inv['CONTRATADO']))         { $contratado        = $inv["CONTRATADO"];}
         if (isset($inv['PAGADO']))             { $pagado            = $inv["PAGADO"];}
         if (isset($inv['SALDO']))              { $saldo             = $inv["SALDO"];}
         if (isset($inv['ARRASTRE']))           { $arrastre          = $inv["ARRASTRE"];}
      
	    // Actualiza ASIGNADO, ASIGNACION_DISPONIBLE y SALDO_POR_ASIGNAR de la inversion
				  
		$montoFinal_asignado_inv = $asignado + $MONTO - $MONTO_DISMINUCION;
		$montoFinal_asignacion_disponible_inv = $asignacion_disponible + $MONTO - $MONTO_DISMINUCION;
		$montoFinal_saldo_por_asignar_inv = $saldo_por_asignar - $MONTO + $MONTO_DISMINUCION;
		
		$query2 = " UPDATE INVERSION 
                        SET ASIGNADO = ".$montoFinal_asignado_inv.",
						ASIGNACION_DISPONIBLE = ".$montoFinal_asignacion_disponible_inv.",
						SALDO_POR_ASIGNAR = ".$montoFinal_saldo_por_asignar_inv." 
                        
                        WHERE ANO=".$this->ano." 
                        AND C_INSTITUCION = ".$C_INSTITUCION ." 
                        AND C_PREINVERSION = ".$C_PREINVERSION ." 
                        AND C_FICHA = ".$C_FICHA;
	  
	    /***EJECUTAR ACTUALIZACIÓN de Inversion***/
        $update_inversiones  = mysql_query($query2, $this->conector);
        
				
        /***OBTENER LISTA DE INVERSION_FINANCIAMIENTO DE LA INVERSION***/
        $query2 = " SELECT * 
            FROM INVERSION_FINANCIAMIENTO 
            WHERE ANO=".$this->ano." 
            AND C_INSTITUCION = ".$C_INSTITUCION ." 
            AND C_PREINVERSION = ".$C_PREINVERSION ." 
            AND C_FICHA = ".$C_FICHA;
        $dtsInvFin = mysql_query($query2, $this->conector);
        
         if(mysql_num_rows($dtsInvFin)==0) {
          /*Error...debe haber al menos 1 registro*/
         } else {
            // Se recorre la informacion de las inversiones asociadas
            while($inv_Fin = mysql_fetch_assoc($dtsInvFin)){
              
               // So obtiene fuente de financiamiento y clasificador presupuestario para identificar línea de financiamiento a modificar
			   $invfin_cff = $inv_Fin["C_FUENTE_FINANCIAMIENTO"];
			   $invfin_ccp = $inv_Fin["C_CLASIFICADOR_PRESUPUESTARIO"];
			   
			   // Se obtiene ASIGNADO, ASIGNACION_DISPONIBLE y SALDO_POR_ASIGNAR de la inversion financiamiento
			   $invfin_asignado   = $inv_Fin["ASIGNADO"];
			   $invfin_asignacion_disponible   = $inv_Fin["ASIGNACION_DISPONIBLE"];
			   $invfin_saldo_por_asignar   = $inv_Fin["SALDO_POR_ASIGNAR"];
               
               if($invfin_ccp==$C_ITEM_PRESUPUESTARIO && $invfin_cff==$C_FUENTE_FINANCIAMIENTO){
                 

                  /*ACTUALIZA SOLICITADO*/
                  // $montoFinal = $invfin_s + $MONTO;
				  
				  // Actualiza ASIGNADO, ASIGNACION_DISPONIBLE y SALDO_POR_ASIGNAR de la inversion financiamiento
				  
				  $montoFinal_asignado_invfin = $invfin_asignado + $MONTO - $MONTO_DISMINUCION;
				  $montoFinal_asignacion_disponible_invfin = $invfin_asignacion_disponible + $MONTO - $MONTO_DISMINUCION;
				  $montoFinal_saldo_por_asignar_invfin = $invfin_saldo_por_asignar - $MONTO + $MONTO_DISMINUCION;
				  

                  $query2 = " UPDATE INVERSION_FINANCIAMIENTO 
                        SET ASIGNADO = ".$montoFinal_asignado_invfin.",
						ASIGNACION_DISPONIBLE = ".$montoFinal_asignacion_disponible_invfin.",
						SALDO_POR_ASIGNAR = ".$montoFinal_saldo_por_asignar_invfin." 
                        
                        WHERE ANO=".$this->ano." 
                        AND C_INSTITUCION = ".$C_INSTITUCION ." 
                        AND C_PREINVERSION = ".$C_PREINVERSION ." 
                        AND C_FICHA = ".$C_FICHA." 
                        AND C_FUENTE_FINANCIAMIENTO = '".$C_FUENTE_FINANCIAMIENTO."' 
                        AND C_CLASIFICADOR_PRESUPUESTARIO='".$C_ITEM_PRESUPUESTARIO."'";
               } else {
                  
                     // Identifiquese
                     // caso 1) no encuentra los registros por ende los crea
                     // Crear los nuevos registros en montos y fuentes
                     $query2 = "INSERT INTO INVERSION_FINANCIAMIENTO(
                        ANO,
                        C_INSTITUCION,
                        C_PREINVERSION,
                        C_FICHA,
                        C_FUENTE_FINANCIAMIENTO,
                        C_CLASIFICADOR_PRESUPUESTARIO,
                        C_ESTADO,
                        SOLICITADO,
                        COSTO_TOTAL,
                        ASIGNADO
                        )
                        VALUES(
                           ".$this->ano.",
                           ".$C_INSTITUCION.",
                           ".$C_PREINVERSION.",
                           ".$C_FICHA.",
                           '".$C_FUENTE_FINANCIAMIENTO."',
                           '".$C_ITEM_PRESUPUESTARIO."',
                           0,
                           ".$MONTO.",
                           ".$MONTO.",
                           ".$MONTO."
                        )
                     ";
                  
               }
			   /***EJECUTAR ACTUALIZACIÓN de Inversion Financiamiento***/
               $actualiza = mysql_query($query2, $this->conector);
			
            }
         }
      }
    }
	// fin de recorrido de asignación
	
    /*****************************************/
    /****ACTUALIZACIÓN DE MONTOS Y FUENTES****/
    /*****************************************/
    if (mysql_errno($this->conector) != 0) { 
      $this->mensaje_error="No se ha podido Actualizar la Base de Datos, mensaje de error de Mysql es: " . mysql_error($this->conector).$query; 
    } 

    return $this->mensaje_error;
  }

   function calcular_valor_maximo() {
      $query  = " SELECT MAX(C_ASIGNACION) FROM ASIGNACION";
      $query .= " WHERE ANO= ".$this->ano;
      $dataset  = mysql_query($query, $this->conector); 
      $this->nroregistros = mysql_num_rows($dataset);
      $record = @mysql_fetch_row($dataset);
      return $record[0]+1;
   }
   
   /*********/
   /***PDF***/
   /*********/
   
   function set_ano($ano){
      $this->ano=$ano;
   }
   
   function getDataForPDF($id_asignacion){
      $query  = " SELECT
                  A.CODIGO                   AS CODIGO, 
                  A.NOMBRE                   AS NOMBRE, 
                  B.C_ITEM_PRESUPUESTARIO       AS ITEMPRESUPUESTARIO, 
                  B.MONTO                 AS MONTO,
                  A.C_CLASIFICADOR_PRESUPUESTARIO  AS CLASIFICADORPRESUPUESTARIO,
                  F.SIGLA_FUENTE_FINANCIAMIENTO AS FUENTEFINANCIAMIENTO 
               FROM 
                  INVERSION A, 
                  ASIGNACION_INVERSION B
               LEFT JOIN 
                  FUENTE_FINANCIAMIENTO F ON(F.C_FUENTE_FINANCIAMIENTO = B.C_FUENTE_FINANCIAMIENTO) 
               WHERE 
                  A.ANO             = B.ANO AND 
                  A.C_INSTITUCION   = B.C_INSTITUCION AND 
                  A.C_PREINVERSION  = B.C_PREINVERSION AND 
                  A.C_FICHA         = B.C_FICHA AND 
                  A.ANO          = ".$this->ano." AND 
                  B.C_ASIGNACION    = ".$id_asignacion;
      
      $datosAsignacion = mysql_query($query, $this->conector);
      
      $array = array();
      while($datos = mysql_fetch_array($datosAsignacion)){
         
         $dato = array(
            'CODIGO'                => $datos["CODIGO"],
            'NOMBRE'                => $datos["NOMBRE"],
            'ITEMPRESUPUESTARIO'       => $datos["ITEMPRESUPUESTARIO"],
            'MONTO'                 => $datos["MONTO"],
            'CLASIFICADORPRESUPUESTARIO'=> $datos["CLASIFICADORPRESUPUESTARIO"],
            'FUENTEFINANCIAMIENTO'     => $datos["FUENTEFINANCIAMIENTO"]);
         
         $array[] = $dato;
         
      }
      return $array;
   }
   
   function get_grupoSubtituloItem($array){
      $retorno = array();
      foreach ($array as $asignacion) {
         $SubtItem = $asignacion["SUBTITULO"].".".$asignacion["ITEM"];
         
         if(in_array($SubtItem, $retorno)) {
         }else{
            $retorno[] = $SubtItem;
         }
      }
      return $retorno;
   }
   
   /*********/
   /***PDF***/
   /*********/
   
   function getPDF($ano,$id_asignacion){
      
      $conector = $this->conector;
      
      /**********************************/
      /***OBTENER DETALLE DE SOLICITUD***/
      /**********************************/
      $query  = " SELECT
                  A.CODIGO                   AS CODIGO, 
                  A.NOMBRE                   AS NOMBRE, 
                  A.SOLICITADO               AS SOLICITADO,
                  A.SALDO_PROXIMO_ANO           AS SOLICITADOPROXAGNO,
                  A.SALDO_PROXIMOS_ANOS         AS SOLICITADOPROXAGNOS,
                  B.C_ITEM_PRESUPUESTARIO       AS ITEMPRESUPUESTARIO, 
                  (B.MONTO - B.MONTO_DISMINUCION)                 AS MONTO,
                  A.C_CLASIFICADOR_PRESUPUESTARIO  AS CLASIFICADORPRESUPUESTARIO,
                  F.SIGLA_FUENTE_FINANCIAMIENTO AS FUENTEFINANCIAMIENTO ,
                  B.C_INSTITUCION               AS INSTITUCION,
                  B.C_PREINVERSION           AS PREINVERSION,
                  B.C_FICHA                  AS FICHA
               FROM 
                  INVERSION A, 
                  ASIGNACION_INVERSION B
               LEFT JOIN 
                  FUENTE_FINANCIAMIENTO F ON(F.C_FUENTE_FINANCIAMIENTO = B.C_FUENTE_FINANCIAMIENTO) 
               WHERE 
                  A.ANO             = B.ANO AND 
                  A.C_INSTITUCION   = B.C_INSTITUCION AND 
                  A.C_PREINVERSION  = B.C_PREINVERSION AND 
                  A.C_FICHA         = B.C_FICHA AND 
                  A.ANO          = ".$ano." AND 
                  B.C_ASIGNACION    = ".$id_asignacion;
      
      $datosAsignacion = mysql_query($query, $conector);
      
      $array_detSoli = array();
      while($datos = mysql_fetch_array($datosAsignacion)){
         
         $dato = array(
            'CODIGO'                => $datos["CODIGO"],
            'NOMBRE'                => $datos["NOMBRE"],
            'ITEMPRESUPUESTARIO'       => $datos["ITEMPRESUPUESTARIO"],
            'MONTO'                 => $datos["MONTO"],
            'CLASIFICADORPRESUPUESTARIO'=> $datos["CLASIFICADORPRESUPUESTARIO"],
            'FUENTEFINANCIAMIENTO'     => $datos["FUENTEFINANCIAMIENTO"],
            'INSTITUCION'           => $datos["INSTITUCION"],
            'PREINVERSION'             => $datos["PREINVERSION"],
            'FICHA'              => $datos["FICHA"],
            'SOLICITADO'            => $datos["SOLICITADO"],
            'SOLICITADOPROXAGNO'       => $datos["SOLICITADOPROXAGNO"],
            'SOLICITADOPROXAGNOS'      => $datos["SOLICITADOPROXAGNOS"]
         );
         
         $array_detSoli[] = $dato;
         
      }
      /**********************************/
      /**********************************/
      
      
      /*************************************/
      /***OBTENER LISTA DE SUBTITULO ITEM***/
      /*************************************/
      $array_subtItem = array();
      foreach ($array_detSoli as $row) {
         
         $codigos = explode('.', $row['CLASIFICADORPRESUPUESTARIO']);
         
         $dato = array(
            'SUBTITULO' => $codigos[0],
            'ITEM'      => $codigos[1],
            'FUENTE'    => $row["FUENTEFINANCIAMIENTO"]
         );
         
         if(in_array($dato, $array_subtItem)) {
         }else{
            $array_subtItem[] = $dato;
         }
      }
      
      /**********************************/
      /***OBTENER LISTA DE CODIGOS BIP***/
      /**********************************/
      $arrayCodBip = array();
      foreach ($array_detSoli as $row) {
         
         $dato = array(
            'CODIGO_BIP'   => $row["CODIGO"],
            'DENOMINACION' => $row["NOMBRE"],
            "SUBTITEM"     => $row["CLASIFICADORPRESUPUESTARIO"],
            "FUENTE"    => $row["FUENTEFINANCIAMIENTO"],
            
            'SOLICITADO'            => $row["SOLICITADO"],
            'SOLICITADOPROXAGNO'       => $row["SOLICITADOPROXAGNO"],
            'SOLICITADOPROXAGNOS'      => $row["SOLICITADOPROXAGNOS"],
            
            "TIPOTABLA"    => "",
            
            "DATOS_ASIG"   => ""
         );
         
         $datosAsig = array(  'MONTO'        => $row["MONTO"],
                        'ASIGNACIONES' => $row["ITEMPRESUPUESTARIO"]);
         $query = "  SELECT *
                  FROM INVERSION_FINANCIAMIENTO 
                  WHERE    ANO                           = ".$ano." 
                        AND C_INSTITUCION                = ".$row["INSTITUCION"]."
                        AND C_PREINVERSION               = ".$row["PREINVERSION"]."
                        AND C_FICHA                   = ".$row["FICHA"]."
                        AND C_CLASIFICADOR_PRESUPUESTARIO   = '".$row["ITEMPRESUPUESTARIO"]."'";
         
         $rs = mysql_query($query, $conector);
         
         if(mysql_num_rows($rs)==0){
            $dato["TIPOTABLA"]= "I";
         }else{
            $dato["TIPOTABLA"]= "M";
         }
         
         /*SE AGREGA PARA ARREGLO GLOBAL */
         if(in_array($dato["CODIGO_BIP"], $arrayCodBip)) {
            $dato["DATOS_ASIG"]= $datosAsig;
         }else{
            $dato["DATOS_ASIG"]= $datosAsig;
            $arrayCodBip[] = $dato;
         }
      }
      
      
      /*ARMANDO ARRAY FINAL*/
      $arrayFinal = array();
      foreach ($arrayCodBip as $value) {
         $sub_item   =  explode('.',$value['SUBTITEM']);
         $sub     =  $sub_item[0];
         $item       =  $sub_item[1];
         $asignacion =  explode('.', $value['DATOS_ASIG']['ASIGNACIONES']);
         $asignacion =  $asignacion[2];
         
         $tipoTabla  =  $value['TIPOTABLA'];
         
         /*SETEANDO TOTAL DE TABLA PARA NUEVO SUBTITULO ITEM*/
         if(!isset($arrayFinal[$tipoTabla][$sub][$item])){$valVertical = 0;}
         
         /*INGRESANDO NOMBRE DEL SUBTITULO ITEM*/
         if (!isset($arrayFinal[$tipoTabla][$sub][$item]['NOMBRE'])) {
            $query = "SELECT N_CLASIFICADOR FROM TCLASIFICADOR_PRESUPUESTARIO WHERE C_CLASIFICADOR = '".$value['SUBTITEM']."'";     
            $rs = mysql_query($query, $conector);
            While($dato = mysql_fetch_array($rs)){$arrayFinal[$tipoTabla][$sub][$item]['NOMBRE']=$dato["N_CLASIFICADOR"];}
         }
         
         /*CREANDO CABEZERAS DE ASIGNACIONES*/
         if (!isset($arrayFinal[$tipoTabla][$sub][$item]['ASIG'][$asignacion])) {
            $arrayFinal[$tipoTabla][$sub][$item]['ASIG'][$asignacion]=$this->consulta_nombre_clasificador($value['DATOS_ASIG']['ASIGNACIONES']);
         }
         
         /*CARGANDO CODIGOS BIP A LA TABLA Y ACTUALIZANDO MONTO DE ASIGNACIONES*/
         if (isset($arrayFinal[$tipoTabla][$sub][$item]['INICIATIVAS'][$value['CODIGO_BIP']])) {
            /*CARGANDO MONTOS*/
            $arrayFinal[$tipoTabla][$sub][$item]['INICIATIVAS'][$value['CODIGO_BIP']]['DATOS_ASIG'][$asignacion] = $value['DATOS_ASIG']['MONTO'];
            $montoHori += $value['DATOS_ASIG']['MONTO'];
         }else{
            /*CREANDO CODIGO BIP*/
            $arrayFinal[$tipoTabla][$sub][$item]['INICIATIVAS'][$value['CODIGO_BIP']]['CODIGO_BIP']   = $value['CODIGO_BIP'];
            $arrayFinal[$tipoTabla][$sub][$item]['INICIATIVAS'][$value['CODIGO_BIP']]['DENOMINACION'] = $value['DENOMINACION'];
            $arrayFinal[$tipoTabla][$sub][$item]['INICIATIVAS'][$value['CODIGO_BIP']]['DATOS_ASIG'][$asignacion] = $value['DATOS_ASIG']['MONTO'];
            
            /*INICIANDO TOTAL HORIZONTAL*/
            $montoHori = $value['DATOS_ASIG']['MONTO'];
         }
         
         /*CARGANDO MONTO HORIZONTAL*/
         $arrayFinal[$tipoTabla][$sub][$item]['INICIATIVAS'][$value['CODIGO_BIP']]['MONTOHORI'] = $montoHori;
         
         /*CARGANDO VALORES FOOTER*/
         if(isset($arrayFinal[$tipoTabla][$sub][$item]['FOOTER'][$asignacion])){
            $valTemp = $arrayFinal[$tipoTabla][$sub][$item]['FOOTER'][$asignacion] + $value['DATOS_ASIG']['MONTO'];
            $valVertical +=$value['DATOS_ASIG']['MONTO'];
         }else{
            $valTemp = 0 + $value['DATOS_ASIG']['MONTO'];
            $valVertical +=$value['DATOS_ASIG']['MONTO'];
         }
         $arrayFinal[$tipoTabla][$sub][$item]['FOOTER'][$asignacion] = $valTemp;
         
         /*CARGANDO TOTAL FOOTER*/
         $arrayFinal[$tipoTabla][$sub][$item]['FOOTER']['TOTAL'] = $valVertical;
         
         /*DATOS X AGNO*/
         if(!isset($arrayFinal[$tipoTabla][$sub][$item]['DATOXAGNO'][$value['CODIGO_BIP']])){
            
            $arrayFinal[$tipoTabla][$sub][$item]['DATOXAGNO'][$value['CODIGO_BIP']]['Denominacion'] = $value['DENOMINACION'];
            $arrayFinal[$tipoTabla][$sub][$item]['DATOXAGNO'][$value['CODIGO_BIP']]['AgnoAnt'] = $value['SOLICITADO'];
            $arrayFinal[$tipoTabla][$sub][$item]['DATOXAGNO'][$value['CODIGO_BIP']]['AgnoAct'] = $value['SOLICITADOPROXAGNO'];
            $arrayFinal[$tipoTabla][$sub][$item]['DATOXAGNO'][$value['CODIGO_BIP']]['AgnoSig'] = $value['SOLICITADOPROXAGNOS'];
         
         }
      }
      natsort($arrayFinal, ksort($arrayFinal));
      
      return $arrayFinal;
   }
   
   /****************************************/
   /***Modificaciones Presupuestarias PDF***/
   /****************************************/
   
   function getModificacionesPresupuestariasPDF($ano){
   
   $conector = $this->conector;
      
      /******************************************************************/
      /***OBTENER LISTA DE MODIFICACIONES PRESUPUESTARIAS SUBTITULO 31***/
      /******************************************************************/
	  
	  
	$query  = "
		SELECT
			'LEY N° 20.713' AS DOCUMENTO,
			'' AS FECHA,
			'' AS NRO_REGISTRO_DIPRES,
			'' AS ESTADO_DECRETO,
			'Ley Presupuestos ".$ano."' AS REFERENCIA,
			PPTO_INICIAL_01.TMP_P_montoPreIni AS MONTO_ITEM_01,
			PPTO_INICIAL_02.TMP_P_montoPreIni AS MONTO_ITEM_02,
			PPTO_INICIAL_03.TMP_P_montoPreIni AS MONTO_ITEM_03,
			(
				PPTO_INICIAL_01.TMP_P_montoPreIni +
				PPTO_INICIAL_02.TMP_P_montoPreIni +
				PPTO_INICIAL_03.TMP_P_montoPreIni
			) AS MONTO_SUBTITULO_31

		FROM
			(
				SELECT
					TMP_Presupuesto.TMP_P_montoPreIni
				FROM
					TMP_Presupuesto
				WHERE
					TMP_Presupuesto.TMP_P_idFrom=(
						SELECT
							TMP_Presupuesto.TMP_P_id
						FROM
							TMP_Presupuesto
						WHERE
							TMP_Presupuesto.TMP_P_agno=".$ano." AND
							TMP_Presupuesto.TMP_P_numero=31
					) AND
					TMP_Presupuesto.TMP_P_numero='01'
			) AS PPTO_INICIAL_01,
			(
				SELECT
					TMP_Presupuesto.TMP_P_montoPreIni
				FROM
					TMP_Presupuesto
				WHERE
					TMP_Presupuesto.TMP_P_idFrom=(
						SELECT
							TMP_Presupuesto.TMP_P_id
						FROM
							TMP_Presupuesto
						WHERE
							TMP_Presupuesto.TMP_P_agno=".$ano." AND
							TMP_Presupuesto.TMP_P_numero=31
					) AND
					TMP_Presupuesto.TMP_P_numero='02'
			) AS PPTO_INICIAL_02,
			(
				SELECT
					TMP_Presupuesto.TMP_P_montoPreIni
				FROM
					TMP_Presupuesto
				WHERE
					TMP_Presupuesto.TMP_P_idFrom=(
						SELECT
							TMP_Presupuesto.TMP_P_id
						FROM
							TMP_Presupuesto
						WHERE
							TMP_Presupuesto.TMP_P_agno=".$ano." AND
							TMP_Presupuesto.TMP_P_numero=31
					) AND
					TMP_Presupuesto.TMP_P_numero='03'
			) AS PPTO_INICIAL_03

		UNION ALL

		SELECT
			(CASE TMP_Solicitud.TMP_Sol_tipoAprob
				WHEN 'Documento' THEN CONCAT('D(H) # ', TMP_Solicitud.TMP_Sol_numDecreto)
				WHEN 'Resolucion' THEN CONCAT('RES # ', TMP_Solicitud.TMP_Sol_numDecreto)
				ELSE ''
			END) AS DOCUMENTO,
			DATE_FORMAT(TMP_Solicitud.TMP_Sol_fechaDecreto, '%d.%m.%Y') AS FECHA,
			'' AS NRO_REGISTRO_DIPRES,
			TMP_Solicitud.TMP_Sol_estado AS ESTADO_DECRETO,
			TMP_Solicitud.TMP_Sol_obs AS REFERENCIA,
			(CASE
				WHEN (DETALLE_ITEM_01.TMP_SD_montoAume-DETALLE_ITEM_01.TMP_SD_montoDism) IS NULL THEN 0
				ELSE (DETALLE_ITEM_01.TMP_SD_montoAume-DETALLE_ITEM_01.TMP_SD_montoDism)
			END) AS MONTO_ITEM_01,
			(CASE
				WHEN (DETALLE_ITEM_02.TMP_SD_montoAume-DETALLE_ITEM_02.TMP_SD_montoDism) IS NULL THEN 0
				ELSE (DETALLE_ITEM_02.TMP_SD_montoAume-DETALLE_ITEM_02.TMP_SD_montoDism)
			END) AS MONTO_ITEM_02,
			(CASE 
				WHEN (DETALLE_ITEM_03.TMP_SD_montoAume-DETALLE_ITEM_03.TMP_SD_montoDism) IS NULL THEN 0
				ELSE (DETALLE_ITEM_03.TMP_SD_montoAume-DETALLE_ITEM_03.TMP_SD_montoDism) 
			END) AS MONTO_ITEM_03,
			(
				(CASE
					WHEN (DETALLE_ITEM_01.TMP_SD_montoAume-DETALLE_ITEM_01.TMP_SD_montoDism) IS NULL THEN 0
					ELSE (DETALLE_ITEM_01.TMP_SD_montoAume-DETALLE_ITEM_01.TMP_SD_montoDism)
				END)
				+
				(CASE
					WHEN (DETALLE_ITEM_02.TMP_SD_montoAume-DETALLE_ITEM_02.TMP_SD_montoDism) IS NULL THEN 0
					ELSE (DETALLE_ITEM_02.TMP_SD_montoAume-DETALLE_ITEM_02.TMP_SD_montoDism)
				END)
				+
				(CASE 
					WHEN (DETALLE_ITEM_03.TMP_SD_montoAume-DETALLE_ITEM_03.TMP_SD_montoDism) IS NULL THEN 0
					ELSE (DETALLE_ITEM_03.TMP_SD_montoAume-DETALLE_ITEM_03.TMP_SD_montoDism) 
				END)
			) AS MONTO_SUBTITULO_31
		FROM
			TMP_Solicitud
			LEFT JOIN TMP_SolicitudDetalle AS DETALLE_ITEM_01 ON
				TMP_Solicitud.TMP_Sol_id=DETALLE_ITEM_01.TMP_Sol_id AND
				DETALLE_ITEM_01.TMP_SD_subtitulo='31' AND
				DETALLE_ITEM_01.TMP_SD_ITEM='01'
			LEFT JOIN TMP_SolicitudDetalle AS DETALLE_ITEM_02 ON
				TMP_Solicitud.TMP_Sol_id=DETALLE_ITEM_02.TMP_Sol_id AND
				DETALLE_ITEM_02.TMP_SD_subtitulo='31' AND
				DETALLE_ITEM_02.TMP_SD_ITEM='02'
			LEFT JOIN TMP_SolicitudDetalle AS DETALLE_ITEM_03 ON
				TMP_Solicitud.TMP_Sol_id=DETALLE_ITEM_03.TMP_Sol_id AND
				DETALLE_ITEM_03.TMP_SD_subtitulo='31' AND
				DETALLE_ITEM_03.TMP_SD_ITEM='03'
		WHERE
			YEAR(TMP_Solicitud.TMP_Sol_fecha)=".$ano." AND
			TMP_Solicitud.TMP_Sol_estado='Con Toma Razón'
		HAVING
			MONTO_SUBTITULO_31<>0";
	  
	  
      
      
      $datosModificaciones = mysql_query($query, $conector);
      
      $array_Modificaciones = array();
      while($datos = mysql_fetch_array($datosModificaciones)){
         
         $dato = array(
            'DOCUMENTO'				=> $datos["DOCUMENTO"],
            'FECHA'                	=> $datos["FECHA"],
            'NRO_REGISTRO_DIPRES'   => $datos["NRO_REGISTRO_DIPRES"],
            'ESTADO_DECRETO'        => $datos["ESTADO_DECRETO"],
            'REFERENCIA'			=> $datos["REFERENCIA"],
            'MONTO_ITEM_01'			=> $datos["MONTO_ITEM_01"],
            'MONTO_ITEM_02'			=> $datos["MONTO_ITEM_02"],
            'MONTO_ITEM_03'			=> $datos["MONTO_ITEM_03"],
            'MONTO_SUBTITULO_31'	=> $datos["MONTO_SUBTITULO_31"]
         );
         
         $array_Modificaciones[] = $dato;
         
      }
      /**********************************/
      /**********************************/
   
	  return $array_Modificaciones;
   
   
   }
   
   /***************************************************/
   /***Modificaciones Presupuestarias en trámite PDF***/
   /***************************************************/
   
   function getModificacionesPresupuestariasEnTramitePDF($ano){
   
   $conector = $this->conector;
      
      /*****************************************************************************/
      /***OBTENER LISTA DE MODIFICACIONES PRESUPUESTARIAS SUBTITULO 31 EN TRAMITE***/
      /*****************************************************************************/
	  
	  
	$query  = "SELECT
	(CASE TMP_Solicitud.TMP_Sol_tipoAprob
    	WHEN 'Documento' THEN CONCAT('D(H) # ', TMP_Solicitud.TMP_Sol_numDecreto)
        WHEN 'Resolucion' THEN CONCAT('RES # ', TMP_Solicitud.TMP_Sol_numDecreto)
        ELSE ''
    END) AS DOCUMENTO,
    DATE_FORMAT(TMP_Solicitud.TMP_Sol_fechaDecreto, '%d.%m.%Y') AS FECHA,
    '' AS NRO_REGISTRO_DIPRES,
    TMP_Solicitud.TMP_Sol_estado AS ESTADO_DECRETO,
    TMP_Solicitud.TMP_Sol_obs AS REFERENCIA,
    (CASE
    	WHEN (DETALLE_ITEM_01.TMP_SD_montoAume-DETALLE_ITEM_01.TMP_SD_montoDism) IS NULL THEN 0
        ELSE (DETALLE_ITEM_01.TMP_SD_montoAume-DETALLE_ITEM_01.TMP_SD_montoDism)
    END) AS MONTO_ITEM_01,
    (CASE
    	WHEN (DETALLE_ITEM_02.TMP_SD_montoAume-DETALLE_ITEM_02.TMP_SD_montoDism) IS NULL THEN 0
        ELSE (DETALLE_ITEM_02.TMP_SD_montoAume-DETALLE_ITEM_02.TMP_SD_montoDism)
    END) AS MONTO_ITEM_02,
    (CASE 
    	WHEN (DETALLE_ITEM_03.TMP_SD_montoAume-DETALLE_ITEM_03.TMP_SD_montoDism) IS NULL THEN 0
        ELSE (DETALLE_ITEM_03.TMP_SD_montoAume-DETALLE_ITEM_03.TMP_SD_montoDism) 
    END) AS MONTO_ITEM_03,
    (
    	(CASE
    		WHEN (DETALLE_ITEM_01.TMP_SD_montoAume-DETALLE_ITEM_01.TMP_SD_montoDism) IS NULL THEN 0
        	ELSE (DETALLE_ITEM_01.TMP_SD_montoAume-DETALLE_ITEM_01.TMP_SD_montoDism)
    	END)
        +
    	(CASE
    		WHEN (DETALLE_ITEM_02.TMP_SD_montoAume-DETALLE_ITEM_02.TMP_SD_montoDism) IS NULL THEN 0
        	ELSE (DETALLE_ITEM_02.TMP_SD_montoAume-DETALLE_ITEM_02.TMP_SD_montoDism)
    	END)
        +
    	(CASE 
    		WHEN (DETALLE_ITEM_03.TMP_SD_montoAume-DETALLE_ITEM_03.TMP_SD_montoDism) IS NULL THEN 0
        	ELSE (DETALLE_ITEM_03.TMP_SD_montoAume-DETALLE_ITEM_03.TMP_SD_montoDism) 
    	END)
    ) AS MONTO_SUBTITULO_31
FROM
	TMP_Solicitud
    LEFT JOIN TMP_SolicitudDetalle AS DETALLE_ITEM_01 ON
	   	TMP_Solicitud.TMP_Sol_id=DETALLE_ITEM_01.TMP_Sol_id AND
    	DETALLE_ITEM_01.TMP_SD_subtitulo='31' AND
    	DETALLE_ITEM_01.TMP_SD_ITEM='01'
    LEFT JOIN TMP_SolicitudDetalle AS DETALLE_ITEM_02 ON
	   	TMP_Solicitud.TMP_Sol_id=DETALLE_ITEM_02.TMP_Sol_id AND
    	DETALLE_ITEM_02.TMP_SD_subtitulo='31' AND
    	DETALLE_ITEM_02.TMP_SD_ITEM='02'
    LEFT JOIN TMP_SolicitudDetalle AS DETALLE_ITEM_03 ON
	   	TMP_Solicitud.TMP_Sol_id=DETALLE_ITEM_03.TMP_Sol_id AND
    	DETALLE_ITEM_03.TMP_SD_subtitulo='31' AND
    	DETALLE_ITEM_03.TMP_SD_ITEM='03'
WHERE
	YEAR(TMP_Solicitud.TMP_Sol_fecha)=".$ano." AND
    TMP_Solicitud.TMP_Sol_estado<>'Con Toma Razón' AND
    TMP_Solicitud.TMP_Sol_estado<>'Sin Efecto'
HAVING
	MONTO_SUBTITULO_31<>0";
	  
	  
      
      
      $datosModificacioneseEnTramite = mysql_query($query, $conector);
      
      $array_Modificaciones_En_Tramite = array();
      while($datos = mysql_fetch_array($datosModificacioneseEnTramite)){
         
         $dato = array(
            'DOCUMENTO'				=> $datos["DOCUMENTO"],
            'FECHA'                	=> $datos["FECHA"],
            'NRO_REGISTRO_DIPRES'   => $datos["NRO_REGISTRO_DIPRES"],
            'ESTADO_DECRETO'        => $datos["ESTADO_DECRETO"],
            'REFERENCIA'			=> $datos["REFERENCIA"],
            'MONTO_ITEM_01'			=> $datos["MONTO_ITEM_01"],
            'MONTO_ITEM_02'			=> $datos["MONTO_ITEM_02"],
            'MONTO_ITEM_03'			=> $datos["MONTO_ITEM_03"],
            'MONTO_SUBTITULO_31'	=> $datos["MONTO_SUBTITULO_31"]
         );
         
         $array_Modificaciones_En_Tramite[] = $dato;
         
      }
      /**********************************/
      /**********************************/
   
	  return $array_Modificaciones_En_Tramite;
   
   
   }
   
   /******************************/
   /***Marco Presupuestario PDF***/
   /******************************/
   
   function getMarcoPresupuestarioPDF($ano){
   
   $conector = $this->conector;
      
      /***************************************************************/
      /***OBTENER MARCO PRESUPUESTARIO PARA IMPRESION DE ASIGNACION***/
      /***************************************************************/
	  
	  
	$query  = "
		SELECT
			TMP_Presupuesto.TMP_P_numero AS CLASIFICADOR_PRESUPUESTARIO,
			TMP_Presupuesto.TMP_P_tipo AS TIPO_CLASIFICADOR,
			TMP_Presupuesto.TMP_P_numero AS SUBTITULO,
			'' AS ITEM,
			'' AS ASIGNACION,
			TMP_Presupuesto.TMP_P_nombre AS GLOSA,
			(CASE
				WHEN TMP_Presupuesto.TMP_P_montoPreVige IS NULL THEN TMP_Presupuesto.TMP_P_montoPreIni
				ELSE TMP_Presupuesto.TMP_P_montoPreVige
			END) AS MONTO
		FROM
			TMP_Presupuesto
		WHERE
			TMP_Presupuesto.TMP_P_agno=".$ano." AND
			TMP_Presupuesto.TMP_P_tipo=1

		UNION ALL

		SELECT
			CONCAT(PPTO_PADRE.TMP_P_numero, '.',TMP_Presupuesto.TMP_P_numero)  AS CLASIFICADOR_PRESUPUESTARIO,
			TMP_Presupuesto.TMP_P_tipo AS TIPO_CLASIFICADOR,
			'' AS SUBTITULO,
			TMP_Presupuesto.TMP_P_numero AS ITEM,
			'' AS ASIGNACION,
			TMP_Presupuesto.TMP_P_nombre AS GLOSA,
			(CASE
				WHEN TMP_Presupuesto.TMP_P_montoPreVige IS NULL THEN TMP_Presupuesto.TMP_P_montoPreIni
				ELSE TMP_Presupuesto.TMP_P_montoPreVige
			END) AS MONTO
		FROM
			TMP_Presupuesto
			LEFT JOIN TMP_Presupuesto AS PPTO_PADRE ON
				TMP_Presupuesto.TMP_P_idFrom=PPTO_PADRE.TMP_P_id
		WHERE
			TMP_Presupuesto.TMP_P_agno=".$ano." AND
			TMP_Presupuesto.TMP_P_tipo=2

		UNION ALL

		SELECT
			CONCAT(PPTO_PADRE_DE_PADRE.TMP_P_numero, '.',PPTO_PADRE.TMP_P_numero, '.',TMP_Presupuesto.TMP_P_numero)  AS CLASIFICADOR_PRESUPUESTARIO,
			TMP_Presupuesto.TMP_P_tipo AS TIPO_CLASIFICADOR,
			'' AS SUBTITULO,
			'' AS ITEM,
			TMP_Presupuesto.TMP_P_numero AS ASIGNACION,
			TMP_Presupuesto.TMP_P_nombre AS GLOSA,
			(CASE
				WHEN TMP_Presupuesto.TMP_P_montoPreVige IS NULL THEN TMP_Presupuesto.TMP_P_montoPreIni
				ELSE TMP_Presupuesto.TMP_P_montoPreVige
			END) AS MONTO
		FROM
			TMP_Presupuesto
			LEFT JOIN TMP_Presupuesto AS PPTO_PADRE ON
				TMP_Presupuesto.TMP_P_idFrom=PPTO_PADRE.TMP_P_id
			LEFT JOIN TMP_Presupuesto AS PPTO_PADRE_DE_PADRE ON
				PPTO_PADRE.TMP_P_idFrom=PPTO_PADRE_DE_PADRE.TMP_P_id
		WHERE
			TMP_Presupuesto.TMP_P_agno=".$ano." AND
			TMP_Presupuesto.TMP_P_tipo=3

		ORDER BY
			CLASIFICADOR_PRESUPUESTARIO";
	  
	
      $datosMarco = mysql_query($query, $conector);
      
      $array_Marco = array();
      while($datos = mysql_fetch_array($datosMarco)){
         
         $dato = array(
            'CLASIFICADOR_PRESUPUESTARIO'	=> $datos["CLASIFICADOR_PRESUPUESTARIO"],
            'TIPO_CLASIFICADOR'             => $datos["TIPO_CLASIFICADOR"],
            'SUBTITULO'   					=> $datos["SUBTITULO"],
            'ITEM'        					=> $datos["ITEM"],
            'ASIGNACION'					=> $datos["ASIGNACION"],
            'GLOSA'							=> $datos["GLOSA"],
            'MONTO'							=> $datos["MONTO"]
         );

         $array_Marco[] = $dato;
         
      }
      /**********************************/
      /**********************************/
   
	  return $array_Marco;

   }
   
   /*********************************************************/
   /***Identificaciones Presupuestarias (asignaciones) PDF***/
   /*********************************************************/
   
   function getIdentificacionesPresupuestariasPDF($ano){
   
   $conector = $this->conector;
      
      /***************************************************************************/
      /***OBTENER IDENTIFICACIONES PRESUPUESTARIAS PARA IMPRESION DE ASIGNACION***/
      /***************************************************************************/
	  
	  
	$query  = "
		SELECT
			(CASE ASIGNACION.C_TIPO_SOLICITUD
				WHEN 1 THEN 'Decreto'
				WHEN 2 THEN 'Resolución Afecta'
				WHEN 3 THEN 'Resolución Afecta FIC'
				ELSE ''
			END) AS TIPO_RESOLUCION,
			ASIGNACION_OFICINAPARTE.NUM_RESOLUCION,
			DATE_FORMAT(ASIGNACION_OFICINAPARTE.FECHA_RESOLUCION, '%d.%m.%Y') AS FECHA_RESOLUCION,
			ASIGNACION_ESTADO.N_ESTADO AS ESTADO_ASIGNACION,
			(CASE
				WHEN INVERSION_ITEM_01.VARIACION_ASIGNACION IS NULL THEN 0
				ELSE INVERSION_ITEM_01.VARIACION_ASIGNACION/1000 
			END) AS VARIACION_ITEM_01,
			(CASE
				WHEN INVERSION_ITEM_02.VARIACION_ASIGNACION IS NULL THEN 0
				ELSE INVERSION_ITEM_02.VARIACION_ASIGNACION/1000 
			END) AS VARIACION_ITEM_02,
			(CASE
				WHEN INVERSION_ITEM_03.VARIACION_ASIGNACION IS NULL THEN 0
				ELSE INVERSION_ITEM_03.VARIACION_ASIGNACION/1000 
			END) AS VARIACION_ITEM_03,
			(
				(CASE
					WHEN INVERSION_ITEM_01.VARIACION_ASIGNACION IS NULL THEN 0
					ELSE INVERSION_ITEM_01.VARIACION_ASIGNACION/1000 
				END) +
				(CASE
					WHEN INVERSION_ITEM_02.VARIACION_ASIGNACION IS NULL THEN 0
					ELSE INVERSION_ITEM_02.VARIACION_ASIGNACION/1000 
				END) +
				(CASE
					WHEN INVERSION_ITEM_03.VARIACION_ASIGNACION IS NULL THEN 0
					ELSE INVERSION_ITEM_03.VARIACION_ASIGNACION/1000 
				END)
			) AS VARIACION_SUBT_31
		FROM
			ASIGNACION
			LEFT JOIN ASIGNACION_OFICINAPARTE ON
				ASIGNACION.ANO=ASIGNACION_OFICINAPARTE.ANO AND
				ASIGNACION.C_ASIGNACION=ASIGNACION_OFICINAPARTE.C_ASIGNACION
			LEFT JOIN ASIGNACION_ESTADO ON
				ASIGNACION.C_ESTADO=ASIGNACION_ESTADO.C_ESTADO
			LEFT JOIN
			(
				SELECT
					ASIGNACION_INVERSION.C_ASIGNACION,
					ASIGNACION_INVERSION.ANO,
					(SUM(ASIGNACION_INVERSION.MONTO)-SUM(ASIGNACION_INVERSION.MONTO_DISMINUCION)) AS VARIACION_ASIGNACION
				FROM
					ASIGNACION_INVERSION
				WHERE
					ASIGNACION_INVERSION.ANO=".$ano." AND
					ASIGNACION_INVERSION.C_ITEM_PRESUPUESTARIO LIKE '31.01%'
				GROUP BY
					ASIGNACION_INVERSION.C_ASIGNACION,
					ASIGNACION_INVERSION.ANO
			) AS INVERSION_ITEM_01 ON
				ASIGNACION.ANO=INVERSION_ITEM_01.ANO AND
				ASIGNACION.C_ASIGNACION=INVERSION_ITEM_01.C_ASIGNACION
			LEFT JOIN
			(
				SELECT
					ASIGNACION_INVERSION.C_ASIGNACION,
					ASIGNACION_INVERSION.ANO,
					(SUM(ASIGNACION_INVERSION.MONTO)-SUM(ASIGNACION_INVERSION.MONTO_DISMINUCION)) AS VARIACION_ASIGNACION
				FROM
					ASIGNACION_INVERSION
				WHERE
					ASIGNACION_INVERSION.ANO=".$ano." AND
					ASIGNACION_INVERSION.C_ITEM_PRESUPUESTARIO LIKE '31.02%'
				GROUP BY
					ASIGNACION_INVERSION.C_ASIGNACION,
					ASIGNACION_INVERSION.ANO
			) AS INVERSION_ITEM_02 ON
				ASIGNACION.ANO=INVERSION_ITEM_02.ANO AND
				ASIGNACION.C_ASIGNACION=INVERSION_ITEM_02.C_ASIGNACION
			LEFT JOIN
			(
				SELECT
					ASIGNACION_INVERSION.C_ASIGNACION,
					ASIGNACION_INVERSION.ANO,
					(SUM(ASIGNACION_INVERSION.MONTO)-SUM(ASIGNACION_INVERSION.MONTO_DISMINUCION)) AS VARIACION_ASIGNACION
				FROM
					ASIGNACION_INVERSION
				WHERE
					ASIGNACION_INVERSION.ANO=".$ano." AND
					ASIGNACION_INVERSION.C_ITEM_PRESUPUESTARIO LIKE '31.03%'
				GROUP BY
					ASIGNACION_INVERSION.C_ASIGNACION,
					ASIGNACION_INVERSION.ANO
			) AS INVERSION_ITEM_03 ON
				ASIGNACION.ANO=INVERSION_ITEM_03.ANO AND
				ASIGNACION.C_ASIGNACION=INVERSION_ITEM_03.C_ASIGNACION
			
		WHERE
			ASIGNACION.ANO=".$ano." AND
			ASIGNACION.C_ESTADO<>9";
	  
	
      $datosIdentificaciones = mysql_query($query, $conector);
      
      $array_Identificaciones = array();
      while($datos = mysql_fetch_array($datosIdentificaciones)){
         
         $dato = array(
		
			'TIPO_RESOLUCION' 	=> $datos["TIPO_RESOLUCION"],
			'NUM_RESOLUCION' 	=> $datos["NUM_RESOLUCION"],
			'FECHA_RESOLUCION' 	=> $datos["FECHA_RESOLUCION"],
			'ESTADO_ASIGNACION' => $datos["ESTADO_ASIGNACION"],
			'VARIACION_ITEM_01' => $datos["VARIACION_ITEM_01"],
			'VARIACION_ITEM_02' => $datos["VARIACION_ITEM_02"],
			'VARIACION_ITEM_03' => $datos["VARIACION_ITEM_03"],
			'VARIACION_SUBT_31' => $datos["VARIACION_SUBT_31"]
         );

         $array_Identificaciones[] = $dato;
         
      }
      /**********************************/
      /**********************************/
   
	  return $array_Identificaciones;

   }
   
   
   function getAsignacionesResExentaPDF($ano,$codigo_asignacion) {
   
   $conector = $this->conector;
      
      /***********************************************************/
      /***OBTENER LISTA DE ASIGNACIONES DE LA RESOLUCION EXENTA***/
      /***********************************************************/
	  
	  
	$query  = "
		SELECT DISTINCT
			ASIGNACION_INVERSION.C_ITEM_PRESUPUESTARIO,
			LEFT(ASIGNACION_INVERSION.C_ITEM_PRESUPUESTARIO,2) AS SUBTITULO,
			CLASIF_SUBTITULO.N_CLASIFICADOR AS NOMBRE_SUBTITULO,
			MID(ASIGNACION_INVERSION.C_ITEM_PRESUPUESTARIO,4,2) AS ITEM,
			CLASIF_ITEM.N_CLASIFICADOR AS NOMBRE_ITEM,
			MID(ASIGNACION_INVERSION.C_ITEM_PRESUPUESTARIO,7,3) AS ASIGNACION,
			CLASIF_ASIGNACION.N_CLASIFICADOR AS NOMBRE_ASIGNACION
			
		FROM
			ASIGNACION_INVERSION
			LEFT JOIN TCLASIFICADOR_PRESUPUESTARIO AS CLASIF_SUBTITULO ON
				ASIGNACION_INVERSION.ANO=CLASIF_SUBTITULO.ANIO AND
				LEFT(ASIGNACION_INVERSION.C_ITEM_PRESUPUESTARIO,2)=CLASIF_SUBTITULO.C_CLASIFICADOR AND
				CLASIF_SUBTITULO.INSTITUCION IN (0,1)
			LEFT JOIN TCLASIFICADOR_PRESUPUESTARIO AS CLASIF_ITEM ON
				ASIGNACION_INVERSION.ANO=CLASIF_ITEM.ANIO AND
				LEFT(ASIGNACION_INVERSION.C_ITEM_PRESUPUESTARIO,5)=CLASIF_ITEM.C_CLASIFICADOR AND
				CLASIF_ITEM.INSTITUCION IN (0,1)
			LEFT JOIN TCLASIFICADOR_PRESUPUESTARIO AS CLASIF_ASIGNACION ON
				ASIGNACION_INVERSION.ANO=CLASIF_ASIGNACION.ANIO AND
				ASIGNACION_INVERSION.C_ITEM_PRESUPUESTARIO=CLASIF_ASIGNACION.C_CLASIFICADOR AND
				CLASIF_ASIGNACION.INSTITUCION IN (0,1)
		WHERE
			ASIGNACION_INVERSION.ANO=".$ano." AND
			ASIGNACION_INVERSION.C_ASIGNACION=".$codigo_asignacion;
	  
      
      $datosAsignaciones = mysql_query($query, $conector);
      
      $array_Asignaciones = array();
      while($datos = mysql_fetch_array($datosAsignaciones)){
         
         $dato = array(
            'C_ITEM_PRESUPUESTARIO'	=> $datos["C_ITEM_PRESUPUESTARIO"],
            'SUBTITULO'				=> $datos["SUBTITULO"],
            'NOMBRE_SUBTITULO'		=> $datos["NOMBRE_SUBTITULO"],
            'ITEM'        			=> $datos["ITEM"],
            'NOMBRE_ITEM'			=> $datos["NOMBRE_ITEM"],
            'ASIGNACION'			=> $datos["ASIGNACION"],
			'NOMBRE_ASIGNACION'		=> $datos["NOMBRE_ASIGNACION"]
			
         );
         
         $array_Asignaciones[] = $dato;
         
      }
      /**********************************/
      /**********************************/
   
	  return $array_Asignaciones;
   
   }
   
   
   
   function getSubasignacionesPDF($ano,$codigo_asignacion, $clasificador_asignacion){
   
   $conector = $this->conector;
      
      /**************************************/
      /***OBTENER LISTA DE SUBASIGNACIONES***/
      /**************************************/
	  
	  
	$query  = "
		SELECT
			INVERSION.CODIGO,
			ASIGNACION_INVERSION.SUBASIGNACION,
			INVERSION.NOMBRE,
			UBICACION_TERRITORIAL_INICIATIVA.UBICACION_ESPECIFICA_NOMBRE AS COMUNA,
			ASIGNACION_INVERSION.MONTO
		FROM
			ASIGNACION_INVERSION
			LEFT JOIN INVERSION ON
				ASIGNACION_INVERSION.ANO=INVERSION.ANO AND
				ASIGNACION_INVERSION.C_INSTITUCION=INVERSION.C_INSTITUCION AND
				ASIGNACION_INVERSION.C_PREINVERSION=INVERSION.C_PREINVERSION AND
				ASIGNACION_INVERSION.C_FICHA=INVERSION.C_FICHA
			LEFT JOIN UBICACION_TERRITORIAL_INICIATIVA ON
				INVERSION.ANO=UBICACION_TERRITORIAL_INICIATIVA.ANO AND
				INVERSION.C_INSTITUCION=UBICACION_TERRITORIAL_INICIATIVA.C_INSTITUCION AND
				INVERSION.C_PREINVERSION=UBICACION_TERRITORIAL_INICIATIVA.C_INICIATIVA AND
				INVERSION.C_FICHA=UBICACION_TERRITORIAL_INICIATIVA.C_FICHA
		WHERE
			ASIGNACION_INVERSION.ANO=".$ano." AND
			ASIGNACION_INVERSION.C_ASIGNACION=".$codigo_asignacion." AND
			ASIGNACION_INVERSION.C_ITEM_PRESUPUESTARIO='".$clasificador_asignacion."'";
	  
      
      $datosSubasignaciones = mysql_query($query, $conector);
      
      $array_Subasignaciones = array();
      while($datos = mysql_fetch_array($datosSubasignaciones)){
         
         $dato = array(
            'CODIGO'				=> $datos["CODIGO"],
            'SUBASIGNACION'			=> $datos["SUBASIGNACION"],
            'NOMBRE'			 	=> $datos["NOMBRE"],
            'COMUNA'        		=> $datos["COMUNA"],
            'MONTO'					=> $datos["MONTO"],
            'C_ITEM_PRESUPUESTARIO'	=> $datos["C_ITEM_PRESUPUESTARIO"]
         );
         
         $array_Subasignaciones[] = $dato;
         
      }
      /**********************************/
      /**********************************/
   
	  return $array_Subasignaciones;
   
   
   }
   
   
   
   function consulta_nombre_clasificador($clasificador){
      $query2  = "SELECT N_CLASIFICADOR FROM TCLASIFICADOR_PRESUPUESTARIO
         WHERE C_CLASIFICADOR = '".$clasificador."'
         AND ANIO = 2012";   

      $dataset = mysql_query($query2, $this->conector);
      $nombre = mysql_fetch_assoc($dataset);
      
      return $nombre['N_CLASIFICADOR'];
   }
   /**
    * codigo BIP
    * denominacion
    * 
    * @return array $resp: Array con los resultados de la consulta
    */
   function get_asignaciones($id_asignacion){
      $resp = array();
      $query  = " SELECT 
                     F.ANO,
                     F.C_INSTITUCION,
                     F.C_PREINVERSION,
                     F.C_FUENTE_FINANCIAMIENTO,
                     F.C_CLASIFICADOR_PRESUPUESTARIO,
                     F.COSTO_EBI,
                     F.COSTO_CORE,
                     F.COSTO_TOTAL,
                     F.GASTADO_ANOS_ANTERIORES,
                     F.SOLICITADO,
                     F.SALDO_PROXIMO_ANO,
                     F.SALDO_PROXIMOS_ANOS,
                     F.ASIGNADO,
                     F.ASIGNACION_DISPONIBLE,
                     F.SALDO_POR_ASIGNAR,
                     F.TOTAL_PROGRAMADO,
                     F.CONTRATADO,
                     F.PAGADO,
                     F.SALDO,
                     F.ARRASTRE,
                     F.FECHA_REGISTRO,
                     I.NOMBRE, 
                     I.CODIGO
                  FROM ASIGNACION_INVERSION A
      LEFT JOIN INVERSION I ON( A.C_PREINVERSION = I.C_PREINVERSION AND
                              A.ANO = I.ANO AND
                              A.C_INSTITUCION = I.C_INSTITUCION AND 
                              A.C_FICHA = I.C_FICHA)
      LEFT JOIN INVERSION_FINANCIAMIENTO F ON( I.C_PREINVERSION = F.C_PREINVERSION AND 
                                             I.ANO = F.ANO AND 
                                             I.C_INSTITUCION = F.C_INSTITUCION AND
                                             I.C_FICHA = F.C_FICHA AND 
                                             A.C_ITEM_PRESUPUESTARIO = F.C_CLASIFICADOR_PRESUPUESTARIO
                                             )
      WHERE C_ASIGNACION = ".$id_asignacion ."
      ORDER BY I.CODIGO  ";

      $asignaciones = mysql_query($query, $this->conector);

      $codigo = "";
      $dato_asig = array();
      while($asignacion = mysql_fetch_assoc($asignaciones)){
         if ($codigo != $asignacion['CODIGO']) {
            $codigo = $asignacion['CODIGO'];
            $dato_asig = "";
         } 
         $cod_item = explode('.', $asignacion['C_CLASIFICADOR_PRESUPUESTARIO']);
         $dato = array(
            'CODIGO' => $asignacion['C_CLASIFICADOR_PRESUPUESTARIO'],
            'SUBTITULO' => $cod_item[0],
            'ITEM' => $cod_item[1],
            'ASIGNACION' => $cod_item[2],
            'MONTO' => (int)($asignacion['SOLICITADO'] / 1000 ),
            );
         $dato_asig[] = $dato;
         $resp[$codigo] = array('DATOS' => $asignacion, 'ASIGNACIONES' => $dato_asig);
      }
      foreach ($resp as $j => $iniciativa) {

         foreach ($iniciativa['ASIGNACIONES'] as $key => $asignacion) {
            $resp[$j]['DATOS']['ASIGNACIONES'][$asignacion['SUBTITULO']][$asignacion['ITEM']][$asignacion['ASIGNACION']] = $asignacion['MONTO'];
            // $resp[$j]['DATOS']['ASIG_'.$asignacion['ASIGNACION']] = $asignacion['MONTO'];
         }
      }
      return $resp;

   }

   function get_nroregistros(){ return $this->nroregistros;}  
   
   function get_error()       { return $this->mensaje_error;}
      
}
//********************************************
// SOLICITADO ASIGNACION
//********************************************   
Class Asignacion_Inversion {
   var $conector;
   var $mensaje_error;
   var $nroregistros;
   var $ano;  
   var $c_asignacion;
   var $c_institucion;
   var $c_preinversion;
   var $c_ficha;
   var $c_item_presupuestario='';
   var $monto;
   var $monto_disminucion;
   var $vector="";
   var $fuentes_financiamiento = "";
   var $subasignacion;
   
   function Asignacion_Inversion($conector) { 
      $this->conector =$conector;
      $this->mensaje_error='';
      $this->estado=0;
      $this->nroregistros=0;
   }
    
   function actualizar_asignacion() {
      /*$fuente = trim($this->fuentes_financiamiento);
      $query2 = " SELECT C_FUENTE_FINANCIAMIENTO 
         FROM FUENTE_FINANCIAMIENTO
         WHERE SIGLA_FUENTE_FINANCIAMIENTO LIKE '%". $fuente ."%'";
      $dataset2 = mysql_query($query2, $this->conector);
      $cod_financiamiento = mysql_fetch_assoc($dataset2);*/


      $query  = "REPLACE INTO ASIGNACION_INVERSION(
         ANO, 
         C_ASIGNACION, 
         C_INSTITUCION, 
         C_PREINVERSION, 
         C_FICHA, 
         C_ITEM_PRESUPUESTARIO, 
         MONTO,
		 MONTO_DISMINUCION,
         C_FUENTE_FINANCIAMIENTO,
		 SUBASIGNACION
         ) VALUES('";
      //$query .= $this->ano."','".$this->c_asignacion."','".$this->c_institucion."','".$this->c_preinversion."','".$this->c_ficha."','".$this->c_item_presupuestario."','".$this->monto."','".$this->monto_disminucion."','".$cod_financiamiento['C_FUENTE_FINANCIAMIENTO']."','".$this->subasignacion."');";
      $query .= $this->ano."','".$this->c_asignacion."','".$this->c_institucion."','".$this->c_preinversion."','".$this->c_ficha."','".$this->c_item_presupuestario."','".$this->monto."','".$this->monto_disminucion."','".$this->fuentes_financiamiento."','".$this->subasignacion."');";
	 
      $dataset  = mysql_query($query, $this->conector);
      if (mysql_errno($this->conector) != 0) { 
         $this->mensaje_error="No se ha podido Actualizar la Base de Datos, mensaje de error de Mysql es: " . mysql_error($this->conector).$query; 
      } else {$this->mensaje_error='';}   
      return $this->mensaje_error;
   }

   function get_nroregistros(){ return $this->nroregistros;}  
   
   function get_error()       { return $this->mensaje_error;}
   
   function consulta_subasignacion() {
   
   
	   $query  = "SELECT
			ASIGNACION_INVERSION.C_ASIGNACION
		FROM
			ASIGNACION_INVERSION
			JOIN ASIGNACION ON
				ASIGNACION_INVERSION.ANO=ASIGNACION.ANO AND
				ASIGNACION_INVERSION.C_ASIGNACION=ASIGNACION.C_ASIGNACION
			
		WHERE
			ASIGNACION_INVERSION.ANO=".$this->ano." AND
			ASIGNACION_INVERSION.C_ITEM_PRESUPUESTARIO='".$this->c_item_presupuestario."' AND
			ASIGNACION_INVERSION.SUBASIGNACION=".$this->subasignacion." AND
			ASIGNACION_INVERSION.C_ASIGNACION<>".$this->c_asignacion." AND
			ASIGNACION.C_ESTADO<>9";
			

      $dataset = mysql_query($query, $this->conector);
	  
	  if (mysql_num_rows($dataset)==0) {
		return 0;
	  } else {
	    $nombre = mysql_fetch_assoc($dataset);
        return $nombre['C_ASIGNACION'];	
	  }
	} 	
	
}

//********************************************
// SITUACION INVERSION
//********************************************   
Class Inversion_Situacion {
   var $conector;
   var $mensaje_error;
   var $nroregistros;
   var $ano;  
   var $c_institucion;
   var $c_preinversion;
   var $c_ficha;
   var $id_situacion;
   var $situacion="";
   var $observaciones="";
   var $fecha_registro;
   var $c_usuario;
   
   function Inversion_Situacion($conector) { 
      $this->conector =$conector;
      $this->mensaje_error='';
      $this->nroregistros=0;
   }
    
   function cargar_grilla_situacion() {
      $datainforme=array();$filas=0;$this->filaseleccionada=-1;
      $query  = " SELECT ID_SITUACION, DATE_FORMAT(FECHA,'%d/%m/%Y'), SITUACION, OBSERVACIONES ";
      $query .= " FROM INVERSION_SITUACION WHERE ANO= ".$this->ano." AND C_PREINVERSION=".$this->c_preinversion;
      $query .= " AND C_INSTITUCION=".$this->c_institucion." AND C_FICHA=".$this->c_ficha." ORDER BY ID_SITUACION DESC";
      $dataset  = mysql_query($query, $this->conector); 
      $this->nroregistros = mysql_num_rows($dataset);
      $rows = array();
      while ($record = mysql_fetch_row($dataset)) { 
   $cols = array();$columna=0;
   foreach ($record as $value) { 
            if ($columna==2 || $columna==3) {$value=ereg_replace("[^ A-Za-z0-9_]", "", $value); }
            $cols[] = '"'.addslashes($value).'"';
            $columna=$columna+1;
   } 
   $rows[] = "\t[".implode(",", $cols)."]"; 
      } 
      $datagrilla="[\n".implode(",\n",$rows)."\n];\n"; 
      return $datagrilla;
   }

   function calcular_valor_maximo() {
      $query  = " SELECT MAX(ID_SITUACION) FROM INVERSION_SITUACION";
      $query .= " WHERE ANO= ".$this->ano." AND C_INSTITUCION=".$this->c_institucion;
      $query .= " AND C_PREINVERSION=".$this->c_preinversion." AND C_FICHA=".$this->c_ficha;
      $dataset  = mysql_query($query, $this->conector); 
      $this->nroregistros = mysql_num_rows($dataset);
      $record = @mysql_fetch_row($dataset);
      return $record[0]+1;
   }

   function actualizar_situacion() {
      $query  = "REPLACE INTO INVERSION_SITUACION(ANO, C_INSTITUCION, C_PREINVERSION, C_FICHA, ID_SITUACION, ";
      $query .= "FECHA, SITUACION, OBSERVACIONES, FECHA_REGISTRO, C_USUARIO) VALUES('";
      $query .= $this->ano."','".$this->c_institucion."','".$this->c_preinversion."','".$this->c_ficha."','".$this->id_situacion."','";
      $query .= $this->fecha."','".$this->situacion."','".$this->observaciones."','".$this->fecha_registro."','".$this->c_usuario."');";
      $dataset  = mysql_query($query, $this->conector);
      if (mysql_errno($this->conector) != 0) { 
         $this->mensaje_error="No se ha podido Actualizar la Base de Datos, mensaje de error de Mysql es: " . mysql_error($this->conector).$query; 
      } else {$this->mensaje_error='';}   
      return $this->mensaje_error;
   }

   function get_nroregistros(){ return $this->nroregistros;}  
   
   function get_error()       { return $this->mensaje_error;}
      
}

//********************************************
// ESTADO DE PAGO INVERSION
//********************************************   
Class Inversion_Pago {
   var $conector;
   var $mensaje_error;
   var $nroregistros;
   var $ano;  
   var $c_institucion;
   var $c_preinversion;
   var $c_ficha;
   var $c_pago;
   var $c_tipo_pago;
   var $fecha;
   var $mes;
   var $c_contrato;
   var $rut_contratista;
   var $c_clasificador="";
   var $n_clasificador="";
   var $detalle="";
   var $asignacion_ano;
   var $cancelado_ano;
   var $saldo_ano;
   var $fecha_recepcion;
   var $presentado_por="";
   var $tipo_documento;
   var $nro_documento="";
   var $fecha_documento;
   var $contrato_inicial;
   var $contrato_aumento;
   var $contrato_actual;
   var $pagos_anteriores;
   var $saldo_por_cancelar;
   var $valor_pago;
   var $anticipo_total;
   var $recuperacion_anticipo;
   var $anticipo_a_recuperar;
   var $anticipo;
   var $retencion_total;
   var $recuperacion_retencion;
   var $retencion_a_recuperar;
   var $retencion;
   var $dev_canje_retencion;
   var $multa;
   var $avance_fisico;
   var $total_liquido;
   var $c_docu_pagar;
   var $nro_docu_pagar="";
   var $fecha_docu_pagar;
   var $c_estado;
   var $fecha_registro;
   var $c_usuario;
   var $n_contratista="";
   var $retencion_adicional;
   var $obs_retencion_adicional;
   var $anticipo_entregado;
   var $firma1="";
   var $firma2="";
   var $firma3="";
   var $firma4="";
   var $firma5="";
   var $cargo1="";
   var $cargo2="";
   var $cargo3="";
   var $cargo4="";
   var $cargo5="";
   var $ufirma2;
   var $ufirma3;
   var $ufirma4;
   var $ufirma5;
   var $subroga1="";
   var $subroga2="";
   var $subroga3="";
   var $subroga4="";
   var $subroga5="";
   var $total_pie;
   var $tesoreria;
   
   function Inversion_Pago($conector) { 
      $this->conector =$conector;
      $this->mensaje_error='';
      $this->nroregistros=0;
   }
    
   function cuantas_firmas() {
      $query = "SELECT MAX(C_FIRMA) FROM INVERSION_PAGO_FIRMAS_PERFIL";
      $dataset  = mysql_query($query, $this->conector); 
      $record = mysql_fetch_row($dataset);
      return $record[0];
   }
   
   function perfiles_firmas($firma) {
      $query = "SELECT C_PERFIL FROM INVERSION_PAGO_FIRMAS_USUARIOS WHERE C_FIRMA=".$firma;
      $dataset  = mysql_query($query, $this->conector); 
      $record = mysql_fetch_row($dataset);
      return $record[0];
   }
   
   function tiene_observaciones() {
      $query  = " SELECT COUNT(*) ";
      $query .= " FROM INVERSION_PAGO_OBSERVACIONES ";
      $query .= " WHERE ANO= ".$this->ano." AND C_PREINVERSION=".$this->c_preinversion;
      $query .= " AND C_INSTITUCION=".$this->c_institucion." AND C_FICHA=".$this->c_ficha." AND C_PAGO=".$this->c_pago;
      $dataset  = mysql_query($query, $this->conector); 
      $record = mysql_fetch_row($dataset);
      return $record[0];
   }

   function perfil_firma($firma) {
      $query  = " SELECT A.N_PERFIL FROM ADMIN_TPERFIL A, INVERSION_PAGO_FIRMAS_PERFIL B ";
      $query .= " WHERE A.C_PERFIL=B.C_PERFIL AND B.C_FIRMA=".$firma;
      $dataset  = mysql_query($query, $this->conector); 
      $record = mysql_fetch_row($dataset);
      return $record[0];
   }

   function traer_datos_pago() {
      $query  = " SELECT A.C_CONTRATO, A.C_CLASIFICADOR, CONCAT(B.N_CLASIFICADOR,' (',B.C_CLASIFICADOR,')'), A.ASIGNACION_ANO, A.CANCELADO_ANO, A.SALDO_ANO ";
      $query .= ", A.C_TIPO_PAGO, DATE_FORMAT(A.FECHA,'%d/%m/%Y'), A.MES, A.DETALLE ";
      $query .= ", A.CONTRATO_INICIAL, A.CONTRATO_AUMENTO, A.CONTRATO_ACTUAL, A.PAGOS_ANTERIORES, A.SALDO_POR_CANCELAR, A.VALOR_PAGO ";
      $query .= ", DATE_FORMAT(A.FECHA_RECEPCION,'%d/%m/%Y'), A.PRESENTADO_POR, A.TIPO_DOCUMENTO, A.NRO_DOCUMENTO,DATE_FORMAT(A.FECHA_DOCUMENTO,'%d/%m/%Y') ";
      $query .= ", A.ANTICIPO_TOTAL, A.RECUPERACION_ANTICIPO, A.ANTICIPO_A_RECUPERAR, A.ANTICIPO ";
      $query .= ", A.RETENCION_TOTAL, A.RECUPERACION_RETENCION, A.RETENCION_A_RECUPERAR, A.RETENCION, A.DEV_CANJE_RETENCION";
      $query .= ", A.MULTA, A.AVANCE_FISICO, A.TOTAL_LIQUIDO, A.C_DOCU_PAGAR, A.NRO_DOCU_PAGAR, DATE_FORMAT(A.FECHA_DOCU_PAGAR,'%d/%m/%Y'), A.C_ESTADO ";
      $query .= ", CONCAT('(',X.RUN,'-',X.DV,')   ',X.NOMBRE,' ',X.AP_PATERNO,' ',X.AP_MATERNO), DATE_FORMAT(A.FECHA_REGISTRO,'%d/%m/%Y'), A.RETENCION_ADICIONAL ";
      $query .= ", A.OBS_RETENCION_ADICIONAL, A.ANTICIPO_ENTREGADO, A.TESORERIA  ";
      $query .= " FROM TCONTRATISTA X, INVERSION_PAGO A";
      $query .= " LEFT JOIN TCLASIFICADOR_PRESUPUESTARIO B ON (A.ANO=B.ANIO AND A.C_CLASIFICADOR=B.C_CLASIFICADOR AND (A.C_INSTITUCION=B.INSTITUCION OR B.INSTITUCION=0)) ";
      $query .= " WHERE A.ANO= ".$this->ano." AND A.C_PREINVERSION=".$this->c_preinversion;
      $query .= " AND A.C_INSTITUCION=".$this->c_institucion." AND A.C_FICHA=".$this->c_ficha." AND A.C_PAGO=".$this->c_pago;
      $query .= " AND A.RUT_CONTRATISTA=X.RUN";
      $dataset  = mysql_query($query, $this->conector); 
      $record = mysql_fetch_row($dataset);
      $this->c_contrato=$record[0];
      $this->c_clasificador=$record[1];
      $this->n_clasificador=$record[2];
      $this->asignacion_ano=number_format($record[3],0,',','.');
      $this->cancelado_ano=number_format($record[4],0,',','.');
      $this->saldo_ano=number_format($record[5],0,',','.');
      $this->c_tipo_pago=round($record[6],0);
      $this->fecha=$record[7];
      $this->mes=$record[8];
      $this->detalle=$record[9];
      $this->contrato_inicial=number_format($record[10],0,',','.');
      $this->contrato_aumento=number_format($record[11],0,',','.');
      $this->contrato_actual=number_format($record[12],0,',','.');
      $this->pagos_anteriores=number_format($record[13],0,',','.');
      $this->saldo_por_cancelar=number_format($record[14],0,',','.');
      $this->valor_pago=number_format($record[15],0,',','.');
      $this->fecha_recepcion=$record[16];
      $this->presentado_por=$record[17];
      $this->tipo_documento=$record[18];
      $this->nro_documento=$record[19];
      $this->fecha_documento=$record[20];
      $this->anticipo_total=number_format($record[21],0,',','.');
      $this->recuperacion_anticipo=number_format($record[22],0,',','.');
      $this->anticipo_a_recuperar=number_format($record[23],0,',','.');
      $this->anticipo=number_format($record[24],0,',','.');
      $this->retencion_total=number_format($record[25],0,',','.');
      $this->recuperacion_retencion=number_format($record[26],0,',','.');
      $this->retencion_a_recuperar=number_format($record[27],0,',','.');
      $this->retencion=number_format($record[28],0,',','.');
      $this->dev_canje_retencion=number_format($record[29],0,',','.');
      $this->multa=number_format($record[30],0,',','.');
      $this->avance_fisico=$record[31];
      $this->total_liquido=number_format($record[32],0,',','.');
      $this->c_docu_pagar=$record[33];
      $this->nro_docu_pagar=$record[34];
      $this->fecha_docu_pagar=$record[35];      
      $this->c_estado=$record[36];
      $this->n_contratista=$record[37];
      $this->fecha_registro=$record[38];
      $this->retencion_adicional=number_format($record[39],0,',','.');
      $this->obs_retencion_adicional=$record[40];
      $this->anticipo_entregado=number_format($record[41],0,',','.');
      $this->tesoreria=$record[42];
   }

   function cargar_grilla_pago() {
      $datainforme=array();$filas=0;$this->filaseleccionada=-1;$total=0;
      $query  = " SELECT A.C_PAGO, DATE_FORMAT(A.FECHA,'%d/%m/%Y'), B.N_MES, ";
      $query .= " IF (E.TIPO=1,CONCAT(E.NOMBRE,' (Rut:',E.RUN,')'),CONCAT(E.NOMBRE,' ',E.AP_PATERNO,' ',E.AP_MATERNO,' (Rut:',E.RUN,')')), ";
      $query .= " CONCAT(C.N_CLASIFICADOR,' (',C.C_CLASIFICADOR,')'), (A.VALOR_PAGO-A.ANTICIPO), ";
      $query .= " IF(A.C_ESTADO=1,'Pendiente',IF(A.C_ESTADO=2,'En Visto Bueno',IF(A.C_ESTADO=3,'Para Devengar',IF(A.C_ESTADO=8,'Obs.Visto Bueno',IF(A.C_ESTADO=9,'Obs.DAF','Devengado'))))), A.C_ESTADO ";
      $query .= " FROM INVERSION_PAGO A, TMESES B, TCLASIFICADOR_PRESUPUESTARIO C, TCONTRATISTA E ";
      $query .= " WHERE A.MES=B.C_MES AND A.C_CLASIFICADOR=C.C_CLASIFICADOR AND A.ANO=C.ANIO AND (A.C_INSTITUCION=C.INSTITUCION OR C.INSTITUCION=0) AND A.RUT_CONTRATISTA=E.RUN ";
      $query .= " AND A.ANO= ".$this->ano." AND A.C_PREINVERSION=".$this->c_preinversion;
      $query .= " AND A.C_INSTITUCION=".$this->c_institucion." AND A.C_FICHA=".$this->c_ficha;
      $query .= " GROUP BY A.C_PAGO, B.N_MES, C.C_CLASIFICADOR ";      
      $query .= " ORDER BY A.C_PAGO DESC";
      $dataset  = mysql_query($query, $this->conector); 
      $this->nroregistros = mysql_num_rows($dataset);
      $rows = array();
      while ($record = mysql_fetch_row($dataset)) { 
   $cols = array();$columna=0;
   foreach ($record as $value) { 
            if ($columna==5) {$total=$total+$value;}
            $columna=$columna+1;
            $cols[] = '"'.addslashes($value).'"';
   } 
   $rows[] = "\t[".implode(",", $cols)."]"; 
      } 
      $datagrilla="[\n".implode(",\n",$rows)."\n];\n"; 
      $this->total_pie=number_format($total,0,',','.');
      return $datagrilla;
   }

   function calcular_valor_maximo($xmes) {
      $query  = " SELECT MAX(C_PAGO) FROM INVERSION_PAGO";
      $query .= " WHERE ANO= ".$this->ano;
      /*." AND C_INSTITUCION=".$this->c_institucion;
      $query .= " AND C_PREINVERSION=".$this->c_preinversion." AND C_FICHA=".$this->c_ficha;*/
      $dataset  = mysql_query($query, $this->conector); 
      $this->nroregistros = mysql_num_rows($dataset);
      $record = @mysql_fetch_row($dataset);
      $retorno=$record[0]+1;
      return $retorno;
   }

   function actualizar_pago_estado() {
      $query  = " UPDATE INVERSION_PAGO SET C_ESTADO=".$this->c_estado; 
      $query .= " WHERE ANO= ".$this->ano." AND C_PREINVERSION=".$this->c_preinversion;
      $query .= " AND C_INSTITUCION=".$this->c_institucion." AND C_FICHA=".$this->c_ficha." AND C_PAGO=".$this->c_pago;
      $dataset  = mysql_query($query, $this->conector); 
      if (mysql_errno($this->conector) != 0) { 
         $this->mensaje_error="No se ha podido Actualizar la Base de Datos, mensaje de error de Mysql es: " . mysql_error($this->conector).$query; 
      } else {$this->mensaje_error='';}   
      return $this->mensaje_error;
   }
   
   function eliminar_estado_pago() {
      $query  = " DELETE FROM INVERSION_PAGO ";
      $query .= " WHERE ANO= ".$this->ano." AND C_PREINVERSION=".$this->c_preinversion;
      $query .= " AND C_INSTITUCION=".$this->c_institucion." AND C_FICHA=".$this->c_ficha." AND C_PAGO=".$this->c_pago;
      $dataset  = mysql_query($query, $this->conector); 
      $query  = " DELETE FROM INVERSION_PAGO_DEVENGO ";
      $query .= " WHERE ANO= ".$this->ano." AND C_PREINVERSION=".$this->c_preinversion;
      $query .= " AND C_INSTITUCION=".$this->c_institucion." AND C_FICHA=".$this->c_ficha." AND C_PAGO=".$this->c_pago;
      $dataset  = mysql_query($query, $this->conector); 
      $query  = " DELETE FROM INVERSION_PAGO_FIRMA ";
      $query .= " WHERE ANO= ".$this->ano." AND C_PREINVERSION=".$this->c_preinversion;
      $query .= " AND C_INSTITUCION=".$this->c_institucion." AND C_FICHA=".$this->c_ficha." AND C_PAGO=".$this->c_pago;
      $dataset  = mysql_query($query, $this->conector); 
      $query  = " DELETE FROM INVERSION_PAGO_OBSERVACIONES ";
      $query .= " WHERE ANO= ".$this->ano." AND C_PREINVERSION=".$this->c_preinversion;
      $query .= " AND C_INSTITUCION=".$this->c_institucion." AND C_FICHA=".$this->c_ficha." AND C_PAGO=".$this->c_pago;
      $dataset  = mysql_query($query, $this->conector);          
      if (mysql_errno($this->conector) != 0) { 
         $this->mensaje_error="No se ha podido Actualizar la Base de Datos, mensaje de error de Mysql es: " . mysql_error($this->conector).$query; 
      } else {$this->mensaje_error='';}   
      return $this->mensaje_error;
   }

   function actualizar_pago() {
      $query  = " REPLACE INTO INVERSION_PAGO VALUES('";
      $query  .= $this->ano."','";  
      $query  .= $this->c_institucion."','";  
      $query  .= $this->c_preinversion."','";  
      $query  .= $this->c_ficha."','";  
      $query  .= $this->c_pago."','";  
      $query  .= $this->c_tipo_pago."','";  
      $query  .= $this->fecha."','";  
      $query  .= $this->mes."','";  
      $query  .= $this->c_contrato."','";  
      $query  .= $this->rut_contratista."','";  
      $query  .= $this->c_clasificador."','";  
      $query  .= $this->detalle."','";  
      $query  .= $this->asignacion_ano."','";  
      $query  .= $this->cancelado_ano."','";  
      $query  .= $this->saldo_ano."','";  
      $query  .= $this->fecha_recepcion."','";  
      $query  .= $this->presentado_por."','";  
      $query  .= $this->tipo_documento."','";  
      $query  .= $this->nro_documento."','";  
      $query  .= $this->fecha_documento."','";  
      $query  .= $this->contrato_inicial."','";  
      $query  .= $this->contrato_aumento."','";  
      $query  .= $this->contrato_actual."','";  
      $query  .= $this->pagos_anteriores."','";  
      $query  .= $this->saldo_por_cancelar."','";  
      $query  .= $this->valor_pago."','";  
      $query  .= $this->anticipo_total."','";  
      $query  .= $this->recuperacion_anticipo."','";  
      $query  .= $this->anticipo_a_recuperar."','";  
      $query  .= $this->anticipo."','";  
      $query  .= $this->retencion_total."','";  
      $query  .= $this->recuperacion_retencion."','";  
      $query  .= $this->retencion_a_recuperar."','";  
      $query  .= $this->retencion."','";  
      $query  .= $this->dev_canje_retencion."','";  
      $query  .= $this->multa."','";  
      $query  .= $this->avance_fisico."','";  
      $query  .= $this->total_liquido."','";  
      $query  .= $this->c_docu_pagar."','";  
      $query  .= $this->nro_docu_pagar."','";  
      $query  .= $this->fecha_docu_pagar."','";  
      $query  .= $this->c_estado."','";  
      $query  .= $this->fecha_registro."','";  
      $query  .= $this->c_usuario."','";  
      $query  .= $this->retencion_adicional."','";  
      $query  .= $this->obs_retencion_adicional."','";  
      $query  .= $this->anticipo_entregado."','";  
      $query  .= $this->tesoreria."');";  
      $dataset  = mysql_query($query, $this->conector);
      if (mysql_errno($this->conector) != 0) { 
         $this->mensaje_error="No se ha podido Actualizar la Base de Datos, mensaje de error de Mysql es: " . mysql_error($this->conector).$query; 
      } else {$this->mensaje_error='';}   
      return $this->mensaje_error;
   }
   
   function datos_firma($usuario,$cuantas_firmas) {
      $query  = " SELECT CONCAT(B.NOMBRES,' ',B.APELLIDO_PATERNO,' ',B.APELLIDO_MATERNO), D.N_PERFIL FROM ";
      $query .= " ADMIN_USUARIO B, ADMIN_USUARIO_PERFIL C, ADMIN_TPERFIL D ";
      $query .= " WHERE B.C_USUARIO=C.C_USUARIO AND C.C_PERFIL=D.C_PERFIL";
      $query .= " AND B.C_USUARIO=".$usuario;
      $dataset  = mysql_query($query, $this->conector);       
      $record = @mysql_fetch_row($dataset);
      $this->firma1=$record[0];
      $this->cargo1=$record[1];
      $query = " SELECT SUBROGA FROM INVERSION_PAGO_FIRMA ";
      $query .= " WHERE C_ORDEN=1";
      $query .= " AND ANO= ".$this->ano." AND C_PREINVERSION=".$this->c_preinversion;
      $query .= " AND C_INSTITUCION=".$this->c_institucion." AND C_FICHA=".$this->c_ficha." AND C_PAGO=".$this->c_pago;
      $dataset  = mysql_query($query, $this->conector);       
      $record = @mysql_fetch_row($dataset);
      $this->subroga1=$record[0];
      for ($i=2;$i<=$cuantas_firmas; $i++) {
         $query  = " SELECT B.C_USUARIO, CONCAT(B.NOMBRES,' ',B.APELLIDO_PATERNO,' ',B.APELLIDO_MATERNO), D.N_PERFIL, A.SUBROGA ";
         $query .= " FROM ADMIN_USUARIO B, ADMIN_USUARIO_PERFIL C, ADMIN_TPERFIL D, INVERSION_PAGO_FIRMA A ";
         $query .= " WHERE B.C_USUARIO=C.C_USUARIO AND C.C_PERFIL=D.C_PERFIL";
         $query .= " AND A.C_USUARIO=B.C_USUARIO AND A.C_ORDEN=".$i;
         $query .= " AND A.ANO= ".$this->ano." AND A.C_PREINVERSION=".$this->c_preinversion;
         $query .= " AND A.C_INSTITUCION=".$this->c_institucion." AND A.C_FICHA=".$this->c_ficha." AND A.C_PAGO=".$this->c_pago;
         $dataset  = mysql_query($query, $this->conector);       
         $record = @mysql_fetch_row($dataset);
         if ($i==2) {
            $this->ufirma2=$record[0];
            $this->firma2=$record[1];
            $this->cargo2=$record[2];
            $this->subroga2=$record[3];
         }   
         if ($i==3) {
            $this->ufirma3=$record[0];
            $this->firma3=$record[1];
            $this->cargo3=$record[2];
            $this->subroga3=$record[3];
         }   
         if ($i==4) {
            $this->ufirma4=$record[0];
            $this->firma4=$record[1];
            $this->cargo4=$record[2];
            $this->subroga4=$record[3];
         }   
         if ($i==5) {
            $this->ufirma5=$record[0];
            $this->firma5=$record[1];
            $this->cargo5=$record[2];
            $this->subroga5=$record[3];
         }   
      }   
      return 1;
   }

   function eliminar_firmas() {
      $query  = " DELETE FROM INVERSION_PAGO_FIRMA";
      $query .= " WHERE ANO= ".$this->ano." AND C_PREINVERSION=".$this->c_preinversion;
      $query .= " AND C_INSTITUCION=".$this->c_institucion." AND C_FICHA=".$this->c_ficha." AND C_PAGO=".$this->c_pago;
      $dataset  = mysql_query($query, $this->conector); 
      if (mysql_errno($this->conector) != 0) { 
         $this->mensaje_error="No se ha podido Actualizar la Base de Datos, mensaje de error de Mysql es: " . mysql_error($this->conector).$query; 
      } else {$this->mensaje_error='';}   
      return $this->mensaje_error;
   }
      
   function insertar_firma($op,$usuario,$subroga) {
      $query  = " INSERT INTO INVERSION_PAGO_FIRMA VALUE('";
      $query .= $this->ano."','".$this->c_institucion."','".$this->c_preinversion."','".$this->c_ficha."','";
      $query .= $this->c_pago."','".$op."','T','".$usuario."','".$subroga."')";
      $dataset  = mysql_query($query, $this->conector); 
      if (mysql_errno($this->conector) != 0) { 
         $this->mensaje_error="No se ha podido Actualizar la Base de Datos, mensaje de error de Mysql es: " . mysql_error($this->conector).$query; 
      } else {$this->mensaje_error='';}   
      return $this->mensaje_error;
   }

   function observar_pago($xcobservacion,$xtipo,$xfecha,$xobservacion) {
      $query  = " INSERT INTO INVERSION_PAGO_OBSERVACIONES VALUE('";
      $query .= $this->ano."','".$this->c_institucion."','".$this->c_preinversion."','".$this->c_ficha."','";
      $query .= $this->c_pago."','".$xcobservacion."','".$xtipo."','".$xfecha."','".$xobservacion."','".$this->fecha_registro."','".$this->c_usuario."')";
      $dataset  = mysql_query($query, $this->conector); 
      if (mysql_errno($this->conector) != 0) { 
         $this->mensaje_error="No se ha podido Actualizar la Base de Datos, mensaje de error de Mysql es: " . mysql_error($this->conector).$query; 
      } else {$this->mensaje_error='';}   
      return $this->mensaje_error;
   }

   function devengar_pago($xfecha,$xcomprobante,$xobservacion) {
      $query  = " INSERT INTO INVERSION_PAGO_DEVENGO VALUE('";
      $query .= $this->ano."','".$this->c_institucion."','".$this->c_preinversion."','".$this->c_ficha."','";
      $query .= $this->c_pago."','".$xfecha."','".$xcomprobante."','".$xobservacion."','".$this->fecha_registro."','".$this->c_usuario."')";
      $dataset  = mysql_query($query, $this->conector); 
      if (mysql_errno($this->conector) != 0) { 
         $this->mensaje_error="No se ha podido Actualizar la Base de Datos, mensaje de error de Mysql es: " . mysql_error($this->conector).$query; 
      } else {$this->mensaje_error='';}   
      return $this->mensaje_error;
   }

   function calcular_valor_maximo_observacion() {
      $query  = " SELECT MAX(C_OBSERVACION) FROM INVERSION_PAGO_OBSERVACIONES";
      $query .= " WHERE ANO= ".$this->ano." AND C_INSTITUCION=".$this->c_institucion;
      $query .= " AND C_PREINVERSION=".$this->c_preinversion." AND C_FICHA=".$this->c_ficha." AND C_PAGO=".$this->c_pago;
      $dataset  = mysql_query($query, $this->conector); 
      $this->nroregistros = mysql_num_rows($dataset);
      $record = @mysql_fetch_row($dataset);
      return $record[0]+1;
   }

   function get_nroregistros(){ return $this->nroregistros;}  
   
   function get_error()       { return $this->mensaje_error;}
      
   function get_filaseleccionada()       { return $this->filaseleccionada;}
}

//********************************************
// MANO DE OBRA INVERSION
//********************************************   
Class Inversion_Mano_Obra {
   var $conector;
   var $mensaje_error;
   var $nroregistros;
   var $ano;  
   var $c_institucion;
   var $c_preinversion;
   var $c_ficha;
   var $mes;
   var $programado;
   var $trabajadores;
   var $contratos;
   var $desvinculaciones;
   var $fecha_registro;
   var $c_usuario;
   
   function Inversion_Mano_Obra($conector) { 
      $this->conector =$conector;
      $this->mensaje_error='';
      $this->nroregistros=0;
   }
    
   function cargar_grilla_mano_obra() {
      $datainforme=array();
      $datainforme[1][0]="Empleo Programado";
      $datainforme[2][0]="Total de Trabajadores";
      $datainforme[3][0]="Contrataciones";
      $datainforme[4][0]="Desvinculaciones";
      for ($i=1;$i<5;$i++) {
         for ($j=1;$j<13;$j++) {
            $datainforme[$i][$j]=0;   
         }
      }
      $query  = " SELECT MES, PROGRAMADO, TRABAJADORES, CONTRATOS, DESVINCULACIONES ";
      $query .= " FROM INVERSION_MANO_OBRA WHERE ANO= ".$this->ano." AND C_PREINVERSION=".$this->c_preinversion;
      $query .= " AND C_INSTITUCION=".$this->c_institucion." AND C_FICHA=".$this->c_ficha." ORDER BY MES";
      $dataset  = mysql_query($query, $this->conector); 
      $this->nroregistros = mysql_num_rows($dataset);
      while ($record = mysql_fetch_row($dataset)) { 
          $datainforme[1][$record[0]]=$record[1];
          $datainforme[2][$record[0]]=$record[2];
          $datainforme[3][$record[0]]=$record[3];
          $datainforme[4][$record[0]]=$record[4];
      }      
      $rows = array();
      for ($i=1;$i<5;$i++) {
   $cols = array();
         for ($j=0;$j<13;$j++) {
            $cols[] = '"'.addslashes($datainforme[$i][$j]).'"';
   } 
   $rows[] = "\t[".implode(",", $cols)."]"; 
      } 
      $datagrilla="[\n".implode(",\n",$rows)."\n];\n"; 
      return $datagrilla;
   }

   function eliminar_mano_obra() {
      $query  = "DELETE FROM INVERSION_MANO_OBRA";
      $query .= " WHERE ANO= ".$this->ano." AND C_PREINVERSION=".$this->c_preinversion;
      $query .= " AND C_INSTITUCION=".$this->c_institucion." AND C_FICHA=".$this->c_ficha;
      $dataset  = mysql_query($query, $this->conector); 
      if (mysql_errno($this->conector) != 0) { 
         $this->mensaje_error="No se ha podido Actualizar la Base de Datos, mensaje de error de Mysql es: " . mysql_error($this->conector).$query; 
      } else {$this->mensaje_error='';}   
      return $this->mensaje_error;
   }

   function actualizar_mano_obra() {
      $query  = "REPLACE INTO INVERSION_MANO_OBRA(ANO, C_INSTITUCION, C_PREINVERSION, C_FICHA, MES, ";
      $query .= "PROGRAMADO, TRABAJADORES, CONTRATOS, DESVINCULACIONES, FECHA_REGISTRO, C_USUARIO) VALUES('";
      $query .= $this->ano."','".$this->c_institucion."','".$this->c_preinversion."','".$this->c_ficha."','".$this->mes."','";
      $query .= $this->programado."','".$this->trabajadores."','".$this->contratos."','".$this->desvinculaciones."','".$this->fecha_registro."','".$this->c_usuario."');";
      $dataset  = mysql_query($query, $this->conector);
      if (mysql_errno($this->conector) != 0) { 
         $this->mensaje_error="No se ha podido Actualizar la Base de Datos, mensaje de error de Mysql es: " . mysql_error($this->conector).$query; 
      } else {$this->mensaje_error='';}   
      return $this->mensaje_error;
   }
 
   function get_nroregistros(){ return $this->nroregistros;}  
   
   function get_error()       { return $this->mensaje_error;}
      
}

//********************************************
// CONTRATISTA
//********************************************   
Class CONTRATISTA {
   var $conector;
   var $mensaje_error;
   var $nroregistros;
   var $run;  
   var $dv="";
   var $tipo="";
   var $nombre="";
   var $ap_paterno="";
   var $ap_materno="";
   var $domicilio="";
   var $contacto="";
   var $fono="";
   var $email="";
   
   function CONTRATISTA($conector) { 
      $this->conector =$conector;
      $this->mensaje_error='';
      $this->nroregistros=0;
   }
    
   function existe_contratista() {
      $query  = " SELECT COUNT(*) FROM TCONTRATISTA WHERE RUN=".$this->run;
      $dataset  = mysql_query($query, $this->conector); 
      $record = mysql_fetch_row($dataset);
      return $record[0];
   }

   function consultar_datos_contratista() {
      $query  = " SELECT DV, IF(TIPO='J',NOMBRE,CONCAT(NOMBRE,' ',AP_PATERNO,' ',AP_MATERNO)) FROM TCONTRATISTA WHERE RUN=".$this->run;
      $dataset  = mysql_query($query, $this->conector); 
      $record = mysql_fetch_row($dataset);
      $this->dv=$record[0];
      $this->nombre=$record[1];
      return $record;
   }

   function actualizar_contratista() {
      $query  = " INSERT INTO TCONTRATISTA VALUES('";
      $query .= $this->run."','".$this->dv."','".$this->tipo."','".$this->nombre."','".$this->ap_paterno."','";
      $query .= $this->ap_materno."','".$domicilio."','".$this->contacto."','".$this->fono."','".$this->email."');";
      $dataset  = mysql_query($query, $this->conector); 
      return 1;
   }

   function get_nroregistros(){ return $this->nroregistros;}  
   
   function get_error()       { return $this->mensaje_error;}
      
}

//********************************************
// MARCO PRESUPUESTARIO
//********************************************   
Class Marco_Presupuestario {
   var $conector;
   var $mensaje_error;
   var $nroregistros;
   var $ano;  
   var $mes;
   var $c_clasificador="";
   var $monto;
   
   function Marco_Presupuestario($conector) { 
      $this->conector =$conector;
      $this->mensaje_error='';
      $this->estado=0;
      $this->nroregistros=0;
   }
    
   function cargar_grilla_marco() {
      $datainforme=array();$filas=0;
      $query .= "SELECT A.C_CLASIFICADOR, SUM(IF(A.MES=0,A.MONTO,0)), SUM(IF(A.MES=99,A.MONTO,0)),B.N_CLASIFICADOR ";
      $query .= " FROM MARCO_PRESUPUESTARIO_MENSUAL A LEFT JOIN TCLASIFICADOR_PRESUPUESTARIO B ON (A.ANO=B.ANIO AND A.C_CLASIFICADOR=B.C_CLASIFICADOR) ";
      $query .= "WHERE A.ANO=2009 GROUP BY A.C_CLASIFICADOR ORDER BY A.C_CLASIFICADOR";
      $dataset  = mysql_query($query, $this->conector); 
      $this->nroregistros = mysql_num_rows($dataset);
      while ($record = mysql_fetch_row($dataset)) { 
         if (strlen($record[0])==2) {
            $datainforme[$filas][0]=$record[0];
            $datainforme[$filas][6]='1';
         }
         if (strlen($record[0])==5) {
            $datainforme[$filas][1]=substr($record[0],3,2);
            $datainforme[$filas][6]='2';
         }
         if (strlen($record[0])>5) {
            $datainforme[$filas][2]=substr($record[0],6,5);
            $datainforme[$filas][6]='3';
         }      
         $datainforme[$filas][3]=$record[3];   
         if ($record[0]=="34") {$datainforme[$filas][3]="Servicio de la Deuda";}
         if ($record[0]=="34.07") {$datainforme[$filas][3]="Deuda Flotante";}
         if ($record[0]=="35") {$datainforme[$filas][3]="Saldo Final de Caja";}
         $datainforme[$filas][4]=number_format($record[1],0,',','.');  
         $datainforme[$filas][5]=number_format($record[2],0,',','.');  
         $filas=$filas+1;
      }
      $rows = array();
      for ($i=0; $i<$filas; $i++) {
   $cols = array();
   for ($j=0;$j<7;$j++) {
            $cols[] = '"'.addslashes($datainforme[$i][$j]).'"';
   } 
   $rows[] = "\t[".implode(",", $cols)."]"; 
      } 
      $datagrilla="[\n".implode(",\n",$rows)."\n];\n"; 
      return $datagrilla;
   }

   function get_nroregistros(){ return $this->nroregistros;}  
   
   function get_error()       { return $this->mensaje_error;}
      
}

//********************************************
// CONVENIOS 
//********************************************   
Class Inversion_Convenios {
   var $conector;
   var $mensaje_error;
   var $nroregistros;
   var $ano;
   var $c_institucion;
   var $c_preinversion;
   var $c_ficha;
   var $c_convenio;
   var $c_tipo_convenio;
   var $fecha_convenio;
   var $nro_resolucion="";
   var $c_tipo_resolucion;
   var $rut;
   var $monto;
   var $descripcion="";
   var $nombre="";
   var $ejecutado_por="";
   var $monto_total;
   var $transferido;
   var $saldo;
   var $dv="";
   var $nombre_rut="";
   var $plazo="";
   var $objetivo="";
   var $objetivo_especifico="";
   var $producto="";
   var $nestadotransferencia;

   var $dv_contraparte="";
   var $nombre_contraparte="";
   
   function Inversion_Convenios($conector) { 
      $this->conector =$conector;
      $this->mensaje_error='';
      $this->estado=0;
      $this->nroregistros=0;
   }
    
   function cargar_convenio() {
      $query .= "SELECT A.C_CONVENIO, A.C_TIPO_CONVENIO ";
      $query .= ", DATE_FORMAT(A.FECHA_CONVENIO,'%d/%m/%Y'), A.NRO_RESOLUCION, DATE_FORMAT(A.FECHA_RESOLUCION,'%d/%m/%Y'), A.C_TIPO_RESOLUCION, A.RUT, A.MONTO, A.DESCRIPCION";
      $query .= ", B.DV, CONCAT(B.AP_PATERNO,' ',B.AP_MATERNO,' ',B.NOMBRE), A.NOMBRE, A.EJECUTADO_POR, C.PLAZO, C.OBJETIVO, C.OBJETIVO_ESPECIFICO, C.PRODUCTO";
      $query .= ", A.MONTO_TOTAL, A.TRANSFERIDO, A.SALDO ";
      $query .= " FROM INVERSION_CONVENIOS A ";
      $query .= " LEFT JOIN TCONTRATISTA B ON (A.RUT=B.RUN) ";
      $query .= " LEFT JOIN INVERSION_CONVENIOS_DATOS C ON (A.ANO=C.ANO AND A.C_INSTITUCION=C.C_INSTITUCION AND A.C_PREINVERSION=C.C_PREINVERSION ";
      $query .= " AND A.C_FICHA=C.C_FICHA AND A.C_CONVENIO=C.C_CONVENIO) ";
      $query .= " WHERE A.ANO= ".$this->ano." AND A.C_PREINVERSION=".$this->c_preinversion;
      $query .= " AND A.C_INSTITUCION=".$this->c_institucion." AND A.C_FICHA=".$this->c_ficha." AND A.C_CONVENIO=".$this->c_convenio;
      $dataset  = mysql_query($query, $this->conector); 
      $this->nroregistros = mysql_num_rows($dataset);
      $record = mysql_fetch_row($dataset);
      $this->c_convenio=$record[0];
      $this->c_tipo_convenio=$record[1];
      $this->fecha_convenio=$record[2];
      $this->nro_resolucion=$record[3];
      $this->fecha_resolucion=$record[4];
      $this->c_tipo_resolucion=$record[5];
      $this->rut=$record[6];
      $this->monto=number_format($record[7],0,',','.');
      $this->descripcion=$record[8];
      $this->dv=$record[9];
      $this->nombre_rut=$record[10];
      $this->nombre=$record[11];
      $this->ejecutado_por=$record[12];
      $this->plazo=$record[13];      
      $this->objetivo=$record[14];      
      $this->objetivo_especifico=$record[15];      
      $this->producto=$record[16];      
      $this->monto_total=number_format($record[17],0,',','.');
      $this->transferido=number_format($record[18],0,',','.');
      $this->saldo=number_format($record[19],0,',','.');
      return $this->nroregistros;
   }

   function insertar_convenio() {
      $query  = " INSERT INTO INVERSION_CONVENIOS VALUES('";
      $query .= $this->ano."','".$this->c_institucion."','".$this->c_preinversion."','".$this->c_ficha."','".$this->c_convenio."','";
      $query .= $this->c_tipo_convenio."','".$this->fecha_convenio."','".$this->nro_resolucion."','".$this->fecha_resolucion."','".$this->c_tipo_resolucion."','";
      $query .= $this->rut."','".$this->monto."','".$this->descripcion."','".$this->nombre."','".$this->ejecutado_por."','".$this->monto_total."','".$this->transferido."','".$this->saldo."')";
      $dataset  = mysql_query($query, $this->conector);
      if (mysql_errno($this->conector) != 0) { 
         $this->mensaje_error="No se ha podido Actualizar la Base de Datos, mensaje de error de Mysql es: " . mysql_error($this->conector).$query; 
      } else {$this->mensaje_error='';}   
      return $this->mensaje_error;
   }

   function insertar_convenio_datos() {
      $query  = " INSERT INTO INVERSION_CONVENIOS_DATOS VALUES('";
      $query .= $this->ano."','".$this->c_institucion."','".$this->c_preinversion."','".$this->c_ficha."','".$this->c_convenio."','";
      $query .= $this->plazo."','".$this->objetivo."','".$this->objetivo_especifico."','".$this->producto."')";
      $dataset  = mysql_query($query, $this->conector);
      if (mysql_errno($this->conector) != 0) { 
         $this->mensaje_error="No se ha podido Actualizar la Base de Datos, mensaje de error de Mysql es: " . mysql_error($this->conector).$query; 
      } else {$this->mensaje_error='';}   
      return $this->mensaje_error;
   }

   function eliminar_convenio() {
      $query  = " DELETE FROM INVERSION_CONVENIOS ";
      $query .= " WHERE ANO= ".$this->ano." AND C_PREINVERSION=".$this->c_preinversion;
      $query .= " AND C_INSTITUCION=".$this->c_institucion." AND C_FICHA=".$this->c_ficha." AND C_CONVENIO=".$this->c_convenio;
      $dataset  = mysql_query($query, $this->conector);
      $query  = " DELETE FROM INVERSION_CONVENIOS_DATOS ";
      $query .= " WHERE ANO= ".$this->ano." AND C_PREINVERSION=".$this->c_preinversion;
      $query .= " AND C_INSTITUCION=".$this->c_institucion." AND C_FICHA=".$this->c_ficha." AND C_CONVENIO=".$this->c_convenio;
      $dataset  = mysql_query($query, $this->conector);
      if (mysql_errno($this->conector) != 0) { 
         $this->mensaje_error="No se ha podido Actualizar la Base de Datos, mensaje de error de Mysql es: " . mysql_error($this->conector).$query; 
      } else {$this->mensaje_error='';}   
      return $this->mensaje_error;
   }

   function calcular_valor_maximo() {
      $query  = " SELECT MAX(C_CONVENIO) FROM INVERSION_CONVENIOS";
      $query .= " WHERE ANO= ".$this->ano." AND C_INSTITUCION=".$this->c_institucion;
      $query .= " AND C_PREINVERSION=".$this->c_preinversion." AND C_FICHA=".$this->c_ficha;
      $dataset  = mysql_query($query, $this->conector); 
      $this->nroregistros = mysql_num_rows($dataset);
      $record = @mysql_fetch_row($dataset);
      return $record[0]+1;
   }

   function cargar_grilla_convenios($xseguimiento) {
      if ($xseguimiento!=5) {
         $query .= "SELECT  A.C_CONVENIO, A.NOMBRE, DATE_FORMAT(A.FECHA_CONVENIO,'%d/%m/%Y'), A.NRO_RESOLUCION, DATE_FORMAT(A.FECHA_RESOLUCION,'%d/%m/%Y'), ";
         $query .= " IF (A.C_TIPO_RESOLUCION=1,'Afecta','Exenta')";
         $query .= ", A.EJECUTADO_POR, A.MONTO";
         $query .= " FROM INVERSION_CONVENIOS A ";
         $query .= " LEFT JOIN TCONTRATISTA B ON (A.RUT=B.RUN) ";
         $query .= " WHERE A.ANO= ".$this->ano." AND A.C_PREINVERSION=".$this->c_preinversion;
         $query .= " AND A.C_INSTITUCION=".$this->c_institucion." AND A.C_FICHA=".$this->c_ficha;
         $query .= " ORDER BY A.C_CONVENIO DESC";
      } else {
         $query .= "SELECT  A.C_CONVENIO, IF(B.TIPO=1,CONCAT(B.NOMBRE,'  ( Rut:',A.RUT,'-',B.DV,' )'),CONCAT(B.NOMBRE,' ',B.AP_PATERNO,' ',B.AP_MATERNO,'  ( Rut:',A.RUT,',',B.DV,' )')), ";
         $query .= " DATE_FORMAT(A.FECHA_CONVENIO,'%d/%m/%Y'), A.NRO_RESOLUCION, DATE_FORMAT(A.FECHA_RESOLUCION,'%d/%m/%Y'), ";
         $query .= " IF (A.C_TIPO_RESOLUCION=1,'Afecta','Exenta')";
         $query .= ", A.EJECUTADO_POR, A.MONTO";
         $query .= " FROM INVERSION_CONVENIOS A ";
         $query .= " LEFT JOIN TCONTRATISTA B ON (A.RUT=B.RUN) ";
         $query .= " WHERE A.ANO= ".$this->ano." AND A.C_PREINVERSION=".$this->c_preinversion;
         $query .= " AND A.C_INSTITUCION=".$this->c_institucion." AND A.C_FICHA=".$this->c_ficha;
         $query .= " ORDER BY A.C_CONVENIO DESC";
      }   
      $dataset  = mysql_query($query, $this->conector); 
      $nroregistros = @mysql_num_rows($dataset);
      $rows = array();
      while ($record = mysql_fetch_row($dataset)) { 
   $cols = array();$columna=0;
   foreach ($record as $value) { 
      if ($columna==7) {$value=number_format($value,0,',','.');}
            $cols[] = '"'.addslashes($value).'"';
      $columna=$columna+1;
   } 
   $rows[] = "\t[".implode(",", $cols)."]"; 
      } 
      $datagrilla="[\n".implode(",\n",$rows)."\n];\n"; 
      return $datagrilla;
   }

   function recuperar_informacion_transferencia() {
      $query  = " SELECT A.MONTO, A.RUT ";
      $query .= " FROM INVERSION_CONVENIOS A";
      $query .= " WHERE A.ANO= ".$this->ano." AND A.C_INSTITUCION=".$this->c_institucion;
      $query .= " AND A.C_PREINVERSION=".$this->c_preinversion." AND A.C_FICHA=".$this->c_ficha;
      $query .= " AND A.C_CONVENIO=".$this->c_convenio;
      $dataset  = mysql_query($query, $this->conector); 
      $record = mysql_fetch_row($dataset);
      return $record;
   }

   function recuperar_transferencias_anteriores() {
      $query  = " SELECT SUM(VALOR_TRANSFERENCIA) FROM INVERSION_TRANSFERENCIA ";
      $query .= " WHERE ANO= ".$this->ano." AND C_INSTITUCION=".$this->c_institucion;
      $query .= " AND C_PREINVERSION=".$this->c_preinversion." AND C_FICHA=".$this->c_ficha;
      $query .= " AND C_CONVENIO=".$this->c_convenio." AND C_TRANSFERENCIA<>".$this->nestadotransferencia;
      $query .= " AND C_ESTADO=4";
      $dataset  = mysql_query($query, $this->conector); 
      $record = mysql_fetch_row($dataset);
      return $record;
   }

   function recuperar_transferencias_anteriores_no_devengadas() {
      $query  = " SELECT SUM(VALOR_TRANSFERENCIA) FROM INVERSION_TRANSFERENCIA ";
      $query .= " WHERE ANO= ".$this->ano." AND C_INSTITUCION=".$this->c_institucion;
      $query .= " AND C_PREINVERSION=".$this->c_preinversion." AND C_FICHA=".$this->c_ficha;
      $query .= " AND C_CONVENIO=".$this->c_convenio." AND C_TRANSFERENCIA<>".$this->nestadotransferencia;
      $query .= " AND C_ESTADO IN (2,3,9)";
      $dataset  = mysql_query($query, $this->conector); 
      $record = mysql_fetch_row($dataset);
      return $record;
   }

   function existe_pago() {
      return 1;
   }

   function get_nroregistros(){ return $this->nroregistros;}  
   
   function get_error()       { return $this->mensaje_error;}
      
}

//********************************************
// ESTADO DE TRANSFERENCIA INVERSION
//********************************************   
Class Inversion_Transferencia {
   var $conector;
   var $mensaje_error;
   var $nroregistros;
   var $ano;  
   var $c_institucion;
   var $c_preinversion;
   var $c_ficha;
   var $c_transferencia;
   var $fecha;
   var $mes;
   var $c_convenio;
   var $rut_contraparte;
   var $c_clasificador="";
   var $n_clasificador="";
   var $detalle="";
   var $fecha_recepcion;
   var $presentado_por="";
   var $tipo_documento;
   var $nro_documento="";
   var $fecha_documento;
   var $valor_convenio;
   var $transferencias_anteriores;
   var $saldo_por_transferir;
   var $valor_transferencia;
   var $nro_docu_pagar="";
   var $fecha_docu_pagar;
   var $c_estado;
   var $fecha_registro;
   var $c_usuario;
   var $n_contraparte="";
   var $firma1="";
   var $firma2="";
   var $firma3="";
   var $firma4="";
   var $firma5="";
   var $cargo1="";
   var $cargo2="";
   var $cargo3="";
   var $cargo4="";
   var $cargo5="";
   var $ufirma2;
   var $ufirma3;
   var $ufirma4;
   var $ufirma5;
   
   function Inversion_Transferencia($conector) { 
      $this->conector =$conector;
      $this->mensaje_error='';
      $this->nroregistros=0;
   }
    
   function cuantas_firmas() {
      $query = "SELECT MAX(C_FIRMA) FROM INVERSION_PAGO_FIRMAS_PERFIL";
      $dataset  = mysql_query($query, $this->conector); 
      $record = mysql_fetch_row($dataset);
      return $record[0];
   }
   
   function perfiles_firmas($firma) {
      $query = "SELECT C_PERFIL FROM INVERSION_PAGO_FIRMAS_USUARIOS WHERE C_FIRMA=".$firma;
      $dataset  = mysql_query($query, $this->conector); 
      $record = mysql_fetch_row($dataset);
      return $record[0];
   }

   function perfil_firma($firma) {
      $query  = " SELECT A.N_PERFIL FROM ADMIN_TPERFIL A, INVERSION_PAGO_FIRMAS_PERFIL B ";
      $query .= " WHERE A.C_PERFIL=B.C_PERFIL AND B.C_FIRMA=".$firma;
      $dataset  = mysql_query($query, $this->conector); 
      $record = mysql_fetch_row($dataset);
      return $record[0];
   }

   function tiene_observaciones() {
      $query  = " SELECT COUNT(*) ";
      $query .= " FROM INVERSION_TRANSFERENCIA_OBSERVACIONES ";
      $query .= " WHERE ANO= ".$this->ano." AND C_PREINVERSION=".$this->c_preinversion;
      $query .= " AND C_INSTITUCION=".$this->c_institucion." AND C_FICHA=".$this->c_ficha." AND C_TRANSFERENCIA=".$this->c_transferencia;
      $dataset  = mysql_query($query, $this->conector); 
      $record = mysql_fetch_row($dataset);
      return $record[0];
   }

   function traer_datos_transferencia() {
      $query  = " SELECT A.C_CONVENIO, A.C_CLASIFICADOR, CONCAT(B.N_CLASIFICADOR,' (',B.C_CLASIFICADOR,')') ";
      $query .= ", DATE_FORMAT(A.FECHA,'%d/%m/%Y'), A.MES, A.DETALLE ";
      $query .= ", A.VALOR_CONVENIO, A.TRANSFERENCIAS_ANTERIORES, A.SALDO_POR_TRANSFERIR, A.VALOR_TRANSFERENCIA ";
      $query .= ", DATE_FORMAT(A.FECHA_RECEPCION,'%d/%m/%Y'), A.PRESENTADO_POR, A.TIPO_DOCUMENTO, A.NRO_DOCUMENTO,DATE_FORMAT(A.FECHA_DOCUMENTO,'%d/%m/%Y') ";
      $query .= ", A.NRO_DOCU_PAGAR, DATE_FORMAT(A.FECHA_DOCU_PAGAR,'%d/%m/%Y'), A.C_ESTADO ";
      $query .= ", CONCAT('(Rut:',X.RUN,'-',X.DV,')  ',X.NOMBRE,' ',X.AP_PATERNO,' ',X.AP_MATERNO), DATE_FORMAT(A.FECHA_REGISTRO,'%d/%m/%Y'), Z.NOMBRE";
      $query .= " FROM TCONTRATISTA X, INVERSION_CONVENIOS Z, INVERSION_TRANSFERENCIA A ";
      $query .= " LEFT JOIN TCLASIFICADOR_PRESUPUESTARIO B ON (A.ANO=B.ANIO AND A.C_CLASIFICADOR=B.C_CLASIFICADOR AND (A.C_INSTITUCION=B.INSTITUCION OR B.INSTITUCION=0)) ";
      $query .= " WHERE A.ANO= ".$this->ano." AND A.C_PREINVERSION=".$this->c_preinversion;
      $query .= " AND A.C_INSTITUCION=".$this->c_institucion." AND A.C_FICHA=".$this->c_ficha." AND A.C_TRANSFERENCIA=".$this->c_transferencia;
      $query .= " AND A.RUT_CONTRAPARTE=X.RUN";
      $query .= " AND A.ANO=Z.ANO AND A.C_INSTITUCION=Z.C_INSTITUCION AND A.C_PREINVERSION=Z.C_PREINVERSION AND A.C_FICHA=Z.C_FICHA AND A.C_CONVENIO=Z.C_CONVENIO";
      $dataset  = mysql_query($query, $this->conector); 
      $record = mysql_fetch_row($dataset);
      $this->c_convenio=$record[0];
      $this->c_clasificador=$record[1];
      $this->n_clasificador=$record[2];
      $this->fecha=$record[3];
      $this->mes=$record[4];
      $this->detalle=$record[5];
      $this->valor_convenio=number_format($record[6],0,',','.');
      $this->transferencias_anteriores=number_format($record[7],0,',','.');
      $this->saldo_por_transferir=number_format($record[8],0,',','.');
      $this->valor_transferencia=number_format($record[9],0,',','.');
      $this->fecha_recepcion=$record[10];
      $this->presentado_por=$record[11];
      $this->tipo_documento=$record[12];
      $this->nro_documento=$record[13];
      $this->fecha_documento=$record[14];
      $this->nro_docu_pagar=$record[15];
      $this->fecha_docu_pagar=$record[16];      
      $this->c_estado=$record[17];
      $this->n_contraparte=$record[18].' / '.$record[20];
      $this->fecha_registro=$record[19];
   }

   function cargar_grilla_transferencia() {
      $datainforme=array();$filas=0;$this->filaseleccionada=-1;
      $query  = " SELECT A.C_TRANSFERENCIA, DATE_FORMAT(A.FECHA,'%d/%m/%Y'), B.N_MES, ";
      $query .= " IF (E.TIPO=1,CONCAT(E.NOMBRE,' (Rut:',E.RUN,')'),CONCAT(E.NOMBRE,' ',E.AP_PATERNO,' ',E.AP_MATERNO,' (Rut:',E.RUN,')')), ";
      $query .= " CONCAT(C.N_CLASIFICADOR,' (',C.C_CLASIFICADOR,')'), A.VALOR_TRANSFERENCIA, ";
      $query .= " IF(A.C_ESTADO=1,'Pendiente',IF(A.C_ESTADO=2,'En Visto Bueno',IF(A.C_ESTADO=3,'Para Devengar',IF(A.C_ESTADO=8,'Obs.Visto Bueno',IF(A.C_ESTADO=9,'Obs.DAF','Devengado'))))), A.C_ESTADO ";
      $query .= " FROM INVERSION_TRANSFERENCIA A, TMESES B, TCLASIFICADOR_PRESUPUESTARIO C, TCONTRATISTA E ";
      $query .= " WHERE A.MES=B.C_MES AND A.C_CLASIFICADOR=C.C_CLASIFICADOR AND A.ANO=C.ANIO AND (A.C_INSTITUCION=C.INSTITUCION OR C.INSTITUCION=0) AND A.RUT_CONTRAPARTE=E.RUN ";
      $query .= " AND A.ANO= ".$this->ano." AND A.C_PREINVERSION=".$this->c_preinversion;
      $query .= " AND A.C_INSTITUCION=".$this->c_institucion." AND A.C_FICHA=".$this->c_ficha." ORDER BY A.C_TRANSFERENCIA DESC";
      $dataset  = mysql_query($query, $this->conector); 
      $this->nroregistros = mysql_num_rows($dataset);
      $rows = array();
      while ($record = mysql_fetch_row($dataset)) { 
   $cols = array();
   foreach ($record as $value) { 
            $cols[] = '"'.addslashes($value).'"';
   } 
   $rows[] = "\t[".implode(",", $cols)."]"; 
      } 
      $datagrilla="[\n".implode(",\n",$rows)."\n];\n"; 
      return $datagrilla;
   }

   function calcular_valor_maximo() {
      $query  = " SELECT MAX(C_TRANSFERENCIA) FROM INVERSION_TRANSFERENCIA";
      $query .= " WHERE ANO= ".$this->ano;
/*      $query .= " WHERE ANO= ".$this->ano." AND C_INSTITUCION=".$this->c_institucion;
      $query .= " AND C_PREINVERSION=".$this->c_preinversion." AND C_FICHA=".$this->c_ficha;*/
      $dataset  = mysql_query($query, $this->conector); 
      $this->nroregistros = mysql_num_rows($dataset);
      $record = @mysql_fetch_row($dataset);
      return $record[0]+1;
   }

   function actualizar_transferencia() {
      $query  = " REPLACE INTO INVERSION_TRANSFERENCIA VALUES('";
      $query  .= $this->ano."','";  
      $query  .= $this->c_institucion."','";  
      $query  .= $this->c_preinversion."','";  
      $query  .= $this->c_ficha."','";  
      $query  .= $this->c_transferencia."','";  
      $query  .= $this->fecha."','";  
      $query  .= $this->mes."','";  
      $query  .= $this->c_convenio."','";  
      $query  .= $this->rut_contraparte."','";  
      $query  .= $this->c_clasificador."','";  
      $query  .= $this->detalle."','";  
      $query  .= $this->fecha_recepcion."','";  
      $query  .= $this->presentado_por."','";  
      $query  .= $this->tipo_documento."','";  
      $query  .= $this->nro_documento."','";  
      $query  .= $this->fecha_documento."','";  
      $query  .= $this->valor_convenio."','";  
      $query  .= $this->transferencias_anteriores."','";  
      $query  .= $this->saldo_por_transferir."','";  
      $query  .= $this->valor_transferencia."','";  
      $query  .= $this->nro_docu_pagar."','";  
      $query  .= $this->fecha_docu_pagar."','";  
      $query  .= $this->c_estado."','";  
      $query  .= $this->fecha_registro."','";  
      $query  .= $this->c_usuario."')";  
      echo $query;
      $dataset  = mysql_query($query, $this->conector);
      if (mysql_errno($this->conector) != 0) { 
         $this->mensaje_error="No se ha podido Actualizar la Base de Datos, mensaje de error de Mysql es: " . mysql_error($this->conector).$query; 
      } else {$this->mensaje_error='';}   
      return $this->mensaje_error;
   }
   
   function datos_firma($usuario,$cuantas_firmas) {
      $query  = " SELECT CONCAT(B.NOMBRES,' ',B.APELLIDO_PATERNO,' ',B.APELLIDO_MATERNO), D.N_PERFIL FROM ";
      $query .= " ADMIN_USUARIO B, ADMIN_USUARIO_PERFIL C, ADMIN_TPERFIL D ";
      $query .= " WHERE B.C_USUARIO=C.C_USUARIO AND C.C_PERFIL=D.C_PERFIL";
      $query .= " AND B.C_USUARIO=".$usuario;
      $dataset  = mysql_query($query, $this->conector);       
      $record = @mysql_fetch_row($dataset);
      $this->firma1=$record[0];
      $this->cargo1=$record[1];
      $query = " SELECT SUBROGA FROM INVERSION_TRANSFERENCIA_FIRMA ";
      $query .= " WHERE C_ORDEN=1";
      $query .= " AND ANO= ".$this->ano." AND C_PREINVERSION=".$this->c_preinversion;
      $query .= " AND C_INSTITUCION=".$this->c_institucion." AND C_FICHA=".$this->c_ficha." AND C_TRANSFERENCIA=".$this->c_transferencia;
      $dataset  = mysql_query($query, $this->conector);       
      $record = @mysql_fetch_row($dataset);
      $this->subroga1=$record[0];
      for ($i=2;$i<=$cuantas_firmas; $i++) {
         $query  = " SELECT B.C_USUARIO, CONCAT(B.NOMBRES,' ',B.APELLIDO_PATERNO,' ',B.APELLIDO_MATERNO), D.N_PERFIL, A.SUBROGA ";
         $query .= " FROM ADMIN_USUARIO B, ADMIN_USUARIO_PERFIL C, ADMIN_TPERFIL D, INVERSION_TRANSFERENCIA_FIRMA A ";
         $query .= " WHERE B.C_USUARIO=C.C_USUARIO AND C.C_PERFIL=D.C_PERFIL";
         $query .= " AND A.C_USUARIO=B.C_USUARIO AND A.C_ORDEN=".$i;
         $query .= " AND A.ANO= ".$this->ano." AND A.C_PREINVERSION=".$this->c_preinversion;
         $query .= " AND A.C_INSTITUCION=".$this->c_institucion." AND A.C_FICHA=".$this->c_ficha." AND A.C_TRANSFERENCIA=".$this->c_transferencia;
         $dataset  = mysql_query($query, $this->conector);       
         $record = @mysql_fetch_row($dataset);
         if ($i==2) {
            $this->ufirma2=$record[0];
            $this->firma2=$record[1];
            $this->cargo2=$record[2];
            $this->subroga2=$record[3];
         }   
         if ($i==3) {
            $this->ufirma3=$record[0];
            $this->firma3=$record[1];
            $this->cargo3=$record[2];
            $this->subroga3=$record[3];
         }   
         if ($i==4) {
            $this->ufirma4=$record[0];
            $this->firma4=$record[1];
            $this->cargo4=$record[2];
            $this->subroga4=$record[3];
         }   
         if ($i==5) {
            $this->ufirma5=$record[0];
            $this->firma5=$record[1];
            $this->cargo5=$record[2];
            $this->subroga5=$record[3];
         }   
      }   
      return 1;
   }

   function eliminar_firmas() {
      $query  = " DELETE FROM INVERSION_TRANSFERENCIA_FIRMA";
      $query .= " WHERE ANO= ".$this->ano." AND C_PREINVERSION=".$this->c_preinversion;
      $query .= " AND C_INSTITUCION=".$this->c_institucion." AND C_FICHA=".$this->c_ficha." AND C_TRANSFERENCIA=".$this->c_transferencia;
      $dataset  = mysql_query($query, $this->conector); 
      if (mysql_errno($this->conector) != 0) { 
         $this->mensaje_error="No se ha podido Actualizar la Base de Datos, mensaje de error de Mysql es: " . mysql_error($this->conector).$query; 
      } else {$this->mensaje_error='';}   
      return $this->mensaje_error;
   }
      
   function insertar_firma($op,$usuario,$subroga) {
      $query  = " INSERT INTO INVERSION_TRANSFERENCIA_FIRMA VALUE('";
      $query .= $this->ano."','".$this->c_institucion."','".$this->c_preinversion."','".$this->c_ficha."','";
      $query .= $this->c_transferencia."','".$op."','T','".$usuario."','".$subroga."')";
      $dataset  = mysql_query($query, $this->conector); 
      if (mysql_errno($this->conector) != 0) { 
         $this->mensaje_error="No se ha podido Actualizar la Base de Datos, mensaje de error de Mysql es: " . mysql_error($this->conector).$query; 
      } else {$this->mensaje_error='';}   
      return $this->mensaje_error;
   }

   function calcular_valor_maximo_observacion() {
      $query  = " SELECT MAX(C_OBSERVACION) FROM INVERSION_TRANSFERENCIA_OBSERVACIONES";
      $query .= " WHERE ANO= ".$this->ano." AND C_INSTITUCION=".$this->c_institucion;
      $query .= " AND C_PREINVERSION=".$this->c_preinversion." AND C_FICHA=".$this->c_ficha." AND C_TRANSFERENCIA=".$this->c_transferencia;
      $dataset  = mysql_query($query, $this->conector); 
      $this->nroregistros = mysql_num_rows($dataset);
      $record = @mysql_fetch_row($dataset);
      return $record[0]+1;
   }

   function observar_transferencia($xcobservacion,$xtipo,$xfecha,$xobservacion) {
      $query  = " INSERT INTO INVERSION_TRANSFERENCIA_OBSERVACIONES VALUE('";
      $query .= $this->ano."','".$this->c_institucion."','".$this->c_preinversion."','".$this->c_ficha."','";
      $query .= $this->c_transferencia."','".$xcobservacion."','".$xtipo."','".$xfecha."','".$xobservacion."','".$this->fecha_registro."','".$this->c_usuario."')";
      $dataset  = mysql_query($query, $this->conector); 
      if (mysql_errno($this->conector) != 0) { 
         $this->mensaje_error="No se ha podido Actualizar la Base de Datos, mensaje de error de Mysql es: " . mysql_error($this->conector).$query; 
      } else {$this->mensaje_error='';}   
      return $this->mensaje_error;
   }

   function actualizar_transferencia_estado() {
      $query  = " UPDATE INVERSION_TRANSFERENCIA SET C_ESTADO=".$this->c_estado; 
      $query .= " WHERE ANO= ".$this->ano." AND C_PREINVERSION=".$this->c_preinversion;
      $query .= " AND C_INSTITUCION=".$this->c_institucion." AND C_FICHA=".$this->c_ficha." AND C_TRANSFERENCIA=".$this->c_transferencia;
      $dataset  = mysql_query($query, $this->conector); 
      if (mysql_errno($this->conector) != 0) { 
         $this->mensaje_error="No se ha podido Actualizar la Base de Datos, mensaje de error de Mysql es: " . mysql_error($this->conector).$query; 
      } else {$this->mensaje_error='';}   
      return $this->mensaje_error;
   }

   function devengar_transferencia($xfecha,$xcomprobante,$xobservacion) {
      $query  = " INSERT INTO INVERSION_TRANSFERENCIA_DEVENGO VALUE('";
      $query .= $this->ano."','".$this->c_institucion."','".$this->c_preinversion."','".$this->c_ficha."','";
      $query .= $this->c_transferencia."','".$xfecha."','".$xcomprobante."','".$xobservacion."','".$this->fecha_registro."','".$this->c_usuario."')";
      echo $query;
      $dataset  = mysql_query($query, $this->conector); 
      if (mysql_errno($this->conector) != 0) { 
         $this->mensaje_error="No se ha podido Actualizar la Base de Datos, mensaje de error de Mysql es: " . mysql_error($this->conector).$query; 
      } else {$this->mensaje_error='';}   
      return $this->mensaje_error;
   }

   function actualizar_transferencia_montos_y_fuentes($xmonto,$xmes, $xclasificador) {
      $query  = " UPDATE INVERSION_FINANCIAMIENTO SET G".$xmes."= (G".$xmes."+".$xmonto.")";
      $query .= " WHERE C_FICHA=".$this->c_ficha." AND C_PREINVERSION=".$this->c_preinversion." AND ";
      $query .= " C_INSTITUCION=".$this->c_institucion." AND ANO=".$this->ano;
      $query .= " AND (C_FUENTE_FINANCIAMIENTO LIKE '03%' OR C_FUENTE_FINANCIAMIENTO LIKE '02%') AND C_CLASIFICADOR_PRESUPUESTARIO='".$xclasificador."'";
      $dataset  = mysql_query($query, $this->conector); 
      $query  = " UPDATE INVERSION SET G".$xmes."= (G.".$xmes."+".$xmonto.")";
      $query .= " WHERE C_FICHA=".$this->c_ficha." AND C_PREINVERSION=".$this->c_preinversion." AND ";
      $query .= " C_INSTITUCION=".$this->c_institucion." AND ANO=".$this->ano;
      $dataset  = mysql_query($query, $this->conector); 
      if (mysql_errno($this->conector) != 0) { 
         $this->mensaje_error="No se ha podido Actualizar la Base de Datos, mensaje de error de Mysql es: " . mysql_error($this->conector).$query; 
      } else {$this->mensaje_error='';}   
      return $this->mensaje_error;      
   }  

   function eliminar_transferencia() {
      $query  = " DELETE FROM INVERSION_TRANSFERENCIA ";
      $query .= " WHERE ANO= ".$this->ano." AND C_PREINVERSION=".$this->c_preinversion;
      $query .= " AND C_INSTITUCION=".$this->c_institucion." AND C_FICHA=".$this->c_ficha." AND C_TRANSFERENCIA=".$this->c_transferencia;
      $dataset  = mysql_query($query, $this->conector); 
      $query  = " DELETE FROM INVERSION_TRANSFERENCIA_DEVENGO ";
      $query .= " WHERE ANO= ".$this->ano." AND C_PREINVERSION=".$this->c_preinversion;
      $query .= " AND C_INSTITUCION=".$this->c_institucion." AND C_FICHA=".$this->c_ficha." AND C_TRANSFERENCIA=".$this->c_transferencia;
      $dataset  = mysql_query($query, $this->conector); 
      $query  = " DELETE FROM INVERSION_TRANSFERENCIA_FIRMA ";
      $query .= " WHERE ANO= ".$this->ano." AND C_PREINVERSION=".$this->c_preinversion;
      $query .= " AND C_INSTITUCION=".$this->c_institucion." AND C_FICHA=".$this->c_ficha." AND C_TRANSFERENCIA=".$this->c_transferencia;
      $dataset  = mysql_query($query, $this->conector); 
      $query  = " DELETE FROM INVERSION_TRANSFERENCIA_OBSERVACIONES ";
      $query .= " WHERE ANO= ".$this->ano." AND C_PREINVERSION=".$this->c_preinversion;
      $query .= " AND C_INSTITUCION=".$this->c_institucion." AND C_FICHA=".$this->c_ficha." AND C_TRANSFERENCIA=".$this->c_transferencia;
      $dataset  = mysql_query($query, $this->conector);          
      if (mysql_errno($this->conector) != 0) { 
         $this->mensaje_error="No se ha podido Actualizar la Base de Datos, mensaje de error de Mysql es: " . mysql_error($this->conector).$query; 
      } else {$this->mensaje_error='';}   
      return $this->mensaje_error;
   }

   function get_nroregistros(){ return $this->nroregistros;}  
   
   function get_error()       { return $this->mensaje_error;}
      
   function get_filaseleccionada()       { return $this->filaseleccionada;}
}

//********************************************
// GASTO ADMINISTRATIVO INVERSION
//********************************************   
Class Inversion_Gasto_Administrativo {
   var $conector;
   var $mensaje_error;
   var $nroregistros;
   var $ano;  
   var $c_institucion;
   var $c_preinversion;
   var $c_ficha;
   var $c_gasto;
   var $fecha;
   var $mes;
   var $c_convenio;
   var $c_clasificador="";
   var $n_clasificador="";
   var $detalle="";
   var $fecha_recepcion;
   var $presentado_por="";
   var $tipo_documento;
   var $nro_documento="";
   var $fecha_documento;
   var $solicitado_ano;
   var $gastos_anteriores;
   var $saldo;
   var $valor_gasto;
   var $nro_docu_pagar="";
   var $fecha_docu_pagar;
   var $c_estado;
   var $fecha_registro;
   var $c_usuario;
   var $n_contraparte="";
   var $firma1="";
   var $firma2="";
   var $firma3="";
   var $firma4="";
   var $firma5="";
   var $cargo1="";
   var $cargo2="";
   var $cargo3="";
   var $cargo4="";
   var $cargo5="";
   var $ufirma2;
   var $ufirma3;
   var $ufirma4;
   var $ufirma5;
   
   function Inversion_Gasto_Administrativo($conector) { 
      $this->conector =$conector;
      $this->mensaje_error='';
      $this->nroregistros=0;
   }
    
   function cuantas_firmas() {
      $query = "SELECT MAX(C_FIRMA) FROM INVERSION_PAGO_FIRMAS_PERFIL";
      $dataset  = mysql_query($query, $this->conector); 
      $record = mysql_fetch_row($dataset);
      return $record[0];
   }
   
   function perfiles_firmas($firma) {
      $query = "SELECT C_PERFIL FROM INVERSION_PAGO_FIRMAS_USUARIOS WHERE C_FIRMA=".$firma;
      $dataset  = mysql_query($query, $this->conector); 
      $record = mysql_fetch_row($dataset);
      return $record[0];
   }

   function perfil_firma($firma) {
      $query  = " SELECT A.N_PERFIL FROM ADMIN_TPERFIL A, INVERSION_PAGO_FIRMAS_PERFIL B ";
      $query .= " WHERE A.C_PERFIL=B.C_PERFIL AND B.C_FIRMA=".$firma;
      $dataset  = mysql_query($query, $this->conector); 
      $record = mysql_fetch_row($dataset);
      return $record[0];
   }

   function tiene_observaciones() {
      $query  = " SELECT COUNT(*) ";
      $query .= " FROM INVERSION_GASTO_ADMINISTRATIVO_OBSERVACIONES ";
      $query .= " WHERE ANO= ".$this->ano." AND C_PREINVERSION=".$this->c_preinversion;
      $query .= " AND C_INSTITUCION=".$this->c_institucion." AND C_FICHA=".$this->c_ficha." AND C_GASTO=".$this->c_gasto;
      $dataset  = mysql_query($query, $this->conector); 
      $record = mysql_fetch_row($dataset);
      return $record[0];
   }

   function traer_datos_gasto_administrativo() {
      $query  = " SELECT A.C_CONVENIO, A.C_CLASIFICADOR, CONCAT(B.N_CLASIFICADOR,' (',B.C_CLASIFICADOR,')') ";
      $query .= ", DATE_FORMAT(A.FECHA,'%d/%m/%Y'), A.MES, A.DETALLE ";
      $query .= ", A.SOLICITADO_ANO, A.GASTOS_ANTERIORES, A.SALDO, A.VALOR_GASTO ";
      $query .= ", DATE_FORMAT(A.FECHA_RECEPCION,'%d/%m/%Y'), A.PRESENTADO_POR, A.TIPO_DOCUMENTO, A.NRO_DOCUMENTO,DATE_FORMAT(A.FECHA_DOCUMENTO,'%d/%m/%Y') ";
      $query .= ", A.NRO_DOCU_PAGAR, DATE_FORMAT(A.FECHA_DOCU_PAGAR,'%d/%m/%Y'), A.C_ESTADO ";
      $query .= ", CONCAT('(',X.RUN,'-',X.DV,')   ',X.NOMBRE,' ',X.AP_PATERNO,' ',X.AP_MATERNO), DATE_FORMAT(A.FECHA_REGISTRO,'%d/%m/%Y')";
      $query .= " FROM TCONTRATISTA X, INVERSION_CONVENIOS Z, INVERSION_GASTO_ADMINISTRATIVO A";
      $query .= " LEFT JOIN TCLASIFICADOR_PRESUPUESTARIO B ON (A.ANO=B.ANIO AND A.C_CLASIFICADOR=B.C_CLASIFICADOR AND (A.C_INSTITUCION=B.INSTITUCION OR B.INSTITUCION=0)) ";
      $query .= " WHERE A.ANO= ".$this->ano." AND A.C_PREINVERSION=".$this->c_preinversion;
      $query .= " AND A.C_INSTITUCION=".$this->c_institucion." AND A.C_FICHA=".$this->c_ficha." AND A.C_GASTO=".$this->c_gasto;
      $query .= " AND A.ANO=Z.ANO AND A.C_INSTITUCION=Z.C_INSTITUCION AND A.C_PREINVERSION=Z.C_PREINVERSION AND A.C_FICHA=Z.C_FICHA AND A.C_CONVENIO=Z.C_CONVENIO ";
      $query .= " AND Z.RUT=X.RUN";
      $dataset  = mysql_query($query, $this->conector); 
      $record = mysql_fetch_row($dataset);
      $this->c_convenio=$record[0];
      $this->c_clasificador=$record[1];
      $this->n_clasificador=$record[2];
      $this->fecha=$record[3];
      $this->mes=$record[4];
      $this->detalle=$record[5];
      $this->solicitado_ano=number_format($record[6],0,',','.');
      $this->gastos_anteriores=number_format($record[7],0,',','.');
      $this->saldo=number_format($record[8],0,',','.');
      $this->valor_gasto=number_format($record[9],0,',','.');
      $this->fecha_recepcion=$record[10];
      $this->presentado_por=$record[11];
      $this->tipo_documento=$record[12];
      $this->nro_documento=$record[13];
      $this->fecha_documento=$record[14];
      $this->nro_docu_pagar=$record[15];
      $this->fecha_docu_pagar=$record[16];      
      $this->c_estado=$record[17];
      $this->n_contraparte=$record[18];
      $this->fecha_registro=$record[19];
   }

   function cargar_grilla_gasto_administrativo() {
      $datainforme=array();$filas=0;$this->filaseleccionada=-1;
      $query  = " SELECT A.C_GASTO, DATE_FORMAT(A.FECHA,'%d/%m/%Y'), B.N_MES, ";
      $query .= " IF (E.TIPO=1,CONCAT(E.NOMBRE,' (Rut:',E.RUN,')'),CONCAT(E.NOMBRE,' ',E.AP_PATERNO,' ',E.AP_MATERNO,' (Rut:',E.RUN,')')), ";
      $query .= " CONCAT(C.N_CLASIFICADOR,' (',C.C_CLASIFICADOR,')'), A.VALOR_GASTO, ";
      $query .= " IF(A.C_ESTADO=1,'Pendiente',IF(A.C_ESTADO=2,'En Visto Bueno',IF(A.C_ESTADO=3,'Para Devengar',IF(A.C_ESTADO=8,'Obs.Visto Bueno',IF(A.C_ESTADO=9,'Obs.DAF','Devengado'))))), A.C_ESTADO ";
      $query .= " FROM INVERSION_GASTO_ADMINISTRATIVO A, TMESES B, TCLASIFICADOR_PRESUPUESTARIO C, TCONTRATISTA E, INVERSION_CONVENIOS Z ";
      $query .= " WHERE A.MES=B.C_MES AND A.C_CLASIFICADOR=C.C_CLASIFICADOR AND A.ANO=C.ANIO AND (A.C_INSTITUCION=C.INSTITUCION OR C.INSTITUCION=0) AND (A.C_INSTITUCION=C.INSTITUCION OR C.INSTITUCION=0) ";
      $query .= " AND A.ANO= ".$this->ano." AND A.C_PREINVERSION=".$this->c_preinversion;
      $query .= " AND A.C_INSTITUCION=".$this->c_institucion." AND A.C_FICHA=".$this->c_ficha;
      $query .= " AND A.ANO=Z.ANO AND A.C_INSTITUCION=Z.C_INSTITUCION AND A.C_PREINVERSION=Z.C_PREINVERSION AND A.C_FICHA=Z.C_FICHA AND A.C_CONVENIO=Z.C_CONVENIO ";
      $query .= " AND Z.RUT=E.RUN";
      $query .= " ORDER BY A.C_GASTO DESC";
      $dataset  = mysql_query($query, $this->conector); 
      $this->nroregistros = mysql_num_rows($dataset);
      $rows = array();
      while ($record = mysql_fetch_row($dataset)) { 
   $cols = array();
   foreach ($record as $value) { 
            $cols[] = '"'.addslashes($value).'"';
   } 
   $rows[] = "\t[".implode(",", $cols)."]"; 
      } 
      $datagrilla="[\n".implode(",\n",$rows)."\n];\n"; 
      return $datagrilla;
   }

   function calcular_valor_maximo() {
      $query  = " SELECT MAX(C_GASTO) FROM INVERSION_GASTO_ADMINISTRATIVO";
      $query .= " WHERE ANO= ".$this->ano;
/*      $query .= " WHERE ANO= ".$this->ano." AND C_INSTITUCION=".$this->c_institucion;
      $query .= " AND C_PREINVERSION=".$this->c_preinversion." AND C_FICHA=".$this->c_ficha;*/
      $dataset  = mysql_query($query, $this->conector); 
      $this->nroregistros = mysql_num_rows($dataset);
      $record = @mysql_fetch_row($dataset);
      return $record[0]+1;
   }

   function actualizar_gasto_administrativo() {
      $query  = " REPLACE INTO INVERSION_GASTO_ADMINISTRATIVO VALUES('";
      $query  .= $this->ano."','";  
      $query  .= $this->c_institucion."','";  
      $query  .= $this->c_preinversion."','";  
      $query  .= $this->c_ficha."','";  
      $query  .= $this->c_gasto."','";  
      $query  .= $this->fecha."','";  
      $query  .= $this->mes."','";  
      $query  .= $this->c_convenio."','";  
      $query  .= $this->c_clasificador."','";  
      $query  .= $this->detalle."','";  
      $query  .= $this->fecha_recepcion."','";  
      $query  .= $this->presentado_por."','";  
      $query  .= $this->tipo_documento."','";  
      $query  .= $this->nro_documento."','";  
      $query  .= $this->fecha_documento."','";  
      $query  .= $this->solicitado_ano."','";  
      $query  .= $this->gastos_anteriores."','";  
      $query  .= $this->saldo."','";  
      $query  .= $this->valor_gasto."','";  
      $query  .= $this->nro_docu_pagar."','";  
      $query  .= $this->fecha_docu_pagar."','";  
      $query  .= $this->c_estado."','";  
      $query  .= $this->fecha_registro."','";  
      $query  .= $this->c_usuario."')";  
      echo $query;
      $dataset  = mysql_query($query, $this->conector);
      if (mysql_errno($this->conector) != 0) { 
         $this->mensaje_error="No se ha podido Actualizar la Base de Datos, mensaje de error de Mysql es: " . mysql_error($this->conector).$query; 
      } else {$this->mensaje_error='';}   
      return $this->mensaje_error;
   }
   
   function datos_firma($usuario,$cuantas_firmas) {
      $query  = " SELECT CONCAT(B.NOMBRES,' ',B.APELLIDO_PATERNO,' ',B.APELLIDO_MATERNO), D.N_PERFIL FROM ";
      $query .= " ADMIN_USUARIO B, ADMIN_USUARIO_PERFIL C, ADMIN_TPERFIL D ";
      $query .= " WHERE B.C_USUARIO=C.C_USUARIO AND C.C_PERFIL=D.C_PERFIL";
      $query .= " AND B.C_USUARIO=".$usuario;
      $dataset  = mysql_query($query, $this->conector);       
      $record = @mysql_fetch_row($dataset);
      $this->firma1=$record[0];
      $this->cargo1=$record[1];
      $query = " SELECT SUBROGA FROM INVERSION_GASTO_ADMINISTRATIVO_FIRMA ";
      $query .= " WHERE C_ORDEN=1";
      $query .= " AND ANO= ".$this->ano." AND C_PREINVERSION=".$this->c_preinversion;
      $query .= " AND C_INSTITUCION=".$this->c_institucion." AND C_FICHA=".$this->c_ficha." AND C_GASTO=".$this->c_gasto;
      $dataset  = mysql_query($query, $this->conector);       
      $record = @mysql_fetch_row($dataset);
      $this->subroga1=$record[0];
      for ($i=2;$i<=$cuantas_firmas; $i++) {
         $query  = " SELECT B.C_USUARIO, CONCAT(B.NOMBRES,' ',B.APELLIDO_PATERNO,' ',B.APELLIDO_MATERNO), D.N_PERFIL, A.SUBROGA ";
         $query .= " FROM ADMIN_USUARIO B, ADMIN_USUARIO_PERFIL C, ADMIN_TPERFIL D, INVERSION_GASTO_ADMINISTRATIVO_FIRMA A ";
         $query .= " WHERE B.C_USUARIO=C.C_USUARIO AND C.C_PERFIL=D.C_PERFIL";
         $query .= " AND A.C_USUARIO=B.C_USUARIO AND A.C_ORDEN=".$i;
         $query .= " AND A.ANO= ".$this->ano." AND A.C_PREINVERSION=".$this->c_preinversion;
         $query .= " AND A.C_INSTITUCION=".$this->c_institucion." AND A.C_FICHA=".$this->c_ficha." AND A.C_GASTO=".$this->c_gasto;
         $dataset  = mysql_query($query, $this->conector);       
         $record = @mysql_fetch_row($dataset);
         if ($i==2) {
            $this->ufirma2=$record[0];
            $this->firma2=$record[1];
            $this->cargo2=$record[2];
            $this->subroga2=$record[3];
         }   
         if ($i==3) {
            $this->ufirma3=$record[0];
            $this->firma3=$record[1];
            $this->cargo3=$record[2];
            $this->subroga3=$record[3];
         }   
         if ($i==4) {
            $this->ufirma4=$record[0];
            $this->firma4=$record[1];
            $this->cargo4=$record[2];
            $this->subroga4=$record[3];
         }   
         if ($i==5) {
            $this->ufirma5=$record[0];
            $this->firma5=$record[1];
            $this->cargo5=$record[2];
            $this->subroga5=$record[3];
         }   
      }   
      return 1;
   }

   function eliminar_firmas() {
      $query  = " DELETE FROM INVERSION_GASTO_ADMINISTRATIVO_FIRMA";
      $query .= " WHERE ANO= ".$this->ano." AND C_PREINVERSION=".$this->c_preinversion;
      $query .= " AND C_INSTITUCION=".$this->c_institucion." AND C_FICHA=".$this->c_ficha." AND C_GASTO=".$this->c_gasto;
      $dataset  = mysql_query($query, $this->conector); 
      if (mysql_errno($this->conector) != 0) { 
         $this->mensaje_error="No se ha podido Actualizar la Base de Datos, mensaje de error de Mysql es: " . mysql_error($this->conector).$query; 
      } else {$this->mensaje_error='';}   
      return $this->mensaje_error;
   }
      
   function insertar_firma($op,$usuario,$subroga) {
      $query  = " INSERT INTO INVERSION_GASTO_ADMINISTRATIVO_FIRMA VALUE('";
      $query .= $this->ano."','".$this->c_institucion."','".$this->c_preinversion."','".$this->c_ficha."','";
      $query .= $this->c_gasto."','".$op."','T','".$usuario."','".$subroga."')";
      $dataset  = mysql_query($query, $this->conector); 
      if (mysql_errno($this->conector) != 0) { 
         $this->mensaje_error="No se ha podido Actualizar la Base de Datos, mensaje de error de Mysql es: " . mysql_error($this->conector).$query; 
      } else {$this->mensaje_error='';}   
      return $this->mensaje_error;
   }

   function calcular_valor_maximo_observacion() {
      $query  = " SELECT MAX(C_OBSERVACION) FROM INVERSION_GASTO_ADMINISTRATIVO_OBSERVACIONES";
      $query .= " WHERE ANO= ".$this->ano." AND C_INSTITUCION=".$this->c_institucion;
      $query .= " AND C_PREINVERSION=".$this->c_preinversion." AND C_FICHA=".$this->c_ficha." AND C_GASTO=".$this->c_gasto;
      $dataset  = mysql_query($query, $this->conector); 
      $this->nroregistros = mysql_num_rows($dataset);
      $record = @mysql_fetch_row($dataset);
      return $record[0]+1;
   }

   function observar_gasto_administrativo($xcobservacion,$xtipo,$xfecha,$xobservacion) {
      $query  = " INSERT INTO INVERSION_GASTO_ADMINISTRATIVO_OBSERVACIONES VALUE('";
      $query .= $this->ano."','".$this->c_institucion."','".$this->c_preinversion."','".$this->c_ficha."','";
      $query .= $this->c_gasto."','".$xcobservacion."','".$xtipo."','".$xfecha."','".$xobservacion."','".$this->fecha_registro."','".$this->c_usuario."')";
      $dataset  = mysql_query($query, $this->conector); 
      if (mysql_errno($this->conector) != 0) { 
         $this->mensaje_error="No se ha podido Actualizar la Base de Datos, mensaje de error de Mysql es: " . mysql_error($this->conector).$query; 
      } else {$this->mensaje_error='';}   
      return $this->mensaje_error;
   }

   function actualizar_gasto_administrativo_estado() {
      $query  = " UPDATE INVERSION_GASTO_ADMINISTRATIVO SET C_ESTADO=".$this->c_estado; 
      $query .= " WHERE ANO= ".$this->ano." AND C_PREINVERSION=".$this->c_preinversion;
      $query .= " AND C_INSTITUCION=".$this->c_institucion." AND C_FICHA=".$this->c_ficha." AND C_GASTO=".$this->c_gasto;
      $dataset  = mysql_query($query, $this->conector); 
      if (mysql_errno($this->conector) != 0) { 
         $this->mensaje_error="No se ha podido Actualizar la Base de Datos, mensaje de error de Mysql es: " . mysql_error($this->conector).$query; 
      } else {$this->mensaje_error='';}   
      return $this->mensaje_error;
   }

   function devengar_gasto_administrativo($xfecha,$xcomprobante,$xobservacion) {
      $query  = " INSERT INTO INVERSION_GASTO_ADMINISTRATIVO_DEVENGO VALUE('";
      $query .= $this->ano."','".$this->c_institucion."','".$this->c_preinversion."','".$this->c_ficha."','";
      $query .= $this->c_gasto."','".$xfecha."','".$xcomprobante."','".$xobservacion."','".$this->fecha_registro."','".$this->c_usuario."')";
      $dataset  = mysql_query($query, $this->conector); 
      if (mysql_errno($this->conector) != 0) { 
         $this->mensaje_error="No se ha podido Actualizar la Base de Datos, mensaje de error de Mysql es: " . mysql_error($this->conector).$query; 
      } else {$this->mensaje_error='';}   
      return $this->mensaje_error;
   }

   function actualizar_gasto_administrativo_montos_y_fuentes($xmonto,$xmes, $xclasificador) {
      $query  = " UPDATE INVERSION_FINANCIAMIENTO SET G".$xmes."= (G".$xmes."+".$xmonto.")";
      $query .= " WHERE C_FICHA=".$this->c_ficha." AND C_PREINVERSION=".$this->c_preinversion." AND ";
      $query .= " C_INSTITUCION=".$this->c_institucion." AND ANO=".$this->ano;
      $query .= " AND (C_FUENTE_FINANCIAMIENTO LIKE '03%' OR C_FUENTE_FINANCIAMIENTO LIKE '02%') AND C_CLASIFICADOR_PRESUPUESTARIO='".$xclasificador."'";
      $dataset  = mysql_query($query, $this->conector); 
      $query  = " UPDATE INVERSION SET G".$xmes."= (G.".$xmes."+".$xmonto.")";
      $query .= " WHERE C_FICHA=".$this->c_ficha." AND C_PREINVERSION=".$this->c_preinversion." AND ";
      $query .= " C_INSTITUCION=".$this->c_institucion." AND ANO=".$this->ano;
      $dataset  = mysql_query($query, $this->conector); 
      if (mysql_errno($this->conector) != 0) { 
         $this->mensaje_error="No se ha podido Actualizar la Base de Datos, mensaje de error de Mysql es: " . mysql_error($this->conector).$query; 
      } else {$this->mensaje_error='';}   
      return $this->mensaje_error;      
   }  

   function recuperar_informacion_gasto_administrativo() {
      $query  = " SELECT SOLICITADO FROM INVERSION_FINANCIAMIENTO ";
      $query .= " WHERE ANO= ".$this->ano." AND C_INSTITUCION=".$this->c_institucion;
      $query .= " AND C_PREINVERSION=".$this->c_preinversion." AND C_FICHA=".$this->c_ficha;
      $query .= " AND C_CLASIFICADOR_PRESUPUESTARIO IN ('31.01.001','31.02.001','31.03.001')";
      $dataset  = mysql_query($query, $this->conector); 
      $record = mysql_fetch_row($dataset);
      return $record;
   }

   function recuperar_gastos_anteriores() {
      $query  = " SELECT SUM(VALOR_GASTO) FROM INVERSION_GASTO_ADMINISTRATIVO ";
      $query .= " WHERE ANO= ".$this->ano." AND C_INSTITUCION=".$this->c_institucion;
      $query .= " AND C_PREINVERSION=".$this->c_preinversion." AND C_FICHA=".$this->c_ficha;
      $query .= " AND C_GASTO<>".$this->nestadogasto;
      $query .= " AND C_ESTADO=4";
      $dataset  = mysql_query($query, $this->conector); 
      $record = mysql_fetch_row($dataset);
      return $record;
   }

   function recuperar_gastos_anteriores_no_devengados() {
      $query  = " SELECT SUM(VALOR_GASTO) FROM INVERSION_GASTO_ADMINISTRATIVO ";
      $query .= " WHERE ANO= ".$this->ano." AND C_INSTITUCION=".$this->c_institucion;
      $query .= " AND C_PREINVERSION=".$this->c_preinversion." AND C_FICHA=".$this->c_ficha;
      $query .= " AND C_GASTO<>".$this->nestadogasto;
      $query .= " AND C_ESTADO IN (2,3,9)";
      echo $query;
      $dataset  = mysql_query($query, $this->conector); 
      $record = mysql_fetch_row($dataset);
      return $record;
   }

   function eliminar_gasto_administrativo() {
      $query  = " DELETE FROM INVERSION_GASTO_ADMINISTRATIVO ";
      $query .= " WHERE ANO= ".$this->ano." AND C_PREINVERSION=".$this->c_preinversion;
      $query .= " AND C_INSTITUCION=".$this->c_institucion." AND C_FICHA=".$this->c_ficha." AND C_GASTO=".$this->c_gasto;
      $dataset  = mysql_query($query, $this->conector); 
      $query  = " DELETE FROM INVERSION_GASTO_ADMINISTRATIVO_DEVENGO ";
      $query .= " WHERE ANO= ".$this->ano." AND C_PREINVERSION=".$this->c_preinversion;
      $query .= " AND C_INSTITUCION=".$this->c_institucion." AND C_FICHA=".$this->c_ficha." AND C_GASTO=".$this->c_gasto;
      $dataset  = mysql_query($query, $this->conector); 
      $query  = " DELETE FROM INVERSION_GASTO_ADMINISTRATIVO_FIRMA ";
      $query .= " WHERE ANO= ".$this->ano." AND C_PREINVERSION=".$this->c_preinversion;
      $query .= " AND C_INSTITUCION=".$this->c_institucion." AND C_FICHA=".$this->c_ficha." AND C_GASTO=".$this->c_gasto;
      $dataset  = mysql_query($query, $this->conector); 
      $query  = " DELETE FROM INVERSION_GASTO_ADMINISTRATIVO_OBSERVACIONES ";
      $query .= " WHERE ANO= ".$this->ano." AND C_PREINVERSION=".$this->c_preinversion;
      $query .= " AND C_INSTITUCION=".$this->c_institucion." AND C_FICHA=".$this->c_ficha." AND C_GASTO=".$this->c_gasto;
      $dataset  = mysql_query($query, $this->conector);          
      if (mysql_errno($this->conector) != 0) { 
         $this->mensaje_error="No se ha podido Actualizar la Base de Datos, mensaje de error de Mysql es: " . mysql_error($this->conector).$query; 
      } else {$this->mensaje_error='';}   
      return $this->mensaje_error;
   }

   function get_nroregistros(){ return $this->nroregistros;}  
   
   function get_error()       { return $this->mensaje_error;}
      
   function get_filaseleccionada()       { return $this->filaseleccionada;}
}

//********************************************
// REQUERIMIENTO INVERSION
//********************************************   
Class Inversion_Requerimiento {
   var $conector;
   var $mensaje_error;
   var $nroregistros;
   var $ano;  
   var $c_institucion;
   var $c_preinversion;
   var $c_ficha;
   var $c_requerimiento;
   var $fecha;
   var $titulo="";
   var $c_unidad_demandante="";
   var $c_unidad_monetaria;
   var $fecha_inicial;
   var $fecha_critica;
   var $fecha_termino;
   var $c_prioridad;
   var $c_origen_demanda;
   var $cantidad;
   var $c_unidad_medida;
   var $c_factor_avance;
   var $descripcion="";
   var $c_comuna;
   var $n_comuna="";
   var $c_programa;
   var $n_programa="";
   var $c_estado;
   var $fecha_registro;
   var $c_usuario;
   var $id_sigfe="";
   var $codigo_sigfe="";
   var $codigo1;
   var $codigo2;
   var $monto1;
   var $monto2;
   var $monto3;
   var $monto4;
   var $monto5;
   var $monto6;
   var $conanticipo;
   var $c_clasificador="";
   var $n_clasificador="";
   var $c_clasificador1="";
   var $n_clasificador1="";
   var $n_rut;
   
   function Inversion_Requerimiento($conector) { 
      $this->conector =$conector;
      $this->mensaje_error='';
      $this->nroregistros=0;
   }
      
   function calcular_valor_maximo($xmes) {
      $query  = " SELECT MAX(C_REQUERIMIENTO) FROM INVERSION_REQUERIMIENTO";
      $query .= " WHERE ANO= ".$this->ano;
      $dataset  = mysql_query($query, $this->conector); 
      $this->nroregistros = mysql_num_rows($dataset);
      $record = @mysql_fetch_row($dataset);
      $retorno=$record[0]+1;
      return $retorno;
   }

   function eliminar_montos() {
      $query  = " DELETE FROM INVERSION_REQUERIMIENTO_MONTOS ";
      $query .= " WHERE ANO= ".$this->ano." AND C_PREINVERSION=".$this->c_preinversion;
      $query .= " AND C_INSTITUCION=".$this->c_institucion." AND C_FICHA=".$this->c_ficha." AND C_REQUERIMIENTO=".$this->c_requerimiento;
      $dataset  = mysql_query($query, $this->conector); 
   }

   function insertar_monto($xv1,$xv2,$xv3,$xv4,$xv5,$xv6,$xv7,$xv8) {
      $query  = " INSERT INTO INVERSION_REQUERIMIENTO_MONTOS VALUES('";
      $query .= $this->ano."','".$this->c_institucion."','".$this->c_preinversion."','".$this->c_ficha."','".$this->c_requerimiento."','";
      $query .= $xv1."','";
      $query .= $xv2."','";
      $query .= ereg_replace("[^-/0-9]", "", $xv3)."','";
      $query .= ereg_replace("[^-/0-9]", "", $xv4)."','";
      $query .= ereg_replace("[^-/0-9]", "", $xv5)."','";
      $query .= ereg_replace("[^-/0-9]", "", $xv6)."','";
      $query .= ereg_replace("[^-/0-9]", "", $xv7)."','";
      $query .= ereg_replace("[^-/0-9]", "", $xv8)."')";
      $dataset  = mysql_query($query, $this->conector); 
   }

   function tiene_observaciones() {
      $query  = " SELECT COUNT(*) ";
      $query .= " FROM INVERSION_REQUERIMIENTO_OBSERVACIONES ";
      $query .= " WHERE ANO= ".$this->ano." AND C_PREINVERSION=".$this->c_preinversion;
      $query .= " AND C_INSTITUCION=".$this->c_institucion." AND C_FICHA=".$this->c_ficha." AND C_REQUERIMIENTO=".$this->c_requerimiento;
      $dataset  = mysql_query($query, $this->conector); 
      $record = mysql_fetch_row($dataset);
      return $record[0];
   }

   function calcular_inicial() {
      $query  = " SELECT A.C_REQUERIMIENTO";
      $query .= " FROM INVERSION_REQUERIMIENTO A ";
      $query .= " WHERE A.ANO= ".$this->ano." AND A.C_PREINVERSION=".$this->c_preinversion;
      $query .= " AND A.C_INSTITUCION=".$this->c_institucion." AND A.C_FICHA=".$this->c_ficha;
      $query .= " ORDER BY A.C_REQUERIMIENTO ";
      $dataset  = mysql_query($query, $this->conector); 
      $record = mysql_fetch_row($dataset);
      return $record[0];
   }      

   function traer_datos_requerimiento() {
      $query  = " SELECT A.C_REQUERIMIENTO,DATE_FORMAT(A.FECHA,'%d/%m/%Y'), A.TITULO, A.C_UNIDAD_DEMANDANTE, A.C_UNIDAD_MONETARIA, DATE_FORMAT(A.FECHA_INICIAL,'%d/%m/%Y'), ";
      $query .= " DATE_FORMAT(A.FECHA_CRITICA,'%d/%m/%Y'), DATE_FORMAT(A.FECHA_TERMINO,'%d/%m/%Y'), A.C_PRIORIDAD, ";
      $query .= " A.C_ORIGEN_DEMANDA, A.CANTIDAD, A.C_UNIDAD_MEDIDA, A.C_FACTOR_AVANCE, A.DESCRIPCION, A.C_COMUNA, ";
      $query .= " A.N_COMUNA, A.C_PROGRAMA, A.N_PROGRAMA, A.C_ESTADO, A.FECHA_REGISTRO, A.C_USUARIO, X.N_UNIDAD_DEMANDANTE ";
      $query .= " FROM INVERSION_REQUERIMIENTO A, UNIDAD_DEMANDANTE X";
      $query .= " WHERE A.ANO= ".$this->ano." AND A.C_PREINVERSION=".$this->c_preinversion;
      $query .= " AND A.C_INSTITUCION=".$this->c_institucion." AND A.C_FICHA=".$this->c_ficha." AND A.C_REQUERIMIENTO=".$this->c_requerimiento;
      $query .= " AND A.C_UNIDAD_DEMANDANTE=X.C_UNIDAD_DEMANDANTE";
      $dataset  = mysql_query($query, $this->conector); 
      $record = mysql_fetch_row($dataset);
      $this->c_requerimiento=$record[0];
      $this->fecha=$record[1];
      $this->titulo=$record[2];
      $this->c_unidad_demandante=$record[3].' - '.$record[21];
      $this->c_unidad_monetaria=$record[4];
      $this->fecha_inicial=$record[5];
      $this->fecha_critica=$record[6];
      $this->fecha_termino=$record[7];
      $this->c_prioridad=$record[8];
      $this->c_origen_demanda=$record[9];
      $this->cantidad=$record[10];
      $this->c_unidad_medida=$record[11];
      $this->c_factor_avance=$record[12];
      $this->descripcion=$record[13];
      $this->c_comuna=$record[14];
      $this->n_comuna=$record[15];
      $this->c_programa=$record[16];
      $this->n_programa=$record[17];
      $this->c_estado=$record[18];
      $this->fecha_registro=$record[19];
      $this->c_usuario=$record[20];
   }

   function traer_datos_requerimiento_1() {
      $query  = " SELECT A.C_REQUERIMIENTO,DATE_FORMAT(A.FECHA,'%d/%m/%Y'), A.TITULO, A.C_UNIDAD_DEMANDANTE, A.C_UNIDAD_MONETARIA, A.FECHA_INICIAL, ";
      $query .= " A.FECHA_CRITICA, A.FECHA_TERMINO, A.C_PRIORIDAD, ";
      $query .= " A.C_ORIGEN_DEMANDA, A.CANTIDAD, A.C_UNIDAD_MEDIDA, A.C_FACTOR_AVANCE, A.DESCRIPCION, A.C_COMUNA, ";
      $query .= " A.N_COMUNA, A.C_PROGRAMA, A.N_PROGRAMA, A.C_ESTADO, A.FECHA_REGISTRO, A.C_USUARIO, X.N_UNIDAD_DEMANDANTE ";
      $query .= " FROM INVERSION_REQUERIMIENTO A, UNIDAD_DEMANDANTE X";
      $query .= " WHERE A.ANO= ".$this->ano." AND A.C_PREINVERSION=".$this->c_preinversion;
      $query .= " AND A.C_INSTITUCION=".$this->c_institucion." AND A.C_FICHA=".$this->c_ficha." AND A.C_REQUERIMIENTO=".$this->c_requerimiento;
      $query .= " AND A.C_UNIDAD_DEMANDANTE=X.C_UNIDAD_DEMANDANTE";
      $dataset  = mysql_query($query, $this->conector); 
      $record = mysql_fetch_row($dataset);
      $this->c_requerimiento=$record[0];
      $this->fecha=$record[1];
      $this->titulo=$record[2];
      $this->c_unidad_demandante=$record[3].' - '.$record[21];
      $this->c_unidad_monetaria=$record[4];
      $this->fecha_inicial=$record[5];
      $this->fecha_critica=$record[6];
      $this->fecha_termino=$record[7];
      $this->c_prioridad=$record[8];
      $this->c_origen_demanda=$record[9];
      $this->cantidad=$record[10];
      $this->c_unidad_medida=$record[11];
      $this->c_factor_avance=$record[12];
      $this->descripcion=$record[13];
      $this->c_comuna=$record[14];
      $this->n_comuna=$record[15];
      $this->c_programa=$record[16];
      $this->n_programa=$record[17];
      $this->c_estado=$record[18];
      $this->fecha_registro=$record[19];
      $this->c_usuario=$record[20];
   }

   function cargar_grilla_requerimiento() {
      $datainforme=array();$filas=0;$this->filaseleccionada=-1;
      $query  = " SELECT A.C_REQUERIMIENTO, DATE_FORMAT(A.FECHA,'%d/%m/%Y'), A.ID_SIGFE, A.TITULO, SUM(B.MONTO), ";
      $query .= " IF(A.C_ESTADO=1,'Pendiente',IF(A.C_ESTADO=2,'En Visto Bueno',IF(A.C_ESTADO=3,'Para Asig.IdSigfe',IF(A.C_ESTADO=8,'Obs.Visto Bueno',IF(A.C_ESTADO=9,'Obs.DAF','Con Id.Sigfe'))))), A.C_ESTADO, A.CODIGO_SIGFE ";
      $query .= " FROM INVERSION_REQUERIMIENTO A";
      $query .= " LEFT JOIN INVERSION_REQUERIMIENTO_MONTOS B ON (A.ANO=B.ANO AND A.C_INSTITUCION=B.C_INSTITUCION AND A.C_PREINVERSION=B.C_PREINVERSION ";
      $query .= " AND A.C_FICHA=B.C_FICHA AND A.C_REQUERIMIENTO=B.C_REQUERIMIENTO) ";
      $query .= " WHERE A.ANO= ".$this->ano." AND A.C_PREINVERSION=".$this->c_preinversion;
      $query .= " AND A.C_INSTITUCION=".$this->c_institucion." AND A.C_FICHA=".$this->c_ficha;
      $query .= " GROUP BY A.C_REQUERIMIENTO ORDER BY A.C_REQUERIMIENTO DESC";
      $dataset  = mysql_query($query, $this->conector); 
      $this->nroregistros = mysql_num_rows($dataset);
      $rows = array();
      while ($record = mysql_fetch_row($dataset)) { 
   $cols = array();
   foreach ($record as $value) { 
            $cols[] = '"'.addslashes($value).'"';
   } 
   $rows[] = "\t[".implode(",", $cols)."]"; 
      } 
      $datagrilla="[\n".implode(",\n",$rows)."\n];\n"; 
      return $datagrilla;
   }

   function actualizar_requerimiento_estado() {
      $query  = " UPDATE INVERSION_REQUERIMIENTO SET C_ESTADO=".$this->c_estado; 
      $query .= " WHERE ANO= ".$this->ano." AND C_PREINVERSION=".$this->c_preinversion;
      $query .= " AND C_INSTITUCION=".$this->c_institucion." AND C_FICHA=".$this->c_ficha." AND C_REQUERIMIENTO=".$this->c_requerimiento;
      $dataset  = mysql_query($query, $this->conector); 
      if (mysql_errno($this->conector) != 0) { 
         $this->mensaje_error="No se ha podido Actualizar la Base de Datos, mensaje de error de Mysql es: " . mysql_error($this->conector).$query; 
      } else {$this->mensaje_error='';}   
      return $this->mensaje_error;
   }
   
   function eliminar_requerimiento() {
      $query  = " DELETE FROM INVERSION_REQUERIMIENTO ";
      $query .= " WHERE ANO= ".$this->ano." AND C_PREINVERSION=".$this->c_preinversion;
      $query .= " AND C_INSTITUCION=".$this->c_institucion." AND C_FICHA=".$this->c_ficha." AND C_REQUERIMIENTO=".$this->c_requerimiento;
      $dataset  = mysql_query($query, $this->conector); 
      $query  = " DELETE FROM INVERSION_REQUERIMIENTO_MONTOS ";
      $query .= " WHERE ANO= ".$this->ano." AND C_PREINVERSION=".$this->c_preinversion;
      $query .= " AND C_INSTITUCION=".$this->c_institucion." AND C_FICHA=".$this->c_ficha." AND C_REQUERIMIENTO=".$this->c_requerimiento;
      $dataset  = mysql_query($query, $this->conector); 
      $query  = " DELETE FROM INVERSION_REQUERIMIENTO_OBSERVACIONES ";
      $query .= " WHERE ANO= ".$this->ano." AND C_PREINVERSION=".$this->c_preinversion;
      $query .= " AND C_INSTITUCION=".$this->c_institucion." AND C_FICHA=".$this->c_ficha." AND C_REQUERIMIENTO=".$this->c_requerimiento;
      $dataset  = mysql_query($query, $this->conector); 
      if (mysql_errno($this->conector) != 0) { 
         $this->mensaje_error="No se ha podido Actualizar la Base de Datos, mensaje de error de Mysql es: " . mysql_error($this->conector).$query; 
      } else {$this->mensaje_error='';}   
      return $this->mensaje_error;
   }
   
   function c_region() {
      $query  = " SELECT C_REGION FROM TREGION ";
      $dataset  = mysql_query($query, $this->conector); 
      $this->nroregistros = mysql_num_rows($dataset);
      $record = @mysql_fetch_row($dataset);
      return $record[0];
   }
  
   function actualizar_requerimiento() {
      $query  = " REPLACE INTO INVERSION_REQUERIMIENTO VALUES('";
      $query  .= $this->ano."','";  
      $query  .= $this->c_institucion."','";  
      $query  .= $this->c_preinversion."','";  
      $query  .= $this->c_ficha."','";  
      $query  .= $this->c_requerimiento."','";  
      $query  .= $this->fecha."','";  
      $query  .= $this->titulo."','";  
      $query  .= $this->c_unidad_demandante."','";  
      $query  .= $this->c_unidad_monetaria."','";  
      $query  .= $this->fecha_inicial."','";  
      $query  .= $this->fecha_critica."','";  
      $query  .= $this->fecha_termino."','";  
      $query  .= $this->c_prioridad."','";  
      $query  .= $this->c_origen_demanda."','";  
      $query  .= $this->cantidad."','";  
      $query  .= $this->c_unidad_monetaria."','";  
      $query  .= $this->c_factor_avance."','";  
      $query  .= $this->descripcion."','";  
      $query  .= $this->c_comuna."','";  
      $query  .= $this->n_comuna."','";  
      $query  .= $this->c_programa."','";  
      $query  .= $this->n_programa."','";  
      $query  .= $this->id_sigfe."','";  
      $query  .= $this->codigo_sigfe."','";  
      $query  .= $this->c_estado."','";  
      $query  .= $this->fecha_registro."','";  
      $query  .= $this->c_usuario."');";
      $dataset  = mysql_query($query, $this->conector);
      if (mysql_errno($this->conector) != 0) { 
         $this->mensaje_error="No se ha podido Actualizar la Base de Datos, mensaje de error de Mysql es: " . mysql_error($this->conector).$query; 
      } else {$this->mensaje_error='';}   
      return $this->mensaje_error;
   }
   
   function observar_requerimiento($xcobservacion,$xtipo,$xfecha,$xobservacion) {
      $query  = " INSERT INTO INVERSION_REQUERIMIENTO_OBSERVACIONES VALUE('";
      $query .= $this->ano."','".$this->c_institucion."','".$this->c_preinversion."','".$this->c_ficha."','";
      $query .= $this->c_requerimiento."','".$xcobservacion."','".$xtipo."','".$xfecha."','".$xobservacion."','".$this->fecha_registro."','".$this->c_usuario."')";
      $dataset  = mysql_query($query, $this->conector); 
      if (mysql_errno($this->conector) != 0) { 
         $this->mensaje_error="No se ha podido Actualizar la Base de Datos, mensaje de error de Mysql es: " . mysql_error($this->conector).$query; 
      } else {$this->mensaje_error='';}   
      return $this->mensaje_error;
   }

   function asignar_id_sigfe($xsigfe,$xsigfe1) {
      $query  = " UPDATE INVERSION_REQUERIMIENTO SET C_ESTADO=4, ID_SIGFE='".$xsigfe."', CODIGO_SIGFE='".$xsigfe1."'";
      $query .= " WHERE ANO= ".$this->ano." AND C_PREINVERSION=".$this->c_preinversion;
      $query .= " AND C_INSTITUCION=".$this->c_institucion." AND C_FICHA=".$this->c_ficha;
      $dataset  = mysql_query($query, $this->conector); 
      if (mysql_errno($this->conector) != 0) { 
         $this->mensaje_error="No se ha podido Actualizar la Base de Datos, mensaje de error de Mysql es: " . mysql_error($this->conector).$query; 
      } else {$this->mensaje_error='';}   
      return $this->mensaje_error;
   }

   function devengar_pago($xfecha,$xcomprobante,$xobservacion) {
      $query  = " INSERT INTO INVERSION_PAGO_DEVENGO VALUE('";
      $query .= $this->ano."','".$this->c_institucion."','".$this->c_preinversion."','".$this->c_ficha."','";
      $query .= $this->c_pago."','".$xfecha."','".$xcomprobante."','".$xobservacion."','".$this->fecha_registro."','".$this->c_usuario."')";
      $dataset  = mysql_query($query, $this->conector); 
      if (mysql_errno($this->conector) != 0) { 
         $this->mensaje_error="No se ha podido Actualizar la Base de Datos, mensaje de error de Mysql es: " . mysql_error($this->conector).$query; 
      } else {$this->mensaje_error='';}   
      return $this->mensaje_error;
   }

   function calcular_valor_maximo_observacion() {
      $query  = " SELECT MAX(C_OBSERVACION) FROM INVERSION_REQUERIMIENTO_OBSERVACIONES";
      $query .= " WHERE ANO= ".$this->ano." AND C_INSTITUCION=".$this->c_institucion;
      $query .= " AND C_PREINVERSION=".$this->c_preinversion." AND C_FICHA=".$this->c_ficha." AND C_REQUERIMIENTO=".$this->c_requerimiento;
      $dataset  = mysql_query($query, $this->conector); 
      $this->nroregistros = mysql_num_rows($dataset);
      $record = @mysql_fetch_row($dataset);
      return $record[0]+1;
   }

   function calcular_monto_total() {
      $query  = " SELECT SUM(MONTO) from INVERSION_REQUERIMIENTO_MONTOS ";
      $query .= " WHERE ANO= ".$this->ano." AND C_INSTITUCION=".$this->c_institucion;
      $query .= " AND C_PREINVERSION=".$this->c_preinversion." AND C_FICHA=".$this->c_ficha." AND C_REQUERIMIENTO=".$this->c_requerimiento;
      $dataset  = mysql_query($query, $this->conector); 
      $record = @mysql_fetch_row($dataset);
      return $record[0];                
   }

   function unidad_demandante() {
      $query  = " SELECT A.ID_UNIDAD_SIGFE FROM UNIDAD_DEMANDANTE A, INVERSION_REQUERIMIENTO B ";
      $query .= " WHERE A.ANO=B.ANO AND A.C_UNIDAD_DEMANDANTE=B.C_UNIDAD_DEMANDANTE AND B.ANO= ".$this->ano." AND B.C_INSTITUCION=".$this->c_institucion;
      $query .= " AND B.C_PREINVERSION=".$this->c_preinversion." AND B.C_FICHA=".$this->c_ficha." AND B.C_REQUERIMIENTO=".$this->c_requerimiento;
      $dataset  = mysql_query($query, $this->conector); 
      $record = @mysql_fetch_row($dataset);
      return $record[0];                
   }

   function traer_codigo_dato_sector() {
      $query  = " SELECT A.CODIGO_DATO FROM SECTOR A, INVERSION B ";
      $query .= " WHERE A.C_SECTOR=B.C_SECTOR AND B.ANO= ".$this->ano." AND B.C_INSTITUCION=".$this->c_institucion;
      $query .= " AND B.C_PREINVERSION=".$this->c_preinversion." AND B.C_FICHA=".$this->c_ficha;
      $dataset  = mysql_query($query, $this->conector); 
      $record = @mysql_fetch_row($dataset);
      return $record[0];                
   }

   function traer_nombre_sector() {
      $query  = " SELECT A.N_SECTOR FROM SECTOR A, INVERSION B ";
      $query .= " WHERE A.C_SECTOR=B.C_SECTOR AND B.ANO= ".$this->ano." AND B.C_INSTITUCION=".$this->c_institucion;
      $query .= " AND B.C_PREINVERSION=".$this->c_preinversion." AND B.C_FICHA=".$this->c_ficha;
      $dataset  = mysql_query($query, $this->conector); 
      $record = @mysql_fetch_row($dataset);
      return $record[0];                
   }

   function traer_id_sigfe_sector() {
      $query  = " SELECT A.ID_SIGFE FROM SECTOR A, INVERSION B ";
      $query .= " WHERE A.C_SECTOR=B.C_SECTOR AND B.ANO= ".$this->ano." AND B.C_INSTITUCION=".$this->c_institucion;
      $query .= " AND B.C_PREINVERSION=".$this->c_preinversion." AND B.C_FICHA=".$this->c_ficha;
      $dataset  = mysql_query($query, $this->conector); 
      $record = @mysql_fetch_row($dataset);
      return $record[0];                
   }

   function traer_montos() {
      $recursos = array (); 
      $query  = " SELECT C_CLASIFICADOR_SIGFE, MONTO FROM INVERSION_REQUERIMIENTO_MONTOS ";
      $query .= " WHERE ANO= ".$this->ano." AND C_INSTITUCION=".$this->c_institucion;
      $query .= " AND C_PREINVERSION=".$this->c_preinversion." AND C_FICHA=".$this->c_ficha." AND C_REQUERIMIENTO=".$this->c_requerimiento;
      $dataset  = mysql_query($query, $this->conector); 
      while ($record = mysql_fetch_row($dataset)) { 
         if ($record[1]!=0) {
      $recurso =  array (
        Recurso => array(
          'Id_Insumo'=> "0",
            'Id_Concepto_Presupuestario'=> $record[0],
            'Cantidad_Recurso'=>"0",
            'Monto_Recurso'=>$record[1]
          ),
          Cronograma => array(
            Tipo_Distribucion=> "1",
            Enero=>"0", Febrero=>"0", Marzo=>"0",
            Abril=>"0", Mayo=>"0", Junio=>"0",
            Julio=>"0", Agosto=>"0", Septiembre=>"0",
            Octubre=>"0", Noviembre=>"0", Diciembre=>"0"
          )
      );
  
     array_push($recursos, $recurso);
       }
   };
   return $recursos;     
   }

   function consultar_id_sigfe() {
      $query  = " SELECT ID_SIGFE FROM INVERSION_REQUERIMIENTO";
      $query .= " WHERE ANO= ".$this->ano." AND C_INSTITUCION=".$this->c_institucion;
      $query .= " AND C_PREINVERSION=".$this->c_preinversion." AND C_FICHA=".$this->c_ficha." AND C_REQUERIMIENTO=".$this->c_requerimiento;
      $dataset  = mysql_query($query, $this->conector); 
      $record = @mysql_fetch_row($dataset);
      return $record[0];
   }

   function id_comuna_sigfe($xcomuna) {
      $query  = " SELECT COUNT(*) FROM COMUNA WHERE ID_COMUNA_INE=".$xcomuna;
      $dataset  = mysql_query($query, $this->conector); 
      $record = @mysql_fetch_row($dataset);
      if ($record[0]==0) {return -1;}
      $query  = " SELECT CODIGO_DATO FROM COMUNA WHERE ID_COMUNA_INE=".$xcomuna;
      $dataset  = mysql_query($query, $this->conector); 
      $record = @mysql_fetch_row($dataset);
      return $record[0];
   }

   function traer_comuna1() {
      $query  = " SELECT C_COMUNA FROM INVERSION_REQUERIMIENTO ";
      $query .= " WHERE ANO= ".$this->ano." AND C_INSTITUCION=".$this->c_institucion;
      $query .= " AND C_PREINVERSION=".$this->c_preinversion." AND C_FICHA=".$this->c_ficha." AND C_REQUERIMIENTO=".$this->c_requerimiento;
      $dataset  = mysql_query($query, $this->conector); 
      $record = @mysql_fetch_row($dataset);
      return $record[0];
   }

   function traer_comuna2($xcomuna) {
      $query  = " SELECT ID_SIGFE FROM COMUNA WHERE CODIGO_DATO='".$xcomuna."'";
      $dataset  = mysql_query($query, $this->conector); 
      $record = @mysql_fetch_row($dataset);
      return $record[0];
   }

   function recuperar_informacion_para_compromiso($xcontrato,$xcompromiso) {
      $query  = " SELECT A.C_CLASIFICADOR, B.N_CLASIFICADOR, A.RUT_CONTRATISTA FROM INVERSION_CONTRATO A, TCLASIFICADOR_PRESUPUESTARIO B";
      $query .= " WHERE A.ANO= ".$this->ano." AND A.C_INSTITUCION=".$this->c_institucion;
      $query .= " AND A.C_PREINVERSION=".$this->c_preinversion." AND A.C_FICHA=".$this->c_ficha;
      $query .= " AND A.C_CONTRATO=".$xcontrato." AND A.C_CLASIFICADOR=B.C_CLASIFICADOR AND (A.C_INSTITUCION=B.INSTITUCION OR B.INSTITUCION=0) AND B.ANIO=".$this->ano;
      $dataset  = mysql_query($query, $this->conector); 
      $record = mysql_fetch_row($dataset);
      $this->c_clasificador=$record[0];
      $this->n_clasificador=$record[1];
      $this->n_rut=$record[2];
      $query  = " SELECT A.ID_SIGFE, A.CODIGO_SIGFE, DATE_FORMAT(A.FECHA,'%d/%m/%Y'), A.TITULO, A.CODIGO_SIGFE FROM INVERSION_REQUERIMIENTO A, INVERSION_REQUERIMIENTO_MONTOS B";
      $query .= " WHERE A.ANO= ".$this->ano." AND A.C_INSTITUCION=".$this->c_institucion;
      $query .= " AND A.C_PREINVERSION=".$this->c_preinversion." AND A.C_FICHA=".$this->c_ficha;
      $query .= " AND A.C_ESTADO=4 AND A.ANO=B.ANO AND A.C_INSTITUCION=B.C_INSTITUCION AND A.C_PREINVERSION=B.C_PREINVERSION ";      
      $query .= " AND A.C_FICHA=B.C_FICHA AND A.C_REQUERIMIENTO=B.C_REQUERIMIENTO AND B.C_CLASIFICADOR='".$this->c_clasificador."'";
      $query .= " ORDER BY A.C_REQUERIMIENTO ";
      $dataset  = mysql_query($query, $this->conector); 
      $record = mysql_fetch_row($dataset);
      $this->id_sigfe=$record[0];
      $this->codigo_sigfe=$record[1];
      $this->fecha=$record[2];
      $this->titulo=$record[3];
      $this->codigo_sigfe=$record[4];
      $query  = " SELECT SUM(MONTO) FROM INVERSION_REQUERIMIENTO_MONTOS ";
      $query .= " WHERE ANO= ".$this->ano." AND C_INSTITUCION=".$this->c_institucion;
      $query .= " AND C_PREINVERSION=".$this->c_preinversion." AND C_FICHA=".$this->c_ficha;
      $query .= " AND C_CLASIFICADOR='".$this->c_clasificador."'";
      $dataset  = mysql_query($query, $this->conector); 
      $record = mysql_fetch_row($dataset);
      $this->monto1=$record[0];
      $query  = " SELECT SUM(MONTO) FROM INVERSION_COMPROMISO ";
      $query .= " WHERE ANO= ".$this->ano." AND C_INSTITUCION=".$this->c_institucion;
      $query .= " AND C_PREINVERSION=".$this->c_preinversion." AND C_FICHA=".$this->c_ficha;
      $query .= " AND C_CONTRATO=".$xcontrato." AND C_CONVENIO=0 AND C_ESTADO=4";
      $query .= " AND C_CLASIFICADOR='".$this->c_clasificador."'";
      $dataset  = mysql_query($query, $this->conector); 
      $record = mysql_fetch_row($dataset);
      $this->monto2=$record[0];
      $query  = " SELECT MONTO, MONTO1 FROM INVERSION_COMPROMISO ";
      $query .= " WHERE ANO= ".$this->ano." AND C_INSTITUCION=".$this->c_institucion;
      $query .= " AND C_PREINVERSION=".$this->c_preinversion." AND C_FICHA=".$this->c_ficha;
      $query .= " AND C_COMPROMISO=".$xcompromiso;
      $dataset  = mysql_query($query, $this->conector); 
      $record = mysql_fetch_row($dataset);
      $this->monto11=$record[0];
      $this->monto12=$record[1];
      $this->monto3=($this->monto1-$this->monto2);
      $this->monto1=number_format($this->monto1,0,',','.');
      $this->monto2=number_format($this->monto2,0,',','.');
      $this->monto3=number_format($this->monto3,0,',','.');
      $query1  = " SELECT ANTICIPO FROM INVERSION_CONTRATO ";
      $query1 .= " WHERE ANO= ".$this->ano." AND C_INSTITUCION=".$this->c_institucion;
      $query1 .= " AND C_PREINVERSION=".$this->c_preinversion." AND C_FICHA=".$this->c_ficha;
      $query1 .= " AND C_CONTRATO=".$xcontrato;
      $dataset1  = mysql_query($query1, $this->conector); 
      $record1 = mysql_fetch_row($dataset1);
      if ($record1[0]>0) {
         $this->conanticipo=1;
         $query1  = " SELECT C_CLASIFICADOR, N_CLASIFICADOR, ID_SIGFE ";
         $query1 .= " FROM TCLASIFICADOR_PRESUPUESTARIO ";
         $query1 .= " WHERE ANIO=".$this->ano." AND C_CLASIFICADOR='32.06.001'";
         $dataset1= mysql_query($query1, $this->conector);
         $record1 = mysql_fetch_row($dataset1);
         $this->c_clasificador1=$record1[0];
         $this->n_clasificador1=$record1[1];
         $query  = " SELECT SUM(MONTO) FROM INVERSION_REQUERIMIENTO_MONTOS ";
         $query .= " WHERE ANO= ".$this->ano." AND C_INSTITUCION=".$this->c_institucion;
         $query .= " AND C_PREINVERSION=".$this->c_preinversion." AND C_FICHA=".$this->c_ficha;
         $query .= " AND C_CLASIFICADOR='32.06.001'";
         $dataset  = mysql_query($query, $this->conector); 
         $record = mysql_fetch_row($dataset);
         $this->monto4=$record[0];
         $query  = " SELECT SUM(MONTO1) FROM INVERSION_COMPROMISO ";
         $query .= " WHERE ANO= ".$this->ano." AND C_INSTITUCION=".$this->c_institucion;
         $query .= " AND C_PREINVERSION=".$this->c_preinversion." AND C_FICHA=".$this->c_ficha;
         $query .= " AND C_CONTRATO=".$xcontrato." AND C_CONVENIO=0 AND C_ESTADO=4";
         $query .= " AND C_CLASIFICADOR='".$this->c_clasificador."'";
         $dataset  = mysql_query($query, $this->conector); 
         $record = mysql_fetch_row($dataset);
         $this->monto5=$record[0];
         $this->monto6=($this->monto4-$this->monto5);
         $this->monto4=number_format($this->monto4,0,',','.');
         $this->monto5=number_format($this->monto5,0,',','.');
         $this->monto6=number_format($this->monto6,0,',','.');
      } else {
         $this->conanticipo=0;
      }
   }

   function recuperar_informacion_para_compromiso1($xcontrato,$xcompromiso) {
      $query  = " SELECT A.C_CLASIFICADOR, A.N_CLASIFICADOR, A.RUT, A.ID_SIGFE_REQUERIMIENTO, A.CODIGO_SIGFE_REQUERIMIENTO, ";
      $query .= " DATE_FORMAT(A.FECHA_REQUERIMIENTO,'%d/%m/%Y'), A.TITULO_REQUERIMIENTO, A.REQUERIDO, A.COMPROMETIDO, A.SALDO, A.MONTO, ";
      $query .= " A.C_CLASIFICADOR1, A.N_CLASIFICADOR1, A.REQUERIDO1, A.COMPROMETIDO1, A.SALDO1, A.MONTO1 ";
      $query .= " FROM INVERSION_COMPROMISO A ";
      $query .= " WHERE A.ANO= ".$this->ano." AND A.C_INSTITUCION=".$this->c_institucion;
      $query .= " AND A.C_PREINVERSION=".$this->c_preinversion." AND A.C_FICHA=".$this->c_ficha;
      $query .= " AND A.C_COMPROMISO=".$xcompromiso;
      $dataset  = mysql_query($query, $this->conector); 
      $record = mysql_fetch_row($dataset);
      $this->c_clasificador=$record[0];
      $this->n_clasificador=$record[1];
      $this->n_rut=$record[2];
      $this->id_sigfe=$record[3];
      $this->codigo_sigfe=$record[4];
      $this->fecha=$record[5];
      $this->titulo=$record[6];
      $this->monto1=number_format($record[7],0,',','.');
      $this->monto2=number_format($record[8],0,',','.');
      $this->monto3=number_format($record[9],0,',','.');
      $this->monto11=$record[10];
      $this->c_clasificador1=$record[11];
      $this->n_clasificador1=$record[12];
      $this->monto4=number_format($record[13],0,',','.');
      $this->monto5=number_format($record[14],0,',','.');
      $this->monto6=number_format($record[15],0,',','.');
      $this->monto12=$record[16];
      if ($record[12]!="") {$this->conanticipo=1;} else {$this->conanticipo=0;}
   }

   function get_nroregistros(){ return $this->nroregistros;}  
   
   function get_error()       { return $this->mensaje_error;}
      
   function get_filaseleccionada()       { return $this->filaseleccionada;}
}

//********************************************
// COMPROMISO INVERSION
//********************************************   
Class Inversion_Compromiso {
   var $conector;
   var $mensaje_error;
   var $nroregistros;
   var $ano;  
   var $c_institucion;
   var $c_preinversion;
   var $c_ficha;
   var $c_compromiso;
   var $c_contrato;
   var $c_convenio;
   var $rut='';
   var $fecha_estimada;
   var $titulo="";
   var $c_unidad_monetaria;
   var $descripcion="";
   var $id_sigfe_requerimiento="";
   var $codigo_sigfe_requerimiento="";
   var $fecha_requerimiento="";
   var $titulo_requerimiento="";
   var $c_clasificador="";
   var $n_clasificador="";
   var $requerido;
   var $comprometido;
   var $saldo;
   var $monto;
   var $c_clasificador1="";
   var $n_clasificador1="";
   var $requerido1;
   var $comprometido1;
   var $saldo1;
   var $monto1;
   var $c_estado;
   var $fecha_registro;
   var $c_usuario;
   var $id_sigfe;
   var $id_sigfe1;
   
   function Inversion_Compromiso($conector) { 
      $this->conector =$conector;
      $this->mensaje_error='';
      $this->nroregistros=0;
   }
      
   function calcular_valor_maximo() {
      $query  = " SELECT MAX(C_COMPROMISO) FROM INVERSION_COMPROMISO";
      $query .= " WHERE ANO= ".$this->ano;
      $dataset  = mysql_query($query, $this->conector); 
      $this->nroregistros = mysql_num_rows($dataset);
      $record = @mysql_fetch_row($dataset);
      $retorno=$record[0]+1;
      return $retorno;
   }

   function calcular_inicial() {
      $query  = " SELECT A.C_COMPROMISO";
      $query .= " FROM INVERSION_COMPROMISO A ";
      $query .= " WHERE A.ANO= ".$this->ano." AND A.C_PREINVERSION=".$this->c_preinversion;
      $query .= " AND A.C_INSTITUCION=".$this->c_institucion." AND A.C_FICHA=".$this->c_ficha;
      $query .= " AND A.C_CONTRATO=".$this->c_contrato." AND A.C_CONVENIO=".$this->c_convenio;
      $query .= " ORDER BY A.C_COMPROMISO ";
      $dataset  = mysql_query($query, $this->conector); 
      $record = mysql_fetch_row($dataset);
      return $record[0];
   }      

   function tiene_observaciones() {
      $query  = " SELECT COUNT(*) ";
      $query .= " FROM INVERSION_COMPROMISO_OBSERVACIONES ";
      $query .= " WHERE ANO= ".$this->ano." AND C_PREINVERSION=".$this->c_preinversion;
      $query .= " AND C_INSTITUCION=".$this->c_institucion." AND C_FICHA=".$this->c_ficha." AND C_COMPROMISO=".$this->c_compromiso;
      $dataset  = mysql_query($query, $this->conector); 
      $record = mysql_fetch_row($dataset);
      return $record[0];
   }

   function traer_datos_proveedor() {
      $query  = " SELECT IF(B.TIPO=1,CONCAT(B.RUN,'-',B.DV,' ',B.NOMBRE),CONCAT(B.RUN,'-',B.DV,' ',B.NOMBRE,' ',B.AP_PATERNO,' ',B.AP_MATERNO)) ";
      $query .= " FROM INVERSION_CONTRATO A, TCONTRATISTA B WHERE A.RUT_CONTRATISTA=B.RUN AND A.C_ESTADO IN (2,4)";
      $query .= " AND A.ANO=".$this->ano." AND A.C_INSTITUCION=".$this->c_institucion." AND A.C_PREINVERSION=".$this->c_preinversion;
      $query .= " AND A.C_FICHA=".$this->c_ficha." AND A.C_CONTRATO=".$this->c_contrato;
      $dataset  = mysql_query($query, $this->conector); 
      $record = mysql_fetch_row($dataset);     
      return $record[0];
   }
   
   function traer_datos_compromiso() {
      $query  = " SELECT A.C_COMPROMISO,DATE_FORMAT(A.FECHA_ESTIMADA,'%d/%m/%Y'), A.TITULO, A.C_UNIDAD_MONETARIA, A.DESCRIPCION, ";
      $query .= " A.C_ESTADO, A.FECHA_REGISTRO, A.C_USUARIO, A.ID_SIGFE_REQUERIMIENTO, A.CODIGO_SIGFE_REQUERIMIENTO,A.FECHA_REQUERIMIENTO, A.TITULO_REQUERIMIENTO, ";
      $query .= " A.C_CONTRATO, A.C_CONVENIO ";
      $query .= " FROM INVERSION_COMPROMISO A";
      $query .= " WHERE A.ANO= ".$this->ano." AND A.C_PREINVERSION=".$this->c_preinversion;
      $query .= " AND A.C_INSTITUCION=".$this->c_institucion." AND A.C_FICHA=".$this->c_ficha." AND A.C_COMPROMISO=".$this->c_compromiso;
      $dataset  = mysql_query($query, $this->conector); 
      $record = mysql_fetch_row($dataset);
      $this->c_compromiso=$record[0];
      $this->fecha_estimada=$record[1];
      $this->titulo=$record[2];
      $this->c_unidad_monetaria=$record[3];
      $this->descripcion=$record[4];
      $this->c_estado=$record[5];
      $this->fecha_registro=$record[6];
      $this->c_usuario=$record[7];
      $this->id_sigfe_requerimiento=$record[8];
      $this->codigo_sigfe_requerimiento=$record[9];
      $this->fecha_requerimiento=$record[10];
      $this->titulo_requerimiento=$record[11];
      $this->c_contrato=$record[12];
      $this->c_convenio=$record[13];
   }

   function traer_datos_compromiso1() {
      $query  = " SELECT A.C_COMPROMISO,A.FECHA_ESTIMADA, A.TITULO, A.C_UNIDAD_MONETARIA, A.DESCRIPCION, ";
      $query .= " A.C_ESTADO, A.FECHA_REGISTRO, A.C_USUARIO, A.ID_SIGFE_REQUERIMIENTO, A.FECHA_REQUERIMIENTO, A.TITULO_REQUERIMIENTO, ";
      $query .= " A.REQUERIDO, A.COMPROMETIDO, A.SALDO, A.MONTO, A.REQUERIDO1, A.COMPROMETIDO1, A.SALDO1, A.MONTO1, ";
      $query .= " A.C_CLASIFICADOR, A.N_CLASIFICADOR, A.C_CONTRATO, A.C_CONVENIO, B.ID_SIGFE, A.RUT, C.ID_SIGFE, A.C_CLASIFICADOR1 ";
      $query .= " FROM TCLASIFICADOR_PRESUPUESTARIO B, INVERSION_COMPROMISO A ";
      $query .= " LEFT JOIN TCLASIFICADOR_PRESUPUESTARIO C ON (A.C_CLASIFICADOR1=C.C_CLASIFICADOR AND A.ANO=C.ANIO AND (A.C_INSTITUCION=C.INSTITUCION OR C.INSTITUCION=0))";
      $query .= " WHERE A.ANO= ".$this->ano." AND A.C_PREINVERSION=".$this->c_preinversion;
      $query .= " AND A.C_INSTITUCION=".$this->c_institucion." AND A.C_FICHA=".$this->c_ficha." AND A.C_COMPROMISO=".$this->c_compromiso;
      $query .= " AND A.C_CLASIFICADOR=B.C_CLASIFICADOR AND A.ANO=B.ANIO AND (A.C_INSTITUCION=B.INSTITUCION OR B.INSTITUCION=0)";
      $dataset  = mysql_query($query, $this->conector); 
      $record = mysql_fetch_row($dataset);
      $this->c_compromiso=$record[0];
      $this->fecha_estimada=$record[1];
      $this->titulo=$record[2];
      $this->c_unidad_monetaria=$record[3];
      $this->descripcion=$record[4];
      $this->c_estado=$record[5];
      $this->fecha_registro=$record[6];
      $this->c_usuario=$record[7];
      $this->id_requerimiento=$record[8];
      $this->fecha_requerimiento=$record[9];
      $this->titulo_requerimiento=$record[10];
      $this->requerido=$record[11];
      $this->comprometido=$record[12];
      $this->saldo=$record[13];
      $this->monto=$record[14];
      $this->requerido1=$record[15];
      $this->comprometido1=$record[16];
      $this->saldo1=$record[17];
      $this->monto1=$record[18];
      $this->c_clasificador=$record[19];
      $this->n_clasificador=$record[20];
      $this->c_contrato=$record[21];
      $this->c_convenio=$record[22];
      $this->id_sigfe=$record[23];
      $this->rut=$record[24];
      $this->id_sigfe1=$record[25];
      $this->c_clasificador1=$record[26];
   }

   function cargar_grilla_compromiso() {
      $datainforme=array();$filas=0;$this->filaseleccionada=-1;
      $query  = " SELECT A.C_COMPROMISO, DATE_FORMAT(A.FECHA_ESTIMADA,'%d/%m/%Y'), A.ID_SIGFE, A.TITULO, (A.MONTO+A.MONTO1), ";
      $query .= " IF(A.C_ESTADO=1,'Pendiente',IF(A.C_ESTADO=2,'En Visto Bueno',IF(A.C_ESTADO=3,'Para Asig.IdSigfe',IF(A.C_ESTADO=8,'Obs.Visto Bueno',IF(A.C_ESTADO=9,'Obs.DAF','Con Id.Sigfe'))))), A.C_ESTADO ";
      $query .= " FROM INVERSION_COMPROMISO A";
      $query .= " WHERE A.ANO= ".$this->ano." AND A.C_PREINVERSION=".$this->c_preinversion;
      $query .= " AND A.C_INSTITUCION=".$this->c_institucion." AND A.C_FICHA=".$this->c_ficha;
      $query .= " AND A.C_CONTRATO=".$this->c_contrato." AND A.C_CONVENIO=".$this->c_convenio;
      $query .= " GROUP BY A.C_COMPROMISO ORDER BY A.C_COMPROMISO DESC";
      $dataset  = mysql_query($query, $this->conector); 
      $this->nroregistros = mysql_num_rows($dataset);
      $rows = array();
      while ($record = mysql_fetch_row($dataset)) { 
   $cols = array();
   foreach ($record as $value) { 
            $cols[] = '"'.addslashes($value).'"';
   } 
   $rows[] = "\t[".implode(",", $cols)."]"; 
      } 
      $datagrilla="[\n".implode(",\n",$rows)."\n];\n"; 
      return $datagrilla;
   }

   function actualizar_compromiso_estado() {
      $query  = " UPDATE INVERSION_COMPROMISO SET C_ESTADO=".$this->c_estado; 
      $query .= " WHERE ANO= ".$this->ano." AND C_PREINVERSION=".$this->c_preinversion;
      $query .= " AND C_INSTITUCION=".$this->c_institucion." AND C_FICHA=".$this->c_ficha." AND C_COMPROMISO=".$this->c_compromiso;
      $dataset  = mysql_query($query, $this->conector); 
      if (mysql_errno($this->conector) != 0) { 
         $this->mensaje_error="No se ha podido Actualizar la Base de Datos, mensaje de error de Mysql es: " . mysql_error($this->conector).$query; 
      } else {$this->mensaje_error='';}   
      return $this->mensaje_error;
   }
   
   function eliminar_compromiso() {
      $query  = " DELETE FROM INVERSION_COMPROMISO ";
      $query .= " WHERE ANO= ".$this->ano." AND C_PREINVERSION=".$this->c_preinversion;
      $query .= " AND C_INSTITUCION=".$this->c_institucion." AND C_FICHA=".$this->c_ficha." AND C_COMPROMISO=".$this->c_compromiso;
      $dataset  = mysql_query($query, $this->conector); 
      $query  = " DELETE FROM INVERSION_COMPROMISO_OBSERVACIONES ";
      $query .= " WHERE ANO= ".$this->ano." AND C_PREINVERSION=".$this->c_preinversion;
      $query .= " AND C_INSTITUCION=".$this->c_institucion." AND C_FICHA=".$this->c_ficha." AND C_COMPROMISO=".$this->c_compromiso;
      $dataset  = mysql_query($query, $this->conector); 
      if (mysql_errno($this->conector) != 0) { 
         $this->mensaje_error="No se ha podido Actualizar la Base de Datos, mensaje de error de Mysql es: " . mysql_error($this->conector).$query; 
      } else {$this->mensaje_error='';}   
      return $this->mensaje_error;
   }
   
   function c_region() {
      $query  = " SELECT C_REGION FROM TREGION ";
      $dataset  = mysql_query($query, $this->conector); 
      $this->nroregistros = mysql_num_rows($dataset);
      $record = @mysql_fetch_row($dataset);
      return $record[0];
   }
  
   function actualizar_compromiso() {
      $query  = " REPLACE INTO INVERSION_COMPROMISO VALUES('";
      $query  .= $this->ano."','";  
      $query  .= $this->c_institucion."','";  
      $query  .= $this->c_preinversion."','";  
      $query  .= $this->c_ficha."','";  
      $query  .= $this->c_compromiso."','";  
      $query  .= $this->c_contrato."','";  
      $query  .= $this->c_convenio."','";  
      $query  .= $this->rut."','";  
      $query  .= $this->fecha_estimada."','";  
      $query  .= $this->titulo."','";  
      $query  .= $this->c_unidad_monetaria."','";  
      $query  .= $this->descripcion."','";  
      $query  .= $this->id_sigfe_requerimiento."','";  
      $query  .= $this->codigo_sigfe_requerimiento."','";  
      $query  .= $this->fecha_requerimiento."','";  
      $query  .= $this->titulo_requerimiento."','";  
      $query  .= $this->c_clasificador."','";  
      $query  .= $this->n_clasificador."','";  
      $query  .= $this->requerido."','";  
      $query  .= $this->comprometido."','";  
      $query  .= $this->saldo."','";  
      $query  .= $this->monto."','";  
      $query  .= $this->c_clasificador1."','";  
      $query  .= $this->n_clasificador1."','";  
      $query  .= $this->requerido1."','";  
      $query  .= $this->comprometido1."','";  
      $query  .= $this->saldo1."','";  
      $query  .= $this->monto1."','";  
      $query  .= $this->id_sigfe."','";  
      $query  .= $this->codigo_sigfe."','";  
      $query  .= $this->c_estado."','";  
      $query  .= $this->fecha_registro."','";  
      $query  .= $this->c_usuario."');";
      $dataset  = mysql_query($query, $this->conector);
      if (mysql_errno($this->conector) != 0) { 
         $this->mensaje_error="No se ha podido Actualizar la Base de Datos, mensaje de error de Mysql es: " . mysql_error($this->conector).$query; 
      } else {$this->mensaje_error='';}   
      return $this->mensaje_error;
   }
   
   function observar_compromiso($xcobservacion,$xtipo,$xfecha,$xobservacion) {
      $query  = " INSERT INTO INVERSION_COMPROMISO_OBSERVACIONES VALUE('";
      $query .= $this->ano."','".$this->c_institucion."','".$this->c_preinversion."','".$this->c_ficha."','";
      $query .= $this->c_compromiso."','".$xcobservacion."','".$xtipo."','".$xfecha."','".$xobservacion."','".$this->fecha_registro."','".$this->c_usuario."')";
      $dataset  = mysql_query($query, $this->conector); 
      if (mysql_errno($this->conector) != 0) { 
         $this->mensaje_error="No se ha podido Actualizar la Base de Datos, mensaje de error de Mysql es: " . mysql_error($this->conector).$query; 
      } else {$this->mensaje_error='';}   
      return $this->mensaje_error;
   }

   function asignar_id_sigfe($xsigfe,$xsigfe1) {
      $query  = " UPDATE INVERSION_COMPROMISO SET C_ESTADO=4, ID_SIGFE='".$xsigfe."', CODIGO_SIGFE='".$xsigfe1."'";
      $query .= " WHERE ANO= ".$this->ano." AND C_PREINVERSION=".$this->c_preinversion;
      $query .= " AND C_INSTITUCION=".$this->c_institucion." AND C_FICHA=".$this->c_ficha;
      $query .= " AND C_COMPROMISO=".$this->c_compromiso;
      $dataset  = mysql_query($query, $this->conector); 
      if (mysql_errno($this->conector) != 0) { 
         $this->mensaje_error="No se ha podido Actualizar la Base de Datos, mensaje de error de Mysql es: " . mysql_error($this->conector).$query; 
      } else {$this->mensaje_error='';}   
      return $this->mensaje_error;
   }

   function calcular_valor_maximo_observacion() {
      $query  = " SELECT MAX(C_OBSERVACION) FROM INVERSION_COMPROMISO_OBSERVACIONES";
      $query .= " WHERE ANO= ".$this->ano." AND C_INSTITUCION=".$this->c_institucion;
      $query .= " AND C_PREINVERSION=".$this->c_preinversion." AND C_FICHA=".$this->c_ficha." AND C_COMPROMISO=".$this->c_compromiso;
      $dataset  = mysql_query($query, $this->conector); 
      $this->nroregistros = mysql_num_rows($dataset);
      $record = @mysql_fetch_row($dataset);
      return $record[0]+1;
   }

   function consultar_id_sigfe() {
      $query  = " SELECT ID_SIGFE FROM INVERSION_COMPROMISO";
      $query .= " WHERE ANO= ".$this->ano." AND C_INSTITUCION=".$this->c_institucion;
      $query .= " AND C_PREINVERSION=".$this->c_preinversion." AND C_FICHA=".$this->c_ficha." AND C_COMPROMISO=".$this->c_compromiso;
      $dataset  = mysql_query($query, $this->conector); 
      $record = @mysql_fetch_row($dataset);
      return $record[0];
   }

   function get_nroregistros(){ return $this->nroregistros;}  
   
   function get_error()       { return $this->mensaje_error;}
      
   function get_filaseleccionada()       { return $this->filaseleccionada;}
}

//********************************************
// DEVENGO INVERSION
//********************************************   
Class Inversion_Devengo {
   var $conector;
   var $mensaje_error;
   var $nroregistros;
   var $ano;  
   var $c_institucion;
   var $c_preinversion;
   var $c_ficha;
   var $c_pago;
   var $titulo="";
   var $valor_devengo;
   var $detalle="";
   var $id_sigfe_com="";
   var $id_sigfe_req="";
   var $id_sigfe_cla="";
   var $devengado;
   var $run_contratista="";
   var $tipo_documento="";
   var $nro_documento="";
   var $fecha_documento="";
   var $tipo_pago;
   var $c_clasificador="";
   var $anticipo;
   var $tesoreria;
   
   function Inversion_Devengo($conector) { 
      $this->conector =$conector;
      $this->mensaje_error='';
      $this->nroregistros=0;
   }

   function c_region() {
      $query  = " SELECT C_REGION FROM TREGION ";
      $dataset  = mysql_query($query, $this->conector); 
      $this->nroregistros = mysql_num_rows($dataset);
      $record = @mysql_fetch_row($dataset);
      return $record[0];
   }

   function traer_datos_pago() {
      $query  = " SELECT CONCAT(A.RUT_CONTRATISTA,'-',B.DV,'  ',B.NOMBRE,' ',B.AP_PATERNO,' ',B.AP_MATERNO), A.VALOR_PAGO, A.DETALLE, CONCAT(B.RUN,'-',B.DV) ";
      $query .= " ,A.C_DOCU_PAGAR,  A.NRO_DOCU_PAGAR, DATE_FORMAT(A.FECHA_DOCU_PAGAR,'%Y-%m-%d'), A.C_TIPO_PAGO, A.ANTICIPO, A.TESORERIA ";
      $query .= " FROM INVERSION_PAGO A, TCONTRATISTA B ";
      $query .= " WHERE A.ANO= ".$this->ano." AND A.C_PREINVERSION=".$this->c_preinversion;
      $query .= " AND A.C_INSTITUCION=".$this->c_institucion." AND A.C_FICHA=".$this->c_ficha." AND A.C_PAGO=".$this->c_pago;
      $query .= " AND A.RUT_CONTRATISTA=B.RUN ";
      $dataset  = mysql_query($query, $this->conector); 
      $record = mysql_fetch_row($dataset);
      $this->titulo=$record[0];
      $this->valor_devengo=$record[1];
      $this->detalle=$record[2];
      $this->run_contratista=$record[3];
      $this->tipo_documento=$record[4];
      $this->nro_documento=$record[5];
      $this->fecha_documento=$record[6];
      $this->tipo_pago=$record[7];
      $this->anticipo=$record[8];
      $this->tesoreria=$record[9];
   }

   function traer_datos_compromiso() {
      $query  = " SELECT B.ID_SIGFE, C.ID_SIGFE, C.C_CLASIFICADOR, B.ID_SIGFE_REQUERIMIENTO FROM INVERSION_PAGO A, INVERSION_COMPROMISO B, TCLASIFICADOR_PRESUPUESTARIO C ";
      $query .= " WHERE A.ANO= ".$this->ano." AND A.C_PREINVERSION=".$this->c_preinversion;
      $query .= " AND A.C_INSTITUCION=".$this->c_institucion." AND A.C_FICHA=".$this->c_ficha." AND A.C_PAGO=".$this->c_pago;
      $query .= " AND A.ANO=B.ANO AND A.C_INSTITUCION=B.C_INSTITUCION AND A.C_PREINVERSION=B.C_PREINVERSION AND A.C_FICHA=B.C_FICHA ";
      $query .= " AND A.C_CONTRATO=B.C_CONTRATO AND B.C_CLASIFICADOR=C.C_CLASIFICADOR AND B.ANO=C.ANIO AND (B.C_INSTITUCION=C.INSTITUCION OR C.INSTITUCION=0)";
      $dataset  = mysql_query($query, $this->conector); 
      $record = mysql_fetch_row($dataset);
      $this->id_sigfe_com=$record[0];
      $this->id_sigfe_cla=$record[1];
      $this->c_clasificador=$record[2];
      $this->id_sigfe_req=$record[3];
   }

   function tiene_compromiso() {
      $query  = " SELECT A.C_TIPO_PAGO, A.C_CLASIFICADOR, A.C_CONTRATO FROM INVERSION_PAGO A ";
      $query .= " WHERE A.ANO= ".$this->ano." AND A.C_PREINVERSION=".$this->c_preinversion;
      $query .= " AND A.C_INSTITUCION=".$this->c_institucion." AND A.C_FICHA=".$this->c_ficha." AND A.C_PAGO=".$this->c_pago;
      $dataset  = mysql_query($query, $this->conector); 
      $record = mysql_fetch_row($dataset);
      if ($record[0]!=4) {
          $query1  = " SELECT SUM(A.MONTO) FROM INVERSION_COMPROMISO A";
          $query1 .= " WHERE A.ANO= ".$this->ano." AND A.C_PREINVERSION=".$this->c_preinversion." AND A.C_ESTADO>2 ";
          $query1 .= " AND A.C_INSTITUCION=".$this->c_institucion." AND A.C_FICHA=".$this->c_ficha." AND A.C_CLASIFICADOR='".$record[1]."'";
          $dataset1  = mysql_query($query1, $this->conector); 
          $record1 = mysql_fetch_row($dataset1);
          $mcompromiso=$record1[0];
          $query1  = " SELECT SUM(A.VALOR_PAGO) FROM INVERSION_PAGO A ";
          $query1 .= " WHERE A.ANO= ".$this->ano." AND A.C_PREINVERSION=".$this->c_preinversion;
          $query1 .= " AND A.C_INSTITUCION=".$this->c_institucion." AND A.C_FICHA=".$this->c_ficha;
          $query1 .= " AND A.C_TIPO_PAGO<4 AND A.C_ESTADO=4 AND A.C_CONTRATO=".$record[2]." AND A.C_PAGO<>".$this->c_pago;
          $dataset1  = mysql_query($query1, $this->conector); 
          $record1 = mysql_fetch_row($dataset1);
          $mcompromiso=$mcompromiso-$record1[0];                
      } else {
          $query1  = " SELECT SUM(A.MONTO1) FROM INVERSION_COMPROMISO A";
          $query1 .= " WHERE A.ANO= ".$this->ano." AND A.C_PREINVERSION=".$this->c_preinversion." AND A.C_ESTADO>2";
          $query1 .= " AND A.C_INSTITUCION=".$this->c_institucion." AND A.C_FICHA=".$this->c_ficha." AND A.C_CLASIFICADOR1='32.06.001'";
          $dataset1  = mysql_query($query1, $this->conector); 
          $record1 = mysql_fetch_row($dataset1);
          $mcompromiso=$record1[0];
          $query1  = " SELECT SUM(A.VALOR_PAGO) FROM INVERSION_PAGO A ";
          $query1 .= " WHERE A.ANO= ".$this->ano." AND A.C_PREINVERSION=".$this->c_preinversion;
          $query1 .= " AND A.C_INSTITUCION=".$this->c_institucion." AND A.C_FICHA=".$this->c_ficha;
          $query1 .= " AND A.C_TIPO_PAGO=4 AND A.C_ESTADO=4 AND A.C_PAGO<>".$this->c_pago;
          $dataset1  = mysql_query($query1, $this->conector); 
          $record1 = mysql_fetch_row($dataset1);
          $mcompromiso=$mcompromiso-$record1[0];                
      }
      return $mcompromiso;
   }

   function get_nroregistros(){ return $this->nroregistros;}  
   
   function get_error()       { return $this->mensaje_error;}
      
   function get_filaseleccionada()       { return $this->filaseleccionada;}

}
?>