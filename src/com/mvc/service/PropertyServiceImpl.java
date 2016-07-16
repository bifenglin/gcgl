package com.mvc.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mvc.dao.EntityDao;
import com.mvc.entity.Contract;
import com.mvc.entity.Property;

@Service("PropertyServiceImpl")
public class PropertyServiceImpl implements PropertyService{
	@Autowired
	private EntityDao entityDao;
	
	@Transactional
	public List<Object> getPropertysByContractIdAndProjectName(String contractId, String projectName){
		StringBuffer sff = new StringBuffer();
		sff.append("select a from ").append(Property.class.getSimpleName()).append(" a ")
		.append("where a.projectName='").append(projectName).append("' and a.contractId='").append(contractId).append("'");
		List<Object> list = entityDao.createQuery(sff.toString());
		return list;
	}
	
	@Transactional
	public List<Object> getPropertysOnByContractIdAndProjectName(String contractId, String projectName){
		StringBuffer sff = new StringBuffer();
		sff.append("select a from ").append(Property.class.getSimpleName()).append(" a ")
		.append("where a.projectName='").append(projectName).append("' and a.contractId='").append(contractId).append("' and a.status='1'");
		List<Object> list = entityDao.createQuery(sff.toString());
		return list;
	}
	
	@Transactional
	public Property getPropertyByPropertyId(String projectName, String propertyId){
		StringBuffer sff = new StringBuffer();
		sff.append("select a from ").append(Property.class.getSimpleName()).append(" a ")
		.append("where a.projectName='").append(projectName).append("' and a.propertyId='").append(propertyId).append("'");
		List<Object> list = entityDao.createQuery(sff.toString());
		if (list.size()>0) {
			return (Property)list.get(0);
		}
		return null;
	}
	
	
	public void save(Property property){
		entityDao.save(property);
	}
	@Transactional
	public void update(Property property){
		entityDao.update(property);
	}
}
