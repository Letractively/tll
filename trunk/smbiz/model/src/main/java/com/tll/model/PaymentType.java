package com.tll.model;

import com.tll.IMarshalable;
import com.tll.INameValueProvider;

/**
 * Payment Type
 * @author jpk
 */
public enum PaymentType implements INameValueProvider<String>, IMarshalable {
	M("Manual"),
	CC("Credit Card"),
	BANK("Bank"),
	PAYPAL("PayPal");

	private final String name;

	private PaymentType(final String name) {
		this.name = name;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public String getValue() {
		return name();
	}

}
