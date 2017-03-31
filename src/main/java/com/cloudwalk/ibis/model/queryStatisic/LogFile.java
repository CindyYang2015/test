package com.cloudwalk.ibis.model.queryStatisic;

import java.io.Serializable;

public class LogFile  implements Serializable{
	/**
	 * serialVersionUID:序列化ID,缓存需要  
	 */
    private static final long serialVersionUID = 4269416158052337256L;
    //文件名
	String fileName;
	//路径
	String path;
	//ip+端口
	String ipAndSort;
	//前端展示用的IP
	String ip;
	//前端展示用的端口
	String sort;
	
	public String getIp() {
		return ip;
	}
	public void setIp(String ip) {
		this.ip = ip;
	}
	public String getSort() {
		return sort;
	}
	public void setSort(String sort) {
		this.sort = sort;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	public String getPath() {
		return path;
	}
	public void setPath(String path) {
		this.path = path;
	}
	
	public String getIpAndSort() {
		return ipAndSort;
	}
	public void setIpAndSort(String ipAndSort) {
		this.ipAndSort = ipAndSort;
	}
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	
}
