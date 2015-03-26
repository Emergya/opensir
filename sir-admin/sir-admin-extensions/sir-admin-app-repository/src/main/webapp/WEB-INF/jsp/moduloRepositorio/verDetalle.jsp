<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ page contentType="text/html;charset=UTF-8" language="java"
	pageEncoding="UTF-8"%>

<head>

</head>
<html>
<body>
	<form class="span-10 last">
		<div class="span-10 last">
			<div class="span-4">
				<label class="labelCampo">Nombre del Estudio:</label>
			</div>
			<div class="span-6 last">${nombre}</div>
		</div>
		
		<div class="span-10 last">
			<div class="span-4">
				<label class="labelCampo">Institución:</label>
			</div>
			<div class="span-6 last">${institucion}</div>
		</div>

		<div class="span-10 last">
			<div class="span-4">
				<label class="labelCampo">Sector:</label>
			</div>
			<div class="span-6 last">${sector}</div>
		</div>
		
		<div class="span-10 last">
			<div class="span-4">
				<label class="labelCampo">Nivel Territorial:</label>
			</div>
			<div class="span-6 last">${nivelT}</div>
		</div>

		<div class="span-10 last">
			<div class="span-4">
				<label class="labelCampo">Año:</label>
			</div>
			<div class="span-6 last">${anyo}</div>
		</div>

		<div class="span-10 last">
			<div class="span-4">
				<label class="labelCampo">Autor:</label>
			</div>
			<div class="span-6 last">${autor}</div>
		</div>

		<div class="span-10">
			<label class="labelCampo">Resumen:</label>
		</div>
		<div class="span-10 last">
			<textarea readonly="readonly" class="campo campoDisabled">${resumen}</textarea>
		</div>
	</form>

	<spring:url value="/repositorioEstudios/" var="moduloRepositorioUrl" />
	<div class="buttons-container">
		<a class="button download" href="${moduloRepositorioUrl}/download/${identificador}">
			Descargar</a>
	</div>
</body>
</html>
