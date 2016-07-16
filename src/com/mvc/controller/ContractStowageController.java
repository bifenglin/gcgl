package com.mvc.controller;

import java.beans.IntrospectionException;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.google.gson.Gson;
import com.mvc.common.ContractStowageEnum;
import com.mvc.common.Parameter;
import com.mvc.common.TypeEnum;
import com.mvc.entity.Contract;
import com.mvc.entity.ContractStowage;
import com.mvc.entity.ContractStowageScore;
import com.mvc.entity.User;
import com.mvc.service.ContractServic;
import com.mvc.service.ContractStowageScoreService;
import com.mvc.service.ContractStowageService;
import com.mvc.service.ProjectLogService;
import com.mvc.service.ProjectService;
import com.mvc.util.ObjectUtil;

@Controller
@RequestMapping("/contractStowage.do")
public class ContractStowageController {
	protected final transient Log log = LogFactory
	.getLog(StudentController.class);
	
	@Autowired
	@Qualifier("ProjectServiceImpl")
	private ProjectService projectService;
	
	@Autowired
	@Qualifier("ProjectLogServiceImpl")
	private ProjectLogService projectLogService;
	
	@Autowired
	@Qualifier("ContractServiceImpl")
	private ContractServic contractServic;
	
	@Autowired
	@Qualifier("ContractStowageServiceImpl")
	private ContractStowageService contractStowageService;
	
	@Autowired
	@Qualifier("ContractStowageScoreServiceImpl")
	private ContractStowageScoreService contractStowageScoreService;
	
	@RequestMapping(params = "method=load")
	public void loadOn(
			@RequestParam("soreType") String soreType,
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
		
		List<Object> onlist = new ArrayList<Object>();
		ContractStowage contractStowage = new ContractStowage();
		if (soreType.equals(ContractStowageEnum.ALLSOCRE.getKey()) ||
				soreType.equals(ContractStowageEnum.ALLAVERAGE.getKey()) ||
				soreType.equals(ContractStowageEnum.ALLFUND.getKey()) || 
				soreType.equals(ContractStowageEnum.ALLMANAGE.getKey()) || 
				soreType.equals(ContractStowageEnum.ALLTECHNOLOGY.getKey()) ||
				soreType.equals(ContractStowageEnum.DATE.getKey()) ||
				soreType.equals(ContractStowageEnum.TIMES.getKey()) ) {
			onlist = contractStowageService.getContractStowagesOnByAndType(soreType, type,offset, limit);
//			List offList = contractStowageService.getContractStowagesOffByAndType(soreType, type,offset, limit);
			ObjectUtil objectUtil = new ObjectUtil();
			List<Object> list = new ArrayList<Object>();
			for(int i=0; i<onlist.size() ; i++){
				ContractStowage tempContractStowage = (ContractStowage) onlist.get(i);
				Map<String, Object> contracStowageMap = objectUtil.convertBean(tempContractStowage);
				List<Object> tempList= contractStowageScoreService.getContracStowageScoresByContractStowageIdLimit(String.valueOf(tempContractStowage.getId()), 0, 1);
				if (tempList.size() >0) {
					ContractStowageScore tempContractStowageScore = (ContractStowageScore) tempList.get(0);
					contracStowageMap.put("contractStowageScoreDate", tempContractStowageScore.getDate());
					contracStowageMap.put("contractStowageScoreUserName", tempContractStowageScore.getUserName());
					contracStowageMap.put("contractStowageScoreUserTruename", tempContractStowageScore.getUserTruename());
					contracStowageMap.put("contractStowageScoreComment", tempContractStowageScore.getComment());
				}
				list.add(contracStowageMap);
			}
			
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
			data.put("count", countMap);
			data.put("contractStowages", list);
//			data.put("contractStowagesOff", offList);
			map.put(Parameter.MSG, "查询成功");
			map.put(Parameter.CODE, Parameter.SUCCESS);
			map.put(Parameter.DATA, data);
			response.getWriter().print(gson.toJson(map));
		} else {
			map.put(Parameter.MSG, "参数错误");
			map.put(Parameter.CODE, Parameter.FALSE);
			response.getWriter().print(gson.toJson(map));
		}
	}
	@RequestMapping(params = "method=loadOff")
	public void loadOff(
			@RequestParam("soreType") String soreType,
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
		
		List<Object> offlist = new ArrayList<Object>();
		ContractStowage contractStowage = new ContractStowage();
		if (soreType.equals(ContractStowageEnum.ALLSOCRE.getKey()) ||
				soreType.equals(ContractStowageEnum.ALLAVERAGE.getKey()) ||
				soreType.equals(ContractStowageEnum.ALLFUND.getKey()) || 
				soreType.equals(ContractStowageEnum.ALLMANAGE.getKey()) || 
				soreType.equals(ContractStowageEnum.ALLTECHNOLOGY.getKey()) ||
				soreType.equals(ContractStowageEnum.DATE.getKey()) ||
				soreType.equals(ContractStowageEnum.TIMES.getKey()) ) {
			offlist = contractStowageService.getContractStowagesOffByAndType(soreType, type,offset, limit);
//			List offList = contractStowageService.getContractStowagesOffByAndType(soreType, type,offset, limit);
			Map countMap = new HashMap<String, Object>();
			Integer count = contractStowageService.getContractStowagesCountByTypeAndStatus(TypeEnum.LABOUR.getKey(), "1");
			countMap.put("labourOn", count);
			count = contractStowageService.getContractStowagesCountByTypeAndStatus(TypeEnum.LABOUR.getKey(), "0");
			countMap.put("labourOff", count);
			count = contractStowageService.getContractStowagesCountByTypeAndStatus(TypeEnum.MACHINE.getKey(), "1");
			countMap.put("machineOn", count);
			count = contractStowageService.getContractStowagesCountByTypeAndStatus(TypeEnum.MACHINE.getKey(), "0");
			countMap.put("machineOff", count);
			count = contractStowageService.getContractStowagesCountByTypeAndStatus(TypeEnum.MATERIAL.getKey(), "1");
			countMap.put("materialOn", count);
			count = contractStowageService.getContractStowagesCountByTypeAndStatus(TypeEnum.MATERIAL.getKey(), "0");
			countMap.put("materialOff", count);
			count = contractStowageService.getContractStowagesCountByTypeAndStatus(TypeEnum.OTHER.getKey(), "1");
			countMap.put("otherOn", count);
			count = contractStowageService.getContractStowagesCountByTypeAndStatus(TypeEnum.MACHINE.getKey(), "0");
			countMap.put("otherOff", count);
			data.put("count", countMap);
			data.put("contractStowages", offlist);
//			data.put("contractStowagesOff", offList);
			map.put(Parameter.MSG, "查询成功");
			map.put(Parameter.CODE, Parameter.SUCCESS);
			map.put(Parameter.DATA, data);
			response.getWriter().print(gson.toJson(map));
		} else {
			map.put(Parameter.MSG, "参数错误");
			map.put(Parameter.CODE, Parameter.FALSE);
			response.getWriter().print(gson.toJson(map));
		}
	}
	@RequestMapping(params = "method=getScore")
	public void getContractScore(
			@RequestParam("scId") Integer scId,
			@RequestParam("offset") Integer offset,
			@RequestParam("limit") Integer limit,
			HttpServletResponse response, 
			HttpServletRequest request) throws IOException, IntrospectionException, IllegalAccessException, InvocationTargetException{
		
		Map<String, Object> map = new HashMap<String, Object>();
		Gson gson = new Gson();
		response.setContentType("text/html;charset=utf-8");
		response.setCharacterEncoding("utf-8");
		Map<String, Object> data = new HashMap<String, Object>();	
		
		List list =  contractStowageService.getContractStowagesOnScoreByScId(scId, offset, limit);
		
		map.put(Parameter.MSG, "查询成功");
		map.put(Parameter.CODE, Parameter.SUCCESS);
		data.put("list", list);
		map.put(Parameter.DATA, data);
		response.getWriter().print(gson.toJson(map));

	}
	@RequestMapping(params = "method=searchContractStowage")
	public void searchContractStowage(
			@RequestParam("key") String key,
			@RequestParam("type") String type,
			@RequestParam("fuck") String status,
			HttpServletResponse response, 
			HttpServletRequest request) throws IOException, IntrospectionException, IllegalAccessException, InvocationTargetException{
		
		Map<String, Object> map = new HashMap<String, Object>();
		Gson gson = new Gson();
		response.setContentType("text/html;charset=utf-8");
		response.setCharacterEncoding("utf-8");
		Map<String, Object> data = new HashMap<String, Object>();	
		
		if (key.equals("") || key == null) {
			map.put(Parameter.MSG, "请填写关键字");
			map.put(Parameter.CODE, Parameter.FALSE);
			response.getWriter().print(gson.toJson(map));
		}
		
		List list;
		if (type.equals("all")) {
			list = contractStowageService.getContractStowageLikeByKeyAndStatus(key, status);
		} else {
			list = contractStowageService.getContractStowageLikeByKeyAndStatusAndType(key, status, type);
		}
		
		map.put(Parameter.MSG, "查询成功");
		map.put(Parameter.CODE, Parameter.SUCCESS);
		data.put("list", list);
		map.put(Parameter.DATA, data);
		response.getWriter().print(gson.toJson(map));

	}
}