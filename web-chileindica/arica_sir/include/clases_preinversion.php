<?
/*********************************************************************
    Entidad Preinversion
**********************************************************************/
// ALTER TABLE PREINVERSION ADD COLUMN `C_ETAPA` INTEGER(1) UNSIGNED DEFAULT '0' AFTER `C_USUARIO`;

Class Preinversion {
   var $conector;
   var $mensaje_error;
   var $nroregistros;
   var $ano;  
   var $c_institucion;
   var $c_preinversion;
   var $n_preinversion="";
   var $unidad_tecnica;
   var $c_clasificador_presupuestario;
   var $c_via_financiamiento;
   var $fecha_inicio;
   var $fecha_termino;
   var $producto="";
   var $codigo="";
   var $codigo_dv="";
   var $impactos="";
   var $c_tipo="";
   var $c_sector;
   var $c_estado_preinversion;
   var $observaciones="";
   var $beneficiarios="";
   var $costo_total;
   var $solicitado;
   var $saldo_proximos_anos;
   var $fecha_registro;
   var $c_usuario;
   var $filaseleccionada;
   var $total;
   var $total1;
   var $total2;
   var $total3;
   var $c_etapa;
   var $c_estado_destino;

   var $direccion;
   var $justificacion;
   
   var $c_analista;
   var $fecha_asignacion;
   var $prioridad;
   var $plazo_analisis;
   var $nro_documento="";
   var $fecha_documento;
   var $observaciones_recepcion="";

   var $numero_oficio = "";
   var $fecha_oficio = "";
   var $fecha_parte;
   var $numero_parte;
   var $respuesta_municipio = "";
   var $id_observacion;
   var $etapa_actual;
   var $estado_actual;
   var $estado_actual_iniciativa;
   var $georeferencia;
   var $rate ="";
   
   var $nombre_formulador;
   var $telefono;
   var $correo_electronico;

   function Preinversion($conector){ 
      $this->conector =$conector;
      $this->mensaje_error='';
      $this->estado=0;
      $this->nroregistros=0;
   }
   
   function asignar_iniciativa() {
      $query  = " REPLACE INTO PREINVERSION_ASIGNACION ";
      $query .= " (ANO, C_PREINVERSION, C_ANALISTA, FECHA_ASIGNACION, PRIORIDAD, ";
      $query .= " PLAZO_ANALISIS, NRO_DOCUMENTO, FECHA_DOCUMENTO, OBSERVACIONES, ";
      $query .= " C_ASIGNADOR, C_ESTADO, C_ESTADO_DESTINO, C_INSTITUCION) VALUES ('";
      $query .= $this->ano."','";
      $query .= $this->c_preinversion."','";
      $query .= $this->c_analista."','";
      $query .= $this->fecha_asignacion."','";
      $query .= $this->prioridad."','";
      $query .= $this->plazo_analisis."','";
      $query .= $this->nro_documento."','";
      $query .= $this->fecha_documento."','";
      $query .= $this->observaciones_recepcion."','";
      $query .= $this->c_usuario."','";
      $query .= $this->estado_actual_iniciativa."','";
      $query .= $this->c_estado_destino."','";      
      $query .= $this->c_institucion."');";
      
		//	echo "<script type='text/javascript'>alert('{$query}');</script>";
		     
      error_log($query);
      $dataset  = mysql_query($query, $this->conector); 
      
      $query  = " UPDATE PREINVERSION SET C_ESTADO_PREINVERSION=".$this->estado;
      $query .= " WHERE ANO=".$this->ano." AND C_PREINVERSION=".$this->c_preinversion." AND C_INSTITUCION=".$this->c_institucion;
      $dataset  = mysql_query($query, $this->conector) ; 

      // Notifiacion email
      $this->envia_email($this->estado);

      // Registrar cambio de estado
      //realiza esto en la tabla UPDATE PREINVERSION SET F_CAMBIO_ESTADO = CURRENT_TIMESTAMP del proyecto seleccionado
      $this->registra_cambio_estado();
   }

function asignar_iniciativa_postulada() {
     // para asignar evaluador a inicitivas en estado Postulada y que tienen que ser Recepcionadas por los analistas
      $query  = " REPLACE INTO PREINVERSION_ASIGNACION ";
      $query .= " (ANO, C_PREINVERSION, C_ANALISTA, FECHA_ASIGNACION, PRIORIDAD, ";
      $query .= " PLAZO_ANALISIS, NRO_DOCUMENTO, FECHA_DOCUMENTO, OBSERVACIONES, ";
      $query .= " C_ASIGNADOR, C_ESTADO, C_ESTADO_DESTINO, C_INSTITUCION) VALUES ('";
      $query .= $this->ano."','";
      $query .= $this->c_preinversion."','";
      $query .= $this->c_analista."','";
      $query .= $this->fecha_asignacion."','";
      $query .= $this->prioridad."','";
      $query .= $this->plazo_analisis."','";
      $query .= $this->nro_documento."','";
      $query .= $this->fecha_documento."','";
      $query .= $this->observaciones."','";
      $query .= $this->c_usuario."','";
      $query .= $this->estado_actual_iniciativa."','";
      $query .= $this->c_estado_destino."','";
      $query .= $this->c_institucion."');";
      
      //die(print_r($query)); 
      $dataset  = mysql_query($query, $this->conector); 
      
      //$query  = " UPDATE PREINVERSION SET C_ESTADO_PREINVERSION=".$this->estado;
      //$query .= " WHERE ANO=".$this->ano." AND C_PREINVERSION=".$this->c_preinversion;
      //$dataset  = mysql_query($query, $this->conector); 

      // Notifiacion email
      //$this->envia_email($this->estado);

      // Registrar cambio de estado
      //$this->registra_cambio_estado();
}


   /**
   * Carga el codigo de item presupuestario
   * 
   * @param int $clasificador
   */
  function get_codigo_clasificador( $clasificador )
  {
    $query    = " SELECT C_CLASIFICADOR FROM PREINVERSION_VIA_FINANCIAMIENTO
            WHERE C_VIA_FINANCIAMIENTO =" . $clasificador . " AND 
            ANIO = ".$this->ano;
    $dataset  = mysql_query($query, $this->conector);

    $codigo   = mysql_fetch_assoc($dataset);
    return  $codigo;
  }

  /**
   * Carga los datos para crear la table de la ficha preinversion
   * subtitulo item, etc
   * 
   * @param int $clasificador: id del clasificador
   */
  function get_items_clasificador( $clasificador )
  {

    $items = array();
    $query  = " SELECT N_CLASIFICADOR, C_CLASIFICADOR FROM TCLASIFICADOR_PRESUPUESTARIO 
          WHERE C_CLASIFICADOR LIKE '".$clasificador."%' 
          AND ANIO = " . $this->ano . "
          AND INICIAL = 2
          AND (INSTITUCION  = 1 OR INSTITUCION =  0)
          AND PREINVERSION = 1";
    $dataset  = mysql_query($query, $this->conector);
    while ($value = mysql_fetch_assoc($dataset)) {
      $items[]  = $value;
    }

    return $items;

  }

  /**
   * retorna las unidades tecnicas para mostrarlas en la ficha de preinversion
   * como un select
   */
  function get_unidades_tecnicas()
  {
    $unidad = array();
    $unidad[] = array('C_INSTITUCION' => 0, 'N_INSTITUCION' => '- Seleccione -');

    $query  = " SELECT C_INSTITUCION, N_INSTITUCION FROM INSTITUCION
            WHERE UTECNICA = 'S' ORDER BY N_INSTITUCION";

    $dataset  = mysql_query($query, $this->conector);

    while ( $value = mysql_fetch_assoc($dataset) ) {
      $unidad[] = $value;
    }

    return $unidad;
  }

  function clean_detalle_financiamiento()
  {
     $borrar   = " DELETE FROM PREINVERSION_DETALLE_FINANCIAMIENTO 
                  WHERE C_PREINVERSION = ".$this->c_preinversion . "
                   AND ANO =".$this->ano." AND C_INSTITUCION=".$this->c_institucion;
     $res      = mysql_query($borrar, $this->conector);
  }
  /**
   * Agrega el detalle de las vias de financimiento a la tabla
   * PREINVERSION_DETALLE_FINANCIAMIENTO
   * 
   * @param string $clasificador
   * @param int $solicitado
   * @param int $saldo
   * @param int $total
   * @param int $unidad
   * @param int $id que se incrementa a medida que se ingresan mas asignaciones por proyecto
   */
  function add_detalle_financiamiento($clasificador, $solicitado, $saldo, $total, $unidad,$id)
  {
   

    $query    = " INSERT INTO PREINVERSION_DETALLE_FINANCIAMIENTO(
          C_PREINVERSION, 
          ANO, 
          C_USUARIO, 
          C_CLASIFICADOR, 
          SOLICITADO_ANO_ACTUAL, 
          SALDO_POR_INVERTIR, 
          COSTO_TOTAL,
          UNIDAD_TECNICA, 
          C_INSTITUCION,
          ID
        )
        VALUES(
          ".$this->c_preinversion.",
          ".date('Y').",
          ".$this->c_usuario.",
          '".$clasificador."',
          ".$solicitado.",
          ".$saldo.",
          ".$total.",
          ".$unidad.",
          ".$this->c_institucion.",
          ".$id."
          )";
   
    $dataset  = mysql_query($query, $this->conector);
    if (mysql_errno($this->conector) != 0) { 
         print_r (mysql_error($this->conector).$query); 
    }
  }

  /**
   * Retorna los detalles de financiamiento
   * 
   * 
   */
  function get_detalle_financiamiento()
  {
    $result = array();
    $query    = " SELECT C_CLASIFICADOR, SOLICITADO_ANO_ACTUAL, SALDO_POR_INVERTIR, COSTO_TOTAL, UNIDAD_TECNICA
                  FROM PREINVERSION_DETALLE_FINANCIAMIENTO
                  WHERE C_PREINVERSION = ".$this->c_preinversion."
                  AND C_INSTITUCION =".$this->c_institucion."
                  AND ANO =". $this->ano;
    $dataset  = mysql_query($query, $this->conector);

    while ($value = mysql_fetch_assoc($dataset)) {
      $result[$value['C_CLASIFICADOR']]   = $value;
    }

    return $result;

  }

  /**
   * Busca el primer documento por parte del formulador el que corresponde al
   * oficio conductor
   */
  function get_oficio_conductor()
  {
    $query    =   " SELECT * FROM PREINVERSION_DOCUMENTACION 
              WHERE C_PREINVERSION = ".$this->c_preinversion."
              AND ANO = ".$this->ano."
              AND VIGENTE = 1
              ORDER BY C_DOCUMENTO ASC 
              LIMIT 1 ";
    $dataset  = mysql_query($query, $this->conector);
    if (mysql_errno($this->conector) != 0) { 
         // print_r (mysql_error($this->conector).$query); 
    }
    $oficio   = mysql_fetch_assoc($dataset);
    return $oficio;
  }

  /**/


   function cargar_iniciativa_estado() {
      $query  =" SELECT B.N_ESTADO_PREINVERSION from PREINVERSION A, PREINVERSION_TESTADO B ";
      $query .=" WHERE A.C_ESTADO_PREINVERSION = B.C_ESTADO_PREINVERSION";
      $query .=" AND A.VIGENTE=1 ";
      $query .=" AND A.C_PREINVERSION= ".$this->c_preinversion;
      $query .=" AND A.C_INSTITUCION= ".$this->c_institucion;
      $query .=" AND A.ANO= ".$this->ano;   
      $dataset  = mysql_query($query, $this->conector); 
      $this->nroregistros = mysql_num_rows($dataset);
      return $dataset;
   }

  function cargar_iniciativa_ficha(){
      $query = sprintf("SELECT A.N_PREINVERSION, A.UNIDAD_TECNICA, A.FECHA_INICIO, A.FECHA_TERMINO, A.PRODUCTO, A.CODIGO, A.IMPACTOS, A.C_SECTOR, A.C_ESTADO_PREINVERSION, A.OBSERVACIONES, A.BENEFICIARIOS, A.C_CLASIFICADOR_PRESUPUESTARIO, B.N_SECTOR, C.N_INSTITUCION, E.N_CLASIFICADOR, A.COSTO_TOTAL, A.SOLICITADO, A.SALDO_PROXIMOS_ANOS, A.C_ETAPA, G.N_ETAPA_IDI, A.C_VIA_FINANCIAMIENTO, F.N_VIA_FINANCIAMIENTO, A.TIPO, A.RATE, A.DIRECCION, A.JUSTIFICACION, A.DESCRIPTOR, A.FUENTE_FINANCIAMIENTO, A.NOMBRE_FORMULADOR, A.TELEFONO, A.CORREO_ELECTRONICO "
      		." FROM PREINVERSION A, SECTOR B, INSTITUCION C, TCLASIFICADOR_PRESUPUESTARIO E, PREINVERSION_VIA_FINANCIAMIENTO F, ETAPA_IDI G " 
      		." WHERE A.C_SECTOR = B.C_SECTOR AND A.UNIDAD_TECNICA = C.C_INSTITUCION AND A.C_CLASIFICADOR_PRESUPUESTARIO = E.C_CLASIFICADOR AND A.ANO = E.ANIO AND A.C_VIA_FINANCIAMIENTO = F.C_VIA_FINANCIAMIENTO AND A.C_ETAPA = G.C_ETAPA_IDI AND A.VIGENTE = 1 AND F.ANIO = %d AND A.C_PREINVERSION = %d AND A.ANO = %d AND A.C_INSTITUCION = %d", $this->ano, $this->c_preinversion, $this->ano, $this->c_institucion);
      $dataset  = mysql_query($query, $this->conector);
      $this->nroregistros = mysql_num_rows($dataset);
      return $dataset;
  }

  function cargar_iniciativa_fuente_financiamiento() {
      $query  =" SELECT A.N_FUENTE_FINANCIAMIENTO, B.FUENTE_FINANCIAMIENTO from FUENTE_FINANCIAMIENTO A, PREINVERSION B";
      $query .=" WHERE A.C_FUENTE_FINANCIAMIENTO = B.FUENTE_FINANCIAMIENTO ";
      $query .=" AND B.C_PREINVERSION= ".$this->c_preinversion;
      $query .=" AND B.VIGENTE=1 ";
      $query .=" AND B.ANO= ".$this->ano;
      $dataset  = mysql_query($query, $this->conector); 
      $this->nroregistros = mysql_num_rows($dataset);
      return $dataset;
  }
  
  function cargar_nombre_rate() {
      $query  =" SELECT A.N_RATE, B.RATE from RATE A, PREINVERSION B";
      $query .=" WHERE A.C_RATE = B.RATE ";
      $query .=" AND B.C_PREINVERSION= ".$this->c_preinversion;
      $query .=" AND B.VIGENTE=1 ";
      $query .=" AND B.ANO= ".$this->ano;
      $dataset  = mysql_query($query, $this->conector); 
      $this->nroregistros = mysql_num_rows($dataset);
      return $dataset;
  }
  
   function cargar_iniciativa_institucion_sector() {
      $query  =" SELECT A.C_INSTITUCION, A.N_INSTITUCION, B.N_SECTOR from PREINVERSION C, INSTITUCION A, SECTOR B ";
      $query .=" WHERE A.C_SECTOR = B.C_SECTOR AND A.C_INSTITUCION= C.C_INSTITUCION ";
      $query .=" AND C.C_PREINVERSION= ".$this->c_preinversion;
      $query .=" AND C.VIGENTE=1 ";
      $query .=" AND C.ANO= ".$this->ano;
      $query .=" AND C.C_INSTITUCION= ".$this->c_institucion;
      $dataset  = mysql_query($query, $this->conector); 
      $this->nroregistros = mysql_num_rows($dataset);
      return $dataset;
   }

   function existe_observaciones_iniciativa() {
      $query  =" SELECT COUNT(*) FROM PREINVERSION_OBSERVACIONES ";
      $query .=" WHERE C_PREINVERSION= ".$this->c_preinversion;
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
      $query .= " FECHA_REGISTRO, C_USUARIO, C_ETAPA, C_VIA_FINANCIAMIENTO, TIPO, CODIGO_DV, RATE, ";
      $query .= " DESCRIPTOR, FUENTE_FINANCIAMIENTO,NOMBRE_FORMULADOR, TELEFONO,CORREO_ELECTRONICO) VALUES('";
      $query .= $this->ano."','".$this->c_institucion."','".$this->c_preinversion."','".$this->n_preinversion."','".$this->unidad_tecnica."','".$this->direccion."', '"; 
      $query .= $this->c_clasificador_presupuestario."','".$this->fecha_inicio."','".$this->fecha_termino."','";
      $query .= $this->producto."','".$this->codigo."','".$this->impactos."','".$this->c_sector."','".$this->observaciones."','".$this->justificacion."','";
      $query .= $this->beneficiarios."','".$this->costo_total."','".$this->solicitado."','".$this->saldo_proximos_anos."','";
      $query .= $this->fecha_registro."','".$this->c_usuario."','".$this->c_etapa."','".$this->c_via_financiamiento."','".$this->c_tipo."','".$this->codigo_dv."','";
      $query .= $this->rate."','".$this->descriptor."','".$this->cb_fuentef."','".$this->nombre_formulador."','".$this->telefono."','".$this->correo_electronico."')";
      /*se comenta linea bara debuguer la consulta sql 09052014*/     
      $dataset  = mysql_query($query, $this->conector);
      if (mysql_errno($this->conector) != 0) { 
         $this->mensaje_error="No se ha podido Actualizar la Base de Datos, mensaje de error de Mysql es: " . mysql_error($this->conector).$query; 
      } else {$this->mensaje_error='';}   
      return $this->mensaje_error;
   }   

  /**
   *  Funcion que devuelve los campos solicitados
   * 
   */

  function obtener_campos( $campos ){
    $query    = " SELECT " . $campos . " FROM PREINVERSION
                WHERE ANO = ".$this->ano." AND C_PREINVERSION = ". $this->c_preinversion;

    $dataset  = mysql_query($query, $this->conector);

    $campos   = mysql_fetch_assoc($dataset);
    return $campos;

  }

  /**
   * 
   */
  function eliminar_iniciativa() {
     $query  =" DELETE FROM PREINVERSION";
     $query .=" WHERE C_PREINVERSION= ".$this->c_preinversion;
     $query .=" AND ANO= ".$this->ano;
     $dataset  = mysql_query($query, $this->conector); 
     if (mysql_errno($this->conector) != 0) { 
        $this->mensaje_error="No se ha podido Actualizar la Base de Datos, mensaje de error de Mysql es: " . mysql_error($this->conector).$query; 
     } else {$this->mensaje_error='';}   
     return $this->mensaje_error;
  }   

   function eliminar_iniciativa_forma_segura() {
      $query  = " UPDATE PREINVERSION";
      $query .= " SET VIGENTE=0";
      $query .= " WHERE C_PREINVERSION= ".$this->c_preinversion;
      $query .= " AND ANO= ".$this->ano;
      $query .= " AND C_INSTITUCION= ".$this->c_institucion;
      $dataset  = mysql_query($query, $this->conector); 
      if (mysql_errno($this->conector) != 0) { 
         $this->mensaje_error="No se ha podido Actualizar la Base de Datos, mensaje de error de Mysql es: " . mysql_error($this->conector).$query; 
      } else {$this->mensaje_error='';}   
      return $this->mensaje_error;
   }   

   function cambiar_estado_iniciativa($xestado) {
      $query  =" UPDATE PREINVERSION SET C_ESTADO_PREINVERSION=".$xestado;
      $query .=" WHERE C_PREINVERSION= ".$this->c_preinversion;
      $query .=" AND ANO= ".$this->ano;
      $query .=" AND C_INSTITUCION=".$this->c_institucion;
      
      $dataset  = mysql_query($query, $this->conector); 
      if (mysql_errno($this->conector) != 0) { 
         $this->mensaje_error="No se ha podido Actualizar la Base de Datos, mensaje de error de Mysql es: " . mysql_error($this->conector).$query; 
      } else {$this->mensaje_error='';}   

      // Notificación de email
      $this->envia_email($xestado);

      // Registrar cambio de estado
      $this->registra_cambio_estado();

      return $this->mensaje_error;
   }   

   function ingresar_observacion($xfecha,$xobservacion) {
      $query1  = " INSERT INTO PREINVERSION_OBSERVACIONES VALUES('";
      $query1 .= $this->ano."','".$this->c_preinversion."','".$this->c_institucion."','".$xfecha."','".$xobservacion."','";
      $query1 .= $this->fecha_registro."','".$this->c_usuario."')";
      $dataset1  = mysql_query($query1, $this->conector);      
      
      if (mysql_errno($this->conector) != 0) { 
         $this->mensaje_error="No se ha podido Actualizar la Base de Datos, mensaje de error de Mysql es: " . mysql_error($this->conector).$query1; 
      } else {$this->mensaje_error='';}   
      return $this->mensaje_error;
   }   
   /**
    * Funcion que ingresa una observacion y opcionalmente agrega un archivo
    * Se guardara todo el historial de observaciones, a diferencia del
    * anterior que solo guardaba la ultima observacion.
    * 
    * @param $xfecha int(8): Fecha de la observacion formato '20120620'
    * @param $xobservacion text: Texto de la observacion
    * @param $archivo varchar: nombre del archivo a guardar
    */
   function ingresar_observacion_historica($xfecha, $xobservacion, $archivo, $ninstitucion,$num_observacion){

      $query1  = " INSERT INTO PREINVERSION_OBSERVACIONES (ANO, C_PREINVERSION, C_INSTITUCION, FECHA_OBSERVACION, OBSERVACION, FECHA_REGISTRO, C_USUARIO,ID, C_ETAPA, N_ADJUNTO, TIPO_OBSERVACION, C_ESTADO_DESTINO) VALUES(' ";
      $query1 .= $this->ano."','".$this->c_preinversion."','".$ninstitucion."','".$xfecha."','".$xobservacion."','";
      $query1 .= $this->fecha_registro."','".$this->c_usuario."','".$num_observacion."','";
      $query1 .= $this->c_etapa."','".$archivo."','1', ".$this->c_estado_destino.")";
      $dataset1  = mysql_query($query1, $this->conector);


      // die(print_r($query1));      
      
      if (mysql_errno($this->conector) != 0) { 
         $this->mensaje_error="No se ha podido Actualizar la Base de Datos, mensaje de error de Mysql es: " . mysql_error($this->conector).$query1; 
      } else {$this->mensaje_error='';}   
      return $this->mensaje_error;
   }

   /**
    * Funcion que guarda las iniciativas con observaciones previas luego 
    * de que son modificadas por el municipio
    * 
    * a la vez, cambia de estado a la iniciativa a "Recepcionado" (estado: 3)
    * 
    */ 
   function aceptar_iniciativa_observada($fecha_observacion, $archivo, $observacion){
      $query1  = " INSERT INTO PREINVERSION_OBSERVACIONES (";
      $query1 .= " ANO, C_PREINVERSION, C_INSTITUCION, FECHA_OBSERVACION, OBSERVACION, ";
      $query1 .= " FECHA_REGISTRO, C_USUARIO, C_ETAPA, N_ADJUNTO, TIPO_OBSERVACION, ";
      $query1 .= " N_OFICINA_PARTE, FECHA_OFICINA_PARTE, FECHA_OFICIO, NUMERO_OFICIO)";
      $query1 .= " VALUES('".$this->ano."','".$this->c_preinversion."','".$this->c_institucion."',";
      $query1 .= "'".$fecha_observacion."','".$observacion."',";
      $query1 .= "'".$this->fecha_registro."','".$this->c_usuario."',";
      $query1 .= "'".$this->estado_actual_iniciativa."','".$archivo."','1','".$this->numero_parte."',";
      $query1 .= "'".$this->fecha_parte."','".$this->fecha_oficio."',";
      $query1 .= "'".$this->numero_oficio."')";

      $dataset = mysql_query($query1, $this->conector);

      if (mysql_errno($this->conector) != 0) { 
         $this->mensaje_error="No se ha podido Actualizar la Base de Datos, mensaje de error de Mysql es: " . mysql_error($this->conector).$query1; 
      } else {$this->mensaje_error='';}   
      return $this->mensaje_error;
   }

   /**
    * Funcion que cambia el RATE a las iniciativas cuando tienen (RS) 
    */
  function actualizar_iniciativa_rate(){
    $query    = "SELECT C_RATE FROM RATE WHERE N_RATE='".$this->rate."'";
    $dataset  = mysql_query($query, $this->conector); 
    $c_rate1  = @mysql_fetch_row($dataset);
    $c_rate   = $c_rate1[0];
    
    $query  = " UPDATE PREINVERSION SET 
                RATE = ".$c_rate."
                WHERE C_PREINVERSION = ".$this->c_preinversion."
                AND ANO = ".$this->ano."
                AND C_INSTITUCION = ".$this->c_institucion;

    $dataset = mysql_query($query, $this->conector);
  }

   /**
    * Funcion que retorna el valor maximo del campo ID que seria la cantidad de 
    * Observaciones totales
    * 
    * @return int: cantidad de observaciones
    */
   function get_cantidad_observaciones(){
      $query1 = " SELECT MAX(ID) FROM PREINVERSION_OBSERVACIONES ";
		$query1 .= " WHERE C_PREINVERSION= ".$this->c_preinversion;
		$query1 .= " AND ANO= ".$this->ano;
		$query1 .= " AND C_INSTITUCION= ".$this->c_institucion;
		echo $query1;
      $dataset1 = mysql_query($query1, $this->conector);
      $datos   = mysql_fetch_row($dataset1);

      return $datos[0];
   }


   /**
    * Funcion que retorna la ultima observacion enviada por el gore
    * 
    * @return array: Observacion, ID
    */
   function get_observacion(){
      $query1  = " SELECT OBSERVACION, ID FROM PREINVERSION_OBSERVACIONES ";
      $query1 .= " WHERE C_PREINVERSION=" . $this->c_preinversion;
      $query1 .= " AND ANO=" . $this->ano;
      $query1 .= " AND C_INSTITUCION= ".$this->c_institucion;
      $query1 .= " ORDER BY ID DESC ";
      $query1 .= " LIMIT 1 ";

      $dataset = mysql_query($query1, $this->conector);
      $datos   = mysql_fetch_row($dataset);
      return   $datos;
   }


   /**
    * Funcion que por medio de un ID obtiene el nombre de usuario
    * correspondiente
    * 
    * @param int $c_usuario: id del usuario a consyltar
    * @return string: Nombre del usuario
    */
   function obtener_usuario(){
      $query1  = " SELECT CONCAT(NOMBRES,' ',APELLIDO_PATERNO) FROM";
      $query1 .= " ADMIN_USUARIO ";
      $query1 .= " WHERE C_USUARIO=".$this->c_usuario;

      $dataset = mysql_query($query1, $this->conector);
      $datos   = mysql_fetch_row($dataset);

      return $datos[0];
   }


   /**
    * Funcion que retorna el nombre de la "etapa", en base al codigo ingresado
    * en realidad hace referencia al "Estado" de la iniciativa, pero se muestra 
    * como "etapa" al usuario
    * 
    * @param int $etapa: codigo de la etapa a consultar
    * @return string: Nombre de la etapa
    */
   function obtener_nombre_etapa($etapa){
      $query1  = " SELECT N_ESTADO_PREINVERSION FROM PREINVERSION_TESTADO ";
      $query1 .= " WHERE C_ESTADO_PREINVERSION =".$etapa;

      $dataset = mysql_query($query1,$this->conector);
      $datos   = mysql_fetch_row($dataset);

      return $datos[0];
   }


   /**
    * Funcion que muestra datos de la ultima respuesta enviara por el municipio
    * para ser vista por el GORE
    */
   function obtener_datos_respuesta(){
      $query1  = " SELECT NUMERO_OFICIO, FECHA_OFICIO, OBSERVACION, N_ADJUNTO";
      $query1 .= " FROM PREINVERSION_OBSERVACIONES ";
      $query1 .= " WHERE C_PREINVERSION='".$this->c_preinversion."' ";
      $query1 .= " AND TIPO_OBSERVACION=2 ";
      $query1 .= " ORDER BY ID DESC ";
      $query1 .= " LIMIT 1 ";

      $dataset = mysql_query($query1, $this->conector);
      $datos   = mysql_fetch_row($dataset);

      return $datos;
   }

   /**
    * Función que retiorna los datos de la última observación realizada por el 
    * GORE hacia el municipio
    */
   function obtener_datos_observacion(){
      $query1  = "SELECT N_ADJUNTO
                  FROM PREINVERSION_OBSERVACIONES
                  WHERE C_PREINVERSION='".$this->c_preinversion."'
                  AND TIPO_OBSERVACION=1
                  ORDER BY ID DESC
                  LIMIT 1 ";
      $dataset = mysql_query($query1, $this->conector);
      $datos   = mysql_fetch_row($dataset);

      return $datos;
   }

   /**
    * Funcion que verifica si un codigo bip existe en los registros
    * 
    * @param $codigo_bip varchar: Codigo BIP
    * @return boolean: si existe o no codigo
    */
  function existe_codigo_bip($codigo_bip,$ano_bip,$cpreinversion_bip,$etapa_iniciativa){
    $query1  = " SELECT CODIGO";
    $query1 .= " FROM PREINVERSION ";
    $query1 .= " WHERE CODIGO='".$codigo_bip."'";
    $query1 .= " AND ANO= ".$ano_bip;
    //$query1 .= " AND C_PREINVERSION= ".$cpreinversion_bip;
    $query1 .= " AND VIGENTE=1 ";
    $query1 .= " AND C_ETAPA=".$etapa_iniciativa;

    $dataset  = mysql_query($query1, $this->conector);
    $datos    = mysql_num_rows($dataset);

    return $datos;
  }
   /**
    * Funcion encargada de ingresar la respuesta del municipio ante una 
    * observacion por parte del GORE
    * 
    * @param string $archivo: Nombre del archivo
    */
   function ingresar_respuesta($archivo, $id_obs){
		// se ingresa el 2 en duro para dejar la iniciativa en estado 2=postulada  
		// el id_obs es el incremental de registros de observacines para una inicitiva 	  	
      $query1  = " INSERT INTO PREINVERSION_OBSERVACIONES (ANO, C_PREINVERSION, C_INSTITUCION, ";
      $query1 .= " NUMERO_OFICIO, FECHA_OFICIO, N_ADJUNTO, OBSERVACION, FECHA_REGISTRO,";
      $query1 .= " C_USUARIO, ID, C_ETAPA, FECHA_OBSERVACION, TIPO_OBSERVACION, C_ESTADO_DESTINO)";
      $query1 .= " VALUES('".$this->ano."','".$this->c_preinversion."','".$this->c_institucion."',";
      $query1 .= "'".$this->numero_oficio."','".$this->fecha_oficio."',";
      $query1 .= "'".$archivo."','".$this->respuesta_municipio."',";
      $query1 .= "'".$this->fecha_registro."','".$this->c_usuario."','".$id_obs."',";
      $query1 .= "'".$this->estado_actual."','".$this->fecha_registro."','2', ".$this->c_estado_destino.")";
      $dataset = mysql_query($query1, $this->conector);

      if (mysql_errno($this->conector) != 0) { 
         $this->mensaje_error="No se ha podido Actualizar la Base de Datos, mensaje de error de Mysql es: " . mysql_error($this->conector).$query1; 
      } else {$this->mensaje_error='';}   
      return $this->mensaje_error;

   }

   /**
    * Retorna el codigo de RATE en base al Nombre
    * 
    * @param string nombre: nombre del Rate a buscar
    * @return int: codigo
    */

   function get_codigo_rate($nombre){
      $query = "SELECT C_RATE FROM RATE WHERE N_RATE='".$nombre."'";
      $dataset  = mysql_query($query, $this->conector); 
      $record = @mysql_fetch_row($dataset);

      return $record[0];
   }

  /**
   * Retorna el nombre del rate en base al codigo ingresado
   * 
   * @param int codigo: codigo del rate
   * @return string nombre: nombre del Rate
   */
  function get_nombre_rate( $codigo ){
    $query  = " SELECT N_RATE FROM RATE WHERE C_RATE = '".$codigo."'";
    $dataset = mysql_query($query, $this->conector);
    $record = mysql_fetch_row($dataset);

    return $record[0];
  }

   /**
    * Funcion que agrega las recomendaciones emitidas por los evaluadores
    * 
    */
  function ingresar_recomendacion($xfecha,$xobservacion, $nombrearchivo, $num) {

    $query1  = " INSERT INTO PREINVERSION_RECOMENDACION (ANO, C_PREINVERSION, ";
    $query1 .= " FECHA_RECOMENDACION, OBSERVACIONES, FECHA_REGISTRO, C_USUARIO, ";
    $query1 .= " ID_RECOMENDACION, ARCHIVO, C_ESTADO, C_INSTITUCION) VALUES('";
    $query1 .= $this->ano."','".$this->c_preinversion."','".$xfecha."','".$xobservacion."','";
    $query1 .= $this->fecha_registro."','".$this->c_usuario."','".$num."',";
    $query1 .= " '".$nombrearchivo."','".$this->estado_actual_iniciativa."','".$this->c_institucion."')";

    $dataset1  = mysql_query($query1, $this->conector);      
    if (mysql_errno($this->conector) != 0) { 
      $this->mensaje_error="No se ha podido Actualizar la Base de Datos, mensaje de error de Mysql es: " . mysql_error($this->conector).$query1; 
    } else {$this->mensaje_error='';}   
    return $this->mensaje_error;
  }   

   /**
    * Funcion que retorna la cantidad de recomendaciones para una iniciativa
    * 
    */
   function cantidad_recomendaciones($ano, $c_preinversion){
    $query1  = " SELECT COUNT(*) FROM PREINVERSION_RECOMENDACION ";
    $query1 .= " WHERE ANO =".$ano." AND C_PREINVERSION =".$c_preinversion;

    $dataset = mysql_query($query1, $this->conector);
    $cantidad = mysql_fetch_row($dataset);

    return $cantidad[0];
   }

   /**
    * Funcion que retorna el nombre del checklist asociado a la iniciativa
    * 
    * @return string: nombre del archivo
    */
   function get_checklist_recomendacion(){
    $query1  = " SELECT ARCHIVO FROM PREINVERSION_RECOMENDACION ";
    $query1 .= " WHERE ANO=".$this->ano ." AND C_PREINVERSION=". $this->c_preinversion;
    $query1 .= " ORDER BY ID_RECOMENDACION DESC LIMIT 1";

    $dataset = mysql_query($query1, $this->conector);
    $archivo = mysql_fetch_row($dataset);

    if ($archivo) {
      return $archivo[0];
    }else{
      return 0;
    }
   }

  /**
   * Funcion que carga las observaciones de la iniciativa
   * 
   */
  function cargar_observaciones($xtipoobs){
    $total_observaciones = array();
    // 1= PREINVERSION_OBSERVACIONES
    // 2= PREINVERSION_RECOMENDACION
    // 3= PREINVERSION_ASIGNACION 
    
    switch ($xtipoobs){
    	case 1: 
    	      // PREINVERSION_OBSERVACIONES
    			$query1  = " SELECT FECHA_CREACION, OBSERVACION, ";
    			$query1 .= " C_USUARIO, C_ETAPA, N_ADJUNTO";
    			$query1 .= " FROM PREINVERSION_OBSERVACIONES ";
    			$query1 .= " WHERE C_PREINVERSION=".$this->c_preinversion." AND ANO=".$this->ano;
   			$query1 .= " AND C_INSTITUCION=".$this->c_institucion;
    			$dataset = mysql_query($query1, $this->conector);
    			// $observaciones = mysql_fetch_row($dataset);
    			// PREINVERSION_OBSERVACIONES
            while ($observacion = mysql_fetch_row($dataset)) {
               $total_observaciones[] = $observacion;
             }
    			break;
             
      case 2: 
      	   // PREINVERSION_RECOMENDACION
    			// Obs. entregadas a los proyectos cuando se otorga RS por lso Analistas
    			// 
    			$query2  = " SELECT FECHA_CREACION, OBSERVACIONES, ";
    			$query2 .= " C_USUARIO, C_ESTADO, ARCHIVO";
    			$query2 .= " FROM PREINVERSION_RECOMENDACION ";
    			$query2 .= " WHERE C_PREINVERSION=".$this->c_preinversion." AND ANO=".$this->ano;
    			$query2 .= " AND C_INSTITUCION=".$this->c_institucion;
    			$dataset2 = mysql_query($query2, $this->conector);
    			// $recomendaciones = mysql_fetch_row($dataset2);
    			// PREINVERSION_RECOMENDACION
            while ($observacion = mysql_fetch_row($dataset2)) {
               $total_observaciones[] = $observacion;
              }
    			break;
    		   
      case 3: 	   
    			// PREINVERSION_ASIGNACION
    			// Obs. entregadas por el Jefe a los Analistas cuando 
    			// son asignados los proyectos para su evaluacion tecnica
    			$query3  = " SELECT FECHA_CREACION, OBSERVACIONES,";
    			$query3 .= " C_ASIGNADOR, C_ESTADO ";
    			$query3 .= " FROM PREINVERSION_ASIGNACION ";
    			$query3 .= " WHERE C_PREINVERSION=".$this->c_preinversion." AND ANO=".$this->ano;
    			$query3 .= " AND C_INSTITUCION=".$this->c_institucion;
    			$dataset3 = mysql_query($query3, $this->conector);
    			// PREINVERSION_ASIGNACION
            while ($observacion = mysql_fetch_row($dataset3)) {
              // $observacion[3] = "0"; // Se agrega Archivo
              $observacion[4] = "0"; // Se agrega Archivo
              $total_observaciones[] = $observacion;
              }
    			break;
    		   
    }
   
    $observaciones_ordenadas = array();
    foreach ($total_observaciones as $key => $observaciones) { 
      $observaciones_ordenadas[$observaciones[0]] = $observaciones;
    }
    arsort($observaciones_ordenadas);

    return $observaciones_ordenadas;
  }

   function ingresar_norecomendacion($xfecha,$xobservacion) {
      $query1  = " INSERT INTO PREINVERSION_NORECOMENDACION VALUES('";
      $query1 .= $this->ano."','".$this->c_preinversion."','".$xfecha."','".$xobservacion."','";
      $query1 .= $this->fecha_registro."','".$this->c_usuario."')";
      $dataset1  = mysql_query($query1, $this->conector);      
      if (mysql_errno($this->conector) != 0) { 
         $this->mensaje_error="No se ha podido Actualizar la Base de Datos, mensaje de error de Mysql es: " . mysql_error($this->conector).$query1; 
      } else {$this->mensaje_error='';}   
      return $this->mensaje_error;
   }   

   function ingresar_aprobacion($xfecha,$xobservacion) {
      $query1  = " INSERT INTO PREINVERSION_APROBACION VALUES('";
      $query1 .= $this->ano."','".$this->c_preinversion."','".$xfecha."','".$xobservacion."','";
      $query1 .= $this->fecha_registro."','".$this->c_usuario."')";
      $dataset1  = mysql_query($query1, $this->conector);      
      if (mysql_errno($this->conector) != 0) { 
         $this->mensaje_error="No se ha podido Actualizar la Base de Datos, mensaje de error de Mysql es: " . mysql_error($this->conector).$query1; 
      } else {$this->mensaje_error='';}   
      return $this->mensaje_error;
   }   

  /**
   * Funcion que guarda las aprobaciones por parte del core
   */

   function ingresar_aprobacion_core($xobservacion, $documento,$vnumero_acuerdo,$vfecha_acuerdo,$vnumero_sesion,$vfecha_sesion) {
      $query1  = " INSERT INTO PREINVERSION_APROBADA_CORE (ANO, C_PREINVERSION, 
        OBSERVACIONES, C_USUARIO, DOCUMENTO_ACUERDO, C_INSTITUCION,NUMERO_ACUERDO,FECHA_ACUERDO,NUMERO_SESION,FECHA_SESION) 
        VALUES('".$this->ano."', '".$this->c_preinversion."','".$xobservacion."',
         '".$this->c_usuario."','".$documento."','".$this->c_institucion."','".$vnumero_acuerdo."','".$vfecha_acuerdo."','".$vnumero_sesion."','".$vfecha_sesion."')";
        
      error_log("registra Ingreso de Aprobado por el CORE:".$query1);
      $dataset1  = mysql_query($query1, $this->conector);      
      if (mysql_errno($this->conector) != 0) { 
         $this->mensaje_error="No se ha podido Actualizar la Base de Datos, mensaje de error de Mysql es: " . mysql_error($this->conector).$query1; 
      } else {$this->mensaje_error='';}   
      return $this->mensaje_error;
   }  
   /**
   * Funcion que guarda las priorizaciones
   * $xfecha, $xobservacion, $xnumsesion, $xtipo_sesion, $xfecha_sesion_alcore, $nombrearchivo
   */
   
   function ingresar_priorizacion($xfecha,$xobservacion,$xnumsesion, $xtipo_sesion, $xfecha_sesion_alcore, $num_presentado, $nombrearchivo) {
      $query1  = " INSERT INTO PREINVERSION_PRIORIZADA (ANO, C_PREINVERSION, 
        FECHA_PRIORIZADO, OBSERVACIONES, C_USUARIO, C_INSTITUCION, NUM_SESION, TIPO_SESION, FECHA_SESION, PRESENTADO, DOCUMENTO_PRESENATDO) 
        VALUES('".$this->ano."', '".$this->c_preinversion."','".$xfecha."',
        '".$xobservacion."', '".$this->c_usuario."', '".$this->c_institucion."', '".$xnumsesion."', '".$xtipo_sesion."', '".$xfecha_sesion_alcore."', '".$num_presentado."', '".$nombrearchivo."')";
      error_log("registra Ingreso de prorizacion:".$query1);
      $dataset1  = mysql_query($query1, $this->conector);      
      if (mysql_errno($this->conector) != 0) { 
         $this->mensaje_error="No se ha podido Actualizar la Base de Datos, mensaje de error de Mysql es: " . mysql_error($this->conector).$query1; 
      } else {$this->mensaje_error='';}   
      return $this->mensaje_error;
   }  

   function presentado_alcore() {
      $query1  = " SELECT MAX(PRESENTADO) FROM PREINVERSION_PRIORIZADA";
      $query1 .= " WHERE ANO=".$this->ano ." AND C_PREINVERSION=". $this->c_preinversion ." AND C_INSTITUCION=". $this->c_institucion  ;
      $query1 .= " ORDER BY ID_RECOMENDACION DESC LIMIT 1";

      $dataset = mysql_query($query1, $this->conector);
      $numpresenatdo = mysql_fetch_row($dataset);
  
      return $numpresenatdo[0];
      
   }
   
   
   
   
  /**
   * Cargar iniciativas
   */
   function cargar_iniciativa($xnano,$xsector,$xinstitucion,$xaccesoinstituciones,$xestado,$xpreinversionsel,$xexcel,$xviafinanciamiento) {
      $fila=0;$this->filaseleccionada=-1;$total1=0;$total2=0;$total3=0;
      $query  =" SELECT A.CODIGO, 
        A.N_PREINVERSION, 
        M.N_VIA_FINANCIAMIENTO, 
        G.N_ETAPA_IDI,
        IF (E.C_NIVEL_UT=1,'Regional',IF (E.C_NIVEL_UT=2,CONCAT('Provincial (',E.UBICACION_ESPECIFICA_NOMBRE,')'),IF (E.C_NIVEL_UT=3,CONCAT('Comunal (',E.UBICACION_ESPECIFICA_NOMBRE,')'),CONCAT('Localidades (',E.UBICACION_ESPECIFICA_NOMBRE,')')))),
        B.N_SECTOR, 
        C.N_INSTITUCION, 
        A.SOLICITADO,  
        Y.N_ESTADO_PREINVERSION, 
        A.C_PREINVERSION AS ID, 
        A.ANO, 
        A.C_INSTITUCION, 
        A.C_PREINVERSION, 
        A.C_ESTADO_PREINVERSION, 
        P.N_INSTITUCION, 
        A.COSTO_TOTAL, 
        A.SALDO_PROXIMOS_ANOS,
        E.AREA_INFLUENCIA,
        DATE_FORMAT(A.F_CAMBIO_ESTADO,'%d/%m/%Y')
        
      FROM INSTITUCION C, INSTITUCION P, PREINVERSION_TESTADO Y, PREINVERSION A 
      LEFT JOIN UBICACION_TERRITORIAL_PREINVERSION E ON (A.ANO=E.ANO AND A.C_INSTITUCION=E.C_INSTITUCION AND A.C_PREINVERSION=E.C_PREINVERSION)
      LEFT JOIN PREINVERSION_VIA_FINANCIAMIENTO M ON (A.C_VIA_FINANCIAMIENTO=M.C_VIA_FINANCIAMIENTO AND A.ANO=M.ANIO)
      LEFT JOIN SECTOR B ON (A.C_SECTOR=B.C_SECTOR)
      LEFT JOIN ETAPA_IDI G ON (A.C_ETAPA=G.C_ETAPA_IDI)
      WHERE A.ANO= ".$xnano."
      AND A.VIGENTE=1 
      AND A.C_INSTITUCION=C.C_INSTITUCION AND A.UNIDAD_TECNICA=P.C_INSTITUCION AND A.C_ESTADO_PREINVERSION=Y.C_ESTADO_PREINVERSION";
      if ($xsector>0) { $query .= " AND A.C_SECTOR = ".$xsector;}
      if ($xviafinanciamiento>0) { $query .= " AND A.C_VIA_FINANCIAMIENTO = ".$xviafinanciamiento;}
      if ($xinstitucion>0) { 
	       $query .= " AND A.C_INSTITUCION = ".$xinstitucion;
      } else {
        if ($xaccesoinstituciones!='999') {
        $query .= " AND A.C_INSTITUCION IN (".$xaccesoinstituciones.") ";
        }
      }
      if ($xestado>0) { $query .= " AND A.C_ESTADO_PREINVERSION IN (".$xestado.")";}
      $query .=" GROUP BY A.CODIGO, A.N_PREINVERSION, A.ANO, A.C_INSTITUCION, A.C_PREINVERSION ";
      $query .=" ORDER BY C.N_INSTITUCION, A.CODIGO, A.N_PREINVERSION ";

      $dataset= mysql_query($query, $this->conector); 
      $this->nroregistros = mysql_num_rows($dataset);
      $rows = array(); 
      while ($record = mysql_fetch_row($dataset)) { 
        $cols = array();
        $total=round($total+$record[8]);
        $total1=round($total1+$record[7]);
        $total2=round($total2+$record[15]);
        $total3=round($total3+$record[16]);
	      $colsel=0;
	      foreach ($record as $value) { 
	        if ($colsel==17) {
            if ($value==0) {$value="No definida";}
            if ($value==1) {$value="Internacional";}
            if ($value==2) {$value="Nacional";}
            if ($value==3) {$value="Regional";}
            if ($value==4) {$value="Provincial";}
            if ($value==5) {$value="Comunal";}
          }
  	      $cols[] = '"'.addslashes($value).'"';
  	      if ($colsel==11) {
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
   }
   
   function cargar_iniciativa_evaluador($xnano,$xsector,$xinstitucion,$xaccesoinstituciones,$xestado,$xpreinversionsel,$xexcel,$xviafinanciamiento,$xevaluador) {
      $fila=0;$this->filaseleccionada=-1;$total1=0;$total2=0;$total3=0;
      $query  ="SELECT A.CODIGO, 
          A.N_PREINVERSION,
           M.N_VIA_FINANCIAMIENTO, 
           G.N_ETAPA_IDI,
           IF (E.C_NIVEL_UT=1,'Regional',IF (E.C_NIVEL_UT=2,CONCAT('Provincial (',E.UBICACION_ESPECIFICA_NOMBRE,')'),IF (E.C_NIVEL_UT=3,CONCAT('Comunal (',E.UBICACION_ESPECIFICA_NOMBRE,')'),CONCAT('Localidades (',E.UBICACION_ESPECIFICA_NOMBRE,')')))), 
           B.N_SECTOR, 
           C.N_INSTITUCION, 
           A.SOLICITADO,  
           Y.N_ESTADO_PREINVERSION, 
           A.C_PREINVERSION AS ID, 
           A.ANO, 
           A.C_INSTITUCION, 
           A.C_PREINVERSION, 
           A.C_ESTADO_PREINVERSION, 
           P.N_INSTITUCION, 
           A.COSTO_TOTAL, 
           A.SALDO_PROXIMOS_ANOS,
           E.AREA_INFLUENCIA, 
           IF(A.C_ESTADO_PREINVERSION = 10, 'MSD',  CONCAT(LEFT(AU.NOMBRES,1),LEFT(AU.APELLIDO_PATERNO,1), LEFT(APELLIDO_MATERNO,1))),
           PA.C_ANALISTA,
           DATE_FORMAT(A.F_CAMBIO_ESTADO,'%d/%m/%Y')

           FROM INSTITUCION C, INSTITUCION P, PREINVERSION_TESTADO Y, PREINVERSION A
           LEFT JOIN UBICACION_TERRITORIAL_PREINVERSION E ON (A.ANO=E.ANO AND A.C_INSTITUCION=E.C_INSTITUCION AND A.C_PREINVERSION=E.C_PREINVERSION)
           LEFT JOIN PREINVERSION_VIA_FINANCIAMIENTO M ON (A.C_VIA_FINANCIAMIENTO=M.C_VIA_FINANCIAMIENTO AND A.ANO=M.ANIO)
           LEFT JOIN SECTOR B ON (A.C_SECTOR=B.C_SECTOR)
           LEFT JOIN ETAPA_IDI G ON (A.C_ETAPA=G.C_ETAPA_IDI)
           LEFT JOIN PREINVERSION_ASIGNACION PA ON (A.C_PREINVERSION=PA.C_PREINVERSION AND A.ANO = PA.ANO AND A.C_INSTITUCION=PA.C_INSTITUCION) 
           LEFT JOIN ADMIN_USUARIO AU ON (PA.C_ANALISTA=AU.C_USUARIO)
           WHERE A.ANO=".$xnano." AND A.VIGENTE=1 ";
      
      if ($xevaluador != 0) {
        $query .=" AND PA.C_ANALISTA=".$xevaluador;
      }
      
      $query .=" AND A.C_INSTITUCION=C.C_INSTITUCION AND A.UNIDAD_TECNICA=P.C_INSTITUCION AND A.C_ESTADO_PREINVERSION=Y.C_ESTADO_PREINVERSION";
      if ($xsector>0) { $query .= " AND A.C_SECTOR = ".$xsector;}
      if ($xviafinanciamiento>0) { $query .= " AND A.C_VIA_FINANCIAMIENTO = ".$xviafinanciamiento;}
      if ($xinstitucion>0) { 
         $query .= " AND A.C_INSTITUCION = ".$xinstitucion;
      } else {
        if ($xaccesoinstituciones!='999') {
        $query .= " AND A.C_INSTITUCION IN (".$xaccesoinstituciones.") ";
        }
      }
      if ($xestado>0) { $query .= " AND A.C_ESTADO_PREINVERSION IN (".$xestado.")";}
      $query .=" GROUP BY A.CODIGO, A.N_PREINVERSION, A.ANO, A.C_INSTITUCION, A.C_PREINVERSION ";
      $query .=" ORDER BY C.N_INSTITUCION, A.CODIGO, A.N_PREINVERSION ";
      $dataset= mysql_query($query, $this->conector); 
      
      $this->nroregistros = mysql_num_rows($dataset);
      $rows = array(); 
      while ($record = mysql_fetch_row($dataset)) { 
         $cols = array();$total=round($total+$record[8]);
         $total1=round($total1+$record[7]);
         $total2=round($total2+$record[15]);
         $total3=round($total3+$record[16]);
    $colsel=0;
    foreach ($record as $value) { 
       if ($colsel==17) {
               if ($value==0) {$value="No definida";}
               if ($value==1) {$value="Internacional";}
               if ($value==2) {$value="Nacional";}
               if ($value==3) {$value="Regional";}
               if ($value==4) {$value="Provincial";}
               if ($value==5) {$value="Comunal";}
            }
       $cols[] = '"'.addslashes($value).'"';
       if ($colsel==11) {
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
   }
   
function cargar_iniciativa_excel($xnano,$xsector,$xinstitucion,$xaccesoinstituciones,$xestado,$xpreinversionsel,$xexcel,$xviafinanciamiento) {
      $query  = " SELECT A.ANO, Y.N_INSTITUCION, A.N_PREINVERSION, Z.N_INSTITUCION, ";
      $query .= " CONCAT(V.N_CLASIFICADOR,' (',V.C_CLASIFICADOR,')'),DATE_FORMAT(A.FECHA_INICIO,'%d/%m/%Y'), DATE_FORMAT(A.FECHA_TERMINO,'%d/%m/%Y'),";
      $query .= " A.PRODUCTO, A.CODIGO, A.IMPACTOS, F.N_SECTOR, G.N_ESTADO_PREINVERSION, A.OBSERVACIONES, A.COSTO_TOTAL, A.SOLICITADO, ";
      $query .= " A.SALDO_PROXIMOS_ANOS, H.N_ETAPA_IDI, I.N_VIA_FINANCIAMIENTO, E.C_NIVEL_UT, E.UBICACION_ESPECIFICA, E.UBICACION_ESPECIFICA_NOMBRE,A.C_PREINVERSION, A.BENEFICIARIOS ";
      $query .= " FROM PREINVERSION A ";
      $query .= " LEFT JOIN UBICACION_TERRITORIAL_PREINVERSION E ON (A.ANO=E.ANO AND A.C_PREINVERSION=E.C_PREINVERSION)";
      $query .= " LEFT JOIN INSTITUCION Y ON (A.C_INSTITUCION=Y.C_INSTITUCION)";
      $query .= " LEFT JOIN INSTITUCION Z ON (A.UNIDAD_TECNICA=Z.C_INSTITUCION)";
      $query .= " LEFT JOIN TCLASIFICADOR_PRESUPUESTARIO V ON (A.C_CLASIFICADOR_PRESUPUESTARIO=V.C_CLASIFICADOR AND A.ANO=V.ANIO)";
      $query .= " LEFT JOIN SECTOR F ON (A.C_SECTOR=F.C_SECTOR)";
      $query .= " LEFT JOIN PREINVERSION_TESTADO G ON (A.C_ESTADO_PREINVERSION=G.C_ESTADO_PREINVERSION)";
      $query .= " LEFT JOIN ETAPA_IDI H ON (A.C_ETAPA=H.C_ETAPA_IDI)";
      $query .= " LEFT JOIN PREINVERSION_VIA_FINANCIAMIENTO I ON (A.C_VIA_FINANCIAMIENTO=I.C_VIA_FINANCIAMIENTO)";
      $query .=" WHERE A.ANO=".$xnano;
      $query .=" AND A.VIGENTE=1 ";
      if ($xsector>0) { $query .= " AND A.C_SECTOR = ".$xsector;}
      if ($xviafinanciamiento>0) { $query .= " AND A.C_VIA_FINANCIAMIENTO = ".$xviafinanciamiento;}
      if ($xinstitucion>0) { 
	 $query .= " AND A.C_INSTITUCION = ".$xinstitucion;
      } else {
	 if ($xaccesoinstituciones!='999') {
	    $query .= " AND A.C_INSTITUCION IN (".$xaccesoinstituciones.") ";
	 }
      }
      if ($xestado>0) { $query .= " AND A.C_ESTADO_PREINVERSION = ".$xestado;}
      $query .=" GROUP BY A.CODIGO, A.N_PREINVERSION, A.ANO, A.C_INSTITUCION, A.C_PREINVERSION ";
      $query .=" ORDER BY Y.N_INSTITUCION, A.CODIGO, A.N_PREINVERSION ";
      $filas=0;        
      $dataset= mysql_query($query, $this->conector); 
      $this->nroregistros = mysql_num_rows($dataset);
      while ($record = @mysql_fetch_row($dataset)) { 
         for ($o=0; $o<22; $o++) {
            if ($o<18) {$this->datainforme[$filas][$o]=$record[$o];}
            if ($o==18) {
      	        switch ($record[$o]) {
      	    	    case 1:
         	       $this->datainforme[$filas][$o]='Regional';
         	    break;
         	    case 2:
      	    	       $this->datainforme[$filas][$o]='Provincial';
      	           break;
         	    case 3:
      	 	       $this->datainforme[$filas][$o]='Comunal';
      	    	    break; 	   
         	    case 4:
      	 	       $this->datainforme[$filas][$o]='Localidad';
      	    	    break; 	   
      	        }
            }
            if ($o==20) {
      	        switch ($record[18]) {
      	    	    case 1:
         	       $this->datainforme[$filas][19]='';
         	       $this->datainforme[$filas][20]='';
         	       $this->datainforme[$filas][21]='';
         	    break;
           	    case 2:
         	       $this->datainforme[$filas][19]=$record[20];
         	       $this->datainforme[$filas][20]='';
         	       $this->datainforme[$filas][21]='';
      	           break;
         	    case 3:
         	       $this->datainforme[$filas][19]='';
         	       $this->datainforme[$filas][20]=$record[20];
         	       $this->datainforme[$filas][21]='';
                     $query3  = "SELECT B.N_PROVINCIA FROM COMUNA A, PROVINCIA B WHERE A.ID_COMUNA_INE IN (".$record[19].") ";
             	       $query3 .= " AND A.C_PROVINCIA=B.C_PROVINCIA GROUP BY B.N_PROVINCIA ";
   	              $dataset3= @mysql_query($query3, $this->conector); 
   	              $xtexto="";
   	              while ($record3 = @mysql_fetch_row($dataset3)) {
   	                 if ($xtexto!="") {$xtexto .=",";}
   	                 $xtexto .= $record3[0];
   	              }   
   	              $this->datainforme[$filas][19]=$xtexto; 
      	    	    break; 	   
         	    case 4:
         	       $this->datainforme[$filas][19]='';
         	       $this->datainforme[$filas][20]='';
         	       $this->datainforme[$filas][21]=$record[20];
                     $query3  = "SELECT B.N_COMUNA, B.ID_COMUNA_INE FROM LOCALIDAD A, COMUNA B ";
                     $query3 .= " WHERE (A.C_PROVINCIA*10000)+(A.C_COMUNA*1000)+A.C_LOCALIDAD IN (".$record[19].") ";
                     $query3 .= " AND A.C_PROVINCIA=B.C_PROVINCIA AND A.C_COMUNA=B.C_COMUNA GROUP BY B.N_COMUNA ";
   	              $dataset3= @mysql_query($query3, $this->conector); 
   	              $xtexto="";$xtexto1="";
   	              while ($record3 = @mysql_fetch_row($dataset3)) {
   	                 if ($xtexto!="") {$xtexto .=",";$xtexto1 .=",";}
   	                 $xtexto .= $record3[0];
   	                 $xtexto1 .= $record3[1];
   	              }   
   	              $this->datainforme[$filas][20]=$xtexto; 
               	$query3  = "SELECT B.N_PROVINCIA FROM COMUNA A, PROVINCIA B WHERE A.ID_COMUNA_INE IN (".$xtexto1.") ";
               	$query3 .= " AND A.C_PROVINCIA=B.C_PROVINCIA GROUP BY B.N_PROVINCIA ";
   	              $dataset3= @mysql_query($query3, $this->conector); 
   	              $xtexto="";
   	              while ($record3 = @mysql_fetch_row($dataset3)) {
   	                 if ($xtexto!="") {$xtexto .=",";}
   	                 $xtexto .= $record3[0];
   	              }   
   	              $this->datainforme[$filas][19]=$xtexto; 
      	    	    break; 	   
      	        }
            }
            if ($o==21) {
               $query1  =' SELECT A.C_GRUPO_BENEFICIARIO, B.N_GRUPO_BENEFICIARIO, SUM(A.HOMBRES), SUM(A.MUJERES), SUM(A.AMBOS) FROM BENEFICIARIO_PREINVERSION A, GRUPO_BENEFICIARIO B';
               $query1 .=' WHERE A.C_GRUPO_BENEFICIARIO=B.C_GRUPO_BENEFICIARIO AND ANO='.$record[0].' AND C_PREINVERSION='.$record[21];
               $query1 .=' GROUP BY A.C_GRUPO_BENEFICIARIO, B.N_GRUPO_BENEFICIARIO ';
	        $dataset1= @mysql_query($query1, $this->conector); 
	        $benef='';$total=0;$canti=0;
	        $benefm='';$benefh='';$benefa='';
   	        while ($record1 = @mysql_fetch_row($dataset1)) {
   	           $total=$total+($record1[2]+$record1[3]+$record1[4]);$canti=$canti+1;
	           if ($benef!='') {
	              $benef .=' / '.$record1[1].':'.($record1[2]+$record1[3]+$record1[4]);
	           } else {
	              $benef .=$record1[1].':'.($record1[2]+$record1[3]+$record1[4]);
	           }                  	       
                  if ($record1[2]>0) {
	              if ($benefh!='') {
	                 $benefh .=' / '.$record1[1].':'.$record1[2];
	              } else {
	                 $benefh .=$record1[1].':'.$record1[2];
	              }   
	           }                  	       
                  if ($record1[3]>0) {
	              if ($benefm!='') {
	                 $benefm .=' / '.$record1[1].':'.$record1[3];
	              } else {
	                 $benefm .=$record1[1].':'.$record1[3];
	              }   
	           }                  	       
                  if ($record1[4]>0) {
	              if ($benefa!='') {
	                 $benefa .=' / '.$record1[1].':'.$record1[4];
	              } else {
	                 $benefa .=$record1[1].':'.$record1[4];
	              }   
	           }                  	       
   	        } 
	        mysql_free_result($dataset1);   
               if ($canti>0) {
	           $benef ='N° Beneficiarios Directos: '.$total.' ('.$benef.')';
	        }
 	        if ($record[22]!='') {$benef .='  ('.$record[22].')';}
               $this->datainforme[$filas][22]=$benef;     	       
               $this->datainforme[$filas][23]=$benefh;     	       
               $this->datainforme[$filas][24]=$benefm;     	       
               $this->datainforme[$filas][25]=$benefa;     	       
               // INSTRUMENTOS
               $query9  = " SELECT A.C_INSTRUMENTO, A.N_INSTRUMENTO, A.TIPO_INSTRUMENTO FROM INSTRUMENTO A, VIGENCIA_INSTRUMENTO B ";
               $query9 .= " WHERE A.C_INSTRUMENTO=B.C_INSTRUMENTO AND B.ANO_VIGENCIA=".$record[0];
	        $dataset9= @mysql_query($query9, $this->conector); 
 	        $this->columnas_instrumentos=0;
   	        while ($record9 = @mysql_fetch_row($dataset9)) {
   	           if ($record9[2]==2) {
     	              $query1  =' SELECT RELACIONADO, RELACION2 FROM RELACION_INSTRUMENTO_PREINVERSION';
                     $query1 .=' WHERE C_INSTRUMENTO='.$record9[0].' AND ANO='.$record[0].' AND C_PREINVERSION='.$record[21];
	              $dataset1= @mysql_query($query1, $this->conector); 
   	              $record1 = @mysql_fetch_row($dataset1);
   	              $this->datainforme[$filas][(26+$this->columnas_instrumentos)]=$record1[0];
   	              $this->datainforme[$filas][(27+$this->columnas_instrumentos)]=ereg_replace("[^/( )A-Za-z0-9_áéíóúÁÉÍÓÚ]", "", $record1[1]);
   	              $this->titulos_instrumentos[$this->columnas_instrumentos]=$record9[1]." (Relacionado)";
   	              $this->titulos_instrumentos[($this->columnas_instrumentos+1)]=$record9[1]." (Observaciones)";
   	           } 
          	    if ($record9[2]==1) {
     	              $query1  =' SELECT RELACION1_NOMBRE, RELACION2_NOMBRE FROM RELACION_INSTRUMENTO_PREINVERSION';
                     $query1 .=' WHERE C_INSTRUMENTO='.$record9[0].' AND ANO='.$record[0].' AND C_PREINVERSION='.$record[21];
	              $dataset1= @mysql_query($query1, $this->conector); 
   	              $record1 = @mysql_fetch_row($dataset1);
   	              $this->datainforme[$filas][(26+$this->columnas_instrumentos)]=$record1[0];
   	              $this->datainforme[$filas][(27+$this->columnas_instrumentos)]=$record1[1];
   	              $this->titulos_instrumentos[$this->columnas_instrumentos]=$record9[1]." (Relación Ppal)";
   	              $this->titulos_instrumentos[$this->columnas_instrumentos+1]=$record9[1]." (Otras Relaciones)";
         	    }    	       	
   	           $this->columnas_instrumentos=$this->columnas_instrumentos+2;
   	       }
	       @mysql_free_result($dataset1);   
            }  
         }
         $filas=$filas+1;
      }
      return $this->datainforme;

   }
function cargar_iniciativa_excel_evaluador($xnano,$xsector,$xinstitucion,$xaccesoinstituciones,$xestado,$xpreinversionsel,$xexcel,$xviafinanciamiento,$xevaluador) {
      $query  = " SELECT CONCAT(LEFT(AU.NOMBRES,1),LEFT(AU.APELLIDO_PATERNO,1), LEFT(APELLIDO_MATERNO,1)),
      A.ANO, Y.N_INSTITUCION, A.N_PREINVERSION, Z.N_INSTITUCION,
      CONCAT(V.N_CLASIFICADOR,' (',V.C_CLASIFICADOR,')'),DATE_FORMAT(A.FECHA_INICIO,'%d/%m/%Y'), DATE_FORMAT(A.FECHA_TERMINO,'%d/%m/%Y'),
      A.PRODUCTO, A.CODIGO, A.IMPACTOS, F.N_SECTOR, G.N_ESTADO_PREINVERSION, A.OBSERVACIONES, A.COSTO_TOTAL, A.SOLICITADO, 
      A.SALDO_PROXIMOS_ANOS, H.N_ETAPA_IDI, I.N_VIA_FINANCIAMIENTO, E.C_NIVEL_UT, E.UBICACION_ESPECIFICA, E.UBICACION_ESPECIFICA_NOMBRE,A.C_PREINVERSION, A.BENEFICIARIOS 
      FROM PREINVERSION A 
      LEFT JOIN UBICACION_TERRITORIAL_PREINVERSION E ON (A.ANO=E.ANO AND A.C_PREINVERSION=E.C_PREINVERSION)
      LEFT JOIN INSTITUCION Y ON (A.C_INSTITUCION=Y.C_INSTITUCION)
      LEFT JOIN INSTITUCION Z ON (A.UNIDAD_TECNICA=Z.C_INSTITUCION)
      LEFT JOIN TCLASIFICADOR_PRESUPUESTARIO V ON (A.C_CLASIFICADOR_PRESUPUESTARIO=V.C_CLASIFICADOR AND A.ANO=V.ANIO)
      LEFT JOIN SECTOR F ON (A.C_SECTOR=F.C_SECTOR)
      LEFT JOIN PREINVERSION_TESTADO G ON (A.C_ESTADO_PREINVERSION=G.C_ESTADO_PREINVERSION)
      LEFT JOIN ETAPA_IDI H ON (A.C_ETAPA=H.C_ETAPA_IDI)
      LEFT JOIN PREINVERSION_VIA_FINANCIAMIENTO I ON (A.C_VIA_FINANCIAMIENTO=I.C_VIA_FINANCIAMIENTO)
      LEFT JOIN PREINVERSION_ASIGNACION PA ON (A.C_PREINVERSION=PA.C_PREINVERSION) 
      LEFT JOIN ADMIN_USUARIO AU ON (PA.C_ANALISTA=AU.C_USUARIO)
      WHERE A.ANO=".$xnano." AND VIGENTE=1 ";

      if ($xevaluador != 0) {
        $query .= " AND PA.C_ANALISTA=".$xevaluador;
      }

      if ($xsector>0) { $query .= " AND A.C_SECTOR = ".$xsector;}
      if ($xviafinanciamiento>0) { $query .= " AND A.C_VIA_FINANCIAMIENTO = ".$xviafinanciamiento;}
      if ($xinstitucion>0) { 
   $query .= " AND A.C_INSTITUCION = ".$xinstitucion;
      } else {
   if ($xaccesoinstituciones!='999') {
      $query .= " AND A.C_INSTITUCION IN (".$xaccesoinstituciones.") ";
   }
      }
      if ($xestado>0) { $query .= " AND A.C_ESTADO_PREINVERSION = ".$xestado;}
      $query .=" GROUP BY A.CODIGO, A.N_PREINVERSION, A.ANO, A.C_INSTITUCION, A.C_PREINVERSION ";
      $query .=" ORDER BY Y.N_INSTITUCION, A.CODIGO, A.N_PREINVERSION ";
      
      $filas=0;        
      $dataset= mysql_query($query, $this->conector); 
      $this->nroregistros = mysql_num_rows($dataset);
      while ($record = @mysql_fetch_row($dataset)) { 
         for ($o=0; $o<22; $o++) {
            if ($o<18) {$this->datainforme[$filas][$o]=$record[$o];}
            if ($o==18) {
                switch ($record[$o]) {
                  case 1:
                 $this->datainforme[$filas][$o]='Regional';
              break;
              case 2:
                     $this->datainforme[$filas][$o]='Provincial';
                   break;
              case 3:
                 $this->datainforme[$filas][$o]='Comunal';
                  break;     
              case 4:
                 $this->datainforme[$filas][$o]='Localidad';
                  break;     
                }
            }
            if ($o==20) {
                switch ($record[18]) {
                  case 1:
                 $this->datainforme[$filas][19]='';
                 $this->datainforme[$filas][20]='';
                 $this->datainforme[$filas][21]='';
              break;
                case 2:
                 $this->datainforme[$filas][19]=$record[20];
                 $this->datainforme[$filas][20]='';
                 $this->datainforme[$filas][21]='';
                   break;
              case 3:
                 $this->datainforme[$filas][19]='';
                 $this->datainforme[$filas][20]=$record[20];
                 $this->datainforme[$filas][21]='';
                     $query3  = "SELECT B.N_PROVINCIA FROM COMUNA A, PROVINCIA B WHERE A.ID_COMUNA_INE IN (".$record[19].") ";
                     $query3 .= " AND A.C_PROVINCIA=B.C_PROVINCIA GROUP BY B.N_PROVINCIA ";
                  $dataset3= @mysql_query($query3, $this->conector); 
                  $xtexto="";
                  while ($record3 = @mysql_fetch_row($dataset3)) {
                     if ($xtexto!="") {$xtexto .=",";}
                     $xtexto .= $record3[0];
                  }   
                  $this->datainforme[$filas][19]=$xtexto; 
                  break;     
              case 4:
                 $this->datainforme[$filas][19]='';
                 $this->datainforme[$filas][20]='';
                 $this->datainforme[$filas][21]=$record[20];
                     $query3  = "SELECT B.N_COMUNA, B.ID_COMUNA_INE FROM LOCALIDAD A, COMUNA B ";
                     $query3 .= " WHERE (A.C_PROVINCIA*10000)+(A.C_COMUNA*1000)+A.C_LOCALIDAD IN (".$record[19].") ";
                     $query3 .= " AND A.C_PROVINCIA=B.C_PROVINCIA AND A.C_COMUNA=B.C_COMUNA GROUP BY B.N_COMUNA ";
                  $dataset3= @mysql_query($query3, $this->conector); 
                  $xtexto="";$xtexto1="";
                  while ($record3 = @mysql_fetch_row($dataset3)) {
                     if ($xtexto!="") {$xtexto .=",";$xtexto1 .=",";}
                     $xtexto .= $record3[0];
                     $xtexto1 .= $record3[1];
                  }   
                  $this->datainforme[$filas][20]=$xtexto; 
                $query3  = "SELECT B.N_PROVINCIA FROM COMUNA A, PROVINCIA B WHERE A.ID_COMUNA_INE IN (".$xtexto1.") ";
                $query3 .= " AND A.C_PROVINCIA=B.C_PROVINCIA GROUP BY B.N_PROVINCIA ";
                  $dataset3= @mysql_query($query3, $this->conector); 
                  $xtexto="";
                  while ($record3 = @mysql_fetch_row($dataset3)) {
                     if ($xtexto!="") {$xtexto .=",";}
                     $xtexto .= $record3[0];
                  }   
                  $this->datainforme[$filas][19]=$xtexto; 
                  break;     
                }
            }
            if ($o==21) {
               $query1  =' SELECT A.C_GRUPO_BENEFICIARIO, B.N_GRUPO_BENEFICIARIO, SUM(A.HOMBRES), SUM(A.MUJERES), SUM(A.AMBOS) FROM BENEFICIARIO_PREINVERSION A, GRUPO_BENEFICIARIO B';
               $query1 .=' WHERE A.C_GRUPO_BENEFICIARIO=B.C_GRUPO_BENEFICIARIO AND ANO='.$record[0].' AND C_PREINVERSION='.$record[21];
               $query1 .=' GROUP BY A.C_GRUPO_BENEFICIARIO, B.N_GRUPO_BENEFICIARIO ';
          $dataset1= @mysql_query($query1, $this->conector); 
          $benef='';$total=0;$canti=0;
          $benefm='';$benefh='';$benefa='';
            while ($record1 = @mysql_fetch_row($dataset1)) {
               $total=$total+($record1[2]+$record1[3]+$record1[4]);$canti=$canti+1;
             if ($benef!='') {
                $benef .=' / '.$record1[1].':'.($record1[2]+$record1[3]+$record1[4]);
             } else {
                $benef .=$record1[1].':'.($record1[2]+$record1[3]+$record1[4]);
             }                           
                  if ($record1[2]>0) {
                if ($benefh!='') {
                   $benefh .=' / '.$record1[1].':'.$record1[2];
                } else {
                   $benefh .=$record1[1].':'.$record1[2];
                }   
             }                           
                  if ($record1[3]>0) {
                if ($benefm!='') {
                   $benefm .=' / '.$record1[1].':'.$record1[3];
                } else {
                   $benefm .=$record1[1].':'.$record1[3];
                }   
             }                           
                  if ($record1[4]>0) {
                if ($benefa!='') {
                   $benefa .=' / '.$record1[1].':'.$record1[4];
                } else {
                   $benefa .=$record1[1].':'.$record1[4];
                }   
             }                           
            } 
          mysql_free_result($dataset1);   
               if ($canti>0) {
             $benef ='N° Beneficiarios Directos: '.$total.' ('.$benef.')';
          }
          if ($record[22]!='') {$benef .='  ('.$record[22].')';}
               $this->datainforme[$filas][22]=$benef;              
               $this->datainforme[$filas][23]=$benefh;             
               $this->datainforme[$filas][24]=$benefm;             
               $this->datainforme[$filas][25]=$benefa;             
               // INSTRUMENTOS
               $query9  = " SELECT A.C_INSTRUMENTO, A.N_INSTRUMENTO, A.TIPO_INSTRUMENTO FROM INSTRUMENTO A, VIGENCIA_INSTRUMENTO B ";
               $query9 .= " WHERE A.C_INSTRUMENTO=B.C_INSTRUMENTO AND B.ANO_VIGENCIA=".$record[0];
          $dataset9= @mysql_query($query9, $this->conector); 
          $this->columnas_instrumentos=0;
            while ($record9 = @mysql_fetch_row($dataset9)) {
               if ($record9[2]==2) {
                    $query1  =' SELECT RELACIONADO, RELACION2 FROM RELACION_INSTRUMENTO_PREINVERSION';
                     $query1 .=' WHERE C_INSTRUMENTO='.$record9[0].' AND ANO='.$record[0].' AND C_PREINVERSION='.$record[21];
                $dataset1= @mysql_query($query1, $this->conector); 
                  $record1 = @mysql_fetch_row($dataset1);
                  $this->datainforme[$filas][(26+$this->columnas_instrumentos)]=$record1[0];
                  $this->datainforme[$filas][(27+$this->columnas_instrumentos)]=ereg_replace("[^/( )A-Za-z0-9_áéíóúÁÉÍÓÚ]", "", $record1[1]);
                  $this->titulos_instrumentos[$this->columnas_instrumentos]=$record9[1]." (Relacionado)";
                  $this->titulos_instrumentos[($this->columnas_instrumentos+1)]=$record9[1]." (Observaciones)";
               } 
                if ($record9[2]==1) {
                    $query1  =' SELECT RELACION1_NOMBRE, RELACION2_NOMBRE FROM RELACION_INSTRUMENTO_PREINVERSION';
                     $query1 .=' WHERE C_INSTRUMENTO='.$record9[0].' AND ANO='.$record[0].' AND C_PREINVERSION='.$record[21];
                $dataset1= @mysql_query($query1, $this->conector); 
                  $record1 = @mysql_fetch_row($dataset1);
                  $this->datainforme[$filas][(26+$this->columnas_instrumentos)]=$record1[0];
                  $this->datainforme[$filas][(27+$this->columnas_instrumentos)]=$record1[1];
                  $this->titulos_instrumentos[$this->columnas_instrumentos]=$record9[1]." (Relación Ppal)";
                  $this->titulos_instrumentos[$this->columnas_instrumentos+1]=$record9[1]." (Otras Relaciones)";
              }             
               $this->columnas_instrumentos=$this->columnas_instrumentos+2;
           }
         @mysql_free_result($dataset1);   
            }  
         }
         $filas=$filas+1;
      }
      return $this->datainforme;

   }
function get_nroregistros(){ return $this->nroregistros;}	
   
function get_error()       { return $this->mensaje_error;}
      	
function get_filaseleccionada()       { return $this->filaseleccionada;}

function ingresa_georeferencia($contenedor_array){
  
  /*  
    $query_georef = "INSERT INTO PREINVERSION_GEOREF (ANO, C_INSTITUCION, C_PREINVERSION, ELEMENTO_GRAFICO, CODIGO_OCG) VALUES ('".$this->ano."','".$this->c_institucion."', '".$this->c_preinversion."', '".$contenedor_array['elementoGrafico']."', '".$contenedor_array['codigoOCG']."')";    
    foreach ($contenedor_array['coordenadas'] as $key => $value) {
        
        $query_georef_coor    = "INSERT INTO PREINVERSION_GEOREF_COOR (ANO, C_INSTITUCION ,C_PREINVERSION, ORDEN, EJEX, EJEY) VALUES ('".$this->ano."','".$this->c_institucion."', '".$this->c_preinversion."', '".$contenedor_array['coordenadas'][$key]['orden']."', '".$contenedor_array['coordenadas'][$key]['ejeX']."', '".$contenedor_array['coordenadas'][$key]['ejeY']."')";
        $dataset_georef_coor  = mysql_query($query_georef_coor, $this->conector);
    }
    $dataset_georef  = mysql_query($query_georef, $this->conector);     
    return true;
  */
    /* NUEVO georeferenciacion */

    // elimina la georeferenciacion actual por si tiene
    $d="DELETE FROM PREINVERSION_GEOREF WHERE ANO=" . $this->ano . " AND C_INSTITUCION=" . $this->c_institucion . " AND C_PREINVERSION=" . $this->c_preinversion;
    mysql_query($d, $this->conector);
    $d="DELETE FROM PREINVERSION_GEOREF_COOR  WHERE ANO=" . $this->ano . " AND C_INSTITUCION=" . $this->c_institucion . " AND C_PREINVERSION=" . $this->c_preinversion;
    mysql_query($d, $this->conector);
    // se inserta la nueva georeferenciacion

    $data=json_decode($contenedor_array);    
    if(count($data)>0) // si hay datos
    {          
       $grafico=0; // un punto
       if(count($data)>1) $grafico=1; // una forma
       $query_georef_ari = "INSERT INTO PREINVERSION_GEOREF (ANO, C_INSTITUCION, C_PREINVERSION, ELEMENTO_GRAFICO, CODIGO_OGC) VALUES ('".$this->ano."', '".$this->c_institucion."', '".$this->c_preinversion."', '". $grafico."', '".$data[0]->zona."')";   
       $orden=1;
       foreach ($data as $val) {       
           $query_georef_coor_ari    = "INSERT INTO PREINVERSION_GEOREF_COOR (ANO, C_INSTITUCION, C_PREINVERSION, ORDEN, EJEX, EJEY) VALUES ('".$this->ano."', '".$this->c_institucion."', '".$this->c_preinversion."', '".$orden."', '".$val->x."', '".$val->y."')";
           $dataset_georef_coor_ari  = mysql_query($query_georef_coor_ari, $this->conector);
           $orden++;
       }
       $dataset_georef_ari  = mysql_query($query_georef_ari, $this->conector);    
    }
    return true;  
  }

function carga_georeferencia(){
      /*
      $query_coo  ="SELECT ANO, C_PREINVERSION, ORDEN, EJEX, EJEY FROM PREINVERSION_GEOREF_COOR WHERE C_PREINVERSION= $this->c_preinversion AND ANO= $this->ano ORDER BY ORDEN";
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
      $query  ="SELECT CODIGO_OGC FROM PREINVERSION_GEOREF WHERE C_PREINVERSION= $this->c_preinversion AND ANO= $this->ano LIMIT 1";            
      $dataset  = mysql_query($query, $this->conector); 
      if(mysql_num_rows($dataset)>0) $R=mysql_fetch_array($dataset);
      $zona=(string)$R['CODIGO_OGC'];
      $forma=array();
      /* Fin nueva georeferenciacion */

      $query_coo  ="SELECT ANO, C_PREINVERSION, ORDEN, EJEX, EJEY FROM PREINVERSION_GEOREF_COOR WHERE C_PREINVERSION= $this->c_preinversion AND ANO= $this->ano ORDER BY ORDEN";
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

  /**
   * Funcion que se encarga de enviar los emails en los cambios de estado
   * 
   */
  function envia_email($c_estado)
  {
    $subject  =   "Aviso Chileindica";
    $cabeceras  = 'MIME-Version: 1.0' . "\r\n";
    $cabeceras .= 'Content-type: text/html; charset=iso-8859-1' . "\r\n";

    // Cabeceras adicionales
    $cabeceras .= 'From: Info Chileindica <info@chileindica.cl>' . "\r\n";

    // Obtener la informacion de la iniciativa
    $query    = "SELECT * FROM PREINVERSION 
                  WHERE C_PREINVERSION = ". $this->c_preinversion
                  ." AND ANO = ". $this->ano." AND C_INSTITUCION =". $this->c_institucion;

    $dataset  = mysql_query($query, $this->conector);
    $data     = mysql_fetch_assoc($dataset);
    $iniciativa = $data['N_PREINVERSION'];
    $usuario    = $data['C_USUARIO'];

    // Obtener email del usuario
    $query    = "SELECT MAIL FROM ADMIN_USUARIO_PERFIL WHERE C_USUARIO = ".$usuario;
    $dataset  =   mysql_query($query, $this->conector);
    $data     =   mysql_fetch_assoc($dataset);
    $email    =   $data['MAIL'];

    // Obtener el nombre del estado
    $query2   = "SELECT N_ESTADO_PREINVERSION 
                FROM PREINVERSION_TESTADO 
                WHERE C_ESTADO_PREINVERSION = ".$c_estado;

    $dataset2 =   mysql_query($query2, $this->conector);
    $data     =   mysql_fetch_assoc($dataset2);
    $estado   =   $data['N_ESTADO_PREINVERSION'];

    // Crear mensaje
    $mensaje =  "La iniciativa <b>".$iniciativa."</b>"
                ." paso a estado <b>". $estado ."</b>" 
                .". <br><br><br><i>Por Favor no responda este mail</i>";

    $estado = mail($email, $subject, $mensaje, $cabeceras);

    if (!$estado) {
      // echo "<script>alert('El email de notificación de cambio de estado no se pudo enviar');</script>";
    }else{
      // echo "<script>alert('".$email."');</script>";
    }

  }

  /**
   * Registra los cambios de estado para ser consultados con fecha y hora
   * 
   * @param int $estado
   */
  function registra_cambio_estado()
  {
    $query = "UPDATE PREINVERSION SET F_CAMBIO_ESTADO = CURRENT_TIMESTAMP";
    $query .= " WHERE C_PREINVERSION = ".$this->c_preinversion; 
    $query .= " AND ANO = ".$this->ano;
    $query .= " AND C_INSTITUCION= ".$this->c_institucion;

    $dataset  = mysql_query($query, $this->conector);
    
    if (mysql_errno($this->conector) != 0) { 
      // $this->mensaje_error="No se ha podido Actualizar la Base de Datos, mensaje de error de Mysql es: " . mysql_error($this->conector).$query1; 
    } 
  }

  /**
   * Agrega el cambio de estado a la bitacora del proyecto guardando la siguiente
   * informacion
   *  -> año              : Año de la iniciativa
   *  -> c_preinversion   : Codigo de la iniciativa
   *  -> c_usuario        : Usuario que realiza la operación
   *  -> c_estado_origen  : Codigo del estado de origen
   *  -> n_estado_origen  : Nombre del estado
   *  -> c_estado_destino : Código del estado de destino
   *  -> n_estado_destino : Nombre del estado de destino
   * 
   * Tabla utilizada
   *  PREINVERSION_REGISTRO_ESTADOS
   * 
   * @param int $origen
   * @param int $destino
   */
  function registra_cambio_estado_bitacora($origen, $destino)
  {
 /* para buscar el ultimo registro de la bitacorta para la iniciativa*/  	
      $querybita = " SELECT MAX(ID) FROM PREINVERSION_REGISTRO_ESTADOS ";
		$querybita .=" WHERE C_PREINVERSION = ".$this->c_preinversion;
		$querybita .=" AND ANO = ".$this->ano;
		$querybita .=" AND C_INSTITUCION= ".$this->c_institucion;
                     
      $datasetbita = mysql_query($querybita, $this->conector);
      $nummaxbita  = mysql_fetch_row($datasetbita);
      $id = $nummaxbita[0] + 1;

     	
  	
    $ano                = $this->ano;
    $c_preinversion     = $this->c_preinversion;
    $c_usuario          = $this->c_usuario;
    $c_estado_origen    = $origen;
    $n_estado_origen    = $this->obtener_nombre_etapa($c_estado_origen);
    $c_estado_destino   = $destino;
    $n_estado_destino   = $this->obtener_nombre_etapa($c_estado_destino);
    $c_institucion      = $this->c_institucion;

    $query  = " INSERT INTO PREINVERSION_REGISTRO_ESTADOS(ID, ANO, C_PREINVERSION, C_USUARIO, C_ESTADO_ORIGEN, 
        N_ESTADO_ORIGEN, C_ESTADO_DESTINO, N_ESTADO_DESTINO, C_INSTITUCION)
        VALUES(".$id.",".$ano.",".$c_preinversion." ,".$c_usuario.",".$c_estado_origen." ,
          '".$n_estado_origen."' ,".$c_estado_destino." ,'".$n_estado_destino."',".$c_institucion." )";
          
    error_log("registra cambio estado:".$query);
    $dataset    = mysql_query($query, $this->conector) or error_log(mysql_error($this->conector));
 
    
  }

  /* Carga el historial historico de la iniciativa */
  
 function cargar_cambio_estado_bitacora($preinversion, $ano, $institucion)
 {
    $datos = array();
    $query  = " SELECT FECHA, C_USUARIO, N_ESTADO_ORIGEN, N_ESTADO_DESTINO";
    $query .= " FROM PREINVERSION_REGISTRO_ESTADOS";
    $query .= " WHERE C_PREINVERSION = ".$preinversion;
    $query .= " AND ANO = ".$ano;
    $query .= " AND C_INSTITUCION = ".$institucion;

    $dataset  = mysql_query($query, $this->conector);
    while ( $value = mysql_fetch_assoc($dataset) ) {
      $datos[] = $value;
    }

    return $datos;
 }

  /* Consulta la fecha de los estados de las iniciativas */

  function get_fecha_estado()
  {
    $query  = "SELECT * FROM PREINVERSION_REGISTRO_ESTADOS
              WHERE ANO = ".$ano." AND 
              C_PREINVERSION = ".$c_preinversion." 
              ORDER BY ID DESC
              LIMIT 1 ";
    $dataset =  mysql_query($query, $this->conector);

    $estado   =  mysql_fetch_assoc($dataset);
    
    return $estados;
  }

}

/*____________________________*/

/* UBICACION TERRITORIAL PREINVERSION */
   
Class Ubicacion_Territorial_Preinversion {
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
   
   function Ubicacion_Territorial_Preinversion($conector) { 
      $this->conector =$conector;
      $this->mensaje_error='';
      $this->estado=0;
      $this->nroregistros=0;
   }
   
   function cargar_ubicacion_territorial() {
      $query  =" SELECT C_NIVEL_UT, UBICACION_GENERAL, UBICACION_ESPECIFICA, UBICACION_ESPECIFICA_NOMBRE, AREA_INFLUENCIA ";
      $query .=" FROM UBICACION_TERRITORIAL_PREINVERSION ";
      $query .=" WHERE C_PREINVERSION= ".$this->c_preinversion;
      $query .=" AND ANO= ".$this->ano;
      $query .=" AND C_INSTITUCION= ".$this->c_institucion;
      $dataset  = mysql_query($query, $this->conector); 
      $this->nroregistros = mysql_num_rows($dataset);
      return $dataset;
   }

   function eliminar_ubicacion_territorial() {
      $query  = " DELETE FROM UBICACION_TERRITORIAL_PREINVERSION";
      $query .= " WHERE ANO=".$this->ano." AND C_PREINVERSION=".$this->c_preinversion." AND C_INSTITUCION=".$this->c_institucion;
      $dataset  = mysql_query($query, $this->conector); 
      if (mysql_errno($this->conector) != 0) { 
         $this->mensaje_error="No se ha podido Actualizar la Base de Datos, mensaje de error de Mysql es: " . mysql_error($this->conector).$query; 
      } else {$this->mensaje_error='';}   
      return $this->mensaje_error;
   }
   
   function eliminar_ubicacion_territorial_1() {
      $query  =" DELETE FROM UBICACION_TERRITORIAL_PREINVERSION";
      $query .=" WHERE C_PREINVERSION= ".$this->c_preinversion;
      $query .=" AND ANO= ".$this->ano;
      $query .=" AND C_INSTITUCION= ".$this->c_institucion;
      $dataset  = mysql_query($query, $this->conector); 
      if (mysql_errno($this->conector) != 0) { 
         $this->mensaje_error="No se ha podido Actualizar la Base de Datos, mensaje de error de Mysql es: " . mysql_error($this->conector).$query; 
      } else {$this->mensaje_error='';}   
      return $this->mensaje_error;
   }

   function actualizar_ubicacion_territorial() {
      $query  = " REPLACE INTO UBICACION_TERRITORIAL_PREINVERSION (ANO, C_INSTITUCION, C_PREINVERSION, C_NIVEL_UT, UBICACION_GENERAL, UBICACION_ESPECIFICA, UBICACION_ESPECIFICA_NOMBRE, AREA_INFLUENCIA) VALUES(";
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

/* RELACION INSTRUMENTOS PREINVERSION */
   
Class Relacion_Instrumentos_Preinversion {
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
   
   function Relacion_Instrumentos_Preinversion($conector) { 
      $this->conector =$conector;
      $this->mensaje_error='';
      $this->estado=0;
      $this->nroregistros=0;
   }
   
   function eliminar_relacion_instrumentos() {
      $query  = " DELETE FROM RELACION_INSTRUMENTO_PREINVERSION";
      $query .= " WHERE ANO=".$this->ano." AND C_PREINVERSION=".$this->c_preinversion." AND C_INSTITUCION=".$this->c_institucion;
      $dataset  = mysql_query($query, $this->conector); 
      if (mysql_errno($this->conector) != 0) { 
         $this->mensaje_error="No se ha podido Actualizar la Base de Datos, mensaje de error de Mysql es: " . mysql_error($this->conector).$query; 
      } else {$this->mensaje_error='';}   
      return $this->mensaje_error;
   }
   
   function eliminar_relacion_instrumentos_1() {
      $query  =" DELETE FROM RELACION_INSTRUMENTO_PREINVERSION";
      $query .=" WHERE C_PREINVERSION= ".$this->c_preinversion;
      $query .=" AND ANO= ".$this->ano;
      $dataset  = mysql_query($query, $this->conector); 
      if (mysql_errno($this->conector) != 0) { 
         $this->mensaje_error="No se ha podido Actualizar la Base de Datos, mensaje de error de Mysql es: " . mysql_error($this->conector).$query; 
      } else {$this->mensaje_error='';}   
      return $this->mensaje_error;
   }
   function actualizar_relacion_instrumentos() {
      $query  = " REPLACE INTO RELACION_INSTRUMENTO_PREINVERSION (ANO, C_INSTITUCION, C_PREINVERSION, C_INSTRUMENTO, RELACIONADO, RELACION1, RELACION2,RELACION1_NOMBRE, RELACION2_NOMBRE) VALUES(";	    
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

// BENEFICIARIOS PREINVERSION
   
Class Beneficiario_Preinversion {
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
   
   function Beneficiario_Preinversion($conector) { 
      $this->conector =$conector;
      $this->mensaje_error='';
      $this->estado=0;
      $this->nroregistros=0;
   }
   
   function cargar_beneficiarios() {
      $query  =" SELECT B.N_GRUPO_BENEFICIARIO,A.HOMBRES,A.MUJERES, A.AMBOS, A.MUJERES+A.HOMBRES+A.AMBOS,A.INDIRECTOS, B.C_GRUPO_BENEFICIARIO ";
      $query .=" FROM GRUPO_BENEFICIARIO B, BENEFICIARIO_PREINVERSION A WHERE B.C_GRUPO_BENEFICIARIO=A.C_GRUPO_BENEFICIARIO "; 
      $query .=" AND A.C_PREINVERSION= ".$this->c_preinversion;
      $query .=" AND A.ANO= ".$this->ano;
      $query .=" AND A.C_INSTITUCION= ".$this->c_institucion;
      $dataset  = mysql_query($query, $this->conector); 
      $this->nroregistros = mysql_num_rows($dataset);
      return $dataset;
   }

   function eliminar_beneficiarios() {
      $query  = " DELETE FROM BENEFICIARIO_PREINVERSION";
      $query .= " WHERE ANO=".$this->ano." AND C_PREINVERSION=".$this->c_preinversion." AND C_INSTITUCION=".$this->c_institucion;
      $dataset  = mysql_query($query, $this->conector); 
      if (mysql_errno($this->conector) != 0) { 
         $this->mensaje_error="No se ha podido Actualizar la Base de Datosu, mensaje de error de Mysql es: " . mysql_error($this->conector).$query; 
      } else {$this->mensaje_error='';}   
      return $this->mensaje_error;
   }
   
   function eliminar_beneficiarios_1() {
      $query  =" DELETE FROM BENEFICIARIO_PREINVERSION";
      $query .=" WHERE C_PREINVERSION= ".$this->c_preinversion;
      $query .=" AND ANO= ".$this->ano;
      $dataset  = mysql_query($query, $this->conector); 
      if (mysql_errno($this->conector) != 0) { 
         $this->mensaje_error="No se ha podido Actualizar la Base de Datosu, mensaje de error de Mysql es: " . mysql_error($this->conector).$query; 
      } else {$this->mensaje_error='';}   
      return $this->mensaje_error;
   }

   function actualizar_beneficiarios() {
      $query  =" REPLACE INTO BENEFICIARIO_PREINVERSION VALUES ('";
      $query .= $this->ano."','".$this->c_institucion."','".$this->c_preinversion."',".$this->vector.")";
      $dataset  = mysql_query($query, $this->conector);
      if (mysql_errno($this->conector) != 0) {
         $this->mensaje_error="No se ha podido Actualizar la Base de Datosu, mensaje de error de Mysql es: " . mysql_error($this->conector).$query;
      } else {$this->mensaje_error='';}
      return $this->mensaje_error;
   }


   function actualizar_beneficiarios_ng($grupo_id, $hombres_id,$mujeres_id,$ambos_id,$indirectos_id) {
      $query  =" REPLACE INTO BENEFICIARIO_PREINVERSION VALUES ('";
      $query .= $this->ano."','".$this->c_institucion."','".$this->c_preinversion."','".$grupo_id."','".$hombres_id."','".$mujeres_id."','".$ambos_id."','".$indirectos_id."')";
      error_log("Actualizar Beneficiario:".$query);
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
