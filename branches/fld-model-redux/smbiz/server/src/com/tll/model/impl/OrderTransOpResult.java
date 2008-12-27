package com.tll.model.impl;

import com.tll.util.INameValueProvider;

/**
 * Order Trans Op Result
 * @author jpk
 */
public enum OrderTransOpResult implements INameValueProvider<String> {
	S("Successful"),
	F("Failed");

	private final String name;

	private OrderTransOpResult(final String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public String getValue() {
		return name();
	}

}
