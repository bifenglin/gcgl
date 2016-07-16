package com.mvc.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mvc.dao.EntityDao;
import com.mvc.entity.Department;
import com.mvc.entity.Student;
import com.mvc.entity.User;
import com.mvc.entity.Utd;
import com.mvc.entity.Utp;

@Service("UserServiceImpl")
public class UserServiceImpl implements UserService{
	@Autowired
	private EntityDao entityDao;
	
	@Autowired
	@Qualifier("UtpServiceImpl")
	private UtpService utpService;
	
	@Autowired
	@Qualifier("ProjectServiceImpl")
	private ProjectService projectService;
		
	@Transactional
	public List<Object> getUser(){
		StringBuffer sff = new StringBuffer();
		sff.append("select a from ").append(User.class.getSimpleName()).append(" a ");
		List<Object> list = entityDao.createQuery(sff.toString());
		return list;
	}
	
	@Transactional
	public Integer getAllUserNumber(){
		StringBuffer sff = new StringBuffer();
		sff.append("select count(a.id) from ").append(User.class.getSimpleName()).append(" a ")
		.append("where authority=2");
		List<Object> list = entityDao.createQuery(sff.toString());
		Integer count = Integer.valueOf(list.get(0).toString());
		return count;
	}
	
	@Transactional
	public Integer getAllAdminNumber(){
		StringBuffer sff = new StringBuffer();
		sff.append("select count(a.name) from ").append(User.class.getSimpleName()).append(" a ")
		.append("where a.authority='1'");
		List<Object> list = entityDao.createQuery(sff.toString());
		Integer count = Integer.valueOf(list.get(0).toString());
		return count;
	}
	
	public void save(User user){
		entityDao.save(user);
	}
	public void delete(Object obj){
		entityDao.delete(obj);
	}
	
	@Transactional
	public List<Object> getUser(User user){
		List<String> propertyNameList = new ArrayList<String>();
		List<String> propertyValueList = new ArrayList<String>();
		propertyNameList.add("name");
		propertyNameList.add("pwd");
		propertyValueList.add(user.getName());
		propertyValueList.add(user.getPwd());
		List<Object> list = entityDao.findByHQLCondition(User.class, propertyNameList, propertyValueList);
		return list;
	}
	@Transactional
	public User getUserByName(String name){
		List<String> propertyNameList = new ArrayList<String>();
		List<String> propertyValueList = new ArrayList<String>();
		propertyNameList.add("name");
		propertyValueList.add(name);
		List<Object> list = entityDao.findByHQLCondition(User.class, propertyNameList, propertyValueList);
		if (list.size()>0) {
			return (User) list.get(0);
		} else {
			return null;
		}
		
	}
	
	@Transactional
	public List<Object> getUserByDepartmentId(Integer departmentId){
		StringBuffer sff = new StringBuffer();
		sff.append("select a from ").append(User.class.getSimpleName()).append(" a,")
		.append(Utd.class.getSimpleName()).append(" b,")
		.append(Department.class.getSimpleName()).append(" c")
		.append(" where c.id=").append(departmentId).append(" and b.departmentName=c.name and b.userName=a.name");
		List<Object> list = entityDao.createQuery(sff.toString());
		return list;
	}
	
	@Transactional
	public List<Object> getUsersNotAdmin(){
		StringBuffer sff = new StringBuffer();
		sff.append("select a from ").append(User.class.getSimpleName()).append(" a ")
		.append("where a.authority!='1'");
		List<Object> list = entityDao.createQuery(sff.toString());
		return list;
	}
	
	@Transactional
	public List<Object> getUsersNotAdminAndNotInDepatment(String departmentName){
		StringBuffer sff = new StringBuffer();
		sff.append("select distinct a from ").append(User.class.getSimpleName()).append(" a ,")
		.append(Utd.class.getSimpleName()).append(" b ")
		.append("where a.authority!='1' and a.name not in(select b.userName from b where a.name=b.userName and b.departmentName='").append(departmentName).append("')");
		List<Object> list = entityDao.createQuery(sff.toString());
		return list;
	}
	
	@Transactional
	public List<Object> getAdmin(){
		StringBuffer sff = new StringBuffer();
		sff.append("select a from ").append(User.class.getSimpleName()).append(" a ")
		.append("where a.authority='1'");
		List<Object> list = entityDao.createQuery(sff.toString());
		return list;
	}
	
	@Transactional
	public User getUserById(Integer id){
		return (User) entityDao.findById(User.class, id);
	}
	
	@Transactional
	public void delete(User user){
		List list = utpService.getUtpByUser(user);
		for (int i = 0; i < list.size(); i++) {
			Utp utp = (Utp) list.get(i);
			if (utp.getPermission().equals("1")) {
				String projectName = utp.getProjectName();
				List projects = utpService.getUtpByProjectName(projectName);
				int flag = 0;
				for (int j = 0; j < projects.size(); j++) {
					Utp tempUtp = (Utp) projects.get(j);
					if (tempUtp.getPermission().equals("2")) {
						tempUtp.setPermission("1");
						utpService.updateUtp(tempUtp);
						flag = 1;
						break;
					}
				}
				if (flag == 0) {
					for (int j = 0; j < projects.size(); j++) {
						Utp tempUtp = (Utp) projects.get(j);
						if (tempUtp.getPermission().equals("3")) {
							tempUtp.setPermission("1");
							utpService.updateUtp(tempUtp);
							flag = 1;
							break;
						}
					}
				}
			}
			utpService.delete(utp);
		}
		entityDao.delete(user);
	}
	
	
	
	@Transactional
	public void update(User user){
		
		entityDao.update(user);
	}
}
