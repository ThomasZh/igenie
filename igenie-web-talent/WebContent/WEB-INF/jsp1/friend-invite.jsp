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
<title>friend invite</title>

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
	<div class="container-fluid">
		<input type="hidden" id="hidden_ekey" value="${ekey}">
		<div class="row">
			<div class="col-xs-12 col-sm-12 col-md-12 photo-bg pad-tb50">
				<div class="my-photo mag-tb10">
					<img class="img-responsive img-circle center-block"
						src="http://tripc2c-person-face.b0.upaiyun.com/2/5/11/10427ef8-9e34-4bd5-9123-551d96953e73.jpg">
				</div>
				<h4 class="mag-tb10 txt-col-white text-center">沐雨骑行</h4>
				<h6 class="txt-col-fen text-center">${friendDesc}</h6>
			</div>

			<div class="container">
				<div class="row">
					<div class="col-xs-12 col-sm-12 col-md-12 act-mag">
						<div class="row">
							<h4 class="act-title col-xs-12 col-sm-12 col-md-12">
								<strong>邀请您加入活动！</strong>
							</h4>
						</div>

						<a href="http://planc2c.com/talent/vote-action.htm?ekey=${ekey}"
							class="btn btn-lg btn-success btn-block active">投他（她）一票</a>

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
									<span class="start-date col-gray">A计划: hang out with friends, no more Plan B</span>
								</h5>
							</div>
						</div>
					</div>
				</div>
			</div>

		</div>
	</div>
	<!-- jQuery (necessary for Bootstrap's JavaScript plugins)   <script src="http://cdn.bootcss.com/jquery/1.11.2/jquery.min.js"></script>-->

	<!-- Include all compiled plugins (below), or include individual files as needed    <script src="js/bootstrap.min.js"></script>-->
</body>
</html>