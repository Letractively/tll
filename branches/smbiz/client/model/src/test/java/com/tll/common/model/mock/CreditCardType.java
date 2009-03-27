/**
 * The Logic Lab
 * @author jpk
 * Feb 12, 2009
 */
package com.tll.common.model.mock;

import com.tll.INameValueProvider;


/**
 * CreditCardType
 * @author jpk
 */
public enum CreditCardType implements INameValueProvider<String> {
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
