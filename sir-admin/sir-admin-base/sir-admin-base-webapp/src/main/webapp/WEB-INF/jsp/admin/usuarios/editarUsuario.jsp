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
<title>Editar Datos de Usuario</title>
<spring:url var="checkUsernameAvailability"
	value="/admin/usuarios/checkUsernameAvailability" />
<spring:url var="formUrl" value="/admin/salvarUsuario" />
<spring:url var="urlImages" value="/img" />
<script type="text/javascript" src="${jqueryValidator}"></script>
<script type="text/javascript" src="${jqueryValidatorLanguage}"></script>

<script type="text/javascript">
	$()
			.ready(
					function() {
						$.validator
								.addMethod(
										"alphanumeric",
										function(value, element) {
											return this.optional(element)
													|| /(?!^[0-9]*$)(?!^[a-zA-Z]*$)^([a-zA-Z0-9]{6,15})$/i
															.test(value);
										},
										"Debe contener números y letras y tener una longitud mínima de 6.");

						var validator = $("#usuarioDto").validate({
							errorClass : "campoError",
							validClass : "campo",
							rules : {
								password : {
									alphanumeric : 6,
								},
								confirmPassword : {
									equalTo : "#password"
								},
								nombreCompleto : {
									required : true
								},
								apellidos : {
									required : true
								},
								email : {
									required : true,
									email : true
								},
								telefono : {
									required : true
								},
								authorityId : {
									required : false
								}
							},
							errorPlacement : function(error, element) {
								$('#help-inline-' + element[0].id).html(error);
							}
						});

						
						$('#admin').click(function() {
							$('#institField')[this.checked ? "hide" : "show"]();

						});
					});
	
	
</script>
</head>
<body>

	<form:form commandName="usuarioDto" action="${formUrl}" method="POST">


		<form:hidden path="id" />

		<div class="span-21 contenidoPresentacion">

			<div class="span-5">
				<label class="labelCampo">Nombre de usuario</label>
			</div>

			<div class="controls span-14 last">
				<form:input path="username" cssClass="campo campoDisabled"
						disabled="true" readonly="true"/>
			</div>

			<div class="span-21 last">
				<html:inputField name="password" label="Contraseña" cssClass="campo"
					ctrCssClass="span-14"
					cssErrorClass="campoError" errorTextClass="fieldError" />
			</div>

			<div class="span-21">
				<div class="span-5">
					<label class="labelCampo" for="confirmPasswrd">Repita
						contraseña</label>
				</div>

				<div class="span-14 last">
					<form:input path="confirmPassword" cssClass="campo"
						id="confirmPassword" cssErrorClass="campoError" />
					<p class="help-inline" id="help-inline-confirmPassword">
						<form:errors path="matchPassword" cssClass="fieldError" />
					</p>
				</div>
			</div>

			<div class="span-21 last">
				<html:inputField label="Nombre" name="nombreCompleto"
					cssClass="campo" cssErrorClass="campoError"
					ctrCssClass="span-14"
					errorTextClass="fieldError" />
			</div>

			<div class="span-21 last">
				<html:inputField label="Apellidos" name="apellidos" cssClass="campo"
					ctrCssClass="span-14"
					cssErrorClass="campoError" errorTextClass="fieldError" />
			</div>

			<div class="span-21 last">
				<html:inputField label="e-mail" name="email" cssClass="campo"
					ctrCssClass="span-14"
					cssErrorClass="campoError" errorTextClass="fieldError" />
			</div>

			<div class="span-21 last">
				<html:inputField label="Fono" name="telefono" cssClass="campo"
					ctrCssClass="span-14"
					cssErrorClass="campoError" errorTextClass="fieldError" />
			</div>

			<div class="span-21 last">
				<html:checkboxField label="¿Es administrador?" name="admin"
					cssClass="campo" cssErrorClass="campoError"
					errorTextClass="fieldError" />
			</div>


			<div class="span-20" id="institField"
				style="display:${esAdmin ? 'none' : 'block'}">
				<div class="span-5">
					<label class="labelCampo" for="grupos">Institución</label>
				</div>

				<div class="span-15 last">
					<form:select path="authorityId" id="authorityId" multiple="false"
						cssClass="campo" cssErrorClass="campoError">
						<option value="">----</option>
						<form:options items="${authorityDtoList}" itemLabel="authority"
							itemValue="id" />
					</form:select>

					<p class="help-inline" id="help-inline-authorityId">
						<form:errors path="authorityId" cssClass="fieldError" />
					</p>

				</div>
			</div>

			<div class="span-20 last">

				<div class="span-5">
					<label class="labelCampo">¿Habilitado?</label>
				</div>

				<div class="span-14 last">
					<form:radiobutton path="valid" value="true" label="Sí"
						cssClass="campo" cssErrorClass="campoError" />
					<form:radiobutton path="valid" value="false" label="No"
						cssClass="campo" cssErrorClass="campoError" />
				</div>

				<div class="span-3 last">
					<form:errors path="valid" cssClass="fieldError" />
				</div>

			</div>

			<div class="span-20 last buttons-container">
				<button type="submit" class="button save">Guardar cambios</button>
				<spring:url var="urlListado" value="/admin/usuarios" />
				<a href="${urlListado}" class="button secondary cancel">Cancelar</a>
			</div>

		</div>

	</form:form>
</body>

</html>
