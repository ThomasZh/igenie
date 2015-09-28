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

<script src="http://res.wx.qq.com/open/js/jweixin-1.0.0.js"></script>
<script type="text/javascript" language="javascript">
	function is_weixin() {
		var ua = navigator.userAgent.toLowerCase();
		if (ua.match(/MicroMessenger/i) == "micromessenger") {
			return true;
		} else {
			return false;
		}
	}

	var browser = {
		versions : function() {
			var u = navigator.userAgent;
			var app = navigator.appVersion;
			return {
				trident : u.indexOf('Trident') > -1,
				presto : u.indexOf('Presto') > -1,
				webKit : u.indexOf('AppleWebKit') > -1,
				gecko : u.indexOf('Gecko') > -1 && u.indexOf('KHTML') == -1,
				mobile : !!u.match(/AppleWebKit.*Mobile.*/),
				ios : !!u.match(/\(i[^;]+;( U;)? CPU.+Mac OS X/),
				android : u.indexOf('Android') > -1 || u.indexOf('Linux') > -1,
				iPhone : u.indexOf('iPhone') > -1,
				iPad : u.indexOf('iPad') > -1,
				webApp : u.indexOf('Safari') == -1
			}
		}(),
		language : (navigator.browserLanguage || navigator.language)
				.toLowerCase()
	};

	function init() {
		var ekey = document.getElementById("hidden_ekey").value;
		//下载页div
		var downloadDiv = document.getElementById("downloadBlock");
		var guideDiv = document.getElementById("guide");

		if (is_weixin()) {
			//weixin为提示使用浏览器打开的div
			if (browser.versions.ios || browser.versions.iPhone
					|| browser.versions.iPad) {
				var imageControl = document.createElement("img");
				imageControl
						.setAttribute("src",
								"http://tripc2c-default.b0.upaiyun.com/app/image/wechat-tips-ios.png");
				guideDiv.appendChild(imageControl);

				var buttonControl = document.createElement("input");
				buttonControl.setAttribute("type", "Submit");
				buttonControl.setAttribute("class",
						"btn btn-lg btn-primary btn-block active");
				buttonControl.setAttribute("onclick",
						"javascript:_system._guide(true)");
				buttonControl.setAttribute("value", "下载iOS客户端");
				downloadDiv.appendChild(buttonControl);

				var buttonControl3 = document.createElement("input");
				buttonControl3.setAttribute("type", "Submit");
				buttonControl3.setAttribute("class",
						"btn btn-lg btn-primary btn-block active");
				buttonControl3.setAttribute("onclick",
						"javascript:_system._guide(true)");
				buttonControl3.setAttribute("value", "打开iOS客户端");
				downloadDiv.appendChild(buttonControl3);
			} else if (browser.versions.android) {
				var imageControl = document.createElement("img");
				imageControl
						.setAttribute("src",
								"http://tripc2c-default.b0.upaiyun.com/app/image/wechat-tips-android.png");
				guideDiv.appendChild(imageControl);

				var buttonControl = document.createElement("input");
				buttonControl.setAttribute("type", "Submit");
				buttonControl.setAttribute("class",
						"btn btn-lg btn-primary btn-block active");
				buttonControl.setAttribute("onclick",
						"javascript:_system._guide(true)");
				buttonControl.setAttribute("value", "下载Android客户端");
				downloadDiv.appendChild(buttonControl);

				var buttonControl3 = document.createElement("input");
				buttonControl3.setAttribute("type", "Submit");
				buttonControl3.setAttribute("class",
						"btn btn-lg btn-primary btn-block active");
				buttonControl3.setAttribute("onclick",
						"javascript:_system._guide(true)");
				buttonControl3.setAttribute("value", "打开Android客户端");
				downloadDiv.appendChild(buttonControl3);
			} else {
				var imageControl = document.createElement("img");
				imageControl.setAttribute("src", "img/wechat-tips-android.png");
				guideDiv.appendChild(imageControl);

				var buttonControl = document.createElement("input");
				buttonControl.setAttribute("type", "Submit");
				buttonControl.setAttribute("class",
						"btn btn-lg btn-primary btn-block active");
				buttonControl.setAttribute("onclick",
						"javascript:_system._guide(true)");
				buttonControl.setAttribute("value", "下载iOS客户端");
				downloadDiv.appendChild(buttonControl);

				var buttonControl3 = document.createElement("input");
				buttonControl3.setAttribute("type", "Submit");
				buttonControl3.setAttribute("class",
						"btn btn-lg btn-primary btn-block active");
				buttonControl3.setAttribute("onclick",
						"javascript:_system._guide(true)");
				buttonControl3.setAttribute("value", "下载Android客户端");
				downloadDiv.appendChild(buttonControl3);
			}
		} else {
			if (browser.versions.ios || browser.versions.iPhone
					|| browser.versions.iPad) {
				var buttonControl = document.createElement("a");
				buttonControl.setAttribute("class",
						"btn btn-lg btn-primary btn-block active");
				buttonControl.setAttribute("href",
						"https://appsto.re/cn/JcMV3.i");
				buttonControl.innerHTML = "下载iOS客户端";
				downloadDiv.appendChild(buttonControl);

				var buttonControl2 = document.createElement("a");
				buttonControl2.setAttribute("class",
						"btn btn-lg btn-primary btn-block active");
				buttonControl2.setAttribute("href", "Aplan://fm?ekey=" + ekey);
				buttonControl2.innerHTML = "打开iOS客户端";
				downloadDiv.appendChild(buttonControl2);
			} else if (browser.versions.android) {
				var buttonControl = document.createElement("a");
				buttonControl.setAttribute("class",
						"btn btn-lg btn-primary btn-block active");
				buttonControl
						.setAttribute("href",
								"http://tripc2c-default.b0.upaiyun.com/app/install/Aplan.apk");
				buttonControl.innerHTML = "下载Android客户端";
				downloadDiv.appendChild(buttonControl);

				var buttonControl2 = document.createElement("a");
				buttonControl2.setAttribute("class",
						"btn btn-lg btn-primary btn-block active");
				buttonControl2.setAttribute("href", "Aplan://fm?ekey=" + ekey);
				buttonControl2.innerHTML = "打开Android客户端";
				downloadDiv.appendChild(buttonControl2);
			} else {
				var buttonControl = document.createElement("a");
				buttonControl.setAttribute("class",
						"btn btn-lg btn-primary btn-block active");
				buttonControl.setAttribute("href",
						"https://appsto.re/cn/JcMV3.i");
				buttonControl.innerHTML = "下载iOS客户端";
				downloadDiv.appendChild(buttonControl);

				var buttonControl2 = document.createElement("a");
				buttonControl2.setAttribute("class",
						"btn btn-lg btn-primary btn-block active");
				buttonControl2
						.setAttribute("href",
								"http://tripc2c-default.b0.upaiyun.com/app/install/Aplan.apk");
				buttonControl2.innerHTML = "下载Android客户端";
				downloadDiv.appendChild(buttonControl2);
			}
		}
	}
</script>
</head>

<body>
	<input type="hidden" id="hidden_ekey" value="${ekey}">

	<c:if test="${'notExist' == rs}">This invite not exist!
	</c:if>
	<!-- if condition -->
	<c:if test="${'ok' == rs}">
		<div class="container-fluid">
			<div class="row">
				<div class="col-xs-12 col-sm-12 col-md-12 photo-bg pad-tb50">
					<div class="my-photo mag-tb10">
						<c:if test="${leaderImageUrl == null}">
							<img class="img-responsive img-circle center-block"
								src="http://tripc2c-default.b0.upaiyun.com/app/image/avatar.png">
						</c:if>
						<c:if test="${leaderImageUrl != null}">
							<img class="img-responsive img-circle center-block"
								src="${leaderImageUrl}">
						</c:if>
					</div>
					<h4 class="mag-tb10 txt-col-white text-center">${leaderName}</h4>
					<h6 class="txt-col-fen text-center">${leaderDesc}</h6>
				</div>

				<div class="container">
					<div class="row">
						<div class="col-xs-12 col-sm-12 col-md-12 act-mag">
							<div class="row">
								<h4 class="act-title col-xs-12 col-sm-12 col-md-12">
									<strong>${activityName}</strong>
								</h4>
								<div class="col-xs-12 col-sm-12 col-md-12 times">
									<h5 class="W_fl item_ico">
										<em><img
											src="http://tripc2c-default.b0.upaiyun.com/app/icon/cal.png"
											width="25" height="25"></em>
									</h5>
									<h5>
										<span class="start-date col-gray">${startTime}</span> <span
											class="col-gray">-</span> <span class="end-date col-gray">${endTime}</span>
									</h5>
								</div>
								<c:if test="${locDesc != null}">
									<div class="col-xs-12 col-sm-12 col-md-12 times">
										<h5 class="W_fl item_ico">
											<em><img
												src="http://tripc2c-default.b0.upaiyun.com/app/icon/location.png"
												width="25" height="25"></em>
										</h5>
										<h5>
											<span class="start-date col-gray">${locDesc}</span>
										</h5>
									</div>
								</c:if>
								<div class="act-images">
									<c:forEach items="${descs}" var="desc" varStatus="stat">
										<h4 class="act-title col-xs-12 col-sm-12 col-md-12">
											<strong>${desc.title}</strong>
										</h4>
										<c:forEach items="${desc.cells}" var="cell" varStatus="stat">
											<div class=" col-xs-12 col-sm-6 col-md-4">
												<c:if test="${cell.type == '0'}">
													<span class="start-date col-gray">${cell.txt}</span>
													<p></p>
												</c:if>
												<c:if test="${cell.type == '1'}">
													<img class="img-responsive" src="${cell.txt}">
													<p></p>
												</c:if>
											</div>
										</c:forEach>
									</c:forEach>
								</div>
								<p></p>
							</div>
						</div>
					</div>

					<div class="row">
						<div id="downloadBlock" class="col-xs-12 col-sm-12 col-md-12">
							<c:if test="${applyFormType == '0'}">
								<a
									href="https://open.weixin.qq.com/connect/oauth2/authorize?appid=wxaa328c83d3132bfb&redirect_uri=http://planc2c.com/m/invite/join.htm?id=${id}&response_type=code&scope=snsapi_userinfo&state=1#wechat_redirect"
									class="btn btn-lg btn-success btn-block active">加入</a>
							</c:if>
							<!-- if condition -->
							<c:if test="${applyFormType == '1'}">
								<a
									href="https://open.weixin.qq.com/connect/oauth2/authorize?appid=wxaa328c83d3132bfb&redirect_uri=http://planc2c.com/m/invite/apply.htm?id=${id}&response_type=code&scope=snsapi_userinfo&state=1#wechat_redirect"
									class="btn btn-lg btn-success btn-block active">报名</a>
							</c:if>
							<!-- if condition -->
						</div>
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
				<!-- container -->
			</div>
		</div>
		<!-- jQuery (necessary for Bootstrap's JavaScript plugins)
		   <script src="http://cdn.bootcss.com/jquery/1.11.2/jquery.min.js"></script>-->

		<!-- Include all compiled plugins (below), or include individual files as needed
		    <script src="js/bootstrap.min.js"></script>-->

	</c:if>
	<!-- if condition -->

	<div id="cover"></div>
	<div id="guide"></div>

	<script type="text/javascript" language="javascript">
		var _system = {
			$ : function(id) {
				return document.getElementById(id);
			},
			_client : function() {
				return {
					w : document.documentElement.scrollWidth,
					h : document.documentElement.scrollHeight,
					bw : document.documentElement.clientWidth,
					bh : document.documentElement.clientHeight
				};
			},
			_scroll : function() {
				return {
					x : document.documentElement.scrollLeft ? document.documentElement.scrollLeft
							: document.body.scrollLeft,
					y : document.documentElement.scrollTop ? document.documentElement.scrollTop
							: document.body.scrollTop
				};
			},
			_cover : function(show) {
				if (show) {
					this.$("cover").style.display = "block";
					this.$("cover").style.width = (this._client().bw > this
							._client().w ? this._client().bw : this._client().w)
							+ "px";
					this.$("cover").style.height = (this._client().bh > this
							._client().h ? this._client().bh : this._client().h)
							+ "px";
				} else {
					this.$("cover").style.display = "none";
				}
			},
			_guide : function(click) {
				this._cover(true);
				this.$("guide").style.display = "block";
				this.$("guide").style.top = (_system._scroll().y + 5) + "px";
				window.onresize = function() {
					_system._cover(true);
					_system.$("guide").style.top = (_system._scroll().y + 5)
							+ "px";
				};
				if (click) {
					_system.$("cover").onclick = function() {
						_system._cover();
						_system.$("guide").style.display = "none";
						_system.$("cover").onclick = null;
						window.onresize = null;
					};
				}
			},
			_zero : function(n) {
				return n < 0 ? 0 : n;
			}
		};

		init();
	</script>

</body>
</html>