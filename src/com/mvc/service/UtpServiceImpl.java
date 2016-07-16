package com.mvc.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mvc.dao.EntityDao;
import com.mvc.entity.User;
import com.mvc.entity.Utp;

@Service("UtpServiceImpl")
public class UtpServiceImpl implements UtpService{
	
	@Autowired
	private EntityDao entityDao;
	
	@Transactional
	public List<Object> getUtpByUser(User user){
		StringBuffer sff = new StringBuffer();
		sff.append("select a from ").append(Utp.class.getSimpleName()).append(" a ")
		.append("where a.userName='").append(user.getName()).append("'");
		List<Object> list = entityDao.createQuery(sff.toString());
		return list;
	}
	
	@Transactional
	public Utp getUtpByUserNameAndProjectName(String userName, String projectName){
		List propertyNameList = new ArrayList();
		List propertyValueList = new ArrayList();
		propertyNameList.add("userName");
		propertyNameList.add("projectName");
		propertyValueList.add(userName);
		propertyValueList.add(projectName);
		List<Object> list = entityDao.findByHQLCondition(Utp.class, propertyNameList, propertyValueList);
		if (list.size()>0) {
			return (Utp) list.get(0);
		} else {
			return null;
		}
		
		
	}
	
	@Transactional
	public List<Object> getUtpByProjectName(String projectName){
		StringBuffer sff = new StringBuffer();
		sff.append("select a from ").append(Utp.class.getSimpleName()).append(" a ")
		.append("where a.projectName='").append(projectName).append("'");
		List<Object> list = entityDao.createQuery(sff.toString());
		return list;
	}
	
	@Transactional
	public void updateUtp(Utp utp) {
		entityDao.update(utp);
	}
	
	public void delete(Utp utp){
		entityDao.delete(utp);
	}
	
	public void save(Utp utp){
		entityDao.save(utp);
	}
}
