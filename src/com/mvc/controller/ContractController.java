package com.mvc.controller;

import java.beans.IntrospectionException;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.text.DecimalFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javassist.expr.NewArray;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.crypto.Data;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.google.gson.Gson;
import com.mvc.common.Parameter;
import com.mvc.common.PermissionEnum;
import com.mvc.entity.Contract;
import com.mvc.entity.ContractStowage;
import com.mvc.entity.ContractStowageScore;
import com.mvc.entity.Project;
import com.mvc.entity.ProjectLog;
import com.mvc.entity.Property;
import com.mvc.entity.Report;
import com.mvc.entity.User;
import com.mvc.service.ContractServic;
import com.mvc.service.ContractStowageService;
import com.mvc.service.ProjectLogService;
import com.mvc.service.ProjectService;
import com.mvc.util.ObjectUtil;


@Controller
@RequestMapping("/contract.do")
public class ContractController {
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
	
	@RequestMapping(params = "method=save")
	public void saveContract(
			@RequestParam("user") String userJson,
			@RequestParam("permission") String permission,
			@RequestParam("contract") String contractJson, 
			@RequestParam("projectName") String projectName,
			HttpServletResponse response, 
			HttpServletRequest request) throws IOException, IntrospectionException, IllegalAccessException, InvocationTargetException{
		
		Map<String, Object> map = new HashMap<String, Object>();
		Gson gson = new Gson();
		response.setContentType("text/html;charset=utf-8");
		response.setCharacterEncoding("utf-8");
		Map<String, Object> data = new HashMap<String, Object>();	
		
		Contract contract = new Contract();
		User user = new User();
		try {
			contract = gson.fromJson(contractJson, Contract.class);
			user = gson.fromJson(userJson, User.class);
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
		
		
		if (contract.getName() == null || contract.getCompany() == null || contract.getName()=="" || contract.getCompany()=="") {
			map.put(Parameter.MSG, "名称和全称不能为空");
			map.put(Parameter.CODE, Parameter.FALSE);
			response.getWriter().print(gson.toJson(map));
			return;
		}
		
		if (contract.getType() == null || contract.getType() == "") {
			map.put(Parameter.MSG, "type不能为空");
			map.put(Parameter.CODE, Parameter.FALSE);
			response.getWriter().print(gson.toJson(map));
			return;
		}
		if (contractServic.getContractByTypeAndContractNameAndProjectName(contract.getType(), contract.getName(), projectName) != null) {
			map.put(Parameter.MSG, "该合同已存在");
			map.put(Parameter.CODE, Parameter.FALSE);
			response.getWriter().print(gson.toJson(map));
			return;
		}
		
		
		String contractId;
		StringBuffer sff = new StringBuffer();
		
		Project project = projectService.getProjectByName(projectName);
		List<Object> list = contractServic.getContractsOnByProjectName(projectName);
		if (list.size() == 0 || list == null) {
			contractId = "H1";
		} else {
			Contract tempContract = (Contract) list.get(list.size()-1);
			Integer index = tempContract.getContractId().indexOf("H")+1;
			String tempContractId = tempContract.getContractId().substring(index);
			Integer trueId = Integer.valueOf(tempContractId)+1;
			sff.append(tempContract.getContractId().substring(0, index)).append(String.valueOf(trueId));
			contractId = sff.toString();
		}
		
		if (contractStowageService.isContractStowageOffByName(contract.getName(), contract.getCompany())) {
			map.put(Parameter.MSG, "添加失败，该合同在黑名单");
			map.put(Parameter.CODE, Parameter.FALSE);
			response.getWriter().print(gson.toJson(map));
			return;
		}
		
		contract.setContractId(contractId);
		contract.setDate(String.valueOf(new Date().getTime()));
		contract.setStatus("1");
		contract.setProjectId(String.valueOf(project.getId()));
		ContractStowage contractStowage = contractStowageService.getContractStowageOnByNameAndType(contract.getName(), contract.getType());
		
		if (contractStowage == null) {
			contractStowage = new ContractStowage();
			contractStowage.setAllAverage("0.0");
			contractStowage.setAllFund("0.0");
			contractStowage.setAllManage("0.0");
			contractStowage.setAllTechnology("0.0");
			contractStowage.setAllScore("0.0");
			contractStowage.setType(contract.getType());
			contractStowage.setCompany(null);
			contractStowage.setDate(String.valueOf(new Date().getTime()));
			contractStowage.setName(contract.getName());
			contractStowage.setTimes("0");
			contractStowage.setRemark(contract.getRemark());
			contractStowage.setStatus("1");
			contractStowageService.save(contractStowage);
//			data.put("contractStowage", );
		} 
		
		ProjectLog projectLog = new ProjectLog();
		projectLog.setCause("saveContract");
		projectLog.setContractId(contractId);
		projectLog.setDate(String.valueOf(new Date().getTime()));
		projectLog.setUserName(user.getName());
		projectLog.setUserTruename(user.getTruename());
		projectLog.setProjectName(contract.getProjectName());
		
		StringBuffer sb = new StringBuffer();
		sb.append(user.getTruename()).append(":").append("创建了").append(contractId).append("编号").append(contract.getName()).append("合同。");
		projectLog.setLogString(sb.toString());
		
		ObjectUtil objectUtil = new ObjectUtil();
//		ContractStowage contractStowage = contractStowageService.getContractStowageOnByNameAndType(contract.getName(), contract.getType());
		Map<String, Object> contractMap = objectUtil.convertBean(contract);
		contractMap.put("contractStowageId", contractStowage.getId());
		projectLogService.save(projectLog);
		contractServic.save(contract);
		
		map.put(Parameter.MSG, "添加成功");
		map.put(Parameter.CODE, Parameter.SUCCESS);
//		data.put("contract", contractMap);
		map.put(Parameter.DATA, contractMap);
		response.getWriter().print(gson.toJson(map));
	}
	
	@RequestMapping(params = "method=update")
	public void updateContract(
			@RequestParam("user") String userJson, 
			@RequestParam("permission") String permission, 
			@RequestParam("contract") String contractJson, 
			HttpServletResponse response, 
			HttpServletRequest request) throws IOException, IntrospectionException, IllegalAccessException, InvocationTargetException{
		
		Map<String, Object> map = new HashMap<String, Object>();
		Gson gson = new Gson();
		response.setContentType("text/html;charset=utf-8");
		response.setCharacterEncoding("utf-8");
		Map<String, Object> data = new HashMap<String, Object>();	
		
		Contract contract = new Contract();
		User user = new User();
		try {
			contract = gson.fromJson(contractJson, Contract.class);
			user = gson.fromJson(userJson, User.class);
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
		
		Contract contractTemp;
		
		if ((contractTemp = contractServic.getContractByContractIdAndProjectName(contract.getContractId(), contract.getProjectName())) != null) {
			contractTemp.setCompany(contract.getCompany());
			contractTemp.setName(contract.getName());
			contractTemp.setRemark(contract.getRemark());
			contractServic.update(contractTemp);
		} else {
			map.put(Parameter.MSG, "合同不存在");
			map.put(Parameter.CODE, Parameter.FALSE);
			response.getWriter().print(gson.toJson(map));
			return;
		}
		
		ProjectLog projectLog = new ProjectLog();
		projectLog.setCause("updateContract");
		projectLog.setContractId(contract.getContractId());
		projectLog.setDate(String.valueOf(new Date().getTime()));
		projectLog.setUserName(user.getName());
		projectLog.setUserTruename(user.getTruename());
		projectLog.setProjectName(contract.getProjectName());
		StringBuffer sb = new StringBuffer();
		sb.append(user.getTruename()).append(":").append("修改了").append(contract.getContractId()).append("编号").append(contract.getName()).append("合同。");
		projectLog.setLogString(sb.toString());
		projectLogService.save(projectLog);
		
		map.put(Parameter.DATA, contractTemp);
		map.put(Parameter.MSG, "更改成功");
		map.put(Parameter.CODE, Parameter.SUCCESS);
		response.getWriter().print(gson.toJson(map));
	}
	

	@RequestMapping(params = "method=delete")
	public void deleteContract(
			@RequestParam("user") String userJson,
			@RequestParam("contract") String contractJson, 
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
		Contract contract = new Contract();
		try {
			user = gson.fromJson(userJson, User.class);
			contract = gson.fromJson(contractJson, Contract.class);
		} catch (Exception e) {
			// TODO: handle exception
			map.put(Parameter.MSG, "参数错误");
			map.put(Parameter.CODE, Parameter.FALSE);
			e.printStackTrace();
			response.getWriter().print(gson.toJson(map));
			return;
		}
		if (!permission.equals(PermissionEnum.ADMIN.getValue()) &&  !permission.equals(PermissionEnum.CREATER.getValue())) {
			map.put(Parameter.MSG, "权限不足");
			map.put(Parameter.CODE, Parameter.FALSE);
			response.getWriter().print(gson.toJson(map));
			return;
		}
		
		Contract tempContract;
		if ((tempContract = contractServic.getContractByContractIdAndProjectName(contract.getContractId(),contract.getProjectName())) != null) {
			contract.setStatus("0");
			contractServic.delete(tempContract);
		} else {
			map.put(Parameter.MSG, "合同不存在");
			map.put(Parameter.CODE, Parameter.FALSE);
			response.getWriter().print(gson.toJson(map));
			return;
		}
		
		
		ProjectLog projectLog = new ProjectLog();
		projectLog.setCause("deleteContract");
		projectLog.setContractId(contract.getContractId());
		projectLog.setDate(String.valueOf(new Date().getTime()));
		projectLog.setUserName(user.getName());
		projectLog.setProjectName(contract.getProjectName());
		StringBuffer sb = new StringBuffer();
		sb.append(user.getTruename()).append(":").append("删除了").append(contract.getContractId()).append("编号").append(contract.getName()).append("合同。");
		projectLog.setLogString(sb.toString());
		projectLogService.save(projectLog);
		
		
		map.put(Parameter.MSG, "删除成功");
		map.put(Parameter.CODE, Parameter.SUCCESS);
		response.getWriter().print(gson.toJson(map));
	}
	
}
