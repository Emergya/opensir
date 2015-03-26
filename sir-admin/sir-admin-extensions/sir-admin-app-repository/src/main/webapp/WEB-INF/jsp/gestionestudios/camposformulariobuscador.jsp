<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ page contentType="text/html;charset=UTF-8" language="java"
	pageEncoding="UTF-8"%>

<div style="display:none;">
	<spring:url value="/gestionestudios/buscar" var="urlBuscar" />
	<form:form commandName="estudioFilter" action="${urlBuscar}" id="estudioFilter" method="post">
		
		<spring:url var="urlImages" value="/img" />

			<!-- Año -->
			<form:select path="anyo" id="anyo">
				<option value="">----</option>
				<form:options items="${anyos}" />
			</form:select>

			<!-- Autor -->
			<form:input path="autor" id="autor" readonly="true" />

			<!-- Sector -->
			<form:select path="sector" id="sector">
				<option value="">----</option>
				<form:options items="${sectores}" />
			</form:select>

			<!-- Institucuion -->
			<form:select path="institucion" id="institucion">
				<option value="">----</option>
				<form:options items="${instituciones}" itemLabel="authority"
					itemValue="authority" />
			</form:select>


			<!-- Ambito territorial -->
			<form:select path="nivelTerritorial" id="nivelTerritorial">
				<option value="">----</option>
				<form:options items="${niveles}" itemLabel="name" itemValue="name" />
			</form:select>


			<!-- Nombre estudio -->
			<form:input path="nombre" id="nombre" readonly="true"/>

			<!-- Busqueda libre-->
			<form:input path="textoLibre" id="textoLibre" readonly="true"/>
			
			<!-- Eliminar -->
			<form:input path="eliminar" id="eliminar" readonly="true"/>
			<form:input path="ideliminar" id="ideliminar" readonly="true"/>
			
	</form:form>
</div>