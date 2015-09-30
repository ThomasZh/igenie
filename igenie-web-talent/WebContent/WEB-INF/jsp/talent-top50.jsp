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
<title>封神榜</title>
</head>

<meta charset="utf-8">
<link rel="shortcut icon" type="image/ico"
	href="http://www.datatables.net/favicon.ico">
<meta name="viewport" content="initial-scale=1.0, maximum-scale=2.0">

<title>封神榜</title>

<link href="talent/css/style.css" rel="stylesheet" />

<link rel="stylesheet" type="text/css"
	href="DataTables-1.10.6/media/css/jquery.dataTables.css">
<link rel="stylesheet" type="text/css"
	href="DataTables-1.10.6/examples/resources/syntax/shCore.css">
<link rel="stylesheet" type="text/css"
	href="DataTables-1.10.6/examples/resources/demo.css">
<link rel="stylesheet" type="text/css"
	href="Responsive-1.0.6/css/dataTables.responsive.css">
<style type="text/css" class="init">
</style>
<script type="text/javascript" language="javascript"
	src="DataTables-1.10.6/media/js/jquery.js"></script>
<script type="text/javascript" language="javascript"
	src="DataTables-1.10.6/media/js/jquery.dataTables.js"></script>
<script type="text/javascript" language="javascript"
	src="DataTables-1.10.6/examples/resources/syntax/shCore.js"></script>
<script type="text/javascript" language="javascript"
	src="DataTables-1.10.6/examples/resources/demo.js"></script>
<script type="text/javascript" language="javascript"
	src="Responsive-1.0.6/js/dataTables.responsive.min.js"></script>
<script type="text/javascript" language="javascript" class="init">
	$(document).ready(function() {
		$('#example').addClass('nowrap').dataTable({
			responsive : true,
			searching : false,
			paging : false,
			columnDefs : [ {
				targets : [ -1, -3 ],
				className : 'dt-body-right'
			} ]
		});
	});
</script>

<body>
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
		<section>
		<h1>
			封神榜 <span>${activityName}</span>
		</h1>

		<table id="example" class="display" cellspacing="0" width="100%">
			<thead>
				<tr>
					<th>#</th>
					<th>头像</th>
					<th>名字</th>
					<th>得票</th>
				</tr>
			</thead>

			<tbody>
				<c:forEach var="talent" items="${talents}">
					<tr>
						<td>${talent.position}</td>
						<td><a
							href="http://www.planc2c.com/talent/talent-profile.htm?ekey=${talent.accountId}"><img
								height="40" width="40" src="${talent.avatarUrl}" /></a></td>
						<td>${talent.nickname}</td>
						<td>${talent.votedNum}</td>
					</tr>
				</c:forEach>
			</tbody>
		</table>
		</section>
	</div>
</body>
</html>