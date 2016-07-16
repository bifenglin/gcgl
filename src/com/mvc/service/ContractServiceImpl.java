package com.mvc.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mvc.dao.EntityDao;
import com.mvc.entity.Contract;
import com.mvc.entity.ContractStowage;
import com.mvc.entity.CostLog;
import com.mvc.entity.Report;

@Service("ContractServiceImpl")
public class ContractServiceImpl implements ContractServic{

	@Autowired
	EntityDao entityDao;
	
	@Autowired
	@Qualifier("ContractStowageServiceImpl")
	ContractStowageService contractStowageService;
	
	@Transactional
	public Contract getContractByContractIdAndProjectName(String contractId, String projectName){
		StringBuffer sff = new StringBuffer();
		sff.append("select a from ").append(Contract.class.getSimpleName()).append(" a ")
		.append("where a.projectName='").append(projectName).append("' and a.contractId='").append(contractId).append("'");
		List<Object> list = entityDao.createQuery(sff.toString());
		if (list.size()>0) {
			return (Contract) list.get(0);
		} else{
			return null;
		}
	}

	@Transactional
	public List<Object> getContractsByProjectId(Integer projectId){
		StringBuffer sff = new StringBuffer();
		sff.append("select a from ").append(Contract.class.getSimpleName()).append(" a ")
		.append("where a.projectId=").append(projectId).append("");
		List<Object> list = entityDao.createQuery(sff.toString());
		return list;
	}
	
	@Transactional
	public List<Object> getContractsByProjectNameOrderByContract(String projectName){
		StringBuffer sff = new StringBuffer();
		sff.append("select a from ").append(Contract.class.getSimpleName()).append(" a ")
		.append("where a.projectName='").append(projectName).append("' order by contractId");
		List<Object> list = entityDao.createQuery(sff.toString());
		return list;
	}
	
	
	@Transactional
	public List<Object> getContractsOnByProjectId(Integer projectId){
		StringBuffer sff = new StringBuffer();
		sff.append("select a from ").append(Contract.class.getSimpleName()).append(" a ")
		.append("where a.projectId=").append(projectId).append(" and a.status='1'");
		List<Object> list = entityDao.createQuery(sff.toString());
		return list;
	}
	
	@Transactional
	public List<Object> getContractsOnByProjectName(String projectName){
		StringBuffer sff = new StringBuffer();
		sff.append("select a from ").append(Contract.class.getSimpleName()).append(" a ")
		.append("where a.projectName='").append(projectName).append("' and a.status='1'");
		List<Object> list = entityDao.createQuery(sff.toString());
		return list;
	}
	
	public void save(Contract contract){
//		if (contractStowageService.getContractStowageByName(contract.getName()) != null) {
//			ContractStowage contractStowage = new ContractStowage();
//			contractStowage.setAllFund("0");
//			contractStowage.setAllManage("0");
//			contractStowage.setAllScore("0");
//			contractStowage.setAllTechnology("0");
//			contractStowage.setCompany(contract.getCompany());
//			contractStowage.setName(contract.getName());
//			contractStowage.setRemark(contract.getRemark());
//			contractStowageService.save(contractStowage);
//		}		
		entityDao.save(contract);
	}
	
	public Contract getContractByTypeAndContractNameAndProjectName(String type, String contractName, String projectName){
		StringBuffer sff = new StringBuffer();
		sff.append("select a from ").append(Contract.class.getSimpleName()).append(" a ")
		.append("where a.projectName='").append(projectName).append("' and a.type='").append(type).append("' and a.name='").append(contractName).append("'");
		List<Object> list = entityDao.createQuery(sff.toString());
		if (list.size()>0) {
			return (Contract) list.get(0);
		} else{
			return null;
		}
	}
	
	@Transactional
	public void update(Contract contract){
		entityDao.update(contract);
	}
	
	public void delete(Contract contract){
		entityDao.delete(contract);
	}
}
