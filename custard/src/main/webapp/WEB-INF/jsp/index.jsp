<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<%@ include file="/WEB-INF/common/init.jsp" %>

<title>custard</title>

<style type="text/css">
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
		location.href='/downloader'
	});
});
</script>
</head>
<body>
<div class="container">
<c:choose>
	<c:when test="${sessionScope.level eq 2}">
	<!-- 관리자용 -->
		<div class="col-xs-4">
			<table class="table table-hover table-condensed">
				<tr>
					<td id="freeBoard" class="text-center" style="border-top:0px;">
						<span class="glyphicon glyphicon-th-list" style="font-size:100px;"></span><br>
						<span class="text-center">자유게시판</span>
					</td>
				</tr>
				<tr>
					<td id="fileBoard" class="text-center" style="border-top:0px;">
						<span class="glyphicon glyphicon-folder-open" style="font-size:100px;"></span><br>
						<span class="text-center">파일게시판</span>
					</td>
				</tr>
			</table>
		</div>
		<div class="col-xs-4">
			<table class="table table-hover table-condensed">
				<tr>
					<td id="useTech" class="text-center" style="border-top:0px;">
						<span class="glyphicon glyphicon-info-sign" style="font-size:100px;"></span><br>
						<span class="text-center">사용기술일람</span>
					</td>
				</tr>
				<tr>
					<td id="adminConsole" class="text-center" style="border-top:0px;">
						<span class="glyphicon glyphicon-edit" style="font-size:100px;"></span><br>
						<span class="text-center">관리모듈</span>
					</td>
				</tr>
			</table>
		</div>
		<div class="col-xs-4">
			<table class="table table-hover table-condensed">
				<tr>
					<td id="adminImg" class="text-center" style="border-top:0px;">
						<span class="glyphicon glyphicon-inbox" style="font-size:100px;"></span><br>
						<span class="text-center">관리모듈 이미지</span>
					</td>
				</tr>
				<tr>
					<td id="downloader" class="text-center" style="border-top:0px;">
						<span class="glyphicon glyphicon-download" style="font-size:100px;"></span><br>
						<span class="text-center">다운로더</span>
					</td>
				</tr>
			</table>
		</div>
	</c:when>
	<c:when test="${sessionScope.level eq 7}">
	<!-- 권한 사용자용 -->
		<div class="col-xs-4">
			<table class="table table-hover table-condensed">
				<tr>
					<td id="fileBoard" class="text-center" style="border-top:0px;">
						<span class="glyphicon glyphicon-folder-open" style="font-size:100px;"></span><br>
						<span class="text-center">파일게시판</span>
					</td>
				</tr>
			</table>
		</div>
		<div class="col-xs-4">
			<table class="table table-hover table-condensed">
				<tr>
					<td id="useTech" class="text-center" style="border-top:0px;">
						<span class="glyphicon glyphicon-info-sign" style="font-size:100px;"></span><br>
						<span class="text-center">사용기술일람</span>
					</td>
				</tr>
			</table>
		</div>
		<div class="col-xs-4">
			<table class="table table-hover table-condensed">
				<tr>
					<td id="downloader" class="text-center" style="border-top:0px;">
						<span class="glyphicon glyphicon-download" style="font-size:100px;"></span><br>
						<span class="text-center">다운로더</span>
					</td>
				</tr>
			</table>
		</div>
	</c:when>
	<c:otherwise>
	<!-- 일반사용자 및 비가입자용 -->
		<div class="col-xs-4">
			<table class="table table-hover table-condensed">
				<tr>
					<td id="freeBoard" class="text-center" style="border-top:0px;">
						<span class="glyphicon glyphicon-th-list" style="font-size:100px;"></span><br>
						<span class="text-center">자유게시판</span>
					</td>
				</tr>
			</table>
		</div>
		<div class="col-xs-4">
			<table class="table table-hover table-condensed">
				<tr>
					<td id="useTech" class="text-center" style="border-top:0px;">
						<span class="glyphicon glyphicon-info-sign" style="font-size:100px;"></span><br>
						<span class="text-center">사용기술일람</span>
					</td>
				</tr>
			</table>
		</div>
		<div class="col-xs-4">
			<table class="table table-hover table-condensed">
				<tr>
					<td id="adminImg" class="text-center" style="border-top:0px;">
						<span class="glyphicon glyphicon-inbox" style="font-size:100px;"></span><br>
						<span class="text-center">관리모듈 이미지</span>
					</td>
				</tr>
			</table>
		</div>
	</c:otherwise>
</c:choose>
</div>
</body>
</html>