$(function() {


		//初始化算法引擎
	$('#manage_recognizerule_editform #inputRecogstepId').combobox({
			url:"../recognizerule/recogStepList",
	        editable:false,
	        method:"post",
	        valueField: 'id',
	        textField: 'recogstepName',      
	        required:true,
	        onSelect:function(record){
	        	    var id = record.id;//得到当前选中ID
	        	    if(id!=null && id!=''){
		            	$('#manage_recognizerule_editform #inputEngineCode').combobox({
		            		url:"../recognizerule/engineList?recogStepId="+id,
		                    editable:false,
		                    method:"post",
		                    valueField: 'engineCode',
		                    textField: 'engineName',
		                    required:true,
		                    onLoadSuccess: function () { //数据加载完毕事件
		                    	//debugger;
		                    	var data = $('#manage_recognizerule_editform #inputEngineCode').combobox('getData');
		                        if (data.length > 0) {
		                            $("#manage_recognizerule_editform #inputEngineCode").combobox('select', data[0].engineCode);
		                        }
		                    }
		                });                
	        	    }
	                
	        }
		});
	
	
	 var recogstepId = $('#manage_recognizerule_editform #inputRecogstepId').combobox("getValue");//得到当前选中ID
	 if(null!=recogstepId && recogstepId!=''){
		 $('#manage_recognizerule_editform #inputEngineCode').combobox({
	 		url:"../recognizerule/engineList?recogStepId="+recogstepId,
	         editable:false,
	         method:"post",
	         valueField: 'engineCode',
	         textField: 'engineName',
	         required:true
	     });	
	 }

	
//	//表单提交之前做一些业务处理
//	$.acooly.framework.submitBefore = function(){
//		var score = $("#inputScore").val();
//		
//		if(!isDouble(score)){
//	    	$.messager.alert("提示","请输入[0-1]之间的小数，作为阀值!");
//	    	return false;
//	    }
//	    if(!decimalDigits(score)){
//	    	$.messager.alert("提示","阀值的小数位数不能超过4位!");
//	    	return false;
//	    }
//	    if(score<0 || score>1 ){
//	    	$.messager.alert("提示","阈值的取值范围在[0-1]之间!");
//	    	return false;
//	    }
//	    return true;
//	};
	
	
});


/**
 * 数字格式化
 * 列表显示4位小数
 */
function double2Formatter(val, row, index){
	if(val){//因为有默认值是未定义，需要判断，直接使用可能会出错
		val=val.toFixed(4);//格式化,保留 4位小数 
	}
	return val; 	
}

function isDouble(s){
	 var regu="^[0-9]+(.[0-9]*)?$"//正则表达式
	 var regexp= new RegExp(regu);
	 if(regexp.test(s))//判断是整数或者小数
	  return true;
	 else
	  return false;
}
function decimalDigits(s){
	var a=s.split(".");
	if(a[1]!=null&&a[1].length>4){
    	return false;
	}else{
		return true;
	}
}