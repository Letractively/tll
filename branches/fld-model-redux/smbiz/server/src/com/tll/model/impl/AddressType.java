package com.tll.model.impl;

import com.tll.util.INameValueProvider;

/**
 * The address type.
 * @author jpk
 */
public enum AddressType implements INameValueProvider<String> {
	HOME("Home"),
	WORK("Work"),
	CONTACT("Contact");

	private final String name;

	private AddressType(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public String getValue() {
		return name();
	}

}
