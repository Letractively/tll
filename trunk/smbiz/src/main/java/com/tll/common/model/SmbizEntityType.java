package com.tll.common.model;

import com.tll.INameValueProvider;

/**
 * SmbizEntityType - Enumeration impl of {@link IEntityType} identifying all relevant
 * entity type in the app.
 * @author jpk
 */
public enum SmbizEntityType implements IEntityType, INameValueProvider<String> {
	ACCOUNT("Account"),
	ASP("Asp"),
	ISP("Isp"),
	MERCHANT("Merchant"),
	CUSTOMER("Customer"),
	VISITOR("Visitor"),
	ACCOUNT_ADDRESS("Account Address"),
	ACCOUNT_HISTORY("Account History"),
	USER("User"),
	ADDRESS("Address"),
	APP_PROPERTY("App Property"),
	AUTHORITY("Authority"),
	CURRENCY("Currency"),
	CUSTOMER_ACCOUNT("Customer Account"),
	INTERFACE("Interface"),
	INTERFACE_MULTI("Multi-Interface"),
	INTERFACE_SINGLE("Single-Interface"),
	INTERFACE_SWITCH("Switch-Interface"),
	INTERFACE_OPTION("Interface Option"),
	INTERFACE_OPTION_ACCOUNT("Interface Option Account"),
	INTERFACE_OPTION_PARAMETER_DEFINITION("Interface Option Paremeter Definition"),
	ORDER("Order"),
	ORDER_ITEM("Order Item"),
	ORDER_TRANS("Order Transaction"),
	ORDER_ITEM_TRANS("Order Item Transaction"),
	PAYMENT_INFO("Payment Info"),
	PAYMENT_TRANS("Payment Transaction"),
	/*PCH("Product Catetgory Hierarchy"),*/
	PROD_CAT("Product Category Binder"),
	PRODUCT_CATEGORY("Product Category"),
	PRODUCT_INVENTORY("Inventory Product"),
	PRODUCT_GENERAL("General Product"),
	SALES_TAX("Sales Tax"),
	SHIP_BOUND_COST("Ship Bound Cost"),
	SHIP_MODE("Ship Mode"),
	SITE_CODE("Site Code");

	private String name;

	private SmbizEntityType(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public String getValue() {
		return name();
	}

	public String descriptor() {
		return getName();
	}

	public boolean isAbstract() {
		return ACCOUNT.equals(this) || INTERFACE.equals(this);
	}

	public boolean isAccountType() {
		return ACCOUNT.equals(this) || ASP.equals(this) || ISP.equals(this) || MERCHANT.equals(this)
		|| CUSTOMER.equals(this);
	}

	public boolean isInterfaceType() {
		return INTERFACE.equals(this) || INTERFACE_MULTI.equals(this) || INTERFACE_SINGLE.equals(this)
		|| INTERFACE_SWITCH.equals(this);
	}

	/**
	 * Mimics the java class hierarchy mechanism so we can check for
	 * <em>compatible</em> types rather than exact equals.
	 * @param type entity type
	 * @return true/false
	 */
	public boolean isSubtype(SmbizEntityType type) {
		switch(this) {
			case ACCOUNT:
				return type.isAccountType();
			case INTERFACE:
				return type.isInterfaceType();
		}
		return false;
	}
}