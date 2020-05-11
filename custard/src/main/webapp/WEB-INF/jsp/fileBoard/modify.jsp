<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<%@ include file="/WEB-INF/common/init.jsp" %>
<title>수정</title>

<style type="text/css">
</style>

<script type="text/javascript">
$(document).ready(function(){
	console.log("${data}");
	$(document).on('click','#saveBtn',function(){
	 	var data = $($(this).parents('form'));
	 	
	 	var title = $('#title').val();
		if (title.length == 0) {
			alert("제목을 입력하세요");
			return;
		} else if (byteCheck(title, 200)) {
			alert("제목의 길이가 초과되었습니다.");
			return;
		}
	
		var content = $('#content').val();
		if (content.length == 0) {
			alert("본문을 입력하세요");
			return;
		} else if (byteCheck(content, 65000)) {
			alert("본문의 길이가 초과되었습니다.");
			return;
		}
	 	
	 	data.attr('method','POST');
	 	data.attr('action',"<c:url value='/fileBoard/update'/>");
	 	data.submit();
	});
	
	$(document).on('click', '#returnBtn', function(){
		location.href = "/fileBoard/list"
	});
});

function fileDelete(fileId){
	$.ajax({
		url : '<c:url value="/fileBoard/fileDelete" />',
		type : "GET",
		data : {
			'fileId' : fileId
		},
		success : function(){
			alert("파일이 삭제되었습니다.");
			$("#fileLine"+fileId).attr('style', 'display:none;');
		},
		error : function (){
			alert("파일 삭제를 실패하였습니다.");
		}
	});
}

//문자열 str과 최댓값 max를 입력받아 해당 값이 최대값 이하인지를 true or false로 리턴하는 함수
function byteCheck(str, max){
	var result = 0;
    for(var i = 0; i < str.length; i++){
    	if(escape(str.charAt(i)).length == 6){
    		//escape하여 16진수 변환했을때 6자라면
    		result++;
    	}
    	result++;
    }
    if(result <= max){
    	return false;
    }else{
    	return true;
    }
}
</script>
</head>
<body>
<div class="container">
	<div class="row">
		<form name="writeForm" method="post" enctype="multipart/form-data">
			<table class="table table-striped table-bordered">
				<tr>
					<td>제목 :</td>
					<td><input class="form-control" type="text" id="title" name="title" maxlength="200" value="${data.title}"></td>
				</tr>
				<tr>
					<td>글쓴이 :</td>
					<td>
						<input class="form-control" type="text" id="name" name="name" value="${data.ownerName}" readonly="readonly">
					</td>
				</tr>
				<tr>
					<td colspan="2" id="contentBox">
						<textarea rows="20" cols="200" id="content" name="content" class="form-control" placeholder="본문을 입력하세요">${data.content}</textarea>
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
				<tr>
					<td>첨부 : </td>
					<td><input type="file" id="attachFile" name="attachFile" multiple="multiple"/></td>
				</tr>
				<tr>
					<td class="name" colspan="1">파일관리 :</td>
					<td colspan="3">
						<c:choose>
							<c:when test="${data.fileList eq null}">첨부파일이 없습니다.</c:when>
							<c:otherwise>
								<c:forEach items="${data.fileList}" var="result" varStatus="i">
									<div id="fileLine${result.id}" style="margin-top:1px;"><button class="btn btn-danger" type="button" onclick="fileDelete(${result.id})">파일삭제</button> <span>${result.fileName}</span></div>
								</c:forEach>
							</c:otherwise>
						</c:choose>
					</td>
				</tr>
				<tr>
					<td colspan="2">
						<button id="returnBtn" class="btn btn-default" type="button">돌아가기</button>
						<button id="saveBtn" class="btn btn-success pull-right" type="button">저장하기</button>
					</td>
				</tr>
			</table>
			<input type="hidden" id="id" name="id" value="${data.id}">
			<input type="hidden" id="readCnt" name="readCnt" value="0">
		</form>
	</div>
</div>
</body>
</html>