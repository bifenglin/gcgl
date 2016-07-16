package com.mvc.service;

import java.util.List;

import com.mvc.entity.Property;

public interface PropertyService {
	public List<Object> getPropertysByContractIdAndProjectName(String contractId, String projectName);
	public List<Object> getPropertysOnByContractIdAndProjectName(String contractId, String projectName);
	public Property getPropertyByPropertyId(String projectName, String propertyId);
	public void save(Property property);
	public void update(Property property);
}
