package com.tll.model.impl;

import com.tll.util.INameValueProvider;

/**
 * Payment Op
 * @author jpk
 */
public enum PaymentOp implements INameValueProvider {
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

	public Object getValue() {
		return name();
	}

}
