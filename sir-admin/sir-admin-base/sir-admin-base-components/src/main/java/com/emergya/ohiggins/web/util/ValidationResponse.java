package com.emergya.ohiggins.web.util;

import java.io.Serializable;
import java.util.List;

public class ValidationResponse implements Serializable {

	private static final long serialVersionUID = 151111660302007131L;
	private String status;
	private List errorMessageList;

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public List getErrorMessageList() {
		return this.errorMessageList;
	}

	public void setErrorMessageList(List errorMessageList) {
		this.errorMessageList = errorMessageList;
	}
}
