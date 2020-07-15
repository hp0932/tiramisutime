<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<%@ include file="/WEB-INF/common/init.jsp" %>

<title>custard</title>

<style type="text/css">
/* index페이지 main 버튼 일체 */
.btnIndex {
	margin : 15px 0px 15px 0px;
}
/* index페이지 main버튼 설명문 */
.textIndex {
	font-size : 22px;
}
/* index main버튼 div 관리 */
.btnIndexDiv {
	height : 170px;
	margin : -3px;
	cursor : pointer;
}
/* 시계 div추가제어 */
.clockDiv {
	border : 3px solid #eee;
	border-radius : 10px;
	cursor: default;
}
.clocker {
	margin : 5px 0px 5px 0px;
}
</style>
	
<script type="text/javascript" charset="UTF-8">
$(document).ready(function(){
	//자유게시판
	$('#freeBoard').click(function(){
		location.href='/board/list'
	});
	
	//파일게시판
	$('#fileBoard').click(function(){
		location.href='/fileBoard/list'
	});
	
	//사용기술
	$('#useTech').click(function(){
		location.href='/tech'
	});
	
	//관리자 메뉴
	$('#adminConsole').click(function(){
		location.href='/member/admin'
	});
	
	//관리자메뉴 이미지
	$('#adminImg').click(function(){
		location.href='/adminImg'
	});
	
	//파일 다운로더
	$('#downloader').click(function(){
		location.href='/fileview/downloader'
	});
	
	//회원정보수정
	/* $('#btnPasswordTest').click(function(){
		location.href='/member/modify'
	}); */
	
	//시계 시작 로딩
	clock1();
	clock2();
});

//시계 1초마다 갱신
setInterval(function(){clock1()}, 1000);
setInterval(function(){clock2()}, 1000);

/* 시계 시간 표시 */
//국내 시간 표시
function clock1(){
	var clock = $('#clock1');
	var txt = $('#clock1txt');
	var now = moment().tz("Asia/Seoul").format('HH:mm:ss');
	var day = moment().tz("Asia/Seoul").format('MM月DD日');
	clock.text(now);
	txt.text(day);
}
//선택한 해외시간 표시
function clock2(){
	var clock = $('#clock2');
	var txt = $('#clock2txt');
	var select = $('#selectTimezone').val();
	var selectTxt = $('#selectTimezone option:checked').text();
	var now = moment().tz(select).format('HH:mm:ss');
	var day = moment().tz(select).format('MM月DD日');
	
	$('#cityName').html(selectTxt);
	clock.text(now);
	txt.text(day);
}

//사용자 선택 시간대를 쿠키에 저장
function selectClocker(){
	var select = $('#selectTimezone').val();
	setCookie("selectTimezone", select, 365);
	clock2();
}

//선택한 세계시간 쿠키 저장
function setCookie(cookieName, value, exdays){
    var exdate = new Date();
    exdate.setDate(exdate.getDate() + exdays);
    var cookieValue = escape(value) + ((exdays==null) ? "" : "; expires=" + exdate.toGMTString());
    document.cookie = cookieName + "=" + cookieValue;
}
</script>
</head>
<body>
<div class="container-fluid">
	<c:if test="${sessionScope.level ne 7}">
	<!-- 자유게시판 : 권한사용자가 아닐 경우 -->
		<div class="btnIndex col-lg-2 col-md-3 col-xs-4">
			<div id="freeBoard" class="text-center btnIndexDiv">
				<span class="glyphicon glyphicon-th-list" style="font-size:100px;"></span><br>
				<span class="textIndex text-center">자유게시판</span>
			</div>
		</div>
	</c:if>
	<!-- 사용기술일람 : 모든 사용자 -->
	<div class="btnIndex col-lg-2 col-md-3 col-xs-4">
		<div id="useTech" class="text-center btnIndexDiv">
			<span class="glyphicon glyphicon-info-sign" style="font-size:100px;"></span><br>
			<span class="textIndex text-center">사용기술일람</span>
		</div>
	</div>
	<c:if test="${sessionScope.level ne 7}">
	<!-- 관리모듈 이미지 : 권한 사용자가 아닐 경우 -->
		<div class="btnIndex col-lg-2 col-md-3 col-xs-4">
			<div id="adminImg" class="text-center btnIndexDiv">
				<span class="glyphicon glyphicon-picture" style="font-size:100px;"></span><br>
				<span class="textIndex text-center">관리모듈 이미지</span>
			</div>
		</div>
	</c:if>
	<c:if test="${sessionScope.level eq 2 || sessionScope.level eq 7}">
	<!-- 파일게시판 : 권한 사용자 및 관리자 -->
		<div class="btnIndex col-lg-2 col-md-3 col-xs-4">
			<div id="fileBoard" class="text-center btnIndexDiv">
				<span class="glyphicon glyphicon-folder-open" style="font-size:100px;"></span><br>
				<span class="textIndex text-center">파일게시판</span>
			</div>
		</div>
	</c:if>
	<c:if test="${sessionScope.level eq 2}">
	<!-- 관리모듈 : 관리자 전용 -->
	<!-- 관리자 : 2 / 권한사용자 : 7 / 일반유저 : 1 -->
		<div class="btnIndex col-lg-2 col-md-3 col-xs-4">
			<div id="adminConsole" class="text-center btnIndexDiv">
				<span class="glyphicon glyphicon-edit" style="font-size:100px;"></span><br>
				<span class="textIndex text-center">관리모듈</span>
			</div>
		</div>
	</c:if>
	<c:if test="${sessionScope.level eq 2 || sessionScope.level eq 7}">
	<!-- 파일 다운로더 : 권한 사용자 및 관리자 -->
		<div class="btnIndex col-lg-2 col-md-3 col-xs-4">
			<div id="downloader" class="text-center btnIndexDiv">
				<span class="glyphicon glyphicon-download" style="font-size:100px;"></span><br>
				<span class="textIndex text-center">다운로더</span>
			</div>
		</div>
	</c:if>
	<c:if test="${sessionScope.level ne null}">
	<!-- 회원정보수정 : 회원일 경우 -->
		<div class="btnIndex col-lg-2 col-md-3 col-xs-4">
			<div id="userModify" class="text-center btnIndexDiv" data-toggle="modal" data-target="#userModal">
				<span class="glyphicon glyphicon-list-alt" style="font-size:100px;"></span><br>
				<span class="textIndex text-center">회원정보수정</span>
			</div>
		</div>
		<!-- 회원정보수정 모달창 -->
		<div class="modal fade" id="userModal" tabindex="-1" role="dialog" aria-labelledby="userModalLabel">
			<div class="modal-dialog" role="document">
			<form name="passTestForm" method="post" action="/member/modify">
				<div class="modal-content">
					<div class="modal-header">
						<button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
						<h4 class="modal-title" id="userModalLabel">비밀번호를 입력해주세요</h4>
					</div>
					<div class="modal-body">
						<input type="password" name="password" id="userPasswordTest" class="form-control">
					</div>
					<div class="modal-footer">
						<button type="button" class="btn btn-warning" data-dismiss="modal">취소</button>
						<button id="btnPasswordTest" class="btn btn-success">확인</button>
					</div>
				</div>
			</form>
			</div>
		</div>
	</c:if>
	<!-- 시계 -->
	<div class="btnIndex col-lg-2 col-md-3 col-xs-4">
		<div id="clockDiv1" class="text-center btnIndexDiv clockDiv">
			<h1 class="clocker" id="clock1txt"></h1>
			<h1 class="clocker" id="clock1"></h1>
			<span class="textIndex text-center" style="font-size:18px;">서울</span>
		</div>
	</div>
	<!-- 해외시간 -->
	<div class="btnIndex col-lg-2 col-md-3 col-xs-4">
		<div id="clockDiv1" class="text-center btnIndexDiv clockDiv">
			<h1 class="clocker" id="clock2txt"></h1>
			<h1 class="clocker" id="clock2"></h1>
			<span id="cityName" class="textIndex text-center" style="font-size:18px;">미국/시카고</span><br>
			<div style="padding:0px 5px 0px 5px;">
				<select id="selectTimezone" class="form-control" onchange="selectClocker()" style="margin:5px 5px 5px 0px;">
					<option value="America/Los_Angeles"<c:if test="${cookie.selectTimezone.value eq 'America/Los_Angeles'}">selected="selected"</c:if>>로스앤젤레스[PST]</option>
					<option value="America/Denver"<c:if test="${cookie.selectTimezone.value eq 'America/Denver'}">selected="selected"</c:if>>덴버[MST]</option>
					<option value="America/Chicago"<c:if test="${cookie.selectTimezone.value eq 'America/Chicago'}">selected="selected"</c:if>>시카고[CST]</option>
					<option value="America/New_York"<c:if test="${cookie.selectTimezone.value eq 'America/New_York'}">selected="selected"</c:if>>뉴욕[EST]</option>
					<option value="Europe/London"<c:if test="${cookie.selectTimezone.value eq 'Europe/London'}">selected="selected"</c:if>>런던[GMT]</option>
				</select>
			</div>
		</div>
	</div>
</div>
</body>
</html>