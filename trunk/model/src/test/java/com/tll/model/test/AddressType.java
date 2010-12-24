/**
 * The Logic Lab
 * @author jpk
 * Feb 12, 2009
 */
package com.tll.model.test;

import com.tll.IMarshalable;
import com.tll.INameValueProvider;


/**
 * AddressType
 * @author jpk
 */
public enum AddressType implements INameValueProvider<String>, IMarshalable {
	HOME("Home"),
	WORK("Work"),
	CONTACT("Contact");

	private final String name;

	private AddressType(String name) {
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
