<%@ taglib uri="http://ckeditor.com" prefix="ckeditor"%>
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
<title>Nueva FAQ</title>
<spring:url value="/admin/faqs" var="urlListadoFaq" />
<spring:url var="salvarFaq" value="/admin/salvarFaq" />
<spring:url var="jqueryckeditor" value="/js/ckeditor/ckeditor.js" />
<spring:url var="jqueryckeditoradaptors"
	value="/js/ckeditor/adapters/jquery.js" />
<script type="text/javascript" src="${jqueryckeditor}"></script>
<script type="text/javascript" src="${jqueryckeditoradaptors}"></script>

</head>
<body>
	<form:form commandName="faqDto" action="${salvarFaq}" method="post">

		<div class="span-22 contenidoPresentacion">

			<div class="span-21 last">
				<!-- Modulo -->
				<div class="span-4">
					<label class="labelCampo">Módulo</label>
				</div>
				<div class="span-16 last">
					<form:select path="modulo" id="modulo" cssClass="campo"
						multiple="false" cssErrorClass="campoError">
						<option value="">----</option>
						<form:options items="${modulosfaq}" />
					</form:select>
					<span class="help-inline" id="help-inline-modulo"> <form:errors
							path="modulo" cssClass="fieldError" />
					</span>
				</div>
			</div>

			<div class="span-21 last">
				<!-- Habilitado Si/No -->
				<div class="span-4">
					<label class="labelCampo">Habilitada</label>
				</div>
				<div class="span-16 last">
					<form:radiobutton path="habilitada" value="true" label="Sí"
						cssClass="campo" cssErrorClass="campoError" />
					<form:radiobutton path="habilitada" value="false" label="No"
						cssClass="campo" cssErrorClass="campoError" />
					<form:errors path="habilitada" cssClass="fieldError" />
				</div>
			</div>

			<!-- Titulo Faq -->
			<div class="span-21 last">
				<div class="span-4">
					<label class="labelCampo">Título</label>
				</div>

				<div class="span-16 last">
					<form:input path="titulo" size="60" cssClass="campo"
						cssErrorClass="campoError" errorTextClass="fieldError" />
					<span class="help-inline" id="help-inline-titulo"
						style="display: block;"> <form:errors path="titulo"
							cssClass="fieldError" />
					</span>
				</div>
			</div>

			<div class="span-21 last">
				<!-- Respuesta -->
				<div class="span-4">
					<label class="labelCampo">Respuesta</label>
				</div>

				<div class="span-15 last">
					<form:textarea path="respuesta" id="respuesta"
						cssClass="campo jquery_ckeditor" cssErrorClass="campoError"
						errorTextClass="fieldError" />
					<span class="help-inline" id="help-inline-respuesta"> <form:errors
							path="respuesta" cssClass="fieldError" />
					</span>
				</div>
			</div>

			<!-- Botonera form -->
			<div class="span-21 last buttons-container">
				<button type="submit" class="button accept">Crear FAQ</button>
				<a href="${urlListadoFaq}" class="button secondary cancel">Cancelar</a>
			</div>
		</div>
	</form:form>
</body>
</html>
