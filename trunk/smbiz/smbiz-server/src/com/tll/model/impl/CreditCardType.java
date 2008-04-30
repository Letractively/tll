package com.tll.model.impl;

import com.tll.util.INameValueProvider;

/**
 * Credit card types.
 * @author jpk
 */
public enum CreditCardType implements INameValueProvider {
	VISA("Visa"),
	MC("Master Card");

	private final String name;

	private CreditCardType(final String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public Object getValue() {
		return name();
	}

}
