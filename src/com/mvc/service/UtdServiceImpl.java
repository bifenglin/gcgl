package com.mvc.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mvc.dao.EntityDao;
import com.mvc.entity.Department;
import com.mvc.entity.User;
import com.mvc.entity.Utd;
import com.mvc.entity.Utp;


@Service("UtdServiceImpl")
public class UtdServiceImpl implements UtdService{
	
	@Autowired
	private EntityDao entityDao;
	
	@Transactional
	public List<Object> getUtdByUser(User user){
		StringBuffer sff = new StringBuffer();
		sff.append("select a from ").append(Utd.class.getSimpleName()).append(" a ")
		.append("where a.userName='").append(user.getName()).append("'");
		List<Object> list = entityDao.createQuery(sff.toString());
		return list;
	}
	
	@Transactional
	public List<Object> getUtdByDepartment(Department department){
		StringBuffer sff = new StringBuffer();
		sff.append("select a from ").append(Utd.class.getSimpleName()).append(" a ")
		.append("where a.departmentName='").append(department.getName()).append("'");
		List<Object> list = entityDao.createQuery(sff.toString());
		return list;
	}
	@Transactional
	public Utd getUtdByUserNameAndDepartmentName(String userName, String departmentName){
		List propertyNameList = new ArrayList();
		List propertyValueList = new ArrayList();
		propertyNameList.add("userName");
		propertyNameList.add("departmentName");
		propertyValueList.add(userName);
		propertyValueList.add(departmentName);
		List<Object> list = entityDao.findByHQLCondition(Utd.class, propertyNameList, propertyValueList);
		if(list.size() >0 ){
			return (Utd) list.get(0);
		} else {
			return null;
		}
		
	}
	
	public void delete(Utd utd) {
		entityDao.delete(utd);
	}
	
	public void save(Utd utd){
		entityDao.save(utd);
	}
	
	
}
