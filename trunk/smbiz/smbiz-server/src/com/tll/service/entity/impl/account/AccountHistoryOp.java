package com.tll.service.entity.impl.account;

import com.tll.util.INameValueProvider;
import com.tll.util.StringUtil;

/**
 * AccountHistoryOp
 * @author jpk
 */
public enum AccountHistoryOp implements INameValueProvider {
	ACCOUNT_ADDED,
	ACCOUNT_UPDATED,
	ACCOUNT_DELETED,
	ACCOUNT_PURGED,
	CUSTOMER_ACCOUNT_ADDED,
	CUSTOMER_ACCOUNT_DELETED,
	CUSTOMER_ACCOUNT_PURGED;

	AccountHistoryOp() {
	}

	public String getName() {
		return StringUtil.formatEnumValue(name());
	}

	public Object getValue() {
		return name();
	}

}
