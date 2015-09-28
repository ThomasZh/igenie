<%@ page language="java" contentType="text/html; charset=GB18030"
    pageEncoding="GB18030"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GB18030">
<title>talent top 50</title>
</head>

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

	<div class="container">
		<section>
			<h1>封神榜 <span>${activityName}</span></h1>

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

</body>
</html>