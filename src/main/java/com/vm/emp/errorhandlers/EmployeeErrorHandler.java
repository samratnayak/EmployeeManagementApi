package com.vm.emp.errorhandlers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.vm.apiresponses.EmployeeApiResponse;
import com.vm.emp.exceptions.CycleInEmployeeHierarchyExcepton;

@ControllerAdvice
public class EmployeeErrorHandler {

	@ExceptionHandler(value 
		      = { CycleInEmployeeHierarchyExcepton.class})
	public ResponseEntity<EmployeeApiResponse<String>> handleCycleInEmployeeHierarchyExcepton(RuntimeException ex){
		EmployeeApiResponse<String> errorResp = new EmployeeApiResponse<String>();
		errorResp.getErrors().add(ex.getMessage());
		return new ResponseEntity<EmployeeApiResponse<String>>(errorResp, HttpStatus.FORBIDDEN);
	}
}
