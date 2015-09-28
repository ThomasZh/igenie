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
<title>talent profile</title>

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

<meta charset="utf-8">
<link rel="shortcut icon" type="image/ico"
	href="http://www.datatables.net/favicon.ico">
<meta name="viewport" content="initial-scale=1.0, maximum-scale=2.0">

<title>神人</title>
<link rel="stylesheet" type="text/css"
	href="../DataTables-1.10.6/media/css/jquery.dataTables.css">
<link rel="stylesheet" type="text/css"
	href="../DataTables-1.10.6/examples/resources/syntax/shCore.css">
<link rel="stylesheet" type="text/css"
	href="../DataTables-1.10.6/examples/resources/demo.css">
<link rel="stylesheet" type="text/css"
	href="../Responsive-1.0.6/css/dataTables.responsive.css">
<style type="text/css" class="init">
</style>
<script type="text/javascript" language="javascript"
	src="../DataTables-1.10.6/media/js/jquery.js"></script>
<script type="text/javascript" language="javascript"
	src="../DataTables-1.10.6/media/js/jquery.dataTables.js"></script>
<script type="text/javascript" language="javascript"
	src="../DataTables-1.10.6/examples/resources/syntax/shCore.js"></script>
<script type="text/javascript" language="javascript"
	src="../DataTables-1.10.6/examples/resources/demo.js"></script>
<script type="text/javascript" language="javascript"
	src="../Responsive-1.0.6/js/dataTables.responsive.min.js"></script>
<script type="text/javascript" language="javascript" class="init">
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
		var isMe = document.getElementById("hidden_isMe").value;
		var isVote = document.getElementById("hidden_isVote").value;
		var isJoin = document.getElementById("hidden_isJoin").value;

		var downloadDiv = document.getElementById("downloadBlock");
		var guideDiv = document.getElementById("guide");

		if (isMe == 'true') {
			if (is_weixin()) {
				var imageControl = document.createElement("img");
				// share vote
				imageControl
						.setAttribute("src",
								"http://tripc2c-default.b0.upaiyun.com/app/image/wechat-tips-ios.png");
				guideDiv.appendChild(imageControl);

				var buttonControl = document.createElement("input");
				buttonControl.setAttribute("type", "Submit");
				buttonControl.setAttribute("class",
						"btn btn-lg btn-success btn-block active");
				buttonControl.setAttribute("onclick",
						"javascript:_system._guide(true)");
				buttonControl.setAttribute("value", "拉票");
				downloadDiv.appendChild(buttonControl);
			}
		} else {
			if (isVote == 'true') {
				if (isJoin == 'true') {
					var buttonControl = document.createElement("input");
					buttonControl.setAttribute("type", "Submit");
					buttonControl.setAttribute("class",
							"btn btn-lg btn-success btn-block active");
					var actionStr = "javascript:window.location.href='http://planc2c.com/talent/join-action.htm?ekey='";
					buttonControl.setAttribute("onclick", actionStr);
					buttonControl.setAttribute("value", "查看我的排名");
					downloadDiv.appendChild(buttonControl);
				} else {
					var buttonControl = document.createElement("input");
					buttonControl.setAttribute("type", "Submit");
					buttonControl.setAttribute("class",
							"btn btn-lg btn-success btn-block active");
					var actionStr = "javascript:window.location.href='http://planc2c.com/talent/join-action.htm?ekey='";
					buttonControl.setAttribute("onclick", actionStr);
					buttonControl.setAttribute("value", "加入封神榜");
					downloadDiv.appendChild(buttonControl);
				}
			} else {
				var buttonControl = document.createElement("input");
				buttonControl.setAttribute("type", "Submit");
				buttonControl.setAttribute("class",
						"btn btn-lg btn-success btn-block active");
				var actionStr = "javascript:window.location.href='http://planc2c.com/talent/vote-action.htm?ekey="
						+ ekey + "'";
				buttonControl.setAttribute("onclick", actionStr);
				buttonControl.setAttribute("value", "投票");
				downloadDiv.appendChild(buttonControl);
			}
		}
	}
</script>
<body>
	<input type="hidden" id="hidden_ekey" value="${ekey}">
	<input type="hidden" id="hidden_isMe" value="${isMe}">
	<input type="hidden" id="hidden_isVote" value="${isVote}">
	<input type="hidden" id="hidden_isJoin" value="${isJoin}">

	<div class="container-fluid">
		<div class="row">
			<div class="col-xs-12 col-sm-12 col-md-12 photo-bg pad-tb50">
				<div class="my-photo mag-tb10">
					<c:if test="${avatarUrl == null}">
						<img class="img-responsive img-circle center-block"
							src="http://tripc2c-default.b0.upaiyun.com/app/image/avatar.png">
					</c:if>
					<c:if test="${avatarUrl != null}">
						<img class="img-responsive img-circle center-block"
							src="${avatarUrl}">
					</c:if>
				</div>
				<h4 class="mag-tb10 txt-col-white text-center">${nickname}</h4>
				<h6 class="txt-col-fen text-center">${friendDesc}</h6>
			</div>

			<div class="container">
				<div class="row">
					<div class="col-xs-12 col-sm-12 col-md-12 act-mag">
						<div class="row">
							<h4 class="act-title col-xs-12 col-sm-12 col-md-12">
								<strong></strong>
							</h4>
						</div>

						<div class="row">
							<div id="downloadBlock" class="col-xs-12 col-sm-12 col-md-12"></div>
						</div>
						<div class="row">
							<a href="http://planc2c.com/talent/talent-top50.htm"
								class="btn btn-lg btn-primary btn-block active">封神榜</a>
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
				</div>
			</div>

		</div>
	</div>
	<!-- jQuery (necessary for Bootstrap's JavaScript plugins)   <script src="http://cdn.bootcss.com/jquery/1.11.2/jquery.min.js"></script>-->

	<!-- Include all compiled plugins (below), or include individual files as needed    <script src="js/bootstrap.min.js"></script>-->

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