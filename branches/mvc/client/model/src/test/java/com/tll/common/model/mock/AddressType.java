/**
 * The Logic Lab
 * @author jpk
 * Feb 12, 2009
 */
package com.tll.common.model.mock;

import com.tll.INameValueProvider;


/**
 * AddressType
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
