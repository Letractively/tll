/**
 * The Logic Lab
 * @author jpk
 * Apr 30, 2008
 */
package com.tll.criteria;

import com.tll.model.Customer;
import com.tll.model.Interface;
import com.tll.model.Isp;
import com.tll.model.Merchant;

/**
 * SelectNamedQueries - Enumeration of the select named queries in orm.xml.
 * @author jpk
 */
public enum SelectNamedQueries implements ISelectNamedQueryDef {
	ISP_LISTING("account.ispList", Isp.class, true, true),
	MERCHANT_LISTING("account.merchantList", Merchant.class, true, true),
	CUSTOMER_LISTING("account.customerList", Customer.class, true, true),
	INTERFACE_SUMMARY_LISTING("interface.summaryList", Interface.class, true, false),
	ACCOUNT_INTERFACE_SUMMARY_LISTING("acntIntf.smry", Interface.class, true, false);

	private final String queryName;
	private final Class<?> entityType;
	private final boolean scalar;
	private final boolean supportsPaging;

	private SelectNamedQueries(String queryName, Class<?> entityType, boolean scalar, boolean supportsPaging) {
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