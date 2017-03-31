/**
 * 基础框架功能封装
 * 
 * @version: 1.0.0 
 * @required JQuery EasyUI
 */
(function($) {
	$.extend({
		acooly : {}
	});
	var curDivId = "";
	$.extend($.acooly,
			{				
				framework : {
					submitBefore:function(){//如果需要在提交之前做些操作，可以重写此方法
						return true;
					},
					getDivId:function() {
						var myDate = new Date();
						curDivId = "div" + myDate.getTime();
						return curDivId;
					},
					getCurWinDivId:function(){
						return curDivId;
					},
					uploadImagePreview :function(opts, callback) {
						$('#' + opts.uploadId).swfupload({
							upload_url: contextPath + opts.url + ";jsessionId=" + window['jsessionId'],
							file_size_limit : (opts.limit) ? opts.limit : 0,
							file_types : (opts.fileTypes && typeof opts.fileTypes === 'string') ? opts.fileTypes : "*.*",
							file_types_description : (opts.fileTypes && typeof opts.fileTypes === 'string') ? opts.fileTypes : "All Files",
							flash_url : contextPath + "/script/jquery-swfupload/swfupload.swf",
							button_image_url : contextPath + "/script/jquery-swfupload/XPButtonUploadText_61x22.png",
				            button_width : 61,
				            button_height : 22,
				            button_text_left_padding : 3,
				            button_text_top_padding : 2,
							button_placeholder_id : opts.placeholderId,
							post_params : (opts.postParams && typeof opts.postParams === 'object') ? opts.postParams : {},
						}).bind('swfuploadLoaded', function(event){
							//console.log('<li>Loaded</li>');
						})
						.bind('fileQueued', function(event, file){
							//console.log('<li>File queued - '+file.name+'</li>');
							$(this).swfupload('startUpload');
						})
						.bind('fileQueueError', function(event, file, errorCode, message){
							console.log('<li>File queue error - '+message+'</li>');
						})
						.bind('fileDialogStart', function(event){
							//console.log('<li>File dialog start</li>');
						})
						.bind('fileDialogComplete', function(event, numFilesSelected, numFilesQueued){
							//console.log('<li>File dialog complete</li>');
						})
						.bind('uploadStart', function(event, file){
							//console.log('<li>Upload start - '+file.name+'</li>');
						})
						.bind('uploadProgress', function(event, file, bytesLoaded){
							//console.log('<li>Upload progress - '+bytesLoaded+'</li>');
						})
						.bind('uploadSuccess', function(event, file, serverData){
							//console.log('<li>Upload success - '+file.name+'</li>');
							var result = $.parseJSON(serverData);
							if($.isFunction(callback)) {
								callback(result);
							}
						})
						.bind('uploadComplete', function(event, file){
							//console.log('<li>Upload complete - '+file.name+'</li>');
							$(this).swfupload('startUpload');
						})
						.bind('uploadError', function(event, file, errorCode, message){
							//console.log('<li>Upload error - '+message+'</li>');
						});
					},                 

                   
                    /**
                     * datagrid 字段编辑完成
                     */
                    editorDone : function(entity, url, callback) {
                    	var $dg = $("#manage_" + entity + "_datagrid");
                    	$dg.datagrid('uncheckAll').datagrid('unselectAll').datagrid('clearSelections');
                    	var rows = $dg.datagrid('getRows');
                    	for ( var i = 0; i < rows.length; i++) {
                    		$dg.datagrid("endEdit", i);
                    	}
                    	if ($dg.datagrid('getChanges').length && url) {
    						var updated = $dg.datagrid('getChanges', "updated");
    						var updateArray = new Array();
    						if (updated.length) {
    							$.each(updated, function(i, item){
    								updateArray.push(item.id + ',' + item.sortNo);
    							});
    						}
    						
    						$.ajax({
    							url : url,
    							data : {"updated": updateArray.join(";")},
    							dataType : 'json',
    							cache : false,
    							success : function(result) {
        							if(result.success) {
    	    							if(callback) {
    	    								callback.call($dg, result);
    	    							}
    	    							$dg.datagrid('acceptChanges');
    	    						}
        							if (result.message) {
        								$.messager.alert({title : '提示', msg : result.message });
        							}
        						},
        						error: function() {
        							$.messager.alert("提示", "提交错误了！");
        						}
    						});
                    	}
                    },
                    
                    editorSave : function(opts) {
                    	var $dg = $("#manage_" + opts.entity + "_datagrid");
                    	$.acooly.framework.editorDone(opts.entity);
                    	if ($dg.datagrid('getChanges').length ) {
	                    	$.messager.confirm("提示", "是否修改排序号", function(r) {
	                    		if(r) {
	                    			var updatedRow = $dg.datagrid('getChanges', "updated");
	        						if(updatedRow.length > 0) {
	        							var value = "", length = updatedRow.length;
	        							$.each(updatedRow, function(i, item){
	        								value += (item.id + "," + item.sortNo);
	        								if(i + 1 < length) {
	        									value += ";";
	        								}
	        							});
	        							$.post(contextPath + opts.url, {"sortValue":value}, function(res) {
	            							if(res.success){
	            								$dg.datagrid('acceptChanges');
	            							}
	            							if(res.message) {
	        									$.messager.alert({title: '提示', msg : res.message});
	        								}
	            						}, "json").error(function() {
	            							$.messager.alert("提示", "提交错误了！");
	            						});
	        						}
	                    		} else {
	                    			$dg.datagrid('rejectChanges');
	                    		}
	                    	});
                    	}
                    },
                    
					/**
					 * 添加
					 */
					create : function(opts){
						if(opts.entity){
							$.acooly.framework.doCreate(opts.url,"manage_" + opts.entity + "_editform","manage_" + opts.entity + "_datagrid",
									opts.title,opts.top,opts.width,opts.height,opts.reload,opts.onSubmit,opts.onSuccess,opts.beforeSerialize);
						}else{
							$.acooly.framework.doCreate(opts.url,opts.form,opts.datagrid,
									opts.title,opts.top,opts.width,opts.height,opts.reload,opts.onSubmit,opts.onSuccess,opts.beforeSerialize);
						}
					},
					
					/**
					 * 执行添加
					 */
					doCreate : function(url,form,datagrid,title,top,width,height,reload,onSubmitCallback,successCallback,beforeSerializeCallback){
						$('#'+datagrid).datagrid('uncheckAll').datagrid('unselectAll').datagrid('clearSelections');
						var t = top?top:null;
						var w = width?width:500;
						var h = height?height:'auto';
						var tt = title?title:'添加';
						var dialogId = $.acooly.framework.getDivId();
						$('<div id="'+dialogId+'"></div>').dialog({
							href : contextPath + url,
							title : tt,top:t,width : w, height: h, modal : true, iconCls:'icon-save',
							buttons : [ {
								text : '增加', 
								iconCls : 'icon-add',
								handler : function() {
//									var d = $(this).closest('.window-body');
									var d = $("#"+dialogId);
									var btnObj = this;
									
									$('#'+form).ajaxSubmit({	
										dataType:"json",
                                        beforeSerialize:function(a,b) {
                                            if(beforeSerializeCallback && !beforeSerializeCallback.call(this,arguments)){
                                                return false;
                                            }
                                        },
										beforeSubmit:function(formData, jqForm, options){
											//如果有回调函数则处理回调函数的验证
											if(onSubmitCallback && !onSubmitCallback.call(this,arguments)){
												return false;
											}
											//编辑页面onSubmit方法
											try {
												var defaultFunc = eval(form + "_onSubmit");
												if(defaultFunc && typeof(defaultFunc)=="function" && !defaultFunc.call(this,arguments)){
													return false;
												}
											} catch (e) {
												// ig
											}
											//默认easy-ui-validator验证
											var validateStatus = $('#'+form).form('validate'); 	
											if(validateStatus == true) {
												$(btnObj).linkbutton('disable');
												return true;
											} else {
												return validateStatus;
											}	
									
										},
										success:function(result, statusText){
											try {
												if (result.success) {
													//如果有回调函数则处理回调函数
													if(successCallback){
														successCallback.call(d, result);
													}
													$.acooly.framework.onSaveSuccess(d,datagrid,result,reload);
												} else {
													$(btnObj).linkbutton('enable');
												}
												
												if (result.message) {
													$.messager.alert({
														title : '提示',
														msg : result.message
													});
												}
											} catch (e) {
												$(btnObj).linkbutton('enable');
												$.messager.alert('提示', e + " DATA: "+ result.toString());
											}											
										},
										error:function() {
											$(btnObj).linkbutton('enable');
											$.messager.alert('错误',"服务器发生错误,请稍后重试！","error");
										}
									});	
								}
							},{
								text : '关闭', 
								iconCls : 'icon-cancel',
								handler : function() {
//									var d = $(this).closest('.window-body');
									var d = $("#"+dialogId);
									d.dialog('close');
								}
							} ],
							onClose : function() {
								$(this).dialog('destroy');
							}
						});
					},		
					
					/**
					 * 新增保存成功后，默认的处理：在datagrid的第一行添加该数据，同时关闭窗口。
					 */
					onSaveSuccess : function(dialog, datagrid, result,reload) {
						if(reload){
							$('#' + datagrid).datagrid('reload');
						}else{
							$('#' + datagrid).datagrid('insertRow', {
								index : 0,
								row : result.entity
							});						
						}
						dialog.dialog('destroy');
					},
					
					/**
					 * 编辑
					 */
					edit : function(opts){
						if(opts.entity){
							$.acooly.framework.doEdit(opts.url,opts.id,"manage_" + opts.entity + "_editform","manage_" + opts.entity + "_datagrid",
									opts.title,opts.top,opts.width,opts.height,opts.reload,opts.onSubmit,opts.onSuccess,opts.beforeSerialize);
						}else{
							$.acooly.framework.doEdit(opts.url,opts.id,opts.form,opts.datagrid,
									opts.title,opts.top,opts.width,opts.height,opts.reload,opts.onSubmit,opts.onSuccess,opts.beforeSerialize);
						}
					},					
					
					
					_getCanonicalUrl: function(url,id){
						if(typeof contextPath != 'undefined') {
							url = contextPath + url;
						} 						
						url += (url.indexOf('?') != -1?'&':'?');
						url += ('id=' + id);
						return url;
					},
					
					_getCanonicalUrlByParam: function(url,param){
						if(typeof contextPath != 'undefined') {
							url = contextPath + url;
						} 						
						var str = "";
						for(var field in param) {
							if(str.length > 0) {
								str += "&";
							}
							str += (field + "=" + param[field]);
						}
						if(!str) return url;						
						url += (url.indexOf('?') != -1?'&':'?');
						url += str;
						return encodeURI(url);
					},
					
					/**
					 * 执行编辑
					 */
					doEdit : function(url,id,form,datagrid,title,top,width,height,reload,onSubmitCallback,successCallback,beforeSerializeCallback){
						//$('#'+datagrid).datagrid('uncheckAll').datagrid('unselectAll').datagrid('clearSelections');
						var t = top?top:null;
						var w = width?width:500;
						var h = height?height:'auto';
						var tt = title?title:'编辑';
					    if(!id || id == ''){
							var checked = $("#"+datagrid).datagrid("getChecked");
							if(checked && checked.length > 0){
								id = $("#"+datagrid).datagrid("getChecked")[0]["id"];
							}
						}
						// changelog-end
						if(!id || id == ''){
							$.messager.alert('提示', '请选择需要编辑的数据');
							return;
						}
						var href = $.acooly.framework._getCanonicalUrl(url,id);
						var dialogId = $.acooly.framework.getDivId();
						$('<div id="'+dialogId+'"></div>').dialog({
							href : href,
							title : tt, top:t,width : w, height: h, modal : true, iconCls:'icon-save',
							buttons : [ {
								text : '修改', 								
								iconCls : 'icon-add',
								handler : function() {
									//var d = $(this).closest('.window-body');
									var d = $("#"+dialogId);
									var btnObj = this;	
									
									$('#'+form).ajaxSubmit({
										dataType:"json",
                                        beforeSerialize:function(a,b) {
                                            if(beforeSerializeCallback && !beforeSerializeCallback.call(this,arguments)){
                                                return false;
                                            }
                                        },
										beforeSubmit:function(formData, jqForm, options){
											
											//如果有回调函数则处理回调函数的验证
											if(onSubmitCallback && !onSubmitCallback.call(this,arguments)){
												return false;
											}
											//编辑页面onSubmit方法
											try {
												var defaultFunc = eval(form + "_onSubmit");
												if(defaultFunc && typeof(defaultFunc)=="function" && !defaultFunc.call(this,arguments)){
													return false;
												}
											} catch (e) {
												// ig
											}
											
											//默认easy-ui-validator验证
											var validateStatus = $('#'+form).form('validate'); 	
											if(validateStatus == true) {
												$(btnObj).linkbutton('disable');
												return true;
											} else {
												return validateStatus;
											}											
										},
										success:function(result, statusText){
											try {
												if (result.success) {
													//如果有回调函数则处理回调函数
													if(successCallback){
														successCallback(d,result);
													}
													$.acooly.framework.onUpdateSuccess(d,datagrid,result,id,reload);
												} else {
													$(btnObj).linkbutton('enable');
												}												
												if (result.message) {
													$.messager.alert({
														title : '提示',
														msg : result.message
													});
												}
											} catch (e) {	
												$(btnObj).linkbutton('enable');
												$.messager.alert('提示', e);
											}											
										},
										error:function() {
											$(btnObj).linkbutton('enable');
											$.messager.alert('错误',"服务器发生错误,请稍后重试！","error");
										}
									});	
								}
							},{
								text : '关闭', 
								iconCls : 'icon-cancel',
								handler : function() {
//									var d = $(this).closest('.panel');
									var d = $("#"+dialogId);
									d.dialog("close");
								}
							} ],
							onClose : function() {
								$(this).dialog('destroy');
							}
						});
					},	
					
					/**
					 * 自定义对话框
					 */
					customDialog : function(opts,otherOpts){
						if(opts.entity){
							$.acooly.framework.createConfigDialog(opts.url,opts.id,"manage_" + opts.entity + "_editform","manage_" + opts.entity + "_datagrid",
									opts.title,opts.top,opts.width,opts.height,opts.reload,opts.onSubmit,opts.onSuccess,opts.beforeSerialize,otherOpts);
						}else{
							$.acooly.framework.createConfigDialog(opts.url,opts.id,opts.form,opts.datagrid,
									opts.title,opts.top,opts.width,opts.height,opts.reload,opts.onSubmit,opts.onSuccess,opts.beforeSerialize,otherOpts);
						}
					},		
					
					/**
					 * 完全可配选中弹出对话框
					 */
					createConfigDialog : function(url,id,form,datagrid,title,top,width,height,reload,onSubmitCallback,successCallback,beforeSerializeCallback,opts){
						/*
						 * 添加的额外属性
						 */
						//是否选中时判断
						var isCheckselect = false;
						//对话框按钮名称及图标
						var buttonName = "保存";
						var buttoniconCls = "icon-add";
						//对话框图标
						var iconCls = "icon-save";
						//成功回调返回状态
						var successCallbackStatus = true;
						//查询的参数param字符串，以下划线表示
						var paramArray = [];
						//查询的参数
						var dataParam = {};
						//是否树形grid
						var isTreeGrid = false;
						if(opts) {
							isCheckselect = opts.isCheckselect?opts.isCheckselect:isCheckselect;
							buttonName = opts.buttonName?opts.buttonName:buttonName;
							buttoniconCls = opts.buttoniconCls?opts.buttoniconCls:buttoniconCls;
							iconCls = opts.iconCls?opts.iconCls:iconCls;
							if(opts.param) {
								paramArray = opts.param.split(",");
							}
							isTreeGrid = opts.isTreeGrid;
						}
						var t = top?top:null;
						var w = width?width:500;
						var h = height?height:'auto';
						var tt = title?title:'编辑';
						//选中的行记录
						var checkedRow;
						if(isTreeGrid) {
							checkedRow = $("#"+datagrid).treegrid("getChecked");
						} else {
							checkedRow = $("#"+datagrid).datagrid("getChecked");
						}										
						
						//根据选中项进行不同业务的处理
						if(opts && opts.afterSelectRowCallback) {
							if(!opts.afterSelectRowCallback.call(this,checkedRow)){
								return;
							}
						}
						
						//设置查询的参数						
						if(checkedRow && checkedRow.length > 0) {
							id = checkedRow[0][paramArray[0]];
							$.each(paramArray, function(i,val){							
								dataParam[val] = checkedRow[0][val];
					        });
						}		
						
						if(isCheckselect) {							
							if(!id || id == ''){
								$.messager.alert('提示', '请选择一行记录');
								return;
							}							
						}	
						
						var href = $.acooly.framework._getCanonicalUrlByParam(url,dataParam);
						var dialogId = $.acooly.framework.getDivId();
						$('<div id="'+dialogId+'"></div>').dialog({							
							href : href,
							title : tt, top:t,width : w, height: h, modal : true, iconCls:iconCls,
							buttons : [ {
								text : buttonName, 
								iconCls : buttoniconCls,
								handler : function() {
									var sbflag = $.acooly.framework.submitBefore();//表单提交之前
									if(!sbflag){
										return false;
									}
									var d = $("#"+dialogId);
									var btnObj = this;	
									$('#'+form).ajaxSubmit({										
										dataType:"json",
                                        beforeSerialize:function(a,b) {
                                            if(beforeSerializeCallback && !beforeSerializeCallback.call(this,arguments)){
                                                return false;
                                            }
                                        },
										beforeSubmit:function(formData, jqForm, options){											
											
											//如果有回调函数则处理回调函数的验证
											if(onSubmitCallback && !onSubmitCallback.call(this,arguments)){
												return false;
											}
											//编辑页面onSubmit方法
											try {
												var defaultFunc = eval(form + "_onSubmit");
												if(defaultFunc && typeof(defaultFunc)=="function" && !defaultFunc.call(this,arguments)){
													return false;
												}
											} catch (e) {
												// ig
											}
											//默认easy-ui-validator验证
											var validateStatus = $('#'+form).form('validate'); 	
											if(validateStatus == true) {
												$(btnObj).linkbutton('disable');
												return true;
											} else {
												return validateStatus;
											}											
										},
										success:function(result, statusText){											
											try {
												if (result.success) {
													//如果有回调函数则处理回调函数
													if(successCallback){
														successCallbackStatus = successCallback.call(d,result);
													}
													if(successCallbackStatus) {
														$.acooly.framework.onUpdateSuccess(d,datagrid,result,id,reload,isTreeGrid);
													} else {
														d.dialog('destroy');
													}													
												} else {
													$(btnObj).linkbutton('enable');
												}
												
												if (result.message) {
													//如果result.success返回成功，则返回带有info图标的对话框，显示成功信息>>由fengdezhi修改
													if(result.success){
														$.messager.alert({
															icon : 'info',
															title : '提示',
															msg : result.message
														});
													}else{
														$.messager.alert({
															icon : 'error',
															title : '提示',
															msg : result.message
														});					
													}
												}
											} catch (e) {
												$(btnObj).linkbutton('enable');
												$.messager.alert('提示', e);
											}											
										},
										error:function() {
											$(btnObj).linkbutton('enable');
											$.messager.alert('错误',"服务器发生错误,请稍后重试！","error");
										}
									});	
								}
							},{
								text : '关闭', 
								iconCls : 'icon-cancel',
								handler : function() {
//									var d = $(this).closest('.window-body');
									var d = $("#"+dialogId);
									d.dialog('close');
								}
							} ],
							onClose : function() {
								$(this).dialog('destroy');
							}
						});
					},
					
					
					/**
					 * 修改更新保存成功后
					 */
					onUpdateSuccess : function(dialog, datagrid, result, id, reload,isTreeGrid) {
						
						if(!$('#' + datagrid)) {
							return;
						}
						if(isTreeGrid) {
							$('#' + datagrid).treegrid('uncheckAll').treegrid('unselectAll').treegrid('clearSelections');
						} else {
							$('#' + datagrid).datagrid('uncheckAll').datagrid('unselectAll').datagrid('clearSelections');
						}
						
						if(reload){
							if(isTreeGrid) {
								$('#' + datagrid).treegrid('reload');
							} else {
								$('#' + datagrid).datagrid('reload');
							}							
						}else{
							
							if(isTreeGrid) {
								//树形grid
								if(result.code == "1") {
									result.entity.state = 'open';
									//新增
									if(!id) {
										//插入根节点
										$('#' + datagrid).treegrid('append',{
											data: [result.entity]
										});
									} else {
										$('#' + datagrid).treegrid('append',{
											parent: id,
											data: [result.entity]
										});
									}									
									
								} else {
									//修改
									//判断是否有子节点
									var row = $('#' + datagrid).treegrid('find',id);
									if(row && row.state) {
										result.entity.state = row.state;
									} 
									$('#' + datagrid).treegrid('update', {
										id:id,
										row : result.entity
									});	
								}
							} else {
								//列表grid
								$('#' + datagrid).datagrid('updateRow', {
									index : $('#' + datagrid).datagrid('getRowIndex', id),
									row : result.entity
								});	
							}	
							
						}
						dialog.dialog('close');
					},
					
					/**
					 * searchData
					 * 
					 * @param searchDataForm
					 * @param datagrid
					 * @param type 默认列表 1表示树列表
					 */
					searchData : function(searchForm, grid,type,url) {
						var queryParams = serializeObject($('#' + searchForm));
						if(isEmptyObject(queryParams)){
							queryParams = serializeObjectFromContainer($('#' + searchForm));
						}
						//验证输入框是否通过验证
						var validateStatus = $('#'+searchForm).form('validate');
						if(!validateStatus) {
							return;
						}
						if(type == 1) {
							$('#' + grid).treegrid('uncheckAll').treegrid('unselectAll').treegrid('clearSelections');
							if(url) {
								$('#' + grid).treegrid({
									url:url,
									queryParams:queryParams
								});
							} else {
								$('#' + grid).treegrid('reload', queryParams);
							}							
						} else {
							$('#' + grid).datagrid('uncheckAll').datagrid('unselectAll').datagrid('clearSelections');
							if(url) {
								$('#' + grid).datagrid({
									url:url,
									queryParams:queryParams
								});
							} else {
								$('#' + grid).datagrid('reload', queryParams);
							}		
							
						}
						
					},				
					
					
					/**
					 * 按键回车直接执行查询
					 * 
					 * @param searchForm
					 * @param datagrid
					 */
					registerKeydown : function(searchForm, grid) {
						
						var form = $('#' + searchForm);
						$('input', form).keydown(function(event) {
							if (event.keyCode == 13) {
								$.acooly.framework.searchData(searchForm, grid);
							}
						});
					},	

					/**
					 * 查詢并導出數據
					 * 
					 * @param url
					 * @param searchForm
					 * @param fileName
					 */
					exports : function(url, searchForm, fileName,confirmTitle,confirmMessage) {
						var title = confirmTitle?confirmTitle:"导出";
						var message = confirmMessage?confirmMessage:"您确定以当前查询条件导出所有数据？";
						$.messager.confirm(title , message, function(r) {
							if(!r){
								return;
							}
							var queryParams = serializeObject($('#'+ searchForm));
							$(queryParams).attr('exportFileName',fileName);
							$.acooly.framework.createAndSubmitForm(url,queryParams);
						});
					},		
					
					/**
					 * 动态创建表单和提交表单
					 */
					createAndSubmitForm : function(url,inputObjects){
						var form = $('<form action="' + contextPath + url + '" method="POST"></form>');
						for ( var key in inputObjects) {
							if (inputObjects[key] != '') {
								var inputForm = $('<input type="hidden" name="' + key + '" value="' + inputObjects[key] + '" />');
								form.append(inputForm);
							}
						}
						form.appendTo($('body')).submit();
						form.remove();					
					},
					
					/**
					 * createDialog
					 */
					createDialog : function(url, width, height, title, entity, successCallback, validateCallback) {
						var dialogId = $.acooly.framework.getDivId();
						$('<div id="'+dialogId+'"></div>').dialog({
							title : title,
							href :  contextPath + url,
							width : width,
							height : height,
							modal : true,
							buttons : [ {
								text : '保存',
								iconCls : 'icon-add',
								handler : function() {
//									var d = $(this).closest('.window-body');
									var d = $("#"+dialogId);
									var btnObj = this;
									
									$("#" + entity.formName).ajaxSubmit({
										dataType:"json",
										beforeSubmit:function(formData, jqForm, options){
											var returnResult = jqForm.form('validate');
											if(returnResult) {
												$(btnObj).linkbutton('disable');
												if($.isFunction(validateCallback)) {
													returnResult = validateCallback(formData);
													$(btnObj).linkbutton((returnResult) ? 'disable' : 'enable');
												}  
												return returnResult;
											} else {
												return returnResult;
											}	
										},
										success:function(result, statusText){
											try {
												if (result.message) {
													$.messager.alert({title : '提示', msg : result.message });
												}
												if (result.success) {
													//如果有回调函数则处理回调函数
													if(successCallback && successCallback != null){
														successCallback.call(d,result);
													}
												} else {
													$(btnObj).linkbutton('enable');
												}
											} catch (e) {
												$(btnObj).linkbutton('enable');
												$.messager.alert('提示', e, "error");
											}
											d.dialog('destroy');
										},
										error : function() {
											$(btnObj).linkbutton('enable');
											$.messager.alert("错误","操作失败！", "error");
										}
									});	
								}
							},{
								text : '关闭', 
								iconCls : 'icon-cancel',
								handler : function() {
//									var d = $(this).closest('.window-body');
									var d = $("#"+dialogId);
									d.dialog('destroy');
								}
							} ],
							onClose : function() {
								$(this).dialog('destroy');
							}
						});
					},
					
					/**
					 * createWindow
					 */
					createWindow : function(url, width, height, title, obj, successCallback) {
						var dialogId = $.acooly.framework.getDivId();
						$('<div id="'+dialogId+'"></div>').dialog({
							title : title,
							href :  url,
							width : width,
							height : height,
							modal : true,
							buttons : [{
								text : '关闭', 
								iconCls : 'icon-cancel',
								handler : function() {
//									var d = $(this).closest('.window-body');
									var d = $("#"+dialogId);
									//如果有回调函数则处理回调函数
									if(successCallback && successCallback != null){
										successCallback.call(d, obj);
									}
									d.dialog('destroy');
								}
							} ],
							onClose : function() {
								$(this).dialog('destroy');
							}
						});
					},
					
					/**
					 * show
					 */
					show : function(url, width, height, grid) {
						var $grid = null;
						if(grid && grid!="" && grid.length > 0) {
							$grid = $("#manage_" + grid + "_datagrid");
						}
						var dialogId = $.acooly.framework.getDivId();
						$('<div id="'+dialogId+'"></div>').dialog({
							href : contextPath + url,
							width : width,
							height : height,
							modal : true,
							title : '查看',
							buttons : [ {
								text : '关闭',
								iconCls : 'icon-cancel',
								handler : function() {
//									var d = $(this).closest('.window-body');
									var d = $("#"+dialogId);
									d.dialog('close');
									if($grid && $grid != null) {
										$grid.datagrid('uncheckAll').datagrid('unselectAll').datagrid('clearSelections');
									}
								}
							} ],
							onClose : function() {
								$(this).dialog('destroy');
								if($grid && $grid != null) {
									$grid.datagrid('uncheckAll').datagrid('unselectAll').datagrid('clearSelections');
								}
							}
						});
					},
					
					/**
					 * 提交通过ID方式修改记录各种状态的submit。支持删除，移动，状态变更等
					 */
					confirmSubmit : function(url, id, datagrid, confirmTitle,
							confirmMessage) {
						var title = confirmTitle ? confirmTitle : '确定';
						var message = confirmMessage ? confirmMessage
								: '您是否要提交该操作？';
						$.messager.confirm(title, message, function(r) {
							if (r) {
								$.ajax({
									url : contextPath + url,
									data : {
										id : id
									},
									dataType : 'json',
									success : function(result) {										
										if (result.success) {
											$('#' + datagrid).datagrid(
													'uncheckAll').datagrid(
													'unselectAll').datagrid(
													'clearSelections');
												$('#' + datagrid).datagrid('reload');
										}
										if (result.message) {
											$.messager.alert({
												title : '提示',
												msg : result.message
											});
										}
									},
									error:function() {
										$.messager.alert('错误',"服务器发生错误,请稍后重试！","error");
									}
								});
							}
						});
					},

					
					confirmRequest : function(url, requestData, datagrid, confirmTitle,
							confirmMessage) {
						var title = confirmTitle ? confirmTitle : '确定';
						var message = confirmMessage ? confirmMessage
								: '您是否要提交该操作？';
						$.messager.confirm(title, message, function(r) {
							if (r) {
								$.ajax({
									url : contextPath + url,
									data : requestData,
									success : function(result) {
										if (result.success) {
											$('#' + datagrid).datagrid('reload');
											$('#' + datagrid).datagrid(
													'uncheckAll').datagrid(
													'unselectAll').datagrid(
													'clearSelections');
										}
										if (result.message) {
											$.messager.alert({
												title : '提示',
												msg : result.message
											});
										}
									},
									error:function() {
										$.messager.alert('错误',"服务器发生错误,请稍后重试！","error");
									}
								});
							}
						});
					},					
					
					/**
					 * 排序移动，支持置顶和上移 confirmTitle,confirmMessage 为可选
					 */
					move : function(url, id, datagrid, confirmTitle,
							confirmMessage) {
						$.acooly.framework.confirmSubmit(url, id, datagrid,
								confirmTitle, confirmMessage);
					},

                    /**
                     * 批量处理数据
                     *
                     * @param url
                     * @param datagrid
                     */
                    domany : function(url, datagrid, confirmTitle,
                                       confirmMessage,tag) {
                        var rows = $('#' + datagrid).datagrid('getChecked');
                        var ids = [];
                        if (rows.length > 0) {
                            ids.push(tag);
                            for ( var i = 0; i < rows.length; i++) {
                                ids.push(rows[i].id);
                            }
                            $.acooly.framework.remove(url, ids.join(','),
                                datagrid, confirmTitle, confirmMessage);
                        } else {
                            $.messager.alert({
                                title : '提示',
                                msg : '请勾选要'+confirmTitle+'的记录！'
                            });
                        }
                    },

                    /**
                     * 选中列表中的一条数据进行数据处理
                     *
                     * @param url
                     * @param datagrid
                     * @param confirmTitle
                     * @param confirmMessage
                     */
                    doone : function(url, datagrid, confirmTitle,
                                       confirmMessage,id) {
                    	if(!id) {
                    		 var rows = $('#' + datagrid).datagrid('getChecked');
                    		 if (rows.length == 1) {
                                 id = rows[0].id;                                
                             } else {
                                 $.messager.alert({
                                     title : '提示',
                                     msg : '必须勾选1条要'+confirmTitle+'的记录！'
                                 });
                                 return;
                             }
                    	}
                    	 $.acooly.framework.confirmSubmit(url, id,
                                 datagrid, confirmTitle, confirmMessage);
                       
                       
                    },
                    /**
					 * 删除 置顶ID，多个ID可以使用逗号分割
					 */
                    usableRow : function(url, requestData, datagrid, confirmTitle,
							confirmMessage) {
						$.acooly.framework.confirmRequest(url, requestData, datagrid,
								confirmTitle, confirmMessage);
					},
					/**
					 * 删除 置顶ID，多个ID可以使用逗号分割
					 */
					remove : function(url, ids, datagrid, confirmTitle,
							confirmMessage) {
						$.acooly.framework.confirmSubmit(url, ids, datagrid,
								confirmTitle, confirmMessage);
					},
					/**
					 * 新增 树节点
					 */
					createTreeNode : function(opts) {
						var dialogId = $.acooly.framework.getDivId();
						$('<div id="'+dialogId+'"></div>').dialog({
							href : contextPath + opts.url,
							title : (opts.title ? opts.title : '添加'),
							top: (opts.top ? opts.top : null),
							width : (opts.width ? opts.width : 500), 
							height: (opts.height ? opts.height : 'auto'),
							modal : true, iconCls:'icon-save',
							buttons : [ {
								text : '增加', 
								iconCls : 'icon-add',
								handler : function() {
//									var d = $(this).closest('.window-body');
									var d = $("#"+dialogId);
									$("#" + opts.editform).ajaxSubmit({
										dataType:"json",
										beforeSerialize:function(a,b) {
                                            if(opts.beforeSerializeCallback && !opts.beforeSerializeCallback.call(this,arguments)){
                                                return false;
                                            }
                                        },
										beforeSubmit:function(formData, jqForm, options){
											return jqForm.form('validate'); 											
										},
										success:function(result, statusText){
											try {
												if (result.success) {
													var node = result.entity;
													node.iconCls = "icon-blank";
													$("#" + opts.treegrid).treegrid("reload");
													d.dialog('destroy');
												}
												if (result.message) {
													$.messager.alert({
														title : '提示',
														msg : result.message
													});
												}
											} catch (e) {
												$.messager.alert('提示', e + " DATA: "+ result.toString());
											}											
										},
										error:function() {
											$.messager.alert('错误',"服务器发生错误,请稍后重试！","error");
										}
									});	
								}
							},{
								text : '关闭', 
								iconCls : 'icon-cancel',
								handler : function() {
//									var d = $(this).closest('.window-body');
									var d = $("#"+dialogId);
									d.dialog('close');
								}
							} ],
							onClose : function() {
								$(this).dialog('destroy');
							}
						});
					},
					/**
					 * 编辑 树节点
					 */
					editTreeNode : function(opts) {
						var dialogId = $.acooly.framework.getDivId();
						$('<div id="'+dialogId+'"></div>').dialog({
							href : contextPath + opts.url,
							title : (opts.title ? opts.title : '编辑'),
							top: (opts.top ? opts.top : null),
							width : (opts.width ? opts.width : 500), 
							height: (opts.height ? opts.height : 'auto'),
							modal : true, iconCls:'icon-save',
							buttons : [ {
								text : '修改', 
								iconCls : 'icon-add',
								handler : function() {
//									var d = $(this).closest('.window-body');
									var d = $("#"+dialogId);
									$("#" + opts.editform).ajaxSubmit({
										dataType:"json",
										beforeSerialize:function(a,b) {
                                            if(opts.beforeSerializeCallback && !opts.beforeSerializeCallback.call(this,arguments)){
                                                return false;
                                            }
                                        },
										beforeSubmit:function(formData, jqForm, options){
											return jqForm.form('validate'); 											
										},
										success:function(result, statusText){
											try {
												if (result.success) {
													var node = result.entity;
													node.iconCls = "icon-blank";
													$("#" + opts.treegrid).treegrid("reload");
													d.dialog('destroy');
												}
												if (result.message) {
													$.messager.alert({
														title : '提示',
														msg : result.message
													});
												}
											} catch (e) {
												$.messager.alert('提示', e + " DATA: "+ result.toString());
											}											
										},
										error:function() {
											$.messager.alert('错误',"服务器发生错误,请稍后重试！","error");
										}
									});	
								}
							},{
								text : '关闭', 
								iconCls : 'icon-cancel',
								handler : function() {
//									var d = $(this).closest('.window-body');
									d = $("#"+dialogId);
									d.dialog('close');
								}
							} ],
							onClose : function() {
								$(this).dialog('destroy');
							}
						});
					},
					/**
					 * 删除 树节点
					 */
					removeTreeNode : function(url,idField,treegrid, submitCallback){
						var title =  '确定';
						var message =  '您是否要提交该操作？';
						var id = "";
						var checkedRow = $("#"+treegrid).treegrid("getChecked");
						if(checkedRow && checkedRow.length > 0) {
							id = checkedRow[0][idField];
						} else {
							$.messager.alert({
								title : '提示',
								msg : "请至少选中一行记录"
							});
							return;
						}	
						$.messager.confirm(title, message, function(r) {
							if (r) {
								$.ajax({
									url : contextPath + url,
									data : {
										id : id
									},
									dataType:"json",
									success : function(result) {
										if (result.success) {
											$('#' + treegrid).treegrid(
													'uncheckAll').treegrid(
													'unselectAll').treegrid(
													'clearSelections');
											if(submitCallback) {
												submitCallback.call(this, id);
											}											
											//移除页面删除项
											$("#"+treegrid).treegrid('remove',id);
											
										}
										if (result.message) {
											$.messager.alert({
												title : '提示',
												msg : result.message
											});
										}
									},
									error:function() {
										$.messager.alert('错误',"服务器发生错误,请稍后重试！","error");
									}
								});
							}
						});
					},
					
					/**
					 * 根据单个属性，批量刪除
					 * 
					 * @param url
					 * @param datagrid
					 */
					removes : function(url,idField,datagrid, confirmTitle,
							confirmMessage) {
						var rows = $('#' + datagrid).datagrid('getChecked');
						var ids = [];
						if (rows.length > 0) {
							for ( var i = 0; i < rows.length; i++) {
								ids.push(rows[i][idField]);
							}
							$.acooly.framework.remove(url, ids.join(','),
									datagrid, confirmTitle, confirmMessage);
						} else {
							$.messager.alert({
								title : '提示',
								msg : '请勾选要删除的记录！'
							});
						}
					},
					/**
					 * 根据多个属性，批量刪除
					 * 
					 * @param url
					 * @param datagrid
					 */
					mulParamRemoves : function(url,paramFields,datagrid, confirmTitle,
							confirmMessage) {
						var rows = $('#' + datagrid).datagrid('getChecked');
						var ids = [];
						var params = paramFields.split(",");
						var jsonStr = "";
						if (rows.length > 0) {
							
							for ( var i = 0; i < rows.length; i++) {
								jsonStr = "{";
								for(var j = 0; j < params.length; j++){
									jsonStr = jsonStr + "'" + params[j] + "':" + "'" + rows[i][params[j]] + "'";
									if(j < (params.length - 1)){
										jsonStr = jsonStr +',';
									}
								}
								jsonStr = jsonStr + "}";
								ids.push(jsonStr);
							}
							$.acooly.framework.remove(url, '['+ids.join(',')+']',
									datagrid, confirmTitle, confirmMessage);
						} else {
							$.messager.alert({
								icon : 'warning',
								title : '提示',
								msg : '请勾选要删除的记录！'
							});
						}
					},
					logout : function(){
						$.messager.confirm('提醒','确定要注销退出吗？',function(r){  
						    if (r){  
						        window.location.href=contextPath+'/manage/logout.html';
						    }  
						});
					},
					changePassword : function(url) {
						var dialogId = $.acooly.framework.getDivId();
						$('<div id="'+dialogId+'"></div>').dialog({
							href : contextPath+url,
							width : 400,
							height : 230,
							modal : true,
							title : '修改密码',
							buttons : [ {
								text : '修改密码',
								iconCls : 'icon-edit',
								handler : function() {
//									var d = $(this).closest('.window-body');
									var d = $("#"+dialogId);
									$('#user_changePassword_form').form('submit', {
										onSubmit:function(){
									    	var isValid = $(this).form('validate');
									    	if(!isValid){
									    		return false;
									    	}
									    	//自定义检查合法性
											if($('#system_newPassword').val() != $('#system_confirmNewPassword').val()){
												$.messager.alert('提示', '两次密码输入不一致');
												return false;
											}
											return true;						
										},
										success : function(data) {
											try {
												var result = $.parseJSON(data);
												if (result.success) {
													d.dialog('destroy');
												}
												if(result.message){
													$.messager.alert({title : '提示',msg : result.message});	
												}
											} catch (e) {
												$.messager.alert('提示', '修改密码失败！');
											}
										},
										error:function() {
											$.messager.alert('错误',"服务器发生错误,请稍后重试！","error");
										}
									});
								}
							} ],
							onClose : function() {
								$(this).dialog('destroy');
							},
							onLoad : function() {
								
							}
						});
					},
					/**
					 * 在数据列表中添加一行数据
					 * @param id
					 * @param dataOpts
					 */
					appendRow:function(id,dataOpts){
						if(!dataOpts) {
							dataOpts = {};
						}
		                $('#'+id).datagrid('insertRow',{
		                	index:0,
		                	row:dataOpts
		                });
		                $('#'+id).datagrid('selectRow', 0)
		                        .datagrid('beginEdit', 0);
			        },
			        /**
					 * 在数据列表中编辑选中的数据
					 * @param id
					 * @param dataOpts
					 */
					editSelectedRow:function(id){						
						var selectedRow = $('#'+id).datagrid('getSelected');
						if(!selectedRow){
							$.messager.alert('提示', '请选择一行记录！');
							return;
						}
						var editIndex = $('#'+id).datagrid('getRowIndex',selectedRow);
		                $('#'+id).datagrid('selectRow', editIndex)
		                        .datagrid('beginEdit', editIndex);
			        },
			        /**
			         * 选中列表中一行数据时，触发
			         * @param index 选中的行数
			         * @param row 选中的行数据
			         * @param opt 附加数据
			         * {
			         * 	id 列表id
			         *  isUpdate 是否提交 默认不提交 1提交
			         * 	url:提交url
			         *  method:get/post
			         * }
			         */
			        updateEditRows:function(index,row,opt) {
			        	if(!opt) return;
			        	var $dg = $('#'+opt.id);
			        	var rows = $dg.datagrid('getRows');
                    	for ( var i = 0; i < rows.length; i++) {
                    		$dg.datagrid("endEdit", i);
                    	}
			        	if(opt.isUpdate != 1) {			        		
			        		return;
			        	} else {
			        		//保存更新的数据
			        		$.acooly.framework.saveEditRows(opt);
			        	}			        	
			        },
			        /**
			         * 保存编辑列表数据
			         * @param opt
			         * {
			         * 	id 列表id
			         *  url 请求的url
			         *  method get/post
			         * }
			         */
			        saveEditRows:function(opt) {
			        	debugger;
			        	if(!opt) return;
			        	//是否需要检查编辑框是否编辑完成
			        	if(!opt.isEndEdit || opt.isEndEdit != 1) {
			        		var $dg = $('#'+opt.id);
				        	var rows = $dg.datagrid('getRows');
	                    	for ( var i = 0; i < rows.length; i++) {
	                    		$dg.datagrid("endEdit", i);
	                    	}
			        	}
			        	if(!opt.params || opt.params.length < 1) return;
			        	var rows = $('#'+opt.id).datagrid('getChanges');
			        	if(!rows || rows.length < 1) return;
			        	var data = {};
			        	var isEmpty = true;
			        	$.each(rows, function(i,item){
			        		isEmpty = true;
			        		$.each(opt.params, function(i,param) {
			        			if(!data[param]) {
				        			   data[param] = new Array();			        			   
				        		}
			        			
			        			if(item[param]) {
			        				data[param].push(item[param]);
			        				isEmpty = false;
			        			} else {
			        				data[param].push(0);
			        			}			        		   
			        		});	
			        		
			        		if(isEmpty) {
			        			var indexRow =  $('#'+opt.id).datagrid('getRowIndex',item);
			        			$('#'+opt.id).datagrid('deleteRow',indexRow);
			        		}
			        		
			        		//设置附加参数
				        	if(opt.optParam && opt.optParam.length >= 1) {
				        		$.each(opt.optParam, function(i,param) {
				        			if(item[param]) {
				        				data[param] = item[param];
				        			} 		        
				        		});
				        	}
				        	
			        	});				        	
			        	
			        	
			        	$.ajax({
							url : contextPath + opt.url,
							traditional:true,
							data : data,
							type:"post",
							dataType:"json",
							success : function(result) {	
								 try {
										if (result.success) {
											//如果有回调函数则处理回调函数
											if(opt.successCallback) {
												opt.successCallback.call(result);
											}	
											 $('#'+opt.id).datagrid('reload');
										} else {
											if (result.message) {
													$.messager.alert({
													title : '提示',
													msg : result.message
												});
											}
											$('#'+opt.id).datagrid('reload');
										}										
										
									} catch (e) {
										$.messager.alert('提示', e);
									}			
							},
							error:function() {
								$.messager.alert('错误',"服务器发生错误,请稍后重试！","error");
							}
						});
			        },
			        /**
			         * 创建一个列表对话框
			         * @param opt
			         * {
			         * url, width, height, title,btnName,listId,valIdField,valId:选中行的id,contentId,contentIdField,successCallback, sumbitCallback,sumbitUrl,istree
			         * }
			         */
			        createTableDialog : function(opt) {
			        	if(!opt) return;
			        	var id = "";
			        	var queryParam = {};
			        	if(!opt.valId) {
			        		var selectedRow;
			        		if(opt.istree) {
			        			selectedRow = $('#'+opt.listId).treegrid('getSelected');
			        		} else {
			        			selectedRow = $('#'+opt.listId).datagrid('getSelected');
			        		}
			        		
				        	if(!selectedRow) {
				        		$.messager.alert({
									title : '提示',
									msg : '至少勾选一条记录！'
								});
				        		return
				        	}
				        	id = selectedRow[opt.valIdField];
			        	} else {
			        		id = opt.valId;
			        	}		        	
						var dialogId = $.acooly.framework.getDivId();	
						queryParam[opt.valIdField] = id;						
						var href = $.acooly.framework._getCanonicalUrlByParam(opt.url,queryParam);
						$('<div id="'+dialogId+'"></div>').dialog({
							title : opt.title,
							href :  href,
							width : opt.width,
							height : opt.height,
							modal : true,
							buttons : [ {
								text : opt.btnName,
								iconCls : 'icon-save',
								handler : function() {
									var d = $("#"+dialogId);									
									var contentId = "";
									if(!opt.sumbitCallback) {
										var rows;
										if(opt.istree) {
											rows = $('#' + opt.contentId).treegrid('getChecked');
										} else {
											rows = $('#' + opt.contentId).datagrid('getChecked');
										}
										
										var ids = [];
										if (rows.length > 0) {
											for ( var i = 0; i < rows.length; i++) {
												ids.push(rows[i][opt.contentIdField]);
											}	
											contentId = ids.join(',');
										} else {
											/*$.messager.alert({
												title : '提示',
												msg : '必须勾选一条记录！'
											});*/
											
											contentId="";
											
										}
									} else {
										contentId = opt.sumbitCallback(opt.contentId);
									}
									//提交请求
									$.ajax({
										url : contextPath + opt.sumbitUrl,
										data : {id:id,contentId:contentId},								
										dataType:"json",
										type:"post",
										success : function(result) {
											if(opt.successCallback) {
												opt.successCallback.call(result);
											}
											if(!result.success) {
												$.messager.alert('提示',result.message);
											}
											d.dialog('destroy');
										},
										error:function() {
											$.messager.alert('错误',"服务器发生错误,请稍后重试！","error");
										}
									});
									
								}
							},{
								text : '关闭', 
								iconCls : 'icon-cancel',
								handler : function() {
									var d = $("#"+dialogId);
									d.dialog('destroy');
								}
							} ],
							onClose : function() {
								$(this).dialog('destroy');
							}
						});
					}
				}
			});
})(jQuery);
