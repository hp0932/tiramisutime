<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
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
.display-none {
	display: none;
}
#joinForm {
	background-color: rgb(242, 245, 247);
	border: 5px solid rgb(255, 255, 255);
	border-radius: 15px;
	position: relative;
}
</style>


<script type="text/javascript">
$(document).ready(function(){
	
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
									<div class="col-lg-2 col-md-2 col-xs-2" style="padding:0px;"><button class="btn btn-info" style="margin-left:5px;">발송</button></div>
								</div>
							</td>
						</tr>
						<tr>
							<td>확인코드 : </td>
							<td><input class="form-control" type="text" id="code" name="code" maxlength="45"></td>
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
	</div>
</body>
</html>