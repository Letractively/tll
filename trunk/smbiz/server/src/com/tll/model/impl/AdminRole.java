package com.tll.model.impl;

import com.tll.util.INameValueProvider;

/**
 * The Admin role
 * @author jpk
 */
public enum AdminRole implements INameValueProvider {
	ROLE_ASP("The Asp role - all powerful."),
	ROLE_ISP("The Isp role"),
	ROLE_MERCHANT("The Merchant role"),
	ROLE_CUSTOMER("The Customer role"),
	ROLE_SHOPPER("The Visitor role");

	private final String name;

	private AdminRole(final String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public Object getValue() {
		return name();
	}

}
