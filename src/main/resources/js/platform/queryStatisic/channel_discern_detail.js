
/**
 * 日期格式化
 * @param date
 * @returns {String}
 */
function formatterDate(date) {
	var day = date.getDate() > 9 ? date.getDate() : "0" + date.getDate();
	var month = (date.getMonth() + 1) > 9 ? (date.getMonth() + 1) : "0" + (date.getMonth() + 1);
	return date.getFullYear() + '-' + month + '-' + day;
}

/**
 * 导出csv
 */
function personCheckExportCSV() {
	var creationDateLTE = $("#creationDateLTE").datebox('getValue');
	var creationDateGTE = $("#creationDateGTE").datebox('getValue');
//	var busCode = $("#busCode").val();
	var busCode = $('#busCode').combobox('getValue')
	var ctfno = $("#ctfno").val();
	window.location.href = "../channelFlow/exportCsv?beginTime=" + creationDateLTE
			+ "&endTime=" + creationDateGTE+"&busCode=" + busCode;
}

/**
 * 格式化结束日期
 * @param value
 * @param row
 * @param index
 * @returns {String}
 */
function getChannelTime(value, row, index){
	return getLocalTime_channel(value);
}

/**
 * 数字时间转字符串
 * @param nS
 * @returns
 */
function getLocalTime_channel(nS) { 
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
 * 添加请求报文链接
 */
function addChannelFlowReqMsgLink(value, row, index) {
	if(!row.flowId) return;
	var astr = "<a href=\"javascript:showChannelFlowMsg('{flowId}',1)\">查看</a>";
	astr = astr.replace(/{flowId}/g,row.flowId);
	return astr;
}

/**
 * 添加响应报文链接
 */
function addChannelFlowResMsgLink(value, row, index) {
	if(!row.flowId) return;
	var astr = "<a href=\"javascript:showChannelFlowMsg('{flowId}',2)\">查看</a>";
	astr = astr.replace(/{flowId}/g,row.flowId);
	return astr;
}

/**
 * 添加渠道流水明细链接
 */
function showChannelFlowDetails(value, row, index) {
	if(!row.flowId) return "";
	var astr = "<a href=\"javascript:showChannelFlowDialog('{flowId}')\">详情</a>";
	astr = astr.replace(/{flowId}/g,row.flowId);
	return astr;
}

/**
 * 显示渠道流水详情对话框
 * @param flowId 渠道流水ID
 */
function showChannelFlowDialog(flowId) {
	/*var title="详情";
	//设置个弹出窗口标题
	$("#channel_discern_detail_dialog").panel({title:title,width:600,height:400});	
	$("#channel_discern_detail_dialog").window("open");*/ 
	$("#flowImgTrId").html("");
	$.ajax({
		url : "../channelFlow/selectChannelFlowById",
		dataType:"json",
		type:"post",
		data:{"flowId":flowId},
		success : function(result) {
			if(result && result.entity) {
				var title = "详情";	
				var width = 800;
				var height = 400;
				//流水号 机构名称 交易名称 渠道名称 接口名称  算法引擎名称 算法引擎版本号 处理结果 证件类型 证件号码 证件姓名 核心客户号 两个生物文件识别分数 生物文件1与特征库比对分数 
				//生物文件2与特征库比对分数 注册认证的客户生物文件 上传的文件 渠道交易流水号 设备号 柜员号 银行卡号 创建时间
				var busCode = getFlowEntityText(result.entity,'busCode');
				//$("#channel_discern_detail_flowId").text(getFlowEntityText(result.entity,'flowId'));
				$("#channel_discern_detail_legalOrgCode").text(getFlowEntityText(result.entity,'legalOrgCode'));
				$("#channel_discern_detail_tradingCode").text(getFlowEntityText(result.entity,'tradingCode'));
				$("#channel_discern_detail_channel").text(getFlowEntityText(result.entity,'channel'));
				$("#channel_discern_detail_busCode").text(getFlowEntityText(result.entity,'interfaceName'));
				$("#channel_discern_detail_interVerCode").text(getFlowEntityText(result.entity,'interVerCode'));
				$("#channel_discern_detail_engineCode").text(getFlowEntityText(result.entity,'engineCode'));
				$("#channel_discern_detail_engineVerCode").text(getFlowEntityText(result.entity,'engineVerCode'));
				if(getFlowEntityText(result.entity,'result') == 1){
					$("#channel_discern_detail_result").text("成功");
				}else if(getFlowEntityText(result.entity,'result') == 0){
					$("#channel_discern_detail_result").text("失败");
				}
				$("#channel_discern_detail_ctftype").text(getFlowEntityText(result.entity,'ctftype'));
				$("#channel_discern_detail_ctfno").text(getFlowEntityText(result.entity,'ctfno'));
				$("#channel_discern_detail_ctfname").text(getFlowEntityText(result.entity,'ctfname'));
				$("#channel_discern_detail_customerId").text(getFlowEntityText(result.entity,'customerId'));
				$("#channel_discern_detail_tradingDate").text(getFlowEntityText(result.entity,'tradingDate'));
				$("#channel_discern_detail_tradingTime").text(getFlowEntityText(result.entity,'tradingTime'));
				$("#channel_discern_detail_tradingFlowNo").text(getFlowEntityText(result.entity,'tradingFlowNo'));
				$("#channel_discern_detail_equipmentNo").text(getFlowEntityText(result.entity,'equipmentNo'));
				$("#channel_discern_detail_tellerNo").text(getFlowEntityText(result.entity,'tellerNo'));
				$("#channel_discern_detail_bankcardNo").text(getFlowEntityText(result.entity,'bankcardNo'));
				
				$('#mission001').hide();
				$('#mission002').hide();
				$('#mission003').hide();
				$('#mission004').hide();
				$('#mission005').hide();
				$('#mission006').hide();
				$('#mission007').hide();
				$('#mission008').hide();
				
				if("reg" == busCode){//如果是注册，显示注册人脸，通过featreId查询特征表找path
						$('#mission001').show();
						$('#regImg001').attr("src","../recog/getFile?fileUrl="+getFlowEntityText(result.entity,'regFilePath'));
						height = 450;					
				}else if("checkPerson" == busCode){//checkPerson显示注册人脸以及比对人脸
						$("#score0021").text(getFlowEntityText(result.entity,'fpOneCmpScore'));
						$('#mission002').show();
						$('#regImg002').attr("src","../recog/getFile?fileUrl="+getFlowEntityText(result.entity,'regFilePath'));
						$('#cmpImg002').attr("src","../recog/getFile?fileUrl="+getFlowEntityText(result.entity,'fileOnePath'));	
						height = 460;
				}else if("compare" == busCode){//compare显示两张人脸						
						$("#score0031").text(getFlowEntityText(result.entity,'cmpScore'));
						$('#mission003').show();
						$('#cmpImg0031').attr("src","../recog/getFile?fileUrl="+getFlowEntityText(result.entity,'fileOnePath'));		
						$('#cmpImg0032').attr("src","../recog/getFile?fileUrl="+getFlowEntityText(result.entity,'fileTwoPath'));
						height = 460;
				}else if("checkPersonEx" == busCode){//显示注册人脸、两个比对人脸						
						$("#score0041").text(getFlowEntityText(result.entity,'fpOneCmpScore'));						
						$("#score0051").text(getFlowEntityText(result.entity,'fpTwoCmpScore'));
						$('#mission004').show();
						$('#regImg004').attr("src","../recog/getFile?fileUrl="+getFlowEntityText(result.entity,'regFilePath'));
						$('#regImg005').attr("src","../recog/getFile?fileUrl="+getFlowEntityText(result.entity,'regFilePath'));
						$('#cmpImg004').attr("src","../recog/getFile?fileUrl="+getFlowEntityText(result.entity,'fileOnePath'));		
						$('#cmpImg005').attr("src","../recog/getFile?fileUrl="+getFlowEntityText(result.entity,'fileTwoPath'));	
						height = 530;
				} else if("ocr" == busCode) {
						$('#mission005').show();
						$('#idcardImg0051').attr("src","../recog/getFile?fileUrl="+getFlowEntityText(result.entity,'fileOnePath'));
						$('#idcardImg0052').attr("src","../recog/getFile?fileUrl="+getFlowEntityText(result.entity,'fileTwoPath'));	
						height = 450;
				} else if("searchByImg" == busCode) {
						$('#mission006').show();
						$('#searchImg0061').attr("src","../recog/getFile?fileUrl="+getFlowEntityText(result.entity,'fileOnePath'));
						height = 450;
			    } else if("checkLiveness" == busCode) {
					$('#mission008').show();
					$('#livenessImg0081').attr("src","../recog/getFile?fileUrl="+getFlowEntityText(result.entity,'fileOnePath'));
					height = 450;
			    } else if("ocrBankCard" == busCode) {
			    	$('#mission007').show();
					$('#bankcardImg0071').attr("src","../recog/getFile?fileUrl="+getFlowEntityText(result.entity,'fileOnePath'));
					height = 450;
			    }		

				$('#channel_discern_detail_dialog').css("display","");
				//显示对话框
				$('#channel_discern_detail_dialog').dialog({
				    title: title,
				    width: width,
				    height: height,
				    closed: false,
				    cache: false,
				    modal: true
				});				
				
			}
		},
		error:function() {
			$.messager.alert('错误',"服务器发生错误,请稍后重试！","error");
		}
	});
	
}

/**
 * 显示图片对话框
 */
function showFLowImgDialog(imgpath) {
	var imgUrl = "../channelService/getImgBypath?imgpath="+imgpath;
	var img = "<img style=\"height:100%;\" src='{imageUrl}' />";
	img = img.replace(/{imageUrl}/g,imgUrl);
	$("#imgId").html(img);
	
	$("#imgId").css("display","");
	$("#imgId").css("text-align","center");
	//显示对话框
	$('#imgId').dialog({
	    title: "图片",
	    width: 600,
	    height: 400,
	    closed: false,
	    cache: false,
	    modal: true
	});	
}

/**
 * 显示接口流水信息
 * @param type 1 请求报文 2 响应报文
 */
function showChannelFlowMsg(flowId,type) {
	$.ajax({
		url : "../channelFlow/selectChannelBLOBFlowById",
		dataType:"json",
		type:"post",
		data:{"flowId":flowId},
		success : function(result) {
			if(result && result.entity) {
				var title = "";
				if(type == 1) {
					$("#msgId").html("<xmp>"+result.entity.requestMsg+"</xmp>");	
					title = "流水请求报文详情";
				} else if(type == 2) {
					$("#msgId").html("<xmp>"+result.entity.reponseMsg+"</xmp>");
					title = "流水响应报文详情";
				}
				$('#msgId').css("display","");
				//显示对话框
				$('#msgId').dialog({
				    title: title,
				    width: 600,
				    height: 400,
				    closed: false,
				    cache: false,
				    modal: true
				});
				
				
			}
		},
		error:function() {
			$.messager.alert('错误',"服务器发生错误,请稍后重试！","error");
		}
	});
}

/**
 * 显示接口流水图片
 */
function showChannelFlowImg(flowId) {
	
	var img = "<img style=\"height:100%;\" src='{imageUrl}' />";
	img = img.replace(/{imageUrl}/g,"../channelFlow/getImgByFlowId?flowId="+flowId);
	$("#imgId").html(img);
	
	$("#imgId").css("display","");
	$("#imgId").css("text-align","center");
	//显示对话框
	$('#imgId').dialog({
	    title: "接口流水图片",
	    width: 600,
	    height: 400,
	    closed: false,
	    cache: false,
	    modal: true
	});	
}
 
/**
 * 获取结果类型
 * @param value 1 成功 2 失败
 * @param row
 * @param index
 */
function getResultTypeName(value, row, index) {
	if(value == 1) {
		return "成功";
	} else if(value == 0) {
		return "失败";
	}
}

/**
 * 获取流水信息
 * @param entity
 * @param field
 */
function getFlowEntityText(entity,field) {
	if(!entity || !field) return "";
	if(!entity[field]) return "";
	return entity[field];
}

$(function() {
	$('#creationDateLTE, #creationDateGTE').val(formatterDate(new Date()));
	$("#manage_channelDiscernDetail_datagrid").datagrid({
		url:'../channelFlow/selectChannelFlowByPage',
		onLoadSuccess: function(data){   
		   //  var a= $('#manage_channelDiscernDetail_datagrid').datagrid('appendRow',{ 
		    	 // interfaceName:"成功总数",interVerCode:"共&nbsp"+data.data.success+"&nbsp条",legalOrgCode:"失败总数",channel:"共&nbsp"+data.data.fail+"&nbsp条",
	       //     });     
	    },
		queryParams:{
			beginTime: $('#creationDateLTE').val(),
			endTime: $('#creationDateGTE').val(),
			busCode:'checkPersonEx'
		}
	}); 
});

