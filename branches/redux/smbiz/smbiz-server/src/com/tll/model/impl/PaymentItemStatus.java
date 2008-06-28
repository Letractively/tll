package com.tll.model.impl;

import com.tll.util.INameValueProvider;

/**
 * Payment Item Status
 * @author jpk
 */
public enum PaymentItemStatus implements INameValueProvider {
	N("None"),
	A("Authorized"),
	S("Sold"),
	C("Credited"),
	V("Voided");

	private final String name;

	private PaymentItemStatus(final String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public Object getValue() {
		return name();
	}

}
