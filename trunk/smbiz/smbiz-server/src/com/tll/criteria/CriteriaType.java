/**
 * The Logic Lab
 * @author jpk
 * Mar 6, 2008
 */
package com.tll.criteria;

import javax.persistence.Query;

/**
 * CriteriaType - Defines the supported criteria types.
 * @author jpk
 */
public enum CriteriaType {
	ENTITY(false, false),
	ENTITY_NAMED_QUERY(false, true),
	SCALAR_NAMED_QUERY(true, true);

	private final boolean scalar;
	private final boolean query;

	/**
	 * Constructor
	 * @param scalar
	 * @param query
	 */
	private CriteriaType(boolean scalar, boolean query) {
		this.scalar = scalar;
		this.query = query;
	}

	/**
	 * @return <code>true</code> when this criteria type represents scalar
	 *         results.
	 */
	public boolean isScalar() {
		return scalar;
	}

	/**
	 * @return <code>true</code> when this criteria will translate to a
	 *         {@link Query} instance.
	 */
	public boolean isQuery() {
		return query;
	}

}
