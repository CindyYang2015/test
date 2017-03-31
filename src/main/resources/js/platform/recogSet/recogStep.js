$(function() {
	/**
	 * 加载识别规则数据
	 */
	$('#manage_recogStep_datagrid').datagrid({  
		url:'../recogStep/list' 
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



//查询
function submitSearch(){
	
	searchData('manage_recogStep_searchform','manage_recogStep_datagrid');
}

function searchData(searchForm, grid,type,url) {
	var queryParams = serializeObject($('#' + searchForm));
	var param1=$("#recogstepName").textbox('getValue');
	var param2=$("#channel").combobox("getText");
	var param3=$("#channel").textbox('getValue');
	var param4=$("#tradingCode").combobox("getText");
	var param5=$("#tradingCode").textbox('getValue');
	
	if(""==param2&&""!=param4){
		queryParams={recogstepName:param1, channel:"",tradingCode:param5}
	}else if(""!=param2&&""==param4){
		queryParams={recogstepName:param1, channel:param3,tradingCode:""}
	}else if(""==param2&&""==param4){
		queryParams={recogstepName:param1, channel:"",tradingCode:""}
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