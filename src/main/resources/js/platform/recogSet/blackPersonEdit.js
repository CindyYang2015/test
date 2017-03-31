$(function() {	
	var id=$("#id").val();
	$.ajax({
		url : "../blackPerson/selectChannelDicValuesByDicType?dicType=6&id="+id,
		dataType:"json",
		type:"post",
		data:{},
		success : function(data) {
			var snciRes = data;
			if(!snciRes || snciRes.length < 1){
				$('#inter_tr10').append('<td align="left">无接口类型!<td>')
				return;
			}
			
			for (var i = 1; i < snciRes.length; i++) { 
				
				if(snciRes[i].checkbox){
					$('#inter_tr11')
					.append(
							'<input type="checkbox" name="tradingCodes" checked="checked"  value="'+snciRes[i].id+'"   />'
							+ snciRes[i].text
							+ '');
				}else{
					$('#inter_tr11')
					.append(
							'<input type="checkbox" name="tradingCodes" value="'+snciRes[i].id+'"   />'
							+ snciRes[i].text
							+ '');
				}
				
			}
		},
		error:function() {
			$.messager.alert('错误',"服务器发生错误,请稍后重试！","error");
		}
	});
	
	$.ajax({
		url : "../blackPerson/selectChannelDicValuesByDicType?dicType=3&id="+id,
		dataType:"json",
		type:"post",
		data:{},
		success : function(data) {
			var snciRes = data;
			if(!snciRes || snciRes.length < 1){
				$('#inter_tr10').append('<td align="left">无接口类型!<td>')
				return;
			}
			
			for (var i = 1; i < snciRes.length; i++) { 
				if(snciRes[i].checkbox){
					$('#inter_tr12')
					.append(
							'<input type="checkbox" name="channels" checked="checked" value="'+snciRes[i].id+'"   />'
							+ snciRes[i].text
							+ '');
				}else{
					$('#inter_tr12')
					.append(
							'<input type="checkbox" name="channels" value="'+snciRes[i].id+'"   />'
							+ snciRes[i].text
							+ '');
				}
				
			}
			
		},
		error:function() {
			$.messager.alert('错误',"服务器发生错误,请稍后重试！","error");
		}
	});
});
