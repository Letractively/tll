/**
 * The Logic Lab
 * @author jpk
 * @since May 15, 2009
 */
package com.tll.common.model.mock;

import com.tll.INameValueProvider;
import com.tll.common.model.IEntityType;


/**
 * TestEntityType - Corres. to the entities held in the test-persistence-unit
 * project.
 * @author jpk
 */
public enum TestEntityType implements IEntityType, INameValueProvider<String> {
	ACCOUNT("Account"),
	ACCOUNT_ADDRESS("Account Address"),
	ADDRESS("Address"),
	CURRENCY("Currency"),
	NESTED_ENTITY("Nested Entity");

	private String name;

	private TestEntityType(String name) {
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
