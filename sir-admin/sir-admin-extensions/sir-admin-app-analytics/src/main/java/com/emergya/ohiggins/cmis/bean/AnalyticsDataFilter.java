package com.emergya.ohiggins.cmis.bean;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.httpclient.util.DateUtil;
import org.apache.commons.lang3.StringUtils;

import com.emergya.ohiggins.cmis.criteria.ComparisonCriteria;
import com.emergya.ohiggins.cmis.criteria.EqualsCriteria;
import com.emergya.ohiggins.cmis.criteria.InDocumentCriteria;
import com.emergya.ohiggins.cmis.criteria.JoinCriteria;
import com.emergya.ohiggins.cmis.criteria.LikeCriteria;
import com.emergya.ohiggins.cmis.criteria.LikeCriteriaMode;

public class AnalyticsDataFilter implements ADocumentFilter {
    
    private Long institutionId;
	private Long categoryId;
	private String tagName;
	private AnalyticsDataLicense license;
	private Long geoContextId;
	private String containedText;
	private Long year;
	
    @Override
    public void addToConditions(JoinCriteria conditions) {
        
        if(institutionId!=null) {
            conditions.add(new EqualsCriteria("institutionId", institutionId.toString()));
        }
        
        if(categoryId!=null) {
        	conditions.add(new EqualsCriteria("categoryId", categoryId.toString()));
        }
        
        if(!StringUtils.isBlank(tagName)) {
        	JoinCriteria tagCriteria = new JoinCriteria(JoinCriteria.OR);
        	conditions.add(tagCriteria);
        	
        	// Just one tag.
        	tagCriteria.add(new EqualsCriteria("tags", tagName));
        	// tag in first position.
        	tagCriteria.add(new LikeCriteria("tags", tagName+",",LikeCriteriaMode.START));
        	// tag in last position
        	tagCriteria.add(new LikeCriteria("tags", ","+tagName,LikeCriteriaMode.END));
        	// tag in the middle
        	tagCriteria.add(new LikeCriteria("tags", ","+tagName+","));
        }
        
        if(license!=null) {
        	conditions.add(new EqualsCriteria("license", license.name()));
        }
        
        if(geoContextId!=null) {
        	conditions.add(new EqualsCriteria("geoContextId", geoContextId.toString()));
        }
        
        if(year!=null) {
        	
        	SimpleDateFormat dParser = new SimpleDateFormat("yyyy/mm/dd");
        	
        	try {
        		Date starYearDate = dParser.parse(year+"/1/1");
				Date endDate = dParser.parse((year+1)+"/1/1");
				
				conditions.add(new ComparisonCriteria("requestAnswerDate", ComparisonCriteria.GE, starYearDate));
				conditions.add(new ComparisonCriteria("requestAnswerDate", ComparisonCriteria.LT, endDate));
	        					
				
			} catch (ParseException e) {
				throw new RuntimeException("Couldn't create dates from the passed year.");				
			}
        }
        
        if(!StringUtils.isBlank(containedText)) {
        	JoinCriteria textCriteria = new JoinCriteria(JoinCriteria.OR);
        	conditions.add(textCriteria);
        	textCriteria.add(new LikeCriteria("name", containedText));
        	textCriteria.add(new LikeCriteria("description", containedText));
        	textCriteria.add(new InDocumentCriteria(containedText));
        }
    }

    /**
     * @return the institutionId
     */
    public Long getInstitutionId() {
        return institutionId;
    }

    /**
     * @param institutionId the institutionId to set
     */
    public void setInstitutionId(Long institutionId) {
        this.institutionId = institutionId;
    }
    
    public Long getCategoryId() {
    	return categoryId;
    }

	public void setCategoryId(Long categoryId) {
		this.categoryId = categoryId;		
	}
	
	public String getTagName() {
		return tagName;
	}

	public void setTagName(String tagName) {
		this.tagName = tagName;		
	}

	public AnalyticsDataLicense getLicense() {
		return license;
	}

	public void setLicense(AnalyticsDataLicense license) {
		this.license = license;
	}

	public Long getGeoContextId() {
		return geoContextId;
	}

	public void setGeoContextId(Long geoContextId) {
		this.geoContextId = geoContextId;
	}

	public String getContainedText() {
		return containedText;
	}

	public void setContainedText(String containedText) {
		this.containedText = containedText;
	}

	public Long getYear() {
		return year;
	}

	public void setYear(Long year) {
		this.year = year;
	}
}
