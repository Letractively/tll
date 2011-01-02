package com.tll.model;

import com.tll.IMarshalable;
import com.tll.INameValueProvider;

/**
 * Order Status
 * @author jpk
 */
public enum OrderStatus implements INameValueProvider<String>, IMarshalable {
	N("none"),
	I("Incomplete"),
	C("Completed"),
	D("Deleted");

	private final String name;

	private OrderStatus(final String name) {
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