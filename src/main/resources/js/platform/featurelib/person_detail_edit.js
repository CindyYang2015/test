$(function() {
	
	//初始化上传文件
	$('#person_feature_file_edit').fileupload({
        dataType: 'json',  
        add: function (e, data) {        	
        	//验证图片类型
        	if(!checkFileType(data.files[0].name)){
        		return;
        	}
            data.submit();
        },
        done: function (e, result) {
        	var imgData = result.result.data.image;
        	var image = "data:image/png;base64,"+imgData;
        	$("#previewImg_person_edit").attr("src",image);
        	$("#person_feature_file").val(imgData);
        }
    });
	
	//初始化算法引擎
	$('#engineId_person_feature_edit').combobox({
		url:"../step/validEngineAllList",
        editable:false,
        method:"post",
        valueField: 'engineCode',
        textField: 'engineName',        
        onSelect:function(record){
                var engineCode = record.engineCode;//得到当前ID
                $('#engineType_person_feature_edit').val(record.engineType);
            	$('#engineverId_person_feature_edit').combobox({
            		url:"../step/selectEngineVer?engineCode="+engineCode,
                    editable:false,
                    method:"post",
                    valueField: 'verCode',
                    textField: 'verNo',
                    onLoadSuccess: function () { //数据加载完毕事件
                    	var data = $('#engineverId_person_feature_edit').combobox('getData');
                        if (data.length > 0) {
                            $("#engineverId_person_feature_edit").combobox('select', data[0].verCode);
                        }
                    },
                    onSelect:function(result){
                    	var personId = $("#personId_person_feature_edit").val();
                    	var partitionId = $("#partitionId_person_feature_edit").val();
                    	var enginev = result.verCode;//得到当前ID
                    	$.ajax({
                    		url : "../person/selectFeatureByEngine",
                    		dataType:"json",
                    		type:"post",
                    		data:{"partitionId":partitionId,"engineType":record.engineType,"engineCode":engineCode,"engineVerCode":enginev,"personId":personId},
                    		success : function(data) {
                    			if(data.success){
                    				var filePath = data.entity.filePath;
                    				$("#previewImg_person_edit").attr("src","../recog/getFile?fileUrl="+filePath);
                    			}else {
                    				$("#previewImg_person_edit").attr("src","../images/upload.png");
                    			}
                    		},
                    		error:function() {
                    			$.messager.alert('错误',"服务器发生错误,请稍后重试！","error");
                    		}
                    	});
                    }
                });                
                
        }
	});
});

/*$(document).ready(function(){
	$("#person_feature_file_edit").uploadPreviewAll({ Img: "previewImg_person_edit", Width: 100, Height: 100 });
});

function showPersonFreatureLoadImgEdit(){
	$("#person_feature_file_edit").click();	
}*/