<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
	<meta charset="utf-8">
	<link rel="shortcut icon" type="image/ico" href="http://www.datatables.net/favicon.ico">
	<meta name="viewport" content="initial-scale=1.0, maximum-scale=2.0">

	<title>报名信息</title>
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
</head>

<body class="dt-example">
	<div class="container">
		<section>
			<h1>报名信息 <span>${activityName}</span></h1>

			<table id="example" class="display" cellspacing="0" width="100%">
				<thead>
					<tr>
					  <c:forEach items="${columnNames}" var="columnName" varStatus="stat"> 
						<th>${columnName}</th>
					  </c:forEach>   
					</tr>
				</thead>

				<tfoot>
					<tr>
					  <c:forEach items="${columnNames}" var="columnName" varStatus="stat"> 
						<th>${columnName}</th>
					  </c:forEach>   
					</tr>
				</tfoot>

				<tbody>
				  <c:forEach items="${datas}" var="row" varStatus="stat"> 
				    <tr>
				      <c:forEach items="${row}" var="column" varStatus="stat"> 
				        <td>${column}</td>
				      </c:forEach>  
				    </tr>
				  </c:forEach>   
				</tbody>
			</table>


			<div class="tabs">
				<div class="js">
					<p>The Javascript shown below is used to initialise the table shown in this example:</p><code class="multiline language-js">$(document).ready(function() {
	$('#example').DataTable();
} );</code>

</body>
</html>