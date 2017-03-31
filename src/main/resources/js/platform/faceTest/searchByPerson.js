$(function() {
	$('#start_check_person')
			.click(
					function() {
						$('#shouPerson').html('');
						$("#find_user_card_form")
								.ajaxSubmit(
										{
											async: false,
											dataType:'json',
											beforeSubmit:function(formData, jqForm, options){				
												//默认easy-ui-validator验证
												var validateStatus = $('#find_user_card_form').form('validate'); 	
												if(validateStatus == true) {
													return true;
												} else {
													/*$.messager.alert('验证错误',"红色标注项为必输项，请完成输入！！","error");*/
													return validateStatus;
												}			
											},
											success : function(data) {
												if (data && data.result.code == '0000') {
													var snciRes = data.result.data.personList;
													for (var i = 0; i < snciRes.length; i++) {
														$('#shouPerson')
																.append(
																		'<div style="width:255px;height:300px;float:left;">'
																				+ '<div style="width:250px;height:140px; margin:0 0 0 20px;"><img src="'
																				+ '../recog/getFile?fileUrl='+snciRes[i].filePath
																				+ '" name="img" id="img" style="width:150px; height:150px;" /></div>'
																				+ '<div style="width:255px;height:150px; line-height:25px; margin:20px 0 0 20px;"><span style="font-size:14px; font-weight:bold;">证件号码 : </span><span style="font-size:14px; font-weight:bold;" name="cId" id="cId">'
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
													$('#shouPerson').text('检索失败');
												}
											},
											error:function() {
												$.messager.alert('错误',"服务器发生错误,请稍后重试！","error");
											}
											
										});
					});

});



