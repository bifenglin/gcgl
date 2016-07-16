package com.mvc.service;

import java.util.List;

import com.mvc.entity.Valuation;

public interface ValuationService {
	public Valuation getValuationByValuationId(String valuationId);
	public List<Object> getValuationByProjectNameLimit(String projectName, Integer offset, Integer limit);
	public List<Object> getValuationByProjectNameAndDate(String projectName, String startDate, String endDate);
	public Valuation getMaxValuationByProjectName(String projectName);
	public List<Object> getValuationUnitByProjectName(String projectName);
	public Integer getMaxValuationId();
	public void save(Valuation valuation);
	public void update(Valuation valuation);
}
