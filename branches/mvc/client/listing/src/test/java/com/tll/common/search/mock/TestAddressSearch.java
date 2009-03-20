package com.tll.common.search.mock;

import com.tll.common.model.mock.MockEntityType;
import com.tll.common.search.EntitySearch;
import com.tll.criteria.CriteriaType;

/**
 * TestAddressSearch
 * @author jpk
 */
public final class TestAddressSearch extends EntitySearch {

	private String address1, postalCode;

	/**
	 * Constructor
	 */
	public TestAddressSearch() {
		super(CriteriaType.ENTITY, MockEntityType.ADDRESS);
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