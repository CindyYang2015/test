$(document).ready(function(){
	
	//$("#face_add_upload_img").uploadPreview({ Img: "faceAdd_img", Width: 200, Height: 200 });
	
	$('#face_add_upload_img').fileupload({
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
        	$("#faceAdd_img").attr("src",image);
        	$("#addfaceImgId").val(imgData);
        }
    });
	
	$('#btn_valide_faceAdd').click(function(){
		$('#btn_valide_faceAdd').linkbutton('disable');
		$("#uploadImgFaceAddForm").ajaxSubmit({
			async: false,
			beforeSubmit:function(formData, jqForm, options){				
				//默认easy-ui-validator验证
			
				var validateStatus = $('#uploadImgFaceAddForm').form('validate'); 	
				if(validateStatus == true) {
					
					return true;
					
				} else {
					return validateStatus;
				}		
				
			},
			success : function(res) {
				$('#face_add_result').css("display","");
				$('#face_add_result').html("<xmp>"+res+"</xmp>");	
			},
			error:function() {
				$.messager.alert('错误',"服务器发生错误,请稍后重试！","error");
				
			}
		
		});
		$('#btn_valide_faceAdd').linkbutton('enable');
		
	});
});

/**
 * 设置客户上传图片点击
 */
function setFaceAddImage() {	
	$("#face_add_upload_img").click();	
}
