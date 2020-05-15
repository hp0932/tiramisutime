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
});

function download(fileName){
	var down = "/fileview/download?fileName=" + fileName;
	location.href=down;
}
</script>
<title>다운로더</title>
</head>
<body>
<div class="container">
	<div class="col-xs-12" style="overflow:scroll; height:330px; border:1px solid gray; padding:0px;">
		<table class="table table-hover table-condensed" style="margin:0px;">
			<c:forEach items="${data.fileList}" var="result" varStatus="i">
				<tr>
					<td onclick='download("${result.fileName}")'>
						<div class="col-xs-8" style="border-right:1px dotted gray;">${result.fileName}</div><div class="col-xs-4">${result.fileSize}</div>
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