
package com.emergya.ohiggins.web.validators;

import com.emergya.ohiggins.dto.AnalyticsDataDto;
import com.opensymphony.module.sitemesh.mapper.OSDecoratorMapper;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Repository;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

/**
 * Validator for AnalyticsDataDto 
 * @author lroman
 */
@Repository
public class AnalyticsDataDtoValidator implements Validator {
	private boolean isPublic;
	
	public AnalyticsDataDtoValidator() {
		isPublic = false;
	}

	public AnalyticsDataDtoValidator(boolean isPublic) {
		this.isPublic = isPublic;
	}
	
    @Override
    public boolean supports(Class<?> type) {
        return AnalyticsDataDto.class.isAssignableFrom(type);
    }

    @Override
    public void validate(Object o, Errors errors) {
        AnalyticsDataDto dto = (AnalyticsDataDto) o;
        
        if(StringUtils.isBlank(dto.getName())) {
            errors.rejectValue("name", "datumDto.name.required", "El campo es obligatorio");
        }
        
        // We only upload files when the data is new.
        if(StringUtils.isBlank(dto.getIdentifier())
                && (dto.getFile()==null || dto.getFile().isEmpty())) {
            
            errors.rejectValue("file", "datumDto.file.required", "El campo es obligatorio");
        }    
        
        // If the data is private we don't need to do more validations.
        if(!isPublic) {
        	return;
        }
        
        if(dto.getInstitutionId()==null) {
        	errors.rejectValue("institutionId", "datumDto.institutionId.required", "El campo es obligatorio");
        }
        
        if(dto.getCategoryId()==null) {
        	errors.rejectValue("categoryId", "datumDto.categoryId.required", "El campo es obligatorio");
        }
        
        if(dto.getGeoContextId()==null) {
        	errors.rejectValue("geoContextId", "datumDto.geoContextId.required", "El campo es obligatorio");
        }
        
        if(dto.getLicense()==null) {
        	errors.rejectValue("license", "datumDto.license.required", "El campo es obligatorio");
        }
        
        if(StringUtils.isBlank(dto.getAuthor())){
            errors.rejectValue("author", "datumDto.author.required", "El campo es obligatorio");
        }
        
        if(StringUtils.isBlank(dto.getDescription())){
            errors.rejectValue("description", "datumDto.description.required", "El campo es obligatorio");
        }
        
    }
    
}
