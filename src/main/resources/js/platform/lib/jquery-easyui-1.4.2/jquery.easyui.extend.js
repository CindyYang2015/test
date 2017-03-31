/**
 * 扩展validatebox的验证规则
 */
var _passwordPolicy = null;
var passwordPolicy = function() {
	console.log(contextPath);
	if (_passwordPolicy == null) {

		$.ajax({
			url : contextPath + "/manage/system/user/passwordPolicy.html",
			async : false,
			success : function(data) {
				// console.log("loaded password policy!" + data);
				_passwordPolicy = data;
			},
			error : function() {
				console.log("load password policy faild!");
			}
		});
	}
	return _passwordPolicy;
};

$
		.extend(
				$.fn.validatebox.defaults.rules,
				{
					/** byte长度验证 */
					byteLength : {
						validator : function(value, param) {
							var str = value;
							var minlen = param[0];
							var maxlen = param[1];
							if (str == null) {
								return false;
							}
							var l = str.length;
							var blen = 0;
							for (var i = 0; i < l; i++) {
								if ((str.charCodeAt(i) & 65280) != 0) {
									blen++;
								}
								blen++;
							}
							if (blen > maxlen || blen < minlen) {
								return false;
							}
							return true;
						},
						message : '输入的数据长度必须在{0}位到{1}位之间'
					},
					// 两个表单相等
					equals : {
						validator : function(value, param) {
							return value == $(param[0]).val();
						},
						message : '两次输入不相同.'
					},
					// 验证汉子
					CHS : {
						validator : function(value) {
							return /^[\u0391-\uFFE5]+$/.test(value);
						},
						message : '只能输入汉字'
					},
					zeroToFive : {
						validator : function(value) {
							return /^[1-5](\.\d{1,2})?$/.test(value);
						},
						message : '只能入输大于等于1小于6的整数或小数（最多保留2位）'
					},
					// 移动手机号码验证
					mobile : {// value值为文本框中的值
						validator : function(value) {
							var reg = /^1[3|4|5|8|9]\d{9}$/;
							return reg.test(value);
						},
						message : '输入手机号码格式不准确.'
					},
					// 通用的电话号码验证
					telephone : {// value值为文本框中的值
						validator : function(value) {
							var reg = /^\d{11}$/;
							return reg.test(value);
						},
						message : '电话号码格式不准确.'
					},
					// 传真号码验证
					fax : {// value值为文本框中的值
						validator : function(value) {
							var reg = /^\d{11}$/;
							return reg.test(value);
						},
						message : '传真格式不准确.'
					},
					// 邮箱验证
					email : {// value值为文本框中的值
						validator : function(value) {
							var reg = /^(\w)+(\.\w+)*@(\w)+((\.\w+)+)$/;
							return reg.test(value);
						},
						message : '邮箱格式不准确.'
					},
					// 浮点数范围验证
					doubleRange : {//浮点数范围验证 param说明 param[0] 表示数据开始 param[1] 表示数据结束 param[2] 表示开始数据id param[3] 表示数据匹配的正则表达式  param[4] 
						validator : function(value,param) {
							if(!param || param.length != 5) return true;							
							var status = param[3].test(value);
							if(!status) {
								$.fn.validatebox.defaults.rules.doubleRange.message = param[4];
								return false;
							}
							
							if(value <  param[0] || value > param[1]) {
								$.fn.validatebox.defaults.rules.doubleRange.message = "输入值必须在"+ param[0] + '至' + param[1] + '范围.';
								return false;
							} 
							
							if(param[2]) {
								//debugger;
								var beginValue = $("#"+param[2]).val();
								if(value < beginValue) {
									$.fn.validatebox.defaults.rules.doubleRange.message = "不能小于开始数值.";
									return false;
								}
							}
							
							return true;
							
							
						},
						message : ''
					},
					// 国内邮编验证
					zipcode : {
						validator : function(value) {
							var reg = /^[1-9]\d{5}$/;
							return reg.test(value);
						},
						message : '邮编必须是非0开始的6位数字.'
					},
					// 用户账号验证(只能包括 _ 数字 字母)
					account : {// param的值为[]中值
						validator : function(value, param) {
							if (value.length < param[0]
									|| value.length > param[1]) {
								$.fn.validatebox.defaults.rules.account.message = '用户名长度必须在'
										+ param[0] + '至' + param[1] + '范围';
								return false;
							} else {
								if (!/^[\w]+$/.test(value)) {
									$.fn.validatebox.defaults.rules.account.message = '用户名只能数字、字母、下划线组成.';
									return false;
								} else {
									return true;
								}
							}
						},
						message : ''
					},
					password : {// value值为文本框中的值
						validator : function(value) {
							var t = passwordPolicy();
							var vaild = true;
							if (t != null && t.regex != null
									&& $.trim(t.regex) != "") {
								var regex = new RegExp(t.regex);
								vaild = regex.test(value);
							}
							return vaild;
						},
						message : function(value) {
							return passwordPolicy().description;
						}
					},
					range : {
						validator : function(value, param) {
							if (/^[1-9]\d*$/.test(value)) {
								return value >= param[0] && value <= param[1]
							} else {
								return false;
							}
						},
						message : '输入的数字在{0}到{1}之间'
					},
					regex : {
						validator : function(value, param) {
							var regex = new RegExp(param[0]);
							return regex.test(value);
						},
						message : '无效的格式.'
					},
					selectEmpty : {
						validator : function(value, param) {
							var selectVal = $("input[name='" + param[0]+"']").val();
							return !(selectVal == "");
						},
						message : '必须选择.'
					},
					comboboxEmpty : {
						validator : function(value, param) {
							return !($("#" + param[0]).find("option:contains('"+value+"')").val() == '');
						},
						message : '必须选择.'
					},
					rengeLength : {
						validator : function(value, param) {
							var len = $.trim(value).length;
							return len >= param[0] && len <= param[1];
						},
						message : "输入内容长度必须介于{0}和{1}之间."
					},
					character : {
						validator : function(value, param) {
							var pattern = new RegExp("[<>&!']");
							return !pattern.test(value);
						},
						message : "不能输入特殊字符."
					},
					numberSize : {
						validator : function(value, param) {
							var num = param[0] - 1;
							var pattern = new RegExp("^[1-9](\\d{1," + num
									+ "})?$");
							return pattern.test(value);
						},
						message : "必须是非0开始介于1位至{0}位的数字."
					},
					intNumber : {
						validator : function(value, param) {
							return /^[1-9](\d{1,8})?$/.test(value);
						},
						message : "必须是非0开始介于1位至9位的数字."
					},
					mustNumber : {
						validator : function(value, param) {
							/*debugger;
							var pattern = new RegExp("^(?!0{" + param[0] + "})\\d{" + param[0] + "}$");*/
							return /^[0-9]\d*$/.test(value);
						},
						message : "必须数字."
						//message : "必须是{0}位数的数字,若不足{0}位数,在数字前补0"
					},
					untable: {//不能包含空格
						
		                validator: function (value) {
		                    return /^[^\s]*$/.test(value);
		                },
		                message: '输入值不能包含空格'
		            },
					isCode:{
						validator: function (value) {
		                    return /^[A-Za-z0-9]*$/.test(value);
		                },
		                message: '输入值只能是数字和英文'
					},
		            isEn:{
		            	validator: function (value) {
		                    return /^[A-Za-z]*$/.test(value);
		                },
		                message: '输入值只能是英文'
		            }
				});

$.extend($.fn.datagrid.methods, {
	fixRownumber : function (jq) {
		return jq.each(function () {
			var panel = $(this).datagrid("getPanel");
			//获取最后一行的number容器,并拷贝一份
			var clone = $(".datagrid-cell-rownumber", panel).last().clone();
			//由于在某些浏览器里面,是不支持获取隐藏元素的宽度,所以取巧一下
			clone.css({
				"position" : "absolute",
				left : -1000
			}).appendTo("body");
			var width = clone.width("auto").width();
			//默认宽度是25,所以只有大于25的时候才进行fix
			if (width > 25) {
				//多加5个像素,保持一点边距
				$(".datagrid-header-rownumber,.datagrid-cell-rownumber", panel).width(width + 5);
				//修改了宽度之后,需要对容器进行重新计算,所以调用resize
				$(this).datagrid("resize");
				//一些清理工作
				clone.remove();
				clone = null;
			} else {
				//还原成默认状态
				$(".datagrid-header-rownumber,.datagrid-cell-rownumber", panel).removeAttr("style");
			}
		});
	},
	/**
	 * 级联选择
     * @param {Object} target
     * @param {Object} param 
	 *		param包括两个参数:
     *			id:勾选的节点ID
     *			deepCascade:是否深度级联
     * @return {TypeName} 
	 */
	cascadeCheck : function(target,param){
		var opts = $.data(target[0], "treegrid").options;
		if(opts.singleSelect)
			return;
		var idField = opts.idField;//这里的idField其实就是API里方法的id参数
		var status = false;//用来标记当前节点的状态，true:勾选，false:未勾选
		var selectNodes = $(target).treegrid('getSelections');//获取当前选中项
		for(var i=0;i<selectNodes.length;i++){
			if(selectNodes[i][idField]==param.id)
				status = true;
		}
		//级联选择父节点
		selectParent(target[0],param.id,idField,status);
		selectChildren(target[0],param.id,idField,param.deepCascade,status);
		/**
		 * 级联选择父节点
		 * @param {Object} target
		 * @param {Object} id 节点ID
		 * @param {Object} status 节点状态，true:勾选，false:未勾选
		 * @return {TypeName} 
		 */
		function selectParent(target,id,idField,status){
			var parent = $(target).treegrid('getParent',id);
			if(parent){
				var parentId = parent[idField];
				if(status)
					$(target).treegrid('select',parentId);
//				else
//					$(target).treegrid('unselect',parentId);
				selectParent(target,parentId,idField,status);
			}
		}
		/**
		 * 级联选择子节点
		 * @param {Object} target
		 * @param {Object} id 节点ID
		 * @param {Object} deepCascade 是否深度级联
		 * @param {Object} status 节点状态，true:勾选，false:未勾选
		 * @return {TypeName} 
		 */
		function selectChildren(target,id,idField,deepCascade,status){
			//深度级联时先展开节点
			if(!status&&deepCascade)
				$(target).treegrid('expand',id);
			//根据ID获取下层孩子节点
			var children = $(target).treegrid('getChildren',id);
			for(var i=0;i<children.length;i++){
				var childId = children[i][idField];
				if(status)
					$(target).treegrid('select',childId);
				else
					$(target).treegrid('unselect',childId);
				selectChildren(target,childId,idField,deepCascade,status);//递归选择子节点
			}
		}
	}
});

var formatDate = function(value) {
	return value;
	var date = new Date(value);
	return date.getFullYear() + '-' + formatDatePart(date.getMonth() + 1) + '-'
			+ formatDatePart(date.getDate()) + " "
			+ formatDatePart(date.getHours()) + ":"
			+ formatDatePart(date.getMinutes()) + ":"
			+ formatDatePart(date.getSeconds());
};

var formatDatePart = function(datePart) {
	if (datePart < 10) {
		return datePart;
	} else {
		return datePart;
	}
};

/*
 * formatString功能 使用方法：formatString('字符串{0}字符串{0}字符串{1}','第一个变量','第二个变量');
 * @returns 格式化后的字符串
 */
var formatString = function(str) {
	for ( var i = 0; i < arguments.length - 1; i++) {
		eval("var re = /\\{" + i + "\\}/g;");
		str = str.replace(re, arguments[i + 1]);
	}
	return str;
};

var formatIcon = function(value) {
	return "<span style='vertical-align:middle;display:inline-block; width:16px; height:16px;' class='"
			+ value + "'></span>";
};

var formatRefrence = function(datagrid, filed, value) {
	try {
		return $("#" + datagrid).datagrid('getData').data[filed][value];
	} catch (e) {
		return value;
	}
};

var formatTreeRefrence = function(datagrid, filed, id,value) {
	try {
		return $("#" + datagrid).treegrid('getParent').datagrid('getData').data[filed][value];
	} catch (e) {
		return value;
	}
};

/**
 * 在datagrid中，根据id从List中取出名称
 * @author YUWU
 * @date 2014-10-05
 * datagrid 表格ID
 * filed List名称
 * value id的值
 * id List中对象的ID列名
 * name List中对象的显示名称列名
 */
var formatRefrencList = function(datagrid, filed, value, id, name) {
	var displayName = "";
	try {
		var arrayTemp = $("#" + datagrid).datagrid('getData').data[filed];
		$.each(arrayTemp,function(index,objTemp){
			if(objTemp[id] == value){
				displayName = objTemp[name];
				return;
			}
		});
	} catch (e) {
		return displayName;
	}
	return displayName;
};

var formatRefrenceColor = function(grid, key, value, flag, color) {
	var txt = formatRefrence(grid, key ,value);
	if(flag) {
		return "<span style='color:"+color+";'>" + txt + "</span>";
	}
	return txt;
};

var formatRefrenceColor2 = function(grid, value, flag, color) {
	var txt = value;
	if(flag) {
		return "<span style='color:"+color+";'>" + txt + "</span>";
	}
	return txt;
};

var formatAction = function(actionContainer, value, row) {
	return formatString($('#' + actionContainer).html(), row.id);
};

var formatActionWithStatus = function(actionContainer, actionStatusContainer,
		value, row) {
	if (row.status == 1) {
		return formatString($('#' + actionStatusContainer).html(), row.id);
	} else if (row.status == 0) {
		return formatString($('#' + actionContainer).html(), row.id);
	}
};

/**
 * 
 * 接收一个以逗号分割的字符串，返回List，list里每一项都是一个字符串
 * 
 * @returns list
 */
stringToList = function(value) {
	if (value != undefined && value != '') {
		var values = [];
		var t = value.split(',');
		for ( var i = 0; i < t.length; i++) {
			values.push('' + t[i]);/* 避免他将ID当成数字 */
		}
		return values;
	} else {
		return [];
	}
};

function mapToOptions(map, forSearch) {
	var mapJson = "[";
	if (forSearch) {
		mapJson += "{id:'',text:'所有'}";
	}
	var i = 0;
	for ( var key in map) {
		if (i == 0 && !forSearch) {
			mapJson += "{id:" + key + "," + "text:'" + map[key] + "'}";
		} else {
			mapJson += ",{id:" + key + "," + "text:'" + map[key] + "'}";
		}
		i++;
	}
	mapJson += "]";
	return eval(mapJson);
}

/**
 * 使panel和datagrid在加载时提示
 */
$.fn.panel.defaults.loadingMessage = '加载中....';
$.fn.datagrid.defaults.loadMsg = '加载中....';
$.fn.treegrid.defaults.loadMsg = '加载中....';

/**
 * @requires jQuery,EasyUI panel关闭时回收内存，主要用于layout使用iframe嵌入网页时的内存泄漏问题
 */
$.fn.panel.defaults.onBeforeDestroy = function() {
	var frame = $('iframe', this);
	try {
		if (frame.length > 0) {
			for ( var i = 0; i < frame.length; i++) {
				frame[i].contentWindow.document.write('');
				frame[i].contentWindow.close();
			}
			frame.remove();
			if ($.browser.msie) {
				CollectGarbage();
			}
		}
	} catch (e) {
	}
};

/**
 * 扩展datagrid默认的loadFilter,添加错误处理
 */
$.fn.datagrid.defaults.loadFilter = function(data, parent) {
	if (!data.success && data.message) {
		$.messager.alert('提示', data.message);
		return null;
	}
	return data;
};

/**
 * 通用异常处理
 */
var commonErrorFunction = function(XMLHttpRequest) {
	$.messager.progress('close');
	$.messager.alert('错误', XMLHttpRequest.responseText);
};

$.fn.datagrid.defaults.onLoadError = commonErrorFunction;
$.fn.treegrid.defaults.onLoadError = commonErrorFunction;
$.fn.tree.defaults.onLoadError = commonErrorFunction;
$.fn.combogrid.defaults.onLoadError = commonErrorFunction;
$.fn.combobox.defaults.onLoadError = commonErrorFunction;
$.fn.form.defaults.onLoadError = commonErrorFunction;

/**
 * 改变jQuery的AJAX默认属性和方法
 */
$.ajaxSetup({
	type : 'POST',
	error : commonErrorFunction
});

/**
 * 防止panel/window/dialog组件超出浏览器边界
 * 
 * @param left
 * @param top
 */
var easyuiPanelOnMove = function(left, top) {
	var l = left;
	var t = top;
	if (l < 1) {
		l = 1;
	}
	if (t < 1) {
		t = 1;
	}
	var width = parseInt($(this).parent().css('width')) + 14;
	var height = parseInt($(this).parent().css('height')) + 14;
	var right = l + width;
	var buttom = t + height;
	var browserWidth = $(window).width();
	var browserHeight = $(window).height();
	if (right > browserWidth) {
		l = browserWidth - width;
	}
	if (buttom > browserHeight) {
		t = browserHeight - height;
	}
	$(this).parent().css({/* 修正面板位置 */
		left : l,
		top : t
	});
};
$.fn.dialog.defaults.onMove = easyuiPanelOnMove;
$.fn.window.defaults.onMove = easyuiPanelOnMove;
$.fn.panel.defaults.onMove = easyuiPanelOnMove;

/**
 * 
 * 更换EasyUI主题的方法
 * 
 * @param themeName
 *            主题名称
 */
var changeTheme = function(themeName) {
	var $easyuiTheme = $('#easyuiTheme');
	var url = $easyuiTheme.attr('href');
	var href = url.substring(0, url.indexOf('themes')) + 'themes/' + themeName
			+ '/easyui.css';
	$easyuiTheme.attr('href', href);

	var $iframe = $('iframe');
	if ($iframe.length > 0) {
		for ( var i = 0; i < $iframe.length; i++) {
			var ifr = $iframe[i];
			$(ifr).contents().find('#easyuiTheme').attr('href', href);
		}
	}
	$.cookie('easyuiThemeName', themeName, {
		expires : 7
	});
	selectMenu("layout_menu_theme", themeName);
};

/**
 * 将form表单元素的值序列化成对象
 * 
 * @returns object
 */
serializeObject = function(form) {
	var o = {};
	$.each(form.serializeArray(), function(index) {
		if (o[this['name']]) {
			o[this['name']] = o[this['name']] + "," + this['value'];
		} else {
			o[this['name']] = this['value'];
		}
	});
	return o;
};

/**
 * 从表单的父容器中序列化表单
 */
serializeObjectFromContainer = function(container) {
	var queryParams = {};
	$("input[name^='search']", container).each(
		function() {
			if (queryParams[this['name']]) {
				queryParams[this['name']] = queryParams[this['name']] + ","
						+ this['value'];
			} else {
				queryParams[this['name']] = this['value'];
			}
		});
	return queryParams;
};

/**
 * 判断对象是否为空对象
 */
isEmptyObject = function(obj) {
	for ( var name in obj) {
		return false;
	}
	return true;
};

var onDatagridDblClickCell = function(index, field, value) {
	$(this).datagrid("beginEdit", index);
	var ed = $(this).datagrid("getEditor", {
		index : index,
		field : field
	});
	$(ed.target).val(value);
};

arrayToTree = function(data, id, pid) { // 将ID、PID这种数据格式转换为树格式
	if (!data || !data.length)
		return [];
	var targetData = []; // 存储数据的容器(返回)
	var records = {};
	var itemLength = data.length; // 数据集合的个数
	for ( var i = 0; i < itemLength; i++) {
		var o = data[i];
		records[o[id]] = o;
	}
	for ( var i = 0; i < itemLength; i++) {
		var currentData = data[i];
		var parentData = records[currentData[pid]];
		if (!parentData) {
			targetData.push(currentData);
			continue;
		}
		parentData.children = parentData.children || [];
		parentData.children.push(currentData);
	}
	return targetData;
};

var optionDefaults = {
	url : '',
	params : {},
	target : null,
	emptyable : true,
	emptyText : '请选择'
};

var createOptions = function(opts) {
	opts = $.extend({}, optionDefaults, opts || {});
	$.getJSON(opts.url, opts.params, function(res) {
		var option = new Array();
		option.push({
			text : opts.emptyText,
			value : ""
		});
		if (res.success) {
			for (key in res.entity) {
				option.push({
					text : res.entity[key],
					value : key
				});
			}
		}
		if (opts.target) {
			$("#" + opts.target).combobox("clear").combobox("loadData", option);
		}
	});
};

var default_treeroot = {
	id: "", 
	text: '不限',
	iconCls: 'icon-blank', 
	checked: false,
	state: 'open', 
	attributes: []
};

wapperTree = function(data) {
	var opts = $.extend({}, default_treeroot, {});
	data.unshift(opts);
	return data;
};

/*
 * 获取length长度的字符串,超出的部分用...表示
 */
getLimitStrBylength = function(data,length) {
	if(data) {
		if(data.length > length) {
			return "<span title='"+ data +"'>" + data.substr(0,length) + "..." + "</span>";
			//return data.substr(0,length) + "...";
		}else{
			return data;
		}
	} else {
		return "";
	}
};
//去除HTML tag
function removehtmlTag(str) {
	//去除HTML tag
	var content = str.replace(/<\/?[^>]*>/g,'');	
	return content;
}


(function($) {
	
	function getTop(w, options) {
		var _doc;
		try {
			_doc = w.top.document;
			_doc.getElementsByTagName
		} catch (e) {
			return w
		}
		if (options.locate == "document"
				|| _doc.getElementsByTagName("frameset").length > 0) {
			return w
		}
		return w.top
	}
	
	function setWindowSize(w, options) {
		var _top = getTop(w, options);
		var wHeight = $(_top).height(), wWidth = $(_top).width();
		if (options.locate == "top" || options.locate == "document") {
			if (options.height == "auto") {
				options.height = wHeight * 0.6
			}
			if (options.width == "auto") {
				options.width = wWidth * 0.6
			}
		} else {
			var locate = /^#/.test(options.locate) ? options.locate : "#"
					+ options.locate;
			if (options.height == "auto") {
				options.height = $(locate).height() * 0.6
			}
			if (options.width == "auto") {
				options.width = $(locate).width() * 0.6
			}
		}
	}
	$.extend({
			showWindow : function(options) {
				options = options || {};
				var target;
				var winOpts = $.extend({}, {
					iconCls : "icon-form",
					useiframe : false,
					locate : "top",
					data : undefined,
					width : "auto",
					height : "auto",
					cache : false,
					minimizable : true,
					maximizable : true,
					collapsible : true,
					resizable : true,
					loadMsg : $.fn.datagrid.defaults.loadMsg,
					showMask : false,
					onClose : function() {
						target.dialog("destroy")
					}
				}, options);
				var iframe = null;
				if (/^url:/.test(winOpts.content)) {
					var url = winOpts.content.substr(4,
							winOpts.content.length);
					if (winOpts.useiframe) {
						iframe = $("<iframe>").attr("height", "100%").attr(
								"width", "100%").attr("marginheight", 0)
								.attr("marginwidth", 0).attr("frameborder",
										0);
						setTimeout(function() {
							iframe.attr("src", url)
						}, 10)
					} else {
						winOpts.href = url
					}
					delete winOpts.content
				}
				var selfRefrence = {
					getData : function(name) {
						return winOpts.data ? winOpts.data[name] : null
					},
					close : function() {
						target.panel("close")
					}
				};
				var _top = getTop(window, winOpts);
				var warpHandler = function(handler) {
					if (typeof handler == "function") {
						return function() {
							handler(selfRefrence)
						}
					}
					if (typeof handler == "string" && winOpts.useiframe) {
						return function() {
							iframe[0].contentWindow[handler](selfRefrence)
						}
					}
					if (typeof handler == "string") {
						return function() {
							eval(_top[handler])(selfRefrence)
						}
					}
				};
				setWindowSize(window, winOpts);
				if (winOpts.toolbar && $.isArray(winOpts.toolbar)) {
					$.each(winOpts.toolbar, function(i, button) {
						button.handler = warpHandler(button.handler)
					})
				}
				if (winOpts.buttons && $.isArray(winOpts.buttons)) {
					$.each(winOpts.buttons, function(i, button) {
						button.handler = warpHandler(button.handler)
					})
				}
				var onLoadCallback = winOpts.onLoad;
				winOpts.onLoad = function() {
					onLoadCallback
							&& onLoadCallback
									.call(this, selfRefrence, _top)
				};
				if (winOpts.locate == "top" || winOpts.locate == "document") {
					if (winOpts.useiframe && iframe) {
						if (winOpts.showMask) {
							winOpts.onBeforeOpen = function() {
								var panel = $(this).panel("panel");
								var header = $(this).panel("header");
								var body = $(this).panel("body");
								body.css("position", "relative");
								var mask = $(
										'<div class="datagrid-mask" style="display:block;"></div>')
										.appendTo(body);
								var msg = $(
										'<div class="datagrid-mask-msg" style="display:block; left: 50%;"></div>')
										.html(winOpts.loadMsg).appendTo(
												body);
								setTimeout(function() {
									msg.css("marginLeft",
											-msg.outerWidth() / 2)
								}, 5)
							}
						}
						iframe.bind("load", function() {
							if (iframe[0].contentWindow) {
								onLoadCallback
										&& onLoadCallback.call(this,
												selfRefrence,
												iframe[0].contentWindow);
								target.panel("body").children(
										"div.datagrid-mask-msg").remove();
								target.panel("body").children(
										"div.datagrid-mask").remove()
							}
						});
						target = _top.$("<div>").css({
							overflow : "hidden"
						}).append(iframe).dialog(winOpts)
					} else {
						target = _top.$("<div>").dialog(winOpts)
					}
				} else {
					var locate = /^#/.test(winOpts.locate) ? winOpts.locate
							: "#" + winOpts.locate;
					target = $("<div>").appendTo(locate).dialog(
							$.extend({}, winOpts, {
								inline : true
							}))
				}
				return target
			},
			showModalDialog : function(options) {
				options = options || {};
				var opts = $.extend({}, options, {
					modal : true,
					minimizable : false,
					maximizable : false,
					resizable : false,
					collapsible : false
				});
				return $.showWindow(opts)
			}
		});
})(jQuery);
