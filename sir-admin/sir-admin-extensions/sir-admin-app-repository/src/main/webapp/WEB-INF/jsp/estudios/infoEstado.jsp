<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>${titulo}</title>
<spring:url value="¿Eliminar Estudio?" var="mensajeEliminarEstudio" />
<script type="text/javascript">
	$(document).ready(function() {
		
		//Elimina un estudio
	    $('.enlace-borrar').click(function(event){
	           return confirm('¿Eliminar el estudio?');
	    });
			
	});
</script>

</head>
<body>

<spring:url value="/estudios/editar" var="editarURL"/>

<form action="${editarURL}/${estado}/${identificador}">
	<div class="box-status">
		<p>
			El estudio con título '${nombre}' solicitado a 
			publicación por la institución ${institucion} en la fecha ${solicitud} ${texto}
		</p>		
			<c:if test="${!aceptar}">
				<div class="span-4">
					<label class="labelCampo" style="text-align:left">Causas Rechazo:</label>
				</div>
				<textarea readonly="readonly" class="campoDisabled"  style="height: auto;min-height:100px;">${comentario}</textarea>
			</c:if>
			
		<div class="buttons-container">	
			<c:if test="${aceptar}">
				<!-- input type="submit" name="action" value="Aceptar" / -->
				<button type="submit" name="action" class="button accept">Aceptar</button>
			</c:if>
			<c:if test="${!aceptar}">
				<!-- input type="submit" name="action" value="editar" / -->
				<button type="submit" name="action" class="button edit" value="Editar">Editar</button>
				<!-- input type="submit" name="action" value="eliminar" class="enlace-borrar" / -->
				<button type="submit" name="action" class="button secndary reject-red" value="Eliminar">Eliminar</button>
			</c:if>
		</div>
	</div>
</form>
	

</body>
</html>