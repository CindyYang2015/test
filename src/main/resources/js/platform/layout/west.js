$(function() {
		$.ajax({
			url : "../resources/selectAllResourcesMapForIndex",
			data : {},
			dataType : 'json',
			cache : false,
			success : function(result) {
				$.each(result, function(i,node){
				     if(!node.resourceUrl) {
				    	 var strul = '<ul>';
				    	 if(node.children){
				    		 $.each(node.children, function(i,child){   
				    			 if(child.enabled == 1) {
				    				 strul += '<li id="{pageId}"><div class="lirow" onclick="loadPage(\'{id}\',\'{text}\',\'{url}\',\'{iconCls}\');"><span><img src="..{imageUrl}" /></span>&nbsp;<span class="tree-title">{pageName}</span></span></li>';
				    				 strul = strul.replace(/{pageId}/g, child.resourceId);
				    				 if(!child.resourceImage) {
				    					 child.resourceImage = "/images/icons/business/menu.png";
				    				 }
				    				 var iconCls = child.resourceImage.replace("/images/icons/business/","");
				    				 iconCls = iconCls.replace(".png","");
				    				 iconCls = 'icon-'+iconCls.replace(/_/g,"-");
				    				 strul = strul.replace(/{imageUrl}/g, child.resourceImage);
				    				 strul = strul.replace(/{pageName}/g, child.resourceName);		
				    				 strul = strul.replace(/{id}/g, child.resourceId);	
				    				 strul = strul.replace(/{text}/g, child.resourceName);
				    				 strul = strul.replace(/{url}/g, child.resourceUrl);
				    				 strul = strul.replace(/{iconCls}/g, iconCls);
				    			 }
				    			 
				    		 }); 
				    	 }
				    	 strul += '</ul>';
				    	 var iconCls1 = node.resourceImage.replace("/images/icons/business/","");
				    	 iconCls1 = iconCls1.replace(".png","");
				    	 iconCls1 = 'icon-'+iconCls1.replace(/_/g,"-");
				    	 addPanel({
				    		 title:node.resourceName,
				    		 content:strul,
				    		 iconCls:iconCls1
				    	 });
				     }
				  });  
			},
			error:function() {
				$.messager.alert('错误',"服务器发生错误,请稍后重试！","error");
			}
		});	
});

function addPanel(node){
	//alert(node.iconCls);
	if(!node) return;
    $('#westmenuId').accordion('add',{
        title:node.title,
        content:node.content,
        iconCls:node.iconCls
    });   
}

//点击时，触发更换背景
function loadPage(id,text,url,iconCls) {
	$(".lirow").css("background","none");
	$(".lirow").css("color","#000000");
	$("#"+id).children("div").css("background","#7ebfef");
	$("#"+id).children("div").css("color","#ffffff");	
	layout_center_addTabFun({
        title : text,
        closable : true,
        iconCls : iconCls,
       // content : '<iframe src="' + '..'+url + '" frameborder="0" style="border:0;width:100%;height:99%;"></iframe>',
        href : ".."+url + "?resId=" + id,
        onDestroy :function(){
        	//获取打开的tabs个数
        	var tabsLength = $('#layout_center_tabs').tabs("tabs").length;
        	//获取左侧菜单页展开状态{true:未展开,false:展开}
        	var collapsed = $('#mainLayout').layout('panel','west').panel("options").collapsed;
        	//如果只有首页(tabsLength == 1)且左侧菜单未展开，则进行展开操作
        	if(tabsLength == 1 && collapsed){
        		$('#mainLayout').layout('expand','west');
        	}
        },
        onLoad:function(panel) {
        	//异步加载该菜单的按钮权限
        	$.ajax({
				url : "../resources/selectBtnListByResId",
				data : {resId:id},								
				dataType:"json",
				type:"post",
				success : function(result) {
					if(result.success && result.rows && result.rows.length > 0) {
						//初始化按钮权限
						$.each(result.rows,function(i,row){
							$("a[resId='"+row.resourceId+"']").css("display","");
						});
					}
				}
			});
        }
    });	
	
}