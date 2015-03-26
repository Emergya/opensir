<%-- 
    Document   : dataForm
    Created on : 20-jun-2013, 12:11:13
    Author     : lroman
--%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib uri="http://displaytag.sf.net" prefix="display"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="html" tagdir="/WEB-INF/tags/html"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>

<html>
    <head>

        <spring:url  var="saveDatumUrl" value="/admin/analitico/guardarSolicitudPublicacion"/>
        <spring:url  var="searchTagurl" value="/admin/analitico/buscaTag"/>
        <spring:url  var="cancelUrl" value="/admin/analitico/datos"/>
        <spring:url var="tagitCssUrl" value="/css/jquery.tag-it/jquery.tagit.css"/>
        <spring:url var="tagitJsUrl" value="/js/jquery.tag-it/tag-it.js"/>
        <spring:url var="analyticsJsUrl" value="/js/analytics.js"/>

		<spring:url var="cssBaseUrl" value="/css" />

        <link rel="stylesheet" href="${tagitCssUrl}"></link>
		<link rel="stylesheet" href="${cssBaseUrl}/analyticsAdmin.css"></link>

        <style>
            .tagit {
                border-top: 1px solid #CFCFCF;
                border-left: 1px solid #E9E9E9;
                border-bottom: 1px solid #E9E9E9;
                border-right: 1px solid #EBEBEB;               
                margin: 5px !important;
                padding-left: 10px;
                border-radius: 4px 4px 4px 4px;
                -moz-border-radius: 4px 4px 4px 4px;
                -webkit-border-radius: 4px 4px 4px 4px;
                background: #F6F6F6;
            }
        </style>
        
        <script type="text/javascript">
            urls.analyticsTagsSearchUrl = "${searchTagurl}";
        </script>

        <script type="text/javascript" src="${tagitJsUrl}"></script>
        <script type="text/javascript" src="${analyticsJsUrl}"></script>

        <title>
            Solicitud de Publicación
        </title>
    </head>
    <body>



        <form:form action="${saveDatumUrl}" commandName="datumDto" method="post"
                   enctype="multipart/form-data" modelAttribute="analyticsDataDto">
            <form:hidden path="identifier" id="identifier" name="identifier"/>
            <div class="span-22 last">
                <div class="span-11">
                    <div class="span-11 last">
                        <html:inputField name="name" label="Nombre deseado"
                                         lblCtrCssClass="span-3"
                                         ctrCssClass="span-8 last"
                                         cssClass="campo" cssErrorClass="campoError"
                                         errorTextClass="fieldError" />
                    </div>

                </div>
                <div class="span-11 last">
                    <div class="span-3">
                        <label class="labelCampo">Categoría</label>
                    </div>

                    <div class="span-8 last">

                        <form:select path="categoryId"  id="categoryId"
                                     cssClass="campo" cssErrorClass="campoError">
                            <form:option value="" label="---"></form:option>
                            <form:options items="${categories}"
                                          itemLabel="name" itemValue="id"></form:options>
                        </form:select>

                        <span class="help-inline" id="help-inline-categoryId">
                            <form:errors path="categoryId" cssClass="fieldError" />
                        </span>

                    </div>
                </div>
            </div>

            <div class="span-11">
                <c:if test="${IS_ADMIN}">
                    <div class="span-11">
                        <div class="span-11 last">
                            <div class="span-3">
                                <label class="labelCampo">Institución</label>
                            </div>

                            <div class="span-8 last">

                                <form:select path="institutionId" id="institutionId"
                                             cssClass="campo" cssErrorClass="campoError">
                                    <form:option value="" label="---"></form:option>
                                    <form:options items="${institutions}"
                                                  itemLabel="authority" itemValue="id" />
                                </form:select>

                                <span class="help-inline" id="help-inline-institutionId">
                                    <form:errors path="institutionId" cssClass="fieldError" />
                                </span>

                            </div>
                        </div>
                    </div>
                </c:if>
                <div class="span-11 last">  
                    <html:inputField name="author" label="Autor"
                                     lblCtrCssClass="span-3"
                                     ctrCssClass="span-8 last"
                                     cssClass="campo" cssErrorClass="campoError"
                                     errorTextClass="fieldError" />
                </div>
                <div class="span-11 last">
                    <div class="span-3">
                        <label class="labelCampo">Licencia</label>
                    </div>

                    <div class="span-8 last">

                        <form:select path="license" name="license"
                                     id="license"
                                     cssClass="campo" cssErrorClass="campoError">
                            <form:option value="" label="---"></form:option>
                            <form:options 
                                itemLabel="label" ></form:options>
                        </form:select>

                        <span class="help-inline" id="help-inline-license">
                            <form:errors path="license" cssClass="fieldError" />
                        </span>

                    </div>
                </div>
                <c:if test="${not IS_ADMIN}">
                    <div class="span-11 last">
                        <div class="span-3">
                            <label class="labelCampo">Ámbito territorial</label>
                        </div>

                        <div class="span-8 last">

                            <form:select path="geoContextId" name="geoContextId"
                                         id="geoContextId" 
                                         cssClass="campo" cssErrorClass="campoError">
                                
                                <form:option value="" label="---"/>
                                <form:options items="${geoContexts}"
                                         itemLabel="name" itemValue="id"/>
                            </form:select>

                            <span class="help-inline" id="help-inline-geoContextId">
                                <form:errors path="geoContextId" cssClass="fieldError" />
                            </span>

                        </div>
                    </div>        
                </c:if>
                <div class="span-11 last">  
                    <html:inputField name="tags" label="Etiquetas"
                                     lblCtrCssClass="span-3"
                                     ctrCssClass="span-8 last"
                                     cssClass="campo tagsField" cssErrorClass="campoError"
                                     errorTextClass="fieldError" />
                </div>
            </div>
            <div class="span-11 last">
                <div class="span-3">
                    <label class="labelCampo">Descripción</label>
                </div>

                <div class="span-8 last">

                    <form:textarea path="description" items="${categories}"
                                   id="description"
                                   cssClass="campo" cssErrorClass="campoError"/>

                    <span class="help-inline" id="help-inline-description">
                        <form:errors path="description" cssClass="fieldError" />
                    </span>

                </div>
            </div>
        </div>

        <div class="buttons-container">
            <button class="button save create">
                Solicitar Publicación
            </button>
            <a class="button secondary cancel"
               href="${cancelUrl}">
                Cancelar
            </a>
        </div>

    </form:form>
</body>

</html>
