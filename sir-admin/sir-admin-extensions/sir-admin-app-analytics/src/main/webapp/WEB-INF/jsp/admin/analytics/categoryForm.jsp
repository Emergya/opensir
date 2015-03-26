<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib uri="http://displaytag.sf.net" prefix="display"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<html>
<head>
<title>${empty analitycsCategoryDto.id?"Nueva categoría":"Editar
	categoría"}</title>
<spring:url value="/admin/analitico/guardarCategoria"
	var="saveCategoryUrl"></spring:url>
<spring:url value="/admin/analitico/categorias" var="cancelUrl" />
</head>
<body>
	<form:form commandName="analyticsCategoryDto"
		action="${saveCategoryUrl}" method="post"
		onsubmit="return validateForm('folderDto')">
		<form:hidden path="id" id="id" />
		<!-- Panel izdo. -->
		<div class="span-10">
			<div class="span-10">
				<div class="span-5">
					<label class="labelCampo">Nombre categoría</label>
				</div>
				<div class="span-5 last">
					<form:input path="name" cssClass="campo" required="true"
						minlength="3" cssErrorClass="campoError"
						errorTextClass="fieldError" />
					<div class="help-inline" style="width: 25em; float: left;"
						id="help-inline-name">
						<form:errors path="name" cssClass="fieldError" />
					</div>
				</div>
			</div>

			<!-- Panel derecho -->
			<div class="span-10 last">
				<!-- Habilitado -->
				<div class="span-5">
					<label class="labelCampo">Habilitado</label>
				</div>
				<div class="span-5 last">
					<form:checkbox path="enabled" />
				</div>
				<div class="span-3 last">
					<form:errors path="enabled" cssClass="fieldError" />
				</div>
			</div>
		</div>
		<!-- Botonera -->
		<div class="buttons-container clear">
			<form:button class="button submit">Guardar</form:button>
			<a href="${cancelUrl}" class="button secondary cancel">Cancelar</a>
		</div>
	</form:form>
</body>