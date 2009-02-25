package com.tll.model;

import com.tll.INameValueProvider;

/**
 * Payment Item Status
 * @author jpk
 */
public enum PaymentItemStatus implements INameValueProvider<String> {
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

	public String getValue() {
		return name();
	}

}
