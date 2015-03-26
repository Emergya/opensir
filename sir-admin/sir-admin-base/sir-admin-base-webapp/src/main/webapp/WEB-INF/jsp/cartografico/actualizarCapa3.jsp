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
</head>
<body>
	<c:if test="${correcto}">
		<div class="success">La actualización se ha realizado
			correctamente.</div>
	</c:if>
	<form:errors path="uploadForm.*" cssClass="error" element="div" />

	<!-- Formulario -->
	<form:form>

		<!-- Acciones -->
		<div style="width: 70%; padding-left: 50px; padding-top: 25px;">
			<legend>Resultado de la actualización:</legend>
			<ul>
				<li><c:out value="${addedColumnsNum}" /> Columnas Añadidas</li>
				<li><c:out value="${deletedColumnsNum}" /> Columnas Eliminadas</li>
				<c:if test="${metodo == '1'}">
					<li><c:out value="${updatedRowsNum}"/> Filas actualizadas</li>
					<li><c:out value="${unprocesseRowsNum}" /> Filas no
						procesadas del fichero XLS</li>
				</c:if>
			</ul>
		</div>


		<%--contenidoPresentacion --%>
		<div class="span-22" style="margin-left: 2em;">
			<input type="hidden" value="${id}" name="id" id="layerId" />
		</div>

		<!-- Botonera form -->
		<div class="span-22 buttons-container">
			<a class="button accept" href="${urlLayersInstitucion}">
				Continuar </a>
		</div>
	</form:form>
</body>
</html>