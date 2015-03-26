<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<!-- Vista padre: SolicitudesPublicacion.jsp -->

<spring:url var="urlImages" value="/img" />
<spring:url value="¿Eliminar solicitud publicación de capa?"
	var="mensajeEliminarSolicitudPublicacionCapa" />
<spring:url var="modificarEstado"
	value="/cartografico/editar/${estado}/${identificador}" />

<form:form action="${modificarEstado}" method="post">
	<p style="margin-top: 0.5em">
		${texto}
	</p>
	<c:if test="${!aceptar}">
		<textarea readonly="readonly" class="campoDisabled"
			style="height: auto; min-height: 100px;">${comentario}</textarea>
	</c:if>

	<div class="buttons-container">
		<c:choose>
			<c:when test="${aceptar}">
				<button type="submit" name="action" class="button"
					value="aceptar">Aceptar</button>
			</c:when>
			<c:otherwise>
				<button type="submit" name="action" class="button edit"
					value="Editar">Editar</button>
				<button type="submit" name="action"
					class="button secondary reject-red" value="Eliminar"
					onclick="return mostrarMensajeConfirmacion('¿Eliminar la solicitud publicación de capa?');">Eliminar</button>

			</c:otherwise>
		</c:choose>
	</div>
</form:form>
