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

<link href="talent/css/style.css" rel="stylesheet" />

<body>
	<input type="hidden" id="hidden_ekey" value="${ekey}">
	<div class="wrap">
		<div id="header">
			<p class="plan left">A</p>
			<font class="left">A计划</font> <a href="javascript:;" class="right">下载APP</a>
		</div>
	</div>
	<div id="content clearfix">
		<div class="banner">
			<img src="talent/images/banner.jpg" />
		</div>
		<strong>邀请您加入活动！</strong>
		<div class="txt_con">
			<p class="alignC">
				<a
					href="https://open.weixin.qq.com/connect/oauth2/authorize?appid=wxaa328c83d3132bfb&redirect_uri=http://planc2c.com/talent/join-action.htm?ekey=${ekey}&response_type=code&scope=snsapi_userinfo&state=1#wechat_redirect"
					class="def_btn">加入封神榜</a>
			</p>
		</div>
	</div>

</body>
</html>