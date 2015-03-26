<%@ page contentType="text/html; charset=UTF-8" language="java"%>
<%@ taglib uri="http://www.opensymphony.com/sitemesh/decorator"
	prefix="decorator"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@page
	import="org.springframework.security.core.context.SecurityContext"%>
<%@page import="com.emergya.ohiggins.security.OhigginsUserDetails"%>
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
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN" "http://www.w3.org/TR/html4/strict.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<meta http-equiv="X-UA-Compatible" content="IE=9">
<spring:url value="/faq" var="faqUrl" />
<spring:url value="/contacto" var="contactoUrl" />
<spring:url value="/css/blueprint" var="blueprintUrl" />
<spring:url value="/css/blueprint/css_browser_selector.js"
	var="cssBrowserSelectorJsUrl" />
<spring:url value="/css/jquery-ui/jquery-ui-1.9.2.custom.min.css"
	var="jqueryThemeRoller" />

<spring:url value="/img/favicon.ico" var="faviconUrl" />
<link rel="shortcut icon" href="${faviconUrl}">

<spring:url value="/js" var="urlJS" />

<spring:url value="/css/blueprint/estilo.css" var="estiloCss" />
<spring:url value="/css/blueprint/screen.css" var="screenCss" />
<spring:url value="/css/blueprint/print.css" var="printCss" />
<spring:url value="/css/blueprint/ie.css" var="ieCss" />

<spring:url value="/css/blueprint/icons.css" var="iconsCss" />



<link rel="stylesheet" href="${screenCss}" type="text/css"
	media="screen, projection" />
<link rel="stylesheet" href="${printCss}" type="text/css" media="print" />
<link rel="stylesheet" href="${jqueryThemeRoller}" type="text/css" />

<link rel="stylesheet" href="${estiloCss}" type="text/css" />
<link rel="stylesheet" href="${iconsCss}" type="text/css" />

<jsp:include page="customCssHeader.jsp" flush="true" />

<script type="text/javascript"
	src="http://ajax.googleapis.com/ajax/libs/jquery/1.9.1/jquery.min.js"></script>
<script type="text/javascript"
	src="http://ajax.googleapis.com/ajax/libs/jqueryui/1.10.1/jquery-ui.min.js"></script>

<script type="text/javascript" src="${cssBrowserSelectorJsUrl}"></script>
<spring:url var="jqueryValidator"
	value="/js/jquery-validator/dist/jquery.validate.js" />
<spring:url var="jqueryValidatorLanguage"
	value="/js/jquery-validator/localization/messages_es.js" />

<script type="text/javascript"
	src="${urlJS}/jquery-placeholder/jquery.placeholder.js"></script>
<script type="text/javascript"
	src="${urlJS}/jquery-fileinput/jquery.fileinput.js"></script>

<script type="text/javascript" src="${jqueryValidator}"></script>
<script type="text/javascript" src="${jqueryValidatorLanguage}"></script>


<jsp:include page="customJsHeader.jsp" flush="true" />

<spring:url value="/js/ohigginsApp.js" var="ohigginsAppUrl" />
<script type="text/javascript" src="${ohigginsAppUrl}"></script>
<spring:url value="/js/PrintArea.js" var="printAreaJsUrl" />
<script type="text/javascript" src="${printAreaJsUrl}"></script>



<script type="text/javascript"
	src="${blueprintUrl}/css_browser_selector.js"></script>

<decorator:head />
<spring:url value="/img" var="imagesUrl" />
<script type="text/javascript">
	// So IE<10 doesn't fail if console.debug or console.log is used.
	if (typeof console == "undefined") {
		console = {
			debug : function() {
			},
			log : function() {
			},
			warn : function() {
			},
			error : function() {
			}
		}
	} else if (!console.debug) {
		console.debug = function(text) {
			console.log(text);
		}
	}

	urls.images = '${imagesUrl}';
</script>
</head>
<body class="admin ${module} ${submodule}">
	<div class="container ">
		<%--<div id="header">
			<jsp:include page="header.jsp" flush="true" />
		</div>--%>
		<!-- /#header -->
		<%--
			//Estas pestanyas deben de aparecer en funcion del usuario...
		--%>
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
		<div class="default-container">
			<div id="tabs" class="tabsGenerales">
				<jsp:include page="tabs.jsp" flush="true" />
			</div>
			<div id="content" class="content ">
				<jsp:include page="subTaskTabs.jsp"></jsp:include>
				<div class="subTaskContent tab_container contenido">
					<jsp:include page="../decorators/mensajes.jsp" flush="true"></jsp:include>

					<div class="subTaskBodyWrapper">
						<div class="titulo">
							<h4>
								<decorator:title default="" />
							</h4>
						</div>


						<decorator:body />
					</div>
				</div>
			</div>
		</div>
		<!-- /.default-container -->
		<jsp:include page="footer.jsp" flush="true" />
	</div>
	<!-- /.container -->
</body>
</html>
