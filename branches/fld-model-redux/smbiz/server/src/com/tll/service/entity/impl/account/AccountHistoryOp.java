package com.tll.service.entity.impl.account;

import com.tll.util.INameValueProvider;

/**
 * AccountHistoryOp
 * @author jpk
 */
public enum AccountHistoryOp implements INameValueProvider<String> {
	ACCOUNT_ADDED("Account added"),
	ACCOUNT_UPDATED("Account update"),
	ACCOUNT_DELETED("Account deleted"),
	ACCOUNT_PURGED("Account purget"),
	CUSTOMER_ACCOUNT_ADDED("Customer account added"),
	CUSTOMER_ACCOUNT_DELETED("Customer account deleted"),
	CUSTOMER_ACCOUNT_PURGED("Customer account purged");

	private final String name;

	private AccountHistoryOp(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public String getValue() {
		return name();
	}

}
