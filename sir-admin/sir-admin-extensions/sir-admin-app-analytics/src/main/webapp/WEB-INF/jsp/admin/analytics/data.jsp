<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib uri="http://displaytag.sf.net" prefix="display"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="html" tagdir="/WEB-INF/tags/html"%>


<html>
<head>
<title><c:choose>
		<c:when test="${IS_ADMIN}">
			<h4>Datos públicos</h4>
		</c:when>
		<c:otherwise>
			<h4>Mis datos</h4>
		</c:otherwise>
	</c:choose></title>
<spring:url value="/css" var="cssBaseUrl" />
<spring:url value="/js" var="jsBaseUrl" />
<spring:url value="/admin/analitico/nuevoDato" var="urlNew"></spring:url>
<spring:url value="/admin/analitico/borrarDato" var="urlDelete"></spring:url>
<spring:url value="/admin/analitico/actualizarDato" var="urlEdit"></spring:url>
<spring:url value="/admin/analitico/solicitarPublicacion"
	var="urlPublishRequest"></spring:url>
<spring:url value="/popup/moduloAnaliticoEstadistico/verDetalle" var="viewDetailsUrl"/>

<script type="text/javascript" src="${jsBaseUrl }/analyticsAdmin.js"></script>
<script type="text/javascript">
	$(document).ready(function() {
		$('.enlace-borrar').click(function(event) {
			return confirm("¿Seguro que desea eliminar la entrada?");
		});
	});
</script>

<link rel="stylesheet" href="${cssBaseUrl }/analyticsAdmin.css" />
</head>
<body>
	<p>
		<a id="searchFormToggle"
			title="Pulsa para mostrar/ocultar el formulario de búsqueda"
			href="javascript:analyticsAdmin.toggleSearchForm()">Búsqueda
			avanzada</a>
	</p>
	<div id="searchFormCtr" class="span-21 last ${searchFormCtrClass }">
		<jsp:include page="searchForm.jsp" />
	</div>

	<display:table name="data" id="datum" uid="datum" requestURI=""
		sort="external">
		<display:column title="Nombre" class="primera-cebreada">
                ${datum.name}
            </display:column>
		<display:column title="Categoría">
                ${datum.categoryName}
            </display:column>
		<c:choose>
			<c:when test="${IS_ADMIN}">
				<display:column title="Fecha de publicación" class="ultima-cebreada">
					<fmt:formatDate pattern="dd/MM/yy HH:mm"
						value="${datum.requestAnswerDate.time}" />
				</display:column>
			</c:when>
			<c:otherwise>
				<display:column title="Fecha de creación" class="ultima-cebreada">
					<fmt:formatDate pattern="dd/MM/yy HH:mm"
						value="${datum.creationDate.time}" />
				</display:column>
			</c:otherwise>
		</c:choose>

		<display:column title="Actualizar" class="sin-cebreado modificar"
			headerClass="iconColumn">
			<a href="${urlEdit}/${datum.identifier}" class="icon textless edit"
				title="Actualizar">Actualizar</a>
		</display:column>
		<display:column class="sin-cebreado iconColumn" title="Ver"
			headerClass="iconColumn">
			<a href="javascript:void(0)" class="icon textless view enlace-ver"
			onclick="analyticsAdmin.viewDetails('${datum.name}','${viewDetailsUrl}/${datum.identifier}')"
				title="Ver detalles">Ver</a>
		</display:column>
		<c:if test="${not IS_ADMIN }">
			<display:column class="sin-cebreado solicitar-publicacion iconColumn"
				headerClass="iconColumn" title="Sol. Publicación.">
				<a href="${urlPublishRequest}/${datum.identifier}"
					class="icon textless publish" title="Solicitar publicación">Borrar</a>
			</display:column>
		</c:if>
		<display:column class="sin-cebreado borrar iconColumn"
			headerClass="iconColumn" title="Borrar">
			<a href="${urlDelete}/${datum.identifier}"
				class="icon textless delete enlace-borrar" title="Borrar dato">Borrar</a>
		</display:column>
	</display:table>


	<c:if test="${not IS_ADMIN}">
		<div class="span-20 buttons-container">
			<a href="${urlNew}" class="button new add"> Nuevo Dato</a>
		</div>
	</c:if>
	<div id="viewDetailsPopup"></div>
</body>
</html>