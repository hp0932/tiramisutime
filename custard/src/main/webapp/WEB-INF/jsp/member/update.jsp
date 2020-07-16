<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<title>회원정보수정</title>
<link rel="stylesheet" href="/webjars/bootstrap/3.4.1/dist/css/bootstrap.min.css">

<script type="text/javascript" src="/webjars/jquery/3.4.1/dist/jquery.min.js"></script>
<script type="text/javascript" src="/webjars/bootstrap/3.4.1/dist/js/bootstrap.min.js"></script>


<style type="text/css">
	#joinForm{
		background-color: rgb(242, 245, 247);
		border: 5px solid rgb(255, 255, 255);
		border-radius: 15px;
		position: relative;
	}
</style>


<script type="text/javascript">
$(document).ready(function(){

	$("#btnJoin").on('click', function(){
		var data = $("#joinForm");
		
		var password = $("#password").val();
		if(password.length == 0){
			alert("비밀번호를 입력하세요");
			$("#password").focus();
			return;
		} else if(password.length > 45) {
			alert("비밀번호를 45자 이내로 입력하세요");
			$("#password").focus();
			return;
		}
		var passwordRe = $("#passwordRe").val();
		if(passwordRe.length == 0){
			alert("비밀번호확인을 입력하세요");
			$("#passwordRe").focus();
			return;
		} else if(passwordRe.length > 45) {
			alert("비밀번호확인을 45자 이내로 입력하세요");
			$("#passwordRe").focus();
			return;
		}
		/* 비밀번호와 확인이 올바르게 입력되었는가 */
		if(password != passwordRe){
			alert("비밀번호와 비밀번호 확인이 다릅니다");
			document.getElementById('passwordRe').focus();
			return;
		}
		var name = $("#name").val();
		if(name.length == 0){
			alert("닉네임을 입력하세요");
			$("#name").focus();
			return;
		} else if(name.length > 45) {
			alert("닉네임을 45자 이내로 입력하세요");
			$("#name").focus();
			return;
		}
		var email = $("#email").val();
		if(email.length == 0){
			alert("이메일을 입력하세요");
			$("#email").focus();
			return;
		} else if(email.length > 45) {
			alert("이메일을 45자 이내로 입력하세요");
			$("#email").focus();
			return;
		} else if(email.indexOf("@") == -1 || email.indexOf(".") == -1){
			alert("이메일을 형식에 맞게 입력하세요");
			$("#email").focus();
			return;
		}
		
		$.ajax({
			url : '<c:url value="/member/update" />',
			type : "POST",
			data : {
				'password' : password,
				'email' : email,
				'name' : name
			},
			success : function(result, textStatus, XMLHttpRequest){
				if(result > 0){
					alert('회원정보 수정이 완료되었습니다.\n다시 로그인해 주십시오.');
					location.href = '/';
					return;
				}else if(result == -1){
					alert('아이디를 변경하실 수 없습니다.');
				}else if(result == -2){
					alert('이미 있는 닉네임입니다.\n닉네임을 변경하여 주십시오.');
				}else if(result == -3){
					alert('이미 가입된 이메일입니다.\n이메일을 변경하여 주십시오.');
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
</script>
</head>
<body>
	<br/>
	<br/>
	<br/>
	<div class="container">
		<div class="row">
			<div class="col-md-3 col-xs-1">
			</div>
			<div class="col-md-6 col-xs-10">
				<form id="joinForm" name="joinForm" enctype="multipart/form-data">
					<br />
					<font size="7px">회원정보수정</font>
					<table class="table" id="joinTable">
						<tr>
							<td>아이디 : </td>
							<td>${userId}</td>
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
							<td><input class="form-control" type="text" id="name" name="name" maxlength="45" value="${name}"></td>
						</tr>
						<tr>
							<td>이메일 : </td>
							<td><input class="form-control" type="text" id="email" name="email" maxlength="45" value="${email}"></td>
						</tr>
					</table>
				</form>
				<div class="text-right" style="padding-right:10px;">
					<button type="button" id="btnBack" class="btn btn-warning">돌아가기</button>
					<button type="button" id="btnJoin" class="btn btn-success">확인</button>
				</div>
			</div>
			<div class="col-md-3 col-xs-1">
			</div>
		</div>
	</div>
</body>
</html>