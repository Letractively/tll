package com.tll.model;

import com.tll.IMarshalable;
import com.tll.INameValueProvider;

/**
 * Payment Op
 * @author jpk
 */
public enum PaymentOp implements INameValueProvider<String>, IMarshalable {
	A("Authorize"),
	D("Delay Capture"),
	S("Sale"),
	V("Void"),
	C("Credit");

	private final String name;

	private PaymentOp(final String name) {
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
