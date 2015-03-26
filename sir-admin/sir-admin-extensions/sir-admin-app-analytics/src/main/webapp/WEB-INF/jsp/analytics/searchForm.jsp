<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://displaytag.sf.net" prefix="display"%>
<%@ taglib prefix="html" tagdir="/WEB-INF/tags/html"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<spring:url value="/moduloAnaliticoEstadistico/busquedaAvanzada" var="doCustomSearchUrl"/>
<form:form commandName="searchFilter" method="POST" action="${doCustomSearchUrl }"
	style="padding:0">
    <div class="span-8">
        <div class="span-3">
            <label class="labelCampo">Categoría</label>
        </div>

        <div class="span-5 last">
            <form:select path="categoryId"  id="categoryId"
                         cssClass="campo" cssErrorClass="campoError">
                <form:option value="" label="---"></form:option>
                <form:options items="${categories}"
                              itemLabel="name" itemValue="id"></form:options>
            </form:select>
        </div>
    </div>
    <div class="span-8 last">
         <div class="span-3">
             <label class="labelCampo">Licencia</label>
         </div>

         <div class="span-5 last">

             <form:select path="license" name="license"
                          id="license"
                          cssClass="campo" cssErrorClass="campoError">
                 <form:option value="" label="---"></form:option>
                 <form:options 
                     itemLabel="label" ></form:options>
             </form:select>
         </div>
    </div>
    <div class="span-8">
         <div class="span-3">
             <label class="labelCampo">Institución</label>
         </div>
         <div class="span-5 last">
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
     <div class="span-8 last">
     	<html:inputField name="year" label="Año" 
     		lblCtrCssClass="span-3" ctrCssClass="span-5 last"
     		cssClass="campo"/>
     </div>
     <div class="span-8">
        <div class="span-3">
            <label class="labelCampo">Ámbito territorial</label>
        </div>

        <div class="span-5 last">

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
    <div class="span-8 last">
     	<html:inputField name="containedText" label="Texto contenido" 
     		lblCtrCssClass="span-3" ctrCssClass="span-5 last"
     		cssClass="campo"/>
     </div> 
    <div class="buttons-container">
        <button class="button">Buscar</button>
    </div>
</form:form>