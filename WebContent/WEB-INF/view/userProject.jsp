<%@page import="com.mvc.common.CostTypeEnum"%>
<%@page import="com.mvc.common.TypeEnum"%>
<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ include file="/include/import.jsp"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
User user = (User) session.getAttribute("user");
Project project = (Project)session.getAttribute("project");
SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
String date = df.format(new Date(Long.parseLong(project.getDate())));
String endDate = df.format(new Date(Long.parseLong(project.getExpectEndDate())));
List<ProjectLog> projectLogs = (List<ProjectLog>) request.getAttribute("projectLogs");
List<Project> projects = (List<Project>)session.getAttribute("projects");
List<Contract> contracts = (List<Contract>)request.getAttribute("contracts"); 
List<Report> reports = (List<Report>)request.getAttribute("reports"); 
List<Contract> machineContracts = new ArrayList();
List<Contract> materiaContracts = new ArrayList();
List<Contract> peopleContracts = new ArrayList();
List<Contract> otherContracts = new ArrayList();
for(Contract contract : contracts){
	if(contract.getType().equals(TypeEnum.LABOUR.getKey())){
		peopleContracts.add(contract);
	} else if(contract.getType().equals(TypeEnum.MACHINE.getKey())){
		machineContracts.add(contract);
	} else if(contract.getType().equals(TypeEnum.MATERIAL.getKey())){
		materiaContracts.add(contract);
	} else if(contract.getType().equals(TypeEnum.OTHER.getKey())){
		otherContracts.add(contract);
	}
}
%>
<!DOCTYPE html>
<html lang="en">

<head>
    <meta charset="utf-8">
    <title>管理系统 1.3</title>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta name="description" content="">
    <meta name="author" content="">
    <!-- Le styles -->
    <script type="text/javascript" src="assets/js/jquery.js"></script>

    <link rel="stylesheet" href="assets/css/style.css">
    <link rel="stylesheet" href="assets/css/loader-style.css">
    <link rel="stylesheet" href="assets/css/bootstrap.css">


    <link href="assets/js/footable/css/footable.core.css?v=2-0-1" rel="stylesheet" type="text/css">
    <link href="assets/js/footable/css/footable.standalone.css" rel="stylesheet" type="text/css">
    <link href="assets/js/footable/css/footable-demos.css" rel="stylesheet" type="text/css">

    <link rel="stylesheet" href="assets/js/dataTable/lib/jquery.dataTables/css/DT_bootstrap.css">
    <link rel="stylesheet" href="assets/js/dataTable/css/datatables.responsive.css">

	<link rel="stylesheet" href="assets/css/profile.css">



    <!-- HTML5 shim, for IE6-8 support of HTML5 elements -->
    <!--[if lt IE 9]>
        <script src="http://html5shim.googlecode.com/svn/trunk/html5.js"></script>
        <![endif]-->
    <!-- Fav and touch icons -->
    <link rel="shortcut icon" href="assets/ico/minus.png">
</head>

<body>
    <!-- Preloader -->
    <div id="preloader">
        <div id="status">&nbsp;</div>
    </div>
    <!-- TOP NAVBAR -->
    <nav role="navigation" class="navbar navbar-static-top">
        <div class="container-fluid">
            <!-- Brand and toggle get grouped for better mobile display -->
            <div class="navbar-header">
                <button data-target="#bs-example-navbar-collapse-1" data-toggle="collapse" class="navbar-toggle" type="button">
                    <span class="entypo-menu"></span>
                </button>
                <button class="navbar-toggle toggle-menu-mobile toggle-left" type="button">
                    <span class="entypo-list-add"></span>
                </button>




                <div id="logo-mobile" class="visible-xs">
                   <h1>管理系统<span>v1.2</span></h1>
                </div>

            </div>


            <!-- Collect the nav links, forms, and other content for toggling -->
            <div id="bs-example-navbar-collapse-1" class="collapse navbar-collapse">
                <ul class="nav navbar-nav">
                    
                    <li><a href="#"><i data-toggle="tooltip" data-placement="bottom" title="Help" style="font-size:20px;" class="icon-help tooltitle"></i></a>
                    </li>

                </ul>
                <div id="nt-title-container" class="navbar-left running-text visible-lg">
                    <ul class="date-top">
                        <li class="entypo-calendar" style="margin-right:5px"></li>
                        <li id="Date"></li>
                    </ul>
                   
                    <ul id="nt-title">
                        <li></i>&#160;&#160;公告1:&#160;
                            <b>欢迎</b></i>&#160;
                        </li>
                        <li></i>&#160;&#160;公告2:&#160;
                            <b>欢迎</b></i>&#160;
                        </li>

                    </ul>
                </div>

                <ul style="margin-right:0;" class="nav navbar-nav navbar-right">
                    <li>
                        <a data-toggle="dropdown" class="dropdown-toggle" href="#">
                            Hi, <%=user.getTruename() %> <b class="caret"></b>
                        </a>
                        <ul style="margin-top:14px;" role="menu" class="dropdown-setting dropdown-menu">
                            <li>
                                <a href="#">
                                    <span class="entypo-user"></span>&#160;&#160;我的个人信息</a>
                            </li>
                            <li>
                                <a href="#">
                                    <span class="entypo-vcard"></span>&#160;&#160;修改</a>
                            </li>
                            <li>
                                <a href="#">
                                    <span class="entypo-lifebuoy"></span>&#160;&#160;帮助</a>
                            </li>
                        </ul>
                    </li>
                    <li>
                        <a data-toggle="dropdown" class="dropdown-toggle" href="#">
                            <span class="icon-gear"></span>&#160;&#160;Setting</a>
                        <ul role="menu" class="dropdown-setting dropdown-menu">

                            <li class="theme-bg">
                                <div id="button-bg"></div>
                                <div id="button-bg2"></div>
                                <div id="button-bg3"></div>
                                <div id="button-bg5"></div>
                                <div id="button-bg6"></div>
                                <div id="button-bg7"></div>
                                <div id="button-bg8"></div>
                                <div id="button-bg9"></div>
                                <div id="button-bg10"></div>
                                <div id="button-bg11"></div>
                                <div id="button-bg12"></div>
                                <div id="button-bg13"></div>
                            </li>
                        </ul>
                    </li>
                </ul>

            </div>
            <!-- /.navbar-collapse -->
        </div>
        <!-- /.container-fluid -->
    </nav>

    <!-- /END OF TOP NAVBAR -->

    <!-- SIDE MENU -->
    <div id="skin-select">
        <div id="logo">
         <h1>管理系统<span>v1.2</span></h1>
        </div>

        <a id="toggle">
            <span class="entypo-menu"></span>
        </a>
        <div class="dark">
            <form action="#">
                <span>
                    <input type="text" name="search" value="" class="search rounded id_search" placeholder="Search Menu..." autofocus="">
                </span>
            </form>
        </div>

        <div class="search-hover">
            <form id="demo-2">
                <input type="search" placeholder="Search Menu..." class="id_search">
            </form>
        </div>


        <div class="skin-part">
            <div id="tree-wrap">
                <div class="side-bar">
                    

                    <ul class="topnav menu-left-nest">
                        <li>
                            <a href="#" style="border-left:0px solid!important;" class="title-menu-left">

                                <span class="design-kit"></span>
                                <i data-toggle="tooltip" class="entypo-cog pull-right config-wrap"></i>

                            </a>
                        </li>

                        <li>
                            <a class="tooltip-tip ajax-load" href="<%=path %>/webuser.do" title="概况">
                                <i class="icon-window"></i>
                                <span>概况</span>

                            </a>
                        </li>
                        

                        <li>
                            <a class="tooltip-tip " href="<%=path %>/admin.do?method=logOut" title="login">
                                <i class="icon-download"></i>
                                <span>退出</span>
                            </a>
                        </li>

                    </ul>
                    <ul class="topnav menu-left-nest">
                        <li>
                            <a href="#" style="border-left:0px solid!important;" class="title-menu-left">

                                <span class="widget-menu"></span>
                                <i data-toggle="tooltip" class="entypo-cog pull-right config-wrap"></i>

                            </a>
                        </li>
                         <li>
                            <a class="tooltip-tip ajax-load" href="#" title="Blog App">
                                <i class="fontawesome-road"></i>
                                <span>工程管理</span>
                            </a>
                             <ul>
                             <%
                             	for(Project projectTemp : projects){
                             %>
                                <li>
                                    <a class="tooltip-tip2 ajax-load" href="<%=path %>/webuser.do?method=goUserProject&projectId=<%=projectTemp.getId() %>" title="<%=projectTemp.getName()%>"><i class="fontawesome-exclamation-sign"></i><span><%=projectTemp.getName() %></span></a>
                                </li>
                                <%
                             	}
                                %>
                              	<!-- <li>
                                    <a class="tooltip-tip2 ajax-load" href="webTable.html" title="Blog List"><i class="fontawesome-exclamation-sign"></i><span>工程2</span></a>
                                </li> -->
                            </ul>
                        </li>
						<li>
                            <a class="tooltip-tip ajax-load" href="#" title="Blog App">
                                <i class="entypo-folder"></i>
                                <span>报表管理</span>
                            </a>
                             <ul>
                                <li>
                                    <a class="tooltip-tip2 ajax-load" href="<%=path %>/webuser.do?method=goUserOwnReport" title="我的报表"><i class="entypo-book-open"></i><span>我的报表</span></a>
                                </li>
                            </ul>
                        </li>
                    </ul>
                </div>
            </div>
        </div>
    </div>
    <!-- END OF SIDE MENU -->



    <!--  PAPER WRAP -->
    <div class="wrap-fluid">
        <div class="container-fluid paper-wrap bevel tlbr">

            <!-- CONTENT -->
            <!--TITLE -->
            <div class="row">
                <div id="paper-top">
                    <div class="col-sm-3">
                        <h2 class="tittle-content-header">
                            <span class="entypo-menu"></span>
                            <span>主界面
                            </span>
                        </h2>

                    </div>

                    <div class="col-lg-7">
                        <div class="devider-vertical visible-lg"></div>
                        <div class="tittle-middle-header">

                            <div class="alert">
                                <button type="button" class="close" data-dismiss="alert">×</button>
                                <span class="tittle-alert entypo-info-circled"></span>
                                你好,&nbsp;
                                <strong><%=user.getTruename() %></strong>&nbsp;&nbsp;
                            </div>


                        </div>
                    </div>
                    <div class="col-sm-2">
                        <div class="devider-vertical visible-lg"></div>
                        <div class="btn-group btn-wigdet pull-right visible-lg">
                            <div class="btn">
                                导航</div>
                            <button data-toggle="dropdown" class="btn dropdown-toggle" type="button">
                                <span class="caret"></span>
                                <span class="sr-only">Toggle Dropdown</span>
                            </button>
                            <ul role="menu" class="dropdown-menu">
                                <li>
                                    <a href="<%=path %>/webuser.do">
                                        <span class="entypo-plus-circled margin-iconic"></span>概况</a>
                                </li>
                                <li>
                                    <a href="#">
                                        <span class="entypo-heart margin-iconic"></span>我的个人信息</a>
                                </li>
                            </ul>
                        </div>


                    </div>
                </div>
            </div>
            <!--/ TITLE -->

            <!-- BREADCRUMB -->
            <ul id="breadcrumb">
                <li>
                    <span class="entypo-home"></span>
                </li>
                <li><i class="fa fa-lg fa-angle-right"></i>
                </li>
                <li><a href="#" title="Sample page 1">工程管理</a>
                </li>
                <li><i class="fa fa-lg fa-angle-right"></i>
                </li>
                <li><a href="#" title="Sample page 1"><%=project.getName() %></a>
                </li>
                <li class="pull-right">
                    <div class="input-group input-widget">

                        <input style="border-radius:15px" type="text" placeholder="Search..." class="form-control">
                    </div>
                </li>
            </ul>

            <!-- END OF BREADCRUMB -->
            
            <div class="content-wrap">
                <!-- PROFILE -->
                <div class="row">
                    <div class="col-sm-12">
                        <div class="well profile">
                            <div class="col-sm-12">
                                <div class="col-xs-12 col-sm-4 text-center">

                                    <ul class="list-group">
                                        <li class="list-group-item text-left">
                                            <span class="entypo-user"></span>&nbsp;&nbsp;工程详细</li>
                                        <li class="list-group-item text-center">
                                            <img src="#" alt="" class="img-circle img-responsive img-profile">

                                        </li>

                                        <li class="list-group-item text-right">
                                            <span class="pull-left">
                                                <strong>创建时间</strong>
                                            </span><%=date %></li>
                                        <li class="list-group-item text-right">
                                            <span class="pull-left">
                                                <strong>全称</strong>
                                            </span><%=project.getFullName() %></li>
                                        <li class="list-group-item text-right">
                                            <span class="pull-left">
                                                <strong>预期竣工日期</strong>
                                            </span><%=endDate %></li>
                                        <li class="list-group-item text-right">
                                            <span class="pull-left">
                                                <strong>预期利润率</strong>
                                            </span><%=project.getExpectProfit() %>%</li>

                                    </ul>
                                    
                                    <ul class="list-group">
                                        <li class="list-group-item text-left">
                                            <span class="entypo-user"></span>&nbsp;&nbsp;打印工程台账表</li>

                                        <li class="list-group-item text-right">
                                            <form action="<%=path %>/webuser.do?method=printTaiZhang&projectId=<%=project.getId() %>" method="post" class="form-horizontal bucket-form">
		                                        <div class="form-group">
		                                        		<label class=" col-sm-3 control-label">选择台账排列方式</label>
		                                        		<div class="col-lg-6">
				                                        <select class="filter-status form-control" name="order" >
				                                            <option value="time" >按报表时间
				                                            <option value="id">按合同编号
				                                        </select>
			                                        </div>
		                                    		</div>
		                                        
		                                        <div class="form-group">
		                                            <label class=" col-sm-3 control-label"></label>
		                                            <div class="col-lg-6">
		                                                <p class="form-control-static"><button class="btn btn btn-primary pull-left" type="submit">生成台帐</button>
		                                                </p>
		                                            </div>
		                                        </div>
		                                        
		                                    </form>
                                		  </li>
                                    </ul>
                                    <ul class="list-group">
                                        <li class="list-group-item text-left">
                                            <span class="entypo-user"></span>&nbsp;&nbsp;打印产值表</li>

                                        <li class="list-group-item text-right">
                                
                                            <form action="<%=path %>/webuser.do?method=goPrintValuation" method="post" class="form-horizontal bucket-form">
		                                        
		                                        <div class="form-group">
		                                            <label class=" col-sm-3 control-label"></label>
		                                            <div class="col-lg-6">
		                                                <p class="form-control-static"><button class="btn btn btn-primary pull-left" type="submit">进入产值打印页面</button>
		                                                </p>
		                                            </div>
		                                        </div>
		                                        
		                                    </form>
                                		  </li>
                                    </ul>

                                </div>
                                <div class="col-xs-12 col-sm-8 profile-name">
                                    <h2><%=project.getName() %>
                                    </h2>
                                    <hr>

                                    <dl class="dl-horizontal-profile">
                                        <dt>总成本</dt>
                                        <dd><%=project.getAllCost() %></dd>

                                        <dt>机械成本</dt>
                                        <dd><%=project.getMachineCost() %></dd>

                                        <dt>劳务成本</dt>
                                        <dd><%=project.getPeopleCost() %></dd>

                                        <dt>材料</dt>
                                        <dd><%=project.getMaterialCost() %></dd>

                                        <dt>其他成本</dt>
                                        <dd><%=project.getOtherCost() %></dd>
                                        
                                    </dl>
                                    <hr>

                                    <h5>
                                        <span class="entypo-arrows-ccw"></span>&nbsp;&nbsp;最近日志</h5>

                                    <div class="table-responsive">
                                    		<div class="panel-body">
                                    <form action="<%=path %>/webuser.do?method=printProjectLogs&projectId=<%=project.getId() %>" method="post" class="form-horizontal bucket-form">
                                        <div class="form-group">
                                        		<label class=" col-sm-3 control-label">开始时间</label>
                                        		<div class="col-lg-6">
		                                        <div id="datetimepicker1" class="input-group date">
		                                            <input class="form-control" name="startDate" data-format="yyyy-MM-dd hh:mm:ss" type="text" required="required">
		                                            <span class="input-group-addon add-on">
		                                                <i style="font-style:normal;" data-time-icon="entypo-clock" data-date-icon="entypo-calendar">
		      											</i>
		                                            </span>
		                                        </div>
	                                        </div>
                                    		</div>
                                    		<div class="form-group">
                                        		<label class=" col-sm-3 control-label">结束时间</label>
                                        		<div class="col-lg-6">
		                                        <div id="datetimepicker2" class="input-group date">
		                                            <input class="form-control" name="endDate" data-format="yyyy-MM-dd hh:mm:ss" type="text" required="required">
		
		                                            <span class="input-group-addon add-on">
		                                                <i style="font-style:normal;" data-time-icon="entypo-clock" data-date-icon="entypo-calendar">
		      											</i>
		                                            </span>
		                                        </div>
	                                        </div>
                                    		</div>
                                        <div class="form-group">
                                            <label class=" col-sm-3 control-label"></label>
                                            <div class="col-lg-6">
                                                <p class="form-control-static"><button class="btn btn-success" type="submit">打印日志</button>
                                                </p>
                                            </div>
                                        </div>
                                    </form>
                                </div>
                                        <table class="table table-hover table-striped table-condensed">

                                            <tbody>
                                            <%
                                            		df = new SimpleDateFormat("yyyy年MM月dd日 hh时mm分ss秒");
                                            		for(ProjectLog projectLog:projectLogs){
                                            			String tempDate = df.format(new Date(Long.parseLong(projectLog.getDate())));
                                            %>
                                                <!-- <tr>
                                                    <td><i class="pull-right fa fa-edit"></i> Today, 1:00 - Jeff Manzi liked your post.</td>
                                                </tr> -->
                                                <tr>
                                                    <td><i class="pull-right fa fa-edit"></i> <%=tempDate %> - <%=projectLog.getLogString() %></td>
                                                </tr>
                                             <%
                                         		}
                                             %>
                                            </tbody>
                                        </table>
                                    </div>
					
                                </div>

                            </div>
                        </div>
                    </div>
                </div>
			</div>
			
			<div class="content-wrap">
                <div class="row">
                    <div class="col-sm-12">
                        <div class="nest" id="FilteringClose">
                            <div class="title-alt">
                                <h6>
                                    查看所有报表</h6>
                                <div class="titleClose">
                                    <a class="gone" href="#FilteringClose">
                                        <span class="entypo-cancel"></span>
                                    </a>
                                </div>
                                <div class="titleToggle">
                                    <a class="nav-toggle-alt" href="#Filtering">
                                        <span class="entypo-up-open"></span>
                                    </a>
                                </div>

                            </div>

                            <div class="body-nest" id="Filtering">

                                <div class="row" style="margin-bottom:10px;">
                                    <div class="col-sm-4">
                                        <input class="form-control" id="filter" placeholder="查询" type="text">
                                    </div>
                                    <div class="col-sm-6">
                                        <a href="#clear" style="margin-left:10px;" class="pull-right btn btn-info clear-filter" title="clear filter">重置</a>
                                        <a href="#api" class="pull-right btn btn-info filter-api" title="Filter using the Filter API">查询</a>
                                    </div>

                                </div>

                                <table id="footable-res6" class="demo" data-filter="#filter" data-filter-text-only="true" data-page-size="10">
                                    <thead>
                                        <tr>
                                            <th data-toggle="true">
                                                编号
                                            </th>
                                            <th>
                                                合同名称
                                            </th>
                                            <th>
                                                创建时间
                                            </th>
                                            <th>
                                                操作人员
                                            </th>
                                            <th>
                                                操作
                                            </th>
                                        </tr>
                                    </thead>	
                                    <tbody>
                                    <%for(Report report : reports){ 
                                    		Date tempDate = new Date(Long.parseLong(report.getDate()));
                                    		//SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                                    %>
                                        <tr>
                                            <td><%=report.getReportId() %></td>
                                            <td><%=report.getContractName() %></td>
                                            <td><%=df.format(tempDate) %></td>
                                            <td><%=report.getUserTruename() %></td>
                                            <td><a href="<%=path %>/webuser.do?method=goUserReport&id=<%=report.getId() %>">查看</a></td>
                                    <%} %>     
                                        </tr>
                                        <!-- <tr>
                                            <td>X2B2</td>
                                            <td>合同2</td>
                                            <td>查看</td>
                                            
                                        </tr> -->
                                    </tbody>
                                    <tfoot>
                                        <tr>
                                            <td colspan="5">
                                                <div class="pagination pagination-centered"></div>
                                            </td>
                                        </tr>
                                    </tfoot>
                                </table>

                            </div>

                        </div>
                    </div>
					
                </div>
             </div>
			
			
			<div class="content-wrap">
                <div class="row">
                    <div class="col-sm-12">
                        <div class="nest" id="FilteringClose">
                            <div class="title-alt">
                                <h6>
                                    查看机械合同</h6>
                                <div class="titleClose">
                                    <a class="gone" href="#FilteringClose">
                                        <span class="entypo-cancel"></span>
                                    </a>
                                </div>
                                <div class="titleToggle">
                                    <a class="nav-toggle-alt" href="#Filtering">
                                        <span class="entypo-up-open"></span>
                                    </a>
                                </div>

                            </div>

                            <div class="body-nest" id="Filtering">

                                <div class="row" style="margin-bottom:10px;">
                                    <div class="col-sm-4">
                                        <input class="form-control" id="filter" placeholder="查询" type="text">
                                    </div>
                                    <div class="col-sm-2">
                                        <select class="filter-status form-control">
                                            <option value="active">使用中
                                            <option value="disabled">已删除
                                        </select>
                                    </div>
                                    <div class="col-sm-6">
                                        <a href="#clear" style="margin-left:10px;" class="pull-right btn btn-info clear-filter" title="clear filter">重置</a>
                                        <!-- <a href="#" class="pull-right btn btn-info" title="Filter using the Filter API">打印台账</a> -->
                                    </div>

                                </div>

                                <table id="footable-res1" class="demo" data-filter="#filter" data-filter-text-only="true" data-page-size="10">
                                    <thead>
                                        <tr>
                                            <th data-toggle="true">
                                                编号
                                            </th>
                                            <th>
                                                合同简称
                                            </th>
                                            <th>
                                            		类型
                                            </th>
                                            <th>
                                                操作
                                            </th>
                                            <th data-hide="phone,tablet">
                                                合同全称
                                            </th>
                                            <th data-hide="phone,tablet">
                                                备注
                                            </th>
                                            <th data-hide="phone">
                                                状态
                                            </th>
                                        </tr>
                                    </thead>	
                                    <tbody>
                                    <%
                                    		for(Contract contract : machineContracts){
                                    			String tempValue;
                                    			if(contract.getStatus().equals("1")){
                                    				tempValue = "使用中";
                                    			} else if(contract.getStatus().equals("0")){
                                    				tempValue = "已删除";
                                    			} else{
                                    				tempValue = "无";
                                    			}
                                    			String typeValue;
                                    			if(contract.getType().equals(TypeEnum.MACHINE.getKey())){
                                    				typeValue = TypeEnum.MACHINE.getValue();
                                    			} else if(contract.getType().equals(TypeEnum.MATERIAL.getKey())){
                                    				typeValue = TypeEnum.MATERIAL.getValue();
                                    			} else if(contract.getType().equals(TypeEnum.OTHER.getKey())){
                                    				typeValue = TypeEnum.OTHER.getValue();
                                    			} else if(contract.getType().equals(TypeEnum.PEOPLE.getKey())){
                                    				typeValue = TypeEnum.OTHER.getValue();
                                    			} else{
                                    				typeValue = "无";
                                    			}
                                    %>
                                        <tr>
                                            <td><%=contract.getContractId() %></td>
                                            <td><%=contract.getName() %></td>
                                            <td><%=typeValue %></td>
                                            <td><a href="<%=path %>/webuser.do?method=goUserPrint&contractId=<%=contract.getContractId() %>">操作</a></td>
                                            <td data-value="78025368997"><%=contract.getCompany() %> </td>
                                            <td data-value="78025368997"><%=contract.getRemark() %> </td>
                                            <td data-value="1">
                                                <span class="status-metro status-active" title="Active"><%=tempValue %></span>
                                            </td>
                                        </tr>
                                    <%
                                    		}
                                    %>
                                        <!-- <tr>
                                            <td>H1</td>
                                            <td>合同1</td>
                                            <td>打印</td>
                                            <td data-value="78025368997">备注1</td>
                                            <td data-value="78025368997">备注1</td>
                                            <td data-value="1">
                                                <span class="status-metro status-disabled" title="Disabled">已删除</span>
                                            </td>
                                        </tr> -->
                                    </tbody>
                                    <tfoot>
                                        <tr>
                                            <td colspan="5">
                                                <div class="pagination pagination-centered"></div>
                                            </td>
                                        </tr>
                                    </tfoot>
                                </table>

                            </div>

                        </div>
                    </div>
					
                </div>
                

            </div>
            <div class="content-wrap">
                <div class="row">
                    <div class="col-sm-12">
                        <div class="nest" id="FilteringClose">
                            <div class="title-alt">
                                <h6>
                                    查看劳务合同</h6>
                                <div class="titleClose">
                                    <a class="gone" href="#FilteringClose">
                                        <span class="entypo-cancel"></span>
                                    </a>
                                </div>
                                <div class="titleToggle">
                                    <a class="nav-toggle-alt" href="#Filtering">
                                        <span class="entypo-up-open"></span>
                                    </a>
                                </div>

                            </div>

                            <div class="body-nest" id="Filtering">

                                <div class="row" style="margin-bottom:10px;">
                                    <div class="col-sm-4">
                                        <input class="form-control" id="filter" placeholder="查询" type="text">
                                    </div>
                                    <div class="col-sm-2">
                                        <select class="filter-status form-control">
                                            <option value="active">使用中
                                            <option value="disabled">已删除
                                        </select>
                                    </div>
                                    <div class="col-sm-6">
                                        <a href="#clear" style="margin-left:10px;" class="pull-right btn btn-info clear-filter" title="clear filter">重置</a>
                                        <!-- <a href="#" class="pull-right btn btn-info" title="Filter using the Filter API">打印台账</a> -->
                                    </div>

                                </div>

                                <table id="footable-res2" class="demo" data-filter="#filter" data-filter-text-only="true" data-page-size="10">
                                    <thead>
                                        <tr>
                                            <th data-toggle="true">
                                                编号
                                            </th>
                                            <th>
                                                合同简称
                                            </th>
                                            <th>
                                            		类型
                                            </th>
                                            <th>
                                                操作
                                            </th>
                                            <th data-hide="phone,tablet">
                                                合同全称
                                            </th>
                                            <th data-hide="phone,tablet">
                                                备注
                                            </th>
                                            <th data-hide="phone">
                                                状态
                                            </th>
                                        </tr>
                                    </thead>	
                                    <tbody>
                                    <%
                                    		for(Contract contract : peopleContracts){
                                    			String tempValue;
                                    			if(contract.getStatus().equals("1")){
                                    				tempValue = "使用中";
                                    			} else if(contract.getStatus().equals("0")){
                                    				tempValue = "已删除";
                                    			} else{
                                    				tempValue = "无";
                                    			}
                                    			String typeValue;
                                    			if(contract.getType().equals(TypeEnum.MACHINE.getKey())){
                                    				typeValue = TypeEnum.MACHINE.getValue();
                                    			} else if(contract.getType().equals(TypeEnum.MATERIAL.getKey())){
                                    				typeValue = TypeEnum.MATERIAL.getValue();
                                    			} else if(contract.getType().equals(TypeEnum.OTHER.getKey())){
                                    				typeValue = TypeEnum.OTHER.getValue();
                                    			} else if(contract.getType().equals(TypeEnum.LABOUR.getKey())){
                                    				typeValue = TypeEnum.LABOUR.getValue();
                                    			} else{
                                    				typeValue = "无";
                                    			}
                                    %>
                                        <tr>
                                            <td><%=contract.getContractId() %></td>
                                            <td><%=contract.getName() %></td>
                                            <td><%=typeValue %></td>
                                            <td><a href="<%=path %>/webuser.do?method=goUserPrint&contractId=<%=contract.getContractId()%>">操作</a></td>
                                            <td data-value="78025368997"><%=contract.getCompany() %></td>
                                            <td data-value="78025368997"><%=contract.getRemark() %></td>
                                            <td data-value="1">
                                                <span class="status-metro status-active" title="Active"><%=tempValue %></span>
                                            </td>
                                        </tr>
                                    <%
                                    		}
                                    %>
                                        <!-- <tr>
                                            <td>H1</td>
                                            <td>合同1</td>
                                            <td>打印</td>
                                            <td data-value="78025368997">备注1</td>
                                            <td data-value="78025368997">备注1</td>
                                            <td data-value="1">
                                                <span class="status-metro status-disabled" title="Disabled">已删除</span>
                                            </td>
                                        </tr> -->
                                    </tbody>
                                    <tfoot>
                                        <tr>
                                            <td colspan="5">
                                                <div class="pagination pagination-centered"></div>
                                            </td>
                                        </tr>
                                    </tfoot>
                                </table>

                            </div>

                        </div>
                    </div>
					
                </div>
                

            </div>
            <div class="content-wrap">
                <div class="row">
                    <div class="col-sm-12">
                        <div class="nest" id="FilteringClose">
                            <div class="title-alt">
                                <h6>
                                    查看材料合同</h6>
                                <div class="titleClose">
                                    <a class="gone" href="#FilteringClose">
                                        <span class="entypo-cancel"></span>
                                    </a>
                                </div>
                                <div class="titleToggle">
                                    <a class="nav-toggle-alt" href="#Filtering">
                                        <span class="entypo-up-open"></span>
                                    </a>
                                </div>

                            </div>

                            <div class="body-nest" id="Filtering">

                                <div class="row" style="margin-bottom:10px;">
                                    <div class="col-sm-4">
                                        <input class="form-control" id="filter" placeholder="查询" type="text">
                                    </div>
                                    <div class="col-sm-2">
                                        <select class="filter-status form-control">
                                            <option value="active">使用中
                                            <option value="disabled">已删除
                                        </select>
                                    </div>
                                    <div class="col-sm-6">
                                        <a href="#clear" style="margin-left:10px;" class="pull-right btn btn-info clear-filter" title="clear filter">重置</a>
                                        <!-- <a href="#" class="pull-right btn btn-info" title="Filter using the Filter API">打印台账</a> -->
                                    </div>

                                </div>

                                <table id="footable-res3" class="demo" data-filter="#filter" data-filter-text-only="true" data-page-size="10">
                                    <thead>
                                        <tr>
                                            <th data-toggle="true">
                                                编号
                                            </th>
                                            <th>
                                                合同简称
                                            </th>
                                            <th>
                                            		类型
                                            </th>
                                            <th>
                                                操作
                                            </th>
                                            <th data-hide="phone,tablet">
                                                合同全称
                                            </th>
                                            <th data-hide="phone,tablet">
                                                备注
                                            </th>
                                            <th data-hide="phone">
                                                状态
                                            </th>
                                        </tr>
                                    </thead>	
                                    <tbody>
                                    <%
                                    		for(Contract contract : materiaContracts){
                                    			String tempValue;
                                    			if(contract.getStatus().equals("1")){
                                    				tempValue = "使用中";
                                    			} else if(contract.getStatus().equals("0")){
                                    				tempValue = "已删除";
                                    			} else{
                                    				tempValue = "无";
                                    			}
                                    			String typeValue;
                                    			if(contract.getType().equals(TypeEnum.MACHINE.getKey())){
                                    				typeValue = TypeEnum.MACHINE.getValue();
                                    			} else if(contract.getType().equals(TypeEnum.MATERIAL.getKey())){
                                    				typeValue = TypeEnum.MATERIAL.getValue();
                                    			} else if(contract.getType().equals(TypeEnum.OTHER.getKey())){
                                    				typeValue = TypeEnum.OTHER.getValue();
                                    			} else if(contract.getType().equals(TypeEnum.LABOUR.getKey())){
                                    				typeValue = TypeEnum.LABOUR.getValue();
                                    			} else{
                                    				typeValue = "无";
                                    			}
                                    %>
                                        <tr>
                                            <td><%=contract.getContractId() %></td>
                                            <td><%=contract.getName() %></td>
                                            <td><%=typeValue %></td>
                                            <td><a href="<%=path %>/webuser.do?method=goUserPrint&contractId=<%=contract.getContractId()%>">操作</a></td>
                                            <td data-value="78025368997"><%=contract.getCompany() %></td>
                                            <td data-value="78025368997"><%=contract.getRemark() %></td>
                                            <td data-value="1">
                                                <span class="status-metro status-active" title="Active"><%=tempValue %></span>
                                            </td>
                                        </tr>
                                    <%
                                    		}
                                    %>
                                        <!-- <tr>
                                            <td>H1</td>
                                            <td>合同1</td>
                                            <td>打印</td>
                                            <td data-value="78025368997">备注1</td>
                                            <td data-value="78025368997">备注1</td>
                                            <td data-value="1">
                                                <span class="status-metro status-disabled" title="Disabled">已删除</span>
                                            </td>
                                        </tr> -->
                                    </tbody>
                                    <tfoot>
                                        <tr>
                                            <td colspan="5">
                                                <div class="pagination pagination-centered"></div>
                                            </td>
                                        </tr>
                                    </tfoot>
                                </table>

                            </div>

                        </div>
                    </div>
					
                </div>
                

            </div>
            <div class="content-wrap">
                <div class="row">
                    <div class="col-sm-12">
                        <div class="nest" id="FilteringClose">
                            <div class="title-alt">
                                <h6>
                                    查看其他合同</h6>
                                <div class="titleClose">
                                    <a class="gone" href="#FilteringClose">
                                        <span class="entypo-cancel"></span>
                                    </a>
                                </div>
                                <div class="titleToggle">
                                    <a class="nav-toggle-alt" href="#Filtering">
                                        <span class="entypo-up-open"></span>
                                    </a>
                                </div>

                            </div>

                            <div class="body-nest" id="Filtering">

                                <div class="row" style="margin-bottom:10px;">
                                    <div class="col-sm-4">
                                        <input class="form-control" id="filter" placeholder="查询" type="text">
                                    </div>
                                    <div class="col-sm-2">
                                        <select class="filter-status form-control">
                                            <option value="active">使用中
                                            <option value="disabled">已删除
                                        </select>
                                    </div>
                                    <div class="col-sm-6">
                                        <a href="#clear" style="margin-left:10px;" class="pull-right btn btn-info clear-filter" title="clear filter">重置</a>
                                        <!-- <a href="#" class="pull-right btn btn-info" title="Filter using the Filter API">打印台账</a> -->
                                    </div>

                                </div>

                                <table id="footable-res4" class="demo" data-filter="#filter" data-filter-text-only="true" data-page-size="10">
                                    <thead>
                                        <tr>
                                            <th data-toggle="true">
                                                编号
                                            </th>
                                            <th>
                                                合同简称
                                            </th>
                                            <th>
                                            		类型
                                            </th>
                                            <th>
                                                操作
                                            </th>
                                            <th data-hide="phone,tablet">
                                                合同全称
                                            </th>
                                            <th data-hide="phone,tablet">
                                                备注
                                            </th>
                                            <th data-hide="phone">
                                                状态
                                            </th>
                                        </tr>
                                    </thead>	
                                    <tbody>
                                    <%
                                    		for(Contract contract : otherContracts){
                                    			String tempValue;
                                    			if(contract.getStatus().equals("1")){
                                    				tempValue = "使用中";
                                    			} else if(contract.getStatus().equals("0")){
                                    				tempValue = "已删除";
                                    			} else{
                                    				tempValue = "无";
                                    			}
                                    			String typeValue;
                                    			if(contract.getType().equals(TypeEnum.MACHINE.getKey())){
                                    				typeValue = TypeEnum.MACHINE.getValue();
                                    			} else if(contract.getType().equals(TypeEnum.MATERIAL.getKey())){
                                    				typeValue = TypeEnum.MATERIAL.getValue();
                                    			} else if(contract.getType().equals(TypeEnum.OTHER.getKey())){
                                    				typeValue = TypeEnum.OTHER.getValue();
                                    			} else if(contract.getType().equals(TypeEnum.LABOUR.getKey())){
                                    				typeValue = TypeEnum.LABOUR.getValue();
                                    			}  else{
                                    				typeValue = "无";
                                    			}
                                    %>
                                        <tr>
                                            <td><%=contract.getContractId() %></td>
                                            <td><%=contract.getName() %></td>
                                            <td><%=typeValue %></td>
                                            <td><a href="<%=path %>/webuser.do?method=goUserPrint&contractId=<%=contract.getContractId()%>">操作</a></td>
                                            <td data-value="78025368997"><%=contract.getCompany() %></td>
                                            <td data-value="78025368997"><%=contract.getRemark() %></td>
                                            <td data-value="1">
                                                <span class="status-metro status-active" title="Active"><%=tempValue %></span>
                                            </td>
                                        </tr>
                                    <%
                                    		}
                                    %>
                                        <!-- <tr>
                                            <td>H1</td>
                                            <td>合同1</td>
                                            <td>打印</td>
                                            <td data-value="78025368997">备注1</td>
                                            <td data-value="78025368997">备注1</td>
                                            <td data-value="1">
                                                <span class="status-metro status-disabled" title="Disabled">已删除</span>
                                            </td>
                                        </tr> -->
                                    </tbody>
                                    <tfoot>
                                        <tr>
                                            <td colspan="5">
                                                <div class="pagination pagination-centered"></div>
                                            </td>
                                        </tr>
                                    </tfoot>
                                </table>

                            </div>

                        </div>
                    </div>
					
                </div>
                

            </div>		


            <div class="content-wrap">
                <div class="row">
                    <div class="col-sm-12">
                        <div class="nest" id="FilteringClose">
                            <div class="title-alt">
                                <h6>
                                    查看所有合同</h6>
                                <div class="titleClose">
                                    <a class="gone" href="#FilteringClose">
                                        <span class="entypo-cancel"></span>
                                    </a>
                                </div>
                                <div class="titleToggle">
                                    <a class="nav-toggle-alt" href="#Filtering">
                                        <span class="entypo-up-open"></span>
                                    </a>
                                </div>

                            </div>

                            <div class="body-nest" id="Filtering">

                                <div class="row" style="margin-bottom:10px;">
                                    <div class="col-sm-4">
                                        <input class="form-control" id="filter" placeholder="查询" type="text">
                                    </div>
                                    <div class="col-sm-2">
                                        <select class="filter-status form-control">
                                            <option value="active">使用中
                                            <option value="disabled">已删除
                                        </select>
                                    </div>
                                    <div class="col-sm-6">
                                        <a href="#clear" style="margin-left:10px;" class="pull-right btn btn-info clear-filter" title="clear filter">重置</a>
                                        <!-- <a href="#" class="pull-right btn btn-info" title="Filter using the Filter API">打印台账</a> -->
                                    </div>

                                </div>

                                <table id="footable-res5" class="demo" data-filter="#filter" data-filter-text-only="true" data-page-size="10">
                                    <thead>
                                        <tr>
                                            <th data-toggle="true">
                                                编号
                                            </th>
                                            <th>
                                                合同简称
                                            </th>
                                            <th>
                                            		类型
                                            </th>
                                            <th>
                                                操作
                                            </th>
                                            <th data-hide="phone,tablet">
                                                合同全称
                                            </th>
                                            <th data-hide="phone,tablet">
                                                备注
                                            </th>
                                            <th data-hide="phone">
                                                状态
                                            </th>
                                        </tr>
                                    </thead>	
                                    <tbody>
                                    <%
                                    		for(Contract contract : contracts){
                                    			String tempValue;
                                    			if(contract.getStatus().equals("1")){
                                    				tempValue = "使用中";
                                    			} else if(contract.getStatus().equals("0")){
                                    				tempValue = "已删除";
                                    			} else{
                                    				tempValue = "无";
                                    			}
                                    			String typeValue;
                                    			if(contract.getType().equals(TypeEnum.MACHINE.getKey())){
                                    				typeValue = TypeEnum.MACHINE.getValue();
                                    			} else if(contract.getType().equals(TypeEnum.MATERIAL.getKey())){
                                    				typeValue = TypeEnum.MATERIAL.getValue();
                                    			} else if(contract.getType().equals(TypeEnum.OTHER.getKey())){
                                    				typeValue = TypeEnum.OTHER.getValue();
                                    			} else if(contract.getType().equals(TypeEnum.LABOUR.getKey())){
                                    				typeValue = TypeEnum.LABOUR.getValue();
                                    			} else{
                                    				typeValue = "无";
                                    			}
                                    %>
                                        <tr>
                                            <td><%=contract.getContractId() %></td>
                                            <td><%=contract.getName() %></td>
                                            <td><%=typeValue %></td>
                                            <td><a href="<%=path %>/webuser.do?method=goUserPrint&contractId=<%=contract.getContractId()%>">操作</a></td>
                                            <td data-value="78025368997"><%=contract.getCompany() %></td>
                                            <td data-value="78025368997"><%=contract.getRemark() %></td>
                                            <td data-value="1">
                                                <span class="status-metro status-active" title="Active"><%=tempValue %></span>
                                            </td>
                                        </tr>
                                    <%
                                    		}
                                    %>
                                        <!-- <tr>
                                            <td>H1</td>
                                            <td>合同1</td>
                                            <td>打印</td>
                                            <td data-value="78025368997">备注1</td>
                                            <td data-value="78025368997">备注1</td>
                                            <td data-value="1">
                                                <span class="status-metro status-disabled" title="Disabled">已删除</span>
                                            </td>
                                        </tr> -->
                                    </tbody>
                                    <tfoot>
                                        <tr>
                                            <td colspan="5">
                                                <div class="pagination pagination-centered"></div>
                                            </td>
                                        </tr>
                                    </tfoot>
                                </table>

                            </div>

                        </div>
                    </div>
					
                </div>
                

            </div>
            <%-- <div class="content-wrap">
                <div class="row">
                    <div class="col-sm-12">
                        <div class="nest" id="elementClose">
                            <div class="title-alt">
                                <h6>
                                    打印工程台账表</h6>
                                <div class="titleClose">
                                    <a class="gone" href="#elementClose">
                                        <span class="entypo-cancel"></span>
                                    </a>
                                </div>
                                <div class="titleToggle">
                                    <a class="nav-toggle-alt" href="#element">
                                        <span class="entypo-up-open"></span>
                                    </a>
                                </div>

                            </div>

                            <div class="body-nest" id="element">

                                <div class="panel-body">
                                    <form action="<%=path %>/webuser.do?method=printTaiZhang&projectId=<%=project.getId() %>" method="post" class="form-horizontal bucket-form">
                                        <div class="form-group">
                                        		<label class=" col-sm-3 control-label">选择台账排列方式</label>
                                        		<div class="col-lg-6">
		                                        <select class="filter-status form-control" name="order" >
		                                            <option value="time" >按报表时间
		                                            <option value="id">按合同编号
		                                        </select>
	                                        </div>
                                    		</div>
                                        
                                        <div class="form-group">
                                            <label class=" col-sm-3 control-label"></label>
                                            <div class="col-lg-6">
                                                <p class="form-control-static"><button class="btn btn-success" type="submit">生成台帐</button>
                                                </p>
                                            </div>
                                        </div>
                                        
                                    </form>
                                </div>

                            </div>

                        </div>
                    </div>
                </div>
            </div>--%>
            
            
			<div class="content-wrap">
                <div class="row">
                    <div class="col-sm-12">
                        <div class="nest" id="elementClose">
                            <div class="title-alt">
                                <h6>
                                    打印合同台账表</h6>
                                <div class="titleClose">
                                    <a class="gone" href="#elementClose">
                                        <span class="entypo-cancel"></span>
                                    </a>
                                </div>
                                <div class="titleToggle">
                                    <a class="nav-toggle-alt" href="#element">
                                        <span class="entypo-up-open"></span>
                                    </a>
                                </div>

                            </div>

                            <div class="body-nest" id="element">

                                <div class="panel-body">
                                    <form action="<%=path %>/webuser.do?method=printContract&projectId=<%=project.getId() %>" method="post" class="form-horizontal bucket-form">
                                        <div class="form-group">
                                        		<label class=" col-sm-3 control-label">选择台账排列方式</label>
                                        		<div class="col-lg-6">
		                                        <select class="filter-status form-control" name="order" >
		                                            <option value="time" >按报表时间
		                                            <option value="id">按合同编号
		                                        </select>
	                                        </div>
                                    		</div>
                                        
                                        <div class="form-group">
                                            <label class=" col-sm-3 control-label"></label>
                                            <div class="col-lg-6">
                                                <p class="form-control-static"><button class="btn btn-success" type="submit">生成台帐</button>
                                                </p>
                                            </div>
                                        </div>
                                        
                                    </form>
                                </div>

                            </div>

                        </div>
                    </div>
                </div>
            </div> 
            <!-- /END OF CONTENT -->



            <!-- FOOTER -->
            <div class="footer-space"></div>
            <div id="footer">
                <div class="devider-footer-left"></div>
                <div class="time">
                    <p id="spanDate">
                    <p id="clock">
                </div>
                <div class="copyright">Make with Love
                    <span class="entypo-heart"></span>Collect from <a href="＃" title="＃" target="_blank">成本管控</a> All Rights Reserved</div>
                <div class="devider-footer"></div>

            </div>
            <!-- / END OF FOOTER -->
        </div>
    </div>
    <!--  END OF PAPER WRAP -->

    <!-- RIGHT SLIDER CONTENT -->

    <!-- END OF RIGHT SLIDER CONTENT-->


    <!-- MAIN EFFECT -->
    <script type="text/javascript" src="assets/js/preloader.js"></script>
    <script type="text/javascript" src="assets/js/bootstrap.js"></script>
    <script type="text/javascript" src="assets/js/app.js"></script>
    <script type="text/javascript" src="assets/js/load.js"></script>
    <script type="text/javascript" src="assets/js/main.js"></script>


    <!-- /MAIN EFFECT -->
    <!-- GAGE -->
    <script type="text/javascript" src="assets/js/toggle_close.js"></script>
    <script src="assets/js/footable/js/footable.js?v=2-0-1" type="text/javascript"></script>
    <script src="assets/js/footable/js/footable.sort.js?v=2-0-1" type="text/javascript"></script>
    <script src="assets/js/footable/js/footable.filter.js?v=2-0-1" type="text/javascript"></script>
    <script src="assets/js/footable/js/footable.paginate.js?v=2-0-1" type="text/javascript"></script>
    <script src="assets/js/footable/js/footable.paginate.js?v=2-0-1" type="text/javascript"></script>

	<script type="text/javascript" src="assets/js/timepicker/bootstrap-timepicker.js"></script>
    <script type="text/javascript" src="assets/js/datepicker/bootstrap-datepicker.js"></script>
    <script type="text/javascript" src="assets/js/datepicker/clockface.js"></script>
    <script type="text/javascript" src="assets/js/datepicker/bootstrap-datetimepicker.js"></script>


    <script type="text/javascript">
    $(function() {
        $('.footable-res').footable();
    });
    </script>
    <script type="text/javascript">
    $('#datetimepicker1').datetimepicker({
        language: 'zh-CN'
    });
    $('#datetimepicker2').datetimepicker({
        language: 'zh-CN'
    });
    $('#dp1').datepicker()
    $('#dpYears').datepicker();
    $('#timepicker1').timepicker();
    $('#t1').clockface();
    $('#t2').clockface({
        format: 'HH:mm',
        trigger: 'manual'
    });

    $('#toggle-btn').click(function(e) {
        e.stopPropagation();
        $('#t2').clockface('toggle');
    });
    </script>
    <script type="text/javascript">
    $(function() {
    		
        $('#footable-res2').footable().bind('footable_filtering', function(e) {
            var selected = $('.filter-status').find(':selected').text();
            if (selected && selected.length > 0) {
                e.filter += (e.filter && e.filter.length > 0) ? ' ' + selected : selected;
                e.clear = !e.filter;
            }
        });
        $('#footable-res1').footable().bind('footable_filtering', function(e) {
            var selected = $('.filter-status').find(':selected').text();
            if (selected && selected.length > 0) {
                e.filter += (e.filter && e.filter.length > 0) ? ' ' + selected : selected;
                e.clear = !e.filter;
            }
        });
        $('#footable-res3').footable().bind('footable_filtering', function(e) {
            var selected = $('.filter-status').find(':selected').text();
            if (selected && selected.length > 0) {
                e.filter += (e.filter && e.filter.length > 0) ? ' ' + selected : selected;
                e.clear = !e.filter;
            }
        });
        $('#footable-res4').footable().bind('footable_filtering', function(e) {
            var selected = $('.filter-status').find(':selected').text();
            if (selected && selected.length > 0) {
                e.filter += (e.filter && e.filter.length > 0) ? ' ' + selected : selected;
                e.clear = !e.filter;
            }
        });
        $('#footable-res5').footable().bind('footable_filtering', function(e) {
            var selected = $('.filter-status').find(':selected').text();
            if (selected && selected.length > 0) {
                e.filter += (e.filter && e.filter.length > 0) ? ' ' + selected : selected;
                e.clear = !e.filter;
            }
        });
        $('#footable-res6').footable().bind('footable_filtering', function(e) {
            var selected = $('.filter-status').find(':selected').text();
            if (selected && selected.length > 0) {
                e.filter += (e.filter && e.filter.length > 0) ? ' ' + selected : selected;
                e.clear = !e.filter;
            }
        });

        $('.clear-filter').click(function(e) {
            e.preventDefault();
            $('.filter-status').val('');
            $('table.demo').trigger('footable_clear_filter');
        });

        $('.filter-status').change(function(e) {
            e.preventDefault();
            $('table.demo').trigger('footable_filter', {
                filter: $('#filter').val()
            });
        });

        $('.filter-api').click(function(e) {
            e.preventDefault();

            //get the footable filter object
            var footableFilter = $('table').data('footable-filter');

            alert('about to filter table by "tech"');
            //filter by 'tech'
            footableFilter.filter('tech');

            //clear the filter
            if (confirm('clear filter now?')) {
                footableFilter.clearFilter();
            }
        });
    });
    </script>

</body>

</html>

