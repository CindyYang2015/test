$(function() {
	//初始化算法引擎
	$('#engineId_personRecog_add').combobox({
		url:"../step/validEngineAllList",
        editable:false,
        method:"post",
        valueField: 'engineCode',
        textField: 'engineName',        
        onSelect:function(record){
                var engineCode = record.engineCode;//得到当前ID
            	$('#engineverId_personRecog_add').combobox({
            		url:"../step/selectEngineVer?engineCode="+engineCode,
                    editable:false,
                    method:"post",
                    valueField: 'verCode',
                    textField: 'verNo',
                    onLoadSuccess: function () { //数据加载完毕事件
                    	//debugger;
                    	var data = $('#engineverId_personRecog_add').combobox('getData');
                        if (data.length > 0) {
                            $("#engineverId_personRecog_add").combobox('select', data[0].verCode);
                        }
                    }
                });                
                
        }
	});
});

function fmtPersonRecogFlowDataById(value, row, index) {
	var action = $("#manage_person_recog_action").html();
	action = action.replace(/{personId}/g,row.personId);
	action = action.replace(/{channel}/g,row.channel);
	action = action.replace(/{channelName}/g,row.channelName);
	action = action.replace(/{tradingCodeName}/g,row.tradingCodeName);
	action = action.replace(/{tradingCode}/g,row.tradingCode);
	
	return action; 	
}

/**
 * 加入白名单方法
 * @param id
 */
function onPersonRecogFlowWhiteById(personId,channel,channelName,tradingCode,tradingCodeName,engineCode,engineVerCode){
	$('#channl002').text(channelName);
	$('#trading002').text(tradingCodeName);
	$('#person_recog_add_white_personId').val(personId);
	$('#person_recog_add_white_channel').val(channel);
	$('#person_recog_add_white_tradingCode').val(tradingCode);
	$('#engineId_personRecog_add').val(engineCode);
	$('#engineverId_personRecog_add').val(engineVerCode);
	$('#dlg_person_recog_add_white').dialog('open');
}
function add_black_btn_click_close(){
	$('#dlg_person_recog_add_black').dialog('close');	
}
function add_white_btn_click_close(){
	$('#dlg_person_recog_add_white').dialog('close');
}
/**
 * 加入黑名单方法
 * @param id
 */
function onPersonRecogFlowBlackById(personId,channel,channelName,tradingCode,tradingCodeName){
	$('#channl001').text(channelName);
	$('#trading001').text(tradingCodeName);
	$('#person_recog_add_black_personId').val(personId);
	$('#person_recog_add_black_channel').val(channel);
	$('#person_recog_add_black_tradingCode').val(tradingCode);
	$('#dlg_person_recog_add_black').dialog('open');	
}

/**
 * 加入黑名单
 */
function add_black_btn_click(){
	//客户ID
	var personId = $('#person_recog_add_black_personId').val();
	//渠道
	var channel = $('#person_recog_add_black_channel').val();
	//交易代码
	var tradingCode = $('#person_recog_add_black_tradingCode').val();
	
	var blackType = $("input[name='person_recog_blackType']").val() ;
	if(blackType == null || blackType.trim() == ""){
		$.messager.alert('提示',"请选择黑名单类型！","info");
		return;
	}
	if(personId == null || personId.trim() == ""){
		$.messager.alert('提示',"参数异常！","info");
		return;
	}
	$.ajax({
		url : "../personRecogFlow/isBlackExist",
		dataType:"json",
		type:"post",
		data:{"personId":personId,"blackType":blackType,"channel":channel,"tradingCode":tradingCode},
		success : function(result) {
			if(result.success){
				$.ajax({
					url : "../personRecogFlow/isWhiteExist",
					dataType:"json",
					type:"post",
					data:{"personId":personId,"blackType":blackType,"channel":channel,"tradingCode":tradingCode},
					success : function(result) {
						if(result.success){
							$.ajax({
								url : "../personRecogFlow/addBlack",
								dataType:"json",
								type:"post",
								data:{"personId":personId,"blackType":blackType,"channel":channel,"tradingCode":tradingCode},
								success : function(result) {
									if(result.success){
										$.messager.alert('提示',result.message,"info");
										$('#dlg_person_recog_add_black').dialog('close');
									}
								}
							})
						}else{
							$.messager.confirm('提示',result.message+',是否将该用户从白名单中删除后加入黑名单？',function(r){
							if(r==true){
								$.ajax({
								url : "../personRecogFlow/addBlack",
								dataType:"json",
								type:"post",
								data:{"personId":personId,"blackType":blackType,"channel":channel,"tradingCode":tradingCode},				
								success : function(result) {
									if(result.success){
									$.messager.alert('提示',result.message,"info");
									$('#dlg_person_recog_add_black').dialog('close');
									}
								}
								})
							}
							})
						}
					}
				})
			}else{
				$.messager.alert('提示',result.message,"info");
				$('#dlg_person_recog_add_black').dialog('close');
			}
		},
		error:function() {
			$.messager.alert('错误',"服务器发生错误,请稍后重试！","error");
		}
	});
}

/**
 * 加入白名单
 */
function add_white_btn_click(){
	//客户ID
	var personId = $('#person_recog_add_white_personId').val();
	//渠道
	var channel = $('#person_recog_add_white_channel').val();
	//交易代码
	var tradingCode = $('#person_recog_add_white_tradingCode').val();
	
	var whiteType = $("input[name='person_recog_blackType_white']").val() ;
	if(whiteType == null || whiteType.trim() == ""){
		$.messager.alert('提示',"请选择白名单类型！","info");
		return;
	}
	var engineCode = $('#engineId_personRecog_add').combobox('getValue');
	var engineVerCode = $('#engineverId_personRecog_add').combobox('getValue');
	if(engineCode == null || engineCode.trim() == ""){
		$.messager.alert('提示',"请选择算法引擎！","info");
		return;
	}
	if(engineVerCode == null || engineVerCode.trim() == ""){
		$.messager.alert('提示',"请选择算法版本引擎！","info");
		return;
	}
	var score = $('#score_whitePerson').val();
	if(score == null || score.trim() == ""){
		$.messager.alert('提示',"请输入阈值！","info");
		return;
	}
	if(personId == null || personId.trim() == ""){
		$.messager.alert('提示',"参数异常！","info");
		return;
	}
	$.ajax({
		url : "../personRecogFlow/isWhiteExist",
		dataType:"json",
		type:"post",
		data:{"personId":personId,"whiteType":whiteType,"channel":channel,"tradingCode":tradingCode},
		success : function(result) {
			if(result.success){
				$.ajax({
					url : "../personRecogFlow/isBlackExist",
					dataType:"json",
					type:"post",
					data:{"personId":personId,"whiteType":whiteType,"channel":channel,"tradingCode":tradingCode},
					success : function(result) {
						if(result.success){
							$.ajax({
								url : "../personRecogFlow/addWhite",
								dataType:"json",
								type:"post",
								data:{"personId":personId,"whiteType":whiteType,"channel":channel,"score":score,"engineCode":engineCode,"engineVerCode":engineVerCode,"tradingCode":tradingCode},
								success : function(result) {
									if(result.success){
										$.messager.alert('提示',result.message,"info");
										$('#dlg_person_recog_add_white').dialog('close');
									}
								}
							})
						}else{
							$.messager.confirm('提示',result.message+',是否将该用户从黑名单中删除后加入白名单？',function(r){
							if(r==true){
								$.ajax({
								url : "../personRecogFlow/addWhite",
								dataType:"json",
								type:"post",
								data:{"personId":personId,"whiteType":whiteType,"channel":channel,"score":score,"engineCode":engineCode,"engineVerCode":engineVerCode,"tradingCode":tradingCode},				
								success : function(result) {
									if(result.success){
									$.messager.alert('提示',result.message,"info");
									$('#dlg_person_recog_add_white').dialog('close');
									}
								}
								})
							}
							})
						}
					}
				})
			}else{
				$.messager.alert('提示',result.message,"info");
				$('#dlg_person_recog_add_white').dialog('close');
			}
		},
		error:function() {
			$.messager.alert('错误',"服务器发生错误,请稍后重试！","error");
		}
	});
}

/**
 * 详情(这个详情暂时没用)
 * @param id

function onPersonRecogFlowDetailById(id){
	$.ajax({
		url : "../personRecogFlow/selectPersonRecogFlowById",
		dataType:"json",
		type:"post",
		data:{"id":id},
		success : function(result) {
			if(result && result.entity) {
				$('#dlg_person_recog_detail').dialog('open');//显示弹出窗	
				$("#person_recog_detail_tradingCode").text(getRecogFlowEntityText(result.entity,'tradingCode'));
				$("#person_recog_detail_channel").text(getRecogFlowEntityText(result.entity,'channel'));
				$("#person_recog_detail_busCode").text(getRecogFlowEntityText(result.entity,'busCode'));
				$("#person_recog_detail_interVerCode").text(getRecogFlowEntityText(result.entity,'interVerCode'));
				$("#person_recog_detail_engineCode").text(getRecogFlowEntityText(result.entity,'engineCode'));
				$("#person_recog_detail_engineVerCode").text(getRecogFlowEntityText(result.entity,'engineVerCode'));
				$("#person_recog_detail_legalOrgCode").text(getRecogFlowEntityText(result.entity,'legalOrgCode'));
				
				var personId = getRecogFlowEntityText(result.entity,'personId');
				//清空html
				$('#dlg_person_recog_flow_accordion').html('');
				//ajax请求查询客户基本信息
				$.ajax({
					url : "../person/selectPersonByPrimaryKey",
					dataType:"json",
					type:"post",
					data:{"personId":personId},
					success : function(data) {
						if(data.code == "0"){
							//查询特征列表
							$.ajax({
								url : "../personFeature/selectFeatureByPrimaryKey",
								dataType:"json",
								type:"post",
								data:{"personId":personId,"dicType":"5"},
								success : function(data1) {
									//开始读取特征数据信息
									if(data1.code == "0"){
										var person = data.data;
										var feature = data1.data;
										var engine = data1.engine;
										var myobj=eval(engine);
										//绘制详情
										writePersonDetail2(person,feature,myobj);
									}else{
										$.messager.alert('错误',"客户特征信息拉取异常！","error");
									}
								},
								error:function() {
									$.messager.alert('错误',"服务器发生错误,请稍后重试！","error");
								}
							});
						}else{
							
						}
					},
					error:function() {
						$.messager.alert('错误',"服务器发生错误,请稍后重试！","error");
					}
				});
				
			}
		},
		error:function() {
			$.messager.alert('错误',"服务器发生错误,请稍后重试！","error");
		}
	});
}
 */

/**
 *  写客户基本信息
 * @param data
 */
function writePersonDetail2(person,feature,engine){
	var htmls = "<div class='dialog-button' style='position: relative; top: -1px; width: 100%px;text-align:left'>"
		+ "<span>客户基本信息</span>"
		+ "</div>"
		+ ""
		+ "<table class='person_feature_detail_table' style='width: 100%;'>"
		+ "<tr><td style='width: 20%;text-align: right;'>证件类型:</td>"
		+ "<td style='width: 30%;text-align: left;'>"+checkUndefined_person_recog_flow(person.ctftype)+"</td>"
		+ "<td style='width: 20%;text-align: right;'>证件号码:</td>"
		+ "<td style='width: 30%;text-align: left;'>"+checkUndefined_person_recog_flow(person.ctfno)+"</td></tr>"
		+ "<tr><td style='width: 20%;text-align: right;'>证件姓名:</td>"
		+ "<td style='width: 30%;text-align: left;'>"+checkUndefined_person_recog_flow(person.ctfname)+"</td>"
		+ "<td style='width: 20%;text-align: right;'>核心客户号:</td>"
		+ "<td style='width: 30%;text-align: left;'>"+checkUndefined_person_recog_flow(person.customerId)+"</td></tr>"
		+ "<tr><td style='width: 20%;text-align: right;'>机构名称:</td>"
		+ "<td style='width: 30%;text-align: left;'>"+checkUndefined_person_recog_flow(person.orgName)+"</td>"
		+ "<td style='width: 20%;text-align: right;'>客户属性:</td>"
		+ "<td style='width: 30%;text-align: left;'>"+checkUndefined_person_recog_flow(person.property)+"</td></tr>"
		+ "<tr><td style='width: 20%;text-align: right;'>备注:</td>"
		+ "<td style='width: 80%;text-align: left;' colspan='3'>"+checkUndefined_person_recog_flow(person.remark)+"</td></tr>"
		+ "<tr><td style='width: 20%;text-align: right;'>创建人:</td>"
		+ "<td style='width: 30%;text-align: left;'>"+checkUndefined_person_recog_flow(person.creator)+"</td>"
		+ "<td style='width: 20%;text-align: right;'>创建时间:</td>"
		+ "<td style='width: 30%;text-align: left;'>"+getLocalTime_person_recog_flow(person.createTime)+"</td></tr>"
		+ "<tr><td style='width: 20%;text-align: right;'>更新人:</td>"
		+ "<td style='width: 30%;text-align: left;'>"+checkUndefined_person_recog_flow(person.updator)+"</td>"
		+ "<td style='width: 20%;text-align: right;'>更新时间:</td>"
		+ "<td style='width: 30%;text-align: left;'>"+getLocalTime_person_recog_flow(person.updateTime)+"</td></tr></table>";
	var model = "";
	

		for (var i = 1; i < engine.length; i++) {
				model = model + "<div class='dialog-button' style='position: relative; top: -1px; width: 100%px;text-align:left'><span>***"+engine[i].text+"***</span></div>"
				model = model + "<div title='"+engine[i].text+"' style='overflow:auto;padding:10px;'>";//Accordion 折叠面板开始
				for(var j = 0; j < feature.length; j++){
					if(engine[i].text == feature[j].engineType){
						var filePath =  checkUndefined_person_feature(feature[j].filePath);
						//如果是图片的话
						model = model + "<div style='width:170px;height: 200px;float:left;margin:0 5px;'>";
						var download = "../person/downloadFile?path="+filePath+"";
						model = model + "<a href='"+download+"' title='点击图片即可下载'>";
						if(filePath != null){
							model = model + "<img style='width: 160px;height: 160px;' src='../person/loadImg?path="+filePath+"'></img>";
						}else{
							model = model + "<img style='width: 160px;height: 160px;' src='../images/upload.png'></img>";
						}	
						model = model + "</a><br>"+feature[j].engineCode+"<br>"+feature[j].engineVerCode+"</div>";
					}
				}
				model = model + "</div>";//Accordion 折叠面板结束

		}
	$("#dlg_person_recog_detail").css("height","600");
	$('#dlg_person_recog_flow_accordion').html(htmls+model);
}

function getRecogFlowEntityText(entity,field) {
	if(!entity || !field) return "";
	if(!entity[field]) return "";
	return entity[field];
}

/**
 * 检查空判断
 * @param nS
 * @returns
 */
function checkUndefined_person_recog_flow(nS){
	if(nS == null || nS == undefined){
		return '';
	}
	return nS;
}

/**
 * 数字时间转字符串
 * @param nS
 * @returns
 */
function getLocalTime_person_recog_flow(nS) { 
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
 * 导出csv
 */
function personRecogExportCSV() {
	//根据查询条件到处csv
	var auditNumber_channel = $("#auditNumber_channel").combobox('getValue');
	var auditNumber_tradingCode = $("#auditNumber_tradingCode").combobox('getValue');
	var ctftype = $("#ctftype").combobox('getValue');
	var ctfno = $("#ctfno").textbox('getValue');
	var failCount = $("#failCount").val();
	//alert(auditNumber_channel+"+"+auditNumber_tradingCode+"+"+ctftype+"+"+ctfno+"+"+failCount);
	window.location.href = "../personRecogFlow/exportCsv?channel=" + auditNumber_channel
			+ "&tradingCode=" + auditNumber_tradingCode
			+ "&ctftype=" + ctftype
			+ "&ctfno=" + ctfno
			+ "&failCount=" + failCount;
}