/**
 * The Logic Lab
 * @author jpk Aug 29, 2007
 */
package com.tll.common.search;

import com.tll.common.search.NamedTimeStampEntitySearch;
import com.tll.criteria.CriteriaType;
import com.tll.model.SmbizEntityType;

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
		super(criteriaType, SmbizEntityType.INTERFACE);
	}

	/**
	 * Constructor
	 * @param businessKeyName
	 */
	public InterfaceSearch(String businessKeyName) {
		super(businessKeyName, SmbizEntityType.INTERFACE);
	}

	@Override
	public void clear() {
		super.clear();
	}
}
