$(function() {
	$('#department_west_tree').tree({
		url : '../organization/selectTreeOrgAndDeptInfo',
		method:"post",
		parentField : 'id',
		lines : true,			
		onClick : function(node) {	
			selectedUser(node.id);
		},
		loadFilter:function(data,parent) {		
			setUTreeText(data);
			return data;
		}
	
	});
});

/**
 * 设置树节点显示文本格式
 * @param nodes 树节点
 */
function setUTreeText(nodes) {
	if(nodes && nodes.length > 0) {
		$.each(nodes,function(i,node){
			var nodeId = node.id;
			node.showText = node.text;
			node.text=node.text=  "<span id='userCheckBox" +nodeId+ "' class='user tree-checkbox tree-checkbox0' onclick=\"selectedUser('" + node.id + "')\"/>" + node.text;
			setUTreeText(node.children)
		});		
	}
}
/**
 * 设置树节点被选中
 * @param id
 */
function selectedUser(id){
	debugger;
	  $("#department_west_tree .user").removeClass("tree-checkbox1");
	  $("#userCheckBox" + id).addClass("tree-checkbox1");
	  
	  var node = $('#department_west_tree').tree('find', id);
	  $('#department_west_tree').tree('select', node.target);
	  var queryParams = {};
	  if(node.status == 3) {
	        queryParams.deptCode = node.id;
	        $("#deptCode").val(node.id);
	        $("#orgCodePath").val(null);
	      } else {
	        queryParams.orgCode = node.id;
	        $("#orgCodePath").val(node.idPath);
	        $("#deptCode").val(null);
	      }
	      $('#manage_user_datagrid').datagrid({
	        url:"../user/selectUserByPage",
	        queryParams:queryParams
	      });
}
/**
* 判断是否选中部门，true选中 false未选中
*/
function isSelectDept() {
var selectRow = $('#department_west_tree').tree("getSelected");
return selectRow;
}
/**
* 添加用户信息
*/
function addUser() {
var selectRow = isSelectDept();
if(!selectRow || selectRow.length < 1 || selectRow.status != 3) {
	$.messager.alert('提示', '请先选中部门');
	return;
}
//单位名称
var orgName = encodeURI(encodeURI($('#department_west_tree').tree("getParent",selectRow.target).showText));
//部门名称
var deptName = encodeURI(encodeURI(selectRow.showText));
var url = '../user/create.html?orgName='+orgName+"&deptName="+deptName+'&deptCode='+selectRow.id;
$.acooly.framework.customDialog({reload:true,url:url,entity:'user',width:500,height:230,title:'新增'},{buttonName:'确定'});
}

/**
* 修改用户信息
*/
function editUser() {		
	var url = '../user/edit.html';
	$.acooly.framework.customDialog({reload:true,url:url,entity:'user',width:500,height:230},{buttonName:'确定',isCheckselect:true,param:'userId'});
}

/**
 * 删除用户
 */
function deleteUser() {
	//如果用户设置了相关的资料，不能删除
	$.acooly.framework.removes('../user/deleteJson','userId','manage_user_datagrid');
}

/**
 * 设定用户角色
 */
function setUserRole() {
	$.acooly.framework.createTableDialog({
		url:'../group/setUserRole.html',
		width:600,
		height:400,
		title:'设定角色',
		btnName:'确定',
		listId:'manage_user_datagrid',
		contentId:'manage_userRole_datagrid',
		contentIdField:'groupId',
		valIdField:'userId',
		sumbitUrl:'../user/setUserRole',
		istree:false
	});
}

/**
 * 重置密码 resetPwd
 */
/*function resetUserPwd() {
	var checkedRow = $("#manage_user_datagrid").treegrid("getChecked");
	if(checkedRow && checkedRow.length > 0) {
		id = checkedRow[0].userId;
		$.acooly.framework.confirmSubmit("../user/resetPwd", id, "manage_user_datagrid");
	} else {
		$.messager.show({
			title : '提示',
			msg : '请至少勾选一条记录！'
		});
	}

}*/

/**
 * 重置密码 resetPwd
 * @param userId 用户id
 * @param workCode 用户工号
 */
function resetUserPwd() {
	var checkedRow = $("#manage_user_datagrid").treegrid("getChecked");
	if(checkedRow && checkedRow.length > 0) {
		var userId = checkedRow[0].userId;
		var workCode = checkedRow[0].workCode;
		var title="用户工号["+workCode+"]设置密码";
		//设置个弹出窗口标题
		$("#setUserPwdWindow").panel({title:title,width:400,height:150});	
		$("#setUserPwdWindow").window("open"); 
		$("#manage_user_setPwdform_userId").val(userId);
	} else {
		$.messager.alert({
			title : '提示',
			msg : '请至少勾选一条记录！'
		});
	}	
}

/**
 * 重新设置密码
 */
function setUserPwdBtn() {
	$('#manage_user_setPwdform').form('submit', {
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
			$('#manage_user_setPwdform_userPwd').val(md5_newPwd);
			return true;						
		},
		success : function(data) {
			try {
				var result = $.parseJSON(data);
				if (result.success) {
					$("#setUserPwdWindow").window("close"); 
					//重新登录
					//window.location.href = "../login/loginOut";
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
function setUserPwdCancel() {
	$("#setUserPwdWindow").window("close"); 
	$("#manage_user_setPwdform_userId").val("");
	$("#manage_user_setPwdform_userPwd").val("");
	$("#user_newPassword").val("");
	$("#user_confirmNewPassword").val("");
}