<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://displaytag.sf.net" prefix="display"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>


<html>
<head>
<title>Contactos</title>
<spring:url value="/admin/editarContacto" var="urlEditarContacto" />
<spring:url value="/admin/borrarContacto" var="urlBorrarContacto" />
<spring:url value="/admin/nuevoContacto" var="urlNuevoContacto" />
<spring:url value="¿Eliminar Contacto?" var="mensajeEliminarContacto" />
</head>
<body>


	<display:table name="listaContactos" id="contactoItem">

		<display:column property="titulo" title="Título"
			class="primera-cebreada" />
		<display:column property="email" title="Email Autor" />
		<display:column title="Fecha Recepción">
			<fmt:formatDate value="${contactoItem.fechaCreacion}"
				pattern="dd/MM/yyyy HH:mm" />
		</display:column>

		<display:column title="Leído" class="ultima-cebreada">
			<c:choose>
				<c:when
					test="${contactoItem.leido == null or contactoItem.leido == false}">No</c:when>
				<c:otherwise>Si</c:otherwise>
			</c:choose>

		</display:column>

		<!-- Operaciones -->
		<display:column title="Detalle" class="sin-cebreado"
			headerClass="iconColumn">
			<a href="${urlEditarContacto}/${contactoItem.id}" class="icon textless edit" title="Editar">				
				Editar
			</a>
		</display:column>
		<display:column title="Borrar" class="sin-cebreado"
			headerClass="iconColumn">
			<a href="${urlBorrarContacto}/${contactoItem.id}" 
				class="icon textless delete" title="Borrar"
				onclick="return confirm('${mensajeEliminarContacto}');">
				Borrar
			</a>
		</display:column>
	</display:table>
</body>
</html>