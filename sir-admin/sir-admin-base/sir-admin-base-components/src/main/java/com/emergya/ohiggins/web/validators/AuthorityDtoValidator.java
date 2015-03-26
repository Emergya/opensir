package com.emergya.ohiggins.web.validators;

import org.apache.commons.lang3.StringUtils;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.emergya.ohiggins.dto.AuthorityDto;
import com.emergya.ohiggins.service.InstitucionService;
import com.emergya.ohiggins.web.bean.RegionBean;
import com.emergya.persistenceGeo.service.GeoserverService;
import com.emergya.persistenceGeo.utils.GeoserverUtils;

public class AuthorityDtoValidator implements Validator {
	private InstitucionService institucionService;
	private GeoserverService geoserverService;
	private RegionBean region;
	
	public AuthorityDtoValidator() {
		
	}
	
	public AuthorityDtoValidator(InstitucionService institucionService, GeoserverService geoserverService, RegionBean region) {
		this.institucionService = institucionService;
		this.geoserverService = geoserverService;
		this.region = region;
	}
	
	@Override
	public boolean supports(Class<?> arg0) {

		return AuthorityDto.class.isAssignableFrom(arg0);
	}

	@Override
	public void validate(Object arg0, Errors errors) {
		AuthorityDto authorityDto = (AuthorityDto) arg0;
		
		if(StringUtils.isEmpty(authorityDto.getAuthority()) 
				|| authorityDto.getAuthority().length() < 3) {
			errors.rejectValue("authority", "authorityDto.authority.requiredLength",
					"El nombre debe estar presente y contener al menos 3 carácteres.");
		} else if(!institucionService.isAuthorityAvalaible(authorityDto.getAuthority(), authorityDto.getId(), region != null ? region.getId() : null)) {
			errors.rejectValue("authority", "authorityDto.authority.alreadyUsed",
					"El nombre ya está en uso en otra institución.");
		}
		
		//Add region to name
		String possibleWorkspace = GeoserverUtils.createName(authorityDto.getAuthority() + (region != null ? " " + region.getId().toString() : null));
		if (authorityDto.getId() == null) {
			//Nueva institución
			if (geoserverService.existsWorkspace(possibleWorkspace)){
				errors.rejectValue("authority", 
					"authorityDto.authority.alreadyUsedWorkspace",
					String.format("El espacio de trabajo '%s' ya se usa en GeoServer, elija otro nombre de institución.", possibleWorkspace));
			}
		}
		

		String tipo = authorityDto.getTipoSeleccionado();
		
		if(tipo == null) {
			errors.rejectValue("tipoSeleccionado", "authorityDto.tipoSeleccionado.required",
					"Debe seleccionar una opción.");
		}
		
		String ambito = authorityDto.getAmbitoSeleccionado();
		
		if (!StringUtils.isEmpty(tipo)
				&& StringUtils.isEmpty(ambito)) { 
			errors.rejectValue("ambitoSeleccionado",
					"authorityDto.ambitoSeleccionado.required","Debe introducir el ámbito territorial.");
		}
	}
}