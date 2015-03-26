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
	description="CSS class for the input container element" %>

<div class="span-5">
    <label class="labelCampo" for="${name}">${label}</label>
</div>
<div class="span-10 ${ctrCssClass } last">
    <form:checkbox cssClass="${cssClass}"
        cssErrorClass="${cssErrorClass}" id="${name}" path="${name}" />
    <span> <form:errors path="${name}" cssClass="${errorTextClass}" />
    </span>
</div>
