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
	console.log("${data}");
	$(document).on('click','#btnUpTorrent',function(){
	 	var data = $($(this).parents('form'));
	 	
	 	data.attr('method','POST');
	 	data.attr('action',"<c:url value='/fileview/upload'/>");
	 	data.submit();
	});
	
	//토렌트 버튼 클릭시
	$("#btnTorrent").click(function(){
		//트랜스미션 iframe 페이지 내부 제어
		$("#transmission").children().remove();
		$("#transmission").contents().find("#toolbar").css('display', 'none');
	});
});

//파일 다운로드
function download(fileName){
	var path = "${data.path}";
	if(path == ''){
		path = "${data.folder}";
	}else{
		path = path + "/" + "${data.folder}";
	}
	if(path != ''){
		fileName = path + "/" + fileName;
	}
	
	var data = $('<form></form>');
	//HTML5 표준 : document에 추가되지 않은 form의 submit은 중단
	$('.container').append(data);
	data.attr('name', 'folderForm');
	data.attr('method','POST');
	data.attr('action', "<c:url value='/fileview/download'/>");
	
	data.append($('<input/>', {type: 'hidden', name: 'fileName', value: fileName}));
	data.submit();
	//var down = "/fileview/download?fileName=" + fileName;
	//location.href=down;
}

//폴더 진입
function cdFolder(folder){
	var path = "${param.path}";
	if(path == ''){
		path = "${data.folder}";
	}else{
		path = path + "/" + "${param.folderName}";
	}
	var data = $('<form></form>');
	//HTML5 표준 : document에 추가되지 않은 form의 submit은 중단
	$('.container').append(data);
	data.attr('name', 'folderForm');
	data.attr('method','POST');
	data.attr('action', "<c:url value='/fileview/downloader'/>");
	
	data.append($('<input/>', {type: 'hidden', name: 'path', value: path}));
	data.append($('<input/>', {type: 'hidden', name: 'folderName', value: folder}));
	data.submit();
	//var cd = "/fileview/downloader?folderName=" + folder + "&path=" + path;
	//location.href=cd;
}

//돌아가기 버튼
function goBack(){
	window.history.back();
}

//홈으로 버튼
function goHome(){
	location.href="/fileview/downloader"
}

//토렌트 업로드 폴더
function goTorrentFolder(){
	location.href="/fileview/upTorrent"
}

</script>
<title>다운로더</title>
</head>
<body>
<div class="container">
	<div class="col-xs-12" style="border:1px solid gray; height:31px; padding-top:3px; border-radius:5px; margin-bottom:3px;">
		<div class="col-xs-8" style="border-right:1px dotted gray; padding:0px; font-size: 18px; line-height:27px;">location : <c:if test="${data.path ne ''}">/${data.path}</c:if>/${data.folder}</div>
		<div onclick="goHome()" class="col-xs-4" style="cursor:pointer;"><span class="glyphicon glyphicon-eject pull-right" style="font-size:20px;"></span><span style="font-size:18px;">홈으로</span></div>
	</div>
	<div class="col-xs-12" style="overflow:scroll; height:330px; border:1px solid gray; padding:0px; overflow-x:hidden; z-index:4;  background-color:white;">
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
	<div class="col-xs-2" style="padding:3px 0px 0px 0px; z-index:3; height:47px; background-color:white;"><button id="torrentFolder" onclick="goTorrentFolder()" class="btn btn-default">업로드 폴더</button></div>
	<div class="col-xs-10 text-right" style="margin:0px; padding:3px 0px 0px 0px; z-index:3; height:47px; background-color:white;">
		<form enctype="multipart/form-data" style="display:inline;">
			<label id="fileInputLabel" for="btnFileInput"><input id="btnFileInput" class="btn btn-default" type="file" id="attachFile" name="attachFile" multiple="multiple" style="display:inline;"/></label>
			<button id="btnUpTorrent" class="btn btn-success" type="button">파일 업로드</button>
		</form>
		<button id="btnTorrent" class="btn btn-primary" type="button" data-toggle="collapse" data-target="#divTransmission" aria-expanded="false" aria-controls="divTransmission" onclick="makeToolbar()" type="button">
			다운로드 진행상황
		</button>
	</div>
	<div class="col-xs-12 collapse" id="divTransmission" style="height:400px; padding:0px; top:-68px; z-index:1;">
		<iframe id="transmission" height="500px" class="col-xs-12" src="http://tiramisutime.xyz:2291/transmission/web/" style="padding:0px;"></iframe>
	</div>
	<div class="col-xs-12" style="height:25px; top:-92px; z-index:2; background-color:white;"></div>
</div>
</body>
</html>