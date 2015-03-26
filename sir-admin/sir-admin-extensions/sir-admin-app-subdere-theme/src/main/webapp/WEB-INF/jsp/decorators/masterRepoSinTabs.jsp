<%@ page contentType="text/html; charset=UTF-8" language="java"%>
<%@ taglib uri="http://www.opensymphony.com/sitemesh/decorator"
	prefix="decorator"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN" "http://www.w3.org/TR/html4/strict.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta http-equiv="X-UA-Compatible" content="IE=9">
<title>Sistema de Informaci&oacute;n Regional - Gobierno Regional de Arica y Parinacota</title>
<spring:url value="/faq" var="faqUrl" />
<spring:url value="/contacto" var="contactoUrl" />
<spring:url value="/css/blueprint" var="blueprintUrl" />
<spring:url value="/css/blueprint/estilo.css" var="estiloCss" />
<spring:url value="/css/blueprint/screen.css" var="screenCss" />
<spring:url value="/css/blueprint/print.css" var="printCss" />
<spring:url value="/css/blueprint/ie.css" var="ieCss" />

<spring:url value="/css/blueprint/icons.css" var="iconsCss" />
<spring:url value="/js/tablescroll" var="tablescrollUrl" />


<spring:url value="/css/blueprint/css_browser_selector.js"
	var="cssBrowserSelectorJsUrl" />
<spring:url value="/css/jquery-ui/jquery-ui-1.9.2.custom.min.css"
	var="jqueryThemeRoller" />
<spring:url value="/css/chosen" var="chosenUrl" />

<spring:url value="/js" var="urlJS" />

<spring:url value="/img/favicon.ico" var="faviconUrl" />
<link rel="shortcut icon" href="${faviconUrl}">



<link rel="stylesheet" href="${screenCss}" type="text/css"
	media="screen, projection" />
<link rel="stylesheet" href="${printCss}" type="text/css" media="print" />
<!--[if lt IE 8]><link rel="stylesheet" href="${ieCss}" type="text/css" media="screen, projection"><![endif]-->
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

<script type="text/javascript" src="${jqueryValidator}"></script>
<script type="text/javascript" src="${jqueryValidatorLanguage}"></script>
<jsp:include page="customJsHeader.jsp" flush="true" />

<spring:url value="/js/chosen/chosen.jquery.js" var="chosenJsUrl" />
<spring:url value="/js/ohigginsApp.js" var="ohigginsAppUrl" />
<script type="text/javascript" src="${ohigginsAppUrl}"></script>
<spring:url value="/js/PrintArea.js" var="printAreaJsUrl" />
<script type="text/javascript" src="${printAreaJsUrl}"></script>

<script type="text/javascript"
	src="${tablescrollUrl}/jquery.tablescroll.js" ></script>
        
<script type="text/javascript"
	src="${urlJS}/jquery-placeholder/jquery.placeholder.js"></script>
<decorator:head />
<spring:url value="/img" var="imagesUrl" />
<script type="text/javascript">
	urls.images = '${imagesUrl}';
</script>
</head>
<body id="recursos" class="public repo ${submodule}">
	<div class="container ">
		<div id="header">
			<jsp:include page="header.jsp" flush="true" />
		</div>
		<!-- /#header -->


		<div id="content" class="content ">
			<div class="innerContent">
				<decorator:body />
			</div>
		</div>

		<jsp:include page="footerRepository.jsp" flush="true" />
	</div>
	<!-- /#container -->
</body>
</html>
