/**
 * The Logic Lab
 * @author jpk
 * Feb 12, 2009
 */
package com.tll.model.test;

import com.tll.IMarshalable;
import com.tll.INameValueProvider;


/**
 * CreditCardType
 * @author jpk
 */
public enum CreditCardType implements INameValueProvider<String>, IMarshalable {
	VISA("Visa"),
	MC("Master Card");

	private final String name;

	private CreditCardType(final String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public String getValue() {
		return name();
	}

}
