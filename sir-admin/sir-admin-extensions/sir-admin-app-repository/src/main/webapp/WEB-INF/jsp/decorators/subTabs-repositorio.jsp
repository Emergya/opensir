<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<spring:url value="/estudios" var="estudiosUrl" />
<spring:url value="/gestionestudios" var="gestionestudiosUrl" />

<div class="pestanas">
	<c:choose>
		<c:when test='${usuarioLogado != ""}'>
			<c:choose>
				<c:when test='${IS_ADMIN}'>
					<ul class="tabs subtabs">
						<li
							class='subTab primerSubTab ${submodule eq "gestion" ? "subTabActive" :"" }'><a
							href="${gestionestudiosUrl}/buscador">Gestión estudios</a></li>
						<li
							class='subTab ${submodule eq "pendientes" ? "subTabActive" :"" }'><a
							href="${estudiosUrl}/pendientes">Pendientes</a></li>
					</ul>
				</c:when>
				<c:otherwise>
					<ul class="tabs subtabs">
						<li
							class='subTab primerSubTab ${submodule eq "solicitud" ? "subTabActive" :""}'><a
							href="${estudiosUrl}/solicitud">Solicitud</a></li>
						<li class='subTab ${submodule eq "mias" ? "subTabActive" :"" }'><a
							href="${estudiosUrl}/mias">Mias</a></li>
					</ul>
				</c:otherwise>
			</c:choose>
		</c:when>
	</c:choose>
</div>