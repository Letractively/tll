/**
 * The Logic Lab
 * @author jpk
 * @since May 15, 2009
 */
package com.tll.common.model.test;

import com.tll.INameValueProvider;

/**
 * TestEntityType - Corres. to the entities held in the test-persistence-unit
 * project.
 * @author jpk
 */
public enum TestEntityType implements INameValueProvider<String> {
	ACCOUNT("Account"),
	ACCOUNT_ADDRESS("Account Address"),
	ADDRESS("Address"),
	CURRENCY("Currency"),
	PAYMENT_INFO("Payment Info"),
	NESTED_ENTITY("Nested Entity");

	private String name;

	private TestEntityType(String name) {
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

	public String descriptor() {
		return getName();
	}
}