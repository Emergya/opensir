<%@ page import="net.tanesha.recaptcha.ReCaptcha"%>
<%@ page import="net.tanesha.recaptcha.ReCaptchaFactory"%>
<%@ page import="net.tanesha.recaptcha.ReCaptchaImpl"%>
<%@ page import="net.tanesha.recaptcha.ReCaptchaResponse"%>
<%@ taglib uri="http://ckeditor.com" prefix="ckeditor"%>
<%@ page import="com.emergya.ohiggins.security.OhigginsUserDetails"%>
<%@ page
    import="org.springframework.security.core.context.SecurityContext"%>
    <%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
    <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
    <%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
    <%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
    <%@ page contentType="text/html;charset=UTF-8" language="java"
             pageEncoding="UTF-8"%>

    <html>
        <head>

            <spring:url value="/admin/contactos" var="urlListadoContacto" />
            <spring:url var="salvarContacto" value="/contacto/salvarContacto" />
        </head>
        <body>
            <div id="faq-page" class="pestanas">
                <div id="contentfaqs" class="tab_container contenido">
                    <div class="titulo">
                        <h4>Formulario de Contacto</h4>
                    </div>	
                    <div class="pestanaimg"></div>
                    
                    <div id="faq-contents">
                        <form:form commandName="contactoDto" action="${salvarContacto}"
                               method="post">
                        <p>
                            En este apartado podrás ponerte en contacto con el administrador
                        </p>
                        <!-- Email -->
                        <div class="span-20 last">
                            <div class="span-4">
                                <label class="labelCampo">Correo electrónico</label>
                            </div>

                            <div class="span-16 last">
                                <form:input path="email" maxlength="120" cssClass="campo"
                                            cssErrorClass="campoError" errorTextClass="fieldError" />
                                <span class="help-inline" id="help-inline-email"> <form:errors
                                        path="email" cssClass="fieldError" /></span>
                            </div>
                        </div>

                        <!-- Nombre y apellidos -->
                        <div class="span-20 last">
                            <div class="span-4">
                                <label class="labelCampo">Nombre y apellidos</label>
                            </div>
                            <div class="span-16 last">
                                <form:input path="nombre" maxlength="120" cssClass="campo"
                                            cssErrorClass="campoError" errorTextClass="fieldError" />
                                <span class="help-inline" id="help-inline-nombre"> <form:errors
                                        path="nombre" cssClass="fieldError" />
                                </span>
                            </div>
                        </div>

                        <!-- Titulo -->
                        <div class="span-20 last">
                            <div class="span-4">
                                <label class="labelCampo">Título</label>
                            </div>
                            <div class="span-16 last">
                                <form:input path="titulo" maxlength="120" cssClass="campo"
                                            cssErrorClass="campoError" errorTextClass="fieldError" />
                                <span class="help-inline" id="help-inline-titulo"> <form:errors
                                        path="titulo" cssClass="fieldError" />
                                </span>
                            </div>
                        </div>

                        <!-- descripcion -->
                        <div class="span-20 last">
                            <div class="span-4">
                                <label class="labelCampo">Descripción</label>
                            </div>
                            <div class="span-16 last">
                                <form:textarea path="descripcion" id="descripcion" cssClass="campo"
                                               cssErrorClass="campoError" errorTextClass="fieldError" />
                                <span class="help-inline" id="help-inline-descripcion"> <form:errors
                                        path="descripcion" cssClass="fieldError" />
                                </span>
                            </div>
                        </div>

                        <!-- Captcha -->
                        <div id="form-captcha" class="span-20 last">
                            <div class="prepend-4 span-16 last">
                                <jsp:include page="captcha.jsp" flush="true"></jsp:include>
                                </div>
                            </div>
                            <!-- /#form-captcha -->

                            <!-- Botonera form -->
                            <div class="buttons-container span-20 last">
                                <button type="submit" class="button send">Enviar</button>
                            </div>
                            <!-- /.contenidoPresentacion -->
                    </form:form>
                            
                     </div>
                </div>
                <!-- /contentsfaqs -->
            </div>
            <!-- /#contact-page -->
        </body>
    </html>
