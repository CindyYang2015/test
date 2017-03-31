$(function() {
	
});
/**
 * 设置操作状态的名称
 * @param status
 */
function setOperStatusName(value, row, index) {
	if(value == 1) {
		return "通过";
	} else if(value == 2){
		return "待审核"
	}else if(value == 0){
		return "不通过"
	}else{
		return "异常状态"
	}
}

//根据主键逻辑删除黑名单信息
function deleteBlackPersonByPrimaryKey(){
	//待审核的客户不能操作
	var rows = $('#manage_blackPerson_datagrid').datagrid('getChecked');
	if(rows && rows.length > 0) {
		if(rows[0].status && rows[0].status == 2) {
			$.messager.alert('提示', '待审核的黑名单客户不能操作');
			return;
		} 
	}
	//如果用户设置了相关的资料，不能删除
	$.acooly.framework.removes('../blackPerson/removeBlackPersonByPrimaryKey','blackId','manage_blackPerson_datagrid');
}

/**
* 创建黑名单信息
*/
function createBlackPerson() {		
	$.acooly.framework.customDialog({
		url:'../blackPerson/create.html',
		entity:'blackPerson',
		title:'创建黑名单',
		reload:true,
		width:600,
		height:300
		},{
			buttonName:'确定'
		});
}

/**
* 修改黑名单信息
*/
function editBlackPerson() {
	$.acooly.framework.customDialog({
		url:'../blackPerson/edit.html',
		entity:'blackPerson',
		title:'修改黑名单',
		reload:true,
		width:600,
		height:300
		},{
			buttonName:'确定',
			isCheckselect:true,
			param:'id',
			afterSelectRowCallback:function(row) {
				if(row && row.length > 0) {
					if(row[0].status && row[0].status == 2) {
						$.messager.alert('提示', '待审核的黑名单客户不能操作');
						return false;
					} 
				} 
				return true;
			}
		});
}
