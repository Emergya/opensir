<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib uri="http://displaytag.sf.net" prefix="display"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<html>
<head>
<title>Canales Temáticos</title>
<spring:url value="/cartografico/nuevoCanal" var="urlNuevoCanal" />
<spring:url value="/cartografico/editarCanal" var="urlEditarCanal" />
<spring:url value="/cartografico/editarSubcarpeta"
	var="urlEditarSubcarpeta" />
<spring:url value="/cartografico/borrarCanal" var="urlBorrarCanal" />
<spring:url value="/cartografico/nuevasubcarpeta"
	var="urlNuevaSubcarpeta" />
	
<spring:url value="/img/carpeta.png" var="iconoCarpeta" />

<script type="text/javascript">
	$().ready(function() {
		$('.enlace-borrar').click(function(event) {
			return confirm("¿Seguro que desea eliminar el canal?");
		})
	});
	
	function showNewSubfolderDialog(url) {
		loadContentDialog("popContainer", "Nueva subcarpeta", url, 420);
	}
</script>
</head>
<body>

	<!-- Popup crear Nueva carpeta-->
	<div id="popContainer"></div>

	<display:table name="layerTree" id="node" class="tableScroll">
		<%--Solo mostramos listado de carpetas y canales, no las capas --%>
		<c:choose>
			<c:when test="${node.tipo eq 'folder'}">
				<display:column title="Nombre" class="primera-cebreada">
					<div style="margin-left: ${node.nivel * 25}px">
						<c:choose>
							<c:when test="${node.tipo eq 'folder'}">
								<img class="icono" src="${iconoCarpeta}" />
								${node.node.name}
							</c:when>
							<c:otherwise>
								${node.node.layerLabel }
							</c:otherwise>
						</c:choose>
					</div>
				</display:column>

				<display:column title="Tipo">
					<c:choose>
						<c:when test="${node.node.folderType != null}">
								${node.node.folderType.type}
								</c:when>
						<c:otherwise>
						-
						</c:otherwise>
					</c:choose>
				</display:column>


				<display:column title="Ámbito">
						${node.node.zone.name}
					</display:column>

				<display:column title="Estado" >
					<c:choose>
						<c:when test="${not node.node.enabled}">
							<span title="No habilitado">No hab.</span>
						</c:when>
						<c:otherwise>
							<span title="Habilitado">Hab.</span>
						</c:otherwise>
					</c:choose>

				</display:column>
				<!-- Operaciones -->
				<display:column title="Nueva subcarpeta" class="ultima-cebreada iconColumn">
					<a href="javascript:void(0)" class="icon textless addSubElement"
						onclick="showNewSubfolderDialog('${urlNuevaSubcarpeta}/${node.node.id}');">
						Añadir subcarpeta </a>
				</display:column>

				<display:column title="Editar" class="iconColumn sin-cebreado"
					headerClass="iconColumn">
					<c:choose>
						<c:when test="${not node.node.hasParent}">
							<!-- No es una subcarpeta -->
							<a href="${urlEditarCanal}/${node.node.id}" title="Editar"
								class="icon textless edit"> Editar </a>
						</c:when>
						<c:otherwise>
							<a href="${urlEditarSubcarpeta}/${node.node.id}" title="Editar"
								class="icon textless edit"> Editar </a>
						</c:otherwise>
					</c:choose>

				</display:column>

				<display:column title="Borrar" class="iconColumn sin-cebreado"
					headerClass="iconColumn">
					<a href="${urlBorrarCanal}/${node.node.id}" title="Borrar"
						class="icon textless delete enlace-borrar"> Borrar </a>
				</display:column>
			</c:when>
		</c:choose>
	</display:table>

	<div class="span-20 buttons-container">
		<a href="${urlNuevoCanal}" class="button new add">Nuevo Canal</a>
	</div>
</body>
</html>
