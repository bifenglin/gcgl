package com.mvc.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mvc.dao.EntityDao;
import com.mvc.entity.ProjectCost;
import com.mvc.entity.ProjectLog;
import com.mvc.entity.Valuation;

@Service("ProjectCostServiceImpl")
public class ProjectCostServiceImpl implements ProjectCostService{
	@Autowired
	private EntityDao entityDao;
	
	@Transactional
	public List<Object> getProjectCostsByProjectName(String projectName, Integer offset, Integer limit){
		StringBuffer sff = new StringBuffer();
		sff.append("select a from ").append(ProjectCost.class.getSimpleName()).append(" a ")
		.append("where a.projectName='").append(projectName).append("' order by a.date desc");
		List<Object> list = entityDao.createQueryLimit(sff.toString(), offset, limit);
		return list;
	}
	
	@Transactional
	public List<Object> getProjectCostsByProjectNameAndDate(String projectName, String startDate, String endDate){
		StringBuffer sff = new StringBuffer();
		sff.append("select a from ").append(ProjectCost.class.getSimpleName()).append(" a ")
		.append("where a.projectName='").append(projectName).append("' and a.date between '").append(startDate).append("' and '").append(endDate).append("' order by a.date desc");
		List<Object> list = entityDao.createQuery(sff.toString());
		return list;
	}
	
	
	public void save(ProjectCost projectCost){
		entityDao.save(projectCost);
	}
	
	public void update(ProjectCost projectCost){
		entityDao.update(projectCost);;
	}
	
}
