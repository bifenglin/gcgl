package com.mvc.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mvc.dao.EntityDao;
import com.mvc.entity.ProjectLog;
import com.mvc.entity.Valuation;
import com.mvc.entity.ValuationReport;

@Service("ValuationReportServiceImpl")
public class ValuationReportServiceImpl implements ValuationReportService{
	@Autowired
	private EntityDao entityDao;
	
	@Transactional
	public List<Object> getValuationReportByValuationId(String valuationId){
		StringBuffer sff = new StringBuffer();
		sff.append("select a from ").append(ValuationReport.class.getSimpleName()).append(" a ")
		.append("where a.valuationId='").append(valuationId).append("' order by a.id asc");
		List<Object> list = entityDao.createQuery(sff.toString());
		return list;
	}
	
	@Transactional
	public List<Object> getValuationReportByValuationIdAndName(String valuationId,String name){
		StringBuffer sff = new StringBuffer();
		sff.append("select a from ").append(ValuationReport.class.getSimpleName()).append(" a ")
		.append("where a.valuationId='").append(valuationId).append("' and a.name='").append(name).append("'");
		List<Object> list = entityDao.createQuery(sff.toString());
		return list;
	}
	
	@Transactional
	public ValuationReport getValuationReportById(Integer id){
		return (ValuationReport) entityDao.findById(ValuationReport.class, id);
	}
	
	
	@Transactional
	public List<Object> getValuationReportByLastValuationAndProjectName(String projectName){
		StringBuffer sff = new StringBuffer();
		sff.append("select distinct a from ").append(ValuationReport.class.getSimpleName()).append(" a ")
		.append("where a.valuationId=(select b.valuationId from ").append(Valuation.class.getSimpleName())
		.append(" b where b.id=(select max(b.id) from b where b.projectName='")
		.append(projectName).append("'))");
		List<Object> list = entityDao.createQuery(sff.toString());
		return list;
	}
	
	@Transactional
	public List<Object> getValuationReportByProjectAndUnitsAndDate(String projectName, String[] units, String startDate, String endDate){
		StringBuffer sff = new StringBuffer();
		sff.append("select distinct a from ").append(ValuationReport.class.getSimpleName()).append(" a,")
		.append(Valuation.class.getSimpleName()).append(" b ")
		.append("where b.projectName='").append(projectName).append("' and b.valuationId=a.valuationId and (");
		for (int i = 0; i < units.length; i++) {
			if (units[i] == null || units[i].equals("null")) {
				sff.append("a.unit is null");
			} else {
				sff.append("a.unit ='").append(units[i]).append("'");
			}
			if (i == units.length - 1) {
				sff.append(") and ");
			} else {
				sff.append(" or ");
			}
		}
		sff.append("a.totalDate between '").append(startDate).append("' and '").append(endDate).append("'");
		List<Object> list = entityDao.createQuery(sff.toString());
		return list;
	}
	
	
	
	public void save(ValuationReport valuationReport) {
		entityDao.save(valuationReport);
	}
	
	@Transactional
	public void update(ValuationReport valuationReport) {
		entityDao.update(valuationReport);
	}
	
	@Transactional
	public void delete(ValuationReport valuationReport) {
		entityDao.delete(valuationReport);
	}
}
