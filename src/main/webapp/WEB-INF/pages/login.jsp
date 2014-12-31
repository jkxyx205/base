<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<body>
	<link rel="stylesheet" href="resources/css/signin.css">
    <div class="container">
      <form class="form-signin" role="form" action="testJqgrid">
        <h2 class="form-signin-heading">Please sign in</h2>
        <input class="form-control" placeholder="Username" value="admin" autofocus required>
        <input type="password" class="form-control" placeholder="Password" value="123456" required>
        <div class="checkbox">
          <label>
            <input type="checkbox" value="remember-me"> Remember me
          </label>
          <button type="button" class="btn btn-link" onclick="register();">Register</button>
        </div>
        
        <button class="btn btn-lg btn-primary btn-block" type="submit">Sign in</button>
      </form>

    </div> <!-- /container -->

<script>
	function register() {
		location.href=common.getBase() + '/register';
	}
</script>
    <!-- IE10 viewport hack for Surface/desktop Windows 8 bug -->
<!--     <script src="../../assets/js/ie10-viewport-bug-workaround.js"></script> -->
  </body>
</html>