<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib uri="http://displaytag.sf.net" prefix="display"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<html>
<head>
<title>Canales (categorías)</title>
<spring:url value="/admin/analitico/nuevaCategoria" var="urlNewCategory"></spring:url>
<spring:url value="/admin/analitico/borrarCategoria" var="urlDelete"></spring:url>
<spring:url value="/admin/analitico/editarCategoria" var="urlEdit"></spring:url>

<script type="text/javascript">
	$(document).ready(function() {
		$('.enlace-borrar').click(function(event) {
			return confirm("¿Seguro que desea eliminar la categoría?");
		});
	});
</script>
</head>
<body>
	<display:table name="categories" id="category">
		<display:column title="Nombre" class="primera-cebreada">
					${category.name}
				</display:column>
		<display:column title="Habilitado" class="ultima-cebreada">
					${category.enabled?"Sí":"No" }
				</display:column>
		<display:column class="sin-cebreado iconColumn"
			headerClass="iconColumn" title="Editar">
			<a href="${urlEdit}/${category.id}" class="icon textless edit">Editar</a>
		</display:column>
		<display:column  title="Borrar" class="sin-cebreado iconColumn"
			headerClass="iconColumn">
			<a href="${urlDelete}/${category.id}"
				class="icon textless delete enlace-borrar"> Borrar </a>
		</display:column>
	</display:table>

	<div class="span-20 buttons-container">
		<a href="${urlNewCategory}" class="button new add"> Nueva
			Categoría </a>
	</div>
</body>
</html>