package com.cloudwalk.common.engine.face.eoc;


/**
 * 银行卡信息
 * @author zhuyf
 *
 */
public class BankCard extends EOCResult {
	
	private static final long serialVersionUID = -7529666380078069844L;
	
	/**
	 * 开户行名称
	 */
	private String BankName;
	
	/**
	 * 卡号
	 */
	private String CardNum;
	/**
	 * 卡名
	 */
	private String CardName;
	/**
	 * 卡类型
	 */
	private String CardType;

	public String getBankName() {
		return BankName;
	}

	public void setBankName(String bankName) {
		BankName = bankName;
	}

	public String getCardNum() {
		return CardNum;
	}

	public void setCardNum(String cardNum) {
		CardNum = cardNum;
	}

	public String getCardName() {
		return CardName;
	}

	public void setCardName(String cardName) {
		CardName = cardName;
	}

	public String getCardType() {
		return CardType;
	}

	public void setCardType(String cardType) {
		CardType = cardType;
	}	
	
		
}
