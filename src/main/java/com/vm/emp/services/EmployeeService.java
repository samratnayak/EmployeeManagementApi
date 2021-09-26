package com.vm.emp.services;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.vm.emp.exceptions.CycleInEmployeeHierarchyExcepton;
import com.vm.emp.models.Employee;
import com.vm.emp.repositories.EmployeeRepository;
import com.vm.emp.services.utils.ValidationUtil;

@Service
public class EmployeeService {
	
	public static Logger LOGGER = LoggerFactory.getLogger(EmployeeService.class);

    @Autowired
    private EmployeeRepository empRepo;

    public List<Employee> findAll(){
        return empRepo.findAll();
    }

    public Employee save(Employee e){
        return empRepo.save(e);
    }
    
    public Employee findById(String id) {
    	if(ValidationUtil.isNumericString(id)) {
    		return empRepo.findById(Integer.parseInt(id)).orElseGet(() -> null);
    	}
    	return null;
    }

	public Employee findCommonManager(int id1, int id2) {
		Employee e1 = findById(id1);
		Employee e2 = findById(id2);
		Set<Integer> s = new HashSet<>();
		
		while(e1 != null) {
			e1 = e1.getManager();
			if(e1 != null && e1.getId() != null && e1.getId() != 0) {
				s.add(e1.getId());
			}
		}
		
		while(e2 != null) {
			e2 = e2.getManager();
			if(e2 != null && e2.getId() != null && e2.getId() != 0 && s.contains(e2.getId())) {
				return e2;
			}
		}
		
		return null;
	}

	public void checkCycleInHierarchy(Employee e) {
		Employee temp = e;
		Employee fast = e;
		
		while(temp != null) {
			temp = temp.getManager();
			if(fast != null && fast.getManager() != null)
				fast = fast.getManager().getManager();
			if(temp != null && fast != null)
				LOGGER.debug("temp {} fast {}", temp.getId(), fast.getId());
			if(fast != null && temp != null && fast == temp) {
				throw new CycleInEmployeeHierarchyExcepton("Cycle found in hierachy with Emp id="+e.getId());
			}
		}
	}

	public List<Employee> findReportees(String empId) {
		if(ValidationUtil.isNumericString(empId)) {
			return empRepo.findReportees(Integer.parseInt(empId));
		}
		return null;
	}

	public List<Employee> findManagers(String empId) {
		if(ValidationUtil.isNumericString(empId)) {
			Optional<Employee> e = empRepo.findById(Integer.parseInt(empId));
			if(e.isPresent()) {
				List<Employee> employees = new ArrayList<Employee>();
				Employee it = e.get().getManager();
				while(it != null) {
					employees.add(it);
					it = it.getManager();
				}
				return employees;
			}
		}
		return null;
	}
	
	public Employee findById(int id) {
		return empRepo.findById(id).orElse(null);
	}
}
