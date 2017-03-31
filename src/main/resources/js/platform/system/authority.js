$(function() {
	var legalOrgCode = $("#legalOrgCode").attr("value");
	var url = "../authority/selectAuthoritiesByPage?legalOrgCode="+legalOrgCode;
	$('#manage_authority_datagrid').datagrid({
		url:url,
		onClickRow: function(index,row){
			$.acooly.framework.updateEditRows(index,row,{
				id:'manage_authority_datagrid',
				isUpdate:1,
				url:'../authority/saveAuthorities',
				params:['authorityId','authorityName']
			});
		}
	});
});


//保存权限信息
function saveAuthorities() {
	$.acooly.framework.saveEditRows({
		id:'manage_authority_datagrid',
		url:'../authority/saveAuthorities',
		params:['authorityId','authorityName'],
		optParam:['legalOrgCode']
	});
}

//删除权限信息
function deleteAuthorities() {
	var rows = $('#manage_authority_datagrid').datagrid('getChecked');
	if(!rows || rows.length < 1) return;
	if(rows[0].authorityId == '0') {
		var index = $('#manage_authority_datagrid').datagrid('getRowIndex');
		$('#manage_authority_datagrid').datagrid('deleteRow',index);
		return;	
	}
	$.acooly.framework.removes('../authority/deleteJson','authorityId','manage_authority_datagrid');
}

/**
 * 设置权限与资源的关系
 */
function setAuthRes() {
	$.acooly.framework.createTableDialog({
		url:'../resources/setAuthResource.html',
		width:800,
		height:600,
		title:'设定资源',
		btnName:'确定',
		listId:'manage_authority_datagrid',
		contentId:'manage_authResource_datagrid',
		contentIdField:'resourceId',
		valIdField:'authorityId',
		sumbitUrl:'../authority/saveAuthRes',
		istree:false
	});
}