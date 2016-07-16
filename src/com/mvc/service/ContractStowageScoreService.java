package com.mvc.service;

import java.util.List;

import com.mvc.entity.ContractStowageScore;

public interface ContractStowageScoreService {
	public void save(ContractStowageScore contractStowageScore);
	public List<Object> getContracStowageScoresByContractStowageIdLimit(String contractStowageId, Integer offset, Integer limit);
}
