package com.tll.model.impl;

import com.tll.util.INameValueProvider;

/**
 * Ship Mode Type
 * @author jpk
 */
public enum ShipModeType implements INameValueProvider<String> {
	USPS("United States Postal Service"),
	UPS("UPS"),
	FEDEX("Federal Express");

	private final String name;

	private ShipModeType(final String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public String getValue() {
		return name();
	}

}
