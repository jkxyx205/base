<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Insert title here</title>
</head>
<body>
<h1>This is index2 ${pageContext.request.contextPath}</h1>
</body>
<script type="text/javascript" src="resources/js/common/jquery-1.7.2.min.js"></script>
<script type="text/javascript" src="resources/js/common/common.js"></script>
<script type="text/javascript">
	<a href="${pageContext.request.contextPath}/quartz">quartz.jsp</a>
</script>
</html>