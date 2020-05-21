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

function download(fileName){
	var down = "/fileview/download?fileName=" + fileName;
	location.href=down;
}

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

function goBack(){
	window.history.back();
}

function goHome(){
	location.href="/fileview/downloader"
}
</script>
<title>다운로더</title>
</head>
<body>
<div class="container">
	<div class="col-xs-8"><span>location : <c:if test="${data.path ne ''}">/${data.path}</c:if>/${data.folder}</span></div>
	<div class="col-xs-4"><span onclick="goHome()" class="glyphicon glyphicon-eject pull-right" style="font-size:20px; cursor:pointer;"></span></div>
	<div class="col-xs-12" style="overflow:scroll; height:330px; border:1px solid gray; padding:0px;">
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
	<%-- <div>
		<c:forEach items="${data.fileList}" var="result" varStatus="i">
			<div class="col-xs-2" style="height:65px; border:1px solid gray; border-radius: 10px; margin:2px;">${result.fileName}</div>
		</c:forEach>
	</div> --%>
	<div class="col-xs-12" style="padding:0px;">
	</div>
</div>
</body>
</html>