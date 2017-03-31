$(document).ready(function(){
	//上传的图片预览
	//$("#bankCard_upload_img").uploadPreview({Img: "bankCard_compareImg", Width: 200, Height: 200 });
	$('#bankCard_upload_img').fileupload({
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
        	$("#bankCard_show_img").attr("src",image);
        	$("#bankCardImgId").val(imgData);
        }
    });
	
	//提交表单
	$('#bankOcrFormSubmit').click(function(){
		$("#uploadImgBankOcrForm").ajaxSubmit({
			async: false,
			beforeSubmit:function(formData, jqForm, options){	
				
				//默认easy-ui-validator验证
				var validateStatus = $('#uploadImgBankOcrForm').form('validate'); 	
				if(validateStatus == true) {
					return true;
				} else {
					return validateStatus;
				}			
			},
			success : function(res) {
				$('#face_bankCard_result').css("display","");
				$('#face_bankCard_result').html("<xmp>"+res+"</xmp>");
			},
			error:function() {
				$.messager.alert('错误',"服务器发生错误,请稍后重试！","error");
			}
		
		});
		
		
	});
});

/**
 * 设置客户上传图片点击
 */
function setBankCardImage() {	
	$("#bankCard_upload_img").click();	
}
