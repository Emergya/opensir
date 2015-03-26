package com.emergya.ohiggins.dto;

import com.emergya.ohiggins.cmis.bean.AnalyticsData;
import com.emergya.ohiggins.cmis.bean.AnalyticsDataLicense;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.web.multipart.MultipartFile;

/**
 * Dto class used to add some fields to the AnalyticsData class to make them
 * avalaible in the form.
 *
 * @author lroman
 */
public class AnalyticsDataDto {

    private String tags;
    private MultipartFile file;
    private String identifier;
    private String name;
    private String description;
    private Long institutionId;
    private Long categoryId;
    private Long geoContextId;
    private String author;
    private AnalyticsDataLicense license;

    public AnalyticsDataDto() {
        
    }
    
    public AnalyticsDataDto(AnalyticsData datum) {
        this.identifier = datum.getIdentifier();
        this.name = datum.getName();
        this.geoContextId = datum.getGeoContextId();
        this.categoryId = datum.getCategoryId();
        this.description = datum.getDescription();
        this.license = datum.getLicense();
        this.institutionId = datum.getInstitutionId();
        this.author = datum.getAuthor();
        this.tags = datum.getTags();
    }

    @Required
    public String getName() {
        return name;
    }

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String id) {
        identifier = id;
    }

   
    public void setName(String name) {
        this.name = name;
    }

    @Required
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Required
    public Long getInstitutionId() {
        return institutionId;
    }

    public void setInstitutionId(Long institutionId) {
        this.institutionId = institutionId;
    }

    @Required
    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    @Required
    public Long getGeoContextId() {
        return geoContextId;
    }

    public void setGeoContextId(Long geoContextId) {
        this.geoContextId = geoContextId;
    }

    @Required
    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    @Required
    public AnalyticsDataLicense getLicense() {
        return license;
    }

    public void setLicense(AnalyticsDataLicense license) {
        this.license = license;
    }

    /**
     * @return the file
     */
    public MultipartFile getFile() {
        return file;
    }

    /**
     * @param file the file to set
     */
    public void setFile(MultipartFile file) {
        this.file = file;
    }

    /**
     * @return the tags
     */
    public String getTags() {
        return tags;
    }

    /**
     * @param tags the tags to set
     */
    public void setTags(String tags) {
        this.tags = tags;
    }

    public void toDatum(AnalyticsData datum) {        
        datum.setName(name);
        datum.setCategoryId(categoryId);
        datum.setAuthor(author);
        datum.setDescription(description);
        datum.setGeoContextId(geoContextId);
        if(institutionId!=null){
            datum.setInstitutionId(institutionId);
        }
        datum.setLicense(license);
        datum.setTags(tags);
    }   
}
