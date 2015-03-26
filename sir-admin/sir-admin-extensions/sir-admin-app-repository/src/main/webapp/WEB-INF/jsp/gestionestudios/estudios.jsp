<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ page contentType="text/html;charset=UTF-8" language="java"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://displaytag.sf.net" prefix="display"%>

<head>
<title>Resultados de la Búsqueda</title>
<spring:url value="/js" var="jsPath" />
<script type="text/javascript" src="${jsPath}/studiesSearch.js"></script>

<spring:url value="/gestionestudios/buscador" var="urlGestionEstudios" />
<spring:url value="/gestionestudios/verDetalle" var="urlVerDetalle" />
<spring:url value="/gestionestudios/eliminar" var="urlEliminar" />
<spring:url value="/gestionestudios/download" var="urlDescargar" />
</head>
<html>
<body>

	<jsp:useBean id="list"
		type="java.util.List<com.emergya.ohiggins.cmis.bean.Publicacion>"
		scope="request" />

	<c:set var="myId" value="row" />
	<div style="float: left; width: 100%">
		<display:table name="list" sort="external" defaultsort="5"
			pagesize="${pageSize}" uid="lista" partialList="true" size="${size}"
			decorator="com.emergya.ohiggins.web.decorator.EstudioDecorator">

			<display:column property="name" title="Nombre del Estudio"
				sortable="false" sortName="name" class="primera-cebreada"/>
			<display:column property="institucion" title="Institución"
				sortable="false" sortName="institucion" />
			<display:column property="sector" title="Sector" sortable="false"
				sortName="sector" />
			<display:column property="nivelTerritorial" title="Nivel Territorial"
				sortable="false" sortName="nivelterritorial" />
			<display:column property="anyo" title="Año" sortable="false"
				sortName="anyo" />
			<display:column property="autor" title="Autor" sortable="false"
				sortName="autor" class="ultima-cebreada"/>
			<display:column title="Descargar" sortable="false" sortName="ver"
				headerClass="iconColumn"
				class="iconColumn sin-cebreado">
				<a href="${urlDescargar}/${lista.identifier}"
					title="Descargar" class="icon textless download"> Descargar </a>
			</display:column>
			<display:column title="Ver Detalle" class="iconColumn sin-cebreado"
				headerClass="iconColumn"
				sortable="false" sortName="verDetalle">
				<a
					href="javascript:studiesSearch.viewDetails('${urlVerDetalle}/${lista.identifier}');"
					title="Ver detalle" class="icon textless view"> Ver detalle</a>
			</display:column>
			<display:column title="Eliminar" sortable="false" sortName="eliminar"
				headerClass="iconColumn"
				class="iconColumn sin-cebreado">
				<a
					href="javascript:studiesSearch.deleteStudy('${urlEliminar}/${lista.identifier}')"
					title="Eliminar" class="icon textless delete">Eliminar</a>
			</display:column>
		</display:table>
	</div>

	<div id="studies_detailsView"></div>

	<div class="buttons-container">
		<a href="${urlGestionEstudios}" class="button search"> Nueva
			Búsqueda</a>
	</div>

</body>

</html>