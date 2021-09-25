package com.vm.emp.services;

import com.vm.emp.models.Employee;
import com.vm.emp.repositories.EmployeeRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import javax.persistence.EntityNotFoundException;

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
    
    public Employee findById(Integer id) {
    	Employee e = null;
    	try {
    	 e = empRepo.getById(id);
    	} catch(EntityNotFoundException ex) {
    		LOGGER.error("Emp Not found id {}", id);
    	}
    	return e;
    }
}
