package com.vm.emp.services;

import org.springframework.stereotype.Service;

import com.vm.emp.models.Employee;
import com.vm.requests.EmployeeRequest;

@Service
public class RequestToModelCoverterService {

	
	public Employee convertToModel(EmployeeRequest request) {
		Employee e = new Employee();
		e.setName(request.getName());
		e.setAge(Integer.parseInt(request.getAge()));
		return e;
	}
}
