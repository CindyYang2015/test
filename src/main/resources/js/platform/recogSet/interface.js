/**
 * 设置操作栏
 * @param value
 * @param row
 * @param index
 * @returns
 */
function optFormatterInterface(value, row, index) {
	var action = $("#manage_interface_action").html();
	action = action.replace(/{id}/g,row.id);
	return action;
}
/**
 * 设置操作状态的名称
 * @param status
 */
function setInterfaceStatusName(value, row, index) {
	if(value == 1) {
		return "启用";
	} else {
		return "停用"
	}
}
/**
 * 添加接口信息
 */
function addInterface(){
	$.acooly.framework.customDialog({
		url:'../interface/create.html',
		entity:'interface',
		width:450,
		height:250,
		title:'新增', 
		reload:true,
		onSuccess:function(result) {	
			return true;
		}
	},{
		buttonName:'确定'
	});
}

/**
 * 编辑接口信息
 */
function editInterface() {
	$.acooly.framework.customDialog({
		url:'../interface/edit.html',
		entity:'interface',
		width:450,
		height:250
		},{
			buttonName:'确定',
			isCheckselect:true,
			param:'id',
		});
}
