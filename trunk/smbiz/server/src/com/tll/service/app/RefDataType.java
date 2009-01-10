package com.tll.service.app;

import com.tll.IMarshalable;

/**
 * RefDataType - Encapsulates all types of ref data usable in the app.
 * @author jpk
 */
public enum RefDataType implements IMarshalable {
	US_STATES("usps-state-abbrs"),
	ISO_COUNTRY_CODES("iso-country-codes");

	private final String name;

	/**
	 * Constructor
	 * @param name
	 */
	private RefDataType(String name) {
		this.name = name;
	}

	/**
	 * @return the ref data name
	 */
	public String getName() {
		return name;
	}

}
