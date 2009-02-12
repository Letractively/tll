/**
 * The Logic Lab
 * @author jpk Feb 12, 2009
 */
package com.tll.common.model.mock;

import com.tll.INameValueProvider;
import com.tll.common.model.IEntityType;
import com.tll.util.StringUtil;

/**
 * TestEntityType
 * @author jpk
 */
public enum TestEntityType implements IEntityType, INameValueProvider<String> {

	ACCOUNT("Account"),
	ACCOUNT_ADDRESS("Account Address"),
	ADDRESS("Address"),
	PAYMENT_INFO("Payment Info"),
	CURRENCY("Currency");

	private static final String MODEL_PACKAGE_NAME = TestEntityType.class.getPackage().getName();

	private String name;

	private TestEntityType(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public String getValue() {
		return name();
	}

	public String getEntityClassName() {
		return MODEL_PACKAGE_NAME + StringUtil.enumStyleToJavaClassNotation(name());
	}

	public String getPresentationName() {
		return getName();
	}
}
