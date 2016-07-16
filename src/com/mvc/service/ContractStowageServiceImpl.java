package com.mvc.service;

import java.util.List;

import org.hibernate.annotations.Where;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mvc.dao.EntityDao;
import com.mvc.entity.Contract;
import com.mvc.entity.ContractStowage;
import com.mvc.entity.ContractStowageScore;
import com.mvc.entity.Project;

@Service("ContractStowageServiceImpl")
public class ContractStowageServiceImpl implements ContractStowageService{

	@Autowired
	EntityDao entityDao;
	
	@Transactional
	public ContractStowage getContractStowageOnByName(String name){
		StringBuffer sff = new StringBuffer();
		sff.append("select a from ").append(ContractStowage.class.getSimpleName()).append(" a ")
		.append("where a.name='").append(name).append("' and a.status='1'");
		List<Object> list = entityDao.createQuery(sff.toString());
		if (list.size()>0) {
			return (ContractStowage) list.get(0);
		} else{
			return null;
		}
	} 
	
	@Transactional
	public ContractStowage getContractStowageOnByNameAndType(String name,String type){
		StringBuffer sff = new StringBuffer();
		sff.append("select a from ").append(ContractStowage.class.getSimpleName()).append(" a ")
		.append("where a.name='").append(name).append("' and type='").append(type).append("' and a.status='1'");
		List<Object> list = entityDao.createQuery(sff.toString());
		if (list.size()>0) {
			return (ContractStowage) list.get(0);
		} else{
			return null;
		}
	} 
	
	@Transactional
	public ContractStowage getContractStowageByNameAndType(String name,String type){
		StringBuffer sff = new StringBuffer();
		sff.append("select a from ").append(ContractStowage.class.getSimpleName()).append(" a ")
		.append("where a.name='").append(name).append("' and type='").append(type).append("'");
		List<Object> list = entityDao.createQuery(sff.toString());
		if (list.size()>0) {
			return (ContractStowage) list.get(0);
		} else{
			return null;
		}
	} 
	@Transactional
	public List<Object> getContractStowageLikeByKeyAndStatus(String key, String status){
		StringBuffer sff = new StringBuffer();
		sff.append("select  a from ").append(ContractStowage.class.getSimpleName()).append(" a ")
		.append(" where a.name like '%" + key + "%' and a.status='").append(status).append("'");
		List<Object> list = entityDao.createQuery(sff.toString());
		return list;
	} 
	
	@Transactional
	public List<Object> getContractStowageLikeByKeyAndStatusAndType(String key, String status, String type){
		StringBuffer sff = new StringBuffer();
		sff.append("select  a from ").append(ContractStowage.class.getSimpleName()).append(" a ")
		.append(" where a.name like '%" + key + "%' and type='").append(type).append("' and a.status='").append(status).append("'");
		List<Object> list = entityDao.createQuery(sff.toString());
		return list;
	} 
	
	@Transactional
	public List<Object> getAllContractStowage(){
		StringBuffer sff = new StringBuffer();
		sff.append("select a from ").append(ContractStowage.class.getSimpleName()).append(" a ")
		.append("where 1=1");
		List<Object> list = entityDao.createQuery(sff.toString());
		return list;
	} 
	
	@Transactional
	public List<Object> getAllContractStowageOn(){
		StringBuffer sff = new StringBuffer();
		sff.append("select a from ").append(ContractStowage.class.getSimpleName()).append(" a ")
		.append("where 1=1 and a.status='1'");
		List<Object> list = entityDao.createQuery(sff.toString());
		return list;
	} 
	
	@Transactional
	public List<Object> getAllContractStowageOff(){
		StringBuffer sff = new StringBuffer();
		sff.append("select a from ").append(ContractStowage.class.getSimpleName()).append(" a ")
		.append("where 1=1 and a.status='0'");
		List<Object> list = entityDao.createQuery(sff.toString());
		return list;
	}
	
	@Transactional
	public List<Object> getAllContractStowageOffByType(String type){
		StringBuffer sff = new StringBuffer();
		sff.append("select a from ").append(ContractStowage.class.getSimpleName()).append(" a ")
		.append("where 1=1 and a.type='").append(type).append("' and a.status='0'");
		List<Object> list = entityDao.createQuery(sff.toString());
		return list;
	} 
	
	@Transactional
	public boolean isContractStowageOffByName(String name, String company){
		StringBuffer sff = new StringBuffer();
		sff.append("select a from ").append(ContractStowage.class.getSimpleName()).append(" a ")
		.append("where a.name='").append(name).append("' and a.company='").append(company).append("'  and a.status='0'");
		List<Object> list = entityDao.createQuery(sff.toString());
		if (list.size()>0) {
			return true;
		} else {
			return false;
		}
		
	}

	@Transactional
	public List<Object> getContractStowagesOnBy(String scoreType, Integer offset, Integer limit){
		StringBuffer sff = new StringBuffer();
		sff.append("select a from ").append(ContractStowage.class.getSimpleName()).append(" a ")
		.append("where a.status='1' order by a.").append(scoreType).append(" desc");
		List<Object> list = entityDao.createQueryLimit(sff.toString(), offset, limit);
		return list;
	}
	
	@Transactional
	public List<Object> getContractStowagesOnByAndType(String scoreType, String type, Integer offset, Integer limit){
		StringBuffer sff = new StringBuffer();
		sff.append("select a from ").append(ContractStowage.class.getSimpleName()).append(" a ")
		.append("where a.type='").append(type).append("' and a.status='1' order by a.").append(scoreType).append(" desc");
		List<Object> list = entityDao.createQueryLimit(sff.toString(), offset, limit);
		return list;
	}
	
	
	@Transactional
	public List<Object> getContractStowagesOffByAndType(String scoreType, String type, Integer offset, Integer limit){
		StringBuffer sff = new StringBuffer();
		sff.append("select a from ").append(ContractStowage.class.getSimpleName()).append(" a ")
		.append("where a.type='").append(type).append("' and a.status='0' order by a.").append(scoreType).append(" desc");
		List<Object> list = entityDao.createQueryLimit(sff.toString(), offset, limit);
		return list;
	}
	
	
	@Transactional
	public ContractStowage getContractStowageById(Integer id){
		ContractStowage contractStowage = (ContractStowage) entityDao.findById(ContractStowage.class, id);
		return contractStowage;
	}
	
	@Transactional
	public List<Object> getContractStowagesOnScoreByScId(Integer scId, Integer offset, Integer limit){
		StringBuffer sff = new StringBuffer();
		sff.append("select a from ").append(ContractStowageScore.class.getSimpleName()).append(" a ")
		.append("where a.scId=").append(scId).append(" and a.status='1' order by a.date asc");
		List<Object> list = entityDao.createQueryLimit(sff.toString(), offset, limit);
		return list;
	}
	
	@Transactional
	public Integer getContractStowagesCountByTypeAndStatus(String type, String status){
		StringBuffer sff = new StringBuffer();
		sff.append("select count(a.id) from ").append(ContractStowage.class.getSimpleName()).append(" a ")
		.append("where a.type='").append(type).append("' and a.status='").append(status).append("'");
		List<Object> list = entityDao.createQuery(sff.toString());
		Integer count = Integer.valueOf(list.get(0).toString());
		return count;
	}
	
	
	public ContractStowage save(ContractStowage contractStowage){
		entityDao.save(contractStowage);
		return contractStowage;
	}
	
	@Transactional
	public void update(ContractStowage contractStowage) {
		entityDao.update(contractStowage);
	}
	
	
	@Transactional
	public void delete(ContractStowage contractStowage){
		entityDao.delete(contractStowage);
	}

	
}
