<%@ page import="java.util.Map,org.apache.commons.lang3.StringUtils"%>
<%@ page contentType="text/html; charset=UTF-8" language="java"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<spring:url value="/css/blueprint" var="blueprintUrl" />
<spring:url value="/img" var="imgUrl" />
<spring:url value="/img/logotipo.png" var="logoUrl" />

<%
	Map<String, String> info = (Map<String, String>) request
			.getAttribute("info");

	// Details
	String[] keysInDetails = { "ano", "etapa",
			"nombreInstitucionResponsable", "nRegion", "nombreUnidadTecnica",
			"comuna" };
	String[] labelsInDetails = { "Año",
			"Etapa", "Institución Responsable", "Región",
			"Unidad Técnica", "Comuna" };

	// Financing sources
	String[] keysInFinancingSources = { "fuenteFinanciamiento", "costoTotalAjustado",
			"itemPresupuestario" };
	String[] labelsInFinancingSources = { "Fuente financiamiento",
			"Costo total ajustado", "Ítem presupuestario" };

	// Execution process
	String[] keysInInvestmentExecution = { "gastadoAnosAnteriores", "solicitadoAno",
			"saldoProximoAno", "saldoAnosRestantes", "totalAsignado", "totalPagado", "saldoPorAsignar",
			"asignacionDisponible" };
	String[] labelsInInvestmentExecution = { "Gastado años anteriores",
			"Solicitado año", "Saldo próximo año", "Saldo años restantes", "Total asignado", "Total pagado",
			"Saldo por asignar", "Asignación disponible" };
%>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<meta name="decorator" content="withoutDecoration" />
<link rel="stylesheet" href="${blueprintUrl}/estilo.css" type="text/css" />
<link rel="stylesheet" href="${blueprintUrl}/print.css" media="print"
	type="text/css" />
	
	<style>
		body {
			background: white;
			width: 100%;
			font-family:sans-serif, arial 
		}
		
		.inner {
			padding-left: 2em;
		}
		
		#footer {
			margin-top: 6em;
			text-align: center;
		}
	</style>
	
	<script type="text/javascript">
		window.onload = function() {
			window.print();
		}
	</script>
</head>
<body id="ficha-inversion">
	<div class="container ">
		<div id="cabeceraContainer">
			<img src="${logoUrl}"
				alt="Gobierno Regional - Región de Arica y Paranicota - Gobierno de Chile" />
		</div>
		<h1>Identificación Iniciativa</h1>
		<div id="datosGenerales">
			<div>
				<label class="labelInfo">Nombre iniciativa:</label> <span
					class="info">${info.name}</span>
			</div>
			<div>
				<label class="labelInfo">Código BIP:</label> <span class="info">${info.codBip}</span>
			</div>
		</div>

		<h1>Detalles</h1>
		<div id="details">
			<div class="inner">
				<%
					for (int i = 0; i < keysInDetails.length; i++) {
				%>
				<div>
					<label class="labelInfo"><%=labelsInDetails[i]%>:</label> <span
						class="info"><%=StringUtils.trimToEmpty(info
						.get(keysInDetails[i]))%></span>
				</div>
				<%
					}
				%>
			</div>
		</div>
		<h1>Aprobación de Recursos financieros</h1>
		<div id="financingSources">
			<div class="inner">
				<%
					for (int i = 0; i < keysInFinancingSources.length; i++) {
				%>
				<div>
					<label class="labelInfo"><%=labelsInFinancingSources[i]%>:</label>
					<span class="info"><%=StringUtils.trimToEmpty(info
						.get(keysInFinancingSources[i]))%></span>
				</div>
				<%
					}
				%>
			</div>
		</div>
		<h1>Proceso Ejecución Inversión</h1>
		<div id="executionProcess">
			<div class="inner">
				<%
					for (int i = 0; i < keysInInvestmentExecution.length; i++) {
				%>
				<div>
					<label class="labelInfo"><%=labelsInInvestmentExecution[i]%>:</label>
					<span class="info"><%=StringUtils.trimToEmpty(info
						.get(keysInInvestmentExecution[i]))%></span>
				</div>
				<%
					}
				%>
			</div>
		</div>
		<div id="footer">
			<div>
				<label class="labelInfo">Gobierno Regional de Arica y
					Paranicota</label>
			</div>
			<div>
				<label class="labelInfo">Av. Velasquez Nº 1775 | Fono:
					2207300 | Arica - Chile</label>
			</div>
		</div>
	</div>
</body>
</html>