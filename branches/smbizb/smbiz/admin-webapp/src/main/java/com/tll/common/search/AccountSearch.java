/**
 * The Logic Lab
 * @author jpk Aug 29, 2007
 */
package com.tll.common.search;

import com.tll.common.model.ModelKey;
import com.tll.common.model.SmbizEntityType;
import com.tll.criteria.CriteriaType;

/**
 * AccountSearch
 * @author jpk
 */
public final class AccountSearch extends NamedTimeStampEntitySearch {

	private ModelKey parentAccountRef;
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
	public AccountSearch(CriteriaType criteriaType, SmbizEntityType accountType) {
		super(criteriaType, accountType);
	}

	/**
	 * Constructor
	 * @param businessKeyName
	 */
	public AccountSearch(String businessKeyName) {
		super(businessKeyName, SmbizEntityType.ACCOUNT);
	}

	public final ModelKey getParentAccountRef() {
		return parentAccountRef;
	}

	public final void setParentAccountRef(ModelKey parentAccountRef) {
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
