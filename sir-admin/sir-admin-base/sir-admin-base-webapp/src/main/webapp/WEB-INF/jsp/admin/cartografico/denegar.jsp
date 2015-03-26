<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="html" tagdir="/WEB-INF/tags/html"%>
<%@ taglib prefix="ajax" tagdir="/WEB-INF/tags/ajax"%>
<%@ page contentType="text/html;charset=UTF-8" language="java"
	pageEncoding="UTF-8"%>


<spring:url var="rechazarPublicacion"
	value="/admin/cartografico/rechazarPublicacionRequestLayer" />

<!-- Contenido -->
<p style="margin-top:0.5em">
	Va a denegar la publicación de una capa. Por favor,
	introduzca a continuación el motivo por el cual se produce la
	denegación:
</p>
<form:form commandName="layerPublishRequestDto"
	action="${rechazarPublicacion}" method="post"
	onsubmit="return validateForm('layerPublishRequestDto')">

	<form:hidden path="id" />

	<div class="span-11 last">
		
			<form:textarea path="comentario" id="comentario" cssClass="campo"
				cssErrorClass="campoError" cols="10" rows="5" required="true"
				cssStyle="width:390px;height:100px;" />
		
			<span class="help-inline" id="help-inline-comentario"> 
				<form:errors
					path="comentario" cssClass="fieldError" />
			</span>		
	</div>

	<!-- Botonera -->
	<div class="buttons-container span-11 last">

		<button type="submit" class="button reject">Rechazar publicación</button>
		
		<a href="javascript:void(0)" class="button cancel secondary"
			onclick="$('#denyDialog').dialog('close')">
			Cancelar
		</a>
	
	</div>
</form:form>
</div>
