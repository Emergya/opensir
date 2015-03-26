<?php
   $nivel_pagina=2;
   $restriccion_modulo='01010201';
   include_once("../../include/seguridad.php");
   include_once("../../include/clases_inversiones.php");
   include_once("../../include/clases_interfaces_inversion.php");
   include_once("../../login/valida_usuario.php");
   if (tiene_modulo($vector_funciones,'0101020113',$nrofunciones)==1) {$corresponde=1;} else {$corresponde=0;}

// RECUPERA PARAMETROS SERVIDOR
   $xnano=$_REQUEST["nano"];
   $xninstitucion=$_REQUEST["ninstitucion"];
   $xnpreinversion=$_REQUEST["npreinversion"];
   $xnficha=$_REQUEST["nficha"];
   $soloconsulta=$_REQUEST["nsoloconsulta"];
   $oopcion=2;

   // RECUPERA INFORMACION DE LA INVERSION
   $Binversion=new Inversion($BaseDatos->conector);
   $Binversion->ano=$xnano;
   $Binversion->c_institucion=$_REQUEST["ninstitucion"];
   $Binversion->c_preinversion=$_REQUEST["npreinversion"];
   $Binversion->c_ficha=$_REQUEST["nficha"];
   $xtexto_beneficiarios=$Binversion->cargar_texto_beneficiarios();
   // TABLA UBICACION_TERRITORIAL_INICIATIVA
   $Bubicacion=new Ubicacion_Territorial_Iniciativa($BaseDatos->conector);
   $Bubicacion->ano=$xnano;
   $Bubicacion->c_institucion=$_REQUEST["ninstitucion"];
   $Bubicacion->c_iniciativa=$_REQUEST["npreinversion"];
   $Bubicacion->c_ficha=$_REQUEST["nficha"];
   $dataset=$Bubicacion->cargar_ubicacion_territorial();
   $Registro = mysql_fetch_row($dataset);
   $xnivel_ut	=$Registro[0];
   $xubicacion	=$Registro[1];
   $xcod_ut	=$Registro[2];
   $xnom_ut	=$Registro[3];
   $xinfluencia    =$Registro[4];

   /**
    * Carga georeferencia
    */

    $coordenadas = $Binversion->carga_georeferencia();
    

// INCLUYE BENEFICIARIOS
   $vpantalla=3;
   include("../../include/beneficiarios.php");

// INCLUYE INSTRUMENTOS DE PLANIFICACION A ASOCIAR A LA INICIATIVA
   $vrequerimiento=6;
   include("../../include/instrumentos.php");
   $combo = new ComboBox();
   $combo->conector=$BaseDatos->conector;

?>
<script language="JavaScript">
<!--

/****************************
PARAMETROS FIJOS POR SISTEMA
*****************************/
  var oopcion=<?php echo $oopcion; ?>;
  var soloconsulta=<?php echo $_REQUEST["nsoloconsulta"]; ?>;
  var ancho=0;
  var alto=0;

//-->
</script>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
	<title></title>
	<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1"/>
	<script type="text/javascript" src="pagina1_2_1_ficha_hoja7.js?<?=rand(0,999999)?>"></script>
	<link rel="stylesheet" type="text/css" href="../../css/estilos1.css"/>
	<link rel="stylesheet" type="text/css" href="../../css/formularios.css"/>
	<link rel="stylesheet" type="text/css" href="../../../css/pagina_ficha.css"/>
	<link rel="stylesheet" type="text/css" href="../../../css/administracion.css"/>
	<link rel="stylesheet" type="text/css" href="../../include/clases_interfaces.css"/>
	<link rel="stylesheet" type="text/css" href="../../include/instrumentos.css"/>
	<script type="text/javascript" src="../../include/procesos_comunes.js"></script>
  <script language="JavaScript" src="../../../js/jquery-1.8.1.min.js"></script>
	<script type="text/javascript" src="../../runtime/lib/aw.js"></script>
	<link rel="stylesheet" type="text/css" href="../../runtime/styles/xp/aw.css" />
	<link rel="stylesheet" type="text/css" href="../../css/dhtmlgoodies_calendar.css" />
	<script type="text/javascript" src="../../include/dhtmlgoodies_calendar_gi_paginas.js"></script>
	<!-- SE AÑADE PARA POPUP MAPA GEOLOCALIZACION -->
	<script type="text/javascript" src="../../../js/jquery.cookie.js"></script>
	<script type="text/javascript" src="../../../js/jquery.colorbox-min.js"></script>
	<link rel="stylesheet" href="../../../css/colorbox1/colorbox.css">
	<script language="JavaScript">
	 $(document).ready(function(){        
        /* Se modifica para al geo referenciacion */
        document.form1.coordenadas.value='<? echo $coordenadas[0]; ?>';
        document.form1.contenedor_array.value='<? echo $coordenadas[1]; ?>';        
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
	<!-- FIN SE AÑADE PARA POPUP MAPA GEOLOCALIZACION -->

<!--[if IE]>
	<link rel="stylesheet" type="text/css" href="../../../css/pagina_ficha_ie.css" />
<![endif]-->

<!--[if IE 9]>
  <style>
    .dvContDatosTitulo{
      width: 100%;
    }
    .dvContDatosContenido{
    width: 100%;
    }

    #beneficiarios-box-focus{
      display:none;
    }
  </style>
<![endif]-->

</head>

<body leftmargin="0" topmargin="0" marginwidth="0" marginheight="0" onLoad="inicializa();" onResize="define_tamano_ventana_hoja();">
	<form name="form1" method="post" action=""  style="margin-bottom: 0px;">
		<div id="ILayer" name="ILayer" style="width:100%; hhhheight:100%; text-align:center;">
			<table id="TLayer1">
				<tr>
					<td align="left" width="50%">
						<div class="dvContDatosTitulo">BENEFICIARIOS</div>
						<div id="dvBeneficiarios" class="dvContDatosContenido">
						<?php echo $ventana_beneficiarios; ?>
						</div>
						<div class="dvContDatosTitulo">UBICACIÓN GEOGRÁFICA</div>
						<div id="dvUbicacionGeo" class="dvContDatosContenido">
						<?php
							if ($_REQUEST["nsoloconsulta"]==1 || $corresponde==0) {$condicion=1;} else {$condicion=0;}
							$interface= new I_Ubicacion_Territorial();
							echo $interface->crear_interface($xnivel_ut,$condicion,$xubicacion,$xinfluencia);
						?>
						</div>
						<div class="dvContDatosTitulo">RELACIÓN CON INSTRUMENTOS Y POLÍTICAS</div>
						<div id="dvRelacionInstryPoli" class="dvContDatosContenido">
							<? echo $ventanainstrumento;?>
						</div>
					</td>
				</tr>
			</table>
		</div>

		<table width="100%" border="0" cellspacing="0" cellpadding="0">
			<?php if ($_REQUEST["nsoloconsulta"]==0 && $corresponde==1) { ?>
			<tr>
				<td>
					<table width="100%" border="0" cellpadding="0" cellspacing="0">
						<tr><td><img src="../../imagenes/spacer.gif" width="1" height="4"></td></tr>
						<tr>
							<td>
								<table width="100%" border="0" cellpadding="0" cellspacing="0">
									<tr>
										<td><img src="../../imagenes/spacer.gif" width="30" height="10"></td>
										<td width="10%">&nbsp; </td>
										<td width="75%">&nbsp; </td>
										<td width="15%" align="right">
											<div align='center'>
												<input name='b_grabar' type='button' class='boton' value='Grabar Cambios' style='width:120px;cursor:pointer' onclick='grabar();'>
											</div>
										</td>
										<td><img src="../../imagenes/spacer.gif" width="10" height="10"></td>
										<td><input name='b_ayuda' type='button' class='boton' value='?' style='width:20px;cursor:pointer' onclick='abrir_ayuda("Otros_Antecedentes_Ficha_Seguimiento.htm");' title='Obtener Ayuda'></td>
										<td><img src="../imagenes/spacer.gif" width="5" height="5"></td>
									</tr>
								</table>
							</td>
						</tr>
					</table>
				</td>
			</tr>
			<?php } ?>
		</table>
		<iframe id="igrabar" name="igrabar" src="" frameborder="0" width="0px" height="0px"></iframe>
		<input type="hidden" name="cod_ut" value=<? echo $xcod_ut; ?>>
		<input type="hidden" name="nom_ut" value="<? echo $xnom_ut; ?>">
		<input type="hidden" name="vector_beneficiarios" value="">
		<input type="hidden" name="nano" value=<? echo $_REQUEST["nano"]; ?>>
		<input type="hidden" name="ninstitucion" value=<? echo $_REQUEST["ninstitucion"]; ?>>
		<input type="hidden" name="npreinversion" value=<? echo $_REQUEST["npreinversion"]; ?>>
		<input type="hidden" name="nficha" value=<? echo $_REQUEST["nficha"]; ?>>
		<input type="hidden" name="nsoloconsulta" value=<? echo $_REQUEST["nsoloconsulta"]; ?>>
		<input type="hidden" name="ncorresponde" value=<? echo $corresponde; ?>>
		<input type="hidden" name="usuario" value="<?php echo $_REQUEST["usuario"]; ?>">
		<input type="hidden" name="id_usuario" value="<?php echo $_REQUEST["id_usuario"]; ?>">
		<input type="hidden" name="uscl" value="<?php echo $_REQUEST["uscl"]; ?>">
		<!-- SE AÑADE PARA LA GEOLOCALIZACION -->
		<input type="hidden" name="contenedor_array" id="contenedor_array" value="" />
		<!-- FIN SE AÑADE PARA LA GEOLOCALIZACION -->
		<? echo $textohidden; ?>
		<script>
		   var totalsubfichas=document.form1.SInstrumento.length;
		  $(document).ready(function(){
			//document.getElementById("coordenadas").value="<?php echo $coordenadas; ?>";
		  });
		</script>
	</form>
	<form name="form_ayuda" method="post" action="" style="display:none;">
		<input type="hidden" name="nayuda" value="">
	</form>
</body>
</html>
