/**
 * The Logic Lab
 * @author jpk Aug 29, 2007
 */
package com.tll.client.search.impl;

import com.tll.client.search.NamedTimeStampEntitySearch;
import com.tll.criteria.CriteriaType;
import com.tll.model.EntityType;

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
	 * @param criteriaType
	 */
	public InterfaceSearch(CriteriaType criteriaType) {
		super(criteriaType, EntityType.INTERFACE);
	}

	@Override
	public String descriptor() {
		return "Interface Search";
	}

	@Override
	public void clear() {
		super.clear();
	}
}
