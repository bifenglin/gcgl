package com.mvc.service;

import java.util.List;

import com.mvc.entity.ContractStowage;

public interface ContractStowageService {
	public boolean isContractStowageOffByName(String name, String company);
	public List<Object> getAllContractStowageOn();
	public List<Object> getAllContractStowageOffByType(String type);
	public List<Object> getAllContractStowage();
	public List<Object> getContractStowagesOnScoreByScId(Integer scId, Integer offset, Integer limit);
	public List<Object> getContractStowagesOnBy(String scoreType, Integer offset, Integer limit);
	public List<Object> getContractStowagesOnByAndType(String scoreType, String type, Integer offset, Integer limit);
	public List<Object> getContractStowagesOffByAndType(String scoreType, String type, Integer offset, Integer limit);
	public ContractStowage getContractStowageOnByNameAndType(String name,String type);
	public ContractStowage getContractStowageOnByName(String name);
	public ContractStowage getContractStowageByNameAndType(String name, String type);
	public List<Object> getContractStowageLikeByKeyAndStatus(String key, String status);
	public List<Object> getContractStowageLikeByKeyAndStatusAndType(String key, String status, String type);
	public ContractStowage getContractStowageById(Integer id);
	public Integer getContractStowagesCountByTypeAndStatus(String type, String status);
	public ContractStowage save(ContractStowage contractStowage);
	public void update(ContractStowage contractStowage);
	public void delete(ContractStowage contractStowage);
}
