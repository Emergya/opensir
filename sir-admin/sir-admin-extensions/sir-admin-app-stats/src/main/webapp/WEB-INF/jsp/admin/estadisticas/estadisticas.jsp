<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ page contentType="text/html;charset=UTF-8" language="java"
         pageEncoding="UTF-8"%>

<spring:url var="urlImages" value="/img" />
<spring:url var="urlImages2" value="/gim" />
<html>
    <head>
        <title>Estadísticas</title>
    </head>
    <body>

        <spring:url var="consultaEstadistica"
                    value="/admin/cartografico/estadisticas/consultaEstadistica" />
        <form:form commandName="estadisticaFilterDto"
                   action="${consultaEstadistica}" method="post">

            <div class="adm-statistics-table span-20">
                <div>
                    <table>
                        <thead>
                            <tr>
                                <th>Ayer</th>
                                <th>Esta semana</th>
                                <th>Este mes</th>
                                <th>Este año</th>
                            </tr>
                        </thead>
                        <tbody>
                            <tr>
                                <td class="primera-cebreada">${ayer}</td>
                                <td>${semana}</td>
                                <td>${mes}</td>
                                <td class="ultima-cebreada">${anyo}</td>
                            </tr>
                        </tbody>
                    </table>
                </div>
            </div>

            <!-- Periodo de consulta -->
            <div class="adm-statistics-period ">
                <div class="span-3">
                    <label>Periodo de consulta</label>
                </div>

                <div class="span-17 last">

                    <div class="span-2">
                        <label class="labelCampo">Desde</label>
                    </div>


                    <div class="span-6 last">
                        <form:input path="desde" maxlength="120" id="desde"
                                    cssClass="campo datepicker" readonly="true"
                                    cssErrorClass="campoError" errorTextClass="fieldError" />
                    </div>



                    <div class="span-2">
                        <label class="labelCampo">Hasta</label>
                    </div>
                    <div class="span-6">
                        <form:input path="hasta" maxlength="120" id="hasta"
                                    cssClass="campo datepicker" readonly="true"
                                    cssErrorClass="campoError" errorTextClass="fieldError" />
                    </div>

                    <div class="span-1 last">
                        <input type="image" title="Filtrar Estadísticas"
                               style="margin-top: .6em; margin-left: 1em; width: 2em"
                               src="${urlImages}/lupa.png">
                    </div>

                    <div class="span-17 last">
                        <span class="help-inline" id="help-inline-desde"
                              style="padding-left: 10px"> <form:errors
                                path="desde" cssClass="fieldError" />
                        </span>
                    </div>
                </div>
            </div>

            <div class="adm-statistics-moduletools span-20">
                <div class="table-box box-moduletools">
                    <fieldset>
                        <legend>Módulo y herramientas</legend>
                        <div>
                            <table class="tableScroll">
                                <thead>
                                    <tr>
                                        <th>Módulo</th>
                                        <th>Peticiones</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    <c:forEach var="n" items="${lgoals}">
                                        <tr>
                                            <td title="${n.nombre}" class="primera-cebreada">${n.nombre}</td>
                                            <td class="ultima-cebreada">${n.value}</td>
                                        </tr>
                                    </c:forEach>															
                                </tbody>
                            </table>
                        </div>
                    </fieldset>
                </div>

                <div class="table-box box-services ">
                    <fieldset>
                        <legend>Datos de servicios</legend>
                        <div class="box-services-content">
                            <div class="table-box box-left">
                                <table class="tableScroll">
                                    <thead>
                                        <tr>
                                            <th>Canal</th>
                                            <th>Peticiones</th>
                                        </tr>
                                    </thead>
                                    <tbody>
                                        <c:forEach var="n" items="${lchannels}">
                                            <tr>
                                                <td title="${n.nombre}" class="primera-cebreada">${n.nombre}</td>
                                                <td class="ultima-cebreada">${n.value}</td>
                                            </tr>
                                        </c:forEach>

                                    </tbody>
                                </table>
                            </div>
                            <div class="table-box box-center">
                                <table class="tableScroll">
                                    <thead>
                                        <tr>
                                            <th>Capa</th>
                                            <th>Peticiones</th>
                                        </tr>
                                    </thead>
                                    <tbody>
                                        <c:forEach var="n" items="${lrequestedlayers}">
                                            <tr>
                                                <td title="${n.nombre}"
                                                	class="primera-cebreada">${n.nombre}</td>
                                                <td class="ultima-cebreada">${n.value}</td>
                                            </tr>
                                        </c:forEach>
                                    </tbody>
                                </table>
                            </div>
                            <!--div class="box-right">
                                    <table>
                                            <tr>
                                                    <td>Capa mod.</td>
                                                    <td>Peticiones</td>
                                            </tr>
                            <c:forEach var="n" items="${lupdatelayers}">
                                    <tr>
                                            <td>${n.nombre}</td>
                                            <td>${n.value}</td>
                                    </tr>
                            </c:forEach>
                    </table>
            </div-->
                        </div>
                    </fieldset>
                </div>
            </div>

            <div class="span-20 last buttons-container">
                <a href="${urlEstadisticasGen}" class="button send" target="_blank">Estadísticas
                    generales</a>

				<!--#86526  Reenable if the geoserver's analytics module gets installed someday -->
				<!-- <a href="${urlEstadisticasSig}" class="button send"
                                 target="_blank">Estadísticas SIG</a> -->	
            </div>

        </form:form>

    </body>
</html>