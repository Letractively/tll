package com.tll.common.search.test;

import com.tll.common.model.IEntityType;
import com.tll.common.model.test.TestEntityType;
import com.tll.common.search.IListingSearch;
import com.tll.criteria.CriteriaType;

/**
 * TestAddressSearch
 * @author jpk
 */
public final class TestAddressSearch implements IListingSearch {

	@Override
	public void clear() {
		// no-op
	}

	@Override
	public CriteriaType getCriteriaType() {
		return CriteriaType.ENTITY;
	}

	@Override
	public boolean isSet() {
		return true;
	}

	@Override
	public IEntityType getEntityType() {
		return TestEntityType.ADDRESS;
	}
}