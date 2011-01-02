package com.tll.model;

import com.tll.IMarshalable;
import com.tll.INameValueProvider;

/**
 * Order Item Status
 * @author jpk
 */
public enum OrderItemStatus implements INameValueProvider<String>, IMarshalable {
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

	@Override
	public String getName() {
		return name;
	}

	@Override
	public String getValue() {
		return name();
	}

}
