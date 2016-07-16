package com.mvc.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mvc.dao.EntityDao;
import com.mvc.entity.Location;
import com.mvc.entity.ProjectLog;
import com.mvc.entity.User;
import com.mvc.entity.Utp;
import com.mvc.entity.Valuation;
import com.mvc.entity.ValuationReport;


@Service("ValuationServiceImpl")
public class ValuationServiceImpl implements ValuationService{
	@Autowired
	private EntityDao entityDao;
	

	@Transactional
	public Valuation getValuationByValuationId(String valuationId){
		StringBuffer sff = new StringBuffer();
		sff.append("select a from ").append(Valuation.class.getSimpleName()).append(" a ")
		.append("where a.valuationId='").append(valuationId).append("' order by a.date desc");
		List<Object> list = entityDao.createQuery(sff.toString());
		if (list.size()>0) {
			return (Valuation) list.get(0);
		} else {
			return null;
		}
	}
	
	
	@Transactional
	public List<Object> getValuationByProjectNameLimit(String projectName, Integer offset, Integer limit){
		StringBuffer sff = new StringBuffer();
		sff.append("select a from ").append(Valuation.class.getSimpleName()).append(" a ")
		.append("where a.projectName='").append(projectName).append("' order by a.date desc");
		List<Object> list = entityDao.createQueryLimit(sff.toString(), offset, limit);
		return list;
	}
	
	@Transactional
	public Valuation getMaxValuationByProjectName(String projectName){
		StringBuffer sff = new StringBuffer();
		sff.append("select a from ").append(Valuation.class.getSimpleName()).append(" a ")
		.append("where a.id=(select max(a.id) from a where a.projectName='").append(projectName).append("')");
		List<Object> list = entityDao.createQuery(sff.toString());
		if (list.size()>0) {
			return (Valuation) list.get(0);
		}
		else {
			return null;
		}
	}
	
	
	@Transactional
	public List<Object> getValuationByProjectNameAndDate(String projectName, String startDate, String endDate){
		StringBuffer sff = new StringBuffer();
		sff.append("select a from ").append(Valuation.class.getSimpleName()).append(" a ")
		.append("where a.projectName='").append(projectName).append("' and a.date between '").append(startDate).append("' and '").append(endDate).append("' order by a.date desc");
		List<Object> list = entityDao.createQuery(sff.toString());
		return list;
	}
	
	@Transactional
	public Integer getMaxValuationId(){
		StringBuffer sff = new StringBuffer();
		sff.append("select max(a.id) from ").append(Valuation.class.getSimpleName()).append(" a");
		List<Object> list = entityDao.createQuery(sff.toString());
		if (list.size()>0) {
			return (Integer) list.get(0);
		} else {
			return null;
		}
	}
	
	@Transactional
	public List<Object> getValuationUnitByProjectName(String projectName){
		StringBuffer sff = new StringBuffer();
		sff.append("select distinct b.unit from ").append(Valuation.class.getSimpleName()).append(" a,")
		.append(ValuationReport.class.getSimpleName()).append(" b ")
		.append("where a.projectName='").append(projectName).append("' and a.valuationId=b.valuationId");
		List<Object> list = entityDao.createQuery(sff.toString());
		return list;
	}
	
	public void save(Valuation valuation) {
		entityDao.save(valuation);
	}
	@Transactional
	public void update(Valuation valuation) {
		entityDao.update(valuation);
	}
}
