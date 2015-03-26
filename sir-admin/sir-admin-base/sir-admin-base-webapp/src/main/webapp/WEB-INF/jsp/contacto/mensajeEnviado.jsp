<%@ taglib uri="http://ckeditor.com" prefix="ckeditor"%>
<%@page import="com.emergya.ohiggins.security.OhigginsUserDetails"%>
<%@page import="org.springframework.security.core.context.SecurityContext"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ page contentType="text/html;charset=UTF-8" language="java"
	 pageEncoding="UTF-8"%>

<html>
    <head>
	<title>Contacto</title>

	<spring:url var="returnUrl" value="/contacto/nuevoContacto" />
	
    </head>
    <body>
	<div id="faq-page" class="pestanas">
	    <div id="contentfaqs" class="tab_container contenido">
		<div class="titulo">
		    <h4>Formulario de Contacto</h4>
		</div>	
		<div class="pestanaimg"></div>

		<div id="faq-contents">
		    <div style="text-align: center;">
			<c:choose>
			    <c:when test="${isEnviado == true}">
				<p style="font-weight: bold;font-size: 150%;">

				    <spring:message code="contact.sentMsgTitle_message" />

				</p>
				<p>
				    <spring:message code="contact.sentMsg_message" />
				    ${textoError}
				</p>			
			    </c:when>
			    <c:otherwise>
				<p style="font-weight: bold;font-size: 150%;">
				    <spring:message code="contact.didntSendMsgTitle_message" />
				</p>

				<p>
				    <spring:message code="contact.didntSendMsg_message" />
				</p>
			    </c:otherwise>
			</c:choose>
			<p>
			    <a href="${returnUrl}" class="button">Volver</a>
			</p>
		    </div>

		</div>
	    </div>
	    <!-- /contentsfaqs -->
	</div>
    </body>
</html>