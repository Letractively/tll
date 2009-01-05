package com.tll.model.impl;

import com.tll.INameValueProvider;

/**
 * Payment Op
 * @author jpk
 */
public enum PaymentOp implements INameValueProvider<String> {
	A("Authorize"),
	D("Delay Capture"),
	S("Sale"),
	V("Void"),
	C("Credit");

	private final String name;

	private PaymentOp(final String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public String getValue() {
		return name();
	}

}
