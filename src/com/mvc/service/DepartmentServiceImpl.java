package com.mvc.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mvc.dao.EntityDao;
import com.mvc.entity.Department;
import com.mvc.entity.Project;
import com.mvc.entity.User;
import com.mvc.entity.UserLog;
import com.mvc.entity.Utp;

@Service("DepartmentServiceImpl")
public class DepartmentServiceImpl implements DepartmentService{
	@Autowired
	private EntityDao entityDao;
	
	@Transactional
	public List<Object> getAllDepartments(){
		StringBuffer sff = new StringBuffer();
		sff.append("select a from ").append(Department.class.getSimpleName()).append(" a ");
		List<Object> list = entityDao.createQuery(sff.toString());
		return list;
	}
	
	@Transactional
	public Department getDepartmentById(Integer departmentId){
		return (Department) entityDao.findById(Department.class, departmentId);
	}
	
	@Transactional
	public List<Object> getDepartmentByName(String departmentName){
		StringBuffer sff = new StringBuffer();
		sff.append("select a from ").append(Department.class.getSimpleName()).append(" a ")
		.append("where a.name='").append(departmentName).append("'");
		List<Object> list = entityDao.createQuery(sff.toString());
		return list;
	}
	
	public void saveDepartment(Department department){
		entityDao.save(department);
	}
	
	@Transactional
	public void deleteDepartment(Department department){
		entityDao.delete(department);
	}
}
