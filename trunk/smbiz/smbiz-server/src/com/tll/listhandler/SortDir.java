package com.tll.listhandler;

import com.tll.util.INameValueProvider;

/**
 * Sort direction enumeration.
 * @author jpk
 */
public enum SortDir implements INameValueProvider {
	ASC("Ascending"),
	DESC("Descending");

	private final String name;

	SortDir(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public Object getValue() {
		return toString();
	}

	@Override
	public String toString() {
		return name().toLowerCase();
	}
}
