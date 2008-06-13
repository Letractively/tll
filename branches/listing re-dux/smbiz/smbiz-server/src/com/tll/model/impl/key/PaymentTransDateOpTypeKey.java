package com.tll.model.impl.key;

import java.util.Date;

import com.tll.model.impl.PaymentOp;
import com.tll.model.impl.PaymentTrans;
import com.tll.model.impl.PaymentType;
import com.tll.model.key.BusinessKey;

public final class PaymentTransDateOpTypeKey extends BusinessKey<PaymentTrans> {

	private static final long serialVersionUID = 8100980260820822730L;

	private static final String[] FIELDS = new String[] { "payTransDate", "payOp", "payType" };

	public PaymentTransDateOpTypeKey() {
		super();
	}

	public PaymentTransDateOpTypeKey(Date payTransDate, PaymentOp payOp, PaymentType payType) {
		this();
		setPayTransDate(payTransDate);
		setPaymentOp(payOp);
		setPaymentType(payType);
	}

	public Class<PaymentTrans> getType() {
		return PaymentTrans.class;
	}

	@Override
	protected String[] getFields() {
		return FIELDS;
	}

	public void setEntity(PaymentTrans e) {
		e.setPayTransDate(getPayTransDate());
	}

	@Override
	protected String keyDescriptor() {
		return "Payment Trans Date, Payment Op and Payment Type";
	}

	public Date getPayTransDate() {
		return (Date) getValue(0);
	}

	public void setPayTransDate(Date payTransDate) {
		setValue(0, payTransDate);
	}

	public PaymentOp getPaymentOp() {
		return (PaymentOp) getValue(1);
	}

	public void setPaymentOp(PaymentOp paymentOp) {
		setValue(1, paymentOp);
	}

	public PaymentType getPaymentType() {
		return (PaymentType) getValue(2);
	}

	public void setPaymentType(PaymentType paymentType) {
		setValue(2, paymentType);
	}

}