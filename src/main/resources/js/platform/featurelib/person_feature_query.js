$(function() {
	/**
	 * 加载数据
	 */
	$('#manage_person_feature_datagrid').datagrid({  
		url:'../person/selectPersonFeatureByPage',
		onBeforeLoad:function(params) {
			var ctfno = $("#manage_person_feature_searchform input[id='person_feature_ctfno']").val();
			params.ctfno = ctfno;
			return true;
		}
	}); 	
	
});	
	

function optFormatterPersonFeature(value, row, index) {
	if(row.status == 0) return "";
	var action = $("#manage_person_feature_action").html();
	action = action.replace(/{personId}/g,row.personId);
	action = action.replace(/{partitionId}/g,row.partitionId);
	return action;
}

/**
 * 格式化状态
 * @param value
 * @param row
 * @param index
 */
function optFormatPfStatus(value, row, index) {
	if(value == 1) return "正常";
	else if(value == 0) return "已删除";
	else return "未定义";
}

//新增弹出框
function showPersonFreatureDialog(){
	$('#dlg_person_feature').dialog('open');//显示弹出窗
	$.acooly.framework.customDialog({
		url:'../person/create',
		entity:'person_feature',
		width:600,
		height:400,
		title:'新建', 
		reload:true,
		onSuccess:function(result) {
			var value = $("#manage_person_feature_editform input[id='ctfno_person_feature_edit']").val();
			$("#manage_person_feature_searchform input[id='person_feature_ctfno']").textbox("setValue", value);
			return true;
		}
	},{
		buttonName:'确定'
	});
}

//编辑弹出框
function editPersonFreatureDialog(){
	$.acooly.framework.customDialog({
		url:'../person/edit',
		entity:'person_feature',
		width:600,
		height:400,
		title:'编辑', 
		reload:true
	},{
		buttonName:'确定',isCheckselect:true,param:'personId,partitionId',
		afterSelectRowCallback:function(row) {
			if(row && row.length > 0) {
				if(row[0].status == 0) {
					$.messager.alert('提示', '已删除的客户不能编辑');
					return false;
				} 
			} 
			return true;
		}
	});
}

//删除特征弹出框
function openDeleteFeatureDlg(partitionId,personId){
	if(partitionId == null || partitionId.trim() == ""){
		$.messager.alert('提示',"分区ID不能为空！","info");
		return;
	}
	if(personId == null || personId.trim() == ""){
		$.messager.alert('提示',"客户ID不能为空！","info");
		return;
	}
	$('#engineId_person_feature_delete').combobox({
		url:"../personFeature/validEngineAllList?partitionId="+partitionId+"&personId="+personId,
		editable:false,
		method:"post",
		valueField: 'engineCode',
		textField: 'engineName',        
		onSelect:function(record){
			var engineCode = record.engineCode;//得到当前ID
			$('#engineverId_person_feature_delete').combobox({
				url:"../personFeature/selectEngineVer?partitionId="+partitionId+"&engineCode="+engineCode+"&personId="+personId,
				editable:false,
				method:"post",
				valueField: 'verCode',
				textField: 'verNo',
				onLoadSuccess: function () { //数据加载完毕事件
					//debugger;
					var data = $('#engineverId_person_feature_delete').combobox('getData');
					if (data.length > 0) {
						$("#engineverId_person_feature_delete").combobox('select', data[0].verCode);
					}
				}
			});                
			
		}
	});
	
	$('#person_feature_partitionId').val(partitionId);//赋值
	$('#person_feature_personId').val(personId);//赋值
	
	$('#dlg_person_feature_version').css("display","");
	//显示对话框
	$('#dlg_person_feature_version').dialog({
	    title: "确认删除算法引擎版本",
	    width: 330,
	    height: 200,
	    closed: false,
	    cache: false,
	    modal: true
	});	
}

function delete_engine_feature_click_close(){
	$('#engineId_person_feature_delete').combobox('setValue',"");
	$('#engineverId_person_feature_delete').combobox('setValue',"");
	$('#dlg_person_feature_version').dialog('close');//显示弹出窗
}

/**
 * 删除弹出框删除按钮
 */
function delete_engine_feature_click(){
	var partitionId = $('#person_feature_partitionId').val();
	if(partitionId == null || partitionId.trim() == ""){
		$.messager.alert('提示',"分区ID不能为空！","info");
		return;
	}
	var personId = $('#person_feature_personId').val();
	if(personId == null || personId.trim() == ""){
		$.messager.alert('提示',"客户ID不能为空！","info");
		return;
	}
	var engineCode = $('#engineId_person_feature_delete').combobox('getValue');
	var engineVerCode = $('#engineverId_person_feature_delete').combobox('getValue');
	if(engineCode == null || engineCode.trim() == ""){
		$.messager.alert('提示',"请选择算法引擎！","info");
		return;
	}
	if(engineVerCode == null || engineVerCode.trim() == ""){
		$.messager.alert('提示',"请选择算法版本引擎！","info");
		return;
	}
	$.ajax({
		url : "../person/deletePersonFeature",
		dataType:"json",
		type:"post",
		data:{"partitionId":partitionId,"personId":personId,"engineCode":engineCode,"engineVerCode":engineVerCode},
		success : function(data) {
			if(data.success){
				$('#dlg_person_feature_version').dialog('close');//显示弹出窗
				$.messager.alert('提示',"删除成功！","info");				
			}else {
				$.messager.alert('提示',data.message,"error");
			}
		},
		error:function() {
			$.messager.alert('错误',"服务器发生错误,请稍后重试！","error");
		}
	});
	$('#engineId_person_feature_delete').combobox('setValue',"");
	$('#engineverId_person_feature_delete').combobox('setValue',"");
}

//详情弹出框
function selectPersonFeatureByPrimaryKey(partitionId,personId){
	if(partitionId == null || partitionId.trim() == ""){
		$.messager.alert('提示',"分区ID不能为空！","info");
		return;
	}
	if(personId == null || personId.trim() == ""){
		$.messager.alert('提示',"客户ID不能为空！","info");
		return;
	}
	$('#dlg_person_feature_detail').css("display","");
	//显示对话框
	$('#dlg_person_feature_detail').dialog({
	    title: "客户详情",
	    width: 800,
	    height: 400,
	    closed: false,
	    cache: false,
	    modal: true
	});	
	
	//根据personId查询特征详情以及客户详情
	//清空html
	$('#dlg_person_feature_detail_accordion').html('');
	//ajax请求查询客户基本信息
	$.ajax({
		url : "../person/selectPersonByPrimaryKey",
		dataType:"json",
		type:"post",
		data:{"personId":personId,"partitionId":partitionId},
		success : function(data) {
			if(data.success){
				//查询特征列表
				$.ajax({
					url : "../personFeature/selectFeatureByPrimaryKey",
					dataType:"json",
					type:"post",
					data:{"partitionId":partitionId,"personId":personId,"dicType":"5"},
					success : function(data1) {
						//开始读取特征数据信息
						if(data1.success){
							var person = data.entity;
							var feature = data1.rows;
							var engine = data1.data.engine;
							var myobj=eval(engine);
							//绘制详情
							writePersonDetail(person,feature,myobj);
						}else{
							$.messager.alert('错误',"客户特征信息拉取异常！","error");
						}
					},
					error:function() {
						$.messager.alert('错误',"服务器发生错误,请稍后重试！","error");
					}
				});
			}else{
				$.messager.alert('错误',"客户信息拉取异常！","error");
			}
		},
		error:function() {
			$.messager.alert('错误',"服务器发生错误,请稍后重试！","error");
		}
	});
}

//根据主键逻辑删除客户信息
function deletePersonByPrimaryKey(partitionId,personId){
	if(partitionId == null || partitionId.trim() == ""){
		$.messager.alert('提示',"分区ID不能为空！","info");
		return;
	}
	if(personId == null || personId.trim() == ""){
		$.messager.alert('提示',"客户ID不能为空！","info");
		return;
	}
	$.messager.confirm('提示框', '你确定要删除吗?',function(rs){	
		if(rs){			
			$.ajax({
				url : "../person/removePersonByPrimaryKey",
				dataType:"json",
				type:"post",
				data:{"personId":personId,"partitionId":partitionId},
				success : function(data) {
					if(data.success){			
						$.messager.alert('提示',"删除成功！","info");
						$('#manage_person_feature_datagrid').datagrid('reload'); 
					}else{
						$.messager.alert('提示',data.message,"error");
					}
				},
				error:function() {
					$.messager.alert('错误',"服务器发生错误,请稍后重试！","error");
				}
			});
		}
	});
}

/**
 *  写客户基本信息
 * @param data
 */
function writePersonDetail(person,feature,engine){
	var htmls = "<div class='dialog-button' style='position: relative; top: -1px; width: 100%px;text-align:left'>"
		+ "<span>客户基本信息</span>"
		+ "</div>"
		+ ""
		+ "<table class='person_feature_detail_table' style='width: 100%;'>"
		+ "<tr><td style='width: 20%;text-align: right;'>证件类型:&nbsp;&nbsp;&nbsp;</td>"
		+ "<td style='width: 30%;text-align: left;'>"+checkUndefined_person_feature(person.ctftype)+"</td>"
		+ "<td style='width: 20%;text-align: right;'>证件号码:</td>"
		+ "<td style='width: 30%;text-align: left;'>"+checkUndefined_person_feature(person.ctfno)+"</td></tr>"
		+ "<tr><td style='width: 20%;text-align: right;'>证件姓名:&nbsp;&nbsp;&nbsp;</td>"
		+ "<td style='width: 30%;text-align: left;'>"+checkUndefined_person_feature(person.ctfname)+"</td>"
		+ "<td style='width: 20%;text-align: right;'>客户属性:</td>"
		+ "<td style='width: 30%;text-align: left;'>"+checkUndefined_person_feature(person.customerId)+"</td></tr>"
		+ "<tr><td style='width: 20%;text-align: right;'>机构名称:&nbsp;&nbsp;&nbsp;</td>"
		+ "<td style='width: 30%;text-align: left;'>"+checkUndefined_person_feature(person.orgName)+"</td>"
		+ "<td style='width: 20%;text-align: right;'>备&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;注:</td>"
		+ "<td style='width: 30%;text-align: left;'>"+checkUndefined_person_feature(person.property)+"</td></tr>"
		+ "<tr><td style='width: 20%;text-align:right;'>核心客户号:</td>"
		+ "<td style='width: 80%;text-align: left;' colspan='3'>"+checkUndefined_person_feature(person.remark)+"</td></tr></table>";
	var model = "";
	

		for (var i = 1; i < engine.length; i++) {
				model = model + "<div class='dialog-button' style='position: relative; top: -1px; width: 100%px;text-align:left'><span>***"+engine[i].text+"***</span></div>"
				model = model + "<div title='"+engine[i].text+"' style='overflow:auto;padding:10px;'>";//Accordion 折叠面板开始
				for(var j = 0; j < feature.length; j++){
					if(engine[i].text == feature[j].engineType){
						var filePath =  checkUndefined_person_feature(feature[j].filePath);
						//如果是图片的话
						model = model + "<div style='width:170px;height: 200px;float:left;margin:0 5px;'>";
						var download = "../recog/getFile?fileUrl="+filePath;
						model = model + "<a href='"+download+"' title='点击图片即可下载'>";
						if(filePath != null){
							model = model + "<img style='width: 160px;height: 160px;' src='../recog/getFile?fileUrl="+filePath+"'></img>";
						}else{
							model = model + "<img style='width: 160px;height: 160px;' src='../images/upload.png'></img>";
						}	
						model = model + "</a><br>"+"<span>引擎名称："+feature[j].engineCode+"</span><br><span>引擎版本："+feature[j].engineVerCode+"</span></div>";
					}
				}
				model = model + "</div>";//Accordion 折叠面板结束

		}

	$('#dlg_person_feature_detail_accordion').html(htmls+model);
}

/*$(document).ready(function(){
	$("#person_feature_file_add").uploadPreviewAll({ Img: "previewImg_person_add", Width: 100, Height: 100 });
});

function showPersonFreatureLoadImg(){
	$("#person_feature_file_add").click();	
}*/

/**
 * 数字时间转字符串
 * @param nS
 * @returns
 */
function getLocalTime_perspn_feature(nS) { 
	if(nS == null || nS == undefined){
		return '';
	}
	var date = new Date(nS);
	Y = date.getFullYear() + '-';
	M = (date.getMonth()+1 < 10 ? '0'+(date.getMonth()+1) : date.getMonth()+1) + '-';
	D = date.getDate() + ' ';
	h = date.getHours() + ':';
	m = (date.getMinutes() < 10 ? ('0'+date.getMinutes()) : date.getMinutes()) + ':';
	s = date.getSeconds() < 10 ? ('0'+date.getSeconds()) : date.getSeconds(); 
	return Y+M+D+h+m+s;     
}

/**
 * 检查空判断
 * @param nS
 * @returns
 */
function checkUndefined_person_feature(nS){
	if(nS == null || nS == undefined){
		return '';
	}
	return nS;
}