<%@ page contentType="text/html; charset=UTF-8" language="java"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://www.opensymphony.com/sitemesh/decorator"
	prefix="decorator"%>
<c:url value="/img" var="imgUrl" />
<c:url value="/img/logotipo.png" var="logotipoUrl" />
<c:url value="/img/cabecera.jpg" var="cabeceraUrl" />
<c:url value="/" var="startPage" />

<div class="container">
	<%-- <div id="logo" class="span-4">
		<a href="${startPage}" title="Inicio"> <img
			style="height: 100%; width: 100%" src="${logotipoUrl}"
			alt="Logotipo de la Institución" />
		</a>
	</div>
	<div id="cabecera" class="span-20 last">
		<img style="height: 100%; width: 100%" src="${cabeceraUrl}"
			alt="Imagen de cabecera" />
	</div>--%>

	<jsp:include page="userForm.jsp" flush="true" />

	<div id="loadingDiv" style="display: none">
		<div class="overlay"></div>
		<div class="indicator">Cargando...</div>
	</div>
</div>
