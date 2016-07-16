package com.mvc.controller;

import java.beans.IntrospectionException;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.google.gson.Gson;
import com.mvc.common.Parameter;
import com.mvc.service.ProjectLogService;
import com.mvc.service.ProjectService;

@Controller
@RequestMapping("/projectlog.do")
public class ProjectLogController {
	protected final transient Log log = LogFactory
	.getLog(StudentController.class);
	
	@Autowired
	@Qualifier("ProjectLogServiceImpl")
	private ProjectLogService projectLogService;

	@RequestMapping(params = "method=get")
	public void getProjectLog(
			@RequestParam("projectName") String projectName,
			@RequestParam("offset") Integer offset,
			@RequestParam("limit") Integer limit,
			HttpServletResponse response, 
			HttpServletRequest request) throws IOException, IntrospectionException, IllegalAccessException, InvocationTargetException{
		
		Map<String, Object> map = new HashMap<String, Object>();
		Gson gson = new Gson();
		response.setContentType("text/html;charset=utf-8");
		response.setCharacterEncoding("utf-8");
		Map<String, Object> data = new HashMap<String, Object>();	
		List list =  projectLogService.getProjectLogByProjectName(projectName, offset, limit);
		map.put(Parameter.MSG, "查询成功");
		map.put(Parameter.CODE, Parameter.SUCCESS);
		data.put("list", list);
		map.put(Parameter.DATA, data);
		response.getWriter().print(gson.toJson(map));

	}
	
	@RequestMapping(params = "method=getbyuser")
	public void getProjectLogByUser(
			@RequestParam("projectName") String projectName,
			@RequestParam("userName") String userName,
			@RequestParam("offset") Integer offset,
			@RequestParam("limit") Integer limit,
			HttpServletResponse response, 
			HttpServletRequest request) throws IOException, IntrospectionException, IllegalAccessException, InvocationTargetException{
		
		Map<String, Object> map = new HashMap<String, Object>();
		Gson gson = new Gson();
		response.setContentType("text/html;charset=utf-8");
		response.setCharacterEncoding("utf-8");
		Map<String, Object> data = new HashMap<String, Object>();	
		List list =  projectLogService.getProjectLogByProjectNameAndUser(projectName,userName, offset, limit);
		map.put(Parameter.MSG, "查询成功");
		map.put(Parameter.CODE, Parameter.SUCCESS);
		data.put("list", list);
		map.put(Parameter.DATA, data);
		response.getWriter().print(gson.toJson(map));

	}
}
