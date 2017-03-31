
/**
 * 设置操作状态的名称
 * @param status
 */
function setInterfaceVerStatusName(value, row, index) {
	if(value == 1) {
		return "启用";
	} else {
		return "停用"
	}
}
/**
 * 添加接口信息
 */
function addInterfaceVer(){
	$.acooly.framework.customDialog({
		url:'../interfaceVer/create.html',
		entity:'interfaceVer',
		wwidth:450,
		height:300,
		title:'新增', 
		reload:true,
		onSuccess:function(result) {	
			return true;
		}
	},{
		buttonName:'确定'
	});
}

/**
 * 编辑接口信息
 */
function editInterfaceVer() {
	$.acooly.framework.customDialog({
		url:'../interfaceVer/edit.html',
		entity:'interfaceVer',
		width:450,
		height:300
		},{
			buttonName:'确定',
			isCheckselect:true,
			param:'id',
		});
}


/*初始化下拉框*/
/*$("#interfaceC").combobox({
	valueField:"interfaceCode",
	textField:"interfaceName",
	url:"../interface/selectInterface.html",
	loadFilter:function(data){
		debugger;
		data.push({id:'',text:'全部'});
	   	return data;}
	});
*/


//查询
function submitSearch(){
	searchData('manage_interfaceVer_searchform','manage_interfaceVer_datagrid');
}

function searchData(searchForm, grid,type,url) {
	var queryParams = serializeObject($('#' + searchForm));
	var param1=$("#interfaceC").combobox("getText");
	var param2=$("#verNo").textbox('getValue');
	var param3=$("#verCode").textbox('getValue');
	
	if(""==param1){
		queryParams={interfaceC:param1, verNo:param2,verCode:param3}
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
