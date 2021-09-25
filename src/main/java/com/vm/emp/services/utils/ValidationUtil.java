package com.vm.emp.services.utils;

public class ValidationUtil {

	public static boolean isValidName(String eName) {
		return eName != null && eName.matches("^[A-Za-z].+");
	}
	
	public static boolean isValidAge(String age) {
		return age != null && age.matches("^[1-9][0-9]$");
	}
	
	public static boolean isNumericString(String managerId) {
		return managerId.matches("[0-9]+");
	}
	
	public static boolean isValidString(String val) {
		return val != null  && val.length() > 0;
	}
}
