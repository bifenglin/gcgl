package com.mvc.controller;

import java.beans.IntrospectionException;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javassist.expr.NewArray;

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
import com.mvc.entity.Project;
import com.mvc.entity.ProjectCost;
import com.mvc.entity.Report;
import com.mvc.entity.User;
import com.mvc.entity.Valuation;
import com.mvc.entity.ValuationReport;
import com.mvc.service.ProjectCostService;
import com.mvc.service.ProjectService;
import com.mvc.service.UserService;
import com.mvc.service.UtpService;
import com.mvc.service.ValuationReportService;
import com.mvc.service.ValuationService;
import com.mvc.util.ObjectUtil;

@Controller
@RequestMapping("/valuation.do")
public class ValuationController {
	protected final transient Log log = LogFactory.getLog(ValuationController.class);
	
	@Autowired
	@Qualifier("ValuationServiceImpl")
	private ValuationService valuationService;
	@Autowired
	@Qualifier("ProjectServiceImpl")
	private ProjectService projectService;
	@Autowired
	@Qualifier("ProjectCostServiceImpl")
	private ProjectCostService projectCostService;
	@Autowired
	@Qualifier("ValuationReportServiceImpl")
	private ValuationReportService valuationReportService;
	@Autowired
	@Qualifier("UtpServiceImpl")
	private UtpService utpService;
	
	@RequestMapping(params = "method=getValuation")
	public void getValuation(
			@RequestParam("user") String userJson,
			@RequestParam("projectName") String projectName,
			@RequestParam("offset") Integer offset,
			@RequestParam("limit") Integer limit,
			HttpServletResponse response, 
			HttpServletRequest request) throws IOException, IntrospectionException, IllegalAccessException, InvocationTargetException{
		
		Map<String, Object> map = new HashMap<String, Object>();
		Map<String, Object> data = new HashMap<String, Object>();	
		Gson gson = new Gson();
		response.setContentType("text/html;charset=utf-8");
		response.setCharacterEncoding("utf-8");
		
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
			map.put(Parameter.MSG, "用户不在工程中");
			map.put(Parameter.CODE, Parameter.FALSE);
			data.put("permission", Parameter.FALSE);
			map.put(Parameter.DATA, data);
			response.getWriter().print(gson.toJson(map));
			return;
		}
		
		List valuations = valuationService.getValuationByProjectNameLimit(projectName, offset, limit);
		ObjectUtil objectUtil = new ObjectUtil();
		List list = new ArrayList();
		for (int i = 0; i < valuations.size(); i++) {
			Map valuationMap = new HashMap();
			Valuation valuation = (Valuation) valuations.get(i);
			valuationMap = objectUtil.convertBean(valuation);
			List valuationReports = valuationReportService.getValuationReportByValuationId(valuation.getValuationId());
			if (valuationReports == null) {
				valuationMap.put("valuationReports", new ArrayList());
			} else {
				valuationMap.put("valuationReports", valuationReports);
			}
			long endDate = Long.parseLong(valuation.getDate()) + 86400000;
//			long endDate = startDate + 86400000;
			ProjectCost projectCost;
			List projectCostList = projectCostService.getProjectCostsByProjectNameAndDate(projectName, valuation.getDate(), String.valueOf(endDate));
			if(projectCostList.size()>0){
				projectCost = (ProjectCost) projectCostList.get(0);
				valuationMap.put("projectCost", projectCost.getTodayCost());
			} else {
				projectCost = null;
				valuationMap.put("projectCost", null);
			}
			
			
			list.add(valuationMap);
		}
		
		data.put("valuations", list);
		map.put(Parameter.MSG, "查询成功");
		map.put(Parameter.CODE, Parameter.SUCCESS);
		map.put(Parameter.DATA, data);
		response.getWriter().print(gson.toJson(map));
	}
	
	@RequestMapping(params = "method=analyzes")
	public void analyze(
			@RequestParam("projectName") String projectName,
			@RequestParam("startDate") String startDate,
			@RequestParam("endDate") String endDate,
			HttpServletResponse response, 
			HttpServletRequest request) throws IOException, IntrospectionException, IllegalAccessException, InvocationTargetException{
		
		Map<String, Object> map = new HashMap<String, Object>();
		Map<String, Object> data = new HashMap<String, Object>();	
		Gson gson = new Gson();
		response.setContentType("text/html;charset=utf-8");
		response.setCharacterEncoding("utf-8");
		
		List valuations = valuationService.getValuationByProjectNameAndDate(projectName, startDate, endDate);
		List projectCosts = projectCostService.getProjectCostsByProjectNameAndDate(projectName, startDate, endDate);
		
		Project project = projectService.getProjectByName(projectName);
		Map projectMap = new HashMap<String, Object>();
		projectMap.put("date", project.getDate());
		projectMap.put("expectEndDate", project.getExpectEndDate());
		projectMap.put("expectProfit", project.getExpectProfit());
		projectMap.put("allCost", project.getAllCost());
		projectMap.put("allValuation", project.getAllValuation());
		projectMap.put("cut", project.getCut());
		Valuation todayValuation = (Valuation) valuationService.getMaxValuationByProjectName(projectName);
		data.put("valuations", valuations);
		data.put("projectCosts", projectCosts);
		
		data.put("project", projectMap);
		data.put("todayValuation", todayValuation);
		map.put(Parameter.MSG, "查询成功");
		map.put(Parameter.CODE, Parameter.SUCCESS);
		map.put(Parameter.DATA, data);
		response.getWriter().print(gson.toJson(map));
	}
	
	
	@RequestMapping(params = "method=saveValuationReport")
	public void saveValuationReport(
			@RequestParam("user") String userJson, 
			@RequestParam("ValuationReport") String valuationReportJson,
			HttpServletResponse response, 
			HttpServletRequest request) throws IOException{
		
		Map<String, Object> map = new HashMap<String, Object>();
		Map<String, Object> data = new HashMap<String, Object>();	
		Gson gson = new Gson();
		response.setContentType("text/html;charset=utf-8");
		response.setCharacterEncoding("utf-8");
		
		ValuationReport valuationReport = new ValuationReport();
		User user = new User();
		try {
			user = gson.fromJson(userJson, User.class);
			valuationReport = gson.fromJson(valuationReportJson, ValuationReport.class);
		} catch (Exception e) {
			// TODO: handle exception
			map.put(Parameter.MSG, "参数错误");
			map.put(Parameter.CODE, Parameter.FALSE);
			response.getWriter().print(gson.toJson(map));
			return;
		}
		
		if (valuationReportService.getValuationReportByValuationIdAndName(valuationReport.getValuationId(), valuationReport.getName()).size() > 0) {
			map.put(Parameter.MSG, "该估值项已经存在");
			map.put(Parameter.CODE, Parameter.FALSE);
			response.getWriter().print(gson.toJson(map));
			return;
		}
		if (valuationReport.getPrice()!=null && !valuationReport.getPrice().equals("")) {
			valuationReport.setPriceUser(user.getName());
			valuationReport.setPriceUserTruename(user.getTruename());
			valuationReport.setPriceDate(String.valueOf(new Date().getTime()));
		} else {
			valuationReport.setPrice("");
		}
		if (valuationReport.getTotal()!=null && !valuationReport.getTotal().equals("")) {
			valuationReport.setTotalUser(user.getName());
			valuationReport.setTotalUserTruename(user.getTruename());
			valuationReport.setTotalDate(String.valueOf(new Date().getTime()));
		} else {
			valuationReport.setTotal("");
		}
		valuationReport.setCost("0.0");
		valuationReportService.save(valuationReport);
		
		if (valuationReport.getPrice()!=null && !valuationReport.getPrice().equals("") && valuationReport.getTotal()!=null && !valuationReport.getTotal().equals("")) {
			valuationReport.setCost(String.valueOf(Double.valueOf(valuationReport.getPrice()) * Double.valueOf(valuationReport.getTotal())));
			valuationReportService.update(valuationReport);
			Valuation valuation = valuationService.getValuationByValuationId(valuationReport.getValuationId());
			List valuationReports = valuationReportService.getValuationReportByValuationId(valuationReport.getValuationId());
			Double cost = 0.0;
			for (int i = 0; i < valuationReports.size(); i++) {
				ValuationReport vr = (ValuationReport) valuationReports.get(i);
				cost = cost + Double.valueOf(vr.getCost());
			}
			valuation.setTodayCost(cost);
			valuationService.update(valuation);
		}
		
		
		data.put("valuationReport", valuationReport);
		map.put(Parameter.MSG, "保存成功");
		map.put(Parameter.CODE, Parameter.SUCCESS);
		map.put(Parameter.DATA, data);
		response.getWriter().print(gson.toJson(map));
	}
	
	@RequestMapping(params = "method=updateValuationReport")
	public void updateValuationReport(
			@RequestParam("user") String userJson, 
			@RequestParam("ValuationReport") String valuationReportJson,
			HttpServletResponse response, 
			HttpServletRequest request) throws IOException, IntrospectionException, IllegalAccessException, InvocationTargetException{
		
		Map<String, Object> map = new HashMap<String, Object>();
		Map<String, Object> data = new HashMap<String, Object>();	
		Gson gson = new Gson();
		response.setContentType("text/html;charset=utf-8");
		response.setCharacterEncoding("utf-8");
		
		ValuationReport valuationReport = new ValuationReport();
		User user = new User();
		try {
			user = gson.fromJson(userJson, User.class);
			valuationReport = gson.fromJson(valuationReportJson, ValuationReport.class);
		} catch (Exception e) {
			// TODO: handle exception
			map.put(Parameter.MSG, "参数错误");
			map.put(Parameter.CODE, Parameter.FALSE);
			response.getWriter().print(gson.toJson(map));
			return;
		}
		Valuation valuation = valuationService.getValuationByValuationId(valuationReport.getValuationId());
		long valuationDate = Long.valueOf(valuation.getDate())+(1000*60*60*24);
		if (valuationDate < new Date().getTime()) {
			map.put(Parameter.MSG, "不是在本日，无法操作");
			map.put(Parameter.CODE, Parameter.FALSE);
			response.getWriter().print(gson.toJson(map));
			return;
		}
		ValuationReport tempValuationReport = valuationReportService.getValuationReportById(Integer.valueOf(valuationReport.getId()));
		
		if (valuationReport.getPrice()!=null && !valuationReport.getPrice().equals("")) {
			tempValuationReport.setPrice(valuationReport.getPrice());
			tempValuationReport.setPriceUser(user.getName());
			tempValuationReport.setPriceUserTruename(user.getTruename());
			tempValuationReport.setPriceDate(String.valueOf(new Date().getTime()));
		}
		if (valuationReport.getTotal()!=null && !valuationReport.getTotal().equals("")) {
			tempValuationReport.setTotal(valuationReport.getTotal());
			tempValuationReport.setTotalUser(user.getName());
			tempValuationReport.setTotalUserTruename(user.getTruename());
			tempValuationReport.setTotalDate(String.valueOf(new Date().getTime()));
		}
		
		if (valuationReport.getRemark()!=null && !valuationReport.getRemark().equals("")) {
			tempValuationReport.setRemark(valuationReport.getRemark());
		} 
		
		valuationReportService.update(tempValuationReport);
		if (tempValuationReport.getPrice()!=null && !tempValuationReport.getPrice().equals("") && tempValuationReport.getTotal()!=null && !tempValuationReport.getTotal().equals("")) {
			tempValuationReport.setCost(String.valueOf(Double.valueOf(tempValuationReport.getPrice()) * Double.valueOf(tempValuationReport.getTotal())));
			valuationReportService.update(tempValuationReport);
			
			List valuationReports = valuationReportService.getValuationReportByValuationId(valuationReport.getValuationId());
			Double cost = 0.0;
			for (int i = 0; i < valuationReports.size(); i++) {
				ValuationReport vr = (ValuationReport) valuationReports.get(i);
				if (vr.getCost() != null && !vr.getCost().equals("0.0")) {
					cost = cost + Double.valueOf(vr.getCost());
				}
			}
			valuation.setTodayCost(cost);
			valuationService.update(valuation);
		}
		ObjectUtil objectUtil = new ObjectUtil();
		Map dataMap = objectUtil.convertBean(tempValuationReport);
		dataMap.put("valuationCost", valuation.getTodayCost());
		data.put("valuationReport", dataMap);
		map.put(Parameter.DATA, data);
		map.put(Parameter.MSG, "保存成功");
		map.put(Parameter.CODE, Parameter.SUCCESS);
		response.getWriter().print(gson.toJson(map));
	}
	
	
	@RequestMapping(params = "method=deleteValuationReport")
	public void deleteValuation(
			@RequestParam("user") String userJson, 
			@RequestParam("ValuationReport") String valuationReportJson,
			HttpServletResponse response, 
			HttpServletRequest request) throws IOException{
		
		Map<String, Object> map = new HashMap<String, Object>();
		Map<String, Object> data = new HashMap<String, Object>();	
		Gson gson = new Gson();
		response.setContentType("text/html;charset=utf-8");
		response.setCharacterEncoding("utf-8");
		
		ValuationReport valuationReport = new ValuationReport();
		User user = new User();
		try {
			user = gson.fromJson(userJson, User.class);
			valuationReport = gson.fromJson(valuationReportJson, ValuationReport.class);
		} catch (Exception e) {
			// TODO: handle exception
			map.put(Parameter.MSG, "参数错误");
			map.put(Parameter.CODE, Parameter.FALSE);
			response.getWriter().print(gson.toJson(map));
			return;
		}
		
		ValuationReport tempValuationReport = valuationReportService.getValuationReportById(Integer.valueOf(valuationReport.getId()));
		Valuation valuation = valuationService.getValuationByValuationId(tempValuationReport.getValuationId());
		if (tempValuationReport!=null && valuation!=null) {
			valuationReportService.delete(tempValuationReport);
			Double cost = valuation.getTodayCost();
			cost = cost - Double.valueOf(tempValuationReport.getCost());
			valuation.setTodayCost(cost);
			valuationService.update(valuation);
		} else {
			map.put(Parameter.MSG, "估值项不存在");
			map.put(Parameter.CODE, Parameter.FALSE);
			response.getWriter().print(gson.toJson(map));
			return;
		}
		
		map.put(Parameter.MSG, "删除成功");
		map.put(Parameter.CODE, Parameter.SUCCESS);
		response.getWriter().print(gson.toJson(map));
	}
}
