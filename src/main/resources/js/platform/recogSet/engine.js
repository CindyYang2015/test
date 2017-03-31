$(function() {
	/**
	 * 加载识别规则数据
	 */
	$('#manage_engine_datagrid').datagrid({  
		url:'../engine/list' 
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
	searchData('manage_engine_searchform','manage_engine_datagrid');
}

function searchData(searchForm, grid,type,url) {
	var queryParams = serializeObject($('#' + searchForm));
	var param1=$("#engineType").combobox("getText");
	var param2=$("#engineCode").textbox('getValue');
	var param3=$("#engineName").textbox('getValue');
	var param4=$("#companyName").textbox('getValue');

	if(""==param1){
		queryParams= {engineType:"", engineCode:param2, engineName:param3,companyName:param4}
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