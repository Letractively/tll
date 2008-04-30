package com.tll.model.impl;

import com.tll.util.INameValueProvider;
import com.tll.util.StringUtil;

/**
 * Interface (entity) status
 * @author jpk
 */
public enum InterfaceStatus implements INameValueProvider {
	ON,
	OFF;

	public String getName() {
		return StringUtil.formatEnumValue(name());
	}

	public Object getValue() {
		return name();
	}

}