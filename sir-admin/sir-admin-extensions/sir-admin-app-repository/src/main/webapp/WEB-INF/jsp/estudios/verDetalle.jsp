<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ page contentType="text/html;charset=UTF-8" language="java"
         pageEncoding="UTF-8"%>

<head>
    <spring:url var="urlImages" value="/img" />
    <spring:url value="/estudios/autorizar" var="urlAutorizar" />
    <title>Detalle del Estudio</title>
    <script type="text/javascript">
        function showRejectDialog() {
            $('#popupRechazarPublicacion').dialog({
                title: "Rechazar publicación",
                width: 450,
                resizable: false,
                modal: true,
                show: {
                    effect: "fade",
                    duration: 200
                },
                hide: {
                    effect: "fade",
                    duration: 200
                }
            });
        }
    </script>
</head>
<html>
    <body>


        <!-- Popup: Rechazar Publicacion -->
        <jsp:include page="rechazar.jsp" flush="true"></jsp:include>

            <form action="../download/${identificador}" onsubmit="return confirm('¿Realmente quiere autorizar la publicación del estudio?');">

            <div id="left-col">
                <div class="inner">
                    <div>
                        <div>
                            <label class="labelCampo">Nombre del Estudio</label> 
                        </div>
                        <div>	
                            <input type="text"
                                   readonly="readonly" value="${nombre}" class="campo campoDisabled" />
                        </div>

                        <div>
                            <label class="labelCampo">Sector </label> 
                        </div>
                        <div>
                          <!--<input type="text" readonly="readonly" value="${sector}" class="campo" />-->
                            <select name="sector" id="sector" class="campo" disabled="disabled">
                                <option value="">-----</option>
                                <c:forEach items="${sectores}" var="sect">
                                    <c:if test="${sect.name == sector}">	
                                        <option value="${sect.name}" selected>
                                            ${sect.name}
                                        </option>
                                    </c:if>
                                    <c:if test="${sect.name != sector}">	
                                        <option value="${sect.name}">
                                            ${sect.name}
                                        </option>
                                    </c:if>
                                </c:forEach>
                            </select>
                        </div>

                        <div>
                            <label class="labelCampo">Año:</label> 
                        </div>
                        <div>
                          <!--<input type="text" readonly="readonly" value="${anyo}" class="campo" /><br />-->
                            <select name="anyo" id="anyo" class="campo" disabled="disabled">
                                <option value="">-----</option>
                                <c:forEach items="${anyos}" var="an">
                                    <c:if test="${an == anyo}">	
                                        <option value="${an}" selected >
                                            ${an}
                                        </option>
                                    </c:if>
                                    <c:if test="${an != anyo}">	
                                        <option value="${an}">
                                            ${an}
                                        </option>
                                    </c:if>
                                </c:forEach>
                            </select>		

                        </div>

                        <div>
                            <label class="labelCampo">Nivel Territorial </label>
                        </div> 
                        <div>	
                          <!--<input type="text" readonly="readonly" value="${nivelT}" class="campo" /><br />-->
                            <select name="nivelTerritorial" id="nivelTerritorial" class="campo" disabled="disabled">
                                <option value="">-----</option>
                                <c:forEach items="${niveles}" var="nivelTerritorial">
                                    <c:if test="${nivelTerritorial.name == nivelT}">	
                                        <option value="${nivelTerritorial.name}" selected >
                                            ${nivelTerritorial.name}
                                        </option>
                                    </c:if>
                                    <c:if test="${nivelTerritorial.name != nivelT}">	
                                        <option value="${nivelTerritorial.name}" >
                                            ${nivelTerritorial.name}
                                        </option>
                                    </c:if>
                                </c:forEach>
                            </select>
                        </div>

                        <div>
                            <label class="labelCampo">Autor</label>
                        </div> 
                        <div>	
                            <input type="text"
                                   readonly="readonly" value="${autor}" class="campo campoDisabled" /><br />
                        </div>
                    </div>
                    <p style="font-size:x-small;line-height:40px;color:#666;padding-left:15px;">Los campos marcados con <strong>(*)</strong> deben cumplimentarse obligatoriamente.</p>
                </div><!-- /.inner -->
            </div>

            <div id="right-col">
                <div class="inner">

                    <div class="resumen">
                        <div class="span-8 last">
                            <label class="labelCampo">Resumen</label>
                        </div>
                        <div class="controls span-10 last">
                            <textarea readonly="readonly" class="campo campoDisabled">${resumen}</textarea>
                            <br />
                        </div>
                    </div>

                </div><!-- /.inner -->
            </div>

            <div id="submit-options">
                <!-- <input type="submit" value="Descargar"></input>-->
                <a href="../download/${identificador}" class="button see">Ver Documento</a>
                <!-- Autorizar -->
                <a href="../autorizarPublicacion/${identificador}" class="button authorize">Autorizar Publicación</a>
                <a onclick="showRejectDialog()"
                   class="button secondary reject-red">Rechazar Publicación</a>
           <!-- <a href="#" onclick="loadContentDialog('popupRechazarPublicacion', 'Rechazar','${urlRechazar}/${identificador}', 625)" 
           class="button secondary reject-red">Rechazar Publicación</a> -->
            </div>


        </form>


    </body>
</html>
