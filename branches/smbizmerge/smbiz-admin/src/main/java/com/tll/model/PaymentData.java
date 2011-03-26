package com.tll.model;

import java.io.Serializable;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

import org.hibernate.validator.constraints.Length;

import com.tll.common.dto.PaymentDataProxy;

public class PaymentData implements Serializable, PaymentDataProxy {

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

	@Override
	@Length(max = MAXLEN_CC_ADDRESS1)
	public String getCcAddress1() {
		return ccAddress1;
	}

	@Override
	public void setCcAddress1(final String ccAddress1) {
		this.ccAddress1 = ccAddress1;
	}

	@Override
	@Length(max = MAXLEN_CC_ADDRESS2)
	public String getCcAddress2() {
		return ccAddress2;
	}

	@Override
	public void setCcAddress2(final String ccAddress2) {
		this.ccAddress2 = ccAddress2;
	}

	@Override
	@Length(max = MAXLEN_CC_CITY)
	public String getCcCity() {
		return ccCity;
	}

	@Override
	public void setCcCity(final String ccCity) {
		this.ccCity = ccCity;
	}

	@Override
	@Length(max = MAXLEN_CC_COUNTRY)
	public String getCcCountry() {
		return ccCountry;
	}

	@Override
	public void setCcCountry(final String ccCountry) {
		this.ccCountry = ccCountry;
	}

	@Override
	@Length(max = MAXLEN_CC_STATE)
	public String getCcState() {
		return ccState;
	}

	@Override
	public void setCcState(final String ccState) {
		this.ccState = ccState;
	}

	@Override
	@Length(max = MAXLEN_CC_ZIP)
	public String getCcZip() {
		return ccZip;
	}

	@Override
	public void setCcZip(final String ccZip) {
		this.ccZip = ccZip;
	}

	@Override
	@Length(max = MAXLEN_BANK_ACCOUNT_NO)
	public String getBankAccountNo() {
		return bankAccountNo;
	}

	@Override
	public void setBankAccountNo(final String bankAccountNo) {
		this.bankAccountNo = bankAccountNo;
	}

	@Override
	@Length(max = MAXLEN_BANK_NAME)
	public String getBankName() {
		return bankName;
	}

	@Override
	public void setBankName(final String bankName) {
		this.bankName = bankName;
	}

	@Override
	@Length(max = MAXLEN_BANK_ROUTING_NUM)
	public String getBankRoutingNo() {
		return bankRoutingNo;
	}

	@Override
	public void setBankRoutingNo(final String bankRoutingNo) {
		this.bankRoutingNo = bankRoutingNo;
	}

	@Override
	@Length(max = MAXLEN_CC_CVV2)
	public String getCcCvv2() {
		return ccCvv2;
	}

	@Override
	public void setCcCvv2(final String ccCvv2) {
		this.ccCvv2 = ccCvv2;
	}

	@Override
	@Min(value = 1)
	@Max(value = 12)
	public int getCcExpMonth() {
		return ccExpMonth;
	}

	@Override
	public void setCcExpMonth(final int ccExpMonth) {
		this.ccExpMonth = ccExpMonth;
	}

	@Override
	@Min(value = 2000)
	@Max(value = 2020)
	public int getCcExpYear() {
		return ccExpYear;
	}

	@Override
	public void setCcExpYear(final int ccExpYear) {
		this.ccExpYear = ccExpYear;
	}

	@Override
	@Length(max = MAXLEN_CC_NAME)
	public String getCcName() {
		return ccName;
	}

	@Override
	public void setCcName(final String ccName) {
		this.ccName = ccName;
	}

	// @CreditCardNumber
	@Override
	@Length(max = MAXLEN_CC_NUM)
	public String getCcNum() {
		return ccNum;
	}

	@Override
	public void setCcNum(final String ccNum) {
		this.ccNum = ccNum;
	}

	@Override
	public CreditCardType getCcType() {
		return ccType;
	}

	@Override
	public void setCcType(final CreditCardType ccType) {
		this.ccType = ccType;
	}
}
