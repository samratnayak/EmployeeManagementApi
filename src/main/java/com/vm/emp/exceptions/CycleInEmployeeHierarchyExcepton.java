package com.vm.emp.exceptions;

public class CycleInEmployeeHierarchyExcepton extends RuntimeException{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public CycleInEmployeeHierarchyExcepton(String message){
		super(message);
	}

}
