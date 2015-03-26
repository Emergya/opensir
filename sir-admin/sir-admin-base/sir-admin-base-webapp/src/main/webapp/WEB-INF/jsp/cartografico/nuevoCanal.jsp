<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ page contentType="text/html;charset=UTF-8" language="java"
	pageEncoding="UTF-8"%>

<html>
<head>
<title>Nuevo Canal Temático</title>

<!-- Scripts js utilizados -->

<spring:url var="urlImages" value="/img" />
<spring:url var="listZones" value="/cartografico/listZones" />

<script type="text/javascript">
    $(document).ready(function () {
        $("#folderDto").validate({
            errorClass : "campoError",
            validClass : "campo",
            rules : {
                name : {
                    required : true,
                    minlength : 3,
                    maxlength : 250
                }
            },
            errorPlacement : function (error, element) {
                $('#help-inline-' + element[0].id).html(error);
            }
        });

    });
</script>

<script type="text/javascript">
    /**
     * Muestra la lista de zonas segun el tipo de planificacion
     */
    function mostrarListZones (url) {
        ajaxLoad(url, "zonesSelect", "${urlImages}");

    }
    function activeChannel () {
        document.getElementById("idcanal").checked = 'true';
    }
    function activeNone () {
        document.getElementById("idnone").checked = 'true';
        mostrarListZones('${listZones}/NONE');
    }
</script>

</head>
<body>
		<!-- Formulario -->
		<spring:url var="saveCanal" value="/cartografico/saveCanal" />

		<form:form commandName="folderDto" action="${saveCanal}" method="post"
			style="float:left;" id="newChannelForm">
			<form:hidden path="id" id="id" />
			<form:hidden path="parent.id" id="parent" />
			<form:hidden path="folderOrder" id="folderOrder" />
			<form:hidden path="user.id" id="user" />
			<form:hidden path="authority.id" id="authority" />

			<div>
				<!-- Panel izdo. -->
				<div class="span-10">
					<div class="span-5">
						<label class="labelCampo">Nombre canal</label>
					</div>
					<div class="span-5 last">
						<form:input path="name" cssClass="campo"
							cssErrorClass="campoError" errorTextClass="fieldError" />
						<span class="help-inline" style="" id="help-inline-name"> <form:errors
								path="name" cssClass="fieldError" />
						</span>
					</div>

					<div class="span-10">
						<div class="span-5">
							<label class="labelCampo">Ámbito Territorial</label>
						</div>
						<div class="span-5 last">
							<div id="zonesSelect">
								<form:select path="zoneSeleccionada" id="zoneSeleccionada"
									cssClass="campo maxWidth" cssErrorClass="campoError">
									<option value="">----</option>
									<form:options items="${allAmbitoTerritorial}" itemLabel="name"
										itemValue="id" />
								</form:select>
							</div>
							<span class="help-inline" id="help-inline-zoneSeleccionada">
								<form:errors path="zoneSeleccionada" cssClass="fieldError" />
							</span>
						</div>
					</div>
				</div>


				<!-- Panel derecho -->
				<div class="pright" style="float: left; width: 50%;">

					<div style="float: left">
						<!-- Habilitado -->

						<div class="span-5">
							<label class="labelCampo">Habilitado</label>
						</div>
						<div class="span-5 radioGroup">

							<form:radiobutton path="enabled" value="true" label="Sí"
								cssClass="campo" cssErrorClass="campoError" />

							<form:radiobutton path="enabled" value="false" label="No"
								cssClass="campo" cssErrorClass="campoError" />
						</div>
						<div class="span-3 last">
							<form:errors path="enabled" cssClass="fieldError" />
						</div>
					</div>
					
				<div style="float: left">
						<!-- Instrumentos de planificacion -->
						<div class="span-5">
							<label class="labelCampo">Tipo.</label>
						</div>
						<div class="span-5 radioGroup">
							<%-- for con radio button donde el path sera el ${nombre de la
							variable que se encuentra declarada} // y el value sera el id de
							la variable, a la hora de mostrar las zonas se hara con el
							atributo type --%>
							<div class="span-5 last">
								<div id="folderSelected">
									<form:select path="folderTypeSelected" id="folderTypeSelected"
										cssClass="campo" cssErrorClass="campoError">
										<option value="">----</option>
										<form:options items="${folderTypeDtoList}" itemLabel="type"
											itemValue="id" />
									</form:select>
								</div>
								<span class="help-inline" id="help-inline-folderSelected">
									<form:errors path="folderTypeSelected" cssClass="fieldError" />
								</span>
							</div>

						</div>
					</div>
					
				</div>
			</div>

			<!-- Edición de capas -->
			<c:if test="${folderDto.id != null}">
				<!-- Solo lo mostramos cuando editamos -->
				<jsp:include page="editorCapasCarpeta.jsp"></jsp:include>
			</c:if>

			<!-- Botonera -->
			<div class="buttons-container">
				
				<form:button class="button save submit">Guardar</form:button>				
				
				<spring:url var="urlListado" value="/cartografico/canales" />
				<a href="${urlListado}" class="button secondary cancel">Cancelar</a>				
			</div>
		</form:form>
</body>
</html>
