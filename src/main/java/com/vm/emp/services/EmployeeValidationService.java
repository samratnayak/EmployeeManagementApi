package com.vm.emp.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.vm.apiresponses.EmployeeApiResponse;
import com.vm.emp.models.Employee;
import com.vm.requests.EmployeeRequest;
import static com.vm.emp.services.utils.ValidationUtil.*;

import java.util.Objects;

@Service
public class EmployeeValidationService {
	
	
	public static Logger LOGGER = LoggerFactory.getLogger(EmployeeService.class);
	
	@Autowired
	private EmployeeService employeeService;

	public EmployeeApiResponse validate(EmployeeRequest emp) {
		EmployeeApiResponse resp = new EmployeeApiResponse();
		if(!isValidName(emp.getName())) {
			resp.getErrors().add("Name can't be null & Must start with a letter");
		}
		if(!isValidAge(emp.getAge())) {
			resp.getErrors().add("Age can't be null & Must be a number");
		}
		
		if(!(Objects.isNull(emp.getManagerId()) || "".equals(emp.getManagerId()))) {
			if(!isNumericString(emp.getManagerId())) {
				resp.getErrors().add("Manager Id contains non-numeric characters");
			} else {
				Employee manager = employeeService.findById(Integer.parseInt(emp.getManagerId()));
				LOGGER.info("manager {}", manager);
				if(Objects.isNull(manager) || manager.getId() == 0)
					resp.getErrors().add("Manager Id is not Found in System, please leave empty if not sure");
			}
		}
		
		if(resp.getErrors().size() > 0) {
			resp.setMessage("Failed");
		}
		
		return resp;
	}
}
