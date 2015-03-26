<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib uri="http://displaytag.sf.net" prefix="display"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<html>
<head>
<title>Usuarios</title>

<spring:url value="/admin/editarUsuario" var="urlEditarUsuario" />
<spring:url value="/admin/borrarUsuario" var="urlBorrarUsuario" />
<spring:url value="/admin/nuevoUsuario" var="urlNuevoUsuario" />
<script type="text/javascript">
	$().ready(function() {
		$('a.delete').click(function(event) {
			return confirm("¿Seguro que desea eliminar el usuario?");
		})
	});
</script>
</head>
<body>
	<display:table name="listaUsuarios" id="usuarioItem">
		<display:column property="username" title="Nombre de usuario"
			class="primera-cebreada" />
		<display:column property="nombreCompleto" title="Nombre" />
		<display:column property="apellidos" title="Apellidos" />
                <display:column title="Email" class="ellipsedColumn">
                    <span title="${usuarioItem.email}">${usuarioItem.email}</span>
                </display:column>
		<display:column property="telefono" title="Fono" />
		<display:column property="authority.nombre" title="Institución" />
		<display:column title="Habilitado" class="ultima-cebreada">
			<c:choose>
				<c:when test="${not usuarioItem.valid}">
                    No              
                </c:when>
				<c:otherwise>
                	Sí
                </c:otherwise>
			</c:choose>
		</display:column>
		<display:column title="Editar" class="sin-cebreado"
			headerClass="iconColumn">
			<a href="${urlEditarUsuario}/${usuarioItem.id}" title="Editar"
				class="icon textless edit"> Editar </a>
		</display:column>
		<display:column title="Borrar" class="sin-cebreado"
			headerClass="iconColumn">
			<a href="${urlBorrarUsuario}/${usuarioItem.id}" title="Borrar"
				class="icon textless delete"> Borrar </a>
		</display:column>
	</display:table>

	<div class="buttons-container">
		<a href="${urlNuevoUsuario}" class="button new">Nuevo usuario</a>
	</div>
</body>
</html>