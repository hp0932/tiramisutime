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
	//토렌트 버튼 클릭시
	$("#btnTorrent").click(function(){
		//트랜스미션 iframe 페이지 내부 제어
		$("#transmission").children().remove();
		$("#transmission").contents().find("#toolbar").css('display', 'none');
	});
});

//파일 다운로드
function download(fileName){
	var down = "/fileview/download?fileName=" + fileName;
	location.href=down;
}

//폴더 진입
function cdFolder(folder){
	var path = "${param.path}";
	if(path == ''){
		path = "${data.folder}";
	}else{
		path = path + "/" + "${param.folderName}";
	}
	var cd = "/fileview/downloader?folderName=" + folder + "&path=" + path;
	location.href=cd;
}

//돌아가기 버튼
function goBack(){
	window.history.back();
}

//홈으로 버튼
function goHome(){
	location.href="/fileview/downloader"
}

function makeToolbar(){
	$("#transmission").get(0).contentDocument.find("#toolbar").remove();
}
</script>
<title>다운로더</title>
</head>
<body>
<div class="container">
	<div class="col-xs-12" style="border:1px solid gray; height:31px; padding-top:3px; border-radius:5px; margin-bottom:3px;">
		<div class="col-xs-8" style="border-right:1px dotted gray;"><span style="font-size:18px;">location : <c:if test="${data.path ne ''}">/${data.path}</c:if>/${data.folder}</span></div>
		<div onclick="goHome()" class="col-xs-4" style="cursor:pointer;"><span class="glyphicon glyphicon-eject pull-right" style="font-size:20px;"></span><span style="font-size:18px;">홈으로</span></div>
	</div>
	<div class="col-xs-12" style="overflow:scroll; height:330px; border:1px solid gray; padding:0px; overflow-x:hidden;">
		<table class="table table-hover table-condensed" style="margin:0px;">
			<c:if test="${data.folder ne ''}">
				<tr>
					<td onclick='goBack()' style="cursor:pointer;">
						<div class="col-xs-12"><span class="glyphicon glyphicon-level-up" style="font-size:19px;"></span>&nbsp&nbsp<span></span><span style="font-weight:bold;">..</span></div>
					</td>
				</tr>
			</c:if>
			<c:forEach items="${data.folderList}" var="result" varStatus="i">
				<tr>
					<td onclick='cdFolder("${result.fileName}")' style="cursor:pointer;">
						<div class="col-xs-12"><span class="glyphicon glyphicon-folder-open" style="font-size:15px;"></span>&nbsp&nbsp<span>${result.fileName}</span></div>
					</td>
				</tr>
			</c:forEach>
			<c:forEach items="${data.fileList}" var="result" varStatus="i">
				<tr>
					<td onclick='download("${result.fileName}")' style="cursor:pointer;">
						<div class="col-xs-8" style="border-right:1px dotted gray;"><span class="glyphicon glyphicon-file" style="font-size:18px;"></span>&nbsp&nbsp<span>${result.fileName}</span></div>
						<div class="col-xs-4">${result.fileSize}</div>
					</td>
				</tr>
			</c:forEach>
			<tr><td></td></tr>
		</table>
	</div>
	<div class="col-xs-12" style="margin-top:3px; margin-bottom:3px; padding:0px;">
		<button id="btnUpTorrent" class="btn btn-success" type="button">파일 업로드</button>
		<button id="btnTorrent" class="btn btn-primary" type="button" data-toggle="collapse" data-target="#divTransmission" aria-expanded="false" aria-controls="divTransmission" onclick="makeToolbar()">
			다운로드 진행상황
		</button>
	</div>
	<div class="col-xs-12 collapse" id="divTransmission" style="padding:0px; margin-bottom:30px;">
		<iframe id="transmission" height="500px" class="col-xs-12" src="http://localhost:8080/" style="padding:0px;"></iframe>
	</div>
</div>
</body>
</html>