<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ page contentType="text/html;charset=UTF-8" language="java"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://displaytag.sf.net" prefix="display"%>

<jsp:useBean id="list"
	type="java.util.List<com.emergya.ohiggins.cmis.bean.Publicacion>"
	scope="request" />

<head>

<spring:url value="/js/studiesSearch.js" var="jsPath" />
<script type="text/javascript" src="${jsPath}"></script>

<spring:url value="/repositorioEstudios" var="moduloRepositorioUrl" />
<spring:url value="/repositorioEstudios/download" var="urlDescargar" />
</head>
<html>
<body>

	<div class="title">
		<div class="span-24">
			<h2>REPOSITORIO DE ESTUDIOS</h2>
			<h3>Región del Libertador General Bernardo O'Higgins</h3>
		</div>
		<div class="clear"></div>
	</div>

	<c:set var="myId" value="row" />
	<div class="listadoEstudios">
		<div class="span-22 cabeceraPresentacion">
			<h4>Resultado de la Búsqueda</h4>
		</div>

		<display:table name="list" sort="external" defaultsort="5"
			pagesize="${pageSize}" uid="lista" partialList="true" size="${size}"
			requestURI=""
			decorator="com.emergya.ohiggins.web.decorator.EstudioDecorator">

			<display:column property="name" title="Nombre del Estudio"
				sortable="false" sortName="name" />
			<display:column property="autor" title="Autor" sortable="false"
				sortName="autor" />
			<display:column property="sector" title="Sector" sortable="false"
				sortName="sector" />
			<display:column property="institucion" title="Institución"
				sortable="false" sortName="institucion" />
			<display:column property="anyo" title="Año" sortable="false"
				sortName="anyo" />
			<%-- 
				<display:column property="nivelTerritorial" title="Nivel Territorial"
					sortable="false" sortName="nivelterritorial" />
 				--%>

			<display:column title="Descargar" sortable="false" sortName="ver"
				class="iconColumn"
				headerClass="iconColumn">
				<a href="${urlDescargar}/${lista.identifier}"
					title="Descargar" class="icon textless download"> Descargar </a>
			</display:column>

			<display:column title="Ver Detalle" class="iconColumn" headerClass="iconColumn"
				sortable="false" sortName="verDetalle">
				<a
					href="javascript:studiesSearch.viewDetails('${moduloRepositorioUrl}/verDetalle/${lista.identifier}');"
					title="Ver Detalle" class="icon textless view"> Ver detalle</a>
			</display:column>
		</display:table>
		<div class="buttons-container">
			<a class="button search" href="${moduloRepositorioUrl}"> Nueva
				Búsqueda</a>
		</div>
	</div>

	<div id="studies_detailsView"></div>


</body>

</html>