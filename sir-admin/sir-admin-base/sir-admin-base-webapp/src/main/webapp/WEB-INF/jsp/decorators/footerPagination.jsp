<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="sec"
	uri="http://www.springframework.org/security/tags"%>
<%@ taglib uri="http://www.opensymphony.com/sitemesh/decorator"
	prefix="decorator"%>

<c:choose>
	<c:when test="${numPages > 1}">
		<div class="span-24">
			<div align="center">
				<a href='<c:url value="${paginationUrl}"/>'> <img
					src="<c:url value='/img/primero.gif'/>" alt="primero" />
				</a>
				<c:if test="${first > 0}">
					<a
						href='<c:url value="${paginationUrl}"/>/<c:out value="${first-10}"/>/<c:out value="${last}"/>'>
						<img src="<c:url value='/img/anterior.gif'/>" alt="anterior" />
					</a>
				</c:if>
				<%
		for (int i = 1; i <= (Long)request.getAttribute("numPages"); i++){
			int last = (i*10) - 1;
			int first = last - 9;
			request.setAttribute("auxFirst", first);
		%>
				<c:choose>
					<c:when test="${first == auxFirst}">
						<a href='<c:url value="${paginationUrl}"/>/<%= first%>/<%= last%>'><label
							class="selectedPage"><%=i %></label></a>
					</c:when>
					<c:otherwise>
						<a href='<c:url value="${paginationUrl}"/>/<%= first%>/<%= last%>'><label><%=i %></label></a>
					</c:otherwise>
				</c:choose>
				<%} %>
				<c:if test="${first < auxFirst}">
					<a
						href='<c:url value="${paginationUrl}"/>/<c:out value="${first+10}"/>/<c:out value="${last+10}"/>'>
						<img src="<c:url value='/img/siguiente.gif'/>" alt="siguiente" />
					</a>
				</c:if>
				<a
					href='<c:url value="${paginationUrl}"/>/<c:out value="${auxFirst}"/>/<c:out value="${auxFirst+9}"/>'>
					<img src="<c:url value='/img/ultimo.gif'/>" alt="ultimo" />
				</a>
			</div>
		</div>
		<div class="span-24">
			<div align="center">
				Mostrando elementos del
				<c:out value="${first+1}" />
				al
				<c:out value="${last+1}" />
				de
				<c:out value="${numElements}" />
				elementos
			</div>
		</div>
	</c:when>
	<c:otherwise>
		<div class="span-24">
			<div align="center">
				Mostrando
				<c:out value="${numPages}" />
				elementos
			</div>
		</div>
	</c:otherwise>
</c:choose>

