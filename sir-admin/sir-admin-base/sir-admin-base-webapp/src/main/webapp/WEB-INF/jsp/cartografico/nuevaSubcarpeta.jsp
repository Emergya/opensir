<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ page contentType="text/html;charset=UTF-8" language="java"
	pageEncoding="UTF-8"%>
<!-- Vista padre: canales.jsp -->

<spring:url var="urlImages" value="/img" />



<div id="newSubfolderDialog" class="span-10">
	<!-- Contenido -->
	<spring:url var="salvarNuevaSubcarpeta"
		value="/cartografico/saveNuevaSubcarpeta" />
	<form:form commandName="folderDto" action="${salvarNuevaSubcarpeta}"
		onsubmit="return validateForm('folderDto')" method="post">
		<form:hidden path="id" id="id" />

		<div class="span-10">
			<div class="span-4">
				<label class="labelCampo">Nombre</label>
			</div>
			<div class="span-5 last">
				<form:input path="name" cssClass="campo" cssErrorClass="campoError"
					required="true" minlength="3" errorTextClass="fieldError" />
				<span class="help-inline" style="width: 25em; float: left;"
					id="help-inline-name"> <form:errors path="name"
						cssClass="fieldError" />
				</span>
			</div>
		</div>

		<div class="buttons-container span-10">
			<button type="submit" class="button save">Guardar Subcarpeta</button>
			<a class="button secondary cancel" href="javascript:void(0)"
				onclick="$('#popContainer').dialog('close')">Cancelar</a>
		</div>
	</form:form>
</div>
