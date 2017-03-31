$(function() {
	$('#manage_groupAuthority_datagrid').datagrid({  		  
		onLoadSuccess:function(data){			
			 if(data && data.rows && data.rows.length>0) {
				 $.each(data.rows,function(i,item){
					 if(item.issys == 99) {
						 var rowindex = $("#manage_groupAuthority_datagrid").datagrid('getRowIndex',item.authorityId);
						 $("#manage_groupAuthority_datagrid").datagrid('checkRow',rowindex);						 
					 }					 
				 });
			 }
			 
		   }		  
		}); 
});