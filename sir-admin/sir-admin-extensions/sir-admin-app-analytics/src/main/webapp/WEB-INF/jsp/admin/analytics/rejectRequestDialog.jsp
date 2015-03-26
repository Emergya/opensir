<%-- 
    Document   : rejectPublication
    Created on : 25-jun-2013, 11:48:48
    Author     : lroman
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib uri="http://displaytag.sf.net" prefix="display"%>
<%@ taglib prefix="html" tagdir="/WEB-INF/tags/html"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>

<spring:url var="rejectPublicationUrl" value="/admin/analitico/rechazarSolicitud"/>

<form:form commandName="datumRequest" action="${rejectPublicationUrl}"
           method="post">
    <p>
        Va a rechazar la solicitud de publicación
        del dato analítico estadístico '${datumRequest.name}'.  
        Por favor, indique a continuación el motivo del
        rechazo que será comunicado al solicitante:
    </p>
    <div>
        <form:hidden path="identifier" />
        <form:textarea path="requestAnswer" id="requestAnswer" cssClass="campo"
                       required="true"
                       cssErrorClass="campoError" cols="10" rows="5"
                       cssStyle="width:97%;height:100px;" />
        <span class="help-inline" id="help-inline-requestAnswer"> <form:errors
                path="requestAnswer" cssClass="fieldError" />
        </span>
    </div>

    <!-- Botonera -->
    <div class="buttons-container">
        <!-- Rechazar -->
        <button type="submit" class="button secondary reject">
            Rechazar publicación</button>
        <!-- Cancelar -->
        <a href="javascript:void(0)" onclick="$('#rejectDialog').dialog('close');" class="button cancel">
            Cancelar</a>
    </div>
</form:form>
