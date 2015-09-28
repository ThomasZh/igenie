<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta charset="UTF-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport"
	content="width=device-width, initial-scale=1,user-scalable=no">
<!-- 上述3个meta标签*必须*放在最前面，任何其他内容都*必须*跟随其后！ -->
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<%@ page isELIgnored="false"%>
<%
	response.setCharacterEncoding("UTF-8");
	request.setCharacterEncoding("UTF-8");
%>
<title>填写报名信息</title>

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

<script type="text/javascript">
	var rows = 1;
	var rowNums = 1;
	var rowArray = new Array();
	rowArray.push(rowNums);

	function remove(arr, item) {
		for (var i = arr.length; i--;) {
			if (arr[i] === item) {
				arr.splice(i, 1);
			}
		}
	}

	function DeleteDom(row) {
		var strId = "participationRow_" + row;
		oldChild = document.getElementById(strId);
		document.getElementById("participationCells").removeChild(oldChild);

		rows--;
		remove(rowArray, row);
		document.getElementById("hidden_participationRowNum").value = rows;
		document.getElementById("hidden_participationRowArray").value = rowArray
				.toString();
	}

	function AddDom() {
		rows++;
		rowNums++;
		rowArray.push(rowNums);
		var strId = "participationRow_" + rows;
		var columns = document.getElementById("hidden_participationColumnNum").value;
		document.getElementById("hidden_participationRowNum").value = rows;
		document.getElementById("hidden_participationRowArray").value = rowArray
				.toString();

		var divControl = document.createElement("div");
		divControl.setAttribute("id", strId);

		var labelControl = document.createElement("h4");
		labelControl.setAttribute("class", "form-signin-heading");
		labelControl.innerHTML = "同行人";
		divControl.appendChild(labelControl);

		var buttonControl = document.createElement("input");
		buttonControl.setAttribute("type", "button");
		buttonControl.setAttribute("class", "btn btn-sm btn_danger active");
		buttonControl.setAttribute("name", "btnDeleteText");
		buttonControl.setAttribute("value", "删除同行人");
		var strAction = "javascript:DeleteDom(" + rows + ");";
		buttonControl.setAttribute("onclick", strAction);
		divControl.appendChild(buttonControl);

		for (var i = 0; i < columns; i++) {
			var seq = document.getElementById("hidden_inputParticipation_"
					+ (i + 1) + "_seq").value;
			var name = document.getElementById("hidden_inputParticipation_"
					+ (i + 1) + "_name").value;

			var textControl = document.createElement("input");
			textControl.setAttribute("type", "text");
			textControl.setAttribute("class", "form-control");
			textControl.setAttribute("name", "inputParticipation_" + rows + "_"
					+ seq);
			textControl.setAttribute("id", "inputParticipation_" + rows + "_"
					+ seq);
			textControl.setAttribute("placeholder", name);
			divControl.appendChild(textControl);
		}

		document.getElementById("participationCells").appendChild(divControl);
	}
</script>
</head>



<body>

  ${message}

  <div class="container">
    <h2 class="form-signin-heading">报名信息</h2>
    <h4 class="act-title col-xs-12 col-sm-12 col-md-12">
      <strong></strong>
    </h4>
    <form class="form-signin" action="applyAction.htm" commandName="contact">

      <div class="row">
        <div class="col-xs-12 col-sm-12 col-md-12 times">
          <h4 class="form-signin-heading">联系人</h4>
          <c:forEach var="contactCell" items="${contactCells}">
            <input type="text" name="inputContact_${contactCell.seq}"
              id="inputContact_${contactCell.seq}" class="form-control"
              placeholder="${contactCell.name}" required>
          </c:forEach>
        </div>
      </div> <!-- row -->
      
      <div class="row">
        <div class="col-xs-12 col-sm-12 col-md-12 times">
          <h4 class="form-signin-heading">参加人</h4>
          <div id="participationCells">
            <input type="hidden" id="hidden_id" name="hidden_id" value="${id}">
            <input type="hidden"
              id="hidden_participationColumnNum"
              name="hidden_participationColumnNum"
              value="${participationColumnNum}">
            <input type="hidden"
              id="hidden_participationRowNum"
              name="hidden_participationRowNum"
              value="1">
            <input type="hidden"
              id="hidden_participationRowArray"
              name="hidden_participationRowArray"
              value="1">
            <div id="participationRow_1">
              <c:forEach var="participationCell" items="${participationCells}">
                <label for="inputParticipation" class="sr-only">${participationCell}</label>
                <input type="hidden"
                  id="hidden_inputParticipation_${participationCell.seq}_seq"
                  value="${participationCell.seq}">
                <input type="hidden"
                  id="hidden_inputParticipation_${participationCell.seq}_name"
                  value="${participationCell.name}">
                <input type="text"
                  name="inputParticipation_1_${participationCell.seq}"
                  id="inputParticipation_1_${participationCell.seq}"
                  class="form-control" placeholder="${participationCell.name}"
                  required>
              </c:forEach>
            </div> <!-- participationRow_1 -->
          </div>
        </div>
        <div class="col-xs-12 col-sm-12 col-md-12 times">
          <p></p>
          <input type="button" class="btn btn-md btn_success active"
            name="btnCreateText" value="添加同行人" onclick="javascript:AddDom();" />
        </div>
      </div> <!-- row -->

      <div class="row">
        <div class="col-xs-12 col-sm-12 col-md-12 times">
          <p></p>
          <button class="btn btn-lg btn-primary btn-block" type="submit">完成</button>
        </div>
      </div> <!-- row -->
    
      <div class="row">
        <h4 class="act-title col-xs-12 col-sm-12 col-md-12">
          <strong></strong>
        </h4>
        <div class="col-xs-12 col-sm-12 col-md-12 times">
          <h5 class="W_fl item_ico">
            <em><a href="http://planc2c.com">
              <img src="http://tripc2c-default.b0.upaiyun.com/app/image/logo-28.png" width="25" height="25">
            </a></em>
          </h5>
          <h5>
            <span class="start-date col-gray">A计划: hang out with friends, no more Plan B</span>
          </h5>
        </div>
      </div> <!-- row -->
    </form>
  </div> <!-- /container -->

	<!-- jQuery (necessary for Bootstrap's JavaScript plugins)   <script src="http://cdn.bootcss.com/jquery/1.11.2/jquery.min.js"></script>-->

	<!-- Include all compiled plugins (below), or include individual files as needed    <script src="js/bootstrap.min.js"></script>-->

</body>
</html>