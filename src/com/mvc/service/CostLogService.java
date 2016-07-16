package com.mvc.service;

import java.util.List;
import java.util.Map;

import com.mvc.entity.CostLog;

public interface CostLogService {
	public CostLog getCostLogById(Integer id);
	public CostLog getCostLogOnByLocationId(Integer locationId);
	public List getCostLogByProjectName(String projectName);
	public List getCostLogByPropertyId(String propertyId, String projectName);
	public List getCostLogByContractId(String ContractId, String projectName);
	public List getCostLogByContractIdAndDate(String contractId, String projectName, String startDate, String endDate);
	public List getCostLogByPropertyIdAndDate(String projectName, String propertyId, String startDate, String endDate);
	public List getTodayCostLogByProjectAndDate(String projectName);
	public List getTodayCostLogByProjectAndDate(String projectName, String startDate, String endDate);
	public List<Object> getCostLogByProjectName(String projectName, Integer offset, Integer limit);
	public void save(CostLog costLog);
	public void update(CostLog costLog);
	//	public List getTodayStartCostLog(String projectName);
//	public List getTodayEndCostLog(String projectName);
//	public List getTodayStartAndTodayEndCostLog(String projectName);
//	public Map getTodayCost(String projectName);
}
