package com.mvc.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mvc.dao.EntityDao;
import com.mvc.entity.ContractStowageScore;
import com.mvc.entity.CostLog;

@Service("ContractStowageScoreServiceImpl")
public class ContractStowageScoreServiceImple implements ContractStowageScoreService{
	@Autowired
	EntityDao entityDao;
	
	public void save(ContractStowageScore contractStowageScore){
		entityDao.save(contractStowageScore);
	}
	
	@Transactional
	public List<Object> getContracStowageScoresByContractStowageIdLimit(String contractStowageId, Integer offset, Integer limit){
		StringBuffer sff = new StringBuffer();
		sff.append("select a from ").append(ContractStowageScore.class.getSimpleName()).append(" a ")
		.append("where a.contractStowageId='").append(contractStowageId).append("' order by a.date desc");
		List<Object> list = entityDao.createQueryLimit(sff.toString(), offset, limit);
		return list;
	}
	
	
}
