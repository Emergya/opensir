<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ taglib uri="http://www.opensymphony.com/sitemesh/decorator" prefix="decorator" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

  <spring:url value="/faq/faqs/{variableName}" var="faqUrl">
   <spring:param name="variableName" value="administración" />
 </spring:url>
 
  <spring:url value="/contacto/nuevoContacto" var="contactoUrl"/>
   
  <div id="footer">
    <div>
      <a href="${contactoUrl}" target="_blank" style="text-decoration: none;cursor:pointer;"><spring:message code="common.footerContact_message"/></strong></a>
      | <a href="${faqUrl}" target="_blank" style="text-decoration: none;cursor:pointer;"><spring:message code="common.footerFaq_message"/></a>
    </div>
    
    <%-- <div style="padding-bottom: 30px">
      <p><strong><spring:message code="common.footer_message"/></strong></p>
    </div>--%>
  </div>