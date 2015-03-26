<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://www.opensymphony.com/sitemesh/decorator"
	prefix="decorator"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<spring:url value="/moduloAnaliticoEstadistico" var="baseUrl" />
<spring:url value="/popup/moduloAnaliticoEstadistico/verDetalle" var="viewDetailsUrl"/>

<c:forEach items="${data}" var="datum" varStatus="loopVar">
	<div class="ui-draggable ui-dialog datumPreview span-5 last">
		<div class="ui-dialog-titlebar ui-helper-clearfix clear">
			<div class="ui-dialog-title">${datum.name }</div>
		</div>

		<div class="fieldRow clear">
			<div class="span-2">
				<label>Institución</label>
			</div>
			<div class="span-3 last">${datum.institutionName }</div>
		</div>
		<div class="fieldRow clear">
			<div class="span-2">
				<label>Categoría</label>
			</div>
			<div class="span-3 last">${datum.categoryName}</div>
		</div>
		<div class="fieldRow clear">
			<div class="span-2">
				<label>Tags</label>
			</div>
			<div class="span-3 last">
				<c:forTokens items="${datum.tags }" var="tag" delims=",">
					<a href="${baseUrl}/tag/${tag}"> ${tag}</a>
				</c:forTokens>
			</div>
		</div>
		<div class="fieldRow clear">
			<div class="span-2">
				<label>Fecha publicación</label>
			</div>
			<div class="span-3 last">${datum.requestAnswerDateLabel}</div>
		</div>
		<div class="fieldRow clear">
			<div class="span-2">
				<label>Descargas</label>
			</div>
			<div class="span-3 last">
				<fmt:formatNumber value="${datum.downloads}"/>
				</div>
		</div>
		<div class="footer clear">
			<a href="javascript:analytics.viewDetails('${datum.name}','${viewDetailsUrl}/${datum.identifier}')">
				Ver más</a>
		</div>
	</div>	
</c:forEach>



