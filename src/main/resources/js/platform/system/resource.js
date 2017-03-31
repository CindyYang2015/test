$(function() {

});

/**
 * 根据资源类型获取名称
 * @param resType 1：菜单，2：按键
 */
function getResNameByResType(value, row, index) {
	if(!value) {
		return;
	}
	if(value == "1") {
		return '菜单'; 
	} else if(value == "2") {
		return '按键';
	} else {
		return ''; 
	}
}

/**
 * 资源是否可用(1 正常 2 暂停)
 * @param enabled 1 正常 2 暂停
 */
function getResEnabled(value, row, index) {
	if(!value) return;
	if(value == 1) return '正常'; 
	else if(value == 2) return '暂停';
	else return ''; 
}

/**
 * 是否是超级资源1-不是，2-是)
 * @param issys 1-不是，2-是
 */
function getResIssys(value, row, index) {
	if(!value) return;
	if(value == 1) {
		return '不是';
	} else if(value == 2) {
		return '是';
	} else {
		return ''; 
	}
}

/**
 * 添加资源信息
 */
function addResInfo() {
	$.acooly.framework.customDialog({
		url:'../resources/create.html',
		entity:'resource',
		width:600,
		height:350,
		title:'新增',
		onSuccess:function(result) {			
			return true;
		}
	},{
		buttonName:'确定',
		param:'resourceId',
		isTreeGrid:true
	});
}

/**
 * 编辑资源信息
 */
function editResInfo() {
	$.acooly.framework.customDialog({
		url:'../resources/edit.html',
		entity:'resource',
		width:600,
		height:350
		},{
			buttonName:'确定',
			isCheckselect:true,
			param:'resourceId',
			isTreeGrid:true
		});
}

/**
 * 设置资源图标
 */
function setResIcon() {
	var dialogId = $.acooly.framework.getDivId();	
	$('<div id="'+dialogId+'"></div>').dialog({
		title : '资源图标',
		href :  '../resources/icon?dialogId='+dialogId,
		width : 300,
		height : 300,
		modal : true,
		onClose : function() {
			$(this).dialog('destroy');
		}
	});
}

/**
 * 设置资源的图片
 * @param value
 * @param row
 * @param index
 */
function setResourceImage(value, row, index) {
	if(!value) return;
	var str = "<img src='{image}' />";
	str = str.replace(/{image}/g,'..'+value);
	return str;
}

/**
 * 设置资源图片选择是否显示
 * @param record
 */
function setResourceImage(record) {
	if(!record) return;
	if(record.value == '1') {
		$("#resourceImageTrId").css("display","");
	} else {
		$("#resourceImageTrId").css("display","none");
	}
}
