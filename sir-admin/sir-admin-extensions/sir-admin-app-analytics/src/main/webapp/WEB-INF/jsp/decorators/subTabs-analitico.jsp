<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>


<spring:url value="/admin/analitico" var="analyticsUrl" />

<div class="pestanas">
		<c:choose>
			<c:when test='${IS_ADMIN}'>
				<ul class="tabs subtabs">
					<li
						class='subTab primerSubTab ${submodule eq "categorias" ? "subTabActive" :""}'><a
						href="${analyticsUrl}/categorias">Canales</a></li>
					<li
						class='subTab primerSubTab ${submodule eq "datos" ? "subTabActive" :""}'><a
						href="${analyticsUrl}/datos">Datos públicos</a></li>
					<li
						class='subTab ${submodule eq "solicitudesPublicacion" ? "subTabActive" :""}'><a
						href="${analyticsUrl}/solicitudesPublicacion">Solicitudes
							de publicación</a></li>
				</ul>
			</c:when>
			<c:otherwise>
				<ul class="tabs subtabs">
					<li
						class='subTab primerSubTab ${submodule eq "datos" ? "subTabActive" :""}'><a
						href="${analyticsUrl}/datos">Mis datos</a></li>
					<li
						class='subTab ${submodule eq "solicitudesPublicacion" ? "subTabActive" :""}'><a
						href="${analyticsUrl}/solicitudesPublicacion">Solicitudes
							de publicación</a></li>
				</ul>
			</c:otherwise>
		</c:choose>
</div>