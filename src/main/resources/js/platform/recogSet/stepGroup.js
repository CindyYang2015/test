$(function() {
	/**
	 * 加载识别规则数据
	 */
	$('#manage_stepGroup_datagrid').datagrid({  
		url:'../stepGroup/list' 
	}); 
	
});
/**
 * easyui dataGid格式化状态 1:有效 2:无效
 */
function statusFormatter(val, row, index){
    if (val=="1")
    	return "有效";
    if (val=="0")
        return "无效";    
}


function submitBeforeDo(){
	var stepids = $('#steps').combobox('getValues');
	if(stepids!=''){
		$("#stepIds").val(stepids);
		return true;
	}else{
		$.messager.alert("提示","请选择策略");
		return false;
	}
}