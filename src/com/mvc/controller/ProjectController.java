package com.mvc.controller;

import java.beans.IntrospectionException;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.cfg.annotations.Nullability;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.mvc.common.CostTypeEnum;
import com.mvc.common.Parameter;
import com.mvc.common.PermissionEnum;
import com.mvc.common.TypeEnum;
import com.mvc.entity.Contract;
import com.mvc.entity.ContractStowage;
import com.mvc.entity.CostLog;
import com.mvc.entity.Location;
import com.mvc.entity.Project;
import com.mvc.entity.ProjectCost;
import com.mvc.entity.ProjectLog;
import com.mvc.entity.Property;
import com.mvc.entity.User;
import com.mvc.entity.Utp;
import com.mvc.entity.Valuation;
import com.mvc.entity.ValuationReport;
import com.mvc.service.AnnouncementService;
import com.mvc.service.ContractServic;
import com.mvc.service.ContractStowageService;
import com.mvc.service.CostLogService;
import com.mvc.service.LocationService;
import com.mvc.service.ProjectCostService;
import com.mvc.service.ProjectLogService;
import com.mvc.service.ProjectService;
import com.mvc.service.PropertyService;
import com.mvc.service.StudentService;
import com.mvc.service.UserService;
import com.mvc.service.UtdService;
import com.mvc.service.UtpService;
import com.mvc.service.ValuationReportService;
import com.mvc.service.ValuationService;
import com.mvc.util.DateUtil;
import com.mvc.util.ObjectUtil;

@Controller
@RequestMapping("/project.do")
public class ProjectController {
	protected final transient Log log = LogFactory
	.getLog(StudentController.class);
	
	@Autowired
	@Qualifier("ProjectServiceImpl")
	private ProjectService projectService;
	
	@Autowired
	@Qualifier("AnnouncementServiceImpl")
	private AnnouncementService announcementService;
	
	@Autowired
	@Qualifier("UtpServiceImpl")
	private UtpService utpService;
	
	@Autowired
	@Qualifier("UtdServiceImpl")
	private UtdService utdService;
	
	@Autowired
	@Qualifier("ContractServiceImpl")
	private ContractServic contractServic;
	
	@Autowired
	@Qualifier("UserServiceImpl")
	private UserService userService;
	
	@Autowired
	@Qualifier("ContractStowageServiceImpl")
	private ContractStowageService contractStowageService;
	
	@Autowired
	@Qualifier("LocationServiceImpl")
	private LocationService locationService;
	
	@Autowired
	@Qualifier("PropertyServiceImpl")
	private PropertyService propertyService;
	
	@Autowired
	@Qualifier("ProjectLogServiceImpl")
	private ProjectLogService projectLogService;
	
	@Autowired
	@Qualifier("CostLogServiceImpl")
	private CostLogService costLogService;
	
	@Autowired
	@Qualifier("ValuationServiceImpl")
	private ValuationService valuationService;
	
	@Autowired
	@Qualifier("ValuationReportServiceImpl")
	private ValuationReportService valuationReportService;
	
	@Autowired
	@Qualifier("ProjectCostServiceImpl")
	private ProjectCostService projectCostService;
	
	@Autowired
	private ObjectUtil objectUtil;
	
	@Autowired
	private  DateUtil dateUtil;
	
	
	@RequestMapping(params = "method=load")
	public void loadProject(
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
			// TODO: handle exceptionß
			map.put(Parameter.MSG, "参数错误");
			map.put(Parameter.CODE, Parameter.FALSE);
			response.getWriter().print(gson.toJson(map));
			return;
		}
		
		Project project;
		if ((project = projectService.getProjectByName(projectName)) == null ) {
			map.put(Parameter.MSG, "工程不存在");
			map.put(Parameter.CODE, Parameter.FALSE);
			response.getWriter().print(gson.toJson(map));
			return;
		}
		if (project.getStatus().equals("0")) {
			map.put(Parameter.MSG, "工程已竣工");
			map.put(Parameter.CODE, Parameter.FALSE);
			response.getWriter().print(gson.toJson(map));
			return;
		}
		
		List propertyList = new ArrayList();
		Map<String, Object> propertyMap = new HashMap<String, Object>();
		List contracts = contractServic.getContractsOnByProjectId(project.getId());
		List contractList = new ArrayList();
		for(int i = 0; i< contracts.size(); i++){
			Contract contract = (Contract) contracts.get(i);
			Map<String, Object> contracMap = objectUtil.convertBean(contract);
			ContractStowage contractStowage = contractStowageService.getContractStowageOnByNameAndType(contract.getName(), contract.getType());
			if (contractStowage != null) {
				contracMap.put("contractStowageId", contractStowage.getId());
			} else {
				contracMap.put("contractStowageId", 0);
			}
			
			List propertys = propertyService.getPropertysOnByContractIdAndProjectName(contract.getContractId(), projectName);
			for(int j = 0; j< propertys.size(); j++){
				List locations = new ArrayList();
				Property property = (Property) propertys.get(j);
				propertyMap = objectUtil.convertBean(property);
				locations = locationService.getLocationsByPropertyId(property.getPropertyId(), projectName);
				if (locations == null) {
					propertyMap.put("locations", new ArrayList());
				} else {
					propertyMap.put("locations", locations);
				}
				propertyList.add(propertyMap);
			}
			contractList.add(contracMap);
		}
		
		Utp utp = (Utp) utpService.getUtpByUserNameAndProjectName(user.getName(), projectName);
		if (utp!=null) {
			if (utp.getPermission().equals(PermissionEnum.ADMIN.getKey())) {
				data.put("permission", PermissionEnum.ADMIN.getValue());
			} else if (utp.getPermission().equals(PermissionEnum.CREATER.getKey())) {
				data.put("permission", PermissionEnum.CREATER.getValue());
			} else if (utp.getPermission().equals(PermissionEnum.USER.getKey())) {
				data.put("permission", PermissionEnum.USER.getValue());
			}else if (utp.getPermission().equals(PermissionEnum.APPLICANT.getKey())) {
				data.put("permission", PermissionEnum.APPLICANT.getValue());
			} else if (utp.getPermission().equals(PermissionEnum.TOP.getKey())) {
				data.put("permission", PermissionEnum.TOP.getValue());
			}
		} else {
			map.put(Parameter.MSG, "权限不足");
			map.put(Parameter.CODE, Parameter.FALSE);
			response.getWriter().print(gson.toJson(map));
			return;
		}
		long costTime = Long.valueOf(project.getCostDate());
		long nowTime = new Date().getTime();
		//如果是第二天
		if (costTime < nowTime) {
			Integer day = (int) ((nowTime - costTime)/(24*60*60*1000))+1;
			for (int i = 0; i < day; i++) {
				long dayLong = (i*24*60*60*1000);
				//计算昨天的projectCost
				ProjectCost projectCost = new ProjectCost();
				projectCost.setDate(String.valueOf(costTime+dayLong));
				projectCost.setProjectName(projectName);
				projectCost.setCost(project.getAllCost());
				//更新昨日的projectCost
				long startDate = Long.parseLong(projectCost.getDate()) - 86400000;
				List projectCostList = projectCostService.getProjectCostsByProjectNameAndDate(projectName, String.valueOf(startDate), projectCost.getDate());
				if (projectCostList.size() > 0) {
					ProjectCost lastProjectCost = (ProjectCost) projectCostList.get(0);
					Double todayCost = Double.valueOf(project.getAllCost()) - Double.valueOf(lastProjectCost.getCost());
					projectCost.setTodayCost(String.valueOf(todayCost));
				} else {
					projectCost.setTodayCost(project.getAllCost());
				}
				
				
				List locations = locationService.getLocationsCostTypeDayByProject(projectName);
				Calendar cal = Calendar.getInstance();
				  cal.set(Calendar.HOUR_OF_DAY, 0);
				  cal.set(Calendar.SECOND, 0);
				  cal.set(Calendar.MINUTE, 0);
				  cal.set(Calendar.MILLISECOND, 001);
				  Double projectAllCost = Double.valueOf(project.getAllCost());
				  Double peopleCost = Double.valueOf(project.getPeopleCost());
				  Double machineCost = Double.valueOf(project.getMachineCost());
				  for(int j=0; j<locations.size(); j++){
					Location location = (Location) locations.get(j);
					
					double cost = Double.valueOf(location.getPrice()) * Double.valueOf(location.getTotal());
					Property property = propertyService.getPropertyByPropertyId(projectName, location.getPropertyId());
					//如果是按小时结算 跳过
					if (property.getCostType().equals(CostTypeEnum.HOURREN.getValue()) || 
							property.getCostType().equals(CostTypeEnum.HOURTAI.getValue()) || 
							property.getCostType().equals(CostTypeEnum.TIME.getValue()) ||
							property.getType().equals(TypeEnum.MATERIAL.getValue())) {
						continue;
					}
					
					
					Contract contract = contractServic.getContractByContractIdAndProjectName(property.getContractId(), projectName);
					if (property.getType().equals(TypeEnum.PEOPLE.getValue())) {
						peopleCost = peopleCost + cost;
					} else {
						machineCost = machineCost + cost;
					}
					
					projectAllCost = projectAllCost + cost;
					
					ProjectLog projectLog = new ProjectLog();
					projectLog.setCause("dayCost");
					projectLog.setContractId(contract.getContractId());
					projectLog.setDate(String.valueOf(new Date().getTime() - dayLong));
					projectLog.setUserName(user.getName());
					projectLog.setUserTruename(user.getTruename());
					projectLog.setProjectName(projectName);
					StringBuffer sb = new StringBuffer();
					sb.append("系统").append(":增加").append(contract.getContractId()).append("编号").append(contract.getName()).append("合同的")
					.append(property.getPropertyId()).append("编号").append(property.getName()).append("合同项当天金额").append(cost).append("元。");
					projectLog.setLogString(sb.toString());
					projectLogService.save(projectLog);
					
					CostLog costLog = new CostLog();
					costLog.setStartDate(String.valueOf(cal.getTimeInMillis() - dayLong));
					costLog.setEndDate(String.valueOf(cal.getTimeInMillis() - dayLong));
					costLog.setCostType(property.getCostType());
					costLog.setTotal(property.getTotal());
					costLog.setType(contract.getType());
					costLog.setCost(String.valueOf(cost));
					costLog.setContractId(contract.getContractId());
					costLog.setProjectName(property.getProjectName());
					costLog.setPropertyId(property.getPropertyId());
					costLog.setPrice(property.getPrice());
					
					Property tempProperty = propertyService.getPropertyByPropertyId(projectName, location.getPropertyId());
					costLog.setPropertyName(tempProperty.getName());
					costLog.setStatus("0");
					costLog.setRemark("自动结算");
					costLog.setLocationId(location.getLocationId());
					costLogService.save(costLog);
				}
				//增加工程中的估值
				Valuation lastValuation = valuationService.getMaxValuationByProjectName(projectName);
				Double allValuation = Double.valueOf(project.getAllValuation());
				if (lastValuation!=null) {
					allValuation = allValuation + lastValuation.getTodayCost();
					lastValuation.setCost(allValuation);
					valuationService.update(lastValuation);
				} else {
					lastValuation = new Valuation();
					lastValuation.setCost(allValuation);
					lastValuation.setDate(String.valueOf(nowTime - dayLong));
					lastValuation.setTodayCost(0.0);
					lastValuation.setProjectName(projectName);
					lastValuation.setValuationId(String.valueOf(Integer.valueOf(valuationService.getMaxValuationId())+1));
					valuationService.save(lastValuation);
				}
				
				project.setAllValuation(String.valueOf(allValuation));
				project.setAllCost(String.valueOf(projectAllCost));
				project.setMachineCost(String.valueOf(machineCost));
				project.setPeopleCost(String.valueOf(peopleCost));
				
				
				Long oneDay = (long) (1000 * 60 * 60 * 24);
				Long costDay = costTime+oneDay - dayLong;
				project.setCostDate(dateUtil.dayEndDate(new Date()));
				
				//创建新一天估值
				Valuation valuation = new Valuation();
				String valuationId = String.valueOf(valuationService.getMaxValuationId()+1);
				valuation.setValuationId(valuationId);
				valuation.setProjectName(projectName);
				valuation.setDate(String.valueOf(Long.parseLong(dateUtil.dayStartDate(new Date())) - dayLong));
				valuation.setCost(0.0);
				valuation.setTodayCost(0.0);
				List valuationReports = valuationReportService.getValuationReportByLastValuationAndProjectName(projectName);
				Double cost = 0.0;
				ValuationReport tempValuationReport;
				for (int j = 0; j < valuationReports.size(); j++) {
					ValuationReport valuationReport = (ValuationReport) valuationReports.get(j);
					tempValuationReport = new ValuationReport(); 
					tempValuationReport.setName(valuationReport.getName());
					tempValuationReport.setPrice(valuationReport.getPrice());
					tempValuationReport.setPriceDate(String.valueOf(Long.parseLong(dateUtil.dayStartDate(new Date())) - dayLong));
					tempValuationReport.setPriceUser(valuationReport.getPriceUser());
					tempValuationReport.setPriceUserTruename(valuationReport.getPriceUserTruename());
					tempValuationReport.setRemark(valuationReport.getRemark());
					tempValuationReport.setValuationId(valuationId);
					tempValuationReport.setCost("0.0");
					valuationReportService.save(tempValuationReport);
				}
				//更新昨日的projectCost
//				projectCostService.getProjectCostsByProjectNameAndDate(projectName, startDate, endDate);
				
				
				
				valuationService.save(valuation);
				projectCostService.save(projectCost);
				projectService.update(project);
			}
		}
		
		List costLogs = costLogService.getTodayCostLogByProjectAndDate(projectName);
		Long announcementNumber = announcementService.getAnnouncementNumberByProjectName(projectName);
		
		map.put(Parameter.MSG, "刷新成功");
		map.put(Parameter.CODE, Parameter.SUCCESS);
		data.put("announcementNumber", announcementNumber);
		data.put("project", project);
		data.put("contracts", contractList);
		data.put("propertys", propertyList);
		data.put("costLogs", costLogs);
		map.put(Parameter.DATA, data);
		response.getWriter().print(gson.toJson(map));
	}
	
	@RequestMapping(params = "method=save")
	public void saveProject(
			@RequestParam("project") String projectJson, 
			@RequestParam("map")String projectMap,
			@RequestParam("center")String projectCenter,
			HttpServletResponse response, 
			HttpServletRequest request) throws IOException{
		
		Map<String, Object> map = new HashMap<String, Object>();
		Gson gson = new Gson();
		response.setContentType("text/html;charset=utf-8");
		response.setCharacterEncoding("utf-8");
		Map<String, Object> data = new HashMap<String, Object>();	
		
		Project project = new Project();
		
		
		try {
			project = gson.fromJson(projectJson, Project.class);
		} catch (Exception e) {
			// TODO: handle exception
			map.put(Parameter.MSG, "参数错误");
			map.put(Parameter.CODE, Parameter.FALSE);
			response.getWriter().print(gson.toJson(map));
			return;
		}
		
		project.setCenter(projectCenter);
		project.setMap(projectMap);
		project.setAllCost("0");
		project.setCostDate(String.valueOf(new Date().getTime()));
		project.setMachineCost("0");
		project.setMaterialCost("0");
		project.setOtherCost("0");
		project.setPeopleCost("0");
		project.setAllValuation("0");
		project.setStatus("1");
		project.setCostDate(dateUtil.dayEndDate(new Date()));
		project.setExpectEndDate(project.getExpectEndDate());
		project.setExpectProfit(project.getExpectProfit());
		
		System.out.println("creat"+project.getName());
		project.setDate(String.valueOf(new Date().getTime()));
		if (project.getName().equals("") || project.getName().length() >=20 || project.getName()== null) {
			map.put(Parameter.MSG, "工程名称为空或者工程名称长度不能超过10");
			map.put(Parameter.CODE, Parameter.FALSE);
			response.getWriter().print(gson.toJson(map));
			return;
		}
		if (projectService.getProjectByName(project.getName()) != null) {
			map.put(Parameter.MSG, "工程名称已经存在");
			map.put(Parameter.CODE, Parameter.FALSE);
			response.getWriter().print(gson.toJson(map));
			return;
		}
		if (project.getCreator() ==null) {
			map.put(Parameter.MSG, "中心点未设定");
			map.put(Parameter.CODE, Parameter.FALSE);
			response.getWriter().print(gson.toJson(map));
			return;
		}
		
		if (project.getCreator() == null || project.getCreator().equals("")) {
			map.put(Parameter.MSG, "未知错误，请联系管理人员");
			map.put(Parameter.CODE, Parameter.FALSE);
			response.getWriter().print(gson.toJson(map));
			return;
		}
		
		//创建新一天估值
		Valuation valuation = new Valuation();
		String valuationId;
		Integer maxId = valuationService.getMaxValuationId();
		if (maxId == null) {
			valuationId = "1";
		} else {
			valuationId = String.valueOf(valuationService.getMaxValuationId()+1);
		}
		valuation.setValuationId(valuationId);
		valuation.setDate(dateUtil.dayStartDate(new Date()));
		valuation.setCost(0.0);
		valuation.setProjectName(project.getName());
		valuation.setTodayCost(0.0);
		valuationService.save(valuation);
		
		Utp utp = new Utp();
		utp.setProjectName(project.getName());
		utp.setUserName(project.getCreator());
		utp.setPermission("1");
		utp.setIsRead("1");
		utpService.save(utp);
		
		projectService.saveProject(project);
		
		User user = (User)userService.getUser(userService.getUserByName(project.getCreator())).get(0);
		List<Object> list = projectService.getUtpProjectsByUser(user);
		data.put("user", userService.getUser(user).get(0));
		data.put("list", list);
		map.put(Parameter.MSG, "创建成功");
		map.put(Parameter.CODE, Parameter.SUCCESS);
		map.put(Parameter.DATA, data);
		response.getWriter().print(gson.toJson(map));
		
	}
	
	@RequestMapping(params = "method=update")
	public void updateProject(
			@RequestParam("user") String userJson,
			@RequestParam("project") String projectJson, 
			@RequestParam("map")String projectMap,
			@RequestParam("projectName") String projectName,
			@RequestParam("center")String projectCenter,
			HttpServletResponse response, 
			HttpServletRequest request) throws IOException{
		
		
		
		Map<String, Object> map = new HashMap<String, Object>();
		Gson gson = new Gson();
		response.setContentType("text/html;charset=utf-8");
		response.setCharacterEncoding("utf-8");
		Map<String, Object> data = new HashMap<String, Object>();	
		
		Project project = new Project();
		User user = new User();
		
		try {
			user = gson.fromJson(userJson, User.class);
			project = gson.fromJson(projectJson, Project.class);
		} catch (Exception e) {
			// TODO: handle exception
			map.put(Parameter.MSG, "参数错误");
			map.put(Parameter.CODE, Parameter.FALSE);
			response.getWriter().print(gson.toJson(map));
			return;
		}
		
		if (!utpService.getUtpByUserNameAndProjectName(user.getName(), projectName).getPermission().equals("1")) {
			map.put(Parameter.MSG, "权限不足");
			map.put(Parameter.CODE, Parameter.FALSE);
			response.getWriter().print(gson.toJson(map));
			return;
		}
		
		
		Project tempProject = projectService.getProjectByName(project.getName());
		if (!projectName.equals(project.getName())) {
			if (tempProject != null) {
				map.put(Parameter.MSG, "工程名称已经存在");
				map.put(Parameter.CODE, Parameter.FALSE);
				response.getWriter().print(gson.toJson(map));
				return;
			}
		}
		
		if (project.getName().equals("") || project.getName().length() >=20 || project.getName()== null) {
			map.put(Parameter.MSG, "工程名称为空或者工程名称长度不能超过10");
			map.put(Parameter.CODE, Parameter.FALSE);
			response.getWriter().print(gson.toJson(map));
			return;
		}
//		if (projectService.getProjectByName(project.getName()) != null) {
//			map.put(Parameter.MSG, "工程名称已经存在");
//			map.put(Parameter.CODE, Parameter.FALSE);
//			response.getWriter().print(gson.toJson(map));
//			return;
//		}
		if (project.getCreator() ==null) {
			map.put(Parameter.MSG, "中心点未设定");
			map.put(Parameter.CODE, Parameter.FALSE);
			response.getWriter().print(gson.toJson(map));
			return;
		}
		
		if (project.getCreator() == null || project.getCreator().equals("")) {
			map.put(Parameter.MSG, "未知错误，请联系管理人员");
			map.put(Parameter.CODE, Parameter.FALSE);
			response.getWriter().print(gson.toJson(map));
			return;
		}
		tempProject = projectService.getProjectByName(projectName);
		tempProject.setCenter(projectCenter);
		tempProject.setMap(projectMap);
		tempProject.setName(project.getName());
		tempProject.setFullName(project.getFullName());
		tempProject.setExpectProfit(project.getExpectProfit());
		tempProject.setExpectEndDate(project.getExpectEndDate());
		tempProject.setDepartmentName(project.getDepartmentName());
		tempProject.setCut(project.getCut());
//		
//		project.setCenter(projectCenter);
//		project.setMap(projectMap);
//		project.setStatus("1");
//		project.setCostDate(dateUtil.dayEndDate(new Date()));
//		project.setExpectEndDate(project.getExpectEndDate());
//		project.setExpectProfit(project.getExpectProfit());
		
		Utp utp = utpService.getUtpByUserNameAndProjectName(user.getName(), projectName);
		utp.setProjectName(project.getName());
		utpService.updateUtp(utp);
		
		projectService.update(tempProject);
		
//		User user = (User)userService.getUser(userService.getUserByName(project.getCreator())).get(0);
//		data.put("user", userService.getUser(user).get(0));
		map.put(Parameter.MSG, "修改成功");
		map.put(Parameter.CODE, Parameter.SUCCESS);
//		map.put(Parameter.DATA, data);
		response.getWriter().print(gson.toJson(map));
		
	}
	
	@RequestMapping(params = "method=search")
	public void searchProject(
			@RequestParam("projectName") String projectName, 
			@RequestParam("user") String userJson, 
			HttpServletResponse response, 
			HttpServletRequest request) throws IOException{
		
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
		
		List list = new ArrayList();
		if (projectName != null) {
			list = projectService.getProjectLikeByName(projectName);
		} else {
			map.put(Parameter.MSG, "查询失败,参数错误");
			map.put(Parameter.CODE, Parameter.FALSE);
			response.getWriter().print(gson.toJson(map));
		}
		
		if (list != null) {
			map.put(Parameter.MSG, "查询成功");
			map.put(Parameter.CODE, Parameter.SUCCESS);
			data.put("projects", list);
			map.put(Parameter.DATA, data);
			response.getWriter().print(gson.toJson(map));
		} else {
			map.put(Parameter.MSG, "查询失败");
			map.put(Parameter.CODE, Parameter.FALSE);
			response.getWriter().print(gson.toJson(map));
		}
	}
	
	@RequestMapping(params = "method=join")
	public void joinProject(
			@RequestParam("projectName") String projectName, 
			@RequestParam("userName")String userName,
			HttpServletResponse response, 
			HttpServletRequest request) throws IOException{
		Map<String, Object> map = new HashMap<String, Object>();
		Gson gson = new Gson();
		response.setContentType("text/html;charset=utf-8");
		response.setCharacterEncoding("utf-8");
		Map<String, Object> data = new HashMap<String, Object>();	
		
		if (projectName.equals("")||userName.equals("")) {
			map.put(Parameter.MSG, "参数错误");
			map.put(Parameter.CODE, Parameter.FALSE);
			response.getWriter().print(gson.toJson(map));
			return;
		} 
		
		if (projectService.getProjectByName(projectName) == null || userService.getUserByName(userName) == null) {
			map.put(Parameter.MSG, "工程或者用户不存在");
			map.put(Parameter.CODE, Parameter.FALSE);
			response.getWriter().print(gson.toJson(map));
			return;
		}
		if (utpService.getUtpByUserNameAndProjectName(userName, projectName) != null) {
			map.put(Parameter.MSG, "已经申请或者已经在工程中");
			map.put(Parameter.CODE, Parameter.FALSE);
			response.getWriter().print(gson.toJson(map));
			return;
		}
		
		Utp utp = new Utp();
		utp.setPermission(PermissionEnum.APPLICANT.getKey());
		utp.setProjectName(projectName);
		utp.setUserName(userName);
		utp.setIsRead("1");
		utpService.save(utp);
		map.put(Parameter.MSG, "申请成功");
		map.put(Parameter.CODE, Parameter.SUCCESS);
		response.getWriter().print(gson.toJson(map));
	}
	
	@RequestMapping(params = "method=checkJoin")
	public void checkJoin(
			@RequestParam("user") String userJson,
			HttpServletResponse response, 
			HttpServletRequest request) throws IOException{
		Map<String, Object> map = new HashMap<String, Object>();
		Gson gson = new Gson();
		response.setContentType("text/html;charset=utf-8");
		response.setCharacterEncoding("utf-8");
		Map<String, Object> data = new HashMap<String, Object>();	
		
		System.out.println("checkJoin"+new Date());
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
		if (userService.getUser(user).size()==0) {
			map.put(Parameter.MSG, "用户不存在");
			map.put(Parameter.CODE, Parameter.FALSE);
			response.getWriter().print(gson.toJson(map));
			return;
		}
		
		List<Object> checkJoinList = projectService.getCheckJoin(user);
		List<Object> checkReadList = projectService.getCheckAdmit(user);
		
		data.put("checkJoinList", checkJoinList);
		data.put("checkReadList", checkReadList);
		map.put(Parameter.MSG, "查询成功");
		map.put(Parameter.CODE, Parameter.SUCCESS);
		map.put(Parameter.DATA, data);
		response.getWriter().print(gson.toJson(map));
	}
	
	@RequestMapping(params = "method=readJoin")
	public void readJoin(
			@RequestParam("user") String userJson,
			@RequestParam("projectName") String projectName,
			HttpServletResponse response,
			HttpServletRequest request) throws IOException{
		Map<String, Object> map = new HashMap<String, Object>();
		Gson gson = new Gson();
		response.setContentType("text/html;charset=utf-8");
		response.setCharacterEncoding("utf-8");
		Map<String, Object> data = new HashMap<String, Object>();	
		
		System.out.println("checkJoin"+new Date());
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
		if (userService.getUser(user).size()==0) {
			map.put(Parameter.MSG, "用户不存在");
			map.put(Parameter.CODE, Parameter.FALSE);
			response.getWriter().print(gson.toJson(map));
			return;
		}
		
		Utp utp = utpService.getUtpByUserNameAndProjectName(user.getName(), projectName);
		if (utp == null) {
			map.put(Parameter.MSG, "已经确定过");
			map.put(Parameter.CODE, Parameter.FALSE);
			response.getWriter().print(gson.toJson(map));
			return;
		}
		
		utp.setIsRead("1");
		utpService.updateUtp(utp);

		map.put(Parameter.MSG, "查看成功");
		map.put(Parameter.CODE, Parameter.SUCCESS);
		response.getWriter().print(gson.toJson(map));
	}
	
	
	@RequestMapping(params = "method=admit")
	public void admitProject(
			@RequestParam("user") String user,
			@RequestParam("projectName") String projectName, 
			@RequestParam("userName")String userName,
			HttpServletResponse response, 
			HttpServletRequest request) throws IOException{
			
		Map<String, Object> map = new HashMap<String, Object>();
		Gson gson = new Gson();
		response.setContentType("text/html;charset=utf-8");
		response.setCharacterEncoding("utf-8");
		Map<String, Object> data = new HashMap<String, Object>();	

		Utp creatorUtp;
		Utp userUtp;
		userUtp = utpService.getUtpByUserNameAndProjectName(userName, projectName);
		creatorUtp = utpService.getUtpByUserNameAndProjectName(user, projectName);
		if (creatorUtp != null && userUtp != null) {
			if ((creatorUtp.getPermission().equals(PermissionEnum.CREATER.getKey()) || creatorUtp.getPermission().equals(PermissionEnum.ADMIN.getKey())) && userUtp.getPermission().equals("0")) {
				if (utdService.getUtdByUserNameAndDepartmentName(userUtp.getUserName(), "上级部门") != null) {	
					userUtp.setPermission(PermissionEnum.TOP.getKey());
				} else {
					userUtp.setPermission(PermissionEnum.USER.getKey());
				}
				userUtp.setIsRead("0");
				utpService.updateUtp(userUtp);
				map.put(Parameter.MSG, "允许成功");
				map.put(Parameter.CODE, Parameter.SUCCESS);
				response.getWriter().print(gson.toJson(map));
				return;
			} else {
				map.put(Parameter.MSG, "权限不足或者对方没有申请");
				map.put(Parameter.CODE, Parameter.FALSE);
				response.getWriter().print(gson.toJson(map));
				return;
			}
		} else {
			map.put(Parameter.MSG, "不存在");
			map.put(Parameter.CODE, Parameter.FALSE);
			response.getWriter().print(gson.toJson(map));
		}
		
	}
	
	@RequestMapping(params = "method=refuse")
	public void refuseProject(
			@RequestParam("user") String user,
			@RequestParam("projectName") String projectName, 
			@RequestParam("userName")String userName,
			HttpServletResponse response, 
			HttpServletRequest request) throws IOException{
			
		Map<String, Object> map = new HashMap<String, Object>();
		Gson gson = new Gson();
		response.setContentType("text/html;charset=utf-8");
		response.setCharacterEncoding("utf-8");
		Map<String, Object> data = new HashMap<String, Object>();	
		
		Utp creatorUtp;
		Utp userUtp;
		if ((creatorUtp = utpService.getUtpByUserNameAndProjectName(user, projectName)) != null && (userUtp = utpService.getUtpByUserNameAndProjectName(userName, projectName)) != null) {
			if (creatorUtp.getPermission().equals("1")||creatorUtp.getPermission().equals("2")) {
				utpService.delete(userUtp);
				map.put(Parameter.MSG, "拒绝成功");
				map.put(Parameter.CODE, Parameter.SUCCESS);
				response.getWriter().print(gson.toJson(map));
				return;
			}	else {
				map.put(Parameter.MSG, "权限不足");
				map.put(Parameter.CODE, Parameter.FALSE);
				response.getWriter().print(gson.toJson(map));
				return;
			}
		}
		
		map.put(Parameter.MSG, "不存在");
		map.put(Parameter.CODE, Parameter.FALSE);
		response.getWriter().print(gson.toJson(map));
	}
	
	
	
	@RequestMapping(params = "method=updatePermisson")
	public void updatePermission(
			@RequestParam("user") String userJson,
			@RequestParam("utp") String utpJson, 
			HttpServletResponse response, 
			HttpServletRequest request) throws IOException{
		
		Map<String, Object> map = new HashMap<String, Object>();
		Gson gson = new Gson();
		response.setContentType("text/html;charset=utf-8");
		response.setCharacterEncoding("utf-8");
		Map<String, Object> data = new HashMap<String, Object>();	
		
		User user = new User();
		Utp utp = new Utp();
		try {
			user = gson.fromJson(userJson, User.class);
			utp = gson.fromJson(utpJson, Utp.class);
		} catch (Exception e) {
			// TODO: handle exception
			map.put(Parameter.MSG, "参数错误");
			map.put(Parameter.CODE, Parameter.FALSE);
			response.getWriter().print(gson.toJson(map));
			return;
		}
		
		if ( !utpService.getUtpByUserNameAndProjectName(user.getName(), utp.getProjectName()).getPermission().equals("1") ) {
			map.put(Parameter.MSG, "权限不足");
			map.put(Parameter.CODE, Parameter.FALSE);
			response.getWriter().print(gson.toJson(map));
			return;
		}
		
		utpService.updateUtp(utp);
		map.put(Parameter.MSG, "修改成功");
		map.put(Parameter.CODE, Parameter.SUCCESS);
		response.getWriter().print(gson.toJson(map));
		return;
	}
	
	@RequestMapping(params = "method=getProjectUsers")
	public void getProjectUsers(
			@RequestParam("projectName") String projectName,
			HttpServletResponse response, 
			HttpServletRequest request) throws IOException, IntrospectionException, IllegalAccessException, InvocationTargetException{
		
		Map<String, Object> map = new HashMap<String, Object>();
		Gson gson = new Gson();
		response.setContentType("text/html;charset=utf-8");
		response.setCharacterEncoding("utf-8");
		Map<String, Object> data = new HashMap<String, Object>();	
		
		List utps = utpService.getUtpByProjectName(projectName);
		List list = new ArrayList();
		for (int i = 0; i < utps.size(); i++) {
			Utp utp = (Utp) utps.get(i);
			Map utpMap = objectUtil.convertBean(utp);
			if (utp.getPermission().equals(PermissionEnum.ADMIN.getKey())) {
				utpMap.put("permission", PermissionEnum.ADMIN.getValue());
			} else if (PermissionEnum.CREATER.getKey().equals(utp.getPermission())) {
				utpMap.put("permission", PermissionEnum.CREATER.getValue());
			} else if (PermissionEnum.USER.getKey().equals(utp.getPermission())) {
				utpMap.put("permission", PermissionEnum.USER.getValue());
			} else if (PermissionEnum.APPLICANT.getKey().equals(utp.getPermission())) {
				utpMap.put("permission", PermissionEnum.APPLICANT.getValue());
			} else if (PermissionEnum.TOP.getKey().equals(utp.getPermission())) {
				utpMap.put("permission", PermissionEnum.TOP.getValue());
			}
			User user = userService.getUserByName(utp.getUserName());
			utpMap.put("phone", user.getPhone());
			utpMap.put("userTruename", user.getTruename());
			list.add(utpMap);
		}
		
		
		data.put("utps", list);
		map.put(Parameter.MSG, " 查询成功");
		map.put(Parameter.CODE, Parameter.SUCCESS);
		map.put(Parameter.DATA, data);
		response.getWriter().print(gson.toJson(map));
		return;
	}
	
	@RequestMapping(params = "method=finishProject")
	public void finishProject(
			@RequestParam("user") String userJson,
			@RequestParam("projectName") String projectName, 
			HttpServletResponse response, 
			HttpServletRequest request) throws IOException{
		
		Map<String, Object> map = new HashMap<String, Object>();
		Gson gson = new Gson();
		response.setContentType("text/html;charset=utf-8");
		response.setCharacterEncoding("utf-8");
		Map<String, Object> data = new HashMap<String, Object>();	
		
		User user = new User();
		Utp utp = new Utp();
		try {
			user = gson.fromJson(userJson, User.class);
		} catch (Exception e) {
			// TODO: handle exception
			map.put(Parameter.MSG, "参数错误");
			map.put(Parameter.CODE, Parameter.FALSE);
			response.getWriter().print(gson.toJson(map));
			return;
		}
		
		
		if ( !utpService.getUtpByUserNameAndProjectName(user.getName(), projectName).getPermission().equals("1")) {
			map.put(Parameter.MSG, "权限不足");
			map.put(Parameter.CODE, Parameter.FALSE);
			response.getWriter().print(gson.toJson(map));
			return;
		}
		
		Project project = projectService.getProjectByName(projectName);
		if (project == null) {
			map.put(Parameter.MSG, "工程不存在");
			map.put(Parameter.CODE, Parameter.FALSE);
			response.getWriter().print(gson.toJson(map));
			return;
		}
		
		List contracts = contractServic.getContractsOnByProjectName(projectName);
		for (int i = 0; i < contracts.size(); i++) {
			Contract contract = (Contract) contracts.get(i);
			List propertys = propertyService.getPropertysByContractIdAndProjectName(contract.getContractId(), projectName);
			for (int j = 0; j < propertys.size(); j++) {
				Property property = (Property) propertys.get(j);
				List Locations = locationService.getLocationsByPropertyId(property.getPropertyId(), projectName);
				for (int k = 0; k < Locations.size(); k++) {
					Location location = (Location) Locations.get(k);
					if (location.getStatus().equals("1")) {
						map.put(Parameter.MSG, "还有合同项没有关闭或者离场");
						map.put(Parameter.CODE, Parameter.FALSE);
						response.getWriter().print(gson.toJson(map));
						return;
					}
				}
			}
		}
		
		project.setStatus("0");
		projectService.update(project);
		
		map.put(Parameter.MSG, "修改成功");
		map.put(Parameter.CODE, Parameter.SUCCESS);
		response.getWriter().print(gson.toJson(map));
		return;
	}
	
}
