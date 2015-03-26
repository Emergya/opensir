<!DOCTYPE html> 

<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java"
	pageEncoding="UTF-8"%>

<html lang="es">

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