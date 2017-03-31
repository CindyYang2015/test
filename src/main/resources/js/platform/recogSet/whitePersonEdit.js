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
                    	var actionval = $("#action").val();
                    	var engVerVal = $("#manage_whitePerson_editform #engineverCode").val();
                    	if(actionval=='edit' && engVerVal!=''){//表示修改
                    		$("#manage_whitePerson_editform #engineverId_whitePerson_add").combobox('select', engVerVal);
                    	}else{
                        	var data = $('#engineverId_whitePerson_add').combobox('getData');
                            if (data.length > 0) {
                                $("#engineverId_whitePerson_add").combobox('select', data[0].verCode);
                            }

                    	}                    	
                    	
                    }
                });                
                
        },onLoadSuccess: function () { //数据加载完毕事件
        	var actionval = $("#action").val();
        	var engVal = $("#manage_whitePerson_editform #engineCode").val();
        	if(actionval=='edit' && engVal!=''){//表示修改
        		$("#manage_whitePerson_editform #engineId_whitePerson_add").combobox('select', engVal);
        	}
        }
	});
	
	
	
	var id=$("#id").val();
	$.ajax({
		url : "../whitePerson/selectChannelDicValuesByDicType?dicType=6&id="+id,
		dataType:"json",
		type:"post",
		data:{},
		success : function(data) {
			var snciRes = data;
			console.log(data);
			if(!snciRes || snciRes.length < 1){
				$('#inter_tr100').append('<td align="left">无接口类型!<td>')
				return;
			}
			for (var i = 1; i < snciRes.length; i++) { 
				
				if(snciRes[i].checkbox){
					$('#inter_tr111')
					.append(
							'<input type="checkbox" name="tradingCodes" checked="checked"  value="'+snciRes[i].id+'"   />'
							+ snciRes[i].text
							+ '');
				}else{
					$('#inter_tr111')
					.append(
							'<input type="checkbox" name="tradingCodes" value="'+snciRes[i].id+'"   />'
							+ snciRes[i].text
							+ '');
				}
				
			}
		},
		error:function() {
			$.messager.alert('错误',"服务器发生错误,请稍后重试！","error");
		}
	});
	
	$.ajax({
		url : "../whitePerson/selectChannelDicValuesByDicType?dicType=3&id="+id,
		dataType:"json",
		type:"post",
		data:{},
		success : function(data) {
			var snciRes = data;
			console.log(data);
			if(!snciRes || snciRes.length < 1){
				$('#inter_tr100').append('<td align="left">无接口类型!<td>')
				return;
			}
			
			for (var i = 1; i < snciRes.length; i++) { 
				if(snciRes[i].checkbox){
					$('#inter_tr122')
					.append(
							'<input type="checkbox" name="channels" checked="checked" value="'+snciRes[i].id+'"   />'
							+ snciRes[i].text
							+ '');
				}else{
					$('#inter_tr122')
					.append(
							'<input type="checkbox" name="channels" value="'+snciRes[i].id+'"   />'
							+ snciRes[i].text
							+ '');
				}
				
			}
			
		},
		error:function() {
			$.messager.alert('错误',"服务器发生错误,请稍后重试！","error");
		}
	});
});
