package com.cloudwalk.ibis.model.queryStatisic;

import java.io.Serializable;

/**
 * 
 * Project Name: ibis-v1.2
 * File Name: ChannelFlowBank.java
 * Package Name:com.cloudwalk.ibis.model.queryStatisic
 * Description: 身份证OCR
 * @date : 2017年2月23日 下午2:13:42 
 * @author: 张宇超
 * @version: V1.0 
 * @since: 1.6
 * *******************************************************************************
 * 序号  修改时间  修改人  修改内容
 *  1
 *  2
 * *******************************************************************************
 * @Copyright: @ 2010-2016 重庆中科云丛科技有限公司  All Rights Reserved.
 */
public class ChannelFlowCard implements Serializable{
	
    /** 
	 * @Fields serialVersionUID : 序列化ID
	 */ 
	private static final long serialVersionUID = 4078122662571925381L;

	/**
	 * FLOW_ID 流水ID
	 */
    private String flowId;
    
    /**
     * FRONT_IMGPATH 身份证正面照路径
     */
    private String frontImgpath;
    
    /**
     * BLACK_IMGPATH 身份证反面照路径
     */
    private String blackImgpath;
    
    /**
     * IDCARD_NO 身份证号码
     */
    private String idcardNo;
    
    /**
     * ADDRESS 地址
     */
    private String address;
    
    /**
     * FOLK 民族
     */
    private String folk;
    
    /**
     * BIRTHDAY 生日
     */
    private String birthday;
    
    /**
     * VALIDDATE1 身份证开始有效期
     */
    private String validdate1;
    
    /**
     * VALIDDATE2 身份证结束有效期
     */
    private String validdate2;
    
    /**
     * AUTHORITY 签发机关
     */
    private String authority;
    
    /**
     * SEX 性别
     */
    private String sex;
    
    /**
     * FACE_IMGPATH 芯片图片地址
     */
    private String faceImgpath;
    
    /**
     * FLAG 正反面标志，1正面 0反面 2两面都存在
     */
    private Short flag = 2;
    
    /**
     * REMARK 备注
     */
    private String remark;

    /**
     * IDCARD_NAME 姓名
     */
    private String idcardName;

    public String getFlowId() {
        return flowId;
    }

    public void setFlowId(String flowId) {
        this.flowId = flowId == null ? null : flowId.trim();
    }

    public String getFrontImgpath() {
        return frontImgpath;
    }

    public void setFrontImgpath(String frontImgpath) {
        this.frontImgpath = frontImgpath == null ? null : frontImgpath.trim();
    }

    public String getBlackImgpath() {
        return blackImgpath;
    }

    public void setBlackImgpath(String blackImgpath) {
        this.blackImgpath = blackImgpath == null ? null : blackImgpath.trim();
    }

    public String getIdcardNo() {
        return idcardNo;
    }

    public void setIdcardNo(String idcardNo) {
        this.idcardNo = idcardNo == null ? null : idcardNo.trim();
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address == null ? null : address.trim();
    }

    public String getFolk() {
        return folk;
    }

    public void setFolk(String folk) {
        this.folk = folk == null ? null : folk.trim();
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday == null ? null : birthday.trim();
    }

    public String getValiddate1() {
        return validdate1;
    }

    public void setValiddate1(String validdate1) {
        this.validdate1 = validdate1 == null ? null : validdate1.trim();
    }

    public String getValiddate2() {
        return validdate2;
    }

    public void setValiddate2(String validdate2) {
        this.validdate2 = validdate2 == null ? null : validdate2.trim();
    }

    public String getAuthority() {
        return authority;
    }

    public void setAuthority(String authority) {
        this.authority = authority == null ? null : authority.trim();
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex == null ? null : sex.trim();
    }

    public String getFaceImgpath() {
        return faceImgpath;
    }

    public void setFaceImgpath(String faceImgpath) {
        this.faceImgpath = faceImgpath == null ? null : faceImgpath.trim();
    }

    public Short getFlag() {
        return flag;
    }

    public void setFlag(Short flag) {
        this.flag = flag;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark == null ? null : remark.trim();
    }

	public String getIdcardName() {
		return idcardName;
	}

	public void setIdcardName(String idcardName) {
		this.idcardName = idcardName;
	}

	@Override
	public String toString() {
		return "ChannelFlowCard [flowId=" + flowId + ", frontImgpath="
				+ frontImgpath + ", blackImgpath=" + blackImgpath
				+ ", idcardNo=" + idcardNo + ", address=" + address + ", folk="
				+ folk + ", birthday=" + birthday + ", validdate1="
				+ validdate1 + ", validdate2=" + validdate2 + ", authority="
				+ authority + ", sex=" + sex + ", faceImgpath=" + faceImgpath
				+ ", flag=" + flag + ", remark=" + remark + ", idcardName="
				+ idcardName + "]";
	}
    
}