
$(function() {
	$('#unit_west_tree').tree({
			url : '../organization/selectTreeOrganizationInfo',
			parentField : 'id',	
			lines : true,
			/*checkbox:true,*/
			onClick : function(node) {
				selected(node.id);
			},
			loadFilter:function(data,parent) {	
				setDTreeText(data);
				return data;
			}
		
		});
	});

/**
 * 设置树节点被选中
 * @param id
 */
function selected(id){
		$("#unit_west_tree .dept").removeClass("tree-checkbox1");
		$("#deptCheckBox" + id).addClass("tree-checkbox1");
		var node = $('#unit_west_tree').tree('find', id);
		$('#unit_west_tree').tree('select', node.target);
		$('#manage_department_datagrid').datagrid({
			url:"../department/selectDepartmentByPage",
			queryParams:{"orgCode":node.idPath}	
		});
} 

/**
 * 设置树节点显示文本格式
 * @param nodes 树节点
 */
function setDTreeText(nodes) {
	if(nodes && nodes.length > 0) {
		$.each(nodes,function(i,node){
				var nodeId = node.id;
				node.showText = node.text;
				node.text="<span id='deptCheckBox" +nodeId+ "'class='dept tree-checkbox tree-checkbox0' onclick=\"selected('" +nodeId+ "')\"/>" + node.text;
				setDTreeText(node.children)
		});		
	}
}

/**
 * 判断是否选中机构，true选中 false未选中
 */
function isSelectOrg() {
	var selectRow = $('#unit_west_tree').tree("getSelected");
	return selectRow;
}
/**
 * 添加部门信息
 */
function addDept() {
	var selectRow = isSelectOrg();
	if(!selectRow || selectRow.length < 1) {
		$.messager.alert('提示', '请先选中单位');
		return;
	}
	var url = '../department/create.html?orgName='+encodeURI(encodeURI(selectRow.showText))+'&orgCode='+selectRow.idPath;
	$.acooly.framework.customDialog({reload:true,url:url,entity:'department',width:560,height:250,title:'新增'},{buttonName:'确定'});
}

/**
 * 修改部门信息
 */
function editDept() {
	var selectRow = isSelectOrg();
	if(!selectRow || selectRow.length < 1) {
		$.messager.alert('提示', '请先选中单位');
		return;
	}
	var url = '../department/edit.html?orgName='+encodeURI(encodeURI(selectRow.showText))+'&orgCode='+selectRow.id;
	$.acooly.framework.customDialog({reload:true,url:url,entity:'department',width:560,height:250},{buttonName:'确定',isCheckselect:true,param:'deptCode'});
}

/**
 * 判断是否选中机构，true选中 false未选中
 */
function isSelectOrg() {
	var selectRow = $('#unit_west_tree').tree("getSelected");
	return selectRow;
}
/**
 * 添加部门信息
 */
function addDept() {
	var selectRow = isSelectOrg();
	if(!selectRow || selectRow.length < 1) {
		$.messager.alert('提示', '请先选中单位');
		return;
	}
	var url = '../department/create.html?orgName='+encodeURI(encodeURI(selectRow.showText))+'&orgCode='+selectRow.idPath;
	$.acooly.framework.customDialog({reload:true,url:url,entity:'department',width:500,height:220,title:'新增'},{buttonName:'确定'});
}

/**
 * 修改部门信息
 */
function editDept() {
	var selectRow = isSelectOrg();
	if(!selectRow || selectRow.length < 1) {
		$.messager.alert('提示', '请先选中单位');
		return;
	}
	var url = '../department/edit.html?orgName='+encodeURI(encodeURI(selectRow.showText))+'&orgCode='+selectRow.id;
	$.acooly.framework.customDialog({reload:true,url:url,entity:'department',width:500,height:220},{buttonName:'确定',isCheckselect:true,param:'deptCode'});
}
