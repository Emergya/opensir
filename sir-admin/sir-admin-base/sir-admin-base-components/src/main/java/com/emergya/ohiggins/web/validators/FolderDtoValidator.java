package com.emergya.ohiggins.web.validators;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.emergya.ohiggins.dto.FolderDto;
import com.emergya.ohiggins.service.FolderService;
import com.google.common.base.Strings;

@Component
public class FolderDtoValidator implements Validator {

	protected FolderService folderService;

	@Override
	public boolean supports(Class<?> arg0) {

		return FolderDto.class.isAssignableFrom(arg0);
	}

	
	public FolderDtoValidator() {
	}
	
	public FolderDtoValidator(FolderService folderService) {
		this.folderService = folderService;
	}
	
	@Override
	public void validate(Object arg0, Errors errors) {
		FolderDto folderDto = (FolderDto) arg0;

		String name = folderDto.getName();
		if (name == null || name.length() < 3) {
			errors.rejectValue("name", "folderDto.name.requiredLength",
					"El nombre debe estar presente y contener al menos 3 carácteres.");
		}

		
		//Estas dos validaciones NO se estan usando. Comprobar requisitos de PRI/PRC 
		if (folderDto.getFolderType() != null && folderDto.getFolderType().getId() != 1
				&& Strings.isNullOrEmpty(folderDto.getZoneSeleccionada())) {
			errors.rejectValue(
					"zoneSeleccionada",
					"folderDto.zoneSeleccionada.required",
					"El canal corresponde a un instrumento de planificación, debe definir el ámbito territorial");
		}

		if (folderDto.getFolderType() != null && folderDto.getFolderType().getId() != 1 && !folderDto.getIsChannel()) {
			errors.rejectValue("isChannel",
					"folderDto.isChannel.mustBeChannel",
					"El canal corresponde a un instrumento de planificación, debe ser una capa");
		}
		

		
		
		if (folderDto.getFolderTypeSelected()==null || folderDto.getFolderTypeSelected().equals("")) {
			errors.rejectValue("isChannel",
					"folderDto.isChannel.typeNotSpecified",
					"No se ha especificado ningún tipo de canal o instrumento de planificación.");
			errors.rejectValue("folderTypeSelected",
					"folderDto.folderTypeSelected.folderNeedsParent",
					"No se ha especificado ningún tipo de canal o instrumento de planificación.");
			
		} else {
			try{
				Long.decode(folderDto.getFolderTypeSelected());
			}catch(NumberFormatException e){
				errors.rejectValue(
						"isChannel",
						"folderDto.isChannel.folderNotANumber",
						"No se ha especificado un canal o instrumento de planificación correcto.");
				errors.rejectValue(
						"folderTypeSelected",
						"folderDto.folderTypeSelected.folderNotANumber",
						"No se ha especificado un canal o instrumento de planificación correcto.");
			}
		}
	}
}
