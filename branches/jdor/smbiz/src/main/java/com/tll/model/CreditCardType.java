package com.tll.model;

import com.tll.IMarshalable;
import com.tll.INameValueProvider;

/**
 * Credit card types.
 * @author jpk
 */
public enum CreditCardType implements INameValueProvider<String>, IMarshalable {
	VISA("Visa"),
	MC("Master Card");

	private final String name;

	private CreditCardType(final String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public String getValue() {
		return name();
	}

}
