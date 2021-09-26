package com.vm.emp.controllers;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.vm.apiresponses.EmployeeApiResponse;
import com.vm.emp.mappers.EmployeeMapper;
import com.vm.emp.models.Employee;
import com.vm.emp.models.views.EmployeeView;
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

	public static final Logger LOGGER = LoggerFactory.getLogger(EmployeeController.class);

	@Autowired
	private EmployeeService service;

	@Autowired
	private EmployeeValidationService validationService;

	@Autowired
	private RequestToModelCoverterService convertService;

	@PostMapping("/employees")
	@ApiOperation(value = "Create an Employee", response = EmployeeApiResponse.class)
	@ApiResponses(value = { @ApiResponse(code = 201, message = "Created"),
			@ApiResponse(code = 400, message = "Bad Request") })
	public ResponseEntity<EmployeeApiResponse<String>> create(@RequestBody EmployeeRequest employeeRequestEntity) {
		EmployeeApiResponse<String> resp = validationService.validate(employeeRequestEntity);
		if (resp.getErrors().size() > 0) {
			return new ResponseEntity<EmployeeApiResponse<String>>(resp, HttpStatus.BAD_REQUEST);
		}
		Employee e = convertService.convertToModel(employeeRequestEntity);
		int id = service.save(e).getId();

		resp.setData("Success, id=" + id);
		return new ResponseEntity<EmployeeApiResponse<String>>(resp, addAccessHeader(), HttpStatus.CREATED);
	}

	private HttpHeaders addAccessHeader() {
		HttpHeaders headers = new HttpHeaders();
		headers.add("Access-Control-Allow-Origin", "*");
		return headers;
	}
	
	@PatchMapping("/employees/{empId}")
	@ApiOperation(value = "Update an an Employee", response = EmployeeApiResponse.class)
	@ApiResponses(value = { @ApiResponse(code = 201, message = "Updated"),
	@ApiResponse(code = 400, message = "Bad Request") })
	public ResponseEntity<EmployeeApiResponse<String>> putEmployee(@RequestBody EmployeeRequest employeeRequestEntity, 
			@PathVariable(value = "empId") String empId) {
		final EmployeeApiResponse<String> resp = validationService.validateForPatch(employeeRequestEntity);
		if (resp.getErrors().size() > 0) {
			return new ResponseEntity<EmployeeApiResponse<String>>(resp, HttpStatus.BAD_REQUEST);
		}
		
		final EmployeeApiResponse<String> emptyResp = validationService.validateAllEmpty(employeeRequestEntity);
		if(emptyResp.getErrors().size() > 0) {
			return new ResponseEntity<EmployeeApiResponse<String>>(emptyResp, addAccessHeader(), HttpStatus.BAD_REQUEST);
		}
		
		final Employee existingEmp = service.findById(empId);
		if(Objects.isNull(existingEmp)) {
			final EmployeeApiResponse<String> empNotFoundResp = new EmployeeApiResponse<String>();
			empNotFoundResp.getErrors().add("Employee not found with id="+empId);
			return new ResponseEntity<EmployeeApiResponse<String>>(empNotFoundResp, HttpStatus.BAD_REQUEST);
		}
		
		validationService.checkCyclePresentInHierarchy(existingEmp, employeeRequestEntity);
		
		Employee e = convertService.mergeWithExistingEmployee(employeeRequestEntity, existingEmp);
		service.save(e);
		
		final EmployeeApiResponse<String> successResp = new EmployeeApiResponse<String>();
		successResp.setData("Successfully updated details for emp id "+empId);
		
		return new ResponseEntity<EmployeeApiResponse<String>>(successResp, addAccessHeader(), HttpStatus.CREATED);
	}

	@GetMapping("/employees")
	@ApiOperation(value = "Get all employees", response = List.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Success|OK") })
	public ResponseEntity<List<EmployeeView>> employees() {
		List<Employee> employees = service.findAll();
		LOGGER.info("employees {}", employees.size());
		return new ResponseEntity<>(employees.stream().map(EmployeeMapper::toEmployeeView).collect(Collectors.toList()),
				addAccessHeader(),
				HttpStatus.OK);
	}

	@GetMapping("/employees/common-manager")
	@ApiOperation(value = "Common Superior Manager between 2 employees", response = EmployeeView.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Success|OK") })
	public ResponseEntity<EmployeeApiResponse<EmployeeView>> commonSuperiorManager(@RequestParam(value = "id1") int id1,
			@RequestParam(value = "id2") int id2) {
		Employee e = service.findCommonManager(id1, id2);
		EmployeeView view = EmployeeMapper.toEmployeeView(e);
		EmployeeApiResponse<EmployeeView> response = new EmployeeApiResponse<EmployeeView>();
		response.setData(view);
		if(Objects.isNull(view)) {
			response.getErrors().add("Common Manager Not Found!");
			return new ResponseEntity<>(response, addAccessHeader(), HttpStatus.NOT_FOUND);
		}
		
		return new ResponseEntity<>(response, HttpStatus.OK);
	}
	
	@GetMapping("/employees/{empId}/reportees")
	@ApiOperation(value = "All reportess for an Employee", response = EmployeeView.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Success|OK") })
	public ResponseEntity<EmployeeApiResponse<List<EmployeeView>>> reportees(@PathVariable(value = "empId") String empId) {
		List<Employee> reportees = service.findReportees(empId);
		List<EmployeeView> reporteesView = Optional.ofNullable(reportees)
			.orElse(new ArrayList<>(0))
			.stream()
			.map(EmployeeMapper::toEmployeeView)
			.collect(Collectors.toList());
		
		EmployeeApiResponse<List<EmployeeView>> response = new EmployeeApiResponse<List<EmployeeView>>();
		response.setData(reporteesView);
		
		if(reporteesView.size() == 0) {
			response.getErrors().add("No reportees found under employee with id "+empId);
			return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
		}
		
		return new ResponseEntity<>(response, addAccessHeader(), HttpStatus.OK);
	}
	
	@GetMapping("/employees/{empId}/managers")
	@ApiOperation(value = "All managers in hierarchy for an Employee", response = EmployeeView.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Success|OK") })
	public ResponseEntity<EmployeeApiResponse<List<EmployeeView>>> managers(@PathVariable(value = "empId") String empId) {
		List<Employee> reportees = service.findManagers(empId);
		List<EmployeeView> reporteesView = Optional.ofNullable(reportees)
			.orElse(new ArrayList<>(0))
			.stream()
			.map(EmployeeMapper::toEmployeeView)
			.collect(Collectors.toList());
		
		EmployeeApiResponse<List<EmployeeView>> response = new EmployeeApiResponse<List<EmployeeView>>();
		response.setData(reporteesView);
		
		if(reporteesView.size() == 0) {
			response.getErrors().add("No managers found in hierarchy for Employee id "+empId);
			return new ResponseEntity<>(response, addAccessHeader(), HttpStatus.NOT_FOUND);
		}
		
		return new ResponseEntity<>(response, HttpStatus.OK);
	}
	
	@GetMapping("/employees/{empId}")
	@ApiOperation(value = "Employee By ID", response = EmployeeView.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Success|OK") })
	public ResponseEntity<EmployeeApiResponse<EmployeeView>> employeeById(@PathVariable(value = "empId") int empId) {
		Employee e = service.findById(empId);
		EmployeeView view = EmployeeMapper.toEmployeeView(e);
		EmployeeApiResponse<EmployeeView> response = new EmployeeApiResponse<EmployeeView>();
		response.setData(view);
		if(Objects.isNull(view)) {
			response.getErrors().add("Employee Not Found! with id ="+empId);
			return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
		}
		
		return new ResponseEntity<>(response, addAccessHeader(), HttpStatus.OK);
	}
}
