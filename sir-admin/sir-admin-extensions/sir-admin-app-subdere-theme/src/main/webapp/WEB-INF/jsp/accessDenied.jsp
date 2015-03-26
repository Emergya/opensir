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
	<!-- customCssHeader -->
<spring:url value="/css/blueprint/estiloArica.css" var="estiloAricaCss" />
<link rel="stylesheet" href="${estiloAricaCss}" type="text/css" />

<!-- /customCssHeader -->
</head>
<body>
<div class="pagina-error">
	<h2 class="error">
		No tiene los permisos necesarios para acceder a esta página.
	</h2>
</div>
</body>
</html>