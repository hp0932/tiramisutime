<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<%@ include file="/WEB-INF/common/init.jsp" %>

<title>custard</title>

<style type="text/css">
.btnIndex {
	margin : 15px 0px 15px 0px;
}
.textIndex {
	font-size : 22px;
}
.btnIndexDiv {
	height : 170px;
	margin : -3px;
	cursor : pointer;
}
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
	$('#freeBoard').click(function(){
		location.href='/board/list'
	});
	
	$('#fileBoard').click(function(){
		location.href='/fileBoard/list'
	});
	
	$('#useTech').click(function(){
		location.href='/tech'
	});
	
	$('#adminConsole').click(function(){
		location.href='/member/admin'
	});
	$('#adminImg').click(function(){
		location.href='/adminImg'
	});
	$('#downloader').click(function(){
		location.href='/fileview/downloader'
	});
	
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
				<span class="glyphicon glyphicon-inbox" style="font-size:100px;"></span><br>
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
				<select id="selectTimezone" class="form-control" style="margin:5px 5px 5px 0px;">
					<option value="America/Los_Angeles">로스앤젤레스[PST]</option>
					<option value="America/Denver">덴버[MST]</option>
					<option value="America/Chicago">시카고[CST]</option>
					<option value="America/New_York" selected="selected">뉴욕[EST]</option>
					<option value="Asia/Tokyo">도쿄</option>
				</select>
			</div>
		</div>
	</div>
</div>
</body>
</html>