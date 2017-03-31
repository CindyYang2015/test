$(function() {
	/**
	 * 加载识别规则数据
	 */
	$('#manage_step_datagrid').datagrid({  
		url:'../step/list' 
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
//表单提交之前做一些业务处理
function submitBeforeCheck(){
	
	var englist=$('[id^=engineId_]');//模糊查询ID为engineId_的输入框
	var listValues = [];
	var valuesArr = [];
	var flag = false;
	$.each(englist,function(index,ele){
		var engId = ele.id;
		var idtime = engId.split("_")[1];
		var engVal = $('#'+engId).combobox('getValue');
		var verVal = $("#engineverId_"+idtime).combobox('getValue');
		if($.inArray(engVal,valuesArr)>-1){
			$.messager.alert("提示","不能选择重复的算法引擎");
			flag = true;
	    	return false; 
		}
		listValues.push(engVal+"_"+verVal);
		valuesArr.push(engVal);
	});
	if(flag){
		return false;
	}
	$("#engineIds").val(listValues.join("|"));
	return true;
}