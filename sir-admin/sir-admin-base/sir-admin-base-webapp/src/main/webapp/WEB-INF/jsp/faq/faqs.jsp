<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ page
	import="org.springframework.security.core.context.SecurityContext"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="html" tagdir="/WEB-INF/tags/html"%>
<%@ taglib prefix="ajax" tagdir="/WEB-INF/tags/ajax"%>
<%@ taglib uri="http://displaytag.sf.net" prefix="display"%>
<%@ page contentType="text/html;charset=UTF-8" language="java"
	pageEncoding="UTF-8"%>

<html>
<head>
    <spring:url value="/contacto/nuevoContacto" var="contactoUrl" />
    <link rel="stylesheet"
        href="http://code.jquery.com/ui/1.9.0/themes/base/jquery-ui.css" />
    <script type="text/javascript">
        $(document).ready(function () {
            $("#accordion").accordion({autoHeight: false,heightStyle: "content"});
        });
    </script>
</head>
<body>
	<div id="faq-page" class="pestanas">
		<div id="contentfaqs" class="tab_container contenido">
			<div class="titulo">
				<h4>Preguntas Frecuentes (FAQ)</h4>
			</div>
			<!-- /titulo -->
			<div class="pestanaimg"></div>

			<div id="faq-contents">
				<div style="margin-bottom: 15px;">
					<label>Esta sección despliega aquellas preguntas más
						frecuentes relacionadas con el funcionamiento de la plataforma así
						como dudas que al usuario le puedan surgir durante el uso de la
						misma.</label> <label>Si no encuentra la información que está
						buscando, puede utilizar el <a href="${contactoUrl}">formulario
							de contacto</a> para trasladarnos cualquier duda.
					</label>
				</div>
				<!-- Resultados -->
				<div id="accordion">
					<c:forEach items="${listaFaqs}" var="faq">
						<h4>${faq.titulo}</h4>
						<div>
							<p>${faq.respuesta}</p>
						</div>
					</c:forEach>
				</div>
				<!-- /accordion -->
			</div>
			<!-- /faq-contents -->
		</div>
		<!-- /contentsfaqs -->
	</div>
	<!-- /pestanas -->
</body>
</html>
