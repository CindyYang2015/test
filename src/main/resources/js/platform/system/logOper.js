/**
 * 设置操作状态的名称
 * @param status
 */
function setOperStatusName(value, row, index) {
	if(value == 1) {
		return "成功";
	} else {
		return "失败"
	}
}