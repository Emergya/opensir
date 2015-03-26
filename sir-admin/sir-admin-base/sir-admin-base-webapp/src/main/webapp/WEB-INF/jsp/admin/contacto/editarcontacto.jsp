<%@ taglib uri="http://ckeditor.com" prefix="ckeditor"%>
<%@page import="com.emergya.ohiggins.security.OhigginsUserDetails"%>
<%@page
	import="org.springframework.security.core.context.SecurityContext"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ page contentType="text/html;charset=UTF-8" language="java"
	pageEncoding="UTF-8"%>

<html>
<head>
<title>Datos Contacto</title>
<spring:url var="urlImages" value="/img" />
<spring:url value="¿Eliminar Contacto?" var="mensajeEliminarContacto" />

<!-- Scripts js utilizados -->

<spring:url var="jqueryckeditor" value="/js/ckeditor/ckeditor.js" />
<spring:url var="jqueryckeditoradaptors"
	value="/js/ckeditor/adapters/jquery.js" />
<script type="text/javascript" src="${jqueryckeditor}"></script>
<script type="text/javascript" src="${jqueryckeditoradaptors}"></script>

<spring:url value="/admin/contactos" var="urlListadoContacto" />
<spring:url value="/admin/leerContacto" var="urlLeerContacto" />
<spring:url var="salvarContacto" value="/contacto/salvarContacto" />
<spring:url var="eliminarContacto"
	value="/admin/borrarContacto/${contactoDto.id}" />

</head>
<body>
	<form:form commandName="contactoDto" action="${eliminarContacto}"
		method="post">

		<div class="span-21 last">
			<!-- Email -->
			<div class="span-4">
				<label class="labelCampo">Correo electrónico</label>
			</div>
			<div class="span-16 last">
				<form:input path="email" maxlength="120"
					cssClass="campo campoDisabled" cssErrorClass="campoError"
					readonly="true" disabled="true" />
				<span class="help-inline" id="help-inline-email"
					style="display: block;"> <form:errors path="email"
						cssClass="fieldError" />
				</span>
			</div>
		</div>
		<!-- /email -->

		<div class="span-21 last">
			<!-- Nombre y apellidos -->
			<div class="span-4">
				<label class="labelCampo">Nombre y apellidos</label>
			</div>
			<div class="span-16 last">
				<form:input path="nombre" maxlength="120"
					cssClass="campo campoDisabled" cssErrorClass="campoError"
					readonly="true" disabled="true" />
				<span class="help-inline" id="help-inline-nombre"
					style="display: block;"> <form:errors path="nombre"
						cssClass="fieldError" />
				</span>
			</div>

		</div>

		<div class="span-21 last">

			<!-- Titulo -->
			<div class="span-4">
				<label class="labelCampo">Título</label>
			</div>

			<div class="span-16 last">
				<form:input path="titulo" maxlength="120"
					cssClass="campo campoDisabled" cssErrorClass="campoError"
					readonly="true" disabled="true" />
				<span class="help-inline" id="help-inline-titulo" style=""> <form:errors
						path="titulo" cssClass="fieldError" />
				</span>
			</div>

		</div>


		<div class="span-21 last">
			<!-- fecha y hora -->
			<div class="span-4">
				<label class="labelCampo">Fecha y hora</label>
			</div>
			<div class="span-16 last">
				<%--<form:input path="fechaCreacion"  
						cssClass="campo" 
	                    cssErrorClass="campoError"
	                    errorTextClass="fieldError"
						/>--%>
				<input type="text" class="campo campoDisabled"
					value="<fmt:formatDate value="${contactoDto.fechaCreacion}" pattern="dd/MM/yyyy HH:mm"/>"
					readonly="readonly" disabled="disabled" /> <span
					class="help-inline" id="help-inline-fechaCreacion"
					style="display: block;"> <form:errors path="fechaCreacion"
						cssClass="fieldError" />
				</span>
			</div>
		</div>

		</fieldset>

		<fieldset class="span-21">
			<legend>Contenido</legend>
			<!-- descripcion -->
			<%--<div class="span-4">
	        				<label class="labelCampo">Descripción</label>
	        			</div>--%>
			<div>
				<form:textarea path="descripcion" id="descripcion"
					cssClass="campo campoDisabled" cssErrorClass="campoError"
					readonly="true" disabled="true" />
				<span class="help-inline" id="help-inline-descripcion"> <form:errors
						path="descripcion" cssClass="fieldError" />
				</span>
			</div>
		</fieldset>

		<!-- Botonera form -->
		<div class="buttons-container">
			<button type="submit"
				onclick="return confirm('${mensajeEliminarContacto}')"
				class="button reject">Eliminar</button>

			<a href="${urlLeerContacto}/${contactoDto.id}"
				class="button secondary cancel">Cancelar</a>
		</div>
	</form:form>
</body>
</html>