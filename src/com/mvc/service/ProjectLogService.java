package com.mvc.service;

import java.util.List;

import com.mvc.entity.ProjectLog;

public interface ProjectLogService {
	public List<Object> getProjectLogByProjectNameAndDate(String projectName,String startDate, String endDate);
	public List<Object> getProjectLogByProjectNameAndUser(String projectName,String userName, Integer offset, Integer limit);
	public List<Object> getProjectLogByProjectName(String projectName, Integer offset, Integer limit);
	public void save(ProjectLog projectLog);
}
