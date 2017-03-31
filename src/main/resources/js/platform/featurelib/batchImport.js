$(function() {
	//初始化算法引擎
	$('#engineId_batchImport').combobox({
		url:"../step/validEngineAllList",
        editable:false,
        method:"post",
        valueField: 'engineCode',
        textField: 'engineName',        
        onSelect:function(record){
                var engineCode = record.engineCode;//得到当前ID
                $('#engineType_batchImport').val(record.engineType);
            	$('#engineverId_batchImport').combobox({
            		url:"../step/selectEngineVer?engineCode="+engineCode,
                    editable:false,
                    method:"post",
                    valueField: 'verCode',
                    textField: 'verNo',
                    onLoadSuccess: function () { //数据加载完毕事件
                    	//debugger;
                    	var data = $('#engineverId_batchImport').combobox('getData');
                        if (data.length > 0) {
                            $("#engineverId_batchImport").combobox('select', data[0].verCode);
                        }
                    }
                });                
                
        }
	});
});	

var timeOut = false;
function detection() {
	var value = $('#p').progressbar('getValue');
	if (value < 100) {
		value += Math.floor(Math.random() * 10);
		$('#p').progressbar('setValue', value);
		if(timeOut) {
			$('#p').progressbar('setValue', 0);
		} else {
			setTimeout(arguments.callee, 500);
		}
		
	}
};

function flowstatusFormatter(val, row, index){
    if (val=="0")
    	return "上传成功";
    if (val=="1")
        return "上传失败";    
}
function flowCreateTimeFormatter(val, row, index){
	return getLocalTime_batchImport(val);
}
function modelDownLoad(){
	window.location.href = "../batchImport/downloadmodel";
}

function upLoad() {
	$('#p_progressbar').progressbar('setValue', 0);
	
	var batchImport_engineCode = $('#engineId_batchImport').combobox('getValue');
	if(batchImport_engineCode == null || batchImport_engineCode.trim() == "") {
		$.messager.alert("提示","请选择算法引擎！");
		return;
	}
	var batchImport_engineVerId = $('#engineverId_batchImport').combobox('getValue');
	if(batchImport_engineVerId == null || batchImport_engineVerId.trim() == "") {
		$.messager.alert("提示","算法引擎版本！");
		return;
	}
	
	var batchImport_cType = $('#batchImport_cType').combobox('getValue');
	if(batchImport_cType == null || batchImport_cType.trim() == "") {
		$.messager.alert("提示","请选择证件类型！");
		return;
	}
	
	/*var batchImport_custProperty = $('#batchImport_custProperty').combobox('getValue');
	if(batchImport_custProperty == null || batchImport_custProperty.trim() == "") {
		$.messager.alert("提示","请选择客户属性！");
		return;
	}*/
	
	var zipFile = $("#batchImport_zipFile").filebox("getValue");	
	if(zipFile==""){
		$.messager.alert("提示","请选择上传的.zip文件！");
		return;
	}else if(zipFile.substring(zipFile.length-4,zipFile.length).toString()!='.zip'){
		$.messager.alert("提示","上传文件只支持.zip文件！");
		return;
	} else {
		$("#loadImg").css('display','block');
		var validateStatus = $('#fileUpload_batchImport').form('validate'); 	
		if(validateStatus){

			var options = {
					type : 'post',
					url:'../batchImport/saveBatImport',
					success : function(responseText) {
					    if(responseText==''){
					    	$.messager.alert("提示","导入失败");
					    	//重新初始化进度条
							timeOut = true;
							$("#loadImg").css('display','none');
					    	return;
					    }

						var data=eval('('+responseText+')');

						if(data.code==0){
							console.log(1);
							$("#batchImport_zipFile").filebox('setValue', '');//每次上传完成清除zipFile的值，
							console.log(2);
				        	 $.messager.alert("提示","导入完成！"+data.message);
				        	 $('#p').progressbar('setValue', 100);
				        	 $("#loadImg").css('display','none');
							 $('#manage_batchImport_datagrid').datagrid('reload',{  
								url:'../batchImport/selectImportFlowByPage'
							 });
						}else{						
							$.messager.alert("提示",data.message);
						}
						
						//重新初始化进度条
						timeOut = true;
						$("#loadImg").css('display','none');
						
					},
					error:function(){
						$.messager.alert('错误',"服务器发生错误,请稍后重试！","error");
					}
				};
			$("#fileUpload_batchImport").ajaxSubmit(options);
		 
		}
		
//		$('#fileUpload_batchImport').ajaxSubmit({	
//			dataType:"json",
//			beforeSubmit:function(formData, jqForm, options){			
//				//默认easy-ui-validator验证
//				var validateStatus = $('#fileUpload_batchImport').form('validate'); 	
//				if(validateStatus == true) {
//					return true;
//				} else {
//					return validateStatus;
//				}
//				$('#subumit').attr({"disabled":"disabled"});
//		
//			},
//			success:function(result, statusText){
//				try {
//					if(result && result.success) {
//						 $("#batchImport_zipFile").filebox('setValue', '');//每次上传完成清除zipFile的值，
//			        	 $.messager.alert("提示","导入完成！");
//			        	 $('#p').progressbar('setValue', 100);
//			        	 $("#loadImg").css('display','none');
//						 $('#manage_batchImport_datagrid').datagrid('reload',{  
//							url:'../batchImport/selectImportFlowByPage'
//						 }); 			             
//					} else {
//						//重新初始化进度条
//						timeOut = true;
//						$("#loadImg").css('display','none');
//						$.messager.alert("提示",result.message);
//					}
//					$('#subumit').removeAttr("disabled");
//				} catch (e) {
//					$.messager.alert('提示', e);
//				}											
//			},
//			error:function() {				
//				$.messager.alert('错误',"服务器发生错误,请稍后重试！","error");
//			}
//			
//		});	
		detection();//上传进度条
	}
	
	
}

//重置表单
function batchImportReset() {
	$('#batchImport_cType').combobox('setValue', '');
	$('#batchImport_custProperty').combobox('setValue', '');
	$('#batchImport_zipFile').filebox('setValue', '');
}

/**
 * 数字时间转字符串
 * @param nS
 * @returns
 */
function getLocalTime_batchImport(nS) { 
	//debugger;
	if(nS == null || nS == undefined){
		return '';
	}
	//基于'/'格式的日期字符串，才是被各个浏览器所广泛支持的，‘-’连接的日期字符串，则是只在chrome下可以正常工作。
	var date=new Date(Date.parse(nS.replace(/-/g,"/")));
	/*var date = new Date(nS);*/
	Y = date.getFullYear() + '-';
	M = (date.getMonth()+1 < 10 ? '0'+(date.getMonth()+1) : date.getMonth()+1) + '-';
	D = date.getDate() + ' ';
	h = date.getHours() + ':';
	m = (date.getMinutes() < 10 ? ('0'+date.getMinutes()) : date.getMinutes()) + ':';
	s = date.getSeconds() < 10 ? ('0'+date.getSeconds()) : date.getSeconds(); 
	return Y+M+D+h+m+s;      
}