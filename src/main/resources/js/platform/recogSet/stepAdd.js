
$(function() {
	//初始化算法引擎
	$('#engineId_0').combobox({
		url:"../step/validEngineAllList",
        editable:false,
        method:"post",
        valueField: 'id',
        textField: 'engineName',        
        onSelect:function(record){
                var engineCode = record.engineCode;//得到当前ID
            	$('#engineverId_0').combobox({
            		url:"../step/selectEngineVer?engineCode="+engineCode,
                    editable:false,
                    method:"post",
                    valueField: 'id',
                    textField: 'verNo',
                    onLoadSuccess: function () { //数据加载完毕事件
                    	//debugger;
                    	var data = $('#engineverId_0').combobox('getData');
                        if (data.length > 0) {
                            $("#engineverId_0").combobox('select', data[0].id);
                        }
                    }
                });                
                
        }
	});
	
//	//表单提交之前做一些业务处理
//	$.acooly.framework.submitBefore = function(){
//		var englist=$('[id^=engineId_]');//模糊查询ID为engineId_的输入框
//		var listValues = "";
//		var flag = false;
//		$.each(englist,function(index,ele){
//			var engId = ele.id;
//			var idtime = engId.split("_")[1];
//			var engVal = $('#'+engId).combobox('getValue');
//			var verVal = $("#engineverId_"+idtime).combobox('getValue');
//			if(listValues.indexOf(engVal)>=0 ||
//			   listValues.indexOf(verVal)>=0	
//				){
//				$.messager.alert("提示","不能选择重复的算法引擎");
//				flag = true;
//		    	return false; 
//			}			
//			if(index==0){
//				listValues = listValues + engVal+"_"+verVal;
//			}else{
//				listValues = listValues + "|"+engVal+"_"+verVal;
//			}
//		});
//		if(flag){
//			return false;
//		}
//		$("#engineIds").val(listValues);
//		return true;
//	};

});
//添加一行算法引擎
function addStepRow(curObj){
	winHeightChange('add');
	//debugger;
	var index = $("#stepTable tr").length-1;//得到table的行数减2
	var ids = genTimeId("engineId","engineverId").split("|");
	var engineIdStr = ids[0];
	var engineverIdStr = ids[1];
	var trhtml = "<tr id='engineTr_"+index+"'> "+
				"<th>算法引擎：</th>"+
				"<td>"+
				"	<input class='easyui-combobox' name='"+engineIdStr+"' id='"+engineIdStr+"'editable='false' data-options='required:true'/>"+
				"</td>"+
				"<th>引擎版本：</th>"+
				"<td>"+
				"	<input  class='easyui-combobox' name='"+engineverIdStr+"' id='"+engineverIdStr+"'editable='false'  data-options='required:true'/>"+
				"	<a href='javascript:void(0);' id='a_add_"+index+"' class='easyui-linkbutton' iconCls='icon-add' title='添加一行' plain='true' "+
				"	onclick='addStepRow(this)' index='"+index+"'>"+
				"	</a>"+
				"	<a href='javascript:void(0);' id='a_del_"+index+"' class='easyui-linkbutton' iconCls='icon-remove' title='删除一行' plain='true' "+ 
				"onclick='delStepRow(this)' index='"+index+"'>"+
				"	</a>"+
				"</td>"+
				"</tr>";
	$("#stepTable").append(trhtml);
	
	
	//初始化算法引擎
	$('#'+engineIdStr).combobox({
		url:"../step/validEngineAllList",
        editable:false,
        method:"post",
        valueField: 'id',
        textField: 'engineName',        
        onSelect:function(record){
                var engineCode = record.engineCode;//得到当前ID
            	$('#'+engineverIdStr).combobox({
            		url:"../step/selectEngineVer?engineCode="+engineCode,
                    editable:false,
                    method:"post",
                    valueField: 'id',
                    textField: 'verNo',
                    onLoadSuccess: function () { //数据加载完毕事件
                    	//debugger;
                    	var data = $('#'+engineverIdStr).combobox('getData');
                        if (data.length > 0) {
                            $("#"+engineverIdStr).combobox('select', data[0].id);
                        }
                    }
                });                
                
        }
	});	
	//初始化样式
   	$('#'+engineverIdStr).combobox({
        editable:false,
        method:"post",
        valueField: 'id',
        textField: 'verNo'
    }); 	
   	//+号按钮初始化样式
   	$("#a_add_"+index).linkbutton({
   		iconCls: "icon-add"
   	});
   	$("#a_del_"+index).linkbutton({
   		iconCls: "icon-remove"
   	});
   	
}

//改变窗口高度
function winHeightChange(flagStr){
	var curWinId = $.acooly.framework.getCurWinDivId();//获得当前窗口div的id
	var curHeight = $('#'+curWinId).css("height");//得到当前窗口高度
	//$('#'+curWinId).dialog({height: parseInt(curHeight+"")+20});//动态设置当前窗口高度
		
	if(flagStr=='add'){
		$('#'+curWinId).css("height",(parseInt(curHeight+"")+35)+"px");//动态设置当前窗口高度
	}else if(flagStr=='del'){
		$('#'+curWinId).css("height",(parseInt(curHeight+"")-35)+"px");//动态设置当前窗口高度
	}
}




function delStepRow(curObj){
	var  index = $(curObj).attr("index");
	var trlength = $("#stepTable tr").length;//得到table行数
	winHeightChange('del');//减去窗口高度
	$("#engineTr_"+index).remove();//移除本行
	for(var i=parseInt(index)+1;i<trlength-1;i++){
		$("#engineTr_"+i).attr("id","engineTr_"+(i-1));
		$("#a_add_"+i).attr("index",""+(i-1));
		$("#a_add_"+i).attr("id","a_add_"+(i-1));
		$("#a_del_"+i).attr("index",""+(i-1));
		$("#a_del_"+i).attr("id","a_del_"+(i-1));
	}
}
var idIndex=0;
function genTimeId(pre1,pre2){
	var myDate = new Date();
	var datetime = myDate.getTime()+idIndex;
	var curId = pre1+"_"+datetime+"|"+pre2+"_"+datetime;
	idIndex++;
	return curId;
}






