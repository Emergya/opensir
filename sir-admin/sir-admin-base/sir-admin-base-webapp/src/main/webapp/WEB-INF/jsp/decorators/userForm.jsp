<%--<%@page import="com.emergya.ohiggins.security.DummyAuthentificationProvider"--%>
<%@page import="com.emergya.ohiggins.security.OhigginsUserDetails"%>
<%@page
	import="org.springframework.security.core.context.SecurityContext"%>
<%@ page contentType="text/html; charset=UTF-8" language="java"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://www.opensymphony.com/sitemesh/decorator"
	prefix="decorator"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<%
	SecurityContext context = (SecurityContext) session
			.getAttribute("SPRING_SECURITY_CONTEXT");
	String usuarioLogado = "";
	if (context != null
			&& context.getAuthentication().isAuthenticated()
			&& !context.getAuthentication().getAuthorities().isEmpty()) {
		usuarioLogado = context.getAuthentication().getName();
		//request.setAttribute("IS_ADMIN", context.getAuthentication().getAuthorities().contains(DummyAuthentificationProvider.ADMIN_AUTH));
		request.setAttribute(
				"IS_ADMIN",
				context.getAuthentication().getAuthorities()
						.contains(OhigginsUserDetails.ADMIN_AUTH));
	}
	request.setAttribute("usuarioLogado", usuarioLogado);
%>

<spring:url value="/controlUsuarioLogado" var="urlAdministracion" />
<%--<c:choose> --%>
	<%--<c:when test="${usuarioLogado != ''}">--%>
	<c:if test="${usuarioLogado != ''}">
		<div id="userDiv" class="userLogin">

			<div id="user_name">
				<a href="${urlAdministracion}"
					alt="Acceder al área de administración"
					title="Acceder al área de administración"><%=usuarioLogado%></a>
			</div>
			<div class="separator">|</div>
			<div id="user_logout">
				<a href="<c:url value="/logout"/>"> <img alt="Logout"
					src='<c:url value="/img/logout.png"/>' title="Logout" />
				</a>
			</div>
		</div>
	</c:if>
	<%--</c:when>--%>
	<%--<c:otherwise>
		<div class="login-box">
			<div class="centeredContainer">
				<h1></h1>
			</div>
			<c:if test="${not empty param.login_error}">
				<font color="red"><c:out
						value="${SPRING_SECURITY_LAST_EXCEPTION.message}" />. </font>
			</c:if>
			<div id="loginForm">
				<div class="body_form_labeled_60">
					<form name="f" action="<c:url value='/j_spring_security_check'/>"
						method="POST">

						<div class="line">

							<input type='text' name='j_username'
								value='<c:if test="${not empty param.login_error}"><c:out value="${SPRING_SECURITY_LAST_USERNAME}"/></c:if>'
								placeholder="Usuario" />
						</div>

						<div class="line">
							<input type='password' name='j_password' placeholder="Contraseña">
						</div>

						<div class="botonera">
							<div class="left_50">
								<div class="centeredContainer">
									<input name="submit" type="submit" class="inputBtn"
										value="Enviar">
								</div>
							</div>
							<div class="right_50">
								<div class="centeredContainer">
									<input name="reset" type="reset" class="inputBtn"
										value="Restablecer">
								</div>
							</div>
						</div>

						<div class="line checkbox">
							<input type="checkbox" name="_spring_security_remember_me"
								id="rememberme">
							<p>
								<strong><label for="rememberme">Recordarme</label></strong>
							</p>

						</div>

					</form>
				</div>
			</div>
		</div>-->
	</c:otherwise>--%>
<%--</c:choose>--%>
