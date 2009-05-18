package com.tll.model;

import com.tll.IMarshalable;
import com.tll.INameValueProvider;

/**
 * FieldEnum
 * @author jpk
 */
public enum FieldEnum implements INameValueProvider<String>, IMarshalable {
	NEW("New"),
	OPEN("Open"),
	PROBATION("Probation"),
	CLOSED("Closed"),
	DELETED("Deleted");

	private final String name;

	private FieldEnum(final String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public String getValue() {
		return toString();
	}

}
