$(function() {
	/**
	 * 加载单位数据
	 */
	$('#manage_organization_datagrid')
			.treegrid(
					{
						url : '../organization/selectPageOrganizationInfo',
						idField : 'orgCode',
						treeField : 'orgName'
						/*
						 * pageSize: 10, pageList: [ 10, 20, 30, 40, 50 ],
						 */						
						
					});
});

/**
 * easyui dataGid格式化状态 是否法人 1：是 0：否
 */
function statusFormatter(val, row, index) {
	if (val == "1")
		return "是";
	if (val == "0")
		return "否";
}

function checkAdd() {
	var rows = $('#manage_organization_datagrid').datagrid('getChecked');
	if (rows.length > 0 && rows[0].orgLevel == "网点") {
		/* alert("不可新建!"); */
		$.messager.alert('提示', "网点属于最小机构节点,不可在其下面新建子节点！");
	} else {
		$.acooly.framework.customDialog({
			url : '../organization/create.html',
			entity : 'organization',
			width : 500,
			height : 220,
			title : '新增'
		}, {
			buttonName : '确定',
			isCheckselect : true,
			param : 'orgCode',
			isTreeGrid : true
		});
	}
}

function checkEdit() {
	$.acooly.framework.customDialog({
		url : '../organization/edit.html',
		entity : 'organization',
		width : 500,
		height : 220
	}, {
		buttonName : '确定',
		isCheckselect : true,
		param : 'orgCode',
		isTreeGrid : true
	});
}