<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<title>회원가입</title>
<link rel="stylesheet" href="/webjars/bootstrap/3.4.1/dist/css/bootstrap.min.css">

<script type="text/javascript" src="/webjars/jquery/3.4.1/dist/jquery.min.js"></script>
<script type="text/javascript" src="/webjars/bootstrap/3.4.1/dist/js/bootstrap.min.js"></script>


<style type="text/css">
#joinForm {
	background-color: rgb(242, 245, 247);
	border: 5px solid rgb(255, 255, 255);
	border-radius: 15px;
	position: relative;
}
.wrap-loading {
	position: fixed;
	left: 0;
	right: 0;
	top: 0;
	bottom: 0;
	background: rgba(0,0,0,0.2);
}
/* 로딩 이미지 */
.wrap-loading div {
	position: fixed;
	top:50%;
	left:50%;
	margin-left: -100px;
	margin-top: -100px;
}
/* 기본적으로 감출때 사용 */
.display-none {
	display: none;
}
</style>


<script type="text/javascript">
$(document).ready(function(){
	
	$("#btnEmailCode").on('click', function(){
		emailCode();
	});
	
	$("#btnCode").on('click', function(){
		codeTest();
	});
	
	$("#btnJoin").on('click', function(){
		var bwf = document.joinForm;
		
		var userId = bwf.userId.value;
		if(userId.length == 0){
			alert("아이디를 입력하세요");
			document.getElementById('userId').focus();
			return;
		} else if(userId.length > 45) {
			alert("아이디를 45자 이내로 입력하세요");
			document.getElementById('userId').focus();
			return;
		}
		var password = bwf.password.value;
		if(password.length == 0){
			alert("비밀번호를 입력하세요");
			document.getElementById('password').focus();
			return;
		} else if(password.length > 45) {
			alert("비밀번호를 45자 이내로 입력하세요");
			document.getElementById('password').focus();
			return;
		}
		var passwordRe = bwf.passwordRe.value;
		if(passwordRe.length == 0){
			alert("비밀번호확인을 입력하세요");
			document.getElementById('passwordRe').focus();
			return;
		} else if(passwordRe.length > 45) {
			alert("비밀번호확인을 45자 이내로 입력하세요");
			document.getElementById('passwordRe').focus();
			return;
		}
		/* 비밀번호와 확인이 올바르게 입력되었는가 */
		if(password != passwordRe){
			alert("비밀번호와 비밀번호 확인이 다릅니다");
			document.getElementById('passwordRe').focus();
			return;
		}
		var name = bwf.name.value;
		if(name.length == 0){
			alert("닉네임을 입력하세요");
			document.getElementById('name').focus();
			return;
		} else if(name.length > 45) {
			alert("닉네임을 45자 이내로 입력하세요");
			document.getElementById('name').focus();
			return;
		}
		var email = bwf.email.value;
		if(email.length == 0){
			alert("이메일을 입력하세요");
			document.getElementById('email').focus();
			return;
		} else if(email.length > 45) {
			alert("이메일을 45자 이내로 입력하세요");
			document.getElementById('email').focus();
			return;
		} else if(email.indexOf("@") == -1 || email.indexOf(".") == -1){
			alert("이메일을 형식에 맞게 입력하세요");
			document.getElementById('email').focus();
			return;
		}
		
		$.ajax({
			url : '<c:url value="/member/commit" />',
			type : "POST",
			data : {
				'userId' : userId,
				'password' : password,
				'email' : email,
				'name' : name
			},
			success : function(result, textStatus, XMLHttpRequest){
				if(result > 0){
					alert('가입이 완료되었습니다.');
					location.href = '/';
					return;
				}else if(result == -1){
					alert('이미 가입된 아이디입니다.\아이디를 변경하여 주십시오.');
				}else if(result == -2){
					alert('이미 있는 닉네임입니다.\닉네임을 변경하여 주십시오.');
				}else if(result == -3){
					alert('이미 가입된 이메일입니다.\이메일을 변경하여 주십시오.');
				}
			},
			error : function (XMLHttpRequest, textStatus, errorThrown){
				alert('가입에 실패하였습니다 \n'+XMLHttpRequest.responseText);
			}
		});
	});
	
	$('#btnBack').click(function(){
		location.href='/'
	});
});

//이메일 코드발송
function emailCode(){
	//이메일을 입력받고 코드 발송
	var email = $("#email").val();
	if(email.length == 0){
		alert("이메일을 입력하세요");
		$("#email").focus();
		return;
	} else if(email.indexOf("@") == -1 || email.indexOf(".") == -1){
		alert("이메일을 형식에 맞게 입력하세요");
		$("#email").focus();
		return;
	}
	//email주소로 난수값 발송
	$.ajax({
		url : '<c:url value="/member/joinCode" />',
		type : "POST",
		data : {
			'email' : email
		},
		success : function(result){
			if(result == -1){
				alert("이미 가입된 이메일입니다.\n메일주소를 확인해주세요.");
				return;
			}else{
				alert("메일로 코드가 발송되었습니다.\n코드는 5분간 유효합니다.");
				return;
			}
		},
		beforeSend : function(){
			//ajax처리 전 버튼 변경 및 wrap 처리
			$('.wrap-loading').removeClass('display-none');
		},
		complete : function(){
			//ajax처리 후 버튼 복구 및 warp 제거
			$('.wrap-loading').addClass('display-none');
			
			$('#btnCode').removeClass('display-none');
			$('#btnCodeDummy').addClass('display-none');
		}
	});
}

//발송된 코드를 확인
function codeTest(){
	var code = $("#code").val();
	var email = $("#email").val();
	if(code.length == 0){
		alert("코드를 입력하세요");
		$("#code").focus();
		return;
	}
	
	//코드값 확인
	$.ajax({
		url : '<c:url value="/member/joinCodeTest" />',
		type : "POST",
		data : {
			'code' : code,
			'email' : email
		},
		success : function(result){
			if(result == -1){
				alert("잘못된 코드입니다.\n코드를 확인해주세요.");
				return;
			}else{
				alert("코드가 확인되었습니다.\n가입을 진행해주세요.");
				return;
			}
		},
		beforeSend : function(){
			//ajax처리 전 버튼 변경 및 wrap 처리
			$('.wrap-loading').removeClass('display-none');
		},
		complete : function(){
			//ajax처리 후 버튼 복구 및 warp 제거
			$('.wrap-loading').addClass('display-none');
			
			$('#btnJoin').removeClass('display-none');
			$('#btnJoinDummy').addClass('display-none');
		}
	});
}
</script>
</head>
<body>
	<br/>
	<br/>
	<br/>
	<div class="container">
		<div class="row">
			<div class="col-lg-3 col-md-2 col-xs-1">
			</div>
			<div class="col-lg-6 col-md-8 col-xs-10">
				<form id="joinForm" name="joinForm" enctype="multipart/form-data">
					<br />
					<font size="7px">가입을 환영합니다</font>
					<table class="table" id="joinTable">
						<tr>
							<td>아이디 : </td>
							<td><input class="form-control" type="text" id="userId" name="userId" maxlength="45"></td>
						</tr>
						<tr>
							<td>비밀번호 : </td>
							<td><input class="form-control" type="password" id="password" name="password" maxlength="45"></td>
						</tr>
						<tr>
							<td>비밀번호 확인 : </td>
							<td><input class="form-control" type="password" id="passwordRe" name="passwordRe" maxlength="45"></td>
						</tr>
						<tr>
							<td>닉네임 : </td>
							<td><input class="form-control" type="text" id="name" name="name" maxlength="45"></td>
						</tr>
						<tr>
							<td>이메일 : </td>
							<td>
								<div class="col-xs-12" style="padding:0px;">
									<div class="col-lg-10 col-md-10 col-xs-10" style="padding:0px;"><input class="form-control" type="text" id="email" name="email" maxlength="45"></div>
									<div class="col-lg-2 col-md-2 col-xs-2" style="padding:0px;">
										<button id="btnEmailCode" type="button" class="btn btn-info" style="margin-left:5px;">발송</button>
										<button id="btnEmailCodeDummy" type="button" class="btn display-none" style="margin-left:5px;">발송</button>
									</div>
								</div>
							</td>
						</tr>
						<tr>
							<td>확인코드 : </td>
							<td>
								<div class="col-lg-10 col-md-10 col-xs-10" style="padding:0px;"><input class="form-control" type="text" id="code" name="code" maxlength="45"></div>
								<div class="col-lg-2 col-md-2 col-xs-2" style="padding:0px;">
									<button id="btnCode" type="button" class="btn btn-success display-none" style="margin-left:5px;">확인</button>
									<button id="btnCodeDummy" type="button" class="btn" style="margin-left:5px;">확인</button>
								</div>
							</td>
						</tr>
					</table>
				</form>
				<div class="text-right" style="padding-right:10px;">
					<button id="btnBack" class="btn btn-warning">가입취소</button>
					<button id="btnJoin" class="btn btn-success display-none">회원가입</button>
					<button id="btnJoinDummy" class="btn">회원가입</button>
				</div>
			</div>
			<div class="col-lg-3 col-md-2 col-xs-1">
			</div>
		</div>
		<div class="wrap-loading display-none">
			<div><img src="/static/img/loading.gif"></div>
		</div>
	</div>
</body>
</html>