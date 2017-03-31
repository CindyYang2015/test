
$(function() {

	initStepRow();
});




//添加一行算法引擎,次方法存在同步问题，通过往后台传递一个唯一标示再返回回来解决的。
function addNewStepRow(curObj){
	winHeightChange('add');
	//debugger;
	var index = $("#stepTable tr").length-1;//得到table的行数减2
	var ids = genTimeId("engineId","engineverId").split("|");
	var engineIdStr = ids[0];
	var engineverIdStr = ids[1];
	var trhtml = "<tr id='engineTr_"+index+"'> "+
				"<th>算法引擎：</th>"+
				"<td>"+
				"	<input class='easyui-combobox' name='"+engineIdStr+"' id='"+engineIdStr+"'  editable='false'data-options='required:true'/>"+
				"</td>"+
				"<th>引擎版本：</th>"+
				"<td>"+
				"	<input class='easyui-combobox' name='"+engineverIdStr+"' id='"+engineverIdStr+"' editable='false' data-options='required:true'/>"+
				"	<a href='javascript:void(0);' id='a_add_"+index+"' class='easyui-linkbutton' iconCls='icon-add' title='添加一行' plain='true' "+
				"	onclick='addNewStepRow(this)' index='"+index+"'>"+
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



//添加一行算法引擎
function initStepRow(curObj){

	var engVals =  $("#engineIds").val();
	var engArr = engVals.split("|");
	
	var engmap = new Map();
	for(var i=0;i<engArr.length;i++){
		
		winHeightChange('add');//添加窗口高度
		var index = $("#stepTable tr").length-1;//得到table的行数减2
		var ids = genTimeId("engineId","engineverId").split("|");
		var engineIdStr = ids[0];
		var engineverIdStr = ids[1];	
		var eng_ver_arr = engArr[i].split("_");
		engmap.put(engineIdStr,eng_ver_arr[0]);
		engmap.put(engineverIdStr,eng_ver_arr[1]);
		var trhtml = "<tr id='engineTr_"+index+"'> "+
		"<th>算法引擎：</th>"+
		"<td>"+
		"	<input class='easyui-combobox'  name='"+engineIdStr+"' id='"+engineIdStr+"'  editable='false'  data-options='required:true'/>"+
		"</td>"+
		"<th>引擎版本：</th>"+
		"<td>"+
		"	<input class='easyui-combobox'  name='"+engineverIdStr+"' id='"+engineverIdStr+"' editable='false' data-options='required:true'/>"+
		"	<a href='javascript:void(0);' id='a_add_"+index+"' class='easyui-linkbutton' iconCls='icon-add' title='添加一行' plain='true' "+
		"	onclick='addNewStepRow(this)' index='"+index+"'>"+
		"	</a>"+
		"	<a href='javascript:void(0);' id='a_del_"+index+"' class='easyui-linkbutton' iconCls='icon-remove' title='删除一行' plain='true' "+ 
		"onclick='delStepRow(this)' index='"+index+"'>"+
		"	</a>"+
		"</td>"+
		"</tr>";
		$("#stepTable").append(trhtml);		
		
		
		//初始化算法引擎
		$('#'+engineIdStr).combobox({
			url:"../step/validEngineAllList?inputId="+engineIdStr,
	        editable:false,
	        method:"post",
	        valueField: 'id',
	        textField: 'engineName',        
	        onSelect:function(record){
	        		debugger;
	                var engineCode = record.engineCode;//得到当前ID
	                var curVerInputId = record.inputId.replace("engineId","engineverId");
	            	$('#'+curVerInputId).combobox({
	            		url:"../step/selectEngineVer?engineCode="+engineCode+"&inputId="+curVerInputId,
	                    editable:false,
	                    method:"post",
	                    valueField: 'id',
	                    textField: 'verNo',
	                    onLoadSuccess: function () { //数据加载完毕事件
	                    	//debugger;
	                    	var data = $(this).combobox('getData');
	                    	var verid = engmap.get(data[0].inputId);
	                    	
	                    	var verflag = false;
	                    	for(var y=0;y<data.length;y++){
	                    		if(data[y].id==verid){
	                    			$(this).combobox('select', verid);
	                    			verflag = true;
	                    			break;
	                    		}
	                    	}
	                    	if(!verflag){
	                    		$(this).combobox('select', data[0].id);
	                    	}
	                    	
	                    	
//	                        if (data.length > 0) {
//	                            $(this).combobox('select', data[0].id);
//	                        }
	                    }
	                });                
	                
	        },onLoadSuccess: function () {

	        	var data = $(this).combobox('getData');
	        	if(data.length>0){
	        		$(this).combobox('select', engmap.get(data[0].inputId));
	        	}
	        	
	        }
		});	

		
//		//初始化引擎版本
//		var engineCode = eng_ver_arr[0];//得到当前ID
//    	$('#'+engineverIdStr).combobox({
//    		url:"../step/selectEngineVer?engineCode="+engineCode+"&inputId="+engineverIdStr,
//            editable:false,
//            method:"post",
//            valueField: 'id',
//            textField: 'verNo',
//            onLoadSuccess: function () { //数据加载完毕事件
//            	//debugger;
//            	var data = $(this).combobox('getData');
//            	
//                if (data.length > 0) {
//                    $(this).combobox('select', engmap.get(data[0].inputId));
//                }
//            }
//        });     

    	
	   	//+号按钮初始化样式
	   	$("#a_add_"+index).linkbutton({
	   		iconCls: "icon-add"
	   	});
	   	$("#a_del_"+index).linkbutton({
	   		iconCls: "icon-remove"
	   	});		

	}
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
	var datetime = myDate.getTime()+""+idIndex;
	var curId = pre1+"_"+datetime+"|"+pre2+"_"+datetime;
	idIndex++;
	return curId;
}

//js实现map功能
function Map() {
    /** 存放键的数组(遍历用到) */
    this.keys = new Array();
    /** 存放数据 */
    this.data = new Object();
    
    /**
     * 放入一个键值对
     * @param {String} key
     * @param {Object} value
     */
    this.put = function(key, value) {
        if(this.data[key] == null){
            this.keys.push(key);
        }
        this.data[key] = value;
    };
    
    /**
     * 获取某键对应的值
     * @param {String} key
     * @return {Object} value
     */
    this.get = function(key) {
        return this.data[key];
    };
    
    /**
     * 删除一个键值对
     * @param {String} key
     */
    this.remove = function(key) {
        this.keys.remove(key);
        this.data[key] = null;
    };
    
    /**
     * 遍历Map,执行处理函数
     * 
     * @param {Function} 回调函数 function(key,value,index){..}
     */
    this.each = function(fn){
        if(typeof fn != 'function'){
            return;
        }
        var len = this.keys.length;
        for(var i=0;i<len;i++){
            var k = this.keys[i];
            fn(k,this.data[k],i);
        }
    };
    
    /**
     * 获取键值数组(类似Java的entrySet())
     * @return 键值对象{key,value}的数组
     */
    this.entrys = function() {
        var len = this.keys.length;
        var entrys = new Array(len);
        for (var i = 0; i < len; i++) {
            entrys[i] = {
                key : this.keys[i],
                value : this.data[i]
            };
        }
        return entrys;
    };
    
    /**
     * 判断Map是否为空
     */
    this.isEmpty = function() {
        return this.keys.length == 0;
    };
    
    /**
     * 获取键值对数量
     */
    this.size = function(){
        return this.keys.length;
    };
    
    /**
     * 重写toString 
     */
    this.toString = function(){
        var s = "{";
        for(var i=0;i<this.keys.length;i++,s+=','){
            var k = this.keys[i];
            s += k+"="+this.data[k];
        }
        s+="}";
        return s;
    };
}




