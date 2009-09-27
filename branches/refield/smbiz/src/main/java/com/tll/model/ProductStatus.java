package com.tll.model;

import com.tll.IMarshalable;
import com.tll.INameValueProvider;

/**
 * Product Status
 * @author jpk
 */
public enum ProductStatus implements INameValueProvider<String>, IMarshalable {
	A("Active"),
	I("Inactive");

	private final String name;

	private ProductStatus(final String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public String getValue() {
		return name();
	}

}
