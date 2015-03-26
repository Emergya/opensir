<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>

<spring:url var="urlImages" value="/img" />
<spring:url var="urlJS" value="/js" />

<html>
<head>
<title>Proyectos de Inversión</title>
<script type="text/javascript"></script>
<!-- Scripts js utilizados -->

<script type="text/javascript" src="${urlJS}/investments.js"></script>
</head>
<body>
	<form:errors path="uploadForm.*" cssClass="error" element="div" />


	<spring:url value="/cartografico/inversion2" var="formUrl" />
	<form:form action="${formUrl}" commandName="uploadForm"
		enctype="multipart/form-data" method="post">

		<div class="span-12">
			<label class="labelArriba" for="shpProyectosGeo">SHP
				Proyectos georreferenciados</label>
		</div>
		<!-- /span-11 -->
		<div class="span-8 last" style="text-align: center">
			<label class="labelArriba">&Uacute;ltima actualizaci&oacute;n</label>
		</div>
		<!-- /last -->
		<div class="span-12 fileinputs">
			<input class="file span-11" id="shpProyectosGeo" type="file"
				name="shpProyectosGeo" accept="application/zip" />
			<form:errors path="shpProyectosGeo"
				cssClass="help-inline campoError span-24" element="div" />
		</div>
		<!-- /fileinputs -->
		<div class="span-8 last">
			<button type="button" class="button accept" value="shpProyectosGeo">Actualizar</button>
			<fmt:formatDate type="BOTH" value="${SHP_PROYECTOS_GEO}" />
		</div>
		<!-- /last -->
		<div class="span-24">
			<label class="labelArriba" for="basePreGore">XLS Acuerdos
				CORE</label>
		</div>
		<!-- /span-24 -->
		<div class="span-12 fileinputs">
			<input class="file span-11" id="acuerdosCore" type="file"
				name="acuerdosCore" accept="application/vnd.ms-excel" />

			<form:errors path="acuerdosCore"
				cssClass="help-inline campoError span-24" element="div" />
		</div>
		<!-- /fileinputs -->
		<div class="span-8 last">
			<button type="button" class="button accept" value="acuerdosCore">Actualizar</button>
			<fmt:formatDate type="BOTH" value="${ACUERDOS_CORE}" />
		</div>
		<!-- /last -->
		<div class="span-24">
			<label class="labelArriba" for="basePreGore">XLS Base Unidad
				Preinversi&oacute;n GORE</label>
		</div>
		<!-- /span-24 -->
		<div class="span-12 fileinputs">
			<input class="file span-11" id="basePreGore" type="file"
				name="basePreGore" accept="application/vnd.ms-excel" />


			<form:errors path="basePreGore"
				cssClass="help-inline campoError span-24" element="div" />
		</div>
		<!-- /fileinputs -->
		<div class="span-8 last">
			<button type="button" class="button accept" value="basePreGore">Actualizar</button>
			<fmt:formatDate type="BOTH" value="${BASE_PREINVERSION_GORE}" />
		</div>
		<!-- /last -->
		<div class="span-24">
			<label class="labelArriba" for="chileindicaEjec">XLS
				Proyectos Chileindica M&oacute;dulo Ejecuci&oacute;n</label>
		</div>
		<!-- /span-24 -->
		<div class="span-12 fileinputs">
			<input class="file span-11" id="chileindicaEjec" type="file"
				name="chileindicaEjec" accept="application/vnd.ms-excel" />

			<form:errors path="chileindicaEjec"
				cssClass="help-inline campoError span-24" element="div" />
		</div>
		<!-- /fileinputs -->
		<div class="span-8 last">
			<button type="button" class="button accept" value="chileindicaEjec">Actualizar</button>
			<fmt:formatDate type="BOTH" value="${CHILEINDICA_EJECUCION}" />
		</div>
		<!-- /last -->
		<div class="span-24">
			<label class="labelArriba" for="chileindicaEjecDetalle"> XLS
				Proyectos Chileindica M&oacute;dulo Ejecuci&oacute;n Detalle </label>
		</div>
		<!-- /span-24 -->
		<div class="span-12 fileinputs">
			<input class="file span-11" id="chileindicaEjecDetalle" type="file"
				name="chileindicaEjecDetalle" accept="application/vnd.ms-excel" />
			<form:errors path="chileindicaEjecDetalle"
				cssClass="help-inline campoError span-24" element="div" />
		</div>
		<!-- /fileinputs -->
		<div class="span-8 last">
			<button type="button" class="button accept"
				value="chileindicaEjecDetalle">Actualizar</button>
			<fmt:formatDate type="BOTH" value="${CHILEINDICA_EJECUCION_DETALLE}" />
		</div>
		<!-- /last -->
		<div class="span-24">
			<label class="labelArriba" for="chileindicaPreinversion">XLS
				Proyectos Chileindica M&oacute;dulo Preinversi&oacute;n</label>
		</div>
		<!-- /span-24 -->
		<div class="span-12 fileinputs">
			<input class="file span-11" id="chileindicaPreinversion" type="file"
				name="chileindicaPreinversion" accept="application/vnd.ms-excel" />
			<form:errors path="chileindicaPreinversion"
				cssClass="help-inline campoError span-24" element="div" />
		</div>
		<!-- /fileinputs -->
		<div class="span-8 last">
			<button type="button" class="button accept"
				value="chileindicaPreinversion">Actualizar</button>
			<fmt:formatDate type="BOTH" value="${CHILEINDICA_PREINVERSION}" />
		</div>
		<!-- /last -->
		<div class="span-24">

			<label class="labelArriba" for="proyectosDacg">XLS Proyectos
				DACG</label>
		</div>
		<!-- /span-24 -->
		<div class="span-12 fileinputs">
			<input class="file span-11" id="proyectosDacg" type="file"
				name="proyectosDacg" accept="application/vnd.ms-excel" />
			<form:errors path="proyectosDacg"
				cssClass="help-inline campoError span-24" element="div" />
		</div>
		<!-- /fileinputs -->
		<div class="span-8 last">
			<button type="button" class="button accept" value="proyectosDacg">Actualizar</button>
			<fmt:formatDate type="BOTH" value="${PROYECTOS_DACG}" />
		</div>
		<!-- /last -->
		<div class="span-24">
			<label class="labelArriba" for="projMideso">XLS Proyectos
				MIDESO </label>
		</div>
		<!-- /span-24 -->
		<div class="span-12 fileinputs">
			<input class="file span-11" id="projMideso" type="file"
				name="projMideso" accept="application/vnd.ms-excel" />
			<form:errors path="projMideso"
				cssClass="help-inline campoError span-24" element="div" />
		</div>
		<!-- /fileinputs -->
		<div class="span-8 last">
			<button type="button" class="button accept" value="projMideso">Actualizar</button>
			<fmt:formatDate type="BOTH" value="${PROYECTOS_MIDESO}" />
		</div>
		<!-- /last -->

	</form:form>
</body>
</html>
