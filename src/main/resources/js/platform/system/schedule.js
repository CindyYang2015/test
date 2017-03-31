
/**
 * 设置任务状态名称
 * @param value
 * @param row
 * @param index
 */
function setTaskStatusName(value, row, index) {
	if(value == 1) return "已开启";
	else if(value == 0) return "已关闭";
	else return "未定义";
}

/**
 * 格式化任务状态操作按钮
 * @param value
 * @param row
 * @param index
 * @returns
 */
function optFormatterTaskStatus(value, row, index) {
	if(row.status == 0) return "";
	var action = $("#manage_schedule_action").html();
	action = action.replace(/{taskCode}/g,row.taskCode);
	action = action.replace(/{taskStatus}/g,row.taskStatus);
	if(row.taskStatus == 1) {
		action = action.replace(/{operName}/g,"关闭");
	} else if(row.taskStatus == 0) {
		action = action.replace(/{operName}/g,"开启");
	} 
	return action;
}

/**
 * 更新任务状态
 * @param taskCode
 * @param taskStatus
 */
function openTaskStatusDlg(taskCode,taskStatus) {
	setTimeout(function(){
		if(taskStatus == 1) {
			//关闭任务
			$.acooly.framework.removes('../schedule/closeTask','taskCode','manage_schedule_datagrid');
		} else if(taskStatus == 0) {
			//开启任务
			$.acooly.framework.removes('../schedule/openTask','taskCode','manage_schedule_datagrid');
		}
	}, 100);	
}

/**
 * 查看任务记录
 * @param taskCode
 */
function openTaskRecord(taskCode){
	$.acooly.framework.createWindow("../schedule/recordList?taskCode="+taskCode, 800, 600, "任务执行记录");
}
