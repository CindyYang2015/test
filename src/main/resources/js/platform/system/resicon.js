$(function() {
	//提交请求
	$.ajax({
		url : "../resources/resourceIconList",
		dataType:"json",
		type:"post",
		success : function(result) {
			//添加图标
			if(result && result.rows && result.rows.length > 0) {
				var rowCounts = 6;
				var curRowIndex = 0;
				var rowTrTemplate = '<tr id="resTrId{index}"></tr>';
				var rowTdTemplate = '<td onclick="setResImageBg(this);" ondblclick="getResImagePath(\'{imagePath}\',this)"><img style="padding:2px;" src="..{imagePath}" /></td>';
				$.each(result.rows,function(i,row){
					if(i%rowCounts == 0) {
						curRowIndex = i / rowCounts;						
						var rowTr = rowTrTemplate.replace(/{index}/g,curRowIndex);
						$("#resIconTableId").append(rowTr);
					} 	
					
					var tdStr = rowTdTemplate.replace(/{imagePath}/g,row);
					$("#resTrId"+curRowIndex).append(tdStr);
					
				});
			}
		},
		error:function() {
			$.messager.alert('错误',"服务器发生错误,请稍后重试！","error");
		}
	});
});

/**
 * 设置资源图片背景
 * @param obj
 */
function setResImageBg(obj) {
	if(!obj) return;
	$("#resIconTableId").find("img").css("background","none");
	$(obj).find("img").css("background","#7ebfef");
}

/**
 * 获取资源图标信息
 * @param imagePath
 */
function getResImagePath(imagePath,obj) {
	$("#manage_resource_editform").find("img").attr("src",".."+imagePath);
	$("#resourceImage").val(imagePath);
	var dialogId = $("#dialogId").val();	
	$("#"+dialogId).dialog('destroy');
}