$(document).ready(function(){
	//$("#face_idcard_upload_img").uploadPreview({ Img: "face_idcard_compareImg", Width: 200, Height: 200 });
	
	$('#face_idcard_upload_img').fileupload({
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
        	$("#face_idcard_show_img").attr("src",image);
        	$("#faceIdcardImg").val(imgData);
        }
    });
	
	$('#btn_valide_similarity').click(function(){
		$("#uploadImgFaceIdcardForm").ajaxSubmit({
			async: false,
			beforeSubmit:function(formData, jqForm, options){				
				//默认easy-ui-validator验证
				var validateStatus = $('#uploadImgFaceIdcardForm').form('validate'); 	
				if(validateStatus == true) {
					return true;
				} else {
					return validateStatus;
				}			
			},
			success : function(res) {
				//var data=eval('('+res+')');	
				$('#face_idcard_result').css("display","");
				$('#face_idcard_result').html("<xmp>"+res+"</xmp>");
				/*$('#face_idcard_result_code').html(data.code);
				$('#face_idcard_result_message').html(data.message);
				if (data.code == 0) {					
					$('#face_idcard_result_data').css("display","");
					$('#face_idcard_result_sim').html(data.data.sim);
				}*/
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
function setFaceToIDCardImage() {	
	$("#face_idcard_upload_img").click();	
}
