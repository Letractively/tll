/**
 * The Logic Lab
 * @author jpk Aug 29, 2007
 */
package com.tll.client.search.impl;

import com.tll.client.model.IEntityType;
import com.tll.client.search.NamedTimeStampEntitySearch;

/**
 * UserSearch
 * @author jpk
 */
public class UserSearch extends NamedTimeStampEntitySearch {

	/**
	 * Constructor
	 */
	public UserSearch() {
		super();
	}

	/**
	 * Constructor
	 * @param searchType
	 */
	public UserSearch(int searchType) {
		super(searchType, IEntityType.USER);
	}

	@Override
	public String descriptor() {
		return "User Search";
	}

	@Override
	public void clear() {
		super.clear();

	}

	@Override
	public boolean equals(Object obj) {
		if(!super.equals(obj)) {
			return false;
		}
		if(obj instanceof UserSearch == false) return false;
		return true;
	}

	@Override
	public int hashCode() {
		int hash = super.hashCode();
		return hash;
	}

}
