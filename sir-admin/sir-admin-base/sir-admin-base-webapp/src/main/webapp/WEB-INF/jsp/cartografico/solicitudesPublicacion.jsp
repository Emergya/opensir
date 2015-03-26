<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib uri="http://displaytag.sf.net" prefix="display"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<html>
    <head>
        <title>Solicitudes de Publicación</title>
        <spring:url var="urlShowStatus" value="/cartografico/estado" />

        <script type="text/javascript">
            function mostrarEstado(url, estado) {
                var title = "";
                switch (estado) {
                    case "PENDIENTE":
                        title = "Pendiente de publicación";
                        break;
                    case "RECHAZADA":
                        title = "Solicitud rechazada";
                        break;
                    case "ACEPTADA":
                    case "LEIDA":
                        title = "Solicitud aceptada";
                }
                loadContentDialog("requestStatusDialog", title, url);
            }
        </script>

    </head>
    <body>
        <display:table name="layerPublish" id="item" class="tableScroll">
            <display:column title="Capa" property="sourceLayerName"
                            class="primera-cebreada">
            </display:column>

            <display:column title="Actualización">
                <c:choose>
                    <c:when test="${item.update}">
                        Sí
                    </c:when>
                    <c:otherwise>
                        No
                    </c:otherwise>
                </c:choose>
            </display:column>

            <display:column title="Capa actual">
                ${item.updatedLayerPath}
            </display:column>

            <display:column title="Fecha solicitud">
                <fmt:formatDate value="${item.fechasolicitud}"
                                pattern="dd/MM/yyyy HH:mm" />
            </display:column>

            <display:column title="Fecha respuesta">
                <fmt:formatDate value="${item.fecharespuesta}"
                                pattern="dd/MM/yyyy HH:mm" />
            </display:column>

            <!-- Ajax -->
            <display:column title="Estado"
                            class="ultima-cebreada">
                <a href="javascript:void(0)"
                   onclick="mostrarEstado('${urlShowStatus}/${item.id}', '${item.estado }')">
                    ${item.estado } </a>
                </display:column>
            </display:table>

        <div id="requestStatusDialog"></div>
    </body>
</html>
