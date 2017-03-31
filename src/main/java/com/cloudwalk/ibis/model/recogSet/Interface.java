package com.cloudwalk.ibis.model.recogSet;

import java.io.Serializable;
import java.util.Date;


/**
 * 
 * ClassName: Interface 
 * Description: 接口信息表对应实体类
 * date: 2016年9月26日 下午5:59:33 
 *
 * @author 胡钰鑫
 * @version 
 * @since JDK 1.7
 */
public class Interface implements Serializable {
	/** 
	* @Fields serialVersionUID : 序列化唯一标识符
	*/ 
	private static final long serialVersionUID = -6275400398613319541L;
	/*
	 *主键
	 */
	private String id;
	/*
	 * 接口代码
	 */
	private String interfaceCode;
	/*
	 * 接口名称
	 */
	private String interfaceName;
	/*
	 * 状态：1.启用  0停用
	 */
	private Integer status;
	/*
	 * 备注
	 */
	private String remark;
	/*
	 * 创建人
	 */
	private String creator;
	/*
	 * 创建时间
	 */
	private Date createTime;
	/*
	 * 更新人
	 */
	private String updator;
	/*
	 * 更新时间
	 */
	private Date updateTime;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getInterfaceCode() {
		return interfaceCode;
	}
	public void setInterfaceCode(String interfaceCode) {
		this.interfaceCode = interfaceCode;
	}
	public String getInterfaceName() {
		return interfaceName;
	}
	public void setInterfaceName(String interfaceName) {
		this.interfaceName = interfaceName;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getCreator() {
		return creator;
	}
	public void setCreator(String creator) {
		this.creator = creator;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public String getUpdator() {
		return updator;
	}
	public void setUpdator(String updator) {
		this.updator = updator;
	}
	public Date getUpdateTime() {
		return updateTime;
	}
	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}
	
	
}
