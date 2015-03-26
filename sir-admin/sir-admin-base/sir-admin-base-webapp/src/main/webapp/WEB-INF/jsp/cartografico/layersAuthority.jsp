<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib uri="http://displaytag.sf.net" prefix="display"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<html>
<head>
<title>Capas de la Institución</title>
<script type="text/javascript">
	function removeLayer(url) {
		if (confirm("¿Realmente quieres eliminar la capa?")) {
			window.location = url;
		}
	}
</script>
<spring:url value="/cartografico/borrarLayer" var="urlBorrarLayer" />
<spring:url value="/cartografico/editarLayer" var="urlEditarLayer" />
<spring:url value="/cartografico/nuevaSolicitudPublicacion"
	var="urlPublicarLayer" />

</head>
<body>
	<display:table name="layersAuthority" id="layerItem"
		class="tableScroll">
		<display:column property="layerLabel" title="Nombre capa"
			class="primera-cebreada" />
		<display:column property="type.name" title="Tipo" 
			class="ultima-cebreada"/>

		<!-- Operaciones -->
		<display:column title="Actualizar" class="iconColumn sin-cebreado"
			headerClass="iconColumn">
			<a href="${urlEditarLayer}/${layerItem.id}" title="Actualizar"
				class="icon textless edit"> Actualizar </a>
		</display:column>
		<display:column title="Sol. Public." class="iconColumn sin-cebreado"
			headerClass="iconColumn">
			<a href="${urlPublicarLayer}/${layerItem.id}" title="Publicar"
				class="icon textless publish"> Publicar </a>
		</display:column>
		<display:column title="Borrar" class="iconColumn sin-cebreado"
			headerClass="iconColumn">
			<a href="javascript:void(0)" title="Borrar"
				onclick="removeLayer('${urlBorrarLayer}/${layerItem.id}')"
				class="icon textless delete"> Borrar </a>
		</display:column>
	</display:table>
</body>
</html>