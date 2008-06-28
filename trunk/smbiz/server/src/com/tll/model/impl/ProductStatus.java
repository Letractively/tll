package com.tll.model.impl;

import com.tll.util.INameValueProvider;

/**
 * Product Status
 * @author jpk
 */
public enum ProductStatus implements INameValueProvider {
	A("Active"),
	I("Inactive");

	private final String name;

	private ProductStatus(final String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public Object getValue() {
		return name();
	}

}
