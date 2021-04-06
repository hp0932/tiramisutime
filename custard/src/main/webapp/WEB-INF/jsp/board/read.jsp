<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<%@ include file="/WEB-INF/common/init.jsp" %>
<title>읽기</title>

<style type="text/css">
#readTable {
	background-color: rgb(242, 245, 247);
	border: 5px solid rgb(255, 255, 255);
	border-radius: 15px;
	position: relative;
	top : -5px;
}

</style>

<script type="text/javascript">
$(document).ready(function(){
	$("#btnModify").click(function(){
		var nowThread = "${data.id}";
		location.href = '/board/modify?nowThread=' + nowThread;
	});
	
	$("#btnDelete").click(function(){
		var nowThread = "${data.id}";
		location.href = '/board/delete?nowThread=' + nowThread;
	});
	
	$("#btnWrite").click(function(){
		location.href = '/board/write'
	});
	
	$("#btnList").click(function(){
		location.href = '/board/list'
	})
});
</script>
</head>
<body>
<div class="container">
	<div class="row">
		<div class="col-xs-12">
			<table class="table table-bordered" id="readTable">
				<colgroup>
					<col width="20%">
					<col width="40%">
					<col width="20%">
					<col width="20%">
				</colgroup>
				<tr>
					<td colspan="1">제목 :</td>
					<td colspan="3">${data.title}</td>
				</tr>
				<tr>
					<td colspan="1">글쓴이 :</td>
					<td colspan="1">${data.ownerName}</td>
					<td colspan="1">조회수:</td>
					<td colspan="1">${data.readCnt}</td>
				</tr>
				<tr>
					<!-- 내용 -->
					<td colspan="4" height="300"><div style="white-space: pre-line;">${data.content}</div></td>
				</tr>
				<tr>
					<td colspan="1">작성일시 :</td>
					<td colspan="3">${data.createDate}</td>
				</tr>
			</table>
		</div>
	</div>
	<c:choose>
		<c:when test="${sessionScope.userId eq data.ownerId}">
			<div class="col-xs-12 text-right">
				<button class="btn btn-success" type="button" id="btnWrite">글쓰기</button>
				<button class="btn btn-primary" type="button" id="btnModify">수정</button>
				<button class="btn btn-danger"  type="button" id="btnDelete">삭제</button>
				<button class="btn btn-info"    type="button" id="btnList">목록</button>
			</div>
		</c:when>
		<c:when test="${sessionScope.level eq 2}">
			<div class="col-xs-12 text-right">
				<button class="btn btn-success" type="button" id="btnWrite">글쓰기</button>
				<button class="btn btn-danger"  type="button" id="btnDelete">삭제</button>
				<button class="btn btn-info"    type="button" id="btnList">목록</button>
			</div>
		</c:when>
		<c:when test="${sessionScope.userId ne null}">
			<div class="col-xs-12 text-right">
				<button class="btn btn-success" type="button" id="btnWrite">글쓰기</button>
				<button class="btn btn-info"    type="button" id="btnList">목록</button>
			</div>
		</c:when>
		<c:otherwise>
			<div class="col-xs-12 text-right">
				<button class="btn btn-info"    type="button" id="btnList">목록</button>
			</div>
		</c:otherwise>
	</c:choose>
</div>
</body>
</html>