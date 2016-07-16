package com.mvc.service;

import java.util.List;

import com.mvc.entity.Department;

public interface DepartmentService {
	public List<Object> getAllDepartments();
	public void saveDepartment(Department department);
	public Department getDepartmentById(Integer departmentId);
	public List<Object> getDepartmentByName(String departmentName);
	public void deleteDepartment(Department department);
}
