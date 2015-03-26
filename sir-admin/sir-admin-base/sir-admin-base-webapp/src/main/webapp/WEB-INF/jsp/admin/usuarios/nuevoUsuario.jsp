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
<title>Nuevo Usuario</title>
<spring:url var="checkUsernameAvailability"
	value="/admin/usuarios/checkUsernameAvailability" />
<spring:url var="urlImages" value="/img" />

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

						var validator = $("#usuarioDto")
								.validate(
										{
											errorClass : "campoError",
											validClass : "campo",
											rules : {
												username : {
													required : true,
													minlength : 4,
													remote : "${checkUsernameAvailability}"
												},
												password : {
													required : true,
													alphanumeric : 6,
												},
												confirmPassword : {
													required : true,
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
											messages : {
												username : {
													remote : jQuery.validator
															.format('Ya está en uso, elija otro nombre de usuario')
												}
											},
											errorPlacement : function(error,
													element) {
												$(
														'#help-inline-'
																+ element[0].id)
														.html(error);
											}
										});

						
						/*Fix #81540 to avoid multiple ajax requests (one per text change) of jquery remote option.*/
						var delay = (function(){
					  		var timer = 0;
					  		return function(callback, ms){
					    		clearTimeout (timer);
					    		timer = setTimeout(callback, ms);
					  		};
						})();
						//Fix in username, only remote field;
		        		$('#username').on({
					        focus: function () {
					        	$("#usuarioDto").validate().settings.onkeyup = false;
					        },
					        blur: function () {
					        	$("#usuarioDto").validate().settings.onkeyup =
					                $.validator.defaults.onkeyup;
					        },
					        keyup:function() {
					        	$("#username").validate().settings.onkeyup = false;
						    	delay(function(){
						    		$("#usuarioDto").validate().element("#username");
						    		$("#username").validate().settings.onkeyup = $.validator.defaults.onkeyup;
						       	}, 500 );
					        }
					    });



						$('#admin').click(
								function() {
									$('#institField')[this.checked ? "hide"
											: "show"]();

								});
					});
</script>
</head>
<body>

	<form:form commandName="usuarioDto" action="salvarUsuario"
		method="POST">

		<div class="span-21 contenidoPresentacion">

			<div class="span-21  last">
				<html:inputField label="Nombre de usuario" name="username"
					cssClass="campo" cssErrorClass="campoError"
					ctrCssClass="span-14" errorTextClass="fieldError" />
			</div>

			<div class="span-21  last">
				<html:inputField label="Contraseña" name="password" cssClass="campo"
					ctrCssClass="span-14"
					cssErrorClass="campoError" errorTextClass="fieldError" />
			</div>

			<div class="span-21">
				<div class="span-5">
					<label class="labelCampo" for="confirmPasswrd">Repita
						contraseña</label>
				</div>

				<div class="controls span-14 last">
					<form:input path="confirmPassword" cssClass="campo"
						id="confirmPassword" cssErrorClass="campoError" />
					<span class="help-inline" id="help-inline-confirmPassword">
						<form:errors path="matchPassword" cssClass="fieldError" />
					</span>
				</div>
			</div>

			<div class="span-21  last">
				<html:inputField label="Nombre" name="nombreCompleto"
					cssClass="campo" cssErrorClass="campoError"
					ctrCssClass="span-14" errorTextClass="fieldError" />
			</div>

			<div class="span-21  last">
				<html:inputField label="Apellidos" name="apellidos" cssClass="campo"
					ctrCssClass="span-14"
					cssErrorClass="campoError" errorTextClass="fieldError" />
			</div>

			<div class="span-21  last">
				<html:inputField label="E-mail" name="email" cssClass="campo"
					cssErrorClass="campoError" ctrCssClass="span-14" 
					errorTextClass="fieldError" />
			</div>

			<div class="span-21  last">
				<html:inputField label="Fono" name="telefono" cssClass="campo"
					cssErrorClass="campoError" ctrCssClass="span-14"
					errorTextClass="fieldError" />
			</div>

			<div class="span-21  last">
				<html:checkboxField label="¿Es administrador?" name="admin"
					cssClass="campo" cssErrorClass="campoError"
					ctrCssClass="span-14" errorTextClass="fieldError" />
			</div>

			<div class="span-21 last" id="institField"
				style="display:${esAdmin ? 'none' : 'block'}">
				<div class="span-5">
					<label class="labelCampo" for="grupos">Institución</label>
				</div>
				<div class="span-14">
					<form:select path="authorityId" id="authorityId" multiple="false"
						cssClass="campo" cssErrorClass="campoError">
						<option value="">----</option>
						<form:options items="${authorityDtoList}" itemLabel="authority"
							itemValue="id" />
					</form:select>
					<span class="help-inline" id="help-inline-authorityId"> <form:errors
							path="authorityId" cssClass="fieldError" />
					</span>
				</div>
			</div>

			<div class="span-5">
				<label class="labelCampo">¿Habilitado?</label>
			</div>

			<div class="span-12">
				<form:radiobutton path="valid" value="true" label="Sí"
					cssClass="campo" cssErrorClass="campoError" />
				<form:radiobutton path="valid" value="false" label="No"
					cssClass="campo" cssErrorClass="campoError" />
			</div>

			<div class="span-21 last">
				<form:errors path="valid" cssClass="fieldError" />
			</div>

			<div class="span-21 last buttons-container">
				<button type="submit" class="button create">Crear</button>
				<spring:url var="urlListado" value="/admin/usuarios" />
				<a href="${urlListado}" class="button secondary cancel">Cancelar</a>
			</div>

		</div>
	</form:form>
	</div>
</body>
</html>
