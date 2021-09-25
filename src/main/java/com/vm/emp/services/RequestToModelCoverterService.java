package com.vm.emp.services;

import org.springframework.beans.factory.annotation.Autowired;
import static com.vm.emp.services.utils.ValidationUtil.isValidString;
import org.springframework.stereotype.Service;

import com.vm.emp.models.Employee;
import com.vm.requests.EmployeeRequest;

@Service
public class RequestToModelCoverterService {

	@Autowired
	private EmployeeService empService;
	
	public Employee convertToModel(EmployeeRequest request) {
		Employee e = new Employee();
		e.setName(request.getName());
		e.setAge(Integer.parseInt(request.getAge()));
		if(isValidString(request.getManagerId()))
			e.setManager(empService.findById(Integer.parseInt(request.getManagerId())));
		return e;
	}
}
