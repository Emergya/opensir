<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ page contentType="text/html;charset=UTF-8" language="java"
	pageEncoding="UTF-8"%>

<head>
<title>Búsqueda</title>
<spring:url value="/gestionestudios/buscar" var="urlBuscar" />
</head>
<html>
<body>


	<form:form commandName="estudioFilter" action="${urlBuscar}"
		method="post">



		<!-- Año -->
		<div class="span-9">
			<div class="span-4">
				<label class="labelCampo">Año</label>
			</div>
			<div class="span-5 last">
				<form:select path="anyo" id="anyo" cssClass="campo">
					<option value="">----</option>
					<form:options items="${anyos}" />
				</form:select>
			</div>
		</div>

		<!-- Autor -->
		<div class="span-9">
			<div class="span-4">
				<label class="labelCampo">Autor</label>
			</div>
			<div class="span-5 last">
				<form:input path="autor" id="autor" cssClass="campo" />
			</div>
		</div>

		<!-- Sector -->
		<div class="span-9 sector">
			<div class="span-4">
				<label class="labelCampo">Sector</label>
			</div>
			<div class="span-5 last">
				<form:select path="sector" id="sector">
					<option value="">----</option>
					<form:options items="${sectores}" />
				</form:select>
			</div>
		</div>


		<!-- Institucion -->
		<div class="span-9">
			<div class="span-4">
				<label class="labelCampo">Institución</label>
			</div>
			<div class="span-5 last">
				<form:select path="institucion" id="institucion">
					<option value="">----</option>
					<form:options items="${instituciones}" itemLabel="authority"
						itemValue="authority" />
				</form:select>
			</div>
		</div>

		<!-- Ambito territorial -->
		<div class="span-9">
			<div class="span-4">
				<label class="labelCampo">Ámbito Territorial</label>
			</div>
			<div class="span-5 last">
				<form:select path="nivelTerritorial" id="nivelTerritorial">
					<option value="">----</option>
					<form:options items="${niveles}" itemLabel="name" itemValue="name" />
				</form:select>
			</div>
		</div>

		<!-- Nombre estudio -->
		<div class="span-9">
			<div class="span-4">
				<label class="labelCampo">Nombre del Estudio</label>
			</div>
			<div class="span-5 last">
				<form:input path="nombre" id="nombre" cssClass="campo" />
			</div>
		</div>


		<!-- Busqueda libre-->
		<div class="span-9 textolibre">
			<div class="span-4">
				<label class="labelCampo">Texto libre</label>
			</div>
			<div class="span-5 last">
				<form:input path="textoLibre" id="textoLibre" cssClass="campo" />
			</div>
		</div>

		<!-- Boton -->
		<div class="span-21 last buttons-container">
			<button type="submit" class="button search">Nueva Búsqueda</button>
		</div>
	</form:form>
</body>
</html>
