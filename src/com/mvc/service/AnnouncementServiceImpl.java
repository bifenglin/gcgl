package com.mvc.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mvc.dao.EntityDao;
import com.mvc.entity.Announcement;
import com.mvc.entity.Contract;
import com.mvc.entity.ProjectLog;

@Service("AnnouncementServiceImpl")
public class AnnouncementServiceImpl implements AnnouncementService{
	@Autowired
	EntityDao entityDao;
	
	@Transactional
	public List<Object> getAnnouncementsByProjectName(String projectName, Integer offset, Integer limit){
		StringBuffer sff = new StringBuffer();
		sff.append("select a from ").append(Announcement.class.getSimpleName()).append(" a ")
		.append("where a.projectName='").append(projectName).append("' order by date desc");
		List<Object> list = entityDao.createQueryLimit(sff.toString(), offset, limit);
		return list;
	}
	
	@Transactional
	public Integer getMaxAnnouncementIdByProjectName(String projectName){
		StringBuffer sff = new StringBuffer();
		sff.append("select max(a.id) from ").append(Announcement.class.getSimpleName()).append(" a ")
		.append("where a.projectName='").append(projectName).append("'");
		List<Object> list = entityDao.createQuery(sff.toString());
		Integer id = (Integer) list.get(0);
		if (id != null ) {
			return id;
		} else {
			return 1;
		}
	}
	
	@Transactional
	public Long getAnnouncementNumberByProjectName(String projectName){
		StringBuffer sff = new StringBuffer();
		sff.append("select count(a) from ").append(Announcement.class.getSimpleName()).append(" a ")
		.append("where a.projectName='").append(projectName).append("'");
		List<Object> list = entityDao.createQuery(sff.toString());
		Long id = (Long) list.get(0);
		if (id != null ) {
			return id;
		} else {
			return (long) 0;
		}
	}
	
	public Announcement getAnnouncementById(Integer id) {
		return (Announcement) entityDao.findById(Announcement.class, id);
	}
	
	public void save(Announcement announcement){
		entityDao.save(announcement);
	}
	
	@Transactional
	public void delete(Announcement announcement){
		entityDao.delete(announcement);
	}
	
}
