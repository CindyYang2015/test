$(function() {
	$('#manage_userRole_datagrid').datagrid({  		  
		onLoadSuccess:function(data){			
			 if(data && data.rows && data.rows.length>0) {
				 $.each(data.rows,function(i,item){
					 if(item.groupLevel == 99) {
						 var rowindex = $("#manage_userRole_datagrid").datagrid('getRowIndex',item.groupId);
						 $("#manage_userRole_datagrid").datagrid('checkRow',rowindex);						 
					 }					 
				 });
			 }
			 
		   }		  
		}); 
});