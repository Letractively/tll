/**
 * The Logic Lab
 * @author jpk Aug 29, 2007
 */
package com.tll.common.search.impl;

import com.tll.common.search.EntitySearch;
import com.tll.criteria.CriteriaType;
import com.tll.model.EntityType;

/**
 * UserSearch
 * @author jpk
 */
public class AddressSearch extends EntitySearch {

	private String address1, postalCode;

	/**
	 * Constructor
	 */
	public AddressSearch() {
		super();
	}

	/**
	 * Constructor
	 * @param criteriaType
	 */
	public AddressSearch(CriteriaType criteriaType) {
		super(criteriaType, EntityType.ADDRESS);
	}

	/**
	 * Constructor
	 * @param businessKeyName
	 */
	public AddressSearch(String businessKeyName) {
		super(businessKeyName, EntityType.ADDRESS);
	}

	@Override
	public void clear() {
		super.clear();

	}

	public String getAddress1() {
		return address1;
	}

	public void setAddress1(String address1) {
		this.address1 = address1;
	}

	public String getPostalCode() {
		return postalCode;
	}

	public void setPostalCode(String postalCode) {
		this.postalCode = postalCode;
	}

}
