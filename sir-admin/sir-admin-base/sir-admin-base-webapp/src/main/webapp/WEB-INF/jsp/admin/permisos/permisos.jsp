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
        <title>Permisos por tipo de Institución</title>
        <spring:url value="/admin/salvarPermiso" var="salvarPermiso" />
        <spring:url value="/admin/usuarios" var="urlListadoUsuarios" />
    </head>
    <body>

        <!-- Formulario -->
        <form:form commandName="permisoDto" action="${salvarPermiso}"
                   method="post">

            <div class="tiposYPermisos">
                <table class="tableScroll">
                    <thead>
                        <!-- Cabecera tabla -->
                        <tr>
                            <th>Permiso
                            </th>
                            <c:forEach items="${tiposAutoridad}" var="tipoautoridad">
                                <th>${tipoautoridad.name}
                                </th>
                            </c:forEach>
                        </tr>

                    </thead>

                    <tbody>

                        <!-- Contenido tabla -->
                        <c:forEach items="${permisos}" var="permiso">
                            <tr>
                                <td style="width:12" class="primera-cebreada">${permiso.nombre}</td>
                                <c:forEach items="${tiposAutoridad}" var="tipoautoridad"
                                           varStatus="loopStatus">
                                    
                                    <c:choose>
                                        <c:when test="${loopStatus.last}">
                                            <td class="ultima-cebreada">
                                        </c:when>
                                        <c:otherwise>
                                            <td>
                                        </c:otherwise>
                                    </c:choose>
                                    
                                        <!-- Check --> 
                                        <form:checkbox path="seleccionados"
                                            value="${tipoautoridad.id}/${permiso.id}" cssClass="campo"
                                            cssErrorClass="campoError" />
                                    </td>
                                </c:forEach>
                            </tr>
                        </c:forEach>
                    </tbody>
                </table>
            </div>
            <!-- Botonera form -->
            <div class="buttons-container span-21 last">
                <button type="submit" class="button save">Guardar cambios</button>
                <a href="${urlListadoUsuarios}" class="button secondary cancel">Cancelar</a>
            </div>

        </form:form>
    </body>
</html>