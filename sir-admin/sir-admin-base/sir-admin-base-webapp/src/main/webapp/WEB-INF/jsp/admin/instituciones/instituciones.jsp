<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://displaytag.sf.net" prefix="display"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<html>
<head>
<title>Asociar Instituciones</title>
<spring:url value="/admin/editarInstitucion" var="urlEditarInstitucion" />
<spring:url value="/admin/borrarInstitucion" var="urlBorrarInstitucion" />
<spring:url value="/admin/nuevaInstitucion" var="urlNuevaInstitucion" />
<spring:message var="mensajeEliminarInstitucion" code="authorities.deleteConfirmation"
                text="¿Desea eliminar la Institución? Se borrarán las capas privadas de la misma, así como sus solicitudes de publicación."/>
</head>
<body>
	<!-- Listado de instituciones -->
	<display:table name="listaInstituciones" id="institucionItem">
		<display:column property="authority" title="Nombre Institución"
			class="primera-cebreada" />
		<display:column property="type.name" title="Tipo"
			class="ultima-cebreada" />
		<display:column title="Editar" class="sin-cebreado" headerClass="iconColumn">
			<a href="${urlEditarInstitucion}/${institucionItem.id}" title="Editar"
				class="icon textless edit"> Editar </a>
		</display:column>
		<display:column title="Borrar" class="sin-cebreado" headerClass="iconColumn">
			<a href="${urlBorrarInstitucion}/${institucionItem.id}" title="Borrar"
				onclick="return confirm('${mensajeEliminarInstitucion}');"
				class="icon textless delete"> Eliminar </a>
		</display:column>
	</display:table>

	<div class="buttons-container">
		<a href="${urlNuevaInstitucion}" class="button new"> Nueva
			Institución</a>
	</div>
</body>
</html>