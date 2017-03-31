$(document).ready(function() {
	//$("#face_img1").uploadPreview({ Img: "face_face_compareImg1", Width: 200, Height: 200 });
	//$("#face_img2").uploadPreview({ Img: "face_face_compareImg2", Width: 200, Height: 200 });
	
	//人脸图片1上传
	$('#face_img1').fileupload({
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
        	$("#face_face_compareImg1").attr("src",image);
        	$("#compareFaceImg1Id").val(imgData);
        }
    });
	
	//人脸图片2上传
	$('#face_img2').fileupload({
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
        	$("#face_face_compareImg2").attr("src",image);
        	$("#compareFaceImg2Id").val(imgData);
        }
    });
	
	$('#similarity_compare_face').click(function() {
		$("#uploadImgFaceFaceForm").ajaxSubmit({
			async: false,
			beforeSubmit:function(formData, jqForm, options){				
				//默认easy-ui-validator验证
				var validateStatus = $('#uploadImgFaceFaceForm').form('validate'); 	
				if(validateStatus == true) {
					return true;
				} else {
					/*$.messager.alert('验证错误',"红色标注项为必输项，请完成输入！！","error");*/
					return validateStatus;
				}			
			},
			success : function(res) {				
				$('#similar_result_face').css("display","");
				$('#similar_result_face').html("<xmp>"+res+"</xmp>");
				
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
function setFaceToFaceImage(type) {	
	if(type == 1) {
		$("#face_img1").click();
	} else {
		$("#face_img2").click();
	}
	
}
