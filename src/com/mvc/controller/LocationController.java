package com.mvc.controller;

import java.beans.IntrospectionException;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.text.DecimalFormat;
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
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.google.gson.Gson;
import com.mvc.common.CostTypeEnum;
import com.mvc.common.Parameter;
import com.mvc.common.TypeEnum;
import com.mvc.entity.Contract;
import com.mvc.entity.CostLog;
import com.mvc.entity.Location;
import com.mvc.entity.Project;
import com.mvc.entity.ProjectLog;
import com.mvc.entity.Property;
import com.mvc.entity.User;
import com.mvc.service.ContractServic;
import com.mvc.service.ContractStowageService;
import com.mvc.service.CostLogService;
import com.mvc.service.LocationService;
import com.mvc.service.ProjectLogService;
import com.mvc.service.ProjectService;
import com.mvc.service.PropertyService;
import com.mvc.util.ObjectUtil;

@Controller
@RequestMapping("/location.do")
public class LocationController {
	protected final transient Log log = LogFactory
	.getLog(StudentController.class);
	
	@Autowired
	@Qualifier("ProjectServiceImpl")
	private ProjectService projectService;
	
	@Autowired
	@Qualifier("ProjectLogServiceImpl")
	private ProjectLogService projectLogService;
	
	@Autowired
	@Qualifier("PropertyServiceImpl")
	private PropertyService propertyService;
	
	@Autowired
	@Qualifier("ContractServiceImpl")
	private ContractServic contractServic;
	
	@Autowired
	@Qualifier("LocationServiceImpl")
	private LocationService locationService;
	
	@Autowired
	@Qualifier("ContractStowageServiceImpl")
	private ContractStowageService contractStowageService;
	
	@Autowired
	@Qualifier("CostLogServiceImpl")
	private CostLogService costLogService;
	
	@Transactional
	@RequestMapping(params = "method=save")
	public void saveLocation(
			@RequestParam("user") String userJson,
			@RequestParam("permission") String permission, 
			@RequestParam("location") String locationJson, 
			@RequestParam("point") String point, 
			@RequestParam("contractId") String contractId, 
			@RequestParam("projectName") String projectName,
			HttpServletResponse response, 
			HttpServletRequest request) throws IOException, IntrospectionException, IllegalAccessException, InvocationTargetException{
		
		Map<String, Object> map = new HashMap<String, Object>();
		Gson gson = new Gson();
		response.setContentType("text/html;charset=utf-8");
		response.setCharacterEncoding("utf-8");
		Map<String, Object> data = new HashMap<String, Object>();	
		
		
		Contract contract = contractServic.getContractByContractIdAndProjectName(contractId, projectName);
		Location location = new Location();
		User user = new User();
		try {
			location = gson.fromJson(locationJson, Location.class);
			user = gson.fromJson(userJson, User.class);
		} catch (Exception e) {
			// TODO: handle exception
			map.put(Parameter.MSG, "参数错误");
			map.put(Parameter.CODE, Parameter.FALSE);
			response.getWriter().print(gson.toJson(map));
			return;
		}
		if (location.getTotal().equals("") || location.getTotal()==null) {
			map.put(Parameter.MSG, "参数错误");
			map.put(Parameter.CODE, Parameter.FALSE);
			response.getWriter().print(gson.toJson(map));
			return;
		}
		
		String locationId = null;
		Integer tempId;
		if ((tempId = locationService.getMaxLocationId()) != null) {
			locationId = String.valueOf(tempId+1);
		} else {
			locationId = "1";
		}
		location.setLocationId(locationId);
		location.setDate(String.valueOf(new Date().getTime()));
		location.setPoint(point);
		location.setProjectName(projectName);
		location.setContractId(contractId);
		
		Property property;
		if ((property = propertyService.getPropertyByPropertyId(projectName, location.getPropertyId())) == null) {
			map.put(Parameter.MSG, "合同项已经被删除");
			map.put(Parameter.CODE, Parameter.FALSE);
			response.getWriter().print(gson.toJson(map));
			return;
		}
//		if (property.getCostType().equals(CostTypeEnum.DAY.getValue())) {
//			location.setStatus("1");
//		} else {
//			
//		}
		location.setStatus("0");
		
		location.setType(property.getType());
		location.setCostType(property.getCostType());
		location.setPrice(property.getPrice());
		
		Double total = Double.valueOf(property.getTotal());
		total = total + Double.valueOf(location.getTotal());
		property.setTotal(String.valueOf(total));
		propertyService.update(property);
		
		ProjectLog projectLog = new ProjectLog();
		projectLog.setCause("saveLocation");
		projectLog.setContractId(contractId);
		projectLog.setDate(String.valueOf(new Date().getTime()));
		projectLog.setUserName(user.getName());
		projectLog.setProjectName(projectName);
		StringBuffer sb = new StringBuffer();
		sb.append(user.getTruename()).append(":").append("入场了").append(contract.getContractId()).append("编号")
		.append(contract.getName()).append("合同的").append(contract.getName()).append("合同项");
		projectLog.setLogString(sb.toString());
		projectLogService.save(projectLog);
		
		Project project = projectService.getProjectByName(projectName);
		CostLog costLog = new CostLog();
		if(property.getCostType().equals(CostTypeEnum.DAYREN.getValue()) || property.getCostType().equals(CostTypeEnum.DAYTAI.getValue()) || property.getType().equals(TypeEnum.RENT.getValue())){
			double cost = Double.valueOf(property.getPrice()) * Double.valueOf(location.getTotal());
			String nowDate = String.valueOf(new Date().getTime());
			
			if (property.getType().equals(TypeEnum.PEOPLE.getValue())) {
				Double peopleCost = Double.valueOf(project.getPeopleCost());
				peopleCost = peopleCost + cost;
				project.setPeopleCost(String.valueOf(peopleCost));
			}else if(property.getType().equals(TypeEnum.MATERIAL.getValue()) || property.getType().equals(TypeEnum.RENT.getValue())){
				Double materialCost = Double.valueOf(project.getMaterialCost());
				materialCost = materialCost + cost;
				project.setMaterialCost(String.valueOf(materialCost));
			} else {
				Double machineCost = Double.valueOf(project.getMachineCost());
				machineCost = machineCost + cost;
				project.setMachineCost(String.valueOf(machineCost));
			}
			
			Double allCost = Double.valueOf(project.getAllCost());
			allCost = allCost + cost;
			project.setAllCost(String.valueOf(allCost));
			
			costLog.setStartDate(nowDate);
			costLog.setEndDate(nowDate);
			costLog.setTotal(location.getTotal());
			costLog.setCost(String.valueOf(cost));
			costLog.setContractId(contract.getContractId());
			costLog.setProjectName(property.getProjectName());
			costLog.setPropertyId(property.getPropertyId());
			costLog.setPropertyName(property.getName());
			costLog.setPrice(property.getPrice());
			costLog.setStatus("0");
			costLog.setCostType(property.getCostType());
			costLog.setType(contract.getType());
			costLog.setRemark(location.getRemark());
			costLog.setLocationId(locationId);
			costLogService.save(costLog);
			
			projectLog = new ProjectLog();
			projectLog.setCause("dayCost");
			projectLog.setContractId(contract.getContractId());
			projectLog.setDate(String.valueOf(new Date().getTime()));
			projectLog.setUserName(user.getName());
			projectLog.setProjectName(contract.getProjectName());
			sb = new StringBuffer();
			sb.append("系统").append(":增加").append(contract.getContractId()).append("编号").append(contract.getName()).append("合同的")
			.append(property.getPropertyId()).append("编号").append(property.getName()).append("合同项，当天金额：").append(cost).append("元。");
			projectLog.setLogString(sb.toString());
			projectLogService.save(projectLog);
			projectService.update(project);
		}
		
		locationService.save(location);
		
		data.put("location", location);
		data.put("project", project);
		data.put("action", "save");
		data.put("costLog", costLog);
		map.put(Parameter.DATA, data);
		map.put(Parameter.MSG, "添加成功");
		map.put(Parameter.CODE, Parameter.SUCCESS);
		map.put(Parameter.DATA, data);
		response.getWriter().print(gson.toJson(map));
	}
	
	@RequestMapping(params = "method=add")
	public void add(
			@RequestParam("user") String userJson,
			@RequestParam("location") String locationJson, 
			@RequestParam("projectName") String projectName,
			@RequestParam("permission") String permission,
			HttpServletResponse response, 
			HttpServletRequest request) throws IOException, IntrospectionException, IllegalAccessException, InvocationTargetException{
		Map<String, Object> map = new HashMap<String, Object>();
		Gson gson = new Gson();
		response.setContentType("text/html;charset=utf-8");
		response.setCharacterEncoding("utf-8");
		Map<String, Object> data = new HashMap<String, Object>();	
		
		
		User user = new User();
		Location location = new Location();
		try {
			user = gson.fromJson(userJson, User.class);
			location = gson.fromJson(locationJson, Location.class);
		} catch (Exception e) {
			// TODO: handle exception
			map.put(Parameter.MSG, "参数错误");
			map.put(Parameter.CODE, Parameter.FALSE);
			response.getWriter().print(gson.toJson(map));
			return;
		}
		if (location.getTotal()==null || location.getTotal().equals("")) {
			map.put(Parameter.MSG, "参数错误");
			map.put(Parameter.CODE, Parameter.FALSE);
			response.getWriter().print(gson.toJson(map));
			return;
		}
		
		
		
		Property property = propertyService.getPropertyByPropertyId(projectName, location.getPropertyId());
		
		Contract contract = contractServic.getContractByContractIdAndProjectName(property.getContractId(), projectName);
		Project project = projectService.getProjectByName(projectName);
		
		CostLog costLog = new CostLog();
		if(property.getType().equals(TypeEnum.MATERIAL.getValue()) || property.getType().equals(TypeEnum.AMOUNT.getValue()) ){
			String nowDate = String.valueOf(new Date().getTime());
			Double total = Double.valueOf(location.getTotal());
			Double cost = total * Double.valueOf(property.getPrice());
			Double allCost = Double.valueOf(project.getAllCost());
			
			if (property.getType().equals(TypeEnum.AMOUNT.getValue())) {
				Double peopleCost = Double.valueOf(project.getPeopleCost());
				peopleCost = peopleCost + cost;
				project.setPeopleCost(String.valueOf(peopleCost));
			} else {
				Double materialCost = Double.valueOf(project.getMaterialCost());
				materialCost = materialCost + cost;
				project.setMaterialCost(String.valueOf(materialCost));
			}
			
			allCost = allCost + cost;
			project.setAllCost(String.valueOf(allCost));
			
			projectService.update(project);
			propertyService.update(property);
			
			costLog.setStartDate(nowDate);
			costLog.setEndDate(nowDate);
			costLog.setCostType(property.getCostType());
			costLog.setTotal(String.valueOf(total));
			costLog.setCost(String.valueOf(cost));
			costLog.setContractId(contract.getContractId());
			costLog.setProjectName(property.getProjectName());
			costLog.setPropertyId(property.getPropertyId());
			costLog.setPropertyName(property.getName());
			costLog.setPrice(property.getPrice());
			costLog.setStatus("0");
			costLog.setLocationId("0");
			costLog.setType(contract.getType());
			costLog.setRemark(location.getRemark());
			
			Double tempTotal = Double.valueOf(property.getTotal());
			tempTotal = tempTotal + total;
			property.setTotal(String.valueOf(tempTotal));
			
			ProjectLog projectLog = new ProjectLog();
			projectLog.setCause("saveProperty");
			projectLog.setContractId(contract.getContractId());
			projectLog.setDate(String.valueOf(new Date().getTime()));
			projectLog.setUserName(user.getName());
			projectLog.setProjectName(contract.getProjectName());

			DecimalFormat df= new DecimalFormat("#.##");  
			StringBuffer sb = new StringBuffer();
			sb.append(user.getTruename()).append(":").append("增加了").append(contract.getContractId()).append("编号").append(contract.getName()).append("合同的")
			.append(property.getPropertyId()).append("编号").append(property.getName()).append("合同项，数量：").append(total).append(property.getCostType()).append(",").append("金额:").append(df.format(cost)).append("元。");
			projectLog.setLogString(sb.toString());
			projectLogService.save(projectLog);
			costLogService.save(costLog);
		} else {
//		  (property.getType().equals(TypeEnum.OTHER.getValue()) ||property.getType().equals(TypeEnum.LABOUR.getValue()) ){
			String nowDate = String.valueOf(new Date().getTime());
			Double cost = Double.valueOf(location.getPrice());
			Double allCost = Double.valueOf(project.getAllCost());
			
			if (property.getType().equals(TypeEnum.LABOUR.getValue())) {
				Double peopleCost = Double.valueOf(project.getPeopleCost());
				peopleCost = peopleCost + cost;
				project.setPeopleCost(String.valueOf(peopleCost));
			}  else if (property.getType().equals(TypeEnum.OTHER.getValue())) {
				Double otherCost = Double.valueOf(project.getOtherCost());
				otherCost = otherCost + cost;
				project.setOtherCost(String.valueOf(otherCost));
			} else if (property.getType().equals(TypeEnum.MATERIAL.getValue())) {
				Double materialCost = Double.valueOf(project.getMaterialCost());
				materialCost = materialCost + cost;
				project.setMaterialCost(String.valueOf(materialCost));
			}  else {
				Double machineCost = Double.valueOf(project.getMachineCost());
				machineCost = machineCost + cost;
				project.setMachineCost(String.valueOf(machineCost));
			}
			
			Double tempPrice = Double.valueOf(property.getPrice());
			tempPrice = tempPrice + cost;
			property.setPrice(String.valueOf(tempPrice));
			
			allCost = allCost + cost;
			project.setAllCost(String.valueOf(allCost));
			
			projectService.update(project);
			propertyService.update(property);
			
			costLog.setStartDate(nowDate);
			costLog.setEndDate(nowDate);
			costLog.setCostType(property.getCostType());
			costLog.setTotal(property.getTotal());
			costLog.setCost(String.valueOf(cost));
			costLog.setContractId(contract.getContractId());
			costLog.setProjectName(property.getProjectName());
			costLog.setPropertyId(property.getPropertyId());
			costLog.setPropertyName(property.getName());
			costLog.setPrice(property.getPrice());
			costLog.setStatus("0");
			costLog.setLocationId("0");
			costLog.setType(contract.getType());
			costLog.setRemark(location.getRemark());
			costLogService.save(costLog);
			
			ProjectLog projectLog = new ProjectLog();
			projectLog.setCause("saveProperty");
			projectLog.setContractId(contract.getContractId());
			projectLog.setDate(String.valueOf(new Date().getTime()));
			projectLog.setUserName(user.getName());
			projectLog.setProjectName(contract.getProjectName());
			
			StringBuffer sb = new StringBuffer();
			sb.append(user.getTruename()).append(":").append("使用了").append(contract.getContractId()).append("编号").append(contract.getName()).append("合同的")
			.append(property.getPropertyId()).append("编号").append(property.getName()).append("合同项，").append("金额：").append(cost).append("元。");
			projectLog.setLogString(sb.toString());
			projectLogService.save(projectLog);
		}
		
		
		data.put("action", "add");
		data.put("costLog", costLog);
		data.put("project", project);
		data.put("property", property);
		map.put(Parameter.MSG, "添加成功");
		map.put(Parameter.CODE, Parameter.SUCCESS);
		map.put(Parameter.DATA, data);
		response.getWriter().print(gson.toJson(map));
	}
	
	
	@RequestMapping(params = "method=update")
	public void updateLocation(
			@RequestParam("user") String userJson,
			@RequestParam("permission") String permission, 
			@RequestParam("locationId") String locationId, 
			@RequestParam("point") String point, 
			@RequestParam("contractId") String contractId, 
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
		
		Contract contract = contractServic.getContractByContractIdAndProjectName(contractId, projectName);
		Location location = locationService.getLocationByLocationId(locationId);
		location = locationService.getLocationById(location.getId());
		location.setDate(String.valueOf(new Date().getTime()));
		location.setPoint(point);
		locationService.update(location);
		
		ProjectLog projectLog = new ProjectLog();
		projectLog.setCause("updateLocation");
		projectLog.setContractId(contractId);
		projectLog.setDate(String.valueOf(new Date().getTime()));
		projectLog.setUserName(user.getName());
		projectLog.setUserTruename(user.getTruename());
		projectLog.setProjectName(projectName);
		StringBuffer sb = new StringBuffer();
		sb.append(user.getTruename()).append(":").append("移动了").append(contract.getContractId()).append("编号")
		.append(contract.getName()).append("合同的").append(contract.getName()).append("合同项。");
		projectLog.setLogString(sb.toString());
		projectLogService.save(projectLog);
		
		map.put(Parameter.MSG, "修改成功");
		map.put(Parameter.CODE, Parameter.SUCCESS);
		response.getWriter().print(gson.toJson(map));
	}
	
	@RequestMapping(params = "method=turnOn")
	public void turnOnLocation(
			@RequestParam("user") String userJson,
			@RequestParam("permission") String permission, 
			@RequestParam("locationId") String locationId, 
			@RequestParam("contractId") String contractId, 
			@RequestParam("projectName") String projectName,
			HttpServletResponse response, 
			HttpServletRequest request) throws IOException, IntrospectionException, IllegalAccessException, InvocationTargetException{
		
		Map<String, Object> map = new HashMap<String, Object>();
		Gson gson = new Gson();
		response.setContentType("text/html;charset=utf-8");
		response.setCharacterEncoding("utf-8");
		Map<String, Object> data = new HashMap<String, Object>();	
		
		Location location ;
		User user = new User();
		try {
//			location = gson.fromJson(locationJson, Location.class);
			user = gson.fromJson(userJson, User.class);
		} catch (Exception e) {
			// TODO: handle exception
			map.put(Parameter.MSG, "参数错误");
			map.put(Parameter.CODE, Parameter.FALSE);
			response.getWriter().print(gson.toJson(map));
			return;
		}
		
		
		if ((location = locationService.getLocationByLocationId(locationId))!= null) {
			location.setStatus("1");
			locationService.update(location);
		} else {
			map.put(Parameter.MSG, "该坐标不存在");
			map.put(Parameter.CODE, Parameter.FALSE);
			response.getWriter().print(gson.toJson(map));
			return;
		}
		
		if (location.getStatus().equals("0") && costLogService.getCostLogOnByLocationId(location.getId()) != null) {
			map.put(Parameter.MSG, "该坐标已经开启了");
			map.put(Parameter.CODE, Parameter.FALSE);
			response.getWriter().print(gson.toJson(map));
			return;
		}
		Project project = projectService.getProjectByName(projectName);
		Contract contract = contractServic.getContractByContractIdAndProjectName(contractId, projectName);
		
		ProjectLog projectLog = new ProjectLog();
		projectLog.setCause("turnOnLocation");
		projectLog.setContractId(contractId);
		projectLog.setDate(String.valueOf(new Date().getTime()));
		projectLog.setUserName(user.getName());
		projectLog.setUserTruename(user.getTruename());
		projectLog.setProjectName(projectName);
		StringBuffer sb = new StringBuffer();
		sb.append(user.getTruename()).append(":").append("开启了").append(contract.getContractId()).append("编号")
		.append(contract.getName()).append("合同的").append(contract.getName()).append("合同项。");
		projectLog.setLogString(sb.toString());
		
		Property property = propertyService.getPropertyByPropertyId(projectName, location.getPropertyId());
		CostLog costLog = new CostLog();
		costLog.setLocationId(String.valueOf(location.getId()));
		costLog.setContractId(contractId);
		costLog.setStatus("1");
		costLog.setProjectName(projectName);
		costLog.setTotal(location.getTotal());
		costLog.setType(contract.getType());
		costLog.setStartDate(String.valueOf(new Date().getTime()));
		costLog.setPropertyId(location.getPropertyId());
		costLog.setPropertyName(property.getName());
		costLog.setPropertyType(property.getType());
		costLog.setEndDate("0");
		costLog.setCostType(property.getCostType());
		costLog.setPrice(property.getPrice());
		costLog.setRemark(location.getRemark());
		costLogService.save(costLog);
		
		projectLogService.save(projectLog);
		
		data.put("costLog", costLog);
		data.put("project", project);
		map.put(Parameter.DATA, data);
		map.put(Parameter.MSG, "开启成功");
		map.put(Parameter.CODE, Parameter.SUCCESS);
		response.getWriter().print(gson.toJson(map));
	}
	
	@RequestMapping(params = "method=turnOff")
	public void turnOffLocation(
			@RequestParam("user") String userJson,
			@RequestParam("permission") String permission, 
			@RequestParam("locationId") String locationId, 
			@RequestParam("contractId") String contractId, 
			@RequestParam("projectName") String projectName,
			HttpServletResponse response, 
			HttpServletRequest request) throws IOException, IntrospectionException, IllegalAccessException, InvocationTargetException{
		
		Map<String, Object> map = new HashMap<String, Object>();
		Gson gson = new Gson();
		response.setContentType("text/html;charset=utf-8");
		response.setCharacterEncoding("utf-8");
		Map<String, Object> data = new HashMap<String, Object>();	
		
		Location location;
		User user = new User();
		try {
//			location = gson.fromJson(locationJson, Location.class);
			user = gson.fromJson(userJson, User.class);
		} catch (Exception e) {
			// TODO: handle exception
			map.put(Parameter.MSG, "参数错误");
			map.put(Parameter.CODE, Parameter.FALSE);
			response.getWriter().print(gson.toJson(map));
			return;
		}
		
		if ((location = locationService.getLocationByLocationId(locationId)) == null) {
			map.put(Parameter.MSG, "该坐标不存在");
			map.put(Parameter.CODE, Parameter.FALSE);
			response.getWriter().print(gson.toJson(map));
			return;
		}
		
		if (location.getStatus().equals("0")) {
			map.put(Parameter.MSG, "该坐标已经关闭");
			map.put(Parameter.CODE, Parameter.FALSE);
			response.getWriter().print(gson.toJson(map));
			return;
		}
		
		CostLog costLog = costLogService.getCostLogOnByLocationId(Integer.valueOf(locationId));
		if (costLog == null) {
			map.put(Parameter.MSG, "异常错误，请联系管理员");
			map.put(Parameter.CODE, Parameter.FALSE);
			response.getWriter().print(gson.toJson(map));
			return;
		}
		Property property = propertyService.getPropertyByPropertyId(projectName, location.getPropertyId());
		if (property.getCostType().equals(CostTypeEnum.DAYREN.getValue()) || property.getCostType().equals(CostTypeEnum.DAYTAI.getValue()) || property.getType().equals(TypeEnum.RENT.getValue())) {
			map.put(Parameter.MSG, "该合同项无法关闭");
			map.put(Parameter.CODE, Parameter.FALSE);
			response.getWriter().print(gson.toJson(map));
			return;
		}
		Contract contract = contractServic.getContractByContractIdAndProjectName(contractId, projectName);
		
		
		Double endDate = Double.valueOf(new Date().getTime());
		costLog.setEndDate(String.valueOf((new Date().getTime())));
		Double startDate = Double.valueOf(costLog.getStartDate());
		Double costDate = (endDate - startDate)/Double.valueOf((1000*60*60));
		DecimalFormat df= new DecimalFormat("#.##");  
		costLog.setTotal(df.format(costDate));
		Double cost = costDate*Double.valueOf(costLog.getPrice())*Double.valueOf(location.getTotal());
		costLog.setCost(String.valueOf(cost));
		costLog.setStatus("0");
		costLog.setLocationId(location.getLocationId());
		
		Project project = projectService.getProjectByName(projectName);
		double tempCost;
		if (contract.getType().equals(TypeEnum.LABOUR.getKey())) {
			tempCost = Double.valueOf(project.getPeopleCost());
			tempCost = tempCost + cost;
			project.setPeopleCost(String.valueOf(tempCost));
		} else {
			tempCost = Double.valueOf(project.getMachineCost());
			tempCost = tempCost + cost;
			project.setMachineCost(String.valueOf(tempCost));
		}
		tempCost = Double.valueOf(project.getAllCost());
		tempCost = tempCost + cost;
		project.setAllCost(String.valueOf(tempCost));
		
		ProjectLog projectLog = new ProjectLog();
		projectLog.setCause("turnOffLocation");
		projectLog.setContractId(contractId);
		projectLog.setDate(String.valueOf(new Date().getTime()));
		projectLog.setUserName(user.getName());
		projectLog.setUserTruename(user.getTruename());
		projectLog.setProjectName(projectName);
		StringBuffer sb = new StringBuffer();
		sb.append(user.getTruename()).append(":").append("停止了").append(contract.getContractId()).append("编号")
		.append(contract.getName()).append("合同的").append(property.getName()).append("合同项，金额：").append(df.format(cost)).append("元。");
		projectLog.setLogString(sb.toString());
		
		projectLogService.save(projectLog);
		projectService.update(project);
		costLogService.update(costLog);
		location.setStatus("0");
		locationService.update(location);
		
		map.put(Parameter.DATA, data);
		data.put("costLog", costLog);
		data.put("project", project);
		map.put(Parameter.MSG, "停止成功");
		map.put(Parameter.CODE, Parameter.SUCCESS);
		
		response.getWriter().print(gson.toJson(map));
	}

	@RequestMapping(params = "method=delete")
	public void deleteLocation(
			@RequestParam("user") String userJson,
			@RequestParam("permission") String permission, 
			@RequestParam("locationId") String locationId, 
			@RequestParam("contractId") String contractId, 
			@RequestParam("projectName") String projectName,
			HttpServletResponse response, 
			HttpServletRequest request) throws IOException, IntrospectionException, IllegalAccessException, InvocationTargetException{
		
		Map<String, Object> map = new HashMap<String, Object>();
		Gson gson = new Gson();
		response.setContentType("text/html;charset=utf-8");
		response.setCharacterEncoding("utf-8");
		Map<String, Object> data = new HashMap<String, Object>();	
		
		Location location;
		User user = new User();
		try {
//			location = gson.fromJson(locationJson, Location.class);
			user = gson.fromJson(userJson, User.class);
		} catch (Exception e) {
			// TODO: handle exception
			map.put(Parameter.MSG, "参数错误");
			map.put(Parameter.CODE, Parameter.FALSE);
			response.getWriter().print(gson.toJson(map));
			return;
		}
		
		if ((location = locationService.getLocationByLocationId(locationId)) == null) {
			map.put(Parameter.MSG, "该坐标不存在");
			map.put(Parameter.CODE, Parameter.FALSE);
			response.getWriter().print(gson.toJson(map));
			return;
		}
		
		if (location.getStatus().equals("1") && location.getCostType().equals(CostTypeEnum.HOURREN.getValue()) && location.getCostType().equals(CostTypeEnum.HOURTAI.getValue())) {
			map.put(Parameter.MSG, "请先关闭");
			map.put(Parameter.CODE, Parameter.FALSE);
			response.getWriter().print(gson.toJson(map));
			return;
		}
		Property property = propertyService.getPropertyByPropertyId(projectName, location.getPropertyId());
		Double total = Double.valueOf(property.getTotal());
		total = total - Double.valueOf(location.getTotal());
		property.setTotal(String.valueOf(total));
		propertyService.update(property);
		
		Contract contract = contractServic.getContractByContractIdAndProjectName(contractId, projectName);
		ProjectLog projectLog = new ProjectLog();
		projectLog.setCause("deleteLocation");
		projectLog.setContractId(contractId);
		projectLog.setDate(String.valueOf(new Date().getTime()));
		projectLog.setUserName(user.getName());
		projectLog.setUserTruename(user.getTruename());
		projectLog.setProjectName(projectName);
		StringBuffer sb = new StringBuffer();
		sb.append(user.getTruename()).append(":").append("离场了").append(contract.getContractId()).append("编号")
		.append(contract.getName()).append("合同的").append(contract.getName()).append("合同项。");
		projectLog.setLogString(sb.toString());
		
		projectLogService.save(projectLog);
		locationService.delete(location);
		List costLogs = costLogService.getTodayCostLogByProjectAndDate(projectName);
		data.put("costLogs", costLogs);
		map.put(Parameter.DATA, data);
		map.put(Parameter.MSG, "离场成功");
		map.put(Parameter.CODE, Parameter.SUCCESS);
		response.getWriter().print(gson.toJson(map));
	}
}