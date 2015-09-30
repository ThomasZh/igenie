<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport"
	content="width=device-width, initial-scale=1,user-scalable=no">
<!-- 上述3个meta标签*必须*放在最前面，任何其他内容都*必须*跟随其后！ -->
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<%@ page isELIgnored="false"%>
<title>活动邀请</title>

<!-- Bootstrap -->
<link rel="stylesheet" type="text/css" href="css/bootstrap-theme.css">
<link rel="stylesheet" type="text/css" href="css/bootstrap.css">
<link rel="stylesheet" type="text/css" href="css/common.css">

<!-- HTML5 shim and Respond.js for IE8 support of HTML5 elements and media queries -->
<!-- WARNING: Respond.js doesn't work if you view the page via file:// -->
<!--[if lt IE 9]>
    <script src="http://cdn.bootcss.com/html5shiv/3.7.2/html5shiv.min.js"></script>
    <script src="http://cdn.bootcss.com/respond.js/1.4.2/respond.min.js"></script>
<![endif]-->

</head>
<body>
	<div class="container">
		<div class="row">
			<c:if test="${'false' == approved}">
				<h2>等待审批！</h2>
			</c:if>
			<!-- if condition -->
			<c:if test="${'true' == approved}">
				<h2>加入活动成功！</h2>
			</c:if>		
		</div>

		<div class="row">
			<h4 class="act-title col-xs-12 col-sm-12 col-md-12">
				<strong></strong>
			</h4>
			<div class="col-xs-12 col-sm-12 col-md-12 times">
				<h5 class="W_fl item_ico">
					<em><a href="http://planc2c.com"><img
							src="http://tripc2c-default.b0.upaiyun.com/app/image/logo-28.png"
							width="25" height="25"></a></em>
				</h5>
				<h5>
					<span class="start-date col-gray">A计划: hang out with
						friends, no more Plan B</span>
				</h5>
			</div>
		</div>
	</div>
</body>
</html>