<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib uri="http://displaytag.sf.net" prefix="display"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<spring:url value="/estudios/estado/" var="statusURL" />

<head>
	<title>Mis Solicitudes de Publicación</title>
	<script type="text/javascript">
		
		var statusURL= '${statusURL}';
	
		function showStudyStatus(status, id) {
			loadContentDialog("studyStatusDialog", "Solicitud de publicación "+status, statusURL+status+"/"+id, 450);
		}
	</script>
</head>
<html>
<body>
	
		<jsp:useBean id="list"
			type="java.util.List<com.emergya.ohiggins.cmis.bean.Publicacion>"
			scope="request" />
			
		

		<display:table name="list" sort="external" defaultsort="5"
			pagesize="${pageSize}" uid="lista" partialList="true"
                        id="study"
                        size="${size}" requestURI=""
			decorator="com.emergya.ohiggins.web.decorator.EstudioDecorator">
			<display:column property="anyo" title="Año" sortable="false"
				sortName="anyo" class="primera-cebreada"/>
			<display:column property="sector" title="Sector" sortable="false"
				sortName="sector" />
			<display:column property="nivelTerritorial" title="Nivel Territorial"
				sortable="false" sortName="nivelTerritorial" />
			<display:column property="name" title="Nombre del Estudio" sortable="false"
				sortName="name" />
			<display:column property="autor" title="Autor" sortable="false"
				sortName="autor" />
			<display:column property="institucion" title="Institucion"
				sortable="false" sortName="institucion" />
			<display:column title="Resumen" sortable="false"                                        
				sortName="resumen" class="ellipsedColumn">
                            <span title="${study.resumen}">${study.resumen}</span>
                        </display:column>
			<display:column property="estado" title="Estado" sortable="false"
				sortName="estado" class="ultima-cebreada"/>
		</display:table>
		
		<div id="studyStatusDialog"></div>
</body>
</html>