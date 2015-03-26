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

	String[] keysInfoInPIBlo1 = { "institucionPostula", "estadoPI",
			"anyo", "fuenteFinanciamiento", "lineaFinanciera",
			"montoTotal", "sector", "etapa", "ubicacion", "descripcion" };
	String[] labelsInfoInPIBlo1 = { "Institución que postula",
			"Estado", "Año de postulación", "Fuente",
			"Linea Financiera", "Monto Total (postulado) (CL$)",
			"Sector", "Etapa que postula", "Ubicación", "Descripción" };

	String[] keysInfoInPIBlo2 = { "oficio", "carpeta", "ingresado",
			"requisitos", "vinculacion", "lineamiento",
			"politicaTurismo", "politicaCiencia", "plan",
			"reconstruccion", "programacion", "autoridadRegional",
			"mesa", "observaciones" };
	String[] labelsInfoInPIBlo2 = {
			"Oficio Conductor Institución que postula",
			"Carpeta Digital BIP",
			"Ingresado a Módulo Preinversión Chile Indica",
			"Requisitos Mínimos",
			"Vinculación con Instrumentos y Políticas ERD 2011-2020",
			"Lineamiento UDE", "Política Regional de Turismo",
			"Política Regional de Ciencia y Tecnología",
			"Plan O'Higgins", "Plan Recontrucción",
			"Convenios de Programación",
			"Seleccionado Autoridad Regional",
			"Mesa Técnica de revisión temática", "Observaciones" };

	String[] keysinfoInETE = { "institucionEvalua", "estadoETE",
			"fechaEstado", "fechaIngresoSNI" };
	String[] labelsinfoInETE = { "Institución que evalúa", "Estado",
			"Fecha último estado", "Fecha Ingreso S.N.I." };

	String[] keysinfoInARF = { "institucionApruebaRecursos",
			"estadoRF", "nacuerdo", "fechaAcuerdo", "montoAprobado" };
	String[] labelsinfoInARF = { "Institución que aprueba recursos",
			"Estado", "Nº Acuerdo", "Fecha Acuerdo CORE",
			"Monto Aprobado CORE (CL$)" };

	String[] keysinfoInPEI = { "institucionGasto", "unidad", "gastado",
			"pagado" };
	String[] labelsinfoInPEI = { "Institución que ejecuta Gasto",
			"Unidad Técnica", "Gastado años anteriores (CL$)",
			"Pagado a la fecha (año vigente) (CL$)" };
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN" "http://www.w3.org/TR/html4/strict.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<link rel="stylesheet" href="${blueprintUrl}/estilo.css" type="text/css" />
<link rel="stylesheet" href="${blueprintUrl}/print.css" media="print"
	type="text/css" />
<script type="text/javascript"
	src="http://ajax.googleapis.com/ajax/libs/jquery/1.7.2/jquery.min.js"></script>
</head>
<body id="ficha-inversion">
	<div class="container ">

		<div id="cabeceraContainer">
			<img src="${logoUrl}"
				alt="Gobierno Regional - Región del Libertador General Bernardo O'higgins - Gobierno de Chile" />
		</div>

		<div id="datosGenerales">
			<div>
				<label class="labelInfo">Nombre iniciativa:</label> <span
					class="info">${name}</span>
			</div>
			<div>
				<label class="labelInfo">Código BIP:</label> <span class="info">${codBip}</span>
			</div>
		</div>

		<c:if test="${infoinpi}">
			<h1>Postulación iniciativa</h1>
			<div id="postulacionIniciativa">
				<div class="inner">
					<c:if test="${infoinpiblo1}">
						<div id="blo1">
							<%
								for (int i = 0; i < keysInfoInPIBlo1.length; i++) {
											if (info.containsKey(keysInfoInPIBlo1[i])) {
							%>
							<div>
								<label class="labelInfo"><%=labelsInfoInPIBlo1[i]%>:</label> <span
									class="info"><%=StringUtils.trimToEmpty(info
									.get(keysInfoInPIBlo1[i]))%></span>
							</div>
							<%
								}
										}
							%>
						</div>
					</c:if>
					<c:if test="${IS_PUBLIC_SERVICE || IS_ADMIN}">
						<div id="blo2">
							<%
								for (int i = 0; i < keysInfoInPIBlo2.length; i++) {
							%>
							<div>
								<label class="labelInfo"><%=labelsInfoInPIBlo2[i]%>:</label> <span
									class="info"><%=StringUtils.trimToEmpty(info
								.get(keysInfoInPIBlo2[i]))%></span>
							</div>
							<%
								}
							%>
						</div>
					</c:if>
				</div>
			</div>
		</c:if>
		<c:if test="${infoinete}">
			<h1>Evaluación técnico económica</h1>
			<div id="evalTecEco">
				<div class="inner">
					<%
						for (int i = 0; i < keysinfoInETE.length; i++) {
								if (info.containsKey(keysinfoInETE[i])) {
					%>
					<div>
						<div>
							<label class="labelInfo"><%=labelsinfoInETE[i]%>:</label> <span
								class="info"><%=StringUtils.trimToEmpty(info
								.get(keysinfoInETE[i]))%></span>
						</div>
					</div>
					<%
						}
							}
					%>
				</div>
			</div>
		</c:if>
		<c:if test="${infoinarf}">
			<h1>Aprobación recursos financieros</h1>
			<div id="aproRecFin">
				<div class="inner">
					<%
						for (int i = 0; i < keysinfoInARF.length; i++) {
								if (info.containsKey(keysinfoInARF[i])) {
					%>
					<div>
						<div>
							<label class="labelInfo"><%=labelsinfoInARF[i]%>:</label> <span
								class="info"><%=StringUtils.trimToEmpty(info
								.get(keysinfoInARF[i]))%></span>
						</div>
					</div>
					<%
						}
							}
					%>
				</div>
			</div>
		</c:if>
		<c:if test="${infoinpei}">
			<h1>Proceso ejecución inversión</h1>
			<div id="procEjeInv">
				<div class="inner">
					<%
						for (int i = 0; i < keysinfoInPEI.length; i++) {
								if (info.containsKey(keysinfoInPEI[i])) {
					%>
					<div>
						<div>
							<label class="labelInfo"><%=labelsinfoInPEI[i]%>:</label> <span
								class="info"><%=StringUtils.trimToEmpty(info
								.get(keysinfoInPEI[i]))%></span>
						</div>
					</div>
					<%
						}
							}
					%>
				</div>
			</div>
		</c:if>

		<div id="footer">
			<div>
				<label class="labelInfo">Gobierno Regional del Libertador
					Bernardo O'Higgins</label>
			</div>
			<div>
				<label class="labelInfo">Plaza de Los Héroes s/n. Rancagua.</label>
			</div>
			<div>
				<label class="labelInfo">Fono: (72) 205900 - Fax (72) 237148</label>
			</div>
		</div>

	</div>
</body>
</html>
