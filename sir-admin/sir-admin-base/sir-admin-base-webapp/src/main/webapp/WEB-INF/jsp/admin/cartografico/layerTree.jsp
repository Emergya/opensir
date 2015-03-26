<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib uri="http://displaytag.sf.net" prefix="display"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<html>
<head>

<title>Árbol de Capas</title>

<spring:url value="/img/carpeta.png" var="iconoCarpeta" />
<spring:url var="printingUrl"
	value="/admin/cartografico/impresionArbolCapas" />

</head>
<body>
	<display:table name="layerTree" id="node" class="tableScroll">
		<display:column title="Nombre" class="primera-cebreada">
			<%--<c:forEach var="i" begin="0" end="${node.nivel}">
					<blockquote>			
				</c:forEach>--%>
			<div
				style="margin-left: ${node.tipo eq 'folder' ? node.nivel * 25 : node.nivel * 25}px; line-height:100%;">
				<c:choose>
					<c:when test="${node.tipo eq 'folder'}">
						<img class="icono" src="${iconoCarpeta}" />
								${node.node.name}
						</c:when>
					<c:otherwise>
								${node.node.layerLabel}
							</c:otherwise>
				</c:choose>
			</div>
			<%--<c:forEach var="i" begin="0" end="${node.nivel}">
					</blockquote>			
				</c:forEach>--%>
		</display:column>

		<display:column title="Institución Solicitante">
			<c:if test="${node.tipo eq 'layer'}">
						${node.node.requestedByAuth.authority}
			</c:if>
		</display:column>

		<display:column title="Habilitada">
			<c:choose>
				<c:when test="${not node.node.enabled}">
						No
					</c:when>
				<c:otherwise>
						Sí
					</c:otherwise>
			</c:choose>
		</display:column>

		<display:column title="Tipo">
			<c:choose>
				<c:when test="${node.tipo eq 'layer'}">
					<c:choose>
						<c:when test="${not node.node.isChannel}">
							<span
								title="Pertenece a una carpeta, se mostrará en el visor pulsando en «Ver más»">Carpeta</span>
						</c:when>
						<c:otherwise>
							<span
								title="Pertenece a un canal, se mostrará en el visor al cargar dicho canal temático padre">Canal</span>
						</c:otherwise>
					</c:choose>
				</c:when>
			</c:choose>
		</display:column>

		<display:column title="Fecha últ. actualización"
			class="ultima-cebreada">
			<c:choose>
				<c:when test="${node.tipo eq 'layer'}">
					<fmt:formatDate value="${node.node.updateDate}"
						pattern="dd/MM/yyyy HH:mm" />
				</c:when>
			</c:choose>
		</display:column>
	</display:table>

	<div class="buttons-container">
		<a type="submit" id="imprime" class="button print" title="Imprimir"
			href="${printingUrl }" target="_blank">Imprimir</a>

	</div>
</body>
</html>