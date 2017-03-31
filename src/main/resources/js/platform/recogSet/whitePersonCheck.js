$(function() {
	/**
	 * 加载单位数据
	 */
});
/**
 * 设置操作状态的名称
 * @param status
 */
function setStatusName(value, row, index) {
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
/**
 * 设置操作状态的名称
 * @param status
 */
function setOperStatusName(value, row, index) {
	if(value == 1) {
		return "新增";
	} else if(value == 2){
		return "修改"
	}else if(value == 3){
		return "删除"
	}else{
		return "未知操作"
	}
}
function optFormatterWhitePersonCheck(value, row, index) {
	var rowStatus=row.status;
	if(rowStatus == 2){
		var action = $("#manage_WhitePerson_Check_action").html();
		action = action.replace(/{personId}/g,row.id);
		return action;
	}
	return null;
}

//根据主键逻辑审核客户信息
function checkWhitePersonByPrimaryKey(){
	var url = '../whitePersonCheck/check';
	$.acooly.framework.customDialog({reload:true,url:url,entity:'whitePersonCheck',width:400,height:200},{buttonName:'确定',isCheckselect:true,param:'id'});
}
