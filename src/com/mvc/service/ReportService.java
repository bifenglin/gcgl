package com.mvc.service;

import java.util.List;

import com.mvc.entity.Report;

public interface ReportService {
	public void delate(Report report);
	public List<Object> getReportByReportIdProjectIdAndProjectId(String reportId, Integer projectId);
	public List<Object> getReportsByProjectName(String projectName);
	public void save(Report report);
	public Report getReportById(Integer id);
	public List<Object> getReportsByUserName(String userName);
	public List<Object> getReportsByProjectNameOrderByContract(String projectName);
}
