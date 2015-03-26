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
	<div class="tab_container contenido">
		<c:choose>
			<c:when test="${ noMetadata}">
			<p>No hay metadatos para la capa seleccionada, no proviene de una solicitud de publicación.</p>
			</c:when>
			<c:otherwise>
				<form:form commandName="layerDto">
					<div class="contenidoPresentacion">
						<div>
							<!-- Metadatos para capas vectoriales / rasters-->
							<fieldset style="padding:5px;margin:5px">
								<legend>Información de mantenimiento</legend>
								<div>
									<!-- frecuencia -->
									<div>
										<label class="x-form-item-label">Frecuencia</label>
									</div>
									<div>
										<form:input path="metadata.frecuencia" style="width:100%"
                                                                                        title="Intervalo de tiempo en el que se realizan cambios"
											id="metadata.frecuencia" cssClass="x-form-text x-form-field"
											readonly="true" />
									</div>
									<!-- siguiente Fecha -->
									<div>
										<label class="labelCampo">Siguiente</label>
									</div>
									<div>
										<form:input path="metadata.siguiente" style="width:100%"
                                                                                        title="Fecha en la que está prevista una revisión del dato"
											id="metadata.siguiente" cssClass="x-form-text x-form-field"
											readonly="true" />
									</div>
									<!-- periodo -->
									<div>
										<label class="labelCampo">Periodo</label>
									</div>
									<div>
										<form:input path="metadata.periodo" style="width:100%"
                                                                                        title="Periodo de mantenimiento si es diferente de los pre-definidos en el estándar"   
											id="metadata.periodo" cssClass="x-form-text x-form-field"
											readonly="true" />
									</div>
									<!-- rango -->
									<div>
										<label class="labelCampo">Rango</label>
									</div>
									<div>
										<form:input path="metadata.rango" style="width:100%"
                                                                                          title="Rango de datos a mantener"
											id="metadata.rango" cssClass="x-form-text x-form-field"
											readonly="true" />
									</div>
									<!-- otros -->
									<div>
										<label class="labelCampo">Otros</label>
									</div>
									<div>
										<form:textarea path="metadata.otros" style="width:100%;height:7em"
                                                                                            title="Información adicional acerca del rango o extensión del recurso"
											id="metadata.otros" cssClass="x-form-text x-form-field"
											readonly="true" />
									</div>
									<!-- responsable -->
									<div>
										<label class="labelCampo">Responsable</label>
									</div>
									<div>
										<form:textarea path="metadata.responsable" style="width:100%;height:7em"
                                                                                            title="Identificación o forma de comunicarse con las personas u organizaciones responsables del mantenimiento del dato"
											id="metadata.responsable" cssClass="x-form-text x-form-field"
											readonly="true" />

									</div>
									<!-- requerimientos -->
									<div>
										<label class="labelCampo">Requerimientos</label>
									</div>
									<div>
										<form:textarea path="metadata.requerimientos" style="width:100%;height:7em"
                                                                                        title="Requerimientos específicos para mantener el dato"
											id="metadata.requerimientos" cssClass="x-form-text x-form-field"
											readonly="true" />
									</div>
								</div>
							</fieldset>
							<fieldset style="padding:5px;margin:5px">
								<legend>Información de distribución</legend>
								<div>
									<!-- formato -->
									<div>
										<label class="labelCampo">Formato</label>
									</div>
									<div>
										<form:input path="metadata.formato" style="width:100%"
                                                                                             title="Descripción del formato del dato a distribuir"
											id="metadata.formato" cssClass="x-form-text x-form-field"
											readonly="true" />
									</div>
									<!-- distribuidor -->
									<div>
										<label class="labelCampo">Distribuidor</label>
									</div>
									<div>
										<form:input path="metadata.distribuidor" style="width:100%"
                                                                                            title="Información del distribuidor"
											id="metadata.distribuidor" cssClass="x-form-text x-form-field"
											readonly="true" />
									</div>
									<!-- informacion-->
									<div>
										<label class="labelCampo">Información</label>
									</div>
									<div>
										<form:textarea path="metadata.informacion" style="width:100%;height:7em"
                                                                                               title="Información técnica sobre el recurso"
											id="metadata.informacion" cssClass="x-form-text x-form-field"
											readonly="true" />
									</div>
								</div>
							</fieldset>
							<!-- Metadatos para capas raster -->
							<c:if test="${layerType eq 'Raster'}">
								<div>
									<fieldset style="padding:5px;margin:5px">
										<legend>Información de Geo-Rectificación</legend>
										<div>
											<!--referencia -->
											<div>
												<label class="labelCampo">Referencia</label>
											</div>
											<div>
												<form:input path="metadata.referencia" style="width:100%"
													   title="Referencia geográfica para validar la georrectificación del dato"
                                                                                                            id="metadata.referencia" cssClass="x-form-text x-form-field"
													readonly="true" />
											</div>
										</div>
									</fieldset>
									<fieldset style="padding:5px;margin:5px">
										<legend>Información de Geo-Referenciación</legend>
										<div>
											<!--informacionGeolocalizacion -->
											<div>
												<label class="labelCampo">Información
													Geolocalización</label>
											</div>
											<div>
												<form:input path="metadata.informacionGeolocalizacion"
                                                                                                             title="Información usada para geolocalizar el dato"
													style="width:100%" id="metadata.informacionGeolocalizacion"
													errorTextClass="fieldError" readonly="true" />
											</div>
										</div>
									</fieldset>


									<fieldset style="padding:5px;margin:5px">
										<legend>Información de Geo-Localización</legend>
										<div>
											<!-- calidad -->
											<div>
												<label class="labelCampo">Calidad</label>
											</div>
											<div>
												<form:input path="metadata.calidad" style="width:100%"
                                                                                                             title="Evaluación general de la calidad del dato"
													id="metadata.calidad" cssClass="x-form-text x-form-field"
													readonly="true" />
											</div>

											<!-- informacionColeccionPuntoReferencia -->
											<div>
												<label class="labelCampo">Inf. Colec. Punto Ref.</label>
											</div>
											<div>
												<form:input
													path="metadata.informacionColeccionPuntoReferencia"
													style="width:100%"
                                                                                                        title="Información de colecciones de puntos de control en tierra para la geolocalización"
													id="metadata.informacionColeccionPuntoReferencia"
													errorTextClass="fieldError" readonly="true" />
											</div>

											<!-- informacionPuntoReferencia -->
											<div>
												<label class="labelCampo">Información Punto
													Referencia</label>
											</div>
											<div>
												<form:input path="metadata.informacionPuntoReferencia"
                                                                                                            title="Identificador de la colección GCP"
													style="width:100%" id="metadata.informacionPuntoReferencia"
													errorTextClass="fieldError" readonly="true" />
											</div>

											<!-- nombrePuntoReferencia -->
											<div>
												<label class="labelCampo">Nombre Punto Referencia</label>
											</div>
											<div>
												<form:input path="metadata.nombrePuntoReferencia"
                                                                                                             title="Nombre de la colección GCP"
													maxlength="10" id="metadata.nombrePuntoReferencia"
													errorTextClass="fieldError" readonly="true" />
											</div>

											<!-- crsPuntoReferencia -->
											<div>
												<div>
													<label class="labelCampo">CRS Punto Referencia</label>
												</div>
												<div>
													<form:input path="metadata.crsPuntoReferencia"
														style="width:100%" id="metadata.crsPuntoReferencia"
                                                                                                                 title="Sistema de coordenadas de los puntos de la colección GCP"
														errorTextClass="fieldError" readonly="true" />
												</div>
											</div>


											<!-- puntosDeReferencia -->
											<div>
												<div>
													<label class="labelCampo">Puntos De Referencia</label>
												</div>
												<div>
													<form:input path="metadata.puntosDeReferencia"
                                                                                                                      title="Puntos de la colección GCP"
														style="width:100%" id="metadata.puntosDeReferencia"
														errorTextClass="fieldError" readonly="true" />
												</div>
											</div>
										</div>
									</fieldset>


									<fieldset style="padding:5px;margin:5px">
										<legend>Puntos de control</legend>
										<div>
											<!--posicion -->
											<div>
												<label class="labelCampo">Posición</label>
											</div>
											<div>
												<form:input path="metadata.posicion" style="width:100%"
                                                                                                            title="Posición del punto"
													id="metadata.posicion" cssClass="x-form-text x-form-field"
													readonly="true" />
											</div>

											<!-- precision -->
											<div>
												<label class="labelCampo">Precisión</label>
											</div>
											<div>
												<form:input path="metadata.precision" style="width:100%"
                                                                                                             title="Precisión del punto"
													id="metadata.precision" cssClass="x-form-text x-form-field"
													readonly="true" />
											</div>
										</div>
									</fieldset>
								</div>
							</c:if>
						</div>
					</div>
				</form:form>
			</c:otherwise>
		</c:choose>

	</div>
</body>
</html>