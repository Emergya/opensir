<?
   $restriccion_modulo='01010103';
   $nivel_pagina=2;
   include_once("../../include/seguridad.php");
   include_once("../../include/clases_login.php");   
   include_once("../../login/valida_usuario.php"); 
   include_once("../../include/clases_iniciativa_propir.php");
   include_once("../../include/clases_interfaces.php");

// RECUPERA PARAMETROS SERVIDOR
   $xnano=$_POST["nano"];
   $xsector=$_POST["nsector"];
   $xinstitucion=$_POST["ninstitucion"];
   $xestado=$_POST["nestado"];     
   $xpreinversion=$_POST["npreinversion"];     
   $xaccion=$_POST["naccion"];     
   $soloconsulta=$_POST["nsoloconsulta"];     
   $anchoiframe=$_POST["ancho_iframe"];   
    
// RECUPERA INFORMACION DE LA PREINVERSION
   if ($xaccion==2) {
   	// ESTADO INICIATIVA
	$Biniciativa=new Iniciativa_Propir($BaseDatos->conector);
   	$Biniciativa->c_preinversion=$xpreinversion;
   	$Biniciativa->ano=$xnano;
   	$dataset=$Biniciativa->cargar_iniciativa_estado();
   	$Registro = mysql_fetch_row($dataset);
   	$xnombreestado	=$Registro[0];
   	// DATOS INICIATIVA
   	$dataset=$Biniciativa->cargar_iniciativa_ficha();
   	$Registro = mysql_fetch_row($dataset);
   	$xnombre		=	$Registro[0];
        $xunidad_tecnica	=	$Registro[1];
        $xprograma 		=	$Registro[2];
        $xfecha_inicio		=	$Registro[3];
        $xfecha_termino		=	$Registro[4];
        $xsituacion		=	$Registro[5];
        $xproducto		=	$Registro[6];
   	$xcodigo		=	$Registro[7];
   	$xtipocodigo		=	$Registro[8];     
        $ximpactos		=	$Registro[9];
   	$xsectorinversion	=	$Registro[10];
   	$xestado_propir      	=	$Registro[11];
   	$xobservaciones    	=	$Registro[12];
   	$xtexto_beneficiarios	=	$Registro[13];
   	$xclasificador		=	$Registro[14];
   	$xetapaidi		=	$Registro[15];
   	$xrate			=	$Registro[16];
   	$xnsector			=	$Registro[17];
   	$xnunidad_tecnica			=	$Registro[18];
   	$xnprograma			=	$Registro[19];
   	$xnclasificador			=	$Registro[20];
   	$xnetapaidi			=	$Registro[21];
   	$xnrate			=	$Registro[22];
   	$xnsituacion			=	$Registro[23];
   	$xresponsable_ejecucion			=	$Registro[24];
   	$xnresponsable_ejecucion			=	$Registro[25];
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
	$xnombresector	     =$Registro[2];
        // TABLA UBICACION_TERRITORIAL_PREINVERSION_PROPIR
	$Bubicacion=new Ubicacion_Territorial_Iniciativa_Propir($BaseDatos->conector);
   	$Bubicacion->c_preinversion=$xpreinversion;
   	$Bubicacion->ano=$xnano;        
   	$dataset=$Bubicacion->cargar_ubicacion_territorial();
   	$Registro = mysql_fetch_row($dataset);
   	$xnivel_ut	=$Registro[0];
   	$xubicacion	=$Registro[1];
   	$xcod_ut	=$Registro[2];
   	$xnom_ut	=$Registro[3];
	$xinfluencia    =$Registro[4];
	// OBSERVACIONES
   	$xnroobservaciones=$Biniciativa->existe_observaciones_iniciativa();
	$query  = " SELECT RDR, PORCENTAJE FROM RDR_PREINVERSION_ARI ";
    $query .=" WHERE (C_INSTITUCION*1000)+C_PREINVERSION= ".$xpreinversion;
    $query .=" AND ANO= ".$xnano;
    $dataset  = mysql_query($query, $BaseDatos->conector); 
	//GEO
	$carga_georeferencia = $Biniciativa->carga_georeferencia_propir();

   } else {
	// NOMBRE INSTITUCION Y SECTOR

	$Biniciativa=new Iniciativa_Propir($BaseDatos->conector);
   	$Biniciativa->c_institucion=$xinstitucion;
   	$dataset=$Biniciativa->cargar_iniciativa_institucion_sector_1();
        $Registro = mysql_fetch_row($dataset);
	$xnombreinstitucion	=$Registro[0];
	$xnombresector		=$Registro[1];
   	$xnombreestado		='Nueva';
   	$xsectorinversion	=0;
   	$xfuente		=0;
   	$xcodigo		='';
   	$xnombre		='';
   	$xmonto			='';
   	$xantecedentes		='';
   	$xcod_ut		='';
   	$xnom_ut		='';
     	$xestado_propir		=1;
     	$xunidad_tecnica	=$xinstitucion;
		$xresponsable_ejecucion=$xinstitucion;
     	$xprograma		=0;
   	$xclasificador 		="";
   	$xetapaidi		=0;
   	$xrate			=0;
   	$xnsector			="";
   	$xnunidad_tecnica			=	"";
   	$xnprograma			=	"";
   	$xnclasificador			=	"";
   	$xnetapaidi			=	"";
   	$xnrate			=	"";
   	$xnsituacion			=	"";
	$xnroobservaciones=0;
   }	
// INCLUYE BENEFICIARIOS
   $vpantalla=2;
   include("../../include/beneficiarios.php");   
// INCLUYE INSTRUMENTOS DE PLANIFICACION A ASOCIAR A LA DEMANDA
   $vrequerimiento=3;
   include("../../include/instrumentos.php");   
 
   $combo = new ComboBox();
   $combo->conector=$BaseDatos->conector;
?>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.1//EN" "http://www.w3.org/TR/xhtml11/DTD/xhtml11.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
		<title>Módulo de Seguimiento de Inversiones</title>
		<meta http-equiv="X-UA-Compatible" content="IE=8" />
    	<script language="JavaScript" src="../../../js/jquery-1.8.1.min.js"></script>
		<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1"/>
		<link rel="stylesheet" href="../../css/estilos1.css" 	type="text/css"/>
		<link rel="stylesheet" href="../../css/formularios.css" type="text/css"/>
		<script type="text/javascript" src="../../include/procesos_comunes.js"	></script>
		<script type="text/javascript" src="pagina1_1_3_ficha.js"				></script>
		<script type="text/javascript" src="../../runtime/lib/aw.js"			></script>
		<link href="../../runtime/styles/xp/aw.css" rel="stylesheet"/>
		<link href="../../css/dhtmlgoodies_calendar.css" rel="stylesheet"/>
		<script src="../../include/dhtmlgoodies_calendar_gi_paginas.js" type="text/javascript"></script>
		
		
		
    <script language="JavaScript" src="../../../js/jquery-1.8.1.min.js"></script>
		<link rel="stylesheet" href="../../include/clases_interfaces.css" 	type="text/css"/>
		<link rel="stylesheet" href="../../include/instrumentos.css" 	type="text/css"/>
		<link rel="stylesheet" href="../../../css/pagina_ficha.css" 	type="text/css"/>
		<!--[if IE]><link href="../../../css/pagina_ficha_ie.css" rel="stylesheet" type="text/css" /><![endif]-->
		<link rel="stylesheet" href="../../css/administracion.css" 	type="text/css"/>
		<link rel="stylesheet" type="text/css" href="../../($Inbox)/../../css/administracion.css">
		<!-- SE AÑADE PARA POPUP MAPA GEOLOCALIZACION -->
		<script type="text/javascript" src="../../../js/jquery.cookie.js"></script>
		<script type="text/javascript" src="../../../js/jquery.colorbox-min.js"></script>
		<link rel="stylesheet" href="../../../css/colorbox1/colorbox.css">
		<!-- FIN SE AÑADE PARA POPUP MAPA GEOLOCALIZACION -->
		<script type="text/javascript" >
		/****************************
			PARAMETROS FIJOS POR SISTEMA
			*****************************/
			var soloconsulta=<? echo $soloconsulta; ?>;
			var ancho=0;
			var alto=0;

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
	<body onLoad="inicializa();">
	<?php
	   $interface= new I_Mensaje();
	   echo $interface->crear_interface();
	?>
		<form name="form1" method="post" action="">
			<div id="dvHead">
				<div id="dvHeadContenido">
					<div id="dvHeadLeft">
						<span class="tit1">PROGRAMA PUBLICO DE INVERSION</span><br/>
						<span class="tit2">Propir - Ficha</span>
					</div>
					<div id="dvHeadRight">
						<div id="dvHeadRightTop">
							<span><? echo $xnombreinstitucion; ?> > Año: <? echo $xnano; ?> > Estado: <? echo $xnombreestado; ?></span><br/>
						</div>
						<div id="dvHeadRightBottom">
							<span><a href="#" onclick="cerrar();">Cerrar</a> | <a href="#" onclick='abrir_ayuda("Ficha_PROPIR.htm");'title='Obtener Ayuda' >Ayuda</a></span>
						</div>
					</div>
				</div>
			</div>
			<script>
			jQuery(document).ready(function($) {
				$("#hoja").height(parent.document.form_plantilla.alto_iframe.value-100);
			});
			
			//document.write('<div id="hoja" name="hoja" style="height:'+(parent.document.form_plantilla.alto_iframe.value-150)+'px;overflow:auto"');
			</script>
			<div id="hoja" name="hoja" style="overflow:auto">
			<div id="dvContenedor">
				<div id="dvContFiltro">
					<div class="dvFiltro1">
						<span>Nombre Iniciativa</span>
            <br />
						<input id="e_nombre" name="e_nombre" type="text" value="<? echo $xnombre; ?>" maxlength="150"/>
					</div>
					<div class="dvFiltro2">
						<span>Sector</span>
            <br />
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
						<tr>
							<td colspan="2">Servicio Resp. Ejecución</td>
							<td colspan="4">
							<?
							if ($soloconsulta==0) {
								$combo->indice_query=82;
								$combo->valor=$xresponsable_ejecucion;
								$combo->nombre="e_responsable_ejecucion";
								$combo->indice_cero=0;
								$combo->accion="";
								$combo->ancho="300";
								$muestracombo=$combo->crearcombo();
								echo '<script> document.write("'.$muestracombo.'");</script>';
							} else {
								echo "<input name='e_responsable_ejecucion' type='text' readonly class='form_editconmarco' id='e_responsable_propir' style='width:300px' value='".$xnresponsable_ejecucion."'>";
							}	
							?>
							</td>
						</tr>
						<tr>
							<td>Código</td>
							<td>Tipo</td>
							<td colspan="2">Unidad Técnica</td>
							<td colspan="2">Programa dentro del cual se circunscribe la acción</td>
						</tr>
						<tr>
							<td><input name="e_codigo" type="text" size="17" value="<? echo $xcodigo; ?>" /></td>
							<td>
							<? 
							if ($soloconsulta==0) {
								echo "<select name='cb_tipo_codigo' onChange='activa_boton();'>";
								if ($xtipocodigo==1) {$sel="selected='selected'";} else {$sel="";}
								echo "<option value='1' ".$sel.">BIP</option>";
								if ($xtipocodigo!=1) {$sel="selected='selected'";} else {$sel="";}
								echo "<option value='0' ".$sel.">Otro</option>";
								echo "</select>";
							} else {
								if ($xtipocodigo==1) {$sel='BIP';} else {$sel='Otro';}
								echo "<input name='cb_tipo_codigo' type='text' readonly='readonly' id='cb_tipo_codigo' value='".$sel."'/>";
							}	
							?>
							</td>
							<td colspan="2">
							<?
							if ($soloconsulta==0) {
								$combo->indice_query=83;
								$combo->valor=$xunidad_tecnica;
								$combo->nombre="e_unidad_tecnica";
								$combo->indice_cero=0;
								$combo->accion="activa_boton();";
								$combo->ancho="300";
								$muestracombo=$combo->crearcombo();
								echo '<script> document.write("'.$muestracombo.'");</script>';
							} else {
								echo "<input name='e_unidad_tecnica' type='text' style='width:350px !important;' readonly='readonly' id='e_unidad_tecnica' value='".$xnunidad_tecnica."'/>";
							}	
							?>
							</td>
							<td colspan="2">
							<?
							if ($soloconsulta==0) {
								$combo->indice_query=8;
								$combo->valor=$xprograma;
								$combo->nombre="e_programa";
								$combo->indice_cero=0;
								$combo->accion="activa_boton();";
								$combo->ancho="385";
								$muestracombo=$combo->crearcombo();
								echo '<script> document.write("'.$muestracombo.'");</script>';
							} else {
								echo "<input name='e_programa' type='text' readonly='readonly' id='e_programa' value='".$xnprograma."'/>";
							}												
							?>
							</td>
						</tr>
						<tr>
							<td>Fecha Inicio</td>
							<td>Fecha Termino</td>
							<td>Item Presupuestario</td>
							<td>Etapa</td>
							<td>Rate</td>
							<td>Situación</td>
						</tr>
						<tr>
							<td>
								<input name="fecha_inicio" type="text" readonly="readonly"
								<?php 
								if ($soloconsulta==0) {
									echo " onClick=\"activa_boton();displayCalendar(document.form1.fecha_inicio,'dd/mm/yyyy',this)\"";
								}
								?>  
								value="<?php echo $xfecha_inicio; ?>" /> 
							</td>
							<td>
								<input name="fecha_termino" type="text" readonly="readonly"  
								<?php 
								if ($soloconsulta==0) { 
									echo " onClick=\"activa_boton();displayCalendar(document.form1.fecha_termino,'dd/mm/yyyy',this)\"";
								}
								?>
								value="<? echo $xfecha_termino; ?>" />
							</td>
							<td>
							<?
							if ($soloconsulta==0) {
								$combo->indice_query=31;
								$combo->valor=$xclasificador;
								$combo->nombre="cb_item_presupuestario";
								$combo->indice_cero=9;
								$combo->accion="configurar_ficha();activa_boton();";
								$combo->ancho="140";
								$combo->comodin=$xnano;	
								$combo->comodin .=" AND INICIAL=1 AND INSTITUCION = '$xinstitucion' ";
                                if ($xaccion!=2) {
                                  $combo->comodin .=" AND C_CLASIFICADOR NOT LIKE '24%' AND C_CLASIFICADOR NOT LIKE '33%' ";
                                }   
						        $combo->comodin .=" ORDER BY C_CLASIFICADOR";  							    
								$muestracombo=$combo->crearcombo_comodin1();
								echo '<script> document.write("'.$muestracombo.'");</script>';
							} else {
								echo "<input name='cb_item_presupuestario' type='text' style='width:350px !important;' readonly='readonly' id='cb_item_presupuestario' value='".$xnclasificador."'/>";
							}	
							?>
							</td>
							<td>
							<?
							if ($soloconsulta==0) {
								$combo->indice_query=18;
								$combo->valor=$xetapaidi;
								$combo->nombre="cb_etapa";
								$combo->indice_cero=0;
								$combo->accion="activa_boton();";
								$combo->ancho="100";
								$muestracombo=$combo->crearcombo();
								echo '<script> document.write("'.$muestracombo.'");</script>';
							} else {
								echo "<input name='cb_etapa' type='text' readonly='readonly'  id='cb_etapa' value='".$xnetapaidi."'/>";
							}	
							?>
							</td>
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
							<td colspan="3">Cuantificación de las unidades físicas</td>
							<!-- <td colspan="3">Impactos</td> -->
							<td colspan="3">Descripción de la Iniciativa de Inversión</td>
						</tr>
						<tr>
							<td colspan="3">
								<textarea name='e_producto' cols="10" onChange="activa_boton();"><? echo $xproducto; ?></textarea>
							</td>
<!-- 							<td colspan="3">
								<textarea name='e_impactos' cols="10" onChange="activa_boton();"><? echo $ximpactos; ?></textarea>
							</td> -->
						<!-- </tr>
						<tr> -->
							
						<!-- </tr>
						<tr> -->
							<td colspan="3"><textarea id="textarea8" name="Observaciones" onChange="activa_boton();"><? echo $xobservaciones;?></textarea></td>
						</tr>
					</table>
				</div>
				<div class="dvContDatosTitulo">MONTOS Y FUENTES($)</div>
				<div id="dvMontosFuentes" class="dvContDatosContenido">
					<style>
						/*.aw-alternate-even {background: #ffffff;} 
						.aw-alternate-odd {background: #Ffffdd;} 
						.aw-grid-control {height: 100%; width: 100%; margin: 0px; font: menu;}
						.aw-grid-column  {border-right: 1px solid threedlightshadow;}
						.aw-grid-row     {border-bottom: 1px solid threedlightshadow;}
						.aw-grid-footer .aw-grid-footer {BORDER-RIGHT: medium none; PADDING-RIGHT: 0px; BORDER-TOP: medium none; PADDING-LEFT: 0px; BACKGROUND: none transparent scroll repeat 0% 0%; PADDING-BOTTOM: 1px; BORDER-LEFT: medium none; PADDING-TOP: 0px; BORDER-BOTTOM: medium none}
						.aw-grid-footer .aw-item-box {BACKGROUND: none transparent scroll repeat 0% 0%; BORDER-BOTTOM: medium none}
						.aw-grid-footer {	BORDER-TOP: darkgray 1px solid; BACKGROUND: #EBEADB; BORDER-LEFT: darkgray 1px solid}
						.aw-grid-cell {border-right: darkgray 1px solid;border-bottom: darkgray 1px solid;}
						.aw-cells-selected {BACKGROUND: #555555!important; color: #ffffff!important;}
						.aw-edit-cell  {    BACKGROUND: #555555!important; color: #ffffff!important;}						        
						#montos_fuentes .aw-column-0 {text-align: left; cursor:hand;}
						#montos_fuentes .aw-column-1 {text-align: right;}
						#montos_fuentes .aw-column-2 {text-align: right;}
						#montos_fuentes .aw-column-3 {text-align: right;}
						#montos_fuentes .aw-column-4 {text-align: right;}
						#montos_fuentes .aw-column-6 {text-align: left; cursor:hand;}
						.aw-grid-headers  {color:navy;}
						.aw-grid-header .aw-item-box {BACKGROUND: #000000 transparent scroll repeat 0% 0%; BORDER-BOTTOM: darkgray 1px solid;BORDER-LEFT: darkgray 1px solid;}
						#montos_fuentes .aw-gpanel-top .aw-grid-header {TEXT-ALIGN: center}
						#montos_fuentes {width:100%!important;}*/
					</style>
					<?
					// CARGA GRILLA
					$Bfinanciamiento=new Financiamiento_Iniciativa_Propir($BaseDatos->conector);
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
							$rows[] = "\t['','','','','','0','','0']"; 
						}
						echo "[\n".implode(",\n",$rows)."\n];\n"; 
					}
					?>
					<script language="JavaScript">
						var Data3 = <?= aw_cells3($dataset) ?>;
						var monto="Dinero ya asignado al 31/12/"+<? echo ($xnano-1);?>;
						var Titulos3 = [ "Fuente Financiamiento", "Costo Total", "Gast.Años Anteriores", "Solicitado Año","Saldo Prox.Años","C_FUENTE_FINANCIAMIENTO","Serv.Resp.Inform.Ejecución","C_SERVICIO" ];
						var obj3 = new AW.Grid.Extended;
						obj3.setId("montos_fuentes");
						//obj3.setControlPosition(0, 0);
						obj3.setCellText(Data3);	
						obj3.setHeaderText(Titulos3);
						obj3.getHeadersTemplate().setClass("text", "wrap");
						obj3.setHeaderHeight(34);
						obj3.setControlSize(978, 150);
						obj3.setColumnWidth(300, 0);
						obj3.setColumnWidth(130, 1);
						obj3.setColumnWidth(130, 2);
						obj3.setColumnWidth(130, 3);
						obj3.setColumnWidth(130, 4);
						obj3.setColumnWidth(151, 6);
						var numero3 = new AW.Formats.Number; 
						var texto3 = new AW.Formats.String;     
						obj3.setColumnCount(Titulos3.length);
						obj3.setRowCount(Data3.length);
						obj3.setColumnIndices([0,1,2,3,4,6]); 
            obj3.setColumnResizable(false, 0);
            obj3.setColumnResizable(false, 1);
            obj3.setColumnResizable(false, 2);
            obj3.setColumnResizable(false, 3);
            obj3.setColumnResizable(false, 4);  
            obj3.setColumnResizable(false, 6);	
						numero3.setTextFormat("#.###"); 
						hint3 = ["Doble Clic para selecconar Fuente","Costo Total de la Inversión",monto,"Solicitado Año","Saldo Próximos Años","","Doble Clic para seleccionar Serv.Resp.Ejecución",""] 
						if (soloconsulta!=1) {obj3.setCellTooltip(hint3);}
						obj3.setCellFormat([texto3,numero3,numero3,numero3,numero3]);
						if (soloconsulta!=1) {obj3.setCellEditable(true);} else {obj3.setCellEditable(false);}
						obj3.setCellEditable(false, 0);	
						obj3.setCellEditable(false, 4);	
						obj3.setCellEditable(false, 6);	
						obj3.setFooterVisible(true);      
						obj3.setFooterText(["Total:",""]);
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
							if (column==0 || column==6) {return "error";}
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
							if (text!=""){
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
									}
								}
								var numero = new oNumero(total);
								obj3.setFooterText(numero.formato(0,true,''), col, 0);obj3.getFooterTemplate(col, 0).refresh();
								var numero = new oNumero(total3);								   
								obj3.setFooterText(numero.formato(0,true,''), 4, 0);obj3.getFooterTemplate(4, 0).refresh();
							}
						};
						if (soloconsulta!=1) {
							obj3.onCellDoubleClicked = function(event, col, row) { 
								if (col==0) {
									seleccionar_fuente(row,Data3[row][0],Data3[row][5]);
								}
								if (col==6) {
									seleccionar_servicio(row,Data3[row][6],Data3[row][7]);
								}
							}
						}
						obj3.onKeyEnter	= function(event) { 
							var colid = obj3.getSelectedColumns();			
							var rowid = obj3.getSelectedRows();
							if (colid==0) {
								seleccionar_fuente(rowid,Data3[rowid][0],Data3[rowid][5]);
							}   
							if (colid==6) {
								seleccionar_fuente(rowid,Data3[rowid][6],Data3[rowid][7]);
							}   
						}
						document.write(obj3);	
						for (var i=0; i<obj3.getRowCount(); i++) {
							for (var j=1; j<5; j++) {
								if (obj3.getCellText(j,i)!='') {
									var numero = new oNumero(numero_entero(obj3.getCellText(j,i)));
									obj3.setCellText(numero.formato(0, true,''),j,i);
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
					<br /><br />	
					<span>IMPORTANTE: Los montos a ingresar deben ser en $ (pesos) </span>
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
			<div id="dvBotones" class="">
				<div class="left">
			<?
			if ($soloconsulta!=1) {
				echo "<input name='b_grabar'  class='boton' type='button' value='Dejar Pendiente' onclick='activa_proceso(1);' title='Permite grabar los cambios realizados en la base de datos' />";
			}
			?>
			
			<?
			if ($soloconsulta!=1 && ($xestado_propir==1 || $xestado_propir==3)) {
				echo "<input name='b_enviar' class='boton' type='button' value='Enviar Revisión' onclick='activa_proceso(10);' title='Graba los cambios y envía la ficha a Revisión' />";
			}
			?>
			
				</div>
				<div class="right">
			<?php
			if ($xnroobservaciones>0) {
				?>
				<input name='b_observaciones' type='button' class='boton' value='Observaciones' style='width:80px;cursor:pointer' onclick='mostrar_observaciones();' title='Permite ver las observaciones'>
				<?
			}
			?>
					<!--<input name='b_cerrar' type='button' class='boton' value='Cerrar' style='width:80px;cursor:pointer' onclick='cerrar();' title='Permite cerrar la ventana actual'>-->
				</div>
			</div>
			
			<input type="hidden" name="nsector"		 				value="<? echo $xsector; ?>"/>
			<input type="hidden" name="nestado" 					value="<? echo $xestado; ?>"/>
			<input type="hidden" name="nano" 						value="<? echo $xnano ?>"/>
			<input type="hidden" name="ninstitucion"				value="<? echo $xinstitucion ?>"/>
			<input type="hidden" name="npreinversion" 				value="<? echo $xpreinversion ?>"/>
			<input type="hidden" name="nestadopropir" 				value="<? echo $xestado_propir ?>"/>
			<input type="hidden" name="naccion" 					value="0" />
			<input type="hidden" name="cod_ut" 						value="<? echo $xcod_ut; ?>"/>
			<input type="hidden" name="ancho_iframe" 				value="<?php echo $_REQUEST["ancho_iframe"]; ?>" />
			<input type="hidden" name="nom_ut" 						value="<? echo $xnom_ut; ?>"/>
			<input type="hidden" name="vector_financiamiento"		value=""/>
			<input type="hidden" name="vector_beneficiarios" 		value=""/>
			<input type="hidden" name="netapaidi" 					value="<? echo $xetapaidi; ?>"/>
			<input type="hidden" name="color1" 						value="<?php echo $_REQUEST["color1"]; ?>" />
			<input type="hidden" name="color2" 						value="<?php echo $_REQUEST["color2"]; ?>" />
			<input type="hidden" name="color3" 						value="<?php echo $_REQUEST["color3"]; ?>" />
			<input type="hidden" name="ncosto_total" 				value="0"/> 
			<input type="hidden" name="ngastado_anos_anteriores"	value="0"/>
			<input type="hidden" name="nsolicitado" 				value="0"/>
			<input type="hidden" name="nsaldo_proximos_anos" 		value="0"/>
			<input type="hidden" name="usuario" 					value="<?php echo $_REQUEST["usuario"]; ?>"/>
			<input type="hidden" name="id_usuario" 					value="<?php echo $_REQUEST["id_usuario"]; ?>"/>
			<input type="hidden" name="uscl" 						value="<?php echo $_REQUEST["uscl"]; ?>"/>
			<input type="hidden" name="contenedor_array" id="contenedor_array" value="" />
			
			<? echo $textohidden; ?>
			
			<iframe id="v_grabar" name="v_grabar" src="" frameborder="0" width="0px" height="0px"></iframe>
			
			<script>
				var totalsubfichas=document.form1.SInstrumento.length;//inicializa();
			</script>   
		</form>
		
		<?php
			@mysql_close($BaseDatos->conector);
		?>
		
		<form name="form_ayuda" method="post" action="">
			<input type="hidden" name="nayuda" value="">
		</form>
		<?php include_once('piwik/propir_editar.php'); ?>
	</body>
</html>