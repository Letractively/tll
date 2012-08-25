package com.tll.model;

import com.tll.IMarshalable;
import com.tll.INameValueProvider;

/**
 * The address type.
 * @author jpk
 */
public enum AddressType implements INameValueProvider<String>, IMarshalable {
	HOME("Home"),
	WORK("Work"),
	CONTACT("Contact");

	private final String name;

	private AddressType(final String name) {
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
