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
<title>Nueva Instituci&oacuten</title>
<spring:url value="/admin/instituciones" var="urlListadoInstitucion" />
<script type="text/javascript">
	// We define the validator this way becasuse we don't have validation support
	// in the html taglib (we can't specify required or minlength)
	/* $()
			.ready(
					function() {
						$.validator
								.addMethod(
										"alphanumeric",
										function(value, element) {
											return this.optional(element)
													|| /(?!^[0-9]*$)(?!^[a-zA-Z]*$)^([a-zA-Z0-9]{3,60})$/i
															.test(value);
										},
										"Debe contener números y letras y tener una longitud mínima de 3.");

						var validator = $("#authorityDto").validate({
							errorClass : "campoError",
							validClass : "campo",
							rules : {
								authority : {
									required : true,
									minlength : 3
								}
							},
							errorPlacement : function(error, element) {
								$('#help-inline-' + element[0].id).html(error);
							}
						});

					}); */
</script>
</head>
<body>

	<form:form commandName="authorityDto" action="salvarInstitucion"
		method="post">


		<!-- Nombre institucion -->
		<div class="span-21 adm-institution last">
			<%--<html:inputField name="authority" label="Nombre Institución"
				cssClass="campo" cssErrorClass="campoError"
				errorTextClass="fieldError" />--%>
			<div id="nombre">

				<div class="span-5">
					<label class="labelCampo">Nombre</label>
				</div>

				<div class="span-15 last">

					<form:select path="authority" items="${nombresInstitucion}"
						itemLabel="nInstitucion" itemValue="nInstitucion" id="authority"
						cssClass="campo" cssErrorClass="campoError">
					</form:select>

					<span class="help-inline" id="help-inline-nombreSeleccionado">
						<form:errors path="authority" cssClass="fieldError" />
					</span>

				</div>
			</div>
		</div>

		<!-- Tipo institucion Check (Municipalidad/Servicio Publico/Otros) -->
		<div class="span-21">
			<div class="span-5">
				<label class="labelCampo">Tipo</label>
			</div>

			<div class="span-15">
				<form:radiobuttons path="tipoSeleccionado" items="${tiposAutoridad}"
					itemLabel="name" itemValue="id" cssClass="campo"
					cssErrorClass="campoError" />
				<span class="help-inline" id="help-inline-tipoSeleccionado">	
					<form:errors path="tipoSeleccionado" cssClass="fieldError" />
				</span>
			</div>
		</div>

		<!-- Ambito territorial -->
		<div class="span-21 last">

			<div id="capaAmbitoTerritorial">

				<div class="span-5">
					<label class="labelCampo">Ámbito Territorial</label>
				</div>

				<div class="span-15 last">

					<form:select path="ambitoSeleccionado" items="${ambitoTerritorial}"
						itemLabel="name" itemValue="id" id="ambitoSeleccionado"
						cssClass="campo" cssErrorClass="campoError">
					</form:select>

					<span class="help-inline" id="help-inline-ambitoSeleccionado">
						<form:errors path="ambitoSeleccionado" cssClass="fieldError" />
					</span>

				</div>
			</div>

		</div>

		<!-- Botonera form -->
		<div class="buttons-container">
			<button type="submit" class="button new create">Crear</button>
			<a href="${urlListadoInstitucion}" class="button secondary cancel">Cancelar</a>
		</div>
	</form:form>
</body>
</html>