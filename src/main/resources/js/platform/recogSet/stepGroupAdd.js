$(function() {
	
	//初始化算法引擎
	$('#manage_stepGroup_editform #steps').combobox({
		url:"../stepGroup/stepList",
        editable:false,
        method:"post",
        multiple:true,
        valueField: 'id',
        textField: 'stepName',
        onLoadSuccess: function () {
        	if($("#action").val()=='edit'){
        		$('#manage_stepGroup_editform #steps').combobox("setValues",$("#manage_stepGroup_editform #stepsTemp").val());
        	}
        }
	});
	
//	//表单提交之前做一些业务处理
//	$.acooly.framework.submitBefore = function(){
//		var stepids = $('#manage_stepGroup_editform #steps').combobox('getValues');
//		if(stepids!=''){
//			$("#stepIds").val(stepids);
//			return true;
//		}else{
//			$.messager.alert("提示","请选择策略");
//			return false;
//		}
//	};
	
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