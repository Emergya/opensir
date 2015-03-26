<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ page contentType="text/html;charset=UTF-8" language="java"
	pageEncoding="UTF-8"%>

<%@ page import="com.emergya.ohiggins.cmis.bean.Publicacion"%>

<spring:url value="/estudios/rechazarPublicacion" var="urlRechazar" />

<div id="popupRechazarPublicacion" style="display:none">
	<form:form commandName="publicacion" action="${urlRechazar}"
		method="post">
		
		<p>Va a rechazar la solicitud de publicación
				de un estudio. Por favor, indique a continuación el motivo del
				rechazo que será comunicado al solicitante.
		</p>
		<div>
			<form:hidden path="identifier" />
			<form:textarea path="comentario" id="comentario" cssClass="campo"
				cssErrorClass="campoError" cols="10" rows="5"
				cssStyle="width:390px;height:100px;" />
			<span class="help-inline" id="help-inline-comentario"> <form:errors
					path="comentario" cssClass="fieldError" />
			</span>
		</div>

		<!-- Botonera -->
		<div class="buttons-container">
			<!-- Rechazar -->
			<button type="submit" class="button secondary reject-red">Rechazar
				publicación</button>
			<!-- Cancelar -->
			<a href="javascript:void(0)" onclick="$('#popupRechazarPublicacion').dialog('close');" class="button cancel">Cancelar</a>
		</div>
	</form:form>
</div>