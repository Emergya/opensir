<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>


<spring:url value="/cartografico" var="cartograficoUrl" />
<spring:url value="/admin" var="adminUrl" />

<div class="pestanas">
	<c:choose>
		<c:when test='${usuarioLogado != ""}'>
			<c:choose>
				<c:when test='${IS_ADMIN}'>
					<ul class="tabs subtabs">
						<li
							class='subTab primerSubTab ${submodule eq "canales" ? "subTabActive" :""}'><a
							href="${cartograficoUrl}/canales">Canales</a></li>
						<li
							class='subTab primerSubTab ${submodule eq "layerTree" ? "subTabActive" :""}'><a
							href="${adminUrl}/cartografico">Árbol de capas</a></li>
						<li
							class='subTab ${submodule eq "solicitudesPublicacion" ? "subTabActive" :""}'><a
							href="${adminUrl}/cartografico/solicitudesPublicacion">Solicitudes
								de publicación</a></li>
						<li
							class='subTab ${submodule eq "inversion" ? "subTabActive" :""}'><a
							href="${cartograficoUrl}/inversion">Inversi&oacute;n</a></li>
					</ul>
				</c:when>
				<c:otherwise>
					<ul class="tabs subtabs">
						<li
							class='subTab primerSubTab ${submodule eq "layersAuthority" ? "subTabActive" :""}'><a
							href="${cartograficoUrl}/layersAuthority">Capas de la
								Institución</a></li>
						<li
							class='subTab ${submodule eq "solicitudesPublicacion" ? "subTabActive" :""}'><a
							href="${cartograficoUrl}/solicitudesPublicacion">Solicitudes
								de publicación</a></li>
					</ul>
				</c:otherwise>
			</c:choose>
		</c:when>
	</c:choose>

</div>