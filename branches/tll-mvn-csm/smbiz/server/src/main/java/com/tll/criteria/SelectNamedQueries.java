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
	ISP_LISTING("account.ispList", Isp.class, true),
	MERCHANT_LISTING("account.merchantList", Merchant.class, true),
	CUSTOMER_LISTING("account.customerList", Customer.class, true),
	INTERFACE_SUMMARY_LISTING("interface.summaryList", Interface.class, true),
	INTERFACES("interface.select", Interface.class, false);

	private final String queryName;
	private final Class<?> entityType;
	private final boolean scalar;

	private SelectNamedQueries(String queryName, Class<?> entityType, boolean scalar) {
		this.queryName = queryName;
		this.entityType = entityType;
		this.scalar = scalar;
	}

	public String getQueryName() {
		return queryName;
	}

	/**
	 * @return The counterpart named query that retrieves the count of this named
	 *         query.
	 */
	public String getCountCounterpartQueryName() {
		return queryName + ".count";
	}

	public Class<?> getEntityType() {
		return entityType;
	}

	public boolean isScalar() {
		return scalar;
	}

	@Override
	public String toString() {
		return queryName;
	}

	/**
	 * Finds the {@link SelectNamedQueries} given the query name.
	 * @param queryName The query name
	 * @return The associated query definition
	 * @throws IllegalArgumentException When no matching
	 *         {@link SelectNamedQueries} is found.
	 */
	public static SelectNamedQueries fromQueryName(String queryName) {
		for(SelectNamedQueries nq : SelectNamedQueries.values()) {
			if(nq.getQueryName().equals(queryName)) return nq;
		}
		throw new IllegalArgumentException("Undefined named query: " + queryName);
	}
}