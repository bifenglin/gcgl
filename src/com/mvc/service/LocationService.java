package com.mvc.service;

import java.util.List;

import com.mvc.entity.Location;

public interface LocationService {
	public Location getLocationByLocationId(String locationId);
	public Integer getMaxLocationId();
	public void update(Location location);
	public void save(Location location);
	public void delete(Location location);
	public List<Object> getLocationsByPropertyId(String propertyId, String projectName);
	public List<Object> getLocationsCostTypeDayByProject(String projectName);
	public Location getLocationById(Integer id);
}
