package com.vm.emp.mappers;

import java.util.Objects;

import com.vm.emp.models.Employee;
import com.vm.emp.models.views.EmployeeView;
import com.vm.emp.models.views.EmployeeView.EmployeeViewBuilder;

public class EmployeeMapper {

	public static EmployeeView toEmployeeView(Employee e) {
		if(Objects.isNull(e)) {
			return null;
		}
		int managerId = -1;
		String managerName = null;
		if(e.getManager() != null) {
			managerName = e.getManager().getName();
			managerId = e.getManager().getId();
		}
		return EmployeeViewBuilder.instance()
				.id(e.getId())
				.age(e.getAge())
				.name(e.getName())
				.managerName(managerName)
				.managerId(managerId)
				.build();
	}
	
}
