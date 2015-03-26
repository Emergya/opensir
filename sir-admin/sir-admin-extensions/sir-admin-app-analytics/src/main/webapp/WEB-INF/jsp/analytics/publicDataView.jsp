<%-- 
    Document   : publicDataView
    Created on : 27-jun-2013, 13:34:37
    Author     : lroman
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://www.opensymphony.com/sitemesh/decorator"
	prefix="decorator"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<html>
<head>
<meta name="decorator" content="masterSinTabs" />

<spring:url value="/moduloAnaliticoEstadistico" var="baseUrl" />

<spring:url value="/popup/moduloAnaliticoEstadistico/cargaPagina"
	var="loadPageUrl" />

<script type="text/javascript"
	src="${baseUrl }/../js/jquery.tagcloud/jquery.tagcloud.js"></script>
<script type="text/javascript"
	src="${baseUrl }/../js/masonry/jquery.masonry.js"></script>
<script type="text/javascript"
	src="${baseUrl }/../js/masonry/jquery.masonry.ordered.js"></script>
<script type="text/javascript"
	src="${baseUrl }/../js/infinite-scroll/jquery.infinitescroll.js"></script>
<script type="text/javascript"
	src="${baseUrl }/../js/analyticsPublic.js"></script>

<link rel="stylesheet" href="${baseUrl }/../css/analytics.css" />

<spring:url var="trackingUrl" value="/stats/modules/Analítico Estadístico"/>
<script type="text/javascript">
    $(document).on("ready",function(){       
        $.post('${trackingUrl}');
    });
    
</script>
</head>

<body>
	<div id="faq-page">

		<div class="titulo">
			<h4>Consulta de Datos de la Región</h4>
		</div>

		<div class="padding">

			<div>
				<a href="${baseUrl}/masRecientes" class="button ${recentBtnClass}">Más
					recientes</a> <a href="${baseUrl}/masDescargados"
					class="button ${downloadedBtnClass}">Más descargados</a> 
					
				<a href="javascript:analytics.toggleSearchForm()"
					title="Pulsa para mostrar/ocultar el formulario de búsqueda">
					Búsqueda avanzada</a>
			</div>



			<div class="span-18">
				<div id="searchFormCtr" class="span-16 last ${searchFormCtrClass }">
					<jsp:include page="searchForm.jsp" flush="true" />
				</div>

				<div class="span-18 last columnHeader">
					<label>Mostrando resultados ${resultsLabel}</label>
				</div>
				<c:if test="${empty data }">
					<div class="span-18 padding">No hay datos para mostrar,
						pruebe cambiando el filtro.</div>
				</c:if>
				<div class="span-18 last" id="dataItemsCtr">


					<jsp:include page="dataGrid.jsp"></jsp:include>

					<div class="loadMoreCtr clear">
						<c:if test="${not empty nextPage }">
							<a class="loadMore" href="${loadPageUrl}/${filterId}/${nextPage}">
								Nueva página</a>
						</c:if>
					</div>
				</div>
			</div>
			<div class="span-4 last columnHeader">
				<label>Categorías</label>

				<div id="categoriesCloud" class="tagCloudCtr">
					<c:forEach var="category" items="${categories}">
						<a href="${baseUrl}/categoria/${category.id}" rel="1">
							${category.name}</a>
					</c:forEach>
				</div>


				<label>Etiquetas</label>
				<div id="tagsCloud" class="tagCloudCtr">
					<c:forEach var="tag" items="${tags}">
						<a href="${baseUrl}/tag/${tag.name}" rel="${tag.useCount}">
							${tag.name}</a>
					</c:forEach>
				</div>

			</div>
			<div class="clear"></div>
		</div>
	</div>

	<div id="viewDetailsPopup"></div>

</body>
</html>
