
/**
 * 设置任务状态名称
 * @param value
 * @param row
 * @param index
 */
function setTaskRecordStatusName(value, row, index) {
	if(value == 1) return "进行中";
	else if(value == 2) return "完成";
	else if(value == 3) return "超时";
	else return "未定义";
}

$(function() {
	var taskCode = $('#taskCodeId').val();
	$("#manage_scheduleRecord_datagrid").datagrid({
		url:'../schedule/selectscheduleRecordByPage',		
		queryParams:{
			taskCode:taskCode
		}
	}); 
});