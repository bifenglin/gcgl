package com.mvc.controller;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.mvc.common.Parameter;
import com.mvc.entity.Project;
import com.mvc.entity.Student;
import com.mvc.entity.User;
import com.mvc.entity.Utd;
import com.mvc.entity.Utp;
import com.mvc.service.ProjectService;
import com.mvc.service.StudentService;
import com.mvc.service.UserService;
import com.mvc.service.UserServiceImpl;
import com.mvc.service.UtdService;
import com.mvc.service.UtpService;

@Controller
@RequestMapping("/user.do")
public class UserController {
	protected final transient Log log = LogFactory.getLog(UserController.class);
	
	@Autowired
	@Qualifier("UserServiceImpl")
	private UserService userService;
	
	@Autowired
	@Qualifier("UtpServiceImpl")
	private UtpService utpService;
	
	@Autowired
	@Qualifier("ProjectServiceImpl")
	private ProjectService projectService;
	
	@Autowired
	@Qualifier("UtdServiceImpl")
	private UtdService utdService;
	
	public UserController() {
		// TODO Auto-generated constructor stub
	}
			
	@RequestMapping(params = "method=login")
	public void login(@RequestParam("user") String userJson, 
			HttpServletResponse response, 
			HttpServletRequest request) throws IOException{
		
		Map<String, Object> map = new HashMap<String, Object>();
		Map<String, Object> data = new HashMap<String, Object>();	
		Gson gson = new Gson();
		response.setContentType("text/html;charset=utf-8");
		response.setCharacterEncoding("utf-8");
		
//		String json = (String)request.getParameter("params");
//		System.out.println("json:"+json);
		
		User user = new User();
		try {
			user = gson.fromJson(userJson, User.class);
		} catch (Exception e) {
			// TODO: handle exception
			map.put(Parameter.MSG, "参数错误");
			map.put(Parameter.CODE, Parameter.FALSE);
			response.getWriter().print(gson.toJson(map));
			return;
		}
		System.out.println("login:"+user.getName()+user.getPwd());
		
		
		if (userService.getUser(user).size() == 1) {
			user = (User)userService.getUser(user).get(0);
//			List<Object> list = projectService.getUtpProjectsByUser(user);
//			List<Object> projects = projectService.getAllProjectsNameAndMap();
			List utds = utdService.getUtdByUser(user);
			for (int i = 0; i < utds.size(); i++) {
				Utd utd = (Utd) utds.get(0);
				if (utd.getDepartmentName().equals("上级部门")) {
					List<Object> notInPermission = projectService.getProjectsOn(user);
					List<Object> notInProjects = projectService.getProjectsNotInProjectByUser(user);
					List<Object> inProjects = projectService.getProjectsInProjectByUser(user);
					List<Object> checkJoinList = projectService.getCheckJoin(user);
					List<Object> checkReadList = projectService.getCheckAdmit(user);

					data.put("checkReadList", checkReadList);
					data.put("user", userService.getUser(user).get(0));
//					data.put("list", list);
					data.put("inProjects", inProjects);
					data.put("notInProjects", notInProjects);
					data.put("notInPermission", notInPermission);
					data.put("checkJoinList", checkJoinList);
					data.put("utds", utds);
					map.put(Parameter.DATA, data);
					map.put(Parameter.MSG, "登陆成功");
					map.put(Parameter.CODE, Parameter.SUCCESS);
//					map.put("user", userService.getUser().get(0));
					response.getWriter().print(gson.toJson(map));
					return;
				}
			}
			List<Object> inProjects = projectService.getAllProjectsNameAndMapInProject(user,utds);
			List<Object> notInProjects = projectService.getAllProjectsNameAndMapNotInProject(user, utds);
			List<Object> notInPermission = projectService.getAllProjectsNameAndMapNotInPermission(user, utds);
			List<Object> checkJoinList = projectService.getCheckJoin(user);
			List<Object> checkReadList = projectService.getCheckAdmit(user);
			
			data.put("user", userService.getUser(user).get(0));
//			data.put("list", list);
			data.put("inProjects", inProjects);
			data.put("notInProjects", notInProjects);
			data.put("notInPermission", notInPermission);
			data.put("checkJoinList", checkJoinList);
			data.put("checkReadList", checkReadList);
			data.put("utds", utds);
			map.put(Parameter.DATA, data);
			map.put(Parameter.MSG, "登录成功");
			map.put(Parameter.CODE, Parameter.SUCCESS);
//			map.put("user", userService.getUser().get(0));
			response.getWriter().print(gson.toJson(map));
		} else {
			map.put(Parameter.MSG, "登录失败，用户帐号或密码不正确");
			map.put(Parameter.CODE, Parameter.FALSE);
			response.getWriter().print(gson.toJson(map));
		}
		
	}
			
	@RequestMapping(params = "method=changePwd")
	public void changePwd(@RequestParam("pwd")String mapJson, 
			HttpServletResponse response, 
			HttpServletRequest request) throws IOException{
		
		Map<String, Object> map = new HashMap<String, Object>();
		Gson gson = new Gson();
		response.setContentType("text/html;charset=utf-8");
		response.setCharacterEncoding("utf-8");
		Map<String, Object> data = new HashMap<String, Object>();	
		User oldUser = new User();
		User user = new User();
		Map reqMap;
		List userList;
		try {
			reqMap = gson.fromJson(mapJson, new TypeToken<Map<String, Object>>() {}.getType());  
			user.setName(reqMap.get("name").toString());
			user.setPwd(reqMap.get("pwd").toString());
		} catch (Exception e) {
			map.put(Parameter.MSG, "参数错误");
			map.put(Parameter.CODE, Parameter.FALSE);
			response.getWriter().print(gson.toJson(map));
			return;
		}
		if ((userList = userService.getUser(user)).size() >0) {
			oldUser = (User) userList.get(0);
			oldUser.setPwd(reqMap.get("newPwd").toString());
			userService.update(oldUser);
			map.put(Parameter.MSG, "更新成功");
			map.put(Parameter.CODE, Parameter.SUCCESS);
			map.put(Parameter.DATA, data);
			response.getWriter().print(gson.toJson(map));
		} else {
			map.put(Parameter.MSG, "用户密码错误");
			map.put(Parameter.CODE, Parameter.FALSE);
			response.getWriter().print(gson.toJson(map));
		}
	}
}
