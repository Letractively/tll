package com.tll.model.impl;

import com.tll.util.INameValueProvider;

/**
 * Account source enumeration.
 * @author jpk
 */
public enum AccountSource implements INameValueProvider {
	ADMIN("Admin"),
	STOREFRONT("Storefront");

	private final String name;

	private AccountSource(final String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public Object getValue() {
		return name();
	}
}
