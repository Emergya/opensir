<%@page import="java.util.Map,org.apache.commons.lang3.StringUtils"%>
<%@ page contentType="text/html; charset=UTF-8" language="java"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

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
			"saldoProximoAno", "saldoAnosRestantes", "totalAsignado", "totalPagado", "saldoPorAsignar", "asignacionDisponible"};
	String[] labelsInInvestmentExecution = { "Gastado años anteriores",
			"Solicitado año", "Saldo próximo año", "Saldo años restantes", "Total asignado", "Total pagado", "Saldo por asignar", "Asignación disponible"};
%>
<div class="container ">

	<div id="headerPopup">
		<div>
			<label class="labelInfo">Nombre iniciativa:</label>
			<div class="nameIni">${info.name}</div>
		</div>
		<div>
			<label class="labelInfo">Código BIP:</label> <span class="nameIni">${info.codBip}</span>
		</div>
	</div>




	<div id="details">

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
	<div id="financingSources">

		<%
			for (int i = 0; i < keysInFinancingSources.length; i++) {
		%>
		<div>
			<label class="labelInfo"><%=labelsInFinancingSources[i]%>:</label> <span
				class="info"><%=StringUtils.trimToEmpty(info
						.get(keysInFinancingSources[i]))%></span>
		</div>
		<%
			}
		%>

	</div>
	<div id="executionProcess">

		<%
			for (int i = 0; i < keysInInvestmentExecution.length; i++) {
		%>
		<div>
			<label class="labelInfo"><%=labelsInInvestmentExecution[i]%>:</label> <span
				class="info"><%=StringUtils.trimToEmpty(info
						.get(keysInInvestmentExecution[i]))%></span>
		</div>
		<%
			}
		%>

	</div>
</div>