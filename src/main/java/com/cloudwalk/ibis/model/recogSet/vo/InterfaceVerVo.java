package com.cloudwalk.ibis.model.recogSet.vo;

import java.io.Serializable;

import com.cloudwalk.ibis.model.recogSet.InterfaceVer;
/**
 * 
 * ClassName: InterfaceVer 
 * Description: 接口版本实体类.
 * date: 2016年9月27日 上午11:43:38 
 *
 * @author 胡钰鑫
 * @version 
 * @since JDK 1.7
 */
public class InterfaceVerVo extends InterfaceVer implements Serializable {

	private static final long serialVersionUID = -73992579847158021L;
	
	/*
	 * 接口名称 
	 */
	private String interfaceName;
	
	public String getInterfaceName() {
		return interfaceName;
	}
	public void setInterfaceName(String interfaceName) {
		this.interfaceName = interfaceName;
	}
	
}
