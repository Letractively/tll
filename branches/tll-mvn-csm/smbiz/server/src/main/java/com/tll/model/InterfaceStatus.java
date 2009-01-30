package com.tll.model;

import com.tll.INameValueProvider;

/**
 * Interface (entity) status
 * @author jpk
 */
public enum InterfaceStatus implements INameValueProvider<String> {
	ON("On"),
	OFF("Off");

	private final String name;

	private InterfaceStatus(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public String getValue() {
		return name();
	}

}