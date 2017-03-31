package com.cloudwalk.ibis.model.result.ver001;

/**
 * 身份证信息
 * @author zhuyf
 *
 */
public class IDCardData extends Data {
	
	private static final long serialVersionUID = 8829572328564504740L;
	
	//身份证号码
	private String ctfno;
	
	//姓名
	private String ctfname;		
		
	//地址
	private String address;
		
	//民族
	private String folk;
		
	//生日
	private String birthday;
	
	//性别
	private String sex;	
	
	//签发机关
	private String authority;
		
	//头像
	private String headImg;	
		
	//身份证有效期
	private String validdate;
	
	//身份证类型 1正面 0反面 2正反面
	private int type;

	public String getCtfno() {
		return ctfno;
	}

	public void setCtfno(String ctfno) {
		this.ctfno = ctfno;
	}

	public String getCtfname() {
		return ctfname;
	}

	public void setCtfname(String ctfname) {
		this.ctfname = ctfname;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getFolk() {
		return folk;
	}

	public void setFolk(String folk) {
		this.folk = folk;
	}

	public String getBirthday() {
		return birthday;
	}

	public void setBirthday(String birthday) {
		this.birthday = birthday;
	}

	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public String getAuthority() {
		return authority;
	}

	public void setAuthority(String authority) {
		this.authority = authority;
	}

	public String getHeadImg() {
		return headImg;
	}

	public void setHeadImg(String headImg) {
		this.headImg = headImg;
	}

	public String getValiddate() {
		return validdate;
	}

	public void setValiddate(String validdate) {
		this.validdate = validdate;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}
	

}
