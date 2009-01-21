/**
 * The Logic Lab
 * @author jpk
 * Apr 30, 2008
 */
package com.tll.criteria;

import com.tll.model.EntityType;
import com.tll.model.EntityUtil;

/**
 * SelectNamedQueries - Enumeration of the system defined data retrieval based
 * named queries.
 * @author jpk
 */
public enum SelectNamedQueries {
	ISP_LISTING("account.ispList", EntityType.ISP, true),
	MERCHANT_LISTING("account.merchantList", EntityType.MERCHANT, true),
	CUSTOMER_LISTING("account.customerList", EntityType.CUSTOMER, true),
	INTERFACE_SUMMARY_LISTING("interface.summaryList", EntityType.INTERFACE, true),
	INTERFACES("interface.select", EntityType.INTERFACE, false);

	private final String queryName;
	private final EntityType entityType;
	private final boolean scalar;

	private SelectNamedQueries(String queryName, EntityType entityType, boolean scalar) {
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

	public EntityType getEntityType() {
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
	 * Converts to a {@link SelectNamedQuery} data object.
	 * @return A new {@link SelectNamedQuery}
	 */
	public SelectNamedQuery toSelectNamedQuery() {
		return new SelectNamedQuery(getQueryName(), EntityUtil.entityClassFromType(getEntityType()), isScalar());
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