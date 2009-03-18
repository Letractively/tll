/**
 * The Logic Lab
 * @author jpk
 * @since Mar 17, 2009
 */
package com.tll.criteria;

import com.tll.model.Account;
import com.tll.model.Address;

public enum TestSelectNamedQueries implements ISelectNamedQueryDef {
	ACCOUNT_LISTING("account.testScalarQuery", Account.class, true, true),
	ADDRESS_LISTING("address.testScalarQuery", Address.class, true, true);

	private final String queryName;
	private final Class<?> entityType;
	private final boolean scalar;
	private final boolean supportsPaging;

	private TestSelectNamedQueries(String queryName, Class<?> entityType, boolean scalar, boolean supportsPaging) {
		this.queryName = queryName;
		this.entityType = entityType;
		this.scalar = scalar;
		this.supportsPaging = supportsPaging;
	}

	public String getQueryName() {
		return queryName;
	}

	public Class<?> getEntityType() {
		return entityType;
	}

	public boolean isScalar() {
		return scalar;
	}

	public boolean isSupportsPaging() {
		return supportsPaging;
	}

	@Override
	public String toString() {
		return queryName;
	}
}