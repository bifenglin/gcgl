package com.mvc.controller;

import java.beans.IntrospectionException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.websocket.Session;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openxmlformats.schemas.spreadsheetml.x2006.main.STSourceType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.mvc.common.Parameter;
import com.mvc.common.TypeEnum;
import com.mvc.entity.ContractStowage;
import com.mvc.entity.Department;
import com.mvc.entity.Project;
import com.mvc.entity.User;
import com.mvc.entity.Utd;
import com.mvc.entity.Utp;
import com.mvc.service.ContractStowageService;
import com.mvc.service.DepartmentService;
import com.mvc.service.ProjectService;
import com.mvc.service.UserService;
import com.mvc.service.UtdService;
import com.mvc.service.UtpService;
import com.mvc.util.ObjectUtil;

@Controller
@RequestMapping("/admin.do")
public class WebAdminController {
	protected final transient Log log = LogFactory.getLog(WebAdminController.class);
	
	@Autowired
	@Qualifier("UserServiceImpl")
	private UserService userService;
	
	@Autowired
	@Qualifier("ProjectServiceImpl")
	private ProjectService projectService;
	
	@Autowired
	@Qualifier("UtpServiceImpl")
	private UtpService utpService;
	
	@Autowired
	@Qualifier("DepartmentServiceImpl")
	private DepartmentService departmentService;
	
	@Autowired
	@Qualifier("UtdServiceImpl")
	private UtdService utdService;
	
	@Autowired
	@Qualifier("ContractStowageServiceImpl")
	private ContractStowageService contractStowageService;
	
	
	@RequestMapping
	public String load(ModelMap modelMap){
		modelMap.put("msg", "");
		System.out.println("load");
		return "login";
	}
	
	@RequestMapping(params = "method=goAdminIndex")
	public String goAdminIndex(ModelMap modelMap, HttpSession session){
		System.out.println("goAdminIndex");
		if (session.getAttribute("user") == null) {
			modelMap.put("msg", "请登陆");
			return "login";
		}
		return "adminIndex";
	}
	
	@RequestMapping(params = "method=login")
	public String login(@RequestParam("name") String name,
			@RequestParam("pwd") String pwd,
			HttpServletRequest request,
			HttpSession session,
			ModelMap modelMap){
		User user = new User();
		user.setName(name);
		user.setPwd(pwd);
		List list;
		if ((list = userService.getUser(user)).size()!=1) {
			modelMap.put("msg", "账号密码错误");
			return "login";
		} else {
			user = (User) list.get(0);
		}
		if (user.getAuthority().equals("1")) {
			session.setAttribute("user", user);
			Integer projectCount = projectService.getAllProjectNumber();
			Integer userCount = userService.getAllUserNumber();
			Integer adminCount = userService.getAllAdminNumber();
			List departments = departmentService.getAllDepartments();
			
			session.setAttribute("departments", departments);
			session.setAttribute("projectCount", projectCount);
			session.setAttribute("adminCount", adminCount);
			session.setAttribute("userCount", userCount);
			return "adminIndex";
		} else {
			int onProjectsNum = 0;
			int offProjectsNum = 0;
			user = (User) list.get(0);
			List projectList = projectService.getProjectsInProjectByUser(user);
			session.setAttribute("user", user);
			session.setAttribute("projects", projectList);
			List<Object> utpList = utpService.getUtpByUser(user);
			modelMap.put("allProjectsNum", utpList.size());
			for (int i = 0; i < projectList.size(); i++) {
				Project project = (Project) projectList.get(i);
				if (project.getStatus().equals("1")) {
					onProjectsNum++;
				} else {
					offProjectsNum++;
				}
			}
			modelMap.put("onProjectsNum", onProjectsNum);
			modelMap.put("offProjectsNum", offProjectsNum);
			
			return "userIndex";
		}
		
	}
	
	@RequestMapping(params = "method=checkAdmin")
	public String checkAdmin(ModelMap modelMap, HttpSession session, HttpServletRequest request){
		if (session.getAttribute("user") == null) {
			modelMap.put("msg", "请登陆");
			return "login";
		}
		List userList = userService.getAdmin();
		modelMap.put("userList", userList);
		return "adminUserTable";
	}
	
	@RequestMapping(params = "method=logOut")
	public String logOut(ModelMap modelMap, HttpSession session){
		System.out.println("logOut");
		session.setAttribute("user", null);
		session.setAttribute("projects", null);
		return "login";
	}
	
	@RequestMapping(params = "method=goAdminUserTable")
	public String goAdminUserTable(ModelMap modelMap, HttpSession session, HttpServletRequest request){
		String tempId = request.getParameter("departmentId");
		String action = request.getParameter("action");
		Integer departmentId = null;
		if (tempId != null) {
			session.setAttribute("departmentId", departmentId);
		}
//		else {
//			departmentId = Integer.valueOf(request.getParameter("id"));
//		}
		
		String id = request.getParameter("id");
		if (id == null) {
			if (action != null && action.equals("goUser")) {
				modelMap.put("departmentName", "所有用户");
				List list = userService.getUser();
				modelMap.put("userList", list);
				modelMap.put("msg", "");
				return "adminUserTable";
			}
			Department department = departmentService.getDepartmentById(departmentId);
			modelMap.put("departmentName", department.getName());
			List list = userService.getUserByDepartmentId(departmentId);
			modelMap.put("userList", list);
			modelMap.put("msg", "");
			return "adminUserTable";
		}
		if (id!=null && action.equals("update")) {
			String name = request.getParameter("name");
			User targetUser = userService.getUserById(Integer.valueOf(id));
			if (userService.getUserByName(name)!=null && !targetUser.getName().equals(name)) {
				modelMap.put("msg", "帐号名已经存在");
				modelMap.put("targetUser", targetUser);
				return "adminUpdateUser";
			}
			
			String pwd = request.getParameter("pwd");
			String truename = request.getParameter("truename");

			String permission = request.getParameter("permission");
			String remark = request.getParameter("remark");
			String phone = request.getParameter("phone");
			String authority = request.getParameter("authority");
			System.out.println(authority);
			if (remark == null) {
				remark = "";
			}
			if (phone == null) {
				phone = "";
			}
			targetUser.setAuthority(authority);
			targetUser.setDate(String.valueOf(new Date().getTime()));
			targetUser.setName(name);
			targetUser.setPhone(phone);
			targetUser.setPwd(pwd);
			targetUser.setRemark(remark);
			targetUser.setTruename(truename);
			userService.update(targetUser);
			
			modelMap.put("departmentName", "所有用户");
			List list = userService.getUser();
			modelMap.put("userList", list);
			modelMap.put("msg", "修改成功");
			return "adminUserTable";
		}
		
		if (id!=null && action.equals("deleteUser")) {
			User user = userService.getUserById(Integer.valueOf(id));
			if (utdService.getUtdByUser(user).size()>0) {
				List list = userService.getUser();
				modelMap.put("departmentName", "所有用户");
				modelMap.put("msg", "该用户在部门中，请先删除部门中的用户");
				modelMap.put("userList", list);
				return "adminUserTable";
			}
//			userService.delete(user);
			modelMap.put("departmentName", "所有用户");
			modelMap.put("msg", "删除成功");
			userService.delete(user);
			List list = userService.getUser();
			modelMap.put("userList", list);
			return "adminUserTable";
		}
		
		Department department = departmentService.getDepartmentById(departmentId);
		modelMap.put("departmentName", department.getName());
		User targetUser = userService.getUserById(Integer.valueOf(id));
		Utd utd = (utdService.getUtdByUserNameAndDepartmentName(targetUser.getName(), department.getName()));
		utdService.delete(utd);
		List list = userService.getUserByDepartmentId(departmentId);
		modelMap.put("msg", "删除成功");
		modelMap.put("userList", list);
		return "adminUserTable";
	}
	
	@RequestMapping(params = "method=goCheckDepartment")
	public String goCheckDepartment(ModelMap modelMap, HttpSession session, HttpServletRequest request) throws IntrospectionException, IllegalAccessException, InvocationTargetException{
		String action = request.getParameter("action");
		String departmentId = null;
		
		if (session.getAttribute("user") == null) {
			modelMap.put("msg", "登录超时，请重新登录");
			return "login";
		}
		
		
		if (action == null) {
			modelMap.put("msg", "");
			List departments = departmentService.getAllDepartments();
			List departmentList = new ArrayList();
			ObjectUtil objectUtil = new ObjectUtil();
			for (int i = 0; i < departments.size(); i++) {
				Department department = (Department) departments.get(i);
				Map departmentMap = objectUtil.convertBean(department);
				List users = userService.getUserByDepartmentId(department.getId());
				departmentMap.put("users", users);
				departmentList.add(departmentMap);
			}
			session.setAttribute("departments", departmentList);
			return "adminCheckDepartment";
		}
		if (action.equals("deleteDepartment")) {
			departmentId = request.getParameter("departmentId");
			Department department = departmentService.getDepartmentById(Integer.valueOf(departmentId));
			
			if (utdService.getUtdByDepartment(department).size()>0) {
				modelMap.put("msg", "请先清空部门里所有帐号");
				List departments = departmentService.getAllDepartments();
				List departmentList = new ArrayList();
				ObjectUtil objectUtil = new ObjectUtil();
				for (int i = 0; i < departments.size(); i++) {
					Department departmentTemp = (Department) departments.get(i);
					Map departmentMap = objectUtil.convertBean(departmentTemp);
					List users = userService.getUserByDepartmentId(departmentTemp.getId());
					departmentMap.put("users", users);
					departmentList.add(departmentMap);
				}
				session.setAttribute("departments", departmentList);
				return "adminCheckDepartment";
			} 
			departmentService.deleteDepartment(department);
			List departments = departmentService.getAllDepartments();
			List departmentList = new ArrayList();
			ObjectUtil objectUtil = new ObjectUtil();
			for (int i = 0; i < departments.size(); i++) {
				Department departmentTemp = (Department) departments.get(i);
				Map departmentMap = objectUtil.convertBean(departmentTemp);
				List users = userService.getUserByDepartmentId(departmentTemp.getId());
				departmentMap.put("users", users);
				departmentList.add(departmentMap);
			}
			session.setAttribute("departments", departmentList);
			modelMap.put("msg", "删除成功");
			return "adminCheckDepartment";
		} else  {
			String userId = request.getParameter("userId");
			departmentId = request.getParameter("departmentId");
			User user;
			Department department;
			Utd utd;
			if ((user = userService.getUserById(Integer.valueOf(userId))) !=null && (department = departmentService.getDepartmentById(Integer.valueOf(departmentId))) != null && (utd = utdService.getUtdByUserNameAndDepartmentName(user.getName(), department.getName())) != null) {
				utdService.delete(utd);
				List departments = departmentService.getAllDepartments();
				List departmentList = new ArrayList();
				ObjectUtil objectUtil = new ObjectUtil();
				for (int i = 0; i < departments.size(); i++) {
					Department departmentTemp = (Department) departments.get(i);
					Map departmentMap = objectUtil.convertBean(departmentTemp);
					List users = userService.getUserByDepartmentId(departmentTemp.getId());
					departmentMap.put("users", users);
					departmentList.add(departmentMap);
				}
				session.setAttribute("departments", departmentList);
				modelMap.put("msg", "删除成功");
				return "adminCheckDepartment";
			} else {
				List departments = departmentService.getAllDepartments();
				List departmentList = new ArrayList();
				ObjectUtil objectUtil = new ObjectUtil();
				for (int i = 0; i < departments.size(); i++) {
					Department departmentTemp = (Department) departments.get(i);
					Map departmentMap = objectUtil.convertBean(departmentTemp);
					List users = userService.getUserByDepartmentId(departmentTemp.getId());
					departmentMap.put("users", users);
					departmentList.add(departmentMap);
				}
				session.setAttribute("departments", departmentList);
				modelMap.put("msg", "删除失败，用户不存在");
				return "adminCheckDepartment";
			}
		} 
	}
	
	
	@RequestMapping(params = "method=goA")
	public String goAddUser(ModelMap modelMap, HttpSession session, HttpServletRequest request){
		
//		System.out.println("adminAddUser");
		List departments = departmentService.getAllDepartments();
		modelMap.put("departments", departments);
		String name = request.getParameter("name");
		
		String department = request.getParameter("department");
//		System.out.println(department);
		if (name == null) {
			modelMap.put("msg", "");
			return "adminAddUser";
		}
		
		if (department == null) {
			User user = new User();
			if (userService.getUserByName(name)!=null) {
				modelMap.put("msg", "帐号名已经存在");
				return "adminAddUser";
			}
			
			String pwd = request.getParameter("pwd");
			String truename = request.getParameter("truename");

			String permission = request.getParameter("permission");
			String remark = request.getParameter("remark");
			String phone = request.getParameter("phone");
			String authority = request.getParameter("authority");
			System.out.println(authority);
			if (remark == null) {
				remark = "";
			}
			if (phone == null) {
				phone = "";
			}
			user.setAuthority(authority);
			user.setDate(String.valueOf(new Date().getTime()));
			user.setName(name);
			user.setPhone(phone);
			user.setPwd(pwd);
			user.setRemark(remark);
			user.setTruename(truename);
			userService.save(user);
			
			modelMap.put("msg", "账户添加成功");
			return "adminAddUser";
		}
		
		if (utdService.getUtdByUserNameAndDepartmentName(name, department) != null) {
			modelMap.put("msg", "该用户已经在该部门");
			return "adminAddUser";
		}
		
		Utd utd = new Utd();
		utd.setDepartmentName(department);
		utd.setUserName(name);
		utdService.save(utd);
		modelMap.put("msg", "加入成功");
		return "adminAddUser";
	}
	
	
	@RequestMapping(params = "method=goAdminTable")
	public String goAdminTable(ModelMap modelMap, HttpSession session, HttpServletRequest request){
		String id = request.getParameter("id");
		if (id == null) {
			List list = userService.getAdmin();
			modelMap.put("userList", list);
			modelMap.put("departmentName", "管理员");
			modelMap.put("msg", "");
			return "adminUserTable";
		}
		
		User user = new User();
		user = userService.getUserById(Integer.valueOf(id));
		user.setAuthority("2");
		userService.update(user);;
		List list = userService.getAdmin();
		modelMap.put("userList", list);
		modelMap.put("departmentName", "管理员");
		modelMap.put("msg", "删除成功");
		
		return "adminUserTable";
	}
	
	@RequestMapping(params = "method=goAdminAddDepartment")
	public String goAdminAddDepartment(ModelMap modelMap, HttpSession session, HttpServletRequest request){
		String name = request.getParameter("name");
		if (name == null) {
			modelMap.put("msg", "");
			return "adminAddDepartment";
		}
		String remark = request.getParameter("remark");
		if (departmentService.getDepartmentByName(name).size()>0) {
			modelMap.put("msg", "部门名已经存在");
			return "adminAddDepartment";
		}
		Department department = new Department();
		department.setName(name);
		department.setRemark(remark);
		departmentService.saveDepartment(department);
		
		modelMap.put("msg", "添加"+name+"成功");
		return "adminAddDepartment";
	}
	
	@RequestMapping(params = "method=goAdminCheckContractStowage")
	public String goAdminCheckContractStowage(ModelMap modelMap, HttpSession session, HttpServletRequest request){
		String id = request.getParameter("id");
		Map countMap = new HashMap<String, Object>();
		Integer labourOn = contractStowageService.getContractStowagesCountByTypeAndStatus(TypeEnum.LABOUR.getKey(), "1");
		countMap.put("labourOn", labourOn);
		Integer labourOff = contractStowageService.getContractStowagesCountByTypeAndStatus(TypeEnum.LABOUR.getKey(), "0");
		countMap.put("labourOff", labourOff);
		Integer machineOn = contractStowageService.getContractStowagesCountByTypeAndStatus(TypeEnum.MACHINE.getKey(), "1");
		countMap.put("machineOn", machineOn);
		Integer machineOff = contractStowageService.getContractStowagesCountByTypeAndStatus(TypeEnum.MACHINE.getKey(), "0");
		countMap.put("machineOff", machineOff);
		Integer materialOn = contractStowageService.getContractStowagesCountByTypeAndStatus(TypeEnum.MATERIAL.getKey(), "1");
		countMap.put("materialOn", materialOn);
		Integer materialOff = contractStowageService.getContractStowagesCountByTypeAndStatus(TypeEnum.MATERIAL.getKey(), "0");
		countMap.put("materialOff", materialOff);
		Integer otherOn = contractStowageService.getContractStowagesCountByTypeAndStatus(TypeEnum.OTHER.getKey(), "1");
		countMap.put("otherOn", otherOn);
		Integer otherOff = contractStowageService.getContractStowagesCountByTypeAndStatus(TypeEnum.MACHINE.getKey(), "0");
		countMap.put("otherOff", otherOff);
		countMap.put("allOn", labourOn+machineOn+materialOn+otherOn);
		countMap.put("allOff", labourOff+machineOff+materialOff+otherOff);
		if (id == null) {
			modelMap.put("msg", "");
			List contractStowages = contractStowageService.getAllContractStowage();
			System.out.println(contractStowages.size());
			modelMap.put("contractStowages", contractStowages);
			modelMap.put("count", countMap);
			return "adminCheckContractStowage";
		}
		ContractStowage contractStowage;
		if ((contractStowage = contractStowageService.getContractStowageById(Integer.valueOf(id))) != null) {
			String type = request.getParameter("type");
			if (type.equals("join")) {
				contractStowage.setStatus("0");
				contractStowageService.update(contractStowage);
				modelMap.put("msg", "加入黑名单成功");
			} else {
				type.equals("delete");
				modelMap.put("msg", "删除成功");
				contractStowageService.delete(contractStowage);
			}
		}
		List contractStowages = contractStowageService.getAllContractStowage();
		modelMap.put("contractStowages", contractStowages);
		modelMap.put("count", countMap);
		return "adminCheckContractStowage";
	}
	
	@RequestMapping(params = "method=goAdminDepartmentAddUser")
	public String goAdminDepartmentAddUser(ModelMap modelMap, HttpSession session, HttpServletRequest request){
		String departmentId = request.getParameter("departmentId");
//		String action = request.getParameter("action");
		Department department = departmentService.getDepartmentById(Integer.valueOf(departmentId));
		modelMap.put("msg", "");
		System.out.println(department.getName());
		List userList = userService.getUsersNotAdminAndNotInDepatment(department.getName());
		modelMap.put("userList", userList);
		session.setAttribute("department", department);
		modelMap.put("departmentName", "所有用户");
		return "adminDepartmentAddUser";
	}
	
	@RequestMapping(params = "method=goAdminUpdateUser")
	public String goAdminUpdateUser(ModelMap modelMap, HttpSession session, HttpServletRequest request){
		String id = request.getParameter("id");
//		String action = request.getParameter("action");
		User targetUser = userService.getUserById(Integer.valueOf(id));
		if (targetUser == null) {
			modelMap.put("msg", "用户不存在");
			modelMap.put("departmentName", "所有用户");
			List list = userService.getUser();
			modelMap.put("userList", list);
			return "adminUserTable";
		}
		modelMap.put("targetUser", targetUser);
		modelMap.put("msg", "");
		return "adminUpdateUser";
	}
	
	@RequestMapping(params = "method=adminDepartmentAddUser")
	public String adminDepartmentAddUser(ModelMap modelMap, HttpSession session, HttpServletRequest request) throws IntrospectionException, IllegalAccessException, InvocationTargetException{
		String[] choices = request.getParameterValues("choice");
		Department department = (Department) session.getAttribute("department");
		for (int i = 0; i < choices.length; i++) {
			User user = userService.getUserById(Integer.valueOf(choices[i]));
			Utd utd = new Utd();
			utd.setDepartmentName(department.getName());
			utd.setUserName(user.getName());
			utdService.save(utd);
		}
		List departments = departmentService.getAllDepartments();
		List departmentList = new ArrayList();
		ObjectUtil objectUtil = new ObjectUtil();
		for (int i = 0; i < departments.size(); i++) {
			Department departmentTemp = (Department) departments.get(i);
			Map departmentMap = objectUtil.convertBean(departmentTemp);
			List users = userService.getUserByDepartmentId(departmentTemp.getId());
			departmentMap.put("users", users);
			departmentList.add(departmentMap);
		}
		session.setAttribute("departments", departmentList);
		modelMap.put("msg", "添加成功");
		return "adminCheckDepartment";
	}
	
}
