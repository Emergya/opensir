<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ page contentType="text/html;charset=UTF-8" language="java"
	pageEncoding="UTF-8"%>


<html>
<head>
	
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
	<spring:url value="/faq" var="faqUrl"/>
	<spring:url value="/contacto" var="contactoUrl"/>
    <spring:url value="/css/blueprint" var="blueprintUrl" />
	<link rel="stylesheet" href="${blueprintUrl}/screen.css"
		type="text/css" media="screen, projection" />
	<link rel="stylesheet" href="${blueprintUrl}/print.css" type="text/css"
		media="print" />
	
	<link rel="stylesheet" href="${blueprintUrl}/estilo.css"
		type="text/css" />
	<script type="text/javascript" 
			src="http://ajax.googleapis.com/ajax/libs/jquery/1.7.2/jquery.min.js"></script>

    <spring:url value="/js/ohigginsApp.js" var="${ohigginsAppUrl}"/>
	<script type="text/javascript" 
			src="${ohigginsAppUrl}"></script>
   
</head>
<body>

	<div class="container">
		<!-- Cabecera -->
	    <div id="header">
	    	<jsp:include page="/WEB-INF/jsp/decorators/header.jsp" flush="true" />
	    </div>
	    
	    <!-- Contenido -->
	    <div id="content">
	    	<h2 class="accesoDenegado">
				Se ha producido un error inesperado <br> 
				Consulte con el Administrador
			</h2>
	    </div>
	    
	    
	    <!-- Footer -->
	    <div id="footer">
	    	<div class="span-24">
				<a href="${contactoUrl}/nuevoContacto" style="text-decoration: none;">
					<label class="pie enlacePiePagina" 
						style="border-right-width: 2px;
							   border-right-style: solid;
							   padding-right: 5px;
							   margin-left: 40%;
							   ">Contacto</label>
				</a>
				<a href="${faqUrl}/faqs" style="text-decoration: none;cursor:pointer;">
					<label class="pie enlacePiePagina">Preguntas frecuentes</label>
				</a>	
			</div>
			
			<!-- Pie de pagina -->
			<div class="span-24">
				<label class="pie" style="margin-left: 30%">Gobierno Regional Región del Libertador General Bernardo O'Higgins</label>	
			</div>
	    </div>
	</div>


</body>
</html>