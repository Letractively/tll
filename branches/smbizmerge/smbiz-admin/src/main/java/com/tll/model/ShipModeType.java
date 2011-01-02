package com.tll.model;

import com.tll.IMarshalable;
import com.tll.INameValueProvider;

/**
 * Ship Mode Type
 * @author jpk
 */
public enum ShipModeType implements INameValueProvider<String>, IMarshalable {
	USPS("United States Postal Service"),
	UPS("UPS"),
	FEDEX("Federal Express");

	private final String name;

	private ShipModeType(final String name) {
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
