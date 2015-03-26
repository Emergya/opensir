<?
	$color1="#6597D2";   
	$color2="#A9C0DC";
	
	$restriccion_modulo='01010104';
	$nivel_pagina=2;
	include_once("../../include/seguridad.php");
	include_once("../../include/clases_login.php");   
	include_once("../../login/valida_usuario.php"); 
	include_once("../../include/clases_preinversion.php");
	include_once("../../include/clases_interfaces.php");
	
	$fecha_actual = date('d/m/Y');
	// RECUPERA PARAMETROS SERVIDOR
	$xnano=$_POST["nano"];
	$xsector=$_POST["nsector"];
	$xinstitucion=$_POST["ninstitucion"];
	$xestado=$_POST["nestado"]; 
	$xestadopreinversion= $_POST['nestadopreinversion'];  
	$xpreinversion=$_POST["npreinversion"];     
	$xaccion=$_POST["naccion"];     
	$soloconsulta=$_POST["nsoloconsulta"];

        $ocultaprimarykey  =  $_REQUEST["ocultaprimarykey"];

        $registros=explode("|",$ocultaprimarykey);
	if($xpreinversion!=0){
        foreach($registros as $data){
        	 $registro=explode(",",$data);
        	 $xpreinversion=$registro[0];
        	 $xinstitucion=$registro[2];
		 $xnano=$registro[3];
	         
		}	
	}



	// RECUPERA INFORMACION DE LA PREINVERSION
	if ($xaccion==2 || $xaccion==100 || $xaccion == 107 || $xaccion == 108) {
		// ESTADO INICIATIVA
		// $fuente="Fndr";
		$Biniciativa=new Preinversion($BaseDatos->conector);
		$Biniciativa->c_preinversion=$xpreinversion;
		$Biniciativa->ano=$xnano;
		// cargar Institucion para buscar arreglo 09 mayo 2014
		$Biniciativa->c_institucion=$xinstitucion;
		$dataset=$Biniciativa->cargar_iniciativa_estado();
		$Registro = mysql_fetch_row($dataset);
		$xnombreestado	=$Registro[0];
		// DATOS INICIATIVA
		$dataset=$Biniciativa->cargar_iniciativa_ficha();
		
		$Registro = mysql_fetch_row($dataset);
		
		
		$xnombre		=	$Registro[0];
		$xunidad_tecnica	=	$Registro[1];
		$xfecha_inicio		=	$Registro[2];
		$xfecha_termino		=	$Registro[3];
		$xproducto		=	$Registro[4];
		$xcodigo		=	$Registro[5];
		$ximpactos		=	$Registro[6];
		$xsectorinversion	=	$Registro[7];
		$xestado_ari      	=	$Registro[8];
		$xobservaciones    	=	$Registro[9];
		$xtexto_beneficiarios	=	$Registro[10];
		$xclasificador		=	$Registro[11];
		$xnsector		=	$Registro[12];
		$xnunidad_tecnica	=	$Registro[13];
		$xnclasificador		=	$Registro[14];
		$xvalor1=number_format($Registro[15],0,',','.');
		$xvalor2=number_format($Registro[16],0,',','.');
		$xvalor3=number_format($Registro[17],0,',','.');
		$xetapaidi=$Registro[18];
		$xnetapaidi=$Registro[19];
		$xviafinanciamiento=$Registro[20];
		$xnviafinanciamiento=$Registro[21];
		$xtipo = $Registro[22];
		$xrate = $Registro[23];
		$xdireccion = $Registro[24];
		$xjustificacion = $Registro[25];
		$xdescriptor = $Registro[26];
		$xnfuentef = $Registro[27];
		$xnombre_formulador = $Registro[28];
		$xtelefono = $Registro[29];
		$xcorreo_electronico = $Registro[30];
		if ($xfecha_inicio>0) {
			$xfecha_inicio=substr($xfecha_inicio,6,2).'/'.substr($xfecha_inicio,4,2).'/'.substr($xfecha_inicio,0,4);
		 } else {
			$xfecha_inicio='';
		 }
		 if ($xfecha_termino>0) {
			$xfecha_termino=substr($xfecha_termino,6,2).'/'.substr($xfecha_termino,4,2).'/'.substr($xfecha_termino,0,4);
		 } else {
			$xfecha_termino='';
		}

	    // NOMBRE FUENTE FINANCIAMIENTO
		$dataset=$Biniciativa->cargar_iniciativa_fuente_financiamiento();
		$Registro = mysql_fetch_row($dataset);
		
		$xfuente_financi = $Registro[0];
		
	    // NOMBRE RATE
		$dataset=$Biniciativa->cargar_nombre_rate();
		$Registro = mysql_fetch_row($dataset);
		
		$xnrate = $Registro[0];

		// NOMBRE INSTITUCION Y SECTOR
		$dataset=$Biniciativa->cargar_iniciativa_institucion_sector();
		$Registro = mysql_fetch_row($dataset);
		
		$xinstitucion       =$Registro[0];
		$xnombreinstitucion =$Registro[1];
		$xnombresector	     =$Registro[2];
		
		// TABLA UBICACION_TERRITORIAL_PREINVERSION
		$Bubicacion=new Ubicacion_Territorial_Preinversion($BaseDatos->conector);
		$Bubicacion->c_preinversion=$xpreinversion;
		$Bubicacion->ano=$xnano;        
		$Bubicacion->c_institucion=$xinstitucion;
		$dataset		 =$Bubicacion->cargar_ubicacion_territorial();
		$Registro	 = mysql_fetch_row($dataset);
		$xnivel_ut	 =$Registro[0];
		$xubicacion	 =$Registro[1];
		$xcod_ut		 =$Registro[2];
		$xnom_ut		 =$Registro[3];
		$xinfluencia =$Registro[4];
		
		// OBSERVACIONES
		$xnroobservaciones = $Biniciativa->existe_observaciones_iniciativa();
		$texto_observacion = $Biniciativa->get_observacion();
		$nombre_checklist  = $Biniciativa->get_checklist_recomendacion();
		
		//GEO
		$carga_georeferencia = $Biniciativa->carga_georeferencia();
	} else {
		// NOMBRE INSTITUCION Y SECTOR
		$Biniciativa                  = new Preinversion($BaseDatos->conector);
		$Biniciativa->c_institucion   = $xinstitucion;
		$dataset=$Biniciativa->cargar_iniciativa_institucion_sector_1();
		
		$Registro                     = mysql_fetch_row($dataset);
		
		$xnombreinstitucion   = $Registro[0];
		$xnombresector	      = $Registro[1];
		$xnombreestado	      = 'Nueva';
		$xsectorinversion     = 0;
		$xfuente	      = 0;
		$xcodigo	      = '';
		$xnombre	      = '';
		$xmonto		      = '';
		$xantecedentes	      = '';
		$xcod_ut	      = '';
		$xnom_ut	      = '';
		$xestado_ari	      = 1;
		$xunidad_tecnica      = $xinstitucion;
		$xetapaidi            = 0;
		$xnetapaidi           = "";
		$xclasificador 	      = "33.03.125";
		$xnroobservaciones    = 0;
		$xviafinanciamiento   = 0;
		$xnviafinanciamiento  = "";
		$xtipo                = "Otro";
		$xdescriptor	      = '';
		$xnfuentef	      = 0;
	}	
	// INCLUYE BENEFICIARIOS
	$vpantalla=7;
	//include("../../include/beneficiarios.php");   
	// INCLUYE INSTRUMENTOS DE PLANIFICACION A ASOCIAR A LA DEMANDA
	$vrequerimiento=7;
	include("../../include/instrumentos.php");   
	$combo = new ComboBox();
	$combo->conector=$BaseDatos->conector;
?>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.1//EN" "http://www.w3.org/TR/xhtml11/DTD/xhtml11.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
		<title>Ficha de Preinversión</title>
		<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1"/>
		
		<!-- Bootstrap CSS -->
        <link href='../../../css/bootstrap.min.css' rel='stylesheet' type='text/css' >
        <link href='../../../css/bootstrap_chileindica.css' rel='stylesheet' type='text/css' >
		
		
		
		<link rel="stylesheet" href="../../css/estilos1.css" 	type="text/css"/>
		<link rel="stylesheet" href="../../css/formularios.css" type="text/css"/>
		<!-- <link rel="stylesheet" href="../../css/formulario_preinversion.css" 	type="text/css"/> -->
		<link href="../../runtime/styles/xp/aw.css" rel="stylesheet"/>
		<link href="../../css/dhtmlgoodies_calendar.css"  rel="stylesheet"/>
		<link rel="stylesheet" href="../../../css/colorbox1/colorbox.css" />
		<link rel="stylesheet" href="../../include/clases_interfaces.css" 	type="text/css"/>
		<link rel="stylesheet" href="../../include/instrumentos.css" 	type="text/css"/>
		<link rel="stylesheet" href="../../css/administracion.css" 	type="text/css"/>
		<link rel="stylesheet" href="../../../css/pagina_ficha.css" 	type="text/css"/>
		<link rel="stylesheet" href="../../../css/pagina_ficha_preinversion.css" 	type="text/css"/>
		<link rel="stylesheet" type="text/css" href="../../($Inbox)/../../css/administracion.css">
		<link rel="stylesheet" type="text/css" href="//ajax.googleapis.com/ajax/libs/jqueryui/1.7.2/themes/smoothness/jquery-ui.css" />
		<link href="../../css/plantilla1.css" rel="stylesheet" type="text/css">
		<link href='../../../css/bootstrap-fix.css' rel='stylesheet' type='text/css' >
		
		
		
		<!--[if IE]><link href="../../../css/pagina_ficha_ie.css" rel="stylesheet" type="text/css" /><![endif]-->
		<style>
			#carga{
				position: absolute;
			}
		</style>
		<?php include '../../../jslibs/jslibs.php' ?>
		
		<script type="text/javascript" src="../../include/dhtmlgoodies_calendar_gi_paginas.js"></script>
		<script type="text/javascript" src="../../include/procesos_comunes.js"></script>
		<script type="text/javascript" src="pagina1_1_4_ficha.js"></script>
		
		<!-- Funcion que carga el Código BIP -->
        <script type="text/javascript" src="pagina1_1_4_codigo_BIP.js"></script>
		
		
		<script type="text/javascript" src="../../runtime/lib/aw.js"></script>
<?php
//		<script type="text/javascript" src="../../../js/jquery-ui.js"></script>
//		<script type="text/javascript" src="../../../js/jquery.ui.datepicker-es.js"></script>
?>
		<script type="text/javascript" src="../../../js/jquery.colorbox-min.js"></script>
		<script
	src="<?=$ROOT_FOLDER?>/jslibs/ui-bootstrap-0.11.2/ui-bootstrap-tpls-0.11.2.min.js"></script>
		<script type="text/javascript"
	src="<?=$ROOT_FOLDER?>/jslibs/ui-grid/3.0.0-rc.12/ui-grid.min.js"></script>
<link rel="stylesheet"
	href="<?=$ROOT_FOLDER?>/jslibs/ui-grid/3.0.0-rc.12/ui-grid.min.css"
	type="text/css">
		<!-- Bootstrap -->
		<!-- <link rel="stylesheet" type="text/css" href="../../../css/bootstrap.min.css">
		<script type="text/javascript" src="../../../js/bootstrap.min.js"></script> -->

<!-- SE AÑADE PARA POPUP MAPA GEOLOCALIZACION -->
<script type="text/javascript" src="../../../js/jquery.cookie.js"></script>
<!-- FIN SE AÑADE PARA POPUP MAPA GEOLOCALIZACION -->

		<script type="text/javascript" >
			// ***************************************************************************
			// Parametros fijos del sistema
			// ***************************************************************************
			var soloconsulta=<? echo $soloconsulta; ?>;
			var xaccion=<?php echo $xaccion ?>;
			var ancho=0;
			var alto=0;
			
			jQuery(document).ready(function($) {
				$.datepicker.regional[ "es" ];
				$( ".datepicker" ).datepicker({
					dateFormat: 	"dd/mm/yy" ,
					changeMonth: 	true,
					changeYear:	true
				});
				// Para la geolocalización en mapa
        		//$("#linkMmap").colorbox({iframe:true,width:"100%",height:"100%",preloading:true,title:false});
        		// Fin para la geolocalización en mapa
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
	<body>
		<?php
		   $interface= new I_Mensaje();
		   echo $interface->crear_interface();


		   // print_r($_REQUEST);
		?>
		<script type="text/javascript">
			$(document).ready(function(){
				$('#cb_via_financiamiento').css('width', '350px');
				$('#cb_via_financiamiento').css('padding-left', '5px');
				$('#cb_via_financiamiento').css('margin-left', '5px');
				//document.form1.coordenadas.value="<? echo $carga_georeferencia; ?>";
			});
		</script>
		<form name="form1" method="post" action="" enctype="multipart/form-data">
			<div id="dvHead">
				<div id="dvHeadContenido">
					<div id="dvHeadLeft">
						<span class="tit1">FICHA DE POSTULACIÓN PREINVERSION</span><br/>
						<span class="tit2">Preinversión - Ficha</span>
					</div>
					<div id="dvHeadRight">
						<div id="dvHeadRightTop">
							<span><? echo $xnombreinstitucion; ?> > Año: <? echo $xnano; ?> > Estado: <? echo $xnombreestado; ?></span><br/>
						</div>
						<div id="dvHeadRightBottom">
							<span><a href="#" onclick="cerrar();">Cerrar</a> | <a href="#">Ayuda</a></span>
						</div>
					</div>
				</div>
			</div>
				<div id="hoja" name="hoja" style="overflow:auto;height:802px">
			<div id="dvContenedor">
<div id="dvDatosIniciativa" class="dvContDatosContenido">
			<table class="dvContFiltro" width="100%">
				<tr><td class="dvFiltro1"><span class="resaltar">(*) </span><span>Nombre Iniciativa</span></td><td class="dvFiltro2" width="170px"><span class="resaltar">(*) </span><span>Sector</span>
 </td></tr>
				<tr><td class="dvFiltro1">
<input id="e_nombre" name="e_nombre" type="text" value="<? echo $xnombre; ?>" maxlength="150"/>
					</td><td class="dvFiltro2" width="170px">
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
</td></tr>
			</table>
</div>

				<div class="dvContDatosTitulo">ANTECEDENTES GENERALES</div>
				<div id="dvAntecedentesGenerales" class="dvContDatosContenido">
					<table>
						<tr>
							<td><span class="resaltar">(*) </span> Código</td>
							<td>Tipo</td>
							<td>Unidad Técnica</td>
							<td>Dirección</td>
						</tr>
						<tr>
							<td>
								<input id="e_codigo" name="e_codigo" type="text" maxlength="20" onKeyUp="valida_bip();" value="<?php echo $xcodigo; ?>" />
							</td>
							<td>
								<input id="e_tipo" name="e_tipo" type="text" maxlength="20" value="<?php echo $xtipo; ?>" readonly="readonly" />
							</td> 
							<td>
								<?
								if ($soloconsulta==0) {
									$combo->indice_query=83;
          					        $combo->valor=$xunidad_tecnica;
          					        $combo->nombre="e_unidad_tecnica";
          					        $combo->indice_cero=0;
          					        $combo->accion="";
          					        $combo->ancho="200";
          					        $muestracombo=$combo->crearcombo();
          					        echo '<script> document.write("'.$muestracombo.'");</script>';
          						} else {
									echo "<input name='e_unidad_tecnica' type='text' readonly='readonly' id='e_unidad_tecnica' value='".$xnunidad_tecnica."'/>";
          						}	
					            ?>
							</td>
							<td colspan="2">
								<input id="direccion" name="direccion" style="" type="text" maxlength="256" value="<?php echo $xdireccion; ?>"/>
							</td> 
						</tr>
						<tr>
							<td><span class="resaltar">(*) </span>Fecha Inicio</td>
							<td><span class="resaltar">(*) </span>Fecha Término</td>
							<td>Etapa</td>
							<td>Rate</td>
							<td><span class="resaltar">(*) </span>Vía de Financiamiento</td>
						</tr>
						<tr>
							<td>
								<input name="fecha_inicio" type="text" <?php echo ($soloconsulta == 0) ? "class='datepicker' " :  "readonly='readonly'";  ?>  value="<?php echo $xfecha_inicio; ?>" />
							</td>
							<td>
								<input name="fecha_termino" type="text" <?php echo ($soloconsulta == 0) ? "class='datepicker' " :  "readonly='readonly'";  ?> value="<?php echo $xfecha_termino; ?>" />
							</td>
							<td>
								<?php
								if($soloconsulta==0) {
									$combo->indice_query=18;
									$combo->valor=$xetapaidi;
									$combo->nombre="cb_etapa";
									$combo->indice_cero=0;
									$combo->accion="valida_bip()";
									$combo->ancho="100";
									$muestracombo=$combo->crearcombo();
									echo '<script> document.write("'.$muestracombo.'");</script>';
								} else {
									echo "<input name='cb_etapa' type='text' readonly='readonly' id='cb_etapa' value='".$xnetapaidi."'/>";
								}	
								?>
							</td>
							<td>
								<input id="cb_rate" name="cb_rate" type="text" maxlength="20" value="<?php echo $xnrate; ?>" readonly="readonly"/>
							</td>
							<td colspan="2">
								<!-- Via FInanciamiento -->
								<!-- <label for="">Via Financiamiento</label> -->
								<?php
									if ($soloconsulta==0) {
										$combo->indice_query=49;
										$combo->valor=$xviafinanciamiento;
										$combo->nombre="cb_via_financiamiento";
										//$combo->id="cb_via_financiamiento";
										$combo->indice_cero=9;
										$combo->accion="";
										$combo->ancho="100";
										$combo->comodin=$xnano;
										$muestracombo=$combo->crearcombo_comodin();
										echo '<script> document.write("'.$muestracombo.'");</script>';
									} else {
										echo "<input id='cb_via_financiamiento_vista' name='cb_via_financiamiento_vista' type='text' readonly='readonly' value='".$xnviafinanciamiento."'/>";
										echo "<input id='cb_via_financiamiento' name='cb_via_financiamiento' type='hidden' readonly='readonly' value='".$xviafinanciamiento."'/>";
									}
								?>
							</td>
						</tr>
						<tr>
							<td colspan="2"><span class="resaltar">(*) </span>Cuantificación de las unidades físicas</td>
							<td colspan="2"><span class="resaltar">(*) </span>Descripción de la iniciativa de Inversión</td>
							<td colspan="2">Justificación</td>
						</tr>
						<tr>
							<td colspan="2">
								<textarea name='e_producto' rows="5" cols="5"><? echo $xproducto; ?></textarea>
                           </td>
							<td colspan="2">
								<textarea id="Observaciones" name="Observaciones" rows="5" cols="5"><? echo $xobservaciones;?></textarea>
							</td>
							<td colspan="2">
								<textarea id="justificacion_proyecto" name="justificacion_proyecto" rows="5" cols="5"><? echo $xjustificacion;?></textarea>
							</td>
						</tr>
						<tr>
						  <td colspan="2">Descriptor</td>
						  <td colspan="2">Fuente de Financiamiento </td>
						  <td colspan="2">&nbsp;</td>
					  </tr>
						<tr>
						  <td colspan="2"><textarea id="descriptor" name="descriptor" rows="5" cols="5"><? echo $xdescriptor;?></textarea></td>
						  <td colspan="2" valign="top"><?
						if ($soloconsulta==0) {
							$combo->indice_query=64;							
							$combo->valor=$xnfuentef;
							$combo->nombre="cb_fuentef";
							$combo->indice_cero="";
							$combo->accion="";
							$combo->ancho="170";
							$muestracombo=$combo->crearcombo();
							echo '<script>document.write("'.$muestracombo.'");</script>';
						}else{
							echo "<input name='cb_fuentef' type='text' readonly='readonly' id='cb_fuentef' value='".$xfuente_financi."'/>";
						}
						?></td>
						  <td colspan="2">&nbsp;</td>
					  </tr>
					  <tr><td>&nbsp;</td></tr>
					  <tr> 
					      <td BGCOLOR="#002C56"><font size=2 color="WHITE"><I>Datos del Formulador</I></font></td>
					  </tr>  
					  <tr>
					     <td colspan="2">Nombre</td>
					     <td colspan="1">Telefono</td>
					     <td colspan="2">Correo Electronico</td>
					  </tr>
					  <tr>
					      <td colspan="2">
								<input id="nombre_formulador" name="nombre_formulador" type="text" maxlength="150" value="<?php echo $xnombre_formulador; ?>"/>
							</td>
							<td colspan="1">
								<input id="telefono" name="telefono" style="" type="text" maxlength="15" value="<?php echo $xtelefono; ?>"/>
							</td> 
							<td colspan="2">
								<input id="correo_electronico" name="correo_electronico" style="" type="text" maxlength="100" value="<?php echo $xcorreo_electronico; ?>"/>
							</td> 
					 </tr>		
					</table>
				</div>
				<div class="dvContDatosTitulo">MONTOS Y FUENTES($)</div>
				<div id="dvMontosFuentes" class="dvContDatosContenido">
					<div class="divs">
						<div>
							<span class="resaltar">(*) </span><span>Solicitado Año</span></br>
							<input name="e_solicitado" id="e_solicitado" type="text"  value="<?php echo $xvalor2; ?>" maxlength="12" onChange="calcular_saldo();"/>
						</div>
						<div>
							<span>Saldo Próximos Años</span></br>
							<input name="e_saldo" id="e_saldo" type="text" value="<?php echo $xvalor3; ?>" maxlength="12" readonly="readonly"/>
						</div>
						<div>
							<span class="resaltar">(*) </span><span>Costo Total</span></br>
							<input name="e_costo_total" id="e_costo_total" type="text" value="<?php echo $xvalor1; ?>" maxlength="12" onChange="calcular_saldo();"/>
						</div>
<!-- 						<div>
							<span>Gastado Años Anteriores</span></br>
							<input name="e_anterior" id="e_anterior" type="text" value="" maxlength="12" readonly="readonly"/>
						</div> -->
					
						<div>
							<h2 id="mensaje_montos"></h2>
						</div>
					</div>
					<hr />
					<h2>Detalle de montos</h2>
					<div id="detalle_via_financiamiento"></div>
					<hr />
					<span>IMPORTANTE: Los montos a ingresar deben ser en $ (pesos)</span>
				</div>
				<div class="dvContDatosTitulo">BENEFICIARIOS</div>
				<div id="dvBeneficiarios" class="dvContDatosContenido">
				
					<?php include("../../include/beneficiarios_ng.php"); ?>
					<?php //echo $ventana_beneficiarios; ?>
				</div>
				<div class="dvContDatosTitulo">UBICACIÓN GEOGRÁFICA</div>
				<div id="dvUbicacionGeo" class="dvContDatosContenido">
 					<?
						$interface= new I_Ubicacion_Territorial();
						echo $interface->crear_interface($xnivel_ut,$soloconsulta,$xubicacion,$xinfluencia);
					?>
				</div>
				<div class="dvContDatosTitulo">RELACIÓN CON INSTRUMENTOS Y POLÍTICAS</div>
				<div id="dvRelacionInstryPoli" class="dvContDatosContenido">
					<? echo $ventanainstrumento;?>
				</div>
				<div class="dvContDatosTitulo">DOCUMENTACIÓN ANEXA</div>
				<div id="dvDocuAnexa" class="dvContDatosContenido">

				<?php if ( $xaccion == 1) { ?>

					<h2><span class="resaltar">(*) </span><a href="#" class="boton" id="addfile">Agregar otro archivo</a></h2>
						<div id="attachment">
							<p>
								<label for="filename"><input type="text" id="filenames" size="20" name="filename[1]" value="" placeholder="Nombre archivo" /></label>
								<label for="file"><input type="file" id="files" name="file[1]" class="file" placeholder="Archivo" /></label>
							</p>
						</div>

					<script type="text/javascript">
					$(document).ready(function(){
						var scntDiv = $('#attachment');
						var idf = $('#attachment p').size() + 1;
				        $(document).on('click','#addfile', function() {
				        	$('<p><label for="filename"><input type="text" id="filenames" size="20" name="filename[' + idf +']" class="file" value="" placeholder="Nombre archivo" /></label><label for="file"><input type="file" id="files" name="file[' + idf + ']" placeholder="Archivo" /></label> <a href="#" class="remove boton">Eliminar</a></p>').appendTo(scntDiv);
				        	idf++;
				        	return false;
				        });
				        $(document).on('click','.remove', function() {
				        	if( idf > 2 ) {
				        		$(this).parents('p').remove();
				        		idf--;
				        	}else{

				        	}
				        	return false;
				        });

					});
					</script>
				<? } ?>
				<?php
				if ($soloconsulta==0 && $xpreinversion != 0 && $xaccion != 100) {
				?>
					<input type="button" class="btn_blanco" value="Nuevo Documento" style="float: right;margin-bottom: 10px;" onClick="abrirventana2('pagina1_1_4_ficha_subir_archivo.php?<?php echo 'nano='.$xnano.'&npreinversion='.$xpreinversion.'&ninstitucion='.$xinstitucion ?>','',600,220);" />
				<?php 
				}
				?>
				<?php if ($xpreinversion != "0"){ ?>
					<iframe id="gdocumentacion" name="gdocumentacion" src="" frameborder="0" width="100%" height="115px"  scrolling="auto"></iframe>
				<?php } ?>
				</div>
			</div>
			</div>
			<div id="dvBotones" class="acciones">
			<?php if ( $xaccion == 100) { ?>
				<input name='b_observar' id='b_observar' type='button'  class='boton' value='Observar' onclick='activa_proceso(20);' title='Observar Iniciativa'/>
				<input type='button' class='boton' name='btn_recepcionar' id='btn_recepcionar' value='Recepcionar' onClick='activa_proceso(28)' />
			 <!-- <input name='b_preadmisible' id='b_preadmisible' type='button' class='boton' value='Dejar Preadmisible' onclick='activa_proceso(22);' title='Dejar Preadmisible'/> -->
				
			<?php if ($xnroobservaciones>0) { ?>
				<input type="button" class="boton right" value="Observaciones" onClick="mostrar_observaciones();"/>
				<!-- <input name='b_observaciones' id='b_observaciones' type='button' class='boton' value='Observaciones' onClick='mostrar_observaciones();' title='Ver Observaciones historicas de la iniciativa'/> -->
			<?php } ?>
			<?php }else if ( $xaccion == 107 || $xaccion == 108 ) { ?>
			
			<!-- Evaluar Tecnicamente -->
			<?php if (tiene_modulo($vector_funciones,'0101010410',$nrofunciones)==1){ ?>
			
			<!-- Encargado del proceso -->
			<input name='b_observar_tec' id='b_observar_tec' type='button' class='boton' value='Observar' onclick='MM_showHideLayers("Lobservacion_2","","show");' title='Observar Iniciativa'/>
			<input name='b_autorizar_tec' id='b_autorizar_tec' type='button' class='boton' value='Autorizar Tec.' onclick='activa_proceso(26);' title='Autoriza Tecnicamente la Iniciativa'/>
			<?php }else{ ?>
			
			<!-- Evaluador -->
				<input name='b_observar_tec' id='b_observar_tec' type='button' class='boton' value='Observar' onclick='activa_proceso(24);' title='Observar Iniciativa'/>
				<input name='b_recomendar' id='b_recomendar' type='button' class='boton' value='Rec. Socialmente' onclick='activa_proceso(25);' title='Recomienda Socialmente'/>
			<?php } ?>
			<?php if (tiene_modulo($vector_funciones,'0101010410',$nrofunciones)==1){ ?>
				<?php	if ($nombre_checklist != "0") { ?>
						<input name='b_checklist' id='b_checklist' type='button' class='boton' value='Checklist' onclick='mostrar_documento(".$nombre_checklist.");' title='Ver Checklist por parte del Evaluador'/>
				<?php	} ?>
			<?php	} ?>
			<?php	if ($xnroobservaciones>0){ ?>
					<input name='b_observaciones' id='b_observaciones' type='button' class='boton' value='Observaciones' onClick='mostrar_observaciones();' title='Ver Observaciones historicas de la iniciativa'/>
			<?php	} ?>
			<?php }else{ ?> 
				<!-- Formulador trabaja en las observaciones -->
				<?php if ($soloconsulta!=1 && ($xestado_ari==1 || $xestado_ari==4)) { ?>
					<input name='b_pendiente' type='button' class='boton' value='Dejar Pendiente' onclick='activa_proceso(1);' title='Graba los cambios y deja la ficha en estado pendiente'/>
				<?php } ?>
				<?php if ($soloconsulta!=1 && $xestado_ari==4) { ?>
					<input name='b_enviar_gore' id='b_enviar_gore' type='button' class='boton' value='Enviar Nuevamente al GORE'  onclick='activa_proceso(15);' title='Graba los cambios y envía la ficha al GORE'/>
					<?php } ?> 
				
				<!-- Observacion enviada por ET GORE -->
				<?php if ($soloconsulta!=1 && ($xestado_ari==14 || $xestado_ari==15 || $xestado_ari==25)) { ?>
					<input name='b_pendiente_2' type='button' class='boton' value='Dejar Pendiente' onclick='activa_proceso(1);' title='Graba los cambios y deja la ficha en estado pendiente'/>
					<input name='b_enviar_gore_2' id='b_enviar_gore_2' type='button' class='boton' value='Enviar Nuevamente a ET GORE' onclick="mostrar_ventana_observacion('LRespuestaObservacion', 500, 350);" title='Graba los cambios y envía la ficha nuevamente a Evaluación (GORE)' />
				 <?php } ?>
				<?php if ($soloconsulta!=1 && $xestado_ari==1) { ?>
					<input name='b_enviar' id='b_enviar' type='button' class='boton' value='Postular Iniciativa' onclick='activa_proceso(10);' title='Graba los cambios y postula la iniciativa'/>
				<?php } ?>
				<?php if ($soloconsulta!=1 && $xestado_ari==1) { ?>
					<input name='b_imprimir' type='button' class='boton' value='Imprimir Ficha' onclick='activa_proceso(100);' title='Imprime los datos de la ficha actual'/>
				<?php } ?>
				<?php if ($xnroobservaciones>0) { ?>
					<!-- <img alt='observaciones' src='../../imagenes/observaciones.gif' width='80px' height='20px' onClick='mostrar_observaciones();'/> -->
					<input name='b_observaciones' id='b_observaciones' type='button' class='boton' value='Observaciones' onClick='mostrar_observaciones();' title='Ver Observaciones historicas de la iniciativa'/>
				<?php } ?>
			<?php } ?>
			<?php if ($soloconsulta!=1 && $xestado_ari==1 && $xaccion==2){ ?>
				<input name='b_eliminar' type='button' class='boton' value='Eliminar' onclick='activa_proceso(27);' title='Elimina la iniciativa'/>
			<?php } ?>
			</div>
			
			<!-- Capa para el ingreso de la recomendacion tecnica -->
			<div id="Lrecomendacion" class="dvModal">
				<span>RECOMENDAR LA INICIATIVA</span><br/><br/>
				<span>Fecha</span><br/>
				<input name="e_fecha_recomendacion" type="text" id="e_fecha_recomendacion" value="<?php echo $fecha_actual ?>" maxlength="10" readonly="readonly" /><br/><br/>
                <span>Observaciones a la recomendación</span><br/>
				<textarea name="e_recomendacion" id="e_recomendacion" rows="5" cols="5"></textarea><br/><br/>
				<span>Adjuntar Documento (Checklist)</span><br/>
				<input type="file" name="archivo_checklist" id="archivo_checklist"/><br/><br/>
				<input name="b_aceptar22" type="button" class='inputButton' id="b_aceptar2" value="Aceptar" onClick="grabar_recomendacion();"/><br/><br/>
				<input name="b_cancelar22" type="button" class='inputButton' id="b_cancelar22" value="Cancelar" onClick="MM_showHideLayers('Lrecomendacion','','hide');"/>
			</div>
			
			<!-- Capa de ingreso de Observaciones -->
			<div id="Lobservacion" class="dvModal">
				<span>OBSERVAR INICIATIVA</span><br/><br/>
				<span>Fecha</span><br/>
				<input name="e_fecha_observacion" type="text" id="e_fecha_observacion" value="<?php echo $fecha_actual ?>" maxlength="10" readonly="readonly" /><br/><br/>
                <span>Seleccione Tipo de Observación</span><br/>
				
				<input type="radio" class="inputRadio" value="15" name="tipo_observacion" id="tipo_observacion_FI">
				<label for="tipo_observacion_FI">(FI) Falta Información</label><br/>
				
				<input type="radio" class="inputRadio" value="14" name="tipo_observacion" id="tipo_observacion_OT">
				<label for="tipo_observacion_OT">(OT) Otras razones Técnicas</label><br/><br/>
				
				<input type="radio" class="inputRadio" value="25" name="tipo_observacion" id="tipo_observacion_IN" />
				<label for="tipo_observacion_IN">(IN) Incumplimiento de Normativa</label>
				<span>Adjuntar Documento</span><br/>
				<input type="file" name="adjunto" id="adjunto"/><br/><br/>
				<span>Observación</span><br/>
				
				<textarea name="e_observacion_tec" id="e_observacion_tec" rows="5" cols="5"></textarea><br/><br/>
				<input name="b_aceptar2" type="button" class='inputButton' id="b_aceptar3" value="Aceptar" onClick="grabar_nueva_observacion();"/><br/><br/>
				<input name="b_cancelar2" type="button" class='inputButton' id="b_cancelar2" value="Cancelar" onClick="MM_showHideLayers('Lobservacion','','hide');"/>
			</div>
			
			<!-- Capa de ingreso de Observaciones por parte del encargado al evaluador -->
			<div id="Lobservacion_2" class="dvModal">
				<span>OBSERVAR INICIATIVA</span><br/><br/>
				<span>Fecha</span><br/>
				<input name="e_fecha_observacion_2" type="text" id="e_fecha_observacion" value="<?php echo $fecha_actual ?>" maxlength="10" readonly="readonly" /><br/><br/>
                <span>Observación</span><br/>
				<textarea name="e_observacion_tec_2" id="e_observacion_tec_2" rows="5" cols="5"></textarea><br/>
				<span>Al Observar la iniciativa, quedara en estado "Reevaluación" y se devolvera al Evaluador para que trabaje en ella</span><br/><br/>
				<input name="b_aceptar2" type="button" class='inputButton' id="b_aceptar3" value="Aceptar" onClick="grabar_nueva_observacion_2();"/><br/><br/>
				<input name="b_cancelar2" type="button" class='inputButton' id="b_cancelar2" value="Cancelar" onClick="MM_showHideLayers('Lobservacion_2','','hide');"/>
			</div>
			
			<!-- Capa para el ingreso de respuestas por parte del municipio -->
			<div id="LRespuestaObservacion" class="capa_mensajes">
				
				<div class="row">
					<h3>Observación</h3>
					<textarea name="obs_observacion" id="obs_observacion" readonly="readonly" rows="3" cols="5"><?php echo $texto_observacion[0]; ?></textarea><br/><br/>
				</div>
				
				<div class="row">
					<h3>Respuesta</h3>
					<textarea name="obs_respuesta" id="obs_respuesta" rows="3" cols="5"></textarea>
				</div>

				<div class="row">
					<h3>Opcionalmente, puede adjuntar un documento</h3>
					<input type="file" name="obs_adjunto" id="obs_adjunto"/><br/><br/>
				</div>
				
				<div class="row">
					<input style="width: auto !important" name="b_aceptar_observacion" type="button" class='boton' id="b_aceptar_observacion" value="Aceptar y Enviar" onClick="activa_proceso(15);"/>
					<input style="width: auto !important" name="b_cancelar_observacion" type="button" class='boton' id="b_cancelar_observacion" value="Cancelar" onClick="ocultar_ventana_observacion('LRespuestaObservacion');" />
				</div>
			</div>

					<!-- Nuevo Modal para el ingreso de codigo BIP -->
                        <div class="modal fade bs-modal-sm" id="ingreso_codigo_bip" tabindex="-1" role="dialog" aria-labelledby="ingreso_codigo_bip_label" aria-hidden="true">
                          <div class="modal-dialog modal-sm">
                            <div class="modal-content" style="width: 400px";>
                              <div class="modal-header">
                                <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
                                <h4 class="modal-title" id="ingreso_codigo_bip_label">Ingreso de Código BIP</h4>
                              </div>
                              <div class="modal-body">
                                <p>Ingrese Código BIP, Etapa y el año de la Iniciativa a Crear</p>

                                <table>
                                        <tr>
                                                <td width="100px">Código BIP</td>
                                                <td><input type="text" maxlength="12" name="e_codigo_bip" id="e_codigo_bip" class=""></td>
                                                <td>-</td>
                                                <td><input size="2" maxlength="1" value="0" name="e_dv_codigo_bip" id="e_dv_codigo_bip" type="text" class=""></td>
                                        </tr>
                                        <tr>
                                                <td>Etapa</td>
                                                <td colspan="3">
                                                        <select name="etapa_iniciativa" id="etapa_iniciativa" style="width='150px'">
                                                              <option value="3">Prefactibilidad</option>
                                                              <option value="4">Factibilidad</option>
                                                              <option value="5">Dise&ntilde;o</option>
                                                              <option value="6">Ejecuci&oacute;n</option>
                                                            </select>
                                                </td>
                                        </tr>
                                        <tr>
                                                <td>Año</td>
                                                <td colspan="3">
                                                        <?php $bip_anio_actual = date('Y'); ?>
                                        <select name="bip_anio" id="bip_anio">
                                                <option value="<?php echo $bip_anio_actual; ?>"><?php echo $bip_anio_actual; ?></option>
                                                <option value="<?php echo $bip_anio_actual + 1; ?>"><?php echo $bip_anio_actual + 1; ?></option>
                                        </select>
                                                </td>
                                        </tr>
                                </table>

                              </div>
                              <div class="modal-footer">
                                <button type="button" class="btn btn-primary" id="cargar_codigo_bip" name="cargar_codigo_bip">Aceptar</button>
                                <button type="button" class="btn btn-default" data-dismiss="modal">Cancelar</button>
                              </div>
                            </div>
                          </div>
                        </div>
			
			
			
			<!-- <textarea rows="9" cols="9" name="array_coordenadas" value=""></textarea> -->
			<input type="hidden" name="existe_codigo" id="existe_codigo" value="0">
			<input type="hidden" name="e_codigo_dv" id="e_codigo_dv" value="">
			<input type="hidden" name="id_observacion" id="id_observacion" value="<?php echo $texto_observacion[1]?>">
			<input type="hidden" name="nsector" id="nsector" value="<? echo $xsector; ?>">
			<input type="hidden" name="nestado" id="nestado" value="<? echo $xestado; ?>">
			<input type="hidden" name="nestadopreinversion" id="nestadopreinversion" value="<?php echo $xestadopreinversion ?>">
			<input type="hidden" name="nano" id="nano" value="<? echo $xnano ?>">
			<input type="hidden" name="ninstitucion" id="ninstitucion" value="<? echo $xinstitucion ?>">
			<input type="hidden" name="npreinversion" id="npreinversion" value="<? echo $xpreinversion ?>">
			<input type="hidden" name="nestadoari" id="nestadoari" value="<? echo $xestado_ari; ?>">
			<input type="hidden" name="nestado_actual" id="nestado_actual" value="<? echo $xestado_ari; ?>">
			<input type="hidden" name="naccion" id="naccion" value="0">
			<input type="hidden" name="cod_ut" id="cod_ut" value="<? echo $xcod_ut; ?>">
			<input type="hidden" name="nom_ut" id="nom_ut" value="<? echo $xnom_ut; ?>">
			<input type="hidden" name="vector_beneficiarios" id="vector_beneficiarios" value="">
			<input type="hidden" name="color1" id="color1" value="<?php echo $_REQUEST["color1"]; ?>">
			<input type="hidden" name="color2" id="color2" value="<?php echo $_REQUEST["color2"]; ?>">
			<input type="hidden" name="color3" id="color3" value="<?php echo $_REQUEST["color3"]; ?>">
			<input type="hidden" name="usuario" id="usuario" value="<?php echo $_REQUEST["usuario"]; ?>">
			<input type="hidden" name="id_usuario" id="id_usuario" value="<?php echo $_REQUEST["id_usuario"]; ?>">
			<input type="hidden" name="uscl" id="uscl" value="<?php echo $_REQUEST["uscl"]; ?>">
			<input type="hidden" name="cdocumento" id="cdocumento" value="0">
			<input type="hidden" name="ndocumento" id="ndocumento" value="">
			<input type="hidden" name="contenedor_array" id="contenedor_array" value="" />


			<input type="hidden" name="c_documento">
			<input type="hidden" name="c_archivo">
			
			<iframe id="i_grabar" name="i_grabar" src="" frameborder="0" width="0px" height="0px"></iframe>



			<? echo $textohidden; ?>
			<script type="text/javascript">
				var totalsubfichas=document.form1.SInstrumento.length;
				inicializa();
			</script>
			<iframe id="v_grabar" name="v_grabar" src="" frameborder="0" width="0px" height="0px"></iframe>
		</form>
<?php
   @mysql_close($BaseDatos->conector);
?>
		<form name="form_ayuda" method="post" action="">
			<input type="hidden" name="nayuda" value="" />
		</form>
	</body>
</html>
