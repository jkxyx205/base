<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    
<%@ taglib uri="http://www.springframework.org/tags" prefix="s"%>
<%@ taglib  uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1">
<base href="<%=basePath %>" >
<title>
        <sitemesh:write property="title" /> 
</title>
<!-- 新 Bootstrap 核心 CSS 文件 -->
<link rel="stylesheet" href="resources/plugin/bootstrap/css/bootstrap.min.css">
<!-- 可选的Bootstrap主题文件（一般不用引入） -->
<link rel="stylesheet" href="resources/plugin/bootstrap/css/bootstrap-theme.min.css">

<link rel="stylesheet" href="resources/plugin/bootstrap_select/css/bootstrap-multiselect.css" type="text/css">

<link rel="stylesheet" href="resources/plugin/bootstrap_datetimepicker/css/bootstrap-datetimepicker.min.css" media="screen">

<!-- css jqgrid start-->
<link rel="stylesheet"  href="resources/plugin/jqueryUI/jquery-ui.min.css" />
<link rel="stylesheet"  href="resources/plugin/jqGrid/css/ui.jqgrid.css" />

<link rel="stylesheet" href="resources/plugin/bootstrap_validate/css/formValidation.min.css" media="screen">

<link href="resources/plugin/jquery-loadmask-0.4/jquery.loadmask.css" rel="stylesheet" type="text/css" />

<link rel="stylesheet" href="resources/css/common/bootstrap_custom.css" media="screen">

<!-- <link href="http://www.gbtags.com/gb/networks/uploads/245f40e7-2d25-4346-ac05-88d96b032fc7/css/bootstrap.min.css" rel="stylesheet"> -->
<!-- <link href="http://www.gbtags.com/gb/networks/uploads/245f40e7-2d25-4346-ac05-88d96b032fc7/css/font-awesome.css" rel="stylesheet"> -->
<!-- <link href="http://www.gbtags.com/gb/networks/uploads/245f40e7-2d25-4346-ac05-88d96b032fc7/css/custom.css" rel="stylesheet"> -->

</head>
<body>
<script type="text/javascript" src="resources/js/common/jquery-1.11.0.min.js"></script>

<div class="container" id="w_container">
	<div class="page-header">
        
		  <div class="row">
		  <div class="col-md-4 col-md-offset-8">
		  	<table>
			<tr>
				<td colspan="2"><a href="?lang=en">English</a>&nbsp;&nbsp;<a href="?lang=zh">中文简体</a>&nbsp;&nbsp;<a href="?lang=hk">中文繁體</a>
				&nbsp;&nbsp;<a href="?lang=fr">Français</a></td>
			</tr>
			</table>
		  </div>
		</div>
		
		
		<hr/>
		<sitemesh:write property='body' />
    </div>
</div>  
<script type="text/javascript" src="resources/plugin/jquery-loadmask-0.4/jquery.loadmask.min.js"></script>
<script>
	$("#w_container").mask("Waiting...");
</script>  
<center>Copyright © 2014-2015, Rick.Xu/jkxyx205@163.com All Rights Reserved</center>

<!-- js jqgrid start-->
<script type="text/javascript" src="resources/plugin/jqueryUI/jquery-ui.min.js"></script>
	<!-- i18n -->
<script type="text/javascript" src="resources/plugin/jqGrid/js/i18n/grid.locale-${sessionScope.lang }.js"></script>
<script type="text/javascript" src="resources/plugin/jqGrid/js/jquery.jqGrid.src.js"></script>
<!-- js jqgrid end-->

<script type="text/javascript" src="resources/js/common/jquery.cookie.js"></script>
<script type="text/javascript" src="resources/js/common/jquery.form.js"></script>
<script type="text/javascript" src="resources/js/common/jquery.form2json.js"></script>
<script type="text/javascript" src="resources/js/common/json2.js"></script>
<script type="text/javascript" src="resources/js/common/jquery.placeholder.js"></script>


<script type="text/javascript" src="resources/js/common/lang.js"></script>

<script src="resources/plugin/bootstrap/js/bootstrap.min.js"></script>
<script type="text/javascript" src="resources/plugin/bootstrap_select/js/bootstrap-multiselect.js"></script>

<script type="text/javascript" src="resources/plugin/bootstrap_datetimepicker/js/bootstrap-datetimepicker.min.js" charset="UTF-8"></script>
<script type="text/javascript" src="resources/plugin/bootstrap_datetimepicker/js/locales/bootstrap-datetimepicker.${sessionScope.lang }.js" charset="UTF-8"></script>

<script type="text/javascript" src="resources/plugin/bootstrap_validate/js/formValidation.js" charset="UTF-8"></script>
<script type="text/javascript" src="resources/plugin/bootstrap_validate/js/framework/bootstrap.min.js" charset="UTF-8"></script> 
<script type="text/javascript" src="resources/plugin/bootstrap_validate/js/language/${sessionScope.lang }.js" charset="UTF-8"></script> 

<script type="text/javascript" src="resources/js/common/dictionary.js"></script>

<script type="text/javascript" src="resources/js/common/common.js"></script>
</body>
</html>