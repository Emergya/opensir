<%-- 
    Document   : pending
    Created on : 24-jun-2013, 17:16:11
    Author     : lroman
--%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib uri="http://displaytag.sf.net" prefix="display"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="html" tagdir="/WEB-INF/tags/html"%>

<!DOCTYPE html>
<html>
    <head>
        <spring:url var="downloadUrl" value="/admin/analitico/descarga"/>
        <spring:url var="viewRequestUrl" value="/admin/analitico/verSolicitud"/>
        <spring:url var="prepareRejectionUrl" value="/popup/analitico/verRechazo"/>
        <spring:url var="viewAnswerUrl" value="/popup/analitico/verRespuesta"/>
        <spring:url var="acceptRequestUrl" value="/admin/analitico/aceptarPublicacion"/>
        
        <spring:url var="jsBaseUrl" value="/js"/>
        
        <script src="${jsBaseUrl}/analyticsAdmin.js" type="text/javascript"></script>
        
        <title>Solicitudes de Publicación</title>
    </head>
    <body>
        <display:table name="requests" id="datumRequest" uid="datumRequest" requestURI=""
                       sort="external">

            <display:column title="Nombre" property="name"
                            class="primera-cebreada">

            </display:column>
            <display:column title="Categoría" property="categoryName">

            </display:column>
            <display:column title="Descripción" class="ellipsedColumn">
                <span title="${datumRequest.description }">${datumRequest.description}</span>
            </display:column>
            <display:column title="Tipo">
                <c:choose>
                    <c:when test="${datumRequest.update}">
                        Actualización "${datumRequest.updatedDataName}"
                    </c:when>
                    <c:otherwise>
                        Nueva publicación
                    </c:otherwise>
                </c:choose>
            </display:column>
            <c:choose>
                <c:when test="${IS_ADMIN }">
                    <display:column title="Fecha solicitud" class="ultima-cebreada">
                        <fmt:formatDate value="${datumRequest.requestDate.time}" pattern="dd/MM/yy HH:mm"/>
                    </display:column>

                    <display:column class="sin-cebreado iconColumn"
                                    title="Ver" headerClass="iconColumn">
                        <a href="${viewRequestUrl }/${datumRequest.identifier}" class="icon textless view enlace-ver"
                           title="Ver detalles">Ver</a>
                    </display:column>
                    <display:column class="sin-cebreado iconColumn"
                                    title="Descargar" headerClass="iconColumn">
                        <a href="${downloadUrl}/${datumRequest.identifier}" class="icon textless download"
                           title="Descargar documento">Descargar</a>
                    </display:column>

                    <display:column class="sin-cebreado iconColumn"
                                    title="Publicar" headerClass="iconColumn">
                        <a href="javascript:analyticsAdmin.acceptRequest('${acceptRequestUrl}/${datumRequest.identifier}')"
                           class="icon textless accept ok publish"
                           title="Aceptar publicación">Publicar</a>
                    </display:column>
                    <display:column class="sin-cebreado iconColumn"
                                    title="Descargar" headerClass="iconColumn">
                        <a href="javascript:loadContentDialog('rejectDialog', 'Rechazar solicitud', '${prepareRejectionUrl}/${datumRequest.identifier}', 420)"
                           class="icon textless cancel reject"
                           title="Rechazar publicación">Rechazar</a>
                    </display:column>
                </c:when>
                <c:otherwise>
                    <display:column title="Fecha solicitud">
                        <fmt:formatDate value="${datumRequest.requestDate.time}" pattern="dd/MM/yy HH:mm"/>
                    </display:column>

                    <display:column class="ultima-cebreada" title="Estado">
                        <a href="javascript:loadContentDialog('stateDialog', 'Respuesta a la solicitud', '${viewAnswerUrl}/${datumRequest.identifier}', 420)"
                           title="Ver estado">${datumRequest.state}</a>
                    </display:column>
                </c:otherwise>
            </c:choose>
        </display:table>

        <c:choose>
            <c:when test="${IS_ADMIN }">
                <div id="rejectDialog"></div>
            </c:when>
            <c:otherwise>
                <div id="stateDialog"></div>
            </c:otherwise>
        </c:choose>
    </body>
</html>
