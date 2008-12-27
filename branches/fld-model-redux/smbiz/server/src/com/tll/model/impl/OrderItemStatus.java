package com.tll.model.impl;

import com.tll.util.INameValueProvider;

/**
 * Order Item Status
 * @author jpk
 */
public enum OrderItemStatus implements INameValueProvider<String> {
	N("none"),
	O("Ordered"),
	C("Committed"),
	M("Removed"),
	S("Shipped"),
	R("Returned");

	private final String name;

	private OrderItemStatus(final String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public String getValue() {
		return name();
	}

}
