package com.tll.model;

import com.tll.IMarshalable;
import com.tll.INameValueProvider;

/**
 * Order Item Trans Op
 * @author jpk
 */
public enum OrderItemTransOp implements INameValueProvider<String>, IMarshalable {
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

	@Override
	public String getName() {
		return name;
	}

	@Override
	public String getValue() {
		return name();
	}

}
