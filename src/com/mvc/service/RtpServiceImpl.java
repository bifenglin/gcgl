package com.mvc.service;

import java.util.List;

import org.apache.poi.hwpf.model.ListTables;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mvc.dao.EntityDao;
import com.mvc.entity.Report;
import com.mvc.entity.Rtp;


@Service("RtpServiceImpl")
public class RtpServiceImpl implements RtpService{
	@Autowired
	private EntityDao entityDao;
	
	@Transactional
	public List<Object> getRtpsByContractIdAndProjectName(String contractId, String projectName){
		StringBuffer sff = new StringBuffer();
		sff.append("select a from ").append(Rtp.class.getSimpleName()).append(" a ")
		.append("where a.projectName='").append(projectName).append("' and contractId='").append(contractId).append("'");
		List<Object> list = entityDao.createQuery(sff.toString());
		return list;
	}
	
	@Transactional
	public List<Object> getRtpsByProjectNameAndReportId(String projectName, String reportId){
		StringBuffer sff = new StringBuffer();
		sff.append("select a from ").append(Rtp.class.getSimpleName()).append(" a ")
		.append("where a.projectName='").append(projectName).append("' and reportId='").append(reportId).append("'");
		List<Object> list = entityDao.createQuery(sff.toString());
		return list;
	}
	
	@Transactional
	public List<Object> getRtpsByProjectNameAndPropertyId(String projectName, String propertyId){
		StringBuffer sff = new StringBuffer();
		sff.append("select a from ").append(Rtp.class.getSimpleName()).append(" a ")
		.append("where a.projectName='").append(projectName).append("' and propertyId='").append(propertyId).append("'");
		List<Object> list = entityDao.createQuery(sff.toString());
		return list;
	}
	
	@Transactional
	public Rtp getTopDateRtpByProjectNameAndPropertyId(String projectName, String propertyId){
		StringBuffer sff = new StringBuffer();
		sff.append("select a from ").append(Rtp.class.getSimpleName()).append(" a ")
		.append("where a.projectName='").append(projectName).append("' and a.propertyId='").append(propertyId)
		.append("' order by a.endDate desc");
		List<Object> list = entityDao.createQuery(sff.toString());
		if (list.size()>0) {
			return (Rtp) list.get(0);
		} else {
			return null;
		}
	}
//	
//	@Transactional
//	public List<Object> getRtpsByProjectNameAndReportId(String projectName, String reportId){
//		StringBuffer sff = new StringBuffer();
//		sff.append("select a from ").append(Rtp.class.getSimpleName()).append(" a ")
//		.append("where a.projectName='").append(projectName).append("' and a.reportId='").append(reportId)
//		.append("' order by a.endDate desc");
//		List<Object> list = entityDao.createQuery(sff.toString());
//		return list;
//	}
	
	
	
	public void save(Rtp rtp){
		entityDao.save(rtp);
	}
	
	@Transactional
	public void delete(Rtp rtp){
		entityDao.delete(rtp);
	}
}
