<%-- 
    Document   : stateDialog
    Created on : 25-jun-2013, 13:39:16
    Author     : lroman
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib uri="http://displaytag.sf.net" prefix="display"%>
<%@ taglib prefix="html" tagdir="/WEB-INF/tags/html"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>


<spring:url var="changeStateUrl" value="/admin/analitico/cambiaEstadoRespuesta"/>
<spring:url var="editionUrl" value="/admin/analitico/editarSolicitudPublicacion"/>

<form:form commandName="datumRequest" action="${changeStateUrl}" method="post">
    <form:hidden path="identifier"/>
    <div class="box-status">
        <p>
            El dato analítico estadístico con título '${datumRequest.name}' solicitado a 
            publicación por la institución ${datumRequest.institutionName} el

            <fmt:formatDate pattern="dd/MM/yy 'a las' HH:mm" value="${datumRequest.requestDate.time}"/>

            ${stateLabel}.
        </p>		
        <c:if test="${datumRequest.state == 'RECHAZADA'}">
            <div class="span-4">
                <label class="labelCampo" style="text-align:left">Causas Rechazo:</label>
            </div>
            <form:textarea path="requestAnswer" readonly="true" 
                           class="campoDisabled"  style="height: auto;min-height:100px;"/>
        </c:if>

        <div class="buttons-container">	
            <c:choose>
                <c:when test="${datumRequest.state eq 'RECHAZADA'}">
                    <!-- input type="submit" name="action" value="editar" / -->
                    <a class="button edit" value="Editar"
                       href='${editionUrl}/${datumRequest.identifier}'>Editar Solicitud</a>
                    <!-- input type="submit" name="action" value="eliminar" class="enlace-borrar" / -->
                    <button type="submit" name="action" class="button secondary reject">Eliminar</button>
                </c:when>
                <c:otherwise>


                    <!-- input type="submit" name="action" value="Aceptar" / -->
                    <button type="submit" name="action" class="button accept">Aceptar</button>
                </c:otherwise>
            </c:choose>
        </div>
    </div>
</form:form>
