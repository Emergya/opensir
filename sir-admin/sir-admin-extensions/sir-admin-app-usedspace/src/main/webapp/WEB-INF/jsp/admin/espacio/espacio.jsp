<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib uri="http://displaytag.sf.net" prefix="display"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ page contentType="text/html;charset=UTF-8" language="java"
	pageEncoding="UTF-8"%>

<spring:url value="/admin/espacio" var="urlEspacio" />
<spring:url value="/admin/espacio/busqueda" var="urlBuscar" />
<spring:url var="urlImages" value="/img" />
<spring:url var="urlImages2" value="/gim" />
<html>
<head>
<title>Espacio Usado</title>
</head>
<body>

	<form:form commandName="spaceFilter" action="${urlBuscar}"
		method="post">

		<!-- Institucion -->
		<div class="span-20 last">
			<div class="span-3">
				<label class="labelCampo">Institución</label>
			</div>
			<div class="span-13">
				<form:select path="authorityId" id="institucion" multiple="false"
					cssClass="campo" cssErrorClass="campoError">
					<option value="">----</option>
					<form:options items="${instituciones}" itemLabel="authority"
						itemValue="id" />
				</form:select>
			</div>
			<div class="span-4 last">
				<button type="submit" class="button search">Actualizar</button>
			</div>
		</div>

		<div class="span-20 last" style="margin-top:40px">
			<display:table name="capasInstitucion" id="institucionItem"
				class="tableScroll">
				<display:column property="layerName" title="Nombre capa" 
					class="primera-cebreada"/>
				<display:column property="layerType" title="Tipo" />
				<display:column property="layerSpace" title="Espacio" 
					class="ultima-cebreada"/>
			</display:table>
		</div>
	</form:form>
</body>
</html>