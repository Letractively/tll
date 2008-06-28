package com.tll.model.impl;

import com.tll.util.INameValueProvider;

/**
 * Order Status
 * @author jpk
 */
public enum OrderStatus implements INameValueProvider {
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

	public Object getValue() {
		return name();
	}

}