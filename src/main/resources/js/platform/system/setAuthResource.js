$(function() {
	$('#manage_authResource_datagrid').treegrid({
		onClickRow:function(row){
			//级联选择
			$(this).treegrid('cascadeCheck',{
				id:row.resourceId, //节点ID
				deepCascade:true //深度级联
			});
		},		
		onLoadSuccess:function(row, data){		
			 if(data && data.rows && data.rows.length>0) {
				 $.each(data.rows,function(i,item){
					 if(item.issys == 99) {
						 $("#manage_authResource_datagrid").treegrid('select',item.resourceId);
					 }					 
				 });
			 }
			 
		   }		  
		}); 
});