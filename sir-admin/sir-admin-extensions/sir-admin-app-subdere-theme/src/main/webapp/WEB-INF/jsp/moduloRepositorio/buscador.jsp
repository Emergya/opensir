<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ page contentType="text/html;charset=UTF-8" language="java"
	pageEncoding="UTF-8"%>

<head>

<spring:url value="/js" var="jsPath" />
<script type="text/javascript" src="${jsPath}/studiesSearch.js"></script>

<spring:url var="urlImages" value="/img" />
<spring:url value="/repositorioEstudios/buscar" var="urlBuscar" />

<spring:url value="/css" var="cssPath" />

<spring:url var="trackingUrl" value="/stats/modules/Repositorio"/>
<script type="text/javascript">
    $(document).on("ready",function(){       
        $.post('${trackingUrl}');
    });
    
</script>
</head>
<html>
<body>
	<div class="title">
		<div class="span-24">
			<h2>REPOSITORIO DE ESTUDIOS</h2>
		</div>
		<div class="clear"></div>
	</div>
	<form:form commandName="estudioFilter" action="${urlBuscar}"
		id="studiesSearch_form" method="get">

		<jsp:include page="buscadorFormFieldsInclude.jsp" flush="true"></jsp:include>

		<form:hidden path="nivelTerritorial" id="studiesSearch_area" />

		<div class="span-21 searchMapContainer">
			<div class="span-18 ambito">
				<h5>Ámbito Territorial</h5>
			</div>

			<div>
				<button class="button region"
					onClick="studiesSearch.doSearch('${region.name}')">REGIÓN
					DE ARICA Y PARINACOTA</button>
			</div>
			<div class="span-10 indented">


				<div class="provincesContainer">
					<div class="first-button">
						<button class='button arica'
							onClick="studiesSearch.doSearch('Provincia de Arica')">Provincia de
							Arica</button>
					</div>
					<div class="second-button">
						<button class='button parinacota'
							onClick="studiesSearch.doSearch('Parinacota')">Provincia
							de Parinacota</button>
					</div>
				</div>


				<!-- This is the way to retrieve from the database
							the localities and show them in a 3 column numbered list  -->
				<div class="localitiesContainer">
					<c:forEach var="locality" items="${localities}"
						varStatus="iterStatus">

						<c:if test="${iterStatus.index%2==0}">
							<!-- We add the columns each 2 elements -->
							<div class="span-3 last">
								<ol class="multi-column" start="${iterStatus.index+1 }">
						</c:if>

						<li><a href="javascript:void(0)"
							onclick="studiesSearch.doSearch('${locality.name}')">
								${locality.name}</a></li>

						<c:if test="${iterStatus.index%2==1 || iterStatus.last}">
							<!--We close in the last element-->
							</ol>
							</div>
						</c:if>
				</c:forEach>
			</div>
		</div>
		<div class="span-10 last map">
			<jsp:include page="searchMap.jsp" />
		</div>

		</div>
	</form:form>
</body>
</html>