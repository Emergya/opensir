<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<c:choose>
	<c:when test="${param.msg eq 'success' }">
		<div class="success">La operaci&oacute;n se ha realizado
			correctamente.</div>
	</c:when>
	<c:when test="${param.msg eq 'errorDeleteInstitution' }">
		<div class="error">No se puede eliminar la instituci&oacute;n.
			A&uacute;n tiene usuarios asociados.</div>
	</c:when>
	<c:when test="${param.msg eq 'errorDeleteAdmin' }">
		<div class="error">No se puede eliminar al &uacute;ltimo usuario
			administrador.</div>
	</c:when>
	<c:otherwise>
		<c:if test="${not empty param.msg}">
			<div class="notice">${param.msg }</div>
		</c:if>
	</c:otherwise>
</c:choose>
