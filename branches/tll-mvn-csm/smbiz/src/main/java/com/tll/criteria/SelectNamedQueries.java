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
 * SelectNamedQueries - Enumeration of the system defined data retrieval based
 * named queries.
 * @author jpk
 */
public enum SelectNamedQueries implements ISelectNamedQueryDef {
	ISP_LISTING("account.ispList", Isp.class, true, true),
	MERCHANT_LISTING("account.merchantList", Merchant.class, true, true),
	CUSTOMER_LISTING("account.customerList", Customer.class, true, true),
	INTERFACE_SUMMARY_LISTING("interface.summaryList", Interface.class, true, false),
	INTERFACES("interface.select", Interface.class, false, false);

	private final String baseQueryName;
	private final Class<?> entityType;
	private final boolean scalar;
	private final boolean supportsPaging;

	private SelectNamedQueries(String baseQueryName, Class<?> entityType, boolean scalar, boolean supportsPaging) {
		this.baseQueryName = baseQueryName;
		this.entityType = entityType;
		this.scalar = scalar;
		this.supportsPaging = supportsPaging;
	}

	public String getBaseQueryName() {
		return baseQueryName;
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
		return baseQueryName;
	}
}