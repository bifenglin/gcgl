package com.mvc.service;

import java.util.List;

import com.mvc.entity.Announcement;

public interface AnnouncementService {
	public List<Object> getAnnouncementsByProjectName(String projectName, Integer offset, Integer limit);
	public Announcement getAnnouncementById(Integer id);
	public Integer getMaxAnnouncementIdByProjectName(String projectName);
	public Long getAnnouncementNumberByProjectName(String projectName);
	public void save(Announcement announcement);
	public void delete(Announcement announcement);
}
