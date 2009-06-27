/**
 * The Logic Lab
 * @author jpk Feb 12, 2009
 */
package com.tll.common.model.test;

import com.tll.INameValueProvider;
import com.tll.common.model.IEntityType;

/**
 * MockEntityType
 * @author jpk
 */
public enum MockEntityType implements IEntityType, INameValueProvider<String> {

	ACCOUNT("Account"),
	ACCOUNT_ADDRESS("Account Address"),
	ADDRESS("Address"),
	PAYMENT_INFO("Payment Info"),
	CURRENCY("Currency");

	private String name;

	private MockEntityType(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public String getValue() {
		return name();
	}

	public String descriptor() {
		return getName();
	}
}
