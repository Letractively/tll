package com.tll.model.impl.key;

import com.tll.model.impl.AppProperty;
import com.tll.model.key.NameKey;

/**
 * AppPropertyNameKey
 * @author jpk
 */
public final class AppPropertyNameKey extends NameKey<AppProperty> {

	private static final long serialVersionUID = -8390527655557327262L;

	/**
	 * Constructor
	 */
	public AppPropertyNameKey() {
		super(AppProperty.class);
	}

	/**
	 * Constructor
	 * @param name
	 */
	public AppPropertyNameKey(String name) {
		this();
		setName(name);
	}

}