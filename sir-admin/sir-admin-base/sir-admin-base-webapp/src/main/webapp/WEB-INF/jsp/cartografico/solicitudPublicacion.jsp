<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ page contentType="text/html;charset=UTF-8" language="java"
	pageEncoding="UTF-8"%>

<html>
<head>
<title>Solicitar Publicación de capa '${sourceLayerTitle}'</title>
<spring:url value="/js/publishRequests.js" var="publishRequestsJsUrl" />
<script type="text/javascript" src="${publishRequestsJsUrl }"></script>
<spring:url value="/cartografico/layersAuthority"
	var="urlLayersInstitucion" />
<spring:url value="/cartografico/jsonLayerMetadata"
	var="loadMetadataUrl" />

<script type="text/javascript">
	urls["loadMetadata"] = "${loadMetadataUrl}";
</script>

<spring:url var="salvarSolicitudPublicacion"
	value="/cartografico/salvarSolicitudPublicacion" />

</head>

<body>



	<form:form commandName="layerPublishRequestDto" class="publicationRequestForm"
		action="${salvarSolicitudPublicacion}" method="post">


		<!-- Id solicitud publicacion -->
		<form:hidden path="id" id="layerPublishRequestId" />

		<!-- Id de la capa -->
		<form:hidden path="sourceLayerId" id="sourceLayerId" />

		<!-- Id usuario Logado-->
		<form:hidden path="user.id" id="userId" />

		<!-- Id usuario auth-->
		<form:hidden path="auth.id" id="authorityId" />

		<!-- Id Metadatos-->
		<form:hidden path="metadata.id" id="metadataId" />

		<!-- Acciones -->
		<div class="header span-22 last">
			<fieldset class="span-10">
				<legend>Acción</legend>
				<div class="span-5">
					<form:radiobutton path="accionEjecutar" value="PUBLISH_NEW"
						onclick="publishRequests.toggleAction('PUBLISH_NEW');"
						label="Publicar como nueva" />
				</div>
				<div class="span-5 last">
					<form:radiobutton path="accionEjecutar"
						value="PUBLISH_UPDATE"
						onclick="publishRequests.toggleAction('PUBLISH_UPDATE');"
						label="Actualizar capa publicada" />
				</div>
				<span class="help-inline" id="help-inline-accionEjecutar"> <form:errors
						path="accionEjecutar" cssClass="fieldError" />
				</span>
			</fieldset>

			<fieldset class="span-10 last">
				<legend>Datos de capa</legend>
				<!-- Nombre deseado -->
				<div id="desiredNameRow" class="span-10"
					style="display: ${layerPublishRequestDto.accionEjecutar eq 'PUBLISH_NEW' ? 'block' : 'none'}">
					<div class="">
						<label class="labelCampoDatos">Nombre deseado</label>
					</div>
					<div class="">
						<form:input path="nombredeseado" maxlength="250"
							id="nombredeseado" cssClass="campo" cssErrorClass="campoError"
							errorTextClass="fieldError" />
						<div class="help-inline" id="help-inline-nombredeseado">
							<form:errors path="nombredeseado" cssClass="fieldError" />
						</div>
					</div>
				</div>
				<!-- Capa a actualizar -->
				<div id="updatedLayerRow" class="span-10 last"
					style="display: ${layerPublishRequestDto.accionEjecutar eq 'PUBLISH_UPDATE' ? 'block' : 'none'}">
					<div class="">
						<label class="labelCampoDatos">Capa a actualizar</label>
					</div>
					<div class="">
						<form:select path="updatedLayerId" id="updatedLayerId"
							multiple="false" cssClass="campo" cssErrorClass="campoError"
							onchange="publishRequests.loadMetadataOfUpdatableLayer(value)"
							data-placeholder="Click para seleccionar">
							<option value=""></option>
							<form:options items="${layersActualizar}" itemLabel="layerLabel"
								itemValue="id" />
						</form:select>
						<div class="help-inline span-6" id="help-inline-updatedLayerId">
							<form:errors path="updatedLayerId" cssClass="fieldError" />
						</div>
					</div>
				</div>
				<div class="span-10 last">
					<!-- Tipo de capa -->
					<div class="">
						<label class="labelCampoDatos">Tipo capa</label>
					</div>
					<div class="">
						<form:input path="tipoCapaSeleccionada" id="tipoCapaSeleccionada"
							cssClass="campo campoDisabled" disabled="true"
							cssErrorClass="campoError" />
						<span class="help-inline" id="help-inline-tipoCapaSeleccionada">
							<form:errors path="tipoCapaSeleccionada" cssClass="fieldError" />
						</span>
					</div>
				</div>
			</fieldset>
		</div>		
		<!-- Metadatos para capas vectoriales / rasters-->
		<fieldset>
			<legend>Información de mantenimiento</legend>

			<div class="span-18 last">
				<!-- frecuencia -->
				<div class="span-9">
					<div class="span-3">
						<label class="labelCampo">Frecuencia</label>
					</div>
					<div class="span-6 last">
						<form:input path="metadata.frecuencia" maxlength="20"
							id="metadata.frecuencia" cssClass="campo"
							title="Invervalo de tiempo en el que se realizan cambios"
							cssErrorClass="campoError" errorTextClass="fieldError" />
						<span class="help-inline" style="display: blocK;"
							id="help-inline-metadata.frecuencia"> <form:errors
								path="metadata.frecuencia" cssClass="fieldError" />
						</span>
					</div>
				</div>
				<div class="span-9 last">
					<div class="span-3">
						<label class="labelCampo">Siguiente</label>
					</div>
					<div class="span-6 last">
						<form:input path="metadata.siguiente" maxlength="250"
							id="metadata.siguiente" cssClass="campo datepicker"
							title="Fecha en la que está prevista una revisión del dato"
							readonly="true" cssErrorClass="campoError datepicker"
							errorTextClass="fieldError" />
						<span class="help-inline" style="display: blocK;"
							id="help-inline-metadata.siguiente"> <form:errors
								path="metadata.siguiente" cssClass="fieldError" />
						</span>
					</div>
				</div>
			</div>

			<div class="span-18 last">
				<div class="span-9">
					<!-- periodo -->
					<div class="span-3">
						<label class="labelCampo">Periodo</label>
					</div>
					<div class="span-6 last">
						<form:input path="metadata.periodo" maxlength="250"
							title="Periodo de mantenimiento si es diferente de los predefinidos en el estándar"
							id="metadata.periodo" cssClass="campo" cssErrorClass="campoError"
							errorTextClass="fieldError" />
						<span class="help-inline" style="display: blocK;"
							id="help-inline-metadata.periodo"> <form:errors
								path="metadata.periodo" cssClass="fieldError" />
						</span>
					</div>
				</div>
				<div class="span-9 last">
					<!-- rango -->
					<div class="span-3">
						<label class="labelCampo">Rango</label>
					</div>
					<div class="span-6 last">
						<form:input path="metadata.rango" maxlength="250"
							id="metadata.rango" cssClass="campo" cssErrorClass="campoError"
							title="Rango de datos a mantener" errorTextClass="fieldError" />
						<span class="help-inline" style="display: blocK;"
							id="help-inline-metadata.rango"> <form:errors
								path="metadata.rango" cssClass="fieldError" />
						</span>
					</div>
				</div>
			</div>

			<div class="span-18 last">
				<!-- otros -->
				<div class="span-3">
					<label class="labelCampo">Otros</label>
				</div>
				<div class="span-15 last">
					<form:textarea path="metadata.otros" maxlength="250"
						title="Información adicional acerca del rango o extensión del recurso"
						id="metadata.otros" cssClass="campo" cssErrorClass="campoError"
						errorTextClass="fieldError" />
					<span class="help-inline" style="display: blocK;"
						id="help-inline-metadata.otros"> <form:errors
							path="metadata.otros" cssClass="fieldError" />
					</span>
				</div>
			</div>
			<div class="span-18 last">
				<!-- responsable -->
				<div class="span-3">
					<label class="labelCampo">Responsable</label>
				</div>
				<div class="span-15 last">
					<form:textarea path="metadata.responsable"
						title="Identificación o forma de comunicarse con las personas u organizaciones responsables del mantenimiento del dato"
						id="metadata.responsable" cssClass="campo"
						cssErrorClass="campoError" errorTextClass="fieldError" />
					<span class="help-inline" style="display: blocK;"
						id="help-inline-metadata.responsable"> <form:errors
							path="metadata.responsable" cssClass="fieldError" />
					</span>
				</div>
			</div>

			<div class="span-18 last">

				<!-- requerimientos -->
				<div class="span-3">
					<label class="labelCampo">Requerimientos</label>
				</div>
				<div class="span-15 last">
					<form:textarea path="metadata.requerimientos"
						id="metadata.requerimientos" cssClass="campo"
						title="Requerimientos específicos para mantener el dato"
						cssErrorClass="campoError" errorTextClass="fieldError" />
					<span class="help-inline" style="display: blocK;"
						id="help-inline-metadata.requerimientos"> <form:errors
							path="metadata.requerimientos" cssClass="fieldError" />
					</span>
				</div>


			</div>
		</fieldset>



		<fieldset>
			<legend>Información de distribución</legend>
			<div class="span-18 last">
				<div class="span-9">
					<!-- formato -->
					<div class="span-3">
						<label class="labelCampo">Formato</label>
					</div>
					<div class="span-6 last">
						<form:input path="metadata.formato" maxlength="250"
							title="Descripción del formato del dato a distribuir"
							id="metadata.formato" cssClass="campo" cssErrorClass="campoError"
							errorTextClass="fieldError" />
						<span class="help-inline" style="display: blocK;"
							id="help-inline-metadata.formato"> <form:errors
								path="metadata.formato" cssClass="fieldError" />
						</span>
					</div>
				</div>
				<div class="span-9 last">
					<!-- distribuidor -->
					<div class="span-3">
						<label class="labelCampo">Distribuidor</label>
					</div>
					<div class="span-6 last">
						<form:input path="metadata.distribuidor" maxlength="250"
							id="metadata.distribuidor" cssClass="campo"
							title="Información del distribuidor" cssErrorClass="campoError"
							errorTextClass="fieldError" />
						<span class="help-inline" style="display: blocK;"
							id="help-inline-metadata.distribuidor"> <form:errors
								path="metadata.distribuidor" cssClass="fieldError" />
						</span>
					</div>
				</div>
			</div>

			<div class="span-18 last">

				<!-- informacion-->
				<div class="span-3">
					<label class="labelCampo">Información</label>
				</div>
				<div class="span-15 last">
					<form:textarea path="metadata.informacion"
						title="Información técnica del recurso" id="metadata.informacion"
						cssClass="campo" cssErrorClass="campoError"
						errorTextClass="fieldError" />
					<span class="help-inline" style="display: blocK;"
						id="help-inline-metadata.informacion"> <form:errors
							path="metadata.informacion" cssClass="fieldError" />
					</span>
				</div>
			</div>
		</fieldset>

		<!-- Metadatos para capas raster -->
		<c:if
			test="${layerPublishRequestDto.sourceLayerType.tipo eq 'Raster'}">
			<fieldset>
				<legend>Información de Geo-Rectificación</legend>
				<div span="span-18 last">
					<div class="span-9 last">
						<!--referencia -->
						<div class="span-3">
							<label class="labelCampo">Referencia</label>
						</div>
						<div class="span-6 last">
							<form:input path="metadata.referencia" maxlength="250"
								id="metadata.referencia" cssClass="campo"
								title="Referencia geográfica para validar la georrectificación del dato"
								cssErrorClass="campoError" errorTextClass="fieldError" />
							<span class="help-inline" style="display: blocK;"
								id="help-inline-metadata.referencia"> <form:errors
									path="metadata.referencia" cssClass="fieldError" />
							</span>
						</div>
					</div>

				</div>
			</fieldset>

			<fieldset>
				<legend>Información de Geo-Referenciación</legend>
				<div class="span-18 last">
					<div class="span-9">
						<!--informacionGeolocalizacion -->
						<div class="span-3">
							<label class="labelCampo long">Inf. de Geolocalización</label>
						</div>
						<div class="span-6 last">
							<form:input path="metadata.informacionGeolocalizacion"
								title="Información usada para geolocalizar el dato"
								maxlength="250" id="metadata.informacionGeolocalizacion"
								cssClass="campo" cssErrorClass="campoError"
								errorTextClass="fieldError" />
							<span class="help-inline" style="display: blocK;"
								id="help-inline-metadata.informacionGeolocalizacion"> <form:errors
									path="metadata.informacionGeolocalizacion"
									cssClass="fieldError" />

							</span>
						</div>
					</div>
				</div>
			</fieldset>


			<fieldset>
				<legend>Información de Geo-Localización</legend>
				<div class="span-18 last">
					<div class="span-9">
						<!-- calidad -->
						<div class="span-3">
							<label class="labelCampo">Calidad</label>
						</div>
						<div class="span-6 last">
							<form:input path="metadata.calidad" maxlength="250"
								title="Evaluación general de la calidad del dato"
								id="metadata.calidad" cssClass="campo"
								cssErrorClass="campoError" errorTextClass="fieldError" />
							<span class="help-inline" style="display: blocK;"
								id="help-inline-metadata.calidad"> <form:errors
									path="metadata.calidad" cssClass="fieldError" />
							</span>
						</div>
					</div>
					<div class="span-9 last">
						<!-- informacionColeccionPuntoReferencia -->
						<div class="span-3">
							<label class="labelCampo long">Inf. Colec. Punto Referencia</label>
						</div>
						<div class="span-6 last">
							<form:input path="metadata.informacionColeccionPuntoReferencia"
								maxlength="250"
								title="Información de colecciones de puntos de control en tierra para la geolocalización"
								id="metadata.informacionColeccionPuntoReferencia"
								cssClass="campo" cssErrorClass="campoError"
								errorTextClass="fieldError" />
							<span class="help-inline" style="display: blocK;"
								id="help-inline-metadata.informacionColeccionPuntoReferencia">
								<form:errors path="metadata.informacionColeccionPuntoReferencia"
									cssClass="fieldError" />
							</span>
						</div>
					</div>
				</div>

				<div class="span-18 last">
					<div class="span-9">
						<!-- informacionPuntoReferencia -->
						<div class="span-3">
							<label class="labelCampo">Inf. Punto Referencia</label>
						</div>
						<div class="span-6 last">
							<form:input path="metadata.informacionPuntoReferencia"
								title="Indentificador de la colección GCP" maxlength="250"
								id="metadata.informacionPuntoReferencia" cssClass="campo"
								cssErrorClass="campoError" errorTextClass="fieldError" />
							<span class="help-inline" style="display: blocK;"
								id="help-inline-metadata.informacionPuntoReferencia"> <form:errors
									path="metadata.informacionPuntoReferencia"
									cssClass="fieldError" />
							</span>
						</div>
					</div>
					<div class="span-9 last">
						<!-- nombrePuntoReferencia -->
						<div class="span-3">
							<label class="labelCampo long">Nombre Punto Referencia</label>
						</div>
						<div class="span-6 last">
							<form:input path="metadata.nombrePuntoReferencia" maxlength="10"
								title="Nombre de la colección GCP"
								id="metadata.nombrePuntoReferencia" cssClass="campo"
								cssErrorClass="campoError" errorTextClass="fieldError" />
							<span class="help-inline" style="display: blocK;"
								id="help-inline-metadata.nombrePuntoReferencia"> <form:errors
									path="metadata.nombrePuntoReferencia" cssClass="fieldError" />
							</span>
						</div>
					</div>
				</div>

				<div class="span-18 last">
					<div class="span-9">
						<!-- crsPuntoReferencia -->

						<div class="span-3">
							<label class="labelCampo">CRS Punto Referencia</label>
						</div>
						<div class="span-6 last">
							<form:input path="metadata.crsPuntoReferencia" maxlength="250"
								title="Sistema de coordenadas de los puntos de la colección GCP"
								id="metadata.crsPuntoReferencia" cssClass="campo"
								cssErrorClass="campoError" errorTextClass="fieldError" />
							<span class="help-inline" style="display: blocK;"
								id="help-inline-metadata.crsPuntoReferencia"> <form:errors
									path="metadata.crsPuntoReferencia" cssClass="fieldError" />
							</span>
						</div>

					</div>
					<div class="span-9 last">
						<!-- puntosDeReferencia -->
						<div class="span-3">
							<label class="labelCampo">Puntos De Referencia</label>
						</div>
						<div class="span-6 last">
							<form:input path="metadata.puntosDeReferencia" maxlength="250"
								id="metadata.puntosDeReferencia" cssClass="campo"
								title="Puntos de la colección GCP" cssErrorClass="campoError"
								errorTextClass="fieldError" />
							<span class="help-inline" style="display: blocK;"
								id="help-inline-metadata.puntosDeReferencia"> <form:errors
									path="metadata.puntosDeReferencia" cssClass="fieldError" />
							</span>
						</div>
					</div>
				</div>

			</fieldset>


			<fieldset>
				<legend>Puntos de control</legend>
				<div class="span-9">
					<!--posicion -->
					<div class="span-3">
						<label class="labelCampo">Posición</label>
					</div>
					<div class="span-6 last">
						<form:input path="metadata.posicion" maxlength="250"
							title="Posición del punto" id="metadata.posicion"
							cssClass="campo" cssErrorClass="campoError"
							errorTextClass="fieldError" />
						<span class="help-inline" style="display: blocK;"
							id="help-inline-metadata.posicion"> <form:errors
								path="metadata.posicion" cssClass="fieldError" />
						</span>
					</div>
				</div>
				<div class="span-9 last">

					<!-- precision -->
					<div class="span-3">
						<label class="labelCampo">Precisión</label>
					</div>
					<div class="span-6 last">
						<form:input path="metadata.precision" maxlength="250"
							id="metadata.precision" cssClass="campo"
							title="Precisión del punto" cssErrorClass="campoError"
							errorTextClass="fieldError" />
						<span class="help-inline" style="display: blocK;"
							id="help-inline-metadata.precision"> <form:errors
								path="metadata.precision" cssClass="fieldError" />
						</span>
					</div>

				</div>
			</fieldset>
		</c:if>

		<!-- Botonera form -->
		<div class="span-18 last buttons-container">
			<button type="submit" class="button submit save">Guardar</button>
			<a href="${urlLayersInstitucion}" class="button secondary cancel">Cancelar
			</a>
		</div>
	</form:form>
</body>
</html>
