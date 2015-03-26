<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ taglib uri="http://www.opensymphony.com/sitemesh/decorator" prefix="decorator" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

  <spring:url value="/faq/faqs" var="faqUrl"/>
  <spring:url value="/contacto/nuevoContacto" var="contactoUrl"/>
   
  <div id="footer">
    <div class="span-24" style="text-align: center;">
      <a href="${contactoUrl}" target="_blank" style="text-decoration: none;cursor:pointer;">Contacto</a>
      | <a href="${faqUrl}" target="_blank" style="text-decoration: none;cursor:pointer;">Preguntas frecuentes</a>
    </div>
    
    <div class="span-24" style="text-align: center; padding-bottom: 30px">
      <p><strong>Gobierno Regional de Arica y Parinacota | Av. Velasquez Nº 1775 | Fono: 2207300 | Arica - Chile</strong></p>
    </div>
  </div>
