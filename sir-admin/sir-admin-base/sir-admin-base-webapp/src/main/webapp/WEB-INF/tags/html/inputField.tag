<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ attribute name="name" required="true" rtexprvalue="true"
              description="Bean field name"%>
<%@ attribute name="label" required="true" rtexprvalue="true"
              description="Field label"%>
<%@ attribute name="cssClass" rtexprvalue="true"
              description="CSS class for input normal state"%>
<%@ attribute name="cssErrorClass" rtexprvalue="true"
              description="CSS class for input error state"%>
<%@ attribute name="errorTextClass" rtexprvalue="true"
              description="CSS class for error text"%>
<%@ attribute name="ctrCssClass" required="false"
              description="CSS class for the input container element"%>
<%@ attribute name="type" required="false"
              description="Type attribute for the field."%>
<%@ attribute name="lblCtrCssClass" required="false"
              description="Class for the container of the label"%>

<%@ attribute name="required" required="false"
              description="If the field is required"%>

<c:if test="${empty lblCtrCssClass}">
    <c:set var="lblCtrCssClass" value="span-5"/>
</c:if>
      
<c:if test="${empty type}">
    <c:set var="type" value="text"/>
</c:if>

<div class="${lblCtrCssClass}">
    <label class="labelCampo" for="${name}">${label}</label>
</div>


<div class="controls ${ctrCssClass }">

    <c:choose>
        <c:when test="${required}">
            <form:input path="${name}" cssClass="${cssClass}" id="${name}"
                        cssErrorClass="${cssErrorClass }" 
                        type="${type}"
                        required="true"/>		
        </c:when>
        <c:otherwise>
            <form:input path="${name}" cssClass="${cssClass}" id="${name}"
                        cssErrorClass="${cssErrorClass }" 
                        type="${type}"/>		
        </c:otherwise>        
    </c:choose>

    <span class="help-inline" id="help-inline-${name}"> <form:errors
            path="${name}" cssClass="${errorTextClass}" />
    </span>
</div>