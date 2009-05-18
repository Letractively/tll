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
		super(criteriaType, SmbizEntityType.ADDRESS);
	}

	/**
	 * Constructor
	 * @param businessKeyName
	 */
	public AddressSearch(String businessKeyName) {
		super(businessKeyName, SmbizEntityType.ADDRESS);
	}

	@Override
	public void clear() {
		super.clear();
		address1 = null;
		postalCode = null;
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
