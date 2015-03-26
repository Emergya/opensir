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
<spring:url var="publicarLayer"
	value="/admin/cartografico/publicarRequestLayer" />
</head>

<body>
	<form:form commandName="layerPublishRequestDto"
		action="${publicarLayer}" method="post">

		<form:hidden path="id" />
		
		<c:if test="${updatedLayerDeleted}">
			<div class="span-9">
				<p>La capa que el usuario pretendía actualizar fue borrada. Puede actualizar otra capa o publicarla como nueva.</p>
			</div>
		</c:if>

		<div class="span-9">
			<!-- Listado de carpetas -->
			<div class="span-3">
				<label class="labelCampo">Carpetas</label>
			</div>
			<div class="span-6 last">
				<form:select path="carpeta" id="carpeta" cssClass="campo"
					data-placeholder="Click para seleccionar"
					cssErrorClass="campoError">
					<option value=""></option>
					<c:forEach items="${layerTree}" var="ltt">
						<c:choose>
							<c:when test="${ltt.tipo eq 'folder'}">
								<c:choose>
									<c:when test="${ltt.node.id eq layerPublishRequestDto.carpeta}">
										<option value="${ltt.node.id}"
											style="margin-left: ${ltt.nivel * 25}px;" selected="true">
											${ltt.node.name}</option>
									</c:when>
									<c:otherwise>
								<option value="${ltt.node.id}"
									style="margin-left: ${ltt.nivel * 25}px;">
									${ltt.node.name}</option>
									</c:otherwise>
								</c:choose>
							</c:when>
						</c:choose>
					</c:forEach>
				</form:select>
				<span class="help-inline" id="help-inline-carpeta"> <form:errors
						path="carpeta" cssClass="fieldError"></form:errors>
				</span>
			</div>
		</div>
		<div class="span-9">
			
			<div class="span-3">
				<label class="labelCampo">Tipo de solicitud</label>
			</div>
			<div class="span-6 last">
			
					<form:radiobutton path="accionEjecutar" value="PUBLISH_NEW"
					onclick="publishRequests.toggleAction('PUBLISH_NEW',false);" />
						Nueva
					<form:radiobutton path="accionEjecutar" value="PUBLISH_UPDATE"
					onclick="publishRequests.toggleAction('PUBLISH_UPDATE',false);" />
						Actualización
			</div>
		</div>

		<div class="span-9" id="updatedLayerRow"
			style="display: ${layerPublishRequestDto.accionEjecutar eq 'PUBLISH_UPDATE' ? 'block' : 'none'}">
		
			<div class="span-3">
				<label class="labelCampo">Capa a actualizar</label>
			</div>
			<div class="span-6 last">
				<form:select path="updatedLayerId" id="updatedLayerId"
					data-placeholder="Click para seleccionar" multiple="false"
					cssClass="campo" cssErrorClass="campoError">
					<option value=""></option>
					<form:options items="${layersActualizar}" itemLabel="layerLabel"
						itemValue="id" />
				</form:select>
				<span class="help-inline" style="float: left;"
					id="help-inline-updatedLayerId"> <form:errors
						path="updatedLayerId" cssClass="fieldError" />
				</span>
			</div>
		</div>
		<div class="span-9" id="desiredNameRow"
			style="display: ${layerPublishRequestDto.accionEjecutar eq 'PUBLISH_NEW' ? 'block' : 'none'}">
			<div class="span-3">
				<label class="labelCampo">Nombre capa nueva</label>
			</div>
			<div class="span-6 last">
				<form:input path="nombredeseado" maxlength="120" id="nombredeseado"
					cssClass="campo" cssErrorClass="campoError"
					errorTextClass="fieldError" />
				<span class="help-inline" style="float: left;"
					id="help-inline-nombredeseado"> <form:errors
						path="nombredeseado" cssClass="fieldError" />
				</span>
			</div>
		</div>

		<!-- Botonera -->
		<div class="span-9 buttons-container">

			<button type="submit" class="button request">Autorizar 
				Publicación</button>
			<a class="button secondary cancel" href="javascript:void(0)"
				onclick="$('#acceptDialog').dialog('close')">Cancelar</a>
		</div>
	</form:form>
</body>
</html>
