/**
 * The Logic Lab
 * @author jpk
 * @since Mar 17, 2009
 */
package com.tll.criteria.test;

import com.tll.criteria.ISelectNamedQueryDef;
import com.tll.model.test.Account;
import com.tll.model.test.Address;

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

	@Override
	public String getQueryName() {
		return queryName;
	}

	@Override
	public Class<?> getEntityType() {
		return entityType;
	}

	@Override
	public boolean isScalar() {
		return scalar;
	}

	@Override
	public boolean isSupportsPaging() {
		return supportsPaging;
	}

	@Override
	public String toString() {
		return queryName;
	}
}