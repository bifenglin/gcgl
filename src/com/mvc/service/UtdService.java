package com.mvc.service;

import java.util.List;

import org.springframework.transaction.annotation.Transactional;

import com.mvc.entity.Department;
import com.mvc.entity.User;
import com.mvc.entity.Utd;
import com.mvc.entity.Utp;

public interface UtdService {
	public List<Object> getUtdByUser(User user);
	public Utd getUtdByUserNameAndDepartmentName(String userName, String departmentName);
	public void save(Utd utd);
	public List<Object> getUtdByDepartment(Department department);
	public void delete(Utd utd);
}
