package com.mvc.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mvc.dao.EntityDao;
import com.mvc.entity.ContractStowageScore;
import com.mvc.entity.Project;
import com.mvc.entity.ProjectLog;
import com.mvc.entity.User;
import com.mvc.entity.Utp;

@Service("ProjectLogServiceImpl")
public class ProjectLogServiceImpl implements ProjectLogService{
	@Autowired
	private EntityDao entityDao;
	
	@Transactional
	public List<Object> getProjectLogByProjectName(String projectName, Integer offset, Integer limit){
		StringBuffer sff = new StringBuffer();
		sff.append("select a from ").append(ProjectLog.class.getSimpleName()).append(" a ")
		.append("where a.projectName='").append(projectName).append("' order by a.date desc");
		List<Object> list = entityDao.createQueryLimit(sff.toString(), offset, limit);
		return list;
	}
	
	@Transactional
	public List<Object> getProjectLogByProjectNameAndUser(String projectName,String userName, Integer offset, Integer limit){
		StringBuffer sff = new StringBuffer();
		sff.append("select a from ").append(ProjectLog.class.getSimpleName()).append(" a ")
		.append("where a.projectName='").append(projectName).append("' and a.userName='").append(userName).append("' order by a.date desc");
		List<Object> list = entityDao.createQueryLimit(sff.toString(), offset, limit);
		return list;
	}
	
	@Transactional
	public List<Object> getProjectLogByProjectNameAndDate(String projectName,String startDate, String endDate){
		StringBuffer sff = new StringBuffer();
		sff.append("select a from ").append(ProjectLog.class.getSimpleName()).append(" a ")
		.append("where a.projectName='").append(projectName).append("' and a.date between '").append(startDate).append("' and '").append(endDate).append("'");
		List<Object> list = entityDao.createQuery(sff.toString());
		return list;
	}
	
	public void save(ProjectLog projectLog){
		entityDao.save(projectLog);
	}
	
}
