<%-- 
    Document   : dataForm
    Created on : 20-jun-2013, 12:11:13
    Author     : lroman
--%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib uri="http://displaytag.sf.net" prefix="display"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="html" tagdir="/WEB-INF/tags/html"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>

<html>
<head>

<spring:url value="/popup/moduloAnaliticoEstadistico/verDetalle" var="viewDetailsUrl"/>
<spring:url var="downloadUrl" value="/admin/analitico/descarga"/>
<spring:url var="searchTagurl" value="/admin/analitico/buscaTag" />
<spring:url var="cancelUrl" value="/admin/analitico/datos" />
<spring:url var="tagitCssUrl"
	value="/css/jquery.tag-it/jquery.tagit.css" />
<spring:url var="tagitJsUrl" value="/js/jquery.tag-it/tag-it.js" />
<spring:url var="analyticsJsUrl" value="/js/analytics.js" />
<spring:url var="prepareRejectionUrl"
	value="/popup/analitico/verRechazo" />
<spring:url var="acceptRequestUrl"
	value="/admin/analitico/aceptarPublicacion" />
	
<spring:url var="acceptRequestUrl"
	value="/admin/analitico/publicarSolicitudModificada" />

<spring:url var="cssBaseUrl" value="/css" />
<spring:url var="jsBaseUrl" value="/js" />

<script src="${jsBaseUrl}/analyticsAdmin.js" type="text/javascript"></script>

<link rel="stylesheet" href="${tagitCssUrl}"></link>
<link rel="stylesheet" href="${cssBaseUrl}/analyticsAdmin.css"></link>


<script type="text/javascript">
	urls.analyticsTagsSearchUrl = "${searchTagurl}";
</script>

<script type="text/javascript" src="${tagitJsUrl}"></script>
<script type="text/javascript" src="${analyticsJsUrl}"></script>

<title>Solicitud de Publicación</title>
</head>
<body>
	<form:form commandName="datumDto" method="post"
		action="${acceptRequestUrl }" modelAttribute="analyticsDataDto"
		onsubmit="return confirm('Se publicarará el dato analítico estadístico. ¿Desea continuar?')">
		<form:hidden path="identifier" id="identifier" name="identifier" />
		<div class="span-22 last">
			<div class="span-11">
				<div class="span-11 last">
					<html:inputField name="name" label="Nombre deseado"
						lblCtrCssClass="span-3" required="true" ctrCssClass="span-8 last"
						cssClass="campo" cssErrorClass="campoError"
						errorTextClass="fieldError" />
				</div>
			</div>
			<div class="span-11 last">
				<div class="span-3">
					<label class="labelCampo">Categoría</label>
				</div>

				<div class="span-8 last">

					<form:select path="categoryId" id="categoryId" cssClass="campo"
						cssErrorClass="campoError">
						<form:option value="" label="---"></form:option>
						<form:options items="${categories}" itemLabel="name"
							itemValue="id"></form:options>
					</form:select>

					<span class="help-inline" id="help-inline-categoryId"> <form:errors
							path="categoryId" cssClass="fieldError" />
					</span>

				</div>
			</div>
		</div>

		<div class="span-11">

			<div class="span-11">
				<div class="span-11 last">
					<div class="span-3">
						<label class="labelCampo">Institución</label>
					</div>

					<div class="span-8 last">

						<form:select path="institutionId" id="institutionId"
							cssClass="campo" cssErrorClass="campoError">
							<form:option value="" label="---"></form:option>
							<form:options items="${institutions}" itemLabel="authority"
								itemValue="id" />
						</form:select>

						<span class="help-inline" id="help-inline-institutionId"> <form:errors
								path="institutionId" cssClass="fieldError" />
						</span>

					</div>
				</div>
			</div>
			<div class="span-11 last">
				<html:inputField name="author" label="Autor" lblCtrCssClass="span-3"
					ctrCssClass="span-8 last" cssClass="campo"
					cssErrorClass="campoError" errorTextClass="fieldError" />
			</div>
			<div class="span-11 last">
				<div class="span-3">
					<label class="labelCampo">Licencia</label>
				</div>

				<div class="span-8 last">

					<form:select path="license" name="license" id="license"
						cssClass="campo" cssErrorClass="campoError">
						<form:option value="" label="---"></form:option>
						<form:options itemLabel="label"></form:options>
					</form:select>

					<span class="help-inline" id="help-inline-license"> <form:errors
							path="license" cssClass="fieldError" />
					</span>

				</div>
			</div>
			<div class="span-11 last">
				<div class="span-3">
					<label class="labelCampo">Ámbito territorial</label>
				</div>

				<div class="span-8 last">

					<form:select path="geoContextId" name="geoContextId"
						id="geoContextId" cssClass="campo" cssErrorClass="campoError">

						<form:option value="" label="---" />
						<form:options items="${geoContexts}" itemLabel="name"
							itemValue="id" />
					</form:select>

					<span class="help-inline" id="help-inline-geoContextId"> <form:errors
							path="geoContextId" cssClass="fieldError" />
					</span>

				</div>
			</div>
			<div class="span-11 last">
				<html:inputField name="tags" label="Etiquetas"
					lblCtrCssClass="span-3" ctrCssClass="span-8 last"
					cssClass="campo tagsField" cssErrorClass="campoError"
					errorTextClass="fieldError" />
			</div>
		</div>
		<div class="span-11 last">
			<div class="span-3">
				<label class="labelCampo">Descripción</label>
			</div>

			<div class="span-8 last">

				<form:textarea path="description" items="${categories}"
					id="description" cssClass="campo" cssErrorClass="campoError" />

				<span class="help-inline" id="help-inline-description"> <form:errors
						path="description" cssClass="fieldError" />
				</span>

			</div>
		</div>
		</div>

		<div class="buttons-container span-22">
			<div class="span-13">
				<a class="button download"
					href="${downloadUrl}/${analyticsDataDto.identifier}">
					Descargar
				</a>
				<a class="button view viewDetails" href="javascript:void(0)"
					href="javascript:void(0)"
					onclick="analyticsAdmin.viewDetails('${datumDto.name}','${viewDetailsUrl}/${analyticsDataDto.identifier}')">
					Previsualizar
				</a>				
			</div>
			<div class="span-9 last buttons-right"">
				<button class="button accept ok publish">
					Publicar </button> 
				<a class="button reject deny reject"
					href="javascript:void(0)"
					onclick="loadContentDialog('rejectDialog', 'Rechazar solicitud', '${prepareRejectionUrl}/${analyticsDataDto.identifier}', 420)">
					Rechazar 
				</a>
				 <a class="button secondary cancel" href="${cancelUrl}">Volver </a>
			</div>
		</div>

	</form:form>

	<div id="rejectDialog"></div>
	<div id="viewDetailsPopup"></div>
</body>

</html>
