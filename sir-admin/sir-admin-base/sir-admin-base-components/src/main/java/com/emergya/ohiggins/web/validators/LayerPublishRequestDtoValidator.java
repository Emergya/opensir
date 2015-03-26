package com.emergya.ohiggins.web.validators;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.emergya.ohiggins.dto.LayerMetadataTmpDto;
import com.emergya.ohiggins.dto.LayerPublishRequestDto;
import com.google.common.base.Strings;

@Component
public final class LayerPublishRequestDtoValidator implements Validator {

	private static final String FORMATO_FECHA="dd/MM/yyyy";
	private static final String ACTION_NEW="PUBLICAR_COMO_NUEVA";
	private static final String ACTION_UPDATE="PUBLICAR_COMO_ACTUALIZA";
	private static final String LAYER_TYPE_RASTER= "Raster";
	
	private static final String REQUIRED_MSG =  "El campo es obligatorio.";
	
	private boolean metadataValidationEnabled = true;

	public LayerPublishRequestDtoValidator() {
	}
	
	public LayerPublishRequestDtoValidator(boolean metadataValidationEnabled) {
	    this.setMetadataValidationEnabled(metadataValidationEnabled);
	}

	public void setMetadataValidationEnabled(boolean metadataValidationEnabled) {
	    this.metadataValidationEnabled = metadataValidationEnabled;
	}

	public boolean isMetadataValidationEnabled() {
	    return metadataValidationEnabled;
	}
	
	@Override
	public boolean supports(Class<?> arg0) {

		return LayerPublishRequestDto.class.isAssignableFrom(arg0);
	}

	@Override
	public void validate(Object arg0, Errors errors) {
		LayerPublishRequestDto requestDto = (LayerPublishRequestDto) arg0;

		String action = requestDto.getAccionEjecutar();
		if(Strings.isNullOrEmpty(action)) {
			errors.rejectValue("accionEjecutar","layerPublishRequestDto.accionEjectuar.missing",
					"Hay que seleccionar la acción a ejecutar.");
			return;
		} 		
		
		if(action.equals(ACTION_NEW)){
			requestDto.setActualizacion(false);
			requestDto.setUpdatedLayerId(null);
			if(Strings.isNullOrEmpty(requestDto.getNombredeseado())) {
				errors.rejectValue("nombredeseado","layerPublishRequestDto.nombredeseado.missing",
					REQUIRED_MSG);
			}
		} else if (action.equals(ACTION_UPDATE)){			
			requestDto.setActualizacion(true);
			requestDto.setNombredeseado(null);
			if(requestDto.getUpdatedLayerId()==null) {
				errors.rejectValue("capaActualizar","layerPublishRequestDto.capaActualizar.missing",
						REQUIRED_MSG);
			}
		}
		
		if(!isMetadataValidationEnabled()) {
		    return;
		}
		validateCommonMetadata(requestDto.getMetadata(), errors);
		
		String layerType = requestDto.getSourceLayerType().getTipo();
		if(layerType.equals(LAYER_TYPE_RASTER)) {
			validateRasterMetadata(requestDto.getMetadata(), errors);
		}
	}

	private void validateRasterMetadata(LayerMetadataTmpDto metadataTmpDto, Errors errors) {
		if(Strings.isNullOrEmpty(metadataTmpDto.getReferencia())) {
			errors.rejectValue("metadata.referencia", 
					"layerPublishRequestDto.metadata.referencia.missing",
					REQUIRED_MSG);
		}
		
		if(Strings.isNullOrEmpty(metadataTmpDto.getInformacionGeolocalizacion())) {
			errors.rejectValue("metadata.informacionGeolocalizacion", 
					"layerPublishRequestDto.metadata.informacionGeolocalizacion.missing",
					REQUIRED_MSG);
		}
		
		if(Strings.isNullOrEmpty(metadataTmpDto.getCalidad())) {
			errors.rejectValue("metadata.calidad", 
					"layerPublishRequestDto.metadata.calidad.missing",
					REQUIRED_MSG);
		}
		
		if(Strings.isNullOrEmpty(metadataTmpDto.getInformacionColeccionPuntoReferencia())) {
			errors.rejectValue("metadata.informacionColeccionPuntoReferencia", 
					"layerPublishRequestDto.metadata.informacionColeccionPuntoReferencia.missing",
					REQUIRED_MSG);
		}
		
		if(Strings.isNullOrEmpty(metadataTmpDto.getInformacionPuntoReferencia())) {
			errors.rejectValue("metadata.informacionPuntoReferencia", 
					"layerPublishRequestDto.metadata.informacionPuntoReferencia.missing",
					REQUIRED_MSG);
		}
		
		if(Strings.isNullOrEmpty(metadataTmpDto.getNombrePuntoReferencia())) {
			errors.rejectValue("metadata.nombrePuntoReferencia", 
					"layerPublishRequestDto.metadata.nombrePuntoReferencia.missing",
					REQUIRED_MSG);
		}
		
		if(Strings.isNullOrEmpty(metadataTmpDto.getCrsPuntoReferencia())) {
			errors.rejectValue("metadata.crsPuntoReferencia", 
					"layerPublishRequestDto.metadata.crsPuntoReferencia.missing",
					REQUIRED_MSG);
		}

		if(Strings.isNullOrEmpty(metadataTmpDto.getPuntosDeReferencia())) {
			errors.rejectValue("metadata.puntosDeReferencia", 
					"layerPublishRequestDto.metadata.puntosDeReferencia.missing",
					REQUIRED_MSG);
		}
		
		if(Strings.isNullOrEmpty(metadataTmpDto.getPosicion())) {
			errors.rejectValue("metadata.posicion", 
					"layerPublishRequestDto.metadata.posicion.missing",
					REQUIRED_MSG);
		}
		
		if(Strings.isNullOrEmpty(metadataTmpDto.getPrecision())) {
			errors.rejectValue("metadata.precision", 
					"layerPublishRequestDto.metadata.precision.missing",
					REQUIRED_MSG);
		}
		
	}

	private void validateCommonMetadata(LayerMetadataTmpDto metadataTmpDto, Errors errors) {
		if(Strings.isNullOrEmpty(metadataTmpDto.getFrecuencia())) {
			errors.rejectValue("metadata.frecuencia", "layerPublishRequestDto.metadata.frecuencia.missing",
					REQUIRED_MSG);
		}
		
		if(Strings.isNullOrEmpty(metadataTmpDto.getSiguiente())) {
			errors.rejectValue("metadata.siguiente","layerPublishRequestDto.metadata.siguiente.missing",
					REQUIRED_MSG);
		} else {
			SimpleDateFormat dateFmt = new SimpleDateFormat(FORMATO_FECHA);
			try{
				dateFmt.parse(metadataTmpDto.getSiguiente());
			}catch(ParseException e) {
				errors.rejectValue("metadata.siguiente", "layerPublishRequestDto.metadata.siguiente.invalid",
					"La fecha debe tener un formato «dd/mm/aa».");
			}
		}
		
		if(Strings.isNullOrEmpty(metadataTmpDto.getPeriodo())) {
			errors.rejectValue("metadata.periodo", 
					"layerPublishRequestDto.metadata.periodo.missing",
					REQUIRED_MSG);
		}
		
		if(Strings.isNullOrEmpty(metadataTmpDto.getRango())){
			errors.rejectValue("metadata.rango",
					"layerPublishRequestDto.metadata.rango.missing",
					REQUIRED_MSG);
		}
		
		if(Strings.isNullOrEmpty(metadataTmpDto.getOtros())) {
			errors.rejectValue("metadata.otros", 
					"layerPublishRequestDto.metadata.otros.missing",
					REQUIRED_MSG);
		}
		
		if(Strings.isNullOrEmpty(metadataTmpDto.getResponsable())) {
			errors.rejectValue("metadata.responsable", 
					"layerPublishRequestDto.metadata.responsable.missing",
					REQUIRED_MSG);
		}
		

		if(Strings.isNullOrEmpty(metadataTmpDto.getRequerimientos())) {
			errors.rejectValue("metadata.requerimientos", 
					"layerPublishRequestDto.metadata.requerimientos.missing",
					REQUIRED_MSG);
		}
		

		if(Strings.isNullOrEmpty(metadataTmpDto.getFormato())) {
			errors.rejectValue("metadata.formato", 
					"layerPublishRequestDto.metadata.formato.missing",
					REQUIRED_MSG);
		}
		

		if(Strings.isNullOrEmpty(metadataTmpDto.getDistribuidor())) {
			errors.rejectValue("metadata.distribuidor", 
					"layerPublishRequestDto.metadata.distribuidor.missing",
					REQUIRED_MSG);
		}
		

		if(Strings.isNullOrEmpty(metadataTmpDto.getInformacion())) {
			errors.rejectValue("metadata.informacion", 
					"layerPublishRequestDto.metadata.informacion.missing",
					REQUIRED_MSG);
		}
	}
}
