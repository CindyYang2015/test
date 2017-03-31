$(function() {
	//初始化算法引擎
	$('#engineId_whitePerson_add').combobox({
		url:"../step/validEngineAllList",
        editable:false,
        method:"post",
        valueField: 'engineCode',
        textField: 'engineName',        
        onSelect:function(record){
                var engineCode = record.engineCode;//得到当前ID
            	$('#engineverId_whitePerson_add').combobox({
            		url:"../step/selectEngineVer?engineCode="+engineCode,
                    editable:false,
                    method:"post",
                    valueField: 'verCode',
                    textField: 'verNo',
                    onLoadSuccess: function () { //数据加载完毕事件
                    	//debugger;
                    	var data = $('#engineverId_whitePerson_add').combobox('getData');
                        if (data.length > 0) {
                            $("#engineverId_whitePerson_add").combobox('select', data[0].verCode);
                        }
                    }
                });                
                
        }
	});
});

/**
 * 设置操作状态的名称
 * @param status
 */
function setOperStatusName(value, row, index) {
	if(value == 1) {
		return "通过";
	} else if(value == 2){
		return "待审核"
	}else if(value == 0){
		return "不通过"
	}else{
		return "异常状态"
	}
}


//关闭窗口
function closeWhitePersonWindow(){
	$("#whitePerson_crate").window("close");
}
//根据主键逻辑删除白名单信息
function deleteWhitePersonByPrimaryKey(){
	//待审核的客户不能操作
	var rows = $('#manage_whitePerson_datagrid').datagrid('getChecked');
	if(rows && rows.length > 0) {
		if(rows[0].status && rows[0].status == 2) {
			$.messager.alert('提示', '待审核的白名单客户不能操作');
			return;
		} 
	}
	//如果用户设置了相关的资料，不能删除
	$.acooly.framework.removes('../whitePerson/removeWhitePersonByPrimaryKey','whiteId','manage_whitePerson_datagrid');
}

/**
 * 创建白名单
 */
function createWhitePerson() {
	var title="创建白名单";
	var url = '../whitePerson/create';
	$.acooly.framework.customDialog({
		title:title,
		reload:true,
		url:url,
		entity:'whitePerson',
		width:600,
		height:320
		},{
			buttonName:'确定'			
		});
}

	/**
	* 修改白名单信息
	*/
	function editWhitePerson() {
		var title="修改白名单";
		var url = '../whitePerson/edit';
		$.acooly.framework.customDialog({title:title,reload:true,url:url,entity:'whitePerson',width:600,height:320},
				{buttonName:'确定',isCheckselect:true,param:'id',
					afterSelectRowCallback:function(row) {
						if(row && row.length > 0) {
							if(row[0].status && row[0].status == 2) {
								$.messager.alert('提示', '待审核的白名单客户不能操作');
								return false;
							} 
						} 
						return true;
				}});
	}
	