<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://www.opensymphony.com/sitemesh/decorator" prefix="decorator" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>


<spring:url var="downloadUrl" value="/moduloAnaliticoEstadistico/descarga"></spring:url>

<form:form commandName="datum">
    <div class="span-10 last">
    	<div class="span-5">
    		<div class="span-5 last"><label  class="labelCampo">Institución</label></div>
    		<div class="span-5 last">
    			<div>
    				<form:input class="campo" path="institutionName" readonly="true"/>
    			</div>
    		</div>
    	</div>
    	<div class="span-5 last">
    		<div class="span-5 last"><label class="labelCampo">Fecha de publicación</label></div>
    		<div class="span-5 last">
    			<div>
    				<form:input class="campo" path="requestAnswerDateLabel" readonly="true"/>
    			</div>
    		</div>
    	</div>
    </div>
    
    <div class="span-10 last">
    	<label  class="labelCampo">Descripción</label>
    </div>
    <div class="span-10 last">
    	<form:textarea path="description" readonly="true" rows="5"/>
    </div>
    <div class="span-10 last">
    	<div class="span-5">
    		<div class="span-5 last"><label  class="labelCampo">Categoría</label></div>
    		<div class="span-5 last">
    			<div>
    				<form:input class="campo" path="categoryName" readonly="true"/>
    			</div>
    		</div>
    	</div>
    	<div class="span-5 last">
    		<div class="span-5 last"><label class="labelCampo">ÁmbitoTerritorial</label></div>
    		<div class="span-5 last">
    			<div>
    				<form:input class="campo" path="geoContextName" readonly="true"/>
    			</div>
    		</div>
    	</div>
    </div>
     <div class="span-10 last">
    	<div class="span-5">
    		<div class="span-5 last"><label  class="labelCampo">Licencia</label></div>
    		<div class="span-5 last">
    			<div>
    				<c:choose>
    					<c:when test="${empty datum.license }">
    						<input class="campo" readonly="true" type="text"/>
    					</c:when>
    					<c:otherwise>
    						<form:input class="campo" path="license.label" readonly="true"/>
    					</c:otherwise>
    				</c:choose>
    				
    			</div>
    		</div>
    	</div>
    	<div class="span-5 last">
    		<div class="span-5 last"><label  class="labelCampo">Descargas</label></div>
    		<div class="span-5 last">
    			<div>
    				<form:input class="campo" path="downloads" readonly="true"/>
    			</div>
    		</div>
    	</div>
    </div>

    <div class="buttons-container">
         <a href="${downloadUrl }/${datum.identifier}" class="button download">
            Descargar</a>
        <a href="javascript:void(0)" onclick="$('#viewDetailsPopup').dialog('close');" class="button cancel">
            Cancelar</a>
    </div>
</form:form>