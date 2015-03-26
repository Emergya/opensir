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
<c:choose>
	<c:when test="${metodo == '0'}">
		<spring:url value="/cartografico/actualizaCapaAuthorityShp"
			var="formUrl" />
	</c:when>
	<c:otherwise>
		<spring:url value="/cartografico/actualizaCapaAuthorityXls"
			var="formUrl" />
	</c:otherwise>
</c:choose>
</head>
<body>
	<form:errors path="uploadForm.*" cssClass="error" element="div" />
	
	<form:form action="${formUrl}" commandName="uploadForm"
		enctype="multipart/form-data" method="post">
		
		<input type="hidden" value="${id}" name="id" id="layerId" />
		<input type="hidden" value="${sessionKey}" name="sessionKey"
			id="sessionKey" />
		
		
		<c:if test="${correcto}">
			<!-- Acciones -->
			<div style="width: 70%; float: left; padding-left: 50px;">
				<c:if test="${metodo == '1' and not empty sameColumns}">
					<legend>Columnas a Actualizar</legend>
					<b>Las siguientes columnas serán actualizadas en la capa:</b>
					<br />
					<ul>
						<c:forEach items="${sameColumns}" var="sameColumn">
							<li><c:out value="${sameColumn}" /></li>
						</c:forEach>
					</ul>
				</c:if>

				<c:if test="${not empty addedColumns}">
					<legend>Columnas Añadidas</legend>
					<b>Las siguientes columnas serán añadidas a la capa:</b>
					<br />
					<ul>
						<c:forEach items="${addedColumns}" var="addedColumn">
							<li><c:out value="${addedColumn}" /></li>
						</c:forEach>
					</ul>
				</c:if>


				<c:if test="${not empty deletedColumns}">
					<legend>Columnas Eliminadas</legend>
					<b>Las siguientes columnas serán eliminadas de forma permanente
						de la capa:</b>
					<br />
					<ul>
						<c:forEach items="${deletedColumns}" var="deletedColumn">
							<li><c:out value="${deletedColumn}" /></li>
						</c:forEach>
					</ul>
				</c:if>
			</div>
		</c:if>

		<!-- Botonera form -->
		<div class="span-22 buttons-container">
			<c:if test="${correcto}">
				<button type="submit" class="button accept">Continuar</button>
			</c:if>

			<a class="button secondary cancel" href="${urlLayersInstitucion}">
				Cancelar </a>
		</div>

	</form:form>
</body>
</html>