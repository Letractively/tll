/**
 * The Logic Lab
 * @author jpk Aug 29, 2007
 */
package com.tll.client.search.impl;

import com.tll.client.model.IEntityType;
import com.tll.client.search.NamedTimeStampEntitySearch;

/**
 * InterfaceSearch
 * @author jpk
 */
public class InterfaceSearch extends NamedTimeStampEntitySearch {

	/**
	 * Constructor
	 */
	public InterfaceSearch() {
		super();
	}

	/**
	 * Constructor
	 * @param searchType
	 */
	public InterfaceSearch(int searchType) {
		super(searchType, IEntityType.INTERFACE);
	}

	@Override
	public String descriptor() {
		return "Interface Search";
	}

	@Override
	public void clear() {
		super.clear();
	}

	@Override
	public boolean equals(Object obj) {
		if(!super.equals(obj) || obj instanceof InterfaceSearch == false) return false;
		return true;
	}

	@Override
	public int hashCode() {
		int hash = super.hashCode();
		return hash;
	}

}
