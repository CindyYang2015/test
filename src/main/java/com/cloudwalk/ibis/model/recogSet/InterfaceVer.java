package com.cloudwalk.ibis.model.recogSet;

import java.io.Serializable;
import java.util.Date;
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
public class InterfaceVer implements Serializable {

	/** 
	* @Fields serialVersionUID : 序列化唯一标识符
	*/ 
	private static final long serialVersionUID = -5251294115326467241L;
	/*
	 * 主键
	 */
	private String id;
	/*
	 * 接口代码
	 */
	private String interfaceCode;
	/*
	 * 版本代码
	 */
	private String verCode;
	/*
	 * 版本号
	 */
	private String verNo;
	/*
	 * 状态 1:有效 0:无效
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
	public String getVerCode() {
		return verCode;
	}
	public void setVerCode(String verCode) {
		this.verCode = verCode;
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
	public String getVerNo() {
		return verNo;
	}
	public void setVerNo(String verNo) {
		this.verNo = verNo;
	}
	
}
