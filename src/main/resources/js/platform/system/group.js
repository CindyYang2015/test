$(function() {
	var legalOrgCode = $("#legalOrgCode").attr("value");
	var url = "../group/selectGroupByPage?legalOrgCode="+legalOrgCode;
	$('#manage_group_datagrid').datagrid({
		url:url,
		onClickRow: function(index,row){
			$.acooly.framework.updateEditRows(index,row,{
				id:'manage_group_datagrid',
				isUpdate:1,
				url:'../group/saveGroup',
				params:['groupId','groupCname','groupEname']
			});
		}
	});
});

//保存角色信息
function saveGroup() {
	$.acooly.framework.saveEditRows({
		id:'manage_group_datagrid',
		url:'../group/saveGroup',
		params:['groupId','groupCname','groupEname'],
		optParam:['legalOrgCode']
	});
}

//删除角色信息
function deleteGroup() {
	var rows = $('#manage_group_datagrid').datagrid('getChecked');
	if(!rows || rows.length < 1) return;
	if(rows[0].groupId == '0') {
		var index = $('#manage_group_datagrid').datagrid('getRowIndex');
		$('#manage_group_datagrid').datagrid('deleteRow',index);
		return;	
	}
	$.acooly.framework.removes('../group/deleteJson','groupId','manage_group_datagrid');
}


/**
 * 设定角色的权限
 */
function setGroupAuthority() {
	$.acooly.framework.createTableDialog({
		url:'../authority/setGroupAuthority.html',
		width:500,
		height:400,
		title:'设定权限',
		btnName:'确定',
		listId:'manage_group_datagrid',
		contentId:'manage_groupAuthority_datagrid',
		contentIdField:'authorityId',
		valIdField:'groupId',
		sumbitUrl:'../group/saveGroupAuthority',
		istree:false
	});
}