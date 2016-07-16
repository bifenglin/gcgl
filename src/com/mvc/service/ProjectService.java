package com.mvc.service;

import java.util.List;

import com.mvc.entity.Project;
import com.mvc.entity.User;


public interface ProjectService {
	public List<Object> getProjectsOn(User user);
	public Project getProjectById(Integer id);
	public List<Object> getProjectsByUser(User user);
	public List<Object> getProjectsInProjectByUser(User user);
	public List<Object> getProjectsNotInProjectByUser(User user);
	public List<Object> getProjectsAndAuthorityByUser(User user);
	public List<Object> getUtpProjectsByUser(User user);
	public void saveProject(Project project);
	public List getProjectLikeByName(String name);
	public Project getProjectByName(String name);
	public Integer getAllProjectNumber();
	public List<Object> getCheckJoin(User user);
	public List<Object> getCheckAdmit(User user);
	public List<Object> getAllProjectsNameAndMap(List utds);
	public List<Object> getAllProjectsNameAndMapNotInProject(User user, List utds);
	public List<Object> getAllProjectsNameAndMapInProject(User user, List utds);
	public List<Object> getAllProjectsNameAndMapNotInPermission(User user, List utds);
	public void update(Project project);
}
