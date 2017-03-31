(function() {
//	$("#face_img_file").uploadPreview({ Img: "find_face_idcard_compareImg", Width: 200, Height: 200 });

})(jQuery);

$(document).ready(function() {
	
	$('#face_search_upload_img').fileupload({
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
        	$("#face_search_show_img").attr("src",image);
        	$("#faceSearchImgId").val(imgData);
        }
    });
	
	$('#start_search').click(function() {
		$('#showPersonByFace').html('');
		$("#form_faceinfo").ajaxSubmit({
			async: false,
			dataType:'json',
			beforeSubmit:function(formData, jqForm, options){				
				//默认easy-ui-validator验证
				var validateStatus = $('#form_faceinfo').form('validate'); 	
				if(validateStatus == true) {
					return true;
				} else {
					return validateStatus;
				}			
			},
			success : function(data) {				
				if (data && data.result.code == '0000') {
					var snciRes = data.result.data.personList;
					if(!snciRes || snciRes.length < 1) return;
					for (var i = 0; i < snciRes.length; i++) { 	
						$('#showPersonByFace')
						.append(
								'<div style="width:255px;height:255px;float:left;overflow:hidden;text-overflow:ellipsis;white-space:nowrap;">'
								+ '<div style="width:250px;height:140px; margin:0 0 0 20px;"><img src="'
								+ '../recog/getFile?fileUrl='+snciRes[i].filePath
								+ '" name="img" id="img" style="width:150px; height:150px;" /></div>'
								+ '<div style="width:250px;height:100px;line-height:20px;margin:15px 0 0 20px;"><span style="font-size:14px; font-weight:bold;">证件号码 : </span><span style="font-size:14px;" name="cId" id="cId">'
								+ snciRes[i].ctfno
								+ '</span><br /><span style="font-size:14px; font-weight:bold;">证件类型 : </span><span style="font-size:14px;" name="cType" id="cType">'
								+ snciRes[i].ctftype
								+ '</span><br /><span style="font-size:14px; font-weight:bold;">证件姓名 : </span><span style="font-size:14px;" name="cName" id="cName">'
								+ snciRes[i].ctfname
								+ '</span><br /><span style="font-size:14px; font-weight:bold;">相似度 : </span><span style="font-size:14px;" name="sim" id="sim">'
								+ snciRes[i].score
								+ '</span><br /></div></div>');
					}
				} else {
					$('#showPersonByFace').text('检索失败');
				}
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
function setFindFaceToIDCardImage() {	
	$("#face_img_file").click();	
}
