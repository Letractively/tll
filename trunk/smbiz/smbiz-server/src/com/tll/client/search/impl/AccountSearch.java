/**
 * The Logic Lab
 * @author jpk Aug 29, 2007
 */
package com.tll.client.search.impl;

import com.tll.client.model.RefKey;
import com.tll.client.search.NamedTimeStampEntitySearch;

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
	 * @param searchType
	 * @param accountType The entity type corres. to a concrete account type.
	 */
	public AccountSearch(int searchType, String accountType) {
		super(searchType, accountType);
	}

	@Override
	public String descriptor() {
		return "Account Search";
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
	public boolean equals(Object obj) {
		if(!super.equals(obj) || obj instanceof AccountSearch == false) return false;
		final AccountSearch that = (AccountSearch) obj;
		if(that.parentAccountRef == null) {
			if(parentAccountRef != null) return false;
		}
		else {
			if(parentAccountRef == null || !parentAccountRef.equals(that.parentAccountRef)) return false;

		}
		if(that.status == null) {
			if(status != null) return false;
		}
		else {
			if(status == null || !status.equals(that.status)) return false;

		}
		return true;
	}

	@Override
	public int hashCode() {
		int hash = super.hashCode();
		if(parentAccountRef != null) {
			hash = hash * 21 + parentAccountRef.hashCode();
		}
		if(status != null) {
			hash = hash * 27 + status.hashCode();
		}
		return hash;
	}

	@Override
	public String toString() {
		String s = super.toString();
		s += (" parent:" + (parentAccountRef == null ? "null" : "[" + parentAccountRef.toString() + "]"));
		s += " status:" + status;
		return s;
	}
}
