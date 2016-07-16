package com.mvc.service;

import java.util.List;

import com.mvc.entity.User;

public interface UserService {
	public List<Object> getUser();
	public void save(User user);
	public void delete(Object obj);
	public List<Object> getUser(User user);
	public User getUserByName(String name);
	public void update(User user);
	public Integer getAllUserNumber();
	public Integer getAllAdminNumber();
	public List<Object> getUserByDepartmentId(Integer departmentId);
	public List<Object> getUsersNotAdminAndNotInDepatment(String departmentName);
	public List<Object> getUsersNotAdmin();
	public List<Object> getAdmin();
	public User getUserById(Integer id);
	public void delete(User user);
}
