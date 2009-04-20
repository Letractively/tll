package com.tll.model;

import com.tll.INameValueProvider;

/**
 * Order Status
 * @author jpk
 */
public enum OrderStatus implements INameValueProvider<String> {
	N("none"),
	I("Incomplete"),
	C("Completed"),
	D("Deleted");

	private final String name;

	private OrderStatus(final String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public String getValue() {
		return name();
	}

}