package com.vm.emp.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.vm.apiresponses.EmployeeApiResponse;
import com.vm.emp.exceptions.CycleInEmployeeHierarchyExcepton;
import com.vm.emp.models.Employee;
import com.vm.requests.EmployeeRequest;
import static com.vm.emp.services.utils.ValidationUtil.*;

import java.util.List;
import java.util.Objects;

@Service
public class EmployeeValidationService {
	
	
	public static Logger LOGGER = LoggerFactory.getLogger(EmployeeService.class);
	
	@Autowired
	private EmployeeService employeeService;

	public EmployeeApiResponse<String> validate(EmployeeRequest emp) {
		EmployeeApiResponse<String> resp = new EmployeeApiResponse<String>();
		if(!isValidString(emp.getName()) || !isValidName(emp.getName())) {
			resp.getErrors().add("Name can't be null & Must start with a letter");
		}
		if(!isValidString(emp.getAge()) || !isValidAge(emp.getAge())) {
			resp.getErrors().add("Age can't be null & Must be a number below 100");
		}
		
		validateManagerId(emp, resp);
		
		if(resp.getErrors().size() > 0) {
			resp.setData("Failed");
		}
		
		return resp;
	}

	private void validateManagerId(EmployeeRequest emp, EmployeeApiResponse<String> resp) {
		if(isValidString(emp.getManagerId())) {
			if(!isNumericString(emp.getManagerId())) {
				resp.getErrors().add("Manager Id contains non-numeric characters");
			} else {
				Employee manager = employeeService.findById(Integer.parseInt(emp.getManagerId()));
				LOGGER.debug("manager {}", manager);
				if(Objects.isNull(manager) || manager.getId() == 0)
					resp.getErrors().add("Manager Id is not Found in System, please leave empty if not sure");
			}
		}
	}
	
	public EmployeeApiResponse<String> validateForPatch(EmployeeRequest emp) {
		EmployeeApiResponse<String> resp = new EmployeeApiResponse<String>();
		if(isValidString(emp.getName()) && !isValidName(emp.getName())) {
			resp.getErrors().add("Name must start with Letter");
		}
		if(isValidString(emp.getAge()) && !isValidAge(emp.getAge())) {
			resp.getErrors().add("Age Must be a number below 100");
		}
		
		validateManagerId(emp, resp);
		
		return resp;
	}
	
	public boolean isAllEmpty(EmployeeRequest emp) {
		return !isValidString(emp.getName()) && !isValidString(emp.getAge()) && !isValidString(emp.getManagerId());
	}
	
	public EmployeeApiResponse<String> validateAllEmpty(EmployeeRequest emp){
		EmployeeApiResponse<String> resp = new EmployeeApiResponse<String>();
		if(isAllEmpty(emp)) {
			resp.getErrors().add("All Fields Empty");
		}
		return resp;
	}
	
	public boolean checkCyclePresentInHierarchy(Employee e, EmployeeRequest emp) {
		if (isValidString(emp.getManagerId())) {
			if(e.getId() == Integer.parseInt(emp.getManagerId())) {
				throw new CycleInEmployeeHierarchyExcepton("Employee can't be his/her own Manager id=" + e.getId());
			}
			List<Employee> reportees = employeeService.findReportees(String.valueOf(e.getId()));
			reportees.stream().forEach(employee -> {
				if (String.valueOf(employee.getId()).equals(emp.getManagerId())) {
					throw new CycleInEmployeeHierarchyExcepton("Cycle in hierarchy found for id=" + e.getId()+", Please remove the manager from id="+emp.getManagerId());
				}
			});
		}
		return false;
	}

	
}
