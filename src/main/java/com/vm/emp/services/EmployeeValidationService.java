package com.vm.emp.services;

import org.springframework.stereotype.Service;

import com.vm.apiresponses.EmployeeApiResponse;
import com.vm.requests.EmployeeRequest;
import static com.vm.emp.services.utils.ValidationUtil.*;

@Service
public class EmployeeValidationService {

	public EmployeeApiResponse validate(EmployeeRequest emp) {
		EmployeeApiResponse resp = new EmployeeApiResponse();
		if(!isValidName(emp.getName())) {
			resp.getErrors().add("Name can't be null & Must start with a letter");
		}
		if(!isValidAge(emp.getAge())) {
			resp.getErrors().add("Age can't be null & Must be a number");
		}
		
		if(resp.getErrors().size() > 0) {
			resp.setMessage("Failed");
		}
		
		return resp;
	}
}
