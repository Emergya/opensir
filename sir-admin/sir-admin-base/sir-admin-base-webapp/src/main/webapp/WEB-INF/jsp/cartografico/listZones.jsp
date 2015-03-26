<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ page contentType="text/html;charset=UTF-8" language="java"
    pageEncoding="UTF-8"%>
    
<select name="zoneSeleccionada" id="zoneSeleccionada" style="width:180px;">
         	<option value="">----</option>
           	<%--<form:options items="${ambitoTerritorial}" itemLabel="name" itemValue="id" />--%>
           	<c:forEach var="l" items="${ambitoTerritorial}" >
           		<option value="${l.id}">${l.name}</option>
           	</c:forEach>
</select>