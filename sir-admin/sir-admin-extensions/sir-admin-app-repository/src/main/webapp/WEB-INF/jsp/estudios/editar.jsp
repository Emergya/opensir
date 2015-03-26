<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib uri="http://displaytag.sf.net" prefix="display"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<%@ page import="com.emergya.ohiggins.cmis.bean.Publicacion"%>
<head>
    <title>Solicitud de Publicación</title>
    <spring:url var="urlListadoPendientes" value="/estudios/pendientes" />
    <spring:url var="urlListadoMias" value="/estudios/mias" />

    <script type="text/javascript">
        $(document)
                .ready(
                function() {

                    $.validator
                            .addMethod(
                            "alphanumeric",
                            function(value, element) {
                                return this.optional(element)
                                        || /(?!^[0-9]*$)(?!^[a-zA-Z]*$)^([a-zA-Z0-9]{3,60})$/i
                                        .test(value);
                            },
                            "Debe contener números y letras y tener una longitud mínima de 3.");

                    $("#publicacion").validate({
                        errorClass: "campoError",
                        validClass: "campo",
                        rules: {
                            file: {
                                required: true
                            },
                            anyo: {
                                required: true
                            },
                            sector: {
                                required: true
                            },
                            nivelTerritorial: {
                                required: true
                            },
                            name: {
                                required: true,
                                minlength: 4,
                                maxlength: 100
                            },
                            autor: {
                                required: true,
                                minlength: 4,
                                maxlength: 100
                            }
                        },
                        errorPlacement: function(error, element) {
                            $('#help-inline-' + element[0].id).html(error);
                        }
                    });

                });
    </script>
</head>
<html>
    <body>
        <spring:url var="uploadUrl" value="/estudios/upload"/>
        <form action="${uploadUrl}" method="post" enctype="multipart/form-data" id="publicacion">			
        <div class="span-21 last">

            <div class="span-20 last">
                <!-- File -->
                <%-- || !publicacion.identifier.isEmpty() --%>
                <c:if
                    test="${publicacion.identifier != null || publicacion.identifier != '' }">
                    <div class="span-4">
                        <label class="labelCampo">Fichero</label>
                    </div>
                    <div class="span-16 last">
                        <input type="file" name="file" size="20" id="file" class="campo" />
                        <span class="help-inline" id="help-inline-file"
                              style="display: block;"> <form:errors path="file"
                                     cssClass="fieldError" />
                        </span>
                    </div>
                </c:if>
            </div>

            <div class="span-20 last">
                <!-- Año -->
                <div class="span-4">
                    <label class="labelCampo">Año</label>
                </div>
                <div class="span-16 last">
                    <select name="anyo" id="anyo" class="campo">
                        <option value="">-----</option>
                        <c:forEach items="${anyos}" var="an">
                            <c:if test="${an == publicacion.anyo}">
                                <option value="${an}" selected>${an}</option>
                            </c:if>
                            <c:if test="${an != publicacion.anyo}">
                                <option value="${an}">${an}</option>
                            </c:if>
                        </c:forEach>
                    </select> <span class="help-inline" id="help-inline-anyo"
                                    style="display: block;"> <form:errors path="anyo"
                                 cssClass="fieldError" />
                    </span>
                </div>
            </div>

            <div class="span-20 last">
                <!-- Sector -->
                <div class="span-4">
                    <label class="labelCampo">Sector</label>
                </div>
                <div class="span-16 last">
                    <select name="sector" id="sector" class="campo">
                        <option value="">-----</option>
                        <c:forEach items="${sectores}" var="sector">
                            <c:if test="${sector.name == publicacion.sector}">
                                <option value="${sector.name}" selected>${sector.name}</option>
                            </c:if>
                            <c:if test="${sector.name != publicacion.sector}">
                                <option value="${sector.name}">${sector.name}</option>
                            </c:if>
                        </c:forEach>
                    </select> <span class="help-inline" id="help-inline-sector"
                                    style="display: block;"> <form:errors path="sector"
                                 cssClass="fieldError" />
                    </span>
                </div>
            </div>

            <div class="span-20 last">
                <!-- Nivel T -->
                <div class="span-4">
                    <label class="labelCampo">Nivel territorial</label>
                </div>
                <div class="span-16 last">
                    <select name="nivelTerritorial" id="nivelTerritorial" class="campo"
                            style="width: 30%;">
                        <option value="">-----</option>
                        <c:forEach items="${niveles}" var="nivelTerritorial">
                            <c:if
                                test="${nivelTerritorial.name == publicacion.nivelTerritorial}">
                                <option value="${nivelTerritorial.name}" selected>
                                    ${nivelTerritorial.name}</option>
                                </c:if>
                                <c:if
                                    test="${nivelTerritorial.name != publicacion.nivelTerritorial}">
                                <option value="${nivelTerritorial.name}">
                                    ${nivelTerritorial.name}</option>
                                </c:if>
                            </c:forEach>
                    </select> <span class="help-inline" id="help-inline-nivelTerritorial"
                                    style="display: block;"> <form:errors
                            path="nivelTerritorial" cssClass="fieldError" />
                    </span>
                </div>
            </div>

            <div class="span-20 last">
                <!-- Estudio -->
                <div class="span-4">
                    <label class="labelCampo">Nombre Estudio</label>
                </div>
                <div class="span-16 last">
                    <input type="text" name="name" value="${publicacion.name}"
                           id="name" class="campo" /> <span class="help-inline"
                           id="help-inline-name" style="display: block;"> <form:errors
                            path="name" cssClass="fieldError" />
                    </span>
                </div>
            </div>

            <div class="span-20 last">
                <!-- Autor -->
                <div class="span-4">
                    <label class="labelCampo">Autor</label>
                </div>
                <div class="span-16 last">
                    <input type="text" name="autor" id="autor"
                           value="${publicacion.autor}" class="campo" /> <span
                           class="help-inline" id="help-inline-autor" style="display: block;">
                        <form:errors path="autor" cssClass="fieldError" />
                    </span>
                </div>
            </div>

            <div class="span-20 last">
                <!-- Resumen -->
                <div class="span-4">
                    <label class="labelCampo">Resumen</label>
                </div>
                <div class="controls span-15 last">
                    <textarea rows="5" cols="10" name="resumen" id="resumen">${publicacion.resumen}</textarea>
                </div>
            </div>

            <!-- Botonera -->
            <div class="span-21 last buttons-container">
                <input type="hidden" name="identifier"
                       value="${publicacion.identifier}" />
                <button type="submit" class="pointer button save">Guardar</button>
                <a href="${urlListadoMias}" class="button secondary cancel">Cancelar</a>
            </div>
        </form>
</body>
</html>