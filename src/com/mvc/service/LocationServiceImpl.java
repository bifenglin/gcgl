package com.mvc.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mvc.common.CostTypeEnum;
import com.mvc.dao.EntityDao;
import com.mvc.entity.Location;
import com.mvc.entity.Project;
import com.mvc.entity.Property;
import com.mvc.entity.User;
import com.mvc.entity.Utp;

@Service("LocationServiceImpl")
public class LocationServiceImpl implements LocationService{
	@Autowired
	private EntityDao entityDao;
	
	@Transactional
	public List<Object> getLocationsByPropertyId(String propertyId, String projectName){
		StringBuffer sff = new StringBuffer();
		sff.append("select a from ").append(Location.class.getSimpleName()).append(" a ")
		.append("where a.projectName='").append(projectName).append("' and a.propertyId='").append(propertyId).append("'");
		List<Object> list = entityDao.createQuery(sff.toString());
		return list;
	}
	
	@Transactional
	public List<Object> getLocationsCostTypeDayByProject(String projectName){
		StringBuffer sff = new StringBuffer();
		sff.append("select a from ").append(Location.class.getSimpleName()).append(" a ,")
		.append(Property.class.getSimpleName()).append(" b ")
		.append("where a.projectName='").append(projectName).append("' and a.propertyId=b.propertyId and (b.costType='").append(CostTypeEnum.DAYREN.getValue()).append("' or b.costType='").append(CostTypeEnum.DAYTAI).append("')");
		List<Object> list = entityDao.createQuery(sff.toString());
		return list;
	}
	
	@Transactional
	public Location getLocationById(Integer id){
		return (Location) entityDao.findById(Location.class, id);
	}
	
	@Transactional
	public Location getLocationByLocationId(String locationId){
		StringBuffer sff = new StringBuffer();
		sff.append("select a from ").append(Location.class.getSimpleName()).append(" a ")
		.append("where a.locationId='").append(locationId).append("' ");
		List<Object> list = entityDao.createQuery(sff.toString());
		if (list.size()>0) {
			return (Location) list.get(0);
		} else {
			return null;
		}
	}
	@Transactional
	public Integer getMaxLocationId(){
		StringBuffer sff = new StringBuffer();
		sff.append("select max(a.id) from ").append(Location.class.getSimpleName()).append(" a");
		List<Object> list = entityDao.createQuery(sff.toString());
		if (list.size()>0) {
			return (Integer) list.get(0);
		} else {
			return null;
		}
	}
	
	public void save(Location location){
		entityDao.save(location);
	}
	
	@Transactional
	public void update(Location location){
		entityDao.update(location);
	}
	@Transactional
	public void delete(Location location){
		entityDao.delete(location);
	}
}
