<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>

<div class="span-22 cabeceraPresentacion">
	<h4>Criterios de Búsqueda</h4>
</div>

<div class="span-22 contenidoPresentacion">

	<!-- Año -->
	<div class="span-10">
		<div class="span-3">
			<label class="labelCampo">Año</label>
		</div>
		<div class="span-7 last">
			<form:select path="anyo" id="anyo" cssClass="campo">
				<option value="">----</option>
				<form:options items="${anyos}" />
			</form:select>
		</div>
	</div>

	<!-- Nombre estudio -->
	<div class="span-10">
		<div class="span-3">
			<label class="labelCampo">Nombre Estudio</label>
		</div>
		<div class="span-7 last">
			<form:input path="nombre" id="nombre" cssClass="campo" />
		</div>
	</div>

	<!-- Sector -->
	<div class="span-10">
		<div class="span-3">
			<label class="labelCampo">Sector</label>
		</div>
		<div class="span-7 last">
			<form:select path="sector" id="sector">
				<option value="">----</option>
				<form:options items="${sectores}" />
			</form:select>
		</div>
	</div>

	<!-- Autor -->
	<div class="span-10">
		<div class="span-3">
			<label class="labelCampo">Autor</label>
		</div>
		<div class="span-7 last">
			<form:input path="autor" id="autor" cssClass="campo" />
		</div>
	</div>


	<!-- Institucion -->
	<div class="span-10">

		<div class="span-3">
			<label class="labelCampo">Institución</label>
		</div>
		<div class="span-7 last">
			<form:select path="institucion" id="institucion">
				<option value="">----</option>
				<form:options items="${instituciones}" itemLabel="authority"
					itemValue="authority" />
			</form:select>
		</div>
	</div>

	<!-- Busqueda libre-->
	<div class="span-10">
		<div class="span-3">
			<label class="labelCampo">Texto Libre</label>
		</div>
		<div class="span-7 last">
			<form:input path="textoLibre" id="textoLibre" cssClass="campo" />
		</div>
	</div>
</div>