<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib uri="http://displaytag.sf.net" prefix="display"%>
<%@ page contentType="text/html;charset=UTF-8" language="java"
	pageEncoding="UTF-8"%>



<spring:url var="toggleEnabledURL"
	value="/cartografico/toggleLayerEnabled" />
<spring:url var="toggleIsChannelURL"
	value="/cartografico/toggleLayerIsChannel" />
<spring:url var="addLayerToFolderURL"
	value="/cartografico/addLayerToFolder" />
<spring:url var="removeLayerFromFolderURL"
	value="/cartografico/removeLayerFromFolder" />
<spring:url var="moveLayerURL" value="/cartografico/moveLayer" />

<script type="text/javascript">
	urls.toggleLayerEnabled = "${toggleEnabledURL}";
	urls.toggleLayerIsChannel="${toggleIsChannelURL}";
	urls.addLayerToFolder= "${addLayerToFolderURL}";
	urls.removeLayerFromFolder="${removeLayerFromFolderURL}";
	urls.moveLayer="${moveLayerURL}";
	isInChannel = ${folderIsInChannel};
</script>

<spring:url value="/js/folderLayersSelector.js"
	var="folderLayersSelectorJs" />
<script type="text/javascript" src="${folderLayersSelectorJs }"></script>

<div class="folderLayerSelectorCtr">
	<div class="uLayerCtr">
		<label class="caption">Capas disponibles</label>
		<display:table name="sessionScope.unassignedLayers" id="uLayer"
			class="layerTable tableScroll">
			<display:setProperty name="basic.empty.showtable" value="true" />
			<display:column title="Nombre" class="primera-cebreada">
				<span title="${uLayer.layerLabel }">${uLayer.layerLabel}</span>
			</display:column>
			<display:column title="Habilitado">
				<c:choose>
					<c:when test="${uLayer.enabled }">
						<input type="checkbox" checked="checked"
							onchange="toggleLayerEnabled(false,${uLayer.id})" />
					</c:when>
					<c:otherwise>
						<input type="checkbox"
							onchange="toggleLayerEnabled(false,${uLayer.id})" />
					</c:otherwise>
				</c:choose>
			</display:column>
			<display:column title="Orden">
				<a href="javascript:void(0)"
					class="layerUpLink icon textless arrowUp" title="Subir"
					onclick="moveLayer(false, -1, ${uLayer.id},this)">Subir</a>
				<a href="javascript:void(0)"
					class="layerDownLink icon textless arrowDown" title="Bajar"
					onclick="moveLayer(false, 1, ${uLayer.id},this)">Bajar</a>
			</display:column>
			<display:column title="Añadir" class="ultima-cebreada">
				<a href="javascript:void(0)" class="icon textless add"
					title="Añadir" onclick="addLayerToFolder(${uLayer.id},this)">Añadir</a>
			</display:column>
		</display:table>
	</div>

	<div class="fLayerCtr">
		<label class="caption">Capas de la carpeta</label>
		<display:table name="sessionScope.folderLayers" id="fLayer"
			class="layerTable tableScroll">
			<display:setProperty name="basic.empty.showtable" value="true" />
			<display:column title="Nombre" class="primera-cebreada"> 
				<span title="${fLayer.layerLabel }">${fLayer.layerLabel}</span>
			</display:column>
			<display:column title="Habilitado">
				<c:choose>
					<c:when test="${fLayer.enabled }">
						<input type="checkbox" checked="checked"
							onchange="toggleLayerEnabled(true, ${fLayer.id}, checked)" />
					</c:when>
					<c:otherwise>
						<input type="checkbox"
							onchange="toggleLayerEnabled(true, ${fLayer.id}, checked)" />
					</c:otherwise>
				</c:choose>
			</display:column>
			<display:column title="Precarga">
				<c:choose>
					<c:when test="${folderIsInChannel and fLayer.enabled }">
						<c:choose>
							<c:when test="${fLayer.isChannel}">
								<input type="checkbox" checked="checked"
									id="isChannel-${fLayer.id}"
									onchange="toggleLayerIsChannel(${fLayer.id})" />
							</c:when>
							<c:otherwise>
								<input type="checkbox" id="isChannel-${fLayer.id}"
									onchange="toggleLayerIsChannel(${fLayer.id})" />
							</c:otherwise>
						</c:choose>
					</c:when>
					<c:otherwise>
						<input type="checkbox" disabled="disabled"
							id="isChannel-${fLayer.id}"
							onchange="toggleLayerIsChannel(${fLayer.id})" />
					</c:otherwise>
				</c:choose>
			</display:column>
			<display:column title="Orden">
				<a href="javascript:void(0)" title="Subir"
					class="layerUpLink icon textless arrowUp"
					onclick="moveLayer(true, -1, ${fLayer.id},this)">Subir</a>

				<a href="javascript:void(0)" title="Bajar"
					class="layerDownLink icon textless arrowDown"
					onclick="moveLayer(true, 1, ${fLayer.id},this)">Bajar</a>
			</display:column>
			<display:column title="Quitar" class="ultima-cebreada">
				<a href="javascript:void(0)" class="icon textless remove"
					title="Quitar" onclick="removeLayerFromFolder(${fLayer.id},this)">Quitar</a>
			</display:column>
		</display:table>
		<c:if test="${MAX_PRE_LAYERS_REACHED }">
			<span class="fieldError">No se puede marcar 'Precarga' en más
				de 5 capas</span>
		</c:if>
	</div>
</div>
