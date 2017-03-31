$(function() {
	/**
	 * 加载数据
	 */
	$('#manage_dicValues_datagrid').datagrid({  
		url:'../dic/selectPageDicValues'
	});
});
/**
 * easyui dataGid格式化状态 1:启用，2：禁用
 */
function statusFormatter(val, row, index){
    if (val=="1")
    	return "启用";
    if (val=="2")
        return "禁用";    
}