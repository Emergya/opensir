package com.emergya.ohiggins.cmis.bean;

import com.emergya.ohiggins.cmis.ADocumentField;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigInteger;
import java.text.DateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.chemistry.opencmis.commons.data.PropertyData;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Required;

public class AnalyticsData implements ADocument {

    public static final String PREFIX = "ohgA";
    public static final String DOCUMENT_TYPE = "P:" + PREFIX + ":analytics_data";
    private String identifier;
    private String name;
    private String description;
    private Long institutionId;
    private String institutionName;
    private Long categoryId;
    private String categoryName;
    private Long geoContextId;
    private String geoContextName;
    private String author;
    private AnalyticsDataLicense license;
    private ADocumentState state;
    private Calendar requestDate;
    private Calendar requestAnswerDate;    
    private Calendar creationDate;
    private String requestAnswer;
    
    private String updatedDataIdentifier;
    private String updatedDataName;
    
    private String tags;
    
    private Long downloads;

    public AnalyticsData() {
        
    }

    @Override
    public String getPrefix() {
        return PREFIX;
    }

    @Override
    @Required
    @ADocumentField
    public String getName() {
        return name;
    }

    @Override
    public String getIdentifier() {
        return identifier;
    }

    @Override
    public void setIdentifier(String id) {
        identifier = id;
    }

    @Override
    public String getDocumentType() {
        return DOCUMENT_TYPE;
    }

    @Override
    public String getStateProperty() {
        return "state";
    }

    @Override
    public String getUserProperty() {
        return "institutionId";
    }

    
    @Override
    public Map<String, Object> toAlfrescoProperties() {
        Map<String, Object> props = new HashMap<String, Object>();

        Class<? extends AnalyticsData> c = this.getClass();
        // An accesor method.
        try {
            for (Method m : c.getMethods()) {
                String methodName = m.getName();
                if (m.isAnnotationPresent(ADocumentField.class)) {
                    Object value = m.invoke(this);

                    if (value != null && value.getClass().isEnum()) {
                        value = value.toString();
                    }

                    String fieldName = methodName.replaceAll("^get", "").replaceAll("^is", "");

                    fieldName = fieldName.substring(0, 1).toLowerCase() + fieldName.substring(1);
                    fieldName = PREFIX + ":" + fieldName;

                    props.put(fieldName, value);
                }
            }
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        } catch (IllegalArgumentException e) {
            throw new RuntimeException(e);
        } catch (InvocationTargetException e) {
            throw new RuntimeException(e);
        }

        return props;
    }
    
    protected String[] getExcludedFields() {
        return new String[] {"cmis:name"};
    }

    @Override
    public void copyFromProperties(List<PropertyData<?>> properties) {
        Class<? extends AnalyticsData> c = this.getClass();

        Map<String, Method> methods = new HashMap<String, Method>();
        for (Method m : c.getDeclaredMethods()) {
            if (m.getName().startsWith("set")) {
                methods.put(m.getName(), m);
            }
        }

        for (PropertyData<?> data : properties) {
            String fieldId = data.getId();
            
            if(Arrays.asList(getExcludedFields()).contains(fieldId)) {
                continue;
            }

            Object value = data.getFirstValue();
            if (value == null) {
                continue;
            }

            int separatorIndex = fieldId.indexOf(":");
            
            fieldId = fieldId.substring(separatorIndex+1);

            String setterName = "set" + fieldId.substring(0, 1).toUpperCase() + fieldId.substring(1);

            try {
                Method m = methods.get(setterName);
                
                if(m == null) {
                    continue;
                }

                Class parameterClass = m.getParameterTypes()[0];
                if (parameterClass.isEnum()) {
                    value = Enum.valueOf(parameterClass, value.toString());
                } else if (parameterClass == Long.class
                        && value.getClass() == BigInteger.class) {
                    value = new Long(((BigInteger) value).longValue());
                }

                m.invoke(this, value);
            } catch (Throwable e) {
                throw new RuntimeException(e);
            }
        }

    }

    public void setName(String name) {
        this.name = name;
    }

    @Required
    @ADocumentField
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Required
    @ADocumentField
    public Long getInstitutionId() {
        return institutionId;
    }

    public void setInstitutionId(Long institutionId) {
        this.institutionId = institutionId;
    }

    @Required
    @ADocumentField
    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    @Required
    @ADocumentField
    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    @Required
    @ADocumentField
    public AnalyticsDataLicense getLicense() {
        return license;
    }

    public void setLicense(AnalyticsDataLicense license) {
        this.license = license;
    }

    @ADocumentField
    public ADocumentState getState() {
        return state;
    }

    public void setState(ADocumentState state) {
        this.state = state;
    }

    @ADocumentField
    public Calendar getRequestDate() {
        return requestDate;
    }

    public void setRequestDate(Calendar requestDate) {
        this.requestDate = requestDate;
    }

    @ADocumentField
    public Calendar getRequestAnswerDate() {
        return requestAnswerDate;
    }
    
    public String getRequestAnswerDateLabel() {
    	if (requestAnswerDate==null)
    		return "";
    	
    	String result = DateFormat.getDateInstance(DateFormat.SHORT).format(requestAnswerDate.getTime());
    	if (result!=null)
    		return result;
    	else
    		return ""; 
    }

    public void setRequestAnswerDate(Calendar requestAnswerDate) {
        this.requestAnswerDate = requestAnswerDate;
    }

    @ADocumentField
    public String getRequestAnswer() {
        return requestAnswer;
    }

    public void setRequestAnswer(String requestAnswer) {
        this.requestAnswer = requestAnswer;
    }

    /**
     * @return the categoryName
     */
    public String getCategoryName() {
        return categoryName;
    }

    /**
     * @param categoryName the categoryName to set
     */
    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    /**
     * @return the institutionName
     */
    public String getInstitutionName() {
        return institutionName;
    }

    /**
     * @param institutionName the institutionName to set
     */
    public void setInstitutionName(String institutionName) {
        this.institutionName = institutionName;
    }

    /**
     * @return the geoContextId
     */
    @ADocumentField
    public Long getGeoContextId() {
        return geoContextId;
    }

    /**
     * @param geoContextId the geoContextId to set
     */
    public void setGeoContextId(Long geoContextId) {
        this.geoContextId = geoContextId;
    }

    /**
     * @return the updatedDataIdentifier
     */
    @ADocumentField
    public String getUpdatedDataIdentifier() {
        return updatedDataIdentifier;
    }

    /**
     * @param updatedDataIdentifier the updatedDataIdentifier to set
     */
    public void setUpdatedDataIdentifier(String updatedDataIdentifier) {
        this.updatedDataIdentifier = updatedDataIdentifier;
    }

    /**
     * @return the updatedDataName
     */
    @ADocumentField
    public String getUpdatedDataName() {
        return updatedDataName;
    }

    /**
     * @param updatedDataName the updatedDataName to set
     */
    public void setUpdatedDataName(String updatedDataName) {
        this.updatedDataName = updatedDataName;
    }
    
    public boolean isUpdate() {
        return StringUtils.isNotBlank(updatedDataIdentifier);
    }

    /**
     * @return the tags
     */
    @ADocumentField
    public String getTags() {
        return tags;
    }

    /**
     * @param tags the tags to set
     */
    public void setTags(String tags) {
        this.tags = tags;
    }

    /**
     * @return the creationDate
     */
    public Calendar getCreationDate() {
        return creationDate;
    }

    /**
     * @param creationDate the creationDate to set
     */
    public void setCreationDate(Calendar creationDate) {
        this.creationDate = creationDate;
    }

	public String getGeoContextName() {
		return geoContextName;
	}

	public void setGeoContextName(String geoContextName) {
		this.geoContextName = geoContextName;
	}

	public Long getDownloads() {
		return downloads;
	}

	public void setDownloads(Long downloads) {
		this.downloads = downloads;
	}
}
