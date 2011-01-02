/**
 * The Logic Lab
 * @author jpk
 * Feb 12, 2009
 */
package com.tll.model.test;

import com.tll.IMarshalable;
import com.tll.INameValueProvider;


/**
 * AccountStatus
 * @author jpk
 */
public enum AccountStatus implements INameValueProvider<String>, IMarshalable {
	NEW("New"),
	OPEN("Open"),
	PROBATION("Probation"),
	CLOSED("Closed"),
	DELETED("Deleted");

	private final String name;

	private AccountStatus(final String name) {
		this.name = name;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public String getValue() {
		return toString();
	}

}
