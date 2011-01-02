package com.tll.model;

import com.tll.IMarshalable;
import com.tll.INameValueProvider;

/**
 * Account source enumeration.
 * @author jpk
 */
public enum AccountSource implements INameValueProvider<String>, IMarshalable {
	ADMIN("Admin"),
	STOREFRONT("Storefront");

	private final String name;

	private AccountSource(final String name) {
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
