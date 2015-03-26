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
<title>Actualizar Capa '${layerLabel}'</title>
<spring:url value="/cartografico/layersAuthority"
	var="urlLayersInstitucion" />
<spring:url value="/cartografico/actualizaCapaAuthority" var="formUrl" />
</head>
<body>
	<c:if test="${correcto}">
		<div class="success">La actualización se ha realizado
			correctamente.</div>
	</c:if>
	
	<form:errors path="uploadForm.*" cssClass="error" element="div" />

	<!-- Formulario -->
	<form:form action="${formUrl}" commandName="uploadForm"
		enctype="multipart/form-data" method="post">

		<!-- Acciones -->
		<fieldset style="width: auto">
			<legend>Método</legend>
			<div style="float: left; width: 100%">
				<input id="methodSHP" type="radio" name="metodo" value="0" ${metodo eq '0' ? 'checked' : ''}>
				<label for="methodSHP">Archivo
				SHP (comprimido en ZIP)</label> 
				<input id="methodXLS" type="radio" name="metodo" value="1" ${metodo eq '1' ? 'checked' : ''}>
				<label for="methodXLS">CSV/Excel</label>
			</div>
		</fieldset>

		<%--contenidoPresentacion --%>
		<div style="padding: 0 2em;">
			<input type="hidden" value="${id}" name="id" id="layerId" />
			<div>
				<label class="labelArriba" for="file">Archivo</label>
			</div>
			<div class="fileinputs">
				<input class="file span-16 campo" id="file" type="file" name="file"
					accept="application/zip,application/vnd.ms-excel" /> <span
					class="help-inline" id="help-inline-file">
				</span>
			</div>
		</div>

		<!-- Botonera form -->
		<div class="span-22 buttons-container">
			<button type="submit" class="button accept">Continuar</button>

			<a class="button secondary cancel" href="${urlLayersInstitucion}">
				Cancelar </a>

		</div>
	</form:form>
</body>
</html>