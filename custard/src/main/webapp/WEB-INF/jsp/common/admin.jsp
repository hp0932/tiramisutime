<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<%@ include file="/WEB-INF/common/init.jsp" %>


<style type="text/css">
</style>

<script type="text/javascript">
$(document).ready(function(){
	
	
});
//레벨 목록 저장
function levelSave(seq){
	var formName = "#levelForm" + seq;
	location.href="/member/admin/levelCommit?" + $(formName).serialize();
}

//유저 레벨 변경
function userSave(seq){
	var formName = "#userForm" + seq;
	location.href="/member/admin/userCommit?" + $(formName).serialize();
}
</script>
<title>관리자 페이지</title>
</head>
<body>
<div class="container">
	<table class="table table-hover table-condensed">
		<tr>
			<td>
				<div class="col-xs-2 text-center"><span style="font-size:17px; font-weight:bold;">레벨</span></div>
				<div class="col-xs-9 text-center"><span style="font-size:17px; font-weight:bold;">레벨 설명</span></div>
				<div class="col-xs-1"></div>
			</td>
		</tr>
		<c:forEach items="${levelList.levelList}" var="result" varStatus="i">
			<tr>
				<td>
					<form id="levelForm${i.count}">
						<input type="hidden" name="id" value="${result.id}">
						<div class="col-xs-2"><input name="level"  class="form-control"  type="number" value="${result.level}"></div>
						<div class="col-xs-9"><input name="levelName" class="form-control"  type="text" value="${result.levelName}"></div>
						<div class="col-xs-1"><button class="btn btn-success" type="button" onclick="levelSave(${i.count})">저장</button></div>
					</form>
				</td>
			</tr>
		</c:forEach>
		<tr>
			<td>
				<form id="levelForm0">
					<div class="col-xs-2"><input name="level" class="form-control" type="number"></div>
					<div class="col-xs-9"><input name="levelName" class="form-control" type="text"></div>
					<div class="col-xs-1"><button class="btn btn-success" type="button" onclick="levelSave(0)">저장</button></div>
				</form>
			</td>
		</tr>
	</table>
	<table class="table table-hover table-condensed">
		<tr>
			<td>
				<div class="col-xs-1 text-center"><span style="font-size:17px; font-weight:bold;">no.</span></div>
				<div class="col-xs-2 text-center"><span style="font-size:17px; font-weight:bold;">아이디</span></div>
				<div class="col-xs-2 text-center"><span style="font-size:17px; font-weight:bold;">닉네임</span></div>
				<div class="col-xs-3 text-center"><span style="font-size:17px; font-weight:bold;">이메일</span></div>
				<div class="col-xs-2 text-center"><span style="font-size:17px; font-weight:bold;">가입일</span></div>
				<div class="col-xs-2 text-center"><span style="font-size:17px; font-weight:bold;">레벨</span></div>
			</td>
		</tr>
		<c:forEach items="${data.memberList}" var="result" varStatus="i">
			<tr>
				<td>
					<div class="col-xs-1 text-center" style="border-right:1px dotted gray;">${result.id}</div>
					<div class="col-xs-2 text-center" style="border-right:1px dotted gray;">${result.userId}</div>
					<div class="col-xs-2 text-center" style="border-right:1px dotted gray;">${result.name}</div>
					<div class="col-xs-3 text-center" style="border-right:1px dotted gray;">${result.email}</div>
					<div class="col-xs-2 text-center" style="border-right:1px dotted gray;">${result.createDate}</div>
					<div class="col-xs-2 text-center">
						<form id="userForm${i.count}">
							<input type="hidden" name="id" value="${result.id}">
							<select name="level" class="form-control" onchange="userSave(${i.count})">
								<c:forEach items="${levelList.levelList}" var="levelResult" varStatus="j">
									<option value="${levelResult.level}"<c:if test="${levelResult.level eq result.level}">selected</c:if>>${levelResult.levelName}</option>
								</c:forEach>
							</select>
						</form>
					</div>
				</td>
			</tr>
		</c:forEach>
	</table>

</div>
</body>
</html>