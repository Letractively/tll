package com.tll.model.impl;

import com.tll.INameValueProvider;

/**
 * Payment Type
 * @author jpk
 */
public enum PaymentType implements INameValueProvider<String> {
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

	public String getValue() {
		return name();
	}

}
