package com.mvc.service;

import java.security.acl.Owner;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mvc.dao.EntityDao;
import com.mvc.entity.CostLog;
import com.mvc.entity.ProjectLog;
import com.mvc.util.DateUtil;

@Service("CostLogServiceImpl")
public class CostLogServiceImpl implements CostLogService{
	@Autowired
	private EntityDao entityDao;
	
	@Transactional
	public CostLog getCostLogById(Integer id){
		return (CostLog) entityDao.findById(CostLog.class, id);
	}
	
	@Transactional
	public CostLog getCostLogOnByLocationId(Integer locationId){
		StringBuffer sff = new StringBuffer();
		sff.append("select a from ").append(CostLog.class.getSimpleName()).append(" a ")
		.append("where a.locationId=").append(locationId).append(" and status='1' ");
		List<Object> list = entityDao.createQuery(sff.toString());
		if (list.size()>0) {
			return (CostLog) list.get(0);
		} else {
			return null;
		}
		
	}
	
	@Transactional
	public List getCostLogByProjectName(String projectName){
		List propertyNameList = new ArrayList();
		propertyNameList.add("projectName");
		List propertyValueList = new ArrayList();
		propertyValueList.add(projectName);
		List<Object> list = entityDao.findByHQLCondition(CostLog.class, propertyNameList, propertyValueList);
		return list;
	}
	
	@Transactional
	public List getCostLogByPropertyId(String propertyId, String projectName){
		List propertyNameList = new ArrayList();
		propertyNameList.add("propertyId");
		propertyNameList.add("projectName");
		List propertyValueList = new ArrayList();
		propertyValueList.add(propertyId);
		propertyValueList.add(projectName);
		List<Object> list = entityDao.findByHQLCondition(CostLog.class, propertyNameList, propertyValueList);
		return list;
	}
	
	@Transactional
	public List getCostLogByContractId(String ContractId, String projectName){
		StringBuffer sff = new StringBuffer();
		List propertyNameList = new ArrayList();
		propertyNameList.add("contractId");
		propertyNameList.add("projectName");
		List propertyValueList = new ArrayList();
		propertyValueList.add(ContractId);
		propertyValueList.add(projectName);
		List<Object> list = entityDao.findByHQLCondition(CostLog.class, propertyNameList, propertyValueList);
		return list;
	}
	
	@Transactional
	public List getCostLogByContractIdAndDate(String contractId, String projectName, String startDate, String endDate){
		StringBuffer sff = new StringBuffer();
		sff.append("select a from ").append(CostLog.class.getSimpleName()).append(" a ")
		.append("where a.projectName='").append(projectName).append("' and a.contractId='").append(contractId).append("' and startDate between '")
		.append(startDate).append("' and '").append(endDate).append("' ");
		List<Object> list = entityDao.createQuery(sff.toString());
		return list;
	}
	
	@Transactional
	public List getCostLogByPropertyIdAndDate(String projectName, String propertyId, String startDate, String endDate){
		StringBuffer sff = new StringBuffer();
		sff.append("select a from ").append(CostLog.class.getSimpleName()).append(" a ")
		.append("where a.projectName='").append(projectName).append("' and a.propertyId='").append(propertyId).append("' and startDate between '")
		.append(startDate).append("' and '").append(endDate).append("'");
		List<Object> list = entityDao.createQuery(sff.toString());
		return list;
	}
	
	@Transactional
	public List getTodayCostLogByProjectAndDate(String projectName){
		DateUtil dateUtil = new DateUtil();
		String startDate = dateUtil.dayStartDate(new Date());
		String endDate = dateUtil.dayEndDate(new Date());
		StringBuffer sff = new StringBuffer();
		sff.append("select distinct a from ").append(CostLog.class.getSimpleName()).append(" a ")
		.append("where (a.projectName='").append(projectName).append("' and a.status='1') or (a.projectName = '").append(projectName).append("' and  (startDate between '")
		.append(startDate).append("' and '").append(endDate).append("') and (endDate between '").append(startDate).
		append("' and '").append(endDate).append("') and a.status='0')");
		List<Object> list = entityDao.createQuery(sff.toString());
		return list;
	}
	
	@Transactional
	public List getTodayCostLogByProjectAndDate(String projectName, String startDate, String endDate){
		StringBuffer sff = new StringBuffer();
		sff.append("select distinct a from ").append(CostLog.class.getSimpleName()).append(" a ")
		.append("where a.projectName='").append(projectName).append("' and a.status='1' or (startDate between '")
		.append(startDate).append("' and '").append(endDate).append("') or (endDate between '").append(startDate).
		append("' and '").append(endDate).append("')");
		List<Object> list = entityDao.createQuery(sff.toString());
		return list;
	}
	
	@Transactional
	public List<Object> getCostLogByProjectName(String projectName, Integer offset, Integer limit){
		StringBuffer sff = new StringBuffer();
		sff.append("select a from ").append(CostLog.class.getSimpleName()).append(" a ")
		.append("where a.projectName='").append(projectName).append("' and status='0' order by a.endDate desc");
		List<Object> list = entityDao.createQueryLimit(sff.toString(), offset, limit);
		return list;
	}
	
	public void save(CostLog costLog){
		entityDao.save(costLog);
	}
	
	@Transactional
	public void update(CostLog costLog){
		entityDao.update(costLog);
	}
	
}
