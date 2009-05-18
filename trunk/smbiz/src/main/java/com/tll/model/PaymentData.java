package com.tll.model;

import java.io.Serializable;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

import org.hibernate.validation.constraints.Length;


public class PaymentData implements Serializable {
	private static final long serialVersionUID = 4794589680528322269L;

	// bank related
	public static final int MAXLEN_BANK_ACCOUNT_NO = 64;
	public static final int MAXLEN_BANK_NAME = 128;
	public static final int MAXLEN_BANK_ROUTING_NUM = 32;

	// cc related
	public static final int MAXLEN_CC_NUM = 20;
	public static final int MAXLEN_CC_CVV2 = 8;
	public static final int MAXLEN_CC_NAME = 128;
	public static final int MAXLEN_CC_ADDRESS1 = 128;
	public static final int MAXLEN_CC_ADDRESS2 = 128;
	public static final int MAXLEN_CC_CITY = 128;
	public static final int MAXLEN_CC_STATE = 128;
	public static final int MAXLEN_CC_ZIP = 15;
	public static final int MAXLEN_CC_COUNTRY = 128;
	public static final int MAXLEN_CC_EXP_MONTH = 2;
	public static final int MAXLEN_CC_EXP_YEAR = 4;

	private String bankAccountNo;

	private String bankName;

	private String bankRoutingNo;

	private CreditCardType ccType;

	private String ccNum;

	private String ccCvv2;

	private int ccExpMonth;

	private int ccExpYear;

	private String ccName;

	private String ccAddress1;

	private String ccAddress2;

	private String ccCity;

	private String ccState;

	private String ccZip;

	private String ccCountry;

	public PaymentData() {
		super();
	}

	@Length(max=MAXLEN_CC_ADDRESS1)
	public String getCcAddress1() {
		return ccAddress1;
	}

	public void setCcAddress1(String ccAddress1) {
		this.ccAddress1 = ccAddress1;
	}

	@Length(max=MAXLEN_CC_ADDRESS2)
	public String getCcAddress2() {
		return ccAddress2;
	}

	public void setCcAddress2(String ccAddress2) {
		this.ccAddress2 = ccAddress2;
	}

	@Length(max=MAXLEN_CC_CITY)
	public String getCcCity() {
		return ccCity;
	}

	public void setCcCity(String ccCity) {
		this.ccCity = ccCity;
	}

	@Length(max=MAXLEN_CC_COUNTRY)
	public String getCcCountry() {
		return ccCountry;
	}

	public void setCcCountry(String ccCountry) {
		this.ccCountry = ccCountry;
	}

	@Length(max=MAXLEN_CC_STATE)
	public String getCcState() {
		return ccState;
	}

	public void setCcState(String ccState) {
		this.ccState = ccState;
	}

	@Length(max=MAXLEN_CC_ZIP)
	public String getCcZip() {
		return ccZip;
	}

	public void setCcZip(String ccZip) {
		this.ccZip = ccZip;
	}

	@Length(max=MAXLEN_BANK_ACCOUNT_NO)
	public String getBankAccountNo() {
		return bankAccountNo;
	}

	public void setBankAccountNo(String bankAccountNo) {
		this.bankAccountNo = bankAccountNo;
	}

	@Length(max=MAXLEN_BANK_NAME)
	public String getBankName() {
		return bankName;
	}

	public void setBankName(String bankName) {
		this.bankName = bankName;
	}

	@Length(max=MAXLEN_BANK_ROUTING_NUM)
	public String getBankRoutingNo() {
		return bankRoutingNo;
	}

	public void setBankRoutingNo(String bankRoutingNo) {
		this.bankRoutingNo = bankRoutingNo;
	}

	@Length(max=MAXLEN_CC_CVV2)
	public String getCcCvv2() {
		return ccCvv2;
	}

	public void setCcCvv2(String ccCvv2) {
		this.ccCvv2 = ccCvv2;
	}

	@Min(value = 1)
	@Max(value = 12)
	public int getCcExpMonth() {
		return ccExpMonth;
	}

	public void setCcExpMonth(int ccExpMonth) {
		this.ccExpMonth = ccExpMonth;
	}

	@Min(value = 2000)
	@Max(value = 2020)
	public int getCcExpYear() {
		return ccExpYear;
	}

	public void setCcExpYear(int ccExpYear) {
		this.ccExpYear = ccExpYear;
	}

	@Length(max=MAXLEN_CC_NAME)
	public String getCcName() {
		return ccName;
	}

	public void setCcName(String ccName) {
		this.ccName = ccName;
	}

	// @CreditCardNumber
	@Length(max = MAXLEN_CC_NUM)
	public String getCcNum() {
		return ccNum;
	}

	public void setCcNum(String ccNum) {
		this.ccNum = ccNum;
	}

	public CreditCardType getCcType() {
		return ccType;
	}

	public void setCcType(CreditCardType ccType) {
		this.ccType = ccType;
	}
}
