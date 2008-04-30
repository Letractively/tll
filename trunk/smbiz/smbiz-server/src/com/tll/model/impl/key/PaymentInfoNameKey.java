package com.tll.model.impl.key;

import com.tll.model.impl.PaymentInfo;
import com.tll.model.key.NameKey;

public final class PaymentInfoNameKey extends NameKey<PaymentInfo> {

	private static final long serialVersionUID = 8164999206292846374L;

	/**
	 * Constructor
	 */
	public PaymentInfoNameKey() {
		super(PaymentInfo.class);
	}

	/**
	 * Constructor
	 * @param name
	 */
	public PaymentInfoNameKey(String name) {
		this();
		setName(name);
	}

}