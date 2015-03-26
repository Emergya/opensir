<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib uri="http://displaytag.sf.net" prefix="display"%>

<head>

<title>Estudios Pendientes de Publicación</title>
<spring:url value="/estudios/download" var="urlDescargar" />
<spring:url value="/estudios/verDetalle" var="urlViewDetails" />
<spring:url var="urlImages" value="/img" />

</head>
<html>
<body>
	<jsp:useBean id="list"
		type="java.util.List<com.emergya.ohiggins.cmis.bean.Publicacion>"
		scope="request" />

	<c:set var="myId" value="row" />
	<display:table name="list" sort="external" defaultsort="5"
		pagesize="${pageSize}" uid="lista" partialList="true" size="${size}"
		requestURI=""
		decorator="com.emergya.ohiggins.web.decorator.EstudioDecorator">
		<display:column property="name" title="Nombre del Estudio"
			sortable="false" sortName="name" class="primera-cebreada" />
		<display:column property="institucion" title="Institución Responsable"
			sortable="false" sortName="institucion" class="ultima-cebreada"/>
		<display:column title="Descargar" sortable="false" sortName="ver"
                                headerClass="iconColumn"
			class="iconColumn sin-cebreado">
			<a href="${urlDescargar}/${lista.identifier}" class="icon textless download">
                            Descargar
			</a>
		</display:column>
		<display:column title="Ver Detalle"
                                headerClass="iconColumn"
			sortable="false" sortName="verDetalle" 
			class="iconColumn sin-cebreado">
                    <a href="${urlViewDetails}/${lista.identifier}" class="icon textless view">
                            Ver Detalle
			</a>
                </display:column>

	</display:table>
</body>
</html>