/**
 * The Logic Lab
 * @author jpk Feb 12, 2009
 */
package com.tll.common.model.mock;

import com.tll.IMarshalable;
import com.tll.INameValueProvider;
import com.tll.common.model.IEntityType;
import com.tll.util.StringUtil;

/**
 * MockEntityType
 * @author jpk
 */
public enum MockEntityType implements IEntityType, INameValueProvider<String>, IMarshalable {

	ACCOUNT("Account"),
	ACCOUNT_ADDRESS("Account Address"),
	ADDRESS("Address"),
	PAYMENT_INFO("Payment Info"),
	CURRENCY("Currency");

	//private static final String MODEL_PACKAGE_NAME = MockEntityType.class.getPackage().getName();
	private static final String MODEL_PACKAGE_NAME = "com.tll.model.";

	private String name;

	private MockEntityType(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public String getValue() {
		return name();
	}

	public String getEntityClassName() {
		return MODEL_PACKAGE_NAME + StringUtil.enumStyleToCamelCase(name(), true);
	}

	public String getPresentationName() {
		return getName();
	}
}
