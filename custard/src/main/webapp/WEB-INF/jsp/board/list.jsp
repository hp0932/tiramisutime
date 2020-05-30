<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<%@ include file="/WEB-INF/common/init.jsp" %>


<style type="text/css">
#threadLine {
	cursor : pointer;
}
</style>

<script type="text/javascript">
$(document).ready(function(){
	
	$("#btnWrite").click(function(){
		location.href = '/board/write'
	});
	
	$("#btnSearch").click(function(){
		var search = $("#searchContent").val();
		searchPage(search);
	});
	
});

function selectPage(select){
	var searchedContent = $("#searchedContent").val();
	if(searchedContent != ""){
		location.href="/board/list?nowPage=" + select + "&searchContent=" + searchedContent;
	}else{
		location.href="/board/list?nowPage=" + select;
	}
}

function searchPage(search){
	location.href="/board/list?searchContent=" + search;
}

function selectThread(select){
	location.href="/board/read?nowThread=" + select;
}

</script>
<title>free board</title>
</head>
<body>
<div class="container">
	<div>
		<div style="min-height:340px;">
		<table class="table table-hover table-condensed">
			<tr>
				<td>
					<div class="col-xs-1 text-center"><span style="font-size:17px; font-weight:bold;">no.</span></div>
					<div class="col-xs-5 text-center"><span style="font-size:17px; font-weight:bold;">title</span></div>
					<div class="col-xs-3 text-center"><span style="font-size:17px; font-weight:bold;">writer</span></div>
					<div class="col-xs-3 text-center"><span style="font-size:17px; font-weight:bold;">date</span></div>
				</td>
			</tr>
			<c:forEach items="${data.boardList.content}" var="result" varStatus="i">
				<tr>
					<td id="threadLine" onclick="selectThread('${result.id}')">
						<div class="col-xs-1 text-center" style="border-right:1px dotted gray;">${result.id}</div>
						<div class="col-xs-5" style="border-right:1px dotted gray;">${result.title}</div>
						<div class="col-xs-3 text-center" style="border-right:1px dotted gray;">${result.ownerName}</div>
						<div class="col-xs-3 text-center">
							<fmt:parseDate var="date" value="${result.createDate}" pattern="yyyy-MM-dd HH:mm:ss"/>
							<fmt:formatDate var="dateView" value="${date}" pattern="yyyy.MM.dd HH:mm"/>
							${dateView}
						</div>
					</td>
				</tr>
			</c:forEach>
		</table>
		<input type="hidden" id="searchedContent" value="${param.searchContent}">
		</div>
		<div class="col-xs-12 text-center" style="margin-bottom:3px;">
			<c:if test="${data.startPage ne 1}">
				<button class="btn btn-default" onclick="selectPage('${data.startPage-10}')"><span class="glyphicon glyphicon-chevron-left"></span></button>
			</c:if>
			<c:forEach begin="${data.startPage}" end="${data.endPage}" var="result" varStatus="i">
				<c:if test="${data.nowPage eq result}">
					<button class="btn btn-success" onclick="selectPage('${result}')">${result}</button>
				</c:if>
				<c:if test="${data.nowPage ne result}">
					<button class="btn btn-default" onclick="selectPage('${result}')">${result}</button>
				</c:if>
			</c:forEach>
			<c:if test="${data.endPage ne data.boardListCount}">
				<button class="btn btn-default" onclick="selectPage('${data.startPage+10}')"><span class="glyphicon glyphicon-chevron-right"></span></button>
			</c:if>
		</div>
	</div>
	<c:choose>
		<c:when test="${sessionScope.userId ne null}">
			<div class="col-xs-12 text-right">
				<form class="form-inline">
					<input class="form-control" type="text" name="searchContent" id="searchContent" size="45" maxlength="45">
					<button class="btn btn-info" type="button" id="btnSearch" class="btnSearch">검색</button>
					<button class="btn btn-success" type="button" id="btnWrite">글쓰기</button>
				</form>
			</div>
		</c:when>
		<c:otherwise>
			<div class="col-xs-12 text-right">
				<form class="form-inline">
					<input class="form-control" type="text" name="searchContent" id="searchContent" size="45" maxlength="45">
					<button class="btn btn-info" type="button" id="btnSearch" class="btnSearch">검색</button>
				</form>
			</div>
		</c:otherwise>
	</c:choose>
</div>
</body>
</html>