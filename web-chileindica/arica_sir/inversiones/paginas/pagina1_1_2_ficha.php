<?
   $restriccion_modulo='01010102';
   $nivel_pagina=2;
   include_once("../../include/seguridad.php");
   include_once("../../include/clases_login.php");   
   include_once("../../login/valida_usuario.php"); 
   include_once("../../include/clases_iniciativa_ari.php");
   include_once("../../include/clases_interfaces.php");
  
// RECUPERA PARAMETROS SERVIDOR
   $xnano=$_POST["nano"];
   $xsector=$_POST["nsector"];
   $xinstitucion=$_POST["ninstitucion"];
  // echo $xinstitucion; exit;
   $xestado=$_POST["nestado"];     
   $xpreinversion=$_POST["npreinversion"];     
   $xaccion=$_POST["naccion"];     
   $soloconsulta=$_POST["nsoloconsulta"];     
    
   

// RECUPERA INFORMACION DE LA PREINVERSION
   if ($xaccion==2) {
    // ESTADO INICIATIVA
  $Biniciativa=new Iniciativa_Ari($BaseDatos->conector);
    $Biniciativa->c_preinversion=$xpreinversion;
    $Biniciativa->ano=$xnano;
    $dataset=$Biniciativa->cargar_iniciativa_estado();
    $Registro = mysql_fetch_row($dataset);
    $xnombreestado  =$Registro[0];
    // DATOS INICIATIVA
    $dataset=$Biniciativa->cargar_iniciativa_ficha();
    $Registro = mysql_fetch_row($dataset);
    $xnombre    = $Registro[0];
        $xunidad_tecnica  = $Registro[1];
        $xprograma    = $Registro[2];
        $xfecha_inicio    = $Registro[3];
        $xfecha_termino   = $Registro[4];
        $xsituacion   = $Registro[5];
        $xproducto    = $Registro[6];
    $xcodigo    = $Registro[7];
    $xtipocodigo    = $Registro[8];     
        $ximpactos    = $Registro[9];
    $xsectorinversion = $Registro[10];
    $xestado_ari        = $Registro[11];
    $xobservaciones     = $Registro[12];
    $xtexto_beneficiarios = $Registro[13];
    $xclasificador    = $Registro[14];
    $xetapaidi    = $Registro[15];
    $xrate      = $Registro[16];
    $xnsector     = $Registro[17];
    $xnunidad_tecnica     = $Registro[18];
    $xnprograma     = $Registro[19];
    $xnclasificador     = $Registro[20];
    $xnetapaidi     = $Registro[21];
    $xnrate     = $Registro[22];
    $xnsituacion      = $Registro[23];
  $xresponsable_propir = $Registro[24];
  $xnresponsable_propir = $Registro[25];
  if ($xfecha_inicio>0) {
       $xfecha_inicio=substr($xfecha_inicio,6,2).'/'.substr($xfecha_inicio,4,2).'/'.substr($xfecha_inicio,0,4);
  } else {$xfecha_inicio='';}
  if ($xfecha_termino>0) {
     $xfecha_termino=substr($xfecha_termino,6,2).'/'.substr($xfecha_termino,4,2).'/'.substr($xfecha_termino,0,4);
  } else {$xfecha_termino='';}
  // NOMBRE INSTITUCION Y SECTOR
    $dataset=$Biniciativa->cargar_iniciativa_institucion_sector();
        $Registro = mysql_fetch_row($dataset);
  $xinstitucion        =$Registro[0];
  $xnombreinstitucion  =$Registro[1];
  $xnombresector       =$Registro[2];
        // TABLA UBICACION_TERRITORIAL_PREINVERSION_ARI
  $Bubicacion=new Ubicacion_Territorial_Iniciativa_Ari($BaseDatos->conector);
    $Bubicacion->c_preinversion=$xpreinversion;
    $Bubicacion->ano=$xnano;        
    $dataset=$Bubicacion->cargar_ubicacion_territorial();
    $Registro = mysql_fetch_row($dataset);
    $xnivel_ut  =$Registro[0];
    $xubicacion =$Registro[1];
    $xcod_ut  =$Registro[2];
    $xnom_ut  =$Registro[3];
    $xinfluencia    =$Registro[4];
  // OBSERVACIONES
    $xnroobservaciones=$Biniciativa->existe_observaciones_iniciativa();
    $xnrodiferencias=$Biniciativa->existe_diferencia_iniciativa();
  $query  = " SELECT RDR, PORCENTAJE FROM RDR_PREINVERSION_ARI ";
    $query .=" WHERE (C_INSTITUCION*1000)+C_PREINVERSION= ".$xpreinversion;
    $query .=" AND ANO= ".$xnano;
    $dataset  = mysql_query($query, $BaseDatos->conector);
    //GEO
    $carga_georeferencia = $Biniciativa->carga_georeferencia_ari();

   } else {
  // NOMBRE INSTITUCION Y SECTOR
  $Biniciativa=new Iniciativa_Ari($BaseDatos->conector);
    $Biniciativa->c_institucion=$xinstitucion;
    $dataset=$Biniciativa->cargar_iniciativa_institucion_sector_1();
        $Registro = mysql_fetch_row($dataset);
  $xnombreinstitucion =$Registro[0];
  $xnombresector    =$Registro[1];
    $xnombreestado    ='Nueva';
    $xsectorinversion =0;
    $xfuente    =0;
    $xcodigo    ='';
    $xnombre    ='';
    $xmonto     ='';
    $xantecedentes    ='';
    $xcod_ut    ='';
    $xnom_ut    ='';
      $xestado_ari    =1;
      $xunidad_tecnica  =$xinstitucion;
      $xresponsable_propir  =$xinstitucion;
      $xprograma    =0;
    $xclasificador    ="";
    $xetapaidi    =0;
    $xrate      =0;
  $xnroobservaciones=0;
  $xnrodiferencias=0;
   }  
  
// INCLUYE BENEFICIARIOS
   $vpantalla=1;
   include("../../include/beneficiarios.php");   
// INCLUYE INSTRUMENTOS DE PLANIFICACION A ASOCIAR A LA DEMANDA
   $vrequerimiento=2;
   include("../../include/instrumentos.php");   
   $combo = new ComboBox();
   $combo->conector=$BaseDatos->conector;
?>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.1//EN" "http://www.w3.org/TR/xhtml11/DTD/xhtml11.dtd">

<html>
<head>
<title>M&oacute;dulo de Seguimiento de Inversiones</title>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
<link rel="stylesheet" href="../../css/estilos1.css" type="text/css">
<link rel="stylesheet" href="../../css/formularios.css" type="text/css">
<link rel="stylesheet" type="text/css" href="../../../css/pagina_ficha.css">
<link rel="stylesheet" type="text/css" href="../../../css/administracion.css">
<link rel="stylesheet" type="text/css" href="../../include/instrumentos.css">
<link rel="stylesheet" type="text/css" href="../../include/clases_interfaces.css">
<link href="../../runtime/styles/xp/aw.css" rel="stylesheet">
<link media=screen href="../../css/dhtmlgoodies_calendar.css" rel=stylesheet>
<!--[if IE]><link href="../../../css/pagina_ficha_ie.css" rel="stylesheet" type="text/css" /><![endif]-->
<script type="text/javascript" src="//ajax.googleapis.com/ajax/libs/jquery/1.8.0/jquery.min.js"></script>
<script language="JavaScript" src="../../../js/jquery-1.8.1.min.js"></script>
<!-- <script language="JavaScript" src="../../include/valida_formulario.js"></script> -->
<script language="JavaScript" src="../../include/procesos_comunes.js"></script>
<script language="JavaScript" src="pagina1_1_2_ficha.js"></script>
<script src="../../runtime/lib/aw.js"></script>
<SCRIPT src="../../include/dhtmlgoodies_calendar_gi_paginas.js" type=text/javascript></SCRIPT>
<!-- SE AÑADE PARA POPUP MAPA GEOLOCALIZACION -->
<script type="text/javascript" src="../../../js/jquery.cookie.js"></script>
<script type="text/javascript" src="../../../js/jquery.colorbox-min.js"></script>
<link rel="stylesheet" href="../../../css/colorbox1/colorbox.css">
<!-- FIN SE AÑADE PARA POPUP MAPA GEOLOCALIZACION -->
<script language="JavaScript">
<!--

/****************************
PARAMETROS FIJOS POR SISTEMA
*****************************/
  var soloconsulta=<? echo $soloconsulta; ?>;
  var ancho=0;
  var alto=0;

//-->
      $(document).ready(function(){        
        /* Se modifica para al geo referenciacion */
        document.form1.coordenadas.value='<? echo $carga_georeferencia[0]; ?>';
        document.form1.contenedor_array.value='<? echo $carga_georeferencia[1]; ?>';        
        /* Fin se modifica*/

        // Para la geolocalización en mapa
        if(soloconsulta==1) modo="R";
        else modo="RMW";        
        // Pasar variables al mapa como región, coordenadas antes guardadas,etc..
        var info={
                    "idRegion": parseInt('<? echo REGION; ?>'),
                    "modo": modo, //1->solo lectura,0=modificacion
                    "forma":[] // por defecto sin formas
                    /*
                    "forma":[
                              {"zona":"32718","x":638149,"y":5857111},
                              {"zona":"32718","x":638649,"y":5887112},
                              {"zona":"32718","x":638849,"y":5877112},
                              {"zona":"32718","x":638149,"y":5857111},
                            ]
                    */
        };
        // Se inyecta la forma en caso de tener valores el input hidden(ver ficha)
        if(document.form1.contenedor_array.value!="")
        {
          var infogeo=document.form1.contenedor_array.value;
          info.forma=JSON.parse(infogeo);          
        }

        $.cookie('infoGeo', JSON.stringify(info), { path: '/'  });           
        //$.session.set('info', JSON.stringify(info)); // se envia como string(no deja guardar object la session)
        // crea popup a mapa
        // si es solo consulta y no hay datos -> no aparece botón a mapa
        if((soloconsulta==1)&&(document.form1.coordenadas.value=="")) $("#linkMmap").hide();
        else $("#linkMmap").colorbox({iframe:true,width:"100%",height:"100%",preloading:true,title:false});
        // Fin para la geolocalización en mapa
      });
</script>
</head>
<body text="#000000" bgcolor="<? echo $_REQUEST["color3"]; ?>" leftmargin="0" topmargin="0" marginwidth="0" marginheight="0" onResize="define_tamano_ventana();" scroll="NO" onLoad="inicializa();">
<?php
   $interface= new I_Mensaje();
   echo $interface->crear_interface();
?>
<form name="form1" method="post" action="">
  <div id="dvHead">
        <div id="dvHeadContenido">
          <div id="dvHeadLeft">
            <span class="tit1">PLANIFICACION DE LA PREINVERSION</span><br/>
            <span class="tit2">Ficha Iniciativa ARI</span>
          </div>
          <div id="dvHeadRight">
            <div id="dvHeadRightTop">
              <span><? echo $xnombreinstitucion; ?> > Año: <? echo $xnano; ?> > Estado: <? echo $xnombreestado; ?></span><br/>
            </div>
            <div id="dvHeadRightBottom">
              <span><a href="#" onclick="cerrar();">Cerrar</a> | <a href="#" onclick='abrir_ayuda("Ficha_ARI.htm");'>Ayuda</a></span>
            </div>
          </div>
        </div>
      </div>
      <div id="hoja" name="hoja" style="height:500px;overflow:auto">
        <div id="dvContenedor">
          <div id="dvContFiltro">

            <div class="dvFiltro1">
              <span>Nombre Iniciativa</span><br />
              <input id="e_nombre" class="form_editconmarco" name="e_nombre" type="text" value="<? echo $xnombre; ?>" maxlength="150"/>
            </div>
            
            <div class="dvFiltro2">
              <span>Sector</span><br />
              <?
              if ($soloconsulta==0) {
                $combo->indice_query=2;
                $combo->valor=$xsectorinversion;
                $combo->nombre="cb_sectorinversion";
                $combo->indice_cero=9;
                $combo->accion="";
                $combo->ancho="170";
                $muestracombo=$combo->crearcombo();
                echo '<script>document.write("'.$muestracombo.'");</script>';
              }else{
                echo "<input name='cb_sectorinversion' type='text' readonly='readonly' id='cb_sectorinversion' value='".$xnsector."'/>";
              }
              ?>
            </div>
          </div>
          <div class="dvContDatosTitulo">ANTECEDENTES GENERALES</div>
          <div id="dvAntecedentesGenerales" class="dvContDatosContenido">
            <table>
              <!-- Barra titulos -->
              <tr>
                <td colspan="3" width="300px">Servicio Resp.Informar</td>
                <!-- <td>Rate</td> -->
                <!-- <td>Situación</td> -->
              <!-- </tr> -->
              <!-- Barra contenido -->
              <!-- <tr> -->
                <td colspan="5">
                  <?
                    if ($soloconsulta==0) {
                      $combo->indice_query=81;
                      $combo->valor=$xresponsable_propir;
                      $combo->nombre="e_responsable_propir";
                      $combo->indice_cero=0;
                      $combo->accion="";
                      $combo->ancho="672";
                      $muestracombo=$combo->crearcombo();
                      echo '<script> document.write("'.$muestracombo.'");</script>';
                    } else {
                      echo "<input name='e_responsable_propir' type='text' readonly class='form_editconmarco' id='e_responsable_propir' style='width: 100% !important;' value='".$xnresponsable_propir."'>";
                    } 
                  ?>
                </td>
                <!-- <td> -->
                  <!-- Rate -->
<!--                   <?
                    if ($soloconsulta==0) {
                      $combo->indice_query=11;
                      $combo->valor=$xrate;
                      $combo->nombre="cb_rate";
                      $combo->indice_cero=0;
                      $combo->accion="";
                      $combo->ancho="20";
                      $muestracombo=$combo->crearcombo();
                      echo '<script> document.write("'.$muestracombo.'");</script>';
                    } else {
                      echo "<input name='cb_rate' type='text' readonly class='form_editconmarco' id='cb_rate' style='width:20px' value='".$xnrate."'>";
                    } 
                  ?>
                </td>
                <td> -->
                  <!-- Situacion -->
<!--                   <?
                    if ($soloconsulta==0) {
                      $combo->indice_query=7;
                      $combo->valor=$xsituacion;
                      $combo->nombre="cb_situacion";
                      $combo->indice_cero=0;
                      $combo->accion="";
                      $combo->ancho="80";
                      $muestracombo=$combo->crearcombo();
                      echo '<script> document.write("'.$muestracombo.'");</script>';
                    } else {
                      echo "<input name='cb_situacion' type='text' readonly class='form_editconmarco' id='cb_situacion' style='width:80px' value='".$xnsituacion."'>";
                    } 
                  ?>
                </td> -->
              </tr>
              <!-- Barra Titulos -->
              <tr>
                <td>Código</td>
                <td>Tipo</td>
                <td colspan="3">Unidad T&eacute;cnica</td>
                <td colspan="3">Programa dentro del cual se circunscribe la acci&oacute;n</td>
              </tr>
              <!-- barra Contenidos-->
              <tr>
                <td><input name="e_codigo" type="text" class="form_editconmarco"  maxlength="17" value="<? echo $xcodigo; ?>"> </td>
                <td>
                  <? if ($soloconsulta==0) {
                      echo "<select name='cb_tipo_codigo'>";
                      if ($xtipocodigo==1) {$sel='selected';} else {$sel='';}
                        echo "<option value='1'".$sel.">BIP</option>";
                        if ($xtipocodigo!=1) {$sel='selected';} else {$sel='';}
                          echo "<option value='0'".$sel.">Otro</option>";
                          echo "</select>";
                        } else {
                          if ($xtipocodigo==1) {$sel='BIP';} else {$sel='Otro';}
                        echo "<input name='cb_tipo_codigo' type='text' readonly class='form_editconmarco' id='cb_tipo_codigo' style='text-align:center;width:45px' value='".$sel."'>";
                        } 
                  ?>
                </td>
                <td colspan="3">
                  <?if ($soloconsulta==0) {
                      $combo->indice_query=83;
                      $combo->valor=$xunidad_tecnica;
                      $combo->nombre="e_unidad_tecnica";
                      $combo->indice_cero=0;
                      $combo->accion="";
                      $combo->ancho="300";
                      $muestracombo=$combo->crearcombo();
                      echo '<script> document.write("'.$muestracombo.'");</script>';
                    } else {
                      echo "<input name='e_unidad_tecnica'style='width:350px !important;' type='text' readonly class='form_editconmarco' id='e_unidad_tecnica' value='".$xnunidad_tecnica."'>";
                    } 
                  ?>
                </td>
                <td colspan="3">
                  <?
                    if ($soloconsulta==0) {
                      $combo->indice_query=8;
                      $combo->valor=$xprograma;
                      $combo->nombre="e_programa";
                      $combo->indice_cero=0;
                      $combo->accion="";
                      $combo->ancho="385";
                      $muestracombo=$combo->crearcombo();
                      echo '<script> document.write("'.$muestracombo.'");</script>';
                    } else {
                      echo "<input name='e_programa' type='text' readonly class='form_editconmarco' id='e_programa' style='width:385px' value='".$xnprograma."'>";
                    }                       
                  ?>
                </td>
              </tr>
              <!-- Barra Titulos -->
              <tr>
                <td>Fecha Inicio</td>
                <td>Fecha T&eacute;rmino</td>
                <td colspan="3">Item Presupuestario</td>
                <td>Etapa</td>
                <td>Rate</td>
                <td>Situación</td>
              </tr>
              <!-- Barra contenido -->
              <tr>
                <td>
                  <!-- Fecha Inicio -->
                  <input name="fecha_inicio" type="text" readonly class="form_editconmarco" 
                      <?php 
                      if ($soloconsulta==0) { 
                         echo "style=\"width:75px;cursor:hand;TEXT-ALIGN:center\" onClick=\"displayCalendar(document.form1.fecha_inicio,'dd/mm/yyyy',this)\"";
                      } else {
                         echo "style=\"width:75px;TEXT-ALIGN:center\"";
                      } 
                      ?>  
                      value="<?php echo $xfecha_inicio; ?>"> 
                </td>
                <!-- fecha termino -->
                <td>
                  <input name="fecha_termino" type="text" readonly class="form_editconmarco" 
                      <?php 
                      if ($soloconsulta==0) { 
                           echo "style=\"width:75px;cursor:hand;TEXT-ALIGN:center\" onClick=\"displayCalendar(document.form1.fecha_termino,'dd/mm/yyyy',this)\"";
                      } else {
                         echo "style=\"width:75px;TEXT-ALIGN:center\"";
                      }
                      ?>
                      value="<? echo $xfecha_termino; ?>">
                </td>
                <!-- Item presupuestario -->
                <td colspan="3"><?
                      if ($soloconsulta==0) {
                        $combo->indice_query=31;
                        $combo->valor=$xclasificador;
                        $combo->nombre="cb_item_presupuestario";
                        $combo->indice_cero=9;
                        $combo->accion="configurar_ficha();";
                        $combo->ancho="140";
                        $combo->comodin=$xnano;
						$combo->comodin .=" AND INICIAL=1 AND INSTITUCION = '$xinstitucion' ";
                        if ($xaccion!=2) {
                          $combo->comodin .=" AND C_CLASIFICADOR NOT LIKE '24%' AND C_CLASIFICADOR NOT LIKE '33%' ";
                        }   
						$combo->comodin .=" ORDER BY C_CLASIFICADOR";
						//echo $combo->comodin;
                        $muestracombo=$combo->crearcombo_comodin1();
                        echo '<script> document.write("'.$muestracombo.'");</script>';
                      } else {
                        echo "<input name='cb_item_presupuestario' type='text' style='width:350px !important;' readonly class='form_editconmarco' id='cb_item_presupuestario' style='width:140px' value='".$xnclasificador."'>";
                      } 
                    ?>
                </td>
                <!-- Etapa -->
                <td>
                  <?
                    if ($soloconsulta==0) {
                      $combo->indice_query=18;
                      $combo->valor=$xetapaidi;
                      $combo->nombre="cb_etapa";
                      $combo->indice_cero=0;
                      $combo->accion="";
                      $combo->ancho="100";
                      $muestracombo=$combo->crearcombo();
                      echo '<script> document.write("'.$muestracombo.'");</script>';
                    } else {
                      echo "<input name='cb_etapa' type='text' readonly class='form_editconmarco' id='cb_etapa' style='width:100px' value='".$xnetapaidi."'>";
                    } 
                  ?>
                </td>
                <!-- Rate -->
                <td>
                  <?
                  if ($soloconsulta==0) {
                    $combo->indice_query=11;
                    $combo->valor=$xrate;
                    $combo->nombre="cb_rate";
                    $combo->indice_cero=0;
                    $combo->accion="activa_boton();";
                    $combo->ancho="70";
                    $muestracombo=$combo->crearcombo();
                    echo '<script> document.write("'.$muestracombo.'");</script>';
                  } else {
                    echo "<input name='cb_rate' type='text' readonly='readonly' id='cb_rate' value='".$xnrate."'/>";
                  } 
                  ?>
                </td>
                <!-- Situación -->
                <td>
                <?
                if ($soloconsulta==0) {
                  $combo->indice_query=7;
                  $combo->valor=$xsituacion;
                  $combo->nombre="cb_situacion";
                  $combo->indice_cero=0;
                  $combo->accion="activa_boton();";
                  $combo->ancho="180";
                  $muestracombo=$combo->crearcombo();
                  echo '<script> document.write("'.$muestracombo.'");</script>';
                } else {
                  echo "<input name='cb_situacion' type='text' readonly='readonly' style ='width:180px !important;' id='cb_situacion' value='".$xnsituacion."'/>";
                } 
                ?>
                </td>
              </tr>
              
              <tr>
                <td colspan="4">Cuantificación de las unidades físicas</td>
                <!-- <td colspan="2">Impactos</td> -->
                <td colspan="4">Descripción de la iniciativa de Inversión</td>
              </tr>
              <tr>
                <td colspan="4">
                  <textarea name='e_producto' rows="5" cols="5"><? echo $xproducto; ?></textarea>
                </td>
<!--                 <td colspan="2">
                   <textarea name='e_impactos' style='width:385px; height:50px; left: 7px; top: 114px;' wrap='VIRTUAL' class='area_texto'><? echo $ximpactos; ?></textarea>
                </td> -->
                <td colspan="4">
                  <textarea id="Observaciones" name="Observaciones" rows="5" cols="5"><? echo $xobservaciones;?></textarea>
                </td>
              </tr>
            </table>
          </div>

          <div class="dvContDatosTitulo">MONTOS Y FUENTES($)</div>

          <div id="dvMontosFuentes" class="dvContDatosContenido">
            <?
              // CARGA GRILLA
              $Bfinanciamiento=new Financiamiento_Iniciativa_Ari($BaseDatos->conector);
              $Bfinanciamiento->c_preinversion=$xpreinversion;
              $Bfinanciamiento->ano=$xnano;        
              $dataset=$Bfinanciamiento->cargar_fuentes_financiamiento();
              $nroregistros = mysql_num_rows($dataset);
              function aw_cells3($dataset) {    
                $canti=0;
                $rows = array(); 
                while ($record = @mysql_fetch_row($dataset)) { 
                  $cols = array(); 
                  foreach ($record as $value) { 
                    $cols[] = '"'.addslashes($value).'"'; 
                  } 
                  $rows[] = "\t[".implode(",", $cols)."]"; 
                  $canti=$canti+1;
                } 
                for ($i=$canti; $i<4; $i++) {
                  $rows[] = "\t['','','','','','0']"; 
                }
                echo "[\n".implode(",\n",$rows)."\n];\n"; 
              }   
              ?>
                                <script language="JavaScript">
                  var Data3 = <?= aw_cells3($dataset) ?>;
                  var monto="Dinero ya asignado al 31/12/"+<? echo ($xnano-1);?>;
                  var Titulos3 = ["Fuente Financiamiento", "Costo Total", "Gast.Años Anteriores", "Solicitado Año","Saldo Prox.Años"];
                  var obj3 = new AW.Grid.Extended;
                  obj3.setId("montos_fuentes");
                  //obj3.setControlPosition(0, 0);
                  obj3.setCellText(Data3);  
                  obj3.setHeaderText(Titulos3);
                  obj3.getHeadersTemplate().setClass("text", "wrap");
                  obj3.setHeaderHeight(34);
                  obj3.setControlSize(978, 150);
                  obj3.setColumnWidth(450, 0);
                  obj3.setColumnWidth(130, 1);
                  obj3.setColumnWidth(130, 2);
                  obj3.setColumnWidth(130, 3);
                  obj3.setColumnWidth(130, 4);
                  var numero3 = new AW.Formats.Number; 
                  var texto3 = new AW.Formats.String;     
                  obj3.setColumnCount(Titulos3.length);
                  obj3.setRowCount(Data3.length);
                  obj3.setColumnIndices([0,1,2,3,4]);   
                  obj3.setColumnResizable(false, 0);
                  obj3.setColumnResizable(false, 1);
                  obj3.setColumnResizable(false, 2);
                  obj3.setColumnResizable(false, 3);
                  obj3.setColumnResizable(false, 4);
                  numero3.setTextFormat("#.###"); 
                  hint3 = ["Doble Clic para selecconar Fuente","Costo Total de la Inversión",monto,"Solicitado Año","Saldo Próximos Años"] 
                  if (soloconsulta!=1) {obj3.setCellTooltip(hint3);}
                  obj3.setCellFormat([texto3,numero3,numero3,numero3,numero3]);
                  if (soloconsulta!=1) {obj3.setCellEditable(true);} else {obj3.setCellEditable(false);}
                  obj3.setCellEditable(false, 0); 
                  obj3.setCellEditable(false, 4); 
                  obj3.setFooterVisible(true);      
                  obj3.setFooterText(["Totales",""]);
                  obj3.setFooterFormat([texto3,numero3,numero3,numero3,numero3]);
                  var total=0;var total1=0;var total2=0;var total3=0;
                  for(var i=0; i<obj3.getRowCount(); i++){
                     if (obj3.getCellText(1,i)!='' && obj3.getCellText(5,i)!='0') {
                        total += parseInt(numero_entero(obj3.getCellText(1,i)));
                     }   
                     if (obj3.getCellText(2,i)!='' && obj3.getCellText(5,i)!='0') {
                        total1 += parseInt(numero_entero(obj3.getCellText(2,i)));
                     }   
                     if (obj3.getCellText(3,i)!='' && obj3.getCellText(5,i)!='0') {
                        total2 += parseInt(numero_entero(obj3.getCellText(3,i)));
                     }   
                     if (obj3.getCellText(1,i)!='' || obj3.getCellText(2,i)!='' || obj3.getCellText(3,i)!='') {
                        if (obj3.getCellText(1,i)!='') {var t1=numero_entero(obj3.getCellText(1,i));} else {var t1=0;}
                        if (obj3.getCellText(2,i)!='') {var t2=numero_entero(obj3.getCellText(2,i));} else {var t2=0;}
                        if (obj3.getCellText(3,i)!='') {var t3=numero_entero(obj3.getCellText(3,i));} else {var t3=0;}
                        var numero = new oNumero(parseInt(t1)-(parseInt(t2)+parseInt(t3)));
                        obj3.setCellText(numero.formato(0,true,''),4,i);
                        total3 += parseInt(numero_entero(obj3.getCellText(4,i)));
                     }
                  }
                  var numero = new oNumero(total);obj3.setFooterText(numero.formato(0,true,''), 1, 0);
                  var numero = new oNumero(total1);obj3.setFooterText(numero.formato(0,true,''), 2, 0);
                  var numero = new oNumero(total2);obj3.setFooterText(numero.formato(0,true,''), 3, 0);
                  var numero = new oNumero(total3);obj3.setFooterText(numero.formato(0,true,''), 4, 0);
                  obj3.onHeaderClicked = function(event,index) {
                     return 'disabled';
                  } 
                     obj3.onCellSelectedChanged = function(event, column, row){
                        if (event==true) {
                     this.raiseEvent("editCurrentCell",{},column,row);
                        }
                     }
                 obj3.onCellTextChanging = function(text, column, row) {
                                if (column==0) {return "error";}
                      if(text.match(/[^0123456789.]/) || text.length>14 || obj3.getCellText(0,row)=="")  {return "error"; }
                 }
                  obj3.onCellValidating = function(text, col, row) {
                     if (col==1 || col==2 || col==3) {
                        value = Number(numero_entero(text));
                        if (col==2) {
                     if (value>Number(numero_entero(obj3.getCellText(1,row)))-Number(numero_entero(obj3.getCellText(3,row)))) {
                        var numero = new oNumero(Number(numero_entero(obj3.getCellText(1,row)))-Number(numero_entero(obj3.getCellText(3,row))));
                        alert("La suma del Monto Gastado y Monto Año no debe ser superior al Monto Total.  Este valor no puede ser supuerior a "+numero.formato(0, true,''));return true;
                     }
                        }
                        if (col==3) {
                     if (value>Number(numero_entero(obj3.getCellText(1,row)))-Number(numero_entero(obj3.getCellText(2,row)))) {
                        var numero = new oNumero(Number(numero_entero(obj3.getCellText(1,row)))-Number(numero_entero(obj3.getCellText(2,row))));
                        alert("La suma del Monto Gastado y Monto Año no debe ser superior al Monto Total.  Este valor no puede ser supuerior a "+numero.formato(0, true,''));return true;
                     }
                        }
                     }
                  }
                  obj3.onCellEditEnded = function(text, col, row) { 
  //                                                         if (text!=""){
                            var numero = new oNumero(numero_entero(text));
                            obj3.setCellText(numero.formato(0, true,''),col,row);
                          obj3.getCellTemplate(col,row).refresh();
                     var total=0;var total3=0;
                     for (var i=0; i<obj3.getRowCount(); i++){
                        if (obj3.getCellText(col,i)!='' && obj3.getCellText(5,i)!='0') {
                          total += parseInt(numero_entero(obj3.getCellText(col,i)));
                        }   
                        if (obj3.getCellText(1,i)!='' || obj3.getCellText(2,i)!='' || obj3.getCellText(3,i)!='') {
                         if (obj3.getCellText(1,i)!='') {var t1=numero_entero(obj3.getCellText(1,i));} else {var t1=0;}
                         if (obj3.getCellText(2,i)!='') {var t2=numero_entero(obj3.getCellText(2,i));} else {var t2=0;}
                         if (obj3.getCellText(3,i)!='') {var t3=numero_entero(obj3.getCellText(3,i));} else {var t3=0;}
                         var numero = new oNumero(parseInt(t1,10)-(parseInt(t2,10)+parseInt(t3,10)));
                         obj3.setCellText(numero.formato(0,true,''),4,i);
                         total3 += parseInt(numero_entero(obj3.getCellText(4,i)));
                        } else {obj3.setCellText('',4,i);}
                     }
                     var numero = new oNumero(total);
                     obj3.setFooterText(numero.formato(0,true,''), col, 0);obj3.getFooterTemplate(col, 0).refresh();
                     var numero = new oNumero(total3);                   
                     obj3.setFooterText(numero.formato(0,true,''), 4, 0);obj3.getFooterTemplate(4, 0).refresh();
  //                                                        }
                  };
                  if (soloconsulta!=1) {
                     obj3.onCellDoubleClicked = function(event, col, row) { 
                        if (col==0) {
                           seleccionar_fuente(row,Data3[row][0],Data3[row][3]);
                        }
                     }
                  }
                  obj3.onKeyEnter = function(event) { 
                     var colid = obj3.getSelectedColumns();     
                     var rowid = obj3.getSelectedRows();
                     if (colid==0) {
                        seleccionar_fuente(rowid,Data3[rowid][0],Data3[rowid][5]);
                     }   
                  }
                  document.write(obj3); 
                  for (var i=0; i<obj3.getRowCount(); i++) {
                     for (var j=1; j<5; j++) {
                        if (obj3.getCellText(j,i)!='') {
                           var numero = new oNumero(numero_entero(obj3.getCellText(j,i)));
              //             obj3.setCellText(numero.formato(0, true,''),j,i);
                       Data3[i][j]=numero.formato(0, true,'');
                       obj3.getCellTemplate(j,i).refresh();
                        }
                     }
                  }

                  function calcular_montos(col) {
                     var total=0;var total3=0;
                     for(var i=0; i<obj3.getRowCount(); i++){
                        if (obj3.getCellText(col,i)!='' && obj3.getCellText(5,i)!='0') {
                     total += parseInt(numero_entero(obj3.getCellText(col,i)));
                        }   
                        if (obj3.getCellText(1,i)!='' || obj3.getCellText(2,i)!='' || obj3.getCellText(3,i)!='') {
                     if (obj3.getCellText(1,i)!='') {var t1=numero_entero(obj3.getCellText(1,i));} else {var t1=0;}
                     if (obj3.getCellText(2,i)!='') {var t2=numero_entero(obj3.getCellText(2,i));} else {var t2=0;}
                     if (obj3.getCellText(3,i)!='') {var t3=numero_entero(obj3.getCellText(3,i));} else {var t3=0;}
                     var numero = new oNumero(parseInt(t1)-(parseInt(t2)+parseInt(t3)));
                     obj3.setCellText(numero.formato(0,true,''),4,i);
                     total3 += parseInt(numero_entero(obj3.getCellText(4,i)));
                        }
                     }
                     var numero = new oNumero(total);
                     obj3.setFooterText(numero.formato(0,true,''), col, 0);obj3.getFooterTemplate(col, 0).refresh();
                     var numero = new oNumero(total3);
                     obj3.setFooterText(numero.formato(0,true,''), 4, 0);obj3.getFooterTemplate(4, 0).refresh();
                  };
                   </script>
                   <br><br>
                   <span><b>IMPORTANTE:</b> Los montos a ingresar deben ser en $ (pesos) </span>
          </div>

          <div class="dvContDatosTitulo">BENEFICIARIOS</div>
          <div id="dvBeneficiarios" class="dvContDatosContenido">
            <?php echo $ventana_beneficiarios; ?>
          </div>
          <div class="dvContDatosTitulo">UBICACIÓN GEOGRÁFICA</div>
          <div id="dvUbicacionGeo" class="dvContDatosContenido">
            <?php
              $interface= new I_Ubicacion_Territorial();
              echo $interface->crear_interface($xnivel_ut,$soloconsulta,$xubicacion,$xinfluencia);
            ?>
          </div>
          <div class="dvContDatosTitulo">RELACIÓN CON INSTRUMENTOS Y POLÍTICAS</div>
          <div id="dvRelacionInstryPoli" class="dvContDatosContenido">
            <? echo $ventanainstrumento;?>
          </div>

        </div>
      </div>
      <div id="dvBotones">
        <div class="left">
          <?php if ($soloconsulta!=1 && ($xestado_ari==1 || $xestado_ari==3)) {echo "<input name='b_pendiente' type='button' class='boton' value='Guardar y Dejar Pendiente' style='width:150px;cursor:pointer' onclick='activa_proceso(1);' title='Graba los cambios y deja la ficha en estado pendiente'>";} ?>
          <?php if ($soloconsulta!=1 && ($xestado_ari==1 || $xestado_ari==3)) {echo "<input name='b_enviar' type='button' class='boton' value='Enviar Intendente' style='width:120px;cursor:pointer' onclick='activa_proceso(10);' title='Graba los cambios y envía la ficha al Intendente'>";} ?>
          <?php if ($xnroobservaciones>0) {
                echo "<img src='../../imagenes/observaciones.gif' width='80' height='20' style='cursor:pointer;' onClick='mostrar_observaciones();'>";
              }?> 
          <?php if ($xnrodiferencias>0) {
                echo "<img src='../../imagenes/diferencias.gif' width='80' height='20' style='cursor:pointer;' onClick='mostrar_diferencias();'>";
              }?> 
        </div>
        <div class="right">
          <!--<input name='b_cerrar' type='button' class='boton' value='Cerrar' style='width:80px;cursor:pointer' onclick='cerrar();' title='Permite cerrar la ventana actual'>
          <input name='b_ayuda' type='button' class='boton' value='Ayuda' style='width:50px;cursor:pointer' onclick='abrir_ayuda("Ficha_ARI.htm");' title='Obtener Ayuda'>-->
        </div>
      </div>

    <input type="hidden" name="nsector" value="<? echo $xsector; ?>">
    <input type="hidden" name="nestado" value="<? echo $xestado; ?>">
    <input type="hidden" name="nano" value="<? echo $xnano ?>">
    <input type="hidden" name="ninstitucion" value="<? echo $xinstitucion ?>">
    <input type="hidden" name="npreinversion" value="<? echo $xpreinversion ?>">
    <input type="hidden" name="nestadoari" value="<? echo $xestado_ari; ?>">
    <input type="hidden" name="naccion" value="0">
    <input type="hidden" name="cod_ut" value="<? echo $xcod_ut; ?>">
    <input type="hidden" name="nom_ut" value="<? echo $xnom_ut; ?>">
    <input type="hidden" name="vector_financiamiento" value="">
    <input type="hidden" name="vector_beneficiarios" value="">
    <input type="hidden" name="netapaidi" value="<? echo $xetapaidi; ?>">
    <input type="hidden" name="color1" value="<?php echo $_REQUEST["color1"]; ?>">
    <input type="hidden" name="color2" value="<?php echo $_REQUEST["color2"]; ?>">
    <input type="hidden" name="color3" value="<?php echo $_REQUEST["color3"]; ?>">
    <input type="hidden" name="ncosto_total" value="0">
    <input type="hidden" name="ngastado_anos_anteriores" value="0">
    <input type="hidden" name="nsolicitado" value="0">
    <input type="hidden" name="nsaldo_proximos_anos" value="0">
    <input type="hidden" name="usuario" value="<?php echo $_REQUEST["usuario"]; ?>">
    <input type="hidden" name="id_usuario" value="<?php echo $_REQUEST["id_usuario"]; ?>">
    <input type="hidden" name="uscl" value="<?php echo $_REQUEST["uscl"]; ?>">
    <input type="hidden" name="contenedor_array" id="contenedor_array" value="" />
    <? echo $textohidden; ?>
    <script>
       var totalsubfichas=document.form1.SInstrumento.length;
    </script>
  <iframe id="v_grabar" name="v_grabar" src="" frameborder="0" width="0px" height="0px" class="displayNone"></iframe>
</form>
<?php
   @mysql_close($BaseDatos->conector);
?>
<form name="form_ayuda" method="post" action="">
  <input type="hidden" name="nayuda" value="">
</form>
</body>
</html>
