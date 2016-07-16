package com.mvc.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mvc.dao.EntityDao;
import com.mvc.entity.Contract;
import com.mvc.entity.Property;
import com.mvc.entity.Report;

@Service("ReportServiceImpl")
public class ReportServiceImpl implements ReportService{
	@Autowired
	private EntityDao entityDao;
	
	@Transactional
	public List<Object> getReportByReportIdProjectIdAndProjectId(String reportId, Integer projectId){
		StringBuffer sff = new StringBuffer();
		sff.append("select a from ").append(Report.class.getSimpleName()).append(" a ")
		.append("where a.reportId='").append(reportId).append("' and projectId=").append(projectId);
		List<Object> list = entityDao.createQuery(sff.toString());
		return list;
	}
	
	@Transactional
	public List<Object> getReportsByProjectName(String projectName){
		StringBuffer sff = new StringBuffer();
		sff.append("select a from ").append(Report.class.getSimpleName()).append(" a ")
		.append("where a.projectName='").append(projectName).append("'");
		List<Object> list = entityDao.createQuery(sff.toString());
		return list;
	}
	
	@Transactional
	public List<Object> getReportsByProjectNameOrderByContract(String projectName){
		StringBuffer sff = new StringBuffer();
		sff.append("select a from ").append(Report.class.getSimpleName()).append(" a ")
		.append("where a.projectName='").append(projectName).append("' order by contractId");
		List<Object> list = entityDao.createQuery(sff.toString());
		return list;
	}
	
	@Transactional
	public List<Object> getReportsByUserName(String userName){
		StringBuffer sff = new StringBuffer();
		sff.append("select a from ").append(Report.class.getSimpleName()).append(" a ")
		.append("where a.userName='").append(userName).append("'");
		List<Object> list = entityDao.createQuery(sff.toString());
		return list;
	}
	
	@Transactional
	public Report getReportById(Integer id){
		return (Report) entityDao.findById(Report.class, id);
	}
	
	public void save(Report report){
		entityDao.save(report);
	}
	@Transactional
	public void delate(Report report){
		entityDao.delete(report);
	}
}
