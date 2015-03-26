<?

/*********************************************************************

    Entidad Iniciativa Ari

**********************************************************************/

// INICIATIVA ARI
   
Class Iniciativa_Ari {
   var $conector;
   var $mensaje_error;
   var $nroregistros;
   var $ano;  
   var $c_institucion;
   var $c_preinversion;
   var $n_preinversion="";
   var $unidad_tecnica;
   var $responsable_propir;
   var $c_programa_ari;
   var $c_clasificador_presupuestario;
   var $c_etapa_idi;
   var $fecha_inicio;
   var $fecha_termino;
   var $c_estado_proyecto;
   var $producto="";
   var $codigo="";
   var $c_tipo_codigo;
   var $rate;
   var $impactos="";
   var $c_sector;
   var $c_estado_preinversion;
   var $observaciones="";
   var $beneficiarios="";
   var $costo_total;
   var $gastado_anos_anteriores;
   var $solicitado;
   var $saldo_proximos_anos;
   var $fecha_registro;
   var $c_usuario;
   var $filaseleccionada;
   var $total;
   var $total1;
   var $total2;
   var $total3;
   
   function Iniciativa_Ari($conector){ 
      $this->conector =$conector;
      $this->mensaje_error='';
      $this->estado=0;
      $this->nroregistros=0;
   }
   
   function cargar_iniciativa_estado() {
      $query  =" SELECT B.N_ESTADO_PREINVERSION from PREINVERSION_ARI A, ESTADO_PREINVERSION B ";
      $query .=" WHERE A.C_ESTADO_PREINVERSION = B.C_ESTADO_PREINVERSION";
      $query .=" AND (A.C_INSTITUCION*10000)+A.C_PREINVERSION= ".$this->c_preinversion;
      $query .=" AND A.ANO= ".$this->ano;   
      $dataset  = mysql_query($query, $this->conector); 
      $this->nroregistros = mysql_num_rows($dataset);
      return $dataset;
   }

   function cargar_iniciativa_ficha() {
      $query  =" SELECT A.N_PREINVERSION, A.UNIDAD_TECNICA, A.C_PROGRAMA_ARI, A.FECHA_INICIO, ";
      $query .=" A.FECHA_TERMINO, A.C_ESTADO_PROYECTO, A.PRODUCTO, A.CODIGO, A.C_TIPO_CODIGO, A.IMPACTOS, A.C_SECTOR, A.C_ESTADO_PREINVERSION, A.OBSERVACIONES, A.BENEFICIARIOS ";
      $query .=" ,A.C_CLASIFICADOR_PRESUPUESTARIO, A.C_ETAPA_IDI, A.RATE, B.N_SECTOR, C.N_INSTITUCION, D.N_PROGRAMA_ARI,  ";
      $query .=" E.N_CLASIFICADOR, F.N_ETAPA_IDI, G.N_RATE, H.N_ESTADO_PROYECTO, A.RESPONSABLE_PROPIR, Z.N_INSTITUCION ";
      $query .=" FROM PREINVERSION_ARI A, SECTOR B, INSTITUCION C, PROGRAMA_ARI D, TCLASIFICADOR_PRESUPUESTARIO E, ETAPA_IDI F, ";
      $query .=" RATE G, ESTADO_PROYECTO H, INSTITUCION Z ";
      $query .=" WHERE A.C_SECTOR=B.C_SECTOR AND A.UNIDAD_TECNICA=C.C_INSTITUCION AND A.C_PROGRAMA_ARI=D.C_PROGRAMA_ARI AND ";
      $query .=" A.C_CLASIFICADOR_PRESUPUESTARIO=E.C_CLASIFICADOR AND A.ANO=E.ANIO AND A.C_ETAPA_IDI=F.C_ETAPA_IDI AND A.RATE=G.C_RATE AND A.C_ESTADO_PROYECTO=H.C_ESTADO_PROYECTO AND ";
      $query .=" (A.C_INSTITUCION*10000)+A.C_PREINVERSION= ".$this->c_preinversion." AND A.ANO= ".$this->ano;  
      $query .=" AND A.RESPONSABLE_PROPIR=Z.C_INSTITUCION";
      $dataset  = mysql_query($query, $this->conector); 
      $this->nroregistros = mysql_num_rows($dataset);
      return $dataset;
   }

   function cargar_iniciativa_institucion_sector() {
      $query  =" SELECT A.C_INSTITUCION, A.N_INSTITUCION, B.N_SECTOR from PREINVERSION_ARI C, INSTITUCION A, SECTOR B ";
      $query .=" WHERE A.C_SECTOR = B.C_SECTOR AND A.C_INSTITUCION= C.C_INSTITUCION ";
      $query .=" AND (C.C_INSTITUCION*10000)+C.C_PREINVERSION= ".$this->c_preinversion;
      $query .=" AND C.ANO= ".$this->ano;
      $dataset  = mysql_query($query, $this->conector); 
      $this->nroregistros = mysql_num_rows($dataset);
      return $dataset;
   }

   function existe_observaciones_iniciativa() {
      $query  =" SELECT COUNT(*) FROM PREINVERSION_ARI_OBSERVACIONES ";
      $query .=" WHERE (C_INSTITUCION*10000)+C_PREINVERSION= ".$this->c_preinversion;
      $query .=" AND ANO= ".$this->ano;
      $dataset  = mysql_query($query, $this->conector); 
      $record = mysql_fetch_row($dataset);
      return $record[0];
   }

   function existe_diferencia_iniciativa() {
      $query  =" SELECT COUNT(*) FROM DIFERENCIA_PREINVERSION_ARI ";
      $query .=" WHERE C_DIFERENCIA>0 AND (C_INSTITUCION*10000)+C_PREINVERSION= ".$this->c_preinversion;
      $query .=" AND ANO= ".$this->ano;
      $dataset  = mysql_query($query, $this->conector); 
      $record = mysql_fetch_row($dataset);
      return $record[0];
   }

   function cargar_iniciativa_institucion_sector_1() {
      $query  =" SELECT A.N_INSTITUCION, B.N_SECTOR from INSTITUCION A, SECTOR B ";
      $query .=" WHERE A.C_SECTOR = B.C_SECTOR AND A.C_INSTITUCION= ".$this->c_institucion; 
      $dataset  = mysql_query($query, $this->conector); 
      $this->nroregistros = mysql_num_rows($dataset);
      return $dataset;
   }

   function calcular_valor_maximo() {
      $query  = " SELECT MAX(C_PREINVERSION) FROM PREINVERSION_ARI";
      $query .= " WHERE C_INSTITUCION= ".$this->c_institucion;
      $query .= " AND ANO= ".$this->ano;
      $dataset  = mysql_query($query, $this->conector); 
      $this->nroregistros = mysql_num_rows($dataset);
      return $dataset;
   }

   function actualizar_iniciativa() {
      $query  = " REPLACE INTO PREINVERSION_ARI";
      $query .= " (ANO, C_INSTITUCION, C_PREINVERSION, N_PREINVERSION, UNIDAD_TECNICA, RESPONSABLE_PROPIR, C_PROGRAMA_ARI, ";
      $query .= " C_CLASIFICADOR_PRESUPUESTARIO, C_ETAPA_IDI, FECHA_INICIO, FECHA_TERMINO, C_ESTADO_PROYECTO, ";
      $query .= " PRODUCTO, CODIGO, C_TIPO_CODIGO, RATE, IMPACTOS, C_SECTOR, C_ESTADO_PREINVERSION, OBSERVACIONES, ";
      $query .= " BENEFICIARIOS, COSTO_TOTAL, GASTADO_ANOS_ANTERIORES, SOLICITADO, SALDO_PROXIMOS_ANOS, ";
      $query .= " FECHA_REGISTRO, C_USUARIO) VALUES('";
      $query .= $this->ano."','".$this->c_institucion."','".$this->c_preinversion."','".$this->n_preinversion."','".$this->unidad_tecnica."','".$this->responsable_propir."','".$this->c_programa_ari."','"; 
      $query .= $this->c_clasificador_presupuestario."','".$this->c_etapa_idi."','".$this->fecha_inicio."','".$this->fecha_termino."','".$this->c_estado_proyecto."','";
      $query .= $this->producto."','".$this->codigo."','".$this->c_tipo_codigo."','".$this->rate."','".$this->impactos."','".$this->c_sector."','".$this->c_estado_preinversion."','".$this->observaciones."','";
      $query .= $this->beneficiarios."','".$this->costo_total."','".$this->gastado_anos_anteriores."','".$this->solicitado."','".$this->saldo_proximos_anos."','";
      $query .= $this->fecha_registro."','".$this->c_usuario."')"; 
      $dataset  = mysql_query($query, $this->conector); 
      if (mysql_errno($this->conector) != 0) { 
         $this->mensaje_error="No se ha podido Actualizar la Base de Datosu, mensaje de error de Mysql es: " . mysql_error($this->conector).$query; 
      } else {$this->mensaje_error='';}   
      return $this->mensaje_error;
   }   

   function eliminar_iniciativa() {
      $query  =" DELETE FROM PREINVERSION_ARI";
      $query .=" WHERE (C_INSTITUCION*10000)+C_PREINVERSION= ".$this->c_preinversion;
      $query .=" AND ANO= ".$this->ano;
      $dataset  = mysql_query($query, $this->conector); 
      $query  =" DELETE FROM RELACION_ARI_PREINVERSION";
      $query .=" WHERE (C_INSTITUCION*10000)+C_PREINVERSION= ".$this->c_preinversion;
      $query .=" AND ANO= ".$this->ano;
      $dataset  = mysql_query($query, $this->conector); 
      if (mysql_errno($this->conector) != 0) { 
         $this->mensaje_error="No se ha podido Actualizar la Base de Datos, mensaje de error de Mysql es: " . mysql_error($this->conector).$query; 
      } else {$this->mensaje_error='';}   
      return $this->mensaje_error;
   }   

   function cambiar_estado_iniciativa($xestado) {
      $query  =" UPDATE PREINVERSION_ARI SET C_ESTADO_PREINVERSION=".$xestado;
      $query .=" WHERE (C_INSTITUCION*10000)+C_PREINVERSION= ".$this->c_preinversion;
      $query .=" AND ANO= ".$this->ano;
      $dataset  = mysql_query($query, $this->conector); 
      if (mysql_errno($this->conector) != 0) { 
         $this->mensaje_error="No se ha podido Actualizar la Base de Datos, mensaje de error de Mysql es: " . mysql_error($this->conector).$query; 
      } else {$this->mensaje_error='';}   
      return $this->mensaje_error;
   }   

   function ingresar_observacion($xfecha,$xobservacion) {
      $query  =" SELECT ANO, C_INSTITUCION, C_PREINVERSION FROM PREINVERSION_ARI";
      $query .=" WHERE (C_INSTITUCION*10000)+C_PREINVERSION= ".$this->c_preinversion;
      $query .=" AND ANO= ".$this->ano;
      $dataset  = mysql_query($query, $this->conector); 
      $record = mysql_fetch_row($dataset);
      $query1  = " INSERT INTO PREINVERSION_ARI_OBSERVACIONES VALUES('";
      $query1 .= $record[0]."','".$record[1]."','".$record[2]."','".$xfecha."','".$xobservacion."','";
      $query1 .= $this->fecha_registro."','".$this->c_usuario."')";
      $dataset1  = mysql_query($query1, $this->conector);      
      if (mysql_errno($this->conector) != 0) { 
         $this->mensaje_error="No se ha podido Actualizar la Base de Datos, mensaje de error de Mysql es: " . mysql_error($this->conector).$query1; 
      } else {$this->mensaje_error='';}   
      return $this->mensaje_error;
   }   

   function ingresar_diferencia($xdiferencia,$xobservacion) {
      $query  =" SELECT ANO, C_INSTITUCION, C_PREINVERSION FROM PREINVERSION_ARI";
      $query .=" WHERE (C_INSTITUCION*10000)+C_PREINVERSION= ".$this->c_preinversion;
      $query .=" AND ANO= ".$this->ano;
      $dataset  = mysql_query($query, $this->conector); 
      $record = mysql_fetch_row($dataset);
      $query1  = " INSERT INTO DIFERENCIA_PREINVERSION_ARI VALUES('";
      $query1 .= $record[0]."','".$record[1]."','".$record[2]."','".$xdiferencia."','".$xobservacion."')";
      $dataset1  = mysql_query($query1, $this->conector);      
      if (mysql_errno($this->conector) != 0) { 
         $this->mensaje_error="No se ha podido Actualizar la Base de Datos, mensaje de error de Mysql es: " . mysql_error($this->conector).$query1; 
      } else {$this->mensaje_error='';}   
      return $this->mensaje_error;
   }   

   function cargar_iniciativa($xnano,$xsector,$xinstitucion,$xaccesoinstituciones,$xestado,$xpreinversionsel,$xexcel) {
      $fila=0;$this->filaseleccionada=-1;$total=0;
      $query  =" SELECT if (A.CODIGO!='',if(A.C_TIPO_CODIGO=1,CONCAT(A.CODIGO,'<br>(BIP)'),CONCAT(A.CODIGO,'')),''), L.N_RATE, A.N_PREINVERSION, ";
      $query .=" CONCAT(M.N_CLASIFICADOR,' (',M.C_CLASIFICADOR,')'), N.N_ETAPA_IDI,";
      $query .=" IF (E.C_NIVEL_UT=1,'Regional',IF (E.C_NIVEL_UT=2,CONCAT('Provincial (',E.UBICACION_ESPECIFICA_NOMBRE,')'),IF (E.C_NIVEL_UT=3,CONCAT('Comunal (',E.UBICACION_ESPECIFICA_NOMBRE,')'),CONCAT('Localidades (',E.UBICACION_ESPECIFICA_NOMBRE,')')))),";	  
      $query .=" B.N_SECTOR, C.N_INSTITUCION, SUM(X.SOLICITADO),  Y.N_ESTADO_PREINVERSION, (A.C_INSTITUCION*10000)+A.C_PREINVERSION AS ID, A.C_ESTADO_PROYECTO, A.ANO, A.C_INSTITUCION, A.C_PREINVERSION, A.C_ESTADO_PREINVERSION ";
      $query .=" , W.C_DIFERENCIA, P.N_INSTITUCION, SUM(X.COSTO_TOTAL), SUM(X.GASTADO_ANOS_ANTERIORES), SUM(X.SALDO_PROXIMOS_ANOS),E.AREA_INFLUENCIA, W.OBSERVACIONES ";
      $query .=" ,G.C_PREINVERSION1 ";
      $query .=" FROM INSTITUCION C, INSTITUCION P, ESTADO_PREINVERSION Y, PREINVERSION_ARI A ";
      $query .=" LEFT JOIN UBICACION_TERRITORIAL_PREINVERSION_ARI E ON (A.ANO=E.ANO AND A.C_INSTITUCION=E.C_INSTITUCION AND A.C_PREINVERSION=E.C_PREINVERSION)";
      $query .=" LEFT JOIN FINANCIAMIENTO_PREINVERSION_ARI X ON (A.ANO=X.ANO AND A.C_INSTITUCION=X.C_INSTITUCION AND A.C_PREINVERSION=X.C_PREINVERSION)";
      $query .=" LEFT JOIN DIFERENCIA_PREINVERSION_ARI W ON (A.ANO=W.ANO AND A.C_INSTITUCION=W.C_INSTITUCION AND A.C_PREINVERSION=W.C_PREINVERSION)"; 
      $query .=" LEFT JOIN RATE L ON (A.RATE=L.C_RATE)";
      $query .=" LEFT JOIN TCLASIFICADOR_PRESUPUESTARIO M ON (A.C_CLASIFICADOR_PRESUPUESTARIO=M.C_CLASIFICADOR AND A.ANO=M.ANIO AND (A.C_INSTITUCION=M.INSTITUCION OR M.INSTITUCION=0))";
      $query .=" LEFT JOIN ETAPA_IDI N ON (A.C_ETAPA_IDI=N.C_ETAPA_IDI)";
      $query .=" LEFT JOIN SECTOR B ON (A.C_SECTOR=B.C_SECTOR)";
      $query .=" LEFT JOIN RELACION_ARI_PREINVERSION G ON(A.ANO=G.ANO AND A.C_INSTITUCION=G.C_INSTITUCION AND A.C_PREINVERSION=G.C_PREINVERSION)";
      $query .=" WHERE A.ANO=".$xnano;
      $query .=" AND A.C_INSTITUCION=C.C_INSTITUCION AND A.UNIDAD_TECNICA=P.C_INSTITUCION AND A.C_ESTADO_PREINVERSION=Y.C_ESTADO_PREINVERSION";
      if ($xsector>0) { $query .= " AND A.C_SECTOR = ".$xsector;}
      if ($xinstitucion>0) { 
	 $query .= " AND (A.C_INSTITUCION = ".$xinstitucion." OR A.RESPONSABLE_PROPIR = ".$xinstitucion.")";
      } else {
	 if ($xaccesoinstituciones!='999') {
	    $query .= " AND (A.C_INSTITUCION IN (".$xaccesoinstituciones.") OR A.RESPONSABLE_PROPIR IN (".$xaccesoinstituciones."))";
	 }
      }
      if ($xestado>0) { $query .= " AND A.C_ESTADO_PREINVERSION = ".$xestado;}
      $query .=" GROUP BY A.CODIGO, A.N_PREINVERSION, A.ANO, A.C_INSTITUCION, A.C_PREINVERSION ";
      $query .=" ORDER BY C.N_INSTITUCION, A.CODIGO, A.N_PREINVERSION ";
      $dataset= mysql_query($query, $this->conector); 
      $this->nroregistros = mysql_num_rows($dataset);
      $total1=0;$total2=0;$total3=0;
      if ($xexcel!=1) {
         $rows = array(); 
         while ($record = mysql_fetch_row($dataset)) { 
	     $cols = array();$total=round($total+$record[8]);
            $total1=round($total1+$record[18]);
            $total2=round($total2+$record[19]);
            $total3=round($total3+$record[20]);
	     $colsel=0;
	     foreach ($record as $value) { 
	        if ($colsel==16) {
                  if ($value==0) {$value="Sin Diferencia";}
                  if ($value==1) {$value="Dif.Presupuestaria";}
                  if ($value==2) {$value="Dif.Prior.Política";}
                  if ($value==3) {$value="Dif. Localización";}
                  if ($value==4) {$value="Dif. Beneficiarios";}
                  if ($value==5) {$value="Dif. Fecha Inicio";}
                  if ($value==6) {$value="Otra Diferencia";}
               }
	        if ($colsel==21) {
                  if ($value==0) {$value="No definida";}
                  if ($value==1) {$value="Internacional";}
                  if ($value==2) {$value="Nacional";}
                  if ($value==3) {$value="Regional";}
                  if ($value==4) {$value="Provincial";}
                  if ($value==5) {$value="Comunal";}
               }
               if ($colsel==22) {$value=ereg_replace("[^/( )A-Za-z0-9_áéíóúÁÉÍÓÚ]", "", $value);}
	        if ($colsel==23) {
             if ($value>0) {$value="SI";}
          }        
	        $cols[] = '"'.addslashes($value).'"';
	        if ($colsel==10) {
	           if ($value==$xpreinversionsel) {$this->filaseleccionada=$fila;}
	        }	      
     	        $colsel++;      	
	     } 
	     $rows[] = "\t[".implode(",", $cols)."]"; 
	     $fila++;
          } 
          $datagrilla="[\n".implode(",\n",$rows)."\n];\n"; 
          $this->total=number_format($total,0,',','.');
          $this->total1=number_format($total1,0,',','.');
          $this->total2=number_format($total2,0,',','.');
          $this->total3=number_format($total3,0,',','.'); 	
          return $datagrilla;
      } else {
         $fila=0;
         while ($record = mysql_fetch_row($dataset)) { 
	     $total=round($total+$record[8]);
            $total1=round($total1+$record[18]);
            $total2=round($total2+$record[19]);
            $total3=round($total3+$record[20]);
            if ($record[16]==0) {$record[16]="Sin Diferencia";}
            if ($record[16]==1) {$record[16]="Dif.Presupuestaria";}
            if ($record[16]==2) {$record[16]="Dif.Prior.Política";}
            if ($record[16]==3) {$record[16]="Dif. Localización";}
            if ($record[16]==4) {$record[16]="Dif. Beneficiarios";}
            if ($record[16]==5) {$record[16]="Dif. Fecha Inicio";}
            if ($record[16]==6) {$record[16]="Otra Diferencia";}
            if ($record[21]==0) {$record[21]="No definida";}
            if ($record[21]==1) {$record[21]="Internacional";}
            if ($record[21]==2) {$record[21]="Nacional";}
            if ($record[21]==3) {$record[21]="Regional";}
            if ($record[21]==4) {$record[21]="Provincial";}
            if ($record[21]==5) {$record[21]="Comunal";}
            $this->datainforme[$fila][0]=$record[0];
            $this->datainforme[$fila][1]=$record[1];
            $this->datainforme[$fila][2]=$record[2];
            $this->datainforme[$fila][3]=$record[3];
            $this->datainforme[$fila][4]=$record[4];
            $this->datainforme[$fila][5]=$record[5];
            $this->datainforme[$fila][6]=$record[21];
            $this->datainforme[$fila][7]=$record[6];
            $this->datainforme[$fila][8]=$record[7];
            $this->datainforme[$fila][9]=$record[17]; 
            $this->datainforme[$fila][10]=$record[18];
            $this->datainforme[$fila][11]=$record[19];
            $this->datainforme[$fila][12]=$record[8];
            $this->datainforme[$fila][13]=$record[20];
            $this->datainforme[$fila][14]=$record[16];
            $this->datainforme[$fila][15]=$record[9];
            $this->datainforme[$fila][16]=$record[23];
	     $fila++;
         }
         $this->datainforme[$fila][2]="Totales";
         $this->datainforme[$fila][12]=$total;
         $this->datainforme[$fila][10]=$total1;
         $this->datainforme[$fila][11]=$total2;
         $this->datainforme[$fila][13]=$total3;
         $this->nroregistros=$fila;
         return $this->datainforme;
      }
   }
   
   function get_nroregistros(){ return $this->nroregistros;}	
   
   function get_error()       { return $this->mensaje_error;}
      	
   function get_filaseleccionada()       { return $this->filaseleccionada;}

   function ingresa_georeferencia_ari($contenedor_array_ari){
 
    /* NUEVO georeferenciacion */

    // elimina la georeferenciacion actual por si tiene
    $d="DELETE FROM PREINVERSION_ARI_GEOREF WHERE ANO=" . $this->ano . " AND C_INSTITUCION=" . $this->c_institucion . " AND C_PREINVERSION=" . $this->c_preinversion;
    mysql_query($d, $this->conector);
    $d="DELETE FROM PREINVERSION_ARI_GEOREF_COOR  WHERE ANO=" . $this->ano . " AND C_INSTITUCION=" . $this->c_institucion . " AND C_PREINVERSION=" . $this->c_preinversion;
    mysql_query($d, $this->conector);
    // se inserta la nueva georeferenciacion

    $data=json_decode($contenedor_array_ari);    
    if(count($data)>0) // si hay datos
    {          
       $grafico=0; // un punto
       if(count($data)>1) $grafico=1; // una forma
       $query_georef_ari = "INSERT INTO PREINVERSION_ARI_GEOREF (ANO, C_INSTITUCION, C_PREINVERSION, ELEMENTO_GRAFICO, CODIGO_OGC) VALUES ('".$this->ano."', '".$this->c_institucion."', '".$this->c_preinversion."', '". $grafico."', '".$data[0]->zona."')";   
       $orden=1;
       foreach ($data as $val) {       
           $query_georef_coor_ari    = "INSERT INTO PREINVERSION_ARI_GEOREF_COOR (ANO, C_INSTITUCION, C_PREINVERSION, ORDEN, EJEX, EJEY) VALUES ('".$this->ano."', '".$this->c_institucion."', '".$this->c_preinversion."', '".$orden."', '".$val->x."', '".$val->y."')";
           $dataset_georef_coor_ari  = mysql_query($query_georef_coor_ari, $this->conector);
           $orden++;
       }
       $dataset_georef_ari  = mysql_query($query_georef_ari, $this->conector);    
    }
    return true;

    /* FIN Nuevo georeferenciacion */

    /* Modificacion georeferenciacion */

    /*
    $query_georef_ari = "INSERT INTO PREINVERSION_ARI_GEOREF (ANO, C_INSTITUCION, C_PREINVERSION, ELEMENTO_GRAFICO, CODIGO_OGC) VALUES ('".$this->ano."', '".$this->c_institucion."', '".$this->c_preinversion."', '". $contenedor_array_ari['elementoGrafico']."', '".$contenedor_array_ari['codigoOCG']."')";
   
    foreach ($contenedor_array_ari['coordenadas'] as $key => $value) {
        
        $query_georef_coor_ari    = "INSERT INTO PREINVERSION_ARI_GEOREF_COOR (ANO, C_INSTITUCION, C_PREINVERSION, ORDEN, EJEX, EJEY) VALUES ('".$this->ano."', '".$this->c_institucion."', '".$this->c_preinversion."', '".$contenedor_array_ari['coordenadas'][$key]['orden']."', '".$contenedor_array_ari['coordenadas'][$key]['ejeX']."', '".$contenedor_array_ari['coordenadas'][$key]['ejeY']."')";
        $dataset_georef_coor_ari  = mysql_query($query_georef_coor_ari, $this->conector);
    }
    $dataset_georef_ari  = mysql_query($query_georef_ari, $this->conector);
    return true;
    */

    /* Modificación georeferenciacion*/

   }

   function carga_georeferencia_ari(){

      /* Insertar para la georeferenciacion */
      $query  ="SELECT CODIGO_OGC FROM PREINVERSION_ARI_GEOREF WHERE (C_INSTITUCION*10000)+C_PREINVERSION= $this->c_preinversion AND ANO= $this->ano LIMIT 1";      
      $dataset  = mysql_query($query, $this->conector); 
      if(mysql_num_rows($dataset)>0) $R=mysql_fetch_array($dataset);
      $zona=(string)$R['CODIGO_OGC'];
      $forma=array();
      /* Fin nueva georeferenciacion */

      $query_coo  ="SELECT ANO, C_INSTITUCION, C_PREINVERSION, ORDEN, EJEX, EJEY FROM PREINVERSION_ARI_GEOREF_COOR WHERE (C_INSTITUCION*10000)+C_PREINVERSION= $this->c_preinversion AND ANO= $this->ano ORDER BY ORDEN";      
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
}

//**********************************************************************

// UBICACION TERRITORIAL INICIATIVAS ARI
   
Class Ubicacion_Territorial_Iniciativa_Ari {
   var $conector;
   var $mensaje_error;
   var $nroregistros;
   var $ano;  
   var $c_institucion;
   var $c_preinversion;
   var $c_nivel_ut;
   var $ubicacion_general="";
   var $ubicacion_especifica="";
   var $ubicacion_especifica_nombre="";
   var $area_influencia;
   var $usuario;
   var $estado;
   
   function Ubicacion_Territorial_Iniciativa_Ari($conector) { 
      $this->conector =$conector;
      $this->mensaje_error='';
      $this->estado=0;
      $this->nroregistros=0;
   }
   
   function cargar_ubicacion_territorial() {
      $query  =" SELECT C_NIVEL_UT, UBICACION_GENERAL, UBICACION_ESPECIFICA, UBICACION_ESPECIFICA_NOMBRE, AREA_INFLUENCIA ";
      $query .=" FROM UBICACION_TERRITORIAL_PREINVERSION_ARI ";
      $query .=" WHERE (C_INSTITUCION*10000)+C_PREINVERSION= ".$this->c_preinversion;
      $query .=" AND ANO= ".$this->ano;
      $dataset  = mysql_query($query, $this->conector); 
      $this->nroregistros = mysql_num_rows($dataset);
      return $dataset;
   }

   function eliminar_ubicacion_territorial() {
      $query  = " DELETE FROM UBICACION_TERRITORIAL_PREINVERSION_ARI";
      $query .= " WHERE ANO=".$this->ano." AND C_INSTITUCION=".$this->c_institucion." AND C_PREINVERSION=".$this->c_preinversion;
      $dataset  = mysql_query($query, $this->conector); 
      if (mysql_errno($this->conector) != 0) { 
         $this->mensaje_error="No se ha podido Actualizar la Base de Datos, mensaje de error de Mysql es: " . mysql_error($this->conector).$query; 
      } else {$this->mensaje_error='';}   
      return $this->mensaje_error;
   }
   
   function eliminar_ubicacion_territorial_1() {
      $query  =" DELETE FROM UBICACION_TERRITORIAL_PREINVERSION_ARI";
      $query .=" WHERE (C_INSTITUCION*10000)+C_PREINVERSION= ".$this->c_preinversion;
      $query .=" AND ANO= ".$this->ano;
      $dataset  = mysql_query($query, $this->conector); 
      if (mysql_errno($this->conector) != 0) { 
         $this->mensaje_error="No se ha podido Actualizar la Base de Datos, mensaje de error de Mysql es: " . mysql_error($this->conector).$query; 
      } else {$this->mensaje_error='';}   
      return $this->mensaje_error;
   }

   function actualizar_ubicacion_territorial() {
      $query  = " REPLACE INTO UBICACION_TERRITORIAL_PREINVERSION_ARI (ANO, C_INSTITUCION, C_PREINVERSION, C_NIVEL_UT, UBICACION_GENERAL, UBICACION_ESPECIFICA, UBICACION_ESPECIFICA_NOMBRE, AREA_INFLUENCIA) VALUES(";
      $query .= "'".$this->ano."','".$this->c_institucion."','".$this->c_preinversion."',";
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

// RELACION INSTRUMENTOS INICIATIVA ARI
   
Class Relacion_Instrumentos_Iniciativa_Ari {
   var $conector;
   var $mensaje_error;
   var $nroregistros;
   var $ano;
   var $c_institucion;
   var $c_preinversion;
   var $c_instrumento;
   var $relacionado="";
   var $relacion1="";
   var $relacion2="";
   var $relacion1_nombre="";
   var $relacion2_nombre="";
   var $usuario;
   var $estado;
   
   function Relacion_Instrumentos_Iniciativa_Ari($conector) { 
      $this->conector =$conector;
      $this->mensaje_error='';
      $this->estado=0;
      $this->nroregistros=0;
   }
   
   function eliminar_relacion_instrumentos() {
      $query  = " DELETE FROM RELACION_INSTRUMENTO_PREINVERSION_ARI";
      $query .= " WHERE ANO=".$this->ano." AND C_INSTITUCION=".$this->c_institucion." AND C_PREINVERSION=".$this->c_preinversion;
      $dataset  = mysql_query($query, $this->conector); 
      if (mysql_errno($this->conector) != 0) { 
         $this->mensaje_error="No se ha podido Actualizar la Base de Datos, mensaje de error de Mysql es: " . mysql_error($this->conector).$query; 
      } else {$this->mensaje_error='';}   
      return $this->mensaje_error;
   }
   
   function eliminar_relacion_instrumentos_1() {
      $query  =" DELETE FROM RELACION_INSTRUMENTO_PREINVERSION_ARI";
      $query .=" WHERE (C_INSTITUCION*10000)+C_PREINVERSION= ".$this->c_preinversion;
      $query .=" AND ANO= ".$this->ano;
      $dataset  = mysql_query($query, $this->conector); 
      if (mysql_errno($this->conector) != 0) { 
         $this->mensaje_error="No se ha podido Actualizar la Base de Datos, mensaje de error de Mysql es: " . mysql_error($this->conector).$query; 
      } else {$this->mensaje_error='';}   
      return $this->mensaje_error;
   }
   function actualizar_relacion_instrumentos() {
      $query  = " REPLACE INTO RELACION_INSTRUMENTO_PREINVERSION_ARI (ANO, C_INSTITUCION, C_PREINVERSION, C_INSTRUMENTO, RELACIONADO, RELACION1, RELACION2,RELACION1_NOMBRE, RELACION2_NOMBRE) VALUES(";	    
      $query .= "'".$this->ano."','".$this->c_institucion."','".$this->c_preinversion."',";
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

//**********************************************************************

// FINANCIAMIENTO INICIATIVAS ARI
   
Class Financiamiento_Iniciativa_Ari {
   var $conector;
   var $mensaje_error;
   var $nroregistros;
   var $ano;  
   var $c_institucion;
   var $c_preinversion;
   var $c_fuente_financiamiento;
   var $costo_total;
   var $gastado_anos_anteriores;
   var $solicitado;
   var $saldo_proximos_anos;
   var $usuario;
   var $estado;
   var $vector="";
   
   function Financiamiento_Iniciativa_Ari($conector) { 
      $this->conector =$conector;
      $this->mensaje_error='';
      $this->estado=0;
      $this->nroregistros=0;
   }
   
   function cargar_fuentes_financiamiento() {
      $query  =" SELECT B.N_FUENTE_FINANCIAMIENTO, A.COSTO_TOTAL, A.GASTADO_ANOS_ANTERIORES, A.SOLICITADO,A.SALDO_PROXIMOS_ANOS, B.C_FUENTE_FINANCIAMIENTO ";
      $query .=" FROM FUENTE_FINANCIAMIENTO B, FINANCIAMIENTO_PREINVERSION_ARI A WHERE B.C_FUENTE_FINANCIAMIENTO=A.C_FUENTE_FINANCIAMIENTO "; 
      $query .=" AND (A.C_INSTITUCION*10000)+A.C_PREINVERSION= ".$this->c_preinversion;
      $query .=" AND A.ANO= ".$this->ano;
      $dataset  = mysql_query($query, $this->conector); 
      $this->nroregistros = mysql_num_rows($dataset);
      return $dataset;
   }

   function eliminar_fuentes_financiamiento() {
      $query  = " DELETE FROM FINANCIAMIENTO_PREINVERSION_ARI";
      $query .= " WHERE ANO=".$this->ano." AND C_INSTITUCION=".$this->c_institucion." AND C_PREINVERSION=".$this->c_preinversion;
      $dataset  = mysql_query($query, $this->conector); 
      if (mysql_errno($this->conector) != 0) { 
         $this->mensaje_error="No se ha podido Actualizar la Base de Datosu, mensaje de error de Mysql es: " . mysql_error($this->conector).$query; 
      } else {$this->mensaje_error='';}   
      return $this->mensaje_error;
   }
   
   function eliminar_fuentes_financiamiento_1() {
      $query  =" DELETE FROM FINANCIAMIENTO_PREINVERSION_ARI";
      $query .=" WHERE (C_INSTITUCION*10000)+C_PREINVERSION= ".$this->c_preinversion;
      $query .=" AND ANO= ".$this->ano;
      $dataset  = mysql_query($query, $this->conector); 
      if (mysql_errno($this->conector) != 0) { 
         $this->mensaje_error="No se ha podido Actualizar la Base de Datosu, mensaje de error de Mysql es: " . mysql_error($this->conector).$query; 
      } else {$this->mensaje_error='';}   
      return $this->mensaje_error;
   }

   function actualizar_fuentes_financiamiento() {
      $query  =" REPLACE INTO FINANCIAMIENTO_PREINVERSION_ARI VALUES ('";
      $query .=$this->ano."','".$this->c_institucion."','".$this->c_preinversion."',".$this->vector.")";
      $dataset  = mysql_query($query, $this->conector);
      if (mysql_errno($this->conector) != 0) { 
         $this->mensaje_error="No se ha podido Actualizar la Base de Datosu, mensaje de error de Mysql es: " . mysql_error($this->conector).$query; 
      } else {$this->mensaje_error='';}   
      return $this->mensaje_error;
   }

   function get_nroregistros(){ return $this->nroregistros;}	
   
   function get_error()       { return $this->mensaje_error;}
    	
}

//**********************************************************************

// BENEFICIARIOS INICIATIVAS ARI
   
Class Beneficiario_Iniciativa_Ari {
   var $conector;
   var $mensaje_error;
   var $nroregistros;
   var $ano;  
   var $c_institucion;
   var $c_preinversion;
   var $c_grupo_beneficiario;
   var $hombres;
   var $mujeres;
   var $ambos;
   var $indirectos;
   var $usuario;
   var $estado;
   var $vector="";
   
   function Beneficiario_Iniciativa_Ari($conector) { 
      $this->conector =$conector;
      $this->mensaje_error='';
      $this->estado=0;
      $this->nroregistros=0;
   }
   
   function cargar_beneficiarios() {
      $query  =" SELECT B.N_GRUPO_BENEFICIARIO,A.HOMBRES,A.MUJERES, A.AMBOS, A.MUJERES+A.HOMBRES+A.AMBOS,A.INDIRECTOS, B.C_GRUPO_BENEFICIARIO ";
      $query .=" FROM GRUPO_BENEFICIARIO B, BENEFICIARIO_PREINVERSION_ARI A WHERE B.C_GRUPO_BENEFICIARIO=A.C_GRUPO_BENEFICIARIO "; 
      $query .=" AND (A.C_INSTITUCION*10000)+A.C_PREINVERSION= ".$this->c_preinversion;
      $query .=" AND A.ANO= ".$this->ano;
      $dataset  = mysql_query($query, $this->conector); 
      $this->nroregistros = mysql_num_rows($dataset);
      return $dataset;
   }

   function eliminar_beneficiarios() {
      $query  = " DELETE FROM BENEFICIARIO_PREINVERSION_ARI";
      $query .= " WHERE ANO=".$this->ano." AND C_INSTITUCION=".$this->c_institucion." AND C_PREINVERSION=".$this->c_preinversion;
      $dataset  = mysql_query($query, $this->conector); 
      if (mysql_errno($this->conector) != 0) { 
         $this->mensaje_error="No se ha podido Actualizar la Base de Datosu, mensaje de error de Mysql es: " . mysql_error($this->conector).$query; 
      } else {$this->mensaje_error='';}   
      return $this->mensaje_error;
   }
   
   function eliminar_beneficiarios_1() {
      $query  =" DELETE FROM BENEFICIARIO_PREINVERSION_ARI";
      $query .=" WHERE (C_INSTITUCION*10000)+C_PREINVERSION= ".$this->c_preinversion;
      $query .=" AND ANO= ".$this->ano;
      $dataset  = mysql_query($query, $this->conector); 
      if (mysql_errno($this->conector) != 0) { 
         $this->mensaje_error="No se ha podido Actualizar la Base de Datosu, mensaje de error de Mysql es: " . mysql_error($this->conector).$query; 
      } else {$this->mensaje_error='';}   
      return $this->mensaje_error;
   }

   function actualizar_beneficiarios() {
      $query  =" REPLACE INTO BENEFICIARIO_PREINVERSION_ARI VALUES ('";
      $query .= $this->ano."','".$this->c_institucion."','".$this->c_preinversion."',".$this->vector.")";
      $dataset  = mysql_query($query, $this->conector);
      if (mysql_errno($this->conector) != 0) { 
         $this->mensaje_error="No se ha podido Actualizar la Base de Datosu, mensaje de error de Mysql es: " . mysql_error($this->conector).$query; 
      } else {$this->mensaje_error='';}   
      return $this->mensaje_error;
   }

   function get_nroregistros(){ return $this->nroregistros;}	
   
   function get_error()       { return $this->mensaje_error;}
    	
}

?>