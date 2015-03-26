<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="html" tagdir="/WEB-INF/tags/html"%>
<%@ taglib prefix="ajax" tagdir="/WEB-INF/tags/ajax"%>
<%@ page contentType="text/html;charset=UTF-8" language="java"
	pageEncoding="UTF-8"%>

<html>
<head>
</head>
<body>
	<div class="span-11">
		<form:form commandName="layerPublishRequestDto" method="post">
			<div class="span-11 last" style="height: 400px; overflow-y:auto">
				<fieldset class="span-9" style="margin-left:5px">
					<legend>Información de mantenimiento</legend>


					<!-- frecuencia -->
					<div class="span-9">
						<div class="span-3">
							<label class="labelCampo">Frecuencia</label>
						</div>
						<div class="span-6 last">
							<form:input readonly="true" path="metadata.frecuencia"
                                                                title="Intervalo de tiempo en el que se realizan cambios"
								id="metadata.frecuencia" cssClass="campo"
								cssErrorClass="campoError" errorTextClass="fieldError" />

						</div>
					</div>

					<!-- siguiente Fecha -->
					<div class="span-9">
						<div class="span-3">
							<label class="labelCampo">Siguiente</label>
						</div>
						<div class="span-6 last">
							<form:input readonly="true" path="metadata.siguiente"
								id="metadata.siguiente" cssClass="campo"
                                                                title="Fecha en la que está prevista una revisión del dato"
								cssErrorClass="campoError" errorTextClass="fieldError" />

						</div>
					</div>
					<div class="span-9">
						<!-- periodo -->
						<div class="span-3">
							<label class="labelCampo">Período</label>
						</div>
						<div class="span-6 last">
							<form:input readonly="true" path="metadata.periodo"
								id="metadata.periodo" cssClass="campo"
                                                                title="Periodo de mantenimiento si es diferente de los pre-definidos en el estándar"
								cssErrorClass="campoError" errorTextClass="fieldError" />

						</div>
					</div>
					<div class="span-9 last">
						<!-- rango -->
						<div class="span-3">
							<label class="labelCampo">Rango</label>
						</div>
						<div class="span-6 last">
							<form:input readonly="true" path="metadata.rango"
                                                                title="Rango de datos a mantener"
								id="metadata.rango" cssClass="campo" cssErrorClass="campoError"
								errorTextClass="fieldError" />

						</div>
					</div>

					<div class="span-9">
						<!-- otros -->
						<div class="span-3">
							<label class="labelCampo">Otros</label>
						</div>
						<div class="span-6 last">
							<form:textarea readonly="true" path="metadata.otros"
                                                                 title="Información adicional acerca del rango o extensión del recurso"
								id="metadata.otros" cssClass="campo" cssErrorClass="campoError"
								errorTextClass="fieldError" />

						</div>
					</div>
					<div class="span-9 last">
						<!-- responsable -->
						<div class="span-3">
							<label class="labelCampo">Responsable</label>
						</div>
						<div class="span-6 last">
							<form:textarea readonly="true" path="metadata.responsable"
                                                                title="Identificación o forma de comunicarse con las personas u organizaciones responsables del mantenimiento del dato"
								id="metadata.responsable" cssClass="campo"
								cssErrorClass="campoError" errorTextClass="fieldError" />

						</div>
					</div>

					<div class="span-9 last">
						<!-- requerimientos -->
						<div class="span-3">
							<label class="labelCampo">Requerimientos</label>
						</div>
						<div class="span-6 last">
							<form:textarea readonly="true" path="metadata.requerimientos"
                                                                 title="Requerimientos específicos para mantener el dato"
								id="metadata.requerimientos" cssClass="campo"
								cssErrorClass="campoError" errorTextClass="fieldError" />

						</div>
					</div>

				</fieldset>



				<fieldset class="span-9" style="margin-left:5px">
					<legend>Información de distribución</legend>

					<div class="span-9">
						<!-- formato -->
						<div class="span-3">
							<label class="labelCampo">Formato</label>
						</div>
						<div class="span-6 last">
							<form:input readonly="true" path="metadata.formato"
                                                                title="Descripción del formato del dato a distribuir"
								id="metadata.formato" cssClass="campo"
								cssErrorClass="campoError" errorTextClass="fieldError" />

						</div>
					</div>
					<div class="span-9 last">
						<!-- distribuidor -->
						<div class="span-3">
							<label class="labelCampo">Distribuidor</label>
						</div>
						<div class="span-6 last">
							<form:input readonly="true" path="metadata.distribuidor"
                                                                    title="Información del distribuidor"
								id="metadata.distribuidor" cssClass="campo"
								cssErrorClass="campoError" errorTextClass="fieldError" />

						</div>
					</div>

					<div class="span-9 last">
						<!-- informacion-->
						<div class="span-3">
							<label class="labelCampo">Información</label>
						</div>
						<div class="span-6 last">
							<form:textarea readonly="true" path="metadata.informacion"
                                                                       title="Información técnica sobre el recurso"
								id="metadata.informacion" cssClass="campo"
								cssErrorClass="campoError" errorTextClass="fieldError" />

						</div>

					</div>
				</fieldset>

				<!-- Metadatos para capas raster -->
				<div class="span-9"
					style="display:${layerType eq 'Raster' ? 'block' : 'none'}">
					<fieldset class="span-9" style="margin-left:5px">
						<legend>Información de Geo-Rectificación</legend>

						<div class="span-9 last" >
							<!--referencia -->
							<div class="span-3">
								<label class="labelCampo">Referencia</label>
							</div>
							<div class="span-6 last">
								<form:input readonly="true" path="metadata.referencia"
                                                                            title="Referencia geográfica para validar la georrectificación del dato"
									id="metadata.referencia" cssClass="campo"
									cssErrorClass="campoError" errorTextClass="fieldError" />

							</div>
						</div>
					</fieldset>

					<fieldset class="span-9" style="margin-left:5px">
						<legend>Información de Geo-Referenciación</legend>

						<div class="span-9 last">
							<!--informacionGeolocalizacion -->
							<div class="span-3">
								<label class="labelCampo">Inf. de Geolocalización</label>
							</div>
							<div class="span-6 last">
								<form:input readonly="true" path="metadata.informacionGeolocalizacion"
                                                                        title="Información usada para geolocalizar el dato"
									id="metadata.informacionGeolocalizacion"
									cssClass="campo" cssErrorClass="campoError"
									errorTextClass="fieldError" />

							</div>
						</div>
					</fieldset>


					<fieldset class="span-9" style="margin-left:5px">
						<legend>Información de Geo-Localización</legend>

						<div class="span-9">
							<!-- calidad -->
							<div class="span-3">
								<label class="labelCampo">Calidad</label>
							</div>
							<div class="span-6 last">
								<form:input readonly="true" path="metadata.calidad"
                                                                         title="Evaluación general de la calidad del dato"
									id="metadata.calidad" cssClass="campo"
									cssErrorClass="campoError" errorTextClass="fieldError" />

							</div>
						</div>
						<div class="span-9 last">
							<!-- informacionColeccionPuntoReferencia -->
							<div class="span-3">
								<label class="labelCampo">Inf. Colec. Punto Referencia</label>
							</div>
							<div class="span-6 last">
								<form:input readonly="true" path="metadata.informacionColeccionPuntoReferencia"
									title="Información de colecciones de puntos de control en tierra para la geolocalización"
									id="metadata.informacionColeccionPuntoReferencia"
									cssClass="campo" cssErrorClass="campoError"
									errorTextClass="fieldError" />

							</div>
						</div>
						<div class="span-9">
							<!-- informacionPuntoReferencia -->
							<div class="span-3">
								<label class="labelCampo">Inf. Punto Referencia</label>
							</div>
							<div class="span-6 last">
								<form:input readonly="true" path="metadata.informacionPuntoReferencia"
									id="metadata.informacionPuntoReferencia"
                                                                        title="Identificador de la colección GCP"
									cssClass="campo" cssErrorClass="campoError"
									errorTextClass="fieldError" />

							</div>
						</div>
						<div class="span-9 last">
							<!-- nombrePuntoReferencia -->
							<div class="span-3">
								<label class="labelCampo">Nombre Punto Referencia</label>
							</div>
							<div class="span-6 last">
								<form:input readonly="true" path="metadata.nombrePuntoReferencia" 
									id="metadata.nombrePuntoReferencia" cssClass="campo"
                                                                        title="Nombre de la colección GCP"
									cssErrorClass="campoError" errorTextClass="fieldError" />

							</div>
						</div>
						<div class="span-9">
							<!-- crsPuntoReferencia -->

							<div class="span-3">
								<label class="labelCampo">CRS Punto Referencia</label>
							</div>
							<div class="span-6 last">
								<form:input readonly="true" path="metadata.crsPuntoReferencia"
									id="metadata.crsPuntoReferencia" cssClass="campo"
                                                                        title="Sistema de coordenadas de los puntos de la colección GCP"
									cssErrorClass="campoError" errorTextClass="fieldError" />

							</div>

						</div>
						<div class="span-9 last">
							<!-- puntosDeReferencia -->
							<div class="span-3">
								<label class="labelCampo">Puntos De Referencia</label>
							</div>
							<div class="span-6 last">
								<form:input readonly="true" path="metadata.puntosDeReferencia"
                                                                            title="Puntos de la colección GCP"
									id="metadata.puntosDeReferencia" cssClass="campo"
									cssErrorClass="campoError" errorTextClass="fieldError" />

							</div>
						</div>

					</fieldset>


					<fieldset class="span-9" style="margin-left:5px">
						<legend>Puntos de control</legend>
						<div class="span-9">
							<!--posicion -->
							<div class="span-3">
								<label class="labelCampo">Posición</label>
							</div>
							<div class="span-6 last">
								<form:input readonly="true" path="metadata.posicion"
									id="metadata.posicion" cssClass="campo"
                                                                        title="Posición del punto"
									cssErrorClass="campoError" errorTextClass="fieldError" />

							</div>
						</div>
						<div class="span-9 last">

							<!-- precision -->
							<div class="span-3">
								<label class="labelCampo">Precisión</label>
							</div>
							<div class="span-6 last">
								<form:input readonly="true" path="metadata.precision"
									id="metadata.precision" cssClass="campo"
                                                                        title="Precisión del punto"
									cssErrorClass="campoError" errorTextClass="fieldError" />

							</div>

						</div>
					</fieldset>
				</div>

			</div>
			<!-- Botonera form -->
			<div class="span-10 last buttons-container">
				<a href="javascript:void(0)"
					onclick="$('#metadataDialog').dialog('close')"
					class="button secondary cancel">Cancelar</a>
			</div>
		</form:form>
	</div>
</body>
</html>