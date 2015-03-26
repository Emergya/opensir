
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<spring:url value="/admin" var="adminUrl" />

<div class="pestanas">
	<ul class="tabs subtabs">
		<li
			class='subTab primerSubTab ${submodule eq "usuarios" ? "subTabActive" :"" }'><a
			href="${adminUrl}/usuarios">Usuarios</a></li>
		<li
			class='subTab ${submodule eq "instituciones" ? "subTabActive" :""}'><a
			href="${adminUrl}/instituciones">Instituciones</a></li>
		<li class='subTab ${submodule eq "permisos" ? "subTabActive" :"" }'><a
			href="${adminUrl}/permisos">Permisos por tipo institución</a></li>
		<li class='subTab ${submodule eq "espacio" ? "subTabActive" :"" }'><a
			href="${adminUrl}/espacio">Espacio usado</a></li>
		<li
			class='subTab ${submodule eq "estadisticas" ? "subTabActive" :"" }'><a
			href="${adminUrl}/estadisticas">Estadísticas</a></li>
		<li class='subTab ${submodule eq "faq" ? "subTabActive" :"" }'><a
			href="${adminUrl}/faqs">FAQs</a></li>
		<li class='subTab ${submodule eq "contacto" ? "subTabActive" :"" }'><a
			href="${adminUrl}/contactos">Contacto</a></li>
	</ul>
</div>
