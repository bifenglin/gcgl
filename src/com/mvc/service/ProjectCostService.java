package com.mvc.service;

import java.util.List;

import com.mvc.entity.ProjectCost;

public interface ProjectCostService {
	public void save(ProjectCost projectCost);
	public void update(ProjectCost projectCost);
	public List<Object> getProjectCostsByProjectName(String projectName, Integer offset, Integer limit);
	public List<Object> getProjectCostsByProjectNameAndDate(String projectName, String startDate, String endDate);
}
