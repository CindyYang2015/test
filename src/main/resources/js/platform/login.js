/**
 * @author Jackson He
 * @function :
 * @Description: 初始化
 * @return: none
 */
$(document).ready(function(){
	
  $('#user_login_loginDialog').show().dialog({
      modal : true,
      title : '集成生物识别系统登录',
      closable : false      
    });
	 
	$("#workCode").focus();	
})


/**
 * @author Jackson He
 * @function :keyDown
 * @Description: 监听enter事件
 * @return: none
 */
function keyDown(flag){
	//enter键
	if(event.keyCode==13){
		if (flag == 1) {//用户名
			if (workCodeVilidate()) {
				$("#userPwd").focus();
			}
		} else if (flag == 2) {//用户密码
			if (userPwdVilidate()) {
				login();
			}
		} 
	}
}

/**
 * @author Jackson He
 * @function :login
 * @Description: 登录
 * @return: none
 */
function login(){
	
	$('#loginForm').ajaxSubmit({
		dataType:"json",       
		beforeSubmit:function(formData, jqForm, options){			
			//默认easy-ui-validator验证 $.md5($("#userPwd").val())
			formData[1].value = $.md5(formData[1].value);
			var validateStatus = $('#loginForm').form('validate'); 	
			if(validateStatus == true) {
				return true;
			} else {
				return validateStatus;
			}											
		},
		success:function(res, statusText){
			try {
				if (!res.success) {
					if(res.message == "用户首次登陆需要强制修改密码"){
						$.messager.confirm('提醒', res.message, function(r){
							if (r){
								intUserPwd();
							}
						});
					}else{
						$.messager.alert('提醒', res.message,'info');
						//刷新验证码
						$("#createCheckCode").attr("src","../pictureCheckCode?nocache=" + new Date().getTime());
					}
				} else {
					//调转至主界面
					location.href = "../login/index";
					
				}
			} catch (e) {	
				$.messager.alert('提示', e);
			}											
		},
		error:function() {
			$.messager.alert('错误',"服务器发生错误,请稍后重试！","error");
		}
	});		
}

/**
 * 重置表单数据
 */
function clearForm() {
	$("#workCode").val("");
	$("#userPwd").val("");
}

/**
 * @author Jackson He
 * @function :workCodeVilidate
 * @Description: 用户名验证
 * @return: boolean
 */
function workCodeVilidate(){
	if ($("#workCode").val() == null || $("#workCode").val() == '') {
		$("#workCode").focus();
		return false;
	} else {
		return true;
	}
}

/**
 * @author Jackson He
 * @function :userPwdVilidate
 * @Description: 用户密码验证
 * @return: boolean
 */
function userPwdVilidate(){
	if ($("#userPwd").val() == null || $("#userPwd").val() == '') {
		$("#userPwd").focus();
		return false;
	} else {
		return true;
	}
}

/**
 * 重新加载验证码
 */
function reloadRandCheckCode() {
	$("#createCheckCode").attr("src","../pictureCheckCode?nocache=" + new Date().getTime());
}

$("#workCode").on("keypress",function(e){	
	if(e.keyCode==13){
		$("#userPwd").focus();
	}
})
$("#userPwd").on("keypress",function(e){	
	if(e.keyCode==13){
		$("#randCheckCode").focus();
	}
})
$("#randCheckCode").on("keypress",function(e){
	if(e.keyCode==13){
		login();
	}
})

/**
 * 首次登陆用户强制修改密码 initPwd
 * @param workCode 用户工号
 * @param userPwd 用户密码
 */
function intUserPwd() {
	
	var workCode = $("#workCode").val();
	var userPwd = $("#password").val();
	var title="用户工号["+workCode+"]设置密码";
	
	$("#manage_user_initPwdform_workCode").val(workCode);
	$("#manage_user_initPwdform_userPwd").val(userPwd);
	//设置个弹出窗口标题
	$("#initPwdWindow").panel({title:title,width:400,height:150});	
	$("#initPwdWindow").window("open"); 
	
}

/**
 * 重新设置密码
 */
function initUserPwdBtn() {
	$('#manage_user_initPwdform').form('submit', {
		onSubmit:function(){
	    	var isValid = $(this).form('validate');
	    	if(!isValid){
	    		return false;
	    	}
	    	
	    	var newPwd = $('#user_newPassword').val();
	    	if(!validatePwdInput(newPwd)) {
	    		$.messager.alert('提示', '密码应满足至少8位，并包括数字、小写字母、大写字母和特殊符号4类中至少3类');
	    		return false;
	    	}
	    	//自定义检查合法性
			if(newPwd != $('#user_confirmNewPassword').val()){
				$.messager.alert('提示', '两次密码输入不一致');
				return false;
			}
			
			var md5_newPwd = $.md5(newPwd);
			$('#manage_user_initPwdform_userPwd').val(md5_newPwd);
			return true;						
		},
		success : function(data) {
			try {
				var result = $.parseJSON(data);
				if (result.success) {
					$("#initPwdWindow").window("close");
					//重新登录
					window.location.href = "../login/loginOut";

				}
				if(result.message){
					$.messager.alert({title : '提示',msg : result.message});	
				}
			} catch (e) {
				$.messager.alert('提示', '修改密码失败！');
			}
		},
		error:function() {
			$.messager.alert('错误',"服务器发生错误,请稍后重试！","error");
		}
	});	
}

/**
 * 关闭对话框
 */
function initUserPwdCancel() {
	$("#initPwdWindow").window("close"); 
	$("#manage_user_initPwdform_workCode").val("");
	$("#manage_user_initPwdform_userPwd").val("");
	$("#user_newPassword").val("");
	$("#user_confirmNewPassword").val("");
}


/**让window居中*/
var easyuiPanelOnOpen = function (left, top) {
    var iframeWidth = $(this).parent().parent().width();
   
    var windowWidth = $(this).parent().width();

    var setWidth = (iframeWidth - windowWidth) / 2;
    $(this).parent().css({/* 修正面板位置 */
        left: setWidth,
    });

    $(".window-shadow").hide();
};
$.fn.window.defaults.onOpen = easyuiPanelOnOpen;