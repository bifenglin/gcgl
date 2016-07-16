package com.mvc.service;

import java.util.List;

import com.mvc.entity.ValuationReport;

public interface ValuationReportService {
	public List<Object> getValuationReportByValuationIdAndName(String valuationId,String name);
	public ValuationReport getValuationReportById(Integer id);
	public List<Object> getValuationReportByValuationId(String valuationId);
	public List<Object> getValuationReportByProjectAndUnitsAndDate(String projectName, String[] units, String startDate, String endDate);
	public void save(ValuationReport valuationReport);
	public void update(ValuationReport valuationReport);
	public List<Object> getValuationReportByLastValuationAndProjectName(String projectName);
	public void delete(ValuationReport valuationReport);
}
