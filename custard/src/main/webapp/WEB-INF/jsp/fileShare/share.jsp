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
	//파일 업로드
	$(document).on('click','#btnUpTorrent',function(){
	 	var data = $($(this).parents('form'));
	 	
	 	data.attr('method','POST');
	 	data.attr('action',"<c:url value='/fileshare/upload'/>");
	 	data.submit();
	});
});

//파일 다운로드
function download(fileName){
	//패스가 없을 경우 폴더값을 가져와서 다운로드
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
	data.attr('action', "<c:url value='/fileshare/download'/>");
	
	data.append($('<input/>', {type: 'hidden', name: 'fileName', value: fileName}));
	data.submit();
	//var down = "/fileview/download?fileName=" + fileName;
	//location.href=down;
}

//폴더 다운로드
function dirDown(folderName){
	//패스가 없을 경우 폴더값을 가져와서 다운로드
	var path = "${data.path}";
	if(path == ''){
		path = "${data.folder}";
	}else{
		path = path + "/" + "${data.folder}";
	}
	if(path != ''){
		folderName = path + "/" + folderName;
	}
	//해당 폴더 내부의 파일 갯수가 몇개인지 가져옴(list length)
	$.ajax({
		url : "<c:url value='/fileshare/count'/>",
		type : "POST",
		data : {
			'folderName' : folderName
		},
		success : function(result){
			//파일 갯수만큼 폴더 내부파일 다운로드 실행
			for (var i = 0; i < result; i++) {
				getDir(folderName, i);
			}
		}
	});
}

//폴더 다운로드 실행
function getDir(folderName, count){
	//0.5초씩 지연실행
	//count를 곱하여 파일 count*0.5초 지연실행. 0.5/1.0/1.5 순으로...
	var iv = count * 500;
	setTimeout(function(){
		var url = '/fileshare/dirDown';
		//count값으로 name값을 주입하여 iframe 생성, body에 append
		var frame = $('<iframe name="' + count + '" style="display: none;"></iframe>');
		frame.appendTo("body");
		
		//form을 만들어 iframe을 통해 submit, count값을 name으로 가진 iframe에 target 지정
		var form = $('<form></form>');
		form.attr('name', 'downForm');
		form.attr('method', 'post');
		form.attr('action', url);
		form.attr('target', count);
		
		//form에 input값을 추가한 후, submit
		form.append($('<input/>', {type: 'hidden', name: 'folderName', value: folderName}));
		form.append($('<input/>', {type: 'hidden', name: 'count', value: count}));
		form.appendTo("body");
		
		form.submit();
	}, iv);
}

//파일 삭제
function fileDelete(fileName){
	if(confirm("파일 또는 폴더를 삭제하시겠습니까?")){
		//패스가 없을 경우 폴더값을 가져와서 삭제
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
		data.attr('action', "<c:url value='/fileshare/delete'/>");
		
		data.append($('<input/>', {type: 'hidden', name: 'fileName', value: fileName}));
		data.submit();
	}
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
	data.attr('action', "<c:url value='/fileshare/downloader'/>");
	
	//form을 만들어서 input type hidden형식으로 추가하여 post형식으로 발송
	data.append($('<input/>', {type: 'hidden', name: 'path', value: path}));
	data.append($('<input/>', {type: 'hidden', name: 'folderName', value: folder}));
	data.submit();
}

//돌아가기 버튼
function goBack(){
	window.history.back();
}

//홈으로 버튼
function goHome(){
	location.href="/fileshare/downloader"
}

</script>
<title>파일공유</title>
</head>
<body>
<div class="container">
	<div class="col-xs-12" style="border:1px solid gray; height:31px; padding-top:3px; border-radius:5px; margin-bottom:3px;">
		<div class="col-xs-8" style="border-right:1px dotted gray; padding:0px; font-size: 18px; line-height:27px;">location : <c:if test="${data.path ne ''}">/${data.path}</c:if>/${data.folder}</div>
		<div onclick="goHome()" class="col-xs-4" style="cursor:pointer;"><span class="glyphicon glyphicon-eject pull-right" style="font-size:20px;"></span><span style="font-size:18px;">홈으로</span></div>
	</div>
	<div class="col-xs-12" style="overflow:scroll; height:330px; border:1px solid gray; padding:0px; overflow-x:hidden; z-index:4; background-color:white;">
		<table class="table table-hover table-condensed" style="margin:0px;">
			<c:if test="${data.folder ne ''}">
				<tr>
					<!-- 뒤로가기(상위 폴더로) -->
					<td onclick='goBack()' style="cursor:pointer;">
						<div class="col-xs-12">
							<span class="glyphicon glyphicon-level-up" style="font-size:19px;"></span>&nbsp&nbsp<span></span><span style="font-weight:bold;">..</span>
						</div>
					</td>
				</tr>
			</c:if>
			<c:forEach items="${data.folderList}" var="result" varStatus="i">
				<tr>
					<td style="padding:0px;">
						<!-- 폴더 선택 -->
						<div class="col-xs-10 col-md-11" style="cursor:pointer; padding:5px 15px 5px 15px;" onclick='cdFolder("${result.fileName}")'>
							<!-- 폴더 진입 -->
							<span class="glyphicon glyphicon-folder-open" style="font-size:15px;"></span>&nbsp&nbsp<span>${result.fileName}</span>
						</div>
						<div class="col-xs-2 col-md-1 text-right" style="padding:4px 15px 3px 0px;">
							<!-- 폴더 다운로드 -->
							<button class="btn btn-primary" style="padding:3px 7px 1px 6px;" onclick="dirDown('${result.fileName}')"><span class="glyphicon glyphicon-download-alt" style="font-size:14px;"></span></button>
							<!-- 폴더 삭제 -->
							<button class="btn btn-danger" style="padding:3px 6px 1px 6px;" onclick="fileDelete('${result.fileName}')"><span class="glyphicon glyphicon-trash" style="font-size:14px;"></span></button>
						</div>
					</td>
				</tr>
			</c:forEach>
			<c:forEach items="${data.fileList}" var="result" varStatus="i">
				<tr>
					<td style="padding:0px;">
						<!-- 파일 다운로드(파일명 div클릭시) -->
						<div onclick='download("${result.fileName}")' class="col-xs-8" style="border-right:1px dotted gray; cursor:pointer; padding:5px 15px 5px 15px;">
							<span class="glyphicon glyphicon-file" style="font-size:18px;"></span>&nbsp&nbsp<span>${result.fileName}</span>
						</div>
						<div class="col-xs-3" style="padding:5px 15px 5px 15px;">${result.fileSize}</div>
						<div class="col-xs-1 text-right" style="padding:4px 15px 3px 15px;">
							<button class="btn btn-danger" style="padding:3px 6px 1px 6px;" onclick="fileDelete('${result.fileName}')"><span class="glyphicon glyphicon-trash" style="font-size:14px;"></span></button>
						</div>
					</td>
				</tr>
			</c:forEach>
			<tr><td></td></tr>
		</table>
	</div>
	<div class="col-xs-12 text-right" style="margin:0px; padding:3px 0px 0px 0px; z-index:3; height:44px; background-color:white;">
		<form enctype="multipart/form-data" style="display:inline;">
			<label id="fileInputLabel" for="btnFileInput"><input id="btnFileInput" class="btn btn-default" type="file" id="attachFile" name="attachFile" multiple="multiple" style="display:inline;"/></label>
			<button id="btnUpTorrent" class="btn btn-success" type="button">파일 업로드</button>
		</form>
	</div>
</div>
</body>
</html>