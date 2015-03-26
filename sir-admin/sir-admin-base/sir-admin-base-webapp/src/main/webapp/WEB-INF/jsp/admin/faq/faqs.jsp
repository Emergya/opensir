<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://displaytag.sf.net" prefix="display"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<html>
<head>
<title>FAQs</title>
<spring:url value="/admin/editarFaq" var="urlEditarFaq" />
<spring:url value="/admin/borrarFaq" var="urlBorrarFaq" />
<spring:url value="/admin/nuevoFaq" var="urlNuevoFaq" />
<spring:url value="¿Eliminar FAQ?" var="mensajeEliminarFaq" />

</head>
<body>
	<display:table name="listaFaqs" id="faqItem">
		<display:column property="titulo" title="Título"
			class="primera-cebreada" />
		<display:column title="Contenido" >
			<span  class="faqEllipsedColumn" title="${faqItem.respuestaRecortada}" >${faqItem.respuestaRecortada}</span>
		</display:column>
		<display:column property="modulo" title="Módulo" />
		<display:column title="Hab." class="ultima-cebreada">
			<c:if test="${not faqItem.habilitada}">
                    No               
                </c:if>
			<c:if test="${faqItem.habilitada}">
                    Si              
                </c:if>
		</display:column>

		<display:column title="Editar"
			headerClass="iconColumn" 
			class="sin-cebreado">
			<a href="${urlEditarFaq}/${faqItem.id}" class="icon textless edit" title="Editar"> 
				Editar
			</a>
		</display:column>
		<display:column title="Borrar" class="sin-cebreado"
			headerClass="iconColumn">
			<a href="${urlBorrarFaq}/${faqItem.id}" title="Borrar"
				onclick="return confirm('${mensajeEliminarFaq}');"
				class="icon textless delete">
				Borrar
			</a>
		</display:column>
	</display:table>

	<!-- Nueva Faq -->
	<div class="buttons-container">
		<a href="${urlNuevoFaq}" class="button new">Nueva FAQ</a>
	</div>
</body>
</html>