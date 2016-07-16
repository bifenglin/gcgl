package com.mvc.controller;

import java.beans.IntrospectionException;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Date;
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
import com.mvc.entity.Announcement;
import com.mvc.entity.Contract;
import com.mvc.entity.User;
import com.mvc.service.AnnouncementService;
import com.mvc.service.UtpService;

@Controller
@RequestMapping("/announcement.do")
public class AnnouncementController {
	protected final transient Log log = LogFactory
	.getLog(AnnouncementController.class);
	
	@Autowired
	@Qualifier("UtpServiceImpl")
	private UtpService utpService;
	
	@Autowired
	@Qualifier("AnnouncementServiceImpl")
	private AnnouncementService announcementService;
	
	@RequestMapping(params = "method=save")
	public void save(
			@RequestParam("message") String message,
			@RequestParam("user") String userJson,
			@RequestParam("projectName") String projectName,
			HttpServletResponse response, 
			HttpServletRequest request) throws IOException, IntrospectionException, IllegalAccessException, InvocationTargetException{
		
		Map<String, Object> map = new HashMap<String, Object>();
		Gson gson = new Gson();
		response.setContentType("text/html;charset=utf-8");
		response.setCharacterEncoding("utf-8");
		Map<String, Object> data = new HashMap<String, Object>();	
		
		Announcement announcement = new Announcement();
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
		if (utpService.getUtpByUserNameAndProjectName(user.getName(), projectName) == null) {
			map.put(Parameter.MSG, "权限不足");
			map.put(Parameter.CODE, Parameter.FALSE);
			response.getWriter().print(gson.toJson(map));
			return;
		}
		if (message.length() >= 250) {
			map.put(Parameter.MSG, "超过字数限制");
			map.put(Parameter.CODE, Parameter.FALSE);
			response.getWriter().print(gson.toJson(map));
			return;
		}
		announcement.setProjectName(projectName);
		announcement.setUserTruename(user.getTruename());
		announcement.setMessage(message);
		announcement.setUserName(user.getName());
		announcement.setId(announcementService.getMaxAnnouncementIdByProjectName(projectName)+1);
		announcement.setDate(String.valueOf(new Date().getTime()));
		announcementService.save(announcement);
		
		data.put("announcement", announcement);
		map.put(Parameter.MSG, "发布成功");
		map.put(Parameter.CODE, Parameter.SUCCESS);
		map.put(Parameter.DATA, data);
		response.getWriter().print(gson.toJson(map));
	}
	
	@RequestMapping(params = "method=delete")
	public void delete(
			@RequestParam("announcementId") Integer announcementId,
			@RequestParam("user") String userJson,
			@RequestParam("projectName") String projectName,
			HttpServletResponse response, 
			HttpServletRequest request) throws IOException, IntrospectionException, IllegalAccessException, InvocationTargetException{
		
		Map<String, Object> map = new HashMap<String, Object>();
		Gson gson = new Gson();
		response.setContentType("text/html;charset=utf-8");
		response.setCharacterEncoding("utf-8");
		Map<String, Object> data = new HashMap<String, Object>();	
		
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
		Announcement announcement = announcementService.getAnnouncementById(announcementId);
		if (announcement == null) {
			map.put(Parameter.MSG, "该公告已删除");
			map.put(Parameter.CODE, Parameter.FALSE);
			response.getWriter().print(gson.toJson(map));
			return;
		}
		if (!announcement.getUserName().equals(user.getName())) {
			map.put(Parameter.MSG, "只能删除自己的公告");
			map.put(Parameter.CODE, Parameter.FALSE);
			response.getWriter().print(gson.toJson(map));
			return;
		}
		announcementService.delete(announcement);
		map.put(Parameter.MSG, "删除成功");
		map.put(Parameter.CODE, Parameter.SUCCESS);
		response.getWriter().print(gson.toJson(map));
	}
	
	@RequestMapping(params = "method=get")
	public void get(
			@RequestParam("offset") Integer offset,
			@RequestParam("limit") Integer limit,
			@RequestParam("user") String userJson,
			@RequestParam("projectName") String projectName,
			HttpServletResponse response, 
			HttpServletRequest request) throws IOException, IntrospectionException, IllegalAccessException, InvocationTargetException{
		
		Map<String, Object> map = new HashMap<String, Object>();
		Gson gson = new Gson();
		response.setContentType("text/html;charset=utf-8");
		response.setCharacterEncoding("utf-8");
		Map<String, Object> data = new HashMap<String, Object>();	
		
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
		List<Object> announcements = announcementService.getAnnouncementsByProjectName(projectName, offset, limit);
//		List<Object> list = new ArrayList<Object>();
//		for (int i = 0; i < announcements.size(); i++) {
//			Announcement announcement = (Announcement) announcements.get(i);
//			
//		}
		Long announcementNumber = announcementService.getAnnouncementNumberByProjectName(projectName);
		data.put("announcements", announcements);
		data.put("announcementNumber", announcementNumber);
		map.put(Parameter.MSG, "发布成功");
		map.put(Parameter.DATA, data);
		map.put(Parameter.CODE, Parameter.SUCCESS);
		response.getWriter().print(gson.toJson(map));
	}
}
