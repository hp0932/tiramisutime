<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">

<link rel="stylesheet" href="/webjars/bootstrap/3.4.1/dist/css/bootstrap.min.css">

<script type="text/javascript" src="/webjars/jquery/3.4.1/dist/jquery.min.js"></script>
<script type="text/javascript" src="/webjars/bootstrap/3.4.1/dist/js/bootstrap.min.js"></script>
<script type="text/javascript" src="/webjars/moment/2.25.3/moment.js"></script>

<style type="text/css">
#headerBar {
	margin-top : 5px;
	border-radius : 10px/10px;
}

#btnHome {
	cursor : pointer;
}
.container-fluid {
	margin : 0px 30px 0px 30px;
}
</style>

<script type="text/javascript">
$(document).ready(function(){
	
	/* var loginStatus = getParameterByName('loginStatus');
	if(loginStatus == -1) {
		alert("로그인에 실패하였습니다.\아이디 및 패스워드를 확인해주세요.");
	} */
	
	$("#btnHome").on('click', function(){
		location.href = '/'
	});
	
	$("#btnJoin").on('click', function(){
		location.href = '/member/join'
	});
	
	$("#btnLogout").on('click', function(){
		location.href = '/member/logout'
	});
	
	$(document).on('click','#btnLogin',function(){
		login();
	});
});

function login(){
	var userId = $("#userId").val();
	var password = $("#password").val();
	$.ajax({
		url : '<c:url value="/member/login" />',
		type : "POST",
		data : {
			'userId' : userId,
			'password' : password
		},
		success : function(result){
			if(result == -1){
				alert("로그인에 실패하였습니다.\아이디 및 패스워드를 확인해주세요.");
				location.href = '/';
				return;
			}else{
				location.href = '/';
				return;
			}
		}
	});
}

function getParameterByName(name) {
	name = name.replace(/[\[]/, "\\[").replace(/[\]]/, "\\]");
	var regex = new RegExp("[\\?&]" + name + "=([^&#]*)"),
		results = regex.exec(location.search);
	return results === null ? "" : decodeURIComponent(results[1].replace(/\+/g, " "));
}


</script>
</head>
<body>
<div class="container-fluid">
	<div id="headerBar" class="col-xs-12">
		<div class="col-xs-1" style="padding-top:2px; padding-bottom:2px;">
			<span id="btnHome" class="glyphicon glyphicon-home" style="font-size:25px;"></span>
		</div>
		<div class="col-xs-11 text-right" style="padding:0px;">
			<c:choose>
				<c:when test="${sessionScope.userId ne null}">
					<div class="text-right">
							<c:if test="${sessionScope.level eq 2}"><span style="font-size:18px;font-weight:bold;">[관리자]</span></c:if>
							<c:if test="${sessionScope.level eq 3}"><span style="font-size:18px;font-weight:bold;">[운영자]</span></c:if>
							<span style="font-size:18px;">${sessionScope.name}님 환영합니다&nbsp&nbsp</span>
							<button id="btnLogout" type="button" class="btn btn-primary">로그아웃</button>
					</div>
				</c:when>
				<c:otherwise>
					<div class="text-right" style="padding:0px;">
						<form id="loginForm" class="form-inline">
							<input class="form-control" type="text" id="userId" name="userId" placeholder="아이디를 입력해주세요" onkeypress="if( event.keyCode==13 ){login();}">
							<input class="form-control" type="password" id="password" name="password" placeholder="비밀번호를 입력해주세요" onkeypress="if( event.keyCode==13 ){login();}">
							<button id="btnLogin" type="button" class="btn btn-primary">로그인</button>
							<button id="btnJoin" type="button" class="btn btn-success">회원가입</button>
						</form>
					</div>
				</c:otherwise>
			</c:choose>
		</div>
	</div>
	<div class="col-xs-12 text-center">
		<h2>ready custard</h2>
	</div>
</div>
</body>
</html>