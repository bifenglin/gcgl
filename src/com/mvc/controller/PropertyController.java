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
import com.mvc.common.CostTypeEnum;
import com.mvc.common.Parameter;
import com.mvc.common.PermissionEnum;
import com.mvc.common.TypeEnum;
import com.mvc.entity.Contract;
import com.mvc.entity.CostLog;
import com.mvc.entity.Location;
import com.mvc.entity.Project;
import com.mvc.entity.ProjectLog;
import com.mvc.entity.Property;
import com.mvc.entity.Report;
import com.mvc.entity.User;
import com.mvc.service.ContractServic;
import com.mvc.service.CostLogService;
import com.mvc.service.ProjectLogService;
import com.mvc.service.ProjectService;
import com.mvc.service.PropertyService;
import com.mvc.util.ObjectUtil;

@Controller
@RequestMapping("/property.do")
public class PropertyController {
	protected final transient Log log = LogFactory
	.getLog(StudentController.class);
	
	@Autowired
	@Qualifier("ProjectServiceImpl")
	private ProjectService projectService;
	
	@Autowired
	@Qualifier("CostLogServiceImpl")
	private CostLogService costLogService;
	
	@Autowired
	@Qualifier("PropertyServiceImpl")
	private PropertyService propertyService;
	
	@Autowired
	@Qualifier("ProjectLogServiceImpl")
	private ProjectLogService projectLogService;
	
	@Autowired
	@Qualifier("ContractServiceImpl")
	private ContractServic contractServic;
	
	@RequestMapping(params = "method=save")
	public void saveProperty(
			@RequestParam("user") String userJson,
			@RequestParam("property") String propertyJson, 
			@RequestParam("projectName") String projectName,
			@RequestParam("permission") String permission,
			HttpServletResponse response, 
			HttpServletRequest request) throws IOException, IntrospectionException, IllegalAccessException, InvocationTargetException{
		
		Map<String, Object> map = new HashMap<String, Object>();
		Gson gson = new Gson();
		response.setContentType("text/html;charset=utf-8");
		response.setCharacterEncoding("utf-8");
		Map<String, Object> data = new HashMap<String, Object>();	
		
		Property property = new Property();
		User user = new User();
		try {
			user = gson.fromJson(userJson, User.class);
			property = gson.fromJson(propertyJson, Property.class);
		} catch (Exception e) {
			// TODO: handle exception
			map.put(Parameter.MSG, "参数错误");
			map.put(Parameter.CODE, Parameter.FALSE);
			response.getWriter().print(gson.toJson(map));
			return;
		}
		
		if (!permission.equals(PermissionEnum.ADMIN.getValue()) &&  !permission.equals(PermissionEnum.CREATER.getValue())) {
			map.put(Parameter.MSG, "权限不足");
			map.put(Parameter.CODE, Parameter.FALSE);
			response.getWriter().print(gson.toJson(map));
			return;
		}
		
		String propertyId;
		StringBuffer sff = new StringBuffer();
		List<Object> list = propertyService.getPropertysByContractIdAndProjectName(property.getContractId(), projectName);
		if (list.size() == 0 || list == null) {
			propertyId = property.getContractId()+"X1";
		} else {
			Property tempProperty = (Property) list.get(list.size()-1);
			Integer index = tempProperty.getPropertyId().indexOf("X")+1;
			String tempPropertyId = tempProperty.getPropertyId().substring(index);
			Integer trueId = Integer.valueOf(tempPropertyId)+1;
			sff.append(tempProperty.getPropertyId().substring(0, index)).append(String.valueOf(trueId));
			propertyId = sff.toString();
		}
		property.setProjectName(projectName);
		property.setContractId(property.getContractId());
		property.setStatus("1");
		property.setPropertyId(propertyId);
		property.setDate(String.valueOf(new Date().getTime()));
		Contract contract = contractServic.getContractByContractIdAndProjectName(property.getContractId(), projectName);

		
		ProjectLog projectLog = new ProjectLog();
		projectLog.setCause("saveProperty");
		projectLog.setContractId(contract.getContractId());
		projectLog.setDate(String.valueOf(new Date().getTime()));
		projectLog.setUserName(user.getName());
		projectLog.setProjectName(contract.getProjectName());
		StringBuffer sb = new StringBuffer();
		sb.append(user.getTruename()).append(":").append("添加了").append(contract.getContractId()).append("编号").append(contract.getName()).append("合同的")
		.append(propertyId).append("编号").append(property.getName()).append("合同项。");
		projectLog.setLogString(sb.toString());
		projectLogService.save(projectLog);

		propertyService.save(property);
		List costLogs = costLogService.getTodayCostLogByProjectAndDate(projectName);
		
		data.put("costLogs", costLogs);
		data.put("property", property);
		map.put(Parameter.DATA, data);
		map.put(Parameter.MSG, "添加成功");
		map.put(Parameter.CODE, Parameter.SUCCESS);
		response.getWriter().print(gson.toJson(map));
	}
	
	@RequestMapping(params = "method=update")
	public void updateProperty(
			@RequestParam("user") String userJson,
			@RequestParam("property") String propertyJson, 
			@RequestParam("projectName") String projectName,
			@RequestParam("permission") String permission,
			HttpServletResponse response, 
			HttpServletRequest request) throws IOException, IntrospectionException, IllegalAccessException, InvocationTargetException{
		
		Map<String, Object> map = new HashMap<String, Object>();
		Gson gson = new Gson();
		response.setContentType("text/html;charset=utf-8");
		response.setCharacterEncoding("utf-8");
		Map<String, Object> data = new HashMap<String, Object>();	
		
		Property property = new Property();
		User user = new User();
		try {
			user = gson.fromJson(userJson, User.class);
			property = gson.fromJson(propertyJson, Property.class);
		} catch (Exception e) {
			// TODO: handle exception
			map.put(Parameter.MSG, "参数错误");
			map.put(Parameter.CODE, Parameter.FALSE);
			response.getWriter().print(gson.toJson(map));
			return;
		}
		
		if (!permission.equals(PermissionEnum.ADMIN.getValue()) && !permission.equals(PermissionEnum.CREATER.getValue())) {
			map.put(Parameter.MSG, "权限不足");
			map.put(Parameter.CODE, Parameter.FALSE);
			response.getWriter().print(gson.toJson(map));
			return;
		}
		
		Contract contract = contractServic.getContractByContractIdAndProjectName(property.getContractId(), projectName);
		Property tempProperty = propertyService.getPropertyByPropertyId(projectName, property.getPropertyId());
		if ( tempProperty == null) {
			map.put(Parameter.MSG, "合同项不存在");
			map.put(Parameter.CODE, Parameter.FALSE);
			response.getWriter().print(gson.toJson(map));
			return;
		} else {
			tempProperty.setPrice(property.getPrice());
			tempProperty.setCostType(property.getCostType());
			tempProperty.setName(property.getName());
			tempProperty.setRemark(property.getRemark());
			tempProperty.setTotal(property.getTotal());
			tempProperty.setType(property.getType());
		}
		
		ProjectLog projectLog = new ProjectLog();
		projectLog.setCause("updateProperty");
		projectLog.setContractId(contract.getContractId());
		projectLog.setDate(String.valueOf(new Date().getTime()));
		projectLog.setUserName(user.getName());
		projectLog.setProjectName(contract.getProjectName());
		StringBuffer sb = new StringBuffer();
		sb.append(user.getTruename()).append(":").append("修改了").append(contract.getContractId()).append("编号").append(contract.getName()).append("合同的")
		.append(tempProperty.getPropertyId()).append("编号").append(tempProperty.getName()).append("合同项。");
		projectLog.setLogString(sb.toString());
		projectLogService.save(projectLog);
		
		propertyService.update(tempProperty);
		
		map.put(Parameter.DATA, property);
		map.put(Parameter.MSG, "更改成功");
		map.put(Parameter.CODE, Parameter.SUCCESS);
		response.getWriter().print(gson.toJson(map));
	}
	
	
	
	
	@RequestMapping(params = "method=delete")
	public void deleteProperty(
			@RequestParam("user") String userJson,
			@RequestParam("property") String propertyJson, 
			@RequestParam("projectName") String projectName,
			@RequestParam("permission") String permission,
			HttpServletResponse response, 
			HttpServletRequest request) throws IOException, IntrospectionException, IllegalAccessException, InvocationTargetException{

		Map<String, Object> map = new HashMap<String, Object>();
		Gson gson = new Gson();
		response.setContentType("text/html;charset=utf-8");
		response.setCharacterEncoding("utf-8");
		Map<String, Object> data = new HashMap<String, Object>();	
		
		Property property = new Property();
		User user = new User();
		try {
			user = gson.fromJson(userJson, User.class);
			property = gson.fromJson(propertyJson, Property.class);
		} catch (Exception e) {
			// TODO: handle exception
			map.put(Parameter.MSG, "参数错误");
			map.put(Parameter.CODE, Parameter.FALSE);
			response.getWriter().print(gson.toJson(map));
			return;
		}
		
		if (!permission.equals(PermissionEnum.ADMIN.getValue()) &&  !permission.equals(PermissionEnum.CREATER.getValue())) {
			map.put(Parameter.MSG, "权限不足");
			map.put(Parameter.CODE, Parameter.FALSE);
			response.getWriter().print(gson.toJson(map));
			return;
		}
		
		Contract contract = contractServic.getContractByContractIdAndProjectName(property.getContractId(), projectName);
		Property tempProperty = propertyService.getPropertyByPropertyId(projectName, property.getPropertyId());
		if (tempProperty == null) {
			map.put(Parameter.MSG, "合同项不存在");
			map.put(Parameter.CODE, Parameter.FALSE);
			response.getWriter().print(gson.toJson(map));
			return;
		}
		
		tempProperty.setStatus("0");
		propertyService.update(tempProperty);
		
		ProjectLog projectLog = new ProjectLog();
		projectLog.setCause("saveProperty");
		projectLog.setContractId(contract.getContractId());
		projectLog.setDate(String.valueOf(new Date().getTime()));
		projectLog.setUserName(user.getName());
		projectLog.setProjectName(contract.getProjectName());
		StringBuffer sb = new StringBuffer();
		sb.append(user.getTruename()).append(":").append("删除了").append(contract.getContractId()).append("编号").append(contract.getName()).append("合同的")
		.append(property.getPropertyId()).append("编号").append(property.getName()).append("合同项。");
		projectLog.setLogString(sb.toString());
		projectLogService.save(projectLog);
		
		
		map.put(Parameter.MSG, "删除成功");
		map.put(Parameter.CODE, Parameter.SUCCESS);
		response.getWriter().print(gson.toJson(map));
	}
	
}
