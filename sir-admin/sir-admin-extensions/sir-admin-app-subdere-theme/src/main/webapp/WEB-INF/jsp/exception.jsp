<!DOCTYPE html> 

<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java"
	pageEncoding="UTF-8"%>

<html lang="es">
<spring:url value="/img/favicon.ico" var="faviconUrl" />
<link rel="shortcut icon" href="${faviconUrl}">

<!-- customCssHeader -->
<spring:url value="/css/blueprint/estiloArica.css" var="estiloAricaCss" />
<link rel="stylesheet" href="${estiloAricaCss}" type="text/css" />

<!-- /customCssHeader -->

<body>

<div class="pagina-error">
	



	    	<h2 class="error">
				Se ha producido un error inesperado. Consulte con el Administrador.
			</h2>


	<div class="message">
    	<p>${exception.message}</p>
	</div>

    <!-- Exception: ${exception.message}.
		  	<c:forEach items="${exception.stackTrace}" var="stackTrace"> 
				${stackTrace} 
			</c:forEach>
	  	-->


</div>

</body>

</html>