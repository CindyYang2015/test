$(document).ready(function() {
	//$("#frontImg").uploadPreview({ Img: "frontImage", Width: 200, Height: 200 });
	//$("#backImg").uploadPreview({ Img: "backImage", Width: 200, Height: 200 });
	
	$('#idcard_front_upload_img').fileupload({
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
        	$("#idcard_front_show_img").attr("src",image);
        	$("#idcardFrontImgId").val(imgData);
        }
    });
	
	
	$('#idcard_back_upload_img').fileupload({
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
        	$("#idcard_back_show_img").attr("src",image);
        	$("#idcardBackImgId").val(imgData);
        }
    });
	
	$('#ocrFormSubmit').click(function() {
		$("#uploadImgOcrForm").ajaxSubmit({
			async: false,
			beforeSubmit:function(formData, jqForm, options){				
				//默认easy-ui-validator验证
				var validateStatus = $('#uploadImgOcrForm').form('validate'); 	
				if(validateStatus == true) {
					return true;
				} else {
					/*$.messager.alert('验证错误',"红色标注项为必输项，请完成输入！！","error");*/
					return validateStatus;
				}			
			},
			success:function(res) {				
				$('#ocr_result').css("display","");
				$('#ocr_result').html("<xmp>"+res+"</xmp>");				
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
function setOcrImage(type) {	
	if(type == 1) {
		$("#frontImg").click();
	} else {
		$("#backImg").click();
	}
	
}
