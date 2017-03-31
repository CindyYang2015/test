package com.cloudwalk.ibis.model.result.ver001;

/**
 * 银行卡信息
 * @author zhuyf
 *
 */
public class BankCardData extends Data {
	
	private static final long serialVersionUID = -3725133374424632749L;
	
	/**
	 * 开户行名称
	 */
	private String bankName;
	
	/**
	 * 卡号
	 */
	private String cardNum;
	/**
	 * 卡类型
	 */
	private String CardType;
	/**
	 * 卡名称
	 */
	private String CardName;

	public String getCardType() {
		return CardType;
	}

	public void setCardType(String cardType) {
		CardType = cardType;
	}

	public String getBankName() {
		return bankName;
	}

	public void setBankName(String bankName) {
		this.bankName = bankName;
	}

	public String getCardNum() {
		return cardNum;
	}

	public void setCardNum(String cardNum) {
		this.cardNum = cardNum;
	}

	public String getCardName() {
		return CardName;
	}

	public void setCardName(String cardName) {
		CardName = cardName;
	}	

}
