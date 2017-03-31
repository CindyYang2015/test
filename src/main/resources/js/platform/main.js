var contextPath="";
$(function() {

});

/**
 * 刷新当前center部分的页面
 * @param title
 */
function layout_center_refreshTab(title) {
	$('#layout_center_tabs').tabs('getTab', title).panel('refresh');
}

/**
 * 将左侧菜单点击的页面加载到center部分
 * @param opts
 */
function layout_center_addTabFun(opts) {
	var t = $('#layout_center_tabs');
	if (t.tabs('exists', opts.title)) {
		t.tabs('select', opts.title);
	} else {
		t.tabs('add', opts);
	}
}

/**
 * 修改密码
 */
function changePwd() {
	var dialogId = $.acooly.framework.getDivId();
	$('<div id="'+dialogId+'"></div>').dialog({
		href : '../html/changePassword.html',
		width : 400,
		height : 230,
		modal : true,
		title : '修改密码',
		buttons : [ {
			text : '保存',
			iconCls : 'icon-save',
			handler : function() {
				var d = $("#"+dialogId);
				$('#user_changePassword_form').form('submit', {
					onSubmit:function(){
				    	var isValid = $(this).form('validate');
				    	if(!isValid){
				    		return false;
				    	}
				    	
				    	var newPwd = $('#system_newPassword').val();
				    	if(!validatePwdInput(newPwd)) {
				    		$.messager.alert('提示', '密码应满足至少8位，并包括数字、小写字母、大写字母和特殊符号4类中至少3类');
				    		return false;
				    	}
				    	
				    	//自定义检查合法性
				    	if(newPwd != $('#system_confirmNewPassword').val()){
							$.messager.alert('提示', '两次密码输入不一致');
							return false;
						}
						return true;						
					},
					success : function(data) {
						try {
							var result = $.parseJSON(data);
							if (result.success) {
								d.dialog('destroy');
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
		} ],
		onClose : function() {
			$(this).dialog('destroy');
		},
		onLoad : function() {
			
		}
	});
}

/**
 * 验证密码 密码应满足至少8位，并包括数字、小写字母、大写字母和特殊符号4类中至少3类
 */
function validatePwdInput(pwdStr) {
	var pwdStatus = true;
    var regUpper = /[A-Z]/;
    var regLower = /[a-z]/;
    var regNumber = /[0-9]/;
    var regStr = /[^A-Za-z0-9]/;
    var complex = 0;
    
    if (regNumber.test(pwdStr)) {
        ++complex;
    }
    if (regLower.test(pwdStr)) {
        ++complex;
    }
    if (regUpper.test(pwdStr)) {
        ++complex;
    }
    if (regStr.test(pwdStr)) {
        ++complex;
    }
    if (complex < 3 || pwdStr.length < 8) {
    	pwdStatus = false;
    }
    return pwdStatus;
}

