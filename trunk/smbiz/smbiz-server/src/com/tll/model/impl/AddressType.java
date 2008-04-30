package com.tll.model.impl;

import com.tll.util.INameValueProvider;
import com.tll.util.StringUtil;

/**
 * The address type.
 * @author jpk
 */
public enum AddressType implements INameValueProvider {
	HOME,
	WORK,
	CONTACT;

	public String getName() {
		return StringUtil.formatEnumValue(name());
	}

	public Object getValue() {
		return name();
	}

}
