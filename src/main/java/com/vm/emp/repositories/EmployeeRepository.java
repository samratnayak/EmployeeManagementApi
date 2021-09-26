package com.vm.emp.repositories;

import com.vm.emp.models.Employee;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface EmployeeRepository extends JpaRepository<Employee, Integer> {

	@Query(value = "WITH RECURSIVE temp(id, managerid) as (" + 
			"SELECT id, managerid FROM employee WHERE id= :empId " + 
			"UNION ALL " + 
			"SELECT e.id, t.id FROM temp t, employee e WHERE e.managerid = t.id " + 
			") " + 
			"SELECT e.* FROM temp t INNER JOIN employee e on e.id=t.id WHERE t.id != :empId", nativeQuery = true)
	List<Employee> findReportees(@Param("empId") int empId);
}
