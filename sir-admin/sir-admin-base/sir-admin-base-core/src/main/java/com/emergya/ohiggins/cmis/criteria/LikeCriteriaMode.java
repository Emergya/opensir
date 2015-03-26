package com.emergya.ohiggins.cmis.criteria;

public enum LikeCriteriaMode {
	ANYWHERE("'%%%s%%'"),
	START("'%s%%'"),
	END("'%%%s'");
	
	String pattern;
	
	private LikeCriteriaMode(String pattern) {
		this.pattern = pattern;
	}	
}
