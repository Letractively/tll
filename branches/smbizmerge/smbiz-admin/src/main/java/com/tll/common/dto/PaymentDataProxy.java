package com.tll.common.dto;

import com.google.web.bindery.requestfactory.shared.ProxyFor;
import com.google.web.bindery.requestfactory.shared.ValueProxy;
import com.tll.model.CreditCardType;
import com.tll.model.PaymentData;

/**
 * @author jpk
 */
@ProxyFor(PaymentData.class)
public interface PaymentDataProxy extends ValueProxy {

	String getCcAddress1();

	void setCcAddress1(final String ccAddress1);

	String getCcAddress2();

	void setCcAddress2(final String ccAddress2);

	String getCcCity();

	void setCcCity(final String ccCity);

	String getCcCountry();

	void setCcCountry(final String ccCountry);

	String getCcState();

	void setCcState(final String ccState);

	String getCcZip();

	void setCcZip(final String ccZip);

	String getBankAccountNo();

	void setBankAccountNo(final String bankAccountNo);

	String getBankName();

	void setBankName(final String bankName);

	String getBankRoutingNo();

	void setBankRoutingNo(final String bankRoutingNo);

	String getCcCvv2();

	void setCcCvv2(final String ccCvv2);

	int getCcExpMonth();

	void setCcExpMonth(final int ccExpMonth);

	int getCcExpYear();

	void setCcExpYear(final int ccExpYear);

	String getCcName();

	void setCcName(final String ccName);

	String getCcNum();

	void setCcNum(final String ccNum);

	CreditCardType getCcType();

	void setCcType(final CreditCardType ccType);
}