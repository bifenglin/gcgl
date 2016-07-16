package com.mvc.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mvc.dao.EntityDao;
import com.mvc.entity.Project;
import com.mvc.entity.User;
import com.mvc.entity.Utd;
import com.mvc.entity.Utp;

@Service("ProjectServiceImpl")
public class ProjectServiceImpl implements ProjectService{
	@Autowired
	private EntityDao entityDao;
	
	@Transactional
	public List<Object> getProjectsByUser(User user){
		StringBuffer sff = new StringBuffer();
		sff.append("select c from ").append(User.class.getSimpleName()).append(" a ")
		.append(",").append(Utp.class.getSimpleName()).append(" b ")
		.append(",").append(Project.class.getSimpleName()).append(" c ")
		.append("where b.userName='").append(user.getName()).append("' and c.name=b.projectName");
		List<Object> list = entityDao.createQuery(sff.toString());
		return list;
	}
	

	@Transactional
	public List<Object> getProjectsOn(User user){
		StringBuffer sff = new StringBuffer();
		sff.append("select distinct a from ").append(Project.class.getSimpleName()).append(" a ")
		.append(",").append(Utp.class.getSimpleName()).append(" b ")
		.append("where a.name=b.projectName and a.name not in (select b.projectName from b where b.userName='").append(user.getName()).append("') and a.status='1'");
		List<Object> list = entityDao.createQuery(sff.toString());
		return list;
	}
	
	@Transactional
	public List<Object> getAllProjectsNameAndMap(){
		StringBuffer sff = new StringBuffer();
		sff.append("select new map(a.name as name,a.center as center) from ").append(Project.class.getSimpleName()).append(" a ")
		.append("where 1=1 and a.status='1'");
		List<Object> list = entityDao.createQuery(sff.toString());
		return list;
	}
	
	@Transactional
	public List<Object> getAllProjectsNameAndMap(List utds){
		StringBuffer sff = new StringBuffer();
		sff.append("select new map(a.name as name,a.center as center) from ").append(Project.class.getSimpleName()).append(" a ,")
		.append(Utd.class.getSimpleName()).append(" b ")
		.append("where a.name=b.userName ");
		if (utds.size()>0) {
			Utd utd= (Utd) utds.get(0);
			sff.append("and (departmentName='").append(utd.getDepartmentName()).append("'");
			for (int i = 0; i < utds.size()-1; i++) {
				utd= (Utd) utds.get(i+1);
				sff.append("or b.departmentName='").append(utd.getDepartmentName()).append("'");
			}
			utd= (Utd) utds.get(utds.size());
			sff.append("or departmentName='").append(utd.getDepartmentName()).append("') and a.status='1' ");
		}
		
		List<Object> list = entityDao.createQuery(sff.toString());
		return list;
	}
	
	@Transactional
	public List<Object> getAllProjectsNameAndMapInProject(User user,List utds){
		StringBuffer sff = new StringBuffer();
		sff.append("select distinct new map(a.name as name,a.center as center) from ")
		.append(Project.class.getSimpleName()).append(" a ,")
		.append(Utp.class.getSimpleName()).append(" b ,")
		.append(User.class.getSimpleName()).append(" c ,")
		.append(Utd.class.getSimpleName()).append(" d ")
		.append("where c.name='").append(user.getName()).append("' and c.name=b.userName and c.name=d.userName and b.permission != '0' and b.projectName=a.name and a.status='1'");
		if (utds.size()==1) {
			Utd utd= (Utd) utds.get(0);
			sff.append("and d.departmentName='").append(utd.getDepartmentName()).append("'");
		} else if (utds.size()>1) {
			Utd utd= (Utd) utds.get(0);
			sff.append("and (d.departmentName='").append(utd.getDepartmentName()).append("'");
			for (int i = 1; i < utds.size(); i++) {
				utd= (Utd) utds.get(i);
				if (i!=(utds.size() - 1)) {
					sff.append("or d.departmentName='").append(utd.getDepartmentName()).append("'");
				} else {
					sff.append("or d.departmentName='").append(utd.getDepartmentName()).append("') ");
				}
			}
			
		}
		List<Object> list = entityDao.createQuery(sff.toString());
		return list;
	}
	
	@Transactional
	public List<Object> getAllProjectsNameAndMapNotInProject(User user, List utds){
		StringBuffer sff = new StringBuffer();
		sff.append("select distinct new map(a.name as name,a.center as center) from ")
		.append(Project.class.getSimpleName()).append(" a ,")
		.append(Utp.class.getSimpleName()).append(" b ,")
		.append(User.class.getSimpleName()).append(" c ,")
		.append(Utd.class.getSimpleName()).append(" d ")
		.append("where c.name='").append(user.getName()).append("' and c.name=b.userName and c.name=d.userName and b.permission = '0' and b.projectName=a.name and a.status='1'");
		if (utds.size()==1) {
			Utd utd= (Utd) utds.get(0);
			sff.append("and d.departmentName='").append(utd.getDepartmentName()).append("'");
		} else if (utds.size()>1) {
			Utd utd= (Utd) utds.get(0);
			sff.append("and (d.departmentName='").append(utd.getDepartmentName()).append("'");
			for (int i = 1; i < utds.size(); i++) {
				utd= (Utd) utds.get(i);
				if (i!=(utds.size() - 1)) {
					sff.append("or d.departmentName='").append(utd.getDepartmentName()).append("'");
				} else {
					sff.append("or d.departmentName='").append(utd.getDepartmentName()).append("') ");
				}
			}
		}
		List<Object> list = entityDao.createQuery(sff.toString());
		return list;
	}
	
	
	@Transactional
	public List<Object> getAllProjectsNameAndMapNotInPermission(User user, List utds){
		StringBuffer sff = new StringBuffer();
		sff.append("select distinct new map(a.name as name,a.center as center) from ")
		.append(Project.class.getSimpleName()).append(" a ,")
		.append(Utp.class.getSimpleName()).append(" b ,")
		.append(User.class.getSimpleName()).append(" c ")
		.append("where c.name=b.userName and b.projectName= a.name and a.status='1' and a.name not in (select a.name from a ,b , c ")
		.append("where c.name='").append(user.getName()).append("' and c.name=b.userName and b.projectName= a.name)");
		if (utds.size()==1) {
			Utd utd= (Utd) utds.get(0);
			sff.append("and a.departmentName='").append(utd.getDepartmentName()).append("'");
		} else if (utds.size()>1) {
			Utd utd= (Utd) utds.get(0);
			sff.append("and (a.departmentName='").append(utd.getDepartmentName()).append("'");
			for (int i = 1; i < utds.size(); i++) {
				utd= (Utd) utds.get(i);
				if (i!=(utds.size() - 1)) {
					sff.append("or a.departmentName='").append(utd.getDepartmentName()).append("'");
				} else {
					sff.append("or a.departmentName='").append(utd.getDepartmentName()).append("') ");
				}
			}
		}
		List<Object> list = entityDao.createQuery(sff.toString());
		return list;
	}
	
	@Transactional
	public List<Object> getProjectsInProjectByUser(User user){
		StringBuffer sff = new StringBuffer();
		sff.append("select c from ").append(User.class.getSimpleName()).append(" a ")
		.append(",").append(Utp.class.getSimpleName()).append(" b ")
		.append(",").append(Project.class.getSimpleName()).append(" c ")
		.append("where a.name='").append(user.getName()).append("' and a.name=b.userName and c.name=b.projectName and (b.permission='1' or b.permission='2' or b.permission='3' or permission='4') ");
		List<Object> list = entityDao.createQuery(sff.toString());
		return list;
	}
	
	@Transactional
	public List<Object> getProjectsNotInProjectByUser(User user){
		StringBuffer sff = new StringBuffer();
		sff.append("select c from ").append(User.class.getSimpleName()).append(" a ")
		.append(",").append(Utp.class.getSimpleName()).append(" b ")
		.append(",").append(Project.class.getSimpleName()).append(" c ")
		.append("where a.name='").append(user.getName()).append("' and a.name=b.userName and c.name=b.projectName and b.permission='0'");
		List<Object> list = entityDao.createQuery(sff.toString());
		return list;
	}
	
	public Project getProjectById(Integer id) {
		Project project = (Project) entityDao.findById(Project.class, id);
		return project;
	}
	
	@Transactional
	public List<Object> getUtpProjectsByUser(User user){
		StringBuffer sff = new StringBuffer();
		sff.append("select new map(c as project, b as utp) from ").append(User.class.getSimpleName()).append(" a ")
		.append(",").append(Utp.class.getSimpleName()).append(" b ")
		.append(",").append(Project.class.getSimpleName()).append(" c ")
		.append("where a.name='").append(user.getName()).append("' and a.name=b.userName and c.name=b.projectName");
		List<Object> list = entityDao.createQuery(sff.toString());
		return list;
	}
	@Transactional
	public List<Object> getProjectsAndAuthorityByUser(User user){
		StringBuffer sff = new StringBuffer();
		sff.append("select  new map(projectName as b.name,permission as b.permission) from ").append(User.class.getSimpleName()).append(" a ")
		.append(",").append(Utp.class.getSimpleName()).append(" b ")
		.append(",").append(Project.class.getSimpleName()).append(" c ")
		.append("where a.name=b.userName and c.name=b.projectName");
		List<Object> list = entityDao.createQuery(sff.toString());
		return list;
	}
	
	@Transactional
	public List<Object> getCheckJoin(User user){
		StringBuffer sff = new StringBuffer();
		sff.append("select new map(c.name as projectName,a.name as userName, a.truename as userTruename) from ").append(User.class.getSimpleName()).append(" a ")
		.append(",").append(Utp.class.getSimpleName()).append(" b ")
		.append(",").append(Project.class.getSimpleName()).append(" c ")
		.append("where b.permission='0' and c.name=b.projectName and b.userName = a.name and b.projectName in (select distinct c.name from ").append(User.class.getSimpleName()).append(" a ")
		.append(",").append(Utp.class.getSimpleName()).append(" b ")
		.append(",").append(Project.class.getSimpleName()).append(" c ")
		.append(" where b.userName='").append(user.getName()).append("' and (b.permission='1' or permission='2') and a.name=b.userName and b.projectName=c.name)");
//		.append(" and c.name=b.userName and b.projectName = c.name");
		List<Object> list = entityDao.createQuery(sff.toString());
		return list;
	}
	
	@Transactional
	public List<Object> getCheckAdmit(User user){
		StringBuffer sff = new StringBuffer();
		sff.append("select new map(c.name as projectName,a.name as userName, a.truename as userTruename) from ").append(User.class.getSimpleName()).append(" a ")
		.append(",").append(Utp.class.getSimpleName()).append(" b ")
		.append(",").append(Project.class.getSimpleName()).append(" c ")
		.append(" where a.name='").append(user.getName()).append("' and b.isRead='0' and a.name=b.userName and b.projectName=c.name");
		List<Object> list = entityDao.createQuery(sff.toString());
		return list;
	}

	
	@Transactional
	public Project getProjectByName(String name){
		StringBuffer sff = new StringBuffer();
		List propertyNameList = new ArrayList();
		propertyNameList.add("name");
		List propertyValueList = new ArrayList();
		propertyValueList.add(name);
		List<Object> list = entityDao.findByHQLCondition(Project.class, propertyNameList, propertyValueList);
		if (list.size()>0) {
			return (Project) list.get(0);
		} else {
			return null;
		}
		
	}
	
	
	@Transactional
	public List getProjectLikeByName(String name){
		StringBuffer sff = new StringBuffer();
		sff.append("select  a from ").append(Project.class.getSimpleName()).append(" a ")
		.append(" where name like '%" + name + "%'");
		List<Object> list = entityDao.createQuery(sff.toString());
		return list;
	}

	@Transactional
	public Integer getAllProjectNumber(){
		StringBuffer sff = new StringBuffer();
		sff.append("select  count(a.id) from ").append(Project.class.getSimpleName()).append(" a ");
		List<Object> list = entityDao.createQuery(sff.toString());
		Integer count = Integer.valueOf(list.get(0).toString());
		return count;
	}
	public void saveProject(Project project){
		entityDao.save(project);
	}
	@Transactional
	public void update(Project project){
		entityDao.update(project);
	}
}
