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
<title>my friends</title>

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
	<link rel="shortcut icon" type="image/ico" href="http://www.datatables.net/favicon.ico">
	<meta name="viewport" content="initial-scale=1.0, maximum-scale=2.0">

	<title>封神榜</title>
	<link rel="stylesheet" type="text/css" href="../DataTables-1.10.6/media/css/jquery.dataTables.css">
	<link rel="stylesheet" type="text/css" href="../DataTables-1.10.6/examples/resources/syntax/shCore.css">
	<link rel="stylesheet" type="text/css" href="../DataTables-1.10.6/examples/resources/demo.css">
	<link rel="stylesheet" type="text/css" href="../Responsive-1.0.6/css/dataTables.responsive.css">
	<style type="text/css" class="init">

	</style>
	<script type="text/javascript" language="javascript" src="../DataTables-1.10.6/media/js/jquery.js"></script>
	<script type="text/javascript" language="javascript" src="../DataTables-1.10.6/media/js/jquery.dataTables.js"></script>
	<script type="text/javascript" language="javascript" src="../DataTables-1.10.6/examples/resources/syntax/shCore.js"></script>
	<script type="text/javascript" language="javascript" src="../DataTables-1.10.6/examples/resources/demo.js"></script>
	<script type="text/javascript" language="javascript" src="../Responsive-1.0.6/js/dataTables.responsive.min.js"></script>
	<script type="text/javascript" language="javascript" class="init">
		$(document).ready( function () {
			$('#example')
				.addClass( 'nowrap' )
				.dataTable( {
					responsive: true,
					searching: false,
					paging: false,
					columnDefs: [
						{ targets: [-1, -3], className: 'dt-body-right' }
					]
				} );
		} );
		</script>
		
<body>
	<input type="hidden" id="hidden_ekey" value="${ekey}">

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
								<div id="downloadBlock" class="col-xs-12 col-sm-12 col-md-12 times"></div>
							</div>
							
							<a href="http://planc2c.com/talent/edit-profile.htm"
									class="btn btn-lg btn-success btn-block active">我是达人</a>
							<a href="http://planc2c.com/talent/talent-top50.htm"
									class="btn btn-lg btn-primary btn-block active">封神榜</a>
									
	<div class="container">
		<section>
			<h1>朋友圈 <span>${activityName}</span></h1>

			<table id="example" class="display" cellspacing="0" width="100%">
				<thead>
					<tr>
						<th>#</th>
						<th>Avatar</th>
						<th>Name</th>  
						<th>Score</th> 
					</tr>
				</thead>

				<tfoot>
					<tr>
						<th>#</th>
						<th>Avatar</th>
						<th>Name</th>  
						<th>Score</th> 
					</tr>
				</tfoot>

				<tbody>
				    <tr>
				        <td>1</td>
				        <td><img height="40" width="40" class="img-responsive img-circle center-block" src="http://tripc2c-person-face.b0.upaiyun.com/2015/06/04/7cd6b72bf642522cde1b2a33273c01c7.jpg"></td>
				        <td>Hann</td>
				        <td>1000</td>
				    </tr>
				    <tr>
				        <td>2</td>
				        <td><img height="40" width="40" class="img-responsive img-circle center-block" src="http://tripc2c-person-face.b0.upaiyun.com/1/3/5/8a578aae-f832-408b-b733-9a9bc2953e16.jpg"></td>
				        <td>Dylan</td>
				        <td>500</td>
				    </tr>
				    <tr>
				        <td>3</td>
				        <td><img height="40" width="40" class="img-responsive img-circle center-block" src="http://tripc2c-person-face.b0.upaiyun.com/1/3/5/60a056fb-48fe-40ed-89b3-b3c68b3ca56d.jpg"></td>
				        <td>Sara</td>
				        <td>350</td>
				    </tr>
				</tbody>
			</table>
		</section>
	</div>									
									
							<div class="row">
								<h4 class="act-title col-xs-12 col-sm-12 col-md-12">
									<strong></strong>
								</h4>
								<div class="col-xs-12 col-sm-12 col-md-12 times">
									<h5 class="W_fl item_ico">
										<em><a href="http://planc2c.com"><img src="http://tripc2c-default.b0.upaiyun.com/app/image/logo-28.png" width="25" height="25"></a></em>
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