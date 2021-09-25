package com.vm.emp.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.vm.apiresponses.EmployeeApiResponse;
import com.vm.emp.models.Employee;
import com.vm.emp.services.EmployeeService;
import com.vm.emp.services.EmployeeValidationService;
import com.vm.emp.services.RequestToModelCoverterService;
import com.vm.requests.EmployeeRequest;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
@Api(value = "Employee Rest end points")
public class EmployeeController {

    @Autowired
    private EmployeeService service;
    
    @Autowired
    private EmployeeValidationService validationService;
    
    @Autowired
    private RequestToModelCoverterService convertService;
    
    
    @PostMapping("/employees")
    @ApiOperation(value = "Create an Employee", response = EmployeeApiResponse.class)
    @ApiResponses(value = { @ApiResponse(code = 201, message = "Created"),
    		@ApiResponse(code = 400, message = "Bad Request")})
    public ResponseEntity<EmployeeApiResponse> create( @RequestBody
    		EmployeeRequest employeeRequestEntity
    		){
    	EmployeeApiResponse resp = validationService.validate(employeeRequestEntity);
    	if(resp.getErrors().size() > 0) {
    		return new ResponseEntity<EmployeeApiResponse>(resp, HttpStatus.BAD_REQUEST);
    	}
    	
    	Employee e = convertService.convertToModel(employeeRequestEntity);
    	int id = service.save(e).getId();
    	
    	resp.setMessage("Success, id="+id);
    	return new ResponseEntity<EmployeeApiResponse>(resp, HttpStatus.CREATED);
    }
    
    @GetMapping("/employees")
    @ApiOperation(value = "Get all employees", response = List.class)
    @ApiResponses(value = { @ApiResponse(code = 200, message = "Success|OK")})
    public ResponseEntity<List<Employee>> employees(){
        List<Employee> employees = service.findAll();
        return new ResponseEntity<>(employees, HttpStatus.OK);
    }
}
