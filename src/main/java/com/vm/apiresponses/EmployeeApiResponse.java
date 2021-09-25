package com.vm.apiresponses;

import java.util.ArrayList;
import java.util.List;

public class EmployeeApiResponse {

	private String message;
	private List<String> errors = new ArrayList<String>(0);
	
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public List<String> getErrors() {
		return errors;
	}
	public void setErrors(List<String> errors) {
		this.errors = errors;
	}
	
	
}
