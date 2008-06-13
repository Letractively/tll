package com.tll.model.impl;

import com.tll.util.INameValueProvider;

/**
 * Order Item Trans Op
 * @author jpk
 */
public enum OrderItemTransOp implements INameValueProvider {
	A("Add Item"),
	U("Update Item Quantity"),
	C("Commit Item"),
	D("Uncommit Item"),
	M("Remove Item"),
	S("Ship Item"),
	R("Return Item");

	private final String name;

	private OrderItemTransOp(final String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public Object getValue() {
		return name();
	}

}