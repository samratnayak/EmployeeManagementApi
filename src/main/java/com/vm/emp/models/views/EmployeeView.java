package com.vm.emp.models.views;

public class EmployeeView {

	private String id;
	private String name;
	private String age;
	private String managerId;
	private String managerName;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getAge() {
		return age;
	}
	public void setAge(String age) {
		this.age = age;
	}
	public String getManagerId() {
		return managerId;
	}
	public void setManagerId(String managerId) {
		this.managerId = managerId;
	}
	public String getManagerName() {
		return managerName;
	}
	public void setManagerName(String managerName) {
		this.managerName = managerName;
	}
	
	public static class EmployeeViewBuilder {
		private String id;
		private String name;
		private String age;
		private String managerId;
		private String managerName;
		
		private EmployeeViewBuilder() {}
		
		public static EmployeeViewBuilder instance() {
			return new EmployeeViewBuilder();
		}
		
		public EmployeeViewBuilder id(int id) {
			this.id = String.valueOf(id);
			return this;
		}
		
		public EmployeeViewBuilder age(int age) {
			this.age = String.valueOf(age);
			return this;
		}
		
		public EmployeeViewBuilder name(String name) {
			this.name = name;
			return this;
		}
		
		public EmployeeViewBuilder managerId(int managerId) {
			if(managerId > 0)
				this.managerId = String.valueOf(managerId);
			return this;
		}
		
		public EmployeeViewBuilder managerName(String managerName) {
			this.managerName = managerName;
			return this;
		}
		
		public EmployeeView build() {
			EmployeeView view = new EmployeeView();
			view.setId(this.id);
			view.setName(this.name);
			view.setAge(this.age);
			view.setManagerName(this.managerName);
			view.setManagerId(this.managerId);
			return view;
		}
	}
}
