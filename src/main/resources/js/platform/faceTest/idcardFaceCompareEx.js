$(document).ready(function() {
	//$("#spotImg").uploadPreview({ Img: "spotImage", Width: 200, Height: 200 });
	//$("#idcardImg").uploadPreview({ Img: "idcardImage", Width: 200, Height: 200 });
	//$("#netCheckImg").uploadPreview({ Img: "netCheckImage", Width: 200, Height: 200 });
	
	$('#face_spot_upload_img').fileupload({
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
        	$("#face_spot_show_img").attr("src",image);
        	$("#spotImgId").val(imgData);
        }
    });
	
	$('#face_idcardex_upload_img').fileupload({
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
        	$("#face_idcardex_show_img").attr("src",image);
        	$("#idcardImgId").val(imgData);
        }
    });
	
	$('#face_netcheck_upload_img').fileupload({
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
        	$("#face_netcheck_show_img").attr("src",image);
        	$("#netCheckImgId").val(imgData);
        }
    });
	
	$("#submit_two_face").click(function() {
		$("#uploadImgTwoCardForm").ajaxSubmit({
			async: false,
			beforeSubmit:function(formData, jqForm, options){	
				//debugger;
				//默认easy-ui-validator验证
				var validateStatus = $('#uploadImgTwoCardForm').form('validate'); 	
				if(validateStatus == true) {
					return true;
				} else {
					/*$.messager.alert('验证错误',"红色标注项为必输项，请完成输入！！","error");*/
					return validateStatus;
				}			
			},
			success : function(res) {				
				//var data=eval('('+res+')');	
				$('#two_idcard_face_similar_result').css("display","");
				$('#two_idcard_face_similar_result').html("<xmp>"+res+"</xmp>");
				/*$('#similar_result_face_code').html(data.code);
				$('#similar_result_face_message').html(data.message);
				if (data.code == 0) {					
					$('#similar_result_face_data').css("display","");
					$('#similar_result_face_sim').html(data.data.sim);
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
function setFaceToIDCardImageEx(type) {	
	if(type == 1) {
		$("#spotImg").click();
	} else if(type == 2){
		$("#idcardImg").click();
	}else{
		$("#netCheckImg").click();
	}
	
}
