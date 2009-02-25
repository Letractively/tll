/**
 * The Logic Lab
 * @author jpk
 * Feb 12, 2009
 */
package com.tll.common.model.mock;

import com.tll.INameValueProvider;


/**
 * AccountStatus
 * @author jpk
 */
public enum AccountStatus implements INameValueProvider<String> {
	NEW("New"),
	OPEN("Open"),
	PROBATION("Probation"),
	CLOSED("Closed"),
	DELETED("Deleted");

	private final String name;

	private AccountStatus(final String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public String getValue() {
		return toString();
	}

}
