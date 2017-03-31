
//初始化算法引擎
$('#manage_recognizerule_searchform #recogstepId').combobox({
	url:"../recognizerule/recogStepList",
    method:"post",
    valueField: 'id',
    textField: 'recogstepName',   
    panelHeight:'120',
    loadFilter:function(data){
        data.push({id:'',recogstepName:'全部'});
          return data;
     },
    onSelect:function(record){
            var id = record.id;//得到当前ID
        	$('#manage_recognizerule_searchform #engineCode').combobox({
        		url:"../recognizerule/engineList?recogStepId="+id,
                method:"post",
                valueField: 'engineCode',
                textField: 'engineName',
                panelHeight:'auto',
                onLoadSuccess: function () { //数据加载完毕事件
                	var data = $('#manage_recognizerule_searchform #engineCode').combobox('getData');
                    if (data.length > 0) {
                        $("#manage_recognizerule_searchform #engineCode").combobox('select', data[0].engineCode);
                    }
                }
            });                
            
    }
});

//查询方法
function submitSearch(){
	searchData('manage_recognizerule_searchform','manage_recognizerule_datagrid');
}

function searchData(searchForm, grid,type,url) {
	var queryParams = serializeObject($('#' + searchForm));
	//var param1=$("#recogstepId").textbox('getValue');
	var param2=$("#recogstepId").combobox("getText");
	/*var param3=$("#engineCode").textbox('getValue');
	var param4=$("#engineCode").combobox("getText");*/
	var param5=$("#status").textbox('getValue');
	if(""==param2){
		queryParams= {recogstepId: "", engineCode: "", status: param5}
	}
	if(isEmptyObject(queryParams)){
		queryParams = serializeObjectFromContainer($('#' + searchForm));
	}
	//验证输入框是否通过验证
	var validateStatus = $('#'+searchForm).form('validate');
	if(!validateStatus) {
		return;
	}
	if(type == 1) {
		$('#' + grid).treegrid('uncheckAll').treegrid('unselectAll').treegrid('clearSelections');
		if(url) {
			$('#' + grid).treegrid({
				url:url,
				queryParams:queryParams
			});
		} else {
			$('#' + grid).treegrid('reload', queryParams);
		}							
	} else {
		$('#' + grid).datagrid('uncheckAll').datagrid('unselectAll').datagrid('clearSelections');
		if(url) {
			$('#' + grid).datagrid({
				url:url,
				queryParams:queryParams
			});
		} else {
			$('#' + grid).datagrid('reload', queryParams);
		}		
		
	}
	
}
function submitBeforeValidate(){
	var score = $("#inputScore").val();
	
	if(!isDouble(score)){
    	$.messager.alert("提示","请输入[0-1]之间的小数，作为阀值!");
    	return false;
    }
    if(!decimalDigits(score)){
    	$.messager.alert("提示","阀值的小数位数不能超过4位!");
    	return false;
    }
    if(score<0 || score>1 ){
    	$.messager.alert("提示","阈值的取值范围在[0-1]之间!");
    	return false;
    }
    return true;
}



/**
 * easyui dataGid格式化状态 1:有效 2:无效
 */
function statusFormatter(val, row, index){
    if (val=="1")
    	return "有效";
    if (val=="0")
        return "无效";    
}
/**
 * easyui dataGid格式化返回结果 1:成功 2:失败，3:需审核
 */
function resultFormatter(val, row, index){
    if (val=="1")
    	return "成功";
    if (val=="2")
        return "失败"; 
    if (val=="3")
        return "需审核";
}
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