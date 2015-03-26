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

<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<spring:url value="/faq" var="faqUrl" />
<spring:url value="/contacto" var="contactoUrl" />
<spring:url value="/css/blueprint" var="blueprintUrl" />
<link rel="stylesheet" href="${blueprintUrl}/screen.css" type="text/css"
	media="screen, projection" />
<link rel="stylesheet" href="${blueprintUrl}/print.css" type="text/css"
	media="print" />

<link rel="stylesheet" href="${blueprintUrl}/estilo.css" type="text/css" />
<script type="text/javascript"
	src="http://ajax.googleapis.com/ajax/libs/jquery/1.7.2/jquery.min.js"></script>

<jsp:include page="/WEB-INF/jsp/decorators/customCssHeader.jsp" flush="true" />

<spring:url value="/js/ohigginsApp.js" var="${ohigginsAppUrl}" />
<script type="text/javascript" src="${ohigginsAppUrl}"></script>
</head>
<html>
<body id="notFound"> 

	<div class="container">
		<!-- Cabecera -->
		<div id="header">
			<jsp:include page="/WEB-INF/jsp/decorators/header.jsp" flush="true" />
		</div>

		<!-- Contenido -->
		<div class="pagina-error">
			<h2 class="error">La página a la que intenta acceder no existe.</h2>
		</div>


		<!-- Footer -->
		<jsp:include page="WEB-INF/jsp/decorators/footer.jsp"></jsp:include>
	</div>

</body>
</html>
