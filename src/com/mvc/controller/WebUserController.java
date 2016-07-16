package com.mvc.controller;

import java.io.OutputStream;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javassist.expr.NewArray;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFPrintSetup;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.CellRangeAddress;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.hssf.util.Region;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.PrintSetup;
import org.apache.poi.ss.usermodel.Row;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.mvc.common.Parameter;
import com.mvc.common.TypeEnum;
import com.mvc.entity.Contract;
import com.mvc.entity.CostLog;
import com.mvc.entity.Project;
import com.mvc.entity.ProjectLog;
import com.mvc.entity.Property;
import com.mvc.entity.Report;
import com.mvc.entity.Rtp;
import com.mvc.entity.User;
import com.mvc.entity.Utp;
import com.mvc.entity.ValuationReport;
import com.mvc.service.ContractServic;
import com.mvc.service.CostLogService;
import com.mvc.service.ProjectLogService;
import com.mvc.service.ProjectService;
import com.mvc.service.PropertyService;
import com.mvc.service.ReportService;
import com.mvc.service.RtpService;
import com.mvc.service.UserService;
import com.mvc.service.UtpService;
import com.mvc.service.ValuationReportService;
import com.mvc.service.ValuationService;
import com.mvc.util.ObjectUtil;

@Controller
@RequestMapping("/webuser.do")
public class WebUserController {
	protected final transient Log log = LogFactory.getLog(WebUserController.class);
	
	@Autowired
	@Qualifier("ValuationServiceImpl")
	private ValuationService valuationService;
	
	@Autowired
	@Qualifier("ValuationReportServiceImpl")
	private ValuationReportService valuationReportService;
	
	@Autowired
	@Qualifier("UserServiceImpl")
	private UserService userService;
	
	@Autowired
	@Qualifier("PropertyServiceImpl")
	private PropertyService propertyService;
	
	@Autowired
	@Qualifier("RtpServiceImpl")
	private RtpService rtpService;
	
	@Autowired
	@Qualifier("ReportServiceImpl")
	private ReportService reportService;
	
	@Autowired
	@Qualifier("UtpServiceImpl")
	private UtpService utpService;
	
	@Autowired
	@Qualifier("ContractServiceImpl")
	private ContractServic contractServic;
	
	@Autowired
	@Qualifier("ProjectServiceImpl")
	private ProjectService projectService;
	
	@Autowired
	@Qualifier("CostLogServiceImpl")
	private CostLogService costLogService ;
	
	@Autowired
	@Qualifier("ProjectLogServiceImpl")
	private ProjectLogService projectLogService ;
	
	@RequestMapping
	public String load(ModelMap modelMap, HttpSession session){
		User user = (User) session.getAttribute("user");
		if (user == null) {
			modelMap.put("msg", "请登录");
			return "login";
		}
		int createProjectsNum = 0;
		int joinProjectsNum = 0;
		int applyProjectsNum = 0;
		
		List projectList = projectService.getProjectsInProjectByUser(user);
		List<Object> utpList = utpService.getUtpByUser(user);
		modelMap.put("allProjectsNum", utpList.size());
		for (int i = 0; i < utpList.size(); i++) {
			Utp utp = (Utp) utpList.get(i);
			if (utp.getPermission().equals("1")) {
				createProjectsNum++;
			} else if (utp.getPermission().equals("2")) {
				joinProjectsNum++;
			} else if (utp.getPermission().equals("0")){
				applyProjectsNum++;
			}
		}
		modelMap.put("joinProjectsNum", joinProjectsNum);
		modelMap.put("applyProjectsNum", applyProjectsNum);
		modelMap.put("createProjectsNum", createProjectsNum);
		
		return "userIndex";
	}
	
	@RequestMapping(params = "method=goUserProject")
	public String goUserProject(ModelMap modelMap, HttpSession session, HttpServletRequest request){
		Integer projectId = Integer.valueOf(request.getParameter("projectId"));
		Project project ;
		if ((project = projectService.getProjectById(projectId)) != null) {
			List<Object> contracts = new ArrayList<Object>();
			contracts = contractServic.getContractsByProjectId(projectId);
			modelMap.put("contracts", contracts);
			session.setAttribute("project", project);
			List projectLogs = projectLogService.getProjectLogByProjectName(project.getName(), 0, 6);
			List reports = reportService.getReportsByProjectName(project.getName());
			modelMap.put("projectLogs", projectLogs);
			modelMap.put("reports", reports);
			return "userProject";
		} else {
			modelMap.put("msg", "异常错误");
			return "userProject";
		}
	}
	
	@RequestMapping(params = "method=goUserPrint")
	public String goUserPrint(ModelMap modelMap, HttpSession session, HttpServletRequest request){
		String contractId = request.getParameter("contractId");
		Project project = (Project) session.getAttribute("project");
		List propertys = propertyService.getPropertysByContractIdAndProjectName(contractId, project.getName());
		Contract contract = contractServic.getContractByContractIdAndProjectName(contractId, project.getName());
		ObjectUtil objectUtil = new ObjectUtil();
		Property property;
		List topRtps = new ArrayList();
		for (int i = 0; i < propertys.size(); i++) {
			property = (Property) propertys.get(i);
			Rtp topRtp = rtpService.getTopDateRtpByProjectNameAndPropertyId(project.getName(), property.getPropertyId());
			if (topRtp!=null) {
				topRtps.add(topRtp);
			}
		}
		
		System.out.println(propertys.size());
		modelMap.put("topRtps", topRtps);
		session.setAttribute("project", project);
		session.setAttribute("contract", contract);
		session.setAttribute("propertys", propertys);
		modelMap.put("msg", "");
		return "userPrint";
	}
	
	@RequestMapping(params = "method=goUserOwnReport")
	public String goUserOwnReport(ModelMap modelMap, HttpSession session, HttpServletRequest request){
		User user = (User) session.getAttribute("user");
		String id = request.getParameter("id");
//		String projectName = session.getAttribute("project");
		System.out.println("userOwnReport");
		if (id == null) {
			List reports = reportService.getReportsByUserName(user.getName());
			modelMap.put("reports", reports);
			return "userOwnReport";
		} 
		Report report = reportService.getReportById(Integer.valueOf(id));
		List rtps = rtpService.getRtpsByProjectNameAndReportId(report.getProjectName(), report.getReportId());
		reportService.delate(report);
		for (int i = 0; i < rtps.size(); i++) {
			Rtp rtp = (Rtp) rtps.get(i);
			rtpService.delete(rtp);
		}
		List reports = reportService.getReportsByUserName(user.getName());
		modelMap.put("reports", reports);
		return "userOwnReport";
	}
	
	@RequestMapping(params = "method=goUserReport")
	public String goUserReport(ModelMap modelMap, HttpSession session, HttpServletRequest request){
		String id = request.getParameter("id");
		if(id == null){
			User user = (User) session.getAttribute("user");
			Project project = (Project) session.getAttribute("project");
			Contract contract = (Contract) session.getAttribute("contract");
			List<Property> propertys = (List) session.getAttribute("propertys");
			String[] choices = request.getParameterValues("choice");
			String startDateString = request.getParameter("startDate");
			String endDateString = request.getParameter("endDate");
			Date startDate;
			Date endDate;
			try {
				 SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				 startDate = df.parse(startDateString);
				 endDate = df.parse(endDateString);
			} catch (Exception e) {
				modelMap.put("contractId", contract.getId());
				modelMap.put("msg", "日期格式错误");
				return "userPrint";
			}
			
			StringBuffer msg = new StringBuffer();
			Report report = new Report();
			Rtp rtp = new Rtp();
			report.setContractId(contract.getContractId());
			report.setProjectName(project.getName());
			String reportId;
			StringBuffer sff = new StringBuffer();
			List<Object> list = reportService.getReportsByProjectName(project.getName());
			if (list.size() == 0 || list == null) {
				reportId = contract.getContractId()+"B1";
			} else {
				Report tempReport = (Report) list.get(list.size()-1);
				Integer index = tempReport.getReportId().indexOf("B")+1;
				String tempReportId = tempReport.getReportId().substring(index);
				Integer trueId = Integer.valueOf(tempReportId)+1;
				sff.append(tempReport.getReportId().substring(0, index)).append(String.valueOf(trueId));
				reportId = sff.toString();
			}
			
			List rtps = new ArrayList();
			double allCost = 0.0;
			for (int i = 0; i < choices.length; i++) {
				double tempCost = 0.0;
				double tempTotal = 0.0;
				List tempList = costLogService.getCostLogByPropertyIdAndDate(project.getName(), choices[i], String.valueOf(startDate.getTime()), String.valueOf(endDate.getTime()));
				for (int j = 0; j < tempList.size(); j++) {
					CostLog costLog = (CostLog) tempList.get(j);
					if (costLog.getCost() == null) {
						continue;
					}
					tempTotal += Double.parseDouble(costLog.getTotal());
					tempCost += Double.parseDouble(costLog.getCost());
					allCost += Double.parseDouble(costLog.getCost());
				}
				Property property = propertyService.getPropertyByPropertyId(project.getName(), choices[i]);
				List oldRtps = rtpService.getRtpsByProjectNameAndPropertyId(project.getName(), property.getPropertyId());
				for (int j = 0; j < oldRtps.size(); j++) {
					Rtp oldRtp = (Rtp) oldRtps.get(j);
					Long oldStartDate = Long.valueOf(oldRtp.getStartDate());
					Long oldEndDate = Long.valueOf(oldRtp.getEndDate());
					if (oldStartDate > startDate.getTime() || oldEndDate > oldEndDate) {
						msg.append("编号").append(property.getPropertyId()).append(property.getName()).append("合同项结算时间重叠。");
					}
				}
				
				rtp.setPrice(property.getPrice());
				rtp.setPropertyId(choices[i]);
				rtp.setReportId(reportId);
				rtp.setProjectName(project.getName());
				rtp.setCost(String.valueOf(tempCost));
				rtp.setTotal(String.valueOf(tempTotal));
				rtp.setStartDate(String.valueOf(startDate.getTime()));
				rtp.setEndDate(String.valueOf(endDate.getTime()));
				rtp.setPropertyName(property.getName());
				rtps.add(rtp);
				rtpService.save(rtp);
			}
			
			
			report.setUserName(user.getName());
			report.setReportId(reportId);;
			report.setStartDate(String.valueOf(startDate.getTime()));
			report.setEndDate(String.valueOf(endDate.getTime()));
			report.setUserTruename(user.getTruename());
			report.setContractName(contract.getName());
			report.setContractCompany(contract.getCompany());
			report.setDate(String.valueOf(new Date().getTime()));
			report.setCost(String.valueOf(allCost));
			reportService.save(report);
			
			rtps = rtpService.getRtpsByProjectNameAndReportId(project.getName(), reportId);
			modelMap.put("msg", msg.toString());
			modelMap.put("report", report);
			modelMap.put("rtps", rtps);
		} else {
			Report report = reportService.getReportById(Integer.valueOf(id));
			List<Object> rtps = rtpService.getRtpsByProjectNameAndReportId(report.getProjectName(), report.getReportId());
			modelMap.put("msg", "");
			modelMap.put("report", report);
			modelMap.put("rtps", rtps);
		}
		return "userReport";
	}
	
	@RequestMapping(params = "method=goPrintValuation")
	public String goUserPrintValuation(ModelMap modelMap, HttpSession session, HttpServletRequest request){
		Project project = (Project) session.getAttribute("project");
		List units = valuationService.getValuationUnitByProjectName(project.getName());
		session.setAttribute("units", units);
		modelMap.put("msg", "");
		return "userPrintValuation";
	}
	
	@RequestMapping(params = "method=printLog")
	public String printLog(ModelMap modelMap, 
			HttpSession session,
			HttpServletResponse response,
			HttpServletRequest request) throws ParseException{
		String id = request.getParameter("id");
		String action = request.getParameter("action");
		Report report = reportService.getReportById(Integer.valueOf(id));
		List<Object> rtps = rtpService.getRtpsByProjectNameAndReportId(report.getProjectName(), report.getReportId());
		Contract contract = contractServic.getContractByContractIdAndProjectName(report.getContractId(), report.getProjectName());
		if (action.equals("print")) {
			Map<String, String> startMap = new HashMap<String, String>();

			String fileName = report.getProjectName()+"日志表.xls";
			String sheetName = report.getProjectName()+"日志表.xls";
			saveExcel(response,sheetName,fileName, report, rtps, contract);	
			modelMap.put("msg", "打印成功");
			modelMap.put("id", id);
			return "userReport";
		} else {
//			String contractId = request.getParameter("contractId");
			Project project = (Project) session.getAttribute("project");
			List propertys = propertyService.getPropertysByContractIdAndProjectName(contract.getContractId(), project.getName());
//			Contract contract = contractServic.getContractByContractIdAndProjectName(contractId, project.getName());
			
			reportService.delate(report);
			List deleteRtps = rtpService.getRtpsByProjectNameAndReportId(project.getName(), report.getReportId());
			for (int i = 0; i < deleteRtps.size(); i++) {
				Rtp tempRtp = (Rtp) deleteRtps.get(i);
				rtpService.delete(tempRtp);
			}
			
			ObjectUtil objectUtil = new ObjectUtil();
			Property property;
			List topRtps = new ArrayList();
			for (int i = 0; i < propertys.size(); i++) {
				property = (Property) propertys.get(i);
				Rtp topRtp = rtpService.getTopDateRtpByProjectNameAndPropertyId(project.getName(), property.getPropertyId());
				if (topRtp!=null) {
					topRtps.add(topRtp);
				}
			}
			
			modelMap.put("topRtps", topRtps);
			modelMap.put("msg","请重新选择");
//			modelMap.put("contractId", contract.getContractId());
			return "userPrint";
		}
		
	}
	
	@RequestMapping(params = "method=printTaiZhang")
	public void printTaiZhang(ModelMap modelMap, 
			HttpSession session,
			HttpServletResponse response,
			HttpServletRequest request) throws ParseException{
		String id = request.getParameter("projectId");
		Project project = projectService.getProjectById(Integer.valueOf(id));
		String order = request.getParameter("order");
		List reports;
		if (order.equals("id")) {
			reports = reportService.getReportsByProjectNameOrderByContract(project.getName());
			order = "按合同编号";
		} else {
			reports = reportService.getReportsByProjectName(project.getName());
			order = "按报表时间";
		}
		System.out.println(reports.size());
		String fileName = project.getName()+"台账表.xls";
		String sheetName = project.getName()+"台账表.xls";
		printTaiZhangExcel(response, sheetName, fileName,project, reports, order );
	}
	

	@RequestMapping(params = "method=printProjectLogs")
	public void printProjectLogs(ModelMap modelMap, 
			HttpSession session,
			HttpServletResponse response,
			HttpServletRequest request) throws ParseException{
		String id = request.getParameter("projectId");
		Project project = projectService.getProjectById(Integer.valueOf(id));
		String startDateString = request.getParameter("startDate");
		String endDateString = request.getParameter("endDate");
		Date startDate = null;
		Date endDate = null;
		try {
			 SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			 startDate = df.parse(startDateString);
			 endDate = df.parse(endDateString);
		} catch (Exception e) {
			modelMap.put("msg", "日期格式错误");
		}
		List projectLogs = projectLogService.getProjectLogByProjectNameAndDate(project.getName(), String.valueOf(startDate.getTime()), String.valueOf(endDate.getTime()));
		String fileName = project.getName()+"日志表.xls";
		String sheetName = project.getName()+"日志表.xls";
		printProjectLogsExcel(response, sheetName, fileName,project, projectLogs, startDate, endDate );
	}
	
	@RequestMapping(params = "method=printValuation")
	public void printValuation(ModelMap modelMap, 
			HttpSession session,
			HttpServletResponse response,
			HttpServletRequest request) throws ParseException{

		User user = (User) session.getAttribute("user");
		Project project = (Project) session.getAttribute("project");
		String[] choices = request.getParameterValues("choice");
		String startDateString = request.getParameter("startDate");
		String endDateString = request.getParameter("endDate");
		Date startDate;
		Date endDate;
		try {
			 SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			 startDate = df.parse(startDateString);
			 endDate = df.parse(endDateString);
		} catch (Exception e) {
//			modelMap.put("msg", "日期格式错误");
			return;
//			return "userPrintValuation";
		}
		
		StringBuffer msg = new StringBuffer();
		Report report = new Report();
		Rtp rtp = new Rtp();
		report.setProjectName(project.getName());
		String reportId;
		StringBuffer sff = new StringBuffer();
		
		String fileName = project.getName()+"产值结算表.xls";
		String sheetName = project.getName()+"产值结算表.xls";
		List valuationReports = valuationReportService.getValuationReportByProjectAndUnitsAndDate(project.getName(), choices, String.valueOf(startDate.getTime()), String.valueOf(endDate.getTime()));
		
		printValuationReportExcel(response, sheetName, fileName, project, valuationReports, startDate, endDate);
//		modelMap.put("msg", "打印成功");
//		return "userPrintValuation";
	}
	
public void printProjectLogsExcel(HttpServletResponse response, String sheetName, String fileName,Project project, List<ProjectLog> projectLogs, Date startDate, Date endDate){
		
		try {
			SimpleDateFormat df;
			// 声明一个工作薄
			@SuppressWarnings("resource")
			HSSFWorkbook workbook = new HSSFWorkbook();
			// 生成一个表格
			HSSFSheet sheet = workbook.createSheet(sheetName);
			
			HSSFCellStyle titleStyle = workbook.createCellStyle();
			titleStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
			titleStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
			// 生成一个字体
			HSSFFont titleFont = workbook.createFont();
//			font.setColor(HSSFColor.VIOLET.index);
			titleFont.setFontHeightInPoints((short)20);
			titleStyle.setFont(titleFont);
		    sheet.addMergedRegion(CellRangeAddress.valueOf("$A$1:$I$1")); 
		    Row titleRow = sheet.createRow(0);  
		    titleRow.setHeightInPoints(30);
		    Cell titleCell = titleRow.createCell(0);
		    titleCell.setCellStyle(titleStyle);
		    titleCell.setCellValue(project.getFullName()+"工程日志");
			
		    Row row = sheet.createRow(3);
		    Cell cell = row.createCell(0);
		    cell.setCellValue("工程名称:");
		    
		    cell = row.createCell(1);
		    cell.setCellValue(project.getFullName());
		    sheet.addMergedRegion(CellRangeAddress.valueOf("$H$5:$I$5")); 
		    row = sheet.createRow(4);
		    cell = row.createCell(0);
		    cell.setCellValue("时间:");
		    cell = row.createCell(1);
		    df = new SimpleDateFormat("yyyy年MM月dd日");
		    cell.setCellValue(df.format(new Date()));
		    
			sheet.addMergedRegion(new CellRangeAddress(5, 5, 1, 2));
			sheet.addMergedRegion(new CellRangeAddress(5, 5, 3, 4));
		    row = sheet.createRow(5);
		    cell = row.createCell(0);
		    cell.setCellValue("打印范围:");
		    cell = row.createCell(1);
		    df = new SimpleDateFormat("yy年MM月dd日");
		    cell.setCellValue(df.format(startDate));
		    cell = row.createCell(3);
		    cell.setCellValue(df.format(endDate));
		    
		    
		    HSSFCellStyle style = workbook.createCellStyle();
			style.setBorderBottom(HSSFCellStyle.BORDER_THIN);
			style.setBorderLeft(HSSFCellStyle.BORDER_THIN);
			style.setBorderRight(HSSFCellStyle.BORDER_THIN);
			style.setBorderTop(HSSFCellStyle.BORDER_THIN);
			style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
			
			sheet.addMergedRegion(new CellRangeAddress(6, 6, 1, 3));
			sheet.addMergedRegion(new CellRangeAddress(6, 6, 4, 8));
			row = sheet.createRow(6);
			
			cell = row.createCell(0);
			cell.setCellValue("序号");
			cell.setCellStyle(style);
			cell = row.createCell(1);
			cell.setCellValue("日期");
			cell.setCellStyle(style);
			cell = row.createCell(2);
			cell.setCellStyle(style);
			cell = row.createCell(3);
			cell.setCellStyle(style);
			cell = row.createCell(4);
			cell.setCellStyle(style);
			cell.setCellValue("日志");
			cell = row.createCell(5);
			cell.setCellStyle(style);
			cell = row.createCell(6);
			cell.setCellStyle(style);
			cell = row.createCell(7);
			cell.setCellStyle(style);
			cell = row.createCell(8);
			cell.setCellStyle(style);
			df = new SimpleDateFormat("yy年MM月dd日hh时mm分");
//			df = new SimpleDateFormat("yy-MM-dd hh:mm:ss");
			for(int i=0; i<projectLogs.size(); i++){
				ProjectLog projectLog = projectLogs.get(i);
				row = sheet.createRow(7+i);
				sheet.addMergedRegion(new CellRangeAddress(7+i, 7+i, 1, 3));
				sheet.addMergedRegion(new CellRangeAddress(7+i, 7+i, 4, 8));
				cell = row.createCell(0);
				cell.setCellValue(i+1);
				cell.setCellStyle(style);
				cell = row.createCell(1);
				Date date = new Date(Long.valueOf(projectLog.getDate()));
				cell.setCellValue(df.format(date));
				cell.setCellStyle(style);
				cell = row.createCell(2);
				cell.setCellStyle(style);
				cell = row.createCell(3);
				cell.setCellStyle(style);
				cell = row.createCell(4);
				cell.setCellStyle(style);
				cell.setCellValue(projectLog.getLogString());
				cell = row.createCell(5);
				cell.setCellStyle(style);
				cell = row.createCell(6);
				cell.setCellStyle(style);
				cell = row.createCell(7);
				cell.setCellStyle(style);
				cell = row.createCell(8);
				cell.setCellStyle(style);
			}
			sheet.addMergedRegion(new CellRangeAddress(7+projectLogs.size(), 7+projectLogs.size(), 0, 1));
			row = sheet.createRow(7+projectLogs.size());
			cell = row.createCell(0);
			cell.setCellValue("相关人员签字：");
			
			
			//设置打印 
		    HSSFPrintSetup print = (HSSFPrintSetup) sheet.getPrintSetup(); 
//		    print.setLandscape(true);//设置横向打印 
//		    print.setScale((short) 70);//设置打印缩放70% 
		    print.setPaperSize(HSSFPrintSetup.A4_PAPERSIZE);//设置为A4纸张 
		    print.setLeftToRight(true);//設置打印顺序先行后列,默认为先列行            
		    print.setFitHeight((short) 10);//设置缩放调整为10页高 
		    print.setFitWidth((short) 10);//设置缩放调整为宽高 

			// 设置生成的文件类型
			response.setContentType("application/vnd.ms-excel");
			// 设置文件头编码方式和文件名
			response.setHeader("Content-Disposition", "filename="
					+ new String(fileName.getBytes("gb2312"), "iso8859-1"));
			OutputStream out = response.getOutputStream();
			workbook.write(out);
			out.close();
			
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	
}
	
	@RequestMapping(params = "method=printContract")
	public void printContract(ModelMap modelMap, 
			HttpSession session,
			HttpServletResponse response,
			HttpServletRequest request) throws ParseException{
		String id = request.getParameter("projectId");
		Project project = projectService.getProjectById(Integer.valueOf(id));
		String order = request.getParameter("order");
		List contracts;
		if (order.equals("id")) {
			contracts = contractServic.getContractsByProjectNameOrderByContract(project.getName());
			order = "按合同编号";
		} else {
			contracts = contractServic.getContractsByProjectNameOrderByContract(project.getName());
			order = "按报表时间";
		}
		
		String fileName = project.getName()+"合同台账表.xls";
		String sheetName = project.getName()+"合同台账表.xls";
		printContractExcel(response, sheetName, fileName,project, contracts, order );
	}
	
	
	
	public void printContractExcel(HttpServletResponse response, String sheetName, String fileName,Project project, List<Contract> contracts, String order){
		
		try {
			SimpleDateFormat df;
			// 声明一个工作薄
			@SuppressWarnings("resource")
			HSSFWorkbook workbook = new HSSFWorkbook();
			// 生成一个表格
			HSSFSheet sheet = workbook.createSheet(sheetName);
			
			HSSFCellStyle titleStyle = workbook.createCellStyle();
			titleStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
			titleStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
			// 生成一个字体
			HSSFFont titleFont = workbook.createFont();
//			font.setColor(HSSFColor.VIOLET.index);
			titleFont.setFontHeightInPoints((short)20);
			titleStyle.setFont(titleFont);
		    sheet.addMergedRegion(CellRangeAddress.valueOf("$A$1:$I$1")); 
		    Row titleRow = sheet.createRow(0);  
		    titleRow.setHeightInPoints(30);
		    Cell titleCell = titleRow.createCell(0);
		    titleCell.setCellStyle(titleStyle);
		    titleCell.setCellValue(project.getName()+"合同台账");
			
		    Row row = sheet.createRow(3);
		    Cell cell = row.createCell(0);
		    cell.setCellValue("工程名称:");
		    
		    cell = row.createCell(1);
		    cell.setCellValue(project.getName());
		    sheet.addMergedRegion(CellRangeAddress.valueOf("$H$5:$I$5")); 
		    row = sheet.createRow(4);
		    cell = row.createCell(0);
		    cell.setCellValue("台账类型:");
		    cell = row.createCell(1);
		    cell.setCellValue(order);
		    cell = row.createCell(6);
		    cell.setCellValue("时间:");
		    cell = row.createCell(7);
		    df = new SimpleDateFormat("yyyy年MM月dd日");
		    cell.setCellValue(df.format(new Date()));
			
		    HSSFCellStyle style = workbook.createCellStyle();
			style.setBorderBottom(HSSFCellStyle.BORDER_THIN);
			style.setBorderLeft(HSSFCellStyle.BORDER_THIN);
			style.setBorderRight(HSSFCellStyle.BORDER_THIN);
			style.setBorderTop(HSSFCellStyle.BORDER_THIN);
			style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
			
			sheet.addMergedRegion(new CellRangeAddress(5, 5, 1, 2));
			sheet.addMergedRegion(new CellRangeAddress(5, 5, 3, 4));
			sheet.addMergedRegion(new CellRangeAddress(5, 5, 5, 6));
			sheet.addMergedRegion(new CellRangeAddress(5, 5, 7, 8));
			row = sheet.createRow(5);
			
			cell = row.createCell(0);
			cell.setCellValue("序号");
			cell.setCellStyle(style);
			cell = row.createCell(1);
			cell.setCellValue("编号");
			cell.setCellStyle(style);
			cell = row.createCell(2);
			cell.setCellStyle(style);
			cell = row.createCell(3);
			cell.setCellValue("合同名称");
			cell.setCellStyle(style);
			cell = row.createCell(4);
			cell.setCellStyle(style);
			cell = row.createCell(5);
			cell.setCellValue("类型");
			cell.setCellStyle(style);
			cell = row.createCell(6);
			cell.setCellStyle(style);
			cell = row.createCell(7);
			cell.setCellValue("签订日期");
			cell.setCellStyle(style);
			cell = row.createCell(8);
			cell.setCellStyle(style);
			df = new SimpleDateFormat("yy年MM月dd日hh时mm分");
			double cost = 0.0;
			for(int i=0; i<contracts.size(); i++){
				Contract contract = contracts.get(i);
				String type = null;
				if (contract.getType().equals(TypeEnum.LABOUR.getKey())) {
					type = TypeEnum.LABOUR.getValue();
				} else if (contract.getType().equals(TypeEnum.MACHINE.getKey())){
					type = TypeEnum.MACHINE.getValue();
				} else if (contract.getType().equals(TypeEnum.MATERIAL.getKey())){
					type = TypeEnum.MATERIAL.getValue();
				} else if (contract.getType().equals(TypeEnum.OTHER.getKey())){
					type = TypeEnum.OTHER.getValue();
				} 
				
				sheet.addMergedRegion(new CellRangeAddress(6+i, 6+i, 1, 2));
				sheet.addMergedRegion(new CellRangeAddress(6+i, 6+i, 3, 4));
				sheet.addMergedRegion(new CellRangeAddress(6+i, 6+i, 5, 6));
				sheet.addMergedRegion(new CellRangeAddress(6+i, 6+i, 7, 8));
				row = sheet.createRow(6+i);
				cell = row.createCell(0);
				cell.setCellValue(i+1);
				cell.setCellStyle(style);
				cell = row.createCell(1);
				cell.setCellValue(contract.getContractId());
				cell.setCellStyle(style);
				cell = row.createCell(2);
				cell.setCellStyle(style);
				cell = row.createCell(3);
				cell.setCellValue(contract.getName());
				cell.setCellStyle(style);
				cell = row.createCell(4);
				cell.setCellStyle(style);
				cell = row.createCell(5);
				cell.setCellValue(type);
				cell.setCellStyle(style);
				cell = row.createCell(6);
				cell.setCellStyle(style);
				cell = row.createCell(7);
				Date date = new Date(Long.parseLong(contract.getDate()));
				cell.setCellValue(df.format(date));
				cell.setCellStyle(style);
				cell = row.createCell(8);
				cell.setCellStyle(style);
			}
			sheet.addMergedRegion(new CellRangeAddress(6+contracts.size(), 6+contracts.size(), 0, 1));
			row = sheet.createRow(6+contracts.size());
			cell = row.createCell(0);
			cell.setCellValue("相关人员签字：");
			
			
			//设置打印 
		    HSSFPrintSetup print = (HSSFPrintSetup) sheet.getPrintSetup(); 
//		    print.setLandscape(true);//设置横向打印 
//		    print.setScale((short) 70);//设置打印缩放70% 
		    print.setPaperSize(HSSFPrintSetup.A4_PAPERSIZE);//设置为A4纸张 
		    print.setLeftToRight(true);//設置打印顺序先行后列,默认为先列行            
		    print.setFitHeight((short) 10);//设置缩放调整为10页高 
		    print.setFitWidth((short) 10);//设置缩放调整为宽高 

			// 设置生成的文件类型
			response.setContentType("application/vnd.ms-excel");
			// 设置文件头编码方式和文件名
			response.setHeader("Content-Disposition", "filename="
					+ new String(fileName.getBytes("gb2312"), "iso8859-1"));
			OutputStream out = response.getOutputStream();
			workbook.write(out);
			out.close();
			
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	
}
	
	public void printTaiZhangExcel(HttpServletResponse response, String sheetName, String fileName,Project project, List<Report> reports, String order){
			
			try {
				SimpleDateFormat df;
				// 声明一个工作薄
				@SuppressWarnings("resource")
				HSSFWorkbook workbook = new HSSFWorkbook();
				// 生成一个表格
				HSSFSheet sheet = workbook.createSheet(sheetName);
				
				HSSFCellStyle titleStyle = workbook.createCellStyle();
				titleStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
				titleStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
				// 生成一个字体
				HSSFFont titleFont = workbook.createFont();
//				font.setColor(HSSFColor.VIOLET.index);
				titleFont.setFontHeightInPoints((short)20);
				titleStyle.setFont(titleFont);
			    sheet.addMergedRegion(CellRangeAddress.valueOf("$A$1:$I$1")); 
			    Row titleRow = sheet.createRow(0);  
			    titleRow.setHeightInPoints(30);
			    Cell titleCell = titleRow.createCell(0);
			    titleCell.setCellStyle(titleStyle);
			    titleCell.setCellValue(project.getName()+"结算台账");
				
			    Row row = sheet.createRow(3);
			    Cell cell = row.createCell(0);
			    cell.setCellValue("工程名称:");
			    
			    cell = row.createCell(1);
			    cell.setCellValue(project.getName());
			    sheet.addMergedRegion(CellRangeAddress.valueOf("$H$5:$I$5")); 
			    row = sheet.createRow(4);
			    cell = row.createCell(0);
			    cell.setCellValue("台账类型:");
			    cell = row.createCell(1);
			    cell.setCellValue(order);
			    cell = row.createCell(6);
			    cell.setCellValue("时间:");
			    cell = row.createCell(7);
			    df = new SimpleDateFormat("yyyy年MM月dd日");
			    cell.setCellValue(df.format(new Date()));
				
			    HSSFCellStyle style = workbook.createCellStyle();
				style.setBorderBottom(HSSFCellStyle.BORDER_THIN);
				style.setBorderLeft(HSSFCellStyle.BORDER_THIN);
				style.setBorderRight(HSSFCellStyle.BORDER_THIN);
				style.setBorderTop(HSSFCellStyle.BORDER_THIN);
				style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
				
				sheet.addMergedRegion(new CellRangeAddress(5, 5, 1, 2));
				sheet.addMergedRegion(new CellRangeAddress(5, 5, 3, 4));
				row = sheet.createRow(5);
				
				cell = row.createCell(0);
				cell.setCellValue("序号");
				cell.setCellStyle(style);
				cell = row.createCell(1);
				cell.setCellValue("合同名称");
				cell.setCellStyle(style);
				cell = row.createCell(2);
				cell.setCellStyle(style);
				cell = row.createCell(3);
				cell.setCellValue("时间");
				cell.setCellStyle(style);
				cell = row.createCell(4);
				cell.setCellStyle(style);
				cell = row.createCell(5);
				cell.setCellValue("操作人员");
				cell.setCellStyle(style);
				cell = row.createCell(6);
				cell.setCellValue("结算单编号");
				cell.setCellStyle(style);
				cell = row.createCell(7);
				cell.setCellValue("结算值");
				cell.setCellStyle(style);
				cell = row.createCell(8);
				cell.setCellValue("备注");
				cell.setCellStyle(style);
				df = new SimpleDateFormat("yy年MM月dd日hh时mm分");
				double cost = 0.0;
				for(int i=0; i<reports.size(); i++){
					Report report = reports.get(i);
					sheet.addMergedRegion(new CellRangeAddress(6+i, 6+i, 1, 2));
					sheet.addMergedRegion(new CellRangeAddress(6+i, 6+i, 3, 4));
					row = sheet.createRow(6+i);
					cell = row.createCell(0);
					cell.setCellValue(i+1);
					cell.setCellStyle(style);
					cell = row.createCell(1);
					cell.setCellValue(report.getContractName());
					cell.setCellStyle(style);
					cell = row.createCell(2);
					cell.setCellStyle(style);
					cell = row.createCell(3);
					Date date = new Date(Long.parseLong(report.getDate()));
					cell.setCellValue(df.format(date));
					cell.setCellStyle(style);
					cell = row.createCell(4);
					cell.setCellStyle(style);
					cell = row.createCell(5);
					cell.setCellValue(report.getUserTruename());
					cell.setCellStyle(style);
					cell = row.createCell(6);
					cell.setCellValue(report.getReportId());
					cell.setCellStyle(style);
					cell = row.createCell(7);
					cell.setCellValue(report.getCost());
					cost = cost + Double.parseDouble(report.getCost());
					cell.setCellStyle(style);
					cell = row.createCell(8);
					cell.setCellStyle(style);
				}
				sheet.addMergedRegion(new CellRangeAddress(6+reports.size(), 6+reports.size(), 1, 2));
				sheet.addMergedRegion(new CellRangeAddress(6+reports.size(), 6+reports.size(), 3, 4));
				row = sheet.createRow(6+reports.size());
				cell = row.createCell(0);
				cell.setCellValue("合计");
				cell.setCellStyle(style);
				cell = row.createCell(1);
				cell.setCellStyle(style);
				cell = row.createCell(2);
				cell.setCellStyle(style);
				cell = row.createCell(3);
				cell.setCellStyle(style);
				cell = row.createCell(4);
				cell.setCellStyle(style);
				cell = row.createCell(5);
				cell.setCellStyle(style);
				cell = row.createCell(6);
				cell.setCellStyle(style);
				cell = row.createCell(7);
				cell.setCellValue(cost);
				cell.setCellStyle(style);
				cell = row.createCell(8);
				cell.setCellStyle(style);
				
				sheet.addMergedRegion(new CellRangeAddress(7+reports.size(), 7+reports.size(), 0, 1));
				row = sheet.createRow(7+reports.size());
				cell = row.createCell(0);
				cell.setCellValue("相关人员签字：");
				
				
				//设置打印 
			    HSSFPrintSetup print = (HSSFPrintSetup) sheet.getPrintSetup(); 
//			    print.setLandscape(true);//设置横向打印 
//			    print.setScale((short) 70);//设置打印缩放70% 
			    print.setPaperSize(HSSFPrintSetup.A4_PAPERSIZE);//设置为A4纸张 
			    print.setLeftToRight(true);//設置打印顺序先行后列,默认为先列行            
			    print.setFitHeight((short) 10);//设置缩放调整为10页高 
			    print.setFitWidth((short) 10);//设置缩放调整为宽高 

				// 设置生成的文件类型
				response.setContentType("application/vnd.ms-excel");
				// 设置文件头编码方式和文件名
				response.setHeader("Content-Disposition", "filename="
						+ new String(fileName.getBytes("gb2312"), "iso8859-1"));
				OutputStream out = response.getOutputStream();
				workbook.write(out);
				out.close();
				
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		
	}
	public void printValuationReportExcel(HttpServletResponse response, String sheetName, String fileName,Project project, List<ValuationReport> valuationReports, Date startDate, Date endDate){
		
		try {
			SimpleDateFormat df;
			// 声明一个工作薄
			@SuppressWarnings("resource")
			HSSFWorkbook workbook = new HSSFWorkbook();
			// 生成一个表格
			HSSFSheet sheet = workbook.createSheet(sheetName);
			
			HSSFCellStyle titleStyle = workbook.createCellStyle();
			titleStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
			titleStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
			// 生成一个字体
			HSSFFont titleFont = workbook.createFont();
//			font.setColor(HSSFColor.VIOLET.index);
			titleFont.setFontHeightInPoints((short)20);
			titleStyle.setFont(titleFont);
		    sheet.addMergedRegion(CellRangeAddress.valueOf("$A$1:$I$1")); 
		    Row titleRow = sheet.createRow(0);  
		    titleRow.setHeightInPoints(30);
		    Cell titleCell = titleRow.createCell(0);
		    titleCell.setCellStyle(titleStyle);
		    titleCell.setCellValue(project.getName()+"产值结算表");
			
		    Row row = sheet.createRow(3);
		    Cell cell = row.createCell(0);
		    cell.setCellValue("工程名称:");
		    
		    cell = row.createCell(1);
		    cell.setCellValue(project.getName());
		    sheet.addMergedRegion(new CellRangeAddress(4, 4, 1, 2));
		    sheet.addMergedRegion(new CellRangeAddress(4, 4, 4, 5));
		    sheet.addMergedRegion(new CellRangeAddress(4, 4, 7, 8));
		    row = sheet.createRow(4);
		    cell = row.createCell(0);
		    cell.setCellValue("结算范围:");
		    cell = row.createCell(1);
		    df = new SimpleDateFormat("yyyy年MM月dd日");
		    cell.setCellValue(df.format(startDate));
			cell = row.createCell(3);
			cell.setCellValue("至");
			cell = row.createCell(4);
			cell.setCellValue(df.format(endDate));
			cell = row.createCell(6);
			cell.setCellValue("打印时间:");
			cell = row.createCell(7);
			cell.setCellValue(df.format(new Date()));
		    
		    HSSFCellStyle style = workbook.createCellStyle();
			style.setBorderBottom(HSSFCellStyle.BORDER_THIN);
			style.setBorderLeft(HSSFCellStyle.BORDER_THIN);
			style.setBorderRight(HSSFCellStyle.BORDER_THIN);
			style.setBorderTop(HSSFCellStyle.BORDER_THIN);
			style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
			
			sheet.addMergedRegion(new CellRangeAddress(5, 5, 1, 2));
			sheet.addMergedRegion(new CellRangeAddress(5, 5, 3, 4));
			sheet.addMergedRegion(new CellRangeAddress(5, 5, 6, 7));
			row = sheet.createRow(5);
			
			cell = row.createCell(0);
			cell.setCellValue("序号");
			cell.setCellStyle(style);
			cell = row.createCell(1);
			cell.setCellValue("单位工程名称");
			cell.setCellStyle(style);
			cell = row.createCell(2);
			cell.setCellStyle(style);
			cell = row.createCell(3);
			cell.setCellValue("时间");
			cell.setCellStyle(style);
			cell = row.createCell(4);
			cell.setCellStyle(style);
			cell = row.createCell(5);
			cell.setCellValue("操作人员");
			cell.setCellStyle(style);
			cell = row.createCell(6);
			cell.setCellValue("结算值");
			cell.setCellStyle(style);
			cell = row.createCell(7);
			cell.setCellStyle(style);
			cell = row.createCell(8);
			cell.setCellValue("备注");
			cell.setCellStyle(style);
			df = new SimpleDateFormat("yy年MM月dd日hh时mm分");
			double cost = 0.0;
			DecimalFormat doubleDf = new java.text.DecimalFormat("#.00");
			for(int i=0; i<valuationReports.size(); i++){
				ValuationReport valuationReport = valuationReports.get(i);
				sheet.addMergedRegion(new CellRangeAddress(6+i, 6+i, 1, 2));
				sheet.addMergedRegion(new CellRangeAddress(6+i, 6+i, 3, 4));
				sheet.addMergedRegion(new CellRangeAddress(6+i, 6+i, 6, 7));
				row = sheet.createRow(6+i);
				cell = row.createCell(0);
				cell.setCellValue(i+1);
				cell.setCellStyle(style);
				cell = row.createCell(1);
				if (valuationReport.getUnit() == null) {
					cell.setCellValue("空");
				} else {
					cell.setCellValue(valuationReport.getUnit());
				}
				cell.setCellStyle(style);
				cell = row.createCell(2);
				cell.setCellStyle(style);
				cell = row.createCell(3);
				Date date = new Date(Long.parseLong(valuationReport.getTotalDate()));
				cell.setCellValue(df.format(date));
				cell.setCellStyle(style);
				cell = row.createCell(4);
				cell.setCellStyle(style);
				cell = row.createCell(5);
				cell.setCellValue(valuationReport.getTotalUserTruename());
				cell.setCellStyle(style);
				cell = row.createCell(6);
				
				cell.setCellValue(doubleDf.format(Double.valueOf(valuationReport.getCost())));
				cell.setCellStyle(style);
				cell = row.createCell(7);
				cell.setCellStyle(style);
				cell = row.createCell(8);
				cell.setCellValue(valuationReport.getUnit());
				cost = cost + Double.parseDouble(valuationReport.getCost());
				cell.setCellStyle(style);

			}
			sheet.addMergedRegion(new CellRangeAddress(6+valuationReports.size(), 6+valuationReports.size(), 1, 2));
			sheet.addMergedRegion(new CellRangeAddress(6+valuationReports.size(), 6+valuationReports.size(), 3, 4));
			sheet.addMergedRegion(new CellRangeAddress(6+valuationReports.size(), 6+valuationReports.size(), 6, 7));
			row = sheet.createRow(6+valuationReports.size());
			cell = row.createCell(0);
			cell.setCellValue("合计");
			cell.setCellStyle(style);
			cell = row.createCell(1);
			cell.setCellStyle(style);
			cell = row.createCell(2);
			cell.setCellStyle(style);
			cell = row.createCell(3);
			cell.setCellStyle(style);
			cell = row.createCell(4);
			cell.setCellStyle(style);
			cell = row.createCell(5);
			cell.setCellStyle(style);
			cell = row.createCell(6);
			cell.setCellValue(doubleDf.format(cost));
			cell.setCellStyle(style);
			cell = row.createCell(7);
			cell.setCellStyle(style);
			cell = row.createCell(8);
			cell.setCellStyle(style);
			
			sheet.addMergedRegion(new CellRangeAddress(7+valuationReports.size(), 7+valuationReports.size(), 0, 1));
			row = sheet.createRow(7+valuationReports.size());
			cell = row.createCell(0);
			cell.setCellValue("相关人员签字：");
			
			
			//设置打印 
		    HSSFPrintSetup print = (HSSFPrintSetup) sheet.getPrintSetup(); 
//		    print.setLandscape(true);//设置横向打印 
//		    print.setScale((short) 70);//设置打印缩放70% 
		    print.setPaperSize(HSSFPrintSetup.A4_PAPERSIZE);//设置为A4纸张 
		    print.setLeftToRight(true);//設置打印顺序先行后列,默认为先列行            
		    print.setFitHeight((short) 10);//设置缩放调整为10页高 
		    print.setFitWidth((short) 10);//设置缩放调整为宽高 

			// 设置生成的文件类型
			response.setContentType("application/vnd.ms-excel");
			// 设置文件头编码方式和文件名
			response.setHeader("Content-Disposition", "filename="
					+ new String(fileName.getBytes("gb2312"), "iso8859-1"));
			OutputStream out = response.getOutputStream();
			workbook.write(out);
			out.close();
			
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	
}
	
	public void saveExcel(HttpServletResponse response, String sheetName, String fileName, Report report ,List<Object> rtps, Contract contract){
		
		try {
			SimpleDateFormat df;
			Date tempDate;
			// 声明一个工作薄
			@SuppressWarnings("resource")
			HSSFWorkbook workbook = new HSSFWorkbook();
			// 生成一个表格
			HSSFSheet sheet = workbook.createSheet(sheetName);
//			sheet.setAutobreaks(true); 
//		    sheet.autoSizeColumn((short) 1);//自动根据长度调整单元格长度 
			// 生成一个样式
			//生成标题样式
			HSSFCellStyle titleStyle = workbook.createCellStyle();
			// 设置这些样式
//			style.setFillForegroundColor(HSSFColor.SKY_BLUE.index);
//			style.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
//			style.setBorderBottom(HSSFCellStyle.BORDER_THIN);
//			style.setBorderLeft(HSSFCellStyle.BORDER_THIN);
//			style.setBorderRight(HSSFCellStyle.BORDER_THIN);
//			style.setBorderTop(HSSFCellStyle.BORDER_THIN);
			titleStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
			titleStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
			// 生成一个字体
			HSSFFont titleFont = workbook.createFont();
			
//			font.setColor(HSSFColor.VIOLET.index);
			titleFont.setFontHeightInPoints((short)20);
//			titleFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD); 
//			font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
			// 把字体应用到当前的样式
			titleStyle.setFont(titleFont);
		    sheet.addMergedRegion(CellRangeAddress.valueOf("$A$1:$I$1")); 
		    Row titleRow = sheet.createRow(0);  
		    titleRow.setHeightInPoints(30);
		    Cell titleCell = titleRow.createCell(0);
		    titleCell.setCellStyle(titleStyle);
		    titleCell.setCellValue(report.getProjectName()+"结算表");
		    //生成head内容样式
		    HSSFCellStyle headStyle = workbook.createCellStyle();
		    HSSFFont headFont = workbook.createFont();
		    headFont.setFontHeightInPoints((short)12);
		    headStyle.setFont(headFont);
		    
		    Row row = sheet.createRow(3);
		    Cell cell = row.createCell(0);
		    cell.setCellValue("工程名称:");
		    
		    cell = row.createCell(1);
		    cell.setCellValue(report.getProjectName());
		    
		    row = sheet.createRow(4);
		    cell = row.createCell(0);
		    cell.setCellValue("合同方:");
		    cell = row.createCell(1);
		    cell.setCellValue(contract.getCompany());
		    cell = row.createCell(7);
		    cell.setCellValue("编号:");
		    cell = row.createCell(8);
		    cell.setCellValue(contract.getContractId());
		    
		    //生成表格顶部
		    //table第一行
		    HSSFCellStyle tableRow1Style = workbook.createCellStyle();
		    tableRow1Style.setBorderTop(HSSFCellStyle.BORDER_THIN);
		    
		    HSSFCellStyle tableCell1Style = workbook.createCellStyle();
		    tableCell1Style.setBorderTop(HSSFCellStyle.BORDER_THIN);
		    tableCell1Style.setBorderLeft(HSSFCellStyle.BORDER_THIN);
		    
		    HSSFCellStyle tableCell8Style = workbook.createCellStyle();
		    tableCell8Style.setBorderTop(HSSFCellStyle.BORDER_THIN);
		    tableCell8Style.setBorderRight(HSSFCellStyle.BORDER_THIN);
		    
		    
		    row = sheet.createRow(5);

		    cell = row.createCell(0);
		    cell.setCellStyle(tableCell1Style);
		    cell.setCellValue("合同信息:");
		    cell = row.createCell(1);
		    cell.setCellStyle(tableRow1Style);
		    
		    if (contract.getType().equals(TypeEnum.MACHINE.getKey())) {
		    		cell.setCellValue(TypeEnum.MACHINE.getValue());
			} else if(contract.getType().equals(TypeEnum.MATERIAL.getKey())){
				cell.setCellValue(TypeEnum.MATERIAL.getValue());
			} else if (contract.getType().equals(TypeEnum.PEOPLE.getKey())) {
				cell.setCellValue(TypeEnum.PEOPLE.getValue());
			} else {
				cell.setCellValue(TypeEnum.OTHER.getValue());
			}
		    
		    cell = row.createCell(2);
		    cell.setCellStyle(tableRow1Style);
		    cell.setCellValue("");
		    cell = row.createCell(3);
		    cell.setCellStyle(tableRow1Style);
		    cell.setCellValue("合同编号:");
		    cell = row.createCell(4);
		    cell.setCellStyle(tableRow1Style);
		    cell.setCellValue(contract.getContractId());
		    cell = row.createCell(5);
		    cell.setCellStyle(tableRow1Style);
		    cell.setCellValue("");
		    cell = row.createCell(6);
		    cell.setCellStyle(tableRow1Style);
		    cell.setCellValue("");
		    cell = row.createCell(7);
		    cell.setCellStyle(tableRow1Style);
		    cell.setCellValue("");
		    cell = row.createCell(8);
		    cell.setCellStyle(tableCell8Style);
		    cell.setCellValue("");
		    //table第二行
//		    HSSFCellStyle tableRow2Style = workbook.createCellStyle();
		    
		    HSSFCellStyle tableRow2Cell1Style = workbook.createCellStyle();
		    tableRow2Cell1Style.setBorderLeft(HSSFCellStyle.BORDER_THIN);
		    
		    HSSFCellStyle tableRow2Cell9Style = workbook.createCellStyle();
		    tableRow2Cell9Style.setBorderRight(HSSFCellStyle.BORDER_THIN);
		    
//		    sheet.addMergedRegion(CellRangeAddress.valueOf("$A$6:$B$6")); 
		    row = sheet.createRow(6);
		    
		    cell = row.createCell(0);
		    cell.setCellStyle(tableRow2Cell1Style);
		    cell.setCellValue("合同项范围:");
		    cell = row.createCell(1);
		    cell.setCellValue("");
		    cell = row.createCell(2);
		    cell.setCellValue("");
		    cell = row.createCell(3);
		    cell.setCellValue("");
		    cell = row.createCell(4);
		    cell.setCellValue("");
		    cell = row.createCell(5);
		    cell.setCellValue("");
		    cell = row.createCell(6);
		    cell.setCellValue("");
		    cell = row.createCell(7);
		    cell.setCellValue("");
		    cell = row.createCell(8);
		    cell.setCellStyle(tableRow2Cell9Style);
		    cell.setCellValue("");
		   //合同项范围
		    
		    for(int i = 0; i<rtps.size(); i++){
		    		Rtp rtp = (Rtp) rtps.get(i);
		    		row = sheet.createRow(7+i);
		    		cell = row.createCell(0);
		    		cell.setCellStyle(tableRow2Cell1Style);
		    		cell = row.createCell(1);
		    		cell.setCellValue("编号:");
		    		cell = row.createCell(2);
		    		cell.setCellValue(rtp.getPropertyId());
		    		cell = row.createCell(3);
		    		cell.setCellValue("");
		    		cell = row.createCell(4);
		    		cell.setCellValue("价格:");
		    		cell = row.createCell(5);
		    		cell.setCellValue(rtp.getPrice());
		    		cell = row.createCell(6);
		    		cell.setCellValue("");
		    		cell = row.createCell(7);
		    		cell.setCellValue("");
		    		cell = row.createCell(8);
		    		cell.setCellValue("");
		    		cell.setCellStyle(tableRow2Cell9Style);
		    }
		    //table头部的最后一行
		    HSSFCellStyle tableRow4Style = workbook.createCellStyle();
		    tableRow4Style.setBorderBottom(HSSFCellStyle.BORDER_THIN);
		    
		    HSSFCellStyle tableRow4Cell1Style = workbook.createCellStyle();
		    tableRow4Cell1Style.setBorderBottom(HSSFCellStyle.BORDER_THIN);
		    tableRow4Cell1Style.setBorderLeft(HSSFCellStyle.BORDER_THIN);
		    
		    HSSFCellStyle tableRow4Cell8Style = workbook.createCellStyle();
		    tableRow4Cell8Style.setBorderBottom(HSSFCellStyle.BORDER_THIN);
		    tableRow4Cell8Style.setBorderRight(HSSFCellStyle.BORDER_THIN);
		    
		    sheet.addMergedRegion(new CellRangeAddress(7+rtps.size(), 7+rtps.size(), 1, 3));
		    sheet.addMergedRegion(new CellRangeAddress(7+rtps.size(), 7+rtps.size(), 5, 7));
		    row = sheet.createRow(7+rtps.size());

		    cell = row.createCell(0);
		    cell.setCellStyle(tableRow2Cell1Style);
		    cell.setCellValue("时间范围:");
		    df = new SimpleDateFormat("yyyy年MM月dd日 HH时mm分ss秒");
		    tempDate = new Date(Long.parseLong(report.getStartDate()));
		    cell = row.createCell(1);
		    cell.setCellValue(df.format(tempDate));
		    cell = row.createCell(2);
		    cell.setCellValue("");
		    cell = row.createCell(3);
		    cell.setCellValue("");
		    cell = row.createCell(4);
		    cell.setCellValue("到");
		    tempDate = new Date(Long.parseLong(report.getEndDate()));
		    cell = row.createCell(5);
		    cell.setCellValue(df.format(tempDate));
		    cell = row.createCell(6);
		    cell.setCellValue("");
		    cell = row.createCell(7);
		    cell.setCellValue("");
		    cell = row.createCell(8);
		    cell.setCellStyle(tableRow2Cell9Style);
		    cell.setCellValue("");
		    
		    row = sheet.createRow(8+rtps.size());

		    cell = row.createCell(0);
		    cell.setCellStyle(tableRow4Cell1Style);
		    cell.setCellValue("明细如下:");
		    cell = row.createCell(1);
		    cell.setCellStyle(tableRow4Style);
		    cell.setCellValue("");
		    cell = row.createCell(2);
		    cell.setCellStyle(tableRow4Style);
		    cell.setCellValue("");
		    cell = row.createCell(3);
		    cell.setCellStyle(tableRow4Style);
		    cell.setCellValue("");
		    cell = row.createCell(4);
		    cell.setCellStyle(tableRow4Style);
		    cell.setCellValue("");
		    cell = row.createCell(5);
		    cell.setCellStyle(tableRow4Style);
		    cell.setCellValue("");
		    cell = row.createCell(6);
		    cell.setCellStyle(tableRow4Style);
		    cell.setCellValue("");
		    cell = row.createCell(7);
		    cell.setCellStyle(tableRow4Style);
		    cell.setCellValue("");
		    cell = row.createCell(8);
		    cell.setCellStyle(tableRow4Cell8Style);
		    cell.setCellValue("");
		    
		    //合同明细
		    HSSFCellStyle tempCellStyle = workbook.createCellStyle();
		    tempCellStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);
		    tempCellStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);
		    tempCellStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);
		    tempCellStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);
		    tempCellStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		    
//		    tableRow4Cell8Style.setBorderRight(HSSFCellStyle.BORDER_THIN);
		    sheet.addMergedRegion(new CellRangeAddress(9+rtps.size(), 9+rtps.size(), 0, 2));
		    sheet.addMergedRegion(new CellRangeAddress(9+rtps.size(), 9+rtps.size(), 3, 5));
		    sheet.addMergedRegion(new CellRangeAddress(9+rtps.size(), 9+rtps.size(), 6, 8));
		    row = sheet.createRow(9+rtps.size());
		    cell = row.createCell(0);
		    cell.setCellStyle(tempCellStyle);
		    cell.setCellValue("合同项编号");
		    cell = row.createCell(1);
		    cell.setCellStyle(tempCellStyle);
		    cell.setCellValue("");
		    cell = row.createCell(2);
		    cell.setCellStyle(tempCellStyle);
		    cell.setCellValue("");
		    cell = row.createCell(3);
		    cell.setCellStyle(tempCellStyle);
		    cell.setCellValue("数量");
		    cell = row.createCell(4);
		    cell.setCellStyle(tempCellStyle);
		    cell.setCellValue("");
		    cell = row.createCell(5);
		    cell.setCellStyle(tempCellStyle);
		    cell.setCellValue("");
		    cell = row.createCell(6);
		    cell.setCellStyle(tempCellStyle);
		    cell.setCellValue("金额");
		    cell = row.createCell(7);
		    cell.setCellStyle(tempCellStyle);
		    cell.setCellValue("");
		    cell = row.createCell(8);
		    cell.setCellStyle(tempCellStyle);
		    cell.setCellValue("");
		    
		    for(int i = 0; i<rtps.size(); i++){
		    		Rtp rtp = (Rtp) rtps.get(i);
		    		sheet.addMergedRegion(new CellRangeAddress(10+rtps.size()+i, 10+rtps.size()+i, 0, 2));
			    sheet.addMergedRegion(new CellRangeAddress(10+rtps.size()+i, 10+rtps.size()+i, 3, 5));
			    sheet.addMergedRegion(new CellRangeAddress(10+rtps.size()+i, 10+rtps.size()+i, 6, 8));
			    row = sheet.createRow(10+rtps.size()+i);
			    cell = row.createCell(0);
			    cell.setCellStyle(tempCellStyle);
			    cell.setCellValue(rtp.getPropertyId());
			    cell = row.createCell(1);
			    cell.setCellStyle(tempCellStyle);
			    cell.setCellValue("");
			    cell = row.createCell(2);
			    cell.setCellStyle(tempCellStyle);
			    cell.setCellValue("");
			    cell = row.createCell(3);
			    cell.setCellStyle(tempCellStyle);
			    cell.setCellValue(rtp.getTotal());
			    cell = row.createCell(4);
			    cell.setCellStyle(tempCellStyle);
			    cell.setCellValue("");
			    cell = row.createCell(5);
			    cell.setCellStyle(tempCellStyle);
			    cell.setCellValue("");
			    cell = row.createCell(6);
			    cell.setCellStyle(tempCellStyle);
			    cell.setCellValue(rtp.getCost());
			    cell = row.createCell(7);
			    cell.setCellStyle(tempCellStyle);
			    cell.setCellValue("");
			    cell = row.createCell(8);
			    cell.setCellStyle(tempCellStyle);
			    cell.setCellValue("");
		    }
		    
		    //foottable第一行
//		    tableRow1Style = workbook.createCellStyle();
//		    tableRow1Style.setBorderTop(HSSFCellStyle.BORDER_THIN);
//		    
//		    tableCell1Style = workbook.createCellStyle();
//		    tableCell1Style.setBorderTop(HSSFCellStyle.BORDER_THIN);
//		    tableCell1Style.setBorderLeft(HSSFCellStyle.BORDER_THIN);
//		    
//		    tableCell8Style = workbook.createCellStyle();
//		    tableCell8Style.setBorderTop(HSSFCellStyle.BORDER_THIN);
//		    tableCell8Style.setBorderRight(HSSFCellStyle.BORDER_THIN);
		    
		    sheet.addMergedRegion(new CellRangeAddress(10+rtps.size()+rtps.size(), 10+rtps.size()+rtps.size(), 1, 2));
		    row = sheet.createRow(10+rtps.size()+rtps.size());
		    cell = row.createCell(0);
		    cell.setCellStyle(tableCell1Style);
		    cell.setCellValue("结算值:");
		    cell = row.createCell(1);
		    cell.setCellStyle(tableRow1Style);
		    cell.setCellValue(report.getCost());
		    cell = row.createCell(2);
		    cell.setCellStyle(tableRow1Style);
		    cell.setCellValue("");
		    cell = row.createCell(3);
		    cell.setCellStyle(tableRow1Style);
		    cell.setCellValue("");
		    cell = row.createCell(4);
		    cell.setCellStyle(tableRow1Style);
		    cell.setCellValue("");
		    cell = row.createCell(5);
		    cell.setCellStyle(tableRow1Style);
		    cell.setCellValue("");
		    cell = row.createCell(6);
		    cell.setCellStyle(tableRow1Style);
		    cell.setCellValue("");
		    cell = row.createCell(7);
		    cell.setCellStyle(tableRow1Style);
		    cell.setCellValue("");
		    cell = row.createCell(8);
		    cell.setCellStyle(tableCell8Style);
		    cell.setCellValue("");
		    sheet.addMergedRegion(new CellRangeAddress(11+rtps.size()+rtps.size(), 11+rtps.size()+rtps.size(), 1, 2));
		    row = sheet.createRow(11+rtps.size()+rtps.size());
		    cell = row.createCell(0);
		    cell.setCellStyle(tableRow2Cell1Style);
		    cell.setCellValue("结算日期:");
		    cell = row.createCell(1);
		    df = new SimpleDateFormat("yyyy年MM月dd日");
		    tempDate= new Date(Long.valueOf(report.getDate()));
		    cell.setCellValue(df.format(tempDate));
		    
		    cell = row.createCell(2);
		    cell.setCellValue("");
		    cell = row.createCell(3);
		    cell.setCellValue("");
		    cell = row.createCell(4);
		    cell.setCellValue("");
		    cell = row.createCell(5);
		    cell.setCellValue("");
		    cell = row.createCell(6);
		    cell.setCellValue("");
		    cell = row.createCell(7);
		    cell.setCellValue("");
		    cell = row.createCell(8);
		    cell.setCellStyle(tableRow2Cell9Style);
		    cell.setCellValue("");
		    
		    row = sheet.createRow(12+rtps.size()+rtps.size());
		    cell = row.createCell(0);
		    cell.setCellStyle(tableRow2Cell1Style);
		    cell.setCellValue("操作人员:");
		    cell = row.createCell(1);
		    cell.setCellValue(report.getUserTruename());
		    cell = row.createCell(2);
		    cell.setCellValue("");
		    cell = row.createCell(3);
		    cell.setCellValue("");
		    cell = row.createCell(4);
		    cell.setCellValue("");
		    cell = row.createCell(5);
		    cell.setCellValue("");
		    cell = row.createCell(6);
		    cell.setCellValue("");
		    cell = row.createCell(7);
		    cell.setCellValue("");
		    cell = row.createCell(8);
		    cell.setCellStyle(tableRow2Cell9Style);
		    cell.setCellValue("");
		    
		    row = sheet.createRow(13+rtps.size()+rtps.size());
		    cell = row.createCell(0);
		    cell.setCellStyle(tableRow4Cell1Style);
		    cell.setCellValue("统计员:");
		    cell = row.createCell(1);
		    cell.setCellStyle(tableRow4Style);
		    cell.setCellValue("");
		    cell = row.createCell(2);
		    cell.setCellStyle(tableRow4Style);
		    cell.setCellValue("");
		    cell = row.createCell(3);
		    cell.setCellStyle(tableRow4Style);
		    cell.setCellValue("审核");
		    cell = row.createCell(4);
		    cell.setCellStyle(tableRow4Style);
		    cell.setCellValue("");
		    cell = row.createCell(5);
		    cell.setCellStyle(tableRow4Style);
		    cell.setCellValue("");
		    cell = row.createCell(6);
		    cell.setCellStyle(tableRow4Style);
		    cell.setCellValue("项目经理");
		    cell = row.createCell(7);
		    cell.setCellStyle(tableRow4Style);
		    cell.setCellValue("");
		    cell = row.createCell(8);
		    cell.setCellStyle(tableRow4Cell8Style);
		    cell.setCellValue("");
		    
//			// 产生表格标题行
//			row = sheet.createRow(startMap.size());
//			for (short i = 0; i < firstLine.length; i++) {
//				HSSFCell cellTemp = row.createCell(i);
//				cellTemp.setCellStyle(style);
//				HSSFRichTextString text = new HSSFRichTextString(firstLine[i]);
//				cellTemp.setCellValue(text);
//			}
//			// 遍历集合数据，产生数据行
//			for (short i = 0; i < list.size(); i++) {
//				row = sheet.createRow(i + startMap.size() +1);
//				CostLog costLog = (CostLog) list.get(i);
//				for (short j = 0; j < firstLine.length; j++) {
//					HSSFCell cellTemp = row.createCell(j);
//					cellTemp.setCellStyle(style);
//					HSSFRichTextString text = null;
//					if (firstLine[j] == "开始时间") {
//						SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//						Date date = new Date(Long.parseLong(costLog.getStartDate()));
//						text = new HSSFRichTextString(df.format(date));
//					} else if (firstLine[j] == "结束时间") {
//						SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//						Date date = new Date(Long.parseLong(costLog.getEndDate()));
//						text = new HSSFRichTextString(df.format(date));
//					} else if (firstLine[j] == "金额") {
//						text = new HSSFRichTextString(costLog.getCost());
//					}
//					cellTemp.setCellValue(text);
//				}
//			}
		    
		    
		    //设置打印 
		    HSSFPrintSetup print = (HSSFPrintSetup) sheet.getPrintSetup(); 
//		    print.setLandscape(true);//设置横向打印 
//		    print.setScale((short) 70);//设置打印缩放70% 
		    print.setPaperSize(HSSFPrintSetup.A4_PAPERSIZE);//设置为A4纸张 
		    print.setLeftToRight(true);//設置打印顺序先行后列,默认为先列行            
		    print.setFitHeight((short) 10);//设置缩放调整为10页高 
		    print.setFitWidth((short) 10);//设置缩放调整为宽高 

			// 设置生成的文件类型
			response.setContentType("application/vnd.ms-excel");
			// 设置文件头编码方式和文件名
			response.setHeader("Content-Disposition", "filename="
					+ new String(fileName.getBytes("gb2312"), "iso8859-1"));
			OutputStream out = response.getOutputStream();
			workbook.write(out);
			out.close();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
}
