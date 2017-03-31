package com.cloudwalk.common.engine.face.eoc;


/**
 * 身份证信息
 * @author zhuyf
 *
 */
public class IDCard extends EOCResult {
	
	private static final long serialVersionUID = -3464052541506908721L;

	//1表示 正面， 正面， 0表示 反面
	private int type;
	
	//身份证人脸信息
	private IDCardFace face;
	
	//身份证号码
	private String cardno;
	
	//姓名
	private String name;		
		
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
		
	//身份证开始日期
	private String validdate1;
	
	//身份证结束日期
	private String validdate2;

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public IDCardFace getFace() {
		return face;
	}

	public void setFace(IDCardFace face) {
		this.face = face;
	}

	public String getCardno() {
		return cardno;
	}

	public void setCardno(String cardno) {
		this.cardno = cardno;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
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

	public String getValiddate1() {
		return validdate1;
	}

	public void setValiddate1(String validdate1) {
		this.validdate1 = validdate1;
	}

	public String getValiddate2() {
		return validdate2;
	}

	public void setValiddate2(String validdate2) {
		this.validdate2 = validdate2;
	}	
		
}
