<%@ page import="net.tanesha.recaptcha.ReCaptcha"%>
<%@ page import="net.tanesha.recaptcha.ReCaptchaFactory"%>
<%@ page import="net.tanesha.recaptcha.ReCaptchaImpl"%>
<%@ page import="net.tanesha.recaptcha.ReCaptchaResponse"%>
<%@page import="com.emergya.ohiggins.web.util.Utils"%>
<%@page import="com.emergya.ohiggins.security.OhigginsUserDetails"%>
<%@page
	import="org.springframework.security.core.context.SecurityContext"%>
<%@ page contentType="text/html;charset=UTF-8" language="java"
	pageEncoding="UTF-8"%>

<!-- Captcha -->
<div style="float: left; margin-top: 40px;">
	<label>Para evitar el uso de correos spam, introduzca el texto
		que aparece</label>

	<%
		//ReCaptcha c = ReCaptchaFactory.newReCaptcha("6LfSbdgSAAAAAKxKLbYj7V0gzIjHHMJ_Ju9cNaua","6LfSbdgSAAAAACD0vwv_NeUa6ac3Gkh7tw_tedI7",false);
		ReCaptcha c = ReCaptchaFactory.newReCaptcha(
				(String) request.getAttribute(Utils.PUBLIC_KEY_CAPTCHA),
				(String) request.getAttribute(Utils.PRIVATE_KEY_CAPTCHA),
				false);

		out.print(c.createRecaptchaHtml(null, "clean", null));
	%>
</div>

