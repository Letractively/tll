/**
 * The Logic Lab
 * @author jpk Aug 29, 2007
 */
package com.tll.common.search.impl;

import com.tll.common.model.RefKey;
import com.tll.common.search.NamedTimeStampEntitySearch;
import com.tll.criteria.CriteriaType;
import com.tll.model.EntityType;

/**
 * AccountSearch
 * @author jpk
 */
public final class AccountSearch extends NamedTimeStampEntitySearch {

	private RefKey parentAccountRef;
	private String status;

	/**
	 * Constructor
	 */
	public AccountSearch() {
		super();
	}

	/**
	 * Constructor
	 * @param criteriaType
	 * @param accountType The entity type corres. to a concrete account type.
	 */
	public AccountSearch(CriteriaType criteriaType, EntityType accountType) {
		super(criteriaType, accountType);
	}

	/**
	 * Constructor
	 * @param businessKeyName
	 */
	public AccountSearch(String businessKeyName) {
		super(businessKeyName, EntityType.ACCOUNT);
	}

	public final RefKey getParentAccountRef() {
		return parentAccountRef;
	}

	public final void setParentAccountRef(RefKey parentAccountRef) {
		this.parentAccountRef = parentAccountRef;
	}

	public final String getStatus() {
		return status;
	}

	public final void setStatus(String status) {
		this.status = status;
	}

	@Override
	public void clear() {
		super.clear();

		if(parentAccountRef != null) parentAccountRef.clear();
		status = null;

	}

	@Override
	public String toString() {
		String s = super.toString();
		s += (" parent:" + (parentAccountRef == null ? "null" : "[" + parentAccountRef.toString() + "]"));
		s += " status:" + status;
		return s;
	}
}
