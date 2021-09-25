package com.vm.emp.services;

import com.vm.emp.models.Employee;
import com.vm.emp.repositories.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmployeeService {

    @Autowired
    private EmployeeRepository empRepo;

    public List<Employee> findAll(){
        return empRepo.findAll();
    }

    public Employee save(Employee e){
        return empRepo.save(e);
    }
}
