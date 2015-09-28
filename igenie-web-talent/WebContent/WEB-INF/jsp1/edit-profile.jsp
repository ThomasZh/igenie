<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1,user-scalable=no">
<!-- 上述3个meta标签*必须*放在最前面，任何其他内容都*必须*跟随其后！ -->
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<%@ page isELIgnored="false"%>
<title>edit profile</title>

<!-- Bootstrap -->
<link rel="stylesheet" type="text/css" href="css/bootstrap-theme.css">
<link rel="stylesheet" type="text/css" href="css/bootstrap.css">
<link rel="stylesheet" type="text/css" href="css/common.css">

<style type="text/css">
#cover {
	display: none;
	position: absolute;
	left: 0;
	top: 0;
	z-index: 18888;
	background-color: #000000;
	opacity: 0.7;
}

#guide {
	display: none;
	position: absolute;
	right: 18px;
	top: 5px;
	z-index: 19999;
}

#guide img {
	width: 300px;
	height: 175px;
}
</style>

<!-- HTML5 shim and Respond.js for IE8 support of HTML5 elements and media queries -->
<!-- WARNING: Respond.js doesn't work if you view the page via file:// -->
<!--[if lt IE 9]>
    <script src="http://cdn.bootcss.com/html5shiv/3.7.2/html5shiv.min.js"></script>
    <script src="http://cdn.bootcss.com/respond.js/1.4.2/respond.min.js"></script>
<![endif]-->

<body>
	${message}
	<div class="container">
		<form class="form-signin" action="my-friends.htm" commandName="signin">
			<h2 class="form-signin-heading">Please input information</h2>
			<label for="inputEmail" class="sr-only">Telephone</label>
			<input type="email" name="inputEmail" id="inputEmail" class="form-control" placeholder="telephone" required autofocus>
			<label for="inputPassword" class="sr-only">Wechat loginname</label>
			<input type="email" name="inputPassword" id="inputPassword" class="form-control" placeholder="wechat loginname" required>
			<label for="inputPassword" class="sr-only">Description</label>
			<input type="email" name="inputEmail" id="inputEmail" class="form-control" placeholder="description" required autofocus>
			<div class="checkbox">
				<label> <input type="checkbox" value="remember-me">
					订阅
				</label>
			</div>
			<button class="btn btn-lg btn-primary btn-block" type="submit">Done</button>
		</form>

	</div>
	<!-- /container -->

	<!-- IE10 viewport hack for Surface/desktop Windows 8 bug -->
	<script src="../../assets/js/ie10-viewport-bug-workaround.js"></script>
	<!-- jQuery (necessary for Bootstrap's JavaScript plugins)   <script src="http://cdn.bootcss.com/jquery/1.11.2/jquery.min.js"></script>-->
	<!-- Include all compiled plugins (below), or include individual files as needed    <script src="js/bootstrap.min.js"></script>-->
</body>
</html>