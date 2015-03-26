<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib uri="http://displaytag.sf.net" prefix="display"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<html>
<head>
<title>Solicitudes Pendientes</title>
<spring:url value="/moduloCartografico" var="urlmoduloCartografico" />
<spring:url var="urlVerMetadatos"
	value="/admin/cartografico/mostrarMetadatos" />
<spring:url var="urlPublishLayer"
	value="/admin/cartografico/publicarLayer" />
<spring:url var="urlDenyPublication"
	value="/admin/cartografico/rechazarPublicacion" />

<spring:url value="/js" var="jsPath" />
<script type="text/javascript" src="${jsPath}/publishRequests.js"></script>

</head>
<body>
	<display:table name="layerPublish" id="item" class="tableScroll">

		<display:column title="Usuario" class="primera-cebreada">
				${item.user.username}
			</display:column>

		<display:column title="Institución">
				${item.auth.authority}
			</display:column>

		<display:column title="Capa" class="overflowEllipsis"
			style="width: 10em; max-width: 10em">
			<span title="${item.sourceLayerName }">${item.sourceLayerName}</span>
		</display:column>

		<display:column title="Actualización">
			<c:choose>
				<c:when test="${item.update}">
						Sí
					</c:when>
				<c:otherwise>
						No
					</c:otherwise>
			</c:choose>
		</display:column>

		<display:column title="Capa actual"
			class="overflowEllipsis ultima-cebreada"
			style="width: 15em; max-width: 15em">
			<span title="${item.updatedLayerPath }">${item.updatedLayerPath}</span>
		</display:column>

		<display:column title="Ver capa" class="iconColumn sin-cebreado"
			headerClass="iconColumn">
			<%-- pasar como parametro el id de la layer 
				/${item.layer.id} --%>
			<a href="${urlmoduloCartografico}" target='_blank' title="Ver capa"
				class="icon textless eye"> Ver capa </a>
		</display:column>

		<display:column title="Ver metadatos" class="iconColumn sin-cebreado"
			headerClass="iconColumn">
			<a href="javascript:void(0)" class="icon textless metadata" title="Ver metadatos"
				onclick="publishRequests.showMetadata('${urlVerMetadatos}/${item.estado}/${item.id}','${item.nombredeseado}')">
				Ver metadatos </a>
		</display:column>

		<display:column title="Publicar" class="iconColumn sin-cebreado"
			headerClass="iconColumn">
			<a href="javascript:void(0)" class="icon textless ok" title="Publicar"
				onclick="publishRequests.publishLayer('${urlPublishLayer}/${item.id}');">
				Publicar </a>
		</display:column>

		<display:column title="Denegar" class="iconColumn sin-cebreado"
			headerClass="iconColumn">
			<a href="javascript:void(0)" class="icon textless cancel" title="Denegar"
				onclick="publishRequests.denyPublication('${urlDenyPublication}/${item.id}');">
				Denegar </a>
		</display:column>
	</display:table>

	<div id="metadataDialog"></div>
	<div id="acceptDialog"></div>
	<div id="denyDialog"></div>
</body>
</html>