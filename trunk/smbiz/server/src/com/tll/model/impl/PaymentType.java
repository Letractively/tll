package com.tll.model.impl;

import com.tll.util.INameValueProvider;

/**
 * Payment Type
 * @author jpk
 */
public enum PaymentType implements INameValueProvider {
	M("Manual"),
	CC("Credit Card"),
	BANK("Bank"),
	PAYPAL("PayPal");

	private final String name;

	private PaymentType(final String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public Object getValue() {
		return name();
	}

}
