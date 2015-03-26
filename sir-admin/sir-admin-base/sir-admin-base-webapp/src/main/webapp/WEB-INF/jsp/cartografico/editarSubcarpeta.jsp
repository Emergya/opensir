<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib uri="http://displaytag.sf.net" prefix="display"%>
<%@ page contentType="text/html;charset=UTF-8" language="java"
	pageEncoding="UTF-8"%>

<html>
<head>

<title>
	<c:forEach var="parentName" items="${sessionScope.parentNames}" varStatus="loop">
						${parentName} &gt; 
	</c:forEach>
	Editar Carpeta 
</title>

<spring:url var="saveSubcarpeta" value="/cartografico/saveSubcarpeta" />
</head>
<body>
	<!-- Formulario -->
	<form:form commandName="folderDto" action="${saveSubcarpeta}"
		method="post" onsubmit="return validateForm('folderDto')">
		<form:hidden path="id" id="id" />

		<div style="height: 35px">
			<div class="span-20">
				<!-- Panel izdo. -->
				<div class="span-10">
					<div class="span-5">
						<label class="labelCampo">Nombre canal</label>
					</div>
					<div class="span-5 last">
						<form:input path="name" cssClass="campo" required="true"
							minlength="3" cssErrorClass="campoError"
							errorTextClass="fieldError" />
						<span class="help-inline" style="width: 25em; float: left;"
							id="help-inline-name"> <form:errors path="name"
								cssClass="fieldError" />
						</span>
					</div>
				</div>

				<!-- Panel derecho -->
				<div class="span-10 last">
					<!-- Habilitado -->

					<div class="span-5">
						<label class="labelCampo">Habilitado</label>
					</div>
					<div class="span-5 last radioGroup">

						<form:radiobutton path="enabled" value="true" label="Sí"
							cssClass="campo" cssErrorClass="campoError" />

						<form:radiobutton path="enabled" value="false" label="No"
							cssClass="campo" cssErrorClass="campoError" />
					</div>
					<div class="span-3 last">
						<form:errors path="enabled" cssClass="fieldError" />
					</div>
				</div>
			</div>
		</div>

		<!-- Selector de capas -->
		<jsp:include page="editorCapasCarpeta.jsp"></jsp:include>

		<!-- Botonera -->
		<div class="buttons-container">

			<form:button class="button submit">Guardar</form:button>

			<spring:url var="urlListado" value="/cartografico/canales" />
			<a href="${urlListado}" class="button secondary cancel">Cancelar</a>
		</div>

	</form:form>
</body>
</html>