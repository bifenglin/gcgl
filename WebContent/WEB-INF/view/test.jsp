<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE html>
<html lang="en">

<head>
    <meta charset="utf-8">
    <title>彩票统计 0.1</title>
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





    <!-- HTML5 shim, for IE6-8 support of HTML5 elements -->
    <!--[if lt IE 9]>
        <script src="http://html5shim.googlecode.com/svn/trunk/html5.js"></script>
        <![endif]-->
    <!-- Fav and touch icons -->
    <link rel="shortcut icon" href="assets/ico/minus.png">
</head>

<body><div id="awwwards" class="right black"><a href="#" target="_blank">＃</a></div>
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
                <button class="navbar-toggle toggl-menu-mobile toggle-left" type="button">
                    <span class="entypo-list-add"></span>
                </button>




                <div id="logo-mobile" class="visible-xs">
                   <h1>彩票统计<span>v0.1</span></h1>
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
                            Hi, Admin <b class="caret"></b>
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
         <h1>彩票统计<span>v0.1</span></h1>
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
                            <a class="tooltip-tip ajax-load" href="index.html" title="Dashboard">
                                <i class="icon-window"></i>
                                <span>主页面</span>
                            </a>
                        </li>
                        

                        <li>
                            <a class="tooltip-tip " href="login.html" title="login">
                                <i class="icon-download"></i>
                                <span>退出</span>
                            </a>
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
                            <span>数据查看
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
                                <strong>Admin!</strong>&nbsp;&nbsp;
                            </div>


                        </div>
                    </div>
                    <div class="col-sm-2">
                        <div class="devider-vertical visible-lg"></div>
                        <div class="btn-group btn-wigdet pull-right visible-lg">
                            <div class="btn">
                                Widget</div>
                            <button data-toggle="dropdown" class="btn dropdown-toggle" type="button">
                                <span class="caret"></span>
                                <span class="sr-only">Toggle Dropdown</span>
                            </button>
                            <ul role="menu" class="dropdown-menu">
                                <li>
                                    <a href="#">
                                        <span class="entypo-plus-circled margin-iconic"></span>Add New</a>
                                </li>
                                <li>
                                    <a href="#">
                                        <span class="entypo-heart margin-iconic"></span>Favorite</a>
                                </li>
                                <li>
                                    <a href="#">
                                        <span class="entypo-cog margin-iconic"></span>Setting</a>
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
                <li><a href="#" title="Sample page 1">主页面</a>
                </li>
                
                <li class="pull-right">
                    <div class="input-group input-widget">

                        <input style="border-radius:15px" type="text" placeholder="Search..." class="form-control">
                    </div>
                </li>
            </ul>

            <!-- END OF BREADCRUMB -->



            <div class="content-wrap">
                <div class="row">
                    <div class="col-sm-12">
                        <div class="nest" id="FilteringClose">
                            <div class="title-alt">
                                <h6>
                                    输入数据</h6>
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
                            		<input tabindex="17" type="checkbox" id="checkbox5" >
                  				<label for="checkbox5">万</label>
                  				<input tabindex="17" type="checkbox" id="checkbox4" >
                  				<label for="checkbox4">千</label>
                  				<input tabindex="17" type="checkbox" id="checkbox3" >
                  				<label for="checkbox3">百</label>
                  				<input tabindex="17" type="checkbox" id="checkbox2" >
                  				<label for="checkbox2">十</label>
                  				<input tabindex="17" type="checkbox" id="checkbox1" >
                  				<label for="checkbox1">个</label>
                                <div class="form-group">
                                    <textarea placeholder="Comment" rows="10" class="form-update" id="data"></textarea>
                                </div>
                                <button type="submit" class="btn btn-info" id="submitData">提交</button>
                            </div>

                        </div>
                    </div>
             
					
                </div>
            </div>
			<div class="content-wrap">
                <div class="row">


                    <div class="col-sm-12">

                        <div class="nest" id="FootableClose">
                            <div class="title-alt">
                                <h6>
                                    数据</h6>
                                <div class="titleClose">
                                    <a class="gone" href="#FootableClose">
                                        <span class="entypo-cancel"></span>
                                    </a>
                                </div>
                                <div class="titleToggle">
                                    <a class="nav-toggle-alt" href="#Footable">
                                        <span class="entypo-up-open"></span>
                                    </a>
                                </div>

                            </div>

                            <div class="body-nest" id="Footable">

                                <p>数据</p>

                                <table class="table-striped footable-res footable metro-blue" data-page-size="6" id="mianData">
                                    <thead>
                                        <tr>
                                            <th>
                                                直选
                                            </th>
                                        </tr>
                                    </thead>
                                    <tbody>
                                        <tr id="zhixuan">
                                            <!--<td>112</td>
                                            <td>112</td>-->
                                        </tr>
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
                    <span class="entypo-heart"></span>Collect from <a href="＃" title="＃" target="_blank">彩票统计/a> All Rights Reserved</div>
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


    <script type="text/javascript">
    $(function() {
        $('.footable-res').footable();
    });
    </script>
    
    <script type="text/javascript">
function clone(obj){  
    var o;  
    switch(typeof obj){  
    case 'undefined': break;  
    case 'string'   : o = obj + '';break;  
    case 'number'   : o = obj - 0;break;  
    case 'boolean'  : o = obj;break;  
    case 'object'   :  
        if(obj === null){  
            o = null;  
        }else{  
            if(obj instanceof Array){  
                o = [];  
                for(var i = 0, len = obj.length; i < len; i++){  
                    o.push(clone(obj[i]));  
                }  
            }else{  
                o = {};  
                for(var k in obj){  
                    o[k] = clone(obj[k]);  
                }  
            }  
        }  
        break;  
    default:          
        o = obj;break;  
    }  
    return o;     
}  


    	
    </script>
    
    <script type="text/javascript">
    
    $(function() {
    	
    		$('#submitData').click(function(){
//  			alert($('#data').val());
    			data = $('#data').val();
    			var arry = data.split("\n");
    			alert("check");
    			var flag=0;
    			var checkArry = new Array;
    			if($('#checkbox1').is(':checked')) {
			    checkArry[flag]=5;
			    flag++;
			}
			if($('#checkbox2').is(':checked')) {
			    checkArry[flag]=4;
			    flag++;
			}
			if($('#checkbox3').is(':checked')) {
			    checkArry[flag]=3;
			    flag++;
			}
			if($('#checkbox4').is(':checked')) {
			    checkArry[flag]=2;
			    flag++;
			}
			if($('#checkbox5').is(':checked')) {
			    checkArry[flag]=1;
			    flag++;
			}
			var weishu = checkArry.length;
			var weishuzhi = 1;
			for(var i = 0; i < weishu; i++){
				weishuzhi = 10*weishuzhi;
			}

			
			var yilou=new Array();
			
			for(var i = 0; i<weishuzhi; i++){
				var temp = 0;
				var iString = "";
				var tempi = clone(i);
				var iweishu = 0;
				while(tempi>10){
					tempi =  parseInt(tempi/10);
					iweishu = iweishu + 1;
				}
				for(var j =1; j< weishu-iweishu; j++){
					iString += "0";
				}
				iString += i;
//					 iString = "00"+i;
//				} else if(i>=10 && i<100){
//					iString = "0"+i;
//				} else if(i> 100 && i<1000){
//					iString = ""+i;
//				}
				for(var l=0; l < arry.length; l++){
					var checkString = "";
					for(var k = 0; k< weishu; k++){
						checkString += arry[arry.length-l-1].substr(12+checkArry[weishu-k-1], 1);
					}
//	    				var a = arry[arry.length-l-1].substr(17, 1);
//	    				var b = arry[arry.length-l-1].substr(16, 1);
//	    				var c = arry[arry.length-l-1].substr(15, 1);
	    				if(checkString == iString){
	    					break;
	    				}
	    				temp++;
				}
				//遗漏的期数＝总期数－temp
				yilou[i] = arry.length - temp;
			}
			
			var chuxian=new Array();
			
			for(var i = 0; i<weishuzhi; i++){
				var temp = 0;
				var iString = "";
				var tempi = clone(i);
				var iweishu = 0;
				while(tempi>10){
					tempi =  parseInt(tempi/10);
					iweishu = iweishu + 1;
				}
				for(var j =1; j< weishu-iweishu; j++){
					iString += "0";
				}
				iString += i;
//					 iString = "00"+i;
//				} else if(i>=10 && i<100){
//					iString = "0"+i;
//				} else if(i> 100 && i<1000){
//					iString = ""+i;
//				}
				for(var l=0; l < arry.length; l++){
					var checkString = "";
					for(var k = 0; k< weishu; k++){
						checkString += arry[arry.length-l-1].substr(12+checkArry[weishu-k-1], 1);
//						checkString += k;
					}
//	    				var a = arry[arry.length-l-1].substr(17, 1);
//	    				var b = arry[arry.length-l-1].substr(16, 1);
//	    				var c = arry[arry.length-l-1].substr(15, 1);
	    				if(checkString == iString){
	    					temp++;
	    				}
	    				
				}
				//遗漏的期数＝总期数－temp
				chuxian[i] =  temp;
			}
			
			var yuchu = new Array;
			for(var i =0 ;i<weishuzhi; i++){
				yuchu[i] = (yilou[i] * chuxian[i])/weishuzhi;
			}
			
			var tempArry = new Array;
			tempArry = clone(yuchu);
			
			yuchu.sort();
			var finnalArry = new Array;
			
			for(var i = 0; i<weishuzhi; i++){
				var flag = 0;
				for(var j=0; j<weishuzhi; j++){
					if(yuchu[i] == tempArry[j]){
						finnalArry[i]=j;
						flag = 1;
						break;
					}
				}
				if(flag == 0){
					finnalArry[i] = 0;
				}
			}
			
			var outArry = new Array;
			var flag = 0;
			for(var i = 0; i<finnalArry.length; i++){
				if(finnalArry[i]!=0 && finnalArry[i]!= 1){
					outArry[flag] = finnalArry[i];
					flag++;
				}
			}
			
			var table1 = $('#mianData'); 
			
			for(var i = outArry.length-1 ; i>= 0; i-- ){
				var firstTr = table1.find('tbody>tr:first'); 
				var row = $("<tr></tr>"); 
				var td = $("<td></td>"); 
				td.append(outArry[i]); 
				row.append(td); 
				table1.append(row); 
			}
			
			
			
    		});
    	
        $('#footable-res2').footable().bind('footable_filtering', function(e) {
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

