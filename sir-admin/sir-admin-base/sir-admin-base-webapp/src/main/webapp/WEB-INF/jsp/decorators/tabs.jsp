<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="sec"
	uri="http://www.springframework.org/security/tags"%>
<%@ taglib uri="http://www.opensymphony.com/sitemesh/decorator"
	prefix="decorator"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>


<spring:url value="/admin" var="adminUrl" />
<spring:url value="/user" var="userUrl" />
<spring:url value="/estudios" var="adminRepositorioUrl" />
<spring:url value="/estudios" var="userRepositorioUrl" />
<spring:url value="/cartografico" var="userCartograficoUrl" />

<c:choose>
	<c:when test='${usuarioLogado != ""}'>
		<c:choose>
			<c:when test='${IS_ADMIN}'>
				<ul class="tabs parentTabs">
					<li id="general"
						class='primerTab ${module eq "general" ? "active" :"" }'><a
						href="${adminUrl}/usuarios"><span>General</span></a></li>
					<li id="cartografico"
						class='${module eq "cartografico" ? "active" :"" }'><a
						href="${adminUrl}/cartografico"><span>Cartográfico</span></a></li>
					<li id="repositorioEstudios"
						class='ultimoDerecha ${module eq "repositorio" ? "active" :"" }'><a
						href="${adminRepositorioUrl}/pendientes"><span>Repositorio de
							estudios</span></a></li>
				</ul>
			</c:when>
			<c:otherwise>
				<ul class="tabs parentTabs">
					<%--<li id="cartografico" class='primerTab ${module eq "cartografico" ? "active" :"" }'><a href="${userUrl}/cartografico">Cartografico</a></li>--%>
					<li id="cartografico"
						class='primerTab ${module eq "cartografico" ? "active" :"" }'><a
						href="${userCartograficoUrl}/layersAuthority"><span>Cartográfico</span></a></li>
					<li id="repositorioEstudios"
						class='ultimoDerecha ${module eq "repositorio" ? "active" :"" }'><a
						href="${userRepositorioUrl}/mias"><span>Repositorio de estudios</span></a></li>
				</ul>
			</c:otherwise>
		</c:choose>
	</c:when>
	<c:otherwise>
	</c:otherwise>
</c:choose>