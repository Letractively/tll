package com.tll.model.impl;

import com.tll.util.INameValueProvider;

/**
 * AccountStatus
 * @author jpk
 */
public enum AccountStatus implements INameValueProvider {
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

	public Object getValue() {
		return toString();
	}

}
