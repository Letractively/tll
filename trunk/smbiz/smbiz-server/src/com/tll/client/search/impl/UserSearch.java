/**
 * The Logic Lab
 * @author jpk Aug 29, 2007
 */
package com.tll.client.search.impl;

import com.tll.client.search.NamedTimeStampEntitySearch;
import com.tll.criteria.CriteriaType;
import com.tll.model.EntityType;

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
	 * @param criteriaType
	 */
	public UserSearch(CriteriaType criteriaType) {
		super(criteriaType, EntityType.USER);
	}

	@Override
	public String descriptor() {
		return "User Search";
	}

	@Override
	public void clear() {
		super.clear();

	}
}
