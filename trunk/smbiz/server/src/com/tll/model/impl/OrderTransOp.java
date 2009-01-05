package com.tll.model.impl;

import com.tll.INameValueProvider;

/**
 * Order Item Trans Op
 * @author jpk
 */
public enum OrderTransOp implements INameValueProvider<String> {
	CO("Create Order"),
	DO("Delete Order"),
	AN("Add Notes"),
	CM("Commit Order"),
	MC("Mark Order Completed"),
	AI("Add Items"),
	UI("Update Item Quantities"),
	MI("Remove Items"),
	SI("Ship Items"),
	RI("Return Items"),
	IA("Inventory Availability Check"),
	RT("Re-Calc Order Total"),
	PA("Authorize Items") // Pay
	// Trans
	,
	PS("Sale Items") // Pay Trans
	,
	PD("Delay Capture Items") // Pay Trans
	,
	PV("id Items") // Pay Trans
	,
	PC("Credit Items") // Pay Trans
	,
	BA("Batch Authorize"),
	BD("Batch Delay Capture"),
	BS("Batch Sale"),
	BV("Batch id"),
	BC("Batch Credit");

	private final String name;

	private OrderTransOp(final String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public String getValue() {
		return name();
	}

}
