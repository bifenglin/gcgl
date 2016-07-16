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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.google.gson.Gson;
import com.mvc.common.Parameter;
import com.mvc.entity.Contract;
import com.mvc.entity.ContractStowage;
import com.mvc.entity.ContractStowageScore;
import com.mvc.entity.User;
import com.mvc.service.ContractStowageScoreService;
import com.mvc.service.ContractStowageService;
import com.mvc.service.ProjectService;

@Controller
@RequestMapping("/contractStowageScore.do")
public class ContractStowageScoreController {
	protected final transient Log log = LogFactory
	.getLog(StudentController.class);
	
	@Autowired
	@Qualifier("ContractStowageServiceImpl")
	private ContractStowageService contractStowageService;
	
	@Autowired
	@Qualifier("ContractStowageScoreServiceImpl")
	private ContractStowageScoreService contractStowageScoreService;
	
	@RequestMapping(params = "method=save")
	public void save(
			@RequestParam("user") String userJson,
			@RequestParam("contractStowageScore") String contractStowageScoreJson, 
			HttpServletResponse response, 
			HttpServletRequest request) throws IOException, IntrospectionException, IllegalAccessException, InvocationTargetException{
		
		Map<String, Object> map = new HashMap<String, Object>();
		Gson gson = new Gson();
		response.setContentType("text/html;charset=utf-8");
		response.setCharacterEncoding("utf-8");
		Map<String, Object> data = new HashMap<String, Object>();	
		
		ContractStowageScore contractStowageScore = new ContractStowageScore();
		User user = new User();
		try {
			contractStowageScore = gson.fromJson(contractStowageScoreJson, ContractStowageScore.class);
			user = gson.fromJson(userJson, User.class);
		} catch (Exception e) {
			// TODO: handle exception
			map.put(Parameter.MSG, "参数错误");
			map.put(Parameter.CODE, Parameter.FALSE);
			response.getWriter().print(gson.toJson(map));
			return;
		}
		
		ContractStowage contractStowage = contractStowageService.getContractStowageById(Integer.valueOf(contractStowageScore.getContractStowageId()));
		if (contractStowage==null) {
			map.put(Parameter.MSG, "供方不存在");
			map.put(Parameter.CODE, Parameter.FALSE);
			response.getWriter().print(gson.toJson(map));
			return;
		}
		if (contractStowageScore.getComment().equals("") || contractStowageScore.getComment() == null) {
			map.put(Parameter.MSG, "评论不能为空");
			map.put(Parameter.CODE, Parameter.FALSE);
			response.getWriter().print(gson.toJson(map));
			return;
		}
		
		if (contractStowage != null) {
			Double allFund = (Double.valueOf(contractStowage.getAllFund())+Double.valueOf(contractStowageScore.getFund()))/2.0;
			contractStowage.setAllFund(String.valueOf(allFund));
			Double allManage = (Double.valueOf(contractStowage.getAllManage())+Double.valueOf(contractStowageScore.getManage()))/2.0;
			contractStowage.setAllManage(String.valueOf(allManage));
			Double allTechnology = (Double.valueOf(contractStowage.getAllTechnology())+Double.valueOf(contractStowageScore.getTechnology()))/2.0;
			contractStowage.setAllTechnology(String.valueOf(allTechnology));
			Double allSorce = Double.valueOf(contractStowage.getAllScore())+allFund + allManage + allTechnology;
			contractStowage.setAllScore(String.valueOf(allSorce));
			Integer times = Integer.valueOf(contractStowage.getTimes())+1;
			contractStowage.setTimes(String.valueOf(times));
			Double allAverage = Double.valueOf(contractStowage.getAllAverage());
			allAverage = (allSorce/Double.valueOf(times));
			DecimalFormat df = new DecimalFormat("######0.0");   
			contractStowage.setAllAverage(String.valueOf(df.format(allAverage)));
			contractStowageService.update(contractStowage);
		} 
		
		contractStowageScore.setDate(String.valueOf(new Date().getTime()));
		contractStowageScoreService.save(contractStowageScore);
		
		
		map.put(Parameter.MSG, "添加成功");
		map.put(Parameter.CODE, Parameter.SUCCESS);
		data.put("contractStowageScore", contractStowageScore);
		map.put(Parameter.DATA, data);
		response.getWriter().print(gson.toJson(map));
	}
	
	@RequestMapping(params = "method=get")
	public void get(
			@RequestParam("user") String userJson,
			@RequestParam("contractStowageName") String contractStowageName, 
			@RequestParam("type") String type, 
			@RequestParam("offset") Integer offset,
			@RequestParam("limit") Integer limit,
			HttpServletResponse response, 
			HttpServletRequest request) throws IOException, IntrospectionException, IllegalAccessException, InvocationTargetException{
		
		Map<String, Object> map = new HashMap<String, Object>();
		Gson gson = new Gson();
		response.setContentType("text/html;charset=utf-8");
		response.setCharacterEncoding("utf-8");
		Map<String, Object> data = new HashMap<String, Object>();	
		
		ContractStowage contractStowage = contractStowageService.getContractStowageByNameAndType(contractStowageName, type);
		if (contractStowage == null) {
			map.put(Parameter.MSG, "供方不存在");
			map.put(Parameter.CODE, Parameter.FALSE);
			response.getWriter().print(gson.toJson(map));
			return;
		}
		List<Object> contractStowages = contractStowageScoreService.getContracStowageScoresByContractStowageIdLimit(String.valueOf(contractStowage.getId()), offset, limit);
		map.put(Parameter.MSG, "查询成功");
		map.put(Parameter.CODE, Parameter.SUCCESS);
		data.put("contractStowageScores", contractStowages);
		map.put(Parameter.DATA, data);
		response.getWriter().print(gson.toJson(map));
	}
		
	
}
