$(function() {
	/**
	 * 加载识别规则数据
	 */
	$('#manage_engineVer_datagrid').datagrid({  
		url:'../engineVer/list' 
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
function Search(){
	searchData('manage_engineVer_searchform','manage_engineVer_datagrid');
}

function searchData(searchForm, grid,type,url) {
	var queryParams = serializeObject($('#' + searchForm));
	var param1=$("#engineCode").combobox("getText");
	var param2=$("#verCode").textbox('getValue');
	var param3=$("#verNo").textbox('getValue');
	var param4=$("#status").textbox('getValue');

	if(""==param1){
		queryParams= {engineCode:"", verCode:param2, verNo:param3,status:param4}
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