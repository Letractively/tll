package com.tll.model.impl;

import com.tll.util.INameValueProvider;

/**
 * Interface (entity) status
 * @author jpk
 */
public enum InterfaceStatus implements INameValueProvider {
	ON("On"),
	OFF("Off");

	private final String name;

	private InterfaceStatus(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public Object getValue() {
		return name();
	}

}