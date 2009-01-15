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

	/**
	 * Constructor
	 * @param businessKeyName
	 */
	public UserSearch(String businessKeyName) {
		super(businessKeyName, EntityType.USER);
	}

	@Override
	public void clear() {
		super.clear();

	}
}
