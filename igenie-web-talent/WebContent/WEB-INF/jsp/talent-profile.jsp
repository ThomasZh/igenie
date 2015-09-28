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
<title>神人</title>


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

<link href="talent/css/style.css" rel="stylesheet" />

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

	<div class="wrap">
		<div id="header">
			<p class="plan left">A</p>
			<font class="left">A计划</font> <a href="javascript:;" class="right">下载APP</a>
		</div>
		
		<div id="content clearfix" style="margin-top:8%;">
            <div class="txt_con">
                <div class="rbox">
                	<c:if test="${avatarUrl == null}">
                		<p class="alignC pad0 photo"><img src="http://tripc2c-default.b0.upaiyun.com/app/image/avatar.png" /></p>
					</c:if>
					<c:if test="${avatarUrl != null}">
						<p class="alignC pad0 photo"><img src="${avatarUrl}" /></p>
					</c:if>
                    <p class="alignC col_26">${nickname}</p>
                    <p class="alignC fon24">-第${position}名-</p>
                    <p class="alignC" style="margin-top:-18px;"><a href="http://planc2c.com/talent/talent-top50.htm" class="col_9a">查看榜单</a></p>
                    <p class="alignC col_fe69">“排名有点靠后哦！赶紧邀请朋友，帮助投票”</p>
                    <p class="alignC"><div class="def_btn" id="downloadBlock"></div></p>
                    <p class="alignC pad30 col_fa9">-朋友帮助投了${voted}票-</p>
                    <div class="tp">
                    	<c:forEach var="vote" items="${votes}">
                        	<a href="http://www.planc2c.com/talent/talent-profile.htm?ekey=${vote.accountId}" class="pics"><img src="${vote.avatarUrl}" /></a>
                        </c:forEach>
                    </div>
                </div>
            </div>
        </div>
	</div>
	
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