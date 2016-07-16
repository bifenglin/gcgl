package com.mvc.service;

import java.util.List;

import com.mvc.entity.Contract;

public interface ContractServic {
	public Contract getContractByContractIdAndProjectName(String contractId, String projectName);
	public Contract getContractByTypeAndContractNameAndProjectName(String type, String contractName, String projectName);
	public List<Object> getContractsByProjectId(Integer projectId);
	public List<Object> getContractsByProjectNameOrderByContract(String projectName);
	public List<Object> getContractsOnByProjectId(Integer projectId);
	public List<Object> getContractsOnByProjectName(String projectName);
	public void save(Contract contract);
	public void update(Contract contract);
	public void delete(Contract contract);
}
