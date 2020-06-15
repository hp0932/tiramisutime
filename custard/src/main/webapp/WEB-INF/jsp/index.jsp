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
});
</script>
</head>
<body>
<div class="container-fluid">
	<!-- 관리자 : 2 / 권한사용자 : 7 / 일반유저 : 1 -->
	<!-- 자유게시판 : 권한사용자가 아닐 경우 -->
	<c:if test="${sessionScope.level ne 7}">
		<div class="btnIndex col-lg-2 col-md-3 col-xs-4">
			<div id="freeBoard" class="text-center" style="border-top:0px; cursor:pointer;">
				<span class="glyphicon glyphicon-th-list" style="font-size:100px;"></span><br>
				<span class="textIndex text-center">자유게시판</span>
			</div>
		</div>
	</c:if>
	<!-- 사용기술일람 : 모든 사용자 -->
	<div class="btnIndex col-lg-2 col-md-3 col-xs-4">
		<div id="useTech" class="text-center" style="border-top:0px; cursor:pointer;">
			<span class="glyphicon glyphicon-info-sign" style="font-size:100px;"></span><br>
			<span class="textIndex text-center">사용기술일람</span>
		</div>
	</div>
	<!-- 관리모듈 이미지 : 권한 사용자가 아닐 경우 -->
	<c:if test="${sessionScope.level ne 7}">
		<div class="btnIndex col-lg-2 col-md-3 col-xs-4">
			<div id="adminImg" class="text-center" style="border-top:0px; cursor:pointer;">
				<span class="glyphicon glyphicon-inbox" style="font-size:100px;"></span><br>
				<span class="textIndex text-center">관리모듈 이미지</span>
			</div>
		</div>
	</c:if>
	<!-- 파일게시판 : 권한 사용자 및 관리자 -->
	<c:if test="${sessionScope.level eq 2 || sessionScope.level eq 7}">
		<div class="btnIndex col-lg-2 col-md-3 col-xs-4">
			<div id="fileBoard" class="text-center" style="border-top:0px; cursor:pointer;">
				<span class="glyphicon glyphicon-folder-open" style="font-size:100px;"></span><br>
				<span class="textIndex text-center">파일게시판</span>
			</div>
		</div>
	</c:if>
	<!-- 관리모듈 : 관리자 전용 -->
	<c:if test="${sessionScope.level eq 2}">
		<div class="btnIndex col-lg-2 col-md-3 col-xs-4">
			<div id="adminConsole" class="text-center" style="border-top:0px; cursor:pointer;">
				<span class="glyphicon glyphicon-edit" style="font-size:100px;"></span><br>
				<span class="textIndex text-center">관리모듈</span>
			</div>
		</div>
	</c:if>
	<!-- 파일 다운로더 : 권한 사용자 및 관리자 -->
	<c:if test="${sessionScope.level eq 2 || sessionScope.level eq 7}">
		<div class="btnIndex col-lg-2 col-md-3 col-xs-4">
			<div id="downloader" class="text-center" style="border-top:0px; cursor:pointer;">
				<span class="glyphicon glyphicon-download" style="font-size:100px;"></span><br>
				<span class="textIndex text-center">다운로더</span>
			</div>
		</div>
	</c:if>
</div>
</body>
</html>