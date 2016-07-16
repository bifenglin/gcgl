<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ include file="/include/import.jsp"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
List<String> units = (List)session.getAttribute("units");
Project project = (Project) session.getAttribute("project");
String msg = (String) request.getAttribute("msg");
User user = (User) session.getAttribute("user");
List<Project> projects = (List<Project>)session.getAttribute("projects");
SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh-mm-ss");
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

    <link href="assets/js/colorPicker/bootstrap-colorpicker.css" rel="stylesheet">
    <link href="assets/js/validate/validate.css" rel="stylesheet">
    <link href="assets/js/idealform/css/jquery.idealforms.css" rel="stylesheet">

    <link href="assets/js/footable/css/footable.core.css?v=2-0-1" rel="stylesheet" type="text/css">
    <link href="assets/js/footable/css/footable.standalone.css" rel="stylesheet" type="text/css">
    <link href="assets/js/footable/css/footable-demos.css" rel="stylesheet" type="text/css">

    <link rel="stylesheet" href="assets/js/dataTable/lib/jquery.dataTables/css/DT_bootstrap.css">
    <link rel="stylesheet" href="assets/js/dataTable/css/datatables.responsive.css">

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
                                <strong><%=user.getTruename() %>!</strong>&nbsp;&nbsp;
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
                <li><a href="<%=path %>/webuser.do?method=goUserProject&projectId=<%=project.getId() %>" title="Sample page 1"><%=project.getName() %></a>
                </li>
                <li class="pull-right">
                    <div class="input-group input-widget">

                        <input style="border-radius:15px" type="text" placeholder="Search..." class="form-control">
                    </div>
                </li>
            </ul>

            <!-- END OF BREADCRUMB -->
  <%if(!msg.equals("")){ %>
            <div class="content-wrap">
                <div class="row">
                    <div class="col-sm-12">

                        <div class="nest" id="alertClose">
                            <div class="title-alt">
                                <h6>
                                    </h6>
                                <div class="titleClose">
                                    <a class="gone" href="#alertClose">
                                        <span class="entypo-cancel"></span>
                                    </a>
                                </div>
                                <div class="titleToggle">
                                    <a class="nav-toggle-alt" href="#alert">
                                        <span class="entypo-up-open"></span>
                                    </a>
                                </div>

                            </div>

                            <div class="body-nest" id="alert">

                                <div class="alert alert-danger">
                                    <button data-dismiss="alert" class="close" type="button">×</button>
                                    <span class="entypo-attention"></span>
                                    <strong> <%=msg %></strong>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
	<%} %>
	 		<div class="content-wrap">
                <div class="row">
                    <div class="col-sm-12">
                        <div class="nest" id="elementClose">
                            <div class="title-alt">
                                <h6>
                                    生成产值结算表</h6>
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
                                    <form action="<%=path %>/webuser.do?method=printValuation" method="post" class="form-horizontal bucket-form">
                                    		<div class="form-group">
                                            <label class=" col-sm-3 control-label">工程名</label>
                                            <div class="col-lg-6">
                                                <p class="form-control-static"><%=project.getName() %>
                                                </p>
                                            </div>
                                        </div>
                                     
                                        <div class="form-group">
                                        		<label class=" col-sm-3 control-label">开始时间</label>
                                        		<div class="col-lg-6">
		                                        <div id="datetimepicker1" class="input-group date">
		                                            <input class="form-control" name="startDate" data-format="yyyy-MM-dd hh:mm:ss" type="text">
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
		                                            <input class="form-control" name="endDate" data-format="yyyy-MM-dd hh:mm:ss" type="text">
		
		                                            <span class="input-group-addon add-on">
		                                                <i style="font-style:normal;" data-time-icon="entypo-clock" data-date-icon="entypo-calendar">
		      											</i>
		                                            </span>
		                                        </div>
	                                        </div>
                                    		</div>
                                        <div class="form-group">
                                            <label class="col-sm-3 control-label">单位工程范围</label>
                                            <div class="col-sm-6">
                                                <div class="nest" id="FilteringClose">
                            

						                            <div class="body-nest" id="Filtering">
						                                <div class="row" style="margin-bottom:10px;">
						                                    <div class="col-sm-4">
						                                        <input class="form-control" id="filter" placeholder="查询" type="text">
						                                    </div>
						                                    <div class="col-sm-2">
						                                        <!-- <select class="filter-status form-control">
						                                            <option value="active">开启中
						                                            <option value="suspended">已停止
						                                            <option value="disabled">已删除
						                                        </select> -->
						                                    </div>
						                                    <div class="col-sm-6">
						                                        <a href="#clear" style="margin-left:10px;" class="pull-right btn btn-info clear-filter" title="clear filter">重置</a>
						                                    </div>
						
						                                </div>
						
						                                <table id="footable-res2" class="demo" data-filter="#filter" data-filter-text-only="true" data-page-size="10">
						                                    <thead>
						                                        <tr>
						                                            <th data-toggle="true">
						                                                编号
						                                            </th>
						                                            <th>
						                                                单位工程名称
						                                            </th>
						                                            <th data-hide="phone">
						                                            		状态
						                                            </th>
						                                        </tr>
						                                    </thead>	
						                                    <tbody>
						                                    
						                                    <%
						                                    int i=0;
						                                    for(String unit : units){ 
						                                    		i++;
						                                    		String tempValue = "空";
						                                    %>
						                                        <tr>
						                                            <td>
						                                            		
					                                                    	<input tabindex="13" type="checkbox" name="choice" value="<%=unit %>" id="<%=unit %>">
					                                                		
						                                            		<%=i %>
					                                            		</td>
						                                            <td>
						                                            		<%if(unit == null) { 
						                                            			out.print(tempValue);
						                                            		}else{
						                                            			out.print(unit);
						                                            		}
						                                            		%>
						                                            </td>
						                                        </tr>
						                                    <%} %>
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
                                        <div class="form-group">
                                            <label class=" col-sm-3 control-label"></label>
                                            <div class="col-lg-6">
                                                <p class="form-control-static"><button class="btn btn-success" type="submit">生成报表</button>
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
			
			<%-- <div class="content-wrap">
                <div class="row">
                    <div class="col-sm-12">
                        <div class="nest" id="FilteringClose">
                            <div class="title-alt">
                                <h6>
                                    上次结算时间</h6>
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
                                    <!-- <div class="col-sm-2">
                                        <select class="filter-status form-control">
                                            <option value="active">正常
                                            <option value="disabled">锁定
                                        </select>
                                    </div> -->
                                    <div class="col-sm-6">
                                        <a href="#clear" style="margin-left:10px;" class="pull-right btn btn-info clear-filter" title="clear filter">重置</a>
                                        <!-- <a href="#api" class="pull-right btn btn-info filter-api" title="Filter using the Filter API">查询</a> -->
                                    </div>

                                </div>

                                <table id="footable-res3" class="demo" data-filter="#filter" data-filter-text-only="true" data-page-size="10">
                                    <thead>
                                        <tr>
                                            <th data-toggle="true">
                                                合同项编号
                                            </th>
                                            <th>
                                                名称
                                            </th>
                                            <th>
                                                结算时间
                                            </th>
                                            <th>
                                                结算金额
                                            </th>
                                        </tr>
                                    </thead>	
                                    <tbody>
                                    <%
                                    for(Rtp rtp : topRtps){
                                    
                                    %>
                                        <tr>
                                            <td><%=rtp.getPropertyId() %></td>
                                            <td><%=rtp.getPropertyName() %>
                                            <td><%=sdf.format(new Date(Long.parseLong(rtp.getStartDate()))) %>到<%=sdf.format(new Date(Long.parseLong(rtp.getEndDate()))) %></td>
                                            <td><%=rtp.getCost() %></td>
                                        </tr>
                                    <%
                                    }
                                    %>
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
 --%>

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

    <script type="text/javascript" src="assets/js/colorPicker/bootstrap-colorpicker.min.js"></script>
    <script type="text/javascript" src="assets/js/inputMask/jquery.maskedinput.js"></script>
    <script type="text/javascript" src="assets/js/switch/bootstrap-switch.js"></script>
    <script type="text/javascript" src="assets/js/validate/jquery.validate.min.js"></script>
    <script type="text/javascript" src="assets/js/idealform/jquery.idealforms.js"></script>

    <script type="text/javascript" src="assets/js/timepicker/bootstrap-timepicker.js"></script>
    <script type="text/javascript" src="assets/js/datepicker/bootstrap-datepicker.js"></script>
    <script type="text/javascript" src="assets/js/datepicker/clockface.js"></script>
    <script type="text/javascript" src="assets/js/datepicker/bootstrap-datetimepicker.js"></script>


    <script type="text/javascript" src="assets/js/tag/jquery.tagsinput.js"></script>

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
    <script>
    $(document).ready(function() {
        //Validation
        $('#contact-form').validate({
            rules: {
                name: {
                    minlength: 2,
                    required: true
                },
                pwd: {
                    minlength: 2,
                    required: true
                }
            },
            highlight: function(element) {
                $(element).closest('.control-group').removeClass('success').addClass('error');
            },
            success: function(element) {
                element
                    .text('通过!').addClass('valid')
                    .closest('.control-group').removeClass('error').addClass('success');
            }
        });

        // MASKED INPUT

        $("#date").mask("99/99/9999", {
            completed: function() {
                alert("Your birthday was: " + this.val());
            }
        });
        $("#phone").mask("(999) 999-9999");

        $("#money").mask("99.999.9999", {
            placeholder: "*"
        });
        $("#ssn").mask("99--AAA--9999", {
            placeholder: "*"
        });

    });
    </script>

    <!-- GAGE -->
    <script type="text/javascript" src="assets/js/toggle_close.js"></script>
    <script src="assets/js/footable/js/footable.js?v=2-0-1" type="text/javascript"></script>
    <script src="assets/js/footable/js/footable.sort.js?v=2-0-1" type="text/javascript"></script>
    <script src="assets/js/footable/js/footable.filter.js?v=2-0-1" type="text/javascript"></script>
    <script src="assets/js/footable/js/footable.paginate.js?v=2-0-1" type="text/javascript"></script>
    <script src="assets/js/footable/js/footable.paginate.js?v=2-0-1" type="text/javascript"></script>


    <script type="text/javascript">
    $(function() {
        $('.footable-res').footable();
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
        $('#footable-res3').footable().bind('footable_filtering', function(e) {
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
