/**
 * 检查文件类型
 * @param fileName
 * @returns {Boolean}
 */
function checkFileType(fileName){
	if(typeof(fileName) == "undefined" || fileName == null || fileName == ""){        
        $.messager.alert('提示',"请选择图片！");
        return false;
    } 
    if(!/\.(jpg|jpeg|png|JPG|PNG)$/.test(fileName))
    {        
        $.messager.alert('提示',"图片类型必须是.jpeg,jpg,png中的一种！");
        return false;
    }
    return true;
}

/**
 * 数字时间转字符串
 * @param nS
 * @returns
 */
function getLocalTime(nS) { 
	if(nS == null || nS == undefined){
		return '';
	}
	var date = new Date(nS);
	Y = date.getFullYear() + '-';
	M = (date.getMonth()+1 < 10 ? '0'+(date.getMonth()+1) : date.getMonth()+1) + '-';
	D = date.getDate() + ' ';
	h = date.getHours() + ':';
	m = (date.getMinutes() < 10 ? ('0'+date.getMinutes()) : date.getMinutes()) + ':';
	s = date.getSeconds() < 10 ? ('0'+date.getSeconds()) : date.getSeconds(); 
	return Y+M+D+h+m+s;     
}