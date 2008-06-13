package com.tll.model.impl.key;

import com.tll.model.impl.PaymentTrans;
import com.tll.model.key.BusinessKey;

public final class PaymentTransRefNumKey extends BusinessKey<PaymentTrans> {

	private static final long serialVersionUID = 8100980260820822730L;

	private static final String[] FIELDS = new String[] { "refNum" };

	public PaymentTransRefNumKey() {
		super();
	}

	public PaymentTransRefNumKey(String refNum) {
		this();
		setRefNum(refNum);
	}

	public Class<PaymentTrans> getType() {
		return PaymentTrans.class;
	}

	@Override
	protected String[] getFields() {
		return FIELDS;
	}

	public void setEntity(PaymentTrans e) {
		e.setRefNum(getRefNum());
	}

	@Override
	protected String keyDescriptor() {
		return "RefNum";
	}

	public String getRefNum() {
		return (String) getValue(0);
	}

	public void setRefNum(String refNum) {
		setValue(0, refNum);
	}

}