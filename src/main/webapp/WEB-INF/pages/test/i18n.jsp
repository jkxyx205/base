<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    
<%@ taglib uri="http://www.springframework.org/tags" prefix="s"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Insert title here</title>
<style type="text/css">
/*validate css*/
table.tb_validate td {
	vertical-align: top;
}

label.error {
  color: #a94442;
  display: block;
}

input.error {
 border-color: #a94442;
 padding: 1px;
 margin: 2px;
}

/*validate css end*/
</style>

</head>
<body>
<h1>This is index2 ${pageContext.request.contextPath}</h1>

<div style='margin:0 auto; width:500px'>
	<form id="login">
		<table class="tb_validate">
			<tr>
				<td><label for="username"><s:message code="username" arguments="(placeHolder)"/>:</label></td>
				<td><input name="username" id="username" type="text"></td>
			</tr>
			<tr>
				<td><label for="password"><s:message code="password"/>:</label></td>
				<td><input name="password" id="password" type="password"></td>
			</tr>
			<tr>
				<td><input id="btn_submit" type="button" value="<s:message code="submit"/>"></td>
				<td><input id="btn_reset" type="reset" value="<s:message code="reset"/>"></td>
			</tr>
			<tr>
				<td colspan="2"><a href="?lang=en">English</a>&nbsp;&nbsp;<a href="?lang=zh">中文简体</a>&nbsp;&nbsp;<a href="?lang=hk">中文繁体</a>
				&nbsp;&nbsp;<a href="?lang=fr">法语</a></td>
			</tr>
		</table>
	</form>
	<s:message code="upload" />
</div>

</body>
<script type="text/javascript" src="resources/js/common/jquery-1.7.2.min.js"></script>
<script type="text/javascript" src="resources/js/common/jquery.cookie.js"></script>
<script type="text/javascript" src="resources/js/common/jquery.form.js"></script>
<script type="text/javascript" src="resources/js/common/jquery.validate.js"></script>
<script type="text/javascript" src="resources/js/common/lang.js"></script>
<script type="text/javascript">
	$(function() {
		//get i18n message from js(lang.js)
		//alert(I18nUtil.getMessageByCode("username"));

		
		//add validator
		$("#login").validate({
			rules: {
				username:"required",
				password:{
					required:true,
					minlength:3
				}
			}
		});
		
		$("#btn_submit").click(function() {
			 $("#login").ajaxSubmit(
			    {
				 url:"login",
				 beforeSubmit:function() {
					return $("#login").valid();
				 },
				 resetForm:true,
				 success:function(data) {
	 				alert(data);
				}});
		});
	});
</script>

</html>