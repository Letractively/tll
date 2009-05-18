/**
 * The Logic Lab
 * @author jpk Aug 29, 2007
 */
package com.tll.common.search;

import com.tll.common.model.SmbizEntityType;
import com.tll.criteria.CriteriaType;

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
		super(criteriaType, SmbizEntityType.USER);
	}

	/**
	 * Constructor
	 * @param businessKeyName
	 */
	public UserSearch(String businessKeyName) {
		super(businessKeyName, SmbizEntityType.USER);
	}

	@Override
	public void clear() {
		super.clear();

	}
}
